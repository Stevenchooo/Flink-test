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

import com.huawei.ide.beans.res.business.BusinessProperty;
import com.huawei.ide.daos.res.business.BusinessPropertyDao;

/**
 * 
 * BusinessPropertyDaoImpl
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
 * @see  [相关类/方法]
 */
@Repository(value = "com.huawei.ide.daos.res.business.impl.BusinessPropertyDaoImpl")
public class BusinessPropertyDaoImpl extends JdbcDaoSupport implements BusinessPropertyDao
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
     * 业务规则属性
     * <功能详细描述>
     * 
     * @author  cWX306007
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
     * @see  [相关类/方法]
     */
    private class BusinessPropertyRowMapper implements RowMapper<BusinessProperty>
    {
        @Override
        public BusinessProperty mapRow(ResultSet resultSet, int rowNum)
            throws SQLException
        {
            BusinessProperty businessProperty = new BusinessProperty();
            businessProperty.setId(resultSet.getInt("id"));
            businessProperty.setBusinessId(resultSet.getInt("business_id"));
            businessProperty.setScenarioId(resultSet.getInt("scenario_id"));
            businessProperty.setOperator(resultSet.getString("operator"));
            businessProperty.setDomainId(resultSet.getInt("domain_id"));
            businessProperty.setScenarioDomainOperator(resultSet.getString("scenario_domain_operator"));
            businessProperty.setCustomRule(resultSet.getString("custom_rule"));
            businessProperty.setDomainPropertyId(Integer.parseInt(resultSet.getString("domain_property_id")));
            businessProperty.setDomainPropertyName(resultSet.getString("domain_property_name"));
            businessProperty.setDomainPropertyCategory(resultSet.getString("domain_property_category"));
            businessProperty.setDomainPropertyDefaultVal(resultSet.getString("domain_property_default_val"));
            businessProperty.setDomainPropertyOperator(resultSet.getString("domain_property_operator"));
            businessProperty.setDomainOperatorValue(resultSet.getString("domain_operator_value"));
            businessProperty.setRuleOperator(resultSet.getString("rule_operator"));
            return businessProperty;
        }
    }
    
    /**
     * 判断应用场景是否被使用
     * @param scenarioId
     *        scenarioId
     * @return  boolean
     */
    @Override
    public boolean judgeScenarioInUseById(int scenarioId)
    {
        String sql = "SELECT * FROM T_BUSINESS_SCENARIO_DOMAIN_PROPERTY WHERE scenario_id = ?";
        List<BusinessProperty> businessPropertyList =
            this.getJdbcTemplate().query(sql, new Object[] {scenarioId}, new BusinessPropertyRowMapper());
        if (businessPropertyList.isEmpty())
        {
            return false;
        }
        return true;
    }
    
}
