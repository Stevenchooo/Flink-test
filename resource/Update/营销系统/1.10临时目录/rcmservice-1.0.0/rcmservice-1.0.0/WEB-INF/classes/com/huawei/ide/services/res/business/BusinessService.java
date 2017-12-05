/*
 * 文 件 名:  BusinessService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.services.res.business;

import java.util.List;

import com.huawei.ide.beans.res.business.Business;
import com.huawei.ide.beans.res.business.BusinessPackage;
import com.huawei.ide.beans.res.business.BusinessPackageRelation;
import com.huawei.ide.beans.res.business.BusinessRule;

/**
 * 业务规则对象数据库服务类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public interface BusinessService
{
    
    /**
     * 创建业务规则对象
     * <功能详细描述>
     * @param business
     *        business
     * @see [类、类#方法、类#成员]
     */
    public void createBusiness(Business business);
    
    /**
    * 创建业务规则对象
    * 并返回BusinessId
    * @param business
    *         business
    * @return  int
    * @see [类、类#方法、类#成员]
    */
    public int createBusinessReturnId(Business business);
    
    /**
     * 更新业务规则对象
     * <功能详细描述>
     * @param id
     *        id
     * @param business
     *        business
     * @see [类、类#方法、类#成员]
     */
    public void updateBusiness(int id, Business business);
    
    /**
     * 删除业务规则对象
     * <功能详细描述>
     * @param id
     *         id
     * @see [类、类#方法、类#成员]
     */
    public void deleteBusiness(int id);
    
    /**
     * 查询指定业务规则对象
     * <功能详细描述>
     * @param id
     *        id
     * @return   Business
     * @see [类、类#方法、类#成员]
     */
    public Business queryBusiness(int id);
    
    /**
     * 根据Business查询指定业务对象
     * <功能详细描述>
     * @param name
     *         name
     * @return   Business
     * @see [类、类#方法、类#成员]
     */
    public Business queryBusinessByName(String name);
    
    /**
     * 查询所有业务规则对象
     * <功能详细描述>
     * @return List<Business>
     * @see [类、类#方法、类#成员]
     */
    public List<Business> queryAllBusinesses();
    
    /**
     * 创建业务规则
     * <功能详细描述>
     * @param businessRule
     *        businessRule
     * @see [类、类#方法、类#成员]
     */
    public void createBusinessRule(BusinessRule businessRule);
    
    /**
     * 更新业务规则
     * <功能详细描述>
     * @param id
     *        id
     * @param businessRule
     *         businessRule
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
     * @return  BusinessRule
     *          BusinessRule
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
     *          businessId
     * @return  List<BusinessRule>
     * @see [类、类#方法、类#成员]
     */
    public List<BusinessRule> queryBusinessRulesByBusinessId(int businessId);
    
    /**
     * 创建业务规则对象所属package
     * <功能详细描述>
     * @param businessPackage
     *         businessPackage
     * @see [类、类#方法、类#成员]
     */
    public void createBusinessPackage(BusinessPackage businessPackage);
    
    /**
     * 更新业务规则对象所属package
     * <功能详细描述>
     * @param id
     *        id
     * @param businessPackage
     *         businessPackage
     * @see [类、类#方法、类#成员]
     */
    public void updateBusinessPackage(int id, BusinessPackage businessPackage);
    
    /**
     * 删除业务规则对象所属package
     * <功能详细描述>
     * @param id
     *        id
     * @see [类、类#方法、类#成员]
     */
    public void deleteBusinessPackage(int id);
    
    /**
     * 查询指定业务规则对象所属package
     * <功能详细描述>
     * @param id
     *         id
     * @return  BusinessPackage
     * @see [类、类#方法、类#成员]
     */
    public BusinessPackage queryBusinessPackage(int id);
    
    /**
     * 查询所有业务规则对象所属package
     * <功能详细描述>
     * @return  List<BusinessPackage>
     * @see [类、类#方法、类#成员]
     */
    public List<BusinessPackage> queryAllBusinessPackages();
    
    /**
     * 创建业务规则package关系
     * <功能详细描述>
     * @param businessPackageRelation
     *        businessPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public void createBusinessPackageRelation(BusinessPackageRelation businessPackageRelation);
    
    /**
     * 更新业务规则package关系
     * <功能详细描述>
     * @param id
     *         id
     * @param businessPackageRelation
     *         businessPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public void updateBusinessPackageRelation(int id, BusinessPackageRelation businessPackageRelation);
    
    /**
     * 查询业务规则package关系
     * <功能详细描述>
     * @param id
     *          id
     * @return  BusinessPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public BusinessPackageRelation queryBusinessPackageRelationById(int id);
    
    /**
     * 删除业务规则package关系
     * <功能详细描述>
     * @param id
     *         id
     * @see [类、类#方法、类#成员]
     */
    public void deleteBusinessPackageRelation(int id);
    
    /**
     * 根据PackageId删除业务规则package关系
     * <功能详细描述>
     * @param packageId
     *         packageId
     * @see [类、类#方法、类#成员]
     */
    public void deleteBusinessPackageRelationByPackageId(int packageId);
    
    /**
     * 根据BusinessId删除业务规则package关系
     * <功能详细描述>
     * @param businessId
     *         businessId
     * @see [类、类#方法、类#成员]
     */
    public void deleteBusinessPackageRelationByBusinessId(int businessId);
    
    /**
    * 根据PackageId查询业务规则package关系
    * <功能详细描述>
    * @param packageId
    *        packageId
    * @return  List<BusinessPackageRelation>
    * @see [类、类#方法、类#成员]
    */
    public List<BusinessPackageRelation> queryBusinessPackageRelationsByPackageId(int packageId);
    
    /**
     * 根据BusinessId查询业务规则package关系
     * <功能详细描述>
     * @param businessId
     *          businessId
     * @return  List<BusinessPackageRelation>
     * @see [类、类#方法、类#成员]
     */
    public List<BusinessPackageRelation> queryBusinessPackageRelationsByBusinessId(int businessId);
    
    /**
     * 根据scenarioId判断该scenario是否被使用
     * <功能详细描述>
     * @param scenarioId
     *        scenarioId
     * @return  boolean
     * @see [类、类#方法、类#成员]
     */
    public boolean judgeScenarioInUseById(int scenarioId);
}
