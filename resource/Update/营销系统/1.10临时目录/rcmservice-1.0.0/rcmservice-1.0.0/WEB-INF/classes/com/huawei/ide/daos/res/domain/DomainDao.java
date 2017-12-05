package com.huawei.ide.daos.res.domain;

import java.util.List;

import com.huawei.ide.beans.res.domain.Domain;

/**
 * 领域对象数据库接口类
 * @author  z00219375
 * @version  [版本号, 2015年12月23日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public interface DomainDao
{
    /**
     * 创建领域对象
     * @param domain domain
     * @see [类、类#方法、类#成员]
     */
    public void createDomain(Domain domain);
    
    /**
     * 更新领域对象
     * @param id id
     * @param domain domain
     * @see [类、类#方法、类#成员]
     */
    public void updateDomain(int id, Domain domain);
    
    /**
     * 删除领域对象
     * @param id id
     * @see [类、类#方法、类#成员]
     */
    public void deleteDomain(int id);
    
    /**
     * 查询指定领域对象
     * @param id id
     * @return Domain domain
     * @see [类、类#方法、类#成员]
     */
    public Domain queryDomain(int id);
    
    /**
     * 查询所有领域对象
     * @return List<Domain> domain
     * @see [类、类#方法、类#成员]
     */
    public List<Domain> queryAllDomains();
    
    /**
     * 查询domain领域对象总数
     * @return int domain领域对象总数
     * @see [类、类#方法、类#成员]
     */
    public int queryDomainTotalNum();
    
    /**
     * 分页查询指定domain领域对象
     * @param index  分页查询指定索引，从0开始
     * @param pageSize  分页查询指定的页大小
     * @return List<Domain> domain
     * @see [类、类#方法、类#成员]
     */
    public List<Domain> queryDomainsByPage(int index, int pageSize);
    
    /**
     * 查询所有已发布的Domains
     * @return List<Domain> domain
     * @see [类、类#方法、类#成员]
     */
    public List<Domain> queryAllPublishedDomains();
}
