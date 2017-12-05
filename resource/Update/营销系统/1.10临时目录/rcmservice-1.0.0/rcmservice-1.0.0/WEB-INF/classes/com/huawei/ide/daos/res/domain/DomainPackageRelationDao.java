/*
 * 文 件 名:  DomainPackageRelationDao.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2016年1月30日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.daos.res.domain;

import java.util.List;

import com.huawei.ide.beans.res.domain.DomainPackageRelation;

/**
 * 领域对象package关系数据库操作类
 * 
 * @author z00219375
 * @version [版本号, 2016年1月30日]
 * @see [相关类/方法]
 * @since [Consumer Cloud Big Data Platform Dept]
 */
public interface DomainPackageRelationDao
{
    /**
     * 创建领域对象package关系
     * @param domainPackageRelation domainPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public void createDomainPackageRelation(
            DomainPackageRelation domainPackageRelation);

    /**
     * 更新领域对象package关系
     * @param id id
     * @param domainPackageRelation domainPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public void updateDomainPackageRelation(int id,
            DomainPackageRelation domainPackageRelation);

    /**
     * 删除领域对象package关系
     * @param id id
     * @see [类、类#方法、类#成员]
     */
    public void deleteDomainPackageRelation(int id);

    /**
     * 根据PackageId删除领域对象package关系
     * @param packageId packageId
     * @see [类、类#方法、类#成员]
     */
    public void deleteDomainPackageRelationByPackageId(int packageId);

    /**
     * 根据DomainId删除领域对象package关系
     * @param domainId domainId
     * @see [类、类#方法、类#成员]
     */
    public void deleteDomainPackageRelationByDomainId(int domainId);

    /**
     * 根据PackageId查询领域对象package关系
     * @param packageId packageId
     * @return List<DomainPackageRelation> DomainPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public List<DomainPackageRelation> queryDomainPackageRelationsByPackageId(
            int packageId);

    /**
     * 根据DomainId查询领域对象package关系
     * @param domainId domainId
     * @return List<DomainPackageRelation> DomainPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public List<DomainPackageRelation> queryDomainPackageRelationsByDomainId(
            int domainId);

}
