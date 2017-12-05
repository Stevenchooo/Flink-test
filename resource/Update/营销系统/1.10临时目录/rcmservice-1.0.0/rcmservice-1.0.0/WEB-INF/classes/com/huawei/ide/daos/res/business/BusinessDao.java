/*
 * 文 件 名:  BusinessDao.java
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

import com.huawei.ide.beans.res.business.Business;

/**
 * 业务规则对象数据库操作类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public interface BusinessDao
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
     * <功能详细描述>
     * @param business
     *        business
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
     *        id
     * @see [类、类#方法、类#成员]
     */
    public void deleteBusiness(int id);
    
    /**
     * 查询指定业务规则对象
     * <功能详细描述>
     * @param id
     *        id
     * @return  Business
     * @see [类、类#方法、类#成员]
     */
    public Business queryBusiness(int id);
    
    /**
     * 根据Business查询指定业务对象
     * <功能详细描述>
     * @param name
     *        name
     * @return   Business
     * @see [类、类#方法、类#成员]
     */
    public Business queryBusinessByName(String name);
    
    /**
     * 查询所有业务规则对象
     * <功能详细描述>
     * @return  List<Business>
     * @see [类、类#方法、类#成员]
     */
    public List<Business> queryAllBusinesses();
}
