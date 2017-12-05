/*
 * 文 件 名:  AdDicMapInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-6-4
 */
package com.huawei.manager.mkt.info;

import java.util.List;
import java.util.Map;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-6-4]
 * @see  [相关类/方法]
 */
public class AdDicMapInfo
{
    //网站名称集合
    private Map<String, List<MktMeta>> mktMap;
    
    //媒体类型集合
    private Map<String, Integer> mediaMap;
    
    //网站名称集合
    private Map<String, List<MktMeta>> webMap;
    
    //素材类型集合
    private Map<String, String> materiaMap;
    
    //端口所属集合
    private Map<String, String> portMap;
    
    //着陆平台集合
    private Map<String, String> platformMap;
    
    //引流类型集合
    private Map<String, String> flowMap;
    
    //资源所属集合
    private Map<String, String> resourceMap;
    
    //布尔集合
    private Map<String, String> booleanMap;
    
    //监控平台集合
    private Map<String, String> monitorPlatformMap;
    
    //媒体类型与网站名称集合
    private Map<Integer, Integer> mediaAndWebMap;
    
    public Map<String, List<MktMeta>> getMktMap()
    {
        return mktMap;
    }
    
    public void setMktMap(Map<String, List<MktMeta>> mktMap)
    {
        this.mktMap = mktMap;
    }
    
    public Map<String, Integer> getMediaMap()
    {
        return mediaMap;
    }
    
    public void setMediaMap(Map<String, Integer> mediaMap)
    {
        this.mediaMap = mediaMap;
    }
    
    public Map<String, List<MktMeta>> getWebMap()
    {
        return webMap;
    }
    
    public void setWebMap(Map<String, List<MktMeta>> webMap)
    {
        this.webMap = webMap;
    }
    
    public Map<String, String> getMateriaMap()
    {
        return materiaMap;
    }
    
    public void setMateriaMap(Map<String, String> materiaMap)
    {
        this.materiaMap = materiaMap;
    }
    
    public Map<String, String> getPortMap()
    {
        return portMap;
    }
    
    public void setPortMap(Map<String, String> portMap)
    {
        this.portMap = portMap;
    }
    
    public Map<String, String> getPlatformMap()
    {
        return platformMap;
    }
    
    public void setPlatformMap(Map<String, String> platformMap)
    {
        this.platformMap = platformMap;
    }
    
    public Map<String, String> getFlowMap()
    {
        return flowMap;
    }
    
    public void setFlowMap(Map<String, String> flowMap)
    {
        this.flowMap = flowMap;
    }
    
    public Map<String, String> getResourceMap()
    {
        return resourceMap;
    }
    
    public void setResourceMap(Map<String, String> resourceMap)
    {
        this.resourceMap = resourceMap;
    }
    
    public Map<String, String> getBooleanMap()
    {
        return booleanMap;
    }
    
    public void setBooleanMap(Map<String, String> booleanMap)
    {
        this.booleanMap = booleanMap;
    }
    
    public Map<String, String> getMonitorPlatformMap()
    {
        return monitorPlatformMap;
    }
    
    public void setMonitorPlatformMap(Map<String, String> monitorPlatformMap)
    {
        this.monitorPlatformMap = monitorPlatformMap;
    }
    
    public Map<Integer, Integer> getMediaAndWebMap()
    {
        return mediaAndWebMap;
    }
    
    public void setMediaAndWebMap(Map<Integer, Integer> mediaAndWebMap)
    {
        this.mediaAndWebMap = mediaAndWebMap;
    }
    
}
