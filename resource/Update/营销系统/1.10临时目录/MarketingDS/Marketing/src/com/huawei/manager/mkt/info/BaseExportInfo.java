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
public class BaseExportInfo extends ExportInfo
{
    //媒体名称
    private String mediaName;
    
    //曝光PV
    private String bgPv;
    
    //曝光UV
    private String bgUv;
    
    //点击PV
    private String djPv;
    
    //点击UV
    private String djUv;
    
    public String getMediaName()
    {
        return mediaName;
    }
    
    public void setMediaName(String mediaName)
    {
        this.mediaName = mediaName;
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
    
    @Override
    public String toString()
    {
        return "BaseExportInfo [ mediaName=" + mediaName + ", bgPv=" + bgPv + ", bgUv=" + bgUv + ", djPv=" + djPv
            + ", djUv=" + djUv + "]";
    }
    
}
