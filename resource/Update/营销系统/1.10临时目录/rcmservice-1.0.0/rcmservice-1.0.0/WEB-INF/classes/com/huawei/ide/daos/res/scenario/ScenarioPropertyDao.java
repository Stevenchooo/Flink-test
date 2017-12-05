/*
 * 文 件 名:  ScenarioPropertyDao.java
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

import com.huawei.ide.beans.res.scenario.ScenarioProperty;
import com.huawei.ide.beans.res.scenario.ScenarioPropertyVO;

/**
 * 应用场景对象属性数据库操作类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public interface ScenarioPropertyDao
{
    /**
     * 创建应用场景对象属性
     * <功能详细描述>
     * @param scenarioProperty
     *        scenarioProperty
     * @see [类、类#方法、类#成员]
     */
    public void createScenarioProperty(ScenarioProperty scenarioProperty);
    
    /**
     * 更新应用场景对象属性
     * <功能详细描述>
     * @param id
     *        id
     * @param scenarioProperty
     *        scenarioProperty
     * @see [类、类#方法、类#成员]
     */
    public void updateScenarioProperty(int id, ScenarioProperty scenarioProperty);
    
    /**
    * <一句话功能简述>
    * <功能详细描述>
    * @param id
    *        id
    * @see [类、类#方法、类#成员]
    */
    public void deleteScenarioProperty(int id);
    
    /**
     * 根据外键scenarioId
     * 删除应用场景对象属性
     * <功能详细描述>
     * @param scenarioId
     *        scenarioId
     * @see [类、类#方法、类#成员]
     */
    public void deleteScenarioPropertyByScenarioId(int scenarioId);
    
    /**
     * 查询所有应用场景对象属性
     * <功能详细描述>
     * @param id
     *        id
     * @return  ScenarioProperty
     * @see [类、类#方法、类#成员]
     */
    public ScenarioProperty queryScenarioProperty(int id);
    
    /**
     * 查询所有应用场景对象属性
     * <功能详细描述>
     * @return  List<ScenarioProperty>
     * @see [类、类#方法、类#成员]
     */
    public List<ScenarioProperty> queryAllScenarioPropertys();
    
    /**
     * 查询指定scenarioId对应的所有ScenarioPropertys
     * <功能详细描述>
     * @param scenarioId
     *         scenarioId
     * @return  List<ScenarioProperty>
     * @see [类、类#方法、类#成员]
     */
    public List<ScenarioProperty> queryScenarioPropertysByScenarioId(int scenarioId);
    
    /**
     * 查询指定scenarioId对应的所有ScenarioPropertyVOs
     * <功能详细描述>
     * @param scenarioId
     *        scenarioId
     * @return  List<ScenarioPropertyVO>
     * @see [类、类#方法、类#成员]
     */
    public List<ScenarioPropertyVO> queryScenarioPropertyVOsByScenarioId(int scenarioId);
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param domainID
     *         domainID
     * @return  boolean
     * @see [类、类#方法、类#成员]
     */
    public boolean queryScenarioPropertyByDomainID(int domainID);
    
    /**
     * 通过应用场景名获取应用场景的id
     * <功能详细描述>
     * @param name
     *        name
     * @return  Integer
     * @see [类、类#方法、类#成员]
     */
    public Integer queryScenarioIdByScenarioName(String name);
    
}
