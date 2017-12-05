package com.huawei.manager.mkt.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.huawei.manager.mkt.constant.ExcelColName;
import com.huawei.manager.mkt.info.BaseExportInfo;
import com.huawei.manager.mkt.info.ExportInfo;
/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author  s00359263
 * @version [Internet Business Service Platform SP V100R100, 2015-11-23]
 * @see  [相关类/方法]
 *  
 */
public class BaseExport extends Export
{
    private static final String SHEET_NAME = "基础数据媒体排名";
    
    /**
     * <获取已经完成导出信息列表>
     * @return         导出广告位信息列表
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
     * @param rexportList   存放日志记录对象的队列
     * @see [类、类#方法、类#成员]
     */
    public void addSingleExportInfo(Map<String, Object> objMap, List<ExportInfo> rexportList)
    {
        
        if (null != objMap && !objMap.isEmpty())
        {
            
            BaseExportInfo info = new BaseExportInfo();
            
            info.setMediaName((String)objMap.get(ExcelColName.MEM_MEDIANAME));
            info.setBgPv((String)objMap.get(ExcelColName.MEM_BGPV));
            info.setBgUv((String)objMap.get(ExcelColName.MEM_BGUV));
            info.setDjPv((String)objMap.get(ExcelColName.MEM_DJPV));
            info.setDjUv((String)objMap.get(ExcelColName.MEM_DJUV));
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
        
        exportList.add(ExcelColName.CMEDIANAME);
        exportList.add(ExcelColName.BGPV);
        exportList.add(ExcelColName.BGUV);
        exportList.add(ExcelColName.DJPV);
        exportList.add(ExcelColName.DJUV);
        
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
        
        exportList.add(ExcelColName.MEM_MEDIANAME);
        exportList.add(ExcelColName.MEM_BGPV);
        exportList.add(ExcelColName.MEM_BGUV);
        exportList.add(ExcelColName.MEM_DJPV);
        exportList.add(ExcelColName.MEM_DJUV);
        
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
