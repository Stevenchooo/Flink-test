/*
 * 文 件 名:  ScenarioPackageRelation.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  cWX306007
 * 修改时间:  2016年3月25日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.daos.res.scenario;

import java.util.List;

import com.huawei.ide.beans.res.scenario.Scenario;
import com.huawei.ide.beans.res.scenario.ScenarioPackageRelation;

/**
 * 
 * 应用场景package关系数据库操作类
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年3月25日]
 * @see  [相关类/方法]
 */
public interface ScenarioPackageRelationDao
{
    /**
     * 创建应用场景package关系
     * <功能详细描述>
     * @param scenarioPackageRelation
     *         scenarioPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public void createScenarioPackageRelation(ScenarioPackageRelation scenarioPackageRelation);
    
    /**
     * 更新应用场景package关系
     * <功能详细描述>
     * @param id
     *        id
     * @param scenarioPackageRelation
     *        scenarioPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public void updateScenarioPackageRelation(int id, ScenarioPackageRelation scenarioPackageRelation);
    
    /**
     * 删除应用场景package关系
     * <功能详细描述>
     * @param id
     *        id
     * @see [类、类#方法、类#成员]
     */
    public void deleteScenarioPackageRelation(int id);
    
    /**
     * 根据PackageId删除应用场景package关系
     * <功能详细描述>
     * @param packageId
     *        packageId
     * @see [类、类#方法、类#成员]
     */
    public void deleteScenarioPackageRelationByPackageId(int packageId);
    
    /**
     * 根据ScenarioId删除应用场景package关系
     * <功能详细描述>
     * @param scenarioId
     *        scenarioId
     * @see [类、类#方法、类#成员]
     */
    public void deleteScenarioPackageRelationByScenarioId(int scenarioId);
    
    /**
     * 根据PackageId查询应用场景package关系
     * <功能详细描述>
     * @param packageId
     *         packageId
     * @return   List<ScenarioPackageRelation>
     * @see [类、类#方法、类#成员]
     */
    public List<ScenarioPackageRelation> queryScenarioPackageRelationsByPackageId(int packageId);
    
    /**
     * 根据ScenarioId查询应用场景package关系
     * <功能详细描述>
     * @param scenarioId
     *        scenarioId
     * @return  List<ScenarioPackageRelation>
     * @see [类、类#方法、类#成员]
     */
    public List<ScenarioPackageRelation> queryScenarioPackageRelationsByScenarioId(int scenarioId);
    
    /**
     * 通过scenario更新应用场景package关系
     * <功能详细描述>
     * @param scenario
     *        scenario
     * @see [类、类#方法、类#成员]
     */
    public void updatePackageRelationByScenario(Scenario scenario);
}
