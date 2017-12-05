package com.huawei.manager.mkt.info;

/**
 * <一句话功能简述>
 * 
 * <功能详细描述>
 * 
 * @author  s00359263
 * @version [Internet Business Service Platform SP V100R100, 2015-11-23]
 * @see  [相关类/方法]
 *  
 */
public class LogExportInfo extends ExportInfo
{
    private String operTime;
    
    private String operator;
    
    private String operRes;
    
    private String opeRequest;
    
    private String opeRrsponse;
    
    public String getOperTime()
    {
        return operTime;
    }
    
    public void setOperTime(String operTime)
    {
        this.operTime = operTime;
    }
    
    public String getOperator()
    {
        return operator;
    }
    
    public void setOperator(String operator)
    {
        this.operator = operator;
    }
    
    public String getOperRes()
    {
        return operRes;
    }
    
    public void setOperRes(String operRes)
    {
        this.operRes = operRes;
    }
    
    public String getOpeRequest()
    {
        return opeRequest;
    }
    
    public void setOpeRequest(String opeRequest)
    {
        this.opeRequest = opeRequest;
    }
    
    public String getOpeRrsponse()
    {
        return opeRrsponse;
    }
    
    public void setOpeRrsponse(String opeRrsponse)
    {
        this.opeRrsponse = opeRrsponse;
    }
    
    @Override
    public String toString()
    {
        return "LogExportInfo [operTime=" + operTime + ", operator=" + operator + ", operRes=" + operRes
            + ", opeRequest=" + opeRequest + ", opeRrsponse=" + opeRrsponse + "]";
    }
    
}
