/*
 * 文 件 名:  PersonService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年11月27日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.services.person;

import java.util.List;

import com.huawei.ide.beans.person.Person;

/**
 * PersonService
 * @author  z00219375
 * @version  [版本号, 2015年11月27日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public interface PersonService
{
    /**
     * 新增用户
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void addPerson();
    
    /**
     * 更新用户
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void updatePerson();
    
    /**
     * 删除用户
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void deletePerson();
    
    /**
     * 查询所有用户
     * <功能详细描述>
     * @return  List<Person>
     * @see [类、类#方法、类#成员]
     */
    public List<Person> queryPersons();
}
