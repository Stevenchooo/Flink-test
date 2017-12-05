/*
 * 文 件 名:  PersonDao.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月7日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.daos.person;

import java.util.List;

import com.huawei.ide.beans.person.Person;

/**
 * 客户对象数据库操作类
 * @author  z00219375
 * @version  [版本号, 2015年12月7日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public interface PersonDao
{
    /**
     * 创建表
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void createTable();
    
    /**
     * 删除表
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void deleteTable();
    
    /**
     * 创建客户
     * <功能详细描述>
     * @param person
     *        person
     * @see [类、类#方法、类#成员]
     */
    public void createPerson(Person person);
    
    /**
     * 更新客户
     * <功能详细描述>
     * @param name
     *        name
     * @param person
     *        person
     * @see [类、类#方法、类#成员]
     */
    public void updatePerson(String name, Person person);
    
    /**
     * 删除客户
     * <功能详细描述>
     * @param name
     *        name
     * @see [类、类#方法、类#成员]
     */
    public void deletePerson(String name);
    
    /**
     * 查询客户
     * <功能详细描述>
     * @param name
     *        name
     * @return  List<Person>
     * @see [类、类#方法、类#成员]
     */
    public List<Person> queryPerson(String name);
}
