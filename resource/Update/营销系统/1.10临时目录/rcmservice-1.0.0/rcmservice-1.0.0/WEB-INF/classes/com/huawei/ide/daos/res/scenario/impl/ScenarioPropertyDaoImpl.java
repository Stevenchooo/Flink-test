/*
 * 文 件 名:  ScenarioPropertyDaoImpl.java
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
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Repository;

import com.huawei.ide.beans.res.domain.CustomRule;
import com.huawei.ide.beans.res.domain.DomainPropertyTemp;
import com.huawei.ide.beans.res.scenario.ScenarioProperty;
import com.huawei.ide.beans.res.scenario.ScenarioPropertyVO;
import com.huawei.ide.daos.res.scenario.ScenarioPropertyDao;

/**
 * 应用场景对象属性数据库操作实现类
 * 
 * @author z00219375
 * @version [版本号, 2015年12月28日]
 * @see [相关类/方法]
 * @since [Consumer Cloud Big Data Platform Dept]
 */
@Repository(value = "com.huawei.ide.daos.res.scenario.impl.ScenarioPropertyDaoImpl")
public class ScenarioPropertyDaoImpl extends JdbcDaoSupport implements ScenarioPropertyDao
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
     * 应用场景属性
     * <功能详细描述>
     * @author  cWX306007
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月16日]
     * @see  [相关类/方法]
     */
    private class ScenarioPropertyRowMapper implements RowMapper<ScenarioProperty>
    {
        @Override
        public ScenarioProperty mapRow(ResultSet resultSet, int rowNum)
            throws SQLException
        {
            ScenarioProperty scenarioProperty = new ScenarioProperty();
            scenarioProperty.setId(resultSet.getInt("id"));
            scenarioProperty.setScenarioId(resultSet.getInt("scenario_id"));
            scenarioProperty.setDomainId(resultSet.getInt("domain_id"));
            scenarioProperty.setOperator(resultSet.getString("operator"));
            scenarioProperty.setCustomRule(resultSet.getString("custom_rule"));
            scenarioProperty.setDomainPropertyId(Integer.parseInt(resultSet.getString("domain_property_id")));
            scenarioProperty.setDomainPropertyName(resultSet.getString("domain_property_name"));
            scenarioProperty.setDomainPropertyCategory(resultSet.getString("domain_property_category"));
            scenarioProperty.setDomainPropertyDefaultVal(resultSet.getString("domain_property_default_val"));
            scenarioProperty.setDomainPropertyOperator(resultSet.getString("domain_property_operator"));
            scenarioProperty.setDomainOperatorValue(resultSet.getString("domain_operator_value"));
            scenarioProperty.setRuleOperator(resultSet.getString("rule_operator"));
            return scenarioProperty;
        }
    }
    
    /**
     * 创建新的应用场景属性
     * @param scenarioProperty
     *         scenarioProperty
     */
    @Override
    public void createScenarioProperty(ScenarioProperty scenarioProperty)
    {
        String sql =
            "INSERT INTO T_SCENARIO_DOMAIN_PROPERTY(scenario_id, domain_id, operator, custom_rule, domain_property_id, domain_property_name, domain_property_category, domain_property_default_val, domain_property_operator, domain_operator_value, rule_operator) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        this.getJdbcTemplate().update(sql,
            new Object[] {scenarioProperty.getScenarioId(), scenarioProperty.getDomainId(),
                scenarioProperty.getOperator(), scenarioProperty.getCustomRule(),
                scenarioProperty.getDomainPropertyId(), scenarioProperty.getDomainPropertyName(),
                scenarioProperty.getDomainPropertyCategory(), scenarioProperty.getDomainPropertyDefaultVal(),
                scenarioProperty.getDomainPropertyOperator(), scenarioProperty.getDomainOperatorValue(),
                scenarioProperty.getRuleOperator()});
    }
    
    /**
     * 更新应用场景属性
     * @param id
     *        id
     * @param scenarioProperty
     *        scenarioProperty
     */
    @Override
    public void updateScenarioProperty(int id, ScenarioProperty scenarioProperty)
    {
        String sql =
            "UPDATE T_SCENARIO_DOMAIN_PROPERTY SET scenario_id=?,domain_id=?,operator=?,custom_rule=?,domain_property_id=?,domain_property_name=?,domain_property_category=?,domain_property_default_val=?,domain_property_operator=?,domain_operator_value=?,rule_operator=? WHERE id=?";
        this.getJdbcTemplate().update(sql,
            new Object[] {scenarioProperty.getScenarioId(), scenarioProperty.getDomainId(),
                scenarioProperty.getOperator(), scenarioProperty.getCustomRule(),
                scenarioProperty.getDomainPropertyId(), scenarioProperty.getDomainPropertyName(),
                scenarioProperty.getDomainPropertyCategory(), scenarioProperty.getDomainPropertyDefaultVal(),
                scenarioProperty.getDomainPropertyOperator(), scenarioProperty.getDomainOperatorValue(),
                scenarioProperty.getRuleOperator(), id});
    }
    
    /**
     * 删除应用场景属性
     * @param id
     *        id
     */
    @Override
    public void deleteScenarioProperty(int id)
    {
        String sql = "DELETE FROM T_SCENARIO_DOMAIN_PROPERTY WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {id});
    }
    
    /**
     * 根据应用场景id删除应用场景属性
     * @param scenarioId
     *        scenarioId
     */
    @Override
    public void deleteScenarioPropertyByScenarioId(int scenarioId)
    {
        String sql = "DELETE FROM T_SCENARIO_DOMAIN_PROPERTY WHERE scenario_id=?";
        this.getJdbcTemplate().update(sql, new Object[] {scenarioId});
    }
    
    /**
     * 根据id查询应用场景属性
     * @param id
     *        id
     * @return ScenarioProperty
     */
    @Override
    public ScenarioProperty queryScenarioProperty(int id)
    {
        String sql = "SELECT * FROM T_SCENARIO_DOMAIN_PROPERTY WHERE id=?";
        ScenarioProperty scenarioProperty =
            this.getJdbcTemplate().queryForObject(sql, new Object[] {id}, new ScenarioPropertyRowMapper());
        return scenarioProperty != null ? scenarioProperty : null;
    }
    
    /**
     * 查询所有的应用场景属性
     * @return List<ScenarioProperty>
     */
    @Override
    public List<ScenarioProperty> queryAllScenarioPropertys()
    {
        String sql = "SELECT * FROM T_SCENARIO_DOMAIN_PROPERTY ORDER BY ID";
        List<ScenarioProperty> scenarioProperties = this.getJdbcTemplate().query(sql, new ScenarioPropertyRowMapper());
        return (scenarioProperties != null && !scenarioProperties.isEmpty()) ? scenarioProperties : null;
    }
    
    /**
     * 通过应用场景id查询应用场景属性
     * @param scenarioId
     *        scenarioId
     * @return List<ScenarioProperty>
     */
    @Override
    public List<ScenarioProperty> queryScenarioPropertysByScenarioId(int scenarioId)
    {
        String sql = "SELECT * FROM T_SCENARIO_DOMAIN_PROPERTY WHERE scenario_id=? ORDER BY ID";
        List<ScenarioProperty> scenarioProperties =
            this.getJdbcTemplate().query(sql, new Object[] {scenarioId}, new ScenarioPropertyRowMapper());
        return (scenarioProperties != null && !scenarioProperties.isEmpty()) ? scenarioProperties : null;
    }
    
    /**
     * 通过应用场景id查询应用场景属性
     * @param scenarioId
     *        scenarioId
     * @return List<ScenarioPropertyVO>
     */
    @Override
    public List<ScenarioPropertyVO> queryScenarioPropertyVOsByScenarioId(int scenarioId)
    {
        List<ScenarioPropertyVO> scenarioPropertyVOs = new ArrayList<ScenarioPropertyVO>();
        String sqlGroupby =
            "SELECT DOMAIN_ID FROM T_SCENARIO_DOMAIN_PROPERTY WHERE scenario_id=? GROUP BY DOMAIN_ID ORDER BY DOMAIN_ID";
        List<Integer> domainIds =
            this.getJdbcTemplate().queryForList(sqlGroupby, new Object[] {scenarioId}, Integer.class);
        for (Integer domainId : domainIds)
        {
            ScenarioPropertyVO vo = new ScenarioPropertyVO();
            vo.setScenarioId(scenarioId);
            vo.setDomainId(domainId);
            List<CustomRule> customRules = new ArrayList<CustomRule>();
            List<DomainPropertyTemp> domainProperties = new ArrayList<DomainPropertyTemp>();
            String sql = "SELECT * FROM T_SCENARIO_DOMAIN_PROPERTY WHERE scenario_id=? and domain_id=? ORDER BY ID";
            List<ScenarioProperty> scenarioProperties =
                this.getJdbcTemplate().query(sql, new Object[] {scenarioId, domainId}, new ScenarioPropertyRowMapper());
            for (int i = 0; i < scenarioProperties.size(); i++)
            {
                ScenarioProperty scenarioProperty = scenarioProperties.get(i);
                // first do vo init
                if (i == 0)
                {
                    vo.setDomainOperator(scenarioProperty.getOperator());
                }
                String customRuleStr = scenarioProperty.getCustomRule();
                if (null != customRuleStr && customRuleStr.trim().length() != 0)
                {
                    CustomRule customRule = new CustomRule();
                    customRule.setCustomRule(customRuleStr);
                    customRule.setCustomRuleOperator(scenarioProperty.getRuleOperator());
                    customRules.add(customRule);
                }
                else
                {
                    DomainPropertyTemp domainProperty = new DomainPropertyTemp();
                    domainProperty.setDomainPropertyId(scenarioProperty.getDomainPropertyId());
                    domainProperty.setDomainPropertyName(scenarioProperty.getDomainPropertyName());
                    domainProperty.setDomainPropertyCategory(scenarioProperty.getDomainPropertyCategory());
                    domainProperty.setDomainPropertyDefaultVal(scenarioProperty.getDomainPropertyDefaultVal());
                    domainProperty.setDomainPropertyOperator(scenarioProperty.getDomainPropertyOperator());
                    domainProperty.setDomainPropertyOperatorValue(scenarioProperty.getDomainOperatorValue());
                    domainProperty.setDomainPropertyRuleOperator(scenarioProperty.getRuleOperator());
                    domainProperties.add(domainProperty);
                }
            }
            vo.setCustomRules(customRules);
            vo.setDomainProperties(domainProperties);
            scenarioPropertyVOs.add(vo);
        }
        return !scenarioPropertyVOs.isEmpty() ? scenarioPropertyVOs : null;
    }
    
    /**
     * 通过领域对象id查询应用场景属性
     * @param domainID
     *        domainID
     * @return boolean
     */
    @Override
    public boolean queryScenarioPropertyByDomainID(int domainID)
    {
        String sql = "SELECT * FROM T_SCENARIO_DOMAIN_PROPERTY WHERE domain_id=?";
        List<ScenarioProperty> scenarioProperties =
            this.getJdbcTemplate().query(sql, new Object[] {domainID}, new ScenarioPropertyRowMapper());
        return (null != scenarioProperties && !scenarioProperties.isEmpty());
    }
    
    /**
     * 通过应用场景名查询应用场景id
     * @param name
     *         name
     * @return  Integer
     */
    @Override
    public Integer queryScenarioIdByScenarioName(String name)
    {
        String sql = "SELECT ID FROM T_SCENARIO WHERE name =? ";
        List<Integer> scenarioIds = this.getJdbcTemplate().queryForList(sql, new Object[] {name}, Integer.class);
        return (null != scenarioIds && !scenarioIds.isEmpty()) ? scenarioIds.get(0) : null;
    }
    
}
