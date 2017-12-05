/*
 * 文 件 名:  ConfigService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.services.res.config;

import java.util.List;

import com.huawei.ide.beans.res.config.Config;

/**
 * 系统配置对象数据库服务类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public interface ConfigService
{
    /**
     * 创建系统配置对象
     * @param config config
     * @see [类、类#方法、类#成员]
     */
    public void createConfig(Config config);
    
    /**
     * 更新系统配置对象
     * @param id id
     * @param config config
     * @see [类、类#方法、类#成员]
     */
    public void updateConfig(int id, Config config);
    
    /**
     * 删除系统配置对象
     * @param id id
     * @see [类、类#方法、类#成员]
     */
    public void deleteConfig(int id);
    
    /**
     * 查询指定系统配置对象
     * @param id id
     * @return Config Config
     * @see [类、类#方法、类#成员]
     */
    public Config queryConfig(int id);
    
    /**
     * 查询所有系统配置对象
     * @return List<Config> List<Config>
     * @see [类、类#方法、类#成员]
     */
    public List<Config> queryConfigs();
    
    /**
     * 查询config对象总数
     * @return int int
     * @see [类、类#方法、类#成员]
     */
    public int queryConfigTotalNum();
    
    /**
     * 分页查询指定config对象
     * @param index
     * 分页查询指定索引，从0开始
     * @param pageSize
     * 分页查询指定的页大小
     * @return List<Config> List<Config>
     * @see [类、类#方法、类#成员]
     */
    public List<Config> queryConfigsByPage(int index, int pageSize);
}
