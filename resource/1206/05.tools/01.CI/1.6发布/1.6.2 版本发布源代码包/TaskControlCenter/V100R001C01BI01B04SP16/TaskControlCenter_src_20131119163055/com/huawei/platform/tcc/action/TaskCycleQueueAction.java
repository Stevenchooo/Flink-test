/*
 * 文 件 名:  TaskCycleQueueAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-2-17
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.tcc.entity.CycleTaskDetailEntity;
import com.huawei.platform.tcc.privilegeControl.Operator;
import com.opensymphony.xwork2.ActionContext;

/**
 * 任务周期队列
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-2-17]
 */
public class TaskCycleQueueAction extends BaseAction
{
    /**
     * 序列化
     */
    private static final long serialVersionUID = 2900090532378768051L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskCycleQueueAction.class);
    
    /**
     * 任务周期运行队列的json格式
     * 
     * @return 任务周期运行队列的json格式
     * @throws Exception 数据库操作异常
     */
    public String reqRsRunningQueueJson()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        List<CycleTaskDetailEntity> cycleDetails = getTccService().getCycleDetailsRunning();
        cycleDetails = filterVisibleCycleDetails(cycleDetails);
        
        int page =
            StringUtils.isEmpty(request.getParameter("page")) ? 0 : Integer.valueOf(request.getParameter("page"));
        int rows =
            StringUtils.isEmpty(request.getParameter("rows")) ? 0 : Integer.valueOf(request.getParameter("rows"));
        
        int startIndex = (page - 1) * rows;
        int endIndex = startIndex + rows;
        startIndex = startIndex > cycleDetails.size() ? cycleDetails.size() : startIndex;
        endIndex = endIndex > cycleDetails.size() ? cycleDetails.size() : endIndex;
        
        //JSONObject纯对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", cycleDetails.size());
        jsonObject.put("rows", cycleDetails.subList(startIndex, endIndex));
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(jsonObject,
            SerializerFeature.UseISO8601DateFormat).getBytes("UTF-8")));
        return SUCCESS;
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
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        
        int page =
            StringUtils.isEmpty(request.getParameter("page")) ? 0 : Integer.valueOf(request.getParameter("page"));
        int rows =
            StringUtils.isEmpty(request.getParameter("rows")) ? 0 : Integer.valueOf(request.getParameter("rows"));
        
        List<CycleTaskDetailEntity> cycleDetails = getTccService().getCycleDetailsWaitting();
        cycleDetails = filterVisibleCycleDetails(cycleDetails);
        int startIndex = (page - 1) * rows;
        int endIndex = startIndex + rows;
        startIndex = startIndex > cycleDetails.size() ? cycleDetails.size() : startIndex;
        endIndex = endIndex > cycleDetails.size() ? cycleDetails.size() : endIndex;
        
        //JSONObject纯对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", cycleDetails.size());
        jsonObject.put("rows", cycleDetails.subList(startIndex, endIndex));
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(jsonObject,
            SerializerFeature.UseISO8601DateFormat).getBytes("UTF-8")));
        return SUCCESS;
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
        List<CycleTaskDetailEntity> visibleCycleDetails = new ArrayList<CycleTaskDetailEntity>();
        Operator operator = getOperator();
        //不可见
        if (null == operator)
        {
            return visibleCycleDetails;
        }
        
        for (CycleTaskDetailEntity cycleDetail : cycleDetails)
        {
            if (operator.canQuery(cycleDetail.getUserGroup()))
            {
                visibleCycleDetails.add(cycleDetail);
            }
        }
        return visibleCycleDetails;
    }
}
