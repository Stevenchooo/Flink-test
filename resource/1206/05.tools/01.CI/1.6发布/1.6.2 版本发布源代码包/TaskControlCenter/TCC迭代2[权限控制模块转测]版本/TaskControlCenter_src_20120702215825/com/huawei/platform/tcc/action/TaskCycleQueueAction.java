/*
 * 文 件 名:  TaskCycleQueueAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-2-17
 */
package com.huawei.platform.tcc.action;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.tcc.PrivilegeControl.Operator;
import com.huawei.platform.tcc.entity.CycleTaskDetailEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.opensymphony.xwork2.ActionContext;

/**
 * 任务周期队列
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-2-17]
 */
public class TaskCycleQueueAction extends BaseAction
{
    /**
     * 序列化
     */
    private static final long serialVersionUID = 2900090532378768051L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskCycleQueueAction.class);
    
    /**
     * 每页显示的条数
     */
    private int rows;
    
    /**
     * 当前页
     */
    private int page;
    
    /**
     * 任务周期运行队列的json格式
     * 
     * @return 任务周期运行队列的json格式
     * @throws Exception 数据库操作异常
     */
    public String reqRsRunningQueueJson()
        throws Exception
    {
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        //必须加上,防止前端从JSON中取出的数据成乱码
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        List<CycleTaskDetailEntity> cycleDetails = getTccService().getCycleDetailsRunning();
        cycleDetails = filterVisibleCycleDetails(cycleDetails);
        
        //JSONObject纯对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", cycleDetails.size());
        jsonObject.put("rows", cycleDetails);
        out.print(JSONObject.toJSONString(jsonObject, SerializerFeature.UseISO8601DateFormat));
        return null;
    }
    
    /**
     * 任务周期等待队列的json格式
     * 
     * @return 任务周期等待队列的json格式
     * @throws Exception 数据库操作异常
     */
    public String reqRsWaittingQueueJson()
        throws Exception
    {
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        //必须加上,防止前端从JSON中取出的数据成乱码
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        List<CycleTaskDetailEntity> cycleDetails = getTccService().getCycleDetailsWaitting();
        cycleDetails = filterVisibleCycleDetails(cycleDetails);
        
        //JSONObject纯对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", cycleDetails.size());
        jsonObject.put("rows", cycleDetails);
        out.print(JSONObject.toJSONString(jsonObject, SerializerFeature.UseISO8601DateFormat));
        return null;
    }
    
    /**
     * 过滤掉当前用户不可见的周期详细
     * 
     * @param cycleDetails 周期详细集合
     * @return 可见的周期详细集合
     * @throws Exception 数据库查询异常
     */
    private List<CycleTaskDetailEntity> filterVisibleCycleDetails(List<CycleTaskDetailEntity> cycleDetails)
        throws Exception
    {
        List<Long> taskIds = new ArrayList<Long>();
        for (CycleTaskDetailEntity cycleDetail : cycleDetails)
        {
            if (!taskIds.contains(cycleDetail.getTaskId()))
            {
                taskIds.add(cycleDetail.getTaskId());
            }
        }
        
        //可见的任务Id集合
        List<Long> visibleTaskIds = filterVisibleTaskIds(taskIds);
        
        List<CycleTaskDetailEntity> visibleCycleDetails = new ArrayList<CycleTaskDetailEntity>();
        for (CycleTaskDetailEntity cycleDetail : cycleDetails)
        {
            if (visibleTaskIds.contains(cycleDetail.getTaskId()))
            {
                visibleCycleDetails.add(cycleDetail);
            }
        }
        return visibleCycleDetails;
    }
    
    /**
     * 过滤掉当前用户不可见的任务Id集合
     * 
     * @param taskIds 待检查的任务Id集合
     * @return 可见的任务Id集合
     * @throws Exception 数据库查询异常
     */
    private List<Long> filterVisibleTaskIds(List<Long> taskIds)
        throws Exception
    {
        List<Long> visibleTaskIds = new ArrayList<Long>();
        if (null != taskIds && !taskIds.isEmpty())
        {
            //权限处理
            Operator operator = getOperator();
            if (null != operator)
            {
                //获取原任务的业务和任务组
                List<TaskEntity> srcTasks = getTccPortalService().getTaskList(taskIds);
                Integer srcServiceId;
                String srcGroupName;
                for (TaskEntity srcTask : srcTasks)
                {
                    srcServiceId = srcTask.getServiceid();
                    srcGroupName = srcTask.getServiceTaskGroup();
                    //需要具有重做权限
                    if (operator.canQuery(srcServiceId, srcGroupName))
                    {
                        visibleTaskIds.add(srcTask.getTaskId());
                    }
                }
            }
        }
        
        return visibleTaskIds;
    }
    
    public int getRows()
    {
        return rows;
    }
    
    public void setRows(int rows)
    {
        this.rows = rows;
    }
    
    public int getPage()
    {
        return page;
    }
    
    public void setPage(int page)
    {
        this.page = page;
    }
}
