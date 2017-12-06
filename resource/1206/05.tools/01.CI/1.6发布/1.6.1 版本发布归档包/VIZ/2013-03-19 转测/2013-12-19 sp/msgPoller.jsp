<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="com.huawei.bi.task.bean.logger.ExeLoggerBuilder"%>
<%@page import="com.huawei.bi.task.bean.logger.IExeLogger"%>
<%@page import="com.huawei.bi.task.domain.Execution"%>
<%@page import="com.huawei.bi.task.bean.TaskBean"%>
<%@page import="com.huawei.bi.task.bean.logger.TailedFileLogger"%>
<%@page import="java.util.List"%>
<%@page import="org.json.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
    String exeId = request.getParameter("exeId");
    String target = request.getParameter("target");
    boolean fromFile = "true".equals(request.getParameter("fromFile"));
    Map<String, String> map = new HashMap<String, String>();
    JSONObject result = new JSONObject();
    JSONArray array = new JSONArray();
    TailedFileLogger exeLogger = null;
    try
    {
        TaskBean bean = new TaskBean();
        exeLogger = (TailedFileLogger)ExeLoggerBuilder.buildExeLogger(Execution.EXE_TYPE_MERGE, exeId);
        
        List<String> list = null;
        if ("log".equals(target))
        {
            list = exeLogger.readDebug(fromFile);
        }
        else if ("result".equals(target))
        {
            list = exeLogger.readResult(fromFile);
        }
        else
        {
            // TODO
        }
        
        StringBuffer sb = new StringBuffer();
        int previousIndex = -1;
        for (int i = 0; i < list.size(); i++)
        {
            String line = list.get(i);
            if ("result".equals(target))
            {
                JSONArray ja = new JSONArray();
                String[] values = line.split("\t");
                for (String value : values)
                {
                    ja.put(value);
                }
                //当查询出来的数据上一次和本一次不符合时，把差额的数据用NULL补全
                if (previousIndex != -1 && values.length < previousIndex)
                {
                    int differentValue = previousIndex - values.length;
                    for (int j = 0; j < differentValue; j++)
                    {
                        ja.put("NULL");
                    }
                }
                previousIndex = values.length;
                array.put(ja);
            }
            else
            {
                sb.append(line);
                sb.append("<br/>");
            }
        }
        if ("result".equals(target))
        {
            map.put("result", array.toString());
        }
        response.setContentType("application/json");
        response.setHeader("Cache-Control", "no-store");
        map.put("info", sb.toString());
        Execution execution = bean.getExecution(Execution.EXE_TYPE_MERGE, exeId);
        map.put("status", execution.getStatus());
    }
    catch (Exception e)
    {
        map.put("error", e.getMessage());
    }
    finally
    {
        if (null != exeLogger)
        {
            exeLogger.close();
        }
    }
    
    response.getWriter().write(new JSONObject(map).toString());
%>