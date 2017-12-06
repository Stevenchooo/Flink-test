/*
 * 文 件 名:  GroupRevokeReq.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2013-6-3
 */
package com.huawei.devicecloud.platform.bi.odp.jersey.req;

import com.huawei.devicecloud.platform.bi.odp.jersey.info.GroupFetchInfo;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Open Data Platform Service, 2013-6-3]
 * @see  [相关类/方法]
 */
public class GroupRevokeReq
{
    private GroupFetchInfo groupFetchInfo;
    
    private String authInfo;

    public GroupFetchInfo getGroupFetchInfo()
    {
        return groupFetchInfo;
    }

    public void setGroupFetchInfo(GroupFetchInfo groupFetchInfo)
    {
        this.groupFetchInfo = groupFetchInfo;
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
        result = prime * result + ((groupFetchInfo == null) ? 0 : groupFetchInfo.hashCode());
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
        GroupRevokeReq other = (GroupRevokeReq)obj;
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
        if (groupFetchInfo == null)
        {
            if (other.groupFetchInfo != null)
            {
                return false;
            }
        }
        else if (!groupFetchInfo.equals(other.groupFetchInfo))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("GroupRevokeReq [groupFetchInfo=");
        builder.append(groupFetchInfo);
        builder.append(", authInfo=");
        builder.append(authInfo);
        builder.append("]");
        return builder.toString();
    }
    
}
