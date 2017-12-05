/*
 * 文 件 名:  DomainPackageDaoImpl.java
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

import com.huawei.ide.beans.res.domain.DomainPackage;
import com.huawei.ide.daos.res.domain.DomainPackageDao;

/**
 * 领域对象所属package数据库操作实现类
 * @author  z00219375
 * @version  [版本号, 2015年12月23日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Repository(value = "com.huawei.ide.daos.res.domain.impl.DomainPackageDaoImpl")
public class DomainPackageDaoImpl extends JdbcDaoSupport implements DomainPackageDao
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
    private class DomainPackageRowMapper implements RowMapper<DomainPackage>
    {
        @Override
        public DomainPackage mapRow(ResultSet resultSet, int rowNum)
            throws SQLException
        {
            DomainPackage domainPackage = new DomainPackage();
            domainPackage.setId(resultSet.getInt("id"));
            domainPackage.setName(resultSet.getString("name"));
            return domainPackage;
        }
    }
    
    /**
     * 创建领域对象所属package
     * @param domainPackage domainPackage
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void createDomainPackage(DomainPackage domainPackage)
    {
        String sql = "INSERT INTO T_DOMAIN_PACKAGE(name) VALUES(?)";
        this.getJdbcTemplate().update(sql, new Object[] {domainPackage.getName()});
    }
    
    /**
     * 更新领域对象所属package
     * @param id id
     * @param domainPackage domainPackage
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void updateDomainPackage(int id, DomainPackage domainPackage)
    {
        String sql = "UPDATE T_DOMAIN_PACKAGE SET name=? WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {domainPackage.getName(), id});
    }
    
    /**
     * 删除领域对象所属package
     * @param id id
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteDomainPackage(int id)
    {
        String sql = "DELETE FROM T_DOMAIN_PACKAGE WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {id});
    }
    
    /**
     * 查询所有领域对象所属package
     * @return List<DomainPackage> domainPackage
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<DomainPackage> queryAllDomainPackages()
    {
        String sql = "SELECT * FROM T_DOMAIN_PACKAGE ORDER BY NAME";
        List<DomainPackage> domainPackages = this.getJdbcTemplate().query(sql, new DomainPackageRowMapper());
        return (domainPackages != null && !domainPackages.isEmpty()) ? domainPackages : null;
    }
    
}
