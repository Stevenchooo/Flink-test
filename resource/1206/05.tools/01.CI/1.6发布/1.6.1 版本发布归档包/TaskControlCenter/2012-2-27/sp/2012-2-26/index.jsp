<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" type="text/css" href="css/common.css" />
<script type="text/javascript" src="js/jquery-1.7.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script language="javascript" type="text/javascript"
	src="DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="js/menu.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="stylesheet" type="text/css" href="css/table.css" />
<link rel="stylesheet" type="text/css" href="themes/icon.css" />
<link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
<title>测试用页面</title>
<script>
  function singleRangeRedo(bSingle)
  {
	 $("#singleFlag").val(bSingle);
  	 $("#redoTestAction").submit();
  }
</script>
</head>
<body>

	<form id="redoTestAction" action="redoTestAction" method="post">
	   <br/>
	   <br/>
	   <br/>
		<table width="820" >
			<tr>
				<td>任务Id：<input name="taskId" id="taskId" type="text" size="40" />eg:1000
				</td>
				<td>任务Id：<input name="rangeTaskId" id="rangeTaskId" type="text" size="40" />eg:1000
				</td>
			</tr>
			<tr>
			
				<td>周期Id：<input name="cycleId" id="cycleId" type="text" size="40" />eg:20111011-10</td>
				<td>起始日期：
				<input id="startTime" name="startTime" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" value="2012-01-01 00:00:00" class="Wdate"/>
				</td>
			</tr>
			<tr>
			
				<td></td>
				<td>结束日期：<input id="endTime" name="endTime" type="text" onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" value="2012-03-01 00:00:00" class="Wdate"/>
				</td>
			</tr>

			<tr>
				<td>
				    <input name="redo" type="checkbox" value="true">重做周期任务依赖树/测试重做周期任务</input>
				</td>
				<td>
				    <input name="rangeRedo" type="checkbox" value="true">重做周期任务依赖树/测试重做周期任务</input>
				</td>
			</tr>
			<tr>
				<td>
					<input name="singleRedo" type="button" onclick="singleRangeRedo(true)" value="提交" />
				</td>
				<td>
					<input name="rangeRedo" type="button" onclick="singleRangeRedo(false)" value="提交" />
				</td>
			</tr>
		</table>
		<input id ="singleFlag" name="single" type="hidden"/>
<br/>
<br/>
<br/>
		<table width="850">
			<tr valign="top">
				<td>正在运行的任务数： <s:property value="runningNum" />
					<table width="200" border="1">
						<s:iterator value="#request.runningTasks" status="rowstatus"
							id="it">

							<tr>
								<td><s:property value="#it.taskId" /></td>
								<td><s:property value="#it.cycleId" /></td>
							</tr>

						</s:iterator>
					</table></td>
				<td>等待运行的任务数： <s:property value="waittingNum" />
					<table width="200" border="1">
						<s:iterator value="#request.waittingTasks" status="rowstatus" id="it">
							<tr>
								<td><s:property value="#it.taskId" /></td>
								<td><s:property value="#it.cycleId" />
								</td>
								</tr>
						</s:iterator>
					</table></td>
					<td>重做任务周期列表（或者任务树根节点列表）： <s:property value="taskRsListNum" />
					<table width="300" border="1">
						<s:iterator value="#request.taskRsList" status="rowstatus" id="it">
							<tr>
								<td><s:property value="#it.taskId" /></td>
								<td><s:property value="#it.cycleId" />
								</td>
								</tr>
						</s:iterator>
					</table></td>
			</tr>
		</table>
		</br> </br> <font color="red"><s:property value="message" />
		</font>

	</form>
</body>
</html>