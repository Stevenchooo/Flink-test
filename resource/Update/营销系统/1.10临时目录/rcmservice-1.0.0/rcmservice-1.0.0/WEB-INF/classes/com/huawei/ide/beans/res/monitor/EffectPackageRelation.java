package com.huawei.ide.beans.res.monitor;

/**
 * url package关系类
 * @author zWX301264
 *
 */
public class EffectPackageRelation
{
    /**
     * 主键id
     */
    private int id;
    
    /**
     * packageId
     */
    private int packageId;
    
    /**
     * urlId
     */
    private int urlId;
    
    /**
     * 对象名称
     */
    private String packageName;
    
    /**
     * url
     */
    private String urlName;
    
    /**
     * url
     */
    private String url;
    
    /**
     * 
     * type
     */
    private String type;
    
    /**
     * 返回 id
     * @return id 返回 id
     */
    public int getId()
    {
        return id;
    }
    
    /**
     * 对id进行赋值
     * @param id 对id进行赋值
     */
    public void setId(int id)
    {
        this.id = id;
    }
    
    /**
     * 获取packageId
     * @return packageId 返回 packageId
     */
    public int getPackageId()
    {
        return packageId;
    }
    
    /**
     * 设置packageId
     * @param packageId 对packageId进行赋值
     */
    public void setPackageId(int packageId)
    {
        this.packageId = packageId;
    }
    
    /**
     * 获取urlId
     * @return urlId 返回 urlId
     */
    public int getUrlId()
    {
        return urlId;
    }
    
    /**
     * 设置urlId
     * @param urlId 对urlId进行赋值
     */
    public void setUrlId(int urlId)
    {
        this.urlId = urlId;
    }
    
    /**
     * 获取packageName
     * @return packageName 返回 packageName
     */
    public String getPackageName()
    {
        return packageName;
    }
    
    /**
     * 设置packageName
     * @param packageName 对packageName进行赋值
     */
    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }
    
    /**
     * 获取urlName
     * @return urlName 返回 urlName
     */
    public String getUrlName()
    {
        return urlName;
    }
    
    /**
     * 设置urlName
     * @param urlName 对urlName进行赋值
     */
    public void setUrlName(String urlName)
    {
        this.urlName = urlName;
    }
    
    /**
     * 获取url
     * @return url 返回 url
     */
    public String getUrl()
    {
        return url;
    }
    
    /**
     * 设置url
     * @param url 对url进行赋值
     */
    public void setUrl(String url)
    {
        this.url = url;
    }
    
    /**
     * 获取type
     * @return type 返回 type
     */
    public String getType()
    {
        return type;
    }
    
    /**
     * 设置type
     * @param type 对type进行赋值
     */
    public void setType(String type)
    {
        this.type = type;
    }
    
}
