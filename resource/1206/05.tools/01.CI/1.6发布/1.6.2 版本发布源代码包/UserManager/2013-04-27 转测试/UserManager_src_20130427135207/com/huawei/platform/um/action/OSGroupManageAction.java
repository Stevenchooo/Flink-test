/*
 * 文 件 名:  UserManageAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  OS用户管理
 * 创 建 人:  z00190465
 * 创建时间:  2012-12-13
 */
package com.huawei.platform.um.action;

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
import com.huawei.platform.um.constants.type.OperType;
import com.huawei.platform.um.constants.type.ReturnValue2PageType;
import com.huawei.platform.um.dao.UMDao;
import com.huawei.platform.um.domain.KeyValuePair;
import com.huawei.platform.um.domain.OsGroupSearch;
import com.huawei.platform.um.domain.ReturnValue2Page;
import com.huawei.platform.um.entity.OSGroupInfoEntity;
import com.huawei.platform.um.entity.OperateAuditInfoEntity;
import com.huawei.platform.um.privilegeControl.OperatorMgnt;
import com.huawei.platform.um.utils.UMUtil;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.opensymphony.xwork2.ActionContext;

/**
 * OS组管理
 * 
 * @author  z00190465
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2012-12-13]
 */
public class OSGroupManageAction extends BaseAction
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1318129454607971013L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OSUserManageAction.class);
    
    //dao
    private UMDao umDao;
    
    /**
     * 获取OS组集合JSON字符串
     * @return OS组集合JSON字符串
     * @throws Exception 异常
     */
    public String reqOSGroups()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            //查询的任务ID列表，用”;“号分隔
            String searchOsGroup = request.getParameter("oSGroup");
            
            OsGroupSearch search = new OsGroupSearch();
            if (!StringUtils.isEmpty(request.getParameter("serviceId")))
            {
                search.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
            }
            
            if (!StringUtils.isEmpty(searchOsGroup))
            {
                searchOsGroup = new String(searchOsGroup.getBytes("ISO8859-1"), "UTF-8");
                searchOsGroup = URLDecoder.decode(searchOsGroup, "UTF-8");
                
                search.setUserGroup(searchOsGroup);
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
            Integer count = umDao.getOSGroupsCountBySearch(search);
            //否则以名字模糊查询
            List<OSGroupInfoEntity> groups = umDao.getOSGroupsBySearch(search);
            
            jsonObject.put("total", count);
            jsonObject.put("rows", groups);
            
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            setInputStream(new ByteArrayInputStream(UMUtil.replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
            
        }
        catch (NumberFormatException e)
        {
            LOGGER.error("search groups fail", e);
            throw e;
        }
        catch (IOException e)
        {
            LOGGER.error("search groups fail", e);
            throw e;
        }
        catch (NullPointerException e)
        {
            LOGGER.error("search groups fail", e);
            throw e;
        }
        return SUCCESS;
    }
    
    /**
     * OS用户键值对列表
     * @return OS用户键值对列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String reqOSGroupNames()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        boolean containAllCol = false;
        try
        {
            containAllCol = Boolean.parseBoolean(request.getParameter("containAllCol"));
        }
        catch (Exception e)
        {
            LOGGER.warn("containAllCol must be true or false!", e);
        }
        
        //获取所有的用户组
        List<OSGroupInfoEntity> groupList = getUmDao().getOSGroups();
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        KeyValuePair keyValuePair;
        if (containAllCol)
        {
            keyValuePairList.add(new KeyValuePair("-1", "全部"));
        }
        for (OSGroupInfoEntity group : groupList)
        {
            keyValuePair = new KeyValuePair(group.getUserGroup(), group.getUserGroup());
            keyValuePairList.add(keyValuePair);
        }
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(keyValuePairList).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 保存OS组信息
     * @return 操作成功标志符
     */
    public synchronized String saveOSGroup()
    {
        ReturnValue2Page rv = new ReturnValue2Page(true, ReturnValue2PageType.NORMAL);
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Boolean oSGroupReqAdd = "true".equals(request.getParameter("oSGroupReqAdd"));
        OSGroupInfoEntity osGroup = null;
        try
        {
            osGroup = new OSGroupInfoEntity();
            osGroup.setUserGroup(request.getParameter("userGroup"));
            osGroup.setDesc(request.getParameter("desc"));
            
            //新增
            if (oSGroupReqAdd)
            {
                //新增记录
                umDao.addOSGroup(osGroup);
                //记录操作日志
                LOGGER.info("add osGroup[{}] successfully!", new Object[] {osGroup});
                
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.OS_GROUP_ADD);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setOperParameters(osGroup.getUserGroup());
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
            else
            {
                //更新记录
                umDao.updateOSGroup(osGroup);
                
                //记录日志
                LOGGER.info("update osGroup[{}] successfully!", new Object[] {osGroup});
                
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.OS_GROUP_UPDATE);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setOperParameters(osGroup.getUserGroup());
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
            rv.setSuccess(false);
            String opt = (null != oSGroupReqAdd && oSGroupReqAdd) ? "add" : "update";
            LOGGER.error("failed to " + opt + " the osGroup[{}].", new Object[] {osGroup, e});
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
            LOGGER.error("save the role fail", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 删除OS组
     * @return 删除OS组
     * @throws Exception 异常
     */
    public String deleteOSGroup()
        throws Exception
    {
        ReturnValue2Page rv = new ReturnValue2Page(true, ReturnValue2PageType.NORMAL);
        //删除任务
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        String userGroup = request.getParameter("userGroup");
        try
        {
            if (null != userGroup)
            {
                OSGroupInfoEntity groupE = new OSGroupInfoEntity();
                groupE.setUserGroup(userGroup);
                //删除OS组
                umDao.deleteOSGroup(groupE);
                
                //记录日志
                LOGGER.info("delete group[{}] success.", userGroup);
                rv.setSuccess(true);
                
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.OS_GROUP_DELETE);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setOperParameters(userGroup);
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
        }
        catch (Exception e)
        {
            if (e.getCause() instanceof MySQLIntegrityConstraintViolationException)
            {
                rv.setSuccess(false);
                rv.setReturnValue2PageType(ReturnValue2PageType.FOREIGNKEY_CONSTRAINT);
            }
            LOGGER.info("deleteOSGroup error! userGroup is {}.", userGroup, e);
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
            LOGGER.error("failed to delete osGroup[{}].", userGroup, e);
            throw e;
        }
        
        return SUCCESS;
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
