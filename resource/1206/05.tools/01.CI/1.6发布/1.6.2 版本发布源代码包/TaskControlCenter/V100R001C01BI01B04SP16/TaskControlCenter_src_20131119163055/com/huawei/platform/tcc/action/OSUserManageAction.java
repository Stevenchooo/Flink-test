/*
 * 文 件 名:  UserManageAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  OS用户管理
 * 创 建 人:  z00190465
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.io.IOException;
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
import com.huawei.platform.tcc.constants.type.OperType;
import com.huawei.platform.tcc.constants.type.ReturnValue2PageType;
import com.huawei.platform.tcc.dao.TccDao;
import com.huawei.platform.tcc.domain.KeyValuePair;
import com.huawei.platform.tcc.domain.ReturnValue2Page;
import com.huawei.platform.tcc.domain.Search;
import com.huawei.platform.tcc.entity.OSUserGroupServiceEntity;
import com.huawei.platform.tcc.entity.OSUserInfoEntity;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.huawei.platform.tcc.entity.TaskSearchEntity;
import com.huawei.platform.tcc.event.EventType;
import com.huawei.platform.tcc.event.Eventor;
import com.huawei.platform.tcc.privilegeControl.Operator;
import com.huawei.platform.tcc.privilegeControl.OperatorMgnt;
import com.huawei.platform.tcc.utils.TccUtil;
import com.huawei.platform.tcc.utils.crypt.CryptUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * OS用户管理
 * 
 * @author  l00194471
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-6-19]
 */
public class OSUserManageAction extends BaseAction
{
    /**
     * 序号
     */
    private static final long serialVersionUID = 1318129454607971013L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OSUserManageAction.class);
    
    private TccDao tccDao;
    
    /**
     * 删除OS用户信息
     * @return 操作成功的标志位
     * @throws Exception 异常
     */
    public String deleteOSUser()
        throws Exception
    {
        OSUserInfoEntity mOSUserInfo = new OSUserInfoEntity();
        ReturnValue2Page rv = new ReturnValue2Page(false, ReturnValue2PageType.NORMAL);
        //删除任务
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String mOSUser = request.getParameter("OSUser");
        try
        {
            if (null != mOSUser)
            {
                //任务表中是否有OS用户的任务
                TaskSearchEntity taskSearch = new TaskSearchEntity();
                taskSearch.setOsUser(mOSUser);
                taskSearch.setTaskNames(null);
                taskSearch.setTaskIds(null);
                Integer size = tccDao.getTaskListSizeByNames(taskSearch);
                if (size > 0)
                {
                    rv.setSuccess(false);
                    rv.setReturnValue2PageType(ReturnValue2PageType.FOREIGNKEY_CONSTRAINT);
                }
                else
                {
                    // 检查是否有用户的os用户设置为要删除的os用户
                    mOSUserInfo.setOsUser(mOSUser);
                    //删除OS用户
                    getOperatorMgnt().deleteOSUser(mOSUserInfo);
                    
                    //通知改变
                    Eventor.fireEvent(this, EventType.DELETE_OS_USER, mOSUserInfo.getOsUser());
                    
                    //记录日志
                    LOGGER.info("delete OSUser[{}] success.", TccUtil.truncatEncode(mOSUserInfo));
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
        }
        catch (Exception e)
        {
            LOGGER.info("deleteOSUser error! osUser is {}.", TccUtil.truncatEncode(mOSUser), e);
        }
        
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(rv,
            SerializerFeature.UseISO8601DateFormat,
            SerializerFeature.WriteNullStringAsEmpty,
            SerializerFeature.WriteNullNumberAsZero,
            SerializerFeature.WriteMapNullValue).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 保存OS用户信息
     * @return 操作成功标志符
     */
    public synchronized String saveOSUser()
    {
        OSUserInfoEntity mOSUser = new OSUserInfoEntity();
        Boolean userReqAdd = false;
        Boolean isContinue = true;
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        //JSONObject纯对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("success", false);
        try
        {
            request.setCharacterEncoding("UTF-8");
            mOSUser.setOsUser(request.getParameter("OSUser"));
            mOSUser.setPemKey(StringUtils.isEmpty(request.getParameter("pemKey")) ? null
                : request.getParameter("pemKey"));
            mOSUser.setUserGroup(request.getParameter("userGroup"));
            mOSUser.setDesc(request.getParameter("desc"));
            if (!StringUtils.isEmpty(mOSUser.getPemKey()))
            {
                String pemKey = CryptUtil.encryptToAESStr(mOSUser.getPemKey(), "RJz$asd9*zmkjsTH");
                //加密
                mOSUser.setPemKey(pemKey);
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
                    //新增记录
                    getOperatorMgnt().addOSUser(mOSUser);
                    
                    //通知改变
                    Eventor.fireEvent(this, EventType.ADD_OS_USER, mOSUser.getOsUser());
                    
                    //记录操作日志
                    LOGGER.info("add OSUser[{}] successfully!", new Object[] {TccUtil.truncatEncode(mOSUser)});
                    jsonObject.put("success", true);
                    
                    //记录审计信息
                    OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                    operateAuditInfo.setOpType(OperType.OSUSER_ADD);
                    operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                    operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                    operateAuditInfo.setOperParameters(mOSUser.toString());
                    getOperationRecord().writeOperLog(operateAuditInfo);
                }
                else
                {
                    //更新
                    OSUserInfoEntity oldOsUser = getOperatorMgnt().getOSUser(mOSUser.getOsUser());
                    
                    //更新记录
                    getOperatorMgnt().updateOSUser(mOSUser);
                    
                    //通知改变
                    Eventor.fireEvent(this, EventType.UPDATE_OS_USER, mOSUser.getOsUser());
                    
                    //修改了业务
                    if (!oldOsUser.getUserGroup().equals(mOSUser.getUserGroup()))
                    {
                        //修改任务表中的用户组关联的业务Id
                        if (null != mOSUser.getUserGroup())
                        {
                            //获取os用户关联的组以及业务Id
                            List<String> osUsers = new ArrayList<String>();
                            osUsers.add(mOSUser.getOsUser());
                            List<OSUserGroupServiceEntity> osUGSs =
                                getOperatorMgnt().getOSUserGroupServicesByName(osUsers);
                            OSUserGroupServiceEntity osUGS = osUGSs.get(0);
                            
                            //更新相关信息
                            getOperatorMgnt().updateTaskGroupServiceId(osUGS);
                        }
                    }
                    
                    //记录日志
                    LOGGER.info("update OSUser[{}] successfully!", new Object[] {TccUtil.truncatEncode(mOSUser)});
                    jsonObject.put("success", true);
                    
                    //记录审计信息
                    OperateAuditInfoEntity operateAuditInfo = new OperateAuditInfoEntity();
                    operateAuditInfo.setOpType(OperType.OSUSER_MODIFY);
                    operateAuditInfo.setOperator(OperatorMgnt.getOperatorName(request.getSession()));
                    operateAuditInfo.setLoginIp(OperatorMgnt.getLoginIp(request.getSession()));
                    operateAuditInfo.setOperParameters(mOSUser.toString());
                    getOperationRecord().writeOperLog(operateAuditInfo);
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
            LOGGER.error("failed to " + opt + " the OSUser[{}].", new Object[] {TccUtil.truncatEncode(mOSUser), e});
        }
        
        try
        {
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            setInputStream(new ByteArrayInputStream(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")).getBytes("UTF-8")));
        }
        catch (IOException e1)
        {
            LOGGER.error("saveOSUser fail", e1);
        }
        return SUCCESS;
    }
    
    /**
     * 可操作的OS用户集合JSON字符串
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String getOptOSUsers()
        throws Exception
    {
        List<OSUserGroupServiceEntity> osUGSE = new ArrayList<OSUserGroupServiceEntity>();
        try
        {
            Operator operator = getOperator();
            if (null != operator)
            {
                osUGSE = getOperatorMgnt().getOSUserGroupServicesByName(operator.getOptOsUsers());
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOptOSUsers error!", e);
        }
        
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(osUGSE).getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 可操作的OS用户集合JSON字符串
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String getVisibleOsUsers()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        List<KeyValuePair> keyValues = new ArrayList<KeyValuePair>();
        try
        {
            boolean containAllCol = false;
            containAllCol = Boolean.parseBoolean(request.getParameter("containAllCol"));
            if (containAllCol)
            {
                keyValues.add(new KeyValuePair("-1", "全部"));
            }
            
            Operator operator = getOperator();
            if (null != operator)
            {
                List<String> osUsers = getOperatorMgnt().getVisibleOsUsers(operator.getVisibleGroups());
                //构造os用户的key，value对
                for (String osUser : osUsers)
                {
                    keyValues.add(new KeyValuePair(osUser, osUser));
                }
            }
        }
        catch (Exception e)
        {
            LOGGER.error("getOptOSUsers error!", e);
        }
        
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(keyValues).getBytes("UTF-8")));
        return SUCCESS;
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
        try
        {
            request.setCharacterEncoding("UTF-8");
            List<OSUserInfoEntity> users = null;
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
            Integer count = getOperatorMgnt().getOSUsersCountByName(search);
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
            keyValuePair = new KeyValuePair(mOSUserInfo.getOsUser(), mOSUserInfo.getOsUser());
            keyValuePairList.add(keyValuePair);
        }
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(keyValuePairList).getBytes("UTF-8")));
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
