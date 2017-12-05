/*
 * 文 件 名:  ScenarioDao.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.daos.res.scenario;

import java.util.List;

import com.huawei.ide.beans.res.scenario.Scenario;

/**
 * 应用场景对象数据库操作类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public interface ScenarioDao
{
    /**
     * 创建应用场景对象
     * <功能详细描述>
     * @param scenario
     *         scenario
     * @see [类、类#方法、类#成员]
     */
    public void createScenario(Scenario scenario);
    
    /**
     * 更新应用场景对象
     * <功能详细描述>
     * @param id
     *        id
     * @param scenario
     *        scenario
     * @see [类、类#方法、类#成员]
     */
    public void updateScenario(int id, Scenario scenario);
    
    /**
     * 删除应用场景对象
     * <功能详细描述>
     * @param id
     *        id
     * @see [类、类#方法、类#成员]
     */
    public void deleteScenario(int id);
    
    /**
     * 查询应用场景对象
     * <功能详细描述>
     * @param id
     *         id
     * @return  Scenario
     * @see [类、类#方法、类#成员]
     */
    public Scenario queryScenario(int id);
    
    /**
     * 查询所有应用场景对象
     * <功能详细描述>
     * @return  List<Scenario>
     * @see [类、类#方法、类#成员]
     */
    public List<Scenario> queryAllScenarios();
    
    /**
     * 查询scenario对象总数
     * <功能详细描述>
     * @return  int
     * @see [类、类#方法、类#成员]
     */
    public int queryScenarioTotalNum();
    
    /**
    * 分页查询指定scenario对象
    * <功能详细描述>
    * @param index
    *        index
    * @param pageSize
    *         pageSize
    * @return  List<Scenario>
    * @see [类、类#方法、类#成员]
    */
    public List<Scenario> queryScenariosByPage(int index, int pageSize);
    
}