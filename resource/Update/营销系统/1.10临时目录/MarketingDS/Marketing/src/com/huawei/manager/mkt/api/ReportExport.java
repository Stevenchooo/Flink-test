/*
 * 文 件 名:  ReportExport.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-5-27
 */
package com.huawei.manager.mkt.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.mkt.dao.FlowReportQueryDao;
import com.huawei.manager.mkt.info.ReportExportInfo;
import com.huawei.manager.mkt.util.ExcelUtils;
import com.huawei.manager.mkt.util.FileUtils;
import com.huawei.manager.utils.Constant;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.util.LogUtil;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-5-27]
 * @see  [相关类/方法]
 */
public class ReportExport extends AuthRDBProcessor
{
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 接口处理后如何处理
     * @param context   系统上下文
     * @param dbConn      数据库连接
     * @return          是否成功
     */
    public int process(MethodContext context, DBConnection dbConn)
    {
        LOG.debug("enter ReportExport");
        FlowReportQueryDao.setContext(context, dbConn, true);
        //导出信息列表
        List<ReportExportInfo> exportList = getReportExportInfo(context);
        
        //动态分配的文件路径
        String filePath = AdExoprt.class.getResource(Constant.SLANT).getPath() + Constant.SUBEXCELPATH
            + System.currentTimeMillis() + ".xls";
        //生成临时文件
        ExcelUtils.writeReportExportFile(filePath, exportList);
        
        FileUtils.writeHttpFileResponse(context, filePath);
        //写入ad信息
        
        return RetCode.OK;
        
    }
    
    /**
     * <获取已经完成导出信息列表>
     * @param context  系统上下文
     * @return         导出广告位信息列表
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    private List<ReportExportInfo> getReportExportInfo(MethodContext context)
    {
        //数据查询结果
        Map<String, Object> results = context.getResults();
        
        List<ReportExportInfo> exportList = new ArrayList<ReportExportInfo>();
        
        //存储过程返回的列表
        List<Map<String, Object>> list = (List<Map<String, Object>>)results.get("result");
        
        //列表为空，直接返回
        if (null == list || list.isEmpty())
        {
            return null;
        }
        
        //遍历查询结果集，添加入列表
        for (Map<String, Object> objMap : list)
        {
            addReportExportList(exportList, objMap);
        }
        
        //列表返回
        return exportList;
    }
    
    /**
     * <将查询的单个记录插入返回列表>
     * @param exportList   返回数据列表
     * @param objMap       单个查询信息
     * @see [类、类#方法、类#成员]
     */
    private void addReportExportList(List<ReportExportInfo> exportList, Map<String, Object> objMap)
    {
        //参数校验
        if (null == exportList || null == objMap || objMap.isEmpty())
        {
            return;
        }
        
        //获取对应的广告位导出信息
        ReportExportInfo info = getReportExportInfo(objMap);
        
        //插入对应的列表
        if (null != info)
        {
            exportList.add(info);
        }
    }
    
    /**
     * <将数据库查询的map转成ReportExportInfo>
     * @param objMap   数据查询map结果体
     * @return         查询导出信息
     * @see [类、类#方法、类#成员]
     */
    private ReportExportInfo getReportExportInfo(Map<String, Object> objMap)
    {
        if (null != objMap && !objMap.isEmpty())
        {
            ReportExportInfo info = new ReportExportInfo();
            
            //mktLandInfoActName
            info.setMktLandInfoActName((String)objMap.get("mktInfoName"));
            
            //mktLandInfoWebName
            info.setMktLandInfoWebName((String)objMap.get("adInfoWebName"));
            
            //mktLandInfoChannel
            info.setMktLandInfoChannel((String)objMap.get("adInfoChannel"));
            
            //mktLandInfoAdPosition 
            info.setMktLandInfoAdPosition((String)objMap.get("adInfoPosition"));
            
            //mktLandInfoPort
            info.setMktLandInfoPort((String)objMap.get("adInfoPort"));
            
            //mktLandInfoUseDate
            info.setMktLandInfoUseDate((String)objMap.get("adInfoDeliveryTimes"));
            
            //reportDate
            info.setReportDate((String)objMap.get("reportDate"));
            
            //mktLandInfoSID
            info.setMktLandInfoSID((String)objMap.get("mktLandInfoSID"));
            
            //mktLandInfoCID
            info.setMktLandInfoCID((String)objMap.get("mktLandInfoCID"));
            
            //add by sxy 20151113
            //platform
            info.setPlatform((String)objMap.get("platform"));
            
            if (null != objMap.get("dns_name"))
            {
                //dns_name
                info.setDnsname((String)objMap.get("dns_name"));
            }
            
            //bgPv
            if (null != objMap.get("bgPv"))
            {
                info.setBgPv(objMap.get("bgPv").toString());
            }
            
            //bgUv
            if (null != objMap.get("bgUv"))
            {
                info.setBgUv(objMap.get("bgUv").toString());
            }
            
            if (null != objMap.get("djPv"))
            {
                //djPv
                info.setDjPv(objMap.get("djPv").toString());
            }
            
            if (null != objMap.get("djUv"))
            {
                //djUv
                info.setDjUv(objMap.get("djUv").toString());
            }
            
            //resource
            info.setResource((String)objMap.get("resource"));
            
            //operator
            info.setOperator((String)objMap.get("operator"));
            
            return info;
        }
        
        return null;
        
    }
    
}
