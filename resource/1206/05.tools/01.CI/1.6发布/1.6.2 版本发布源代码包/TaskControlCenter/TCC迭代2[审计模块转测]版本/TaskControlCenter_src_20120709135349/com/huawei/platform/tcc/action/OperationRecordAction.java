/*
 * 文 件 名:  OperationRecordAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2012,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  l00194471
 * 创建时间:  2012-07-04
 */
package com.huawei.platform.tcc.action;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.tcc.domain.OperationRecordSearch;
import com.huawei.platform.tcc.entity.OperateAuditInfoEntity;
import com.opensymphony.xwork2.ActionContext;

/**
 * 审计
 * 
 * @author  l00194471
 * @version [Internet Business Service Platform SP V100R100, 2012-07-04]
 */
public class OperationRecordAction extends BaseAction
{
    /**
     * 序列号
     */
    private static final long serialVersionUID = 1318129454607971013L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(OperationRecordAction.class);
    
    /**
     * 每页显示的条数
     */
    private int rows;
    
    /**
     * 当前页
     */
    private int page;
    
    /**
     * 由查询条件获取审计记录
     * @return 查询成功标志位
     * @throws Exception 异常
     * @see [类、类#方法、类#成员]
     */
    public String reqOperationRecords()
        throws Exception
    {
        HttpServletRequest request =
            (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        try
        {
            request.setCharacterEncoding("UTF-8");
            Integer count = 0;
            String serviceId = request.getParameter("serviceId");
            String taskId = request.getParameter("taskId");
            String operType = request.getParameter("operType");
            String operator = request.getParameter("operator");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            //以名字模糊查询
            OperationRecordSearch search = new OperationRecordSearch();
            search.setOperatorName(StringUtils.isEmpty(operator) ? null : operator);
            search.setOperType(StringUtils.isEmpty(operType) ? null : Integer.valueOf(operType));
            search.setServiceId(StringUtils.isEmpty(serviceId) ? null : serviceId);
            search.setTaskId(StringUtils.isEmpty(taskId) ? null : taskId);
            search.setStartTime(StringUtils.isEmpty(startTime) ? null : df.parse(startTime));
            search.setEndTime(StringUtils.isEmpty(endTime) ? null : df.parse(endTime));
            search.setPageIndex((page - 1) * rows);
            search.setPageSize(rows);
            List<OperateAuditInfoEntity> operationRecords = getOperationRecord().readRecord(search);
            count = getOperationRecord().getRecordCount(search);
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("total", count);
            jsonObject.put("rows", operationRecords);
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
