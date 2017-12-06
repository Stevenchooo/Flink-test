/*
 * 文 件 名:  GroupsPrepareReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2013-6-3
 */
package com.huawei.devicecloud.platform.bi.odp.jersey.req;

import com.huawei.devicecloud.platform.bi.odp.jersey.info.GroupsPrepareInfo;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Open Data Platform Service, 2013-6-3]
 * @see  [相关类/方法]
 */
public class GroupsPrepareReq
{
    private GroupsPrepareInfo groupsPrepareInfo;
    
    private String authInfo;

    public GroupsPrepareInfo getGroupsPrepareInfo()
    {
        return groupsPrepareInfo;
    }

    public void setGroupsPrepareInfo(GroupsPrepareInfo groupsPrepareInfo)
    {
        this.groupsPrepareInfo = groupsPrepareInfo;
    }

    public String getAuthInfo()
    {
        return authInfo;
    }

    public void setAuthInfo(String authInfo)
    {
        this.authInfo = authInfo;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((authInfo == null) ? 0 : authInfo.hashCode());
        result = prime * result + ((groupsPrepareInfo == null) ? 0 : groupsPrepareInfo.hashCode());
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
        GroupsPrepareReq other = (GroupsPrepareReq)obj;
        if (authInfo == null)
        {
            if (other.authInfo != null)
            {
                return false;
            }
        }
        else if (!authInfo.equals(other.authInfo))
        {
            return false;
        }
        if (groupsPrepareInfo == null)
        {
            if (other.groupsPrepareInfo != null)
            {
                return false;
            }
        }
        else if (!groupsPrepareInfo.equals(other.groupsPrepareInfo))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("GroupsPrepareReq [groupsPrepareInfo=");
        builder.append(groupsPrepareInfo);
        builder.append(", authInfo=");
        builder.append(authInfo);
        builder.append("]");
        return builder.toString();
    }
    
    
}
