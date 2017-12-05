/*
 * 文 件 名:  BusinessPackageDaoImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.daos.res.business.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.huawei.ide.beans.res.business.BusinessPackage;
import com.huawei.ide.daos.res.business.BusinessPackageDao;

/**
 * 业务规则对象所属package数据库操作实现类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Repository(value = "com.huawei.ide.daos.res.business.impl.BusinessPackageDaoImpl")
public class BusinessPackageDaoImpl extends JdbcDaoSupport implements BusinessPackageDao
{
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
     * 业务规则包
     * <功能详细描述>
     * 
     * @author  cWX306007
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
     * @see  [相关类/方法]
     */
    private class BusinessPackageRowMapper implements RowMapper<BusinessPackage>
    {
        @Override
        public BusinessPackage mapRow(ResultSet resultSet, int rowNum)
            throws SQLException
        {
            BusinessPackage businessPackage = new BusinessPackage();
            businessPackage.setId(resultSet.getInt("id"));
            businessPackage.setName(resultSet.getString("name"));
            return businessPackage;
        }
    }
    
    /**
     * 新建业务规则包
     * @param businessPackage
     *        businessPackage
     */
    @Override
    public void createBusinessPackage(BusinessPackage businessPackage)
    {
        String sql = "INSERT INTO T_BUSINESS_PACKAGE(name) VALUES(?)";
        this.getJdbcTemplate().update(sql, new Object[] {businessPackage.getName()});
    }
    
    /**
     * 更新业务规则包
     * @param id
     *        id
     * @param businessPackage
     *        businessPackage
     */
    @Override
    public void updateBusinessPackage(int id, BusinessPackage businessPackage)
    {
        String sql = "UPDATE T_BUSINESS_PACKAGE SET name=? WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {businessPackage.getName(), id});
    }
    
    /**
     * 删除业务规则包
     * @param id
     *        id
     */
    @Override
    public void deleteBusinessPackage(int id)
    {
        String sql = "DELETE FROM T_BUSINESS_PACKAGE WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {id});
    }
    
    /**
     * 根据id查询业务规则包
     * @param id
     *        id
     * @return  BusinessPackage
     */
    @Override
    public BusinessPackage queryBusinessPackage(int id)
    {
        String sql = "SELECT * FROM T_BUSINESS_PACKAGE WHERE id=?";
        BusinessPackage businessPackage =
            this.getJdbcTemplate().queryForObject(sql, new Object[] {id}, new BusinessPackageRowMapper());
        return businessPackage != null ? businessPackage : null;
    }
    
    /**
     * 查询所有的业务规则包
     * @return  List<BusinessPackage>
     */
    @Override
    public List<BusinessPackage> queryAllBusinessPackages()
    {
        String sql = "SELECT * FROM T_BUSINESS_PACKAGE ORDER BY NAME";
        List<BusinessPackage> businessPackages = this.getJdbcTemplate().query(sql, new BusinessPackageRowMapper());
        return (businessPackages != null && !businessPackages.isEmpty()) ? businessPackages : null;
    }
    
}
