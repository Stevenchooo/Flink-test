package com.huawei.manager.mkt.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.huawei.manager.base.run.process.AuthRDBProcessor;
import com.huawei.manager.mkt.constant.ExcelColName;
import com.huawei.manager.mkt.info.FreqExportInfo;
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
public class FreqTranform extends AuthRDBProcessor
{
    
    /**
     * 处理后
     * @param context          系统上下文
     * @param conn             数据库连接
     * @return                 是否成功
     * @throws SQLException    sql异常
     */
    @SuppressWarnings("unchecked")
    @Override
    protected int afterProcess(MethodContext context, DBConnection conn)
        throws SQLException
    {
        //数据查询结果
        Map<String, Object> results = context.getResults();
        
        List<Map<String, Object>> list = (List<Map<String, Object>>)results.get(ExcelColName.MEM_TABLELIST);
        //展示的最大值
        Integer selectMax = Integer.valueOf((String)context.getParameter(ExcelColName.MEM_PSALECTMAX));
        List<FreqExportInfo> listFreq = getFreqExportInfo(list, selectMax);
        
        context.setResult(ExcelColName.MEM_TABLELISTFK, listFreq);
        
        return RetCode.OK;
    }
    
    /**
     * 处理后
     * @param list          系统上下文
     * @param selectMax     频次数
     * @return              数据集
     */
    public static List<FreqExportInfo> getFreqExportInfo(List<Map<String, Object>> list, Integer selectMax)
    {
        if (null == list || list.isEmpty())
        {
            return null;
        }
        
        List<FreqExportInfo> freqList = new ArrayList<FreqExportInfo>();
        
        //遍历查询结果集，添加入列表
        String oldfreqName = "";
        FreqExportInfo oldFreq = new FreqExportInfo();
        Integer[] freqNum = new Integer[selectMax];
        
        for (int i = 0; i < selectMax; i++)
        {
            freqNum[i] = 0;
        }
        Integer freqIndex = 0;
        for (Map<String, Object> objMap : list)
        {
            freqIndex = 0;
            if (!oldfreqName.equals((String)objMap.get(ExcelColName.MEM_MEDIANAME)))
            {
                if (!"".equals(oldfreqName))
                {
                    oldFreq.setUserList(Arrays.asList(freqNum));
                    freqList.add(oldFreq);
                    freqNum = new Integer[selectMax];
                    for (int i = 0; i < selectMax; i++)
                    {
                        freqNum[i] = 0;
                    }
                    oldFreq = new FreqExportInfo();
                }
                
                oldfreqName = (String)objMap.get(ExcelColName.MEM_MEDIANAME);
                freqIndex = (Integer)objMap.get(ExcelColName.MEM_FREQUENCY);
                if (freqIndex <= 0 || freqIndex > selectMax)
                {
                	oldFreq.setMediaName(oldfreqName);
                    continue;
                }
                freqNum[freqIndex - 1] = (Integer)objMap.get(ExcelColName.MEM_FK);
                oldFreq.setMediaName(oldfreqName);
            }
            else
            {
                oldFreq.setMediaName((String)objMap.get(ExcelColName.MEM_MEDIANAME));
                freqIndex = (Integer)objMap.get(ExcelColName.MEM_FREQUENCY);
                if (freqIndex <= 0 || freqIndex > selectMax)
                {
                    continue;
                }
                freqNum[freqIndex - 1] = (Integer)objMap.get(ExcelColName.MEM_FK);
            }
        }
        oldFreq.setUserList(Arrays.asList(freqNum));
        freqList.add(oldFreq);
        
        return freqList;
    }
    
}
