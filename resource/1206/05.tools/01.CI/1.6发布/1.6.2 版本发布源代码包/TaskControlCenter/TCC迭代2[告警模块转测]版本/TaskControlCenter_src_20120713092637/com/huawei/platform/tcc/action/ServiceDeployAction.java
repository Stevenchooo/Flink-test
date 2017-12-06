/*
 * 文 件 名:  ServiceDeployAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-18
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.constants.ResultCodeConstants;
import com.huawei.platform.tcc.constants.type.NodeType;
import com.huawei.platform.tcc.constants.type.OperType;
import com.huawei.platform.tcc.domain.KeyValuePair;
import com.huawei.platform.tcc.entity.NodeInfoEntity;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.huawei.platform.tcc.entity.ServiceDeployInfoEntity;
import com.opensymphony.xwork2.ActionContext;

/**
 * 业务分布管理
 * 
 * @author  w00190929
 * @version [Internet Business Service Platform SP V100R100, 2012-2-17]
 */
public class ServiceDeployAction extends BaseAction
{
    
    /**
     * 序列号
     */
    private static final long serialVersionUID = 1948146075731512446L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceDeployAction.class);
    
    /**
     * 每页显示的条数
     */
    private int rows;
    
    /**
     * 当前页
     */
    private int page;
    
    /**
     * 获取业务部署信息
     * @return  操作成功标志位|业务部署信息
     * @throws Exception 统一封装的异常
     */
    public String getServiceDeployInfos() throws Exception
    {
        String result = "true";
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
                .get(ServletActionContext.HTTP_REQUEST);
        Integer serviceId = null;
        try
        {
            if (null != request.getParameter("serviceId"))
            {
                serviceId = Integer.parseInt(request.getParameter("serviceId"));
                
                ServiceDeployInfoEntity serviceDeploy = new ServiceDeployInfoEntity();
                serviceDeploy.setServiceId(serviceId);
                List<ServiceDeployInfoEntity> sdList = getTccPortalService().getServiceDeployInfo(serviceDeploy);
                result += "|";
                //返回的业务部署信息用分号分隔
                StringBuilder builder = new StringBuilder();
                for (ServiceDeployInfoEntity entity : sdList)
                {
                    builder.append(String.format("%d,%s,%s;",
                            entity.getNodeType(),
                            entity.getNodeIpAddr(),
                            (null == entity.getDataDirectory()) ? "" : entity.getDataDirectory()));
                }
                result += builder.toString();
            }
        }
        catch (Exception e)
        {
            result = "false";
            LOGGER.error("grab getServiceDeployInfo fail! serviceId is {}", serviceId, e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
        
        setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
        return "success";
    }
    
    /**
     * 保存业务部署信息
     * @return 操作成功标志符
     */
    public synchronized String saveServiceDeploy()
    {
        String result = "true";
        try
        {
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
                    .get(ServletActionContext.HTTP_REQUEST);
            
            ServiceDeployInfoEntity dataDeploy = new ServiceDeployInfoEntity();
            dataDeploy.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
            dataDeploy.setNodeType(NodeType.DATA_SERVER);
            dataDeploy.setNodeIpAddr(request.getParameter("dataNodeAddr"));
            dataDeploy.setDataDirectory(request.getParameter("dataDir"));
            if (getTccPortalService().addServiceDeploy(dataDeploy))
            {
                LOGGER.info("add serviceDeployInfo[{}].", dataDeploy);
            }
            else
            {
                LOGGER.info("update serviceDeployInfo[{}].", dataDeploy);
                getTccPortalService().updateServiceDeploy(dataDeploy);
            }
            
            ServiceDeployInfoEntity workDeploy = new ServiceDeployInfoEntity();
            workDeploy.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
            workDeploy.setNodeType(NodeType.HIVE_GATEWAY_SERVER);
            workDeploy.setNodeIpAddr(request.getParameter("hiveNodeAddr"));
            workDeploy.setDataDirectory(request.getParameter("workDir"));
            
            if (getTccPortalService().addServiceDeploy(workDeploy))
            {
                LOGGER.info("add serviceDeployInfo[{}].", workDeploy);
            }
            else
            {
                LOGGER.info("update serviceDeployInfo[{}].", workDeploy);
                getTccPortalService().updateServiceDeploy(workDeploy);
            }
            //记录审计信息
            OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
            operateAuditInfo.setOpType(OperType.SERVICEDEPLOY_UPDATE);
            operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
            operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
            operateAuditInfo.setServiceIdSingle(String.valueOf(dataDeploy.getServiceId()));
            operateAuditInfo.setOperParameters(dataDeploy.toString() + " " + workDeploy.toString());
            getOperationRecord().writeOperLog(operateAuditInfo);
        }
        catch (Exception e)
        {
            LOGGER.error("save serviceDeploy fail.", e);
            result = "false";
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e)
        {
            LOGGER.error("save serviceDeploy fail.", e);
        }
        return SUCCESS;
    }
    
    /**
     * 根据节点类型查询所有的节点地址
     * @return 查询成功标志位
     * @throws Exception 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public String getNodeAddrList() throws Exception
    {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
                .get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
                .get(ServletActionContext.HTTP_RESPONSE);
        try
        {
            NodeInfoEntity nodeInfo = new NodeInfoEntity();
            Integer nodeType = Integer.parseInt(request.getParameter("nodeType"));
            nodeInfo.setNodeType(nodeType);
            
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            
            //查询所有节点信息
            List<NodeInfoEntity> nodes = getTccPortalService().getNodeInfoList(nodeInfo);
            List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
            
            KeyValuePair keyValuePair;
            
            for (NodeInfoEntity node : nodes)
            {
                keyValuePair = new KeyValuePair(node.getNodeIpAddr(), node.getNodeIpAddr());
                keyValuePairList.add(keyValuePair);
            }
            
            out.print(JSONObject.toJSONString(keyValuePairList));
        }
        catch (Exception e)
        {
            LOGGER.error("getNodeAddrList fail", e);
            throw e;
        }
        return null;
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
