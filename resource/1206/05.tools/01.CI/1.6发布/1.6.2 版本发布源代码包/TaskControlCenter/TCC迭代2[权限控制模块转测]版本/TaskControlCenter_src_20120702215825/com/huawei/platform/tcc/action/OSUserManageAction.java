/*
 * 文 件 名:  UserManageAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471
 * 创建时间:  2012-06-19
 */
package com.huawei.platform.tcc.action;

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
import com.huawei.platform.tcc.domain.KeyValuePair;
import com.huawei.platform.tcc.domain.Search;
import com.huawei.platform.tcc.entity.OSUserInfoEntity;
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
    //    private Boolean OSUserReqAdd;
    //    
    //    public Boolean getOSUserReqAdd()
    //    {
    //        return OSUserReqAdd;
    //    }
    //    
    //    public void setOSUserReqAdd(Boolean OSUserReqAdd)
    //    {
    //        this.OSUserReqAdd = OSUserReqAdd;
    //    }
    
    /**
     * 删除OS用户信息
     * @return 操作成功的标志位
     * @throws Exception 异常
     * @see [类、类#方法、类#成员]
     */
    public String deleteOSUser() throws Exception
    {
        OSUserInfoEntity mOSUserInfo = new OSUserInfoEntity();
        try
        {
            //删除任务
            HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
                    .get(ServletActionContext.HTTP_REQUEST);
            HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
                    .get(ServletActionContext.HTTP_RESPONSE);
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("success", false);
            String mOSUser = request.getParameter("OSUser");
            if (null != mOSUser)
            {
                OperatorInfoEntity operatorInfo = new OperatorInfoEntity();
                operatorInfo.setOsUserName(mOSUser);
                if (0 != getOperatorMgnt().getUsersCount(operatorInfo))
                {
                    LOGGER.info("delete OSUser[{}] fail.", mOSUserInfo);
                    jsonObject.put("success", false);
                    jsonObject.put("error", "OSUserHasBeenSet");
                }
                else
                {
                    mOSUserInfo.setUserName(mOSUser);                    
                    //删除业务
                    getOperatorMgnt().deleteOSUser(mOSUserInfo);
                    //记录日志
                    LOGGER.info("delete OSUser[{}] success.", mOSUserInfo);
                    //记录审计信息
                    jsonObject.put("success", true);
                }
            }
            
            PrintWriter out = response.getWriter();
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            out.print(replace2Quotes(JSONObject.toJSONString(jsonObject,
                    SerializerFeature.UseISO8601DateFormat,
                    SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")));
            return null;
        }
        catch (Exception e)
        {
            LOGGER.error("failed to delete OSUser[{}].", mOSUserInfo, e);
            throw e;
        }
    }
    
    /**
     * 保存OS用户信息
     * @return 操作成功标志符
     * @see [类、类#方法、类#成员]
     */
    public synchronized String saveOSUser()
    {
        OSUserInfoEntity mOSUserInfoEntity = new OSUserInfoEntity();
        Boolean userReqAdd = false;
        Boolean isContinue = true;
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
                .get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
                .get(ServletActionContext.HTTP_RESPONSE);
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
                    && !mOSUserInfoEntity.getPwd()
                            .equals(request.getParameter("pwdConfirm")))
            {
                isContinue = false;
                jsonObject.put("error", "diffrentPwd");
            }
            userReqAdd = Boolean.parseBoolean(request.getParameter("OSUserReqAdd"));
        }
        catch (Exception e)
        {
            isContinue = false;
            LOGGER.error("saveOSUser error", e);
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
                        LOGGER.info("add OSUser[{}] successfully!",
                                new Object[] { mOSUserInfoEntity });
                        //记录审计信息
                        jsonObject.put("success", true);
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
                        LOGGER.info("update OSUser[{}] successfully!",
                                new Object[] { mOSUserInfoEntity });
                        //记录审计信息
                        jsonObject.put("success", true);
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
            LOGGER.error("failed to " + opt + " the OSUser[{}].", new Object[] {
                    mOSUserInfoEntity, e });
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
     * @see [类、类#方法、类#成员]
     */
    public String reqOSUsers() throws Exception
    {
        HttpServletRequest request = (HttpServletRequest) ActionContext.getContext()
                .get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
                .get(ServletActionContext.HTTP_RESPONSE);
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
                    userList.add(URLDecoder.decode(userNameArr[0], "utf-8")
                            + ";");
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
    public String reqOSUserNames() throws Exception
    {
        
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
                .get(ServletActionContext.HTTP_RESPONSE);
        //必须加上,防止前端从JSON中取出的数据成乱码
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        Search search = new Search();
        List<OSUserInfoEntity> mOSUserInfoList = getOperatorMgnt().getOSUsersByName(search);
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        KeyValuePair keyValuePair;
        for (OSUserInfoEntity mOSUserInfo : mOSUserInfoList)
        {
            keyValuePair = new KeyValuePair(mOSUserInfo.getUserName(),
                    mOSUserInfo.getUserName());
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
