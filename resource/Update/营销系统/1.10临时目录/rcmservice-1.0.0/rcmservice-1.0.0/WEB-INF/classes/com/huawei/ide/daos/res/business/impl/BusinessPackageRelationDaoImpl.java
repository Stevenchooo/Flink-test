/*
 * 文 件 名:  BusinessPackageRelationDaoImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2016年2月17日
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

import com.huawei.ide.beans.res.business.BusinessPackageRelation;
import com.huawei.ide.daos.res.business.BusinessPackageRelationDao;

/**
 * 业务规则package关系数据库操作实现类
 * @author  z00219375
 * @version  [版本号, 2016年2月17日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Repository(value = "com.huawei.ide.daos.res.business.impl.BusinessPackageRelationDaoImpl")
public class BusinessPackageRelationDaoImpl extends JdbcDaoSupport implements BusinessPackageRelationDao
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
     * 业务规则包关系
     * <功能详细描述>
     * 
     * @author  cWX306007
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月16日]
     * @see  [相关类/方法]
     */
    private class BusinessPackageRelationRowMapper implements RowMapper<BusinessPackageRelation>
    {
        @Override
        public BusinessPackageRelation mapRow(ResultSet resultSet, int rowNum)
            throws SQLException
        {
            BusinessPackageRelation businessPackageRelation = new BusinessPackageRelation();
            businessPackageRelation.setId(resultSet.getInt("id"));
            businessPackageRelation.setPackageId(resultSet.getInt("package_id"));
            businessPackageRelation.setBusinessId(resultSet.getInt("business_id"));
            businessPackageRelation.setPackageName(resultSet.getString("package_name"));
            businessPackageRelation.setBusinessName(resultSet.getString("business_name"));
            businessPackageRelation.setBusinessPublished(resultSet.getString("business_published"));
            return businessPackageRelation;
        }
    }
    
    /**
     * 新建业务规则包关系
     * @param businessPackageRelation
     *        businessPackageRelation
     */
    @Override
    public void createBusinessPackageRelation(BusinessPackageRelation businessPackageRelation)
    {
        String sql =
            "INSERT INTO T_BUSINESS_PACKAGE_RELATION(package_id,business_id,package_name,business_name,business_published) VALUES(?,?,?,?,?)";
        this.getJdbcTemplate().update(sql,
            new Object[] {businessPackageRelation.getPackageId(), businessPackageRelation.getBusinessId(),
                businessPackageRelation.getPackageName(), businessPackageRelation.getBusinessName(),
                businessPackageRelation.getBusinessPublished()});
    }
    
    /**
     * 更新业务规则包关系
     * @param id
     *        id
     * @param businessPackageRelation
     *        businessPackageRelation
     */
    @Override
    public void updateBusinessPackageRelation(int id, BusinessPackageRelation businessPackageRelation)
    {
        String sql =
            "UPDATE T_BUSINESS_PACKAGE_RELATION SET package_id=?,business_id=?,package_name=?,business_name=?,business_published=? WHERE id=?";
        this.getJdbcTemplate().update(sql,
            new Object[] {businessPackageRelation.getPackageId(), businessPackageRelation.getBusinessId(),
                businessPackageRelation.getPackageName(), businessPackageRelation.getBusinessName(),
                businessPackageRelation.getBusinessPublished(), id});
    }
    
    /**
     * 通过id查询业务规则包关系
     * @param id
     *        id
     * @return  BusinessPackageRelation
     */
    @Override
    public BusinessPackageRelation queryBusinessPackageRelationById(int id)
    {
        String sql = "SELECT * FROM T_BUSINESS_PACKAGE_RELATION WHERE id=?";
        BusinessPackageRelation businessPackageRelation =
            this.getJdbcTemplate().queryForObject(sql, new Object[] {id}, new BusinessPackageRelationRowMapper());
        return businessPackageRelation != null ? businessPackageRelation : null;
    }
    
    /**
     * 根据删除业务规则包关系
     * @param id
     *        id
     */
    @Override
    public void deleteBusinessPackageRelation(int id)
    {
        String sql = "DELETE FROM T_BUSINESS_PACKAGE_RELATION WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {id});
    }
    
    /**
     * 根据包id删除业务规则包关系
     * @param packageId
     *         packageId
     */
    @Override
    public void deleteBusinessPackageRelationByPackageId(int packageId)
    {
        String sql = "DELETE FROM T_BUSINESS_PACKAGE_RELATION WHERE package_id=?";
        this.getJdbcTemplate().update(sql, new Object[] {packageId});
    }
    
    /**
     * 通过业务规则id删除业务规则关系
     * @param businessId
     *        businessId
     */
    @Override
    public void deleteBusinessPackageRelationByBusinessId(int businessId)
    {
        String sql = "DELETE FROM T_BUSINESS_PACKAGE_RELATION WHERE business_id=?";
        this.getJdbcTemplate().update(sql, new Object[] {businessId});
    }
    
    /**
     * 通过包id查询业务规则包关系
     * @param packageId
     *        packageId
     * @return  List<BusinessPackageRelation>
     */
    @Override
    public List<BusinessPackageRelation> queryBusinessPackageRelationsByPackageId(int packageId)
    {
        String sql = "SELECT * FROM T_BUSINESS_PACKAGE_RELATION WHERE package_id=? ORDER BY BUSINESS_NAME";
        List<BusinessPackageRelation> businessPackageRelations =
            this.getJdbcTemplate().query(sql, new Object[] {packageId}, new BusinessPackageRelationRowMapper());
        return (businessPackageRelations != null && !businessPackageRelations.isEmpty()) ? businessPackageRelations
            : null;
    }
    
    /**
     * 通过业务规则id查询业务规则包关系
     * @param businessId
     *        businessId
     * @return  List<BusinessPackageRelation>
     */
    @Override
    public List<BusinessPackageRelation> queryBusinessPackageRelationsByBusinessId(int businessId)
    {
        String sql = "SELECT * FROM T_BUSINESS_PACKAGE_RELATION WHERE business_id=? ORDER BY BUSINESS_NAME";
        List<BusinessPackageRelation> businessPackageRelations =
            this.getJdbcTemplate().query(sql, new Object[] {businessId}, new BusinessPackageRelationRowMapper());
        return (businessPackageRelations != null && !businessPackageRelations.isEmpty()) ? businessPackageRelations
            : null;
    }
    
}
