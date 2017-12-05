/*
 * 文 件 名:  ScenarioPackageDaoImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.daos.res.scenario.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.huawei.ide.beans.res.scenario.ScenarioPackage;
import com.huawei.ide.daos.res.scenario.ScenarioPackageDao;

/**
 * 应用场景对象所属package数据库操作实现类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Repository(value = "com.huawei.ide.daos.res.scenario.impl.ScenarioPackageDaoImpl")
public class ScenarioPackageDaoImpl extends JdbcDaoSupport implements ScenarioPackageDao
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
     * 应用场景包
     * <功能详细描述>
     * 
     * @author  cWX306007
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
     * @see  [相关类/方法]
     */
    private class ScenarioPackageRowMapper implements RowMapper<ScenarioPackage>
    {
        @Override
        public ScenarioPackage mapRow(ResultSet resultSet, int rowNum)
            throws SQLException
        {
            ScenarioPackage scenarioPackage = new ScenarioPackage();
            scenarioPackage.setId(resultSet.getInt("id"));
            scenarioPackage.setName(resultSet.getString("name"));
            return scenarioPackage;
        }
    }
    
    /**
     * 新建应用场景包
     * @param scenarioPackage
     *        scenarioPackage
     */
    @Override
    public void createScenarioPackage(ScenarioPackage scenarioPackage)
    {
        String sql = "INSERT INTO T_SCENARIO_PACKAGE(name) VALUES(?)";
        this.getJdbcTemplate().update(sql, new Object[] {scenarioPackage.getName()});
    }
    
    /**
     * 更新业务规则包
     * @param id
     *         id
     * @param scenarioPackage
     *        scenarioPackage
     */
    @Override
    public void updateScenarioPackage(int id, ScenarioPackage scenarioPackage)
    {
        String sql = "UPDATE T_SCENARIO_PACKAGE SET name=? WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {scenarioPackage.getName(), id});
    }
    
    /**
     * 根据id删除业务规则包
     * @param id
     *        id
     */
    @Override
    public void deleteScenarioPackage(int id)
    {
        String sql = "DELETE FROM T_SCENARIO_PACKAGE WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {id});
    }
    
    /**
     * 根据id查询业务规则包
     * @param id
     *        id
     * @return  ScenarioPackage
     */
    @Override
    public ScenarioPackage queryScenarioPackage(int id)
    {
        String sql = "SELECT * FROM T_SCENARIO_PACKAGE WHERE id=?";
        ScenarioPackage scenarioPackage =
            this.getJdbcTemplate().queryForObject(sql, new Object[] {id}, new ScenarioPackageRowMapper());
        return scenarioPackage != null ? scenarioPackage : null;
    }
    
    /**
     * 查询所有的业务规则包
     * @return  List<ScenarioPackage>
     */
    @Override
    public List<ScenarioPackage> queryScenarioPackages()
    {
        String sql = "SELECT * FROM T_SCENARIO_PACKAGE";
        List<ScenarioPackage> scenarioPackages = this.getJdbcTemplate().query(sql, new ScenarioPackageRowMapper());
        return (scenarioPackages != null && !scenarioPackages.isEmpty()) ? scenarioPackages : null;
    }
    
}
