/*
 * 文 件 名:  OdpConfig.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  ODP的配置文件类
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-1
 */
package com.huawei.devicecloud.platform.bi.odp.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.devicecloud.platform.bi.common.utils.CommonUtils;

/**
 * ODP的配置文件类
 * 
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-1]
 */
public final class OdpConfig
{
    /**
     * 提出用户标识值的默认模式
     */
    private static final String DEFAULT_UAF_PATTERN =
        "(?<=user_ad_flag\\s{0,50}\\=\\s{0,50}?)[0-2]\\D|(?<=user_ad_flag\\s{0,50}\\=\\s{0,50}?)[0-2]$";
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OdpConfig.class);
    
    /**
     * limit修正的默认值
     */
    private static final Long DEFAULT_LIMIT_FIXED = 10000L;
    
    private static OdpConfig config;
    
    /**
     * 集群Id
     */
    private Integer clusterId = loadClusterId();
    
    /**
     * 是否启用HTTP头校验
     */
    private boolean enableAuthen = loadEnableAuthen();
    
    /**
     * 记录文件目录
     */
    private String rfilesDir = loadRfilesDir();
    
    /**
     * 是否在启动时初始化流控值
     */
    private boolean initLoadCtrlValue = loadLoadCtrlValue();
    
    /**
     * 提取用户标识的正则表达式
     */
    private String userAdFlagPattern = loadUserAdFlagPat();
    
    /**
     * 用户信息表表名字
     */
    private String upTable = "user_profile_table";
    
    /**
     * mappe文件中的结果集映射Id
     */
    private String resultMapID = "userProfileMap";
    
    /**
     * limit修正阈值
     */
    private Long limitFixedThres = loadLimitFixedThres();
    
    /**
     * 默认构造函数
     */
    private OdpConfig()
    {
        
    }
    
    public Long getLimitFixedThres()
    {
        return limitFixedThres;
    }



    public void setLimitFixedThres(Long limitFixedThres)
    {
        this.limitFixedThres = limitFixedThres;
    }

    public String getUpTable()
    {
        return upTable;
    }
    
    public void setUpTable(String upTable)
    {
        this.upTable = upTable;
    }
    
    public String getResultMapID()
    {
        return resultMapID;
    }
    
    public void setResultMapID(String resultMapID)
    {
        this.resultMapID = resultMapID;
    }
    
    public String getUserAdFlagPattern()
    {
        return userAdFlagPattern;
    }
    
    public void setUserAdFlagPattern(String userAdFlagPattern)
    {
        this.userAdFlagPattern = userAdFlagPattern;
    }
    
    public String getRfilesDir()
    {
        return rfilesDir;
    }
    
    public void setRfilesDir(String rfilesDir)
    {
        this.rfilesDir = rfilesDir;
    }
    
    /**
     * limit修正阈值
     */
    private Long loadLimitFixedThres()
    {
        try
        {
            return Long.parseLong(CommonUtils.getSysConfig("limit.fixedThres"));
        }
        catch (Exception e)
        {
            LOGGER.error("limit.fixedThres(eg '1000') is not correct!", e);
            LOGGER.warn("beacuse no limit.fixedThres item do be found in config file"
                + ", limit.fixedThres is set to {}", DEFAULT_LIMIT_FIXED);
            return DEFAULT_LIMIT_FIXED;
        }
    }
    
    /**
     * 加载集群Id配置项
     */
    private String loadUserAdFlagPat()
    {
        try
        {
            String pat = CommonUtils.getSysConfig("userAdFlag.pattern");
            return null == pat ? DEFAULT_UAF_PATTERN : pat;
        }
        catch (Exception e)
        {
            LOGGER.error("userAdFlag.pattern(eg '(?<=user_ad_flag\\s{0,50}\\=\\s{0,50}?)[0-2]\\D"
                + "|(?<=user_ad_flag\\s{0,50}\\=\\s{0,50}?)[0-2]$') is not correct!", e);
            LOGGER.warn("beacuse no userAdFlag.pattern do be found in config file"
                + ", userAdFlag.pattern is set to {}", DEFAULT_UAF_PATTERN);
            return DEFAULT_UAF_PATTERN;
        }
    }
    
    /**
     * 加载集群Id配置项
     */
    private int loadClusterId()
    {
        try
        {
            return Integer.parseInt(CommonUtils.getSysConfig("cluster.id"));
        }
        catch (Exception e)
        {
            LOGGER.error("cluster.id(eg '1') is not correct!", e);
            LOGGER.warn("beacuse no cluster.id item do be found in config file" + ", cluster.id is set to {}", 1);
            return 1;
        }
    }
    
    /**
     * 是否启用鉴权(默认启用)
     */
    private boolean loadEnableAuthen()
    {
        try
        {
            return "1".equals(CommonUtils.getSysConfig("authen.switch"));
        }
        catch (Exception e)
        {
            LOGGER.error("authen.switch(eg '1') is not correct!", e);
            LOGGER.warn("beacuse no authen.switch item do be found in config file" + ", authen.switch is set to {}", 1);
            return true;
        }
    }
    
    /**
     * 是否启用鉴权(默认启用)
     */
    private boolean loadLoadCtrlValue()
    {
        try
        {
            return "1".equals(CommonUtils.getSysConfig("initloadctrl.switch"));
        }
        catch (Exception e)
        {
            LOGGER.error("initloadctrl.switch(eg '1') is not correct!", e);
            LOGGER.warn("beacuse no initloadctrl.switch item do be found in config file"
                + ", initloadctrl.switch is set to {}", 1);
            return true;
        }
    }
    
    /**
     * 加载
     */
    private String loadRfilesDir()
    {
        try
        {
            return CommonUtils.getSysConfig("rfiles.dir");
        }
        catch (Exception e)
        {
            LOGGER.error("rfiles.dir(eg '/home/user/rfiles/') is not correct!", e);
            LOGGER.warn("beacuse no rfiles.dir item do be found in config file" + ", rfiles.dir is set to \"\"");
            return "";
        }
    }
    
    public boolean isInitLoadCtrlValue()
    {
        return initLoadCtrlValue;
    }
    
    public void setInitLoadCtrlValue(boolean initLoadCtrlValue)
    {
        this.initLoadCtrlValue = initLoadCtrlValue;
    }
    
    /**
     * 创建单例对象
     * @return odp配置
     */
    public static OdpConfig createOdpConfig()
    {
        if (null == config)
        {
            config = new OdpConfig();
        }
        
        return config;
    }
    
    public Integer getClusterId()
    {
        return clusterId;
    }
    
    public void setClusterId(Integer clusterId)
    {
        this.clusterId = clusterId;
    }
    
    public boolean isEnableAuthen()
    {
        return enableAuthen;
    }
    
    public void setEnableAuthen(boolean enableAuthen)
    {
        this.enableAuthen = enableAuthen;
    }
}
