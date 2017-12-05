/*
 * 文 件 名:  ScenarioDaoImpl.java
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

import com.huawei.ide.beans.res.scenario.Scenario;
import com.huawei.ide.daos.res.scenario.ScenarioDao;

/**
 * 应用场景对象数据库操作实现类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Repository(value = "com.huawei.ide.daos.res.scenario.impl.ScenarioDaoImpl")
public class ScenarioDaoImpl extends JdbcDaoSupport implements ScenarioDao
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
     * 应用场景
     * <功能详细描述>
     * 
     * @author  cWX306007
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
     * @see  [相关类/方法]
     */
    private class ScenarioRowMapper implements RowMapper<Scenario>
    {
        @Override
        public Scenario mapRow(ResultSet resultSet, int rowNum)
            throws SQLException
        {
            Scenario scenario = new Scenario();
            scenario.setId(resultSet.getInt("id"));
            scenario.setName(resultSet.getString("name"));
            scenario.setPublished(resultSet.getString("published"));
            return scenario;
        }
    }
    
    /**
     * 新建应用场景
     * @param scenario
     *         scenario
     */
    @Override
    public void createScenario(Scenario scenario)
    {
        String sql = "INSERT INTO T_SCENARIO(name, published) VALUES(?, ?)";
        this.getJdbcTemplate().update(sql, new Object[] {scenario.getName(), scenario.getPublished()});
    }
    
    /**
     * 更新应用场景
     * @param id
     *        id
     * @param scenario
     *        scenario
     */
    @Override
    public void updateScenario(int id, Scenario scenario)
    {
        String sql = "UPDATE T_SCENARIO SET name=?,published=? WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {scenario.getName(), scenario.getPublished(), id});
    }
    
    /**
     * 删除应用场景
     * @param id
     *        id
     */
    @Override
    public void deleteScenario(int id)
    {
        String sql = "DELETE FROM T_SCENARIO WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {id});
    }
    
    /**
     * 根据id查询应用场景
     * @param id
     *        id
     * @return   Scenario
     */
    @Override
    public Scenario queryScenario(int id)
    {
        String sql = "SELECT * FROM T_SCENARIO WHERE id=?";
        Scenario scenario = this.getJdbcTemplate().queryForObject(sql, new Object[] {id}, new ScenarioRowMapper());
        return scenario != null ? scenario : null;
    }
    
    /**
     * 查询所有的应用场景
     * @return   List<Scenario>
     */
    @Override
    public List<Scenario> queryAllScenarios()
    {
        String sql = "SELECT * FROM T_SCENARIO ORDER BY ID";
        List<Scenario> scenarios = this.getJdbcTemplate().query(sql, new ScenarioRowMapper());
        return (scenarios != null && !scenarios.isEmpty()) ? scenarios : null;
    }
    
    /**
     * 查询应用场景总数
     * @return  int
     */
    @Override
    public int queryScenarioTotalNum()
    {
        String sql = "SELECT COUNT(*) FROM T_SCENARIO";
        return this.getJdbcTemplate().queryForObject(sql, Integer.class);
    }
    
    /**
     * 分页查询应用场景
     * @param index
     *        index
     * @param pageSize
     *        pageSize
     * @return  List<Scenario>
     */
    @Override
    public List<Scenario> queryScenariosByPage(int index, int pageSize)
    {
        if (index < 0)
        {
            return null;
        }
        String sql = "SELECT * FROM T_SCENARIO ORDER BY ID LIMIT ?,?";
        List<Scenario> scenarios =
            this.getJdbcTemplate().query(sql, new Object[] {index, pageSize}, new ScenarioRowMapper());
        return (scenarios != null && !scenarios.isEmpty()) ? scenarios : null;
    }
    
}
