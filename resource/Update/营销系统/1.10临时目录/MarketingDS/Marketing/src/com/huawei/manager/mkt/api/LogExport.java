package com.huawei.manager.mkt.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.huawei.manager.mkt.constant.ExcelColName;
import com.huawei.manager.mkt.info.ExportInfo;
import com.huawei.manager.mkt.info.LogExportInfo;


/**
 * <一句话功能简述>
 * <功能详细描述>
 * @author  s00359263
 * @version [Internet Business Service Platform SP V100R100, 2015-11-23]
 * @see  [相关类/方法]
 *  
 */
public class LogExport extends Export
{
    /**
     * sheet页名称
     */
    private static final String SHEET_NAME = "操作日志";
    
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
     * @param rexportList  存放日志记录对象的队列
     * @see [类、类#方法、类#成员]
     */
    public void addSingleExportInfo(Map<String, Object> objMap, List<ExportInfo> rexportList)
    {
        
        if (null != objMap && !objMap.isEmpty())
        {
            
            LogExportInfo info = new LogExportInfo();
            
            info.setOperTime((String)objMap.get(ExcelColName.MEM_OPERATORTIME));
            info.setOperator((String)objMap.get(ExcelColName.MEM_OPERATORLOG));
            info.setOperRes((String)objMap.get(ExcelColName.MEM_OPERATORES));
            info.setOpeRequest((String)objMap.get(ExcelColName.MEM_OPERATOREQ));
            info.setOpeRrsponse((String)objMap.get(ExcelColName.MEM_OPERATORSP));
            rexportList.add(info);
            
        }

        
        /*System.out.println("hisupport =="+EncryptUtil.encode("hisupport"));
        System.out.println("system ==  " +EncryptUtil.encode("123456"));*/
    }
    
    /**
     * <获取生成excel字段名称>
     * @return  获取生成excel字段名称
     * @see [类、类#方法、类#成员]
     */
    public List<String> getColName()
    {
        
        List<String> exportList = new ArrayList<String>();
        
        exportList.add(ExcelColName.OPERATORTIME);
        exportList.add(ExcelColName.OPERATORLOG);
        exportList.add(ExcelColName.OPERATORES);
        exportList.add(ExcelColName.OPERATOREQ);
        exportList.add(ExcelColName.OPERATORSP);
        
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
        
        exportList.add(ExcelColName.MEM_OPERATORTIME);
        exportList.add(ExcelColName.MEM_OPERATORLOG);
        exportList.add(ExcelColName.MEM_OPERATORES);
        exportList.add(ExcelColName.MEM_OPERATOREQ);
        exportList.add(ExcelColName.MEM_OPERATORSP);
        
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
