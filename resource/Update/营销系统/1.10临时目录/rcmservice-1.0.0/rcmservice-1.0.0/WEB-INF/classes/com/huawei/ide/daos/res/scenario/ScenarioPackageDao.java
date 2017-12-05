/*
 * 文 件 名:  ScenarioPackageDao.java
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

import com.huawei.ide.beans.res.scenario.ScenarioPackage;

/**
 * 应用场景对象所属package数据库操作类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public interface ScenarioPackageDao
{
    /**
     * 创建应用场景对象所属package
     * <功能详细描述>
     * @param scenarioPackage
     *        scenarioPackage
     * @see [类、类#方法、类#成员]
     */
    public void createScenarioPackage(ScenarioPackage scenarioPackage);
    
    /**
     * 更新应用场景对象所属package
     * <功能详细描述>
     * @param id
     *        id
     * @param scenarioPackage
     *         scenarioPackage
     * @see [类、类#方法、类#成员]
     */
    public void updateScenarioPackage(int id, ScenarioPackage scenarioPackage);
    
    /**
     * 删除应用场景对象所属package
     * <功能详细描述>
     * @param id
     *        id
     * @see [类、类#方法、类#成员]
     */
    public void deleteScenarioPackage(int id);
    
    /**
     * 查询指定应用场景对象所属package
     * <功能详细描述>
     * @param id
     *        id
     * @return  ScenarioPackage
     * @see [类、类#方法、类#成员]
     */
    public ScenarioPackage queryScenarioPackage(int id);
    
    /**
     * 查询所有应用场景对象所属package
     * <功能详细描述>
     * @return  List<ScenarioPackage>
     * @see [类、类#方法、类#成员]
     */
    public List<ScenarioPackage> queryScenarioPackages();
}
