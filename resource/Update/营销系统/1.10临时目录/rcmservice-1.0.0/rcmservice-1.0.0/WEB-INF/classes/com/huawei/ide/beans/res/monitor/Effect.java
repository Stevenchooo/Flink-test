package com.huawei.ide.beans.res.monitor;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * url类
 * @author zWX301264
 *
 */
@Component(value = "com.huawei.ide.beans.res.effect.Effect")
@Scope(value = "prototype")
public class Effect
{
    /**
     * 主键
     */
    private int id;
    
    /**
     * 对象名称
     */
    private String name;
    
    /**
     * url
     */
    private String url;
    
    /**
     * 获取id
     * @return id
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * 设置id
     * @param id id
     */
    public void setId(int id)
    {
        this.id = id;
    }
    
    /**
     * 获取name
     * @return name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * 设置name
     * @param name name
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * 获取url
     * @return url
     */
    public String getUrl()
    {
        return url;
    }
    
    /**
     * 设置url
     * @param url url
     */
    public void setUrl(String url)
    {
        this.url = url;
    }
    
}
