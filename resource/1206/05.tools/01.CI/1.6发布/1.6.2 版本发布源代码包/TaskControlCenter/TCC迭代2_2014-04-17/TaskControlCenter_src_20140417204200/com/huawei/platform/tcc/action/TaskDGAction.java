/*
 * 文 件 名:  TaskDGAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 20013-2014,  All rights reserved
 * 描    述:  任务DG Action
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-28
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.type.RunningState;
import com.huawei.platform.tcc.domain.DGLink;
import com.huawei.platform.tcc.domain.DGNode;
import com.huawei.platform.tcc.domain.Digraph;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.entity.TaskSearchEntity;
import com.huawei.platform.tcc.privilegeControl.Operator;
import com.huawei.platform.tcc.task.Task;
import com.huawei.platform.tcc.task.TaskManage;
import com.huawei.platform.tcc.task.TaskRelation;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 任务DG Action
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2014-2-8]
 */
public class TaskDGAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskDGAction.class);
    
    /**
     * 序号
     */
    private static final long serialVersionUID = -7231673995488869866L;
    
    private transient TaskManage taskManage;
    
    public TaskManage getTaskManage()
    {
        return taskManage;
    }
    
    public void setTaskManage(TaskManage taskManage)
    {
        this.taskManage = taskManage;
    }
    
    /**
     * 获取任务关系构成的有向图JSON字符串
     * @return 获取任务实例关系构成的有向图JSON字符串
     * @throws Exception 异常
     */
    public String reqTaskDigraph()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            TaskSearchEntity entity = new TaskSearchEntity();
            //查询的任务ID列表，用”;“号分隔
            String searchTaskId = request.getParameter("searchTaskId");
            
            //查询周期类型
            String searchCycleType = request.getParameter("searchCycleType");
            
            //查询状态
            if (StringUtils.isNotEmpty(request.getParameter("searchTaskState")))
            {
                entity.setTaskState(Integer.parseInt(request.getParameter("searchTaskState")));
            }
            
            //查询周期类型
            if (StringUtils.isNotEmpty(searchCycleType))
            {
                entity.setCycleType(searchCycleType);
            }
            
            //查询任务类型
            if (StringUtils.isNotEmpty(request.getParameter("searchTaskType")))
            {
                entity.setTaskType(Integer.parseInt(request.getParameter("searchTaskType")));
            }
            
            //查询业务Id
            if (!StringUtils.isEmpty(request.getParameter("searchServiceId")))
            {
                entity.setServiceId(Integer.parseInt(request.getParameter("searchServiceId")));
            }
            
            //查询OS用户
            String searchOsUser = request.getParameter("searchOsUser");
            
            //0 表示全部选择任务ID
            if (!StringUtils.isEmpty(searchTaskId))
            {
                String[] taskIdArr = searchTaskId.split(";");
                //如果searchTaskId为以分号结尾的单任务名,则保留分号，进行精确查询
                if (taskIdArr.length == 1 && searchTaskId.endsWith(";"))
                {
                    entity.getTaskNames().add(URLDecoder.decode(taskIdArr[0], "utf-8") + ";");
                }
                else
                {
                    for (String taskIdStr : taskIdArr)
                    {
                        if (!StringUtils.isEmpty(taskIdStr))
                        {
                            entity.getTaskNames().add(URLDecoder.decode(taskIdStr, "utf-8"));
                        }
                        else
                        {
                            entity.getTaskNames().add("");
                        }
                    }
                }
            }
            else
            {
                entity.getTaskNames().add("");
            }
            
            if (!StringUtils.isEmpty(searchOsUser))
            {
                searchOsUser = new String(searchOsUser.getBytes("ISO8859-1"), "UTF-8");
                searchOsUser = URLDecoder.decode(searchOsUser, "UTF-8");
                entity.setOsUser(searchOsUser);
            }
            
            //数据过滤
            Operator operator = getOperator();
            
            if (null != operator)
            {
                entity.setVisibleGroups(operator.getVisibleGroups());
            }
            else
            {
                entity.setVisibleGroups(new ArrayList<String>(0));
            }
            
            List<Long> taskIds;
            //返回空数据
            if (null != entity.getVisibleGroups() && entity.getVisibleGroups().isEmpty())
            {
                taskIds = new ArrayList<Long>();
            }
            else
            {
                //否则以名字模糊查询
                taskIds = getTccPortalService().getTaskIdsByNames(entity);
                
            }
            
            //转换为有向图
            Digraph digraph = cov2Higraph(taskIds);
            
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            setInputStream(new ByteArrayInputStream(TccUtil.replace2Quotes(JSONObject.toJSONString(digraph,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("reqTaskDigraph failed", e);
            throw e;
        }
        catch (IOException e)
        {
            LOGGER.error("reqTaskDigraph failed", e);
            throw e;
        }
        catch (NullPointerException e)
        {
            LOGGER.error("reqTaskDigraph failed", e);
            throw e;
        }
        catch (CException e)
        {
            LOGGER.error("reqTaskDigraph failed", e);
            throw e;
        }
        catch (Exception e)
        {
            LOGGER.error("reqTaskDigraph failed", e);
            throw e;
        }
        return SUCCESS;
    }
    
    /**
     * 转化为调度图
     * @param taskIds 任务Id集合
     * @return 调度图
     * @throws CException 异常
     */
    private Digraph cov2Higraph(List<Long> taskIds)
        throws CException
    {
        Digraph digraph = new Digraph();
        digraph.setLinks(new ArrayList<DGLink>());
        //设置分层节点
        digraph.setHiNodes(new ArrayList<List<DGNode>>());
        
        //空直接返回
        if (null == taskIds || taskIds.isEmpty())
        {
            return digraph;
        }
        
        Map<String, DGNode> idNodes = new HashMap<String, DGNode>();
        Map<String, DGNode> nodeLefts = new HashMap<String, DGNode>();
        DGNode dgNode;
        TaskEntity taskE;
        //遍历可见周期集合
        for (Long taskId : taskIds)
        {
            taskE = taskManage.getTaskEntity(taskId);
            dgNode = new DGNode(taskE.getTaskName());
            dgNode.setTaskId(taskE.getTaskId());
            //设置状态
            dgNode.setState(null != taskE.getTaskState() && 0 == taskE.getTaskState() ? RunningState.SUCCESS
                : RunningState.ERROR);
            //构造任务Id名字键值对
            idNodes.put(dgNode.getName(), dgNode);
            nodeLefts.put(dgNode.getName(), dgNode);
        }
        
        List<DGLink> links;
        List<DGNode> curNodes = new ArrayList<DGNode>();
        digraph.getHiNodes().add(curNodes);
        
        //遍历可见周期集合
        for (Entry<String, DGNode> nodeEntry : idNodes.entrySet())
        {
            dgNode = nodeEntry.getValue();
            taskE = taskManage.getTaskEntity(dgNode.getTaskId());
            if (null == taskE)
            {
                LOGGER.warn("task[{}] not exist, ignore it!", dgNode.getTaskId());
                continue;
            }
            
            links = getDGLinks(taskE, idNodes);
            if (null == links || links.isEmpty())
            {
                //根节点
                curNodes.add(dgNode);
                //移除
                nodeLefts.remove(dgNode.getName());
            }
            else
            {
                //构造有向线段
                digraph.getLinks().addAll(links);
            }
        }
        
        List<DGNode> nNodes;
        DGNode nodeTemp;
        int n = 0;
        //不为空
        while (!nodeLefts.isEmpty())
        {
            n++;
            if (curNodes.isEmpty())
            {
                LOGGER.error("cov2Higraph failed, cicle may be exist!,current layer is {}, left {} nodes",
                    n,
                    nodeLefts.size());
                break;
            }
            
            nNodes = new ArrayList<DGNode>();
            for (DGNode node : curNodes)
            {
                //寻找直接所有子女节点
                for (DGLink link : node.getFromLinks())
                {
                    nodeTemp = link.getTargetNode();
                    if (!nNodes.contains(nodeTemp))
                    {
                        nNodes.add(nodeTemp);
                    }
                }
            }
            
            //如果子女节点中有作为子女的节点,则移除
            DGNode[] nodesCp = new DGNode[nNodes.size()];
            nNodes.toArray(nodesCp);
            for (DGNode node : nodesCp)
            {
                for (DGLink link : node.getFromLinks())
                {
                    nodeTemp = link.getTargetNode();
                    if (nNodes.contains(nodeTemp))
                    {
                        //移除子女兄弟
                        nNodes.remove(nodeTemp);
                    }
                }
            }
            
            //如果有父节点在剩余集合中的，则移除
            nodesCp = new DGNode[nNodes.size()];
            nNodes.toArray(nodesCp);
            for (DGNode node : nodesCp)
            {
                for (DGLink link : node.getToLinks())
                {
                    nodeTemp = link.getSourceNode();
                    if (nodeLefts.containsKey(nodeTemp.getName()))
                    {
                        //移除当前节点
                        nNodes.remove(node);
                    }
                }
            }
            
            digraph.getHiNodes().add(nNodes);
            //从剩余节点集合中删除
            for (DGNode node : nNodes)
            {
                nodeLefts.remove(node.getName());
            }
            
            curNodes = nNodes;
        }
        
        return digraph;
    }
    
    //获取依赖任务到当前任务的所有有向线段
    private List<DGLink> getDGLinks(TaskEntity taskE, Map<String, DGNode> idNodes)
    {
        List<DGLink> dgLinks = new ArrayList<DGLink>();
        String name = taskE.getTaskName();
        if (null == name)
        {
            return dgLinks;
        }
        
        //依赖关系为空直接返回
        Task taskDepends = null;
        try
        {
            taskDepends = taskManage.getTaskDepends(taskE.getTaskId());
        }
        catch (Exception e)
        {
            LOGGER.error("getTaskDepends failed! ignore it, taskId is {}.", taskE.getTaskId(), e);
            return dgLinks;
        }
        
        List<TaskRelation> subTaskRs = taskDepends.getSubTaskRs();
        if (null == subTaskRs || subTaskRs.isEmpty())
        {
            return dgLinks;
        }
        
        DGLink dgLink;
        String sNodeName;
        DGNode targetNode = idNodes.get(name);
        if (null == targetNode)
        {
            //节点不存在
            return dgLinks;
        }
        
        DGNode sourceNode;
        boolean moreDeps = false;
        for (TaskRelation taskRela : subTaskRs)
        {
            dgLink = new DGLink();
            sNodeName = taskRela.getDstTask().getTaskEntity().getTaskName();
            sourceNode = idNodes.get(sNodeName);
            //源不存在的依赖不再构建
            if (null == sourceNode)
            {
                moreDeps = true;
                continue;
            }
            
            dgLink.setSource(sourceNode.getName());
            dgLink.setTarget(targetNode.getName());
            //设置源节点和目标节点
            dgLink.setSourceNode(sourceNode);
            dgLink.setTargetNode(targetNode);
            
            //是否弱依赖
            dgLink.setType(taskRela.isIgnoreError());
            
            //设置源和目标节点的Link
            sourceNode.getFromLinks().add(dgLink);
            targetNode.getToLinks().add(dgLink);
            
            dgLinks.add(dgLink);
        }
        
        targetNode.setMoreTLs(moreDeps);
        
        return dgLinks;
    }
}
