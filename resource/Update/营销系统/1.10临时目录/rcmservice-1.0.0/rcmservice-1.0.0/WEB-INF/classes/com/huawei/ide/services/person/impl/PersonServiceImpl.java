/*
 * 文 件 名:  PersonServiceImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年11月27日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.services.person.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.ide.beans.person.Person;
import com.huawei.ide.daos.person.PersonDao;
import com.huawei.ide.services.person.PersonService;

/*
 * @Transactional注解中的属性：
 * propagation : 事务的传播行为
 * isolation : 事务的隔离级别
 * readOnly : 只读
 * rollbackFor : 发生哪些异常回滚
 * noRollbackFor : 发生哪些异常不回滚
 */
/**
 * 
 * PersonServiceImpl
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
 * @see  [相关类/方法]
 */
@Service(value = "com.huawei.ide.services.person.impl.PersonServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
public class PersonServiceImpl implements PersonService
{
    @Resource(name = "com.huawei.ide.daos.person.impl.PersonDaoImpl")
    private PersonDao personDao;
    
    /**
     * 新增
     */
    @Override
    public void addPerson()
    {
        personDao.createPerson(new Person("jack1", "male", 18, "worker1", "nanjing1"));
        personDao.createPerson(new Person("jack2", "female", 19, "worker1", "nanjing2"));
    }
    
    /**
     * 修改
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
    @Override
    public void updatePerson()
    {
        personDao.updatePerson("jack1", new Person("jack1", "male", 21, "worker1", "nanjing1"));
        //int i = 9 / 0;
        personDao.updatePerson("jack2", new Person("jack2", "female", 22, "worker1", "nanjing2"));
    }
    
    /**
     * 删除
     */
    @Override
    public void deletePerson()
    {
        personDao.deletePerson("jack1");
    }
    
    /**
     * 查询
     * @return  List<Person>
     */
    @Override
    public List<Person> queryPersons()
    {
        return personDao.queryPerson("jack2");
    }
}
