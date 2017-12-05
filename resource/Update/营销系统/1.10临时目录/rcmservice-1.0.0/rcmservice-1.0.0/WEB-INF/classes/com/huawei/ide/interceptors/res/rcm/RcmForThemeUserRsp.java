package com.huawei.ide.interceptors.res.rcm;

import java.io.Serializable;

import com.huawei.ide.services.algorithm.entity.Data;


/**
 * 
 * RcmForThemeUserRsp <功能详细描述>
 * 
 * @author z00280396
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
 * @see [相关类/方法]
 */
public class RcmForThemeUserRsp extends BaseResponse implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -5440787639881672867L;
    
    /**
     * 推荐算法返回推荐列表
     */
    private Data data;
    
    /**
     * 请求序号透传回去
     */
    private String reqId;
    
    public Data getData()
    {
        return data;
    }
    
    public void setData(Data data)
    {
        this.data = data;
    }
    
    public String getReqId()
    {
        return reqId;
    }
    
    public void setReqId(String reqId)
    {
        this.reqId = reqId;
    }
    
    public static long getSerialversionuid()
    {
        return serialVersionUID;
    }
    
   
    
    /**
     * hashCode
     * 
     * @return int
     */
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        result = prime * result + ((reqId == null) ? 0 : reqId.hashCode());
        return result;
    }
    
    /**
     * equals
     * 
     * @param obj
     *            obj
     * @return boolean
     */
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!super.equals(obj))
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        RcmForThemeUserRsp other = (RcmForThemeUserRsp)obj;
        if (data == null)
        {
            if (other.data != null)
            {
                return false;
            }
        }
        else if (!data.equals(other.data))
        {
            return false;
        }
        if (reqId == null)
        {
            if (other.reqId != null)
            {
                return false;
            }
        }
        else if (!reqId.equals(other.reqId))
        {
            return false;
        }
        return true;
    }

	@Override
	public String toString() {
		return "RcmForThemeUserRsp [data=" + data + ", reqId=" + reqId + "]";
	}
    
    
}
