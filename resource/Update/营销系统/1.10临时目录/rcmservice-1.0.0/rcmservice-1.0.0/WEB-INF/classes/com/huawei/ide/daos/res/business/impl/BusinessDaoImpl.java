/*
 * 文 件 名:  BusinessDaoImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.daos.res.business.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.huawei.ide.beans.res.business.Business;
import com.huawei.ide.daos.res.business.BusinessDao;
import com.mysql.jdbc.Statement;

/**
 * 业务规则对象数据库操作实现类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Repository(value = "com.huawei.ide.daos.res.business.impl.BusinessDaoImpl")
public class BusinessDaoImpl extends JdbcDaoSupport implements BusinessDao
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessDaoImpl.class);
    
    @Autowired
    private DataSource dataSource;
    
    /**
     * 初始化
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    @PostConstruct
    public void initialize()
    {
        setDataSource(dataSource);
    }
    
    /**
     * 
     * 业务规则
     * <功能详细描述>
     * 
     * @author  cWX306007
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
     * @see  [相关类/方法]
     */
    private class BusinessRowMapper implements RowMapper<Business>
    {
        @Override
        public Business mapRow(ResultSet resultSet, int rowNum)
            throws SQLException
        {
            Business business = new Business();
            business.setId(resultSet.getInt("id"));
            business.setName(resultSet.getString("name"));
            business.setPublished(resultSet.getString("published"));
            return business;
        }
    }
    
    /**
     * 新建业务规则
     * @param business
     *        business
     */
    @Override
    public void createBusiness(Business business)
    {
        String sql = "INSERT INTO T_BUSINESS(name, published) VALUES(?, ?)";
        this.getJdbcTemplate().update(sql, new Object[] {business.getName(), business.getPublished()});
    }
    
    /**
     * 新建业务规则并返回id
     * @param business
     *        business
     * @return   int
     */
    @Override
    public int createBusinessReturnId(final Business business)
    {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        final String sql = "INSERT INTO T_BUSINESS(name, published) VALUES(?, ?)";
        PreparedStatementCreator creator = new PreparedStatementCreator()
        {
            private PreparedStatement ps = null;
            
          /*  @Override
            protected void finalize()
                throws Throwable
            {
                if (null != ps)
                {
                    ps.close();
                }
                super.finalize();
            }*/
            
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                throws SQLException
            {
                if (null == connection || connection.isClosed())
                {
                    return null;
                }
                /*try
                {*/
                    ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, business.getName());
                    ps.setString(2, business.getPublished());
                    /* }
                catch (SQLException e)
                {
                    if (null != ps)
                    {
                        try
                        {
                            ps.close();
                        }
                        catch (SQLException e1)
                        {
                            LOGGER.error(e1.getMessage(), e1);
                        }
                    }
                    throw e;
                }*/
                return ps;
            }
        };
        this.getJdbcTemplate().update(creator, keyHolder);
        return keyHolder.getKey().intValue();
    }
    
    /**
     * 更新业务规则
     * @param id
     *        id
     * @param business
     *        business
     */
    @Override
    public void updateBusiness(int id, Business business)
    {
        String sql = "UPDATE T_BUSINESS SET name=?,published=? WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {business.getName(), business.getPublished(), id});
    }
    
    /**
     * 删除业务规则
     * @param id
     *        id
     */
    @Override
    public void deleteBusiness(int id)
    {
        String sql = "DELETE FROM T_BUSINESS WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {id});
    }
    
    /**
     * 根据id查询业务规则
     * @param id
     *        id
     * @return  Business
     */
    @Override
    public Business queryBusiness(int id)
    {
        String sql = "SELECT * FROM T_BUSINESS WHERE id=?";
        Business business = this.getJdbcTemplate().queryForObject(sql, new Object[] {id}, new BusinessRowMapper());
        return business != null ? business : null;
    }
    
    /**
     * 根据名字查询业务规则
     * @param name
     *       name
     * @return  Business
     */
    @Override
    public Business queryBusinessByName(String name)
    {
        String sql = "SELECT * FROM T_BUSINESS WHERE name=?";
        Business business = this.getJdbcTemplate().queryForObject(sql, new Object[] {name}, new BusinessRowMapper());
        return business != null ? business : null;
    }
    
    /**
     * 查询所有的业务规则
     * @return  List<Business>
     */
    @Override
    public List<Business> queryAllBusinesses()
    {
        String sql = "SELECT * FROM T_BUSINESS ORDER BY ID";
        List<Business> businesses = this.getJdbcTemplate().query(sql, new BusinessRowMapper());
        return (businesses != null && !businesses.isEmpty()) ? businesses : null;
    }
    
}
