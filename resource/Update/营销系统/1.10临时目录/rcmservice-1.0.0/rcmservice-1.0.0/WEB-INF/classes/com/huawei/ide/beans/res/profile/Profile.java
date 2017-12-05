/*
 * 文 件 名:  Profile.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.beans.res.profile;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 系统用户对象类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Component(value = "com.huawei.ide.beans.res.profile.Profile")
@Scope(value = "prototype")
public class Profile
{
    /**
     * 主键
     */
    private int id;
    
    /**
     * 用户ID，采用标准的UUID格式
     */
    private String uuid;
    
    /**
     * 用户名
     */
    private String name;
    
    /**
     * 用户密码
     */
    private String passwd;
    
    /**
     * 用户类型
     * default：1
     * 管理员:0，业务用户:1，一般用户:2
     */
    private int category;
    
    public int getId()
    {
        return id;
    }
    
    public void setId(int id)
    {
        this.id = id;
    }
    
    public String getUuid()
    {
        return uuid;
    }
    
    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getPasswd()
    {
        return passwd;
    }
    
    public void setPasswd(String passwd)
    {
        this.passwd = passwd;
    }
    
    public int getCategory()
    {
        return category;
    }
    
    public void setCategory(int category)
    {
        this.category = category;
    }
    
}
