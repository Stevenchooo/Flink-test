<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><s:text name="新建任务页面"/></title>
<script type="text/javascript" src="js/jquery-1.7.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
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
    if(null == taskId)
    {
        taskId = "";
    }
    %>
    <div style="margin :5px 0px 5px 0px;margin-left: auto; margin-right: auto; background: #fff; border: 1px solid #ccc;width:1100px;">
        <table width="520"  style="margin-left: auto; margin-right: auto;">
			<tr>
				<td><span>任务ID: </span></td>
				<td><input class="easyui-combobox" id="taskIdList" style="width:140px" panelHeight="200px" value="<%=taskId%>"/>
				</td>
				   <td>&nbsp;&nbsp;<span>任务状态: </span></td>
                   <td>
                      <select id="searchTaskState1" class="easyui-combobox" panelHeight="auto" name="taskSearch.taskState" style="width:140px" required="true">
						    <option value="2">请选择（默认全选）</option>
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
                <td><span>任务类型:</span></td>
                  <td>
						<select id="searchTaskType1" class="easyui-combobox" panelHeight="auto"  style="width:140px" required="true">
						    <option value="0">请选择（默认全选）</option>
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
               <td>&nbsp;&nbsp;<span>周期类型: </span></td><td>
						<select id="searchCycleType1" class="easyui-combobox" panelHeight="auto"  style="width:140px" required="true">
						    <option value="0">请选择（默认全选）</option>
							<option value="H">按小时</option>
							<option value="D">按天</option>
							<option value="M">按月</option>
							<option value="Y">按年</option>
						</select></td>
			</tr>
      </table>
   </div>
  <table id="taskList"></table>
  <div id="popOptTask" class="easyui-window" inline="false" closed="true"
		style="width: 600px; height: 500px; padding: 10px">
		<div id="hideOptTask" class="easyui-layout" fit="true"
			style="display: none">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
                <form id="tccTaskForm" name="tccTaskForm" method="post">
                <table id="OptTaskTable" cellpadding="10px" cellspacing="5px" style="margin-left:auto;margin-right:auto;">
					<tr>
						<td><span>业务类型: </span></td><td>
						<select id="serviceid" class="easyui-combobox" name="task.serviceid" style="width:150px" required="true">
						    <option value="0">Dbank</option>
							<option value="1">终端云</option>
							<option value="2">天天浏览器</option>
							<option value="3">Hotalk</option>
							<option value="4" selected="selected">智汇云</option>
							<option value="5">商旅</option>
							<option value="6">电子商务</option>
							<option value="7">UC2(企业通讯)</option>
							<option value="8">HWS(云服务)</option>
							<option value="9">虚拟主机</option>
							<option value="10">云桌面</option>
							<option value="11">S3虚拟存储</option>
						</select><font color="red">*</font></td>
					</tr>
					<tr>
						<td><span>任务名: </span></td><td><input id="taskName" type="text" size="30" name="task.taskName"/><font color="red">*</font></td>
					</tr>
					<tr>
						<td><span>任务描述: </span></td><td><textarea id="taskDesc" name="task.taskDesc" rows="3" cols="40"></textarea><font color="red">*</font></td>
					</tr>
					<tr>
						<td><span>任务类型: </span></td><td>
						<select id="taskType" class="easyui-combobox" name="task.taskType" style="width:150px" required="true">
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
					<tr>
						<td><span>任务优先级: </span></td><td>
						<select id="priority" class="easyui-combobox" name="task.priority" style="width:150px" required="true">
							<option value="1">1</option>
							<option value="2">2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5"  selected="selected">5</option>
							<option value="6">6</option>
							<option value="7">7</option>
							<option value="8">8</option>
							<option value="9">9</option>
						</select><font color="red">*</font>(数字越小优先级越高)</td>
					</tr>
					<tr>
						<td><span>任务执行周期类型: </span></td><td>
						<select id="cycleType" class="easyui-combobox" name="task.cycleType" style="width:150px" panelHeight="auto"  required="true">
							<option value="H">按小时</option>
							<option value="D">按天</option>
							<option value="M">按月</option>
							<option value="Y">按年</option>
						</select><font color="red">*</font>(运行状态时不能修改)</td>
					</tr>
					<tr>
						<td><span>周期长度: </span></td><td><input id="cycleLength" name="task.cycleLength" 
						      class="easyui-numberspinner" precision="0" increment="1" min="1" max="100" value='1'style="width:150px"/>
						      <font color="red">*</font>(运行状态时不能修改)</td>
					</tr>
					<tr>
						<td><span>周期偏移: </span></td><td><input id="cycleOffset" type="text" name="task.cycleOffset"
						     class="easyui-validatebox" validType="cycleOffset"/><font color="red">*</font></td>
					</tr>
					<tr>
						<td><span>依赖任务ID列表: </span></td><td><textarea id="dependTaskIdList" class="easyui-validatebox" validType="dependTaskIdList"
						  name="task.dependTaskIdList" rows="3" cols="40"></textarea></td>
					</tr>
					<tr>
						<td><span>周期间是否顺序依赖: </span></td><td>
						  <input id="cycleDependFlag" type="radio" name="task.cycleDependFlag" value="true" checked="checked"/>是
				          <input id="cycleDependFlag" type="radio" name="task.cycleDependFlag" value="false"/>否</td>
					</tr>
					<tr>
						<td><span>是否有多批标志: </span></td><td>
						 <input id="multiBatchFlag" type="radio" name="task.multiBatchFlag" onclick="enableState()" value="true"/>是
				          <input id="multiBatchFlag" type="radio" name="task.multiBatchFlag" value="false" onclick="disableState()" checked="checked"/>否</td>
					</tr>
					<tr>
					<td><span>分批结束标志: </span></td><td>
						<select id="endBatchFlag" class="easyui-combobox" name="task.endBatchFlag" panelHeight="auto" style="width:200px" required="true">
							<option value="0" selected="selected">普通方式(任务执行逻辑结束)</option>
							<option value="1">指定的输入文件处理处理完成</option>
							<option value="2">等待时间内输入的文件处理完成</option>
							<option value="3">超过等待时间，且最少处理N个文件</option>
						</select><font color="red">*</font></td>
					</tr>
					<tr>
					<td><span>输入文件列表: </span></td><td><textarea id="inputFileList" name="task.inputFileList" rows="3" cols="40"></textarea></td>
					</tr>
					<tr>
					<td><span>输入文件最少个数: </span></td><td><input id="inputFileMinCount"  name="task.inputFileMinCount" 
					   class="easyui-numberspinner" value='0' precision="0" increment="1" min="0" max="100" style="width:150px"/></td>
					</tr>
					<tr>
					<td><span>等待输入时间（分钟）: </span></td><td><input id="waitInputMinutes" name="task.waitInputMinutes" type="text" 
					    class="easyui-numberspinner" value='30' precision="0" increment="10" min="0" max="100" style="width:150px"/>
					    <font color="red">*</font></td>
					</tr>
					<tr>
					<td><span>批文件所在主机IP: </span></td><td><input id="filesInHost" class="easyui-validatebox" validType="ipAddr" name="task.filesInHost" type="text"/></td>
					</tr>
					<tr>
					<td><span>任务最早起始时间: </span></td>
					<td><input id="startTime" name="task.startTime" type="text" 
					 onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate" value="2012-01-01 00:00:00" />
					<font color="red">*</font></td>
					</tr>
					<tr>
					<td><span>任务重做起始时间: </span></td>
					<td><input id="redoStartTime" name="task.redoStartTime" type="text" 
					 onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate" value="2012-01-01 00:00:00" />
					<font color="red">*</font></td>
					</tr>
					<tr>
					<td><span>任务重做结束时间: </span></td>
					<td><input id="redoEndTime" name="task.redoEndTime" type="text" 
					 onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" class="Wdate" value="2020-01-01 00:00:00" />
					<font color="red">*</font></td>
					</tr>
					<tr>
					<td><span>历史集成重做的天数: </span></td><td><input id="redoDayLength" class="easyui-numberspinner"  
					    name="task.redoDayLength" increment="1" min="0" max="20" precision="0" value='0' style="width:150px"/>
					    <font color="red">*</font></td>
					</tr>
					<tr>
					<td><span>任务周期占用的资源数: </span></td><td><input id="weight" class="easyui-numberspinner" 
					    name="task.weight" increment="1" min="1" max="100" precision="0" value='1' style="width:150px"/>
					    <font color="red">*</font></td>
					</tr>
				</table>
	
    <input type="hidden" id="taskId" name="task.taskId"/>
    <input type="hidden" id="taskReqAdd" name="taskReqAdd" />
    <input type="hidden" id="searchTaskId" name="searchTaskId" />
    <input type="hidden" id="searchTaskState" name="searchTaskState" />
    <input type="hidden" id="searchTaskType" name="searchTaskType" />
    <input type="hidden" id="searchCycleType" name="searchCycleType" />
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
</body>

<script type="text/javascript">
$(function() {
	//加载任务ID列表,成功后加载表格
	loadTaskIdList();
	//对查询条件框赋值
	setSearchValue();
	//加载表格
	loadTasks();
});
//加载任务Id列表选择框
function loadTaskIdList()
{
	$('#taskIdList').combobox({
		url : "reqTaskIdJsonObject.action?containAllCol=true",
		valueField : "key",
		textField : "value",
		editable : false,
		onSelect : function(record) {
			if ($("#taskIdList").combobox('getValue') == 0)
			{
				$('#searchTaskState1').combobox('enable');
				$('#searchTaskType1').combobox('enable');
				$('#searchCycleType1').combobox('enable');
			}
			else
			{	
				//其余查询条件不能用		
				setSearchDisable();
			}
		}
		}
	);
}
//设置查询Id框的值
function setSearchValue()
{
	var taskId = $("#taskIdList").combobox('getValue');
	if ((null == taskId && "" == taskId) && (null != $("taskId").val()))
	{
		$("#taskIdList").combobox('setValue',$("taskId").val());
	}else if(null == taskId || "" == taskId)
	{
	     //设为全部
		$("#taskIdList").combobox('setValue','0');
	}
	if ($("#taskIdList").combobox('getValue') != 0)
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
	} else {
			var hisTaskRSCount = grabDatas[1];
			var runFlag = grabDatas[2];
	 		//将maxWeight值设置到控件上
			if(0 == parseInt(hisTaskRSCount))
			{
				if(0 == parseInt(runFlag))
				{
					//提示请先停止任务，然后在修改(...字段),并将相关字段设置为灰色
					$.messager.defaults={ok:"确定",cancel:"关闭"};
					var tips = '当前任务[<font color="red">'+taskId+'</font>]正在运行.<br/>修改任务页面的[<font color="red">周期类型</font>,<font color="red">周期长度</font>,<font color="red">依赖任务ID列表</font>,<font color="red">是否顺序依赖</font>]不能修改!';
 					$.messager.confirm('提示', tips, function(r){
 						setData2TaskPage(rowData,true);
 						$("#popOptTask").window({
 							modal : true,
 							shadow : false,
 							closed : false,
 							title : "修改任务"
 						});
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
			else
			{
				//置灰四个字段
				//相关字段设置为灰色
				setData2TaskPage(rowData,true);
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
		document.getElementById("filesInHost").disabled = true;
	}
}
function enableState()
{
	$('#endBatchFlag').combobox('enable');
	document.getElementById("inputFileList").disabled = false;
	$('#inputFileMinCount').numberspinner('enable');
	$('#waitInputMinutes').numberspinner('enable');
	document.getElementById("filesInHost").disabled = false;
	
}
//清除任务页面内容
function clearTaskPage()
{
	$("#serviceid").combobox('setValue','4');
	$("#taskName").val(null);
	$("#taskDesc").val(null);
	$("#taskType").combobox('setValue','1');
	$("#priority").combobox('setValue','5');
	$("#cycleType").combobox('setValue','H');
	$("#cycleLength").val("1");
	$('input:hidden[name="task.cycleLength"]').val("1");
	$("#cycleOffset").val(null);
	$("input[name= 'task.cycleDependFlag'][value='true']").attr('checked','checked');
	$("input[name= 'task.multiBatchFlag'][value='false']").attr('checked','checked');
	$("#endBatchFlag").combobox('setValue','0');
	$("#dependTaskIdList").val(null);
	$("#inputFileList").val(null);
	$("#inputFileMinCount").val("0");
	$('input:hidden[name="task.inputFileMinCount"]').val("0");
	$("#waitInputMinutes").val("30");
	$('input:hidden[name="task.waitInputMinutes"]').val("30");
	$("#filesInHost").val("127.0.0.1");
	$("#startTime").val("2012-01-01 00:00:00");
	$("#redoStartTime").val("2012-01-01 00:00:00");
	$("#redoDayLength").val("0");
	$('input:hidden[name="task.redoDayLength"]').val("0");
	$("#weight").val("1");
	$('input:hidden[name="task.weight"]').val("1");
	
	//相关字段设置为可用
	$('#cycleType').combobox('enable');
	document.getElementById("dependTaskIdList").disabled = false;
	$('#cycleLength').numberspinner('enable');
	$('input:radio[id="cycleDependFlag"]').each(function(index,obj)
	{
		obj.disabled = false;
	});
}
//更新时，设置内容到打开的更新页面
function setData2TaskPage(rowData,grayFlag)
{
	//grayFlag 关键字段是否置灰
	$("#serviceid").combobox("select",rowData["serviceid"]);
	$("#taskName").val(rowData["taskName"]);
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
	$("#inputFileList").val(rowData["inputFileList"]);
	$("#inputFileMinCount").val(rowData["inputFileMinCount"]);
	$('input:hidden[name="task.inputFileMinCount"]').val(rowData["inputFileMinCount"]);
	$("#waitInputMinutes").val(rowData["waitInputMinutes"]);
	$('input:hidden[name="task.waitInputMinutes"]').val(rowData["waitInputMinutes"]);
	$("#filesInHost").val(rowData["filesInHost"]);
	$("#startTime").val(changeDate(rowData["startTime"]));
	$("#redoStartTime").val(changeDate(rowData["redoStartTime"]));
	$("#redoEndTime").val(changeDate(rowData["redoEndTime"]));
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
		document.getElementById("dependTaskIdList").disabled = true;
		$('#cycleLength').numberspinner('disable');
		$('input:radio[id="cycleDependFlag"]').each(function(index,obj)
		{
			obj.disabled = true;
		});
	}
	else
	{
		//相关字段设置为灰色
		$('#cycleType').combobox('enable');
		document.getElementById("dependTaskIdList").disabled = false;
		$('#cycleLength').numberspinner('enable');
		$('input:radio[id="cycleDependFlag"]').each(function(index,obj)
		{
			obj.disabled = false;
		});
	}
}
//加载任务信息列表到表格中
function loadTasks()
{
	//将要查询的值
	var taskIdValue = $("#taskIdList").combobox('getValue');	
	var taskStateValue = $("#searchTaskState1").combobox('getValue');
	var taskTypeValue = $("#searchTaskType1").combobox('getValue');
	var cycleTypeValue = $("#searchCycleType1").combobox('getValue');
	$('#taskList').datagrid({
		title : '任务列表',
		width : 1100,
		height: 455,
		nowrap : false,
		striped : true,
		loadMsg:'数据加载中,请稍候...',
		singleSelect:false,
		collapsible : true,
		url : 'getTaskAllList.action',
		sortName : '任务更新时间',
		sortOrder : 'desc',
		remoteSort : false,
		queryParams:{
			'searchTaskId' : taskIdValue,
			'searchTaskState' : taskStateValue,
			'searchCycleType' : cycleTypeValue,
			'searchTaskType' : taskTypeValue
			},
		idField : 'taskId',
		frozenColumns : [ [ {
			field : 'ck',
			checkbox : true
		}, {
			title : '任务ID',
			field : 'taskId',
			align : 'center',
			width : 60,
			sortable : true
		} ,				{
			field : 'opt',
			title : '操作',
			width : 125,
			align : 'center',
			rowspan : 2,
			formatter : function(value, rec) {
				var taskId = rec["taskId"];
				var optBtn = "";
				if(rec["taskState"])
				{
					optBtn = '<A style="FLOAT: left" class="l-btn l-btn-plain" onclick="changeTaskState('+taskId+',0)" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-enable">&nbsp;</SPAN></SPAN></SPAN></A>';
					
				}
				else
				{
					optBtn =  '<A style="FLOAT: left" class="l-btn l-btn-plain" onclick="changeTaskState('+taskId+',1)" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-disable">&nbsp;</SPAN></SPAN></SPAN></A>';
					
				}
				optBtn += '<A style="FLOAT: left" class="l-btn l-btn-plain" onclick="editTask('+taskId+')" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-edit">&nbsp;</SPAN></SPAN></SPAN></A>';
				optBtn += '<A style="FLOAT: left" class="l-btn l-btn-plain" onclick="nextStep('+taskId+')" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-next-step">&nbsp;</SPAN></SPAN></SPAN></A>';
				optBtn += '<A style="FLOAT: left" class="l-btn l-btn-plain" onclick="queryTaskRS('+taskId+')" href="javascript:void(0)"><SPAN class=l-btn-left><SPAN class=l-btn-text><SPAN class="l-btn-empty icon-search">&nbsp;</SPAN></SPAN></SPAN></A>';
				return optBtn;
			}
			
		}  ] ],
		columns : [ [
		{
			field : 'taskState',
			title : '任务状态',
			width : 55,
			rowspan : 1,
			align : 'center',
			formatter : function(value, rec) {
			return showTaskState(value);
		}
		}, 						
        {
			field : 'taskName',
			title : '任务名称',
			width : 155,
			align : 'center',
			rowspan : 1
		},
		{
			field : 'taskType',
			title : '任务类型',
			align : 'center',
			width : 100,
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
			width : 60
		}
		,
		{
			field : 'cycleType',
			align : 'center',
			title : '周期类型',
			width : 60,
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
		} ,
		{
			field : 'serviceid',
			title : '业务类型',
			align : 'center',
			width : 93,
			rowspan : 1,
			formatter : function(value, rec) {
				return getServiceID(value);
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
		,
		{
			field : 'startTime',
			title : '任务最早开始时间',
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
		toolbar : [ {
			id : 'btnadd',
			text : '',
			iconCls : 'icon-add',
			handler : function() {
				showOptTask(true,null);
			}
		}
		,'-', {
			id : 'btndel',
			text : '',
			iconCls : 'icon-no',
			handler : function() {
				deleteTask();
			}
		}
		,'-', {
			id : 'btnstart',
			text : '',
			iconCls : 'icon-enable',
			handler : function() {
				//启动任务
				changeTaskStates(0);
			}
		}
		,
		{
			id : 'btnstart',
			text : '',
			iconCls : 'icon-disable',
			handler : function() {
				//停止任务
				changeTaskStates(1);
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
	    afterPageText:'页，共{pages}页',
	    displayMsg:'当前显示从{from}到{to}共{total}记录',
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
			$.messager.defaults={ok:"确定",cancel:"关闭"};
			$.messager.confirm('确认', "检测到<font color='red'>["+normalTaskIds+"]</font>任务正在运行中,请先停止任务!", function(r){
					if(r)
					{
						var url = "TaskList.jsp?taskId=" + normalTaskIds;
						window.open(url);
					}
				});
		}
		else
		{
			$.messager.confirm('严重警告!', "该操作将会删除任务相关的所有信息，包括<font color='red'>[依赖该任务的依赖关系,任务,任务步骤,任务运行状态,批次运行状态,步骤运行状态]</font>你确定还要删除这"+rows.length+"行吗?", function(r){
				if (r){
					var allSucessed = true;
					while(rows && rows.length > 0)
					{
						var taskId = rows[0]["taskId"];
						if(!deleteBackStageTask(taskId))
						{
							allSucessed = false;
						}
						var index = $('#taskList').datagrid('getRowIndex', rows[0]);
						$('#taskList').datagrid('deleteRow', index);
						rows = $('#taskList').datagrid('getSelections');
					}
					
					if(allSucessed == true)					
					{
						  loadTaskIdList();
						  //对查询条件框赋值
						  setSearchValue();
						  //加载表格
						  loadTasks();
						$.messager.alert('提示','删除成功!','info');
					}
					else
					{
						$.messager.alert('提示','删除失败!','error');
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
	$.ajax({
		type : "post",
		url : "deleteTask",
		data : $.param({
			"task.taskId" : taskId
		}),
		async : false,
		success : function(data, textStatus) {
			if (textStatus == "success") {
				if (data == "true") {
					isSucessed = true;
				} else {
					isSucessed = false;
				}
			}
		}
	});
	return isSucessed;
}
function getServiceID(serviceID)
{
	var title;
	switch (parseInt(serviceID)) {
	case 0:
		title = "Dbank";
		break;
	case 1:
		title = "终端云";
		break;
	case 2:
		title = "天天浏览器";
		break;
	case 3:
		title = "Hotalk";
		break;
	case 4:
		title = "智汇云";
		break;
	case 5:
		title = "商旅";
		break;
	case 6:
		title = "电子商务";
		break;
	case 7:
		title = "UC2(企业通讯)";
		break;
	case 8:
		title = "HWS(云服务)";
		break;
	case 9:
		title = "虚拟主机";
		break;
	case 10:
		title = "云桌面";
		break;
	case 11:
		title = "S3虚拟存储";
		break;
	default:
		title = "智汇云";
		break;
	}
	return title;
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
	else if ("D" == (cycleType) || "d" == (cycleType))
	{
		cycleTypeTitle = "按天";
	}
	else if ("M" == (cycleType) || "m" == (cycleType))
	{
		cycleTypeTitle = "按天";
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


//校验IP
$.extend($.fn.validatebox.defaults.rules, {
	ipAddr: {
	validator: function(value, param){
		return vaildIpAddr(value);
	},
	message: '请输入正确的ip地址!'
	}
	});

function vaildIpAddr(value)
{
	var patrn= /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])(\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])){3}$/;
	if(value.match(patrn))
	{
		return true;
	}
	else
	{
		return false;
	}
}

//校验周期偏移
$.extend($.fn.validatebox.defaults.rules, {
	cycleOffset: {
	validator: function(value, param){
		return vaildCycleOffset(value);
	},
	message: '输入格式如：xMxD xhxm  例如：2h10m'
	}
	});

function vaildCycleOffset(value)
{
	//格式：xMxD xhxm  例如：2h10m
	var patrn= /^((([0-9]{0,3}M)?([0-9]{0,3}D)? ?([0-9]{0,3}h)?([0-9]{0,3}m)?)|0)$/;
	if(value.match(patrn))
	{
		return true;
	}
	else
	{
		return false;
	}
}
//校验依赖周期ID列表
$.extend($.fn.validatebox.defaults.rules, {
	dependTaskIdList: {
	validator: function(value, param){
		return vaildTaskIdList(value);
	},
	message: '多个之间采用分号分隔；格式：task_Id,depend_full_cycle(0或1),ignore_err(0或1);...例如：4043009,0,0;4093025,0,0'
	}
	});

function vaildTaskIdList(value)
{
	//格式：4043009,0,0;4093025,0,0
	var patrn= /^(([1-9][0-9]{0,6},(0|1),(0|1);? ?)){0,50}$/;
	if(value.match(patrn))
	{
		return true;
	}
	else
	{
		return false;
	}
}
function vaildData()
{
	if($("#taskName").val() == ""
	   || $("#taskDesc").val() == ""
	   || $("#cycleLength").val() == ""
	   || $("#cycleOffset").val() == ""
	   || $("#startTime").val() == ""
	   || $("#redoStartTime").val() == ""
	   || $("#waitInputMinutes").val() == ""
	   || $("#weight").val() == ""
	   || $("#redoDayLength").val() == "")
	{
		$.messager.alert('提示','*标识的字段不能为空!','info');
		return false;
	}
	if(vaildCycleOffset($("#cycleOffset").val())== false)
	{
		$.messager.alert('提示','周期偏移请输入正确的格式!','info');
		return false;
	}
	if($("#filesInHost").val()!= "")
	{
		if(vaildIpAddr($("#filesInHost").val())== false)
		{
		   $.messager.alert('提示','请输入正确的IP!','info');
		   return false;
		}
	}	
    if($("#dependTaskIdList").val()!= "")
	{
		if(vaildTaskIdList($("#dependTaskIdList").val())== false)
		{
		   $.messager.alert('提示','请输入正确的依赖任务ID列表格式!','info');
		   return false;
		}
	}	
		return true;	
}
function saveTask(){
	 //前台同步提交数据
	 // 新增加的ajax同步请求
	 if(vaildData())
	{
	   enableState();
	   //相关字段设置为可用
	   $('#cycleType').combobox('enable');
	   document.getElementById("dependTaskIdList").disabled = false;
	   $('#cycleLength').numberspinner('enable');
	   $('input:radio[id="cycleDependFlag"]').each(function(index,obj)
	   {
		  obj.disabled = false;
	   });
       $.ajax({
	       type: "post",
	       url: "saveTccTask",
	       data: $("#tccTaskForm").serialize(),
	       async: false,
	       success: function(data, textStatus){
	    	   if (textStatus == "success") {
					  data = data.split(",");
					  if (data[0] == "true") {
						  $.messager.alert('提示','保存成功!','info');
						  if(isAdd == true)
						  {
							  loadTaskIdList();
							  //对查询条件框赋值
							  setSearchValue();
							  //加载表格
							  loadTasks();
						  }
						  else
						  {
							  //刷新列表
							  $('#taskList').datagrid('reload');
						  }
						
					  } else {
						  $.messager.alert('提示','对不起,保存失败!','info');
					   }
				 }
			  }
		  });
        $("#popOptTask").window('close');
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
	//停用时，为保证后台延时5s再使任务禁用，页面等待5s的处理时间
	if (value == 1)
	{
		 wait();
		 changeState(taskId,value);
	}
	//启用时，去后台查看任务步骤是否允许任务启用条件
	else 
	{
		var count = getStepEnableCount(taskId);
		//符合条件
		if (count > 0)
		{
			changeState(taskId,value);
		}
		else
		{
			$.messager.defaults={ok:"是",cancel:"否"};
			$.messager.confirm('警告', "没有可用的任务步骤，编辑任务步骤（是）或者继续启用该任务（否）", function(r){
				if (r){
					nextStep(taskId);
				}
				else{
					changeState(taskId,value);
				}
			});
		}
	}	   
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
		//符合条件
		if (count > 0)
		{
			changeState(taskId,value);
		}
		else
		{
			$.messager.defaults={ok:"是",cancel:"否"};
			$.messager.confirm('警告', "任务[<font color='red'>"+taskId+"</font>]没有可用的任务步骤，编辑任务步骤（是）或者继续启用该任务（否）", function(r){
				if (r){
					nextStep(taskId);
				}
				else{
					changeState(taskId,value);
				}
			});
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
				taskIds += "<font color='red'>"+taskId+"</font>;";
				if(count % 4 ==0)
				{
					taskIds += "<br/>";
				}
			}
		}
		var tips = "你确定要" + cmd + "任务["+taskIds+"]吗?";
		
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
				
				if(value == 1)
				{
					wait();
				}
			}
			});
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
	   	$.ajax({
				type : "post",
				url : "changeTaskState",
				data : $.param({
					"task.taskId" : taskId,
					"task.taskState" : value,
					"waitUpdateTime" : 5000
				}),
				async : false,
				success : function(data, textStatus) {
					if (textStatus == "success") {
						if (data == "true") {						
							//修改本地数据
							{
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
						} else {
							isSucessed = false;
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

//等待5s处理   
function wait()
{
	 $.messager.progress({
		title:'请等待',
		msg:'正在处理...',
		interval:500
	});
	setTimeout(function(){
		$.messager.progress('close');
	},5000);
}

function getStepEnableCount(taskId)
{
	var count = 0;
	$.ajax({
		type : "post",
		url : "getStepEnableCount",
		data : $.param({
			"task.taskId" : taskId,
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
</script>
</html>