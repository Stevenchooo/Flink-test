/*
 * 文 件 名:  LandTemplateFileUpload.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-6-2
 */
package com.huawei.manager.mkt.api;

import java.io.InputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;

import com.huawei.manager.mkt.constant.UploadDealState;
import com.huawei.manager.mkt.dao.AdInfoDao;
import com.huawei.manager.mkt.info.AdDicMapInfo;
import com.huawei.manager.mkt.info.AdTemplateInfo;
import com.huawei.manager.mkt.util.AdTemplateFileUtils;
import com.huawei.manager.mkt.util.ExcelUtils;
import com.huawei.manager.mkt.util.FileUtils;
import com.huawei.manager.mkt.util.LandTemplateFileUtils;
import com.huawei.manager.utils.Constant;
import com.huawei.util.DBUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.config.method.MethodConfig;
import com.huawei.waf.core.config.method.process.RDBProcessConfig;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.core.run.process.UploadRDBProcessor;
import com.huawei.waf.protocol.RetCode;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-2]
 * @see  [相关类/方法]
 */
public class LandTemplateFileUpload extends UploadRDBProcessor
{
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 处理文件上传
     * @param context    系统上下文
     * @param paraName   请求参数
     * @param fileName   文件名称
     * @param size       文件大小
     * @param stream     文件流
     * @return           处理是否成功
     */
    public int processFile(MethodContext context, String paraName, String fileName, long size, InputStream stream)
    {
        DBConnection dbConn = null;
        try
        {
            
            Long currentTimeMillis = System.currentTimeMillis();
            
            String desFile = AdTemplateFileUpload.class.getResource(Constant.SLANT).getPath() + Constant.SUBEXCELPATH
                + currentTimeMillis + "_des.xls";
                
            String resFile = AdTemplateFileUpload.class.getResource(Constant.SLANT).getPath() + Constant.SUBEXCELPATH
                + currentTimeMillis + "_res.xls";
                
            //获取是否覆盖标识
            int vmallFlag = Integer.valueOf((String)context.getParameter("vmallFlag"));
            
            //获取是否覆盖标识
            boolean batchFlag = Boolean.valueOf((String)context.getParameter("batchFlag"));
            
            //写文件
            FileUtils.writeFile(stream, desFile);
            
            //获取用户名
            String account = context.getAccount();
            
            //数据库连接
            MethodConfig mc = context.getMethodConfig();
            RDBProcessConfig config = (RDBProcessConfig)mc.getProcessConfig();
            dbConn = DBUtil.getConnection(config.getDataSource(context), config.useTransaction());
            
            //数据库连接
            AdDicMapInfo dicMap = AdInfoDao.getAdDicMapInfo(dbConn, account);
            
            //根据用户名称判断是否为管理员
            boolean adminFlag = AdInfoDao.isAdminAccount(dbConn, account);
            
            Integer userDeptType = AdInfoDao.getUserDeptType(dbConn, account);
            
            //获取列表
            List<AdTemplateInfo> list = AdTemplateFileUtils.readTemplateFile(desFile);
            
            //写文件
            // 先创建工作簿对象 2003
            HSSFWorkbook workbook = new HSSFWorkbook();
            //对每条记录进行操作的结果和数据写入到Excel中，以备下载使用
            HSSFSheet sheet = ExcelUtils.wirteFileHeadRow(workbook, dicMap);
            
            //上传数据
            int index = 1;
            
            int total = list.size();
            
            int updateSuccess = 0;
            
            int createSuccess = 0;
            
            //int ignore = 0;
            int vmallIgnore = 0;
            int honorIgnore = 0;
            int otherIgnore = 0;
            
            //获取能够修改的aid
            for (AdTemplateInfo info : list)
            {
                StringBuffer buffer = new StringBuffer();
                
                UploadDealState checkState = LandTemplateFileUtils.checkAdLandInfo(dbConn,
                    info,
                    buffer,
                    vmallFlag,
                    account,
                    dicMap,
                    userDeptType,
                    adminFlag);
                    
                if (UploadDealState.IGNOREHONOR == checkState)
                {
                    //忽略荣耀记录
                    honorIgnore++;
                }
                else if (UploadDealState.IGNOREVMALL == checkState)
                {
                    // 处理忽略(vmall)
                    vmallIgnore++;
                }
                else if (UploadDealState.IGNOREOTHER == checkState)
                {
                    //处理忽略(other)
                    otherIgnore++;
                }
                else
                {
                    UploadDealState dealState =
                        LandTemplateFileUtils.saveAdLandInfo(dbConn, info, buffer, account, vmallFlag, batchFlag);
                        
                    if (UploadDealState.UPDATE_SUCCESS == dealState)
                    {
                        updateSuccess++;
                    }
                    else if (UploadDealState.ADD_SUCCESS == dealState)
                    {
                        createSuccess++;
                    }
                }
                
                AdTemplateFileUtils.writeFileLineInfo(sheet, index, info, buffer);
                
                index++;
                
            }
            
            // 生成文件  
            FileUtils.writeFile(resFile, workbook);
            
            //页面返回
            context.setResult("total", total);
            context.setResult("updateSuccess", updateSuccess);
            context.setResult("createSuccess", createSuccess);
            //context.setResult("ignore", ignore);
            context.setResult("vmallIgnore", vmallIgnore);
            context.setResult("honorIgnore", honorIgnore);
            context.setResult("otherIgnore", otherIgnore);
            context.setResult("fileTimeMillis", currentTimeMillis);
        }
        finally
        {
            
            DBUtil.freeConnection(dbConn, true);
        }
        
        LOG.debug("LandTemplateFileUpload success");
        return RetCode.OK;
    }
    
}
