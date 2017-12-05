package com.huawei.ide.services.rcm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.core.drools.api.IRuleEngine;
import com.huawei.core.drools.api.IRuleEngineFactory;
import com.huawei.core.drools.api.IRuleTreatment;
import com.huawei.core.drools.beans.RuleTreatment;
import com.huawei.ide.services.algorithm.entity.Cards;
import com.huawei.ide.services.algorithm.entity.Items;
import com.huawei.ide.services.algorithm.process.RcmRedisExecutor;
import com.huawei.ide.services.redis.DCClientService;
import com.huawei.ide.services.res.scenario.ScenarioService;

/**
 * 
 * ActivitiGraphCompiler
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
 * @see  [相关类/方法]
 */
public final class ActivitiGraphCompiler
{
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivitiGraphCompiler.class);
    
    /**
     * 业务名称
     */
    private final String businessName;
    
    /**
     * 流程定义
     */
    private ProcessDefinitionEntity processDefinitionEntity;
    
    /**
     * 流程上下文变量信息
     */
    @SuppressWarnings("unused")
    private Map<String, Object> flowVariables;
    
    /**
     * 规则处理上下文变量
     */
    private Map<String, Object> variables;
    
    /**
     * 规则引擎工厂类
     */
    private IRuleEngineFactory ruleEngineFactory;
    
    /**
     * 场景管理Service
     */
    private ScenarioService scenarioService;
    
    /**
     * Redis客户端服务
     */
    private DCClientService dcClientService;
    
    /**
     * 完成队列
     * key：节点ID
     * value：节点对应的ActivityImpl
     */
    private final Map<String, ActivityImpl> doneElementActivitys;
    
    /**
     * 执行队列
     * value：节点ID
     */
    private final List<String> waitElements;
    
    /**
     * 开始节点的ID
     */
    private final String startElementId;
    
    /**
     * 结束节点的ID
     */
    private final String endElementId;
    
    /**
     * 编译规则的资源
     */
    private final List<String> ruleResourceList;
    
    /**
     * 规则引擎实例
     */
    private IRuleEngine ruleEngine;
    
    /**
     * 规则引擎执行逻辑
     */
    private RuleTreatment ruleTreatment;
    
    /**
     * 构造函数
     * @param businessName
     *        businessName
     * @param processDefinitionEntity
     *        processDefinitionEntity
     * @param flowVariables
     *        flowVariables
     */
    public ActivitiGraphCompiler(String businessName, ProcessDefinitionEntity processDefinitionEntity,
        Map<String, Object> flowVariables)
    {
        this.businessName = businessName;
        this.processDefinitionEntity = processDefinitionEntity;
        this.flowVariables = flowVariables;
        doneElementActivitys = new HashMap<String, ActivityImpl>();
        waitElements = new ArrayList<String>();
        startElementId = ActivitiGraphUtils.getStartElement(processDefinitionEntity);
        endElementId = ActivitiGraphUtils.getEndElement(processDefinitionEntity);
        ruleResourceList = new ArrayList<String>();
        ruleTreatment = new RuleTreatment();
        String ruleHead = "package com.huawei.core.drools.business." + businessName + "; \n"
            + "import com.huawei.core.drools.beans.RuleScenario; \n"
            + "import com.huawei.core.drools.beans.RuleParam; \n"
            + "import com.huawei.core.drools.beans.RuleTreatment; \n";
        ruleResourceList.add(ruleHead);
        ruleResourceList.add(ActivitiGraphUtils.getstartElementRule(processDefinitionEntity, startElementId));
    }
    
    public void setVariables(Map<String, Object> variables)
    {
        this.variables = variables;
    }
    
    public void setRuleEngineFactory(IRuleEngineFactory ruleEngineFactory)
    {
        this.ruleEngineFactory = ruleEngineFactory;
    }
    
    public void setScenarioService(ScenarioService scenarioService)
    {
        this.scenarioService = scenarioService;
    }
    
    public void setDcClientService(DCClientService dcClientService)
    {
        this.dcClientService = dcClientService;
    }
    
    public IRuleEngine getRuleEngine()
    {
        return ruleEngine;
    }
    
    public String getStartElementId()
    {
        return startElementId;
    }
    
    public RuleTreatment getRuleTreatment()
    {
        return ruleTreatment;
    }
    
    /**
     * 遍历ActivitiGraph图节点
     * 获得编译后的business规则对象
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void activitiGraphCompile()
    {
        //获取流程初始节点
        List<String> startIds = ActivitiGraphUtils.getStartElements(processDefinitionEntity);
        //将所有起始节点都加到wait队列中
        waitElements.addAll(startIds);
        //当前处理的节点
        int currentElement = 0;
        //还有未处理完的节点
        while (waitElements.size() > currentElement)
        {
            String id = waitElements.get(currentElement++);
            //如果该节点已处理，或者为结束节点，则不处理该节点
            if (doneElementActivitys.containsKey(id) || endElementId.equals(id))
            {
                continue;
            }
            //获取依赖节点中所有未处理的节点
            List<String> unFinishedElements = getUnFinishedElements(id);
            //未处理的节点个数>0，则把该节点放入阻塞队列
            if (!unFinishedElements.isEmpty())
            {
                continue;
            }
            //处理该节点
            dealWaitElement(id);
        }
        ruleEngine = ruleEngineFactory.createRuleEngine(businessName);
        //ruleResourceList.add(ActivitiGraphUtils.getEndElementRule(processDefinitionEntity, endElementId));
        StringBuffer buffer = new StringBuffer();
        for (String tmp : ruleResourceList)
        {
            buffer.append(tmp);
        }
        ruleEngine.initRuleEngine(buffer.toString());
    }
    
    /**
     * 获取依赖节点中未执行的节点
     * <功能详细描述>
     * @param id
     *        id
     * @return  List<String>
     * @see [类、类#方法、类#成员]
     */
    private List<String> getUnFinishedElements(String id)
    {
        List<String> unFinishedElements = new ArrayList<String>();
        List<String> incomingElements = ActivitiGraphUtils.getIncomingElements(processDefinitionEntity, id);
        for (String element : incomingElements)
        {
            if (doneElementActivitys.containsKey(element) || startElementId.equals(element))
            {
                continue;
            }
            unFinishedElements.add(element);
        }
        return unFinishedElements;
    }
    
    /**
     * 
     * TestRuleTreatment
     * <功能详细描述>
     * 
     * @author  cWX306007
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
     * @see  [相关类/方法]
     */
    class TestRuleTreatment implements IRuleTreatment
    {
        private String name;
        
        private Map<String, Object> variables;
        
        TestRuleTreatment(String name, Map<String, Object> variables)
        {
            this.name = name;
            this.variables = variables;
        }
        
        @Override
        public void handleRuleTreatment()
        {
            LOGGER.debug("TestRuleTreatment '" + name + "', appKey'" + variables.get("appKey") + "'.");
        }
    }
    
    /**
     * 
     * CustomRuleTreatment
     * <功能详细描述>
     * 
     * @author  cWX306007
     * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
     * @see  [相关类/方法]
     */
    class CustomRuleTreatment implements IRuleTreatment
    {
        private String algorithmName;
        
        private Map<String, Object> variables;
        
        CustomRuleTreatment(String algorithmName, Map<String, Object> variables)
        {
            this.algorithmName = algorithmName;
            this.variables = variables;
        }
        
        @Override
        public void handleRuleTreatment()
        {
            Items[] redisResult = RcmRedisExecutor.getResultFRedis(dcClientService, variables, algorithmName);
            variables.put("algorithmID", algorithmName);
            variables.put("rcmResult", redisResult);
            
        }
    }
    
    /**
     * dealUserTaskElement
     * <功能详细描述>
     * @param activityImpl
     *        activityImpl
     * @see [类、类#方法、类#成员]
     */
    private void dealUserTaskElement(ActivityImpl activityImpl)
    {
        ruleResourceList
            .add(ActivitiGraphUtils.getUserTaskRule(processDefinitionEntity, activityImpl, scenarioService));
    }
    
    /**
     * dealServiceTaskElement
     * <功能详细描述>
     * @param activityImpl
     *        activityImpl
     * @see [类、类#方法、类#成员]
     */
    private void dealServiceTaskElement(ActivityImpl activityImpl)
    {
        String algorithmName = activityImpl.getProperty("name").toString();
        /*
        ruleTreatment.addRuleTreatment(activityImpl.getId(),
                new TestRuleTreatment(activityImpl.getId(), variables));
         */
        ruleTreatment.addRuleTreatment(activityImpl.getId(), new CustomRuleTreatment(algorithmName, variables));
        ruleResourceList.add(ActivitiGraphUtils.getServiceTaskRule(processDefinitionEntity, activityImpl));
    }
    
    /**
     * dealWaitElement
     * <功能详细描述>
     * @param id
     *        id
     * @see [类、类#方法、类#成员]
     */
    private void dealWaitElement(String id)
    {
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(id);
        doneElementActivitys.put(id, activityImpl);
        //将该节点的下游节点加入等待队列中
        waitElements.addAll(ActivitiGraphUtils.getOutgoingElements(processDefinitionEntity, id));
        Object typeObj = activityImpl.getProperty("type");
        String type = null;
        if (null != typeObj)
        {
            type = typeObj.toString();
        }
        else
        {
            LOGGER.error("Node '" + id + "' not with the type value.");
            return;
        }
        Object nameObj = activityImpl.getProperty("name");
        if (null == nameObj)
        {
            LOGGER.error("Node '" + id + "' not with the name value.");
            return;
        }
        if (type.equalsIgnoreCase("userTask"))
        {
            dealUserTaskElement(activityImpl);
        }
        else if (type.equalsIgnoreCase("serviceTask"))
        {
            dealServiceTaskElement(activityImpl);
        }
        else
        {
            LOGGER.error("Not support for this node type '" + type + "'");
        }
    }
    
}
