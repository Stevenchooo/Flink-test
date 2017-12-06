/*
 * 文 件 名:  ConnInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  连接信息
 * 创 建 人:  z00190465
 * 创建时间:  2013-3-4
 */
package com.huawei.devicecloud.platform.bi.metasync.conn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.metasync.util.ReadConfig;
import com.huawei.termcloud.uniaccount.crypt.CryptUtil;

/**
 * 连接信息
 * 
 * @author  z00190554
 * @version [Internet Business Service Platform SP V100R100, 2013-2-8]
 */
public class ConnInfo
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ConnInfo.class);
    
    private static final String KEK_WORD = "PkmJygVfrDxsDeeD";
    
    private String driver;
    
    private String sourceUrl;
    
    private String sourceUser;
    
    private String sourcePw;
    
    private String destUrl;
    
    private String destUser;
    
    private String destPw;
    
    private String dataPath;
    
    public String getDriver()
    {
        return driver;
    }
    
    public void setDriver(String driver)
    {
        this.driver = driver;
    }
    
    public String getSourceUser()
    {
        return sourceUser;
    }
    
    public void setSourceUser(String sourceUser)
    {
        this.sourceUser = sourceUser;
    }
    
    public String getSourcePw()
    {
        return sourcePw;
    }
    
    public void setSourcePw(String sourcePw)
    {
        this.sourcePw = sourcePw;
    }
    
    public String getDestUser()
    {
        return destUser;
    }
    
    public void setDestUser(String destUser)
    {
        this.destUser = destUser;
    }
    
    public String getDestPw()
    {
        return destPw;
    }
    
    public void setDestPw(String destPw)
    {
        this.destPw = destPw;
    }
    
    public String getDestUrl()
    {
        return destUrl;
    }
    
    public void setDestUrl(String destUrl)
    {
        this.destUrl = destUrl;
    }
    
    public String getSourceUrl()
    {
        return sourceUrl;
    }
    
    public void setSourceUrl(String sourceUrl)
    {
        this.sourceUrl = sourceUrl;
    }
    
    public String getDataPath()
    {
        return dataPath;
    }
    
    public void setDataPath(String dataPath)
    {
        this.dataPath = dataPath;
    }
    
    /**
     * 获取连接
     */
    public void getConnInfo()
    {
        LOGGER.info("read conn info.");
        setDriver(ReadConfig.get("mysql.driver"));
        setSourceUrl(ReadConfig.get("source.url"));
        setSourceUser(ReadConfig.get("source.user"));
        setSourcePw(CryptUtil.decryptForAESStr(ReadConfig.get("source.pw"), KEK_WORD));
        
        setDestUrl(ReadConfig.get("dest.url"));
        setDestUser(ReadConfig.get("dest.user"));
        setDestPw(CryptUtil.decryptForAESStr(ReadConfig.get("dest.pw"), KEK_WORD));
    }
}
