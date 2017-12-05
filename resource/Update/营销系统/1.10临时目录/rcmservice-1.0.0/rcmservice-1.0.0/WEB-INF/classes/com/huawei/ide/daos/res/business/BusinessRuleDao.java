/*
 * 文 件 名:  BusinessRuleDao.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.daos.res.business;

import java.util.List;

import com.huawei.ide.beans.res.business.BusinessRule;

/**
 * 业务规则数据库操作类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public interface BusinessRuleDao
{
    /**
     * 创建业务规则
     * <功能详细描述>
     * @param businessRule
     *         businessRule
     * @see [类、类#方法、类#成员]
     */
    public void createBusinessRule(BusinessRule businessRule);
    
    /**
     * 更新业务规则
     * <功能详细描述>
     * @param id
     *        id
     * @param businessRule
     *        businessRule
     * @see [类、类#方法、类#成员]
     */
    public void updateBusinessRule(int id, BusinessRule businessRule);
    
    /**
     * 删除业务规则
     * <功能详细描述>
     * @param id
     *        id
     * @see [类、类#方法、类#成员]
     */
    public void deleteBusinessRule(int id);
    
    /**
     * 根据外键businessId
     * 删除对应业务规则
     * <功能详细描述>
     * @param businessId
     *        businessId
     * @see [类、类#方法、类#成员]
     */
    public void deleteBusinessRuleByBusinessId(int businessId);
    
    /**
     * 查询指定业务规则
     * <功能详细描述>
     * @param id
     *        id
     * @return   BusinessRule
     * @see [类、类#方法、类#成员]
     */
    public BusinessRule queryBusinessRule(int id);
    
    /**
     * 查询所有业务规则
     * <功能详细描述>
     * @return  List<BusinessRule> 
     * @see [类、类#方法、类#成员]
     */
    public List<BusinessRule> queryAllBusinessRules();
    
    /**
     * 查询指定businessId对应的所有BusinessRules
     * <功能详细描述>
     * @param businessId
     *        businessId
     * @return   List<BusinessRule>
     * @see [类、类#方法、类#成员]
     */
    public List<BusinessRule> queryBusinessRulesByBusinessId(int businessId);
}
