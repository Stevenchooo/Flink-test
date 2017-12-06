/*
 * 文 件 名:  RoleManageAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  角色管理
 * 创 建 人:  l00194471
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.tcc.constants.type.ForeignKeyConstraintType;
import com.huawei.platform.tcc.constants.type.OperType;
import com.huawei.platform.tcc.constants.type.ReturnValue2PageType;
import com.huawei.platform.tcc.domain.KeyValuePair;
import com.huawei.platform.tcc.domain.ReturnValue2Page;
import com.huawei.platform.tcc.domain.RoleSearch;
import com.huawei.platform.tcc.domain.UserSearch;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.huawei.platform.tcc.entity.OperatorInfoEntity;
import com.huawei.platform.tcc.entity.RoleDefinationEntity;
import com.huawei.platform.tcc.event.EventType;
import com.huawei.platform.tcc.event.Eventor;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.privilegeControl.Role;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 角色管理
 * 
 * @author  l00194471
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-6-19]
 */
public class RoleManageAction extends BaseAction
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1318129454607971013L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleManageAction.class);
    
    /**
     * 保存为用户分配的角色信息
     * @return 操作成功标志符
     */
    public String saveUserRoleId()
    {
        String result = "false";
        List<OperatorInfoEntity> userList = new ArrayList<OperatorInfoEntity>();
        Integer roleId = null;
        Boolean isContinue = true;
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            roleId = Integer.parseInt(request.getParameter("roleId"));
            String users = request.getParameter("users");
            if (null != users)
            {
                String[] userArr = users.split(";");
                for (String userName : userArr)
                {
                    if (!StringUtils.isEmpty(userName))
                    {
                        OperatorInfoEntity user = new OperatorInfoEntity();
                        user.setOperatorName(userName);
                        user.setRoleId(roleId);
                        userList.add(user);
                    }
                }
            }
            else
            {
                isContinue = false;
            }
        }
        catch (Exception e)
        {
            isContinue = false;
            LOGGER.error("saveUserRoleId() error", e);
        }
        try
        {
            if (isContinue)
            {
                for (OperatorInfoEntity user : userList)
                {
                    //更新记录
                    getOperatorMgnt().updateUser(user);
                }
                //记录日志
                LOGGER.info("saveUserRoleId[{}] successfully!",
                    new Object[] {TccUtil.truncatEncode(userList.toString())});
                result = "true";
                
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.ROLE_BIND);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                operateAuditInfo.setOperParameters(userList.toString());
                getOperationRecord().writeOperLog(operateAuditInfo);
            }
        }
        catch (DuplicateKeyException e)
        {
            result = "fasle,2,duplicateKey";
        }
        catch (Exception e)
        {
            LOGGER.error("failed to saveUserRoleId[{}].", new Object[] {TccUtil.truncatEncode(userList.toString()), e});
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("saveUserRoleId() fail", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 异步获取未绑定OS用户的用户集合
     * @return 操作成功标志符
     * @throws Exception 异常
     */
    public String getUsersNoOsUser()
        throws Exception
    {
        List<OperatorInfoEntity> users = getOperatorMgnt().getUsersNoOsUser();
        StringBuilder returnValue = new StringBuilder();
        returnValue.append("true|");
        for (OperatorInfoEntity user : users)
        {
            returnValue.append(user.getOperatorName()).append(";");
        }
        returnValue.deleteCharAt(returnValue.lastIndexOf(";"));
        setInputStream(new ByteArrayInputStream(returnValue.toString().getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 删除角色信息
     * @return 操作成功的标志位
     * @throws Exception 异常
     */
    public String deleteRole()
        throws Exception
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        RoleDefinationEntity roleDef = new RoleDefinationEntity();
        try
        {
            //删除任务
            HttpServletRequest request =
                (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            
            try
            {
                Integer roleId = Integer.parseInt(request.getParameter("roleId"));
                if (null != roleId)
                {
                    UserSearch userSearch = new UserSearch();
                    userSearch.setRoleId(roleId);
                    // 检查是否有用户的os用户设置为要删除的os用户
                    if (0 != getOperatorMgnt().getUsersCount(userSearch))
                    {
                        LOGGER.info("deleteRole[{}] fail.", roleId);
                        rv.setReturnValue2PageType(ReturnValue2PageType.FOREIGNKEY_CONSTRAINT);
                        rv.addReturnValue(ForeignKeyConstraintType.USER);
                    }
                    else
                    {
                        roleDef.setRoleId(roleId);
                        //删除业务
                        getOperatorMgnt().deleteRole(roleDef);
                        
                        //通知改变
                        Eventor.fireEvent(this, EventType.DELETE_ROLE, roleId);
                        
                        //记录日志
                        LOGGER.info("delete role[{}] success.", roleDef);
                        rv.setSuccess(true);
                        
                        //记录审计信息
                        OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                        operateAuditInfo.setOpType(OperType.ROLE_DELETE);
                        operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                        operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                        operateAuditInfo.setOperParameters(roleDef.toString());
                        getOperationRecord().writeOperLog(operateAuditInfo);
                    }
                }
            }
            catch (Exception e)
            {
                LOGGER.info("deleteRole error! role is {}.", roleDef, e);
            }
            
            setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            LOGGER.error("failed to delete role[{}].", roleDef, e);
            throw e;
        }
        return SUCCESS;
    }
    
    /**
     * 保存角色信息
     * @return 操作成功标志符
     */
    public synchronized String saveRole()
    {
        String result = "false";
        RoleDefinationEntity roleDef = new RoleDefinationEntity();
        String roleName = "";
        Boolean roleReqAdd = false;
        Boolean isContinue = true;
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            roleName = request.getParameter("roleName");
            roleDef.setRoleName(roleName);
            roleDef.setDesc(request.getParameter("desc"));
            roleDef.setOsUsers(request.getParameter("osUsers"));
            roleDef.setOtherGroups(request.getParameter("otherGroups"));
            roleReqAdd = Boolean.parseBoolean(request.getParameter("roleReqAdd"));
            //更新
            if (!roleReqAdd)
            {
                roleDef.setRoleId(Integer.parseInt(request.getParameter("roleId")));
            }
            
        }
        catch (Exception e)
        {
            isContinue = false;
            LOGGER.error("saveRole error", e);
        }
        try
        {
            if (isContinue)
            {
                //新增
                if (roleReqAdd)
                {
                    roleDef.setCreateTime(new Date());
                    //新增记录
                    getOperatorMgnt().addRole(roleDef);
                    //记录操作日志
                    LOGGER.info("add role[{}] successfully!", new Object[] {TccUtil.truncatEncode(roleDef)});
                    result = "true," + roleDef.getRoleName();
                    
                    //记录审计信息
                    OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                    operateAuditInfo.setOpType(OperType.ROLE_ADD);
                    operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                    operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                    operateAuditInfo.setOperParameters(roleDef.toString());
                    getOperationRecord().writeOperLog(operateAuditInfo);
                }
                else
                {
                    //更新记录
                    getOperatorMgnt().updateRole(roleDef);
                    
                    //通知改变
                    Eventor.fireEvent(this, EventType.UPDATE_ROLE, roleDef.getRoleId());
                    
                    //记录日志
                    LOGGER.info("update role[{}] successfully!", new Object[] {TccUtil.truncatEncode(roleDef)});
                    result = "true";
                    
                    //记录审计信息
                    OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                    operateAuditInfo.setOpType(OperType.ROLE_MODIFY);
                    operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                    operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                    operateAuditInfo.setOperParameters(roleDef.toString());
                    getOperationRecord().writeOperLog(operateAuditInfo);
                }
            }
        }
        catch (DuplicateKeyException e)
        {
            result = "fasle,2,duplicateKey";
        }
        catch (Exception e)
        {
            String opt = (null != roleReqAdd && roleReqAdd) ? "add" : "update";
            LOGGER.error("failed to " + opt + " the role[{}].", new Object[] {TccUtil.truncatEncode(roleDef), e});
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("save the role fail", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 由查询条件获取角色
     * @return 查询成功标志位
     * @throws Exception 统一封装的异常
     */
    public String reqRoles()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            request.setCharacterEncoding("UTF-8");
            List<RoleDefinationEntity> roles = null;
            String roleName = request.getParameter("roleName");
            String osUsers = request.getParameter("osUsers");
            String otherGroups = request.getParameter("otherGroups");
            RoleSearch roleSearch = new RoleSearch();
            //设置查询字段值
            if (!StringUtils.isEmpty(roleName))
            {
                roleSearch.setRoleName(URLDecoder.decode(roleName, "utf-8"));
            }
            
            if (!StringUtils.isEmpty(osUsers))
            {
                roleSearch.setOsUsers(URLDecoder.decode(osUsers, "utf-8"));
            }
            
            if (!StringUtils.isEmpty(otherGroups))
            {
                roleSearch.setOtherGroups(URLDecoder.decode(otherGroups, "utf-8"));
            }
            
            //以名字模糊查询
            int page =
                StringUtils.isEmpty(request.getParameter("page")) ? 0 : Integer.valueOf(request.getParameter("page"));
            int rows =
                StringUtils.isEmpty(request.getParameter("rows")) ? 0 : Integer.valueOf(request.getParameter("rows"));
            
            roleSearch.setPageIndex((page - 1) * rows);
            roleSearch.setPageSize(rows);
            roles = getOperatorMgnt().getRolesByName(roleSearch);
            Integer count = getOperatorMgnt().getRolesCountByName(roleSearch);
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", count);
            jsonObject.put("rows", roles);
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            setInputStream(new ByteArrayInputStream(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
            return SUCCESS;
        }
        catch (Exception e)
        {
            LOGGER.error("reqRoles fail", e);
            throw e;
        }
    }
    
    /**
     * 角色id键值对列表
     * @return 角色Id键值对列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String reqRoleIds()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        boolean containAllCol = false;
        boolean containSystem = false;
        try
        {
            containAllCol = Boolean.parseBoolean(request.getParameter("containAllCol"));
            containSystem = Boolean.parseBoolean(request.getParameter("containSystem"));
        }
        catch (Exception e)
        {
            LOGGER.warn("containAllCol must be true or false!", e);
        }
        
        List<RoleDefinationEntity> roleIds = getOperatorMgnt().getAllRoleIdList();
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        KeyValuePair keyValuePair;
        if (containAllCol)
        {
            keyValuePairList.add(new KeyValuePair("-1", "全部"));
        }
        for (RoleDefinationEntity roleId : roleIds)
        {
            //移除系统管理员
            if (!containSystem && Role.SYS_ADMIN.equals(roleId.getRoleName()))
            {
                continue;
            }
            keyValuePair = new KeyValuePair(roleId.getRoleId().toString(), roleId.getRoleName());
            keyValuePairList.add(keyValuePair);
        }
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(keyValuePairList).getBytes("UTF-8")));
        return SUCCESS;
    }
}
