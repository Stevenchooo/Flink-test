<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.huawei.platform.tcc.utils.TccUtil"%>
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
<title><s:text name="新建任务步骤页面" />
</title>
<%
    String taskId = TccUtil.covValidTaskId(request.getParameter("taskId"));
    String stepId = TccUtil.covValidStepId(request.getParameter("stepId"));
%>
<script type="text/javascript">
	//是否修改或者新增
	var isAdd = true;
	function showOptStep(isReqAdd,stepId,rowData) {
		isAdd = isReqAdd;
		$("#stepReqAdd").val(isAdd);
		$("#hideOptStep").attr("style","display:block");
		if (isReqAdd) {
			clearStepPage();
			$("#popOptStep").window({
				modal : true,
				shadow : false,
				closed : false,
				title : "新增步骤"
			});
		} else {
			$("#stepId").val(stepId);
			setData2StepPage(rowData);
			$("#popOptStep").window({
				modal : true,
				shadow : false,
				closed : false,
				title : "修改步骤"
			});
		}
	}
	
	//编辑步骤页面
	function editStep(stepId)
	{
		var datas = $('#stepList').datagrid('getData');
		for(var index in datas["rows"])
		{
			if(datas["rows"][index]["stepId"]==stepId)
			{
				showOptStep(false,stepId,datas["rows"][index]);
				break;
			}
		}
	}
	
	function clearStepPage()
	{
		$("#stepName").val(null);
		$("#stepDesc").val(null);
		$('#stepTaskType').combobox("select",1);
		$("#allowRetryCount").val("3");
		$('input:hidden[name="taskStep.allowRetryCount"]').val("3");
		$("#timeoutMinutes").val("30");
		$('input:hidden[name="taskStep.timeoutMinutes"]').val("30");
		$("#command").val(null);
		//$("#stepEnableFlag").attr("checked",false);
		$("#stepId").val(null);
	}
	
	//在弹出的页面上填充好数据
	function setData2StepPage(rowData)
	{
		$("#taskId").val(rowData["taskId"]);
		$("#stepName").val(rowData["stepName"]);
		$("#stepDesc").val(rowData["stepDesc"]);
		$('#stepTaskType').combobox("select",rowData["stepTaskType"]);
		$("#allowRetryCount").val(rowData["allowRetryCount"]);
		$('input:hidden[name="taskStep.allowRetryCount"]').val(rowData["allowRetryCount"]);
		$("#timeoutMinutes").val(rowData["timeoutMinutes"]);
		$('input:hidden[name="taskStep.timeoutMinutes"]').val(rowData["timeoutMinutes"]);
		$("#command").val(rowData["command"]);
		$("#stepEnableFlag").attr("checked",rowData["stepEnableFlag"]);
		$("#stepId").val(rowData["stepId"]);
	}
	
	//在弹出的页面上填充好数据
	function setStepPage2Data(rowData)
	{
		rowData["taskId"] = $("#taskId").val();
		rowData["stepName"] = $("#stepName").val();
		rowData["stepDesc"] = $("#stepDesc").val();
		rowData["stepTaskType"] = $('#stepTaskType').combobox("getValue");
		rowData["allowRetryCount"] = $("#allowRetryCount").val();
		rowData["timeoutMinutes"] = $("#timeoutMinutes").val();
		rowData["command"] = $("#command").val();
		rowData["stepEnableFlag"] = $("#stepEnableFlag").attr("checked");
		rowData["stepId"] = $("#stepId").val();
	}
	
	function grabTaskName(taskId)
	{
		var isSucessed = false;
		var taskName = '';
		checkSessionValid();
	$.ajax({
			type:"post",
			url:"grabTaskName?taskId="+taskId,
			async : false,
			success : function(data, textStatus) {
				if (textStatus == "success") {
					data = data.split("|");
					if (data[0] == "true") {
						isSucessed = true;
						taskName = data[1];
					} else {
						isSucessed = false;
					}
				}
			}
		});
		return taskName;
	}
	
	//删除步骤信息
	function deleteStep()
	{
		var rows = $('#stepList').datagrid('getSelections');
		if (rows && rows.length > 0)
		{
			$.messager.defaults = {
					ok : "是",
					cancel : "否"
				};
			$.messager.confirm('警告', "你确定要删除这"+rows.length+"行吗?", function(r){
				if (r){
					var allSucessed = true;
					while(rows && rows.length > 0)
					{
						var taskId = rows[0]["taskId"];
						var stepId = rows[0]["stepId"];
						var stepName = rows[0]["stepName"];
						if(!deleteBackStageStep(taskId,stepId,stepName))
						{
							allSucessed = false;
							break;
						}
						else
						{
							var index = $('#stepList').datagrid('getRowIndex', rows[0]);
							$('#stepList').datagrid('deleteRow', index);
							rows = $('#stepList').datagrid('getSelections');
						}	
					}
					
					if(allSucessed == true)
					{
						$.messager.alert('提示','删除成功!','info');
					}
					else
					{
						//$.messager.alert('提示','删除失败!','error');
					}
				}
				});
		}
		else
		{
			$.messager.alert('提示','请至少选择一行记录!','info');
		}
	}
	
	//向上或向下交换一行
	function toTopBottom(isTop)
	{
		var rows = $('#stepList').datagrid('getSelections');
		if (rows && rows.length > 0)
		{
			if(rows.length == 1)
			{
				var taskId = rows[0]["taskId"];
				var index = $('#stepList').datagrid('getRowIndex', rows[0]);
				
				//向上移动
				if(isTop)
				{
					if(index == 0)
					{
						$.messager.alert('提示','对不起,已经是第一步!','info');
					}
					else
					{
						var allSucessed = true;
						var datas = $('#stepList').datagrid('getData');
						while(index >0)
						{
							var stepIdOne = datas["rows"][index]["stepId"];
							var stepIdTwo = datas["rows"][index-1]["stepId"];
							if(exchangeTaskStep(taskId,stepIdOne,stepIdTwo))
							{
								datas["rows"][index-1]["stepId"]=stepIdOne;
								datas["rows"][index]["stepId"]=stepIdTwo;
								
								var rowTemp = datas["rows"][index];
								datas["rows"][index] = datas["rows"][index-1];
								datas["rows"][index-1] = rowTemp;
								
								$('#stepList').datagrid('refreshRow',index-1);
								$('#stepList').datagrid('refreshRow',index);
								//alert("恭喜,上移成功!");
								index = index -1;
							}
							else
							{
								allSucessed = false;
								break;
							}
						}
						
						if(allSucessed)
						{
							$('#stepList').datagrid('clearSelections');
							$('#stepList').datagrid('selectRow',0);
						}
						else
						{
							//$.messager.alert('提示','对不起,上移失败!','error');
						}
					}
				}
				else
				{
					var rows = $('#stepList').datagrid('getRows');
					var rowsNum = rows.length;
					if(index == rowsNum-1)
					{
						$.messager.alert('提示','对不起,已经是最后一步!','info');
					}
					else
					{
						var allSucessed = true;
						var datas = $('#stepList').datagrid('getData');
						while(index < rowsNum-1)
						{
							var stepIdOne = datas["rows"][index]["stepId"];
							var stepIdTwo = datas["rows"][index+1]["stepId"];
							if(exchangeTaskStep(taskId,stepIdOne,stepIdTwo))
							{
								datas["rows"][index+1]["stepId"]=stepIdOne;
								datas["rows"][index]["stepId"]=stepIdTwo;
								
								var rowTemp = datas["rows"][index];
								datas["rows"][index] = datas["rows"][index+1];
								datas["rows"][index+1] = rowTemp;
								$('#stepList').datagrid('refreshRow',index+1);
								$('#stepList').datagrid('refreshRow',index);
								$('#stepList').datagrid('clearSelections');
								$('#stepList').datagrid('selectRow',index+1);
								//alert("恭喜,下移成功!");
								index = index + 1;
							}
							else
							{
								allSucessed = false;
								break;
							}
						}
						
						if(allSucessed)
						{
							$('#stepList').datagrid('clearSelections');
							$('#stepList').datagrid('selectRow',rowsNum-1);
						}
						else
						{
							//$.messager.alert('提示','对不起,下移失败!','error');
						}
					}
				}
			}
			else
			{
				$.messager.alert('提示','请仅选择一行记录!','info');
				$('#stepList').datagrid('clearSelections');
			}
		}
		else
		{
			$.messager.alert('提示','请选择一行记录!','info');
		}
	}
	
	//向上或向下交换一行
	function upDown(isUp)
	{
		var rows = $('#stepList').datagrid('getSelections');
		if (rows && rows.length > 0)
		{
			if(rows.length == 1)
			{
				var taskId = rows[0]["taskId"];
				var stepIdOne = rows[0]["stepId"];
				var index = $('#stepList').datagrid('getRowIndex', rows[0]);
				
				//向上移动
				if(isUp)
				{
					if(index == 0)
					{
						$.messager.alert('提示','对不起,已经是第一步!','info');
					}
					else
					{
						var datas = $('#stepList').datagrid('getData');
						var stepIdTwo = datas["rows"][index-1]["stepId"];
						if(exchangeTaskStep(taskId,stepIdOne,stepIdTwo))
						{
							datas["rows"][index-1]["stepId"]=stepIdOne;
							datas["rows"][index]["stepId"]=stepIdTwo;
							
							var rowTemp = datas["rows"][index];
							datas["rows"][index] = datas["rows"][index-1];
							datas["rows"][index-1] = rowTemp;
							
							$('#stepList').datagrid('refreshRow',index-1);
							$('#stepList').datagrid('refreshRow',index);
							$('#stepList').datagrid('clearSelections');
							$('#stepList').datagrid('selectRow',index-1);
							//alert("恭喜,上移成功!");
						}
						else
						{
							//$.messager.alert('提示','对不起,上移失败!','error');
						}
					}
				}
				else
				{
					var rows = $('#stepList').datagrid('getRows');
					var rowsNum = rows.length;
					if(index == rowsNum-1)
					{
						$.messager.alert('提示','对不起,已经是最后一步!','info');
					}
					else
					{
						var datas = $('#stepList').datagrid('getData');
						var stepIdTwo = datas["rows"][index+1]["stepId"];
						if(exchangeTaskStep(taskId,stepIdOne,stepIdTwo))
						{
							datas["rows"][index+1]["stepId"]=stepIdOne;
							datas["rows"][index]["stepId"]=stepIdTwo;
							
							var rowTemp = datas["rows"][index];
							datas["rows"][index] = datas["rows"][index+1];
							datas["rows"][index+1] = rowTemp;
							$('#stepList').datagrid('refreshRow',index+1);
							$('#stepList').datagrid('refreshRow',index);
							$('#stepList').datagrid('clearSelections');
							$('#stepList').datagrid('selectRow',index+1);
							//alert("恭喜,下移成功!");
						}
						else
						{
							//$.messager.alert('提示','对不起,下移失败!','error');
						}
					}
				}
			}
			else
			{
				$.messager.alert('提示','仅选择一行记录!','info');
				$('#stepList').datagrid('clearSelections');
			}
		}
		else
		{
			$.messager.alert('提示','请选择一行记录!','info');
		}
	}
	
	//交换stepIdOne与stepIdTwo的后台数据
	function exchangeTaskStep(taskId,stepIdOne,stepIdTwo)
	{
		var isSucessed = false;
		checkSessionValid();
	$.ajax({
			type:"post",
			url:"exchangeTaskStep",
			data : $.param({
				"taskStepExchange.taskId" : taskId,
				"taskStepExchange.stepIdOne" : stepIdOne,
				"taskStepExchange.stepIdTwo" : stepIdTwo
			}),
			async : false,
			success : function(data, textStatus) {
				if (textStatus == "success") {
					data = data.split(",");
					if (data[0] == "true") {
						isSucessed = true;
					} else {
						isSucessed = false;
						  if(alertPrivilegeNotEnouth(data, '交换步骤失败'))
						  {
							  return isSucessed;
						  }
						  else
						  {
							  $.messager.defaults={ok:"确定",cancel:"取消"};
							  $.messager.alert('提示','交换步骤失败!','error');
						  }
					}
				}
			}
		});
		return isSucessed;
	}
	
	//删除步骤后台数据
	function deleteBackStageStep(taskId,stepId,stepName)
    {
		var isSucessed = false;
    	checkSessionValid();
	$.ajax({
			type : "post",
			url : "deleteTaskStep",
			data : $.param({
				"taskStep.taskId" : taskId,
				"taskStep.stepId" : stepId
			}),
			async : false,
			success : function(data, textStatus) {
				if (textStatus == "success") {
					data = data.split(",");
					if (data[0] == "true") {
						isSucessed = true;
					} else {
						isSucessed = false;
						if(alertPrivilegeNotEnouth(data, '删除步骤<font color="red">'+stepName+'</font>失败!'))
						{
							return isSucessed;
						}
						else if(4  == returnValue2PageType)
						{
							$.messager.defaults={ok:"确定",cancel:"取消"};
							$.messager.alert('提示','对不起,保存失败,没有相关任务信息，请先创建任务!','error');
						}
						else
						{
					   		$.messager.defaults={ok:"确定",cancel:"取消"};
							$.messager.alert('提示','删除步骤<font color="red">'+stepName+'</font>失败!','error');
						}	  
					}
				}
			}
		});
    	return isSucessed;
    }

	//前台同步提交数据
	// 新增加的ajax同步请求
	function saveStep() {
		if(vaildData())
		{
			checkSessionValid();
	$.ajax({
				type : "post",
				url : "saveTccStep",
				data : $("#tccStepForm").serialize(),
				async : false,
				success : function(data, textStatus) {
					
					if (textStatus == "success") {
						data = data.split(",");
						if (data[0] == "true") {
							$.messager.alert('提示','恭喜,保存成功!','info');
							if(isAdd == true)
							{
								loadSteps();
							}
							else
							{
								var stepId = $("#stepId").val();
								var datas = $('#stepList').datagrid('getData');
								for(var index in datas["rows"])
								{
									if(datas["rows"][index]["stepId"]==stepId)
									{
										setStepPage2Data(datas["rows"][index]);
										$('#stepList').datagrid('refreshRow',index);
										break;
									}
								}
							}
							$("#popOptStep").window('close');
						} else {
							
							if(alertPrivilegeNotEnouth(data, '保存失败'))
							{
							   return;
							}
							
							if(data.length >=2)
							  {
								  if('0' == data[1])
								  {
									  $.messager.alert('提示','对不起,保存失败!','info');
								  }
								  else if('4' == data[1])
								  {
									  $.messager.alert('提示','对不起,保存失败,没有相关任务信息，请先创建任务!','info');
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
	
	 //改变步骤启用状态
	 function changeStepState(taskId,stepId,value,stepName)
	    {
		    var isSucessed = false;
	    	checkSessionValid();
	$.ajax({
				type : "post",
				url : "changeStepState",
				data : $.param({
					"taskStep.taskId" : taskId,
					"taskStep.stepId" : stepId,
					"taskStep.stepEnableFlag" : value
				}),
				async : false,
				success : function(data, textStatus) {
					if (textStatus == "success") {
						data = data.split(",");
						if (data[0] == "true") {
							
							//修改本地数据
							{
								var datas = $('#stepList').datagrid('getData');
								for(var index in datas["rows"])
								{
									if(datas["rows"][index]["stepId"]==stepId)
									{
										datas["rows"][index]["stepEnableFlag"] = value;
										$('#stepList').datagrid('refreshRow',index);
										isSucessed = true;
										break;
									}
								}
							}
						} else {
							  isSucessed = false;
							  var opt = "启动";
				        	  if('0'==value)
				        	  {
				        		  opt = '停止';
				        	  }
				        	  
				        	  if(alertPrivilegeNotEnouth(data, opt+'步骤<font color="red">'+stepName+'</font>失败'))
							  {
								  return;
							  }
				        	  else
				        	  {
				        		  $.messager.alert('提示',opt+'步骤<font color="red">'+stepName+'</font>失败','error');
				        	  }
							  
						}
					}
				}
			});
	    	return isSucessed;
	    }

	//处理取消按钮
	function cancel() {
		$("#popOptStep").window('close');
	}

    function getTitle(stepTaskType)
    {
    	var title;
    	switch (parseInt(stepTaskType)) {
    	case 1:
			title = "ODS";
			break;
		case 2:
			title = "DW";
			break;
		case 3:
			title = "DIM";
			break;
		case 4:
			title = "混合型";
			break;
		case 5:
			title = "导mysql";
			break;
		case 6:
			title = "导文件";
			break;
		case 7:
			title = "发送邮件";
			break;
		case 8:
			title = "数据推送";
			break;
		default:
			title = "ODS";
			break;
		}
    	return title;
    }
    
    function showState(stepEnableFlag)
    {
    	if(stepEnableFlag)
    	{
    		return "<img src='themes/icons/enablestate.png'/>";
    	}
    	else
    	{
    		return "<img src='themes/icons/disablestate.png'/>";
    	}
    }
    
	$(function() {
		loadSteps();
		$.messager.defaults={ok:"确定",cancel:"取消"};
	});
	
	$.extend($.fn.validatebox.defaults.rules, {
		ipAddr: {
		validator: function(value, param){
			return vaildIpAddr(value);
		},
		message: '请输入正确的ip地址!'
		}
		});
	
	function vaildData()
	{
		if(isEmpty($("#taskId").val())
		   || isEmpty($("#allowRetryCount").val())
		   || isEmpty($("#timeoutMinutes").val())
		   || isEmpty($("#command").val())
		   || isEmpty($("#stepName").val())
		   )
		{
			$.messager.alert('提示','*标识的字段不能为空白字符串!','info');
			return false;
		}
		else
		{
			return true;
		}
	}
	
	//任务名称称
	var taskName = '';
	function setTaskName()
	{
		var taskId = "<%=taskId%>";
		if('' == taskName)
		{
			taskName = grabTaskName(taskId);
		}
	}
	
	function loadSteps()
	{
		setTaskName();
		var stepId = '<%=stepId%>';
		if(null != stepId && "" != stepId)
		{
			stepId = '&stepId='+stepId;
		}
		else
		{
			stepId = '';
		}
		
		$('#stepList').datagrid({
			onBeforeLoad: function(param)			{				if(!checkSessionValid())				{					return false;				}				else				{					return true;				}			},
			title : '任务步骤列表[<font color="red">'+taskName+'</font>]',
			width : 1000,
			//height: 600,
			loadMsg:'数据加载中,请稍候...',
			nowrap : false,
			striped : true,
			singleSelect:false,
			collapsible : true,
			url : 'getTccStepList?taskId=<%=taskId%>'+stepId,
			sortName : '步骤Id',
			sortOrder : 'stepId',
			remoteSort : false,
			idField : 'stepId',
			frozenColumns : [ [ {
				field : 'ck',
				checkbox : true
			}, {
				title : '步骤ID',
				field : 'stepId',
				align : 'center',
				width : 45,
				sortable : true
			} ,				{
				field : 'opt',
				title : '操作',
				width : 65,
				align : 'center',
				rowspan : 2,
				formatter : function(value, rec) {
					var stepId = rec["stepId"];
					var stepName = rec["stepName"];
					var optBtn = "";
					stepName ="'"+stepName+"'";
					if(rec["stepEnableFlag"])
					{
						optBtn = '<A title="停止任务步骤" style="FLOAT: left" class="l-btn l-btn-plain" onclick="changeStepState(<%=taskId%>,'+stepId+',false,'+stepName+')" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-disable">&nbsp;</SPAN></SPAN></SPAN></A>';
					}
					else
					{
						optBtn = '<A title="启动任务步骤" style="FLOAT: left" class="l-btn l-btn-plain"  onclick="changeStepState(<%=taskId%>,'+stepId+',true,'+stepName+')" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-enable">&nbsp;</SPAN></SPAN></SPAN></A>';
					}
					
					optBtn +='<A title="编辑任务步骤" style="FLOAT: left" class="l-btn l-btn-plain" onclick="editStep('+stepId+')" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-edit">&nbsp;</SPAN></SPAN></SPAN></A>';
					return optBtn;
				}
				
			}  ] ],
			columns : [ [
			             {
				title : '基本信息',
				colspan : 9
			}], [
					
					{
						field : 'stepEnableFlag',
						title : '启用标志',
						width : 60,
						rowspan : 1,
						align : 'center',
						formatter : function(value, rec) {
							return showState(value);
						}
					} ,
            {
				field : 'stepName',
				title : '步骤名称',
				width : 150,
				align : 'center',
				rowspan : 1
			}
			,
			{
				field : 'allowRetryCount',
				title : '重试次数',
				align : 'center',
				width : 60,
				rowspan : 1
			} 
			,
			{
				field : 'timeoutMinutes',
				title : '超时时间(分钟)',
				align : 'center',
				width : 100,
				rowspan : 1
			}
			, 			     {
				field : 'taskId',
				title : '任务名称',
				align : 'center',
				rowspan : 1,
				width : 140,
				formatter : function(value, rec) {
					setTaskName();
					return taskName;
				}
			},
			{
				field : 'command',
				title : '命令',
				align : 'center',
				width : 200,
				rowspan : 1
			} 
			//, {
			//	field : 'stepDesc',
			//	title : '步骤描述',
			//	width : 60,
			//	rowspan : 1
			//}
			] ],
			pagination : false,
			rownumbers : true,
			toolbar : [ {
				id : 'btnadd',
				text : '新增',
				iconCls : 'icon-add',
				handler : function() {
					showOptStep(true);
				}
			}
			,'-', {
				id : 'btnup',
				text : '上移',
				iconCls : 'icon-up',
				handler : function() {
					upDown(true);
				}
			}
			, {
				id : 'btntop',
				text : '置顶',
				iconCls : 'icon-top',
				handler : function() {
					toTopBottom(true);
				}
			}
			, {
				id : 'btndown',
				text : '下移',
				iconCls : 'icon-down',
				handler : function() {
					upDown(false);
				}
			}
			, {
				id : 'btnbottom',
				text : '置底',
				iconCls : 'icon-bottom',
				handler : function() {
					toTopBottom(false);
				}
			}
			, '-', {
				id : 'btndel',
				text : '删除',
				iconCls : 'icon-no',
				handler : function() {
					deleteStep();
				}
			}]
			,
			onDblClickRow:function(rowIndex, rowData)
			{
				showOptStep(false,rowData["stepId"],rowData);
			},
			onLoadSuccess: function()
			{
				setTaskName();
				$('#taskName').val(taskName);
			}
		});
		$('#stepList').datagrid("unselectAll");
	}
</script>

</head>

<body>
	<div
		style="margin: 5px 0px 5px 0px; margin-left: auto; margin-right: auto; width: 1000px;">
		<table id="stepList"></table>
	</div>
	<div id="popOptStep" class="easyui-window" inline="false" closed="true"
		style="width: 600px; height: 430px; padding: 10px">
		<div id="hideOptStep" class="easyui-layout" fit="true"
			style="display: none">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<form id="tccStepForm" name="tccStepForm" action="saveTccStep"
					method="post">
					<table id="OptStepTable" cellpadding="10px" cellspacing="5px"
						style="margin-left: auto; margin-right: auto;">
						<tr>
							<td><span>任务名称: </span>
							</td>
							<td><input id="taskName" style="width: 350px"
								readonly="true" /><font color="red">*</font> <input id="taskId"
								type="hidden" name="taskStep.taskId"
								value="<%=taskId%>" />
							</td>
						</tr>
						<tr>
							<td><span>步骤名: </span>
							</td>
							<td><textarea id="stepName" type="text" maxlength="256"
									name="taskStep.stepName" style="width: 350px" rows="2"></textarea><font
								color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>步骤描述: </span>
							</td>
							<td><textarea id="stepDesc" maxlength="2048"
									name="taskStep.stepDesc" rows="3" style="width: 350px"></textarea>
							</td>
						</tr>
						<tr>
							<td><span>步骤任务类型: </span>
							</td>
							<td><select id="stepTaskType" class="easyui-combobox"
								name="taskStep.stepTaskType" style="width: 120px"
								required="true">
									<option value="1" selected="selected">ODS</option>
									<option value="2">DW</option>
									<option value="3">DIM</option>
									<option value="4">混合型</option>
									<option value="5">导mysql</option>
									<option value="6">导文件</option>
									<option value="7">发送邮件</option>
									<option value="8">数据推送</option>
							</select>
							</td>
						</tr>
						<tr>
							<td><span>失败重试次数: </span>
							</td>
							<td><input id="allowRetryCount"
								name="taskStep.allowRetryCount" value="3"
								class="easyui-numberspinner" increment="1" min="1" max="20"
								precision="0" style="width: 120px" /><font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>超时时间(分钟): </span>
							</td>
							<td><input id="timeoutMinutes"
								name="taskStep.timeoutMinutes" value="30"
								class="easyui-numberspinner" increment="10" min="0" max="525600"
								precision="0" style="width: 120px" /><font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>执行命令: </span>
							</td>
							<td><textarea id="command" type="text" maxlength="8096"
									name="taskStep.command" rows="3" style="width: 350px"></textarea><font
								color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>是否启用: </span>
							</td>
							<td><input id="stepEnableFlag" type="checkbox"
								name="taskStep.stepEnableFlag" value="true" Checked="checked" />
							</td>
						</tr>
					</table>
					<input type="hidden" id="stepId" name="taskStep.stepId" /> <input
						type="hidden" id="stepReqAdd" name="stepReqAdd" />
				</form>
			</div>
			<div region="south" border="false"
				style="text-align: right; padding: 5px 0; height: 40px;">
				<a class="easyui-linkbutton" plain="true" iconCls="icon-ok"
					href="javascript:void(0)" onclick="saveStep()">保存 </a> <a
					class="easyui-linkbutton" plain="true" iconCls="icon-cancel"
					href="javascript:void(0)" onclick="cancel()">取消 </a>
			</div>
		</div>
	</div>
</body>

</html>