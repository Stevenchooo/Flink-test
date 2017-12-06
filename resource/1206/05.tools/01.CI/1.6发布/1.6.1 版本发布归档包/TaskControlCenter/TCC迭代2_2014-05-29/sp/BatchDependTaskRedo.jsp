<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.huawei.platform.tcc.utils.TccUtil"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

<%
   String taskId=TccUtil.covValidTaskId(request.getParameter("taskId"));
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery-1.7.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="stylesheet" type="text/css" href="css/table.css" />
<link rel="stylesheet" type="text/css" href="themes/icon.css" />
<link rel="stylesheet" type="text/css" href="themes/default/easyui.css"/>
<script language="javascript" type="text/javascript"
	src="DatePicker/WdatePicker.js"></script>
<title><s:text name="任务批量重做" />
</title>
<script language="javascript" type="text/javascript" charset="utf-8">
var systemAdmin = false;
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
	
	$('#taskRedoTabs').tabs({
		onSelect : function(title) {
			changeTabShow();
		}
	});
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

//加载任务Id列表选择框
function loadTaskGroupList(choosedServiceId, serviceId, taskGroup)
{
	var url= "getOptServiceTGNameList?containAllCol=false";
	if(null != choosedServiceId)
	{
		url +="&choosedServiceId="+choosedServiceId;
	}
	if(null != serviceId)
	{
		url +="&serviceId="+serviceId;
	}
	if(null != taskGroup)
	{
		url +="&taskGroup="+encodeURI(taskGroup);
	}
	url = encodeURI(url);
	$('#taskGroupList').combobox({
		url : url,
		valueField : "key",
		textField : "value",
		title:'任务组',
		width: 220,
		editable : false,
		onLoadSuccess:function()
		{
			//如果选择了一行,优先设置选择的值
			var rows = $('#taskGroupList').combobox("getData");
			if(rows.length > 0)
			{
				if(null != serviceId && serviceId == choosedServiceId)
				{
					$('#taskGroupList').combobox("select",taskGroup);
				}
				else
				{
				$('#taskGroupList').combobox("select",rows[0]["key"]);
				}
			}
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
	});}

//加载任务Id列表选择框
function loadServiceIdS()
{
	var url= "getVServiceIdNameList?containAllCol=false";
	$('#serviceIdS').combobox({
		url : url,
		valueField : "key",
		textField : "value",
		title:'业务',
		width: 140,
		editable : false,
		multiple : true,
		onLoadSuccess:function()
		{
			initComboboxes();
		}
	});
}

function loadOSUserS()
{
    $('#osUserS').combobox({
			url : "getVisibleOsUsers?containAllCol=false",
			valueField : "key",
			textField : "value",
			title : 'OS用户',
			width : 140,
			editable : false,
			multiple : true,
			onLoadSuccess:function()
			{
				initComboboxes();
			}
	});
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

function loadDeployedNodeList()
{
   $('#deployedNodeList').combobox({
		url : "reqNodes",
		valueField : "key",
		textField : "value",
		width: 150
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
	else
	{
		return;
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
				if('' != showValue)
				{
					showValue = showValue.substring(0, showValue.length-1);
				}
				$('#taskIdList').combobox("setValue",showValue);
				
			}
			else
			{
				$('#taskIdList').combobox("select",0);
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
		$("#taskIdList").combobox('setValue','0');
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
	//$("#serviceId").val(null);
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

var choosedTasks = {};
var choosedNeedDependTasks = {};
var removedTasks = {};
function chooseTasks()
{
	var rows = $('#taskList').datagrid('getSelections');
	if (rows && rows.length > 0)
	{
		
		for(var index in rows)
		{
			var taskId = rows[index]["taskId"];
			choosedTasks[taskId]=taskId;
			//取消原来移除的任务
			removedTasks[taskId]=null;
		}
		
		$.messager.defaults={ok:"是",cancel:"否"};
		$.messager.confirm('确认', "选择的任务已经全部添加成功!继续选择任务(是),跳到待处理任务集合(否).", function(r){
				if(r)
				{
					$('#taskList').datagrid('clearSelections');
				}
				else
				{
					selectTab('c');
				}
			});
	}
	else
	{
		$.messager.alert('提示','请至少选择一行记录!','info');
	}
}

//获取任务依赖树的所有任务集合
function grabTaskDependedIds(taskIds)
{
	var rtnData = null;
	prefix = 's';
	var dataUrl = "grabTaskDependedIds?taskIds=" + taskIds;
    //异步获取集成的任务相关信息字段
	checkSessionValid();
	$.ajax({
		type:"post",
		url:dataUrl,
		async : false,
		success : function(data, textStatus) {
			if (textStatus == "success") {
				rtnData = data;
				data = data.split(",");
				if (data[0] == "true") {
					isSucessed = true;
				} else {
					isSucessed = false;
				}
			}
		}
	});
    return rtnData;
}

function chooseRevDepTasks()
{
	var isSucessed = false;
	var rows = $('#taskList').datagrid('getSelections');
	if (rows && rows.length > 0)
	{
		for(var index in rows)
		{
			var taskId = rows[index]["taskId"];
			choosedNeedDependTasks[taskId]=taskId;
			//取消原来移除的任务
			removedTasks[taskId]=null;
		}
		
		$.messager.defaults={ok:"是",cancel:"否"};
		$.messager.confirm('确认', "已选择任务的反向依赖任务树全部添加成功!继续选择任务(是),跳到待处理任务集合(否).", function(r){
				if(r)
				{
					$('#taskList').datagrid('clearSelections');
				}
				else
				{
					selectTab('c');
				}
			});
	}
	else
	{
		$.messager.alert('提示','请至少选择一行记录!','info');
	}
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
	checkSessionValid();
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

//转换到页面表格中显示
function getTaskType(taskType)
{
	var title;
	switch (parseInt(taskType)) {
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
			$.messager.alert('提示', "请修改任务依赖关系：任务<font style='font-weight:bold;' color='red'>"+$("#taskName").val()+"</font>不能依赖如下任务(没有查询权限)"+formatNoExistTaskIdNames(noExistTaskIdNames,';'),'info');
			return false;
		}
	}
    
	return true;	
}

function isResetTaskRS()
{
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
	   enableState();
	   //相关字段设置为可用
	   $('#cycleType').combobox('enable');
	   document.getElementById("dependTaskIdListDisplay").disabled = false;
	   $('#cycleLength').numberspinner('enable');
	   $('input:radio[id="cycleDependFlag"]').each(function(index,obj)
	   {
		  obj.disabled = false;
	   });
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
function changeTaskState(taskId,value,dataGridId)
{
	//停用时，为保证后台延时5s再使任务禁用，页面等待5s的处理时间
	if (value == 1)
	{
		 changeState(taskId,value,dataGridId);
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
				
				//符合条件
				if (count > 0)
				{
					changeState(taskId,value,dataGridId);
				}
				else
				{
					$.messager.defaults={ok:"是",cancel:"否"};
					$.messager.confirm('警告', "任务<font style='font-weight:bold;' color='red'>"+covTaskId2Name(taskId)+"</font>没有可用的任务步骤，编辑任务步骤（是）或者继续启用该任务（否）", function(r){
						if (r){
							nextStep(taskId);
						}
						else{
							changeState(taskId,value,dataGridId);
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

//改变任务启用状态taskList
function changeTaskStateWithOutWait(taskId,value,dataGridId)
{
	if (value == 1)
	{
		 changeState(taskId,value,dataGridId);
	}
	//启用时，去后台查看任务步骤是否允许任务启用条件
	else 
	{
		var count = getStepEnableCount(taskId);
		var datas = $('#choosedTaskList').datagrid('getData');
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
				
				//符合条件
				if (count > 0)
				{
					changeState(taskId,value,dataGridId);
				}
				else
				{
					$.messager.defaults={ok:"是",cancel:"否"};
					$.messager.confirm('警告', "任务<font style='font-weight:bold;' color='red'>"+covTaskId2Name(taskId)+"</font>没有可用的任务步骤，编辑任务步骤（是）或者继续启用该任务（否）", function(r){
						if (r){
							nextStep(taskId);
						}
						else{
							changeState(taskId,value,dataGridId);
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
	
	var rows = $('#choosedTaskList').datagrid('getSelections');
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
							changeTaskStateWithOutWait(taskId,value,'choosedTaskList');
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
function changeState(taskId,value,dataGridId)
{
	 var isSucessed = false;
	   	checkSessionValid();
	$.ajax({
				type : "post",
				url : "changeTaskState",
				data : $.param({
					"task.taskId" : taskId,
					"task.taskState" : value,
					"waitUpdateTime" : 10000
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
									var datas = $('#'+dataGridId).datagrid('getData');
									for(var index in datas["rows"])
									{
										if(datas["rows"][index]["taskId"]==taskId)
										{
											datas["rows"][index]["taskState"] = value;
											datas["rows"][index]["startOperator"] = startOperator;
											$('#'+dataGridId).datagrid('refreshRow',index);
											isSucessed = true;
											break;
										}
									}
								}
								else
								{
									//更新状态
									var datas = $('#'+dataGridId).datagrid('getData');
									for(var index in datas["rows"])
									{
										if(datas["rows"][index]["taskId"]==taskId)
										{
											datas["rows"][index]["taskState"] = value;
											$('#'+dataGridId).datagrid('refreshRow',index);
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
	else if(dateValue.indexOf(":") == -1)
	{
		return dateValue + " 00:00:00";
	}
	else
	{
		return dateValue;
	}
}
	//处理取消按钮
	function cancel() {
		$("#popOptTask").window('close');
	}

	function changeTabShow()
	{
		//当前选择tab才更新页面显示
		var tab = $('#taskRedoTabs').tabs('getSelected');
		if("源任务集合"==tab.panel('options').title)
		{
			loadTaskIdList(false);
		}
		else
		{
			loadServiceIdS();
			loadOSUserS();
			loadChoosedTaskList();
		}
	}
	
	function initComboboxes()
	{
		changeAll("cycleTypeS","cycleTypeSAll");
		changeAll("taskTypeS","taskTypeSAll");
		changeAll("osUserS","osUserSAll");
		changeAll("taskStateS","taskStateSAll");
		changeAll("redoTypeS","redoTypeSAll");
		changeAll("serviceIdS","serviceIdSAll");
	}
	
	function selectTab(prefix)
	{
		if('s' == prefix)
		{
			//当前选择tab才更新页面显示
			$('#taskRedoTabs').tabs('select',"源任务集合");
		}
		else
		{
			//当前选择tab才更新页面显示
			$('#taskRedoTabs').tabs('select',"待处理任务集合");
		}
	}
	
	function reChoose()
	{
		choosedTasks = {};
		choosedNeedDependTasks = {};
		removedTasks = {};
		selectTab('s');
	}
	
	function grabCanIntegratedTaskInfos()
	{
		prefix = 'c';
	    //异步获取集成的任务相关信息字段
		checkSessionValid();
		$.ajax({
			type:"post",
			url:"grabCanIntegratedTaskInfos?taskIds="+getDistinctTaskIds(prefix),
			async : false,
			success : function(data, textStatus) {
				if (textStatus == "success") {
					data = data.split("|");
					if (data[0] == "true") {
						isSucessed = true;
						//提示是否继续选择
						var taskInfos = data[1].split(";");
						var taskInfoArr;
						for(var index in taskInfos)
						{
							taskInfoArr = taskInfos[index].split(",");
							var taskInfo = {};
							if(taskInfoArr.length == 4)
							{
								taskInfo["taskId"] = taskInfoArr[0];
								taskInfo["cycleType"] = taskInfoArr[1];
								taskInfo["cycleLength"] = taskInfoArr[2];
								taskInfo["cycleDepend"] = taskInfoArr[3];
								//任务Id,周期类型,周期长度,是否顺序依赖
								canIntegratedTaskInfoArr[taskInfoArr[0]] = taskInfo;
							}
						}
					} else {
						isSucessed = false;
					}
				}
			}
		});
	}
	
	
	function showPopTaskInfos()
	{
		//获取选择的任务周期
		var taskCycles = getSelectedTaskCycles('c');
		if("" != taskCycles)
		{
			$("#hideOptITParams").attr("style","display:block");
			$("#popOptIntegratedTaskParams").window({
					modal : true,
					shadow : true,
					closed : false,
					title : "请检查要修改的任务信息"
				});
		}
		else
		{
			$.messager.defaults={ok:"确认",cancel:"取消"};
			$.messager.alert("提示","没有需要处理的任务周期,请重新选择!","info");
		}
	}
	
	//canIntegratedTaskInfoArr数组中的是否包含taskId
	function taskInfoArrContains(taskId)
	{
		if(canIntegratedTaskInfoArr[taskId] != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	function getServiceName(serviceID)
	{
		return serviceIdName[serviceID];
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
	$('#taskList').datagrid({
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
		title : '任务列表',
		width : 1000,
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
					optBtn = '<A title="启动任务" style="FLOAT: left" class="l-btn l-btn-plain" onclick="changeTaskState('+taskId+',0,'+"'taskList'"+')" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-enable">&nbsp;</SPAN></SPAN></SPAN></A>';
					
				}
				else
				{
					optBtn =  '<A title="停止任务" style="FLOAT: left" class="l-btn l-btn-plain" onclick="changeTaskState('+taskId+',1,'+"'taskList'"+')" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-disable">&nbsp;</SPAN></SPAN></SPAN></A>';
					
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
		   			id : 'btnChoose',
		   			text : '添加',
		   			iconCls : 'icon-choose-all',
		   			handler : function() {
		   				chooseTasks();
		   			}
		   		}
		   		,{
		   			id : 'btnChooseDeps',
		   			text : '添加反向依赖树',
		   			iconCls : 'icon-choose-all-tree',
		   			handler : function() {
		   				chooseRevDepTasks();
		   			}
		   		}
		]
		,
		onDblClickRow:function(rowIndex, rowData)
		{
			showOptTask(false,rowData["taskId"],rowData);
		}
	});
	$('#taskList').datagrid("unselectAll");
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
	
	function fillLeftZero(value,length)
	{
		var sValue = value.toString();
		var len = sValue.length;
		for(var i=0;i<length-len;i++)
		{
			sValue = '0' + sValue;
		}
		return sValue;
	}
	
	function formatDate(dateTime)
	{
		if(null != dateTime && "" != dateTime)
		{
			var year = dateTime.getFullYear();
			var mouth = dateTime.getMonth()+1;
			var day = dateTime.getDate();
			var hour = dateTime.getHours();
			var minute = dateTime.getMinutes();
			var second = dateTime.getSeconds();
			var dateStr = fillLeftZero(year,4) +"-"+ fillLeftZero(mouth,2) +"-"+ fillLeftZero(day,2);
			var timeStr = fillLeftZero(hour,2) + ":"+ fillLeftZero(minute,2) +":"+ fillLeftZero(second,2);
			return  dateStr + " " + timeStr;
		}
		else
		{
			return "";
		}
	}
	
	function changeAll(id,checkId)
	{
		var cchecked = $('#'+checkId).attr("checked");
		var checked = ("checked" == cchecked);
		
		var datas = $('#'+id).combobox("getData");
		
		var key;
		for(var index in datas)
		{
		   key = datas[index]["key"];
		   if(null == key)
		   {
		      key = datas[index]["value"];
		   }
		   if(true == checked)
		   {
		   	  	$('#'+id).combobox("select",key);
		   }
		   else
		   {
		   		$('#'+id).combobox("unselect",key);
		   }
		}
	}
	
	function reverseSelectTasks()
	{
		var datas = $('#choosedTaskList').datagrid('getSelections');
		var unSelectRowIndexs = {};
		for(var index in datas)
		{
		   var rowIndex = 	$('#choosedTaskList').datagrid('getRowIndex',datas[index]);
		   unSelectRowIndexs[rowIndex] = rowIndex;
		}
		
		$('#choosedTaskList').datagrid('selectAll');
		
		for(var index in unSelectRowIndexs)
		{
		   $('#choosedTaskList').datagrid('unselectRow',unSelectRowIndexs[index]);
		}
	}
	
	function selectTasks()
	{
		var datas = $('#choosedTaskList').datagrid('getData')["rows"];
		var cycleTypeS = $('#cycleTypeS').combobox("getValues");
		var taskTypeS = $('#taskTypeS').combobox("getValues");
		var osUserS = $('#osUserS').combobox("getValues");
		var taskStateS = $('#taskStateS').combobox("getValues");
		var redoTypeS = $('#redoTypeS').combobox("getValues");
		var serviceIdS = $('#serviceIdS').combobox("getValues");
		for(var index in datas)
		{
			if(false == contains(cycleTypeS,datas[index]["cycleType"].toUpperCase()))
			{
				$('#choosedTaskList').datagrid('unselectRow',index);
				continue;
			}
			
			if(false == contains(taskTypeS,datas[index]["taskType"]))
			{
				$('#choosedTaskList').datagrid('unselectRow',index);
				continue;
			}
			
			if(false == contains(osUserS,datas[index]["osUser"]))
			{
				$('#choosedTaskList').datagrid('unselectRow',index);
				continue;
			}
			
			if(false == contains(taskStateS,datas[index]["taskState"]))
			{
				$('#choosedTaskList').datagrid('unselectRow',index);
				continue;
			}
			
			if(false == contains(redoTypeS,datas[index]["redoType"]))
			{
				$('#choosedTaskList').datagrid('unselectRow',index);
				continue;
			}
			
			if(false == contains(serviceIdS,datas[index]["serviceId"]))
			{
				$('#choosedTaskList').datagrid('unselectRow',index);
				continue;
			}
			
			$('#choosedTaskList').datagrid('selectRow',index);
		}
	}
	
	function contains(datas,data)
	{
		for(var index in datas)
		{
		   if(datas[index] == data)
		   {
		      return true;
		   }
		}
		
		return false;
	}
	
	function batchRedoTasksInternal(filterNoPri)
	{
		if(true != filterNoPri)
		{
   			filterNoPri = false;
		}
			var taskInfos = "";
			//获取要修改的任务信息
			var datas = $('#choosedTaskList').datagrid('getData')["rows"];
			var num = 0;
			for(var index in datas)
			{
				if(null != datas[index])
				{
					if(0 == datas[index]["cycleDependFlag"] || false == datas[index]["cycleDependFlag"])
					{
						$.messager.alert('提示','请将所有的任务都修改为顺序依赖!','info');
						return;	
					}
					else if(0 == datas[index]["taskState"] || false== datas[index]["taskState"])
					{
						$.messager.alert('提示','请先停止所有的任务!','info');
						return;	
					}
					
					taskInfos += datas[index]["taskId"];
					taskInfos += ",";
					taskInfos += datas[index]["cycleDependFlag"];
					taskInfos += ",";
					taskInfos += datas[index]["redoStartTime"];
					taskInfos += ",";
					taskInfos += datas[index]["redoEndTime"];
					taskInfos += ",";
					taskInfos += datas[index]["redoType"];
					taskInfos += ",";
					taskInfos += datas[index]["redoDayLength"];
					taskInfos += ";";
					num++;
				}
			}
			
			//异步获取集成的任务相关信息字段
			checkSessionValid();
			$.ajax({
				type:"post",
				url:"batchRedoTasks",
				async : false,
				data : $.param({
						"taskInfos" : taskInfos,
						"filterNoPriTasks":filterNoPri
					}),
				success : function(data, textStatus) {
					if (textStatus == "success") {
						var jsonD = $.parseJSON(data);
							if (jsonD.success) {
								isSucessed = true;
								$.messager.alert('提示','提交<font color="red">批量重做任务</font>请求成功,请<font color="red">重新启动</font>所有任务以便开始执行!','info');
								
							}
							else
							{
								
								isSucessed = false;
								
								if(null != jsonD.extValue && 2 == jsonD.returnValue2PageType)
								{
									if(num == jsonD.extValue.length)
									{
										//没有任何权限
										$.messager.defaults={ok:"确定",cancel:"取消"};
										$.messager.alert('提示','提交<font color="red">批量重做任务</font>请求失败,原因:<font color="red">没有任务任务的操作权限</font>!','error');
										return isSucessed;
									}
									
									showTPConfirm("<font color='red'>没有如下任务的操作权限，请检查是否忽略对下面任务的处理?</font>",jsonD.extValue,batchRedoTasksInternal,true);
									return isSucessed;
								}
								else
								{
									$.messager.defaults={ok:"确定",cancel:"取消"};
									$.messager.alert('提示','提交<font color="red">批量重做任务</font>请求失败!','error');
								}
							}
					}
				}
			});	
	}
	
	//正在提交集成重做请求
	function submitRedoTasks()
	{
		$.messager.defaults={ok:"是",cancel:"否"};
		var tips = '你确定要批量重做这些任务吗?';
			$.messager.confirm('提示', tips, function(r)
			{
				if(r)
				{
					batchRedoTasksInternal(false);
				}
			});
	}
	
	function formatRedoType(value)
	{
		var redoTypeShow;
		switch (parseInt(value)) {
		case 2:
			redoTypeShow = '周末重做';
			break;
		case 3:
			redoTypeShow = '月末重做';
			break;
		default:
			redoTypeShow = '集成重做';
			break;
		}
		return redoTypeShow;
	}
	
	var preChoosedTasks = "~!@";
	var preChoosedTaskNeedDependss = "~!@";
	var preRemoveTaskids = '~!@';
	//集成重做时,加载需要修改的任务相关参数
	function loadChoosedTaskList()
	{	
		var choosedTaskids = "";
		var choosedNeedDependTaskids = "";
		var removeTaskids = "";
		for(var index in choosedTasks)
		{
			if(null != choosedTasks[index] && undefined != choosedTasks[index])
			{
				choosedTaskids=choosedTaskids+choosedTasks[index]+";";
			}
			
		}
		for(var index in choosedNeedDependTasks)
		{
			if(null != choosedNeedDependTasks[index] && undefined != choosedNeedDependTasks[index])
			{	
				choosedNeedDependTaskids=choosedNeedDependTaskids+choosedNeedDependTasks[index]+";";
			}
		}
		for(var index in removedTasks)
		{
			if(null != removedTasks[index] && undefined != removedTasks[index])
			{	
				removeTaskids=removeTaskids+removedTasks[index]+";";
			}
		}
		
		if(choosedTaskids == preChoosedTasks && choosedNeedDependTaskids == preChoosedTaskNeedDependss && removeTaskids == preRemoveTaskids)
		{
			//不要频繁刷新
			return;
		}
		else
		{
			preChoosedTasks = choosedTaskids;
			preChoosedTaskNeedDependss = choosedNeedDependTaskids;
			preRemoveTaskids = removeTaskids;
		}
		
		$('#choosedTaskList').datagrid({			onBeforeLoad: function(param)			{				if(!checkSessionValid())				{					return false;				}				else				{					return true;				}			},
			title : '双击行修改任务重做配置',
			width : 1000,
		    height:450,
			nowrap : false,
			striped : true,
			loadMsg:'数据加载中,请稍候...',
			singleSelect:false,
			collapsible : true,
			sortName : 'taskId',
			url : 'reqChoosedTaskList',
			method : 'post',
			queryParams : {
				    "removeTaskids":removeTaskids,
				    "choosedTaskids":choosedTaskids,
					"choosedNeedDependTaskids" : choosedNeedDependTaskids
				},
			remoteSort : true,
			idField : 'taskId',
			frozenColumns : [ [ {
				field : 'ck',
				checkbox : true
			}]],
			columns : [[
            {
				field : 'taskId',
				title : '任务名称',
				width : 280,
				align : 'center',
				rowspan : 1,
				formatter : function(value, rec) {
					return covTaskId2Name(value);
				}
			},
			{
				field : 'taskState',
				title : '任务状态',
				width : 55,
				rowspan : 1,
				align : 'center',
				formatter : function(value, rec) {
				return showTaskState(value);
				}
			}
			,
			{
				field : 'redoStartTime',
				title : '重做开始时间',
				align : 'center',
				width : 150,
				rowspan : 1,
				formatter : function(value, rec) {
					//让时间的格式统一
					rec["redoStartTime"] = changeDate(value);
					return rec["redoStartTime"];
				}
			} 
			,
			{
				field : 'redoEndTime',
				title : '重做结束时间',
				align : 'center',
				width : 150,
				rowspan : 1,
				formatter : function(value, rec) {
					//让时间的格式统一
					rec["redoEndTime"] = changeDate(value);
					return rec["redoEndTime"];
				}
			} ,
			{
				field : 'cycleDependFlag',
				title : '是否顺序依赖',
				align : 'center',
				width : 100,
				rowspan : 1,
				formatter : function(value, rec) {
					if(value == 1 || value == true)
					{
						return '是';
					}
					else
					{
						return '否';
					}
				}
			} 
			,
			{
				field : 'redoType',
				title : '重做类型',
				align : 'center',
				width : 100,
				formatter : function(value, rec) {
					return formatRedoType(value);
				}
			}
			,
			{
				field : 'redoDayLength',
				title : '集成重做天数',
				align : 'center',
				width : 80
			}
			]],
			toolbar : [
			           {
				id : 'btndel',
				text : '移除',
				iconCls : 'icon-no',
				handler : function() {
					removeTask();
				}
			    },
				'-',
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
		   			id : 'btnstop',
		   			text : '停止',
		   			iconCls : 'icon-disable',
		   			handler : function() {
		   				//停止任务
		   				changeTaskStates(1);
		   			}
		   		}
		   		,
		   		'-'
		   		,
		   		{
		   			id : 'btnEndEdit',
		   			text : '批量修改',
		   			iconCls : 'icon-edit',
		   			handler : function() {
		   				//批量修改
		   				showOptRedoTaskMulti();
		   			}
		   		}
		   		,
		   		'-'
		   		,
		   		{
					id : 'btnReverseC',
					text : '反选',
					iconCls : 'icon-refresh',
					handler : function() {
						reverseSelectTasks();
					}
				}
				,
				{
					id : 'btnReChoose',
					text : '重新选择',
					iconCls : 'icon-rechoose',
					handler : function() {
						reChoose('s');
					}
				}
			]
			,
			onDblClickRow:function(rowIndex, rowData)
			{
				showOptRedoTask(rowData["taskId"],rowData);
			}
			,
			onLoadSuccess:function()
			{
				var datas = $('#choosedTaskList').datagrid("getData");
				if(null != datas.noPriTasks && datas.noPriTasks.length >0)
				{
					showTPTips("<font color='red'>没有如下任务的查看权限，已经隐藏相应任务的展示!</font>",datas.noPriTasks);
				}
			},
			pagination : false, 
			rownumbers : true
		});
	}
	
	function removeTask()
	{
		var rows = $('#choosedTaskList').datagrid('getSelections');
		if (rows && rows.length > 0)
		{
			while(rows && rows.length > 0)
			{
				var taskId = rows[0]["taskId"];
				removedTasks[taskId] = taskId;
				var rowIndex = $('#choosedTaskList').datagrid("getRowIndex",rows[0]);
				$('#choosedTaskList').datagrid("deleteRow",rowIndex);
				rows = $('#choosedTaskList').datagrid('getSelections');
			}
			
		}
		else
		{
			$.messager.alert('提示','请至少选择一行记录!','info');
		}
	}
	
	function showOptRedoTask(taskId,rowData) {
		$("#hideOptRedoTask").attr("style","display:block");
		$("#multiModify").val(false);
		
		var grabDatas = grabMaxWeightHisRSCountState(taskId);
		if(1 != grabDatas.length && 3 != grabDatas.length)
		{
			//是否提示
			return;
		}
		var runFlag = grabDatas[2];
 		//将maxWeight值设置到控件上
		if(0 == parseInt(runFlag))
		{
			//提示请先停止任务，然后在修改(...字段),并将相关字段设置为灰色
			$.messager.defaults={ok:"确定",cancel:"取消"};
			var tips = '当前任务'+covTaskIds2Name(taskId, ';')+'正在运行.<br/>请先停止任务再继续操作!';
				$.messager.alert('提示', tips, "info");
		}
		else
		{
			setData2RedoTaskPage(rowData);
			$("#popOptRedoTask").window({
				modal : true,
				shadow : false,
				closed : false,
				title : "修改重做任务配置[<font color='red'>"+covTaskId2Name(taskId)+"</font>]"
			});
		}
	}
	
	
	function showOptRedoTaskMulti() {
		$("#hideOptRedoTask").attr("style","display:block");
		$("#multiModify").val(true);
		
		var runStatus = false;
		var rows = $('#choosedTaskList').datagrid('getSelections');
		if (rows && rows.length > 0)
		{
			for(var index in rows)
			{
				var taskId = rows[index]["taskId"];
				if(0 == rows[index]["taskState"])
				{
					runStatus = true;
					//提示请先停止任务，然后在修改(...字段),并将相关字段设置为灰色
					$.messager.defaults={ok:"确定",cancel:"取消"};
					var tips = '当前任务'+covTaskIds2Name(taskId, ';')+'正在运行.<br/>请先停止任务再继续操作!';
						$.messager.alert('提示', tips, "info");
					break;
				}
			}
			
			if(false == runStatus)
			{
				setData2RedoTaskPage();
				$("#popOptRedoTask").window({
					modal : true,
					shadow : false,
					closed : false,
					title : "批量修改重做任务配置"
				});
			}
		}
		else
		{
			$.messager.defaults={ok:"确定",cancel:"取消"};
			$.messager.alert('提示','请至少选择一个任务!','info');
		}
	}
	
	function saveRedoTask()
	{
		if('true' == $("#multiModify").val())
		{
			saveRedoTaskMulti();
		}
		else
		{
			saveRedoTaskSingle();
		}
	}
	
	function saveRedoTaskSingle()
	{
	   var taskId = $("#taskId2").val();
	   var datas = $('#choosedTaskList').datagrid("getData")["rows"];
	   for(var index in datas)
	   {
		   if(datas[index]["taskId"] == taskId)
		   {
			   var dataIndex = $('#choosedTaskList').datagrid("getRowIndex",datas[index]);
			   datas[index]["redoStartTime"]=$("#redoStartTime2").val();
			   datas[index]["redoEndTime"]=$("#redoEndTime2").val();
			   datas[index]["cycleDependFlag"]=true;
			   datas[index]["redoType"]=$("#redoType2").combobox("getValue");
			   datas[index]["redoDayLength"]=$("#redoDayLength2").val();
			   $('#choosedTaskList').datagrid("updateRow",{index:dataIndex,row:datas[index]});
			   break;
		   };
	   }
	   
	   $.messager.alert('提示','重做任务配置保存成功!','info');
       $("#popOptRedoTask").window('close');
   }
	
	function saveRedoTaskMulti()
	{
		var rows = $('#choosedTaskList').datagrid('getSelections');
		if (rows && rows.length > 0)
		{
			for(var index in rows)
			{
				modifyRedoTask(rows[index]);
			}
			$.messager.alert('提示','重做任务配置保存成功!','info');
		    $("#popOptRedoTask").window('close');
		}
		else
		{
			$.messager.defaults={ok:"确定",cancel:"取消"};
			$.messager.alert('提示','请至少选择一个任务!','info');
		}
	}
	
	function modifyRedoTask(row)
	{
	   if(null != row)
	   {
		   var dataIndex = $('#choosedTaskList').datagrid("getRowIndex",row);
		   row["redoStartTime"]=$("#redoStartTime2").val();
		   row["redoEndTime"]=$("#redoEndTime2").val();
		   row["cycleDependFlag"]=true;
		   row["redoType"]=$("#redoType2").combobox("getValue");
		   row["redoDayLength"]=$("#redoDayLength2").val();
		   $('#choosedTaskList').datagrid("updateRow",{index:dataIndex,row:row});
	   }
   }
	
	//更新时，设置内容到打开的更新页面
	function setData2RedoTaskPage(rowData)
	{
		if(null != rowData)
		{
			$("#taskName2").val(rowData["taskName"]);
			$("#taskDesc2").val(rowData["taskDesc"]);
			$("#redoStartTime2").val(changeDate(rowData["redoStartTime"]));
			$("#redoEndTime2").val(changeDate(rowData["redoEndTime"]));
			$("#redoType2").combobox("select",rowData["redoType"]);
			$("#redoDayLength2").val(rowData["redoDayLength"]);
			$('input:hidden[name="task.redoDayLength2"]').val(rowData["redoDayLength"]);
			$("#taskId2").val(rowData["taskId"]);
		}
		else
		{
			$("#taskName2").val('无效');
			$("#taskDesc2").val('无效');
			//使用不同日期时间范围
			var currDate = new Date();
			$("#redoStartTime2").val(getMouthDateString(currDate));
			$("#redoEndTime2").val(getDateString(currDate));
			$("#redoType2").combobox("select",1);
			$("#redoDayLength2").val(0);
			$('input:hidden[name="task.redoDayLength2"]').val(0);
		}
		
		document.getElementById("taskName2").disabled = true;
		document.getElementById("taskDesc2").disabled = true;
		$('input:radio[id="cycleDependFlag"]').each(function(index,obj)
		{
			obj.disabled = true;
		});
		
		$("input[id= 'task.cycleDependFlag2'][value='"+ true +"']").attr('checked','checked');
		if(1 == $("#redoType2").combobox("getValue"))
		{
			//天数不变灰
			$('#redoDayLength2').numberspinner('enable');
		}
		else
		{
			//天数变灰
			$('#redoDayLength2').numberspinner('disable');
		}
		$("#taskReqAdd2").val(false);
	}
	
	function getMouthDateString(dateTime)
	{
		var year = dateTime.getFullYear();
		var mouth = dateTime.getMonth()+1;
		return fillLeftZero(year, 4)+'-'+fillLeftZero(mouth, 2)+'-01'+" 00:00:00";
	}
	
	function getDateString(dateTime)
	{
		var year = dateTime.getFullYear();
		var mouth = dateTime.getMonth()+1;
		var day = dateTime.getDate();
		return fillLeftZero(year, 4)+'-'+fillLeftZero(mouth, 2)+'-'+fillLeftZero(day, 2)+" 00:00:00";
	}

	function cancelRedoTask()
	{
		 $("#popOptRedoTask").window('close');
	}
	
	$(function(){
		$("#redoType2").combobox({
			onSelect:function(record)
			{
				if(record["value"] == 1)
				{
					//天数不变灰
					$('#redoDayLength2').numberspinner('enable');
				}
				else
				{
					//天数变灰
					$('#redoDayLength2').numberspinner('disable');
				}
			}
		});
		
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

</script>
</head>

<body>
	<div id="taskRedoTabs" class="easyui-tabs"
		style="margin: 0px 0px 5px 0px; margin-left: auto; margin-right: auto; background: #fff; border: 1px solid #ccc; width: 1100px;"
		fit="false" plain="true">
		<div title="源任务集合" style="padding: 10px; min-height: 460px;">
			<div
				style="margin: 5px 0px 5px 0px; margin-left: auto; margin-right: auto; background: #fff; border: 1px solid #ccc; width: 1000px;">
				<table width="800" style="margin-left: auto; margin-right: auto;">
					<tr>
						<td><span>业务类型: </span>
						</td>
						<td>
						<input class="easyui-combobox" id="vserviceIdList" style="width: 140px" panelHeight="200px" editable="false" />
						</td>
						<td><span>任务名: </span></td>
						<td><input class="easyui-combobox" id="taskIdList"
							style="width: 210px" panelHeight="200px" /> <input
							id="taskIdOut" type="hidden" value="<%=taskId%>" />
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
								<option value="1">ODS</option>
									<option value="2">DW</option>
									<option value="3">DIM</option>
									<option value="4">混合型</option>
									<option value="5">导mysql</option>
									<option value="6">导文件</option>
									<option value="7">发送邮件</option>
									<option value="8">数据推送</option>
						</select>
						</td>
						<td>&nbsp;&nbsp;<span>周期类型: </span></td>
						<td><select id="searchCycleType1" editable="false"
							class="easyui-combobox" panelHeight="auto" style="width: 140px"
							required="true">
								<option value="0">全部</option>
								<option value="D">按天</option>
								<option value="I">按分钟</option>
								<option value="H">按小时</option>
								<option value="M">按月</option>
								<option value="Y">按年</option>
						</select></td>
					</tr>
				</table>
			</div>
			<table id="taskList"></table>
		</div>
		<div title="待处理任务集合" style="padding: 10px; min-height: 460px;">
			<div style="margin: 5px 0px 5px 0px; margin-left: auto; margin-right: auto; background: #fff; border: 1px solid #ccc; width: 1000px;">
				<table width="900" style="margin-left: auto; margin-right: auto;">
					<tr>
						<td><span>业务类型: </span>
						</td>
						<td>
						<div class="imgTextAlign">
						<input class="easyui-combobox" id="serviceIdS" style="width: 140px" panelHeight="200px" editable="false" />
						<input id="serviceIdSAll" type="checkbox" onClick=changeAll("serviceIdS","serviceIdSAll") checked="checked"/>
						</div>
						</td>
						<td><span>重做类型: </span>
						</td>
						<td>
						<div class="imgTextAlign">
							<select id="redoTypeS" editable="false" multiple="true"
								class="easyui-combobox" style="width: 150px" panelHeight="auto">
									<option value="1">集成重做</option>
									<option value="2">周末重做</option>
									<option value="3">月末重做</option>
							</select>
							<input id="redoTypeSAll" type="checkbox" onClick=changeAll("redoTypeS","redoTypeSAll") checked="checked"/>
						</div>	
						</td>
						
						<td>&nbsp;&nbsp;<span>任务状态: </span></td>
						<td>
						<div class="imgTextAlign">
							<select id="taskStateS" editable="false" multiple="true"
							class="easyui-combobox" panelHeight="auto" style="width: 140px">
								<option value="0">正常</option>
								<option value="1">停止</option>
							</select>
							<input id="taskStateSAll" type="checkbox" onClick=changeAll("taskStateS","taskStateSAll") checked="checked"/>
						</div>
						</td>
						<td rowspan="2" align="center" valign="center"><a
							class="easyui-linkbutton" plain="false" iconCls="icon-search"
							href="javascript:void(0)" onclick="selectTasks()">选中任务 </a>
						</td>
					</tr>
					<tr>
					<td><span>OS用户: </span></td>
					<td>
					<div class="imgTextAlign">
						<input class="easyui-combobox" id="osUserS"
							editable="false" style="width: 140px" panelHeight="200px" />
						<input id="osUserSAll" type="checkbox" onClick=changeAll("osUserS","osUserSAll") checked="checked"/>
					</div>
					</td>
						<td><span>任务类型:</span></td>
						<td>
						<div class="imgTextAlign">
							<select id="taskTypeS" editable="false" multiple="true"
							class="easyui-combobox" panelHeight="auto" style="width: 150px">
									<option value="1">ODS</option>
									<option value="2">DW</option>
									<option value="3">DIM</option>
									<option value="4">混合型</option>
									<option value="5">导mysql</option>
									<option value="6">导文件</option>
									<option value="7">发送邮件</option>
									<option value="8">数据推送</option>
							</select>
							<input id="taskTypeSAll" type="checkbox" onClick=changeAll("taskTypeS","taskTypeSAll") checked="checked"/>
						</div>
						</td>
						<td>&nbsp;&nbsp;<span>周期类型: </span></td>
						<td>
						<div class="imgTextAlign">
							<select id="cycleTypeS" editable="false" multiple="true"
							class="easyui-combobox" panelHeight="auto" style="width: 140px">
								<option value="D">按天</option>
								<option value="I">按分钟</option>
								<option value="H">按小时</option>
								<option value="M">按月</option>
								<option value="Y">按年</option>
							</select>
							<input id="cycleTypeSAll" type="checkbox" onClick=changeAll("cycleTypeS","cycleTypeSAll") checked="checked"/></td>
						</div>
					</tr>
				</table>
			</div>
			<table id="choosedTaskList"></table>
			<div region="south" border="false"
				style="text-align: right; padding: 5px 0; height: 35px;">
				<a class="easyui-linkbutton" plain="true" iconCls="icon-ok"
					href="javascript:void(0)" onclick="submitRedoTasks()">提交批量重做（全部而非选择项） </a> <a
					class="easyui-linkbutton" plain="true" iconCls="icon-cancel"
					href="javascript:void(0)" onclick=reChoose('s')>取消 </a>
			</div>
		</div>
	</div>
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
							<td><span>任务名: </span>
							</td>
							<td><input id="taskName" class="easyui-validatebox"
								validType="nameValidType" maxlength="128" type="text"
								style="width: 350px" name="task.taskName" /><font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>任务描述: </span>
							</td>
							<td><textarea id="taskDesc" name="task.taskDesc"
									maxlength="1024" rows="3" style="width: 350px"></textarea><font
								color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>任务类型: </span>
							</td>
							<td><select id="taskType" editable="false"
								class="easyui-combobox" name="task.taskType"
								style="width: 150px" required="true">
									<option value="1" selected="selected">ODS</option>
									<option value="2">DW</option>
									<option value="3">DIM</option>
									<option value="4">混合型</option>
									<option value="5">导mysql</option>
									<option value="6">导文件</option>
									<option value="7">发送邮件</option>
									<option value="8">数据推送</option>
							</select><font color="red">*</font>
							</td>
						</tr>
						<tr id="priorityRow">
							<td><span>任务优先级: </span>
							</td>
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
							<td><span>任务执行周期类型: </span>
							</td>
							<td><select id="cycleType" editable="false"
								class="easyui-combobox" name="task.cycleType"
								style="width: 150px" panelHeight="auto" required="true">
									<option value="D">按天</option>
									<option value="I">按分钟</option>
									<option value="H">按小时</option>
									<option value="M">按月</option>
									<option value="Y">按年</option>
							</select><font color="red">*</font>(运行状态时不能修改)</td>
						</tr>
						<tr>
							<td><span>周期长度: </span>
							</td>
							<td><input id="cycleLength" name="task.cycleLength"
								class="easyui-numberspinner" precision="0" increment="1" min="1"
								max="100" value='1' style="width: 150px" /> <font color="red">*</font>(运行状态时不能修改)</td>
						</tr>
						<tr>
							<td><span>周期偏移: </span>
							</td>
							<td><input id="cycleOffset" type="text"
								name="task.cycleOffset" class="easyui-validatebox"
								validType="cycleOffset" /><font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>任务依赖关系: </span>
							</td>
							<td><textarea id="dependTaskIdListDisplay" maxlength="1024"
									style="width: 350px" class="easyui-validatebox"
									validType="dependTaskIdList" rows="3"></textarea> <input
								type="hidden" id="dependTaskIdList" name="task.dependTaskIdList" />
							</td>
						</tr>
						<tr>
							<td><span>周期间是否顺序依赖: </span>
							</td>
							<td><input id="cycleDependFlag" type="radio"
								name="task.cycleDependFlag" value="true" checked="checked" />是 <input
								id="cycleDependFlag" type="radio" name="task.cycleDependFlag"
								value="false" />否</td>
						</tr>
						<tr>
							<td><span>是否有多批标志: </span>
							</td>
							<td><input id="multiBatchFlag" type="radio"
								name="task.multiBatchFlag" onclick="enableState()" value="true" />是
								<input id="multiBatchFlag" type="radio"
								name="task.multiBatchFlag" value="false"
								onclick="disableState()" checked="checked" />否</td>
						</tr>
						<tr>
							<td><span>分批结束标志: </span>
							</td>
							<td><select id="endBatchFlag" editable="false"
								class="easyui-combobox" name="task.endBatchFlag"
								panelHeight="auto" style="width: 200px" required="true">
									<option value="0" selected="selected">普通方式(任务执行逻辑结束)</option>
									<option value="1">指定的输入文件处理处理完成</option>
									<option value="2">等待时间内输入的文件处理完成</option>
									<option value="3">超过等待时间，且最少处理N个文件</option>
									<option value="4">超过等待时间，或者最少处理N个文件</option>
									<option value="5">超过等待时间，或者最少处理N个文件(个数严格)</option>
							</select><font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>输入文件列表: </span>
							</td>
							<td><textarea id="inputFileList" name="task.inputFileList"
									maxlength="2048" style="width: 350px" rows="3"></textarea>
							</td>
						</tr>
						<tr>
							<td><span>输入文件最少个数: </span>
							</td>
							<td><input id="inputFileMinCount"
								name="task.inputFileMinCount" class="easyui-numberspinner"
								value='0' precision="0" increment="1" min="0" max="100000"
								style="width: 150px" />
							</td>
						</tr>
						<tr>
							<td><span>等待输入时间（分钟）: </span>
							</td>
							<td><input id="waitInputMinutes"
								name="task.waitInputMinutes" type="text"
								class="easyui-numberspinner" value='30' precision="0"
								increment="10" min="0" max="2000" style="width: 150px" /> <font
								color="red">*</font>
							</td>
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
							<td><span>任务最早起始时间: </span>
							</td>
							<td><input id="startTime" name="task.startTime" type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								class="Wdate" value="2013-01-01 00:00:00" /> <font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>重做起始时间: </span>
							</td>
							<td><input id="redoStartTime" name="task.redoStartTime"
								type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								class="Wdate" value="2013-01-01 00:00:00" /> <font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>重做结束时间: </span>
							</td>
							<td><input id="redoEndTime" name="task.redoEndTime"
								type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								class="Wdate" value="2020-01-01 00:00:00" /> <font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>重做类型: </span>
							</td>
							<td><select id="redoType" editable="false"
								class="easyui-combobox" name="task.redoType"
								style="width: 150px" panelHeight="auto" required="true">
									<option value="1" selected="selected">集成重做</option>
									<option value="2">周末重做</option>
									<option value="3">月末重做</option>
							</select></td>
						</tr>
						<tr>
							<td><span>集成重做合并的天数: </span>
							</td>
							<td><input id="redoDayLength" class="easyui-numberspinner"
								name="task.redoDayLength" increment="1" min="0" max="1000"
								precision="0" value='0' style="width: 150px" /> <font
								color="red">*</font>
							</td>
						</tr>
						<tr id="weightRow">
							<td><span>任务周期占用的资源数: </span>
							</td>
							<td><input id="weight" class="easyui-numberspinner"
								name="task.weight" increment="1" min="1" max="100" precision="0"
								value='1' style="width: 150px" /><font color="red">*</font>
							</td>
						</tr>
					</table>
					
					<input type="hidden" id="taskId" name="task.taskId" /> <input
						id="taskNameSrc" type="hidden" /> <input type="hidden"
						id="taskReqAdd" name="taskReqAdd" /> <input type="hidden"
						id="searchTaskId" name="searchTaskId" /> <input type="hidden"
						id="searchTaskState" name="searchTaskState" /> <input
						type="hidden" id="searchTaskType" name="searchTaskType" /> <input
						type="hidden" id="searchCycleType" name="searchCycleType" />
						 <input	type="hidden" id="resetTaskRS"/>
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
	<div id="popOptRedoTask" class="easyui-window" inline="false"
		closed="true" style="width: 580px; height: 380px; padding: 10px">
		<div id="hideOptRedoTask" class="easyui-layout" fit="true"
			style="display: none;">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<form id="tccRedoTaskForm" name="tccTaskForm" method="post">
					<table id="OptRedoTaskTable" cellpadding="5px" cellspacing="10px"
						style="margin-left: auto; margin-right: auto;">
						<tr>
							<td><span>任务名: </span></td>
							<td><input id="taskName2" class="easyui-validatebox"
								validType="nameValidType" maxlength="128" type="text"
								style="width: 350px" name="task.taskName" /><font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>任务描述: </span></td>
							<td><textarea id="taskDesc2" name="task.taskDesc"
									maxlength="1024" rows="3" style="width: 350px"></textarea><font
								color="red">*</font></td>
						</tr>
						<tr>
							<td><span>周期间是否顺序依赖: </span></td>
							<td><input id="cycleDependFlag2" type="radio"
								name="task.cycleDependFlag" value="true" checked="checked" />是
								<input id="cycleDependFlag" type="radio"
								name="task.cycleDependFlag" value="false" />否<font color="red">*必需修改为顺序依赖</font>
							</td>
						</tr>
						<tr>
							<td><span>重做起始时间: </span></td>
							<td><input id="redoStartTime2" name="task.redoStartTime"
								type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								class="Wdate" value="2012-01-01 00:00:00" /> <font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>重做结束时间: </span></td>
							<td><input id="redoEndTime2" name="task.redoEndTime"
								type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								class="Wdate" value="2020-01-01 00:00:00" /> <font color="red">*</font>
							</td>
						</tr>
						<tr>
							<td><span>重做类型: </span></td>
							<td><select id="redoType2" editable="false"
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
							<td><input id="redoDayLength2" class="easyui-numberspinner"
								name="task.redoDayLength2" increment="1" min="0" max="20"
								precision="0" value='0' style="width: 150px" /> <font
								color="red">*</font></td>
						</tr>

					</table>
					<input type="hidden" id="taskId2" name="task.taskId" />
					<input type="hidden" id="taskReqAdd2" name="taskReqAdd" />
					<input type="hidden" id="multiModify"/>
				</form>
			</div>
			<div region="south" border="false"
				style="text-align: right; padding: 5px 0; height: 40px;">
				<a class="easyui-linkbutton" plain="true" iconCls="icon-ok"
					href="javascript:void(0)" onclick="saveRedoTask()">保存 </a> <a
					class="easyui-linkbutton" plain="true" iconCls="icon-cancel"
					href="javascript:void(0)" onclick="cancelRedoTask()">取消 </a>
			</div>
		</div>
	</div>
	<%@include file="taskPrivTips.jsp"%>
</body>
</html>