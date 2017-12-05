/*
 * 文 件 名:  LandTemplateInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-6-2
 */
package com.huawei.manager.mkt.info;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-2]
 * @see  [相关类/方法]
 */
public class LandTemplateInfo
{
    //广告位ID
    private Integer aid;
    
    //广告位ID字符
    private String aidStr;
    
    //sid
    private String sid;
    
    //cps提供商名称
    private String cpsName;
    
    //source
    private String source;
    
    //渠道名称
    private String channelName;
    
    //channel
    private String channel;
    
    //cid
    private String cid;
    
    //着陆链接
    private String landUrl;
    
    public String getAidStr()
    {
        return aidStr;
    }
    
    public void setAidStr(String aidStr)
    {
        this.aidStr = aidStr;
    }
    
    public Integer getAid()
    {
        return aid;
    }
    
    public void setAid(Integer aid)
    {
        this.aid = aid;
    }
    
    public void setSid(String sid)
    {
        this.sid = sid;
    }
    
    public String getSid()
    {
        return sid;
    }
    
    public String getSource()
    {
        return source;
    }
    
    public void setSource(String source)
    {
        this.source = source;
    }
    
    public String getCpsName()
    {
        return cpsName;
    }
    
    public void setCpsName(String cpsName)
    {
        this.cpsName = cpsName;
    }
    
    public String getCid()
    {
        return cid;
    }
    
    public void setCid(String cid)
    {
        this.cid = cid;
    }
    
    public String getChannelName()
    {
        return channelName;
    }
    
    public void setChannelName(String channelName)
    {
        this.channelName = channelName;
    }
    
    public String getChannel()
    {
        return channel;
    }
    
    public void setChannel(String channel)
    {
        this.channel = channel;
    }
    
    public String getLandUrl()
    {
        return landUrl;
    }
    
    public void setLandUrl(String landUrl)
    {
        this.landUrl = landUrl;
    }
    
    /**
     * 转字符串
     * @return      字符串
     */
    @Override
    public String toString()
    {
        return "LandTemplateInfo [aid=" + aid + ", aidStr=" + aidStr + ", sid=" + sid + ", cpsName=" + cpsName
            + ", source=" + source + ", channelName=" + channelName + ", channel=" + channel + ", cid=" + cid
            + ", landUrl=" + landUrl + "]";
    }
    
}
