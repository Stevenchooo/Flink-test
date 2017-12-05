/*
 * 文 件 名:  ConfigServiceImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.services.res.config.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.ide.beans.res.config.Config;
import com.huawei.ide.daos.res.config.ConfigDao;
import com.huawei.ide.services.res.config.ConfigService;

/**
 * 系统配置对象数据库服务实现类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Service(value = "com.huawei.ide.services.res.config.impl.ConfigServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
public class ConfigServiceImpl implements ConfigService
{
    @Resource(name = "com.huawei.ide.daos.res.config.impl.ConfigDaoImpl")
    private ConfigDao configDao;
    
    /**
     * 创建系统配置对象
     * @param config config
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void createConfig(Config config)
    {
        configDao.createConfig(config);
    }
    
    /**
     * 更新系统配置对象
     * @param id id
     * @param config config
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void updateConfig(int id, Config config)
    {
        configDao.updateConfig(id, config);
    }
    
    /**
     * 删除系统配置对象
     * @param id id
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteConfig(int id)
    {
        configDao.deleteConfig(id);
    }
    
    /**
     * 查询指定系统配置对象
     * @param id id
     * @return Config Config
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Config queryConfig(int id)
    {
        return configDao.queryConfig(id);
    }
    
    /**
     * 查询所有系统配置对象
     * @return List<Config> List<Config>
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Config> queryConfigs()
    {
        return configDao.queryConfigs();
    }
    
    /**
     * 查询config对象总数
     * @return int int
     * @see [类、类#方法、类#成员]
     */
    @Override
    public int queryConfigTotalNum()
    {
        return configDao.queryConfigTotalNum();
    }
    
    /**
     * 分页查询指定config对象
     * @param index
     * 分页查询指定索引，从0开始
     * @param pageSize
     * 分页查询指定的页大小
     * @return List<Config> List<Config>
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Config> queryConfigsByPage(int index, int pageSize)
    {
        return configDao.queryConfigsByPage(index, pageSize);
    }
    
}
