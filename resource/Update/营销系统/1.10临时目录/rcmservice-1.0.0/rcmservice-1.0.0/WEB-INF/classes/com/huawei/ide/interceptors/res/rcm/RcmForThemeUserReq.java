/*
 * 文 件 名:  RcmForTmUserReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190929
 * 创建时间:  2011-10-14
 */
package com.huawei.ide.interceptors.res.rcm;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 
 * RcmForThemeUserReq
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
 * @see  [相关类/方法]
 */
public class RcmForThemeUserReq extends BaseRequest implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 3271209992925132569L;
    
    private String reqId;
    
    private String appSecret;
    
    private String deviceID;
    
    private HashMap<String, Object> paramMap;
    
    /**
     * <构造请求消息>
     * @return    请求消息
     * @see [类、类#方法、类#成员]
     */
    public RcmForThemeUserReq createRcmForThemeUserReq()
    {
        RcmForThemeUserReq req = new RcmForThemeUserReq();
        
        req.setAppKey(super.getAppKey());
        req.setTs(super.getTs());
        req.setDeviceID(deviceID);
        req.setAppSecret(appSecret);
        req.setReqId(reqId);
        req.setParamMap(paramMap);
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
    
    public String getAppSecret()
    {
        return appSecret;
    }
    
    public void setAppSecret(String appSecret)
    {
        this.appSecret = appSecret;
    }
    
    public String getDeviceID()
    {
        return deviceID;
    }
    
    public void setDeviceID(String deviceID)
    {
        this.deviceID = deviceID;
    }
    
    public HashMap<String, Object> getParamMap()
    {
        return paramMap;
    }
    
    public void setParamMap(HashMap<String, Object> paramMap)
    {
        this.paramMap = paramMap;
    }
    
    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((appSecret == null) ? 0 : appSecret.hashCode());
		result = prime * result + ((deviceID == null) ? 0 : deviceID.hashCode());
		result = prime * result + ((paramMap == null) ? 0 : paramMap.hashCode());
		result = prime * result + ((reqId == null) ? 0 : reqId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		RcmForThemeUserReq other = (RcmForThemeUserReq) obj;
		if (appSecret == null) {
			if (other.appSecret != null)
				return false;
		} else if (!appSecret.equals(other.appSecret))
			return false;
		if (deviceID == null) {
			if (other.deviceID != null)
				return false;
		} else if (!deviceID.equals(other.deviceID))
			return false;
		if (paramMap == null) {
			if (other.paramMap != null)
				return false;
		} else if (!paramMap.equals(other.paramMap))
			return false;
		if (reqId == null) {
			if (other.reqId != null)
				return false;
		} else if (!reqId.equals(other.reqId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "RcmForThemeUserReq [reqId=" + reqId + ", appSecret=" + appSecret + ", deviceID=" + deviceID
				+ ", paramMap=" + paramMap + "]";
	}
    
    
}
