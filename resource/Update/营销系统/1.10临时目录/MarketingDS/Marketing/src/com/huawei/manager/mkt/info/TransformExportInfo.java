package com.huawei.manager.mkt.info;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  s00359263
 * @version [Internet Business Service Platform SP V100R100, 2015-11-19]
 * @see  [相关类/方法]
 *  
 */
public class TransformExportInfo extends ExportInfo
{
    
    //营销活动
    private String mktLandInfoActName;
    
    //网站名称
    private String mktLandInfoWebName;
    
    //推广产品 
    private String productName;
    
    //广告位
    private String mktLandInfoAdPosition;
    
    //频道
    private String mktLandInfoChannel;
    
    //端口所属
    private String mktLandInfoPort;
    
    //着陆平台
    private String platform;
    
    //统计周期
    private String reportDate;
    
    //投放日期
    private String mktLandInfoUseDate;
    
    //SID
    private String mktLandInfoSID;
    
    //SID
    private String mktLandInfoCID;
    
    //点击PV
    private String djPv;
    
    //点击UV
    private String djUv;
    
    //曝光PV
    private String bgPv;
    
    //曝光UV
    private String bgUv;
    
    //landing Pv
    private String landingPv;
    
    //landing uv
    private String landingUv;
    
    //着陆率
    private String landingRate;
    
    //预约用户数
    private String bespeakNums;
    
    //预约转化转化率
    private String transformRate;
    
    //购买用户数   
    private String buyNums;
    
    //购买转化率    
    private String buyTransRate;
    
    //平均访问页面数 
    private String avgAccess;
    
    //广告位来源
    private String resource;
    
    //投放人
    private String operator;
    
    //下单数
    private String orderCount;
    
    //支付订单数
    private String orderPayCount;
    
    //实际支付订单数
    private String realOrderCount;
    
    //支付金额
    private String orderPayPriceCount;
    
    //实际支付金额
    private String realOrderAmount;
    
    //预约用户数
    private String reserverUv;
    
    //下单数BI
    private String orderCountBi;
    
    //支付订单数BI
    private String orderPayCountBi;
    
    //实际支付订单数BI
    private String realOrderCountBi;
    
    //支付金额BI
    private String orderPayPriceCountBi;
    
    //实际支付金额BI
    private String realOrderAmountBi;
    
    public String getMktLandInfoWebName()
    {
        return mktLandInfoWebName;
    }
    
    public void setMktLandInfoWebName(String mktLandInfoWebName)
    {
        this.mktLandInfoWebName = mktLandInfoWebName;
    }
    
    public String getMktLandInfoActName()
    {
        return mktLandInfoActName;
    }
    
    public void setMktLandInfoActName(String mktLandInfoActName)
    {
        this.mktLandInfoActName = mktLandInfoActName;
    }
    
    public String getMktLandInfoAdPosition()
    {
        return mktLandInfoAdPosition;
    }
    
    public void setMktLandInfoAdPosition(String mktLandInfoAdPosition)
    {
        this.mktLandInfoAdPosition = mktLandInfoAdPosition;
    }
    
    public String getMktLandInfoChannel()
    {
        return mktLandInfoChannel;
    }
    
    public String getOrderPayPriceCountBi()
    {
        return orderPayPriceCountBi;
    }
    
    public void setOrderPayPriceCountBi(String orderPayPriceCountBi)
    {
        this.orderPayPriceCountBi = orderPayPriceCountBi;
    }
    
    public void setMktLandInfoChannel(String mktLandInfoChannel)
    {
        this.mktLandInfoChannel = mktLandInfoChannel;
    }
    
    public String getMktLandInfoPort()
    {
        return mktLandInfoPort;
    }
    
    public String getMktLandInfoSID()
    {
        return mktLandInfoSID;
    }
    
    public void setMktLandInfoPort(String mktLandInfoPort)
    {
        this.mktLandInfoPort = mktLandInfoPort;
    }
    
    public String getReportDate()
    {
        return reportDate;
    }
    
    public void setReportDate(String reportDate)
    {
        this.reportDate = reportDate;
    }
    
    public void setMktLandInfoUseDate(String mktLandInfoUseDate)
    {
        this.mktLandInfoUseDate = mktLandInfoUseDate;
    }
    
    public String getBgPv()
    {
        return bgPv;
    }
    
    public void setBgPv(String bgPv)
    {
        this.bgPv = bgPv;
    }
    
    public String getMktLandInfoUseDate()
    {
        return mktLandInfoUseDate;
    }
    
    public String getRealOrderAmountBi()
    {
        return realOrderAmountBi;
    }
    
    public void setRealOrderAmountBi(String realOrderAmountBi)
    {
        this.realOrderAmountBi = realOrderAmountBi;
    }
    
    public void setMktLandInfoCID(String mktLandInfoCID)
    {
        this.mktLandInfoCID = mktLandInfoCID;
    }
    
    public void setMktLandInfoSID(String mktLandInfoSID)
    {
        this.mktLandInfoSID = mktLandInfoSID;
    }
    
    public void setOrderCountBi(String orderCountBi)
    {
        this.orderCountBi = orderCountBi;
    }
    
    public String getOrderPayCountBi()
    {
        return orderPayCountBi;
    }
    
    public void setOrderPayCountBi(String orderPayCountBi)
    {
        this.orderPayCountBi = orderPayCountBi;
    }
    
    public String getMktLandInfoCID()
    {
        return mktLandInfoCID;
    }
    
    public String getOrderCountBi()
    {
        return orderCountBi;
    }
    
    public String getRealOrderCountBi()
    {
        return realOrderCountBi;
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
    
    public String getBuyNums()
    {
        return buyNums;
    }
    
    public void setBuyNums(String buyNums)
    {
        this.buyNums = buyNums;
    }
    
    public String getBuyTransRate()
    {
        return buyTransRate;
    }
    
    public String getLandingUv()
    {
        return landingUv;
    }
    
    public void setLandingUv(String landingUv)
    {
        this.landingUv = landingUv;
    }
    
    public String getLandingRate()
    {
        return landingRate;
    }
    
    public void setLandingRate(String landingRate)
    {
        this.landingRate = landingRate;
    }
    
    public String getProductName()
    {
        return productName;
    }
    
    public void setProductName(String productName)
    {
        this.productName = productName;
    }
    
    public String getLandingPv()
    {
        return landingPv;
    }
    
    public void setLandingPv(String landingPv)
    {
        this.landingPv = landingPv;
    }
    
    public String getBespeakNums()
    {
        return bespeakNums;
    }
    
    public void setBespeakNums(String bespeakNums)
    {
        this.bespeakNums = bespeakNums;
    }
    
    public String getTransformRate()
    {
        return transformRate;
    }
    
    public void setTransformRate(String transformRate)
    {
        this.transformRate = transformRate;
    }
    
    public void setBuyTransRate(String buyTransRate)
    {
        this.buyTransRate = buyTransRate;
    }
    
    public String getAvgAccess()
    {
        return avgAccess;
    }
    
    public String getDjUv()
    {
        return djUv;
    }
    
    public void setDjUv(String djUv)
    {
        this.djUv = djUv;
    }
    
    public void setAvgAccess(String avgAccess)
    {
        this.avgAccess = avgAccess;
    }
    
    public String getResource()
    {
        return resource;
    }
    
    public void setResource(String resource)
    {
        this.resource = resource;
    }
    
    public String getPlatform()
    {
        return platform;
    }
    
    public String getOrderCount()
    {
        return orderCount;
    }
    
    public void setOrderCount(String orderCount)
    {
        this.orderCount = orderCount;
    }
    
    public String getOrderPayCount()
    {
        return orderPayCount;
    }
    
    public void setOrderPayCount(String orderPayCount)
    {
        this.orderPayCount = orderPayCount;
    }
    
    public void setPlatform(String platform)
    {
        this.platform = platform;
    }
    
    public String getRealOrderCount()
    {
        return realOrderCount;
    }
    
    public void setRealOrderCount(String realOrderCount)
    {
        this.realOrderCount = realOrderCount;
    }
    
    public String getOrderPayPriceCount()
    {
        return orderPayPriceCount;
    }
    
    public void setOrderPayPriceCount(String orderPayPriceCount)
    {
        this.orderPayPriceCount = orderPayPriceCount;
    }
    
    public String getRealOrderAmount()
    {
        return realOrderAmount;
    }
    
    public void setRealOrderAmount(String realOrderAmount)
    {
        this.realOrderAmount = realOrderAmount;
    }
    
    public String getReserverUv()
    {
        return reserverUv;
    }
    
    public String getOperator()
    {
        return operator;
    }
    
    public void setOperator(String operator)
    {
        this.operator = operator;
    }
    
    public void setReserverUv(String reserverUv)
    {
        this.reserverUv = reserverUv;
    }
    
    public void setRealOrderCountBi(String realOrderCountBi)
    {
        this.realOrderCountBi = realOrderCountBi;
    }
    
    @Override
    public String toString()
    {
        return "TransformExportInfo [mktLandInfoActName=" + mktLandInfoActName + ", mktLandInfoAdPosition="
            + mktLandInfoAdPosition + ", mktLandInfoChannel=" + mktLandInfoChannel + ", mktLandInfoPort="
            + mktLandInfoPort + ", productName=" + productName + ", mktLandInfoWebName=" + mktLandInfoWebName
            + ", platform=" + platform + ", mktLandInfoUseDate=" + mktLandInfoUseDate + ", reportDate=" + reportDate
            + ", mktLandInfoSID=" + mktLandInfoSID + ", mktLandInfoCID=" + mktLandInfoCID + ", bgPv=" + bgPv + ", bgUv="
            + bgUv + ", djPv=" + djPv + ", djUv=" + djUv + ", landingPv=" + landingPv + ", landingUv=" + landingUv
            + ", landingRate=" + landingRate + ", bespeakNums=" + bespeakNums + ", transformRate=" + transformRate
            + ", buyNums=" + buyNums + ", buyTransRate=" + buyTransRate + ", avgAccess=" + avgAccess + ", resource="
            + resource + ", operator=" + operator + "]";
    }
    
}
