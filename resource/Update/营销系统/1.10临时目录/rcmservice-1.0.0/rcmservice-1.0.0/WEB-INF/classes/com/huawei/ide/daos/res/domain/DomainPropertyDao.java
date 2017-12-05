/*
 * 文 件 名:  DomainPropertyDao.java
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

import com.huawei.ide.beans.res.domain.DomainProperty;

/**
 * 领域对象属性数据库操作类
 * @author  z00219375
 * @version  [版本号, 2015年12月23日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public interface DomainPropertyDao
{
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
     * 根据外键domainId
     * 删除领域对象属性
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
}
