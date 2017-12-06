/*
 * 文 件 名:  UserManageAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.um.action;

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
import com.huawei.platform.um.constants.ResultCode;
import com.huawei.platform.um.constants.UMConfig;
import com.huawei.platform.um.constants.type.AppType;
import com.huawei.platform.um.constants.type.NameStoredInSession;
import com.huawei.platform.um.constants.type.OperType;
import com.huawei.platform.um.constants.type.ReturnValue2PageType;
import com.huawei.platform.um.domain.KeyValuePair;
import com.huawei.platform.um.domain.ReturnValue2Page;
import com.huawei.platform.um.domain.UserSearch;
import com.huawei.platform.um.domain.UsernameAndPasswordParam;
import com.huawei.platform.um.entity.OperateAuditInfoEntity;
import com.huawei.platform.um.entity.OperatorInfoEntity;
import com.huawei.platform.um.entity.RoleDefinationEntity;
import com.huawei.platform.um.entity.UserInfoEntity;
import com.huawei.platform.um.entity.UserProfileEntity;
import com.huawei.platform.um.event.EventType;
import com.huawei.platform.um.event.Eventor;
import com.huawei.platform.um.privilegeControl.OperatorMgnt;
import com.huawei.platform.um.utils.UMUtil;
import com.huawei.platform.um.utils.crypt.CryptUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * 用户管理
 * 
 * @author  l00194471
 * @version [Device Cloud Base Platform Dept UserManager V100R100, 2012-6-19]
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
                        
                        //删除用户信息
                        getUmDao().deleteUserInfo(userName);
                        //删除用户配置信息
                        getUmDao().deleteUserProfile(userName);
                        
                        //通知改变
                        Eventor.fireEvent(this, EventType.DELETE_OPERATOR, operatorInfo.getOperatorName());
                        
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
        String encryPwd = "";
        try
        {
            //必需绑定角色
            operatorInfo.setRoleId(Integer.parseInt(request.getParameter("roleId")));
            operatorInfo.setOperatorName(request.getParameter("user"));
            operatorInfo.setDesc(request.getParameter("desc"));
            String pwd = StringUtils.isEmpty(request.getParameter("pwd")) ? null : request.getParameter("pwd");
            encryPwd = pwd == null ? null : UMUtil.encrypt(pwd);
            operatorInfo.setPwd(pwd == null ? null : CryptUtil.getMD5HexString(pwd));
            if (null != pwd && !pwd.equals(request.getParameter("pwdConfirm")))
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
                    operatorInfo.setCreateTime(new Date());
                    //新增记录
                    getOperatorMgnt().addUser(operatorInfo);
                    
                    //新增userprofile
                    UserProfileEntity profile = new UserProfileEntity();
                    profile.setUserName(operatorInfo.getOperatorName());
                    profile.setCptDefaultLoadNode("1");
                    profile.setCptLocation(operatorInfo.getOperatorName());
                    getUmDao().addOrUpdateUserProfile(profile);
                    
                    //新增user_info
                    UserInfoEntity userInfo = new UserInfoEntity();
                    userInfo.setUserName(operatorInfo.getOperatorName());
                    userInfo.setPassword(encryPwd);
                    getUmDao().addOrUpdateUserInfo(userInfo);
                    
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
                else
                {
                    //更新记录
                    getOperatorMgnt().updateUser(operatorInfo);
                    
                    //更新user_info
                    //UserInfoEntity userInfo = new UserInfoEntity();
                    //userInfo.setUserName(operatorInfo.getOperatorName());
                    //userInfo.setPassword(encryPwd);
                    //getUmDao().addOrUpdateUserInfo(userInfo);
                    //无法实现用户名密码同步，所以就不允许修改了
                    
                    //通知改变
                    Eventor.fireEvent(this, EventType.UPDATE_OPERATOR, operatorInfo.getOperatorName());
                    
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
        catch (DuplicateKeyException e)
        {
            result = "fasle,2,duplicateKey";
        }
        catch (Exception e)
        {
            String opt = (null != userReqAdd && userReqAdd) ? "add" : "update";
            LOGGER.error("failed to " + opt + " the user[{}].", new Object[] {operatorInfo, e});
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
            String roleName = request.getParameter("roleName");
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
            
            if (!StringUtils.isEmpty(roleName))
            {
                roleName = URLDecoder.decode(roleName, "utf-8");
                search.setRoleName(roleName);
            }
            
            search.setNames(userList);
            int page =
                StringUtils.isEmpty(request.getParameter("page")) ? 0 : Integer.valueOf(request.getParameter("page"));
            int rows =
                StringUtils.isEmpty(request.getParameter("rows")) ? 0 : Integer.valueOf(request.getParameter("rows"));
            
            search.setPageIndex((page - 1) * rows);
            search.setPageSize(rows);
            users = getOperatorMgnt().getUsers(search);
            Integer count = getOperatorMgnt().getUsersCount(search);
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
            throw new CException(ResultCode.SYSTEM_ERROR, e);
        }
    }
    
    /**
     * 由用户名获取用户
     * @return 查询成功标志位
     * @throws Exception 异常
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
                RoleDefinationEntity role = getOperatorMgnt().getRole(user.getRoleId());
                jsonObject.put("roleId", null == role ? "" : role.getRoleName());
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
            param.setUserName(request.getParameter("userName"));
            if (StringUtils.isEmpty(param.getUserName()))
            {
                isContinue = false;
            }
            if (StringUtils.isEmpty(newPwd))
            {
                isContinue = false;
                jsonObject.put("error", "emptyPwd");
            }
            
            param.setPassword(CryptUtil.getMD5HexString(oldPwd));
            
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
                operatorInfo.setOperatorName(param.getUserName());
                operatorInfo.setPwd(CryptUtil.getMD5HexString(newPwd));
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
            LOGGER.error("failed to modifyPwd[{}].", new Object[] {param.getUserName(), e});
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
