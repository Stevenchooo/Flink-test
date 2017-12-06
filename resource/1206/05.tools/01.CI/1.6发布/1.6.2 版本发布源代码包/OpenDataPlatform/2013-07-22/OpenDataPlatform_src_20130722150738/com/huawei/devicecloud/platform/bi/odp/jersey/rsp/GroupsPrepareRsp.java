/*
 * 文 件 名:  GroupsPrepareRsp.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2013-6-3
 */
package com.huawei.devicecloud.platform.bi.odp.jersey.rsp;

import java.util.List;

import com.huawei.devicecloud.platform.bi.odp.domain.GroupInfo;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Open Data Platform Service, 2013-6-3]
 * @see  [相关类/方法]
 */
public class GroupsPrepareRsp
{
    /**
     * 返回值
     */
    private Integer resultCode;
    
    /**
     * 返回的group信息
     */
    private List<GroupInfo> groupInfo;

    public Integer getResultCode()
    {
        return resultCode;
    }

    public void setResultCode(Integer resultCode)
    {
        this.resultCode = resultCode;
    }

    public List<GroupInfo> getGroupInfo()
    {
        return groupInfo;
    }

    public void setGroupInfo(List<GroupInfo> groupInfo)
    {
        this.groupInfo = groupInfo;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((groupInfo == null) ? 0 : groupInfo.hashCode());
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
        GroupsPrepareRsp other = (GroupsPrepareRsp)obj;
        if (groupInfo == null)
        {
            if (other.groupInfo != null)
            {
                return false;
            }
        }
        else if (!groupInfo.equals(other.groupInfo))
        {
            return false;
        }
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
        builder.append("GroupsPrepareRsp [resultCode=");
        builder.append(resultCode);
        builder.append(", groupInfo=");
        builder.append(groupInfo);
        builder.append("]");
        return builder.toString();
    }

    /**
     * <创建用户预留请求体>
     * @return 用户预留请求体
     * @see [类、类#方法、类#成员]
     */
    public GroupsPrepareRsp createGroupsPrepareRsp()
    {
        GroupsPrepareRsp rsp = new GroupsPrepareRsp();
        rsp.setResultCode(this.resultCode);
        rsp.setGroupInfo(this.groupInfo);
        return rsp;
    }
    
}
