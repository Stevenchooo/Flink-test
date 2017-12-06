<%@page import="java.util.HashMap"%>
<%@page import="com.huawei.cct.websecurity.verifycode.impl.VerifyCode"%>
<%@page import="org.slf4j.Logger"%>
<%@page import="org.slf4j.LoggerFactory"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%
	final Logger LOGGER = LoggerFactory.getLogger("generateVerifyCode");
	 try
        {
            VerifyCode verifyCode = VerifyCode.getInstance();
            verifyCode.create(0,
                60,
                "Arial,Courier,Courier New",
                true,
                18,
                15,
                true,
                5,
                5,
                true,
                false,
                true,
                true,
                false,
                true,
                true,
                true,
                true,
                request.getSession(),
                response,
                out,
                pageContext);
        }
        catch (Exception e)
        {
            LOGGER.error("generateVerifyCode failed!", e);
        }
%>