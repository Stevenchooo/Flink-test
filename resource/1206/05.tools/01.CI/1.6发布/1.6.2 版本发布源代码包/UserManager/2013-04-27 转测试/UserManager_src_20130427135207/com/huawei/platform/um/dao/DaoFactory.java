/*
 * 文 件 名:  DynamicDaoFactory.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  dao工厂
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-6
 */
package com.huawei.platform.um.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.huawei.platform.um.dao.impl.UMDaoImpl;
import com.huawei.platform.um.datasource.DecryptDataSource;
import com.huawei.platform.um.entity.DBServerConfigEntity;

/**
 * dao工厂
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2013-2-6]
 */
public class DaoFactory
{
    private static final String CLASS_PATH = DaoFactory.class.getResource("/").getFile();
    
    private String configLocation;
    
    private BasicDataSource dsTemplate;
    
    /**
     * 获取新的dao对象
     * @param dbConfig db配置
     * @return 新的dao对象
     * @throws Exception 异常
     */
    public UMDao createDao(DBServerConfigEntity dbConfig) throws Exception
    {
        UMDaoImpl newDao = new UMDaoImpl();
        
        //会话工厂
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(newDatasSource(dbConfig));
        //设置资源
        
        Resource resource = new FileSystemResource(CLASS_PATH + configLocation);
        sqlSessionFactoryBean.setConfigLocation(resource);
        
        //设置会话工厂
        newDao.setSqlSessionFactory(sqlSessionFactoryBean.getObject());
        return newDao;
    }
    
    /**
     * 获取新的数据源
     * @param dbConfig db配置
     * @return 新的数据源
     */
    private BasicDataSource newDatasSource(DBServerConfigEntity dbConfig)
    {
        DecryptDataSource umDS = new DecryptDataSource();
        //复制数据源的配置
        umDS.setDriverClassName(dsTemplate.getDriverClassName());
        umDS.setUrl(String.format(dsTemplate.getUrl(),
            dbConfig.getIpAddress(),
            dbConfig.getPort(),
            dbConfig.getDbName()));
        umDS.setUsername(dbConfig.getUserName());
        umDS.setPassword(dbConfig.getPwd());
        umDS.setDefaultAutoCommit(dsTemplate.getDefaultAutoCommit());
        umDS.setDefaultReadOnly(dsTemplate.getDefaultReadOnly());
        umDS.setInitialSize(dsTemplate.getInitialSize());
        umDS.setMaxActive(dsTemplate.getMaxActive());
        umDS.setMaxIdle(dsTemplate.getMaxIdle());
        umDS.setMaxWait(dsTemplate.getMaxWait());
        umDS.setValidationQuery(dsTemplate.getValidationQuery());
        umDS.setTestWhileIdle(dsTemplate.getTestWhileIdle());
        umDS.setTimeBetweenEvictionRunsMillis(dsTemplate.getTimeBetweenEvictionRunsMillis());
        umDS.setMinEvictableIdleTimeMillis(dsTemplate.getMinEvictableIdleTimeMillis());
        umDS.setRemoveAbandoned(dsTemplate.getRemoveAbandoned());
        umDS.setRemoveAbandonedTimeout(dsTemplate.getRemoveAbandonedTimeout());
        umDS.setLogAbandoned(dsTemplate.getLogAbandoned());
        
        return umDS;
    }

    public String getConfigLocation()
    {
        return configLocation;
    }

    public void setConfigLocation(String configLocation)
    {
        this.configLocation = configLocation;
    }

    public BasicDataSource getDsTemplate()
    {
        return dsTemplate;
    }

    public void setDsTemplate(BasicDataSource dsTemplate)
    {
        this.dsTemplate = dsTemplate;
    }
}
