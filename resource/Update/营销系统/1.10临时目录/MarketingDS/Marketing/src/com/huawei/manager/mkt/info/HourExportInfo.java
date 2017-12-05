package com.huawei.manager.mkt.info;

/**
 * <一句话功能简述>
 * 
 * <功能详细描述>
 * 
 * @author  s00359263
 * @version [Internet Business Service Platform SP V100R100, 2015-11-23]
 * @see  [相关类/方法]
 *  
 */
public class HourExportInfo extends ExportInfo
{
    //SID
    private String mktLandInfoSID;
    
    //媒体名称
    private String mediaName;
    
    //频道
    private String mktLandInfoChannel;
    
    //广告位
    private String mktLandInfoAdPosition;
    
    //hour
    private String hour;
    
    //曝光PV
    private String bgPv;
    
    //曝光UV
    private String bgUv;
    
    //点击PV
    private String djPv;
    
    //点击UV
    private String djUv;
    
    private String ctr;

    public String getMktLandInfoSID()
    {
        return mktLandInfoSID;
    }

    public void setMktLandInfoSID(String mktLandInfoSID)
    {
        this.mktLandInfoSID = mktLandInfoSID;
    }

    public String getMediaName()
    {
        return mediaName;
    }

    public void setMediaName(String mediaName)
    {
        this.mediaName = mediaName;
    }
  

    public String getMktLandInfoChannel()
    {
        return mktLandInfoChannel;
    }

    public void setMktLandInfoChannel(String mktLandInfoChannel)
    {
        this.mktLandInfoChannel = mktLandInfoChannel;
    }

    public String getMktLandInfoAdPosition()
    {
        return mktLandInfoAdPosition;
    }

    public void setMktLandInfoAdPosition(String mktLandInfoAdPosition)
    {
        this.mktLandInfoAdPosition = mktLandInfoAdPosition;
    }

    public String getHour()
    {
        return hour;
    }

    public void setHour(String hour)
    {
        this.hour = hour;
    }

    public String getBgPv()
    {
        return bgPv;
    }

    public void setBgPv(String bgPv)
    {
        this.bgPv = bgPv;
    }

    public String getBgUv()
    {
        return bgUv;
    }

    public void setBgUv(String bgUv)
    {
        this.bgUv = bgUv;
    }

    public String getDjPv()
    {
        return djPv;
    }

    public void setDjPv(String djPv)
    {
        this.djPv = djPv;
    }

    public String getDjUv()
    {
        return djUv;
    }

    public void setDjUv(String djUv)
    {
        this.djUv = djUv;
    }

    public String getCtr()
    {
        return ctr;
    }

    public void setCtr(String ctr)
    {
        this.ctr = ctr;
    }

    
    
}
