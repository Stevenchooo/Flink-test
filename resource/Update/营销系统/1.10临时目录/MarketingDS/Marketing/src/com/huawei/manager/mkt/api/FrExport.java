package com.huawei.manager.mkt.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.mkt.constant.ExcelColName;
import com.huawei.manager.mkt.dao.FrExportDao;
import com.huawei.manager.mkt.info.FreqExportInfo;
import com.huawei.manager.mkt.util.ExcelUtils;
import com.huawei.manager.mkt.util.FileUtils;
import com.huawei.manager.utils.Constant;
import com.huawei.util.JsonUtil;
import com.huawei.util.LogUtil;
import com.huawei.util.DBUtil.DBConnection;
import com.huawei.waf.core.run.MethodContext;
import com.huawei.waf.protocol.RetCode;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  s00359263
 * @version [Internet Business Service Platform SP V100R100, 2016年3月8日]
 * @see  [相关类/方法]
 */

public class FrExport extends AuthRDBProcessor
{
    //日志
    private static final Logger LOG = LogUtil.getInstance();
    
    /**
     * 接口处理后如何处理
     * @param context   系统上下文
     * @param dbConn    数据库连接
     * @return          是否成功
     */
    public int process(MethodContext context, DBConnection dbConn)
    {
        LOG.debug("enter FrExport");
        FrExportDao.setContext(context, dbConn);
        Map<String, Object> reqParams = context.getParameters();
        int reportSelectMax = Integer.parseInt(JsonUtil.getAsStr(reqParams, ExcelColName.MEM_REPORTSELECTMAX));
        List<FreqExportInfo> tranRs = transResults(context, reportSelectMax);
        
        //动态分配的文件路径
        String filePath = FrExport.class.getResource(Constant.SLANT).getPath() + Constant.SUBEXCELPATH
            + System.currentTimeMillis() + ".xls";
            
        //生成临时文件
        ExcelUtils.writeFrExportFile(filePath, tranRs, reportSelectMax);
        
        FileUtils.writeHttpFileResponse(context, filePath);
        
        return RetCode.OK;
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param context 系统上下文
     * @param reportSelectMax2 最大选择数
     * @return
     * @see [类、类#方法、类#成员]
     */
    @SuppressWarnings("unchecked")
    private List<FreqExportInfo> transResults(MethodContext context, int reportSelectMax)
    {
        
        Map<String, Object> results = context.getResults();
        //存储过程返回的列表
        List<Map<String, Object>> list = (List<Map<String, Object>>)results.get(ExcelColName.MEM_RESULT);
        
        //列表为空，直接返回
        List<FreqExportInfo> freqList = getFreqExportInfo(list, reportSelectMax);
        if (freqList == null)
        {
            freqList = new ArrayList<FreqExportInfo>();
            return freqList;
        }
        
        return freqList;
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param frlist
     * @param reportSelectMax
     * @return
     * @see [类、类#方法、类#成员]
     */
    private List<FreqExportInfo> getFreqExportInfo(List<Map<String, Object>> frlist, int reportSelectMax)
    {
        if (null == frlist || frlist.isEmpty())
        {
            return null;
        }
        
        String oldfreqName = "";
        FreqExportInfo oldFreq = new FreqExportInfo();
        Integer[] freqNum = new Integer[reportSelectMax];
        List<FreqExportInfo> retfreqList = new ArrayList<FreqExportInfo>();
        
        for (int i = 0; i < reportSelectMax; i++)
        {
            freqNum[i] = 0;
        }
        for (Map<String, Object> objMap : frlist)
        {
            int freqIndex = 0;
            if (!oldfreqName.equals((String)objMap.get(ExcelColName.MEM_MEDIANAME)))
            {
                if (!"".equals(oldfreqName))
                {
                    oldFreq.setUserList(Arrays.asList(freqNum));
                    retfreqList.add(oldFreq);
                    freqNum = new Integer[reportSelectMax];
                    for (int i = 0; i < reportSelectMax; i++)
                    {
                        freqNum[i] = 0;
                    }
                    oldFreq = new FreqExportInfo();
                }
                
                oldfreqName = (String)objMap.get(ExcelColName.MEM_MEDIANAME);
                freqIndex = Integer.valueOf((String)objMap.get(ExcelColName.MEM_FREQUENCY));
                if (freqIndex <= 0 || freqIndex > reportSelectMax)
                {
                    continue;
                }
                freqNum[freqIndex - 1] = Integer.valueOf((String)objMap.get(ExcelColName.MEM_FK));
                oldFreq.setMediaName(oldfreqName);
            }
            else
            {
                oldFreq.setMediaName((String)objMap.get(ExcelColName.MEM_MEDIANAME));
                freqIndex = Integer.valueOf((String)objMap.get(ExcelColName.MEM_FREQUENCY));
                if (freqIndex <= 0 || freqIndex > reportSelectMax)
                {
                    continue;
                }
                freqNum[freqIndex - 1] = Integer.valueOf((String)objMap.get(ExcelColName.MEM_FK));
            }
        }
        oldFreq.setUserList(Arrays.asList(freqNum));
        retfreqList.add(oldFreq);
        
        return retfreqList;
    }
    
}
