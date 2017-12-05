/*
 * 文 件 名:  ScenarioPackageRelationDaoImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  cWX306007
 * 修改时间:  2016年3月28日
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

import com.huawei.ide.beans.res.scenario.Scenario;
import com.huawei.ide.beans.res.scenario.ScenarioPackageRelation;
import com.huawei.ide.daos.res.scenario.ScenarioPackageRelationDao;

/**
 * 领域对象package关系数据库操作实现类
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月28日]
 * @see  [相关类/方法]
 */
@Repository(value = "com.huawei.ide.daos.res.scenario.impl.ScenarioPackageRelationDaoImpl")
public class ScenarioPackageRelationDaoImpl extends JdbcDaoSupport implements ScenarioPackageRelationDao
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
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月17日]
     * @see  [相关类/方法]
     */
    private class ScenarioPackageRelationRowMapper implements RowMapper<ScenarioPackageRelation>
    {
        @Override
        public ScenarioPackageRelation mapRow(ResultSet resultSet, int rowNum)
            throws SQLException
        {
            ScenarioPackageRelation scenarioPackageRelation = new ScenarioPackageRelation();
            scenarioPackageRelation.setId(resultSet.getInt("id"));
            scenarioPackageRelation.setPackageId(resultSet.getInt("package_id"));
            scenarioPackageRelation.setScenarioId(resultSet.getInt("scenario_id"));
            scenarioPackageRelation.setPackageName(resultSet.getString("package_name"));
            scenarioPackageRelation.setScenarioName(resultSet.getString("scenario_name"));
            scenarioPackageRelation.setScenarioPublished(resultSet.getString("scenario_published"));
            
            return scenarioPackageRelation;
        }
    }
    
    /**
     * 新建应用场景包关系
     * @param scenarioPackageRelation
     *        scenarioPackageRelation
     */
    @Override
    public void createScenarioPackageRelation(ScenarioPackageRelation scenarioPackageRelation)
    {
        String sql =
            "INSERT INTO T_SCENARIO_PACKAGE_RELATION(package_id,scenario_id,package_name,scenario_name,scenario_published) VALUES(?,?,?,?,?)";
        this.getJdbcTemplate().update(sql,
            
            new Object[] {scenarioPackageRelation.getPackageId(), scenarioPackageRelation.getScenarioId(),
                scenarioPackageRelation.getPackageName(), scenarioPackageRelation.getScenarioName(),
                scenarioPackageRelation.getScenarioPublished()});
    }
    
    /**
     * 更新应用场景包关系
     * @param id
     *        id
     * @param scenarioPackageRelation
     *        scenarioPackageRelation
     */
    @Override
    public void updateScenarioPackageRelation(int id, ScenarioPackageRelation scenarioPackageRelation)
    {
        String sql =
            "UPDATE T_SCENARIO_PACKAGE_RELATION SET package_id=?,scenario_id=?,package_name=?,scenario_name=?,scenario_published=? WHERE id=?";
        this.getJdbcTemplate().update(sql,
            new Object[] {scenarioPackageRelation.getPackageId(), scenarioPackageRelation.getScenarioId(),
                scenarioPackageRelation.getPackageName(), scenarioPackageRelation.getScenarioName(),
                scenarioPackageRelation.getScenarioPublished(), id});
    }
    
    /**
     * 删除应用场景包关系
     * @param id
     *        id
     */
    @Override
    public void deleteScenarioPackageRelation(int id)
    {
        String sql = "DELETE FROM T_SCENARIO_PACKAGE_RELATION WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {id});
    }
    
    /**
     * 通过包id删除应用场景包关系
     * @param packageId
     *        packageId
     */
    @Override
    public void deleteScenarioPackageRelationByPackageId(int packageId)
    {
        String sql = "DELETE FROM T_SCENARIO_PACKAGE_RELATION WHERE package_id=?";
        this.getJdbcTemplate().update(sql, new Object[] {packageId});
    }
    
    /**
     * 通过应用场景id删除包关系
     * @param scenarioId
     *        scenarioId
     */
    @Override
    public void deleteScenarioPackageRelationByScenarioId(int scenarioId)
    {
        String sql = "DELETE FROM T_SCENARIO_PACKAGE_RELATION WHERE SCENARIO_id=?";
        this.getJdbcTemplate().update(sql, new Object[] {scenarioId});
    }
    
    /**
     * 通过包id删除应用场景关系
     * @param packageId
     *        packageId
     * @return  List<ScenarioPackageRelation>
     */
    @Override
    public List<ScenarioPackageRelation> queryScenarioPackageRelationsByPackageId(int packageId)
    {
        String sql = "SELECT * FROM T_SCENARIO_PACKAGE_RELATION WHERE package_id=? ORDER BY SCENARIO_NAME";
        List<ScenarioPackageRelation> scenarioPackageRelations =
            this.getJdbcTemplate().query(sql, new Object[] {packageId}, new ScenarioPackageRelationRowMapper());
        return (scenarioPackageRelations != null && !scenarioPackageRelations.isEmpty()) ? scenarioPackageRelations
            : null;
    }
    
    /**
     * 通过应用场景id查询应用场景包关系
     * @param scenarioId
     *        scenarioId
     * @return   List<ScenarioPackageRelation>
     */
    @Override
    public List<ScenarioPackageRelation> queryScenarioPackageRelationsByScenarioId(int scenarioId)
    {
        String sql = "SELECT * FROM T_SCENARIO_PACKAGE_RELATION WHERE SCENARIO_id=? ORDER BY SCENARIO_NAME";
        List<ScenarioPackageRelation> scenarioPackageRelations =
            this.getJdbcTemplate().query(sql, new Object[] {scenarioId}, new ScenarioPackageRelationRowMapper());
        return (scenarioPackageRelations != null && !scenarioPackageRelations.isEmpty()) ? scenarioPackageRelations
            : null;
    }
    
    /**
     * 通过应用场景更新包关系
     * @param scenario
     *        scenario
     */
    @Override
    public void updatePackageRelationByScenario(Scenario scenario)
    {
        
        String sql = "UPDATE T_SCENARIO_PACKAGE_RELATION SET scenario_name=?,scenario_published=? WHERE scenario_id=?";
        this.getJdbcTemplate().update(sql,
            new Object[] {scenario.getName(), scenario.getPublished(), scenario.getId()});
    }
    
}
