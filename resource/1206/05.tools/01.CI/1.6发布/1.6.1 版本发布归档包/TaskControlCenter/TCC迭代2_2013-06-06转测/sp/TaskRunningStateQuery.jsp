<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="sx" uri="/struts-dojo-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>

<%
    String taskId = request.getParameter("taskId");
			if (null == taskId) {
				taskId = "0";
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
<script language="javascript" type="text/javascript"
	src="DatePicker/WdatePicker.js"></script>
<title><s:text name="任务运行状态查询" /></title>
<script language="javascript" type="text/javascript" charset="utf-8">
	//已经选择的依赖树根节点集合
	var choosedTaskRSNeedDependss = "";
	//已经选择的任务周期集合
	var choosedTaskRSs = "";
	//可以使用集成执行功能的任务信息，形如[{"0010000":{"taskId":0010000,"cycleType":d,"cycleLength":1,"cycleDepend":true}},{}]
	var canIntegratedTaskInfoArr = new Array();
	
	//初始化对象
	$(function() {
		loadVisibleServiceNameList();
		loadVisibleOsUsers();
		$('#taskRSQueryTabs').tabs({
			onSelect : function(title) {
				changeTabShow();
			}
		});
		
		//使用不同日期时间范围
		var currDate = new Date();
		var taskId = <%=taskId%>;
		if('0' != '' + taskId)
		{
			$('#startTime').val(getMouthDateString(currDate));
			$('#filterStartTime').val(getMouthDateString(currDate));
			currDate.setDate(currDate.getDate()+1);
			$('#endTime').val(getDateString(currDate));
			$('#filterEndTime').val(getDateString(currDate));
		}
		else
		{
			//某个任务的运行状态
			
			$('#endTime').val(getDateString(currDate));
			$('#filterEndTime').val(getDateString(currDate));
			currDate.setDate(currDate.getDate()-1);
			$('#startTime').val(getDateString(currDate));
			$('#filterStartTime').val(getDateString(currDate));
		}
	});
	
	function loadVisibleOsUsers()
	{
    	$('#vOsUsers').combobox({
			url : "getVisibleOsUsers?containAllCol=true",
			valueField : "key",
			textField : "value",
			title : 'OS用户',
			width : 155,
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
			width: 155,
			editable : false,
			onLoadSuccess:function()
			{
				$('#vserviceIdList').combobox("select",-1);
			}
		});
	}

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
				$.messager.defaults={ok:"确定",cancel:"取消"};
				$.messager.alert('提示','请仅选择一行记录!','info');
				$('#stepList').datagrid('clearSelections');
			}
		}
		else
		{
			$.messager.defaults={ok:"确定",cancel:"取消"};
			$.messager.alert('提示','请选择一行记录!','info');
		}
	}

	function vaildData() {
		return true;
	}

	//初始化对象
	$(function() {
		$('#taskIdList').combobox({
			url : "reqTaskIdJsonObject?containAllCol=true",
			valueField : "key",
			textField : "value",
			editable : true,
			width : 220,
			onLoadSuccess : function() {
				initTaskIdNameMap('taskIdList');
				var taskId = covTaskName2Id($("#taskIdList").combobox('getValue'));
				var taskId4Url = <%=taskId%>;
				if("" == taskId4Url)
				{
					if("" == taskId)
					{
						$('#taskIdList').combobox("select",0);
					}
				}
				else
				{
					$('#taskIdList').combobox("select",taskId4Url);
					var taskName = $('#taskIdList').combobox("getText")+";";
					$('#taskIdList').combobox("setValue",taskName);
				}
					
				loadRSList();
			}
		});
	});

	var rowIds = {'s':{},'c':{}};
	var cols = {};
	function mergeCol(prefix)
	{
		
		var rowsIdMap = rowIds[prefix];
		var rowIdTaskId;
		var rowIdTaskIdArr;
		var rowId;
		var taskId;
		for(var index in rowsIdMap)
		{
			rowIdTaskId = rowsIdMap[index];
			rowIdTaskIdArr = rowIdTaskId.split(',');
			rowId = parseInt(rowIdTaskIdArr[0]);
			taskId = parseInt(rowIdTaskIdArr[1]);
			
			$('div[id="'+prefix+'tid_'+rowId+'_'+taskId+'"]').parent('div').parent('td').parent('tr').find('td[field][field!="col0"]').remove();
			
			$('div[id="'+prefix+'tid_'+rowId+'_'+taskId+'"]').parent('div').parent('td').parent('tr').find('td[field][field="col0"]').each(function(index,obj){
				obj.colSpan = cols[prefix];
	    	});
			
			$('div[id="'+prefix+'tid_'+rowId+'_'+taskId+'"]').parent('div').removeAttr('style');
		}
	}
	
	function addCheckButton(rowId, value, prefix)
	{
		var taskIdCycleIdState = value.split(',');
		var rtnValue = "";
		
		if(taskIdCycleIdState.length == 3)
		{
			rtnValue += '<div id="'+prefix+'div_'+value+'" class="imgTextAlign" oncontextmenu=setChoosedValue(this,"'+value+'","'+prefix+'")>';
			if('c' == prefix)
			{
				rtnValue += '<input id="'+prefix+'tcid_'+rowId+'_'+value+'" type="checkbox" checked="checked"/>';
			}
			else
			{
				rtnValue += '<input id="'+prefix+'tcid_'+rowId+'_'+value+'" type="checkbox" />';
			}
			
			//rtnValue += covTaskId2Name(taskIdCycleIdState[0])+','+taskIdCycleIdState[1];
			rtnValue += '<span>'+taskIdCycleIdState[1]+'</span>';
			var state = taskIdCycleIdState[2];
			rtnValue += showState(state);
			
		}
		else
		{
			if(null != value && '' != value)
			{
				var rowValue = rowId+','+value;
				rowIds[prefix][rowValue] = rowValue;

				rtnValue += '<div id='+prefix+'tid_'+rowId+'_'+value+'><font style="font-weight:bold;" color="green">'+covTaskId2Name(value)+'</font></div>';
			}
		}
		rtnValue += '</div>';
		return rtnValue;
	}
	
	function selectRow(rowId,prefix)
	{
		var cchecked = $('[id='+prefix+'rowId_'+rowId+']').attr("checked");
		var checked;
		if("checked" == cchecked)
		{
			checked = true;
		}
		else
		{
			checked = false;
		}
		
		var count =0;
		$('[id^="'+prefix+'tcid_'+rowId+'_"]').each(function(index,obj){
	    		obj.checked=checked;
	    		count++;
	    	});
		//行中没有checkbox
		if(0 == count)
		{
			$('[id^="'+prefix+'tid_'+rowId+'_"]').each(function(index,obj){
				var taskId = parseInt(obj.id.split('_')[2]);
				selectAllCycleId(prefix,taskId,checked);
	    	});
		}
	}
	
	function selectAllCycleId(prefix,taskId,checked)
	{
		if(true == checked)
		{
			$('[id^="'+prefix+'tcid_"][id*="_'+taskId+'"]').each(function(index,obj){
				obj.checked=true;
	    	});
		}
		else
		{
			$('[id^="'+prefix+'tcid_"][id*="_'+taskId+'"]').each(function(index,obj){
				obj.checked=false;
	    	});
		}
	}
	
	function selectAllRows(prefix)
	{
		var checked = $('[id='+prefix+'allRows]').attr("checked");
		if("checked" == checked)
		{
			$('input:checkbox[id^="'+prefix+'"]').each(function(index,obj){
		    		obj.checked=true;
		    	});
		}
		else
		{
			$('input:checkbox[id^="'+prefix+'"]').each(function(index,obj){
	    		obj.checked=false;
	    	});
		}
	}
	
	//抓取运行中的任务Id集合
	function grabNormalTaskIds(choosed)
	{
		prefix = 's';
		var normalTaskIds = "";
		var dataUrl;
		if(null != choosed && true == choosed)
		{
			dataUrl = "grabNormalTaskIds?taskIds=" + getSelectedDistinctTaskIds(prefix);
		}
		else
		{
			dataUrl = "grabNormalTaskIds?taskIds="+getDistinctTaskIds(prefix);
		}
			
	    //异步获取集成的任务相关信息字段
		checkSessionValid();
		$.ajax({
			type:"post",
			url:dataUrl,
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
	
	//获取任务依赖树的所有任务集合
	function grabTaskDependedIds()
	{
		var rtnData = null;
		prefix = 's';
		var dataUrl = "grabTaskDependedIds?taskIds=" + getSelectedDistinctTaskIds(prefix);
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
	
	//将选择的任务周期提交到后台处理，isDependRoot表示是否是依赖树根节点结果
	function addTaskRSs(isDependedRoot,prefix)
	{
		var isSucessed = true;
 		var values = getSelectedTaskCycles(prefix);
 		if("" == values)
 		{
 			$.messager.defaults={ok:"确定",cancel:"取消"};
 			$.messager.alert('提示','请至少选择一个任务周期!','info');
 		}
 		else
 		{
 			if(isDependedRoot)
 			{
 				//获取任务依赖树的所有运行中的任务集合
 				var normalTaskIds = grabNormalTaskIds(true);
 				if("" != normalTaskIds)
 				{
 					$.messager.defaults={ok:"是",cancel:"否"};
 					$.messager.confirm('确认', "反向依赖的任务集合如下："+covTaskIds2Name(normalTaskIds,';')+",当前选择的依赖树仅仅是后台的一个快照(非完全实时)!<br/>为保证前后台同步,是否先手动将任务停止?", function(r){
 							if(r)
 							{
 								var url = "TaskList.jsp?taskId=" + normalTaskIds;
 								window.open(url);
 							}
 							else
 							{
 								choosedTaskRSNeedDependss += ";" + values;
 				 				$.messager.defaults={ok:"是",cancel:"否"};
 								$.messager.confirm('确认', "依赖树已全部添加成功!是否继续选择任务周期?", function(r){
 										if(r)
 										{
 								 			$('input:checkbox[id^="'+prefix+'"]').each(function(index,obj){
 									    		obj.checked=false;
 									    	});
 										}
 										else
 										{
 											selectTab('c');
 										}
 									});
 							}
 						});
 				}
 				else
 				{
 						choosedTaskRSNeedDependss += ";" + values;
		 				$.messager.defaults={ok:"是",cancel:"否"};
						$.messager.confirm('确认', "依赖树已全部添加成功!是否继续选择任务周期?", function(r){
								if(r)
								{
						 			$('input:checkbox[id^="'+prefix+'"]').each(function(index,obj){
							    		obj.checked=false;
							    	});
								}
								else
								{
									selectTab('c');
								}
							});
 				}
 			}
 			else
 			{
 				choosedTaskRSs += ";" + values;
 				$.messager.defaults={ok:"是",cancel:"否"};
 				$.messager.confirm('确认', "任务周期已全部添加成功!是否继续选择任务周期?", function(r){
					if(r)
					{
			 			$('input:checkbox[id^="'+prefix+'"]').each(function(index,obj){
				    		obj.checked=false;
				    	});
					}
					else
					{
						selectTab('c');
					}
				});
 			}
 			

 		}
 		
 		return isSucessed;
	}
	
	//获取已经选择的任务周期Id集合，用“;”分割,prefix为前缀，c或者s
	function getSelectedTaskCycles(prefix)
	{
		var values = "";
		var id = "";
		var tcidArray;
		$('input:checkbox[id^="'+prefix+'tcid_"][checked]').each(function(index,obj){
			id = obj.id;
			tcidArray = id.split("_");
			if (tcidArray.length == 3)
			{
				values += tcidArray[2]+";";
			}
    	});
		return values;
	}
	
	//加载运行状态
	function loadRSList(pageReset)
	{
		var taskId = covTaskName2Id($("#taskIdList").combobox('getValue'));
		if("" == taskId)
		{
			taskId = 0;
		}
		var startTime = $('#startTime').val();
		var endTime = $('#endTime').val();
		var state = $("#state").combobox('getValue');
		var choosedServiceId = $("#vserviceIdList").combobox('getValue');
		var osUser = $("#vOsUsers").combobox('getText');
		var widthTotal = 1000;
		var col = 5;
		var rowIdWidth = 60;
		var checkWidth = 30;
		var widthPerCol = parseInt((widthTotal-rowIdWidth-checkWidth)/col);
		cols['s'] = col;
		$('#taskRSList').datagrid({			onBeforeLoad: function(param)			{				if(!checkSessionValid())				{					return false;				}				else				{					return true;				}			},
			width : widthTotal,
			//height: 358,
			nowrap : false,
			striped : true,
			loadMsg:'数据加载中,请稍候...',
			singleSelect:false,
			collapsible : true,
			url : 'queryTaskRSList2Json?cols='+col,
			queryParams:{
				'taskName': "全部" == $("#taskIdList").combobox('getText')?'':encodeURI($("#taskIdList").combobox('getText')),
				'startCycleID' : startTime,
				'endCycleID' : endTime,
				'state' : "全部" == $("#state").combobox('getText')?"":state,
				'serviceId' : "全部" == $("#vserviceIdList").combobox('getText')?"":choosedServiceId,
				'osUser':("全部" == osUser?'':encodeURI(osUser))
				},
			sortName : 'rowId',
			remoteSort : true,
			idField : 'rowId',
			frozenColumns : [
			[
			{
	    	    title : '<input id="sallRows" type="checkbox" onClick=selectAllRows("s") />',
				field : 'rowId',
				width : checkWidth,
				align : 'center',
				formatter : function(value, rec) {
					return '<input id="srowId_'+value+'" type="checkbox" onClick=selectRow('+value+',"s") />';
				}
	     }] ],
			columns : [[
            {
				field : 'col0',
				title : '第一列',
				width : widthPerCol,
				align : 'center',
				rowspan : 1,
				formatter : function(value, rec) {
					var rowId =  rec["rowId"];
					return addCheckButton(rowId,value,'s');
				}
			},
			{
				field : 'col1',
				title : '第二列',
				align : 'center',
				width : widthPerCol,
				rowspan : 1,
				formatter : function(value, rec) {
					var rowId =  rec["rowId"];
					return addCheckButton(rowId,value,'s');
				}
			} 
			,
			{
				field : 'col2',
				title : '第三列',
				align : 'center',
				width : widthPerCol,
				rowspan : 1,
				formatter : function(value, rec) {
					var rowId =  rec["rowId"];
					return addCheckButton(rowId,value,'s');
				}
			} 
			,
			{
				field : 'col3',
				title : '第四列',
				align : 'center',
				width : widthPerCol,
				rowspan : 1,
				formatter : function(value, rec) {
					var rowId =  rec["rowId"];
					return addCheckButton(rowId,value,'s');
				}
			}
			,
			{
				field : 'col4',
				title : '第五列',
				align : 'center',
				width : widthPerCol,
				rowspan : 1,
				formatter : function(value, rec) {
					var rowId =  rec["rowId"];
					return addCheckButton(rowId,value,'s');
				}
			}
			] ],
			toolbar : [ {
				id : 'btnadd',
				text : '添加',
				iconCls : 'icon-choose-all',
				handler : function() {
					addTaskRSs(false,'s');
				}
			}
			,'-', {
				id : 'btnaddDeps',
				text : '添加反向依赖树',
				iconCls : 'icon-choose-all-tree',
				handler : function() {
					addTaskRSs(true,'s');
				}
			}]
			,
			pagination : true,
			rownumbers : false,
		    showPageList:true,
		    pageNumber:1,
		    pageList: [100,200,500,1000],
		    onLoadSuccess : function()
		    {
		    	mergeCol('s');
		    	$('[id=sallRows]').each(function(index,obj){
		    		obj.checked=false;
		    	});
		    },
		    onRowContextMenu: function (e, field) {
                e.preventDefault();
                if(true == currChoosed['s'])
                {
	  			    $('#smm').menu('show', {
						left: e.pageX,
						top: e.pageY
					});
	  			  currChoosed['s'] = false;
                }
            }
		});
		
		var p = $('#taskRSList').datagrid('getPager');
		$(p).pagination({
		    beforePageText:'第',
		    afterPageText:'页，共{pages}页',
		    displayMsg:'当前显示从{from}到{to}，共{total}记录',
		    onBeforeRefresh:function(pageNumber,pageSize){
		     $(this).pagination('loading');
		     }
		   });
	}

	function changeTabShow()
	{
		//当前选择tab才更新页面显示
		var tab = $('#taskRSQueryTabs').tabs('getSelected');
		if("源任务周期集合"==tab.panel('options').title)
		{
		}
		else if("待处理任务周期集合"==tab.panel('options').title)
		{
			loadChoosedRSList();
		}
		else
		{
			showRelatedTaskInfos();
		}
	}
	
	var preRelatedTaskIds = "";
	function showRelatedTaskInfos()
	{
		var prefix = 'c';
		var relatedTaskIds = getDistinctTaskIds(prefix);
		
		if(relatedTaskIds == "")
		{
			$.messager.defaults={ok:"确认",cancel:"取消"};
			$.messager.confirm('确认', "请先从[源任务周期集合]选择任务周期到[待处理任务周期集合]!", function(r){
					if(r)
					{
						selectTab('s');
					}
					else
					{
						//var url = "TaskList.jsp?taskId=" + relatedTaskIds;
						//$('#taskframe').attr("src", url);
					}
				});
		}
		else
		{
			if(preRelatedTaskIds == relatedTaskIds)
			{
				return;
			}
			else
			{
				preRelatedTaskIds = relatedTaskIds;
			}
			
			var url = "TaskList.jsp?taskId=" + relatedTaskIds;
			$('#taskframe').attr("src", url);
		}
	}
	
	function selectTab(prefix)
	{
		if('s' == prefix)
		{
			//当前选择tab才更新页面显示
			$('#taskRSQueryTabs').tabs('select',"源任务周期集合");
		}
		else if('c' == prefix)
		{
			//当前选择tab才更新页面显示
			$('#taskRSQueryTabs').tabs('select',"待处理任务周期集合");
		}
		else
		{
			$('#taskRSQueryTabs').tabs('select',"相关任务信息");
		}
	}
	
	function redoAllInternal(filterNoPri,values)
	{
		if(true != filterNoPri)
		{
		   filterNoPri = false;
		}
		
		if(null == values)
		{
		    values = getSelectedTaskCycles('c');
		}
		
		//异步提交选择的周期数据
		checkSessionValid();
		$.ajax({
			type:"post",
			url:"redoAll",
			data : $.param({
				"choosedRedoTaskRSs" : values,
				"filterNoPriTasks":filterNoPri
			}),
			async : false,
			success : function(data, textStatus) {
				if (textStatus == "success") {
				    var jsonD = $.parseJSON(data);
					if (jsonD.success) {
						isSucessed = true;
						loadChoosedRSList(true);
						
						//提示是否继续选择
						$.messager.defaults={ok:"确定",cancel:"取消"};
						$.messager.alert('提示','提交重做请求成功!','info');
						
					}
					else
					{
						
						isSucessed = false;
						
						if(null != jsonD.extValue && 2 == jsonD.returnValue2PageType)
						{
							showTPConfirm("<font color='red'>没有如下任务的操作权限，请检查是否忽略对下面任务的处理?</font>",jsonD.extValue,redoAllInternal,true);
							return isSucessed;
						}
						else
						{
							$.messager.defaults={ok:"确定",cancel:"取消"};
							$.messager.alert('提示','提交重做请求失败!','error');
						}
					}
				}
			}
		});
	}
	
	//将选择的任务周期提交到后台处理，isDependRoot表示是否是依赖树根节点结果
	function redoAll()
	{
		var isSucessed = true;
	
 		var values = getSelectedTaskCycles('c');
 		if("" == values)
 		{
 			$.messager.defaults={ok:"确定",cancel:"取消"};
 			$.messager.alert('提示','请至少选择一个任务周期!','info');
 		}
 		else
 		{
 			$.messager.defaults={ok:"是",cancel:"否"};
			var tips = '你确定要重做选定的任务周期?';
				$.messager.confirm('提示', tips, function(r)
				{
					if(r)
					{
						redoAllInternal(false,values);
					}
				});
 		}
 		
 		return isSucessed;
	}
	
	function reChoose(prefix)
	{
		choosedTaskRSs = "";
		choosedTaskRSNeedDependss = "";
		
		selectTab('s');
	}
	
	function getDistinctTaskIds(prefix)
	{
		//传递的任务id集合，以";"分割
		var values = "";
		var id = "";
		var tcidArray;
		var hash = {};
		var taskId;
		$('input:checkbox[id^="'+prefix+'tcid_"]').each(function(index,obj){
			id = obj.id;
			tcidArray = id.split("_");
			if (tcidArray.length == 3)
			{
				taskId = tcidArray[2].split(",")[0];
				if(hash[taskId] != true)
				{
					values += taskId +";";
					hash[taskId] = true;
				}
			}
    	});
		return values;
	}
	
	function getSelectedDistinctTaskIds(prefix)
	{
		
		//传递的任务id集合，以";"分割
		var values = "";
		var id = "";
		var tcidArray;
		var hash = {};
		var taskId;
		if(null == prefix)
		{
			prefix = 'c';
		}
		$('input:checkbox[id^="'+prefix+'tcid_"][checked]').each(function(index,obj){
			id = obj.id;
			tcidArray = id.split("_");
			if (tcidArray.length == 3)
			{
				taskId = tcidArray[2].split(",")[0];
				if(hash[taskId] != true)
				{
					values += taskId +";";
					hash[taskId] = true;
				}
			}
    	});
		return values;
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
						canIntegratedTaskInfoArr = new Array();
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
	
	//为了显示表格
	var preChoosedTaskRSs = "~!@";
	var preChoosedTaskRSNeedDependss = "~!@";
	//加载运行状态
	function loadChoosedRSList(refresh)
	{
		//如果选择没有改变，那么就不绑定数据
		if(choosedTaskRSs == preChoosedTaskRSs
			&& choosedTaskRSNeedDependss == preChoosedTaskRSNeedDependss)
		{
			//是否强制刷新
			if(null == refresh || false == refresh)
			{
				return;
			}
		}
		else
		{
			preChoosedTaskRSs = choosedTaskRSs;
			preChoosedTaskRSNeedDependss = choosedTaskRSNeedDependss;
		}
		
		var widthTotal = 1000;
		var col = 5;
		var rowIdWidth = 60;
		var checkWidth = 20;
		var emptyWidth = 10;
		var widthPerCol = parseInt((widthTotal-rowIdWidth-checkWidth-emptyWidth)/col);
		cols['c'] = col;
		
		var dataUrl = 'getChoosedTaskRSList2Json?cols=5&maxcount=500';
		
		//如果需要强制刷新
		if(true == refresh)
		{
			dataUrl += '&refresh=true';
		}
		
		$('#choosedTaskRSList').datagrid({			onBeforeLoad: function(param)			{				if(!checkSessionValid())				{					return false;				}				else				{					return true;				}			},
			width : 1000,
			//height : 378,
			queryParams:{
			'ctrss' : choosedTaskRSs,
			'ctrsnds' : choosedTaskRSNeedDependss
			},
			method : 'post',
			nowrap : false,
			striped : true,
			loadMsg:'数据加载中,请稍候...',
			singleSelect:false,
			collapsible : true,
			url : dataUrl,
			sortName : 'rowId',
			remoteSort : true,
			idField : 'rowId',
			frozenColumns : [
			[
			{
	    	    title : '<input id="callRows" type="checkbox" checked="checked" onClick=selectAllRows("c") />',
				field : 'rowId',
				width : rowIdWidth,
				align : 'center',
				formatter : function(value, rec) {
					return '<input id="crowId_'+value+'" type="checkbox" checked="checked" onClick=selectRow('+value+',"c") />';
				}
	     }] ],
			columns : [[
            {
				field : 'col0',
				title : '第一列',
				width : widthPerCol,
				align : 'center',
				rowspan : 1,
				formatter : function(value, rec) {
					var rowId =  rec["rowId"];
					return addCheckButton(rowId,value,'c');
				}
			},
			{
				field : 'col1',
				title : '第二列',
				align : 'center',
				width : widthPerCol,
				rowspan : 1,
				formatter : function(value, rec) {
					var rowId =  rec["rowId"];
					return addCheckButton(rowId,value,'c');
				}
			} 
			,
			{
				field : 'col2',
				title : '第三列',
				align : 'center',
				width : widthPerCol,
				rowspan : 1,
				formatter : function(value, rec) {
					var rowId =  rec["rowId"];
					return addCheckButton(rowId,value,'c');
				}
			} 
			,
			{
				field : 'col3',
				title : '第四列',
				align : 'center',
				width : widthPerCol,
				rowspan : 1,
				formatter : function(value, rec) {
					var rowId =  rec["rowId"];
					return addCheckButton(rowId,value,'c');
				}
			}
			,
			{
				field : 'col4',
				title : '第五列',
				align : 'center',
				width : widthPerCol,
				rowspan : 1,
				formatter : function(value, rec) {
					var rowId =  rec["rowId"];
					return addCheckButton(rowId,value,'c');
				}
			}
			] ],
			toolbar : [ {
				id : 'btnredoall',
				text : '重做',
				iconCls : 'icon-redo-all',
				handler : function() {
					redoAll();
				}
			}
			,'-', {
				id : 'btnintredoall',
				text : '集成重做',
				iconCls : 'icon-integrated-redo-all',
				handler : function() {
					integratedRedo('c');
				}
			}
			,
			'-'
			,
			{
				id : 'btnreChoose',
				text : '重新选择',
				iconCls : 'icon-rechoose',
				handler : function() {
					reChoose('c');
				}
			}
			]
			,
			pagination : false,
			rownumbers : true,
		    onLoadSuccess : function()
		    {
		    	mergeCol('c');
		    	if(true != refresh)
				{
			    	$('[id=callRows]').each(function(index,obj){
			    		obj.checked=true;
			    	});
				}
		    	//alarmNotEnough
		    	var datas = $('#choosedTaskRSList').datagrid("getData");
		    	if(true == datas["alarmNotEnough"])
		    	{
		    		$.messager.defaults={ok:"确认"};
					$.messager.alert("<font color='red'>警告</font>","由于依赖的任务周期过多，任务周期并没有全部展示，请使用<font color='red'>任务批量重做功能</font>，谢谢！","warn");
		    	}
		    	else if(null != datas["noPriTasks"])
		    	{
		    	   var noPriTasks = datas["noPriTasks"];
		    	   if(null != noPriTasks && noPriTasks.length > 0)
		    	   {
		    	       showTPTips("<font color='red'>没有如下任务的查看权限，已经忽略相应任务周期的展示!</font>",noPriTasks);
		    	   }
		    	}

		    },
		    onRowContextMenu: function (e, field) {
                e.preventDefault();
                if(true == currChoosed['c'])
                {
	  			    $('#cmm').menu('show', {
						left: e.pageX,
						top: e.pageY
					});
	  			  currChoosed['c'] = false;
                }
            }
		});
	}
	
	function addTaskParamsRows(taskId,redoStartTime,redoEndTime,redoDayLength)
	{		
		var row = new Array();
		row["taskId"] = taskId;
		row["redoStartTime"] =redoStartTime;
		row["redoEndTime"] =redoEndTime;
		row["cycleDepend"] ="true";
		row["redoDayLength"] =redoDayLength;
		$('#integratedTaskParams').datagrid("appendRow",row);
	}
	
	//过滤掉选择的非连续任务周期
	function integratedRedo(prefix)
	{
		var hasChoosed = false;
		$('input:checkbox[id^="'+prefix+'tcid_"][checked]').each(function(){
			hasChoosed = true;
    	});
		
		if(false == hasChoosed)
		{
			$.messager.defaults={ok:"确认",cancel:"取消"};
			$.messager.alert("提示","请至少选择一个任务周期","info");
			return;
		}
		
		grabCanIntegratedTaskInfos();
		loadIntegratedTaskParams();
		var cancelTaskIdsAnoDependTaskIds = filterChoosedByVaildTaskId();
		var cancelTaskIds = cancelTaskIdsAnoDependTaskIds[0];
		var noDependTaskIds = cancelTaskIdsAnoDependTaskIds[1];
		//选择连续问任务周期，不连续的任务周期取消选择
		var values = getSelectedTaskCycles(prefix);
		var cancelDisTasks = "";
		var taskIdcycleIdStates = values.split(";");
		var taskId;
		var cycleId;
		var taskIdcycleIdState;
		var taskInfo = null;
		var preTaskId = "";
		var startTime = {};
		var preCycleId = {};
		var cancelDis = {};
		for(var index in taskIdcycleIdStates)
		{
			if("" != taskIdcycleIdStates[index])
			{
				taskIdcycleIdState = taskIdcycleIdStates[index].split(",");
				taskId = taskIdcycleIdState[0];
				cycleId = taskIdcycleIdState[1];
				if(taskId != preTaskId)
				{
					taskInfo = canIntegratedTaskInfoArr[taskId];
					preTaskId = taskId;
					preCycleId[taskId] = cycleId;
					startTime[taskId] = covCycleId2Date(cycleId);
					continue;
				}
				
				if(null == taskInfo)
				{
					continue;	
				}
				
				if(cycleId != nextCycleId(preCycleId[taskId],taskInfo["cycleType"],taskInfo["cycleLength"]))
				{
					//取消选择
					cancelChoosedByTaskCycleId(taskId,cycleId);
					cancelDis[taskId] = taskId;
					continue;
				}
				else
				{
					//说明是连续的
					preCycleId[taskId]  = cycleId;
				}
				
			}
		}
		
		for(var index in cancelDis)
		{
			cancelDisTasks += cancelDis[index] + ";";
		}
		
		var tips = "";
		if("" != cancelTaskIds)
		{
			tips += '集成重做功能仅对天、小时或者分钟周期类型的非批次任务有效!已经取消任务'+covTaskIds2Name(cancelTaskIds,';')+'的所有的周期的选择.';
		}
		if("" != cancelDisTasks )
		{
			if("" != tips)
			{
				tips += "<br/><br/>";
			}
			tips += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;集成重做功能仅对连续的任务周期有效!已经取消任务'+covTaskIds2Name(cancelDisTasks,';')+'中不连续的任务周期.';
		}
		if("" != noDependTaskIds)
		{
			if("" != tips)
			{
				tips += "<br/><br/>";
			}
			tips += '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;集成重做功能仅对顺序依赖任务有效!'+covTaskIds2Name(noDependTaskIds,';')+'为非顺序依赖任务,已经取消这些任务周期的选择.';
			
			if("" != tips)
			{
					var taskIds = noDependTaskIds.split(";");
					for(var index in taskIds)
					{
						if("" != taskIds[index])
						{
							cancelChoosedByTaskId(taskIds[index]);
						}
					}
					
					var ignore;
					//清除元素数据
					$('#integratedTaskParams').datagrid('loadData', {total:0,rows:[]});
					//添加任务字段行信息
					for(var index in canIntegratedTaskInfoArr)
					{
						ignore = false;
						var taskId = canIntegratedTaskInfoArr[index]["taskId"];
						if(null != startTime[taskId])
						{
						var taskIds = noDependTaskIds.split(";");
						for(var index in taskIds)
						{
							if(taskId == taskIds[index])
							{
								ignore = true;
								break;
							}
						}
							
							if(false == ignore)
							{
								var redoStartTime = startTime[taskId];
								var redoEndTime = covCycleId2Date(preCycleId[taskId]);
								var redoDayLength = 1;
								addTaskParamsRows(taskId, redoStartTime, redoEndTime, redoDayLength);
							}
						}
					}
 					
					$.messager.defaults={ok:"确认",cancel:"取消"};
					$.messager.confirm('确认', tips, function(r){
 					//显示弹出窗口
 						showPopTaskInfos();
					});
			}
		}
		else
		{
			if("" != tips)
			{
				$.messager.defaults={ok:"确认",cancel:"取消"};
				$.messager.confirm('确认', tips, function(r){
						//清除元素数据
						$('#integratedTaskParams').datagrid('loadData', {total:0,rows:[]});
						//添加任务字段行信息
						for(var index in canIntegratedTaskInfoArr)
						{
							ignore = false;
							var taskId = canIntegratedTaskInfoArr[index]["taskId"];
							if(null != startTime[taskId])
							{
								var redoStartTime = startTime[taskId];
								var redoEndTime = covCycleId2Date(preCycleId[taskId]);
								var redoDayLength = 1;
								addTaskParamsRows(taskId, redoStartTime, redoEndTime, redoDayLength);
							}
						}
						//显示弹出窗口
						showPopTaskInfos();
					});
			}
		}
		
		if("" == tips)
		{
			//清除元素数据
			$('#integratedTaskParams').datagrid('loadData', {total:0,rows:[]});
			//添加任务字段行信息
			for(var index in canIntegratedTaskInfoArr)
			{
				ignore = false;
				var taskId = canIntegratedTaskInfoArr[index]["taskId"];
				if(null != startTime[taskId])
				{
					var redoStartTime = startTime[taskId];
					var redoEndTime = covCycleId2Date(preCycleId[taskId]);
					var redoDayLength = 1;
					addTaskParamsRows(taskId, redoStartTime, redoEndTime, redoDayLength);
				}
			}
			//显示弹出窗口
			showPopTaskInfos();
		}
	}
	
	function submitIntegratedRedoInternal(filterNoPri)
	{
		var taskInfos = "";
		//获取要修改的任务信息
		var datas = $('#integratedTaskParams').datagrid('getData')["rows"];
		for(var index in datas)
		{
			if(null != datas[index])
			{
				taskInfos += datas[index]["taskId"];
				taskInfos += ",";
				taskInfos += formatDate(datas[index]["redoStartTime"]);
				taskInfos += ",";
				taskInfos += formatDate(datas[index]["redoEndTime"]);
				taskInfos += ",";
				taskInfos += datas[index]["redoDayLength"];
				taskInfos += ";";
			}
		}
		//获取选择的任务周期
		var taskCycles = getSelectedTaskCycles('c');
		
		if(true != filterNoPri)
		{
	   		filterNoPri = false;
		}
		
		//异步获取集成的任务相关信息字段
		checkSessionValid();
		$.ajax({
			type:"post",
			url:"integratedRedo",
			async : false,
			data : $.param({
					"taskInfos" : taskInfos,
					"taskCycles" : taskCycles,
					"filterNoPriTasks":filterNoPri
				}),
			success : function(data, textStatus) {
				if (textStatus == "success") {
				   			var jsonD = $.parseJSON(data);
							if (jsonD.success) {
								isSucessed = true;
								loadChoosedRSList(true);
								
								//提示是否继续选择
								$.messager.defaults={ok:"确定",cancel:"取消"};
								$.messager.alert('提示','提交集成重做请求成功!','info');
								
							}
							else
							{
								
								isSucessed = false;
								
								if(null != jsonD.extValue && 2 == jsonD.returnValue2PageType)
								{
									showTPConfirm("<font color='red'>没有如下任务的操作权限，请检查是否忽略对下面任务的处理?</font>",jsonD.extValue,submitIntegratedRedoInternal,true);
									return isSucessed;
								}
								else
								{
									$.messager.defaults={ok:"确定",cancel:"取消"};
									$.messager.alert('提示','提交集成重做请求失败!','error');
								}
							}
				}
			}
		});
			
	}
	
	//正在提交集成重做请求
	function submitIntegratedRedo()
	{
		$.messager.defaults={ok:"是",cancel:"否"};
		var tips = '你确定要集成重做选定的任务周期?';
			$.messager.confirm('提示', tips, function(r)
			{
				if(r)
				{
					submitIntegratedRedoInternal(false);
					cancel();
				}
			});
	}
	
	var choosedValue = {};
	var currChoosed = {};
	var preChoosedDivId = {};
	function setChoosedValue(obj,value,prefix)
	{	
		if(null != preChoosedDivId[prefix] && "" != preChoosedDivId[prefix])
		{
			$('div[id="'+preChoosedDivId[prefix]+'"]').each(function(index,obj){
				obj.style.backgroundColor="";
			});
		}
		
		obj.style.backgroundColor="red";
		choosedValue[prefix] = value;
		preChoosedDivId[prefix] = prefix+'div_'+value;
		currChoosed[prefix] = true;
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
	
	//处理取消按钮
	function cancel() {
		$("#hideOptITParams").attr("style","display:none");
		$("#popOptIntegratedTaskParams").window('close');
	}
	
	//根据时间范围过滤掉其它任务周期
	function filterRSList(prefix)
	{
		var cancelTaskIds = cancelCycleIdNoInChoosed(prefix);
		if("" != cancelTaskIds)
		{
			$.messager.defaults={ok:"确认",cancel:"取消"};
			$.messager.alert("提示","任务"+covTaskIds2Name(cancelTaskIds, ';')+"的任务周期Id有不在指定时间范围内的任务周期,已经过滤!","info");	
		}
		else
		{
			$.messager.defaults={ok:"确认",cancel:"取消"};
			$.messager.alert("提示","无需过滤,都满足要求!","info");
		}	
	}
	
	//过滤掉任务Id不在指定时间范围的任务周期
	function cancelCycleIdNoInChoosed(prefix)
	{
		var cancelTaskIds = "";
		var filterStartTime =  $('#filterStartTime').val();
		var filterEndTime =  $('#filterEndTime').val();
		var startCycleId = covDate2StartCycleId(covFormatDateStr2Date(filterStartTime));
		var endCycleId = covDate2CycleId(covFormatDateStr2Date(filterEndTime));
		var value;
		var taskIdCycleIdStateArr;
		var taskId;
		var cycleId;
		var hash = {};
		$('input:checkbox[id^="'+prefix+'tcid_"][checked]').each(function(index,obj){
			id = obj.id;
			tcidArray = id.split("_");
			if (tcidArray.length == 3)
			{
				value = tcidArray[2];
				taskIdCycleIdStateArr = value.split(',');
				if(3 == taskIdCycleIdStateArr.length)
				{
					cycleId = taskIdCycleIdStateArr[1];
					if(cycleId < startCycleId || cycleId > endCycleId)
					{
						//取消选择
						taskId = taskIdCycleIdStateArr[0];
						if(hash[taskId] != true)
						{
							cancelTaskIds += taskId +";";
							hash[taskId] = true;
						}
						
						$('input:checkbox[id="'+id+'"]').each(function(index,obj){
							obj.checked = false;
				    	});
					}
				}
			}
    	});
		return cancelTaskIds;
	}
	
	//cycleId格式yyyyMMdd-HHmm
	function covCycleId2Date(cycleId)
	{
		var year = parseInt(cycleId.substring(0,4),10);
		var mouth = parseInt(cycleId.substring(4,6),10)-1;
		var day = parseInt(cycleId.substring(6,8),10);
		var hour = parseInt(cycleId.substring(9,11),10);
		var minute = parseInt(cycleId.substring(11,14),10);
		return new Date(year,mouth,day,hour,minute,0,0);
	}
	
	//dateStr格式yyyy-MM-dd HH:mm:ss
	function covFormatDateStr2Date(dateStr)
	{
		var year = parseInt(dateStr.substring(0,4),10);
		var mouth = parseInt(dateStr.substring(5,7),10)-1;
		var day = parseInt(dateStr.substring(8,10),10);
		var hour = parseInt(dateStr.substring(11,13),10);
		var minute = parseInt(dateStr.substring(14,16),10);
		var second = parseInt(dateStr.substring(17,19),10);
		return new Date(year,mouth,day,hour,minute,second,0);
	}
	
	//将dateTime转换成cycleId格式yyyyMMdd-HHmm
	function covDate2CycleId(dateTime)
	{
		var year = dateTime.getFullYear();
		var mouth = dateTime.getMonth()+1;
		var day = dateTime.getDate();
		var hour = dateTime.getHours();
		var minute = dateTime.getMinutes();
		return fillLeftZero(year,4) + fillLeftZero(mouth,2) + fillLeftZero(day,2) +"-"+ fillLeftZero(hour,2)+fillLeftZero(minute,2);
	}
	
	function covDate2StartCycleId(startTime)
    {
        if (null == startTime || "" == startTime)
        {
            return "";
        }
      
		var second = startTime.getSeconds();
		if(second != 0)
		{
			 //往后偏移一分钟
			startTime.setMinutes(startTime.getMinutes()+1);
		}
		
		return covDate2CycleId(startTime);
    }
	
	//获取下一个周期Id
	function nextCycleId(cycleId,cycleType,cycleLength)
	{
		var dateTime = covCycleId2Date(cycleId);
		cycleType = cycleType.toLowerCase();
		var length = parseInt(cycleLength);
		switch (cycleType)
		{
			case 'y':
				dateTime.setYear(dateTime.getFullYear()+length);
				break;
			case 'm':
				dateTime.setMonth(dateTime.getMonth()+length);
				break;
			case 'd':
				dateTime.setDate(dateTime.getDate()+length);
				break;
			case 'h':
				dateTime.setHours(dateTime.getHours()+length);	
				break;
			case 'i':
				dateTime.setMinutes(dateTime.getMinutes()+length);	
				break;
		default:
			break;
		}
		return covDate2CycleId(dateTime);
	}
	
	function cancelChoosedByTaskId(taskId)
	{
		prefix = 'c';
		$('input:checkbox[id^="'+prefix+'tcid_"][id*="_'+taskId+',"][checked]').each(function(index,obj){
			obj.checked = false;
    	});
	}
	
	function cancelChoosedByTaskCycleId(taskId,cycleId)
	{
		prefix = 'c';
		$('input:checkbox[id^="'+prefix+'tcid_"][id*="_'+taskId+','+cycleId+',"][checked]').each(function(index,obj){
			obj.checked = false;
    	});
	}
	
	//过滤掉选择的无效任务ID对应的所有任务周期,有效的任务Id是指任务Id信息在canIntegratedTaskInfoArr中存在
	function filterChoosedByVaildTaskId()
	{
		//可以使用集成执行功能的任务信息，形如"0010000,d,1,1;0010001,h,1,1;",解释"任务Id,周期类型,周期长度,是否顺序依赖"
		
		//提示并取消已经选择的任务周期(任务Id不在canIntegratedTaskInfoArr数组中的)
		var taskIds = getSelectedDistinctTaskIds();
		var taskIdArr = taskIds.split(';');
		var cancelTaskIds = "";
		var noCycleDependTaskIds = "";
		var taskInfo;
		for(var index in taskIdArr)
		{
			if("" == taskIdArr[index])
			{
				continue;
			}
			
			if(!taskInfoArrContains(taskIdArr[index]))
			{
				cancelTaskIds += taskIdArr[index]+";";
				cancelChoosedByTaskId(taskIdArr[index]);
			}
			else
			{
				taskInfo = canIntegratedTaskInfoArr[taskIdArr[index]];
				if(taskInfo["cycleDepend"] == "false")
				{
					noCycleDependTaskIds += taskIdArr[index] +";";
				}
			}
		}
		return [cancelTaskIds,noCycleDependTaskIds];
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
	
	//集成重做时,加载需要修改的任务相关参数
	function loadIntegratedTaskParams()
	{	
		$('#integratedTaskParams').datagrid({			onBeforeLoad: function(param)			{				if(!checkSessionValid())				{					return false;				}				else				{					return true;				}			},
			title : '单击行[修改/保存]集成重做天数',
			width : 720,
			nowrap : false,
			striped : true,
			loadMsg:'数据加载中,请稍候...',
			singleSelect:false,
			collapsible : true,
			sortName : 'rowId',
			remoteSort : true,
			idField : 'rowId',
			columns : [[
            {
				field : 'taskId',
				title : '任务名称',
				width : 220,
				align : 'center',
				rowspan : 1,
				formatter : function(value, rec) {
					return covTaskId2Name(value);
				}
			},
			{
				field : 'redoStartTime',
				title : '重做开始时间',
				align : 'center',
				width : 130,
				rowspan : 1,
				formatter : function(value, rec) {
					return formatDate(value);
				}
			} 
			,
			{
				field : 'redoEndTime',
				title : '重做结束时间',
				align : 'center',
				width : 130,
				rowspan : 1,
				formatter : function(value, rec) {
					return formatDate(value);
				}
			} ,
			{
				field : 'cycleDepend',
				title : '是否顺序依赖',
				align : 'center',
				width : 100,
				rowspan : 1
			} 
			,
			{
				field : 'redoDayLength',
				title : '集成重做天数',
				align : 'center',
				width : 80,
				editor:{type:"numberbox",options:{min:1,max:100,precision:0}}
			}
			] ]
			,
			pagination : false,
			rownumbers : true,
		    onSelect:function(rowIndex, rowData){
					$('#integratedTaskParams').datagrid('beginEdit', rowIndex);
			},
			onUnselect:function(rowIndex, rowData){
				$('#integratedTaskParams').datagrid('acceptChanges');
				$('#integratedTaskParams').datagrid('endEdit', rowIndex);
			}
		});
	}
	
	
	//跳转到任务周期详情页面
	function viewTaskRunningdetail(prefix)
	{
		if (null != choosedValue[prefix] && "" != choosedValue[prefix])
		{
			var taskIdCycleIdStates = choosedValue[prefix].split(",");
		    var url = "TaskRunningDetail.jsp?taskId=" + taskIdCycleIdStates[0] + "&cycleId=" + taskIdCycleIdStates[1];
		    window.open(url);
		}
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
								loadRSList();
							}
							else
							{
								loadChoosedRSList(true);
							}
						}
					}
				});
		}
	}
	
</script>
</head>

<body>
	<div id="taskRSQueryTabs" class="easyui-tabs"
		style="margin: 0px 0px 5px 0px; margin-left: auto; margin-right: auto; background: #fff; border: 1px solid #ccc; width: 1100px;"
		fit="false" plain="true">
		<div title="源任务周期集合" style="padding: 10px;min-height:460px;">
			<div style="margin: 0px 0px 5px 0px; margin-left: auto; margin-right: auto; background: #fff; border: 1px solid #ccc; width: 1000px;">
				<form id="taskRunningDetail" name="taskRunningDetail"
					action="queryTaskRunningDetail" method="post">
					<table width="800" style="margin-left: auto; margin-right: auto;">
						<tr>
							<td><span>业务类型: </span>
							</td>
							<td>
							<input class="easyui-combobox" id="vserviceIdList" style="width: 155px" panelHeight="200px" editable="false" />
							</td>
							<td><span>任务名称: </span></td>
							<td><input class="easyui-combobox" id="taskIdList"
								name="taskRs.taskId" style="width:210px;" panelHeight="200px" value="<%=taskId%>" />
							</td>
							<td><span>起始日期： 
							</td>
							<td><input id="startTime" readonly="true" name="startTime" type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								value="2012-01-01 00:00:00" class="Wdate" /></td>

							<td rowspan="2" align="center" valign="center"><a
								class="easyui-linkbutton" plain="false" iconCls="icon-search"
								href="javascript:void(0)" onclick="loadRSList(true)">查询 </a>
							</td>
						</tr>
						<tr>
						<td><span>OS用户: </span></td>
						<td><input class="easyui-combobox" id="vOsUsers"
					editable="false" style="width: 155px" panelHeight="200px" /></td>
							<td><span>运行状态: </span></td>
							<td><select id="state" editable="false"  class="easyui-combobox"
								panelHeight="auto" name="taskStep.stepTaskType"
								style="width: 220px" required="true">
									<option value="-2">全部</option>
									<option value="0">初始化</option>
									<option value="1">开始</option>
									<option value="2">成功</option>
									<option value="3">出错</option>
									<option value="4">超时</option>
									<option value="5">虚拟成功</option>
									<option value="9">请求删除</option>
									<option value="11">文件未到达</option>
									<option value="12">等待切换节点执行</option>
							</select></td>
							<td><span>结束日期： 
							</td>
							<td><input id="endTime" readonly="true" name="endTime" type="text"
								onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
								value="2013-01-01 00:00:00" class="Wdate" /></td>

						</tr>
					</table>
				</form>
			</div>
			<table id="taskRSList"></table>
		</div>
		<div title="待处理任务周期集合" style="padding: 10px;min-height:460px;">
			<div
				style="margin: 0px 0px 5px 0px; margin-left: auto; margin-right: auto; background: #fff; border: 1px solid #ccc; width: 1000px;">
				<table width="600" style="margin-left: auto; margin-right: auto;">
					<tr>
						<td><span>起始日期： 
						</td>
						<td><input id="filterStartTime" readonly="true" name="filterStartTime"
							type="text"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
							value="2012-01-01 00:00:00" class="Wdate" /></td>
						<td><span>结束日期： 
						</td>
						<td><input id="filterEndTime" readonly="true" name="filterEndTime"
							type="text"
							onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})"
							value="2012-03-01 00:00:00" class="Wdate" /></td>
						<td rowspan="2" align="center" valign="center"><a
							class="easyui-linkbutton" plain="false" iconCls="icon-search"
							href="javascript:void(0)" onclick="filterRSList('c')">筛选 </a>
						</td>
					</tr>
				</table>
			</div>
			<table id="choosedTaskRSList"></table>
		</div>
		<div title="相关任务信息" style="padding: 10px;">
			<iframe id="taskframe" scrolling="yes" frameborder="0"
				style="width: 100%;min-height:460px;"> </iframe>

		</div>
	</div>

	<div id="popOptIntegratedTaskParams" class="easyui-window"
		inline="false" closed="true"
		style="width: 770px; height: 500px; padding: 10px">
		<div id="hideOptITParams" class="easyui-layout" fit="true"
			style="display: none">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<table id="integratedTaskParams"></table>
			</div>
			<div region="south" border="false"
				style="text-align: right; padding: 5px 0; height: 35px;">
				<a class="easyui-linkbutton" plain="true" iconCls="icon-ok"
					href="javascript:void(0)" onclick="submitIntegratedRedo()">提交 </a>
				<a class="easyui-linkbutton" plain="true" iconCls="icon-cancel"
					href="javascript:void(0)" onclick="cancel()">取消 </a>
			</div>
		</div>
	</div>
	
	<%@include file="taskPrivTips.jsp"%>
	
	<div id="cmm" class="easyui-menu" style="display:none;width: 140px;">
		<div onclick="viewTaskRunningdetail('c')">查看任务周期详情</div>
		<div onclick="redoTaskCycle('c')">重新执行</div>
		<!--<div onclick="viewLog('c')">查看日志</div>-->
		<div onclick="viewReverseDependTree('c')">查看反向依赖任务树</div>
		<div onclick="viewDependTree('c')">查看正向依赖任务树</div>
	</div>
	<div id="smm" class="easyui-menu" style="display:none;width: 140px;">
		<div onclick="viewTaskRunningdetail('s')">查看任务周期详情</div>
		<div onclick="redoTaskCycle('s')">重新执行</div>
		<!--  <div onclick="viewLog('s')">查看日志</div>-->
		<div onclick="viewReverseDependTree('s')">查看反向依赖任务树</div>
		<div onclick="viewDependTree('s')">查看正向依赖任务树</div>
	</div>
	<div
		style="margin-left: auto; margin-right: auto; background: #fff; border: 1px solid #ccc; width: 1100px;">
		<%@include file="footer.jsp"%>
	</div>
</body>
</html>