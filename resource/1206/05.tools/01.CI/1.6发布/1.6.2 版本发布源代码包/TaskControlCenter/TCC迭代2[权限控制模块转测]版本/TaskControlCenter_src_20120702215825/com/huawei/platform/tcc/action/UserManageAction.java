/*
 * 文 件 名:  UserManageAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.ResultCodeConstants;
import com.huawei.platform.tcc.domain.Search;
import com.huawei.platform.tcc.domain.UsernameAndPasswordParam;
import com.huawei.platform.tcc.entity.OperatorInfoEntity;
import com.opensymphony.xwork2.ActionContext;

/**
 * 用户管理
 * 
 * @author  l00194471
 * @version [Internet Business Service Platform SP V100R100, 2012-6-19]
 */
public class UserManageAction extends BaseAction
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1318129454607971013L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(UserManageAction.class);
    
    /**
     * 每页显示的条数
     */
    private int rows;
    
    /**
     * 当前页
     */
    private int page;
    
//    /**
//     * 新增或更新标志
//     */
//    private Boolean userReqAdd;
//    
//    public Boolean getUserReqAdd()
//    {
//        return userReqAdd;
//    }
//    
//    public void setUserReqAdd(Boolean userReqAdd)
//    {
//        this.userReqAdd = userReqAdd;
//    }
    
    /**
     * 删除用户信息
     * @return 操作成功的标志位
     * @throws Exception 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public String deleteUser()
        throws Exception
    {
        String result = "false";
        OperatorInfoEntity operatorInfo = new OperatorInfoEntity();
        try
        {
            //删除任务
            HttpServletRequest request =
                (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            
            try
            {
                if (null != request.getParameter("user"))
                {
                    operatorInfo.setOperatorName(request.getParameter("user"));
                    
                    //删除业务
                    getOperatorMgnt().deleteUser(operatorInfo);
                    //记录日志
                    LOGGER.info("delete user[{}] sucess.", operatorInfo);
                    //记录审计信息
                    result = "true";
                }
            }
            catch (Exception e)
            {
                LOGGER.info("deleteUser error! user is {}.", operatorInfo, e);
            }
            
            setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
            return SUCCESS;
        }
        catch (Exception e)
        {
            LOGGER.error("failed to delete user[{}].", operatorInfo, e);
            throw e;
        }
    }
    
    /**
     * 保存用户信息
     * @return 操作成功标志符
     * @see [类、类#方法、类#成员]
     */
    public synchronized String saveUser()
    {
        String result = "false";
        OperatorInfoEntity operatorInfo = new OperatorInfoEntity();
        Boolean userReqAdd = false;
        Boolean isContinue = true;
        try
        {
            HttpServletRequest request =
                (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            operatorInfo.setRoleId(request.getParameter("roleId"));
            operatorInfo.setOsUserName(request.getParameter("OSUserName"));           
            operatorInfo.setOperatorName(request.getParameter("user"));
            operatorInfo.setDesc(request.getParameter("desc"));
            operatorInfo.setPwd(StringUtils.isEmpty(request.getParameter("pwd")) ? null : request.getParameter("pwd"));
            if (operatorInfo.getPwd() != null && !operatorInfo.getPwd().equals(request.getParameter("pwdConfirm")))
            {
                isContinue = false;
                result = "false, 3, differentPwd";
            }
            userReqAdd = Boolean.parseBoolean(request.getParameter("userReqAdd"));
        }
        catch (Exception e)
        {
            isContinue = false;
            LOGGER.error("saveUser error", e);
        }
        try
        {
            if (isContinue)
            {
                //新增
                if (userReqAdd)
                {
                    if (null != operatorInfo)
                    {
                        String roleId = operatorInfo.getRoleId();
                        if (StringUtils.isEmpty(roleId) || null != getOperatorMgnt().getRole(roleId))
                        {
                            //新增记录
                            getOperatorMgnt().addUser(operatorInfo);
                            //记录操作日志
                            LOGGER.info("add user[{}] successfully!", new Object[] {operatorInfo});
                            //记录审计信息
                            result = "true," + operatorInfo.getOperatorName();
                        }
                        else
                        {
                            result = "false,1,NoRoleId";
                        }
                    }
                }
                else
                {
                    //更新
                    if (null != operatorInfo)
                    {
                        String roleId = operatorInfo.getRoleId();
                        if (StringUtils.isEmpty(roleId) || null != getOperatorMgnt().getRole(roleId))
                        {
                            //更新记录
                            getOperatorMgnt().updateUser(operatorInfo);
                            //记录日志
                            LOGGER.info("update user[{}] successfully!", new Object[] {operatorInfo});
                            //记录审计信息
                            result = "true";
                        }
                        else
                        {
                            result = "false,1,NoRoleId";
                        }
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
            String opt = (null != userReqAdd && userReqAdd) ? "add" : "update";
            LOGGER.error("failed to " + opt + " the user[{}].", new Object[] {operatorInfo, e});
        }
        finally
        {
            operatorInfo = null;
        }
        
        try
        {
            setInputStream(new ByteArrayInputStream(result.getBytes("UTF-8")));
        }
        catch (UnsupportedEncodingException e1)
        {
            LOGGER.error("save the user fail", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 由查询条件获取用户
     * @return 查询成功标志位
     * @throws CException 统一封装的异常
     * @see [类、类#方法、类#成员]
     */
    public String reqUsers()
        throws CException
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        try
        {
            request.setCharacterEncoding("UTF-8");
            List<OperatorInfoEntity> users = null;
            Integer count = 0;
            String roleId = request.getParameter("roleId");
            if (!StringUtils.isEmpty(roleId))
            {
                users = getOperatorMgnt().getUsersByRoleId(roleId);
                count = getOperatorMgnt().getUsersCountByRoleId(roleId);
            }
            else
            {
                List<String> userList = new ArrayList<String>();
                String userNames = request.getParameter("userNames");
                if (!StringUtils.isEmpty(userNames))
                {
                    String[] userNameArr = userNames.split(";");
                    //如果searchTaskId为以分号结尾的单任务名,则保留分号，进行精确查询
                    if (userNameArr.length == 1 && userNames.endsWith(";"))
                    {
                        userList.add(URLDecoder.decode(userNameArr[0], "utf-8") + ";");
                    }
                    else
                    {
                        for (String userNameStr : userNameArr)
                        {
                            if (!StringUtils.isEmpty(userNameStr))
                            {
                                userList.add(URLDecoder.decode(userNameStr, "utf-8"));
                            }
                            else
                            {
                                userList.add("");
                            }
                        }
                    }
                }
                else
                {
                    userList.add("");
                }
                //以名字模糊查询
                Search search = new Search();
                search.setNames(userList);
                search.setPageIndex((page - 1) * rows);
                search.setPageSize(rows);
                users = getOperatorMgnt().getUsersByName(search);
                count = getOperatorMgnt().getUsersCountByName(search);
            }
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", count);
            jsonObject.put("rows", users);
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            out.print(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")));
            return null;
        }
        catch (Exception e)
        {
            LOGGER.error("reqUsers fail", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 由用户名获取用户
     * @return 查询成功标志位
     * @throws Exception 异常
     * @see [类、类#方法、类#成员]
     */
    public String getUserInfoByName()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        try
        {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            String userName = request.getParameter("userName");
            OperatorInfoEntity user = getOperatorMgnt().getOperatorInfo(userName);
            if (null != user)
            {
                //记录日志
                LOGGER.info("getUserInfoByName[{}] success.", userName);
                jsonObject.put("success", true);
                jsonObject.put("roleId", user.getRoleId());
                jsonObject.put("desc", user.getDesc());
                jsonObject.put("createTime", user.getCreateTime());
            }
            else
            {
                //记录日志
                LOGGER.info("getUserInfoByName[{}] fail.", userName);
                jsonObject.put("success", false);
            }
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            out.print(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")));
            return null;
        }
        catch (Exception e)
        {
            LOGGER.error("getUserInfoByName fail", e);
            throw e;
        }
    }
    
    /**
     * 用户修改密码
     * @return 操作成功标志符
     * @see [类、类#方法、类#成员]
     */
    public synchronized String modifyPwd()
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        //JSONObject纯对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", false);
        UsernameAndPasswordParam param = new UsernameAndPasswordParam();
        String oldPwd = "";
        String newPwd = "";
        String confirmPwd = "";
        Boolean isContinue = true;
        try
        {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            oldPwd = request.getParameter("oldPwd");
            newPwd = request.getParameter("newPwd");
            confirmPwd = request.getParameter("confirmPwd");
            param.setUsername(request.getParameter("userName"));
            param.setPassword(oldPwd);
            if (StringUtils.isEmpty(param.getUsername()))
            {
                isContinue = false;
            }
            if (null == getOperatorMgnt().getOperatorInfo(param))
            {
                isContinue = false;
                jsonObject.put("error", "wrongPwd");
            }
            if (newPwd != null && !newPwd.equals(confirmPwd))
            {
                isContinue = false;
                jsonObject.put("error", "differentPwd");
            }
        }
        catch (Exception e)
        {
            isContinue = false;
            LOGGER.error("modifyPwd error", e);
        }
        try
        {
            if (isContinue)
            {
                //更新记录
                OperatorInfoEntity operatorInfo = new OperatorInfoEntity();
                operatorInfo.setOperatorName(param.getUsername());
                operatorInfo.setPwd(newPwd);
                getOperatorMgnt().updateUser(operatorInfo);
                //记录日志
                LOGGER.info("modifyPwd[{}] successfully!", new Object[] {operatorInfo.getOperatorName()});
                //记录审计信息
                jsonObject.put("success", true);
            }
        }
        catch (DuplicateKeyException e)
        {
            jsonObject.put("error", "duplicateKey");
        }
        catch (Exception e)
        {
            LOGGER.error("failed to modifyPwd[{}].", new Object[] {param.getUsername(), e});
        }
        try
        {
            PrintWriter out = response.getWriter();
            out.print(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")));
        }
        catch (IOException e1)
        {
            LOGGER.error("modifyPwd fail", e1);
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
