package com.huawei.manager.mkt.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.mkt.constant.ExcelColName;
import com.huawei.manager.mkt.dao.TransReportQueryDao;
import com.huawei.manager.mkt.info.ExportInfo;
import com.huawei.manager.mkt.info.TransformExportInfo;
import com.huawei.util.LogUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author  s00359263
 * @version [Internet Business Service Platform SP V100R100, 2015-11-23]
 * @see  [相关类/方法]
 *  
 */
public class TranformExport extends Export
{
    
    /**
     * 日志
     */
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * sheet页名称
     */
    private static final String SHEET_NAME = "转化报表";
    
    /**
     * 接口处理后如何处理
     * @param context   系统上下文
     * @param conn      数据库连接
     * @return          是否成功
     */
    @Override
    public int process(MethodContext context, DBConnection dbConn)
    {
        
        int from = 0;
        int to = 5000;
        TransReportQueryDao.setContext(context, dbConn, from, to);
        try
        {
            super.afterProcess(context, dbConn);
        }
        catch (SQLException e)
        {
            LOG.error("TranformExport SQLException");
        }
        return RetCode.OK;
        
    }
    
    /**
     * <获取已经完成导出信息列表>
     * @return        导出广告位信息列表
     * @see [类、类#方法、类#成员]
     */
    public List<ExportInfo> getReportExportTypeList()
    {
        
        List<ExportInfo> exportList = new ArrayList<ExportInfo>();
        return exportList;
    }
    
    /**
     * <获取一条记录>
     * @param objMap        一条日志记录的kv结构  
     * @param r_exportList  存放日志记录对象的队列
     * @see [类、类#方法、类#成员]
     */
    public void addSingleExportInfo(Map<String, Object> objMap, List<ExportInfo> rexportList)
    {
        
        if (null != objMap && !objMap.isEmpty())
        {
            
            TransformExportInfo info = new TransformExportInfo();
            
            info.setMktLandInfoActName((String)objMap.get("mktInfoName"));
            info.setMktLandInfoAdPosition((String)objMap.get("adInfoPosition"));
            info.setMktLandInfoChannel((String)objMap.get("adInfoChannel"));
            info.setMktLandInfoPort((String)objMap.get("adInfoPort"));
            info.setProductName((String)objMap.get("productName"));
            info.setMktLandInfoWebName((String)objMap.get("adInfoWebName"));
            info.setPlatform((String)objMap.get("platform"));
            info.setMktLandInfoUseDate((String)objMap.get("deliveryTimes"));
            info.setReportDate((String)objMap.get("reportDate"));
            info.setMktLandInfoSID((String)objMap.get("mktLandInfoSID"));
            info.setMktLandInfoCID((String)objMap.get("mktLandInfoCID"));
            info.setBgPv(objMap.get("bgPv").toString());
            info.setBgUv(objMap.get("bgUv").toString());
            info.setDjPv(objMap.get("djPv").toString());
            info.setDjUv(objMap.get("djUv").toString());
            info.setLandingUv(objMap.get("landingUv").toString());
            info.setLandingRate(objMap.get("landingRate").toString());
            info.setResource((String)objMap.get("resource"));
            info.setOperator((String)objMap.get("operator"));
            
            //下单数
            info.setOrderCount(objMap.get("orderCount").toString());
            //支付订单数
            info.setOrderPayCount(objMap.get("orderPayCount").toString());
            //实际支付订单数
            info.setRealOrderCount(objMap.get("realOrderCount").toString());
            //支付金额
            info.setOrderPayPriceCount(objMap.get("orderPayPriceCount").toString());
            //实际支付金额
            info.setRealOrderAmount(objMap.get("realOrderAmount").toString());
            
            //预约用户数
            info.setReserverUv(objMap.get("reserverUv").toString());
            //下单数BI
            info.setOrderCountBi(objMap.get("orderCountBi").toString());
            //支付订单数BI
            info.setOrderPayCountBi(objMap.get("orderPayCountBi").toString());
            //实际支付订单数BI
            info.setRealOrderCountBi(objMap.get("realOrderCountBi").toString());
            //支付金额BI
            info.setOrderPayPriceCountBi(objMap.get("orderPayPriceCountBi").toString());
            //实际支付金额BI
            info.setRealOrderAmountBi(objMap.get("realOrderAmountBi").toString());
            
            rexportList.add(info);
            
        }
    }
    
    /**
     * <获取生成excel字段名称>
     * @return  获取生成excel字段名称
     * @see [类、类#方法、类#成员]
     */
    public List<String> getColName()
    {
        
        List<String> exportList = new ArrayList<String>();
        
        exportList.add(ExcelColName.MKTINFONAME);
        exportList.add(ExcelColName.ADINFOWEBNAME);
        exportList.add(ExcelColName.ADINFOCHANNEL);
        exportList.add(ExcelColName.ADINFOPOSITION);
        exportList.add(ExcelColName.ADINFOPORT);
        
        exportList.add(ExcelColName.PRODUCTNAME);
        exportList.add(ExcelColName.DELIVERYTIMES);
        exportList.add(ExcelColName.PLATFORM);
        exportList.add(ExcelColName.REPORTDATE);
        exportList.add(ExcelColName.MKTLANDINFOSID);
        exportList.add(ExcelColName.MKTLANDINFOCID);
        
        exportList.add(ExcelColName.BGPV);
        exportList.add(ExcelColName.BGUV);
        exportList.add(ExcelColName.DJPV);
        exportList.add(ExcelColName.DJUV);
        exportList.add(ExcelColName.LANDINGUV);
        exportList.add(ExcelColName.LANDINGRATE);
        
        exportList.add(ExcelColName.CNORDERCOUNT);
        exportList.add(ExcelColName.CNORDERPAYCOUNT);
        exportList.add(ExcelColName.CNREALORDERCOUNT);
        exportList.add(ExcelColName.CNORDERPAYPRICECOUNT);
        exportList.add(ExcelColName.CNREALORDERAMOUNT);
        exportList.add(ExcelColName.CNRESERVERUV);
        exportList.add(ExcelColName.RESOURCE);
        exportList.add(ExcelColName.OPERATOR);
        exportList.add(ExcelColName.CNORDERCOUNTBI);
        exportList.add(ExcelColName.CNORDERPAYCOUNTBI);
        exportList.add(ExcelColName.CNREALORDERCOUNTBI);
        exportList.add(ExcelColName.CNORDERPAYPRICECOUNTBI);
        exportList.add(ExcelColName.CNREALORDERAMOUNTBI);
        return exportList;
    }
    
    /**
     * <获取字段列表>
     * @return  获取字段列表
     * @see [类、类#方法、类#成员]
     */
    public List<String> getColNameString()
    {
        
        List<String> exportList = new ArrayList<String>();
        
        exportList.add(ExcelColName.MEM_MKTLANDINFOACTNAME);
        exportList.add(ExcelColName.MEM_MKTLANDINFOWEBNAME);
        exportList.add(ExcelColName.MEM_MKTLANDINFOCHANNEL);
        exportList.add(ExcelColName.MEM_MKTLANDINFOADPOSITION);
        exportList.add(ExcelColName.MEM_MKTLANDINFOPORT);
        
        exportList.add(ExcelColName.MEM_PRODUCTNAME);
        exportList.add(ExcelColName.MEM_MKTLANDINFOUSEDATE);
        exportList.add(ExcelColName.MEM_PLATFORM);
        exportList.add(ExcelColName.MEM_REPORTDATE);
        exportList.add(ExcelColName.MEM_MKTLANDINFOSID);
        exportList.add(ExcelColName.MEM_MKTLANDINFOCID);
        exportList.add(ExcelColName.MEM_BGPV);
        exportList.add(ExcelColName.MEM_BGUV);
        exportList.add(ExcelColName.MEM_DJPV);
        exportList.add(ExcelColName.MEM_DJUV);
        exportList.add(ExcelColName.MEM_LANDINGUV);
        exportList.add(ExcelColName.MEM_LANDINGRATE);
        
        exportList.add(ExcelColName.MEM_ORDERCOUNT);
        exportList.add(ExcelColName.MEM_ORDERPAYCOUNT);
        exportList.add(ExcelColName.MEM_REALORDERCOUNT);
        exportList.add(ExcelColName.MEM_ORDERPAYPRICECOUNT);
        exportList.add(ExcelColName.MEM_REALORDERAMOUNT);
        exportList.add(ExcelColName.MEM_RESERVERUV);
        exportList.add(ExcelColName.MEM_RESOURCE);
        exportList.add(ExcelColName.MEM_OPERATOR);
        exportList.add(ExcelColName.MEM_ORDERCOUNTBI);
        exportList.add(ExcelColName.MEM_ORDERPAYCOUNTBI);
        exportList.add(ExcelColName.MEM_REALORDERCOUNTBI);
        exportList.add(ExcelColName.MEM_ORDERPAYPRICECOUNTBI);
        exportList.add(ExcelColName.MEM_REALORDERAMOUNTBI);
        
        return exportList;
    }
    
    /**
     * <生成excel文档名称>
     * @return  生成excel文档名称
     * @see [类、类#方法、类#成员]
     */
    public String getFileName()
    {
        String fileName = SHEET_NAME;
        return fileName;
    }
}
