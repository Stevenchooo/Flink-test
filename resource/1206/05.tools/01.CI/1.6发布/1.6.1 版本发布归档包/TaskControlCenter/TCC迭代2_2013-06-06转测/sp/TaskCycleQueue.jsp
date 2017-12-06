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
<title><s:text name="任务周期队列" /></title>
<script>
	//初始化对象
	$(function(){
		initTaskIdNames();		
	});


	//将yyyy-MM-DDThh:mm:ss格式转换为MM/DD/yyyy hh:mm:ss格式
	function covDate2OtherFormat(dateTimeStr)
	{
		if(null != dateTimeStr && "" != dateTimeStr)
		{
			dateTimeStr = dateTimeStr.split('.')[0];
			dateTimeStr = dateTimeStr.replace('T',' ');
			dateTimeArr = dateTimeStr.split(' ');
			dateArrStr=dateTimeArr[0].split('-');
			return dateArrStr[1]+"/"+dateArrStr[2]+"/"+dateArrStr[0]+" "+dateTimeArr[1];
		}
		else
		{
			return "";
		}
	}
	
	//计算日期的差值，返回hh:mm:ss时间差,dateStrStart格式"yyyyMMDDThh:mm:ss"
	function diffTimeStr(dateStrStart, dateStrEnd)
	{
		var diffStr="";
		if(null != dateStrStart && "" != dateStrStart
			&& null != dateStrEnd	&& "" != dateStrEnd)
		{
			dateStrStart = covDate2OtherFormat(dateStrStart);
			dateStrEnd = covDate2OtherFormat(dateStrEnd);
			var diffMillSec = Date.parse(dateStrEnd) - Date.parse(dateStrStart);
			var diff = diffMillSec;
			var hours = Math.floor(diff / (60*60*1000));
            diff = diff % (60*60*1000);
            var minutes = Math.floor(diff / (60*1000));
            diff = diff % (60*1000);
            var seconds = Math.floor(diff / 1000);
            diffStr = hours+":"+minutes+":"+seconds;
		}
		return diffStr;
	}
	
	//格式化显示hh:mm:ss时间差
	function formateTimeDiff(value)
	{
		var showDiff = "";
		if("" != value)
		{
			var times = value.split(':');
			if(times.length==3)
			{
				if(0 != times[0])
				{
					showDiff = '<font color="red">'+times[0]+'</font>小时';
				}
				if(0 != times[1])
				{
					showDiff += '<font color="red">'+times[1]+'</font>分钟';
				}
				if(0 != times[2] || ""==showDiff)
				{
					showDiff += '<font color="red">'+times[2]+'</font>秒';
				}
			}
			else
			{
				showDiff = value;
			}
		}
		return showDiff;
	}
	
	//初始化任务与名字键值对集合
	function initTaskIdNames()
	{
		checkSessionValid();
	    $.ajax({
			type:"post",
			url:"reqTaskIdNames",
			async : false,
			success : function(data, textStatus) 
			{
				if (textStatus == "success")
				{
					var datas = data.split("|");
					if (datas[0] == "true")
					{
						//提示是否继续选择
						var taskIdnames ='';
						for(var i = 1;i<datas.length;i++)
						{
							taskIdnames += datas[i];
						}
						initTaskIdNameMapByStr(taskIdnames);
					}
				}
				
				$('#taskRSQueueTabs').tabs({
				onSelect : function(title)
				{
					changeTabShow();
				}
				});
			}
		});
	}

	
	//加载任务周期队列
	function loadRsQueue(runningQueue)
	{	
		var widthTotal = 1000;
		var firstWidth = 40;
		var widths=[260,100,120,90,50,70,210];
		var left = widthTotal;
		left = left - firstWidth - 10;
		for(var i=0;i<widths.length;i++)
		{
			left = left - widths[i];
		}
		
		var tableId='';
		var dataUrl='';
		var prefix = '';
		var caption='';
		if(runningQueue)
		{
		    executeTimeName='已运行时间';
			tableId = 'taskRsRunningQueue';
			dataUrl = 'reqRsRunningQueueJson';
			prefix = 'r';
			caption = '运行队列中的任务周期集合';
		}
		else
		{
			executeTimeName='已等待时间';
			tableId = 'taskRsWaittingQueue';
			dataUrl = 'reqRsWaittingQueueJson';
			prefix = 'w';
			caption = '等待队列中的任务周期集合';
		}
		$('#'+tableId).datagrid({			onBeforeLoad: function(param)			{				if(!checkSessionValid())				{					return false;				}				else				{					return true;				}			},
			width : widthTotal,
			title : caption,
			nowrap : false,
			striped : true,
			loadMsg:'数据加载中,请稍候...',
			singleSelect:false,
			collapsible : true,
			url : dataUrl,
			sortName : '',
			remoteSort : true,
			idField : '',
			frozenColumns : [
			[
	            {
					field : 'taskId',
					title : '任务名',
					width : widths[0],
					align : 'center',
					rowspan : 1,
					formatter : function(value, rec)
					{
						return covTaskId2Name(value);
					}
				},
				{
					field : 'cycleId',
					title : '周期Id',
					align : 'center',
					width : widths[1],
					rowspan : 1,
					formatter : function(value, rec)
					{
						if(rec["startCycleId"] != value)
						{
							return "<font color='green'>"+value+"</font>";
						}
						else
						{
							return value;
						}
					}
				} ] ],
			columns : [
				[{
					title : '基本信息',
					align : 'center',
					colspan : 6
				}],
			 [{
				field : 'startTime',
				title : executeTimeName,
				align : 'center',
				width : widths[2],
				rowspan : 1,
				formatter : function(value, rec)
				{
					var dateStrStart = rec["startTime"];
					var dateStrEnd = rec["currentTime"];
					return formateTimeDiff(diffTimeStr(dateStrStart,dateStrEnd));
				}
			},
			{
				field : 'startCycleId',
				title : '起始周期Id',
				align : 'center',
				width : widths[3],
				rowspan : 1,
				formatter : function(value, rec)
				{
					if(rec["cycleId"] != value)
					{
						return "<font color='green'>"+value+"</font>";
					}
					else
					{
						return value;
					}
				}
			},
			{
				field : 'priority',
				title : '优先级',
				align : 'center',
				width : widths[4],
				rowspan : 1,
				formatter : function(value, rec)
				{
					return value;
				}
			},
			{
				field : 'cycleDependFlag',
				title : '顺序依赖',
				align : 'center',
				width : widths[5],
				rowspan : 1,
				formatter : function(value, rec)
				{
					if(value)
					{
						return "是";
					}
					else
					{
						return "否";
					}
				}
			},
			{
				field : 'dependIdList',
				title : '依赖关系(任务,全周期依赖,忽略错误)',
				align : 'center',
				width : widths[6],
				rowspan : 1,
				formatter : function(value, rec)
				{
					return dependList2table(value);
				}
			},
			{
				field : 'weight',
				title : '权重',
				align : 'center',
				width : left,
				rowspan : 1,
				formatter : function(value, rec)
				{
					return value;
				}
			}
			] ]
			,
			pagination : true,
			rownumbers : true,
			pageNumber:1,
		    pageSize:100,
   		    showPageList:true,
		    pageList: [50,100,200],
		    onRowContextMenu: function (e, field) {
                e.preventDefault();
                var datas = $('#'+tableId).datagrid('getData');
                var data = datas['rows'][field];
                setChoosedValue(data['taskId']+","+data['cycleId'],prefix);
  			    $('#'+prefix+'mm').menu('show', {
					left: e.pageX,
					top: e.pageY
				});
            },
            onLoadSuccess: function()
            {
            	if(runningQueue)
            	{
            		var datas = $('#'+tableId).datagrid('getData');
            		datas = datas['rows'];
            		var weight = 0;
            		for(var index in datas)
            		{
            			weight = weight + parseInt(datas[index]["weight"]);
            		}
            		var cap = caption+"[最大资源数(权重):<font color='red'>"+grabMaxWeight()+"</font>]";
            		cap += "[已占用资源数(权重):<font color='red'>"+weight;
            		cap += "</font>]";
            		//替换节点的名字
        			$('div[class*="panel-header"]').find('div[class*="panel-title"]').each(function(index,obj)
        			{
        				if(obj.innerHTML == caption)
        				{
        					obj.innerHTML = cap;
        				}
        			});
            	}
            }
		});
		
		var p = $('#'+tableId).datagrid('getPager');
		$(p).pagination({
		    showPageList:false,
		    beforePageText:'第',
		    afterPageText:'页，共{pages}页',
		    displayMsg:'当前显示从{from}到{to}，共{total}记录',
		    onBeforeRefresh:function(pageNumber,pageSize){
		     $(this).pagination('loading');
		     }
		   });
	}
	
	//获取最大权重值
	function grabMaxWeight()
	{
		var maxWeight = 0;
	   	checkSessionValid();
	$.ajax({
				type : "post",
				url : "grabMaxWeightHisRSCountState",
				async : false,
				success : function(data, textStatus) {
					if (textStatus == "success") {
						var datas = data.split('|');
						if (datas[0] == "true") {						
							{
								if (datas.length>=1)
								{
									maxWeight = datas[1];
								}
							}
						} 
					}
				}
			});

	   	return maxWeight;
	}
	
	var choosedValue = {};
	
	function setChoosedValue(value,prefix)
	{
		choosedValue[prefix] = value;
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
				    if(cols.length >= 1)
				    {
				    	tableStr +='<td>';
				    	tableStr +=covTaskId2Name(cols[0]);
				    	for(var i = 1;i<cols.length;i++)
				    	{
				    		tableStr +=','+(cols[i]=='1'?"是":"否");
				    	}
				    	tableStr +='</td>';
				    }
				    tableStr +='</tr>';
				}
		    }
			tableStr += '</table>';
	    }
		return tableStr;
	}

	function changeTabShow()
	{
		//当前选择tab才更新页面显示
		var tab = $('#taskRSQueueTabs').tabs('getSelected');
		if("TCC运行队列"==tab.panel('options').title)
		{
			loadRsQueue(true);
		}
		else if("TCC等待队列"==tab.panel('options').title)
		{
			loadRsQueue(false);
		}
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
							if('r' == prefix)
							{
								loadRsQueue(true);
							}
							else
							{
								loadRsQueue(false);
							}
						}
					}
				});
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
</script>
</head>

<body>
	<div id="taskRSQueueTabs" class="easyui-tabs"
		style="margin: 5px 0px 5px 0px; margin-left: auto; margin-right: auto; background: #fff; border: 1px solid #ccc; width: 1100px;"
		fit="false" plain="true">
		<div title="TCC运行队列" style="padding: 10px;min-height:490px;">
			<table id="taskRsRunningQueue"></table>
		</div>
		<div title="TCC等待队列" style="padding: 10px;min-height:490px;">
			<table id="taskRsWaittingQueue"></table>
		</div>
	</div>


	<div id="rmm" class="easyui-menu" style="display:none;width: 140px;">
		<div onclick="viewTaskRunningdetail('r')">查看任务周期详情</div>
		<div onclick="redoTaskCycle('r')">重新执行</div>
		<div onclick="viewReverseDependTree('r')">查看反向依赖任务树</div>
		<div onclick="viewDependTree('r')">查看正向依赖任务树</div>
	</div>
	<div id="wmm" class="easyui-menu" style="display:none;width: 140px;">
		<div onclick="viewTaskRunningdetail('w')">查看任务周期详情</div>
		<div onclick="redoTaskCycle('w')">重新执行</div>
		<div onclick="viewReverseDependTree('w')">查看反向依赖任务树</div>
		<div onclick="viewDependTree('w')">查看正向依赖任务树</div>
	</div>
</body>
</html>