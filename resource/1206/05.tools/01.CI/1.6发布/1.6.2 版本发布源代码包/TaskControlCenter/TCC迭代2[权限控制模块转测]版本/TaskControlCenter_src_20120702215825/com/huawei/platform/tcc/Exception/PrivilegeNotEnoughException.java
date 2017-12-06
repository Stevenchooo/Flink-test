/*
 * 文 件 名:  PrivilegeNotEnoughException.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-6-21
 */
package com.huawei.platform.tcc.Exception;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限不足异常
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-6-21]
 */
public class PrivilegeNotEnoughException extends Exception
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = -1335211905506563304L;
    
    private List<PrivilegeNotEnoughParam> priNotEnoughParams;
    
    /**
     * 构造函数
     * @param msg 错误信息
     */
    public PrivilegeNotEnoughException(String msg)
    {
        super(msg);
    }
    
    /**
     * 构造函数
     * @param priNotEnoughParams 权限不足参数列表
     */
    public PrivilegeNotEnoughException(List<PrivilegeNotEnoughParam> priNotEnoughParams)
    {
        super("Privilege Not Enough");
        this.priNotEnoughParams = priNotEnoughParams;
    }
    
    /**
     * 构造函数
     * @param serviceName 业务名
     * @param taskGroup 任务组
     * @param subPrivilegeType 权限类型
     */
    public PrivilegeNotEnoughException(String serviceName, String taskGroup, Integer subPrivilegeType)
    {
        this.priNotEnoughParams = new ArrayList<PrivilegeNotEnoughParam>(1);
        PrivilegeNotEnoughParam pri = new PrivilegeNotEnoughParam(serviceName, taskGroup, subPrivilegeType);
        this.priNotEnoughParams.add(pri);
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if (null != priNotEnoughParams)
        {
            for (PrivilegeNotEnoughParam pram : priNotEnoughParams)
            {
                sb.append(pram);
                sb.append(',');
            }
        }
        
        return sb.toString();
    }
}
