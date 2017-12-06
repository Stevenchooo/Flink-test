/*
 * 文 件 名:  UserManageAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  OS用户管理
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-13
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.tcc.constants.type.OperType;
import com.huawei.platform.tcc.constants.type.ReturnValue2PageType;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.domain.KeyValuePair;
import com.huawei.platform.tcc.domain.ReturnValue2Page;
import com.huawei.platform.tcc.domain.Search;
import com.huawei.platform.tcc.entity.NodeInfoEntity;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.huawei.platform.tcc.event.EventType;
import com.huawei.platform.tcc.event.Eventor;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 节点管理
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-12-13]
 */
public class NodeManageAction extends BaseAction
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1318129454607971013L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(NodeManageAction.class);
    
    //dao
    private TccDao tccDao;
    
    /**
     * 节点键值对列表
     * @return 节点键值对
     * @throws Exception 数据库操作异常
     */
    public String reqNodes()
        throws Exception
    {
        //获取所有的节点键值对
        List<NodeInfoEntity> nodes = getTccDao().getNodeInfoList();
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        KeyValuePair keyValuePair;
        for (NodeInfoEntity node : nodes)
        {
            keyValuePair = new KeyValuePair(node.getNodeId().toString(), node.getName());
            keyValuePairList.add(keyValuePair);
        }
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(keyValuePairList).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 节点键值对列表
     * @return 节点键值对
     * @throws Exception 数据库操作异常
     */
    public String reqNodeInfos()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            //查询的任务ID列表，用”;“号分隔
            String nodeName = request.getParameter("nodeName");
            Search search = new Search();
            if (!StringUtils.isEmpty(nodeName))
            {
                nodeName = new String(nodeName.getBytes("ISO8859-1"), "UTF-8");
                nodeName = URLDecoder.decode(nodeName, "UTF-8");
                
                search.setNames(nodeName);
            }
            
            int page =
                StringUtils.isEmpty(request.getParameter("page")) ? 0 : Integer.valueOf(request.getParameter("page"));
            int rows =
                StringUtils.isEmpty(request.getParameter("rows")) ? 0 : Integer.valueOf(request.getParameter("rows"));
            
            search.setPageIndex((page - 1) * rows);
            search.setPageSize(rows);
            
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            
            //返回空数据
            Integer count = tccDao.getNodesCount(search);
            //否则以名字模糊查询
            List<NodeInfoEntity> nodes = tccDao.getNodes(search);
            
            jsonObject.put("total", count);
            jsonObject.put("rows", nodes);
            
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            setInputStream(new ByteArrayInputStream(TccUtil.replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
            
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("search nodes fail", e);
            throw e;
        }
        catch (IOException e)
        {
            LOGGER.error("search nodes fail", e);
            throw e;
        }
        catch (NullPointerException e)
        {
            LOGGER.error("search nodes fail", e);
            throw e;
        }
        return SUCCESS;
    }
    
    /**
     * 保存节点信息
     * @return 操作成功标志符
     */
    public String saveNode()
    {
        ReturnValue2Page rv = new ReturnValue2Page(true, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Boolean nodeReqAdd = "true".equals(request.getParameter("nodeReqAdd"));
        NodeInfoEntity node = null;
        try
        {
            node = new NodeInfoEntity();
            node.setName(request.getParameter("name"));
            node.setHost(request.getParameter("host"));
            if (StringUtils.isNotEmpty(request.getParameter("port")))
            {
                node.setPort(Integer.parseInt(request.getParameter("port")));
            }
            node.setDesc(request.getParameter("desc"));
            
            //新增
            if (nodeReqAdd)
            {
                //新增记录
                tccDao.addNodeInfo(node);
                
                //通知改变
                Eventor.fireEvent(this, EventType.ADD_NODE, node.getNodeId());
                
                //记录操作日志
                LOGGER.info("add node[{}] successfully!", new Object[] {TccUtil.truncatEncode(node)});
                
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.NODE_ADD);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setOperParameters(node.toString());
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
            else
            {
                node.setNodeId(Integer.parseInt(request.getParameter("nodeId")));
                
                //更新记录
                tccDao.updateNodeInfo(node);
                
                //通知改变
                Eventor.fireEvent(this, EventType.UPDATE_NODE, node.getNodeId());
                
                //记录日志
                LOGGER.info("update node[{}] successfully!", new Object[] {TccUtil.truncatEncode(node)});
                
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.NODE_UPDATE);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setOperParameters(node.toString());
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
        }
        catch (DuplicateKeyException e)
        {
            rv.setSuccess(false);
            rv.setReturnValue2PageType(ReturnValue2PageType.DUPLICATE_KEY);
        }
        catch (Exception e)
        {
            String opt = (null != nodeReqAdd && nodeReqAdd) ? "add" : "update";
            LOGGER.error("failed to " + opt + " the node[{}].", new Object[] {TccUtil.truncatEncode(node), e});
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("save the node fail", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 删除节点
     * @return 删除节点
     * @throws Exception 异常
     */
    public String deleteNode()
        throws Exception
    {
        ReturnValue2Page rv = new ReturnValue2Page(true, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Integer nodeId = null;
        try
        {
            nodeId = Integer.parseInt(request.getParameter("nodeId"));
            //删除节点
            tccDao.deleteNodeInfo(nodeId);
            
            //通知改变
            Eventor.fireEvent(this, EventType.DELETE_NODE, nodeId);
            
            //记录日志
            LOGGER.info("delete node[{}] success.", nodeId);
            rv.setSuccess(true);
            
            //记录审计信息
            OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
            operateAuditInfo.setOpType(OperType.NODE_DELETE);
            operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
            operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
            operateAuditInfo.setOperParameters(nodeId.toString());
            getOperationRecord().writeOperLog(operateAuditInfo);
        }
        catch (Exception e)
        {
            rv.setSuccess(false);
            LOGGER.info("deleteNode error! nodeId is {}.", nodeId, e);
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            LOGGER.error("failed to delete node[{}].", nodeId, e);
            throw e;
        }
        
        return SUCCESS;
    }
    
    public TccDao getTccDao()
    {
        return tccDao;
    }
    
    public void setTccDao(TccDao tccDao)
    {
        this.tccDao = tccDao;
    }
}
