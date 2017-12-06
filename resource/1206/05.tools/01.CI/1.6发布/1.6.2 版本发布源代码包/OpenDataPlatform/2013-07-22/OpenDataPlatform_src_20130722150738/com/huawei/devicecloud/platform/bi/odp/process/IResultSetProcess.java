/*
 * 文 件 名:  IProcessRS.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  结果集处理接口
 * 创 建 人:  z00190465
 * 创建时间:  2012-8-11
 */
package com.huawei.devicecloud.platform.bi.odp.process;

import java.sql.ResultSet;

/**
 *  结果集处理接口
 * @author  z00190465
 * @version [Open Data Platform Service, 2012-8-11]
 */
public interface IResultSetProcess
{
    /**
     * 处理结果集
     * @param rs 结果集
     * @return 是否结束
     */
    boolean processResultSet(ResultSet rs);
}
