/*
 * 文 件 名:  DomainDaoImpl.java
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

import com.huawei.ide.beans.res.domain.Domain;
import com.huawei.ide.daos.res.domain.DomainDao;

/**
 * 领域对象数据库操作实现类
 * @author  z00219375
 * @version  [版本号, 2015年12月23日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Repository(value = "com.huawei.ide.daos.res.domain.impl.DomainDaoImpl")
public class DomainDaoImpl extends JdbcDaoSupport implements DomainDao
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
     * 
     * 查询结果类
     */
    private class DomainRowMapper implements RowMapper<Domain>
    {
        @Override
        public Domain mapRow(ResultSet resultSet, int rowNum)
            throws SQLException
        {
            Domain domain = new Domain();
            domain.setId(resultSet.getInt("id"));
            domain.setName(resultSet.getString("name"));
            domain.setPublished(resultSet.getString("published"));
            return domain;
        }
    }
    
    /**
     * 创建领域对象
     * @param domain domain
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void createDomain(Domain domain)
    {
        String sql = "INSERT INTO T_DOMAIN(name, published) VALUES(?, ?)";
        this.getJdbcTemplate().update(sql, new Object[] {domain.getName(), domain.getPublished()});
    }
    
    /**
     * 更新领域对象
     * @param id id
     * @param domain domain
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void updateDomain(int id, Domain domain)
    {
        String sql = "UPDATE T_DOMAIN SET name=?,published=? WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {domain.getName(), domain.getPublished(), id});
    }
    
    /**
     * 删除领域对象
     * @param id id
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteDomain(int id)
    {
        String sql = "DELETE FROM T_DOMAIN WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {id});
    }
    
    /**
     * 查询指定领域对象
     * @param id id
     * @return Domain domain
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Domain queryDomain(int id)
    {
        String sql = "SELECT * FROM T_DOMAIN WHERE id=?";
        Domain domain = this.getJdbcTemplate().queryForObject(sql, new Object[] {id}, new DomainRowMapper());
        return domain != null ? domain : null;
    }
    
    /**
     * 查询所有领域对象
     * @return List<Domain> domain
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Domain> queryAllDomains()
    {
        String sql = "SELECT * FROM T_DOMAIN ORDER BY ID";
        List<Domain> domains = this.getJdbcTemplate().query(sql, new DomainRowMapper());
        return (domains != null && !domains.isEmpty()) ? domains : null;
    }
    
    /**
     * 查询domain领域对象总数
     * @return int domain领域对象总数
     * @see [类、类#方法、类#成员]
     */
    @Override
    public int queryDomainTotalNum()
    {
        String sql = "SELECT COUNT(*) FROM T_DOMAIN";
        return this.getJdbcTemplate().queryForObject(sql, Integer.class);
    }
    
    /**
     * 分页查询指定domain领域对象
     * @param index  分页查询指定索引，从0开始
     * @param pageSize  分页查询指定的页大小
     * @return List<Domain> domain
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Domain> queryDomainsByPage(int index, int pageSize)
    {
        if (index < 0)
        {
            return null;
        }
        String sql = "SELECT * FROM T_DOMAIN ORDER BY ID LIMIT ?,?";
        List<Domain> domains = this.getJdbcTemplate().query(sql, new Object[] {index, pageSize}, new DomainRowMapper());
        return (domains != null && !domains.isEmpty()) ? domains : null;
    }
    
    /**
     * 查询所有已发布的Domains
     * @return List<Domain> domain
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Domain> queryAllPublishedDomains()
    {
        String sql = "SELECT * FROM T_DOMAIN WHERE published='true' ORDER BY ID";
        List<Domain> domains = this.getJdbcTemplate().query(sql, new DomainRowMapper());
        return (domains != null && !domains.isEmpty()) ? domains : null;
    }
    
}
