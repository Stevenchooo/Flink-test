/*
 * 文 件 名:  GroupFetchInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2013-6-3
 */
package com.huawei.devicecloud.platform.bi.odp.jersey.info;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Open Data Platform Service, 2013-6-3]
 * @see  [相关类/方法]
 */
public class GroupFetchInfo
{
    private String groupId;

    public String getGroupId()
    {
        return groupId;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((groupId == null) ? 0 : groupId.hashCode());
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
        GroupFetchInfo other = (GroupFetchInfo)obj;
        if (groupId == null)
        {
            if (other.groupId != null)
            {
                return false;
            }
        }
        else if (!groupId.equals(other.groupId))
        {
            return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("GroupFetchInfo [groupId=");
        builder.append(groupId);
        builder.append("]");
        return builder.toString();
    }
    
    
}
