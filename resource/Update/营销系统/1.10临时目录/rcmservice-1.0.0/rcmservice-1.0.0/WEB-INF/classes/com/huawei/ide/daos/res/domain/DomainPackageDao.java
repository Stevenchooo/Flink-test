/*
 * 文 件 名:  DomainPackageDao.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月23日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.daos.res.domain;

import java.util.List;

import com.huawei.ide.beans.res.domain.DomainPackage;

/**
 * 领域对象所属package数据库操作类
 * @author  z00219375
 * @version  [版本号, 2015年12月23日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public interface DomainPackageDao
{
    /**
     * 创建领域对象所属package
     * @param domainPackage domainPackage
     * @see [类、类#方法、类#成员]
     */
    public void createDomainPackage(DomainPackage domainPackage);
    
    /**
     * 更新领域对象所属package
     * @param id id
     * @param domainPackage domainPackage
     * @see [类、类#方法、类#成员]
     */
    public void updateDomainPackage(int id, DomainPackage domainPackage);
    
    /**
     * 删除领域对象所属package
     * @param id id
     * @see [类、类#方法、类#成员]
     */
    public void deleteDomainPackage(int id);
    
    /**
     * 查询所有领域对象所属package
     * @return List<DomainPackage> domainPackage
     * @see [类、类#方法、类#成员]
     */
    public List<DomainPackage> queryAllDomainPackages();
    
}
