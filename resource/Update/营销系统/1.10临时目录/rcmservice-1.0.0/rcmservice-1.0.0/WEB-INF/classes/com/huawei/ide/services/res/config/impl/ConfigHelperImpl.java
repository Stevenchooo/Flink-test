/*
s * 文 件 名:  ConfigHelperImpl.java
 * 版      权:  
 * 描      述:  <描述>
 * 修 改 人:  zhuyuqi
 * 修改时间: 2016年4月6日
 * 跟踪单号: <跟踪单号>
 * 修改单号: <修改单号>
 * 修改内容: <修改内容>
 */
package com.huawei.ide.services.res.config.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.ide.daos.res.config.ConfigHelperDao;
import com.huawei.ide.services.res.config.ConfigHelper;

/**
 * 提供系统配置操作的接口
 * 
 * @author zhuyuqi
 * @version [版本号, 2016年4月6日]
 * @see [相关类/方法]
 * @since [2016年4月6日]
 */
@Service(value = "com.huawei.ide.services.res.config.impl.ConfigHelperImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
public class ConfigHelperImpl implements ConfigHelper
{
    @Resource(name = "com.huawei.ide.daos.res.config.impl.ConfigHelperDaoImpl")
    private ConfigHelperDao configHelperDao;
    
    /**
     * 获取配置值
     * @param configName configName
     * @return  String
     */
    @Override
    public String getConfigValue(String configName)
    {
        return configHelperDao.queryValueByName(configName);
    }
    
}
