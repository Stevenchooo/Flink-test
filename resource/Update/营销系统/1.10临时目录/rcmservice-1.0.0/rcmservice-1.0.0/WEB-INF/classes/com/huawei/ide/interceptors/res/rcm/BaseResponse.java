package com.huawei.ide.interceptors.res.rcm;

/**
 * 返回对象基类
 * @author z00280396
 *
 */
public class BaseResponse
{
    /**
     * 业务错误码
     */
    private String code;
    
    public String getCode()
    {
        return code;
    }
    
    public void setCode(String code)
    {
        this.code = code;
    }
    
    /**
     * hashCode
     * @return  int
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
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
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        BaseResponse other = (BaseResponse)obj;
        if (code == null)
        {
            if (other.code != null)
            {
                return false;
            }
        }
        else if (!code.equals(other.code))
        {
            return false;
        }
        return true;
    }
    
    /**
     * toString
     * @return  String
     */
    @Override
    public String toString()
    {
        return "BaseResponse [code=" + code + "]";
    }
    
}