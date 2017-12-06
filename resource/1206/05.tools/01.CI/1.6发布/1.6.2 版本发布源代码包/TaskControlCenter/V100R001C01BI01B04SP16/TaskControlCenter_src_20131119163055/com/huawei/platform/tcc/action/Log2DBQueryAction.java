/*
 * 文 件 名:  Log2DBQueryAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190929
 * 创建时间:  2012-2-17
 */
package com.huawei.platform.tcc.action;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.huawei.platform.tcc.domain.KeyValuePair;
import com.huawei.platform.tcc.domain.Log2DBQueryParam;
import com.huawei.platform.tcc.entity.Log2DBEntity;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * TCC任务表处理逻辑
 * 
 * @author  w00190929
 * @version [Device Cloud Base Platform Dept TaskControlCenter V100R100, 2012-2-17]
 * @see  [相关类/方法]
 */
public class Log2DBQueryAction extends BaseAction
{
    
    /**
     * 序列号
     */
    private static final long serialVersionUID = -5926666475101361928L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Log2DBQueryAction.class);
    
    /**
     * 每页显示的条数
     */
    private int rows;
    
    /**
     * 当前页
     */
    private int page;
    
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
    
    /**
     * 任务Id列表数据json格式
     * 
     * @return 任务Id列表数据json格式
     * @throws Exception 数据库操作异常
     */
    public String reqUserNameJsonObject()
        throws Exception
    {
        List<String> userNameList = getTccPortalService().getAllUserName();
        List<KeyValuePair> keyValuePairList = new ArrayList<KeyValuePair>();
        
        KeyValuePair keyValuePair;
        
        keyValuePairList.add(new KeyValuePair(Long.toString(0), "全部"));
        
        for (String userName : userNameList)
        {
            keyValuePair = new KeyValuePair(userName, userName);
            keyValuePairList.add(keyValuePair);
        }
        
        setInputStream(new ByteArrayInputStream(JSONObject.toJSONString(keyValuePairList).getBytes("UTF-8")));
        ;
        return SUCCESS;
    }
    
    /**
     * 根据条件查询日志总记录数
     * @return 操作成功标志符
     * @throws Exception 异常
     */
    public String queryLogsCount()
        throws Exception
    {
        String returnValue = "true";
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Log2DBQueryParam logQuery = new Log2DBQueryParam();
        
        logQuery.setUserName(request.getParameter("userName"));
        logQuery.setLevel(request.getParameter("level"));
        logQuery.setStartTime(request.getParameter("startTime"));
        logQuery.setEndTime(request.getParameter("endTime"));
        
        if ("0".endsWith(logQuery.getUserName()))
        {
            logQuery.setUserName(null);
        }
        
        if ("ALL".endsWith(logQuery.getLevel()))
        {
            logQuery.setLevel(null);
        }
        
        logQuery.setStartTime(request.getParameter("startTime") + ".000");
        logQuery.setEndTime(request.getParameter("endTime") + ".000");
        
        try
        {
            Integer count = getTccPortalService().getTccLogCount(logQuery);
            returnValue = "true|" + count;
        }
        catch (Exception e)
        {
            returnValue = "false";
            LOGGER.error("queryLogs error!", e);
        }
        
        setInputStream(new ByteArrayInputStream(returnValue.getBytes("UTF-8")));
        return SUCCESS;
    }
    
    /**
     * 异步获取最大权重值，是否有相应的历史运行状态记录以及任务的状态（正常或停止）
     * @return 操作成功标志符
     * @throws Exception 异常
     */
    public String queryLogs()
        throws Exception
    {
        String returnValue = "true";
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        Log2DBQueryParam logQuery = new Log2DBQueryParam();
        
        logQuery.setUserName(request.getParameter("userName"));
        logQuery.setLevel(request.getParameter("level"));
        logQuery.setStartTime(request.getParameter("startTime"));
        logQuery.setEndTime(request.getParameter("endTime"));
        try
        {
            logQuery.setPage(Integer.parseInt(request.getParameter("pageNumber")));
            logQuery.setRows(Integer.parseInt(request.getParameter("pageSize")));
        }
        catch (Exception e)
        {
            LOGGER.error("queryLogs error! param is [pageNumber={},pageSize={}].",
                new Object[] {TccUtil.truncatEncode(request.getParameter("pageNumber")),
                    TccUtil.truncatEncode(request.getParameter("pageSize")), e});
            returnValue = "false";
        }
        
        try
        {
            if ("0".endsWith(logQuery.getUserName()))
            {
                logQuery.setUserName(null);
            }
            
            if ("ALL".endsWith(logQuery.getLevel()))
            {
                logQuery.setLevel(null);
            }
            
            logQuery.setStartTime(request.getParameter("startTime") + ".000");
            logQuery.setEndTime(request.getParameter("endTime") + ".000");
            logQuery.setStartIndex((long)(logQuery.getPage() - 1) * logQuery.getRows());
            if ("true".endsWith(returnValue))
            {
                List<Log2DBEntity> logList = getTccPortalService().getTccLogList(logQuery);
                returnValue = "true|" + TccUtil.format(logList);
            }
            
        }
        catch (Exception e)
        {
            returnValue = "false";
            LOGGER.error("queryLogs error!", e);
        }
        
        setInputStream(new ByteArrayInputStream(returnValue.getBytes("UTF-8")));
        return SUCCESS;
    }
}
