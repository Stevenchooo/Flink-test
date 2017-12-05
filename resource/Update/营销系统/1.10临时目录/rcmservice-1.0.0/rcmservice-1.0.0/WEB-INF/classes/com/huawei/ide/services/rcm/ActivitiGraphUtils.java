package com.huawei.ide.services.rcm;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;

import com.huawei.ide.services.res.scenario.ScenarioService;

/**
 * 
 * 流程工具类
 * <功能详细描述>
 * 
 * @author  cWX306007
 * @version [Cloud Platform Dept [Consumer Business Group], 2016年5月18日]
 * @see  [相关类/方法]
 */
public final class ActivitiGraphUtils
{
    /**
     * 获得开始节点的id
     * <功能详细描述>
     * @param processDefinitionEntity
     *        processDefinitionEntity
     * @return  String
     * @see [类、类#方法、类#成员]
     */
    public static String getStartElement(ProcessDefinitionEntity processDefinitionEntity)
    {
        List<ActivityImpl> activityImpls = processDefinitionEntity.getActivities();
        if (null != activityImpls)
        {
            for (ActivityImpl activityImpl : activityImpls)
            {
                Object typeObj = activityImpl.getProperty("type");
                if (null == typeObj)
                {
                    continue;
                }
                String type = typeObj.toString();
                if (type.equalsIgnoreCase("startEvent"))
                {
                    return activityImpl.getId();
                }
            }
        }
        // 没有找到开始节点
        return null;
    }
    
    /**
     * 获取结束节点的ID
     * <功能详细描述>
     * @param processDefinitionEntity
     *        processDefinitionEntity
     * @return   String
     * @see [类、类#方法、类#成员]
     */
    public static String getEndElement(ProcessDefinitionEntity processDefinitionEntity)
    {
        List<ActivityImpl> activityImpls = processDefinitionEntity.getActivities();
        if (null != activityImpls)
        {
            for (ActivityImpl activityImpl : activityImpls)
            {
                Object typeObj = activityImpl.getProperty("type");
                if (null == typeObj)
                {
                    continue;
                }
                String type = typeObj.toString();
                if (type.equalsIgnoreCase("endEvent"))
                {
                    return activityImpl.getId();
                }
            }
        }
        // 没有找到结束节点
        return null;
    }
    
    /**
     * 获取流程初始节点
     * <功能详细描述>
     * @param processDefinitionEntity
     *        processDefinitionEntity
     * @return  List<String>
     * @see [类、类#方法、类#成员]
     */
    public static List<String> getStartElements(ProcessDefinitionEntity processDefinitionEntity)
    {
        List<String> startElements = new ArrayList<String>();
        String startEventNodeId = getStartElement(processDefinitionEntity);
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(startEventNodeId);
        if (null == activityImpl)
        {
            return startElements;
        }
        List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();
        if (null != pvmTransitions)
        {
            for (PvmTransition pvmTransition : pvmTransitions)
            {
                startElements.add(pvmTransition.getDestination().getId());
            }
        }
        return startElements;
    }
    
    /**
    * 获取该节点的输出节点
    * <功能详细描述>
    * @param processDefinitionEntity
    *        processDefinitionEntity
    * @param id
    *        id
    * @return   List<String>
    * @see [类、类#方法、类#成员]
    */
    public static List<String> getOutgoingElements(ProcessDefinitionEntity processDefinitionEntity, String id)
    {
        List<String> outgoingElements = new ArrayList<String>();
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(id);
        if (null == activityImpl)
        {
            return outgoingElements;
        }
        List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();
        if (null != pvmTransitions)
        {
            for (PvmTransition pvmTransition : pvmTransitions)
            {
                outgoingElements.add(pvmTransition.getDestination().getId());
            }
        }
        return outgoingElements;
    }
    
    /**
     * 获取该节点的输入节点
     * <功能详细描述>
     * @param processDefinitionEntity
     *        processDefinitionEntity
     * @param id
     *        id
     * @return   List<String>
     * @see [类、类#方法、类#成员]
     */
    public static List<String> getIncomingElements(ProcessDefinitionEntity processDefinitionEntity, String id)
    {
        List<String> incomingElements = new ArrayList<String>();
        ActivityImpl activityImpl = processDefinitionEntity.findActivity(id);
        List<PvmTransition> pvmTransitions = activityImpl.getIncomingTransitions();
        if (null != pvmTransitions)
        {
            for (PvmTransition pvmTransition : pvmTransitions)
            {
                incomingElements.add(pvmTransition.getSource().getId());
            }
        }
        return incomingElements;
    }
    
    /**
     * 获得开始节点对应的rule规则
     * <功能详细描述>
     * @param processDefinitionEntity
     *        processDefinitionEntity
     * @param startElementId
     *        startElementId
     * @return  String
     * @see [类、类#方法、类#成员]
     */
    public static String getstartElementRule(ProcessDefinitionEntity processDefinitionEntity, String startElementId)
    {
        List<String> outgoingElements = getOutgoingElements(processDefinitionEntity, startElementId);
        StringBuffer buffer = new StringBuffer();
        buffer.append("rule 'startElement' \n");
        buffer.append("when \n");
        buffer.append("ruleScenario : RuleScenario(ruleScenario.checkScenarioValid('" + startElementId + "')); \n");
        buffer.append("then \n");
        buffer.append("ruleScenario.setScenarioInvalid('" + startElementId + "'); \n");
        for (String str : outgoingElements)
        {
            buffer.append("ruleScenario.addScenario('" + str + "'); \n");
        }
        //buffer.append("System.out.println('startElement'); \n");
        buffer.append("update(ruleScenario); \n");
        buffer.append("end \n");
        return buffer.toString();
    }
    
    /**
     * 获得结束节点对应的rule规则
     * <功能详细描述>
     * @param processDefinitionEntity
     *        processDefinitionEntity
     * @param endElementId
     *        endElementId
     * @return   String
     * @see [类、类#方法、类#成员]
     */
    public static String getEndElementRule(ProcessDefinitionEntity processDefinitionEntity, String endElementId)
    {
        List<String> incomingElements = getIncomingElements(processDefinitionEntity, endElementId);
        StringBuffer buffer = new StringBuffer();
        buffer.append("rule 'endElement' \n");
        buffer.append("when \n");
        buffer.append("ruleScenario : RuleScenario(ruleScenario.checkScenarioValid('" + endElementId + "')");
        for (String str : incomingElements)
        {
            buffer.append(" && ruleScenario.checkScenarioDone('" + str + "')");
        }
        buffer.append("); \n");
        buffer.append("then \n");
        buffer.append("ruleScenario.setScenarioInvalid('" + endElementId + "'); \n");
        //buffer.append("System.out.println('endElement'); \n");
        buffer.append("end \n");
        return buffer.toString();
    }
    
    /**
     * 根据用户场景获得表达式
     * ruleParam.get('var1')>5
     * (ruleParam.get('var1')>5 || ruleParam.get('var2')>10) && ((String)ruleParam.get('var3')).substring(4)=='2'
     * <功能详细描述>
     * @param scenarioName
     *        scenarioName
     * @param scenarioService
     *        scenarioService
     * @return  String
     * @see [类、类#方法、类#成员]
     */
    private static String getUserTaskScenarioRule(String scenarioName, ScenarioService scenarioService)
    {
        String ruleCondition = scenarioService.generateScenarioRuleCondition(scenarioName);
        return ruleCondition;
    }
    
    /**
     * 根据场景名称获得场景节点的rule规则
     * <功能详细描述>
     * @param processDefinitionEntity
     *        processDefinitionEntity
     * @param activityImpl
     *         activityImpl
     * @param scenarioService
     *        scenarioService
     * @return  String
     * @see [类、类#方法、类#成员]
     */
    public static String getUserTaskRule(ProcessDefinitionEntity processDefinitionEntity, ActivityImpl activityImpl,
        ScenarioService scenarioService)
    {
        String id = activityImpl.getId();
        List<String> outgoingElements = getOutgoingElements(processDefinitionEntity, id);
        String scenarioName = activityImpl.getProperty("name").toString();
        StringBuffer buffer = new StringBuffer();
        buffer.append("rule '" + scenarioName + "' \n");
        buffer.append("when \n");
        buffer.append("ruleScenario : RuleScenario(ruleScenario.checkScenarioValid('" + id + "')); \n");
        buffer.append("ruleParam : RuleParam(" + getUserTaskScenarioRule(scenarioName, scenarioService) + "); \n");
        buffer.append("then \n");
        buffer.append("ruleScenario.setScenarioInvalid('" + id + "'); \n");
        for (String str : outgoingElements)
        {
            buffer.append("ruleScenario.addScenario('" + str + "'); \n");
        }
        //buffer.append("System.out.println('"+scenarioName+"'); \n");
        buffer.append("update(ruleScenario); \n");
        buffer.append("end \n");
        return buffer.toString();
    }
    
    /**
     * 根据算法名称获得算法节点的rule规则
     * <功能详细描述>
     * @param processDefinitionEntity
     *       processDefinitionEntity
     * @param activityImpl
     *        activityImpl
     * @return  String
     * @see [类、类#方法、类#成员]
     */
    public static String getServiceTaskRule(ProcessDefinitionEntity processDefinitionEntity, ActivityImpl activityImpl)
    {
        String id = activityImpl.getId();
        List<String> outgoingElements = getOutgoingElements(processDefinitionEntity, id);
        String algorithmName = activityImpl.getProperty("name").toString();
        StringBuffer buffer = new StringBuffer();
        buffer.append("rule '" + algorithmName + "' \n");
        buffer.append("when \n");
        buffer.append("ruleScenario : RuleScenario(ruleScenario.checkScenarioValid('" + id + "')); \n");
        buffer.append("ruleTreatment : RuleTreatment(); \n");
        buffer.append("then \n");
        buffer.append("ruleScenario.setScenarioInvalid('" + id + "'); \n");
        for (String str : outgoingElements)
        {
            buffer.append("ruleScenario.addScenario('" + str + "'); \n");
        }
        buffer.append("ruleTreatment.handleRuleTreatment('" + id + "'); \n");
        //buffer.append("System.out.println('"+algorithmName+"'); \n");
        buffer.append("update(ruleScenario); \n");
        buffer.append("end \n");
        return buffer.toString();
    }
    
    /**
     * 获得哈希码
     * @return  int
     */
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }
    
    /**
     * 相等判断
     * @param obj
     *        obj
     * @return  boolean
     */
    @Override
    public boolean equals(Object obj)
    {
        return super.equals(obj);
    }
}
