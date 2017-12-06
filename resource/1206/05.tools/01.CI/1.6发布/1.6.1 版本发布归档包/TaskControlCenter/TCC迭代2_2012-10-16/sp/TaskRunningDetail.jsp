<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<%
    String taskId = request.getParameter("taskId");
    String cycleId = request.getParameter("cycleId");
	if (null == taskId)
	{
		taskId = "";
	}
	
	if(null == cycleId)
	{
	    cycleId = "";
	}
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery-1.7.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="stylesheet" type="text/css" href="css/table.css" />
<link rel="stylesheet" type="text/css" href="themes/icon.css" />
<link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
<title><s:text name="任务周期详情" />
</title>
<script>
	//指定jobid的详细信息展示页面地址
	var jobDetailUrl="";
	
	checkSessionValid();
	$.ajax({
		type : "post",
		url : "grabJobDetailUrl",
		async : false,
		success : function(data, textStatus) {
			if (textStatus == "success") {
				var datas = data.split('|');
				if (datas[0] == "true") {						
					{
						for(var i=1;i<datas.length;i++)
						{
							jobDetailUrl += datas[i];
						}
					}
				} 
			}
		}
	});
	
	
	
	function loadLogs(tccLog,taskId,cycleId)
	{
		var logpagId = 'logPag4rs';
		if(tccLog)
		{
			logpagId = 'logPag4tcc';
		}
		$('#'+logpagId).pagination({   
		     total:queryLogMsgsCount(tccLog,taskId,cycleId),   
		     pageSize:200,
		     pageNumber:1,
		     showPageList:false,
			 beforePageText:'第',
			 afterPageText:'页, 共{pages}页',
			 displayMsg:'',
		     onSelectPage : function(pageNumber, pageSize)
		     {
		    	 getLogMsgs(tccLog,taskId,cycleId, pageNumber, pageSize);
		     },
			 onRefresh : function(pageNumber, pageSize)
		     {
				 getLogMsgs(tccLog,taskId,cycleId, pageNumber, pageSize);
		     }
		});
		
		getLogMsgs(tccLog,taskId,cycleId,1,300);
	}
	
	//初始化对象
	$(function() {
		$('#cycleIdList').combobox({
			width : 100
		});

		$('#taskIdList').combobox({
			url : "reqTaskIdJsonObject?containAllCol=false",
			valueField : "key",
			textField : "value",
			editable : true,
			width : 220,
			onChange : function(newValue, oldValue) {
				loadCycleIdData();
			},
			onLoadSuccess : function()
			{
				//外部传来的taskId
				initTaskIdNameMap('taskIdList');
				loadCycleIdData(true);
				//初始化对象
				$(function() {
					$('#taskRunningStatesTabs').tabs({
						onSelect : function(title) {
							changeTabShow();
						}
					});
				});
			}
		});
	});
	
	
	//前台同步提交数据
	// 新增加的ajax同步请求
	function queryDetail() {
		if (vaildData()) {
			changeTabShow();
		}
	}
	
	function queryDependTaskRSDetailChoosed()
	{
		var rows = $('#dependingTaskRunningStates').datagrid('getSelections');
		if (rows && rows.length > 0)
		{
			if(rows.length == 1)
			{
				var taskId = rows[0]["taskId"];
				var cycleId = rows[0]["cycleId"];
				$("#taskIdList").combobox('setValue',taskId);
				$("#cycleIdList").combobox('setValue',cycleId);
				queryDetail();
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

	
	function changeTabShow()
	{
		var taskId = covTaskName2Id($("#taskIdList").combobox('getValue'));
		var cycleId = $("#cycleIdList").combobox('getValue');
		
		//当前选择tab才更新页面显示
		var tab = $('#taskRunningStatesTabs').tabs('getSelected');
		if("任务周期运行状态"==tab.panel('options').title)
		{
			loadRS(taskId, cycleId);
		}
		else if("依赖的任务周期运行状态"==tab.panel('options').title)
		{
			loadDependRSList(taskId,cycleId);
		}
		else if("批次运行状态"==tab.panel('options').title)
		{
			loadBatchRSList(taskId, cycleId);
		}
		else if("步骤运行状态"==tab.panel('options').title)
		{
			loadStepRSList(taskId, cycleId);
		}
		else if("任务信息"==tab.panel('options').title)
		{
			//var taskId = covTaskName2Id($("#taskIdList").combobox('getValue'));
			var url = "TaskList.jsp?taskId=" + taskId+";";
			$('#taskframe').attr("src", url);
			
		}
		else if("任务步骤信息"==tab.panel('options').title)
		{
			//var taskId = covTaskName2Id($("#taskIdList").combobox('getValue'));
			var url = "StepList.jsp?taskId=" + taskId;
			$('#stepsframe').attr("src", url);
		}
		else if("任务周期相关日志"==tab.panel('options').title)
		{
			loadLogs(true, taskId, cycleId);
		}
		else if("远程壳输出日志"==tab.panel('options').title)
		{
			loadJobsList(taskId, cycleId);
			loadLogs(false, taskId, cycleId);
		}
	}
	
	//根据条件查询日志总记录数
	function getLogMsgs(tccLog,taskId,cycleId, pageNumber, pageSize)
	{
		var msgs = "";
		checkSessionValid();
		$.ajax({
			type : "post",
			url : "getLogMsgs",
			data : $.param({
					"tccLog":tccLog,
					"taskId":taskId,
					"cycleId":cycleId,
					"pageNumber":pageNumber,
					"pageSize":pageSize
				}),
			async : false,
			success : function(data, textStatus) {
				if (textStatus == "success") {
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
								msgs = values[0];
							}
						}
					} 
					else
					{
						$.messager.defaults={ok:"确定",cancel:"取消"};
						$.messager.alert('提示','查询出错!','error');
					}
				}
			}
		});
		
		if(tccLog)
		{
			$("#tccLog").find('tr').find('td').empty();
			$("#tccLog").find('tr').find('td').append(msgs);
		}
		else
		{
			$("#rsLog").find('tr').find('td').empty();
			$("#rsLog").find('tr').find('td').append(msgs);
		}
		
		return msgs;
	}
	
	//根据条件查询日志总记录数
	function queryLogMsgsCount(tccLog,taskId,cycleId)
	{
		var count = 0;
		checkSessionValid();
		$.ajax({
			type : "post",
			url : "getLogsCount",
			data : $.param({
				"tccLog":tccLog,
				"taskId":taskId,
				"cycleId":cycleId
				}),
			async : false,
			success : function(data, textStatus) {
				if (textStatus == "success") {
					var datas = data.split(",");
					if (datas[0] == "true") {
						if('3' == datas[1])
						{
							count = datas[2];
						}
						else
						{
							$.messager.defaults={ok:"确定",cancel:"取消"};
							$.messager.alert('提示','查询出错!','error');
						}
					} 
					else
					{
						$.messager.defaults={ok:"确定",cancel:"取消"};
						$.messager.alert('提示','查询出错!','error');
					}
				}
			}
		});
		return count;
	}

	function vaildData() {
		return true;
	}

	function loadCycleIdData(first) {
		$('#cycleIdList').combobox(
				{
					url : 'reqCycleIdJsonObject?taskId='+ covTaskName2Id($("#taskIdList").combobox('getValue')),
					valueField : 'key',
					textField : 'key',
					editable : true,
					width : 100,
					onLoadSuccess : function()
					{
						if(null == first || true != first)
						{
							//如果不是首次
							$('#cycleIdList').val('');
							$('#cycleIdList').combobox('setValue','');
						}
					}
				});
	}

	
	function dependList2table(dependList)
	{
		var tableStr="";
		if("" != dependList)
	    {
			tableStr = '<table class="nonayframe" style="margin-left: auto; margin-right: auto;" border="0">';
			rows = dependList.split(';');
			for(var rIndex in rows)
		    {
				if("" !=rows[rIndex])
				{
					tableStr +='<tr>';
				    cols = rows[rIndex].split(',');
				    if(cols.length == 2)
				    {
				    	tableStr +='<td>';
				    	tableStr +=covTaskId2Name(cols[0]);
				    	tableStr +='</td>';
				    	
				    	tableStr +='<td>';
				    	tableStr +=cols[1];
				    	tableStr +='</td>';
				    }
				    tableStr +='</tr>';
				}
		    }
			tableStr += '</table>';
	    }
		return tableStr;
	}
	
	//获取依赖任务周期的json字符串
	function loadDependRSList(taskId,cycleId)
	{
		checkSessionValid();
	$.ajax({
			type : "post",
			url : 'queryRSList2Json?taskId='+taskId+'&cycleId='+cycleId+'&queryRSType=4',
			async : false,
			success : function(data, textStatus) {
				if (textStatus == "success") {
					var jsonObj = $.parseJSON(data);
					var success = jsonObj["success"];
					var returnValue2PageType = jsonObj["returnValue2PageType"];
					var values = jsonObj["values"];
					if (success == true) {
						if(3 == returnValue2PageType)
						{
							if(values.length >= 1)
							{
								var dependRSJsonObj= $.parseJSON(values[0]);
								//绑定
								loadDependRSListLocal(taskId,cycleId,dependRSJsonObj);
							}
						}
					} 
					else
					{
						if(2 == returnValue2PageType)
						{
							var alertMsg = '对不起, 查询'+formatTaskCycleC(taskId,cycleId)+'依赖的任务周期运行状态失败'+'!<br>权限不足:<br>';
						     if(values.length >= 1)
						     {
								  alertMsg +='<table>';
								  //去掉‘,’号
								  var desValues = values[0].split(',');
								  for(var i=0;i <desValues.length;i++)
							      {
									  if('' != values[i])
								      {
										  alertMsg +='<tr>';
										  alertMsg +='<td>';
										  alertMsg += desValues[i];
										  alertMsg +='</td>';
										  alertMsg +='</tr>';
								      }
							      }
								  alertMsg +='</table>';
								  alertMsg = alertMsg.replace(/\[/g, '[<font color="red">');
								  alertMsg = alertMsg.replace(/\]/g, ']</font>');
								  $.messager.alert('提示',alertMsg,'info');
						     }
				        }
						else
						{
							$.messager.defaults={ok:"确定",cancel:"取消"};
							$.messager.alert('提示','查询'+formatTaskCycleC(taskId,cycleId)+'依赖的任务周期运行状态失败!','error');
						}
					}
				}
			}
		});
	}
	
	//加载运行状态
	function loadRS(taskId,cycleId)
	{
		$('#taskRunningState').datagrid({			onBeforeLoad: function(param)			{				if(!checkSessionValid())				{					return false;				}				else				{					return true;				}			},
			title : '任务运行状态信息['+getTitleKey(taskId,cycleId)+']',
			width : 1000,
			loadMsg:'数据加载中,请稍候...',
			nowrap : false,
			striped : true,
			singleSelect:true,
			collapsible : true,
			url : 'queryRSList2Json?taskId='+taskId+'&cycleId='+cycleId+'&queryRSType=1',
			sortName : '任务Id',
			sortOrder : 'taskId',
			remoteSort : false,
			idField : 'taskId',
			frozenColumns : [ [ 
			{
				field : 'taskId',
				title : '任务名称',
				align : 'center',
				rowspan : 1,
				width : 200,
				formatter : function(value, rec) {
					return covTaskId2Name(value);
				}
			}
			,
			{
				title : '周期Id',
				field : 'cycleId',
				align : 'center',
				width : 85,
				sortable : true
			} 
			,
			{	
				field : 'state',
				title : '状态',
				width : 60,
				rowspan : 1,
				align : 'center',
				formatter : function(value, rec) {
					return showState(value);
				}
			}
			,
			{
				field : 'opt',
				title : '执行时间',
				width : 100,
				align : 'center',
				rowspan : 2,
				formatter : function(value, rec) {
					var dateStrStart = rec["runningStartTime"];
					var dateStrEnd = rec["runningEndTime"];
					return formateTimeDiff(diffTimeStr(dateStrStart,dateStrEnd));
				}
				
			}
			] ],
			columns : [ [
			             {
				title : '基本信息',
				colspan : 9
			}], [

			{
				field : 'returnTimes',
				title : '运行次数',
				align : 'center',
				width : 55,
				rowspan : 1,
				formatter : function(value, rec) {
					return value;
				}
			} ,
            {
				field : 'runningStartTime',
				title : '运行开始时间',
				width : 125,
				align : 'center',
				rowspan : 1,
				formatter : function(value, rec) {
					return dateTimeFormate(value);
				}
			},
			{
				field : 'runningEndTime',
				title : '运行结束时间',
				align : 'center',
				width : 125,
				rowspan : 1,
				formatter : function(value, rec) {
					return dateTimeFormate(value);
				}
			} 
			,
			{
				field : 'beginDependTaskList',
				title : '已启动依赖任务ID列表',
				align : 'center',
				width : 300,
				rowspan : 1,
				formatter : function(value, rec) {
					return dependList2table(value);
				}
			} 
			,
			{
				field : 'finishDependTaskList',
				title : '已完成依赖任务ID列表',
				align : 'center',
				width : 300,
				rowspan : 1,
				formatter : function(value, rec) {
					return dependList2table(value);
				}
			}
			] ],
			pagination : false,
			rownumbers : true,
		    onRowContextMenu: function (e, field) {
                e.preventDefault();
                var datas = $('#taskRunningState').datagrid('getData');
                var data = datas['rows'][field];
                setChoosedValue(data['taskId']+","+data['cycleId'],'s');
  			    $('#smm').menu('show', {
					left: e.pageX,
					top: e.pageY
				});
            }
		});
	}
	
	
	//reverserTree是否是反向依赖树
	function grabCycleOffsetLeftTime(taskId, cycleId)
	{
		var cycleStartLeftTime = '0:0:0';
		checkSessionValid();
	$.ajax({
			type : "post",
			url : "grabCycleOffsetLeftTime?taskId="+taskId+'&cycleId='+cycleId,
			async : false,
			success : function(data, textStatus) {
				if (textStatus == "success") {
					var datas = data.split("|");
					if (datas[0] == "true") {
						cycleStartLeftTime = datas[1];
					} else {
						$.messager.alert('提示','对不起,获取周期偏移剩余时间失败!','info');;
					}
				}
			}
		});
		return cycleStartLeftTime;
	}
	
	//本地加载依赖的任务周期运行状态信息列表
	function loadDependRSListLocal(taskId,cycleId,jsonObj)
	{
		//周期偏移剩余时间
		var cycleOffsetLeftTime = formateTimeDiff(grabCycleOffsetLeftTime(taskId, cycleId));
		var dtitle = '依赖的任务周期运行状态['+getTitleKey(taskId,cycleId)+']周期偏移剩余时间['+cycleOffsetLeftTime+']';
		$('#dependingTaskRunningStates').datagrid({			onBeforeLoad: function(param)			{				if(!checkSessionValid())				{					return false;				}				else				{					return true;				}			},
			title : dtitle,
			width : 1000,
			loadMsg:'数据加载中,请稍候...',
			nowrap : false,
			striped : true,
			singleSelect:true,
			collapsible : true,
			url : null,
			sortName : '任务Id',
			sortOrder : 'taskId',
			remoteSort : false,
			idField : 'taskId',
			frozenColumns : [ [ {
				field : 'ck',
				checkbox : true
			}
			,
			{
				field : 'taskId',
				title : '任务名称',
				align : 'center',
				rowspan : 1,
				width : 200,
				formatter : function(value, rec) {
					return covTaskId2Name(value);
				}
			}
			,
			{
				title : '周期Id',
				field : 'cycleId',
				align : 'center',
				width : 85,
				sortable : true
			} 
			,
			{	
				field : 'state',
				title : '状态',
				width : 60,
				rowspan : 1,
				align : 'center',
				formatter : function(value, rec) {
					return showState(value);
				}
		} ,
			{
				field : 'opt',
				title : '执行时间',
				width : 100,
				align : 'center',
				rowspan : 2,
				formatter : function(value, rec) {
					var dateStrStart = rec["runningStartTime"];
					var dateStrEnd = rec["runningEndTime"];
					return formateTimeDiff(diffTimeStr(dateStrStart,dateStrEnd));
				}
				
			}  ] ],
			columns : [ [
			             {
				title : '基本信息',
				colspan : 9
			}], [
			{
				field : 'returnTimes',
				title : '运行次数',
				align : 'center',
				width : 55,
				rowspan : 1,
				formatter : function(value, rec) {
					return value;
				}
			} ,
            {
				field : 'runningStartTime',
				title : '运行开始时间',
				width : 125,
				align : 'center',
				rowspan : 1,
				formatter : function(value, rec) {
					return dateTimeFormate(value);
				}
			},
			{
				field : 'runningEndTime',
				title : '运行结束时间',
				align : 'center',
				width : 125,
				rowspan : 1,
				formatter : function(value, rec) {
					return dateTimeFormate(value);
				}
			} 
			,
			{
				field : 'beginDependTaskList',
				title : '已启动依赖任务ID列表',
				align : 'center',
				width : 300,
				rowspan : 1,
				formatter : function(value, rec) {
					return dependList2table(value);
				}
			} 
			,
			{
				field : 'finishDependTaskList',
				title : '已完成依赖任务ID列表',
				align : 'center',
				width : 300,
				rowspan : 1,
				formatter : function(value, rec) {
					return dependList2table(value);
				}
			}
			] ],
			pagination : false,
			rownumbers : true,
			toolbar : [ {
				id : 'btnsearch',
				text : '',
				iconCls : 'icon-search',
				handler : function() {
					queryDependTaskRSDetailChoosed();
				}
			}]
			,
			onDblClickRow:function(rowIndex, rowData)
			{
				queryDependTaskRSDetailChoosed();
			},
			onRowContextMenu: function (e, field) {
	            e.preventDefault();
	            var datas = $('#dependingTaskRunningStates').datagrid('getData');
	            var data = datas['rows'][field];
	            setChoosedValue(data['taskId']+","+data['cycleId'],'d');
				    $('#dmm').menu('show', {
					left: e.pageX,
					top: e.pageY
				});
	        }
		});
		
		$('#dependingTaskRunningStates').datagrid("loadData",jsonObj);
	}
	
	
	//加载批次运行状态
	function loadBatchRSList(taskId,cycleId)
	{
		$('#batchRunningStates').datagrid({			onBeforeLoad: function(param)			{				if(!checkSessionValid())				{					return false;				}				else				{					return true;				}			},
			title : '批次运行状态列表信息['+getTitleKey(taskId,cycleId)+']',
			width : 1000,
			nowrap : false,
			loadMsg:'数据加载中,请稍候...',
			striped : true,
			singleSelect:true,
			collapsible : true,
			url : 'queryRSList2Json?taskId='+taskId+'&cycleId='+cycleId+'&queryRSType=2',
			sortName : '任务Id',
			sortOrder : 'taskId',
			remoteSort : false,
			idField : 'taskId',
			frozenColumns : [ [ 
			{
				field : 'taskId',
				title : '任务名称',
				align : 'center',
				rowspan : 1,
				width : 200,
				formatter : function(value, rec) {
					return covTaskId2Name(value);
				}
			}
			,
			{
				title : '周期Id',
				field : 'cycleId',
				align : 'center',
				width : 105,
				sortable : true
			} 
			,
			{
				title : '批次Id',
				field : 'batchId',
				align : 'center',
				width : 70,
				sortable : true
			} 
			,
			{	
				field : 'state',
				title : '状态',
				width : 70,
				rowspan : 1,
				align : 'center',
				formatter : function(value, rec) {
					return showState(value);
				}
		}
			,
			{
				field : 'opt',
				title : '执行时间',
				width : 110,
				align : 'center',
				rowspan : 2,
				formatter : function(value, rec) {
					var dateStrStart = rec["runningStartTime"];
					var dateStrEnd = rec["runningEndTime"];
					return formateTimeDiff(diffTimeStr(dateStrStart,dateStrEnd));
				}
				
			}
		] ],
			columns : [ [
			             {
				title : '基本信息',
				colspan : 9
			}], [
            {
				field : 'runningStartTime',
				title : '运行开始时间',
				width : 145,
				align : 'center',
				rowspan : 1,
				formatter : function(value, rec) {
					return dateTimeFormate(value);
				}
			},
			{
				field : 'runningEndTime',
				title : '运行结束时间',
				align : 'center',
				width : 145,
				rowspan : 1,
				formatter : function(value, rec) {
					return dateTimeFormate(value);
				}
			} 
			,
			{
				field : 'jobInput',
				title : '处理文件名',
				align : 'center',
				width : 250,
				rowspan : 1,
				formatter : function(value, rec) {
					return value;
				}
			} 
			] ],
			pagination : false,
			rownumbers : true
		});
	}
	
	function dateTimeFormate(value)
	{
		if(null != value && "" != value)
		{
			return value.replace('T',' ');
		}
		else
		{
			return "";
		}
	}
	
	function getTitleKey(taskId,cycleId)
	{
		return '<font color="red">'+covTaskId2Name(taskId)+","+cycleId+'</font>';
	}
	
	function toJobDetailWithSpace(jobIds)
	{
		var jobDetails = "";
		if(null != jobIds && "" != jobIds)
		{
			var val = 0;
			var jobIdArr = jobIds.split(',');
			for(var index in jobIdArr)
			{
				if("" != jobIdArr[index])
				{
					val++;
					jobDetails += '<a target="_blank" href="';
					jobDetails += jobDetailUrl.replace("%s",jobIdArr[index]);
					jobDetails += '">';
					jobDetails += jobIdArr[index];
					jobDetails += '</a>';
					if(val % 6 ==0)
					{
						jobDetails += '</br>';
					}
					else
					{
						jobDetails += '&nbsp;&nbsp';
					}
				}
			}
		}
		return jobDetails;
	}
	
	function toJobDetail(jobIds)
	{
		var jobDetails = "";
		if(null != jobIds && "" != jobIds)
		{
			var jobIdArr = jobIds.split(',');
			for(var index in jobIdArr)
			{
				if("" != jobIdArr[index])
				{
					jobDetails += '<a target="_blank" href="';
					jobDetails += jobDetailUrl.replace("%s",jobIdArr[index]);
					jobDetails += '">';
					jobDetails += jobIdArr[index];
					jobDetails += '</a>';
					jobDetails += '</br>';
				}
			}
		}
		return jobDetails;
	}
	
	//加载jobId列表
	function loadJobsList(taskId,cycleId)
	{
		$('#jobIdList').datagrid({
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
			title : '',
			width : 1000,
			nowrap : false,
			loadMsg:'数据加载中,请稍候...',
			striped : true,
			singleSelect:true,
			collapsible : true,
			url : 'queryRSList2Json?taskId='+taskId+'&cycleId='+cycleId+'&queryRSType=3',
			sortName : '任务Id',
			sortOrder : 'taskId',
			remoteSort : false,
			idField : 'taskId',
			columns : [ [
			{
				field : 'runningJobId',
				title : '脚本在hadoop中执行时产生的jobId列表(点击查看运行详情)',
				align : 'center',
				width : 950,
				rowspan : 1,
				formatter : function(value, rec) {
					return toJobDetailWithSpace(value);
				}
			}
			] ],
			pagination : false,
			rownumbers : true
		});
	}
	
	//加载批次运行状态
	function loadStepRSList(taskId,cycleId)
	{
		$('#stepRunningStates').datagrid({			onBeforeLoad: function(param)			{				if(!checkSessionValid())				{					return false;				}				else				{					return true;				}			},
			title : '步骤运行状态列表信息['+getTitleKey(taskId,cycleId)+']',
			width : 1000,
			nowrap : false,
			loadMsg:'数据加载中,请稍候...',
			striped : true,
			singleSelect:true,
			collapsible : true,
			url : 'queryRSList2Json?taskId='+taskId+'&cycleId='+cycleId+'&queryRSType=3',
			sortName : '任务Id',
			sortOrder : 'taskId',
			remoteSort : false,
			idField : 'taskId',
			frozenColumns : [ [ 
			{
				field : 'taskId',
				title : '任务名称',
				align : 'center',
				rowspan : 1,
				width : 200,
				formatter : function(value, rec) {
					return covTaskId2Name(value);
				}
			}
			,
			{
				title : '周期Id',
				field : 'cycleId',
				align : 'center',
				width : 85,
				sortable : true
			} 
			,
			{
				title : '批次Id',
				field : 'batchId',
				align : 'center',
				width : 50,
				sortable : true
			} 
			,
			{
				title : '步骤Id',
				field : 'stepId',
				align : 'center',
				width : 50,
				sortable : true
			} 
			,
			{	
				field : 'state',
				title : '状态',
				width : 60,
				rowspan : 1,
				align : 'center',
				formatter : function(value, rec) {
					return showState(value);
				}
		}
			,
			{
				field : 'opt',
				title : '执行时间',
				width : 100,
				align : 'center',
				rowspan : 2,
				formatter : function(value, rec) {
					var dateStrStart = rec["runningStartTime"];
					var dateStrEnd = rec["runningEndTime"];
					return formateTimeDiff(diffTimeStr(dateStrStart,dateStrEnd));
				}
				
			}  
			] ],
			columns : [ [
			             {
				title : '基本信息',
				colspan : 9
			}], [
			{
				field : 'retryCount',
				title : '重试次数',
				align : 'center',
				width : 60,
				rowspan : 1
			} ,
            {
				field : 'runningStartTime',
				title : '运行开始时间',
				width : 125,
				align : 'center',
				rowspan : 1,
				formatter : function(value, rec) {
					return dateTimeFormate(value);
				}
			},
			{
				field : 'runningEndTime',
				title : '运行结束时间',
				align : 'center',
				width : 125,
				rowspan : 1,
				formatter : function(value, rec) {
					return dateTimeFormate(value);
				}
			} 
			,
			{
				field : 'runningJobId',
				title : 'JobId列表',
				align : 'center',
				width : 100,
				rowspan : 1,
				formatter : function(value, rec) {
					return toJobDetail(value);
				}
			}
			,
			{
				field : 'jobInputList',
				title : '当前任务输入列表',
				align : 'center',
				width : 200,
				rowspan : 1,
				formatter : function(value, rec) {
					return value;
				}
			}
			,
			{
				field : 'jobOutputList',
				title : '当前任务输出列表',
				align : 'center',
				width : 200,
				rowspan : 1,
				formatter : function(value, rec) {
					return value;
				}
			}
			,
			{
				field : 'failReason',
				title : '失败原因',
				align : 'center',
				width : 200,
				rowspan : 1,
				formatter : function(value, rec) {
					return value;
				}
			}
			
			] ],
			pagination : false,
			rownumbers : true
		});
	}
	
	var choosedValue = {};
	
	function setChoosedValue(value,prefix)
	{
		choosedValue[prefix] = value;
	}
	
	//以该节点作为查询条件刷新本页面
	function viewDependTree(prefix)
	{
		if (null != choosedValue[prefix] && "" != choosedValue[prefix])
		{
			var taskIdCycleIdStates = choosedValue[prefix].split(",");
		    var url = "DependTaskTree.jsp?taskId=" + taskIdCycleIdStates[0] + "&cycleId=" + taskIdCycleIdStates[1];
		    window.open(url);
		}
	}
	
	//查看反向依赖树
	function viewReverseDependTree(prefix)
	{
		if (null != choosedValue[prefix] && "" != choosedValue[prefix])
		{
			var taskIdCycleIdStates = choosedValue[prefix].split(",");
		    var url = "DependReverseTree.jsp?taskId=" + taskIdCycleIdStates[0] + "&cycleId=" + taskIdCycleIdStates[1];
		    window.open(url);
		}
	}

	//格式化任务周期的显示
	function formatTaskCycle(taskIdCycleIdStates)
	{
		var taskCycleS = '';
		if(null != taskIdCycleIdStates)
		{
			taskCycleS += '<font style="font-weight:bold;" color="red">';
			if(taskIdCycleIdStates.length >=2 )
			{
				
				taskCycleS += covTaskId2Name(taskIdCycleIdStates[0])+','+taskIdCycleIdStates[1];
			}
			taskCycleS += '</font>';
		}
		return taskCycleS;
	}
	
	function redoTaskCycle(prefix)
	{
		if (null != choosedValue[prefix] && "" != choosedValue[prefix])
		{
			var taskIdCycleIdStates = choosedValue[prefix].split(",");
			$.messager.defaults={ok:"是",cancel:"否"};
			var tips = '你确定要重做任务周期<font color="red">'+covTaskId2Name(taskIdCycleIdStates[0])+','+taskIdCycleIdStates[1]+'</font>?';
				$.messager.confirm('提示', tips, function(r)
				{
					if(r)
					{
						if(true == redoTaskCycleC(taskIdCycleIdStates[0],taskIdCycleIdStates[1]))
						{
							//刷新页面
							if('s' == prefix)
							{
								loadRS(taskIdCycleIdStates[0],taskIdCycleIdStates[1]);
							}
							else
							{
								var taskId = covTaskName2Id($("#taskIdList").combobox('getValue'));
								var cycleId = $("#cycleIdList").combobox('getValue');
								loadDependRSList(taskId,cycleId);
							}
						}
					}
				});
		}
	}
</script>
</head>

<body>
	<div style="margin :5px 0px 0px 0px;margin-left: auto; margin-right: auto; background: #fff; border: 1px solid #ccc;width:1100px;">
		<form id="taskRunningDetail" name="taskRunningDetail"
			action="queryTaskRunningDetail" method="post">
			<table width="550" style="margin-left: auto; margin-right: auto;">
				<tr>
					<td><span>任务名称: </span>
					</td>
					<td><input class="easyui-combobox" id="taskIdList"
						name="taskRs.taskId" panelHeight="210px" value="<%=taskId%>" /><font
						color="red">*</font>
					</td>
					<td><span>周期ID: </span>
					</td>
					<td><input class="easyui-combobox" id="cycleIdList"
						name="taskRs.cycleId" panelHeight="200px" value="<%=cycleId%>"/><font color="red">*</font>
					</td>
					<td><a class="easyui-linkbutton" plain="false"
						iconCls="icon-search" href="javascript:void(0)"
						onclick="queryDetail()">查询 </a></td>
				</tr>
			</table>
		</form>
	</div>
	<div id="taskRunningStatesTabs" class="easyui-tabs" style="margin :5px 0px 5px 0px;margin-left: auto; margin-right: auto; background: #fff; border: 1px solid #ccc;width:1100px;" fit="false"
		plain="true">
		<div title="任务周期运行状态" style="padding: 10px;min-height:480px;">
			<table id="taskRunningState"></table>
		</div>
		<div title="依赖的任务周期运行状态" style="padding: 10px;min-height:480px;">
			<table id="dependingTaskRunningStates"></table>
		</div>
		<div title="批次运行状态" style="padding: 10px;min-height:480px;">
			<table id="batchRunningStates"></table>
		</div>
		<div title="步骤运行状态" style="padding: 10px;min-height:480px;">
			<table id="stepRunningStates"></table>
		</div>
		<div title="任务信息" style="padding: 10px;">
			<iframe id="taskframe" scrolling="yes" frameborder="0"
				style="width: 100%; height: 100%;min-height:480px;"> </iframe>
		</div>
		<div title="任务步骤信息" style="padding: 10px;">
			<iframe id="stepsframe" scrolling="yes" frameborder="0"
				style="width: 100%; height: 100%;min-height:480px;"> </iframe>
		</div>
		
		<div title="任务周期相关日志" style="padding: 10px;">
			<div style="margin-left: auto; margin-right: auto;width:1000px;color:#999999;background:#000000;min-height:480px;">
			 	<table id="tccLog" style="margin-left: auto; margin-right: auto;table-layout:fixed;width:1000px;color:#999999;background:#000000">
			 		<tr>
			 			<td style="word-wrap:break-word; overflow:hidden;width:100%">
			 			</td>
			 		</tr>
			 	</table>
		 	</div>
		 	<div id="logPag4tcc" style="margin-left: auto; margin-right: auto;width: 1000px;background:#efefef;border:1px solid #ccc;"></div>
		</div>
		
		<div title="远程壳输出日志" style="padding: 10px;">
			<table id="jobIdList"></table>
			
			<div style="margin-left: auto; margin-right: auto;width:1000px;color:#999999;background:#000000;min-height:480px;">
			 	<table id="rsLog" style="margin-left: auto; margin-right: auto;table-layout:fixed;width:1000px;color:#999999;background:#000000">
			 		<tr>
			 			<td style="word-wrap:break-word; overflow:hidden;width:100%">
			 			</td>
			 		</tr>
			 	</table>
		 	</div>
		 	<div id="logPag4rs" style="margin-left: auto; margin-right: auto;width: 1000px;background:#efefef;border:1px solid #ccc;">
			</div>
		</div>
		
	</div>
	<div style="margin-left: auto; margin-right: auto; background: #fff; border: 1px solid #ccc;width:1100px;">
			    <%@include file="footer.jsp"%>
	</div>
	
	<div id="smm" class="easyui-menu" style="display:none;width: 140px;">
		<div onclick="redoTaskCycle('s')">重新执行</div>
		<div onclick="viewReverseDependTree('s')">查看反向依赖任务树</div>
		<div onclick="viewDependTree('s')">查看正向依赖任务树</div>
	</div>
	
	<div id="dmm" class="easyui-menu" style="display:none;width: 140px;">
		<div onclick="redoTaskCycle('d')">重新执行</div>
		<div onclick="viewReverseDependTree('d')">查看反向依赖任务树</div>
		<div onclick="viewDependTree('d')">查看正向依赖任务树</div>
	</div>
</body>
</html>