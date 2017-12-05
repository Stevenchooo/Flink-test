/*
 * 文 件 名:  MktUserInfo.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2015-3-28
 */
package com.huawei.manager.mkt.info;

import java.io.Serializable;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2015-3-28]
 * @see  [相关类/方法]
 */
public class MktUserInfo implements Serializable
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1379170375278611582L;

    //账号
    private String account;
    
    //标志
    private Integer flag;
    
    //活动ID
    private Integer mktinfoId;
    
    //姓名
    private String name;
    
    //部门
    private String department;

    public String getAccount()
    {
        return account;
    }

    public void setAccount(String account)
    {
        this.account = account;
    }

    public Integer getFlag()
    {
        return flag;
    }

    public void setFlag(Integer flag)
    {
        this.flag = flag;
    }

    public Integer getMktinfoId()
    {
        return mktinfoId;
    }

    public void setMktinfoId(Integer mktinfoId)
    {
        this.mktinfoId = mktinfoId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDepartment()
    {
        return department;
    }

    public void setDepartment(String department)
    {
        this.department = department;
    }

    /**
     * 转字符串
     * @return      字符串
     */
    @Override
    public String toString()
    {
        return "MktUserInfo [account=" + account + ", flag=" + flag + ", mktinfoId=" + mktinfoId + ", name=" + name
            + ", department=" + department + "]";
    }

    
}
