/*
 * 文 件 名:  AdTemplateFileUtils.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-5-29
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
import org.slf4j.Logger;

import com.huawei.manager.mkt.constant.DaoDealState;
import com.huawei.manager.mkt.constant.UploadDealState;
import com.huawei.manager.mkt.dao.AdInfoDao;
import com.huawei.manager.mkt.info.AdDicMapInfo;
import com.huawei.manager.mkt.info.AdTemplateInfo;
import com.huawei.manager.mkt.util.checkUtil.AdInfoTemplateCheck;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.LogUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-5-29]
 * @see  [相关类/方法]
 */
public class AdTemplateFileUtils
{
    
    private static final String AD_TEMPLATE = "广告位模板";
    
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * <从上次Excel文件中读取广告位信息>
     * @param fileName   读取的文件名
     * @return           广告位信息列表
     * @see [类、类#方法、类#成员]
     */
    public static List<AdTemplateInfo> readTemplateFile(String fileName)
    {
        List<AdTemplateInfo> list = new ArrayList<AdTemplateInfo>();
        
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(fileName);
            HSSFWorkbook xwb = new HSSFWorkbook(fis);
            
            int num = xwb.getNumberOfSheets();
            
            for (int i = 0; i < num; i++)
            {
                getAdTempLateInfoFromSheet(list, xwb, i);
                
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
     * <从excel文件中某一个sheet页获取信息>
     * @param list        广告位信息列表
     * @param xwb         Excelworkboox对象
     * @param index       sheet页序号
     * @see [类、类#方法、类#成员]
     */
    private static void getAdTempLateInfoFromSheet(List<AdTemplateInfo> list, HSSFWorkbook xwb, int index)
    {
        String sheetName = xwb.getSheetName(index);
        
        //判断sheet名称是否正确
        if (!AD_TEMPLATE.equals(sheetName))
        {
            return;
        }
        
        //获取sheet对象
        HSSFSheet xSheet = xwb.getSheetAt(index);
        if (null == xSheet)
        {
            return;
        }
        
        //获取当前sheet页中行数
        int rowNum = xSheet.getPhysicalNumberOfRows();
        if (rowNum <= 1)
        {
            return;
        }
        
        //从第二行开始获取信息
        for (int j = 1; j < rowNum; j++)
        {
            HSSFRow row = xSheet.getRow(j);
            getAdTemplateFromRow(list, row);
        }
        
    }
    
    /**
     * <从Excel文件某一行中获取广告位信息>
     * @param list     广告位列表
     * @param row      文件行信息
     * @see [类、类#方法、类#成员]
     */
    private static void getAdTemplateFromRow(List<AdTemplateInfo> list, HSSFRow row)
    {
        if (null == row)
        {
            return;
        }
        AdTemplateInfo adTemplateInfo = getAdTempLate(row);
        
        if (isValidTempInfo(adTemplateInfo))
        {
            list.add(adTemplateInfo);
        }
        
    }
    
    /**
     * <判断模板是否合法>
     * @param info   模板信息
     * @return       是否合法
     * @see [类、类#方法、类#成员]
     */
    private static boolean isValidTempInfo(AdTemplateInfo info)
    {
        if (null == info)
        {
            return false;
        }
        //广告位ID                        
        if (null != info.getAidStr())
        {
            return true;
        }
        
        //营销活动名称
        else if (null != info.getMktName())
        {
            return true;
        }
        
        //网站名称（下拉）
        else if (null != info.getWebName())
        {
            return true;
        }
        
        //频道 
        else if (null != info.getAdChannel())
        {
            return true;
        }
        
        //广告位
        else if (null != info.getAdPosition())
        {
            return true;
        }
        
        //广告素材类型（下拉）
        else if (null != info.getMaterialType())
        {
            return true;
        }
        
        //素材要求
        else if (null != info.getMaterialDesc())
        {
            return true;
        }
        
        //端口所属（下拉）
        else if (null != info.getPort())
        {
            return true;
        }
        
        //着陆平台（下拉）
        else if (null != info.getPlatform())
        {
            return true;
        }
        
        //着陆页面描述
        else if (null != info.getPlatformDesc())
        {
            return true;
        }
        
        //投放天数
        else if (null != info.getDeliveryDaysStr())
        {
            return true;
        }
        
        //投放日期
        else if (null != info.getDeliveryTimes())
        {
            return true;
        }
        
        //引流类型（下拉）
        else if (null != info.getFlowType())
        {
            return true;
        }
        
        //预计曝光量
        else if (null != info.getExpAmountStr())
        {
            return true;
        }
        
        //预计点击量
        else if (null != info.getClickAmountStr())
        {
            return true;
        }
        
        //刊例价
        else if (null != info.getPublishPriceStr())
        {
            return true;
        }
        
        //净价
        else if (null != info.getNetPriceStr())
        {
            return true;
        }
        
        //资源来源（下拉）
        else if (null != info.getResource())
        {
            return true;
        }
        
        //是否监控曝光（下拉）
        else if (null != info.getIsClick())
        {
            return true;
        }
        
        //是否监控点击（下拉）
        else if (null != info.getIsExposure())
        {
            return true;
        }
        
        //监控平台（下拉） 
        else if (null != info.getMonitorPlatform())
        {
            return true;
        }
        
        //SID
        else if (null != info.getSid())
        {
            return true;
        }
        
        //CPS提供商名称
        else if (null != info.getCpsName())
        {
            return true;
        }
        
        //Source
        else if (null != info.getSource())
        {
            return true;
        }
        
        //channel
        else if (null != info.getLandChannel())
        {
            return true;
        }
        
        //渠道名称
        else if (null != info.getLandChannelName())
        {
            return true;
        }
        
        //CID
        else if (null != info.getCid())
        {
            return true;
        }
        
        //着陆链接
        else if (null != info.getLandUrl())
        {
            return true;
        }
        else
        {
            return false;
        }
        
    }
    
    /**
     * <获取广告位信息>
     * @param row    行信息
     * @return       广告位信息
     * @see [类、类#方法、类#成员]
     */
    private static AdTemplateInfo getAdTempLate(HSSFRow row)
    {
        //获取上传信息
        AdTemplateInfo info = new AdTemplateInfo();
        //获取模板值
        String aidStr = ExcelUtils.getCellValue(row.getCell(0));
        info.setAidStr(aidStr);
        
        String mktName = ExcelUtils.getCellValue(row.getCell(1));
        info.setMktName(mktName);
        
        String webName = ExcelUtils.getCellValue(row.getCell(2));
        info.setWebName(webName);
        
        String adChannel = ExcelUtils.getCellValue(row.getCell(3));
        info.setAdChannel(adChannel);
        
        String adPosition = ExcelUtils.getCellValue(row.getCell(4));
        info.setAdPosition(adPosition);
        
        String materialType = ExcelUtils.getCellValue(row.getCell(5));
        info.setMaterialType(materialType);
        
        String materialDesc = ExcelUtils.getCellValue(row.getCell(6));
        info.setMaterialDesc(materialDesc);
        
        String port = ExcelUtils.getCellValue(row.getCell(7));
        info.setPort(port);
        
        String platform = ExcelUtils.getCellValue(row.getCell(8));
        info.setPlatform(platform);
        
        String platformDesc = ExcelUtils.getCellValue(row.getCell(9));
        info.setPlatformDesc(platformDesc);
        
        String deliveryDaysStr = ExcelUtils.getCellValue(row.getCell(10));
        info.setDeliveryDaysStr(deliveryDaysStr);
        
        String deliveryTimes = ExcelUtils.getCellValue(row.getCell(11));
        info.setDeliveryTimes(deliveryTimes);
        
        String flowType = ExcelUtils.getCellValue(row.getCell(12));
        info.setFlowType(flowType);
        
        String expAmountStr = ExcelUtils.getCellValue(row.getCell(13));
        info.setExpAmountStr(expAmountStr);
        
        String clickAmountStr = ExcelUtils.getCellValue(row.getCell(14));
        info.setClickAmountStr(clickAmountStr);
        
        String publishPriceStr = ExcelUtils.getCellValue(row.getCell(15));
        info.setPublishPriceStr(publishPriceStr);
        
        String netPriceStr = ExcelUtils.getCellValue(row.getCell(16));
        info.setNetPriceStr(netPriceStr);
        
        String resource = ExcelUtils.getCellValue(row.getCell(17));
        info.setResource(resource);
        
        String isExposure = ExcelUtils.getCellValue(row.getCell(18));
        info.setIsExposure(isExposure);
        
        String isClick = ExcelUtils.getCellValue(row.getCell(19));
        info.setIsClick(isClick);
        
        String monitorPlatform = ExcelUtils.getCellValue(row.getCell(20));
        info.setMonitorPlatform(monitorPlatform);
        
        //sid
        String sid = ExcelUtils.getCellValue(row.getCell(21));
        info.setSid(sid);
        
        //cps提供商名称
        String cpsName = ExcelUtils.getCellValue(row.getCell(22));
        info.setCpsName(cpsName);
        
        //source
        String source = ExcelUtils.getCellValue(row.getCell(23));
        info.setSource(source);
        
        //cid
        String cid = ExcelUtils.getCellValue(row.getCell(24));
        info.setCid(cid);
        //渠道名称
        String landChannelName = ExcelUtils.getCellValue(row.getCell(25));
        info.setLandChannelName(landChannelName);
        
        //channel
        String landChannel = ExcelUtils.getCellValue(row.getCell(26));
        info.setLandChannel(landChannel);
        
        //着陆链接
        String landUrl = ExcelUtils.getCellValue(row.getCell(27));
        info.setLandUrl(landUrl);
        
        return info;
    }
    
    /**
     * <写Excel文件行信息>
     * @param sheet    Excel文件sheet页对象
     * @param index    行号
     * @param info     广告位信息
     * @param buffer   操作信息
     * @see [类、类#方法、类#成员]
     * 直接写死也是醉了
     */
    public static void writeFileLineInfo(HSSFSheet sheet, int index, AdTemplateInfo info, StringBuffer buffer)
    {
        //写入excel文件
        HSSFRow row = sheet.getRow(index);
        if (null == row)
        {
            row = sheet.createRow(index);
        }
        
        //活动ID
        HSSFCell adInfoIdCell = row.getCell(0);
        if (null == adInfoIdCell)
        {
            adInfoIdCell = row.createCell(0);
        }
        
        if (null != info.getAid())
        {
            adInfoIdCell.setCellValue(info.getAid());
        }
        else if (null != info.getAidStr())
        {
            adInfoIdCell.setCellValue(info.getAidStr());
        }
        
        //活动名称
        HSSFCell mktNameCell = row.getCell(1);
        if (null == mktNameCell)
        {
            mktNameCell = row.createCell(1);
        }
        
        if (null != info.getMktName())
        {
            mktNameCell.setCellValue(info.getMktName());
        }
        
        //网站名称    
        HSSFCell webNameCell = row.getCell(2);
        if (null == webNameCell)
        {
            webNameCell = row.createCell(2);
        }
        
        if (null != info.getWebName())
        {
            webNameCell.setCellValue(info.getWebName());
        }
        
        //频道  
        HSSFCell channelCell = row.getCell(3);
        if (null == channelCell)
        {
            channelCell = row.createCell(3);
        }
        
        if (null != info.getAdChannel())
        {
            channelCell.setCellValue(info.getAdChannel());
        }
        
        //广告位
        HSSFCell adPositionCell = row.getCell(4);
        if (null == adPositionCell)
        {
            adPositionCell = row.createCell(4);
        }
        
        if (null != info.getAdPosition())
        {
            adPositionCell.setCellValue(info.getAdPosition());
        }
        
        //广告素材类型  
        HSSFCell materialTypeCell = row.getCell(5);
        if (null == materialTypeCell)
        {
            materialTypeCell = row.createCell(5);
        }
        
        if (null != info.getMaterialType())
        {
            materialTypeCell.setCellValue(info.getMaterialType());
        }
        
        //广告素材类型要求
        HSSFCell materialDescCell = row.getCell(6);
        if (null == materialDescCell)
        {
            materialDescCell = row.createCell(6);
        }
        
        if (null != info.getMaterialDesc())
        {
            materialDescCell.setCellValue(info.getMaterialDesc());
        }
        
        //端口所属   
        HSSFCell portCell = row.getCell(7);
        
        if (null == portCell)
        {
            portCell = row.createCell(7);
        }
        
        if (null != info.getPort())
        {
            portCell.setCellValue(info.getPort());
        }
        
        //着陆平台   
        HSSFCell platformCell = row.getCell(8);
        
        if (null == platformCell)
        {
            platformCell = row.createCell(8);
        }
        
        if (null != info.getPlatform())
        {
            platformCell.setCellValue(info.getPlatform());
        }
        
        //着陆页面描述 
        HSSFCell platformDescCell = row.getCell(9);
        if (null == platformDescCell)
        {
            platformDescCell = row.createCell(9);
        }
        if (null != info.getPlatformDesc())
        {
            platformDescCell.setCellValue(info.getPlatformDesc());
        }
        
        //投放天数    
        HSSFCell deliveryDaysCell = row.getCell(10);
        if (null == deliveryDaysCell)
        {
            deliveryDaysCell = row.createCell(10);
        }
        
        if (null != info.getDeliveryDays())
        {
            deliveryDaysCell.setCellValue(info.getDeliveryDays());
        }
        else if (null != info.getDeliveryDaysStr())
        {
            deliveryDaysCell.setCellValue(info.getDeliveryDaysStr());
        }
        
        //投放日期   
        HSSFCell deliveryTimesCell = row.getCell(11);
        if (null == deliveryTimesCell)
        {
            deliveryTimesCell = row.createCell(11);
        }
        
        if (null != info.getDeliveryTimes())
        {
            deliveryTimesCell.setCellValue(info.getDeliveryTimes());
        }
        
        //引流类型    
        HSSFCell flowTypeCell = row.getCell(12);
        if (null == flowTypeCell)
        {
            flowTypeCell = row.createCell(12);
        }
        
        if (null != info.getFlowType())
        {
            flowTypeCell.setCellValue(info.getFlowType());
        }
        
        //预计曝光量 
        HSSFCell expAmountCell = row.getCell(13);
        if (null == expAmountCell)
        {
            expAmountCell = row.createCell(13);
        }
        
        if (null != info.getExpAmount())
        {
            expAmountCell.setCellValue(info.getExpAmount());
        }
        else if (null != info.getExpAmountStr())
        {
            expAmountCell.setCellValue(info.getExpAmountStr());
        }
        
        //预计点击量   
        HSSFCell clickAmountCell = row.getCell(14);
        if (null == clickAmountCell)
        {
            clickAmountCell = row.createCell(14);
        }
        
        if (null != info.getClickAmount())
        {
            clickAmountCell.setCellValue(info.getClickAmount());
        }
        else if (null != info.getClickAmountStr())
        {
            clickAmountCell.setCellValue(info.getClickAmountStr());
        }
        
        //刊例价
        HSSFCell publishPriceCell = row.getCell(15);
        if (null == publishPriceCell)
        {
            publishPriceCell = row.createCell(15);
        }
        if (null != info.getPublishPrice())
        {
            publishPriceCell.setCellValue(info.getPublishPrice());
        }
        else if (null != info.getPublishPriceStr())
        {
            publishPriceCell.setCellValue(info.getPublishPriceStr());
        }
        
        //净价 
        HSSFCell netPriceCell = row.getCell(16);
        if (null == netPriceCell)
        {
            netPriceCell = row.createCell(16);
        }
        if (null != info.getNetPrice())
        {
            netPriceCell.setCellValue(info.getNetPrice());
        }
        else if (null != info.getNetPriceStr())
        {
            netPriceCell.setCellValue(info.getNetPriceStr());
        }
        
        //资源来源    
        HSSFCell resourceCell = row.getCell(17);
        if (null == resourceCell)
        {
            resourceCell = row.createCell(17);
        }
        if (null != info.getResource())
        {
            resourceCell.setCellValue(info.getResource());
        }
        
        //是否监控曝光      
        HSSFCell isExposureCell = row.getCell(18);
        if (null == isExposureCell)
        {
            isExposureCell = row.createCell(18);
        }
        if (null != info.getIsExposure())
        {
            isExposureCell.setCellValue(info.getIsExposure());
        }
        
        //是否监控点击  
        HSSFCell isClickCell = row.getCell(19);
        if (null == isClickCell)
        {
            isClickCell = row.createCell(19);
        }
        if (null != info.getIsClick())
        {
            isClickCell.setCellValue(info.getIsClick());
        }
        
        //监控平台
        HSSFCell monitorPlatformCell = row.getCell(20);
        if (null == monitorPlatformCell)
        {
            monitorPlatformCell = row.createCell(20);
        }
        
        if (null != info.getMonitorPlatform())
        {
            monitorPlatformCell.setCellValue(info.getMonitorPlatform());
        }
        
        //sid
        HSSFCell sidCell = row.getCell(21);
        if (null == sidCell)
        {
            sidCell = row.createCell(21);
        }
        
        if (null != info.getSid())
        {
            sidCell.setCellValue(info.getSid());
        }
        
        //cps提供商名称
        HSSFCell cpsNameCell = row.getCell(22);
        if (null == cpsNameCell)
        {
            cpsNameCell = row.createCell(22);
        }
        
        if (null != info.getCpsName())
        {
            cpsNameCell.setCellValue(info.getCpsName());
        }
        
        //source
        HSSFCell sourceCell = row.getCell(23);
        if (null == sourceCell)
        {
            sourceCell = row.createCell(23);
        }
        if (null != info.getSource())
        {
            sourceCell.setCellValue(info.getSource());
        }
        
      //cid
        HSSFCell cidCell = row.getCell(24);
        if (null == cidCell)
        {
            cidCell = row.createCell(24);
        }
        if (null != info.getCid())
        {
            cidCell.setCellValue(info.getCid());
        }
        
        //渠道名称
        HSSFCell landChannelNameCell = row.getCell(25);
        if (null == landChannelNameCell)
        {
            landChannelNameCell = row.createCell(25);
        }
        if (null != info.getLandChannelName())
        {
            landChannelNameCell.setCellValue(info.getLandChannelName());
        }
        
        //channel
        HSSFCell landChannelCell = row.getCell(26);
        if (null == landChannelCell)
        {
            landChannelCell = row.createCell(26);
        }
        if (null != info.getLandChannel())
        {
            landChannelCell.setCellValue(info.getLandChannel());
        }
               
        //着陆链接
        HSSFCell landUrlCell = row.getCell(27);
        if (null == landUrlCell)
        {
            landUrlCell = row.createCell(27);
        }
        if (null != info.getLandUrl())
        {
            landUrlCell.setCellValue(info.getLandUrl());
        }
        
        //结果
        HSSFCell resCell = row.getCell(28);
        if (null == resCell)
        {
            resCell = row.createCell(28);
        }
        resCell.setCellValue(buffer.toString());
    }
    
    /**
     * <检查广告位录入信息>
     * @param dbConn    数据库连接
     * @param dicMap    字典集合
     * @param info      广告位信息
     * @param buffer    校验信息
     * @param userType   用户类型
     * @param adminFlag  是否是管理员
     * @see [类、类#方法、类#成员]
     */
    public static void checkAdTemplateInfo(DBConnection dbConn, AdDicMapInfo dicMap, AdTemplateInfo info,
        StringBuffer buffer, Integer userType, boolean adminFlag)
    {
        if (null == dicMap)
        {
            buffer.append("获取广告位对应关系失败;");
            return;
        }
        //检查活动ID
        AdInfoTemplateCheck.checkAidField(dbConn, info, buffer);
        
        //检查营销活动名称
        AdInfoTemplateCheck.checkMktNameField(info, dicMap.getMktMap(), buffer, userType);
        
        //检查网站名称
        AdInfoTemplateCheck.checkWebNameField(info, dicMap.getWebMap(), buffer, userType);
        
        //获取媒体类型
        Integer mediaTypeId = dicMap.getMediaAndWebMap().get(info.getWebNameId());
        info.setMediaTypeId(mediaTypeId);
        
        //检查频道
        AdInfoTemplateCheck.checkChannelField(info, buffer);
        
        //检查广告位
        AdInfoTemplateCheck.checkAdPositionField(info, buffer);
        
        //检查广告素材类型
        AdInfoTemplateCheck.checkMaterialTypeField(info, dicMap.getMateriaMap(), buffer);
        
        //检查素材描述
        AdInfoTemplateCheck.checkMaterialDescField(info, buffer);
        
        //检查端口所属
        AdInfoTemplateCheck.checkPortField(info, dicMap.getPortMap(), buffer);
        
        //检查着陆平台
        AdInfoTemplateCheck.checkPlatformField(dbConn, info, dicMap.getPlatformMap(), buffer, userType, adminFlag);
        
        //检查着陆页面描述
        AdInfoTemplateCheck.checkPlatformDescField(info, buffer);
        
        //检查投放天数
        AdInfoTemplateCheck.checkDeliveryDaysField(info, buffer);
        
        //投放日期
        AdInfoTemplateCheck.checkDeliveryTimesField(info, buffer);
        
        //引流类型
        AdInfoTemplateCheck.checkFlowTypeField(info, dicMap.getFlowMap(), buffer);
        
        //预计曝光量   
        AdInfoTemplateCheck.checkExpAmountField(info, buffer);
        
        //预计点击量 
        AdInfoTemplateCheck.checkClickAmountField(info, buffer);
        
        //刊例价   
        AdInfoTemplateCheck.checkPublishPriceField(info, buffer);
        
        //净价    
        AdInfoTemplateCheck.checkNetPriceField(info, buffer);
        
        //资源来源  
        AdInfoTemplateCheck.checkResourceField(info, dicMap.getResourceMap(), buffer);
        
        //是否监控曝光    
        AdInfoTemplateCheck.checkIsExposureField(info, dicMap.getBooleanMap(), buffer);
        
        //是否监控点击 
        AdInfoTemplateCheck.checkIsClickField(info, dicMap.getBooleanMap(), buffer);
        
        //监控平台
        AdInfoTemplateCheck.checkMonitorPlatformField(info, dicMap.getMonitorPlatformMap(), buffer);
        
        //检查用户权限
        AdInfoTemplateCheck.checkAdInfoUserAuthority(dbConn, info, userType, buffer, adminFlag);
        
    }
    
    /**
     * <保存广告位录入信息>
     * @param dbConn        数据库连接
     * @param info        广告位信息
     * @param buffer      校验信息
     * @param account     用户账号
     * @param batchFlag   是否覆盖处理
     * @return            广告位录入处理状态
     * @see [类、类#方法、类#成员]
     */
    public static UploadDealState saveAdTemplateInfo(DBConnection dbConn, AdTemplateInfo info, StringBuffer buffer,
        String account, boolean batchFlag)
    {
        if (0 != buffer.toString().trim().length())
        {
            return UploadDealState.DEAL_FAIL;
        }
        //判断新增或者更新
        boolean addFlag = AdInfoDao.getAddAdInfoFlag(dbConn, info);
        
        //判断有没有权限
        boolean roleFlag = AdInfoDao.getRoleFlag(addFlag, dbConn, info, account);
        
        //新增或者更新
        if (!roleFlag)
        {
            buffer.append("用户没有权限操作此记录");
            return UploadDealState.DEAL_FAIL;
        }
        //新增
        if (addFlag)
        {
            return addAdInfo(dbConn, info, buffer, account);
            
        }
        //可以进行覆盖修改修改
        if (batchFlag)
        {
            return updateAdInfo(dbConn, info, buffer, account);
        }
        
        //不覆盖修改
        buffer.append("重复数据，不更新");
        
        return UploadDealState.DEAL_FAIL;
        
    }
    
    /**
     * <更新广告位信息>
     * @param dbConn     数据连接
     * @param info     广告位信息
     * @param buffer   校验信息
     * @param account  用户账号
     * @return         更新广告位是否正确
     * @see [类、类#方法、类#成员]
     */
    private static UploadDealState updateAdInfo(DBConnection dbConn, AdTemplateInfo info, StringBuffer buffer,
        String account)
    {
        int state = AdInfoDao.updateAdInfo(account, dbConn, info);
        
        if (DaoDealState.OK == state)
        {
            buffer.append("更新记录成功");
            return UploadDealState.UPDATE_SUCCESS;
        }
        else if (DaoDealState.EXIST == state)
        {
            buffer.append("活动名称、网站名称、频道、广告位、端口所属 重复,更新记录失败");
            return UploadDealState.DEAL_FAIL;
        }
        else
        {
            buffer.append("数据库错误,更新记录失败");
            return UploadDealState.DEAL_FAIL;
        }
    }
    
    /**
     * <新增广告位信息>
     * @param dbConn      数据库连接
     * @param info      广告位信息
     * @param buffer    校验信息
     * @param account   用户账号
     * @return          新增广告位是否正确
     * @see [类、类#方法、类#成员]
     */
    private static UploadDealState addAdInfo(DBConnection dbConn, AdTemplateInfo info, StringBuffer buffer,
        String account)
    {
        //保存广告位信息
        Integer state = AdInfoDao.addAdInfo(account, dbConn, info);
        
        if (DaoDealState.OK == state)
        {
            buffer.append("新增记录成功");
            return UploadDealState.ADD_SUCCESS;
            
        }
        else if (DaoDealState.EXIST == state)
        {
            buffer.append("活动名称、网站名称、频道、广告位、端口所属 重复,新增记录失败");
            return UploadDealState.DEAL_FAIL;
        }
        else
        {
            buffer.append("新增记录失败");
            return UploadDealState.DEAL_FAIL;
        }
    }
}
