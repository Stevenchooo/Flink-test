/*
 * 文 件 名:  DomainPackageRelationDaoImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2016年1月30日
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

import com.huawei.ide.beans.res.domain.DomainPackageRelation;
import com.huawei.ide.daos.res.domain.DomainPackageRelationDao;

/**
 * 领域对象package关系数据库操作实现类
 * @author  z00219375
 * @version  [版本号, 2016年1月30日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Repository(value = "com.huawei.ide.daos.res.domain.impl.DomainPackageRelationDaoImpl")
public class DomainPackageRelationDaoImpl extends JdbcDaoSupport implements DomainPackageRelationDao
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
    private class DomainPackageRelationRowMapper implements RowMapper<DomainPackageRelation>
    {
        @Override
        public DomainPackageRelation mapRow(ResultSet resultSet, int rowNum)
            throws SQLException
        {
            DomainPackageRelation domainPackageRelation = new DomainPackageRelation();
            domainPackageRelation.setId(resultSet.getInt("id"));
            domainPackageRelation.setPackageId(resultSet.getInt("package_id"));
            domainPackageRelation.setDomainId(resultSet.getInt("domain_id"));
            domainPackageRelation.setPackageName(resultSet.getString("package_name"));
            domainPackageRelation.setDomainName(resultSet.getString("domain_name"));
            domainPackageRelation.setDomainPublished(resultSet.getString("domain_published"));
            return domainPackageRelation;
        }
    }
    
    /**
     * 创建领域对象package关系
     * @param domainPackageRelation domainPackageRelation
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void createDomainPackageRelation(DomainPackageRelation domainPackageRelation)
    {
        String sql =
            "INSERT INTO T_DOMAIN_PACKAGE_RELATION(package_id,domain_id,package_name,domain_name,domain_published) VALUES(?,?,?,?,?)";
        this.getJdbcTemplate().update(sql,
            new Object[] {domainPackageRelation.getPackageId(), domainPackageRelation.getDomainId(),
                domainPackageRelation.getPackageName(), domainPackageRelation.getDomainName(),
                domainPackageRelation.getDomainPublished()});
    }
    
    /**
     * 更新领域对象package关系
     * @param id id
     * @param domainPackageRelation domainPackageRelation
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void updateDomainPackageRelation(int id, DomainPackageRelation domainPackageRelation)
    {
        String sql =
            "UPDATE T_DOMAIN_PACKAGE_RELATION SET package_id=?,domain_id=?,package_name=?,domain_name=?,domain_published=? WHERE id=?";
        this.getJdbcTemplate().update(sql,
            new Object[] {domainPackageRelation.getPackageId(), domainPackageRelation.getDomainId(),
                domainPackageRelation.getPackageName(), domainPackageRelation.getDomainName(),
                domainPackageRelation.getDomainPublished(), id});
    }
    
    /**
     * 删除领域对象package关系
     * @param id id
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteDomainPackageRelation(int id)
    {
        String sql = "DELETE FROM T_DOMAIN_PACKAGE_RELATION WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {id});
    }
    
    /**
     * 根据PackageId删除领域对象package关系
     * @param packageId packageId
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteDomainPackageRelationByPackageId(int packageId)
    {
        String sql = "DELETE FROM T_DOMAIN_PACKAGE_RELATION WHERE package_id=?";
        this.getJdbcTemplate().update(sql, new Object[] {packageId});
    }
    
    /**
     * 根据DomainId删除领域对象package关系
     * @param domainId domainId
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteDomainPackageRelationByDomainId(int domainId)
    {
        String sql = "DELETE FROM T_DOMAIN_PACKAGE_RELATION WHERE domain_id=?";
        this.getJdbcTemplate().update(sql, new Object[] {domainId});
    }
    
    /**
     * 根据PackageId查询领域对象package关系
     * @param packageId packageId
     * @return List<DomainPackageRelation> DomainPackageRelation
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<DomainPackageRelation> queryDomainPackageRelationsByPackageId(int packageId)
    {
        String sql = "SELECT * FROM T_DOMAIN_PACKAGE_RELATION WHERE package_id=? ORDER BY DOMAIN_NAME";
        List<DomainPackageRelation> domainPackageRelations =
            this.getJdbcTemplate().query(sql, new Object[] {packageId}, new DomainPackageRelationRowMapper());
        return (domainPackageRelations != null && !domainPackageRelations.isEmpty()) ? domainPackageRelations : null;
    }
    
    /**
     * 根据DomainId查询领域对象package关系
     * @param domainId domainId
     * @return List<DomainPackageRelation> DomainPackageRelation
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<DomainPackageRelation> queryDomainPackageRelationsByDomainId(int domainId)
    {
        String sql = "SELECT * FROM T_DOMAIN_PACKAGE_RELATION WHERE domain_id=? ORDER BY DOMAIN_NAME";
        List<DomainPackageRelation> domainPackageRelations =
            this.getJdbcTemplate().query(sql, new Object[] {domainId}, new DomainPackageRelationRowMapper());
        return (domainPackageRelations != null && !domainPackageRelations.isEmpty()) ? domainPackageRelations : null;
    }
    
}
