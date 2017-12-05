/*
 * 文 件 名:  ReportExportInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-5-27
 */
package com.huawei.manager.mkt.info;

/**
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-5-27]
 * @see  [相关类/方法]
 */
public class ReportExportInfo
{
    //活动名称
    private String mktLandInfoActName;
    
    //网站名称
    private String mktLandInfoWebName;
    
    //频道
    private String mktLandInfoChannel;
    
    //广告位
    private String mktLandInfoAdPosition;
    
    //端口所属
    private String mktLandInfoPort;
    
    //投放日期
    private String mktLandInfoUseDate;
    
    //统计周期
    private String reportDate;
    
    //SID
    private String mktLandInfoSID;
    
    //SID
    private String mktLandInfoCID;
    
    //曝光PV
    private String bgPv;
    
    //曝光UV
    private String bgUv;
    
    //点击PV
    private String djPv;
    
    //点击UV
    private String djUv;
    
    //广告位来源
    private String resource;
    
    //投放人
    private String operator;
    
    //着陆平台
    private String platform;
    
    //域名
    private String dnsname;
    
    public String getMktLandInfoActName()
    {
        return mktLandInfoActName;
    }
    
    public void setMktLandInfoActName(String mktLandInfoActName)
    {
        this.mktLandInfoActName = mktLandInfoActName;
    }
    
    public String getMktLandInfoWebName()
    {
        return mktLandInfoWebName;
    }
    
    public void setMktLandInfoWebName(String mktLandInfoWebName)
    {
        this.mktLandInfoWebName = mktLandInfoWebName;
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
    
    public String getMktLandInfoPort()
    {
        return mktLandInfoPort;
    }
    
    public void setMktLandInfoPort(String mktLandInfoPort)
    {
        this.mktLandInfoPort = mktLandInfoPort;
    }
    
    public String getMktLandInfoUseDate()
    {
        return mktLandInfoUseDate;
    }
    
    public void setMktLandInfoUseDate(String mktLandInfoUseDate)
    {
        this.mktLandInfoUseDate = mktLandInfoUseDate;
    }
    
    public String getReportDate()
    {
        return reportDate;
    }
    
    public void setReportDate(String reportDate)
    {
        this.reportDate = reportDate;
    }
    
    public String getMktLandInfoSID()
    {
        return mktLandInfoSID;
    }
    
    public void setMktLandInfoSID(String mktLandInfoSID)
    {
        this.mktLandInfoSID = mktLandInfoSID;
    }
    
    public String getMktLandInfoCID()
    {
        return mktLandInfoCID;
    }
    
    public void setMktLandInfoCID(String mktLandInfoCID)
    {
        this.mktLandInfoCID = mktLandInfoCID;
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
    
    public String getResource()
    {
        return resource;
    }
    
    public void setResource(String resource)
    {
        this.resource = resource;
    }
    
    public String getOperator()
    {
        return operator;
    }
    
    public void setOperator(String operator)
    {
        this.operator = operator;
    }
    
    public String getPlatform()
    {
        return platform;
    }
    
    public void setPlatform(String platform)
    {
        this.platform = platform;
    }
    
    public String getDnsname()
    {
        return dnsname;
    }
    
    public void setDnsname(String dnsname)
    {
        this.dnsname = dnsname;
    }
    
    @Override
    public String toString()
    {
        return "ReportExportInfo [mktLandInfoActName=" + mktLandInfoActName + ", mktLandInfoWebName="
            + mktLandInfoWebName + ", mktLandInfoChannel=" + mktLandInfoChannel + ", mktLandInfoAdPosition="
            + mktLandInfoAdPosition + ", mktLandInfoPort=" + mktLandInfoPort + ", mktLandInfoUseDate="
            + mktLandInfoUseDate + ", reportDate=" + reportDate + ", mktLandInfoSID=" + mktLandInfoSID + ", bgPv="
            + bgPv + ", bgUv=" + bgUv + ", djPv=" + djPv + ", djUv=" + djUv + ", platform=" + platform + ", dnsname="
            + dnsname + ", resource=" + resource + ", operator=" + operator + "]";
    }
    
}
