/*
 * 文 件 名:  BusinessRuleDaoImpl.java
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

import com.huawei.ide.beans.res.business.BusinessRule;
import com.huawei.ide.daos.res.business.BusinessRuleDao;

/**
 * 业务规则对象属性数据库操作实现类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Repository(value = "com.huawei.ide.daos.res.business.impl.BusinessRuleDaoImpl")
public class BusinessRuleDaoImpl extends JdbcDaoSupport implements BusinessRuleDao
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
     * 业务规则rule
     * <功能详细描述>
     * 
     * @author  cWX306007
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
     * @see  [相关类/方法]
     */
    private class BusinessRuleRowMapper implements RowMapper<BusinessRule>
    {
        @Override
        public BusinessRule mapRow(ResultSet resultSet, int rowNum)
            throws SQLException
        {
            BusinessRule businessRule = new BusinessRule();
            businessRule.setId(resultSet.getInt("id"));
            businessRule.setBusinessId(resultSet.getInt("business_id"));
            businessRule.setBpmModelId(resultSet.getString("bpm_model_id"));
            businessRule.setBpmContent(resultSet.getBytes("bpm_content"));
            return businessRule;
        }
    }
    
    /**
     * 新建业务规则rule
     * @param businessRule
     *        businessRule
     */
    @Override
    public void createBusinessRule(BusinessRule businessRule)
    {
        String sql = "INSERT INTO T_BUSINESS_RULE(business_id, bpm_model_id, bpm_content) VALUES(?, ?, ?)";
        this.getJdbcTemplate().update(sql,
            new Object[] {businessRule.getBusinessId(), businessRule.getBpmModelId(), businessRule.getBpmContent()});
        
    }
    
    /**
     * 更新业务规则rule
     * @param id
     *        id
     * @param businessRule
     *        businessRule
     */
    @Override
    public void updateBusinessRule(int id, BusinessRule businessRule)
    {
        String sql = "UPDATE T_BUSINESS_RULE SET business_id=?,bpm_model_id=?,bpm_content=? WHERE id=?";
        this.getJdbcTemplate().update(sql,
            new Object[] {businessRule.getBusinessId(), businessRule.getBpmModelId(), businessRule.getBpmContent(),
                id});
    }
    
    /**
     * 根据id删除业务规则rule
     * @param id
     *        id
     */
    @Override
    public void deleteBusinessRule(int id)
    {
        String sql = "DELETE FROM T_BUSINESS_RULE WHERE id=?";
        this.getJdbcTemplate().update(sql, new Object[] {id});
    }
    
    /**
     * 根据业务规则id删除业务规则rule
     * @param businessId
     *        businessId
     */
    @Override
    public void deleteBusinessRuleByBusinessId(int businessId)
    {
        String sql = "DELETE FROM T_BUSINESS_RULE WHERE business_id=?";
        this.getJdbcTemplate().update(sql, new Object[] {businessId});
    }
    
    /**
     * 根据id删除业务规则rule
     * @param id
     *        id
     * @return  BusinessRule
     */
    @Override
    public BusinessRule queryBusinessRule(int id)
    {
        String sql = "SELECT * FROM T_BUSINESS_RULE WHERE id=?";
        BusinessRule businessRule =
            this.getJdbcTemplate().queryForObject(sql, new Object[] {id}, new BusinessRuleRowMapper());
        return businessRule != null ? businessRule : null;
    }
    
    /**
     * 查询所有的业务规则rule
     * @return  List<BusinessRule>
     */
    @Override
    public List<BusinessRule> queryAllBusinessRules()
    {
        String sql = "SELECT * FROM T_BUSINESS_RULE ORDER BY ID";
        List<BusinessRule> businessRules = this.getJdbcTemplate().query(sql, new BusinessRuleRowMapper());
        return (businessRules != null && businessRules.size() > 0) ? businessRules : null;
        
    }
    
    /**
     * 通过业务规则id查询业务规则
     * @param businessId
     *        businessId
     * @return  List<BusinessRule>
     */
    @Override
    public List<BusinessRule> queryBusinessRulesByBusinessId(int businessId)
    {
        String sql = "SELECT * FROM T_BUSINESS_RULE WHERE business_id=? ORDER BY ID";
        List<BusinessRule> businessRules =
            this.getJdbcTemplate().query(sql, new Object[] {businessId}, new BusinessRuleRowMapper());
        return (businessRules != null && !businessRules.isEmpty()) ? businessRules : null;
    }
    
}
