/*
 * 文 件 名:  ScenarioService.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.services.res.scenario;

import java.util.List;

import com.huawei.ide.beans.res.scenario.Scenario;
import com.huawei.ide.beans.res.scenario.ScenarioPackage;
import com.huawei.ide.beans.res.scenario.ScenarioPackageRelation;
import com.huawei.ide.beans.res.scenario.ScenarioProperty;
import com.huawei.ide.beans.res.scenario.ScenarioPropertyVO;

/**
 * 应用场景对象数据库服务类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
public interface ScenarioService
{
    /**
     * 新建应用场景
     * <功能详细描述>
     * @param scenario
     *        scenario
     * @see [类、类#方法、类#成员]
     */
    public void createScenario(Scenario scenario);
    
    /**
     * 更新应用场景
     * <功能详细描述>
     * @param id
     *         id
     * @param scenario
     *         scenario
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
     *        id
     * @return Scenario
     * @see [类、类#方法、类#成员]
     */
    public Scenario queryScenario(int id);
    
    /**
     * 查询所有应用场景对象
     * <功能详细描述>
     * @return   List<Scenario>
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
     *        pageSize
     * @return  List<Scenario>
     * @see [类、类#方法、类#成员]
     */
    public List<Scenario> queryScenariosByPage(int index, int pageSize);
    
    /**
     * 创建应用场景对象所属package
     * <功能详细描述>
     * @param scenarioPackage
     *          scenarioPackage
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
     * @return   List<ScenarioPackage>
     * @see [类、类#方法、类#成员]
     */
    public List<ScenarioPackage> queryScenarioPackages();
    
    /**
    * 创建应用场景对象属性
    * <功能详细描述>
    * @param scenarioProperty
    *         scenarioProperty
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
     * 删除应用场景对象属性
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
    * 查询应用场景对象属性
    * <功能详细描述>
    * @param id
    *        id
    * @return ScenarioProperty
    *         ScenarioProperty
    * @see [类、类#方法、类#成员]
    */
    public ScenarioProperty queryScenarioProperty(int id);
    
    /**
     * 查询所有应用场景对象属性
     * <功能详细描述>
     * @return List<ScenarioProperty>
     * @see [类、类#方法、类#成员]
     */
    public List<ScenarioProperty> queryAllScenarioPropertys();
    
    /**
     * 查询指定scenarioId对应的所有ScenarioPropertys
     * <功能详细描述>
     * @param scenarioId
     *         scenarioId
     * @return List<ScenarioProperty>
     * @see [类、类#方法、类#成员]
     */
    public List<ScenarioProperty> queryScenarioPropertysByScenarioId(int scenarioId);
    
    /**
     * 查询指定scenarioId对应的所有ScenarioPropertyVOs
     * <功能详细描述>
     * @param scenarioId
     *        scenarioId
     * @return List<ScenarioPropertyVO>
     * @see [类、类#方法、类#成员]
     */
    public List<ScenarioPropertyVO> queryScenarioPropertyVOsByScenarioId(int scenarioId);
    
    /**
     * 查询所有应用场景所属package
     * <功能详细描述>
     * @return List<ScenarioPackage>
     * @see [类、类#方法、类#成员]
     */
    public List<ScenarioPackage> queryAllScenarioPackages();
    
    /**
     * 根据PackageId查询应用场景package关系
     * <功能详细描述>
     * @param packageId
     *         packageId
     * @return  List<ScenarioPackageRelation>
     * @see [类、类#方法、类#成员]
     */
    public List<ScenarioPackageRelation> queryScenarioPackageRelationsByPackageId(int packageId);
    
    /**
     * 创建应用场景package关系
     * <功能详细描述>
     * @param scenarioPackageRelation
     *        scenarioPackageRelation
     * @see [类、类#方法、类#成员]
     */
    public void createScenarioPackageRelation(ScenarioPackageRelation scenarioPackageRelation);
    
    /**
    * 更新应用场景package关系
    * <功能详细描述>
    * @param id
    *        id
    * @param scenarioPackageRelation
    *         scenarioPackageRelation
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
     *         packageId
     * @see [类、类#方法、类#成员]
     */
    public void deleteScenarioPackageRelationByPackageId(int packageId);
    
    /**
     * 根据ScenarioId删除领域对象package关系
     * <功能详细描述>
     * @param scenarioId
     *        scenarioId
     * @see [类、类#方法、类#成员]
     */
    public void deleteScenarioPackageRelationByScenarioId(int scenarioId);
    
    /**
     * 查询应用场景领域对象属性
     * <功能详细描述>
     * @param domainId
     *         domainId
     * @return boolean
     * @see [类、类#方法、类#成员]
     */
    public boolean queryScenarioDomainProperty(int domainId);
    
    /**
     * 通过scenario更新应用场景package关系
     * <功能详细描述>
     * @param scenario
     *         scenario
     * @see [类、类#方法、类#成员]
     */
    public void updateScenarioPackageRelationByScenario(Scenario scenario);
    
    /**
     * 通过应用场景名获取应用场景的id
     * <功能详细描述>
     * @param name
     *          name
     * @return Integer
     * @see [类、类#方法、类#成员]
     */
    public Integer queryScenarioIdByScenarioName(String name);
    
    /**
     * 根据场景名称来生成规则的判断条件
     * ruleParam.get('var1')>5
     * (ruleParam.get('var1')>5 || ruleParam.get('var2')>10) && ((String)ruleParam.get('var3')).substring(4)=='2'
     * @param scenarioName
     *         scenarioName
     * @return  String
     * @see [类、类#方法、类#成员]
     */
    public String generateScenarioRuleCondition(String scenarioName);
}
