<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.huawei.platform.tcc.constants.TccConfig"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="新建任务页面" /></title>
<script type="text/javascript" src="js/jquery-1.7.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script language="javascript" type="text/javascript"
	src="DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="stylesheet" type="text/css" href="css/table.css" />
<link rel="stylesheet" type="text/css" href="themes/icon.css" />
<link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
</head>

<body>
	<%
	    String taskId = request.getParameter("taskId");
				if (null == taskId) {
					taskId = "0";
				}
		
		boolean taskIdCenter = TccConfig.isTaskIdCenter();
	%>
	<div
		style="margin: 5px 0px 5px 0px; margin-left: auto; margin-right: auto; background: #fff; border: 1px solid #ccc; width: 1100px;">
		<table width="800" style="margin-left: auto; margin-right: auto;">
			<tr>
				<td><span>业务类型: </span></td>
				<td><input class="easyui-combobox" id="vserviceIdList"
					style="width: 140px" panelHeight="200px" editable="false" /></td>
				<td><span>任务名称: </span></td>
				<td><input class="easyui-combobox" id="taskIdList"
					style="width: 210px" panelHeight="200px" /> <input id="taskIdOut"
					type="hidden" value="<%=taskId%>" />
				</td>
				<td>&nbsp;&nbsp;<span>任务状态: </span></td>
				<td><select id="searchTaskState1" editable="false"
					class="easyui-combobox" panelHeight="auto"
					name="taskSearch.taskState" style="width: 140px" required="true">
						<option value="2">全部</option>
						<option value="0">正常</option>
						<option value="1">停止</option>
				</select>
				</td>
				<td rowspan="2" align="center" valign="center"><a
					class="easyui-linkbutton" plain="false" iconCls="icon-search"
					href="javascript:void(0)" onclick="loadTasks()">查询 </a>
				</td>
			</tr>
			<tr>
				<td><span>OS用户: </span></td>
				<td><input class="easyui-combobox" id="vOsUsers"
					editable="false" style="width: 140px" panelHeight="200px" /></td>
				<td><span>任务类型:</span></td>
				<td><select id="searchTaskType1" editable="false"
					class="easyui-combobox" panelHeight="auto" style="width: 220px"
					required="true">
						<option value="0">全部</option>
						<option value="1">数据文件导入ODS</option>
						<option value="2">ODS转换到DW</option>
						<option value="3">ODS转换到DIM</option>
						<option value="4">DW内部转换</option>
						<option value="5">DW转换到DM</option>
						<option value="6">DW统计输出报表</option>
						<option value="7">报表导出文件</option>
						<option value="8">DW导出文件</option>
						<option value="9">混合型</option>
				</select>
				</td>
				<td>&nbsp;&nbsp;<span>周期类型: </span></td>
				<td><select id="searchCycleType1" editable="false"
					class="easyui-combobox" panelHeight="auto" style="width: 140px"
					required="true">
						<option value="0">全部</option>
						<option value="H">按小时</option>
						<option value="I">按分钟</option>
						<option value="D">按天</option>
						<option value="M">按月</option>
						<option value="Y">按年</option>
				</select></td>
			</tr>
		</table>
	</div>
	<table id="taskList"></table>
	<div id="popOptTask" class="easyui-window" inline="false" closed="true"
		style="width: 650px; height: 430px; padding: 10px">
		<div id="hideOptTask" class="easyui-layout" fit="true"
			style="display: none;">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<form id="tccTaskForm" name="tccTaskForm" method="post">
					<table id="OptTaskTable" cellpadding="10px" cellspacing="5px"
						style="margin-left: auto; margin-right: auto;">
						<tr>
							<td width="180"><span>业务类型: </span></td>
							<td><input class="easyui-combobox" id="serviceId"
								editable="false" style="width: 150px" panelHeight="200px"
								name="task.serviceId" /> <font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>任务名称: </span></td>
							<td><input id="taskName" class="easyui-validatebox"
								validType="nameValidType" maxlength="128" type="text"
								style="width: 350px" name="task.taskName" /><font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>任务描述: </span></td>
							<td><textarea id="taskDesc" name="task.taskDesc"
									maxlength="1024" rows="3" style="width: 350px"></textarea><font
								color="red">*</font></td>
						</tr>
						<tr>
							<td><span>任务类型: </span></td>
							<td><select id="taskType" editable="false"
								class="easyui-combobox" name="task.taskType"
								style="width: 150px" required="true">
									<option value="1" selected="selected">数据文件导入ODS</option>
									<option value="2">ODS转换到DW</option>
									<option value="3">ODS转换到DIM</option>
									<option value="4">DW内部转换</option>
									<option value="5">DW转换到DM</option>
									<option value="6">DW统计输出报表</option>
									<option value="7">报表导出文件</option>
									<option value="8">DW导出文件</option>
									<option value="9">混合型</option>
							</select><font color="red">*</font></td>
						</tr>
						<tr id="priorityRow">
							<td><span>任务优先级: </span></td>
							<td><select id="priority" editable="false"
								class="easyui-combobox" name="task.priority"
								style="width: 150px" required="true">
									<option value="1">1</option>
									<option value="2">2</option>
									<option value="3">3</option>
									<option value="4">4</option>
									<option value="5" selected="selected">5</option>
									<option value="6">6</option>
									<option value="7">7</option>
									<option value="8">8</option>
									<option value="9">9</option>
							</select><font color="red">*</font>(数字越小优先级越高)</td>
						</tr>
						<tr>
							<td><span>任务执行周期类型: </span></td>
							<td><select id="cycleType" editable="false"
								class="easyui-combobox" name="task.cycleType"
								style="width: 150px" panelHeight="auto" required="true">
									<option value="H">按小时</option>
									<option value="I">按分钟</option>
									<option value="D">按天</option>
									<option value="M">按月</option>
									<option value="Y">按年</option>
							</select><font color="red">*</font>(运行状态时不能修改)</td>
						</tr>
						<tr>
							<td><span>周期长度: </span></td>
							<td><input id="cycleLength" name="task.cycleLength"
								class="easyui-numberspinner" precision="0" increment="1" min="1"
								max="100" value='1' style="width: 150px" /> <font color="red">*</font>(运行状态时不能修改)</td>
						</tr>
						<tr>
							<td><span>周期偏移: </span></td>
							<td><input id="cycleOffset" type="text"
								name="task.cycleOffset" class="easyui-validatebox"
								validType="cycleOffset" /><font color="red">*</font></td>
						</tr>
						<tr>
							<td><span>任务依赖关系: </span></td>
							<td><textarea id="dependTaskIdListDisplay" maxlength="1024"
									style="width: 350px" class="easyui-validatebox"
									validType="dependTaskIdList" rows="3"></textarea> <input
								type="hidden" id="dependTaskIdList" name="task.dependTaskIdList" />
							</td>
						</tr>
						<tr>
							<td><span>周期间是否顺序依赖: </span></td>
							<td><input id="cycleDependFlag" type="radio"
								name="task.cycleDependFlag" value="true" checked="checked" />是
								<input id="cycleDependFlag" type="radio"
								name="task.cycleDependFlag" value="false" />否</td>
						</tr>
						<tr>
							<td><span>是否有多批标志: </span></td>
							<td><input id="multiBatchFlag" type="radio"
								name="task.multiBatchFlag" onclick="enableState()" value="true" />是
								<input id="multiBatchFlag" type="radio"
								name="task.multiBatchFlag" value="false"
								onclick="disableState()" checked="checked" />否</td>
						</tr>
						<tr>
							<td><span>分批结束标志: </span></td>
							<td><select id="endBatchFlag" editable="false"
								class="easyui-combobox" name="task.endBatchFlag"
								panelHeight="auto" style="width: 200px" required="true">
									<option value="0" selected="selected">普通方式(任务执行逻辑结束)</option>
									<option value="1">指定的输入文件处理处理完成</option>
									<option value="2">等待时间内输入的文件处理完成</option>
									<option value="3">超过等待时间，且最少处理N个文件</option>
									<option value="4">超过等待时间，或者最少处理N个文件</option>
							</select><font color="red">*</font></td>
						</tr>
						<tr>
							<td><span>输入文件列表: </span></td>
							<td><textarea id="inputFileList" name="task.inputFileList"
									maxlength="2048" style="width: 350px" rows="3"></textarea></td>
						</tr>
						<tr>
							<td><span>输入文件最少个数: </span></td>
							<td><input id="inputFileMinCount"
								name="task.inputFileMinCount" class="easyui-numberspinner"
								value='0' precision="0" increment="1" min="0" max="10000"
								style="width: 150px" /></td>
						</tr>
						<tr>
							<td><span>等待输入时间（分钟）: </span></td>
							<td><input id="waitInputMinutes"
								name="task.waitInputMinutes" type="text"
								class="easyui-numberspinner" value='30' precision="0"
								increment="10" min="0" max="1000" style="width: 150px" /> <font
								color="red">*</font></td>
						</tr>
						<tr>
							<td><span>部署节点: </span></td>
							<td><input id="deployedNodeList" panelHeight="150px" style="width: 150px"  class="easyui-combobox" multiple="true" editable="false" name="task.deployedNodeList"/>
								<font color="red">多批或者非空步骤任务必选</font>
							</td>
						</tr>
						<tr>
							<td width="180"><span>执行OS用户: </span></td>
							<td><input class="easyui-combobox" id="osUsers"
								editable="false" style="width: 150px" panelHeight="200px"
								name="task.osUser" /> <font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td width="180"><span>所属用户组: </span></td>
							<td><input id="userGroup" readonly=true type="text" style="width: 150px" name="task.userGroup" /><font color="red">*</font></td>
						</tr>
						<tr>
							<td><span>任务最早起始时间: </span></td>
							<td><input id="startTime" name="task.startTime" type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								class="Wdate" value="2013-01-01 00:00:00" /> <font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>重做起始时间: </span></td>
							<td><input id="redoStartTime" name="task.redoStartTime"
								type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								class="Wdate" value="2013-01-01 00:00:00" /> <font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>重做结束时间: </span></td>
							<td><input id="redoEndTime" name="task.redoEndTime"
								type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								class="Wdate" value="2020-01-01 00:00:00" /> <font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>重做类型: </span></td>
							<td><select id="redoType" editable="false"
								class="easyui-combobox" name="task.redoType"
								style="width: 150px" panelHeight="auto" required="true">
									<option value="1" selected="selected">集成重做</option>
									<option value="2">周末重做</option>
									<option value="3">月末重做</option>
							</select>
							</td>
						</tr>
						<tr>
							<td><span>集成重做合并的天数: </span></td>
							<td><input id="redoDayLength" class="easyui-numberspinner"
								name="task.redoDayLength" increment="1" min="0" max="1000"
								precision="0" value='0' style="width: 150px" /> <font
								color="red">*</font></td>
						</tr>
						<tr id="weightRow">
							<td><span>任务周期占用的资源数: </span></td>
							<td><input id="weight" class="easyui-numberspinner"
								name="task.weight" increment="1" min="1" max="100" precision="0"
								value='1' style="width: 150px" /><font color="red">*</font></td>
						</tr>
					</table>
					<input type="hidden" id="taskId" name="task.taskId" /> <input
						id="taskNameSrc" type="hidden" /> <input type="hidden"
						id="taskReqAdd" name="taskReqAdd" /> <input type="hidden"
						id="searchTaskId" name="searchTaskId" /> <input type="hidden"
						id="searchTaskState" name="searchTaskState" /> <input
						type="hidden" id="searchTaskType" name="searchTaskType" /> <input
						type="hidden" id="searchCycleType" name="searchCycleType" /> <input
						type="hidden" id="resetTaskRS" />
				</form>
			</div>
			<div region="south" border="false"
				style="text-align: right; padding: 5px 0; height: 35px;">
				<a class="easyui-linkbutton" plain="true" iconCls="icon-ok"
					href="javascript:void(0)" onclick="saveTask()">保存 </a> <a
					class="easyui-linkbutton" plain="true" iconCls="icon-cancel"
					href="javascript:void(0)" onclick="cancel()">取消 </a>
			</div>
		</div>
	</div>

	<div id="popOptAlarmConfig" class="easyui-window" inline="false"
		closed="true" style="width: 770px; height: 425px; padding: 10px">
		<div id="hideOptAlarmConfig" class="easyui-layout" fit="true"
			style="display: none;">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<form id="alarmConfigForm" name="alarmConfigForm" method="post">
					<table id="OptserviceDeployTable" cellpadding="15px"
						cellspacing="15px" style="margin-left: auto; margin-right: auto;">
						<tr>
							<td><span>允许告警： </span></td>
							<td><input id="isAlarmPermitted" type="checkbox"
								value="true" Checked="checked" /></td>
						</tr>
						<tr>
							<td><span>告警项： </span></td>
							<td><select id="alarmType" class="easyui-combobox"
								style="width: 288px" multiple="true" editable="false">
									<option value="1" selected="selected">任务失败（严重）</option>
									<option value="3" selected="selected">任务执行时间超时（严重）</option>
									<option value="8" selected="selected">文件未到达就执行批次任务（严重）</option>
									<option value="4" selected="selected">任务到达最迟启动时间时仍未启动（严重）</option>
									<option value="5" selected="selected">任务到达最迟结束时间时仍未结束（严重）</option>
									<option value="9" selected="selected">步骤执行超时反馈成任务周期超时（严重）</option>
									<option value="2">任务发生重做 (一般)</option>
									<option value="7">Hadoop资源不足(一般)</option>
									<option value="6">忽略依赖任务（弱依赖）的错误后启动任务(一般)</option>
							</select></td>
						</tr>
						<tr>
							<td><span>告警门槛： </span>
							</td>
							<td>
								<table cellpadding="10px" cellspacing="10px">
									<tr>
										<td><span>最迟启动相对时间: </span>
										</td>
										<td><input id="latestStartTime" style="width: 155px"
											type="text" class="easyui-validatebox"
											validType="cycleOffset" />
										</td>
									</tr>
									<tr>
										<td><span>最迟结束相对时间: </span>
										</td>
										<td><input id="latestEndTime" style="width: 155px"
											type="text" class="easyui-validatebox"
											validType="cycleOffset" />
										</td>
									</tr>
									<tr>
										<td><span>最长执行时间(分钟): </span>
										</td>
										<td><input id="maxRunTime" style="width: 160px"
											value="60" name="maxRunTime" class="easyui-numberspinner"
											increment="10" min="1" max="525600" precision="0"
											style="width:120px" /></td>
									</tr>
								</table></td>
						</tr>
						<tr>
							<td><span>告警渠道：</span>
							</td>
							<td>
								<div
									class="datagrid-wrap panel-body panel-body-noheader datagrid-header">
									<table style="text-align: center; white-space: normal;"
										id="alarmChannelList">
										<tbody>
											<tr>
												<td>告警级别</td>
												<td>接受email列表(以";"号分隔)</td>
												<td>接受手机号码列表(以";"号分隔)</td>
												</del>
											</tr>
										</tbody>
										<tr>
											<td style="width: 50px">严重</td>
											<td><input id="severeEmailList" class="easyui-validatebox"
								validType="emailsValidType" maxlength="1000" style="margin: 5px 5px 5px 5px; width: 250px" type="text" />
											</td>
											<td><input id="severeMobileList" class="easyui-validatebox"
								validType="phonesValidType" maxlength="1000" style="margin: 5px 5px 5px 5px; width: 250px" type="text" />
											</td>
										</tr>
										<tr>
											<td style="width: 50px">一般</td>
											<td><input id="normalEmailList" class="easyui-validatebox"
								validType="emailsValidType" maxlength="1000" style="margin: 5px 5px 5px 5px; width: 250px" type="text" />
											</td>
											<td><input id="normalMobileList" class="easyui-validatebox"
								validType="phonesValidType" maxlength="1000" style="margin: 5px 5px 5px 5px; width: 250px" type="text" />
											</td>
										</tr>
									</table>
								</div></td>
						</tr>
					</table>
					<input id="ac_taskId" type="hidden" />
				</form>
			</div>
			<div region="south" border="false"
				style="text-align: right; padding: 5px 0; height: 40px;">
				<a class="easyui-linkbutton" plain="true" iconCls="icon-ok"
					href="javascript:void(0)" onclick="saveAlarmConfig()">保存 </a> <a
					class="easyui-linkbutton" plain="true" iconCls="icon-cancel"
					href="javascript:void(0)" onclick="cancelAlarmConfig()">取消 </a>
			</div>
		</div>
	</div>
	<div id="popDstTccServerAuth" class="easyui-window" inline="false"
		closed="true" style="width: 450px; height: 250px; padding: 10px">
		<div id="hideDstTccServerAuth" class="easyui-layout" fit="true"
			style="display: none;">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<div id="processing"  class="ajaxLoading">处理中... 请稍等.</div>
				<form id="dstTccServerAuthForm" method="post">
					<table id="DstTccServerAuthTable" cellpadding="15px"
						cellspacing="15px" style="margin-left: auto; margin-right: auto;">
						<tr>
							<td><span>目标TCC服务器: </span></td>
							<td><input class="easyui-combobox" id="tccServers"
								editable="false" style="width: 150px" panelHeight="100px"/> <font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>目标TCC用户名: </span></td>
							<td><input id="dstUserName"	style="width: 150px"/> <font color="red">*</font>
							</td>
							<td>
								<font id="dstUserNameTips" color="red">系统管理员</font>
							</td>
						</tr>
						<tr>
							<td><span>目标TCC密码: </span></td>
							<td><input id="dstPassword" style="width: 150px" type="password"/> <font color="red">*</font>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div region="south" border="false"
				style="text-align: right; padding: 5px 0; height: 40px;">
				<a id='save' class="easyui-linkbutton" plain="true" iconCls="icon-ok"
					href="javascript:void(0)" onclick="syncTcc()">提交 </a> <a
					class="easyui-linkbutton" plain="true" iconCls="icon-cancel"
					href="javascript:void(0)" onclick="cancelSyncTcc()">关闭 </a>
			</div>
		</div>
	</div>
</body>

<script type="text/javascript" charset="utf-8">
var systemAdmin = false;
var taskIdCenter=<%=taskIdCenter%>;
$(function() {
	loadDatas();
});

function loadDatas()
{
	loadAllServiceIdNames();
   	loadVisibleServiceNameList();
	loadVisibleOsUsers();
	//加载任务ID列表,成功后加载表格
	loadTaskIdList(true);
	//是否是系统管理员
	systemAdmin = isSystemAdmin();
}

//获取指定的任务Id和任务名键值对
function grabTaskIdNames(taskIds)
{
	if(!checkSessionValid())return;
	if(null == taskIds || "" == taskIds) return;
	$.ajax({
		type:"post",
		url:"grabTaskIdNames?taskIds="+taskIds,
		async : false,
		success : function(data, textStatus) {
			if (textStatus == "success")
			{
				var jsonObj = $.parseJSON(data);
				var success = jsonObj["success"];
				var returnValue2PageType = jsonObj["returnValue2PageType"];
				var values = jsonObj["values"];
				if (success == true)
				{
					if(3 == returnValue2PageType)
					{
						if(values.length >= 1)
						{
							var keyVlaues= $.parseJSON(values[0]);
							for(var index in keyVlaues)
							{
								taskIdNameMap[keyVlaues[index]['key']]=keyVlaues[index]['value'];
								taskNameIdMap[keyVlaues[index]['value']]=keyVlaues[index]['key'];
							}
						}
					}
				}
			}
		}
	});
}

//从依赖关系dependRelations中的全部任务Id
function getDependTaskIds(dependRelations)
{
	//类型转换一下
	dependRelations = '' + dependRelations;
	var splitChar = ';';
	var taskIds = "";
	if(null != dependRelations && '' != dependRelations)
	{
		var taskIdArr = dependRelations.split(splitChar);
		for(var index in taskIdArr)
		{
			if('' != taskIdArr[index])
			{
				var taskIdFullErrorArr = taskIdArr[index].split(',');
				if(taskIdFullErrorArr.length >= 3)
				{
					taskIds += taskIdFullErrorArr[0]+";";
				}
			}
		}
	}
	return taskIds;
}

var osUGSs ={};
//加载可操作的OS用户、用户组、业务Id、业务名
function loadOptOsUGSs()
{
   $('#osUsers').combobox({
			url : null,
			valueField : "key",
			textField : "value",
			title : 'OS用户',
			width : 150,
			editable : false,
			onSelect : function(record)
			{
				var osUser = record["key"];
				if(null != osUser)
				{
				   $("#serviceId").val(osUGSs[osUser]["serviceId"]);
				   //$("#serviceName").val(osUGSs[osUser]["serviceName"]);
				   $("#userGroup").val(osUGSs[osUser]["userGroup"]);
				}
			}
			});
   $.ajax({
		type:"post",
		url:"getOptOSUsers",
		async : false,
		success : function(data, textStatus) {
			if (textStatus == "success")
			{
				var datas = $.parseJSON(data);
				var osUserDatas = [];
				if(datas.length > 0)
				{
					for(var index in datas)
					{
						osUserDatas[index]={key:datas[index]["osUser"],value:datas[index]["osUser"]};
						osUGSs[datas[index]["osUser"]]=datas[index];
					}
					
					$('#osUsers').combobox('loadData', osUserDatas);
				}
			}
		}
	});
}


function loadVisibleOsUsers()
{
    $('#vOsUsers').combobox({
			url : "getVisibleOsUsers?containAllCol=true",
			valueField : "key",
			textField : "value",
			title : 'OS用户',
			width : 140,
			editable : false,
			onLoadSuccess:function()
			{
			   selectCombox("#vOsUsers",null);
			}
	});
}

//加载任务Id列表选择框
function loadVisibleServiceNameList()
{
	var url= "getVServiceIdNameList?containAllCol=true";
	$('#vserviceIdList').combobox({
		url : url,
		valueField : "key",
		textField : "value",
		title:'可见业务',
		width: 140,
		editable : false,
		onLoadSuccess:function()
		{
			//初始化业务Id名对
			//initServiceIdNameMap('vserviceIdList');
			$('#vserviceIdList').combobox("select",-1);
		}
	});
}

//加载任务Id列表选择框
function loadAllServiceIdNames()
{
	var url= "getAllServiceIdNames?containAllCol=false";
	$('#serviceId').combobox({
		url : url,
		valueField : "key",
		textField : "value",
		title:'可见业务',
		width: 140,
		editable : false,
		onLoadSuccess:function()
		{
			//初始化业务Id名对
			initServiceIdNameMap('serviceId');
			selectCombox("#serviceId",-1);
		}
	});
}

//初始化任务Id名字键值对
var serviceIdName={};
function initServiceIdNameMap(listName)
{
	var datas = $('#'+listName).combobox("getData");
	for(var index in datas)
	{
		serviceIdName[datas[index]['key']]=datas[index]['value'];
	}
}

var nodeIdMap={};
function loadDeployedNodeList()
{
   $('#deployedNodeList').combobox({
		url : "reqNodes",
		valueField : "key",
		textField : "value",
		width: 150,
		onUnselect:function(record)
		{
			var nodeIds = $('#deployedNodeList').combobox('getValues');
			if(null != nodeIds && nodeIds.length > 0)
			{
			   var allNotExists = true;
			   for(var index in nodeIds)
			   {
			      if(null != nodeIdMap[nodeIds[index]])
			      {
			         allNotExists = false;
			      }
			   }
			   
			   if(true == allNotExists)
			   {
			   	 	$('#deployedNodeList').combobox('setText','');
			   	 	$('#deployedNodeList').combobox('clear');
			   }
			}
		},
		onLoadSuccess:function()
		{
		   var nodes = $('#deployedNodeList').combobox('getData');
		   for(var index in nodes)
		   {
		      nodeIdMap[nodes[index].key] = nodes[index].value;
		   }
		}
	});
}

//加载任务Id列表选择框
function loadTaskIdList(first)
{
	if(null != first && true == first)
	{
		var taskId4Url = "<%=taskId%>";
		$("#taskIdOut").val(taskId4Url);
	}
	
	$('#taskIdList').combobox({
		url : "reqTaskIdJsonObject?containAllCol=true",
		valueField : "key",
		textField : "value",
		title:'taskId',
		width: 220,
		editable : true,
		onChange : function(record) {
			var taskId = covTaskName2Id($("#taskIdList").combobox('getValue'));
			$("#taskIdOut").val(taskId);
		},
		onLoadSuccess : function()
		{
			//外部传来的taskId
			initTaskIdNameMap('taskIdList');
			var taskIdOut = $('#taskIdOut').val();
			var showValue = '';
			if('0' != taskIdOut)
			{
				var taskIds = taskIdOut.split(';');
				for(var index in taskIds)
				{
					if(''  != taskIds[index])
					{
						showValue += covTaskId2Name(taskIds[index])+';';
					}
				}
				$('#taskIdList').combobox("setValue",showValue);
			}
			else
			{
				selectCombox("#taskIdList",null);
			}
			
			loadOptOsUGSs();
			loadDeployedNodeList();
			
			//对查询条件框赋值
			setSearchValue();
			
			//加载表格
			loadTasks();
		}
	});
}

//设置查询Id框的值
function setSearchValue()
{	
	var taskId = covTaskName2Id($("#taskIdList").combobox('getValue'));
	if ((null == taskId && "" == taskId) && (null != $("taskId").val()))
	{
		isAdd == true;
		$("#taskIdList").combobox('setValue',$("taskId").val());
	}else if("0" == taskId)
	{
	     //设为全部
		selectCombox("#taskIdList",null);
	}
	if (taskId != '0')
	{
		setSearchDisable();
	}
}
//选择具体的任务ID时，其余查询条件不能用
function setSearchDisable()
{
	$('#searchTaskState1').combobox('setValue','2');
	$('#searchTaskState1').combobox('disable');
	$('#searchTaskType1').combobox('setValue','0');
	$('#searchTaskType1').combobox('disable');
	$('#searchCycleType1').combobox('setValue','0');
	$('#searchCycleType1').combobox('disable');
}
//是否修改或者新增
var isAdd = true;
function showOptTask(isReqAdd,taskId,rowData) {
	isAdd = isReqAdd;
	var grabDatas = grabMaxWeightHisRSCountState(taskId);
	if(1 != grabDatas.length && 3 != grabDatas.length)
	{
		//是否提示
		return;
	}
	
	var maxWeight = grabDatas[0];
	$('#weight').numberbox("options").max = maxWeight;
	$("#taskReqAdd").val(isAdd);
	$("#hideOptTask").attr("style","display:block");
	if (isReqAdd)
	{
		clearTaskPage();
		//设置有关多批标志字段置灰不可用
		disableState();
		$("#popOptTask").window({
					modal : true,
					shadow : false,
					closed : false,
					title : "新增任务"
				});
	}
	else
	 {
			var runFlag = grabDatas[2];
	 		//将maxWeight值设置到控件上
			if(0 == parseInt(runFlag))
			{
				//提示请先停止任务，然后在修改(...字段),并将相关字段设置为灰色
				$.messager.defaults={ok:"确定",cancel:"取消"};
				var tips = '当前任务'+covTaskIds2Name(taskId, ';')+'正在运行.<br/>修改任务页面的[<font color="red">周期类型</font>,<font color="red">周期长度</font>]将不允许修改!';
					$.messager.confirm('提示', tips, function(r){
						if (r) {
							setData2TaskPage(rowData,true);
							$("#popOptTask").window({
								modal : true,
								shadow : false,
								closed : false,
								title : "修改任务"
							});
						}
					});
				
			}
			else
			{
				setData2TaskPage(rowData,false);
				$("#popOptTask").window({
					modal : true,
					shadow : false,
					closed : false,
					title : "修改任务"
				});
			}
	}
}
function disableState()
{
	if ($("input[name='task.multiBatchFlag']:checked").val() == "false")
	{
		$('#endBatchFlag').combobox('disable');
		document.getElementById("inputFileList").disabled = true;
		$('#inputFileMinCount').numberspinner('disable');
		$('#waitInputMinutes').numberspinner('disable');
	}
}
function enableState()
{
	$('#endBatchFlag').combobox('enable');
	document.getElementById("inputFileList").disabled = false;
	$('#inputFileMinCount').numberspinner('enable');
	$('#waitInputMinutes').numberspinner('enable');
}

//清除任务页面内容
function clearTaskPage()
{
	//$("#serviceId").combobox('setValue','0');
	selectCombox("#serviceId",null);
	//$("#serviceName").val(null);
	$("#userGroup").val(null);
	selectCombox("#deployedNodeList",null);
	$('#osUsers').combobox('enable');
	selectCombox("#osUsers",null);
	$("#taskName").val(null);
	$("#taskNameSrc").val(null);
	$("#taskDesc").val(null);
	$("#taskType").combobox('setValue','1');
	$("#priority").combobox('setValue','5');
	$("#cycleType").combobox('setValue','D');
	$("#cycleLength").val("1");
	$('input:hidden[name="task.cycleLength"]').val("1");
	$("#cycleOffset").val(0);
	$("input[name= 'task.cycleDependFlag'][value='true']").attr('checked','checked');
	$("input[name= 'task.multiBatchFlag'][value='false']").attr('checked','checked');
	$("#endBatchFlag").combobox('setValue','0');
	$("#dependTaskIdList").val(null);
	$("#dependTaskIdListDisplay").val(null);
	$("#inputFileList").val(null);
	$("#inputFileMinCount").val("0");
	$('input:hidden[name="task.inputFileMinCount"]').val("0");
	$("#waitInputMinutes").val("30");
	$('input:hidden[name="task.waitInputMinutes"]').val("30");
	$("#startTime").val("2013-01-01 00:00:00");
	$("#redoStartTime").val("2013-01-01 00:00:00");
	$("#redoDayLength").val("0");
	$("#redoType").val("1");
	$('input:hidden[name="task.redoDayLength"]').val("0");
	$("#weight").val("1");
	$('input:hidden[name="task.weight"]').val("1");
	
	//相关字段设置为可用
	$('#cycleType').combobox('enable');
	document.getElementById("dependTaskIdListDisplay").disabled = false;
	$('#cycleLength').numberspinner('enable');
	$('input:radio[id="cycleDependFlag"]').each(function(index,obj)
	{
		obj.disabled = false;
	});
	
	//隐藏不能修改的行
	if(systemAdmin)
	{
		$('#weightRow').attr("style","");
		$('#priorityRow').attr("style","");
	}
	else
	{
		$('#weightRow').attr("style","display:none;");
		$('#priorityRow').attr("style","display:none;");
	}
}
//更新时，设置内容到打开的更新页面
function setData2TaskPage(rowData,grayFlag)
{
	var canSelect = false;
	if(null != rowData["osUser"])
	{
	   var datas = $("#osUsers").combobox('getData');
	   if(datas.length>0)
	   {
	      for(var index in datas)
	      {
	         if(rowData["osUser"] == datas[index]["key"])
	         {
	             $('#osUsers').combobox('enable');
	             //选择
	             $("#osUsers").combobox('select',rowData["osUser"]);
	             canSelect = true;
	             break;
	         }
	      }
	   }
	}
	
	if(false == canSelect)
	{
	   $('#osUsers').combobox('disable');
	   $('#osUsers').combobox("setText",rowData["osUser"]);
	}
	
    $("#deployedNodeList").combobox('clear');
    if(null != rowData['deployedNodeList'])
	{
			var nodeIds = rowData['deployedNodeList'].split(',');
			for(var index in nodeIds)
			{
				if('' != nodeIds[index])
				{
					$("#deployedNodeList").combobox('select', nodeIds[index]);
				}
			}
	}
	//让任务组和业务的成为默认选择
	//$("#serviceName").val(getServiceName(rowData["serviceId"]));
	$("#userGroup").val(rowData["userGroup"]);
	selectCombox("#serviceId",rowData["serviceId"]);
	$("#taskName").val(rowData["taskName"]);
	$('#taskNameSrc').val(rowData["taskName"]);
	$("#taskDesc").val(rowData["taskDesc"]);
	$("#taskType").combobox("select",rowData["taskType"]);
	$("#priority").combobox("select",rowData["priority"]);
	$("#cycleType").combobox("select",rowData["cycleType"]);
	$("#cycleLength").val(rowData["cycleLength"]);
	$('input:hidden[name="task.cycleLength"]').val(rowData["cycleLength"]);
	$("#cycleOffset").val(rowData["cycleOffset"]);
	$("input[name= 'task.cycleDependFlag'][value='"+ rowData["cycleDependFlag"] +"']").attr('checked','checked');
	$("input[name= 'task.multiBatchFlag'][value='"+ rowData["multiBatchFlag"] +"']").attr('checked','checked');
	if (rowData["multiBatchFlag"] == true)
	{
		enableState();
	}
	else
	{
		disableState();
	}
	$("#endBatchFlag").combobox("select",rowData["endBatchFlag"]);
	$("#dependTaskIdList").val(rowData["dependTaskIdList"]);
	
	//获取依赖任务中的任务Id名字集合
	var taskIds = getDependTaskIds(rowData["dependTaskIdList"]);
	grabTaskIdNames(taskIds);
	$("#dependTaskIdListDisplay").val(covDependTaskIds2Name(rowData["dependTaskIdList"]));
	$("#inputFileList").val(rowData["inputFileList"]);
	$("#inputFileMinCount").val(rowData["inputFileMinCount"]);
	$('input:hidden[name="task.inputFileMinCount"]').val(rowData["inputFileMinCount"]);
	$("#waitInputMinutes").val(rowData["waitInputMinutes"]);
	$('input:hidden[name="task.waitInputMinutes"]').val(rowData["waitInputMinutes"]);
	$("#startTime").val(changeDate(rowData["startTime"]));
	$("#redoStartTime").val(changeDate(rowData["redoStartTime"]));
	$("#redoEndTime").val(changeDate(rowData["redoEndTime"]));
	$("#redoType").combobox("select",rowData["redoType"]);
	$("#redoDayLength").val(rowData["redoDayLength"]);
	$('input:hidden[name="task.redoDayLength"]').val(rowData["redoDayLength"]);
	$("#weight").val(rowData["weight"]);
	$('input:hidden[name="task.weight"]').val(rowData["weight"]);
	
	$("#taskId").val(rowData["taskId"]);
	if(true == grayFlag)
	{
		//置灰四个字段
		//相关字段设置为灰色
		$('#cycleType').combobox('disable');
		//document.getElementById("dependTaskIdListDisplay").disabled = true;
		$('#cycleLength').numberspinner('disable');
		//$('input:radio[id="cycleDependFlag"]').each(function(index,obj)
		//{
		//	obj.disabled = true;
		//});
	}
	else
	{
		//相关字段设置为灰色
		$('#cycleType').combobox('enable');
		document.getElementById("dependTaskIdListDisplay").disabled = false;
		$('#cycleLength').numberspinner('enable');
		$('input:radio[id="cycleDependFlag"]').each(function(index,obj)
		{
			obj.disabled = false;
		});
	}
	
	//隐藏不能修改的行
	if(systemAdmin)
	{
		$('#weightRow').attr("style","");
		$('#priorityRow').attr("style","");
	}
	else
	{
		$('#weightRow').attr("style","display:none;");
		$('#priorityRow').attr("style","display:none;");
	}
}
//加载任务信息列表到表格中
function loadTasks()
{
	//将要查询的值
	var taskIdValue = $("#taskIdOut").val();
	var taskStateValue = $("#searchTaskState1").combobox('getValue');
	var taskTypeValue = $("#searchTaskType1").combobox('getValue');
	var cycleTypeValue = $("#searchCycleType1").combobox('getValue');
	var choosedServiceId = $("#vserviceIdList").combobox('getValue');
	var osUser = $("#vOsUsers").combobox('getText');
	$('#taskList').datagrid({			onBeforeLoad: function(param)			{				if(!checkSessionValid())				{					return false;				}				else				{					return true;				}			},
		title : '任务列表',
		width : 1100,
		//height: 455,
		nowrap : false,
		striped : true,
		loadMsg:'数据加载中,请稍候...',
		singleSelect:false,
		collapsible : true,
		url : 'reqDataGridJsonObject',
		sortName : '任务更新时间',
		sortOrder : 'desc',
		remoteSort : false,
		queryParams:{
			'searchTaskId' : ("全部" == $("#taskIdList").combobox('getText')?'':encodeURI($("#taskIdList").combobox('getText'))),
			'searchTaskState' :("全部" == $("#searchTaskState1").combobox('getText'))?"" :taskStateValue,
			'searchCycleType' : "全部" == $("#searchCycleType1").combobox('getText')?'':cycleTypeValue,
			'searchTaskType' :  "全部" == $("#searchTaskType1").combobox('getText')?'':taskTypeValue,
			'searchServiceId' : "全部" == $("#vserviceIdList").combobox('getText')?'':choosedServiceId,
			'searchOsUser' : ("全部" == osUser?'':encodeURI(osUser))
			},
		idField : 'taskId',
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		},
		{
			field : 'taskName',
			title : '任务名称',
			width : 233,
			align : 'center',
			rowspan : 1
		},
		{
			field : 'opt',
			title : '操作',
			width : 150,
			align : 'center',
			rowspan : 2,
			formatter : function(value, rec) {
				var taskId = rec["taskId"];
				var optBtn = "";
				if(rec["taskState"])
				{
					optBtn = '<A title="启动任务" style="FLOAT: left" class="l-btn l-btn-plain" onclick="changeTaskState('+taskId+',0)" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-enable">&nbsp;</SPAN></SPAN></SPAN></A>';
					
				}
				else
				{
					optBtn =  '<A title="停止任务" style="FLOAT: left" class="l-btn l-btn-plain" onclick="changeTaskState('+taskId+',1)" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-disable">&nbsp;</SPAN></SPAN></SPAN></A>';
					
				}
				optBtn += '<A title="编辑任务" style="FLOAT: left" class="l-btn l-btn-plain" onclick="editTask('+taskId+')" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-edit">&nbsp;</SPAN></SPAN></SPAN></A>';
				optBtn += '<A title="转到任务步骤" style="FLOAT: left" class="l-btn l-btn-plain" onclick="nextStep('+taskId+')" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-next-step">&nbsp;</SPAN></SPAN></SPAN></A>';
				optBtn += '<A title="查看任务运行状态" style="FLOAT: left" class="l-btn l-btn-plain" onclick="queryTaskRS('+taskId+')" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-search">&nbsp;</SPAN></SPAN></SPAN></A>';
				optBtn += '<A title="告警配置" style="FLOAT: left" class="l-btn l-btn-plain" onclick="toAlarmConfig('+taskId+')" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-tip">&nbsp;</SPAN></SPAN></SPAN></A>';
				return optBtn;
			}
			
		}  ] ],
		columns : [ [
		{
			field : 'taskState',
			title : '状态',
			width : 45,
			rowspan : 1,
			align : 'center',
			formatter : function(value, rec) {
			return showTaskState(value);
		}
		}
		,
		{
			title : '任务ID',
			field : 'taskId',
			align : 'center',
			width : 65
		} 
		, 						
		{
			field : 'taskType',
			title : '任务类型',
			align : 'center',
			width : 90,
			rowspan : 1,
			formatter : function(value, rec) {
				return getTaskType(value);
			}
		} 
		,	    
		{
			field : 'priority',
			title : '优先级',
			align : 'center',
			rowspan : 1,
			width : 45
		}
		,
		{
			field : 'cycleType',
			align : 'center',
			title : '周期类型',
			width : 55,
			rowspan : 1,
			formatter : function(value, rec) {
				return getCycleType(value);
			}
		} ,
		{
			field : 'cycleLength',
			align : 'center',
			title : '周期长度',
			width : 55,
			rowspan : 1
		}
		,
		{
			field : 'serviceId',
			title : '业务',
			align : 'center',
			width : 60,
			rowspan : 1,
			formatter : function(value, rec) {
				return getServiceName(value);
			}
		},
		{
			field : 'creator',
			title : '创建者',
			align : 'center',
			width : 80,
			rowspan : 1,
			formatter : function(value, rec) {
				return value;
			}
		}
		,
		{
			field : 'osUser',
			title : '执行OS用户',
			align : 'center',
			width : 80,
			rowspan : 1,
			formatter : function(value, rec) {
				return value;
			}
		} 
		, 
		{
			field : 'userGroup',
			title : 'OS用户组',
			align : 'center',
			width : 60,
			rowspan : 1,
			formatter : function(value, rec) {
				return value;
			}
		},
		{
			field : 'lastUpdateTime',
			title : '任务更新时间',
			align : 'center',
			width : 125,
			rowspan : 1,
			formatter : function(value, rec) {
				return changeDate(value);
			}
		} 
		] ],
		pagination : true,
		rownumbers : true,
		pageNumber:1,
		toolbar : [
		           {
			id : 'btnstart',
			text : '启动',
			iconCls : 'icon-enable',
			handler : function() {
				//启动任务
				changeTaskStates(0);
			}
		}
		,
		{
			id : 'btnstart',
			text : '停止',
			iconCls : 'icon-disable',
			handler : function() {
				//停止任务
				changeTaskStates(1);
			}
		}
		,'-',
		{
			id : 'btnadd',
			text : '新增',
			iconCls : 'icon-add',
			handler : function() {
				showOptTask(true,null);
			}
		}
		,'-', {
			id : 'btndel',
			text : '删除',
			iconCls : 'icon-no',
			handler : function() {
				deleteTask();
			}
		}
		,'-', {
			id : 'btndel',
			text : taskIdCenter==true?'全量同步':'部署',
			iconCls : 'icon-reload',
			handler : function() {
				showDstTccServerAuth();
			}
		}
		]
		,
		onDblClickRow:function(rowIndex, rowData)
		{
			showOptTask(false,rowData["taskId"],rowData);
		}
	});
	var p = $('#taskList').datagrid('getPager');
	$(p).pagination({
	    showPageList:true,
	    beforePageText:'第',
	    afterPageText:'页, 共{pages}页',
	    displayMsg:'当前显示从{from}到{to}, 共{total}记录',
	    onBeforeRefresh:function(pageNumber,pageSize){
	     $(this).pagination('loading');
	     }
	   });
}

//编辑任务页面
function editTask(taskId)
{
	var datas = $('#taskList').datagrid('getData');
	for(var index in datas["rows"])
	{
		if(datas["rows"][index]["taskId"]==taskId)
		{
			showOptTask(false,taskId,datas["rows"][index]);
			break;
		}
	}
}

//弹出任务状态查询的页面
function queryTaskRS(taskId)
{
	if (null != taskId && "" != taskId && "0" != taskId)
	{
	    var url = "TaskRunningStateQuery.jsp?taskId=" + taskId;
	    window.open(url);
	}
}

function grabNormalTaskIds(distinctTaskIds)
{
	prefix = 's';
	var normalTaskIds = "";
    //异步获取集成的任务相关信息字段
	checkSessionValid();
	$.ajax({
		type:"post",
		url:"grabNormalTaskIds?taskIds="+distinctTaskIds,
		async : false,
		success : function(data, textStatus) {
			if (textStatus == "success") {
				data = data.split("|");
				if (data[0] == "true") {
					isSucessed = true;
					//提示是否继续选择
					normalTaskIds = data[1];
				} else {
					isSucessed = false;
				}
			}
		}
	});
    return normalTaskIds;
}

//将依赖关系dependTaskIds中的任务Id全部转换为任务名字
function covDependTaskIds2Name(dependTaskIds)
{
	//类型转换一下
	dependTaskIds = '' + dependTaskIds;
	var dependTaskNames = '';
	var splitChar = ';';
	if(null != dependTaskIds && '' != dependTaskIds)
	{
		var taskIdArr = dependTaskIds.split(splitChar);
		for(var index in taskIdArr)
		{
			if('' != taskIdArr[index])
			{
				var taskIdFullErrorArr = taskIdArr[index].split(',');
				if(taskIdFullErrorArr.length >= 3)
				{
					dependTaskNames += covTaskId2Name(taskIdFullErrorArr[0])+','+taskIdFullErrorArr[1]+','+taskIdFullErrorArr[2];
					if(taskIdFullErrorArr.length >= 4)
					{
						dependTaskNames += ','+taskIdFullErrorArr[3];
					}
				}
				dependTaskNames += splitChar;
			}
		}
	}
	return dependTaskNames;
}


//将依赖关系dependTaskIds中的任务Id全部转换为任务名字
function covDependTaskNames2Id(dependTaskNames)
{
	//类型转换一下
	dependTaskNames = '' + dependTaskNames;
	var dependTaskIds = '';
	var splitChar = ';';
	if(null != dependTaskNames && '' != dependTaskNames)
	{
		var taskNameArr = dependTaskNames.split(splitChar);
		for(var index in taskNameArr)
		{
			if('' != taskNameArr[index])
			{
				var taskNameFullErrorArr = taskNameArr[index].split(',');
				if(taskNameFullErrorArr.length >= 3)
				{
					dependTaskIds += covTaskName2Id(taskNameFullErrorArr[0])+','+taskNameFullErrorArr[1]+','+taskNameFullErrorArr[2];
					if(taskNameFullErrorArr.length >= 4)
					{
						dependTaskIds += ','+taskNameFullErrorArr[3];
					}
				}
				dependTaskIds += splitChar;
			}
		}
	}
	return dependTaskIds;
}

//将taskIds中的任务Id全部转换为任务名字
function covTaskIds2Name(taskIds, splitChar)
{
	//类型转换一下
	var taskIdSs = '' + taskIds;
	var taskNames = '';
	if(null != taskIds)
	{
		var taskIdArr = taskIdSs.split(splitChar);
		taskNames += '<table><tbody>';
		for(var index in taskIdArr)
		{
			if('' != taskIdArr[index])
			{
				taskNames += '<tr><td><font style="font-weight:bold;" color="red">';
				taskNames += covTaskId2Name(taskIdArr[index]);
				taskNames += '</font></td></tr>';
			}
		}
		taskNames += '</tbody></table>';
	}
	return taskNames;
}

//删除选中的任务
function deleteTask()
{
	var rows = $('#taskList').datagrid('getSelections');
	if (rows && rows.length > 0)
	{
		$.messager.defaults={ok:"确定",cancel:"取消"};
		var distinctTaskIds = "";
		for(var index in rows)
		{
			var taskId = rows[index]["taskId"];
			distinctTaskIds+= taskId + ";";
		}
		
		var normalTaskIds = grabNormalTaskIds(distinctTaskIds);
		
		if("" != normalTaskIds)
		{
			$.messager.defaults={ok:"确定",cancel:"取消"};
			$.messager.confirm('确认', "检测到"+covTaskIds2Name(normalTaskIds, ';')+"任务正在运行中,请先停止任务!", function(r){
					if(r)
					{
						var url = "TaskList.jsp?taskId=" + normalTaskIds;
						window.open(url);
					}
				});
		}
		else
		{
			$.messager.confirm('严重警告!', "该操作将会删除任务相关的所有信息，包括<font color='red'>[依赖该任务的依赖关系,任务,任务步骤,任务运行状态,批次运行状态,步骤运行状态]</font>你确定要删除这"+rows.length+"行吗?", function(r){
				if (r){
					var allSucessed = true;
					while(rows && rows.length > 0)
					{
						var taskId = rows[0]["taskId"];
						if(!deleteBackStageTask(taskId))
						{
							allSucessed = false;
							return;
						}
						else
					    {
							var index = $('#taskList').datagrid('getRowIndex', rows[0]);
							$('#taskList').datagrid('deleteRow', index);
							rows = $('#taskList').datagrid('getSelections');
					    }
					}
					
					if(allSucessed == true)					
					{
						  //重新刷新
						  $('#taskIdList').combobox('select',0);
						  //$('#taskIdOut').val(0);
						  $('#searchTaskState1').combobox('select',2);
						  $('#searchTaskType1').combobox('select',0);
						  $('#searchCycleType1').combobox('select',0);
						  //重新加载任务列表，包括任务Id列表
						  loadTaskIdList();
						  //对查询条件框赋值
						$.messager.alert('提示','删除成功!','info');
					}
					else
					{
						//$.messager.alert('提示','删除失败!','error');
					}
				}
				});
		}
	}
	else
	{
		$.messager.alert('提示','请至少选择一行记录!','info');
	}
}
//删除任务后台数据
function deleteBackStageTask(taskId)
{
	var isSucessed = false;
	checkSessionValid();
	$.ajax({
		type : "post",
		url : "deleteTask",
		data : $.param({
			"task.taskId" : taskId
		}),
		async : false,
		success : function(data, textStatus) {
			if (textStatus == "success") {
				data = data.split(",");
				  if (data[0] == "true") {
					  isSucessed = true;
				  }
				  else
				  {
					  isSucessed = false;
					  
					  if(alertPrivilegeNotEnouth(data, '删除任务<font color="red">'+covTaskId2Name(taskId)+'</font>失败'))
					  {
						  return isSucessed;
					  }
					  else
					  {
						  $.messager.defaults={ok:"确定",cancel:"取消"};
						  $.messager.alert('提示','删除任务<font color="red">'+covTaskId2Name(taskId)+'</font>失败','error');
					  }
				  }
			}
		}
	});
	return isSucessed;
}

function getServiceName(serviceID)
{
	return serviceIdName[serviceID];
}
//转换到页面表格中显示
function getTaskType(taskType)
{
	var title;
	switch (parseInt(taskType)) {
	case 1:
		title = "数据文件导入ODS";
		break;
	case 2:
		title = "ODS转换到DW";
		break;
	case 3:
		title = "ODS转换到DIM";
		break;
	case 4:
		title = "DW内部转换";
		break;
	case 5:
		title = "DW转换到DM";
		break;
	case 6:
		title = "DW统计输出报表";
		break;
	case 7:
		title = "报表导出文件";
		break;
	case 8:
		title = "DW导出文件";
		break;
	case 9:
		title = "混合型";
		break;
	default:
		title = "数据文件导入ODS";
		break;
	}
	return title;
}
function getCycleType(cycleType)
{
	var cycleTypeTitle = null;
	if ("H" == (cycleType) || "h" == (cycleType))
	{
		cycleTypeTitle = "按小时";
	}
	else if ("I" == (cycleType) || "i" == (cycleType))
	{
		cycleTypeTitle = "按分钟";
	}
	else if ("D" == (cycleType) || "d" == (cycleType))
	{
		cycleTypeTitle = "按天";
	}
	else if ("M" == (cycleType) || "m" == (cycleType))
	{
		cycleTypeTitle = "按月";
	}
	else if ("Y" == (cycleType) || "y" == (cycleType))
	{
		cycleTypeTitle = "按年";
	}

	return cycleTypeTitle;
	
}

//转换任务状态标志
function showTaskState(taskState)
{
	if(taskState)
	{
		//未启用
		return "<img src='themes/icons/disablestate.png'/>";
	}
	else
	{
		//正常
		return "<img src='themes/icons/enablestate.png'/>";
	}
}

function vaildData()
{
	if(isEmpty($("#userGroup").val())
	   || isEmpty($("#osUsers").combobox("getText"))
	   || isEmpty($("#taskName").val())
	   || isEmpty($("#taskDesc").val())
	   || isEmpty($("#cycleLength").val())
	   || isEmpty($("#cycleOffset").val())
	   || isEmpty($("#startTime").val())
	   || isEmpty($("#redoStartTime").val())
	   || isEmpty($("#waitInputMinutes").val())
	   || isEmpty($("#weight").val())
	   || isEmpty($("#redoDayLength").val()))
	{
		$.messager.alert('提示','*标识的字段不能为空白字符串!','info');
		return false;
	}
	
	if(vaildTaskName($("#taskName").val())== false)
	{
		$.messager.alert('提示','请输入正确的格式的任务名!','info');
		return false;
	}
	
	if(vaildCycleOffset($("#cycleOffset").val())== false)
	{
		$.messager.alert('提示','请输入正确的格式的周期偏移!','info');
		return false;
	}

    if($("#dependTaskIdListDisplay").val()!= "")
	{
		if(vaildTaskIdList($("#dependTaskIdListDisplay").val())== false)
		{
		   $.messager.alert('提示','请输入正确的任务依赖关系!','info');
		   return false;
		}
		
		//不能依赖自己
		if((';'+$("#dependTaskIdListDisplay").val()).indexOf(';'+$("#taskName").val()+',',0) >= 0)
		{
		    $.messager.defaults={ok:"是",cancel:"否"};
			$.messager.alert('提示', "请修改任务依赖关系：任务<font style='font-weight:bold;' color='red'>"+$("#taskName").val()+"</font>不能依赖自身",'info');
			return false;
		}
		
		var noExistTaskIdNames = getNoExistTaskIdNames($("#dependTaskIdListDisplay").val());
		if('' != noExistTaskIdNames)
		{
			$.messager.defaults={ok:"是",cancel:"否"};
			$.messager.alert('提示', "请修改任务依赖关系：任务<font style='font-weight:bold;' color='red'>"+$("#taskName").val()+"</font>不能依赖如下任务（没有查询权限）"+formatNoExistTaskIdNames(noExistTaskIdNames,';'),'info');
			return false;
		}
	}
    
	return true;	
}

function isResetTaskRS()
{
	  if(isAdd)
	  {
		  return false;
	  }
	  
	  var taskId = $('#taskId').val();
	  var cycleLength = $('#cycleLength').val();
	  var cycleType = $('#cycleType').combobox('getValue');
	  var datas = $('#taskList').datagrid('getData')["rows"];
	  for(var index in datas)
	  {
		  if(datas[index]['taskId'] == taskId)
		  {
			  if(cycleLength != datas[index]['cycleLength'] || cycleType != datas[index]['cycleType'])
			  {
				  return true;
			  }
		  }
	  }
	  return false;
}

function resetTaskRSAlarm()
{
	var bcontinue = false;
	if(isResetTaskRS())
	{
		$.messager.defaults={ok:"是",cancel:"否"};
		$.messager.confirm('严重警告!', "你修改了任务周期类型或者任务周期长度!<font color='red'>这将导致该任务全部的运行状态(任务周期)重新初始化!</font>,请确认该操作的必要性!终止当前操作(否)，仍然继续(是).注意,如果选是你可能需要先调整任务最早开始时间.", function(r)
		{
			if (r)
			{
				$('#resetTaskRS').val(true);
				bcontinue = true;
			}
			else
			{
				$('#resetTaskRS').val(false);
			}
		});
	}
	else
	{
		return true;
	}
	return bcontinue;
}

function saveTask(){
	$('#resetTaskRS').val(false);
	 //前台同步提交数据
	 var bcontinue = false;
	if(true == isResetTaskRS())
	{
		$.messager.defaults={ok:"是",cancel:"否"};
		$.messager.confirm('严重警告!', "你修改了任务周期类型或者任务周期长度!<font color='red'>这将导致该任务全部的运行状态(任务周期)重新初始化!</font>,请确认该操作的必要性!终止当前操作(否)，仍然继续(是).注意,如果选是你可能需要先调整任务最早开始时间.", function(r)
		{
			if (r)
			{
				$('#resetTaskRS').val(true);
				bcontinue = true;
			}
			else
			{
				$('#resetTaskRS').val(false);
				bcontinue = false;
			}
			
			saveTaskInteral(bcontinue);
		});
	}
	else
	{
		bcontinue = true;
		saveTaskInteral(bcontinue);
	}
}

function saveTaskInteral(bcontinue)
{
	// 新增加的ajax同步请求
	 if(vaildData() && bcontinue)
	{  
	   $('#dependTaskIdList').val(covDependTaskNames2Id($('#dependTaskIdListDisplay').val()));
       checkSessionValid();
	   $.ajax({
	       type: "post",
	       url: "saveTccTask?resetTaskRS="+$('#resetTaskRS').val(),
	       data: $("#tccTaskForm").serialize(),
	       async: false,
	       success: function(data, textStatus){
	    	   if (textStatus == "success") {
	    	          data = data.split(",");
					  if (data[0] == "true") {
						  $.messager.alert('提示','保存成功!','info');
						  $("#popOptTask").window('close');
						  loadDatas();
					  }
					  else
					  {
						    if(alertPrivilegeNotEnouth(data, '保存任务失败'))
							{
								  return;
							}
						  
						  if(data.length >=2)
						  {
							  if('0' == data[1])
							  {
								  $.messager.alert('提示','对不起,保存失败!','info');
							  }
							  else if('1' == data[1])
							  {
								  $.messager.alert('提示','对不起,保存失败. 任务名不允许重复!','info');
							  }
							  else
							  {
								  $.messager.alert('提示','对不起,保存失败!','info');
							  }
						  }
						  else
					      {
						  	  $.messager.alert('提示','对不起,保存失败!','info');
					      }
					  }
				 }
			  }
		  });
    }
}

function grabMaxWeightHisRSCountState(taskId)
{
	var reqUrl = '';
	if(null != taskId)
	{
		reqUrl = "grabMaxWeightHisRSCountState?taskId="+taskId;
	}
	else
	{
		reqUrl = "grabMaxWeightHisRSCountState";
	}
    var grabDatas = new Array();
   	checkSessionValid();
	$.ajax({
			type : "post",
			url : reqUrl,
			async : false,
			success : function(data, textStatus) {
				if (textStatus == "success") {
					var datas = data.split('|');
					if (datas[0] == "true") {						
						{
							if("" != datas[1])
							{
								var grabDataArr = datas[1].split(',');
								if(3 == grabDataArr.length)
								{
									//最大权重取值
									var maxWeight = grabDataArr[0];
									//历史数据任务数
									var hisTaskRSCount = grabDataArr[1];
									//是否运行0正常,1停止
									var runFlag = grabDataArr[2];
									
									grabDatas[0] = parseInt(maxWeight);
									grabDatas[1] = parseInt(hisTaskRSCount);
									grabDatas[2] = parseInt(runFlag);
								}
								else if(1 == grabDataArr.length)
								{
									//最大权重取值
									var maxWeight = grabDataArr[0];
									grabDatas[0] = parseInt(maxWeight);
								}
							}
						}
					} 
				}
			}
		});

   	return grabDatas;
}

//改变任务启用状态
function changeTaskState(taskId,value)
{
	if (value == 1)
	{
		 changeState(taskId,value);
	}
	//启用时，去后台查看任务步骤是否允许任务启用条件
	else 
	{
		var count = getStepEnableCount(taskId);
		var datas = $('#taskList').datagrid('getData');
		var nodes;
		var multiBatch;
		for(var index in datas["rows"])
		{
			if(datas["rows"][index]["taskId"]==taskId)
			{
			    nodes = datas["rows"][index]["deployedNodeList"];
			    multiBatch = datas["rows"][index]["multiBatchFlag"];
			    
			    if(null == nodes || "" == nodes)
			    {
			        if(multiBatch == true || count > 0)
			        {
			        	$.messager.alert('提示',"任务<font style='font-weight:bold;' color='red'>"+covTaskId2Name(taskId)+"</font>需要选择执行节点！",'info');
			        	return;
			        }
			    }
			    
				//符合条件
				if (count > 0)
				{
					changeState(taskId,value);
				}
				else
				{
					$.messager.defaults={ok:"是",cancel:"否"};
					$.messager.confirm('警告', "任务<font style='font-weight:bold;' color='red'>"+covTaskId2Name(taskId)+"</font>没有可用的任务步骤，编辑任务步骤（是）或者继续启用该任务（否）", function(r){
						if (r){
							nextStep(taskId);
						}
						else{
							changeState(taskId,value);
						}
					});
				}
				isSucessed = true;
				break;
			}
		}
	}	   
}

//将taskIds中的任务Id全部转换为任务名字
function formatNoExistTaskIdNames(noExistTaskIdNames, splitChar)
{
	//类型转换一下
	var noExistTaskIdNames = '' + noExistTaskIdNames;
	var taskNames = '';
	if(null != noExistTaskIdNames)
	{
		var taskNameArr = noExistTaskIdNames.split(splitChar);
		taskNames += '<table><tbody>';
		for(var index in taskNameArr)
		{
			if('' != taskNameArr[index])
			{
				taskNames += '<tr><td><font style="font-weight:bold;" color="red">';
				taskNames += taskNameArr[index];
				taskNames += '</font></td></tr>';
			}
		}
		taskNames += '</tbody></table>';
	}
	return taskNames;
}

//获取依赖任务ID列表中还没创建任务的任务名集合
function getNoExistTaskIdNames(dependTaskIds)
{
	//类型转换一下
	dependTaskIds = '' + dependTaskIds;
	var noExistTaskIdNames = '';
	var splitChar = ';';
	if(null != dependTaskIds && '' != dependTaskIds)
	{
		var taskIdArr = dependTaskIds.split(splitChar);
		for(var index in taskIdArr)
		{
			if('' != taskIdArr[index])
			{
				var taskIdFullErrorArr = taskIdArr[index].split(',');
				if(taskIdFullErrorArr.length >= 3)
				{
					if(taskIdFullErrorArr[0] == covTaskName2Id(taskIdFullErrorArr[0]))
					{
						noExistTaskIdNames += taskIdFullErrorArr[0];
						noExistTaskIdNames += splitChar;
					}
				}
			}
		}
	}
	return noExistTaskIdNames;
}

//改变任务启用状态
function changeTaskStateWithOutWait(taskId,value)
{
	if (value == 1)
	{
		 changeState(taskId,value);
	}
	//启用时，去后台查看任务步骤是否允许任务启用条件
	else 
	{
		var count = getStepEnableCount(taskId);
		var datas = $('#taskList').datagrid('getData');
		for(var index in datas["rows"])
		{
			if(datas["rows"][index]["taskId"]==taskId)
			{
				var nodes = datas["rows"][index]["deployedNodeList"];
			    var multiBatch = datas["rows"][index]["multiBatchFlag"];
			    
			    if(null == nodes || "" == nodes)
			    {
			        if(multiBatch == true || count > 0)
			        {
			        	$.messager.alert('提示',"任务<font style='font-weight:bold;' color='red'>"+covTaskId2Name(taskId)+"</font>需要选择执行节点！",'info');
			        	return;
			        }
			    }
			    
				if (count > 0)
				{
					changeState(taskId,value);
				}
				else
				{
					$.messager.defaults={ok:"是",cancel:"否"};
					$.messager.confirm('警告', "任务<font style='font-weight:bold;' color='red'>"+covTaskId2Name(taskId)+"</font>没有可用的任务步骤，编辑任务步骤（是）或者继续启用该任务（否）", function(r){
						if (r){
							nextStep(taskId);
						}
						else{
							changeState(taskId,value);
						}
					});
				}
				isSucessed = true;
				break;
			}
		}
	}	   
}

//改变任务启用状态
function changeTaskStates(value)
{
	var cmd = "";
	if(value == 1)
	{
		cmd = "停止";
	}
	else
	{
		cmd = "启动";
	}
	
	var rows = $('#taskList').datagrid('getSelections');
	if (rows && rows.length > 0)
	{
		var taskIds = "";
		var count =0;
		for(var index in rows)
		{
			var taskId = rows[index]["taskId"];
			var state = rows[index]["taskState"];
			if(state != value)
			{
				count ++;
				taskIds += taskId+";";
			}
		}
		
		if("" == taskIds)
		{
			$.messager.defaults={ok:"是",cancel:"否"};
			$.messager.alert('提示','任务均已经'+cmd+',无需重新' + cmd+'!','info');
		}
		else
		{
			var tips = "你确定要" + cmd + "任务"+covTaskIds2Name(taskIds,';')+"吗?";
			
			$.messager.defaults={ok:"是",cancel:"否"};
			$.messager.confirm('提示!', tips, function(r){
				if (r){
					for(var index in rows)
					{
						var taskId = rows[index]["taskId"];
						var state = rows[index]["taskState"];
						if(state != value)
						{
							changeTaskStateWithOutWait(taskId,value);
						}
					}
				}
				});
		}
	}
	else
	{
		$.messager.defaults={ok:"确定",cancel:"取消"};
		$.messager.alert('提示','请至少选择一个任务!','info');
	}
}

//改变后台任务状态
function changeState(taskId,value)
{
	 var isSucessed = false;
	   	checkSessionValid();
		$.ajax({
				type : "post",
				url : "changeTaskState",
				data : $.param({
					"task.taskId" : taskId,
					"task.taskState" : value
				}),
				async : false,
				success : function(data, textStatus) {
					if (textStatus == "success") {
						data = data.split(",");
						if (data[0] == "true") {						
							//修改本地数据
							{
								if(data.length > 2 && ('5' == data[1] || '3' == data[1]))
								{
									if('5' == data[1])
									{
										$.messager.defaults={ok:"确定",cancel:"取消"};
										$.messager.alert('提示','任务<font color="red">'+covTaskId2Name(taskId)+'</font>已经启动,无需再次启动!','info');
									}
									
									var startOperator = data[2];
									//更新状态和启动用户
									var datas = $('#taskList').datagrid('getData');
									for(var index in datas["rows"])
									{
										if(datas["rows"][index]["taskId"]==taskId)
										{
											datas["rows"][index]["taskState"] = value;
											datas["rows"][index]["startOperator"] = startOperator;
											$('#taskList').datagrid('refreshRow',index);
											isSucessed = true;
											break;
										}
									}
								}
								else
								{
									//更新状态
									var datas = $('#taskList').datagrid('getData');
									for(var index in datas["rows"])
									{
										if(datas["rows"][index]["taskId"]==taskId)
										{
											datas["rows"][index]["taskState"] = value;
											$('#taskList').datagrid('refreshRow',index);
											isSucessed = true;
											break;
										}
									}
								}
							}
						} else {
							  isSucessed = false;
							  var opt = '启动';
				        	  if('1'==value)
				        	  {
				        		  opt = '停止';
				        	  }
				        	  
							  if(alertPrivilegeNotEnouth(data, opt+'任务<font color="red">'+covTaskId2Name(taskId)+'</font>失败'))
							  {
								  return isSucessed;
							  }
							  else
							  {
								  $.messager.defaults={ok:"确定",cancel:"取消"};
								  $.messager.alert('提示',opt+'任务<font color="red">'+covTaskId2Name(taskId)+'</font>失败','error');
							  }
						}
					}
				}
			});
	   	return isSucessed;
}
//有条件查询要显示任务信息
function searchTask()
{
   loadTasks();
}

function getStepEnableCount(taskId)
{
	var count = 0;
	checkSessionValid();
	$.ajax({
		type : "post",
		url : "getStepEnableCount",
		data : $.param({
			"task.taskId" : taskId
		}),
		async : false,
		success : function(data, textStatus) {
			if (textStatus == "success") {
				data = data.split(",");
				if (data[0] == "true") {						
                  {
                	  count = data[1];
				  }
				}
			}
		}
	});
	return count;
}
//到任务步骤列表  
function nextStep(taskId)
{
	if (null != taskId && "" != taskId)
	{
	    var url = "StepList.jsp?taskId=" + taskId;
	    window.open(url);
	}
}
//对日期函数的处理，如"2000-01-01T01:00:00"转化成"2000-01-01 01:00:00"
//"2000-01-01"转化成"2000-01-01 00:00:00"
function changeDate(dateValue)
{
	if (dateValue.indexOf("T") != -1)
	{
	    var reg=new RegExp("T","g");
	    return dateValue.replace(reg, " ");
	}
	else
	{
		return dateValue + " 00:00:00";
	}
}

//处理取消按钮
function cancel() {
	$("#popOptTask").window('close');
}

$(function(){
	$("#redoType").combobox({
		onSelect:function(record)
		{
			if(record["value"] == 1)
			{
				//天数不变灰
				$('#redoDayLength').numberspinner('enable');
			}
			else
			{
				//天数变灰
				$('#redoDayLength').numberspinner('disable');
			}
		}
	});
});
//////////////////////////////////////////告警配置///////////////////////////////////////////
//弹出告警信息配置框
function toAlarmConfig(taskId)
{
	showOptAlarmConfig(taskId);
}


//弹出告警信息配置框
function showOptAlarmConfig(taskId) {
	$("#hideOptAlarmConfig").attr("style","display:block");
	clearAlarmConfigPage();
	loadAlarmConfig(taskId);
}

//更新时，设置内容到打开的更新页面
function clearAlarmConfigPage()
{
	$("#ac_taskId").val('');
	$("#isAlarmPermitted").attr("checked",false);
	$("#alarmType").combobox("clear");
	$("#latestStartTime").val('');
	$("#latestEndTime").val('');
	$("#maxRunTime").val('');
	$('input:hidden[name="maxRunTime"]').val('');
	$("#normalEmailList").val('');
	$("#normalMobileList").val('');
	$("#severeEmailList").val('');
	$("#severeMobileList").val('');
}

//更新时，设置内容到打开的更新页面
function setData2AlarmConfigPage(alarmConfigJsonObj,taskId)
{
	$("#ac_taskId").val(taskId);
	if(null != alarmConfigJsonObj)
	{
		$("#isAlarmPermitted").attr("checked",alarmConfigJsonObj["isAlarmPermitted"]);
		var alarmType = alarmConfigJsonObj["alarmType"];
		for(var index in alarmType)
		{
			if(index >0 && index<=9 && '1' == alarmType[index])
			{
				$("#alarmType").combobox("select",index);
			}
		}
		$("#latestStartTime").val(alarmConfigJsonObj["latestStartTime"]);
		$("#latestEndTime").val(alarmConfigJsonObj["latestEndTime"]);
		if(null != alarmConfigJsonObj["maxRunTime"])
		{
			$("#maxRunTime").val(alarmConfigJsonObj["maxRunTime"]);
			$('input:hidden[name="maxRunTime"]').val(alarmConfigJsonObj["maxRunTime"]);
		}
		
		$("#normalEmailList").val(alarmConfigJsonObj["normalEmailList"]);
		$("#normalMobileList").val(alarmConfigJsonObj["normalMobileList"]);
		$("#severeEmailList").val(alarmConfigJsonObj["severeEmailList"]);
		$("#severeMobileList").val(alarmConfigJsonObj["severeMobileList"]);
	}
}

function loadAlarmConfig(taskId)
{
	checkSessionValid();
	$.ajax({
		type : "post",
		url : "reqAlarmConfig?taskId="+taskId,
		async : false,
		success : function(data, textStatus)
		{
			if (textStatus == "success")
			{
				var jsonObj = $.parseJSON(data);
				var success = jsonObj["success"];
				var returnValue2PageType = jsonObj["returnValue2PageType"];
				var values = jsonObj["values"];
				if (success == true)
				{
					if(3 == returnValue2PageType)
					{
						if(values.length >= 1)
						{
							var alarmConfigJsonObj= $.parseJSON(values[0]);
							//绑定
							setData2AlarmConfigPage(alarmConfigJsonObj,taskId);
							
							$("#popOptAlarmConfig").window({
								modal : true,
								shadow : false,
								closed : false,
								title : "<font color='red'>["+covTaskId2Name(taskId)+"]</font>告警配置"
							});
						}
					}
					else
					{
						$.messager.defaults={ok:"确定",cancel:"取消"};
						$.messager.alert('提示','加载告警配置失败!','error');
					}
				}
				else
				{
					if(2 == returnValue2PageType)
					{
						alertPrivilegeNotEnouthJson(values[0],"加载告警配置失败");
			        }
					else if(4  == returnValue2PageType)
					{
						$.messager.defaults={ok:"确定",cancel:"取消"};
						$.messager.alert('提示','加载告警配置失败,没有相关任务信息!','error');
					}
					else
					{
						$.messager.defaults={ok:"确定",cancel:"取消"};
						$.messager.alert('提示','加载告警配置失败!','error');
					}
				}
			}
		}
	});
}

//处理取消按钮
function cancelAlarmConfig() {
	$("#popOptAlarmConfig").window('close');
}

//保存告警配置
function saveAlarmConfig()
{
	var alarmTypeSelect = $("#alarmType").combobox("getValues");
	var alarmTypeArr = new Array();
	for(var index=0;index<=9;index++)
	{
		alarmTypeArr[index] = "0";
	}
	
	for(var index in alarmTypeSelect)
	{
		alarmTypeArr[alarmTypeSelect[index]] = "1";
	}
	
	var alarmType = "";
	for(var index in alarmTypeArr)
	{
		alarmType += alarmTypeArr[index];
	}
	
	if(vaildAlarmConfigData())
	{
		checkSessionValid();
		$.ajax({
			type : "post",
			url : "saveAlarmConfig",
			data : $.param({
				"taskId":$("#ac_taskId").val(),
				"isAlarmPermitted":"checked" == $("#isAlarmPermitted").attr('checked'),
				"alarmType":alarmType,
				"latestStartTime":$("#latestStartTime").val(),
				"latestEndTime":$("#latestEndTime").val(),
				"maxRunTime":$("#maxRunTime").val(),
				"normalEmailList":$("#normalEmailList").val(),
				"normalMobileList":$("#normalMobileList").val(),
				"severeEmailList":$("#severeEmailList").val(),
				"severeMobileList":$("#severeMobileList").val()
			}),
			async : false,
			success : function(data, textStatus) {
				if (textStatus == "success") {
					var jsonObj = $.parseJSON(data);
					var success = jsonObj["success"];
					var returnValue2PageType = jsonObj["returnValue2PageType"];
					var values = jsonObj["values"];
					if (success == true) {
						$.messager.alert('提示','恭喜,保存成功!','info');
						$("#popOptAlarmConfig").window('close');
					} 
					else
					{
						if(2 == returnValue2PageType)
						{
							alertPrivilegeNotEnouthJson(values[0],"保存失败");
				        }
						else if(4  == returnValue2PageType)
						{
							$.messager.defaults={ok:"确定",cancel:"取消"};
							$.messager.alert('提示','对不起,保存失败,没有相关任务信息，请先创建任务!','error');
						}
						else
						{
							$.messager.defaults={ok:"确定",cancel:"取消"};
							$.messager.alert('提示','对不起,保存失败!','error');
						}	
					}
				}
			}
		});
	}
}

function vaildAlarmConfigData()
{
	if($('#latestStartTime').val() != "")
    {
    	var latestStartTime = $('#latestStartTime').val();
    	if(false == vaildCycleOffset(latestStartTime))
    	{
    		$.messager.alert('提示','请输入正确的最次启动相对时间,格式：xMxD xhxm  例如：2h10m','info');
    		return false;
    	}
    }
	
    if($('#latestEndTime').val() != "")
    {
    	var latestEndTime = $('#latestEndTime').val();
    	if(false == vaildCycleOffset(latestEndTime))
    	{
    		$.messager.alert('提示','请输入正确的最次结束相对时间,格式：xMxD xhxm  例如：2h10m','info');
    		return false;
    	}
    }
	
    if($('#normalEmailList').val() != "")
    {
    	var emails = $('#normalEmailList').val();
    	if(false == validEmails(emails))
    	{
    		$.messager.alert('提示','一般告警级别中,请输入正确的华为邮箱列表,多个使用";"号分隔!','info');
    		return false;
    	}
    }
    
    if($('#normalMobileList').val() != "")
    {
    	var phones = $('#normalMobileList').val();
    	if(false == validPhones(phones))
    	{
    		$.messager.alert('提示','一般告警级别中,请输入正确的手机号码列表,多个使用";"号分隔!','info');
    		return false;
    	}
    }
    
    if($('#severeEmailList').val() != "")
    {
    	var emails = $('#severeEmailList').val();
    	if(false == validEmails(emails))
    	{
    		$.messager.alert('提示','严重告警级别中,请输入正确的华为邮箱列表,多个使用";"号分隔!','info');
    		return false;
    	}
    }
    
    if($('#severeMobileList').val() != "")
    {
    	var phones = $('#severeMobileList').val();
    	if(false == validPhones(phones))
    	{
    		$.messager.alert('提示','严重告警级别中,请输入正确的手机号码列表,多个使用";"号分隔!','info');
    		return false;
    	}
    }
    
	return true;	
}

//弹出目标tcc服务器认证窗口
function showDstTccServerAuth() {
	$("#hideDstTccServerAuth").attr("style","display:block");
	
	$.messager.defaults={ok:"确定",cancel:"取消"};
	if(false == taskIdCenter)
	{
		var rows = $('#taskList').datagrid('getSelections');
		if (null == rows || rows.length == 0)
		{
			$.messager.alert('提示','请至少选择一条任务!','info');
			return;
		}
	}
	
	clearDstTccServerAuth();
	loadTccServers();
	
	$("#popDstTccServerAuth").window({
					modal : true,
					shadow : false,
					closed : false,
					title : true == taskIdCenter?"全量同步":"部署"
				});
}

function clearDstTccServerAuth()
{
	selectCombox("#tccServers",null);
	$("#dstUserName").val(null);
	$("#dstPassword").val(null);
	
	if(true == taskIdCenter)
	{
	   $("#dstUserNameTips").attr("style","display:block;");
	}
	else
	{
	   $("#dstUserNameTips").attr("style","display:none;");
	}
	
}

function loadTccServers()
{
	checkSessionValid();
	$('#tccServers').combobox({
		url : "getAllTccServers",
		valueField : "key",
		textField : "value",
		title:'TCC服务器',
		width: 150,
		editable : false,
		onLoadSuccess:function()
		{
			selectCombox("#tccServers",null);
		}
	});
}

function vaildSyncTcc()
{
    var dstServer = $("#tccServers").combobox("getValue");
	if (isEmpty($("#dstUserName").val()) || isEmpty($("#dstPassword").val())
	  || isEmpty(dstServer))
	{
			$.messager.alert('提示', '*标识的字段必选或者必填!', 'info');
			return false;
	} 
    
	return true;	
}

function syncTcc()
{
	$.messager.defaults={ok:"确定",cancel:"取消"};
	var dstServer = $("#tccServers").combobox("getValue");
	if(false == taskIdCenter)
	{
		var rows = $('#taskList').datagrid('getSelections');
		if (rows && rows.length > 0)
		{
			var distinctTaskIds = "";
			for(var index in rows)
			{
				var taskId = rows[index]["taskId"];
				distinctTaskIds+= taskId + ",";
			}
			
			var choosed = false;
			$.messager.confirm('确认', "你确认要将选择的任务部署到<font color='red'>"+dstServer+"</font>!", function(r)
			{
				if(r)
				{
					syncTccInternal(distinctTaskIds);
				}
			});
		}
		else
		{
			$.messager.alert('提示','请至少选择一条任务!','info');
		}
	}
	else
	{
	        var choosed = false;
			$.messager.confirm('确认', "你确认要将全量任务同步到<font color='red'>"+dstServer+"</font>!", function(r)
			{
				if(r)
				{
					syncTccInternal('');
				}
			});
	}
}


//同步TCC
function syncTccInternal(taskIds)
{
	var dstServer = $("#tccServers").combobox("getValue");
	if(vaildSyncTcc())
	{
		checkSessionValid();
		$("#processing").attr("style","display:block");
		$.ajax({
			type : "post",
			url : "syncTcc",
			data : $.param({
			    "dstServer":dstServer,
				"userName":$("#dstUserName").val(),
				"password":$("#dstPassword").val(),
				"taskIds":taskIds
			}),
			async : false,
			success : function(data, textStatus) {
				$("#processing").attr("style","display:none");
				waitting = true;
				if (textStatus == "success") {
					var jsonObj = $.parseJSON(data);
					var success = jsonObj.success;
					var extValue = jsonObj.extValue;
					if (success == true)
					{
					    if(true == taskIdCenter)
					    {
							$.messager.alert('提示','恭喜, 全量同步成功!','info');
					    }
					    else
					    {
					    	if(null != extValue && extValue.length > 0)
					    	{
					    		var taskIds = '';
					    		for(var index in extValue)
					    		{
					    		   taskIds = taskIds + extValue[index] + ';';
					    		}
					    		$.messager.alert('提示','任务'+covTaskIds2Name(taskIds, ';')+'因任务存在或者权限不足导致部署失败, 其它任务部署成功!','info');
					    	}
					    	else
					    	{
					    		$.messager.alert('提示','恭喜, 选择的任务全部部署成功!','info');
					    	}
					    }
					    
					    $("#popDstTccServerAuth").window('close');
					} 
					else
					{
						var action = (true == taskIdCenter) ? '全量同步':'部署';
						$.messager.defaults={ok:"确定",cancel:"取消"};
						if(extValue == 1005)
						{
							$.messager.alert('提示','对不起,'+action+'失败. 用户名密码不匹配!','error');
						}
						else if(extValue== 1006)
						{
							$.messager.alert('提示','对不起,'+action+'失败. 必需使用系统管理员用户!','error');
						}
						else
						{
							$.messager.alert('提示','对不起,'+action+'失败!','error');
						}
					}
				}
			}
		});
	}
}

function cancelSyncTcc()
{
	$("#popDstTccServerAuth").window('close');
}

</script>
</html>