/*
 * 文 件 名:  ScenarioMvcController.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright YYYY-YYYY,  All rights reserved
 * 描    述:  <描述>
 * 修 改 人:  z00219375
 * 修改时间:  2016年2月4日
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.huawei.ide.controllers.res;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.huawei.ide.beans.res.domain.CustomRule;
import com.huawei.ide.beans.res.domain.Domain;
import com.huawei.ide.beans.res.domain.DomainProperty;
import com.huawei.ide.beans.res.domain.DomainPropertyTemp;
import com.huawei.ide.beans.res.scenario.Scenario;
import com.huawei.ide.beans.res.scenario.ScenarioPackage;
import com.huawei.ide.beans.res.scenario.ScenarioPackageRelation;
import com.huawei.ide.beans.res.scenario.ScenarioProperty;
import com.huawei.ide.beans.res.scenario.ScenarioPropertyVO;
import com.huawei.ide.services.res.business.BusinessService;
import com.huawei.ide.services.res.domain.DomainService;
import com.huawei.ide.services.res.scenario.ScenarioService;

import groovy.json.JsonException;

/**
 * RuleEngineSystem Mvc Controller for Scenario Module
 * @author  z00219375
 * @version  [版本号, 2016年2月4日]
 * @see  [相关类/方法]
 * @since  [Consumer Cloud Big Data Platform Dept]
 */
@Controller
@RequestMapping("/res")
public class ScenarioMvcController
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ScenarioMvcController.class);
    
    private static final String TRUE = "true";
    
    private static final String FALSE = "false";
    
    private static final Integer PACKAGE_NAME_MAX_LENGTH = 20;
    
    private ScenarioService scenarioService;
    
    private DomainService domainService;
    
    private BusinessService businessService;
    
    @Autowired
    public void setScenarioService(ScenarioService scenarioService)
    {
        this.scenarioService = scenarioService;
    }
    
    @Autowired
    public void setDomainService(DomainService domainService)
    {
        this.domainService = domainService;
    }
    
    @Autowired
    public void setBusinessService(BusinessService businessService)
    {
        this.businessService = businessService;
    }
    
    /**
     * 进入应用场景管理页面
     * <功能详细描述>
     * @return  ModelAndView
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping("/scenario_manager")
    public ModelAndView scenarioManager()
    {
        LOGGER.info("scenarioManager start..");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("rule_engine_system/scenario_manager/scenario_manager");
        LOGGER.info("scenarioManager end..");
        return mv;
    }
    
    /**
     * 查询scenarios总数:/res/angularjs/scenario/queryScenarioTotalNum
     * <功能详细描述>
     * @return  ResponseEntity<Integer>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/queryScenarioTotalNum", method = RequestMethod.POST)
    public ResponseEntity<Integer> queryTotalNum()
    {
        LOGGER.info("queryTotalNum start..");
        Integer integer = null;
        try
        {
            integer = scenarioService.queryScenarioTotalNum();
        }
        catch (DataAccessException e)
        {
            LOGGER.error("queryTotalNum failed: DataAccessException!");
            return new ResponseEntity<Integer>(integer, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("queryTotalNum end..");
        return new ResponseEntity<Integer>(integer, HttpStatus.OK);
    }
    
    /**
     * 查询所有的scenarios:/res/angularjs/scenario/queryAllScenarios
     * <功能详细描述>
     * @return  ResponseEntity<List<Scenario>>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/queryAllScenarios", method = RequestMethod.POST)
    public ResponseEntity<List<Scenario>> queryAllScenarios()
    {
        LOGGER.info("queryAllScenarios start..");
        List<Scenario> scenarios = new ArrayList<Scenario>();
        try
        {
            scenarios = scenarioService.queryAllScenarios();
        }
        catch (DataAccessException e)
        {
            LOGGER.error("queryAllScenarios failed: DataAccessException!");
            return new ResponseEntity<List<Scenario>>(scenarios, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("queryAllScenarios end..");
        return new ResponseEntity<List<Scenario>>(scenarios, HttpStatus.OK);
    }
    
    /**
     * 查询指定页的scenarios:/res/angularjs/scenario/queryScenariosByPage
     * <功能详细描述>
     * @param index
     *         index
     * @param pageSize
     *        pageSize
     * @return    ResponseEntity<List<Scenario>>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/queryScenariosByPage", method = RequestMethod.POST)
    public ResponseEntity<List<Scenario>> queryScenariosByPage(@RequestParam("index") Integer index,
        @RequestParam("pageSize") Integer pageSize)
    {
        LOGGER.info("queryScenariosByPage start..");
        List<Scenario> scenarios = new ArrayList<Scenario>();
        try
        {
            scenarios = scenarioService.queryScenariosByPage(index, pageSize);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("queryScenariosByPage failed: DataAccessException!");
            return new ResponseEntity<List<Scenario>>(scenarios, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("queryScenariosByPage end..");
        return new ResponseEntity<List<Scenario>>(scenarios, HttpStatus.OK);
    }
    
    /**
     * 新建scenario:/res/angularjs/scenario/newScenario
     * <功能详细描述>
     * @param name
     *         name
     * @param published
     *         published
     * @return  ResponseEntity<Scenario>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/newScenario", method = RequestMethod.POST)
    public ResponseEntity<Scenario> newScenario(@RequestParam("name") String name,
        @RequestParam("published") String published)
    {
        LOGGER.info("newScenario start..");
        Scenario scenario = new Scenario();
        if (!judgePublishment(published))
        {
            return new ResponseEntity<Scenario>(scenario, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        scenario.setName(name);
        scenario.setPublished(published);
        try
        {
            scenarioService.createScenario(scenario);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("newScenario failed: DataAccessException!");
            return new ResponseEntity<Scenario>(scenario, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("newScenario end..");
        return new ResponseEntity<Scenario>(scenario, HttpStatus.OK);
    }
    
    /**
     * 更新scenario:/res/angularjs/scenario/updateScenario
     * <功能详细描述>
     * @param scenarioStr
     *        scenarioStr
     * @return    ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/updateScenario", method = RequestMethod.POST)
    public ResponseEntity<Object> updateScenario(@RequestParam("scenario") String scenarioStr)
    {
        try
        {
            LOGGER.info("updateScenario start..");
            Scenario scenario = JSON.parseObject(scenarioStr, Scenario.class);
            if (!judgePublishment(scenario.getPublished()))
            {
                LOGGER.error("updateScenario failed: the publish value is invalid!");
                return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            int scenarioId = scenario.getId();
            scenarioService.updateScenario(scenarioId, scenario);
            scenarioService.updateScenarioPackageRelationByScenario(scenario);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("updateScenario failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JSONException e)
        {
            LOGGER.error("updateScenario failed: JSONException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("updateScenario end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 删除scenario:/res/angularjs/scenario/deleteScenario
     * <功能详细描述>
     * @param id
     *        id
     * @return  ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/deleteScenario", method = RequestMethod.POST)
    public ResponseEntity<Object> deleteScenario(@RequestParam("id") String id)
    {
        try
        {
            //TODO 删除关联外键的表 T_SCENARIO_DOMAIN_PROPERTY
            LOGGER.info("deleteScenario start..");
            int scenarioId = Integer.parseInt(id);
            Scenario scenario = scenarioService.queryScenario(scenarioId);
            if (TRUE.equals(scenario.getPublished()))
            {
                LOGGER.error("deleteScenario failed: the publish value is invalid!");
                return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            scenarioService.deleteScenarioPropertyByScenarioId(scenarioId);
            scenarioService.deleteScenarioPackageRelationByScenarioId(scenarioId);
            scenarioService.deleteScenario(scenarioId);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("deleteScenario failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("deleteScenario failed: NumberFormatException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("deleteScenario end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 删除scenarios:/res/angularjs/scenario/deleteScenarios
     * <功能详细描述>
     * @param ids
     *        ids
     * @return   ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/deleteScenarios", method = RequestMethod.POST)
    public ResponseEntity<Object> deleteScenarios(@RequestParam("ids") Object[] ids)
    {
        LOGGER.info("deleteScenarios start..");
        for (Object obj : ids)
        {
            try
            {
                int scenarioId = Integer.parseInt(obj.toString());
                Scenario scenario = scenarioService.queryScenario(scenarioId);
                if (TRUE.equals(scenario.getPublished()))
                {
                    LOGGER.error("deleteScenarios failed: the publish value is invalid!");
                    return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
                }
                //TODO 删除关联外键的表 T_SCENARIO_DOMAIN_PROPERTY
                scenarioService.deleteScenarioPropertyByScenarioId(scenarioId);
                scenarioService.deleteScenarioPackageRelationByScenarioId(scenarioId);
                scenarioService.deleteScenario(scenarioId);
            }
            catch (DataAccessException e)
            {
                LOGGER.error("deleteScenarios failed: DataAccessException!");
                return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            catch (NumberFormatException e)
            {
                LOGGER.error("deleteScenarios failed: NumberFormatException!");
                return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        LOGGER.info("deleteScenarios end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    private List<ScenarioProperty> translateToScenarioPropertys(ScenarioPropertyVO vo)
    {
        List<ScenarioProperty> scenarioProperties = new ArrayList<ScenarioProperty>();
        
        List<CustomRule> customRules = vo.getCustomRules();
        if (null != customRules)
        {
            for (CustomRule customRule : customRules)
            {
                ScenarioProperty scenarioProperty = new ScenarioProperty();
                scenarioProperty.setScenarioId(vo.getScenarioId());
                scenarioProperty.setDomainId(vo.getDomainId());
                scenarioProperty.setOperator(vo.getDomainOperator());
                scenarioProperty.setCustomRule(customRule.getCustomRule());
                scenarioProperty.setRuleOperator(customRule.getCustomRuleOperator());
                scenarioProperties.add(scenarioProperty);
            }
        }
        
        List<DomainPropertyTemp> domainProperties = vo.getDomainProperties();
        if (null != domainProperties)
        {
            for (DomainPropertyTemp domainPropertyTemp : domainProperties)
            {
                ScenarioProperty scenarioProperty = new ScenarioProperty();
                
                scenarioProperty.setScenarioId(vo.getScenarioId());
                scenarioProperty.setDomainId(vo.getDomainId());
                scenarioProperty.setOperator(vo.getDomainOperator());
                int domainPropertyId = domainPropertyTemp.getDomainPropertyId();
                DomainProperty domainProperty = domainService.queryDomainProperty(domainPropertyId);
                scenarioProperty.setDomainPropertyId(domainPropertyId);
                if (null != domainProperty)
                {
                    scenarioProperty.setDomainPropertyName(domainProperty.getName());
                    scenarioProperty.setDomainPropertyCategory(domainProperty.getCategory());
                    scenarioProperty.setDomainPropertyDefaultVal(domainProperty.getDefaultVal());
                }
                
                scenarioProperty.setDomainPropertyOperator(domainPropertyTemp.getDomainPropertyOperator());
                scenarioProperty.setDomainOperatorValue(domainPropertyTemp.getDomainPropertyOperatorValue());
                scenarioProperty.setRuleOperator(domainPropertyTemp.getDomainPropertyRuleOperator());
                scenarioProperties.add(scenarioProperty);
            }
        }
        return scenarioProperties;
    }
    
    /**
     * 保存ScenarioPropertyVOs:/res/angularjs/scenario/newScenarioPropertyVOs
     * <功能详细描述>
     * @param scenarioIdStr
     *         scenarioIdStr
     * @param scenarioPropertyVOsStr
     *         scenarioPropertyVOsStr
     * @return   ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/newScenarioPropertyVOs", method = RequestMethod.POST)
    public ResponseEntity<Object> newScenarioPropertyVOs(@RequestParam("scenarioId") String scenarioIdStr,
        @RequestParam("scenarioPropertyVOs") String scenarioPropertyVOsStr)
    {
        try
        {
            LOGGER.info("newScenarioPropertyVOs start..");
            int scenarioId = Integer.parseInt(scenarioIdStr);
            List<ScenarioPropertyVO> scenarioPropertyVOs =
                JSON.parseArray(scenarioPropertyVOsStr, ScenarioPropertyVO.class);
            
            if (null != scenarioPropertyVOs && scenarioPropertyVOs.size() > 0)
            
            {
                for (ScenarioPropertyVO vo : scenarioPropertyVOs)
                {
                    List<ScenarioProperty> scenarioProperties = translateToScenarioPropertys(vo);
                    for (ScenarioProperty scenarioProperty : scenarioProperties)
                    {
                        Integer domainId = scenarioProperty.getDomainId();
                        if (judgeDomainInvalid(domainId))
                        {
                            LOGGER.error("newScenarioPropertyVOs failed: the Domain is invalid,domainId:" + domainId);
                            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
                        }
                    }
                }
            }
            
            scenarioService.deleteScenarioPropertyByScenarioId(scenarioId);
            
            if (null != scenarioPropertyVOs && scenarioPropertyVOs.size() > 0)
            
            {
                for (ScenarioPropertyVO vo : scenarioPropertyVOs)
                {
                    List<ScenarioProperty> scenarioProperties = translateToScenarioPropertys(vo);
                    for (ScenarioProperty scenarioProperty : scenarioProperties)
                    {
                        scenarioService.createScenarioProperty(scenarioProperty);
                    }
                }
            }
        }
        catch (DataAccessException e)
        {
            LOGGER.error("newScenarioPropertyVOs failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("newScenarioPropertyVOs failed: NumberFormatException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JSONException e)
        {
            LOGGER.error("newScenarioPropertyVOs failed: JSONException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("newScenarioPropertyVOs end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 查询所有的ScenarioPropertyVOs:/res/angularjs/scenario/queryScenarioPropertyVOsByScenarioId
     * <功能详细描述>
     * @param scenarioIdStr
     *        scenarioIdStr
     * @return   ResponseEntity<List<ScenarioPropertyVO>>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/queryScenarioPropertyVOsByScenarioId", method = RequestMethod.POST)
    public ResponseEntity<List<ScenarioPropertyVO>> queryScenarioPropertyVOsByScenarioId(
        @RequestParam("scenarioId") String scenarioIdStr)
    {
        LOGGER.info("queryScenarioPropertyVOsByScenarioId start..");
        List<ScenarioPropertyVO> scenarioPropertyVOs = new ArrayList<ScenarioPropertyVO>();
        try
        {
            int scenarioId = Integer.parseInt(scenarioIdStr);
            scenarioPropertyVOs = scenarioService.queryScenarioPropertyVOsByScenarioId(scenarioId);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("queryScenarioPropertyVOsByScenarioId failed: DataAccessException!");
            return new ResponseEntity<List<ScenarioPropertyVO>>(scenarioPropertyVOs, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("queryScenarioPropertyVOsByScenarioId failed: NumberFormatException!");
            return new ResponseEntity<List<ScenarioPropertyVO>>(scenarioPropertyVOs, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("queryScenarioPropertyVOsByScenarioId end..");
        return new ResponseEntity<List<ScenarioPropertyVO>>(scenarioPropertyVOs, HttpStatus.OK);
    }
    
    /**
     * 查询所有已发布的domains:/res/angularjs/scenario/queryAllPublishedDomains
     * <功能详细描述>
     * @return  ResponseEntity<List<Domain>>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/queryAllPublishedDomains", method = RequestMethod.POST)
    public ResponseEntity<List<Domain>> queryAllPublishedDomains()
    {
        LOGGER.info("queryAllPublishedDomains start..");
        List<Domain> domains = new ArrayList<Domain>();
        try
        {
            domains = domainService.queryAllPublishedDomains();
        }
        catch (DataAccessException e)
        {
            LOGGER.error("queryAllPublishedDomains failed: DataAccessException!");
            return new ResponseEntity<List<Domain>>(domains, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("queryAllPublishedDomains end..");
        return new ResponseEntity<List<Domain>>(domains, HttpStatus.OK);
    }
    
    /**
     * 查询指定的DomainPropertys:/res/angularjs/scenario/queryDesignDomainPropertys
     * <功能详细描述>
     * @param domainIdStr
     *           domainIdStr
     * @return   ResponseEntity<List<DomainProperty>>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/queryDesignDomainPropertys", method = RequestMethod.POST)
    public ResponseEntity<List<DomainProperty>> queryDesignDomainPropertys(@RequestParam("domainId") String domainIdStr)
    {
        LOGGER.info("queryDesignDomainPropertys start..");
        List<DomainProperty> domainProperties = new ArrayList<DomainProperty>();
        try
        {
            Integer domainId = Integer.parseInt(domainIdStr);
            if (judgeDomainInvalid(domainId))
            {
                LOGGER.error("queryDesignDomainPropertys failed: the domain is invalid,the domainId:" + domainId);
                return new ResponseEntity<List<DomainProperty>>(domainProperties, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            domainProperties = domainService.queryDomainPropertysByDomainId(domainId);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("queryDesignDomainPropertys failed: DataAccessException!");
            return new ResponseEntity<List<DomainProperty>>(domainProperties, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("queryDesignDomainPropertys failed: NumberFormatException!");
            return new ResponseEntity<List<DomainProperty>>(domainProperties, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("queryDesignDomainPropertys end..");
        return new ResponseEntity<List<DomainProperty>>(domainProperties, HttpStatus.OK);
    }
    
    /**
     * 新建 scenario package relation:/res/angularjs/scenario/newScenarioPackageRelation
     * <功能详细描述>
     * @param scenarioPackageStr
     *         scenarioPackageStr
     * @param scenarioId
     *          scenarioId
     * @return  ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/newScenarioPackageRelation", method = RequestMethod.POST)
    public ResponseEntity<Object> newScenarioPackageRelation(@RequestParam("scenarioPackage") String scenarioPackageStr,
        @RequestParam("scenarioId") String scenarioId)
    {
        LOGGER.info("newScenarioPackageRelation start..");
        try
        {
            ScenarioPackage scenarioPackage = JSON.parseObject(scenarioPackageStr, ScenarioPackage.class);
            Scenario scenario = scenarioService.queryScenario(Integer.parseInt(scenarioId));
            if (null == scenario)
            {
                LOGGER.error("newScenarioPackageRelation failed: the scenarioId is invalid!");
                return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            ScenarioPackageRelation scenarioPackageRelation = new ScenarioPackageRelation();
            scenarioPackageRelation.setPackageId(scenarioPackage.getId());
            scenarioPackageRelation.setPackageName(scenarioPackage.getName());
            scenarioPackageRelation.setScenarioId(scenario.getId());
            scenarioPackageRelation.setScenarioName(scenario.getName());
            scenarioPackageRelation.setScenarioPublished(scenario.getPublished());
            scenarioService.createScenarioPackageRelation(scenarioPackageRelation);
            
        }
        catch (DataAccessException e)
        {
            LOGGER.error("newScenarioPackageRelation failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("newScenarioPackageRelation failed: NumberFormatException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JsonException e)
        {
            LOGGER.error("newScenarioPackageRelation failed: JsonException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("newScenarioPackageRelation end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 更新Scenario Package Relation:/res/angularjs/scenario/updateScenarioPackageRelation
     * <功能详细描述>
     * @param scenarioPackageRelationStr
     *           scenarioPackageRelationStr
     * @return   ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/updateScenarioPackageRelation", method = RequestMethod.POST)
    public ResponseEntity<Object> updateScenarioPackageRelation(
        @RequestParam("scenarioPackageRelation") String scenarioPackageRelationStr)
    {
        LOGGER.info("updateScenarioPackageRelation start..");
        try
        {
            ScenarioPackageRelation scenarioPackageRelation =
                JSON.parseObject(scenarioPackageRelationStr, ScenarioPackageRelation.class);
            Scenario scenario = scenarioService.queryScenario(scenarioPackageRelation.getScenarioId());
            if (null != scenario)
            {
                scenarioPackageRelation.setScenarioName(scenario.getName());
                scenarioPackageRelation.setScenarioPublished(scenario.getPublished());
                
            }
            scenarioService.updateScenarioPackageRelation(scenarioPackageRelation.getId(), scenarioPackageRelation);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("updateScenarioPackageRelation failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JsonException e)
        {
            LOGGER.error("updateScenarioPackageRelation failed: JsonException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("updateScenarioPackageRelation end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 删除Scenario Package Relation:/res/angularjs/scenario/deleteScenarioPackageRelation
     * <功能详细描述>
     * @param scenarioPackageRelationIdStr
     *            scenarioPackageRelationIdStr
     * @return    ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/deleteScenarioPackageRelation", method = RequestMethod.POST)
    public ResponseEntity<Object> deleteScenarioPackageRelation(
        @RequestParam("scenarioPackageRelationId") String scenarioPackageRelationIdStr)
    {
        LOGGER.info("deleteScenarioPackageRelation start..");
        try
        {
            Integer scenarioPackageRelationId = Integer.parseInt(scenarioPackageRelationIdStr);
            scenarioService.deleteScenarioPackageRelation(scenarioPackageRelationId);
            
        }
        catch (DataAccessException e)
        {
            LOGGER.error("deleteScenarioPackageRelation failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("deleteScenarioPackageRelation failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("deleteScenarioPackageRelation end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 新建Scenario Package:/res/angularjs/scenario/newScenarioPackage
     * <功能详细描述>
     * @param packageStr
     *          packageStr
     * @return   ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/newScenarioPackage", method = RequestMethod.POST)
    public ResponseEntity<Object> newScenarioPackage(@RequestParam("package") String packageStr)
    {
        LOGGER.info("new ScenarioPackage start..");
        try
        {
            ScenarioPackage scenarioPackage = JSON.parseObject(packageStr, ScenarioPackage.class);
            if (scenarioPackage.getName().length() > PACKAGE_NAME_MAX_LENGTH)
            {
                LOGGER.error("newScenarioPackage failed: the packageName's length is too long!");
                return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            scenarioService.createScenarioPackage(scenarioPackage);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("newScenarioPackage failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JsonException e)
        {
            LOGGER.error("newScenarioPackage failed: JsonException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("new ScenarioPackage end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 更新Scenario Package:/res/angularjs/scenario/updateScenarioPackage
     * <功能详细描述>
     * @param packageStr
     *          packageStr
     * @return   ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/updateScenarioPackage", method = RequestMethod.POST)
    public ResponseEntity<Object> updateScenarioPackage(@RequestParam("package") String packageStr)
    {
        LOGGER.info("updateScenarioPackage start..");
        try
        {
            ScenarioPackage scenarioPackage = JSON.parseObject(packageStr, ScenarioPackage.class);
            if (scenarioPackage.getName().length() > PACKAGE_NAME_MAX_LENGTH)
            {
                LOGGER.error("updateScenarioPackage failed: the packageName's length is too long!");
                return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            scenarioService.updateScenarioPackage(scenarioPackage.getId(), scenarioPackage);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("updateScenarioPackage failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (JsonException e)
        {
            LOGGER.error("updateScenarioPackage failed: JsonException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("updateScenarioPackage end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 删除Scenario Package:/res/angularjs/scenario/deleteScenarioPackage
     * <功能详细描述>
     * @param packageIdStr
     *        packageIdStr
     * @return  ResponseEntity<Object>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/deleteScenarioPackage", method = RequestMethod.POST)
    public ResponseEntity<Object> deleteScenarioPackage(@RequestParam("packageId") String packageIdStr)
    {
        LOGGER.info("deleteScenarioPackage start..");
        try
        {
            int packageId = Integer.parseInt(packageIdStr);
            scenarioService.deleteScenarioPackageRelationByPackageId(packageId);
            scenarioService.deleteScenarioPackage(packageId);
        }
        catch (DataAccessException e)
        {
            LOGGER.error("deleteScenarioPackage failed: DataAccessException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("deleteScenarioPackage failed: NumberFormatException!");
            return new ResponseEntity<Object>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("deleteScenarioPackage end..");
        return new ResponseEntity<Object>(null, HttpStatus.OK);
    }
    
    /**
     * 应用场景关系结果类
     * <功能详细描述>
     * @author  cWX306007
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月16日]
     * @see  [相关类/方法]
     */
    class ScenarioPackageRelationResult
    {
        private String scenarioPackageId;
        
        private String scenarioPackageName;
        
        private List<ScenarioPackageRelation> scenarioPackageRelations;
        
        public String getScenarioPackageId()
        {
            return scenarioPackageId;
        }
        
        public void setScenarioPackageId(String scenarioPackageId)
        {
            this.scenarioPackageId = scenarioPackageId;
        }
        
        public String getScenarioPackageName()
        {
            return scenarioPackageName;
        }
        
        public void setScenarioPackageName(String scenarioPackageName)
        {
            this.scenarioPackageName = scenarioPackageName;
        }
        
        public List<ScenarioPackageRelation> getScenarioPackageRelations()
        {
            return scenarioPackageRelations;
        }
        
        public void setScenarioPackageRelations(List<ScenarioPackageRelation> scenarioPackageRelations)
        {
            this.scenarioPackageRelations = scenarioPackageRelations;
        }
        
    }
    
    /**
     * 查询明细的scenarioPackages：/res/angularjs/scenario/queryRefreshScenarioPackages
     * <功能详细描述>
     * @return  ResponseEntity<List<ScenarioPackageRelationResult>>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/queryRefreshScenarioPackages", method = RequestMethod.POST)
    public ResponseEntity<List<ScenarioPackageRelationResult>> queryRefreshScenarioPackages()
    {
        LOGGER.info("queryRefreshScenarioPackages start..");
        List<ScenarioPackageRelationResult> results = new ArrayList<ScenarioPackageRelationResult>();
        try
        {
            List<ScenarioPackage> scenarioPackages = scenarioService.queryAllScenarioPackages();
            if (null != scenarioPackages)
            {
                for (ScenarioPackage scenarioPackage : scenarioPackages)
                {
                    List<ScenarioPackageRelation> scenarioPackageRelations =
                        scenarioService.queryScenarioPackageRelationsByPackageId(scenarioPackage.getId());
                    ScenarioPackageRelationResult result = new ScenarioPackageRelationResult();
                    result.setScenarioPackageId(String.valueOf(scenarioPackage.getId()));
                    result.setScenarioPackageName(scenarioPackage.getName());
                    result.setScenarioPackageRelations(scenarioPackageRelations);
                    results.add(result);
                }
                
            }
        }
        catch (DataAccessException e)
        {
            LOGGER.error("queryRefreshScenarioPackages failed: DataAccessException!");
            return new ResponseEntity<List<ScenarioPackageRelationResult>>(results, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("queryRefreshScenarioPackages end..");
        return new ResponseEntity<List<ScenarioPackageRelationResult>>(results, HttpStatus.OK);
    }
    
    /**
     *该段代码临时保留，日后添加了新的判断应用场景是否被调用的代码后需将该段代码删除，并且删除相应的表：T_BUSINESS_SCENARIO_DOMAIN_PROPERTY
     *根据scenarioId判断该scenario是否被使用：/res/angularjs/scenario/judgeScenarioInUse
     * <功能详细描述>
     * @param scenarioId
     *         scenarioId
     * @return   ResponseEntity<Boolean>
     * @see [类、类#方法、类#成员]
     */
    @RequestMapping(value = "/angularjs/scenario/judgeScenarioInUse", method = RequestMethod.POST)
    public ResponseEntity<Boolean> judgeScenarioInUse(@RequestParam("scenarioId") String scenarioId)
    {
        LOGGER.info("judgeScenarioInUse start..");
        boolean useFlag = true;
        try
        {
            useFlag = businessService.judgeScenarioInUseById(Integer.parseInt(scenarioId));
        }
        catch (DataAccessException e)
        {
            LOGGER.error("judgeScenarioInUse failed: DataAccessException!");
            return new ResponseEntity<Boolean>(true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("judgeScenarioInUse failed: NumberFormatException!");
            return new ResponseEntity<Boolean>(true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("judgeScenarioInUse end..");
        return new ResponseEntity<Boolean>(useFlag, HttpStatus.OK);
    }
    
    private Boolean judgeDomainInvalid(Integer domainId)
    {
        Domain doamin = domainService.queryDomain(domainId);
        if (null != doamin)
        {
            if (TRUE.equals(doamin.getPublished()))
            {
                return false;
            }
        }
        return true;
    }
    
    private Boolean judgePublishment(String publishment)
    {
        if (TRUE.equals(publishment) || FALSE.equals(publishment))
        {
            return true;
        }
        return false;
    }
    
}
