package com.huawei.ide.interceptors.res.rcm;

import java.io.Serializable;
import java.util.Arrays;

import com.huawei.ide.services.algorithm.entity.Items;

/**
 * 
 * RcmForItemListRsp
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年7月20日]
 * @see  [相关类/方法]
 */
public class RcmForItemListRsp implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -5440787539881671867L;
    
    private String resultCode;
    
    private String reqId;
    
    private String rcmScenario;
    
    private Items[] recommendList;
    
    public String getResultCode()
    {
        return resultCode;
    }
    
    public void setResultCode(String resultCode)
    {
        this.resultCode = resultCode;
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
    
    public Items[] getRecommendList()
    {
        return recommendList;
    }
    
    public void setRecommendList(Items[] recommendList)
    {
        this.recommendList = recommendList;
    }
    
    /**
     * hashCode
     * @return int
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((rcmScenario == null) ? 0 : rcmScenario.hashCode());
        result = prime * result + Arrays.hashCode(recommendList);
        result = prime * result + ((reqId == null) ? 0 : reqId.hashCode());
        result = prime * result + ((resultCode == null) ? 0 : resultCode.hashCode());
        return result;
    }
    
    /**
     * equals
     * @param obj
     *         obj
     * @return  boolean
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RcmForItemListRsp other = (RcmForItemListRsp)obj;
        if (rcmScenario == null)
        {
            if (other.rcmScenario != null)
                return false;
        }
        else if (!rcmScenario.equals(other.rcmScenario))
            return false;
        if (!Arrays.equals(recommendList, other.recommendList))
            return false;
        if (reqId == null)
        {
            if (other.reqId != null)
                return false;
        }
        else if (!reqId.equals(other.reqId))
            return false;
        if (resultCode == null)
        {
            if (other.resultCode != null)
                return false;
        }
        else if (!resultCode.equals(other.resultCode))
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
        return "RcmForItemListRsp [resultCode=" + resultCode + ", reqId=" + reqId + ", rcmScenario=" + rcmScenario
            + ", recommendList=" + Arrays.toString(recommendList) + "]";
    }
    
}
