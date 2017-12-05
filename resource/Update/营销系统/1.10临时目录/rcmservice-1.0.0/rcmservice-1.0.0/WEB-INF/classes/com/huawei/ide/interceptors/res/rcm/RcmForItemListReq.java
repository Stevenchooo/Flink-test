package com.huawei.ide.interceptors.res.rcm;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * RcmForItemListReq
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年7月20日]
 * @see  [相关类/方法]
 */
public class RcmForItemListReq extends BaseRequest implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 3271209992925132569L;
    
    private String reqId;
    
    private String rcmScenario;
    
    private String deviceId;
    
    private Integer rcmCount;
    
    private HashMap<String, Object> paramMap;
    
    /**
     * createRcmForItemListReq
     * <功能详细描述>
     * @return RcmForItemListReq
     * @see [类、类#方法、类#成员]
     */
    public RcmForItemListReq createRcmForItemListReq()
    {
        RcmForItemListReq req = new RcmForItemListReq();
        req.setAppKey(super.getAppKey());
        req.setTs(super.getTs());
        req.setDeviceId(deviceId);
        req.setParamMap(paramMap);
        req.setRcmCount(rcmCount);
        req.setRcmScenario(rcmScenario);
        req.setReqId(reqId);
        return req;
    }
    
    public String getReqId()
    {
        return reqId;
    }
    
    public void setReqId(String reqId)
    {
        this.reqId = reqId;
    }
    
    public String getRcmScenario()
    {
        return rcmScenario;
    }
    
    public void setRcmScenario(String rcmScenario)
    {
        this.rcmScenario = rcmScenario;
    }
    
    public String getDeviceId()
    {
        return deviceId;
    }
    
    public void setDeviceId(String deviceId)
    {
        this.deviceId = deviceId;
    }
    
    public Integer getRcmCount()
    {
        return rcmCount;
    }
    
    public void setRcmCount(Integer rcmCount)
    {
        this.rcmCount = rcmCount;
    }
    
    public HashMap<String, Object> getParamMap()
    {
        return paramMap;
    }
    
    public void setParamMap(HashMap<String, Object> paramMap)
    {
        this.paramMap = paramMap;
    }
    
    /**
     * hashCode
     * @return  int
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
        result = prime * result + ((paramMap == null) ? 0 : paramMap.hashCode());
        result = prime * result + ((rcmCount == null) ? 0 : rcmCount.hashCode());
        result = prime * result + ((rcmScenario == null) ? 0 : rcmScenario.hashCode());
        result = prime * result + ((reqId == null) ? 0 : reqId.hashCode());
        return result;
    }
    
    /**
     * equals
     * @param obj
     *        obj
     * @return  boolean
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        RcmForItemListReq other = (RcmForItemListReq)obj;
        if (deviceId == null)
        {
            if (other.deviceId != null)
                return false;
        }
        else if (!deviceId.equals(other.deviceId))
            return false;
        if (paramMap == null)
        {
            if (other.paramMap != null)
                return false;
        }
        else if (!paramMap.equals(other.paramMap))
            return false;
        if (rcmCount == null)
        {
            if (other.rcmCount != null)
                return false;
        }
        else if (!rcmCount.equals(other.rcmCount))
            return false;
        if (rcmScenario == null)
        {
            if (other.rcmScenario != null)
                return false;
        }
        else if (!rcmScenario.equals(other.rcmScenario))
            return false;
        if (reqId == null)
        {
            if (other.reqId != null)
                return false;
        }
        else if (!reqId.equals(other.reqId))
            return false;
        return true;
    }
    
    /**
     * toString
     * @return  String
     */
    @Override
    public String toString()
    {
        return "RcmForItemListReq [reqId=" + reqId + ", rcmScenario=" + rcmScenario + ", deviceId=" + deviceId
            + ", rcmCount=" + rcmCount + ", paramMap=" + paramMap + "]";
    }
    
}
