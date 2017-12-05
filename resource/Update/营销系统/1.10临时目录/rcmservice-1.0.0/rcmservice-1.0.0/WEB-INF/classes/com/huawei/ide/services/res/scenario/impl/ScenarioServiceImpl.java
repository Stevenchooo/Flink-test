/*
 * 文 件 名:  ScenarioServiceImpl.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2015年12月28日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.services.res.scenario.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.huawei.ide.beans.res.domain.CustomRule;
import com.huawei.ide.beans.res.domain.DomainPropertyTemp;
import com.huawei.ide.beans.res.scenario.Scenario;
import com.huawei.ide.beans.res.scenario.ScenarioPackage;
import com.huawei.ide.beans.res.scenario.ScenarioPackageRelation;
import com.huawei.ide.beans.res.scenario.ScenarioProperty;
import com.huawei.ide.beans.res.scenario.ScenarioPropertyVO;
import com.huawei.ide.daos.res.scenario.ScenarioDao;
import com.huawei.ide.daos.res.scenario.ScenarioPackageDao;
import com.huawei.ide.daos.res.scenario.ScenarioPackageRelationDao;
import com.huawei.ide.daos.res.scenario.ScenarioPropertyDao;
import com.huawei.ide.services.res.scenario.ScenarioService;

/**
 * 应用场景对象数据库服务实现类
 * @author  z00219375
 * @version  [版本号, 2015年12月28日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Service(value = "com.huawei.ide.services.res.scenario.impl.ScenarioServiceImpl")
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, readOnly = false)
public class ScenarioServiceImpl implements ScenarioService
{
    @Resource(name = "com.huawei.ide.daos.res.scenario.impl.ScenarioDaoImpl")
    private ScenarioDao scenarioDao;
    
    @Resource(name = "com.huawei.ide.daos.res.scenario.impl.ScenarioPackageDaoImpl")
    private ScenarioPackageDao scenarioPackageDao;
    
    @Resource(name = "com.huawei.ide.daos.res.scenario.impl.ScenarioPropertyDaoImpl")
    private ScenarioPropertyDao scenarioPropertyDao;
    
    @Resource(name = "com.huawei.ide.daos.res.scenario.impl.ScenarioPackageRelationDaoImpl")
    private ScenarioPackageRelationDao scenarioPackageRelationDao;
    
    /**
     * 新建应用场景
     * @param scenario
     *        scenario
     */
    @Override
    public void createScenario(Scenario scenario)
    {
        scenarioDao.createScenario(scenario);
    }
    
    /**
     * 更新应用场景
     * @param id
     *        id
     * @param scenario
     *        scenario
     */
    @Override
    public void updateScenario(int id, Scenario scenario)
    {
        scenarioDao.updateScenario(id, scenario);
    }
    
    /**
     * 删除应用场景
     * @param id
     *        id
     */
    @Override
    public void deleteScenario(int id)
    {
        scenarioDao.deleteScenario(id);
    }
    
    /**
     *   根据id查询应用场景
     * @param id
     *        id
     * @return  Scenario
     */
    @Override
    public Scenario queryScenario(int id)
    {
        return scenarioDao.queryScenario(id);
    }
    
    /**
     * 查询所有的应用场景
     * @return  List<Scenario>
     */
    @Override
    public List<Scenario> queryAllScenarios()
    {
        return scenarioDao.queryAllScenarios();
    }
    
    /**
     *  查询应用场景总数
     * @return  int
     */
    @Override
    public int queryScenarioTotalNum()
    {
        return scenarioDao.queryScenarioTotalNum();
    }
    
    /**
     * 分页查询应用场景
     * @param index
     *         index
     * @param pageSize
     *        pageSize
     * @return  List<Scenario>
     */
    @Override
    public List<Scenario> queryScenariosByPage(int index, int pageSize)
    {
        return scenarioDao.queryScenariosByPage(index, pageSize);
    }
    
    /**
     * 创建应用场景包
     * @param scenarioPackage
     *        scenarioPackage
     */
    @Override
    public void createScenarioPackage(ScenarioPackage scenarioPackage)
    {
        scenarioPackageDao.createScenarioPackage(scenarioPackage);
    }
    
    /**
     * 更新应用场景包
     * @param id
     *        id
     * @param scenarioPackage
     *        scenarioPackage
     */
    @Override
    public void updateScenarioPackage(int id, ScenarioPackage scenarioPackage)
    {
        scenarioPackageDao.updateScenarioPackage(id, scenarioPackage);
    }
    
    /**
     * 删除应用场景包
     * @param id
     *        id
     */
    @Override
    public void deleteScenarioPackage(int id)
    {
        scenarioPackageDao.deleteScenarioPackage(id);
    }
    
    /**
     * 根据id查询应用场景包
     * @param id
     *        id
     * @return  ScenarioPackage
     */
    @Override
    public ScenarioPackage queryScenarioPackage(int id)
    {
        return scenarioPackageDao.queryScenarioPackage(id);
    }
    
    /**
     * 查询所有的应用场景包
     * @return  List<ScenarioPackage>
     */
    @Override
    public List<ScenarioPackage> queryScenarioPackages()
    {
        return scenarioPackageDao.queryScenarioPackages();
    }
    
    /**
     * 创建应用场景属性
     * @param scenarioProperty
     *          scenarioProperty
     */
    @Override
    public void createScenarioProperty(ScenarioProperty scenarioProperty)
    {
        scenarioPropertyDao.createScenarioProperty(scenarioProperty);
    }
    
    /**
     * 更新应用场景属性
     * @param id
     *         id
     * @param scenarioProperty
     *         scenarioProperty
     */
    @Override
    public void updateScenarioProperty(int id, ScenarioProperty scenarioProperty)
    {
        scenarioPropertyDao.updateScenarioProperty(id, scenarioProperty);
    }
    
    /**
     * 根据id删除应用场景属性
     * @param id
     *        id
     */
    @Override
    public void deleteScenarioProperty(int id)
    {
        scenarioPropertyDao.deleteScenarioProperty(id);
    }
    
    /**
     * 根据应用场景id删除应用场景属性
     * @param scenarioId
     *         scenarioId
     */
    @Override
    public void deleteScenarioPropertyByScenarioId(int scenarioId)
    {
        scenarioPropertyDao.deleteScenarioPropertyByScenarioId(scenarioId);
    }
    
    /**
     * 根据id查询应用场景属性
     * @param id
     *         id
     * @return ScenarioProperty
     */
    @Override
    public ScenarioProperty queryScenarioProperty(int id)
    {
        return scenarioPropertyDao.queryScenarioProperty(id);
    }
    
    /**
     * 查询所有应用场景属性
     * @return  List<ScenarioProperty>
     */
    @Override
    public List<ScenarioProperty> queryAllScenarioPropertys()
    {
        return scenarioPropertyDao.queryAllScenarioPropertys();
    }
    
    /**
     * 通过应用场景id查询应用场景属性
     * @param scenarioId
     *         scenarioId
     * @return  List<ScenarioProperty>
     */
    @Override
    public List<ScenarioProperty> queryScenarioPropertysByScenarioId(int scenarioId)
    {
        return scenarioPropertyDao.queryScenarioPropertysByScenarioId(scenarioId);
    }
    
    /**
     * 通过应用场景id查询应用场景属性
     * @param scenarioId
     *          scenarioId
     * @return  List<ScenarioPropertyVO>
     */
    @Override
    public List<ScenarioPropertyVO> queryScenarioPropertyVOsByScenarioId(int scenarioId)
    {
        return scenarioPropertyDao.queryScenarioPropertyVOsByScenarioId(scenarioId);
    }
    
    /**
     * 查询应用场景领域对象属性
     * @param domainId
     *         domainId
     * @return boolean
     */
    @Override
    public boolean queryScenarioDomainProperty(int domainId)
    {
        return scenarioPropertyDao.queryScenarioPropertyByDomainID(domainId);
    }
    
    /**
     * 查询所有的应用场景包
     * @return List<ScenarioPackage>
     */
    @Override
    public List<ScenarioPackage> queryAllScenarioPackages()
    {
        return scenarioPackageDao.queryScenarioPackages();
    }
    
    /**
     * 通过包的id查询应用场景包关系
     * @param packageId
     *         packageId
     * @return  List<ScenarioPackageRelation>
     */
    @Override
    public List<ScenarioPackageRelation> queryScenarioPackageRelationsByPackageId(int packageId)
    {
        return scenarioPackageRelationDao.queryScenarioPackageRelationsByPackageId(packageId);
    }
    
    /**
     * 创建应用场景包关系
     * @param scenarioPackageRelation
     *        scenarioPackageRelation
     */
    @Override
    public void createScenarioPackageRelation(ScenarioPackageRelation scenarioPackageRelation)
    {
        scenarioPackageRelationDao.createScenarioPackageRelation(scenarioPackageRelation);
    }
    
    /**
     * 更新应用场景包关系
     * @param id
     *        id
     * @param scenarioPackageRelation
     *         scenarioPackageRelation
     */
    @Override
    public void updateScenarioPackageRelation(int id, ScenarioPackageRelation scenarioPackageRelation)
    {
        scenarioPackageRelationDao.updateScenarioPackageRelation(id, scenarioPackageRelation);
    }
    
    /**
     * 删除应用场景包关系
     * @param id
     *        id
     */
    @Override
    public void deleteScenarioPackageRelation(int id)
    {
        scenarioPackageRelationDao.deleteScenarioPackageRelation(id);
    }
    
    /**
     * 通过包id删除应用场景包关系
     * @param packageId
     *          packageId
     */
    @Override
    public void deleteScenarioPackageRelationByPackageId(int packageId)
    {
        scenarioPackageRelationDao.deleteScenarioPackageRelationByPackageId(packageId);
    }
    
    /**
     * 通过应用场景id删除应用场景包关系
     * @param scenarioId
     *         scenarioId
     */
    @Override
    public void deleteScenarioPackageRelationByScenarioId(int scenarioId)
    {
        scenarioPackageRelationDao.deleteScenarioPackageRelationByScenarioId(scenarioId);
    }
    
    /**
     * 通过应用场景更新应用场景包关系
     * @param scenario
     *        scenario
     */
    @Override
    public void updateScenarioPackageRelationByScenario(Scenario scenario)
    {
        scenarioPackageRelationDao.updatePackageRelationByScenario(scenario);
    }
    
    /**
     * 通过应用场景名查询应用场景id
     * @param name
     *         name
     * @return  Integer
     */
    @Override
    public Integer queryScenarioIdByScenarioName(String name)
    {
        return scenarioPropertyDao.queryScenarioIdByScenarioName(name);
    }
    
    /**
    * ruleParam.get('var1')>5
    * (ruleParam.get('var1')>5 || ruleParam.get('var2')>10) && ((String)ruleParam.get('var3')).substring(4)=='2'
    * @param scenarioName
    *         scenarioName
    * @return String
    */
    @Override
    public String generateScenarioRuleCondition(String scenarioName)
    {
        Integer scenarioId = this.queryScenarioIdByScenarioName(scenarioName);
        if (null == scenarioId)
        {
            return "";
        }
        StringBuffer conditionBufs = new StringBuffer("eval(true)");
        List<ScenarioPropertyVO> scenarioPropertyVOList = this.queryScenarioPropertyVOsByScenarioId(scenarioId);
        if (null == scenarioPropertyVOList)
        {
            return conditionBufs.toString();
        }
        for (ScenarioPropertyVO scenarioPropertyVO : scenarioPropertyVOList)
        {
            StringBuffer conditionBuf = new StringBuffer("eval(true)");
            
            for (DomainPropertyTemp domainProperty : scenarioPropertyVO.getDomainProperties())
            {
                conditionBuf.append(domainProperty.getDomainPropertyRuleOperator() + "ruleParam.get('"
                    + domainProperty.getDomainPropertyName() + "')" + domainProperty.getDomainPropertyOperator() + "'"
                    + domainProperty.getDomainPropertyOperatorValue() + "'");
            }
            
            for (CustomRule customRule : scenarioPropertyVO.getCustomRules())
            {
                conditionBuf.append(customRule.getCustomRuleOperator() + customRule.getCustomRule());
            }
            
            conditionBufs.append(scenarioPropertyVO.getDomainOperator() + "(" + conditionBuf.toString() + ")");
            
        }
        
        return conditionBufs.toString();
    }
    
}
