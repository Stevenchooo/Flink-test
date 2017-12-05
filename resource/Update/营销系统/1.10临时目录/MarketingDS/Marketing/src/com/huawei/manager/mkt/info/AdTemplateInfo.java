/*
 * 文 件 名:  AdTemplateInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-5-20
 */
package com.huawei.manager.mkt.info;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-5-20]
 * @see  [相关类/方法]
 */
public class AdTemplateInfo
{
    //广告位ID
    private Integer aid;
    
    //广告位ID字符
    private String aidStr;
    
    //营销活动名称
    private String mktName;
    
    //营销活动名称ID
    private Integer mktId;
    
    //媒体类型
    private String mediaType;
    
    //媒体类型ID
    private Integer mediaTypeId;
    
    //网站名称
    private String webName;
    
    //网站名称ID
    private Integer webNameId;
    
    //频道
    private String adChannel;
    
    //广告位
    private String adPosition;
    
    //广告素材类型
    private String materialType;
    
    //素材要求
    private String materialDesc;
    
    //广告素材类型ID
    private String materialTypeId;
    
    //端口所属
    private String port;
    
    //端口所属ID
    private String portId;
    
    //着陆平台
    private String platform;
    
    //着陆平台ID
    private String platformId;
    
    //着陆页面描述
    private String platformDesc;
    
    //投放天数字符
    private String deliveryDaysStr;
    
    //投放天数
    private Integer deliveryDays;
    
    //投放日期
    private String deliveryTimes;
    
    //投放开始日期
    private String deliveryBeginDay;
    
    //投放截止日期
    private String deliveryEndDay;
    
    //引流类型
    private String flowType;
    
    //引流类型ID
    private String flowTypeId;
    
    //预计曝光量字符
    private String expAmountStr;
    
    //预计曝光量
    private Integer expAmount;
    
    //预计点击量字符
    private String clickAmountStr;
    
    //预计点击量
    private Integer clickAmount;
    
    //刊例价字符
    private String publishPriceStr;
    
    //刊例价
    private Integer publishPrice;
    
    //净价字符
    private String netPriceStr;
    
    //净价
    private Integer netPrice;
    
    //资源来源
    private String resource;
    
    //资源来源ID
    private String resourceId;
    
    //是否监控曝光
    private String isExposure;
    
    //是否监控曝光ID
    private String isExposureId;
    
    //是否监控点击
    private String isClick;
    
    //是否监控点击ID
    private String isClickId;
    
    //监控平台
    private String monitorPlatform;
    
    //监控平台ID
    private String monitorPlatformId;
    
    //sid
    private String sid;
    
    //cps提供商名称
    private String cpsName;
    
    //source
    private String source;
    
    //渠道名称
    private String landChannelName;
    
    //channel
    private String landChannel;
    
    //着陆链接
    private String landUrl;
    
    //cid
    private String cid;
    
    //活动属性
    private Integer deptType;
    
    public Integer getDeptType()
    {
        return deptType;
    }
    
    public void setDeptType(Integer deptType)
    {
        this.deptType = deptType;
    }
    
    public Integer getAid()
    {
        return aid;
    }
    
    public void setAid(Integer aid)
    {
        this.aid = aid;
    }
    
    public String getAidStr()
    {
        return aidStr;
    }
    
    public void setAidStr(String aidStr)
    {
        this.aidStr = aidStr;
    }
    
    public Integer getMktId()
    {
        return mktId;
    }
    
    public void setMktId(Integer mktId)
    {
        this.mktId = mktId;
    }
    
    public String getMktName()
    {
        return mktName;
    }
    
    public void setMktName(String mktName)
    {
        this.mktName = mktName;
    }
    
    public Integer getMediaTypeId()
    {
        return mediaTypeId;
    }
    
    public void setMediaTypeId(Integer mediaTypeId)
    {
        this.mediaTypeId = mediaTypeId;
    }
    
    public String getMediaType()
    {
        return mediaType;
    }
    
    public void setMediaType(String mediaType)
    {
        this.mediaType = mediaType;
    }
    
    public String getWebName()
    {
        return webName;
    }
    
    public void setWebName(String webName)
    {
        this.webName = webName;
    }
    
    public Integer getWebNameId()
    {
        return webNameId;
    }
    
    public void setWebNameId(Integer webNameId)
    {
        this.webNameId = webNameId;
    }
    
    public String getAdChannel()
    {
        return adChannel;
    }
    
    public void setAdChannel(String adChannel)
    {
        this.adChannel = adChannel;
    }
    
    public String getAdPosition()
    {
        return adPosition;
    }
    
    public void setAdPosition(String adPosition)
    {
        this.adPosition = adPosition;
    }
    
    public String getMaterialType()
    {
        return materialType;
    }
    
    public void setMaterialType(String materialType)
    {
        this.materialType = materialType;
    }
    
    public String getMaterialDesc()
    {
        return materialDesc;
    }
    
    public void setMaterialDesc(String materialDesc)
    {
        this.materialDesc = materialDesc;
    }
    
    public String getMaterialTypeId()
    {
        return materialTypeId;
    }
    
    public void setMaterialTypeId(String materialTypeId)
    {
        this.materialTypeId = materialTypeId;
    }
    
    public String getPort()
    {
        return port;
    }
    
    public void setPort(String port)
    {
        this.port = port;
    }
    
    public String getPortId()
    {
        return portId;
    }
    
    public void setPortId(String portId)
    {
        this.portId = portId;
    }
    
    public String getPlatform()
    {
        return platform;
    }
    
    public void setPlatform(String platform)
    {
        this.platform = platform;
    }
    
    public String getPlatformId()
    {
        return platformId;
    }
    
    public void setPlatformId(String platformId)
    {
        this.platformId = platformId;
    }
    
    public String getPlatformDesc()
    {
        return platformDesc;
    }
    
    public void setPlatformDesc(String platformDesc)
    {
        this.platformDesc = platformDesc;
    }
    
    public String getDeliveryDaysStr()
    {
        return deliveryDaysStr;
    }
    
    public void setDeliveryDaysStr(String deliveryDaysStr)
    {
        this.deliveryDaysStr = deliveryDaysStr;
    }
    
    public Integer getDeliveryDays()
    {
        return deliveryDays;
    }
    
    public void setDeliveryDays(Integer deliveryDays)
    {
        this.deliveryDays = deliveryDays;
    }
    
    public String getDeliveryTimes()
    {
        return deliveryTimes;
    }
    
    public void setDeliveryTimes(String deliveryTimes)
    {
        this.deliveryTimes = deliveryTimes;
    }
    
    public String getDeliveryBeginDay()
    {
        return deliveryBeginDay;
    }
    
    public void setDeliveryBeginDay(String deliveryBeginDay)
    {
        this.deliveryBeginDay = deliveryBeginDay;
    }
    
    public String getDeliveryEndDay()
    {
        return deliveryEndDay;
    }
    
    public void setDeliveryEndDay(String deliveryEndDay)
    {
        this.deliveryEndDay = deliveryEndDay;
    }
    
    public String getFlowType()
    {
        return flowType;
    }
    
    public void setFlowType(String flowType)
    {
        this.flowType = flowType;
    }
    
    public String getFlowTypeId()
    {
        return flowTypeId;
    }
    
    public void setFlowTypeId(String flowTypeId)
    {
        this.flowTypeId = flowTypeId;
    }
    
    public String getExpAmountStr()
    {
        return expAmountStr;
    }
    
    public void setExpAmountStr(String expAmountStr)
    {
        this.expAmountStr = expAmountStr;
    }
    
    public Integer getExpAmount()
    {
        return expAmount;
    }
    
    public void setExpAmount(Integer expAmount)
    {
        this.expAmount = expAmount;
    }
    
    public String getClickAmountStr()
    {
        return clickAmountStr;
    }
    
    public void setClickAmountStr(String clickAmountStr)
    {
        this.clickAmountStr = clickAmountStr;
    }
    
    public Integer getClickAmount()
    {
        return clickAmount;
    }
    
    public void setClickAmount(Integer clickAmount)
    {
        this.clickAmount = clickAmount;
    }
    
    public String getPublishPriceStr()
    {
        return publishPriceStr;
    }
    
    public void setPublishPriceStr(String publishPriceStr)
    {
        this.publishPriceStr = publishPriceStr;
    }
    
    public Integer getPublishPrice()
    {
        return publishPrice;
    }
    
    public void setPublishPrice(Integer publishPrice)
    {
        this.publishPrice = publishPrice;
    }
    
    public String getNetPriceStr()
    {
        return netPriceStr;
    }
    
    public void setNetPriceStr(String netPriceStr)
    {
        this.netPriceStr = netPriceStr;
    }
    
    public Integer getNetPrice()
    {
        return netPrice;
    }
    
    public void setNetPrice(Integer netPrice)
    {
        this.netPrice = netPrice;
    }
    
    public String getResource()
    {
        return resource;
    }
    
    public void setResource(String resource)
    {
        this.resource = resource;
    }
    
    public String getResourceId()
    {
        return resourceId;
    }
    
    public void setResourceId(String resourceId)
    {
        this.resourceId = resourceId;
    }
    
    public String getIsExposure()
    {
        return isExposure;
    }
    
    public void setIsExposure(String isExposure)
    {
        this.isExposure = isExposure;
    }
    
    public String getIsExposureId()
    {
        return isExposureId;
    }
    
    public void setIsExposureId(String isExposureId)
    {
        this.isExposureId = isExposureId;
    }
    
    public String getIsClick()
    {
        return isClick;
    }
    
    public void setIsClick(String isClick)
    {
        this.isClick = isClick;
    }
    
    public String getIsClickId()
    {
        return isClickId;
    }
    
    public void setIsClickId(String isClickId)
    {
        this.isClickId = isClickId;
    }
    
    public String getMonitorPlatform()
    {
        return monitorPlatform;
    }
    
    public void setMonitorPlatform(String monitorPlatform)
    {
        this.monitorPlatform = monitorPlatform;
    }
    
    public String getMonitorPlatformId()
    {
        return monitorPlatformId;
    }
    
    public void setMonitorPlatformId(String monitorPlatformId)
    {
        this.monitorPlatformId = monitorPlatformId;
    }
    
    public String getSid()
    {
        return sid;
    }
    
    public void setSid(String sid)
    {
        this.sid = sid;
    }
    
    public String getCpsName()
    {
        return cpsName;
    }
    
    public void setCpsName(String cpsName)
    {
        this.cpsName = cpsName;
    }
    
    public String getSource()
    {
        return source;
    }
    
    public void setSource(String source)
    {
        this.source = source;
    }
    
    public String getLandChannelName()
    {
        return landChannelName;
    }
    
    public void setLandChannelName(String landChannelName)
    {
        this.landChannelName = landChannelName;
    }
    
    public String getLandChannel()
    {
        return landChannel;
    }
    
    public void setLandChannel(String landChannel)
    {
        this.landChannel = landChannel;
    }
    
    public String getLandUrl()
    {
        return landUrl;
    }
    
    public void setLandUrl(String landUrl)
    {
        this.landUrl = landUrl;
    }
    
    public String getCid()
    {
        return cid;
    }
    
    public void setCid(String cid)
    {
        this.cid = cid;
    }
    
    /**
     * 转字符串
     * @return   字符串
     */
    @Override
    public String toString()
    {
        return "AdTemplateInfo [aid=" + aid + ", aidStr=" + aidStr + ", mktName=" + mktName + ", mktId=" + mktId
            + ", mediaType=" + mediaType + ", mediaTypeId=" + mediaTypeId + ", webName=" + webName + ", webNameId="
            + webNameId + ", adChannel=" + adChannel + ", adPosition=" + adPosition + ", materialType=" + materialType
            + ", materialDesc=" + materialDesc + ", materialTypeId=" + materialTypeId + ", port=" + port + ", portId="
            + portId + ", platform=" + platform + ", platformId=" + platformId + ", platformDesc=" + platformDesc
            + ", deliveryDaysStr=" + deliveryDaysStr + ", deliveryDays=" + deliveryDays + ", deliveryTimes="
            + deliveryTimes + ", deliveryBeginDay=" + deliveryBeginDay + ", deliveryEndDay=" + deliveryEndDay
            + ", flowType=" + flowType + ", flowTypeId=" + flowTypeId + ", expAmountStr=" + expAmountStr
            + ", expAmount=" + expAmount + ", clickAmountStr=" + clickAmountStr + ", clickAmount=" + clickAmount
            + ", publishPriceStr=" + publishPriceStr + ", publishPrice=" + publishPrice + ", netPriceStr="
            + netPriceStr + ", netPrice=" + netPrice + ", resource=" + resource + ", resourceId=" + resourceId
            + ", isExposure=" + isExposure + ", isExposureId=" + isExposureId + ", isClick=" + isClick + ", isClickId="
            + isClickId + ", monitorPlatform=" + monitorPlatform + ", monitorPlatformId=" + monitorPlatformId
            + ", sid=" + sid + ", cpsName=" + cpsName + ", source=" + source + ", landChannelName=" + landChannelName
            + ", landChannel=" + landChannel + ", cid=" + cid + ", landUrl=" + landUrl + "]";
    }
    
}
