/*
 * 文 件 名:  Config.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.beans.res.config;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 系统配置对象类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Component(value = "com.huawei.ide.beans.res.config.Config")
@Scope(value = "prototype")
public class Config
{
    /**
     * 主键
     */
    private int id;
    
    /**
     * 配置名称
     */
    private String name;
    
    /**
     * 配置值
     */
    private String val;
    
    /**
     * 配置默认值
     */
    private String defaultVal;
    
    /**
     * 配置类型
     */
    private String category;
    
    /**
     * 配置级别
     * default：0
     * 数字越小,优先级越高
     */
    private int level;
    
    /**
     * 备注
     */
    private String comment;
    
    /**
     * 获取id
     * @return id 返回 id
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
     * @return name 返回name
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
    
    /**
     * 获取val
     * @return val 返回 val
     */
    public String getVal()
    {
        return val;
    }
    
    /**
     * 设置val
     * @param val 对val进行赋值
     */
    public void setVal(String val)
    {
        this.val = val;
    }
    
    /**
     * 获取defaultVal
     * @return defaultVal 返回 defaultVal
     */
    public String getDefaultVal()
    {
        return defaultVal;
    }
    
    /**
     * 设置defaultVal
     * @param defaultVal 对defaultVal进行赋值
     */
    public void setDefaultVal(String defaultVal)
    {
        this.defaultVal = defaultVal;
    }
    
    /**
     * 获取category
     * @return category 返回 category
     */
    public String getCategory()
    {
        return category;
    }
    
    /**
     * 设置category
     * @param category 对category进行赋值
     */
    public void setCategory(String category)
    {
        this.category = category;
    }
    
    /**
     * 获取level
     * @return level 返回 level
     */
    public int getLevel()
    {
        return level;
    }
    
    /**
     * 设置level
     * @param level 对level进行赋值
     */
    public void setLevel(int level)
    {
        this.level = level;
    }
    
    /**
     * 获取comment
     * @return comment 返回 comment
     */
    public String getComment()
    {
        return comment;
    }
    
    /**
     * 设置comment
     * @param comment 对comment进行赋值
     */
    public void setComment(String comment)
    {
        this.comment = comment;
    }
    
}
