/*
 * 文 件 名:  DynamicDaoFactory.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  dao工厂
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-6
 */
package com.huawei.platform.tcc.dao;

import org.apache.commons.dbcp.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.huawei.platform.tcc.dao.impl.TccDaoImpl;
import com.huawei.platform.tcc.datasource.TccDataSource;
import com.huawei.platform.tcc.entity.DBServerConfigEntity;

/**
 * dao工厂
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-2-6]
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
    public TccDao createDao(DBServerConfigEntity dbConfig) throws Exception
    {
        TccDaoImpl newDao = new TccDaoImpl();
        
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
        TccDataSource tccDS = new TccDataSource();
        //复制数据源的配置
        tccDS.setDriverClassName(dsTemplate.getDriverClassName());
        tccDS.setUrl(String.format(dsTemplate.getUrl(),
            dbConfig.getIpAddress(),
            dbConfig.getPort(),
            dbConfig.getDbName()));
        tccDS.setUsername(dbConfig.getUserName());
        tccDS.setPassword(dbConfig.getPwd());
        tccDS.setDefaultAutoCommit(dsTemplate.getDefaultAutoCommit());
        tccDS.setDefaultReadOnly(dsTemplate.getDefaultReadOnly());
        tccDS.setInitialSize(dsTemplate.getInitialSize());
        tccDS.setMaxActive(dsTemplate.getMaxActive());
        tccDS.setMaxIdle(dsTemplate.getMaxIdle());
        tccDS.setMaxWait(dsTemplate.getMaxWait());
        tccDS.setValidationQuery(dsTemplate.getValidationQuery());
        tccDS.setTestWhileIdle(dsTemplate.getTestWhileIdle());
        tccDS.setTimeBetweenEvictionRunsMillis(dsTemplate.getTimeBetweenEvictionRunsMillis());
        tccDS.setMinEvictableIdleTimeMillis(dsTemplate.getMinEvictableIdleTimeMillis());
        tccDS.setRemoveAbandoned(dsTemplate.getRemoveAbandoned());
        tccDS.setRemoveAbandonedTimeout(dsTemplate.getRemoveAbandonedTimeout());
        tccDS.setLogAbandoned(dsTemplate.getLogAbandoned());
        
        return tccDS;
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
