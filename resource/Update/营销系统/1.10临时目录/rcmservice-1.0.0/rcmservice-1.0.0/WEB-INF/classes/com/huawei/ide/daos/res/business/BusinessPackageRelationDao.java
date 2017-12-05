/*
 * 文 件 名:  BusinessPackageRelationDao.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2016年2月17日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.daos.res.business;

import java.util.List;

import com.huawei.ide.beans.res.business.BusinessPackageRelation;

/**
 * 业务规则package关系数据库操作类
 * @author  z00219375
 * @version  [版本号, 2016年2月17日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public interface BusinessPackageRelationDao
{
    /**
     * 创建业务规则package关系
     * <功能详细描述>
     * @param businessPackageRelation
     *         businessPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public void createBusinessPackageRelation(BusinessPackageRelation businessPackageRelation);
    
    /**
     * 更新业务规则package关系
     * <功能详细描述>
     * @param id
     *        id
     * @param businessPackageRelation
     *         businessPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public void updateBusinessPackageRelation(int id, BusinessPackageRelation businessPackageRelation);
    
    /**
     * 查询业务规则package关系
     * <功能详细描述>
     * @param id
     *        id
     * @return   BusinessPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public BusinessPackageRelation queryBusinessPackageRelationById(int id);
    
    /**
     * 删除业务规则package关系
     * <功能详细描述>
     * @param id
     *        id
     * @see [类、类#方法、类#成员]
     */
    public void deleteBusinessPackageRelation(int id);
    
    /**
     * 根据PackageId删除业务规则package关系
     * <功能详细描述>
     * @param packageId
     *        packageId
     * @see [类、类#方法、类#成员]
     */
    public void deleteBusinessPackageRelationByPackageId(int packageId);
    
    /**
     * 根据BusinessId删除业务规则package关系
     * <功能详细描述>
     * @param businessId
     *        businessId
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
     *         businessId
     * @return  List<BusinessPackageRelation>
     * @see [类、类#方法、类#成员]
     */
    public List<BusinessPackageRelation> queryBusinessPackageRelationsByBusinessId(int businessId);
}
