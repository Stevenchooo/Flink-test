/*
 * 文 件 名:  BusinessPropertyDao.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  cWX306007
 * 修改时间:  2016年4月1日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.daos.res.business;

/**
 * 
 * 业务规则对象属性数据库操作类
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年4月1日]
 * @see  [相关类/方法]
 */
public interface BusinessPropertyDao
{
    /**
     * 判断应用场景是否被使用
     * <功能详细描述>
     * @param scenarioId
     *        scenarioId
     * @return  boolean
     * @see [类、类#方法、类#成员]
     */
    public boolean judgeScenarioInUseById(int scenarioId);
}
