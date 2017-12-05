package com.huawei.manager.mkt.api;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.mkt.constant.ExcelColName;
import com.huawei.manager.mkt.info.ExportInfo;
import com.huawei.manager.mkt.util.ExcelUtils;
import com.huawei.manager.mkt.util.FileUtils;
import com.huawei.manager.utils.Constant;
import com.huawei.util.LogUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  s00359263
 * @version [Internet Business Service Platform SP V100R100, 2015-11-19]
 * @see  [相关类/方法]
 *  
 */
public class Export extends AuthRDBProcessor
{
    
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 接口处理后如何处理
     * @param context   系统上下文
     * @param conn      数据库连接
     * @return          是否成功
     * @throws SQLException   数据库异常
     */
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        LOG.debug("enter ReportExport");
        
        //获取导出信息列表
        List<ExportInfo> texportList = getReportExportTypeList();
        
        getReportExportInfo(context, texportList);
        //字段中文名称
        List<String> tColName = getColName();
        //成员名称
        List<String> tCyName = getColNameString();
        //文件名称
        String tfileName = getFileName();
        
        //动态分配的文件路径
        String filePath = Export.class.getResource(Constant.SLANT).getPath() + Constant.SUBEXCELPATH
            + System.currentTimeMillis() + ".xls";
        //生成临时文件
        
        ExcelUtils.writeReportExportFile(filePath, texportList, tColName, tCyName, tfileName);
        
        //写入ad信息
        FileUtils.writeHttpFileResponse(context, filePath);
        return RetCode.OK;
    }
    
    /**
     * <获取excel表格名称的基类函数>
     * @return   excel表格名称
     */
    public String getFileName()
    {
        return null;
    }
    
    /**
     * <获取字段列表的基类函数>
     * @return  获取字段列表
     * @see [类、类#方法、类#成员]
     */
    public List<String> getColNameString()
    {
        return null;
    }
    
    /**
     * <获取生成excel字段名称的基类函数>
     * @return  获取生成excel字段名称
     * @see [类、类#方法、类#成员]
     */
    public List<String> getColName()
    {
        return null;
    }
    
    /**
     * <获取已经完成导出信息列表的基类函数>
     * @return         导出广告位信息列表
     * @see [类、类#方法、类#成员]
     */
    public List<ExportInfo> getReportExportTypeList()
    {
        return null;
    }
    
    /**
     * <遍历查询结果集，添加入列表>
     * @param context      系统上下文
     * @param rexportList 输出列表
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    private void getReportExportInfo(MethodContext context, List<ExportInfo> rexportList)
    {
        
        Map<String, Object> results = context.getResults();
        //存储过程返回的列表
        
        List<Map<String, Object>> list = (List<Map<String, Object>>)results.get(ExcelColName.MEM_RESULT);
        
        //列表为空，直接返回
        if (null == list || list.isEmpty())
        {
            return;
        }
        
        //遍历查询结果集，添加入列表
        for (Map<String, Object> objMap : list)
        {
            addReportExportList(rexportList, objMap);
        }
    }
    
    /**
     * <获取一条记录>
     * @param r_exportList  存放日志记录对象的队列       
     * @param objMap         一条日志记录的kv结构
     * @see [类、类#方法、类#成员]
     */
    private void addReportExportList(List<ExportInfo> rexportList, Map<String, Object> objMap)
    {
        //参数校验
        if (null == rexportList || null == objMap || objMap.isEmpty())
        {
            return;
        }
        
        addSingleExportInfo(objMap, rexportList);
        
    }
    
    /**
     * <获取一条记录的基类函数>
     * @param objMap        一条日志记录的kv结构  
     * @param r_exportList  存放日志记录对象的队列
     * @see [类、类#方法、类#成员]
     */
    public void addSingleExportInfo(Map<String, Object> objMap, List<ExportInfo> rexportList)
    {
        return;
    }
    
}
