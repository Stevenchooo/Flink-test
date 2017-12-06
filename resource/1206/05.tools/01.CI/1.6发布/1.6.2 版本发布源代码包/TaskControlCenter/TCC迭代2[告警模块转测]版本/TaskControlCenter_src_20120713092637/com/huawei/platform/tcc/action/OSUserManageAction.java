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
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.constants.type.ForeignKeyConstraintType;
import com.huawei.platform.tcc.constants.type.OperType;
import com.huawei.platform.tcc.constants.type.ReturnValue2PageType;
import com.huawei.platform.tcc.domain.KeyValuePair;
import com.huawei.platform.tcc.domain.ReturnValue2Page;
import com.huawei.platform.tcc.domain.Search;
import com.huawei.platform.tcc.domain.UserSearch;
import com.huawei.platform.tcc.entity.OSUserInfoEntity;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.huawei.platform.tcc.entity.OperatorInfoEntity;
import com.opensymphony.xwork2.ActionContext;

/**
 * 用户管理
 * 
 * @author  l00194471
 * @version [Internet Business Service Platform SP V100R100, 2012-6-19]
 */
public class OSUserManageAction extends BaseAction
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1318129454607971013L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OSUserManageAction.class);
    
    /**
     * 删除OS用户信息
     * @return 操作成功的标志位
     * @throws Exception 异常
     */
    public String deleteOSUser()
        throws Exception
    {
        OSUserInfoEntity mOSUserInfo = new OSUserInfoEntity();
        try
        {
            ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
            //删除任务
            HttpServletRequest request =
                (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            HttpServletResponse response =
                (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            String mOSUser = request.getParameter("OSUser");
            if (null != mOSUser)
            {
                UserSearch userSearch = new UserSearch();
                userSearch.setOsUser(mOSUser);
                // 检查是否有用户的os用户设置为要删除的os用户
                if (0 != getOperatorMgnt().getUsersCount(userSearch))
                {
                    LOGGER.info("delete OSUser[{}] fail.", mOSUserInfo);
                    rv.setReturnValue2PageType(ReturnValue2PageType.FOREIGNKEY_CONSTRAINT);
                    rv.addReturnValue(ForeignKeyConstraintType.USER);
                }
                else
                {
                    mOSUserInfo.setUserName(mOSUser);
                    //删除业务
                    getOperatorMgnt().deleteOSUser(mOSUserInfo);
                    //记录日志
                    LOGGER.info("delete OSUser[{}] success.", mOSUserInfo);
                    rv.setSuccess(true);
                    
                    //记录审计信息
                    OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                    operateAuditInfo.setOpType(OperType.OSUSER_DELETE);
                    operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                    operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                    operateAuditInfo.setOperParameters(mOSUserInfo.toString());
                    getOperationRecord().writeOperLog(operateAuditInfo);
                }
            }
            
            setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            LOGGER.error("failed to delete OSUser[{}].", mOSUserInfo, e);
            throw e;
        }
        return SUCCESS;
    }
    
    /**
     * 保存OS用户信息
     * @return 操作成功标志符
     */
    public synchronized String saveOSUser()
    {
        OSUserInfoEntity mOSUserInfoEntity = new OSUserInfoEntity();
        Boolean userReqAdd = false;
        Boolean isContinue = true;
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        //JSONObject纯对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", false);
        try
        {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            mOSUserInfoEntity.setUserName(request.getParameter("OSUser"));
            mOSUserInfoEntity.setDesc(request.getParameter("desc"));
            mOSUserInfoEntity.setPwd(StringUtils.isEmpty(request.getParameter("pwd")) ? null
                : request.getParameter("pwd"));
            if (mOSUserInfoEntity.getPwd() != null
                && !mOSUserInfoEntity.getPwd().equals(request.getParameter("pwdConfirm")))
            {
                isContinue = false;
                jsonObject.put("error", "diffrentPwd");
            }
            userReqAdd = Boolean.parseBoolean(request.getParameter("OSUserReqAdd"));
        }
        catch (Exception e)
        {
            isContinue = false;
            LOGGER.error("saveOSUser error!", e);
        }
        try
        {
            if (isContinue)
            {
                //新增
                if (userReqAdd)
                {
                    if (null != mOSUserInfoEntity)
                    {
                        //新增记录
                        getOperatorMgnt().addOSUser(mOSUserInfoEntity);
                        //记录操作日志
                        LOGGER.info("add OSUser[{}] successfully!", new Object[] {mOSUserInfoEntity});
                        jsonObject.put("success", true);
                        
                        //记录审计信息
                        OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                        operateAuditInfo.setOpType(OperType.OSUSER_ADD);
                        operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                        operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                        operateAuditInfo.setOperParameters(mOSUserInfoEntity.toString());
                        getOperationRecord().writeOperLog(operateAuditInfo);
                    }
                }
                else
                {
                    //更新
                    if (null != mOSUserInfoEntity)
                    {
                        //更新记录
                        getOperatorMgnt().updateOSUser(mOSUserInfoEntity);
                        //记录日志
                        LOGGER.info("update OSUser[{}] successfully!", new Object[] {mOSUserInfoEntity});
                        jsonObject.put("success", true);
                        
                        //记录审计信息
                        OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                        operateAuditInfo.setOpType(OperType.OSUSER_MODIFY);
                        operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                        operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                        operateAuditInfo.setOperParameters(mOSUserInfoEntity.toString());
                        getOperationRecord().writeOperLog(operateAuditInfo);
                    }
                }
            }
        }
        catch (DuplicateKeyException e)
        {
            jsonObject.put("error", "duplicateKey");
        }
        catch (Exception e)
        {
            String opt = (null != userReqAdd && userReqAdd) ? "add" : "update";
            LOGGER.error("failed to " + opt + " the OSUser[{}].", new Object[] {mOSUserInfoEntity, e});
        }
        
        try
        {
            PrintWriter out = response.getWriter();
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            out.print(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")));
        }
        catch (IOException e1)
        {
            LOGGER.error("saveOSUser fail", e1);
        }
        return null;
    }
    
    /**
     * 由查询条件获取OS用户
     * @return 查询成功标志位
     * @throws Exception 异常
     */
    public String reqOSUsers()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        try
        {
            request.setCharacterEncoding("UTF-8");
            List<OSUserInfoEntity> users = null;
            Integer count = 0;
            List<String> userList = new ArrayList<String>();
            String userNames = request.getParameter("OSUserNames");
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
            
            int page =
                StringUtils.isEmpty(request.getParameter("page")) ? 0 : Integer.valueOf(request.getParameter("page"));
            int rows =
                StringUtils.isEmpty(request.getParameter("rows")) ? 0 : Integer.valueOf(request.getParameter("rows"));
            
            search.setPageIndex((page - 1) * rows);
            search.setPageSize(rows);
            users = getOperatorMgnt().getOSUsersByName(search);
            count = getOperatorMgnt().getOSUsersCountByName(search);
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
            LOGGER.error("reqOSUsers fail", e);
            throw e;
        }
    }
    
    /**
     * OS用户键值对列表
     * @return OS用户键值对列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String reqOSUserNames()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        //必须加上,防止前端从JSON中取出的数据成乱码
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        boolean containAllCol = false;
        try
        {
            containAllCol = Boolean.parseBoolean(request.getParameter("containAllCol"));
        }
        catch (Exception e)
        {
            LOGGER.warn("containAllCol must be true or false!", e);
        }
        Search search = new Search();
        List<OSUserInfoEntity> mOSUserInfoList = getOperatorMgnt().getOSUsersByName(search);
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        KeyValuePair keyValuePair;
        if (containAllCol)
        {
            keyValuePairList.add(new KeyValuePair("-1", "全部"));
        }
        for (OSUserInfoEntity mOSUserInfo : mOSUserInfoList)
        {
            keyValuePair = new KeyValuePair(mOSUserInfo.getUserName(), mOSUserInfo.getUserName());
            keyValuePairList.add(keyValuePair);
        }
        out.print(JSONObject.toJSONString(keyValuePairList));
        return null;
    }
}
