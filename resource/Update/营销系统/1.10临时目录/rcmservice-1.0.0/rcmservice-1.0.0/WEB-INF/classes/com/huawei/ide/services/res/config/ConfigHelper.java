/*
 * 文 件 名:  ConfigHelper.java
 * 版      权:  
 * 描      述:  <描述>
 * 修 改 人:  zhuyuqi
 * 修改时间: 2016年4月6日
 * 跟踪单号: <跟踪单号>
 * 修改单号: <修改单号>
 * 修改内容: <修改内容>
 */
package com.huawei.ide.services.res.config;

/**
 * 提供系统配置操作的接口
 * 
 * @author zhuyuqi
 * @version [版本号, 2016年4月6日]
 * @see [相关类/方法]
 * @since [2016年4月6日]
 */
public interface ConfigHelper
{
    /**
     * 根据配置名称获取配置值，当存在相同配置名称时取级别高的配置值
     * 
     * @param configName
     *            configName
     * @return String String
     */
    public String getConfigValue(String configName);

}
