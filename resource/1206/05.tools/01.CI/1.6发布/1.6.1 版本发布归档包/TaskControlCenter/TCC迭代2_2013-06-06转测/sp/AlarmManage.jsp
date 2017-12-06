<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery-1.7.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="stylesheet" type="text/css" href="css/table.css" />
<link rel="stylesheet" type="text/css" href="themes/icon.css" />
<link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
<script language="javascript" type="text/javascript"
	src="DatePicker/WdatePicker.js"></script>
<title><s:text name="告警管理页面" />
</title>
<script type="text/javascript">
	function showOptAlarm(rowData) {
		$("#hideOptAlarm").attr("style", "display:block");
		setData2AlarmPage(rowData);
		$("#popOptAlarm").window({
			modal : true,
			shadow : false,
			closed : false,
			title : "告警处理"
		});
	}

	//编辑页面
	function editAlarm(taskId, cycleId, alarmType, alarmTime) {
		var datas = $('#AlarmFactList').datagrid('getData');
		for ( var index in datas["rows"]) {
			if (datas["rows"][index]["taskId"] == taskId
					&& datas["rows"][index]["cycleId"] == cycleId
					&& datas["rows"][index]["alarmType"] == alarmType
					&& datas["rows"][index]["alarmTime"] == alarmTime) {
				showOptAlarm(datas["rows"][index]);
				break;
			}
		}
	}

	//在弹出的页面上填充好数据
	function setData2AlarmPage(rowData) {
		var data = {
			total : 1,
			rows : [ {
				"alarmType" : rowData['alarmType'],
				"alarmGrade" : rowData['alarmGrade'],
				"alarmTime" : rowData['alarmTime'],
				"serviceId" : rowData['serviceId'],
				"taskId" : rowData['taskId'],
				"cycleId" : rowData['cycleId'],
				"instanceId" : rowData['instanceId']
			} ]
		};
		$("#alarmInfo").datagrid({			onBeforeLoad: function(param)			{				if(!checkSessionValid())				{					return false;				}				else				{					return true;				}			},
			title : "告警信息",
			width : 850,
			height : 80,
			loadMsg : '数据加载中,请稍候...',
			nowrap : true,
			striped : true,
			collapsible : true,
			remoteSort : false,
			columns : [ [ {
				title : '告警类型',
				field : 'alarmType',
				align : 'center',
				width : 240,
				sortable : true,
				formatter : function(value, rec) {
					return alarmTypeList[value];
				}
			}, {
				field : 'alarmGrade',
				title : '告警级别',
				width : 60,
				align : 'center',
				rowspan : 1,
				formatter : function(value, rec) {
					if (1 == value) {
						return "一般";
					} else if (2 == value) {
						return "严重";
					} else {
						return null;
					}
				}
			}, {
				title : '告警时间',
				field : 'alarmTime',
				align : 'center',
				width : 120,
				sortable : true,
				formatter : function(value, rec) {
					return formatTime(value);
				}
			}, {
				field : 'serviceId',
				title : '业务类型',
				width : 120,
				align : 'center',
				rowspan : 1,
				formatter : function(value, rec) {
					return serviceIdName[value];
				}
			}, {
				title : '任务名称',
				field : 'taskId',
				align : 'center',
				width : 120,
				sortable : true,
				formatter : function(value, rec) {
					return taskIdName[value];
				}
			}, {
				title : '任务周期',
				field : 'cycleId',
				align : 'center',
				width : 80,
				sortable : true
			}, {
				field : 'instanceId',
				title : '实例ID',
				width : 100,
				align : 'center',
				rowspan : 1
			} ] ]
		});
		$("#alarmInfo").datagrid('loadData', data);
		$("#statusOpt").combobox('select', rowData['status']);
		$("#reason").val(rowData['reason']);
		$("#solution").val(rowData['solution']);
	}

	//前台同步提交数据
	// 新增加的ajax同步请求
	function saveAlarm() {
		var datas = $("#alarmInfo").datagrid("getData");
		checkSessionValid();
		$.ajax({
			type : "post",
			url : "saveAlarmHandle",
			data : $.param({
				"taskId" : datas["rows"][0]['taskId'],
				"cycleId" : datas["rows"][0]['cycleId'],
				"alarmTime" : formatTime(datas["rows"][0]['alarmTime']),
				"alarmType" : datas["rows"][0]['alarmType'],
				"reason" : $("#reason").val(),
				"solution" : $("#solution").val(),
				"status" : $("#statusOpt").combobox('getValue')
			}),
			async : false,
			success : function(data, textStatus) {
				if (textStatus == "success") {
					var jsonObj = $.parseJSON(data);
					var success = jsonObj["success"];
					var returnValue2PageType = jsonObj["returnValue2PageType"];
					var values = jsonObj["values"];
					if (success == true) {
						$.messager.alert('提示', '恭喜,保存成功!', 'info');
						loadAlarmFacts();
						$("#popOptAlarm").window('close');
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
							$.messager.alert('提示','对不起,保存失败,没有相关任务信息!','error');
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

	//处理取消按钮
	function cancel() {
		$("#popOptAlarm").window('close');
	}

	$(function() {
		initAlarmTypeListMap();
		loadServiceIdList(true);
		$.messager.defaults = {
			ok : "确定",
			cancel : "取消"
		};
	});

	//加载业务列表选择框
	function loadServiceIdList(firstLoad) {
		$('#serviceNameList').combobox({
			url : "getVServiceIdNameList?containAllCol=true",
			valueField : "key",
			textField : "value",
			title : '业务类型',
			width : 270,
			editable : true,
			onLoadSuccess : function() {
				initServiceIdNameMap();
				$('#serviceNameList').combobox('select', '-1');
				// 初始加载页面，则加载业务列表后加载任务列表
				if (firstLoad) {
					loadTaskIdList(true);
				}
			}
		});
	}

	//初始化业务Id名字键值对
	var serviceIdName = {};
	function initServiceIdNameMap() {
		var datas = $('#serviceNameList').combobox("getData");
		for ( var index in datas) {
			serviceIdName[datas[index]['key']] = datas[index]['value'];
		}
	}

	//加载任务列表选择框
	function loadTaskIdList(firstLoad) {
		$('#taskNameList').combobox({
			url : "reqTaskIdJsonObject?containAllCol=true",
			valueField : "key",
			textField : "value",
			title : '任务名称',
			width : 220,
			editable : true,
			onLoadSuccess : function() {
				initTaskIdNameMap();
				//$('#taskNameList').combobox('select', '0');
				// 初始加载页面，则加载任务列表后加载告警信息表
				if (firstLoad) {
					loadAlarmFacts();
				}
			}
		});
	}

	//初始化任务Id名字键值对
	var taskIdName = {};
	function initTaskIdNameMap() {
		var datas = $('#taskNameList').combobox("getData");
		for ( var index in datas) {
			taskIdName[datas[index]['key']] = datas[index]['value'];
		}
	}

	//初始化告警类型键值对
	var alarmTypeList = {};
	function initAlarmTypeListMap() {
		var datas = $('#alarmType').combobox("getData");
		for ( var index in datas) {
			alarmTypeList[datas[index]['value']] = datas[index]['text'];
		}
	}

	function loadAlarmFacts() {
		//将要查询的值
		var serviceId = $("#serviceNameList").combobox('getValue');
		var grade = $("#alarmGrade").combobox('getValue');
		var status = $("#status").combobox('getValue');
		var type = $("#alarmType").combobox('getValue');
		var startTime = $("#startTime").val();
		var endTime = $("#endTime").val();
		$('#AlarmFactList').datagrid({
			onBeforeLoad: function(param)
			{
				if(!checkSessionValid())
				{
					return false;
				}
				else
				{
					return true;
				}
			},
							title : '告警列表',
							width : 1100,
							//height: 600,
							loadMsg : '数据加载中,请稍候...',
							nowrap : true,
							striped : true,
							singleSelect : true,
							collapsible : true,
							url : 'reqAlarmFacts',
							queryParams : {
								'serviceId' : serviceId == -1 ? "" : serviceId,
								'alarmGrade' : grade == 0 ? "" : grade,
								'status' : status == 0 ? "" : status,
								'alarmType' : type == 0 ? "" : type,
								'startTime' : startTime,
								'endTime' : endTime
							},
							sortName : 'alarmTime',
							sortOrder : 'desc',
							remoteSort : false,
							frozenColumns : [ [
									{
										title : '任务名称',
										field : 'taskId',
										align : 'center',
										width : 120,
										sortable : true,
										formatter : function(value, rec) {
											var taskName = taskIdName[value];
											if (null != taskName) {
												return taskName;
											} else {
												return value;
											}
										}
									},
									{
										title : '任务周期',
										field : 'cycleId',
										align : 'center',
										width : 100,
										sortable : true
									},
									{
										title : '告警时间',
										field : 'alarmTime',
										align : 'center',
										width : 130,
										sortable : true,
										formatter : function(value, rec) {
											return formatTime(value);
										}
									},
									{
										title : '告警类型',
										field : 'alarmType',
										align : 'center',
										width : 200,
										sortable : true,
										formatter : function(value, rec) {
											return alarmTypeList[value];
										}
									},
									{
										field : 'opt',
										title : '操作',
										width : 40,
										align : 'center',
										rowspan : 2,
										formatter : function(value, rec) {
											var taskId = rec['taskId'];
											var cycleId = rec['cycleId'];
											var alarmType = rec['alarmType'];
											var alarmTime = rec['alarmTime'];
											var optBtn = "";
											optBtn += '<A title="告警处理" style="FLOAT: left" class="l-btn l-btn-plain" onclick="editAlarm('
													+ "'"
													+ taskId
													+ "','"
													+ cycleId
													+ "','"
													+ alarmType
													+ "','"
													+ alarmTime
													+ "'"
													+ ')" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-edit">&nbsp;</SPAN></SPAN></SPAN></A>';
											return optBtn;
										}

									} ] ],
							columns : [ [

							{
								field : 'serviceId',
								title : '业务类型',
								width : 150,
								align : 'center',
								rowspan : 1,
								formatter : function(value, rec) {
									var serviceName = serviceIdName[value];
									if (null != serviceName) {
										return serviceName;
									} else {
										return value;
									}
								}
							}, {
								field : 'status',
								title : '告警状态',
								width : 70,
								align : 'center',
								rowspan : 1,
								formatter : function(value, rec) {
									if (1 == value) {
										return "待处理";
									} else if (2 == value) {
										return "处理中";
									} else if (3 == value) {
										return "已处理";
									} else {
										return null;
									}
								}
							}, {
								field : 'instanceId',
								title : '实例ID',
								width : 150,
								align : 'center',
								rowspan : 1
							}, {
								field : 'alarmGrade',
								title : '告警级别',
								width : 60,
								align : 'center',
								rowspan : 1,
								formatter : function(value, rec) {
									if (1 == value) {
										return "一般";
									} else if (2 == value) {
										return "严重";
									} else {
										return null;
									}
								}
							}, {
								field : 'operatorName',
								title : '最近处理人',
								width : 150,
								align : 'center',
								rowspan : 1
							}, {
								field : 'processTime',
								title : '最近处理时间',
								width : 150,
								align : 'center',
								rowspan : 1,
								formatter : function(value, rec) {
									return formatTime(value);
								}
							}, {
								field : 'emailList',
								title : 'Email列表',
								width : 200,
								align : 'center',
								rowspan : 1
							}, {
								field : 'mobileList',
								title : '手机号码列表',
								width : 200,
								align : 'center',
								rowspan : 1
							}, {
								field : 'reason',
								title : '问题原因',
								width : 200,
								align : 'center',
								rowspan : 1
							}, {
								field : 'solution',
								title : '解决措施',
								width : 200,
								align : 'center',
								rowspan : 1
							} ] ],
							pageNumber:1,
							pagination : true,
							rownumbers : true,
							onDblClickRow : function(rowIndex, rowData) {
								showOptAlarm(rowData);
							}
						});
		var p = $('#AlarmFactList').datagrid('getPager');
		$(p).pagination({
			showPageList : true,
			beforePageText : '第',
			afterPageText : '页, 共{pages}页',
			displayMsg : '当前显示从{from}到{to}, 共{total}记录',
			onBeforeRefresh : function(pageNumber, pageSize) {
				$(this).pagination('loading');
			}
		});
	}
</script>

</head>

<body>
	<div
		style="margin: 5px 0px 5px 0px; margin-left: auto; margin-right: auto; background: #fff; border: 1px solid #ccc; width: 1100px;">
		<table width="900" style="margin-left: auto; margin-right: auto;">
			<tr>
				<td><span>业务类型: </span></td>
				<td><input class="easyui-combobox" id="serviceNameList"
					style="width: 270px" panelHeight="200px" editable="false" /></td>
				<td><span>告警级别: </span></td>
				<td><select id="alarmGrade" class="easyui-combobox"
					style="width: 140px" panelHeight="auto" editable="false">
						<option value="0">全部</option>
						<option value="1">一般</option>
						<option value="2">严重</option>
				</select>
				</td>
				<td><span>起始时间: </span></td>
				<td><input id="startTime" readonly="true" type="text"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					class="Wdate" /></td>
				<td rowspan="2" align="center" valign="center"><a
					class="easyui-linkbutton" plain="false" iconCls="icon-search"
					href="javascript:void(0)" onclick="loadAlarmFacts()">查询 </a>
				</td>
			</tr>
			<tr>
				<td><span>告警类型: </span></td>
				<td><select id="alarmType" class="easyui-combobox"
					style="width: 270px" panelHeight="auto" editable="false">
						<option value="0">全部</option>
						<option value="1">任务失败（严重）</option>
						<option value="3">任务执行时间超时（严重）</option>
						<option value="8">文件未到达就执行批次任务（严重）</option>
						<option value="4">任务到达最迟启动时间时仍未启动（严重）</option>
						<option value="5">任务到达最迟结束时间时仍未结束（严重）</option>
						<option value="9">步骤执行超时反馈成任务周期超时（严重）</option>
						<option value="2">任务发生重做 (一般)</option>
						<option value="7">Hadoop资源不足(一般)</option>
						<option value="6">忽略依赖任务（弱依赖）的错误后启动任务(一般)</option>
				</select></td>
				<td><span>告警状态:</span></td>
				<td><select id="status" class="easyui-combobox"
					style="width: 140px" panelHeight="auto" editable="false">
						<option value="0">全部</option>
						<option value="1">待处理</option>
						<option value="2">处理中</option>
						<option value="3">已处理</option>
				</select>
				</td>
				<td><span>结束时间: </span></td>
				<td><input id="endTime" readonly="true" type="text"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
					class="Wdate" /></td>
			</tr>
		</table>
		<div style="display: none">
			<input class="easyui-combobox" id="taskNameList" panelHeight="200px"
				editable="false" />
		</div>
	</div>
	<table id="AlarmFactList"></table>
	<div id="popOptAlarm" class="easyui-window" inline="false"
		closed="true" style="width: 920px; height: 400px; padding: 10px">
		<div id="hideOptAlarm" class="easyui-layout" fit="true"
			style="display: none">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<form id="tccAlarmForm" name="tccAlarmForm" method="post">
					<div style="width: 850px;margin-left: 10px;"><table id="alarmInfo"></table></div>
					<table id="OptAlarmTable" cellpadding="20px" cellspacing="15px"
						style="margin-left: 8px;">
						<tr>
							<td><span>问题原因: </span>
							</td>
							<td><span>解决措施: </span>
							</td>
						</tr>
						<tr>
							<td><textarea id="reason" maxlength="2048" rows="5"
									style="width: 350px"></textarea>
							</td>
							<td><textarea id="solution" maxlength="2048" rows="5"
									style="width: 350px"></textarea>
							</td>
						</tr>
						<tr>
							<td><span>告警状态: </span>
							</td>
						</tr>
						<tr>
							<td><select id="statusOpt" class="easyui-combobox"
								style="width: 140px;" panelHeight="auto" editable="false" >
									<option value="1">待处理</option>
									<option value="2">处理中</option>
									<option value="3">已处理</option>
							</select>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div region="south" border="false"
				style="text-align: right; padding: 5px 0; height: 40px;">
				<a class="easyui-linkbutton" plain="true" iconCls="icon-ok"
					href="javascript:void(0)" onclick="saveAlarm()">确定 </a> <a
					class="easyui-linkbutton" plain="true" iconCls="icon-cancel"
					href="javascript:void(0)" onclick="cancel()">取消 </a>
			</div>
		</div>
	</div>
</body>

</html>