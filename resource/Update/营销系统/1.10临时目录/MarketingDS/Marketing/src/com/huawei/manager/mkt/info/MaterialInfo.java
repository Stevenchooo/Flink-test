/*
 * 文 件 名:  MaterialInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-7-26
 */
package com.huawei.manager.mkt.info;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-7-26]
 * @see  [相关类/方法]
 */
public class MaterialInfo
{
    //广告位ID
    private Integer adInfoId;
    
    //营销活动名称
    private String mktinfoName;
    
    //媒体类型
    private String adInfoMediaType;
    
    //网站名称
    private String adInfoWebName;
    
    //渠道
    private String adInfoChannel;
    
    //广告位
    private String adInfoPosition;
    
    //素材名称
    private String materialName;
    
    //素材存放目录
    private String materialPath;
    
    public Integer getAdInfoId()
    {
        return adInfoId;
    }
    
    public void setAdInfoId(Integer adInfoId)
    {
        this.adInfoId = adInfoId;
    }
    
    public String getMktinfoName()
    {
        return mktinfoName;
    }
    
    public void setMktinfoName(String mktinfoName)
    {
        this.mktinfoName = mktinfoName;
    }
    
    public String getAdInfoMediaType()
    {
        return adInfoMediaType;
    }
    
    public void setAdInfoMediaType(String adInfoMediaType)
    {
        this.adInfoMediaType = adInfoMediaType;
    }
    
    public void setAdInfoWebName(String adInfoWebName)
    {
        this.adInfoWebName = adInfoWebName;
    }
    
    public String getAdInfoWebName()
    {
        return adInfoWebName;
    }
    
    public String getAdInfoChannel()
    {
        return adInfoChannel;
    }
    
    public void setAdInfoChannel(String adInfoChannel)
    {
        this.adInfoChannel = adInfoChannel;
    }
    
    public String getMaterialName()
    {
        return materialName;
    }
    
    public void setMaterialName(String materialName)
    {
        this.materialName = materialName;
    }
    
    public String getAdInfoPosition()
    {
        return adInfoPosition;
    }
    
    public void setAdInfoPosition(String adInfoPosition)
    {
        this.adInfoPosition = adInfoPosition;
    }
    
    public String getMaterialPath()
    {
        return materialPath;
    }
    
    public void setMaterialPath(String materialPath)
    {
        this.materialPath = materialPath;
    }
    
    /**
     * 转字符串
     * @return   字符串
     */
    @Override
    public String toString()
    {
        return "MaterialInfo [adInfoId=" + adInfoId + ", mktinfoName=" + mktinfoName + ", adInfoMediaType="
            + adInfoMediaType + ", adInfoWebName=" + adInfoWebName + ", adInfoChannel=" + adInfoChannel
            + ", adInfoPosition=" + adInfoPosition + ", materialName=" + materialName + ", materialPath="
            + materialPath + "]";
    }
    
}
