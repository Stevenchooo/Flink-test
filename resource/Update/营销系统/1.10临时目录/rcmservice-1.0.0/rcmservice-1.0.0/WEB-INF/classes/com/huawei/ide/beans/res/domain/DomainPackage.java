/*
 * 文 件 名:  DomainPackage.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月23日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.beans.res.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 领域对象所属package
 * @author  z00219375
 * @version  [版本号, 2015年12月23日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Component(value = "com.huawei.ide.beans.res.domain.DomainPackage")
@Scope(value = "prototype")
public class DomainPackage
{
    /**
     * 主键
     */
    private int id;
    
    /**
     * 领域名称
     */
    private String name;
    
    /**
     * 获取id
     * @return 返回 id
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * 设置id
     * @param id 对id进行赋值
     */
    public void setId(int id)
    {
        this.id = id;
    }
    
    /**
     * 获取name
     * @return 返回 name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * 设置name
     * @param name 对name进行赋值
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
}
