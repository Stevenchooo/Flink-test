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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.ResultCodeConstants;
import com.huawei.platform.tcc.constants.type.NameStoredInSession;
import com.huawei.platform.tcc.constants.type.OperType;
import com.huawei.platform.tcc.constants.type.ReturnValue2PageType;
import com.huawei.platform.tcc.domain.KeyValuePair;
import com.huawei.platform.tcc.domain.ReturnValue2Page;
import com.huawei.platform.tcc.domain.Search;
import com.huawei.platform.tcc.domain.UserSearch;
import com.huawei.platform.tcc.domain.UsernameAndPasswordParam;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.huawei.platform.tcc.entity.OperatorInfoEntity;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
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
     * 删除用户信息
     * @return 操作成功的标志位
     * @throws Exception 统一封装的异常
     */
    public String deleteUser()
        throws Exception
    {
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
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
                    String userName = request.getParameter("user");
                    HttpSession session = request.getSession();
                    String operatorName = (String)session.getAttribute(NameStoredInSession.USER_NAME);
                    // 当前登录的用户不能自我删除
                    if (userName.equals(operatorName))
                    {
                        rv.setSuccess(false);
                        rv.setReturnValue2PageType(ReturnValue2PageType.DELECT_ITSELF);
                    }
                    else
                    {
                        operatorInfo.setOperatorName(userName);
                        //删除业务
                        getOperatorMgnt().deleteUser(operatorInfo);
                        //记录日志
                        LOGGER.info("delete user[{}] sucess.", operatorInfo);
                        rv.setSuccess(true);
                        rv.setReturnValue2PageType(ReturnValue2PageType.NORMAL);
                        
                        //记录审计信息
                        OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                        operateAuditInfo.setOpType(OperType.USER_DELETE);
                        operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                        operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                        operateAuditInfo.setOperParameters(operatorInfo.toString());
                        getOperationRecord().writeOperLog(operateAuditInfo);
                    }
                }
            }
            catch (Exception e)
            {
                LOGGER.info("deleteUser error! user is {}.", operatorInfo, e);
            }
            
            setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            LOGGER.error("failed to delete user[{}].", operatorInfo, e);
            throw e;
        }
        return SUCCESS;
    }
    
    /**
     * 保存用户信息
     * @return 操作成功标志符
     */
    public String saveUser()
    {
        String result = "false";
        OperatorInfoEntity operatorInfo = new OperatorInfoEntity();
        Boolean userReqAdd = false;
        Boolean isContinue = true;
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            operatorInfo.setRoleId(request.getParameter("roleId"));
            operatorInfo.setOsUserName(request.getParameter("OSUserName"));
            operatorInfo.setOperatorName(request.getParameter("user"));
            operatorInfo.setDesc(request.getParameter("desc"));
            operatorInfo.setPwd(StringUtils.isEmpty(request.getParameter("pwd")) ? null : request.getParameter("pwd"));
            if (operatorInfo.getPwd() != null && !operatorInfo.getPwd().equals(request.getParameter("pwdConfirm")))
            {
                isContinue = false;
                result = "false,3,differentPwd";
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
                        operatorInfo.setCreateTime(new Date());
                        String roleId = operatorInfo.getRoleId();
                        String osUserName = operatorInfo.getOsUserName();
                        Search osUserSearch = new Search();
                        List<String> osUserList = new ArrayList<String>();
                        osUserList.add(osUserName + ";");
                        osUserSearch.setNames(osUserList);
                        if (!StringUtils.isEmpty(roleId) && null == getOperatorMgnt().getRole(roleId))
                        {
                            result = "false,1,NoRoleId";
                        }
                        else if (!StringUtils.isEmpty(osUserName)
                            && 0 == getOperatorMgnt().getOSUsersCountByName(osUserSearch))
                        {
                            result = "false,4,NoOsUser";
                        }
                        else
                        {
                            //新增记录
                            getOperatorMgnt().addUser(operatorInfo);
                            //记录操作日志
                            LOGGER.info("add user[{}] successfully!", new Object[] {operatorInfo});
                            result = "true," + operatorInfo.getOperatorName();
                            
                            //记录审计信息
                            OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                            operateAuditInfo.setOpType(OperType.USER_ADD);
                            operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                            operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                            operateAuditInfo.setOperParameters(operatorInfo.toString());
                            getOperationRecord().writeOperLog(operateAuditInfo);
                        }
                    }
                }
                else
                {
                    //更新
                    if (null != operatorInfo)
                    {
                        String roleId = operatorInfo.getRoleId();
                        String osUserName = operatorInfo.getOsUserName();
                        Search osUserSearch = new Search();
                        List<String> osUserList = new ArrayList<String>();
                        osUserList.add(osUserName + ";");
                        osUserSearch.setNames(osUserList);
                        if (!StringUtils.isEmpty(roleId) && null == getOperatorMgnt().getRole(roleId))
                        {
                            result = "false,1,NoRoleId";
                        }
                        else if (!StringUtils.isEmpty(osUserName)
                            && 0 == getOperatorMgnt().getOSUsersCountByName(osUserSearch))
                        {
                            result = "false,4,NoOsUser";
                        }
                        else
                        {
                            //更新记录
                            getOperatorMgnt().updateUser(operatorInfo);
                            //记录日志
                            LOGGER.info("update user[{}] successfully!", new Object[] {operatorInfo});
                            result = "true";
                            
                            //记录审计信息
                            OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                            operateAuditInfo.setOpType(OperType.USER_MODIFY);
                            operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                            operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                            operateAuditInfo.setOperParameters(operatorInfo.toString());
                            getOperationRecord().writeOperLog(operateAuditInfo);
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
     */
    public String reqUsers()
        throws CException
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        try
        {
            request.setCharacterEncoding("UTF-8");
            List<OperatorInfoEntity> users = null;
            Integer count = 0;
            String roleId = request.getParameter("roleId");
            String osUser = request.getParameter("osUser");
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
            UserSearch search = new UserSearch();
            search.setNames(userList);
            if (!StringUtils.isEmpty(osUser))
            {
                search.setOsUser(osUser);
            }
            if (!StringUtils.isEmpty(roleId))
            {
                search.setRoleId(URLDecoder.decode(roleId, "UTF-8"));
            }
            
            int page =
                StringUtils.isEmpty(request.getParameter("page")) ? 0 : Integer.valueOf(request.getParameter("page"));
            int rows =
                StringUtils.isEmpty(request.getParameter("rows")) ? 0 : Integer.valueOf(request.getParameter("rows"));
            
            search.setPageIndex((page - 1) * rows);
            search.setPageSize(rows);
            users = getOperatorMgnt().getUsers(search);
            count = getOperatorMgnt().getUsersCount(search);
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", count);
            jsonObject.put("rows", users);
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            setInputStream(new ByteArrayInputStream(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
            return SUCCESS;
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
        try
        {
            request.setCharacterEncoding("UTF-8");
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
            setInputStream(new ByteArrayInputStream(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
            return SUCCESS;
        }
        catch (Exception e)
        {
            LOGGER.error("getUserInfoByName fail", e);
            throw e;
        }
    }
    
    /**
     * 操作员id键值对列表
     * @return 操作员Id键值对列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String reqOperators()
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
        UserSearch search = new UserSearch();
        List<OperatorInfoEntity> operatorInfos = getOperatorMgnt().getUsers(search);
        List<String> operators = new ArrayList<String>();
        for (int i = 0, n = operatorInfos.size(); i < n; i++)
        {
            operators.add(operatorInfos.get(i).getOperatorName());
        }
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        KeyValuePair keyValuePair;
        if (containAllCol)
        {
            keyValuePairList.add(new KeyValuePair("-1", "全部"));
        }
        for (String operator : operators)
        {
            keyValuePair = new KeyValuePair(operator, operator);
            keyValuePairList.add(keyValuePair);
        }
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(keyValuePairList).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 用户键值对列表
     * @return 用户键值对列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String reqUserNameList()
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
        UserSearch search = new UserSearch();
        List<OperatorInfoEntity> userInfoList = getOperatorMgnt().getUsers(search);
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        KeyValuePair keyValuePair;
        if (containAllCol)
        {
            keyValuePairList.add(new KeyValuePair("-1", "全部"));
        }
        for (OperatorInfoEntity userInfo : userInfoList)
        {
            keyValuePair = new KeyValuePair(userInfo.getOperatorName(), userInfo.getOperatorName());
            keyValuePairList.add(keyValuePair);
        }
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(keyValuePairList).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 用户修改密码
     * @return 操作成功标志符
     * @see [类、类#方法、类#成员]
     */
    public String modifyPwd()
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
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
            oldPwd = request.getParameter("oldPwd");
            newPwd = request.getParameter("newPwd");
            confirmPwd = request.getParameter("confirmPwd");
            param.setUsername(request.getParameter("userName"));
            param.setPassword(oldPwd);
            if (StringUtils.isEmpty(param.getUsername()))
            {
                isContinue = false;
            }
            if (StringUtils.isEmpty(newPwd))
            {
                isContinue = false;
                jsonObject.put("error", "emptyPwd");
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
                jsonObject.put("success", true);
                
                //记录审计信息
                OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                operateAuditInfo.setOpType(OperType.MODIFYPWD);
                operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                getOperationRecord().writeOperLog(operateAuditInfo);
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
            setInputStream(new ByteArrayInputStream(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
        }
        catch (IOException e1)
        {
            LOGGER.error("modifyPwd fail", e1);
        }
        return SUCCESS;
    }
}
