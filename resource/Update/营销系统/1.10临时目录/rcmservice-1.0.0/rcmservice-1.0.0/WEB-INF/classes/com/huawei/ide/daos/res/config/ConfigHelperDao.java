package com.huawei.ide.daos.res.config;


/**
 * 系统配置对象数据库操作类
 * 
 * @author zWX301264
 * 
 */
public interface ConfigHelperDao
{
    /**
     * 根据配置名称获取配置值，当存在相同配置名称时取级别高的配置值，level越小级别越高
     * 
     * @param configName
     *            configName
     * @return String String
     */
    String queryValueByName(String configName);
}
