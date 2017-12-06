<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
</head>
<body>

	<form id="redoTestAction" action="redoTestAction" method="post">

		任务Id：<input name="taskId" id="taskId" type="text" size="40" />eg:1000
		</br> 周期Id：<input name="cycleId" id="cycleId" type="text" size="40" />eg:20111011-10
		</br> <input name="bRedo" type="checkbox">重做周期任务依赖树/测试重做周期任务</input> </br> </br> <input
			name="testCycleTask" type="submit" value="提交" /> </br> </br>
			<table width="550" >
			<tr valign="top"><td>
			 正在运行的任务数：
		<s:property value="runningNum" />
		<s:iterator value="#request.runningTasks" status="rowstatus" id="it">
			<table width="200" border="1">
				<tr>
					<td><s:property value="#it.taskId" />
					</td>
					<td><s:property value="#it.cycleId" />
					</td>
				</tr>
			</table>
		</s:iterator>
		</td>
		<td>
		等待运行的任务数：
		<s:property value="waittingNum" />
		<s:iterator value="#request.waittingTasks" status="rowstatus" id="it">
			<table width="200" border="1">
				<tr>
					<td><s:property value="#it.taskId" />
					</td>
					<td><s:property value="#it.cycleId" /></td>
			</table>
		</s:iterator>
		</td></tr></table>
		</br> </br>
		<font color="red"><s:property value="message" /></font>

	</form>
</body>
</html>