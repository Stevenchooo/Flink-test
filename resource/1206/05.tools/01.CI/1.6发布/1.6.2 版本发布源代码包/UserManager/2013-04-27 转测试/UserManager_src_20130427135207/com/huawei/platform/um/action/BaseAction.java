/*
 * 文 件 名:  BaseAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-2-17
 */
package com.huawei.platform.um.action;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.huawei.platform.um.audit.OperationRecord;
import com.huawei.platform.um.dao.UMDao;
import com.huawei.platform.um.entity.ServiceDefinationEntity;
import com.huawei.platform.um.privilegeControl.Operator;
import com.huawei.platform.um.privilegeControl.OperatorMgnt;
import com.huawei.platform.um.service.TccPortalService;
import com.huawei.platform.um.session.SessionContext;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * action基类
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2012-2-17]
 */
public class BaseAction extends ActionSupport
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = 5964814946821333205L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseAction.class);
    
    //dao
    private UMDao umDao;
    
    //portal业务
    private TccPortalService tccPortalService;
    
    //审计记录
    private transient OperationRecord operationRecord;
    
    //操作员管理类
    private transient OperatorMgnt operatorMgnt;
    
    // 输入流，用于写数据到前台
    private transient InputStream inputStream;
    
    /**
     * 获取当前的操作员[只能需要在action中调用]
     * @return 获取当前的操作员
     */
    public Operator getOperator()
    {
        String opName = OperatorMgnt.getOperatorName(getSession());
        return operatorMgnt.getOperator(opName);
    }
    
    /**
     * 获取当前的操作员的用户名[只能需要在action中调用]
     * @return 用户名
     */
    public String getUserName()
    {
        String opName = OperatorMgnt.getOperatorName(getSession());
        return opName;
    }
    
    //通过localthread或者sid获取会话
    private HttpSession getSession()
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        //会话不存在，则创建会话
        HttpSession session;
        //优先使用sid获取会话
        String sid = request.getParameter("sid");
        if (!StringUtils.isBlank(sid))
        {
            session = SessionContext.getInstance().getSession(sid);
        }
        else
        {
            //会话不存在，则创建会话
            session = request.getSession();
        }
        
        return session;
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
    
    public UMDao getUmDao()
    {
        return umDao;
    }
    
    public void setUmDao(UMDao umDao)
    {
        this.umDao = umDao;
    }
}
