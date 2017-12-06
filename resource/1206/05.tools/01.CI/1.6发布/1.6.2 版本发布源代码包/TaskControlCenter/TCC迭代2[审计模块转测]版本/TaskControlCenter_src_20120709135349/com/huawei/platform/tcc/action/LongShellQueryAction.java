/*
 * 文 件 名:  LongShellQueryAction.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190929
 * 创建时间:  2012-2-17
 */
package com.huawei.platform.tcc.action;

import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.huawei.platform.common.CException;
import com.huawei.platform.tcc.constants.ResultCodeConstants;
import com.huawei.platform.tcc.domain.LongTimeShellParam;
import com.huawei.platform.tcc.domain.ServiceTaskGroup;
import com.huawei.platform.tcc.entity.LongTimeShellEntity;
import com.huawei.platform.tcc.privilegeControl.Operator;
import com.huawei.platform.tcc.utils.TccUtil;
import com.opensymphony.xwork2.ActionContext;

/**
 * TCC任务表处理逻辑
 * 
 * @author  w00190929
 * @version [Internet Business Service Platform SP V100R100, 2012-2-17]
 */
public class LongShellQueryAction extends BaseAction
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -6146096447526538841L;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(TccTaskAction.class);

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
     * 查询长执行时间的脚本
     * @return 长执行时间的脚本列表
     * @throws CException 统一封装的异常
     */
    public String queryLongShells()
        throws CException
    {
        HttpServletResponse response =
            (HttpServletResponse)ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        try
        {
            LongTimeShellParam param = new LongTimeShellParam();
            param.setStartIndex((page - 1) * (long)rows);
            param.setRows(rows);
            
            HttpServletRequest request =
                (HttpServletRequest)ActionContext.getContext().get(ServletActionContext.HTTP_REQUEST);
            
            param.setThreshold(Integer.parseInt(request.getParameter("threshold")));
            String startTimeStr = request.getParameter("startTime");
            String endTimeStr = request.getParameter("endTime");
            if (!StringUtils.isBlank(startTimeStr))
            {
                Date startTime = toDate(startTimeStr);
                param.setStartTime(getStartCycleId(startTime));
            }
            
            if (!StringUtils.isBlank(endTimeStr))
            {
                Date endTime = toDate(endTimeStr);
                param.setEndTime(TccUtil.covDate2CycleID(endTime));
            }
            
            //数据过滤
            Operator operator = getOperator();
            List<ServiceTaskGroup> visibleSTGs;
            if (null != operator)
            {
                visibleSTGs = operator.getVisibleServiceTaskGroup();
                List<String> vSTgs = new ArrayList<String>();
                if (null != visibleSTGs)
                {
                    for (ServiceTaskGroup sTG : visibleSTGs)
                    {
                        vSTgs.add(String.format("%d,%s", sTG.getServiceId(), sTG.getTaskGroup()));
                    }
                    param.setVisibleSTgs(vSTgs);
                }
                else
                {
                    param.setVisibleSTgs(null);
                }
            }
            else
            {
                param.setVisibleSTgs(new ArrayList<String>(0));
            }
            
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            //JSONObject纯对象
            JSONObject jsonObject = new JSONObject();
            
            param.setCurrentTime(new Date());
            //返回空数据
            if (null != param.getVisibleSTgs() && param.getVisibleSTgs().isEmpty())
            {
                jsonObject.put("total", 0);
                jsonObject.put("rows", new ArrayList<LongTimeShellEntity>(0));
            }
            else
            {
                Integer count = getTccPortalService().getLongTimeShellsCount(param);
                //否则以名字模糊查询
                List<LongTimeShellEntity> ltsList = getTccPortalService().getLongTimeShellList(param);
                
                jsonObject.put("total", count);
                jsonObject.put("rows", ltsList);
            }
            
            //json的双引号标记有问题，智能使用单引号，但是easy ui又不认单引号，所以需要替换
            out.print(replace2Quotes(JSONObject.toJSONString(jsonObject,
                SerializerFeature.UseISO8601DateFormat,
                SerializerFeature.UseSingleQuotes).replace("\"", "\\\"")));
            return null;
        }
        catch (Exception e)
        {
            LOGGER.error("queryLongShells fail", e);
            throw new CException(ResultCodeConstants.SYSTEM_ERROR, e);
        }
    }
    
    private String getStartCycleId(Date startTime)
    {
        Date ownStartTime = startTime;
        if (null == ownStartTime)
        {
            return null;
        }
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(ownStartTime);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        if (0 != minute || 0 != second)
        {
            cal.add(Calendar.HOUR_OF_DAY, 1);
            ownStartTime = cal.getTime();
        }
        
        return TccUtil.covDate2CycleID(ownStartTime);
    }
    
    private Date toDate(String datetime)
        throws ParseException
    {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setLenient(false);
        return df.parse(datetime);
    }
}
