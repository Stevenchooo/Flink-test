/*
 * 文 件 名:  TaskInstanceDGAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 20013-2014,  All rights reserved
 * 描    述:  任务实例Action
 * 创 建 人:  z00190465
 * 创建时间:  2013-2-28
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.tcc.domain.Digraph;
import com.huawei.platform.tcc.domain.InstanceRelationSearch;
import com.huawei.platform.tcc.entity.TaskSearchEntity;
import com.huawei.platform.tcc.privilegeControl.Operator;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 任务实例Action
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2013-2-28]
 */
public class TaskInstanceDGAction extends BaseAction
{
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskInstanceDGAction.class);
    
    /**
     * 序号
     */
    private static final long serialVersionUID = -7231673995488869866L;
    
    /**
     * 获取任务实例关系构成的有向图JSON字符串
     * @return 获取任务实例关系构成的有向图JSON字符串
     * @throws Exception 异常
     */
    public String reqTaskInstanceDigraph()
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
            
            Integer priority = null;
            if (!StringUtils.isBlank(request.getParameter("priority")))
            {
                priority = Integer.parseInt(request.getParameter("priority"));
            }
            entity.setPriority(priority);
            
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
                taskIds = new ArrayList<Long>(0);
            }
            else
            {
                taskIds = getTccPortalService().getTaskIdsByNames(entity);
            }
            
            //构造实例关系检索对象
            InstanceRelationSearch insRelSearch = new InstanceRelationSearch();
            insRelSearch.setTaskIds(taskIds);
            
            //调度日期
            Date scheduleDate = null;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            df.setLenient(false);
            
            if (!StringUtils.isBlank(request.getParameter("scheduleDate")))
            {
                scheduleDate = df.parse(request.getParameter("scheduleDate"));
            }
            
            insRelSearch.setScheduleDate(scheduleDate);
            
            //期待执行日期
            Date expectExecuteDate = null;
            if (!StringUtils.isBlank(request.getParameter("expectExecuteDate")))
            {
                expectExecuteDate = df.parse(request.getParameter("expectExecuteDate"));
            }
            insRelSearch.setExpectExecuteDate(expectExecuteDate);
            
            Integer state = null;
            if (!StringUtils.isBlank(request.getParameter("state")))
            {
                state = Integer.parseInt(request.getParameter("state"));
            }
            
            insRelSearch.setState(state);
            
            //将taskList转成lineList
            Digraph digraph = getTccService().getDigraph(insRelSearch);
            
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            setInputStream(new ByteArrayInputStream(TccUtil.replace2Quotes(JSONObject.toJSONString(digraph,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
            
        }
        catch (ParseException e)
        {
            LOGGER.error("reqTaskInstanceDigraph failed!", e);
            throw e;
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("reqTaskInstanceDigraph failed!", e);
            throw e;
        }
        catch (IOException e)
        {
            LOGGER.error("reqTaskInstanceDigraph failed!", e);
            throw e;
        }
        catch (NullPointerException e)
        {
            LOGGER.error("reqTaskInstanceDigraph failed!", e);
            throw e;
        }
        return SUCCESS;
    }
}
