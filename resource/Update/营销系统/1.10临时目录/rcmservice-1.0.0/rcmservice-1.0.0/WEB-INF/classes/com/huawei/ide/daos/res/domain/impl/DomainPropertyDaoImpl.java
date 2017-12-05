/*
 * 文 件 名:  DomainPropertyDaoImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月23日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.daos.res.domain.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.huawei.ide.beans.res.domain.DomainProperty;
import com.huawei.ide.daos.res.domain.DomainPropertyDao;

/**
 * 领域对象属性数据库操作实现类
 * 
 * @author z00219375
 * @version [版本号, 2015年12月23日]
 * @see [相关类/方法]
 * @since [Consumer Cloud Big Data Platform Dept]
 */
@Repository(value = "com.huawei.ide.daos.res.domain.impl.DomainPropertyDaoImpl")
public class DomainPropertyDaoImpl extends JdbcDaoSupport implements DomainPropertyDao
{
    @Autowired
    private DataSource dataSource;
    
    /**
     * 初始化
     */
    @PostConstruct
    public void initialize()
    {
        setDataSource(dataSource);
    }
    
    /**
     * 查询结果类
     * @author zWX301264
     *
     */
    private class DomainPropertyRowMapper implements RowMapper<DomainProperty>
    {
        @Override
        public DomainProperty mapRow(ResultSet resultSet, int rowNum)
            throws SQLException
        {
            DomainProperty domainProperty = new DomainProperty();
            domainProperty.setId(resultSet.getInt("id"));
            domainProperty.setDomainId(Integer.parseInt(resultSet.getString("domain_id")));
            domainProperty.setName(resultSet.getString("name"));
            domainProperty.setCategory(resultSet.getString("category"));
            domainProperty.setDefaultVal(resultSet.getString("default_val"));
            return domainProperty;
        }
    }
    
    /**
     * 新建领域对象属性
     * @param domainProperty domainProperty
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void createDomainProperty(DomainProperty domainProperty)
    {
        String sql = "INSERT INTO T_DOMAIN_PROPERTY(domain_id, name, category, default_val) VALUES(?, ?, ?, ?)";
        this.getJdbcTemplate().update(sql,
            new Object[] {domainProperty.getDomainId(), domainProperty.getName(), domainProperty.getCategory(),
                domainProperty.getDefaultVal()});
    }
    
    /**
     * 更新领域对象属性
     * @param id id
     * @param domainProperty domainProperty
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void updateDomainProperty(int id, DomainProperty domainProperty)
    {
        String sql = "UPDATE T_DOMAIN_PROPERTY SET domain_id=?,name=?,category=?,default_val=? WHERE id=?";
        this.getJdbcTemplate().update(sql,
            new Object[] {domainProperty.getDomainId(), domainProperty.getName(), domainProperty.getCategory(),
                domainProperty.getDefaultVal(), id});
    }
    
    /**
     * 删除领域对象属性
     * @param id id
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteDomainProperty(int id)
    {
        String sql = "DELETE FROM T_DOMAIN_PROPERTY WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {id});
    }
    
    /**
     * 根据外键domainId
     * 删除领域对象属性
     * @param domainId domainId
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteDomainPropertyByDomainId(int domainId)
    {
        String sql = "DELETE FROM T_DOMAIN_PROPERTY WHERE domain_id=?";
        this.getJdbcTemplate().update(sql, new Object[] {domainId});
    }
    
    /**
     * 查询领域对象属性
     * @param id id
     * @return DomainProperty DomainProperty
     * @see [类、类#方法、类#成员]
     */
    @Override
    public DomainProperty queryDomainProperty(int id)
    {
        String sql = "SELECT * FROM T_DOMAIN_PROPERTY WHERE id=?";
        DomainProperty domainProperty =
            this.getJdbcTemplate().queryForObject(sql, new Object[] {id}, new DomainPropertyRowMapper());
        return domainProperty != null ? domainProperty : null;
    }
    
    /**
     * 查询所有领域对象属性
     * @return List<DomainProperty> DomainProperty
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<DomainProperty> queryAllDomainPropertys()
    {
        String sql = "SELECT * FROM T_DOMAIN_PROPERTY";
        List<DomainProperty> domainProperties = this.getJdbcTemplate().query(sql, new DomainPropertyRowMapper());
        return (domainProperties != null && !domainProperties.isEmpty()) ? domainProperties : null;
    }
    
    /**
     * 查询指定domainId对应的所有DomainPropertys
     * @param domainId domainId
     * @return List<DomainProperty> DomainProperty
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<DomainProperty> queryDomainPropertysByDomainId(int domainId)
    {
        String sql = "SELECT * FROM T_DOMAIN_PROPERTY WHERE domain_id=?";
        List<DomainProperty> domainPropertys =
            this.getJdbcTemplate().query(sql, new Object[] {domainId}, new DomainPropertyRowMapper());
        return domainPropertys != null ? domainPropertys : null;
    }
    
}
