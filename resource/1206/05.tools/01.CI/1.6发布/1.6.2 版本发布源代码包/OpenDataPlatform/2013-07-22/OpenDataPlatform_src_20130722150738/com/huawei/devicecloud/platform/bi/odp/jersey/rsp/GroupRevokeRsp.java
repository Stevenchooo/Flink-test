/*
 * 文 件 名:  GroupRevokeRsp.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2013-6-3
 */
package com.huawei.devicecloud.platform.bi.odp.jersey.rsp;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Open Data Platform Service, 2013-6-3]
 * @see  [相关类/方法]
 */
public class GroupRevokeRsp
{
    private Integer resultCode;

    public Integer getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(Integer resultCode)
    {
        this.resultCode = resultCode;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((resultCode == null) ? 0 : resultCode.hashCode());
        return result;
    }

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
        GroupRevokeRsp other = (GroupRevokeRsp)obj;
        if (resultCode == null)
        {
            if (other.resultCode != null)
            {
                return false;
            }
        }
        else if (!resultCode.equals(other.resultCode))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("GroupRevokeRsp [resultCode=");
        builder.append(resultCode);
        builder.append("]");
        return builder.toString();
    }

    /**
     * <构造取消预留消息请求>
     * @return  取消预留消息请求
     * @see [类、类#方法、类#成员]
     */
    public GroupRevokeRsp createGroupRevokeRsp()
    {
        GroupRevokeRsp rsp = new GroupRevokeRsp();
        rsp.setResultCode(this.resultCode);
        return rsp;
    }
    
}
