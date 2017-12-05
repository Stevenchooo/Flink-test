package com.huawei.ide.services.rcm;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.huawei.core.drools.api.IRuleEngine;
import com.huawei.core.drools.api.IRuleEngineFactory;
import com.huawei.core.drools.beans.RuleParam;
import com.huawei.core.drools.beans.RuleScenario;
import com.huawei.core.drools.beans.RuleTreatment;
import com.huawei.ide.beans.res.business.Business;
import com.huawei.ide.beans.res.business.BusinessRule;
import com.huawei.ide.interceptors.res.rcm.CommonUtils;
import com.huawei.ide.interceptors.res.rcm.RcmForItemListReq;
import com.huawei.ide.interceptors.res.rcm.RcmForItemListRsp;
import com.huawei.ide.interceptors.res.rcm.RcmForThemeUserReq;
import com.huawei.ide.interceptors.res.rcm.RcmForThemeUserRsp;
import com.huawei.ide.services.algorithm.entity.Cards;
import com.huawei.ide.services.algorithm.entity.Data;
import com.huawei.ide.services.algorithm.entity.Items;
import com.huawei.ide.services.redis.DCClientService;
import com.huawei.ide.services.res.business.BusinessService;
import com.huawei.ide.services.res.scenario.ScenarioService;

/**
 * 
 * RuleEngineService
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月19日]
 * @see  [相关类/方法]
 */
@Component(value = "com.huawei.ide.services.rcm.RuleEngineService")
@Scope(value = "prototype")
public class RuleEngineService implements IRuleEngineService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleEngineService.class);
    
    @Autowired
    private IRuleEngineFactory ruleEngineFactory;
    
    @Autowired
    private BusinessService businessService;
    
    @Autowired
    private ScenarioService scenarioService;
    
    @Autowired
    private DCClientService dcClientService;
    
    @Autowired
    private RepositoryService repositoryService;
    
    /*
    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private FormService formService;
    
    @Autowired
    private IdentityService identityService;
    
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private HistoryService historyService;
    
    @Autowired
    private ManagementService managementService;
    */
    
    private IRuleEngine ruleEngine;
    
    private final Map<String, Object> variables;
    
    private final RuleScenario ruleScenario;
    
    private RuleParam ruleParam;
    
    private RuleTreatment ruleTreatment;
    
    private String startElementId;
    
    private ReentrantLock lock = new ReentrantLock();
    
    /**
     * 无参构造函数
     */
    public RuleEngineService()
    {
        variables = new HashMap<String, Object>();
        ruleScenario = new RuleScenario();
    }
    
    /**
     * 根据Model部署流程
     */
    private Deployment deployActivitiModel(String modelId)
    {
        try
        {
            Model modelData = repositoryService.getModel(modelId);
            ObjectNode modelNode =
                (ObjectNode)new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
            BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
            String processName = modelData.getName() + ".bpmn20.xml";
            Deployment deployment = repositoryService.createDeployment()
                .name(modelData.getName())
                .addString(processName, new String(bpmnBytes))
                .deploy();
            return deployment;
        }
        catch (JsonProcessingException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        catch (IOException e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        catch (Exception e)
        {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
    
    /**
     * buildBusinessRule
     * @param businessName
     *         businessName
     */
    @Override
    public void buildBusinessRule(String businessName)
    {
        Business business = businessService.queryBusinessByName(businessName);
        if (null == business)
        {
            LOGGER.error("No such business with the name '" + businessName + "'.");
            return;
        }
        List<BusinessRule> businessRules = businessService.queryBusinessRulesByBusinessId(business.getId());
        if (null == businessRules || businessRules.isEmpty())
        {
            LOGGER.error("No such business rules of the business '" + businessName + "'.");
            return;
        }
        BusinessRule businessRule = businessRules.get(0);
        String modelId = businessRule.getBpmModelId();
        Deployment deployment = deployActivitiModel(modelId);
        if (null == deployment)
        {
            LOGGER.error("The deployment of the business '" + businessName + "' failed.");
            return;
        }
        ProcessDefinition processDefinition =
            repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        //启动流程
        //runtimeService.startProcessInstanceById(processDefinition.getId());
        ProcessDefinitionEntity processDefinitionEntity =
            (ProcessDefinitionEntity)repositoryService.getProcessDefinition(processDefinition.getId());
        Map<String, Object> flowVariables = processDefinitionEntity.getVariables();
        ActivitiGraphCompiler compiler =
            new ActivitiGraphCompiler(businessName, processDefinitionEntity, flowVariables);
        compiler.setVariables(variables);
        compiler.setRuleEngineFactory(ruleEngineFactory);
        compiler.setScenarioService(scenarioService);
        compiler.setDcClientService(dcClientService);
        compiler.activitiGraphCompile();
        ruleEngine = compiler.getRuleEngine();
        startElementId = compiler.getStartElementId();
        ruleParam = new RuleParam(variables);
        ruleTreatment = compiler.getRuleTreatment();
        LOGGER.info("The Business Rule of the business '" + businessName + "' build successfully.");
    }
    
    /**
     * 将req请求参数放入variables中
     * 可以将所有的请求参数都放入到variables中提供给规则使用
     */
    private final void dealVariablesForReq(RcmForThemeUserReq req)
    {
        variables.put("appKey", req.getAppKey());
        variables.put("deviceID", req.getDeviceID());
        variables.put("reqId", req.getReqId());
        String ts = req.getTs();
        String hour = CommonUtils.getTimeForReq(ts);
        variables.put("showScene", hour);
        
    }
    
    /**
     * 将req请求参数放入variables中
     * 可以将所有的请求参数都放入到variables中提供给规则使用
     */
    private final void dealVariablesForItemListReq(RcmForItemListReq req)
    {
        variables.put("appKey", req.getAppKey());
        variables.put("deviceID", req.getDeviceId());
        variables.put("reqId", req.getReqId());
        variables.put("rcmScenario", req.getRcmScenario());
        variables.put("rcmCount", req.getRcmCount());
        String ts = req.getTs();
        String hour = CommonUtils.getTimeForReq(ts);
        variables.put("showScene", hour);
    }
    
    private final RcmForThemeUserRsp dealVariablesForRsp()
    {
        RcmForThemeUserRsp rsp = new RcmForThemeUserRsp();
        rsp.setReqId(variables.get("reqId").toString());
        rsp.setCode(variables.get("resultCode") == null ? null : variables.get("resultCode").toString());
        Cards[] rcmResult = variables.get("rcmResult") == null ? null : (Cards[])variables.get("rcmResult");
        Data dataApp = new Data();
        dataApp.setCards(rcmResult == null ? null : rcmResult);
        rsp.setData(dataApp);
        return rsp;
    }
    
    private final RcmForItemListRsp dealVariablesForItemListRsp()
    {
        RcmForItemListRsp rsp = new RcmForItemListRsp();
        rsp.setReqId(variables.get("reqId").toString());
        rsp.setRcmScenario(variables.get("rcmScenario").toString());
        rsp.setResultCode(variables.get("resultCode") == null ? null : variables.get("resultCode").toString());
        Items[] rcmResult = variables.get("rcmResult") == null ? null : (Items[])variables.get("rcmResult");
        rsp.setRecommendList(rcmResult);
        return rsp;
    }
    
    /**
     * 执行业务规则
     * @param req
     *        req
     * @return  RcmForThemeUserRsp
     */
    @Override
    public final RcmForThemeUserRsp executeBusinessRule(RcmForThemeUserReq req)
    {
        try
        {
            lock.lock();
            variables.clear();
            dealVariablesForReq(req);
            ruleScenario.init(startElementId);
            ruleEngine.insertFact(ruleScenario);
            ruleEngine.insertFact(ruleParam);
            ruleEngine.insertFact(ruleTreatment);
            ruleEngine.fireAllRules();
            ruleEngine.retract();
            RcmForThemeUserRsp rsp = dealVariablesForRsp();
            return rsp;
        }
        finally
        {
            lock.unlock();
        }
    }
    
    /**
     * executeBusinessRule
     * @param req
     *         req
     * @return   RcmForItemListRsp
     */
    @Override
    public RcmForItemListRsp executeBusinessRule(RcmForItemListReq req)
    {
        try
        {
            LOGGER.info("executeBusinessRule start..");
            lock.lock();
            variables.clear();
            dealVariablesForItemListReq(req);
            ruleScenario.init(startElementId);
            ruleEngine.insertFact(ruleScenario);
            ruleEngine.insertFact(ruleParam);
            ruleEngine.insertFact(ruleTreatment);
            ruleEngine.fireAllRules();
            ruleEngine.retract();
            RcmForItemListRsp rsp = dealVariablesForItemListRsp();
            LOGGER.info("executeBusinessRule end..");
            return rsp;
        }
        finally
        {
            lock.unlock();
        }
    }
    
}
