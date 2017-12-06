/*
 * 文 件 名:  TaskGroupAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.ResultCodeConstants;
import com.huawei.platform.tcc.entity.ServiceTaskGroupInfoEntity;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 业务任务组管理
 * 
 * @author  w00190929
 * @version [Internet Business Service Platform SP V100R100, 2012-2-17]
 */
public class TaskGroupAction extends BaseAction
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = -4278173599543862664L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskGroupAction.class);
    
    /**
     * 业务信息
     */
    private transient ServiceTaskGroupInfoEntity serviceTG;
    
    /**
     * 每页显示的条数
     */
    private int rows;
    
    /**
     * 当前页
     */
    private int page;
    
    /**
     * 保存任务组信息
     * @return 操作成功标志符
     * @see [类、类#方法、类#成员]
     */
    public String saveServiceTG()
    {
        String result = "false";
        serviceTG = new ServiceTaskGroupInfoEntity();
        Boolean serviceTGReqAdd = false;
        Boolean isContinue = true;
        try
        {
            HttpServletRequest request =
                (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            serviceTG.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
            serviceTG.setTaskGroup(request.getParameter("taskGroup"));
            serviceTG.setDesc(request.getParameter("desc"));
            serviceTGReqAdd = Boolean.parseBoolean(request.getParameter("serviceTGReqAdd"));
        }
        catch (Exception e)
        {
            isContinue = false;
            LOGGER.error("saveServiceTG error", e);
        }
        
        try
        {
            if (isContinue)
            {
                //新增
                if (serviceTGReqAdd)
                {
                    if (null != serviceTG)
                    {
                        //判断是否存在任务Id
                        if (null != getTccPortalService().getService(serviceTG.getServiceId()))
                        {
                            //新增记录
                            getTccPortalService().addTaskGroup(serviceTG);
                            //记录操作日志
                            LOGGER.info("add taskGroup[{}] successfully!", new Object[] {serviceTG});
                            //记录审计信息
                            result = "true," + Integer.toString(serviceTG.getServiceId());
                        }
                        else
                        {
                            result = "false,1," + Integer.toString(serviceTG.getServiceId());
                        }
                    }
                }
                else
                {
                    //更新
                    if (null != serviceTG)
                    {
                        //更新记录
                        getTccPortalService().updateTaskGroup(serviceTG);
                        //记录日志
                        LOGGER.info("update taskGroup[{}] successfully!", new Object[] {serviceTG});
                        //记录审计信息
                        result = "true";
                    }
                }
            }
        }
        catch (DuplicateKeyException e)
        {
            result = "fasle,2,duplicateKey";
        }
        catch (Exception e)
        {
            String opt = (null != serviceTGReqAdd && serviceTGReqAdd) ? "add" : "update";
            LOGGER.error("failed to " + opt + " the service[{}].", new Object[] {serviceTG, e});
        }
        finally
        {
            serviceTG = null;
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("save the service fail", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 由查询条件获取任务组信息
     * @return 查询成功标志位
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public String reqServiceTGs()
        throws CException
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        try
        {
            Integer serviceId = Integer.parseInt(request.getParameter("serviceId"));
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            
            //查询业务的所有任务组查询
            List<ServiceTaskGroupInfoEntity> serviceTGs = getTccPortalService().getTaskGroups(serviceId);
            jsonObject.put("total", serviceTGs.size());
            jsonObject.put("rows", serviceTGs);
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            out.print(TccUtil.replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")));
            return null;
        }
        catch (Exception e)
        {
            LOGGER.error("reqServiceTGs fail", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 删除任务组信息
     * @return 操作成功的标志位
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public String deleteServiceTG()
        throws CException
    {
        String result = "false";
        ServiceTaskGroupInfoEntity taskGroup = new ServiceTaskGroupInfoEntity();
        try
        {
            //删除任务
            HttpServletRequest request =
                (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            
            try
            {
                if (null != request.getParameter("serviceId"))
                {
                    taskGroup.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
                    taskGroup.setTaskGroup(request.getParameter("taskGroup"));
                    //TODO 删除角色权限
                    
                    //删除业务
                    getTccPortalService().deleteServiceTG(taskGroup);
                    //记录日志
                    LOGGER.info("delete serviceTG[{}] sucess.", taskGroup);
                    //记录审计信息
                    result = "true";
                }
            }
            catch (Exception e)
            {
                LOGGER.info("deleteServiceTG error! serviceTG is {}.", taskGroup, e);
            }
            
            setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
            return SUCCESS;
        }
        catch (Exception e)
        {
            LOGGER.error("failed to delete serviceTG[{}].", taskGroup, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    public ServiceTaskGroupInfoEntity getServiceDef()
    {
        return serviceTG;
    }
    
    public void setServiceTG(ServiceTaskGroupInfoEntity serviceTG)
    {
        this.serviceTG = serviceTG;
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
