/*
 * 文 件 名:  BusinessPackageDao.java
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

import com.huawei.ide.beans.res.business.BusinessPackage;

/**
 * 业务规则对象所属package数据库操作类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public interface BusinessPackageDao
{
    /**
     * 创建业务规则对象所属package
     * <功能详细描述>
     * @param businessPackage
     *        businessPackage
     * @see [类、类#方法、类#成员]
     */
    public void createBusinessPackage(BusinessPackage businessPackage);
    
    /**
     * 更新业务规则对象所属package
     * <功能详细描述>
     * @param id
     *        id
     * @param businessPackage
     *        businessPackage
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
     *        id
     * @return  BusinessPackage
     * @see [类、类#方法、类#成员]
     */
    public BusinessPackage queryBusinessPackage(int id);
    
    /**
     * 查询所有业务规则对象所属package
     * <功能详细描述>
     * @return   List<BusinessPackage>
     * @see [类、类#方法、类#成员]
     */
    public List<BusinessPackage> queryAllBusinessPackages();
}
