/*
 * 文 件 名:  BaseAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-2-17
 */
package com.huawei.platform.tcc.action;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.tcc.audit.OperationRecord;
import com.huawei.platform.tcc.constants.type.SubPrivilegeType;
import com.huawei.platform.tcc.entity.ServiceDefinationEntity;
import com.huawei.platform.tcc.entity.TaskEntity;
import com.huawei.platform.tcc.exception.PrivilegeNotEnoughException;
import com.huawei.platform.tcc.exception.PrivilegeNotEnoughParam;
import com.huawei.platform.tcc.privilegeControl.Operator;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.service.TccPortalService;
import com.huawei.platform.tcc.service.TccService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * action基类
 * 
 * @author  z00190465
 * @version [Internet Business Service Platform SP V100R100, 2012-2-17]
 */
public class BaseAction extends ActionSupport
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = 5964814946821333205L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseAction.class);
    
    //tcc业务
    private TccService tccService;
    
    //portal业务
    private TccPortalService tccPortalService;
    
    //审计记录
    private transient OperationRecord operationRecord;
    
    //操作员管理类
    private transient OperatorMgnt operatorMgnt;
    
    // 输入流，用于写数据到前台
    private transient InputStream inputStream;
    
    /**
     * 检查当前用户是否具有任务列表的查看权限
     * @param taskList 任务列表
     * @param operator 操作员
     * @throws PrivilegeNotEnoughException 权限不足异常
     */
    public void checkQueryPrivelge(Operator operator, final List<TaskEntity> taskList)
        throws PrivilegeNotEnoughException
    {
        if (null != taskList && !taskList.isEmpty())
        {
            Operator mOperator = operator;
            //权限处理
            if (null == mOperator)
            {
                mOperator = getOperator();
            }
            
            //获取原任务的业务和任务组
            List<TaskEntity> srcTasks = taskList;
            List<PrivilegeNotEnoughParam> params = new ArrayList<PrivilegeNotEnoughParam>();
            Integer srcServiceId;
            String srcGroupName;
            PrivilegeNotEnoughParam param;
            for (TaskEntity srcTask : srcTasks)
            {
                srcServiceId = srcTask.getServiceid();
                srcGroupName = srcTask.getServiceTaskGroup();
                //需要具有重做权限
                if (null == mOperator || !mOperator.canQuery(srcServiceId, srcGroupName))
                {
                    param =
                        new PrivilegeNotEnoughParam(getServiceName(srcServiceId), srcGroupName, SubPrivilegeType.QUERY);
                    
                    //避免重复提示
                    if (!params.contains(param))
                    {
                        params.add(param);
                    }
                }
            }
            
            if (!params.isEmpty())
            {
                throw new PrivilegeNotEnoughException(params);
            }
        }
    }
    
    /**
     * 获取当前的操作员[只能需要在action中调用]
     * @return 获取当前的操作员
     */
    public Operator getOperator()
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String operatorName = OperatorMgnt.getOperatorName(request.getSession());
        Operator operator = null;
        try
        {
            operator = operatorMgnt.createOperator(operatorName);
        }
        catch (Exception e)
        {
            LOGGER.error("getOperator failed.", e);
        }
        return operator;
    }
    
    /**
     * 检查是否有修改权限
     * @param taskE 任务
     * @throws Exception 数据库异常,权限不足异常
     */
    public void checkModify(TaskEntity taskE)
        throws Exception
    {
        List<PrivilegeNotEnoughParam> priParams = new ArrayList<PrivilegeNotEnoughParam>();
        Operator operator = getOperator();
        if (null != taskE)
        {
            if (null == operator || !operator.canModify(taskE.getServiceid(), taskE.getServiceTaskGroup()))
            {
                priParams.add(new PrivilegeNotEnoughParam(getServiceName(taskE.getServiceid()),
                    taskE.getServiceTaskGroup(), SubPrivilegeType.MODIFY));
            }
            
            if (!priParams.isEmpty())
            {
                throw new PrivilegeNotEnoughException(priParams);
            }
        }
    }
    
    /**
     * 检查是否有查询权限
     * @param taskE 任务
     * @throws Exception 数据库异常,权限不足异常
     */
    public void checkQuery(TaskEntity taskE)
        throws Exception
    {
        List<PrivilegeNotEnoughParam> priParams = new ArrayList<PrivilegeNotEnoughParam>();
        Operator operator = getOperator();
        if (null != taskE)
        {
            if (null == operator || !operator.canQuery(taskE.getServiceid(), taskE.getServiceTaskGroup()))
            {
                priParams.add(new PrivilegeNotEnoughParam(getServiceName(taskE.getServiceid()),
                    taskE.getServiceTaskGroup(), SubPrivilegeType.QUERY));
            }
            
            if (!priParams.isEmpty())
            {
                throw new PrivilegeNotEnoughException(priParams);
            }
        }
    }
    
    /**
     * 获取业务名
     * @param serviceId 业务Id
     * @return 业务名
     */
    public String getServiceName(Integer serviceId)
    {
        String servieName = Integer.toString(serviceId);
        try
        {
            ServiceDefinationEntity service = tccPortalService.getService(serviceId);
            if (null != service)
            {
                servieName = service.getServiceName();
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getServiceName error. serviceId is {}", serviceId, e);
        }
        
        return servieName;
    }
    
    /**
     * 将单引号括起来的json字符串转换成双引号
     * 
     * @param jsonSimpleQuote 单引号括起来的json字符串
     * @return 将单引号括起来的json字符串转换成双引号
     */
    public String replace2Quotes(String jsonSimpleQuote)
    {
        
        if (!StringUtils.isEmpty(jsonSimpleQuote))
        {
            StringBuilder sb = new StringBuilder(jsonSimpleQuote);
            char preChar = sb.charAt(0);
            //num为转义字符同时出现的个数
            int num = 0;
            int modeN = 1;
            //2为模数
            modeN++;
            
            if ('\'' == preChar)
            {
                sb.setCharAt(0, '"');
            }
            
            for (int i = 1; i < sb.length(); i++)
            {
                //将不以转义字符\开头的'替换成""
                if ('\\' == preChar)
                {
                    num = (num + 1) % modeN;
                }
                else
                {
                    num = 0;
                }
                
                if ('\'' == sb.charAt(i))
                {
                    if (num == 0)
                    {
                        sb.setCharAt(i, '"');
                    }
                    //为保证兼容ie与firefox，需要将 "d\'b" 替换成  "d'b" ,ie可以正确识别"\'"，但是firefox不行
                    else
                    {
                        //删除掉'\''中的\'替换成'
                        sb.deleteCharAt(i - 1);
                        i--;
                    }
                }
                
                preChar = sb.charAt(i);
            }
            return sb.toString();
        }
        else
        {
            return jsonSimpleQuote;
        }
    }
    
    public InputStream getInputStream()
    {
        return inputStream;
    }
    
    public void setInputStream(InputStream inputStream)
    {
        this.inputStream = inputStream;
    }
    
    public OperatorMgnt getOperatorMgnt()
    {
        return operatorMgnt;
    }
    
    public void setOperatorMgnt(OperatorMgnt operatorMgnt)
    {
        this.operatorMgnt = operatorMgnt;
    }
    
    public TccPortalService getTccPortalService()
    {
        return tccPortalService;
    }
    
    public void setTccService(TccService tccService)
    {
        this.tccService = tccService;
    }
    
    public TccService getTccService()
    {
        return tccService;
    }
    
    public void setTccPortalService(TccPortalService tccPortalService)
    {
        this.tccPortalService = tccPortalService;
    }
    
    public OperationRecord getOperationRecord()
    {
        return operationRecord;
    }
    
    public void setOperationRecord(OperationRecord operationRecord)
    {
        this.operationRecord = operationRecord;
    }
}
