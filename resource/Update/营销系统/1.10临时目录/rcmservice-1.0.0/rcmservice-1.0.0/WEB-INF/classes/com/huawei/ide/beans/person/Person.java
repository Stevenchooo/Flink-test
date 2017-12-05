/*
 * 文 件 名:  Person.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年11月19日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.beans.person;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Person客户
 * @author  z00219375
 * @version  [版本号, 2015年11月19日]
 * @see  [相关类/方法]
 * @since  []
 */
@Component(value = "com.huawei.ide.beans.person.Person")
@Scope(value = "prototype")
public class Person
{
    /*
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Person.class);
    
    /*
     *名字 
     */
    private String name;
    
    /*
     * 性别
     */
    private String sex;
    
    /*
     * 年龄
     */
    private int age;
    
    /*
     * 工作
     */
    private String job;
    
    /*
     * 地址
     */
    private String address;
    
    /**
     * 无参构造函数
     */
    public Person()
    {
    }
    
    /**
     * 有参构造函数
     * @param name  name
     * @param sex sex
     * @param age age
     * @param job job
     * @param address address
     */
    public Person(String name, String sex, int age, String job, String address)
    {
        this.name = name;
        this.sex = sex;
        this.age = age;
        this.job = job;
        this.address = address;
    }
    
    /**
     * 打印信息
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void printPersonInfo()
    {
        LOGGER.info("Person name='" + getName() + "' sex='" + getSex() + "' age='" + getAge() + "' job='" + getJob()
            + "' address='" + getAddress() + "'. ");
    }
    
    /**
     * 打印
     * <功能详细描述>
     * @param message
     *        message
     * @see [类、类#方法、类#成员]
     */
    public static final void print(String message)
    {
        LOGGER.info(message);
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getSex()
    {
        return sex;
    }
    
    public void setSex(String sex)
    {
        this.sex = sex;
    }
    
    public int getAge()
    {
        return age;
    }
    
    public void setAge(int age)
    {
        this.age = age;
    }
    
    public String getJob()
    {
        return job;
    }
    
    public void setJob(String job)
    {
        this.job = job;
    }
    
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    public static Logger getLogger()
    {
        return LOGGER;
    }
    
}
