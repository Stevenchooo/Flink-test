/*
 * 文 件 名:  RoleManageAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.tcc.domain.KeyValuePair;
import com.huawei.platform.tcc.domain.RolePrivilegeInfo;
import com.huawei.platform.tcc.domain.Search;
import com.huawei.platform.tcc.entity.OperatorInfoEntity;
import com.huawei.platform.tcc.entity.RoleDefinationEntity;
import com.huawei.platform.tcc.entity.RolePrivilegeInfoEntity;
import com.opensymphony.xwork2.ActionContext;

/**
 * 角色管理
 * 
 * @author  l00194471
 * @version [Internet Business Service Platform SP V100R100, 2012-6-19]
 */
public class RoleManageAction extends BaseAction
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1318129454607971013L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleManageAction.class);
    
    /**
     * 每页显示的条数
     */
    private int rows;
    
    /**
     * 当前页
     */
    private int page;
    
    /**
     * 保存为用户分配的角色信息
     * @return 操作成功标志符
     * @see [类、类#方法、类#成员]
     */
    public synchronized String saveUserRoleId()
    {
        String result = "false";
        List<OperatorInfoEntity> userList = new ArrayList<OperatorInfoEntity>();
        String roleId = null;
        Boolean isContinue = true;
        try
        {
            HttpServletRequest request =
                (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            roleId = request.getParameter("roleId");
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
                LOGGER.info("saveUserRoleId[{}] successfully!", new Object[] {userList});
                //记录审计信息
                result = "true";
            }
        }
        catch (DuplicateKeyException e)
        {
            result = "fasle,2,duplicateKey";
        }
        catch (Exception e)
        {
            LOGGER.error("failed to saveUserRoleId[{}].", new Object[] {userList, e});
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
     * 异步获取未分配角色的用户集合
     * @return 操作成功标志符
     * @throws Exception 异常
     */
    public String getUsersNoRoleId()
        throws Exception
    {
        List<OperatorInfoEntity> users = getOperatorMgnt().getUsersNoRoleId();
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
     * @see [类、类#方法、类#成员]
     */
    public String deleteRole()
        throws Exception
    {
        String result = "false";
        RoleDefinationEntity roleDef = new RoleDefinationEntity();
        RolePrivilegeInfoEntity rolePrivilegeInfo = new RolePrivilegeInfoEntity();
        try
        {
            //删除任务
            HttpServletRequest request =
                (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            
            try
            {
                String roleId = request.getParameter("roleId");
                if (null != roleId)
                {
                    roleDef.setRoleId(roleId);
                    rolePrivilegeInfo.setRoleId(roleId);
                    //删除业务
                    getOperatorMgnt().deleteRole(roleDef);
                    getOperatorMgnt().deleteRolePrivilege(rolePrivilegeInfo);
                    OperatorInfoEntity operator = new OperatorInfoEntity();
                    operator.setRoleId(roleId);
                    getOperatorMgnt().deleteInOperatorInfo(operator);
                    //记录日志
                    LOGGER.info("delete role[{}] sucess.", roleDef);
                    //记录审计信息
                    result = "true";
                }
            }
            catch (Exception e)
            {
                LOGGER.info("deleteRole error! role is {}.", roleDef, e);
            }
            
            setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
            return SUCCESS;
        }
        catch (Exception e)
        {
            LOGGER.error("failed to delete role[{}].", roleDef, e);
            throw e;
        }
    }
    
    /**
     * 保存角色信息
     * @return 操作成功标志符
     * @see [类、类#方法、类#成员]
     */
    public synchronized String saveRole()
    {
        String result = "false";
        RoleDefinationEntity roleDef = new RoleDefinationEntity();
        List<RolePrivilegeInfoEntity> rolePrivilegeInfos = new ArrayList<RolePrivilegeInfoEntity>();
        String roleId = "";
        Boolean roleReqAdd = false;
        Boolean isContinue = true;
        try
        {
            HttpServletRequest request =
                (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            roleId = request.getParameter("roleId");
            roleDef.setRoleId(roleId);
            roleDef.setDesc(request.getParameter("desc"));
            roleReqAdd = Boolean.parseBoolean(request.getParameter("roleReqAdd"));
            String rolePrivileges = request.getParameter("rolePrivileges");
            if (!StringUtils.isEmpty(rolePrivileges))
            {
                String[] rolePrivilegesArr = rolePrivileges.split(";");
                for (String rolePrivilege : rolePrivilegesArr)
                {
                    if (!StringUtils.isEmpty(rolePrivilege))
                    {
                        String[] roleAttrs = rolePrivilege.split("&");
                        RolePrivilegeInfoEntity rolePrivilegeInfo = new RolePrivilegeInfoEntity();
                        rolePrivilegeInfo.setRoleId(roleId);
                        int index = 0;
                        rolePrivilegeInfo.setServiceId(Integer.valueOf(roleAttrs[index++]));
                        rolePrivilegeInfo.setTaskGroup(roleAttrs[index++]);
                        rolePrivilegeInfo.setPrivilegeType(Integer.valueOf(roleAttrs[index]));
                        rolePrivilegeInfos.add(rolePrivilegeInfo);
                    }
                }
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
                    if (null != roleDef)
                    {
                        //新增记录
                        getOperatorMgnt().addRole(roleDef);
                        for (RolePrivilegeInfoEntity rolePrivilegeInfo : rolePrivilegeInfos)
                        {
                            getOperatorMgnt().addRolePrivilege(rolePrivilegeInfo);
                        }
                        //记录操作日志
                        LOGGER.info("add role[{}] successfully!", new Object[] {roleDef});
                        //记录审计信息
                        result = "true," + roleDef.getRoleId();
                    }
                }
                else
                {
                    //更新
                    if (null != roleDef)
                    {
                        //更新记录
                        getOperatorMgnt().updateRole(roleDef);
                        for (RolePrivilegeInfoEntity rolePrivilegeInfo : rolePrivilegeInfos)
                        {
                            if (rolePrivilegeInfo.getPrivilegeType() == -1)
                            {
                                getOperatorMgnt().deleteRolePrivilege(rolePrivilegeInfo);
                            }
                            else if (getOperatorMgnt().getRolePrivilegeCount(rolePrivilegeInfo) == 0)
                            {
                                getOperatorMgnt().addRolePrivilege(rolePrivilegeInfo);
                            }
                            else
                            {
                                getOperatorMgnt().updateRolePrivilege(rolePrivilegeInfo);
                            }
                        }
                        //记录日志
                        LOGGER.info("update role[{}] successfully!", new Object[] {roleDef});
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
            String opt = (null != roleReqAdd && roleReqAdd) ? "add" : "update";
            LOGGER.error("failed to " + opt + " the role[{}].", new Object[] {roleDef, e});
        }
        finally
        {
            roleDef = null;
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
     * 获取所有任务组和对应的权限类型
     * @return 查询成功标志位
     * @throws Exception 异常
     */
    public String reqPrivileges()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        try
        {
            request.setCharacterEncoding("UTF-8");
            List<RolePrivilegeInfo> privileges = null;
            Integer count = 0;
            String roleId = request.getParameter("roleId");
            Search search = new Search();
            search.setNames(roleId);
            search.setPageIndex((page - 1) * rows);
            search.setPageSize(rows);
            privileges = getOperatorMgnt().getPrivileges(search);
            Search serviceTGSearch = new Search();
            serviceTGSearch.setNames(null);
            count = getTccPortalService().getTaskGroupsCount(serviceTGSearch);
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", count);
            jsonObject.put("rows", privileges);
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            out.print(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")));
            return null;
        }
        catch (Exception e)
        {
            LOGGER.error("reqPrivileges fail", e);
            throw e;
        }
    }
    
    /**
     * 由查询条件获取角色
     * @return 查询成功标志位
     * @throws Exception 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public String reqRoles()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        try
        {
            request.setCharacterEncoding("UTF-8");
            List<RoleDefinationEntity> roles = null;
            Integer count = 0;
            String roleIds = request.getParameter("roleIds");
            List<String> roleList = new ArrayList<String>();
            if (!StringUtils.isEmpty(roleIds))
            {
                String[] roleIdArr = roleIds.split(";");
                //如果为以分号结尾,则保留分号，进行精确查询
                if (roleIdArr.length == 1 && roleIds.endsWith(";"))
                {
                    roleList.add(URLDecoder.decode(roleIdArr[0], "utf-8") + ";");
                }
                else
                {
                    for (String roleIdStr : roleIdArr)
                    {
                        if (!StringUtils.isEmpty(roleIdStr))
                        {
                            roleList.add(URLDecoder.decode(roleIdStr, "utf-8"));
                        }
                        else
                        {
                            roleList.add("");
                        }
                    }
                }
            }
            else
            {
                roleList.add("");
            }
            //以名字模糊查询
            Search search = new Search();
            search.setNames(roleList);
            search.setPageIndex((page - 1) * rows);
            search.setPageSize(rows);
            roles = getOperatorMgnt().getRolesByName(search);
            count = getOperatorMgnt().getRolesCountByName(search);
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", count);
            jsonObject.put("rows", roles);
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            out.print(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")));
            return null;
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
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        boolean containAllCol = false;
        try
        {
            containAllCol = Boolean.parseBoolean(request.getParameter("containAllCol"));
        }
        catch (Exception e)
        {
            LOGGER.warn("containAllCol must be true or false!", e);
        }
        //必须加上,防止前端从JSON中取出的数据成乱码
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        List<String> roleIds = getOperatorMgnt().getAllRoleIdList();
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        KeyValuePair keyValuePair;
        if (containAllCol)
        {
            keyValuePairList.add(new KeyValuePair("全部", "全部"));
        }
        for (String roleId : roleIds)
        {
            keyValuePair = new KeyValuePair(roleId, roleId);
            keyValuePairList.add(keyValuePair);
        }
        out.print(JSONObject.toJSONString(keyValuePairList));
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
