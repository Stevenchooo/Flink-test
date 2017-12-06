/*
 * 文 件 名:  BasicReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2008-2010,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00166278
 * 创建时间:  2011-3-25
 */
package com.huawei.devicecloud.platform.bi.common;


/**
 *  请求对象基类
 * @author l00166278
 *
 */
public class BaseRequestCommon
{
    /**
     * 协议版本号
     */
    private String version;
    
    /**
     * 交易流水号
     */
    private String transactionId;
    
    /**
     * 跟踪标志
     * 0不启动跟踪  1启动跟踪
     */
    private String traceFlag;
    
//    /**
//     * <默认构造函数>
//     */
//    public BaseRequest()
//    {
//    }
    
    public String getVersion()
    {
        return version;
    }
    
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    public String getTransactionId()
    {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId)
    {
        this.transactionId = transactionId;
    }
    
    public String getTraceFlag()
    {
        return traceFlag;
    }
    
    public void setTraceFlag(String traceFlag)
    {
        this.traceFlag = traceFlag;
    }
    

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("BaseRequest [traceFlag=");
        builder.append(traceFlag);
        builder.append(", transactionID=");
        builder.append(transactionId);
        builder.append(", version=");
        builder.append(version);
        builder.append("]");
        return builder.toString();
    }
}
