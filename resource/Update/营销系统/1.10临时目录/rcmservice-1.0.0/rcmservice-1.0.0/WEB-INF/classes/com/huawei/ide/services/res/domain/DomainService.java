/*
 * 文 件 名:  DomainService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月23日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.services.res.domain;

import java.util.List;

import com.huawei.ide.beans.res.domain.Domain;
import com.huawei.ide.beans.res.domain.DomainPackage;
import com.huawei.ide.beans.res.domain.DomainPackageRelation;
import com.huawei.ide.beans.res.domain.DomainProperty;

/**
 * 领域对象数据库服务类
 * 
 * @author z00219375
 * @version [版本号, 2015年12月23日]
 * @see [相关类/方法]
 * @since [Consumer Cloud Big Data Platform Dept]
 */
public interface DomainService
{
    /**
     * 创建领域对象
     * 
     * @param domain
     *            领域对象
     * @see [类、类#方法、类#成员]
     */
    public void createDomain(Domain domain);
    
    /**
     * 更新领域对象
     * 
     * @param id
     *            领域对象id
     * @param domain
     *            领域对象
     * @see [类、类#方法、类#成员]
     */
    public void updateDomain(int id, Domain domain);
    
    /**
     * 删除领域对象
     * 
     * @param id
     *            领域对象id
     * @see [类、类#方法、类#成员]
     */
    public void deleteDomain(int id);
    
    /**
     * 查询指定领域对象
     * 
     * @param id
     *            id
     * @return Domain 领域对象
     * @see [类、类#方法、类#成员]
     */
    public Domain queryDomain(int id);
    
    /**
     * 查询所有领域对象
     * @return List<Domain> 领域对象
     * @see [类、类#方法、类#成员]
     */
    public List<Domain> queryAllDomains();
    
    /**
     * 查询domain领域对象总数
     * @return int 
     * @see [类、类#方法、类#成员]
     */
    public int queryDomainTotalNum();
    
    /**
     * 分页查询指定domain领域对象
     * 
     * @param index
     *            分页查询指定索引，从0开始
     * @param pageSize
     *            分页查询指定的页大小
     * @return List<Domain> [返回类型说明]
     * @see [类、类#方法、类#成员]
     */
    public List<Domain> queryDomainsByPage(int index, int pageSize);
    
    /**
     * 创建领域对象所属package
     * @param domainPackage domainPackage
     * @see [类、类#方法、类#成员]
     */
    public void createDomainPackage(DomainPackage domainPackage);
    
    /**
     * 更新领域对象所属package
     * 
     * @param id id
     * @param domainPackage domainPackage
     * @see [类、类#方法、类#成员]
     */
    public void updateDomainPackage(int id, DomainPackage domainPackage);
    
    /**
     * 删除领域对象所属package
     * 
     * @param id id
     * @see [类、类#方法、类#成员]
     */
    public void deleteDomainPackage(int id);
    
    /**
     * 查询所有领域对象所属package
     * @return List<DomainPackage> DomainPackage
     * @see [类、类#方法、类#成员]
     */
    public List<DomainPackage> queryAllDomainPackages();
    
    /**
     * 创建领域对象package关系
     * @param domainPackageRelation domainPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public void createDomainPackageRelation(DomainPackageRelation domainPackageRelation);
    
    /**
     * 更新领域对象package关系
     * @param id id
     * @param domainPackageRelation domainPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public void updateDomainPackageRelation(int id, DomainPackageRelation domainPackageRelation);
    
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
     * 
     * @param domainId domainId
     * @see [类、类#方法、类#成员]
     */
    public void deleteDomainPackageRelationByDomainId(int domainId);
    
    /**
     * 根据PackageId查询领域对象package关系
     * 
     * @param packageId packageId
     * @return List<DomainPackageRelation> DomainPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public List<DomainPackageRelation> queryDomainPackageRelationsByPackageId(int packageId);
    
    /**
     * 根据DomainId查询领域对象package关系
     * 
     * @param domainId domainId
     * @return List<DomainPackageRelation> DomainPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public List<DomainPackageRelation> queryDomainPackageRelationsByDomainId(int domainId);
    
    /**
     * 新建领域对象属性
     * @param domainProperty domainProperty
     * @see [类、类#方法、类#成员]
     */
    public void createDomainProperty(DomainProperty domainProperty);
    
    /**
     * 更新领域对象属性
     * @param id id 
     * @param domainProperty domainProperty
     * @see [类、类#方法、类#成员]
     */
    public void updateDomainProperty(int id, DomainProperty domainProperty);
    
    /**
     * 删除领域对象属性
     * @param id id
     * @see [类、类#方法、类#成员]
     */
    public void deleteDomainProperty(int id);
    
    /**
     * 根据外键domainId 删除领域对象属性
     *  
     * @param domainId domainId
     * @see [类、类#方法、类#成员]
     */
    public void deleteDomainPropertyByDomainId(int domainId);
    
    /**
     * 查询领域对象属性
     * @param id id
     * @return DomainProperty DomainProperty
     * @see [类、类#方法、类#成员]
     */
    public DomainProperty queryDomainProperty(int id);
    
    /**
     * 查询所有领域对象属性
     * @return List<DomainProperty> DomainProperty
     * @see [类、类#方法、类#成员]
     */
    public List<DomainProperty> queryAllDomainPropertys();
    
    /**
     * 查询指定domainId对应的所有DomainPropertys
     * @param domainId domainId
     * @return List<DomainProperty> DomainProperty
     * @see [类、类#方法、类#成员]
     */
    public List<DomainProperty> queryDomainPropertysByDomainId(int domainId);
    
    /**
     * 查询所有已发布的Domains
     * @return List<Domain> List<Domain>
     * @see [类、类#方法、类#成员]
     */
    public List<Domain> queryAllPublishedDomains();
}
