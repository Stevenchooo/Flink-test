/*
 * 文 件 名:  BusinessServiceImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.services.res.business.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.ide.beans.res.business.Business;
import com.huawei.ide.beans.res.business.BusinessPackage;
import com.huawei.ide.beans.res.business.BusinessPackageRelation;
import com.huawei.ide.beans.res.business.BusinessRule;
import com.huawei.ide.daos.res.business.BusinessDao;
import com.huawei.ide.daos.res.business.BusinessPackageDao;
import com.huawei.ide.daos.res.business.BusinessPackageRelationDao;
import com.huawei.ide.daos.res.business.BusinessPropertyDao;
import com.huawei.ide.daos.res.business.BusinessRuleDao;
import com.huawei.ide.services.res.business.BusinessService;

/**
 * 业务规则对象数据库服务实现类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Service(value = "com.huawei.ide.services.res.business.impl.BusinessServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
public class BusinessServiceImpl implements BusinessService
{
    @Resource(name = "com.huawei.ide.daos.res.business.impl.BusinessDaoImpl")
    private BusinessDao businessDao;
    
    @Resource(name = "com.huawei.ide.daos.res.business.impl.BusinessRuleDaoImpl")
    private BusinessRuleDao businessRuleDao;
    
    @Resource(name = "com.huawei.ide.daos.res.business.impl.BusinessPackageDaoImpl")
    private BusinessPackageDao businessPackageDao;
    
    @Resource(name = "com.huawei.ide.daos.res.business.impl.BusinessPackageRelationDaoImpl")
    private BusinessPackageRelationDao businessPackageRelationDao;
    
    @Resource(name = "com.huawei.ide.daos.res.business.impl.BusinessPropertyDaoImpl")
    private BusinessPropertyDao businessPropertyDao;
    
    /**
     * 新建业务规则
     * @param business
     *          business
     */
    @Override
    public void createBusiness(Business business)
    {
        businessDao.createBusiness(business);
    }
    
    /**
     * 新建业务规则并返回新建规则额的id
     * @param business
     *         business
     * @return  int
     */
    @Override
    public int createBusinessReturnId(Business business)
    {
        return businessDao.createBusinessReturnId(business);
    }
    
    /**
     * 更新业务规则
     * @param id
     *         id
     * @param business
     *        business
     */
    @Override
    public void updateBusiness(int id, Business business)
    {
        businessDao.updateBusiness(id, business);
    }
    
    /**
     * 删除业务规则
     * @param id
     *        id
     */
    @Override
    public void deleteBusiness(int id)
    {
        businessDao.deleteBusiness(id);
    }
    
    /**
     * 根据id查询业务规则
     * @param id
     *         id
     * @return  Business
     */
    @Override
    public Business queryBusiness(int id)
    {
        return businessDao.queryBusiness(id);
    }
    
    /**
     * 通过名称查询业务规则
     * @param name
     *          name
     * @return  Business
     */
    @Override
    public Business queryBusinessByName(String name)
    {
        return businessDao.queryBusinessByName(name);
    }
    
    /**
     * 查询所有的业务规则
     * @return  List<Business>
     */
    @Override
    public List<Business> queryAllBusinesses()
    {
        return businessDao.queryAllBusinesses();
    }
    
    /**
     * 新建业务规则rule
     * @param businessRule
     *         businessRule
     */
    @Override
    public void createBusinessRule(BusinessRule businessRule)
    {
        businessRuleDao.createBusinessRule(businessRule);
    }
    
    /**
     * 更新业务规则rule
     * @param id
     *        id
     * @param businessRule
     *         businessRule
     */
    @Override
    public void updateBusinessRule(int id, BusinessRule businessRule)
    {
        businessRuleDao.updateBusinessRule(id, businessRule);
    }
    
    /**
     * 删除业务规则rule
     * @param id
     *       id
     */
    @Override
    public void deleteBusinessRule(int id)
    {
        businessRuleDao.deleteBusinessRule(id);
    }
    
    /**
     * 通过业务规则id删除业务规则rule
     * @param businessId
     *         businessId
     */
    @Override
    public void deleteBusinessRuleByBusinessId(int businessId)
    {
        businessRuleDao.deleteBusinessRuleByBusinessId(businessId);
    }
    
    /**
     * 通过id查询业务规则rule
     * @param id
     *        id
     * @return  BusinessRule
     */
    @Override
    public BusinessRule queryBusinessRule(int id)
    {
        return businessRuleDao.queryBusinessRule(id);
    }
    
    /**
     * 查询所有的业务规则
     * @return  List<BusinessRule>
     */
    @Override
    public List<BusinessRule> queryAllBusinessRules()
    {
        return businessRuleDao.queryAllBusinessRules();
    }
    
    /**
     * 通过业务规则id查询业务规则rule
     * @param businessId
     *         businessId
     * @return List<BusinessRule>
     */
    @Override
    public List<BusinessRule> queryBusinessRulesByBusinessId(int businessId)
    {
        return businessRuleDao.queryBusinessRulesByBusinessId(businessId);
    }
    
    /**
     * 创建业务规则包
     * @param businessPackage
     *        businessPackage
     */
    @Override
    public void createBusinessPackage(BusinessPackage businessPackage)
    {
        businessPackageDao.createBusinessPackage(businessPackage);
    }
    
    /**
     * 更新业务规则包
     * @param id
     *        id
     * @param businessPackage
     *        businessPackage
     */
    @Override
    public void updateBusinessPackage(int id, BusinessPackage businessPackage)
    {
        businessPackageDao.updateBusinessPackage(id, businessPackage);
    }
    
    /**
     * 删除业务规则包
     * @param id
     *        id
     */
    @Override
    public void deleteBusinessPackage(int id)
    {
        businessPackageDao.deleteBusinessPackage(id);
    }
    
    /**
     * 根据id查询业务规则包
     * @param id
     *        id
     * @return   BusinessPackage
     */
    @Override
    public BusinessPackage queryBusinessPackage(int id)
    {
        return businessPackageDao.queryBusinessPackage(id);
    }
    
    /**
     * 查询所有的业务规则包
     * @return  List<BusinessPackage>
     */
    @Override
    public List<BusinessPackage> queryAllBusinessPackages()
    {
        return businessPackageDao.queryAllBusinessPackages();
    }
    
    /**
     * 创建新的业务规则包关系
     * @param businessPackageRelation
     *         businessPackageRelation
     */
    @Override
    public void createBusinessPackageRelation(BusinessPackageRelation businessPackageRelation)
    {
        businessPackageRelationDao.createBusinessPackageRelation(businessPackageRelation);
    }
    
    /**
     * 更新业务规则包关系
     * @param id
     *         id
     * @param businessPackageRelation
     *        businessPackageRelation
     */
    @Override
    public void updateBusinessPackageRelation(int id, BusinessPackageRelation businessPackageRelation)
    {
        businessPackageRelationDao.updateBusinessPackageRelation(id, businessPackageRelation);
    }
    
    /**
     * 通过id查询业务规则包关系
     * @param id
     *         id
     * @return  BusinessPackageRelation
     */
    @Override
    public BusinessPackageRelation queryBusinessPackageRelationById(int id)
    {
        return businessPackageRelationDao.queryBusinessPackageRelationById(id);
    }
    
    /**
     * 删除业务规则包关系
     * @param id
     *        id
     */
    @Override
    public void deleteBusinessPackageRelation(int id)
    {
        businessPackageRelationDao.deleteBusinessPackageRelation(id);
    }
    
    /**
     * 通过包id删除业务规则包关系
     * @param packageId
     *        packageId
     */
    @Override
    public void deleteBusinessPackageRelationByPackageId(int packageId)
    {
        businessPackageRelationDao.deleteBusinessPackageRelationByPackageId(packageId);
    }
    
    /**
     * 通过业务规则id删除
     * @param businessId
     *        businessId
     */
    @Override
    public void deleteBusinessPackageRelationByBusinessId(int businessId)
    {
        businessPackageRelationDao.deleteBusinessPackageRelationByBusinessId(businessId);
    }
    
    /**
     * 通过包id查询业务规则包关系
     * @param packageId
     *         packageId
     * @return  List<BusinessPackageRelation>
     */
    @Override
    public List<BusinessPackageRelation> queryBusinessPackageRelationsByPackageId(int packageId)
    {
        return businessPackageRelationDao.queryBusinessPackageRelationsByPackageId(packageId);
    }
    
    /**
     * 根据BusinessId查询业务规则package关系
     * @param businessId
     *        businessId
     * @return  List<BusinessPackageRelation>
     */
    @Override
    public List<BusinessPackageRelation> queryBusinessPackageRelationsByBusinessId(int businessId)
    {
        return businessPackageRelationDao.queryBusinessPackageRelationsByBusinessId(businessId);
    }
    
    /**
     * 根据scenarioId判断该scenario是否被使用
     * @param scenarioId
     *         scenarioId
     * @return  boolean
     */
    @Override
    public boolean judgeScenarioInUseById(int scenarioId)
    {
        return businessPropertyDao.judgeScenarioInUseById(scenarioId);
    }
    
}
