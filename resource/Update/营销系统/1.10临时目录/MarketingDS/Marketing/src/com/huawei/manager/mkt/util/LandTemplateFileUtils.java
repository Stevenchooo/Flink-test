/*
 * 文 件 名:  LandTemplateFileUtils.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-6-2
 */
package com.huawei.manager.mkt.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.slf4j.Logger;

import com.huawei.manager.mkt.constant.UploadDealState;
import com.huawei.manager.mkt.dao.LandInfoDao;
import com.huawei.manager.mkt.entity.AdInfoEntity;
import com.huawei.manager.mkt.info.AdDicMapInfo;
import com.huawei.manager.mkt.info.AdTemplateInfo;
import com.huawei.manager.mkt.info.LandTemplateInfo;
import com.huawei.manager.mkt.util.checkUtil.LandInfoTemplateCheck;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.LogUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-2]
 * @see  [相关类/方法]
 */
public class LandTemplateFileUtils
{
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    private static final String VMALL_LAND_TEMPLATE = "着陆链接(VMALL)模板";
    
    private static final String HORNOR_LAND_TEMPLATE = "着陆链接(官网)模板";
    
    //    private static final String OTHER_LAND_TEMPLATE = "着陆链接(第三方)模板";
    
    /**
     * <读取着陆链接模板文件>
     * @param fileName     文件名称
     * @param vmallFlag    是否是vmall模板
     * @return             着陆链接列表
     * @see [类、类#方法、类#成员]
     */
    public static List<LandTemplateInfo> readTemplateFile(String fileName, boolean vmallFlag)
    {
        List<LandTemplateInfo> list = new ArrayList<LandTemplateInfo>();
        
        FileInputStream fis = null;
        
        try
        {
            fis = new FileInputStream(fileName);
            //Excel工作薄
            HSSFWorkbook xwb = new HSSFWorkbook(fis);
            //sheet行数
            int num = xwb.getNumberOfSheets();
            
            for (int i = 0; i < num; i++)
            {
                //遍历sheet读取信息
                getLandInfoFromSheet(vmallFlag, list, xwb, i);
            }
        }
        catch (FileNotFoundException e)
        {
            String log = "readTemplateFile error! Exception is {} , file name is {},file adlist is {}";
            LOG.error(log);
        }
        catch (IOException e)
        {
            String log = "readTemplateFile error! Exception is {} , file name is {},file adlist is {}";
            LOG.error(log);
        }
        finally
        {
            IOUtils.closeQuietly(fis);
        }
        
        return list;
    }
    
    /**
     * <从单一sheet中获取着陆链接>
     * @param vmallFlag     是否是vmall着陆链接
     * @param list          返回的里链接信息列表
     * @param xwb           excel文件工作薄
     * @param index         sheet页序号
     * @see [类、类#方法、类#成员]
     */
    private static void getLandInfoFromSheet(boolean vmallFlag, List<LandTemplateInfo> list, HSSFWorkbook xwb, int index)
    {
        
        if (vmallFlag)
        {
            //从sheet页中获取vmall着陆链接信息
            getVmallLandInfoFromSheet(list, xwb, index);
        }
        else
        {
            //从sheet页中获取荣耀着陆链接信息
            getHonorLandInfoFormSheet(list, xwb, index);
        }
    }
    
    /**
     * <从sheet页中获取vmall着陆链接信息>
     * @param list              返回的里链接信息列表
     * @param xwb               excel文件工作薄
     * @param index             sheet页序号
     * @see [类、类#方法、类#成员]
     */
    private static void getVmallLandInfoFromSheet(List<LandTemplateInfo> list, HSSFWorkbook xwb, int index)
    {
        String sheetName = xwb.getSheetName(index);
        
        //判断sheet页名称是否合法
        if (!VMALL_LAND_TEMPLATE.equals(sheetName))
        {
            return;
        }
        getLandInfoFromValidSheet(true, list, xwb, index);
    }
    
    /**
     * <从sheet页中获取荣耀着陆链接信息>
     * @param list              返回的里链接信息列表
     * @param xwb               excel文件工作薄
     * @param index             sheet页序号
     * @see [类、类#方法、类#成员]
     */
    private static void getHonorLandInfoFormSheet(List<LandTemplateInfo> list, HSSFWorkbook xwb, int index)
    {
        String sheetName = xwb.getSheetName(index);
        
        //判断sheet页名称是否合法
        if (!HORNOR_LAND_TEMPLATE.equals(sheetName))
        {
            return;
        }
        
        getLandInfoFromValidSheet(false, list, xwb, index);
        
    }
    
    /**
     * <从sheet页中获取着陆链接信息>
     * @param vmallFlag         是否是vmall标志
     * @param list              返回的里链接信息列表
     * @param xwb               excel文件工作薄
     * @param index             sheet页序号
     * @see [类、类#方法、类#成员]
     */
    private static void getLandInfoFromValidSheet(boolean vmallFlag, List<LandTemplateInfo> list, HSSFWorkbook xwb,
        int index)
    {
        //获取sheet页对象
        HSSFSheet sheet = xwb.getSheetAt(index);
        
        if (null == sheet)
        {
            return;
        }
        
        int rowNum = sheet.getPhysicalNumberOfRows();
        
        //行数小于1，没有记录
        if (rowNum <= 1)
        {
            return;
        }
        
        //荣第二行开始统计
        for (int i = 1; i < rowNum; i++)
        {
            //从行中获取着陆链接信息
            HSSFRow row = sheet.getRow(i);
            getLandInfoFromRow(vmallFlag, list, row);
        }
    }
    
    /**
     * <从行信息中获取着陆链接信息>
     * @param vmallFlag         是否是vmall标志
     * @param list              返回着陆链接列表
     * @param row               行信息
     * @see [类、类#方法、类#成员]
     */
    private static void getLandInfoFromRow(boolean vmallFlag, List<LandTemplateInfo> list, HSSFRow row)
    {
        //row对象不存在，直接返回
        if (null == row)
        {
            return;
        }
        
        //获取着陆链接信息
        LandTemplateInfo landTemplateInfo = getLandTempLate(row, vmallFlag);
        
        list.add(landTemplateInfo);
    }
    
    /**
     * <从行中获取着陆链接信息>
     * @param row               Excel文件行信息
     * @param vmallFlag         是否是vmall着陆链接
     * @return                  着陆链接信息
     * @see [类、类#方法、类#成员]
     */
    private static LandTemplateInfo getLandTempLate(HSSFRow row, boolean vmallFlag)
    {
        if (vmallFlag)
        {
            return getVmallLandInfo(row);
        }
        else
        {
            return getHonorLandInfo(row);
        }
        
    }
    
    /**
     * <获取荣耀着陆链接信息>
     * @param row    excel文件行信息
     * @return       着陆链接信息
     * @see [类、类#方法、类#成员]
     */
    private static LandTemplateInfo getHonorLandInfo(HSSFRow row)
    {
        LandTemplateInfo info = new LandTemplateInfo();
        
        //广告位ID
        String aidStr = ExcelUtils.getCellValue(row.getCell(0));
        info.setAidStr(aidStr);
        
        //着陆链接
        String landUrl = ExcelUtils.getCellValue(row.getCell(1));
        info.setLandUrl(landUrl);
        
        return info;
        
    }
    
    /**
     * <获取vmall着陆链接信息>
     * @param row    excel文件行信息
     * @return       着陆链接信息
     * @see [类、类#方法、类#成员]
     */
    private static LandTemplateInfo getVmallLandInfo(HSSFRow row)
    {
        LandTemplateInfo info = new LandTemplateInfo();
        
        //广告位ID
        String aidStr = ExcelUtils.getCellValue(row.getCell(0));
        info.setAidStr(aidStr);
        
        //sid
        String sid = ExcelUtils.getCellValue(row.getCell(1));
        info.setSid(sid);
        
        //CPS提供商名称
        String cpsName = ExcelUtils.getCellValue(row.getCell(2));
        info.setCpsName(cpsName);
        
        //Source
        String source = ExcelUtils.getCellValue(row.getCell(3));
        info.setSource(source);
        
        //渠道名称
        String channelName = ExcelUtils.getCellValue(row.getCell(4));
        info.setChannelName(channelName);
        
        //channel
        String channel = ExcelUtils.getCellValue(row.getCell(5));
        info.setChannel(channel);
        
        //cid
        String cid = ExcelUtils.getCellValue(row.getCell(6));
        info.setCid(cid);
        
        //着陆链接
        String landUrl = ExcelUtils.getCellValue(row.getCell(7));
        info.setLandUrl(landUrl);
        
        return info;
    }
    
    /**
     * <写excel文件头信息>
     * @param workbook    excel文件工作薄
     * @param vmallFlag   是否是vmall着陆链接
     * @return            excel文件sheet页对象
     * @see [类、类#方法、类#成员]
     */
    public static HSSFSheet wirteFileHeadRow(HSSFWorkbook workbook, boolean vmallFlag)
    {
        HSSFSheet sheet = null;
        if (vmallFlag)
        {
            sheet = writeVmallFileHeadRow(workbook);
        }
        else
        {
            sheet = writeHonorFileHeadRow(workbook);
        }
        
        return sheet;
    }
    
    /**
     * <写荣耀着陆链接文件头信息>
     * @param workbook   excel文件工作薄
     * @return           excel文件sheet页对象
     * @see [类、类#方法、类#成员]
     */
    private static HSSFSheet writeHonorFileHeadRow(HSSFWorkbook workbook)
    {
        CellStyle style = getHeadRowStyle(workbook);
        
        HSSFSheet sheet = workbook.createSheet("荣耀官网着陆链接录入信息导出");
        
        //插入表头信息
        HSSFRow headRow = sheet.createRow(0);
        for (int i = 0; i < ExcelUtils.getHonorTemplateList().size(); i++)
        {
            HSSFCell headCell = headRow.createCell(i);
            headCell.setCellValue(ExcelUtils.getHonorTemplateList().get(i));
            headCell.setCellStyle(style);
        }
        
        HSSFCell headCell = headRow.createCell(ExcelUtils.getHonorTemplateList().size());
        headCell.setCellValue("操作结果");
        headCell.setCellStyle(style);
        return sheet;
    }
    
    /**
     * <写vmall着陆链接文件头信息>
     * @param workbook   excel文件工作薄
     * @return           excel文件sheet页对象
     * @see [类、类#方法、类#成员]
     */
    private static HSSFSheet writeVmallFileHeadRow(HSSFWorkbook workbook)
    {
        CellStyle style = getHeadRowStyle(workbook);
        
        HSSFSheet sheet = workbook.createSheet("VMALL着陆链接录入信息导出");
        //插入表头信息
        HSSFRow headRow = sheet.createRow(0);
        
        for (int i = 0; i < ExcelUtils.getVmallTemplateList().size(); i++)
        {
            HSSFCell headCell = headRow.createCell(i);
            headCell.setCellValue(ExcelUtils.getVmallTemplateList().get(i));
            headCell.setCellStyle(style);
        }
        
        HSSFCell headCell = headRow.createCell(ExcelUtils.getVmallTemplateList().size());
        headCell.setCellValue("操作结果");
        headCell.setCellStyle(style);
        return sheet;
    }
    
    /**
     * <获取excel文件首行单元格格式>
     * @param workbook   excel文件工作薄
     * @return           单元格格式
     * @see [类、类#方法、类#成员]
     */
    private static CellStyle getHeadRowStyle(HSSFWorkbook workbook)
    {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        return style;
    }
    
    /**
     * <检查广告位着陆链接信息>
     * @param dbConn          数据库链接
     * @param info          着陆链接信息
     * @param buffer        校验信息
     * @param vmallFlag     是否是vmall着陆链接
     * @param account       用户账号
     * @param dicMap        字段信息
     * @param userDeptType  账户类型
     * @param adminFlag     是否是管理员
     * @return              上传处理结果
     * @see [类、类#方法、类#成员]
     */
    public static UploadDealState checkAdLandInfo(DBConnection dbConn, AdTemplateInfo info, StringBuffer buffer,
        int vmallFlag, String account, AdDicMapInfo dicMap, Integer userDeptType, boolean adminFlag)
    {
        if (vmallFlag == 0)
        {
            return checkVmallAdLandInfo(dbConn, info, buffer, account, dicMap, userDeptType, adminFlag, vmallFlag);
        }
        //        else if(vmallFlag == 1)
        //        {
        //            return checkHonorAdLandInfo(dbConn, info, buffer, account, dicMap, userDeptType, adminFlag,vmallFlag);
        //        }
        else
        {
            return checkHonorAdLandInfo(dbConn, info, buffer, account, dicMap, userDeptType, adminFlag, vmallFlag);
        }
        
    }
    
    /**
     * <检查荣耀官网广告位着陆链接信息>
     * @param dbConn          数据库链接
     * @param info          着陆链接信息
     * @param buffer        校验信息
     * @param account       用户账号
     * @param dicMap        字典信息
     * @param userDeptType 用户类型
     * @param adminFlag    管理员
     * @param vmallFlag 
     * @return 
     * @see [类、类#方法、类#成员]
     */
    private static UploadDealState checkHonorAdLandInfo(DBConnection dbConn, AdTemplateInfo info, StringBuffer buffer,
        String account, AdDicMapInfo dicMap, Integer userDeptType, boolean adminFlag, int vmallFlag)
    {
        //检查广告位ID
        boolean aidField = LandInfoTemplateCheck.checkAidField(dbConn, info, buffer, dicMap, userDeptType);
        if (!aidField)
        {
            return UploadDealState.DEAL;
        }
        
        //检查荣耀着陆连接广告位ID是否正确
        boolean aidValid =
            LandInfoTemplateCheck.checkAidValid(dbConn, info, buffer, account, false, userDeptType, adminFlag);
        if (!aidValid)
        {
            return UploadDealState.DEAL;
        }
        
        //判断着陆平台是否错误
        AdInfoEntity entity = LandInfoDao.getAdInfo(dbConn, info.getAid());
        
        UploadDealState state = LandInfoTemplateCheck.checkPlatform(entity, buffer, vmallFlag);
        
        if (UploadDealState.DEAL != state)
        {
            return state;
        }
        
        //检查着陆连接
        LandInfoTemplateCheck.checkLandUrlField(info, buffer);
        
        return UploadDealState.DEAL;
        
    }
    
    /**
     * <检查vmall广告位着陆链接信息>
     * @param dbConn          数据库链接
     * @param info          着陆链接信息
     * @param buffer        校验信息
     * @param account       用户账号
     * @param dicMap        字典
     * @param userDeptType  账号类型
     * @param adminFlag     是否是管理员
     * @param vmallFlag 
     * @return              状态
     * @see [类、类#方法、类#成员]
     */
    private static UploadDealState checkVmallAdLandInfo(DBConnection dbConn, AdTemplateInfo info, StringBuffer buffer,
        String account, AdDicMapInfo dicMap, Integer userDeptType, boolean adminFlag, int vmallFlag)
    {
        //检查广告位ID
        boolean checkAidField = LandInfoTemplateCheck.checkAidField(dbConn, info, buffer, dicMap, userDeptType);
        
        if (!checkAidField)
        {
            return UploadDealState.DEAL;
        }
        
        //检查Vmall着陆连接广告位ID是否正确
        boolean aidValid =
            LandInfoTemplateCheck.checkAidValid(dbConn, info, buffer, account, true, userDeptType, adminFlag);
        if (!aidValid)
        {
            return UploadDealState.DEAL;
        }
        
        //检查着陆平台是否正确
        AdInfoEntity entity = LandInfoDao.getAdInfo(dbConn, info.getAid());
        
        UploadDealState platformState = LandInfoTemplateCheck.checkPlatform(entity, buffer, vmallFlag);
        
        if (UploadDealState.DEAL != platformState)
        {
            return platformState;
        }
        
        //检查SID
        LandInfoTemplateCheck.checkSidField(info, buffer);
        
        //检查cpsName
        LandInfoTemplateCheck.checkCpsNameField(info, buffer);
        
        //检查source
        LandInfoTemplateCheck.checkSourceField(info, buffer);
        
        //检查渠道名称
        LandInfoTemplateCheck.checkChannelNameField(info, buffer);
        
        //检查channel
        LandInfoTemplateCheck.checkChannelField(info, buffer);
        
        //检查cid
        LandInfoTemplateCheck.checkCidField(info, buffer);
        
        //检查着陆连接
        LandInfoTemplateCheck.checkVmallLandUrlField(info, buffer);
        
        return UploadDealState.DEAL;
    }
    
    /**
     * <保存广告位着陆链接信息>
     * @param dbConn          数据库链接
     * @param info          着陆链接信息
     * @param buffer        校验信息
     * @param account       用户账号
     * @param vmallFlag     是否是vmall着陆链接
     * @param batchFlag     是否覆盖处理
     * @return              保存是否成功
     * @see [类、类#方法、类#成员]
     */
    public static UploadDealState saveAdLandInfo(DBConnection dbConn, AdTemplateInfo info, StringBuffer buffer,
        String account, int vmallFlag, boolean batchFlag)
    {
        if (vmallFlag == 0)
        {
            return saveVmallAdLandInfo(dbConn, info, buffer, account, batchFlag);
        }
        else if (vmallFlag == 1)
        {
            return saveHonorAdLandInfo(dbConn, info, buffer, account, batchFlag);
        }
        else
        {
            //sxy 20151127
            return saveHonorAdLandInfo(dbConn, info, buffer, account, batchFlag);
        }
        
    }
    
    /**
     * <保存荣耀官网广告位着陆链接信息>
     * @param dbConn          数据库链接
     * @param info          着陆链接信息
     * @param buffer        校验信息
     * @param account       用户账号
     * @param batchFlag     是否覆盖处理
     * @return              保存是否成功
     * @see [类、类#方法、类#成员]
     */
    private static UploadDealState saveHonorAdLandInfo(DBConnection dbConn, AdTemplateInfo info, StringBuffer buffer,
        String account, boolean batchFlag)
    {
        if (0 != buffer.toString().trim().length())
        {
            return UploadDealState.DEAL_FAIL;
        }
        
        boolean existFlag = LandInfoDao.isLandInfoExist(dbConn, info.getAid());
        if (existFlag && !batchFlag)
        {
            buffer.append("记录已经存在不覆盖处理");
            return UploadDealState.DEAL_FAIL;
        }
        
        boolean flag = LandInfoDao.updateHonorLandInfo(account, dbConn, info);
        
        //获取返回信息
        return getUpdateInfo(buffer, existFlag, flag);
        
    }
    
    /**
     * <获取更新返回信息>
     * @param buffer      返回信息
     * @param existFlag   存在标志
     * @param flag        操作状态
     * @return            更新标志
     * @see [类、类#方法、类#成员]
     */
    private static UploadDealState getUpdateInfo(StringBuffer buffer, boolean existFlag, boolean flag)
    {
        if (flag && existFlag)
        {
            buffer.append("更新记录成功;");
            return UploadDealState.UPDATE_SUCCESS;
        }
        else if (flag)
        {
            buffer.append("新增记录成功;");
            return UploadDealState.ADD_SUCCESS;
        }
        else
        {
            buffer.append("更新记录失败;");
            return UploadDealState.DEAL_FAIL;
        }
    }
    
    /**
     * <保存vmall广告位着陆链接信息>
     * @param dbConn          数据库链接
     * @param info          着陆链接信息
     * @param buffer        校验信息
     * @param account       用户账号
     * @param batchFlag     是否覆盖处理
     * @return              保存是否成功
     * @see [类、类#方法、类#成员]
     */
    private static UploadDealState saveVmallAdLandInfo(DBConnection dbConn, AdTemplateInfo info, StringBuffer buffer,
        String account, boolean batchFlag)
    {
        if (0 != buffer.toString().trim().length())
        {
            return UploadDealState.DEAL_FAIL;
        }
        
        boolean existFlag = LandInfoDao.isLandInfoExist(dbConn, info.getAid());
        if (existFlag && !batchFlag)
        {
            buffer.append("记录已经存在不覆盖处理");
            return UploadDealState.DEAL_FAIL;
        }
        
        boolean flag = LandInfoDao.updateVmallLandInfo(account, dbConn, info);
        
        //获取返回信息
        return getUpdateInfo(buffer, existFlag, flag);
        
    }
    
}
