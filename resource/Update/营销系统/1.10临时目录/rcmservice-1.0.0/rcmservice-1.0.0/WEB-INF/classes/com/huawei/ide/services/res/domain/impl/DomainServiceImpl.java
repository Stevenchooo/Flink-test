/*
 * 文 件 名:  DomainServiceImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月23日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.services.res.domain.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.ide.beans.res.domain.Domain;
import com.huawei.ide.beans.res.domain.DomainPackage;
import com.huawei.ide.beans.res.domain.DomainPackageRelation;
import com.huawei.ide.beans.res.domain.DomainProperty;
import com.huawei.ide.daos.res.domain.DomainDao;
import com.huawei.ide.daos.res.domain.DomainPackageDao;
import com.huawei.ide.daos.res.domain.DomainPackageRelationDao;
import com.huawei.ide.daos.res.domain.DomainPropertyDao;
import com.huawei.ide.services.res.domain.DomainService;

/**
 * 领域对象数据库服务实现类
 * 
 * @author z00219375
 * @version [版本号, 2015年12月23日]
 * @see [相关类/方法]
 * @since [Consumer Cloud Big Data Platform Dept]
 */
@Service(value = "com.huawei.ide.services.res.domain.impl.DomainServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
public class DomainServiceImpl implements DomainService
{
    @Resource(name = "com.huawei.ide.daos.res.domain.impl.DomainDaoImpl")
    private DomainDao domainDao;
    
    @Resource(name = "com.huawei.ide.daos.res.domain.impl.DomainPackageDaoImpl")
    private DomainPackageDao domainPackageDao;
    
    @Resource(name = "com.huawei.ide.daos.res.domain.impl.DomainPackageRelationDaoImpl")
    private DomainPackageRelationDao domainPackageRelationDao;
    
    @Resource(name = "com.huawei.ide.daos.res.domain.impl.DomainPropertyDaoImpl")
    private DomainPropertyDao domainPropertyDao;
    
    /**
     * 创建领域对象
     * 
     * @param domain
     *            领域对象
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void createDomain(Domain domain)
    {
        domainDao.createDomain(domain);
    }
    
    /**
     * 更新领域对象
     * 
     * @param id
     *            领域对象id
     * @param domain
     *            领域对象
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void updateDomain(int id, Domain domain)
    {
        domainDao.updateDomain(id, domain);
    }
    
    /**
     * 删除领域对象
     * 
     * @param id
     *            领域对象id
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteDomain(int id)
    {
        domainDao.deleteDomain(id);
    }
    
    /**
     * 查询指定领域对象
     * 
     * @param id
     *            id
     * @return Domain 领域对象
     * @see [类、类#方法、类#成员]
     */
    @Override
    public Domain queryDomain(int id)
    {
        return domainDao.queryDomain(id);
    }
    
    /**
     * 查询所有领域对象
     * @return List<Domain> 领域对象
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Domain> queryAllDomains()
    {
        return domainDao.queryAllDomains();
    }
    
    /**
     * 查询domain领域对象总数
     * @return int 
     * @see [类、类#方法、类#成员]
     */
    @Override
    public int queryDomainTotalNum()
    {
        return domainDao.queryDomainTotalNum();
    }
    
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
    @Override
    public List<Domain> queryDomainsByPage(int index, int pageSize)
    {
        return domainDao.queryDomainsByPage(index, pageSize);
    }
    
    /**
     * 创建领域对象所属package
     * @param domainPackage domainPackage
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void createDomainPackage(DomainPackage domainPackage)
    {
        domainPackageDao.createDomainPackage(domainPackage);
    }
    
    /**
     * 更新领域对象所属package
     * 
     * @param id id
     * @param domainPackage domainPackage
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void updateDomainPackage(int id, DomainPackage domainPackage)
    {
        domainPackageDao.updateDomainPackage(id, domainPackage);
    }
    
    /**
     * 删除领域对象所属package
     * 
     * @param id id
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteDomainPackage(int id)
    {
        domainPackageDao.deleteDomainPackage(id);
    }
    
    /**
     * 查询所有领域对象所属package
     * @return List<DomainPackage> DomainPackage
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<DomainPackage> queryAllDomainPackages()
    {
        return domainPackageDao.queryAllDomainPackages();
    }
    
    /**
     * 创建领域对象package关系
     * @param domainPackageRelation domainPackageRelation
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void createDomainPackageRelation(DomainPackageRelation domainPackageRelation)
    {
        domainPackageRelationDao.createDomainPackageRelation(domainPackageRelation);
    }
    
    /**
     * 更新领域对象package关系
     * @param id id
     * @param domainPackageRelation domainPackageRelation
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void updateDomainPackageRelation(int id, DomainPackageRelation domainPackageRelation)
    {
        domainPackageRelationDao.updateDomainPackageRelation(id, domainPackageRelation);
    }
    
    /**
     * 删除领域对象package关系
     * @param id id
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteDomainPackageRelation(int id)
    {
        domainPackageRelationDao.deleteDomainPackageRelation(id);
    }
    
    /**
     * 根据PackageId删除领域对象package关系
     * @param packageId packageId
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteDomainPackageRelationByPackageId(int packageId)
    {
        domainPackageRelationDao.deleteDomainPackageRelationByPackageId(packageId);
    }
    
    /**
     * 根据DomainId删除领域对象package关系
     * 
     * @param domainId domainId
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteDomainPackageRelationByDomainId(int domainId)
    {
        domainPackageRelationDao.deleteDomainPackageRelationByDomainId(domainId);
    }
    
    /**
     * 根据PackageId查询领域对象package关系
     * 
     * @param packageId packageId
     * @return List<DomainPackageRelation> DomainPackageRelation
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<DomainPackageRelation> queryDomainPackageRelationsByPackageId(int packageId)
    {
        return domainPackageRelationDao.queryDomainPackageRelationsByPackageId(packageId);
    }
    
    /**
     * 根据DomainId查询领域对象package关系
     * 
     * @param domainId
     *        domainId
     * @return List<DomainPackageRelation> DomainPackageRelation
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<DomainPackageRelation> queryDomainPackageRelationsByDomainId(int domainId)
    {
        return domainPackageRelationDao.queryDomainPackageRelationsByDomainId(domainId);
    }
    
    /**
     * 新建领域对象属性
     * @param domainProperty domainProperty
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void createDomainProperty(DomainProperty domainProperty)
    {
        domainPropertyDao.createDomainProperty(domainProperty);
    }
    
    /**
     * 更新领域对象属性
     * @param id id 
     * @param domainProperty domainProperty
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void updateDomainProperty(int id, DomainProperty domainProperty)
    {
        domainPropertyDao.updateDomainProperty(id, domainProperty);
    }
    
    /**
     * 删除领域对象属性
     * @param id id
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteDomainProperty(int id)
    {
        domainPropertyDao.deleteDomainProperty(id);
    }
    
    /**
     * 根据外键domainId 删除领域对象属性
     *  
     * @param domainId domainId
     * @see [类、类#方法、类#成员]
     */
    @Override
    public void deleteDomainPropertyByDomainId(int domainId)
    {
        domainPropertyDao.deleteDomainPropertyByDomainId(domainId);
    }
    
    /**
     * 查询领域对象属性
     * @param id id
     * @return DomainProperty DomainProperty
     * @see [类、类#方法、类#成员]
     */
    @Override
    public DomainProperty queryDomainProperty(int id)
    {
        return domainPropertyDao.queryDomainProperty(id);
    }
    
    /**
     * 查询所有领域对象属性
     * @return List<DomainProperty> DomainProperty
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<DomainProperty> queryAllDomainPropertys()
    {
        return domainPropertyDao.queryAllDomainPropertys();
    }
    
    /**
     * 查询指定domainId对应的所有DomainPropertys
     * @param domainId domainId
     * @return List<DomainProperty> DomainProperty
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<DomainProperty> queryDomainPropertysByDomainId(int domainId)
    {
        return domainPropertyDao.queryDomainPropertysByDomainId(domainId);
    }
    
    /**
     * 查询所有已发布的Domains
     * @return List<Domain> List<Domain>
     * @see [类、类#方法、类#成员]
     */
    @Override
    public List<Domain> queryAllPublishedDomains()
    {
        return domainDao.queryAllPublishedDomains();
    }
    
}
