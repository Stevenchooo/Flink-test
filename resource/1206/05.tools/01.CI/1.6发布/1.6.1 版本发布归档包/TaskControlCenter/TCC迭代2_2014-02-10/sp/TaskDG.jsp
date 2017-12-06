<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.huawei.platform.tcc.utils.TccUtil"%>
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
<script type="text/javascript" src="js/d3.v2.js"></script>
<script language="javascript" type="text/javascript"
	src="DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="stylesheet" type="text/css" href="css/table.css" />
<link rel="stylesheet" type="text/css" href="themes/icon.css" />
<link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
<style>
  .select, .showControl
  {
     margin: 5px 0px 5px 0px;
     margin-left: auto;
     margin-right: auto;
     background: #fff;
     border: 1px solid #ccc;
     width: 1100px;
  }
  
  .show
  {
     margin: 5px 0px 5px 0px;
     margin-left: auto;
     margin-right: auto;
     background: #fff;
     width: 1100px;
  }
</style>
</head>

<body oncontextmenu="return false;">
	<%
	    String taskId = TccUtil.covValidTaskId(request.getParameter("taskId"));
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
					href="javascript:void(0)" onclick="searchTask()">查询 </a>
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
						<option value="D">按天</option>
						<option value="I">按分钟</option>
						<option value="H">按小时</option>			
						<option value="M">按月</option>
						<option value="Y">按年</option>
				</select></td>
			</tr>
		</table>
	</div>
	<div class="showControl">
		<table width="800" style="margin-left: auto; margin-right: auto;">
			<tr>
				<td><input type="checkbox" checked="true" onchange="changeShowName()" id="showName">显示任务名称</input></td>
				<td><input type="checkbox" checked="true" onchange="changeShowVisual()" id="showVisual">显示虚拟任务</input></td>
				<td><input type="checkbox" onchange="changeDirectDepend()" id="directDepend">只显示直接关联</input></td>
			</tr>
		</table>
	</div>
	<div class="show">
	<!--<digraph></digraph>-->
	<canvas id="myCanvas" width="1100" height="1500"></canvas>
	</div>
	
	<div id="mm" class="easyui-menu" style="display:none;width: 140px;">
	    <div onclick="viewDetail()">属性</div>
		<div id="startStopTask" onclick="startStopTask()"></div>
		<div onclick="showDepends(1,true)">显示前导任务</div>
		<div onclick="showDepends(2,true)">显示后继任务</div>
		<div onclick="showDepends(0,true)">显示关联任务</div>
	</div>
</body>

<script type="text/javascript" charset="utf-8">

$(function()
{
	initImgs();
});

function loadDatas()
{
	loadAllServiceIdNames();
   	loadVisibleServiceNameList();
	loadVisibleOsUsers();
	loadTaskIdList(true);
	searchTask();
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

//加载任务Id列表选择框
function loadServiceNameList(serviceId,taskGroup,showWindow)
{
	var url= "getOptServiceIdNameList?containAllCol=false";
	if(null != serviceId)
	{
		url +="&serviceId="+serviceId;
	}
	if(null != taskGroup)
	{
		url +="&taskGroup="+encodeURI(taskGroup);
	}
	$('#serviceid').combobox({
		url : url,
		valueField : "key",
		textField : "value",
		title:'业务类型',
		width: 220,
		editable : false,
		queryParams:{
			'taskGroup' : null == taskGroup ?"":encodeURI(taskGroup)
			},
		onSelect : function(record)
		{
			var choosedServiceId = record["key"];
			if(null != choosedServiceId)
			{
				loadTaskGroupList(choosedServiceId,serviceId,taskGroup);
			}
		},
		onLoadSuccess:function()
		{
			//如果选择了一行,优先设置选择的值
			var rows = $('#serviceid').combobox("getData");
			if(rows.length > 0)
			{
				if(null != serviceId)
				{
					$('#serviceid').combobox("select",serviceId);
				}
				else
				{
					if(null != rows[0]["key"])
					{
						$('#serviceid').combobox("select",rows[0]["key"]);
					}
				}
				
				if(null != showWindow && true == showWindow)
				{
					$("#popOptTask").window({
						modal : true,
						shadow : false,
						closed : false,
						title : "新增任务"
					});
				}
			}
			else
			{
				if(null != showWindow && true == showWindow)
				{
					$.messager.alert('提示','对不起, 不能添加新的任务!<br>权限不足:<br>没有<font color="red">任何业务组</font>的<font color="red">[完全]</font>权限','info');
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
				$('#taskIdList').combobox("select",0);
			}
			//对查询条件框赋值
			setSearchValue();
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


//加载任务信息列表到表格中
function loadDigraph()
{
	//将要查询的值
	var taskIdValue = $("#taskIdOut").val();
	var taskStateValue = $("#searchTaskState1").combobox('getValue');
	var taskTypeValue = $("#searchTaskType1").combobox('getValue');
	var cycleTypeValue = $("#searchCycleType1").combobox('getValue');
	var choosedServiceId = $("#vserviceIdList").combobox('getValue');
	var osUser = $("#vOsUsers").combobox('getText');
	checkSessionValid();
	$.ajax({
		type:"post",
		url:"reqTaskDigraph",
		data:$.param({
			'searchTaskId' : ("全部" == $("#taskIdList").combobox('getText')?'':encodeURI($("#taskIdList").combobox('getText'))),
			'searchTaskState' :("全部" == $("#searchTaskState1").combobox('getText'))?"" :taskStateValue,
			'searchCycleType' : "全部" == $("#searchCycleType1").combobox('getText')?'':cycleTypeValue,
			'searchTaskType' :  "全部" == $("#searchTaskType1").combobox('getText')?'':taskTypeValue,
			'searchServiceId' : "全部" == $("#vserviceIdList").combobox('getText')?'':choosedServiceId,
			'searchOsUser' : ("全部" == osUser?'':encodeURI(osUser))
			}),
		async : false,
		success : function(data, textStatus) {
			if (textStatus == "success") {
				var digraph = jQuery.parseJSON(data);
				//paint();
				startMainHigraph(digraph.links,digraph.hiNodes);
			}
		}
	});
}

function getServiceName(serviceID)
{
	return serviceIdName[serviceID];
}

//有条件查询要显示任务信息
function searchTask()
{
   loadDigraph();
}

	function showMenu(x,y)
	{
		for(var index in hiGraph.checkedNodes)
		{
			//目前只支持单选
			var node = hiGraph.checkedNodes[index];
			if(null != node)
			{
				var cmd = node.state==3?'启动':'停止';
				$("#mm").menu('setText',{target:$('#startStopTask'),text:cmd});
				$('#mm').menu('show', {left: x,top: y});
	    		
				break;
			}
	   }
	}
	
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////


var canvas=document.getElementById("myCanvas");
var ctx=canvas.getContext("2d");
//是否仅显示直接依赖
var directDepend = false;
//是否显示节点名称
var showName = true;
//是否显示虚拟任务
var showVisual = true;
//节点的半径
var r =5;
//画布宽度
var w = 1100;
var minw = 1100;
var dirw=150;
//最密列的间隔
var dirh = 50;
var subDirH = 60;
//全图
var hiGraph;

//当前图
var subHigraph; 

var imgs = [];

function changeShowName()
{
   if("checked" == $('#showName').attr("checked"))
   {
      showName = true;
   }
   else
   {
      showName = false;
   }
   
   if(null != hiGraph)
   {
      hiGraph.showName = showName;
   }
   
   if(null != subHigraph)
   {
      subHigraph.showName = showName;
   }
   
   paintAllHigraph();
}

function changeDirectDepend()
{
   if("checked" == $('#directDepend').attr("checked"))
   {
      directDepend = true;
   }
   else
   {
      directDepend = false;
   }
   
   if(null != hiGraph)
   {
      hiGraph.directDepend = directDepend;
   }
   
   if(null != subHigraph)
   {
      subHigraph.directDepend = directDepend;
   }
   
   showDepends();
}

function changeShowVisual()
{
   if("checked" == $('#showVisual').attr("checked"))
   {
      showVisual = true;
   }
   else
   {
      showVisual = false;
   }
   
   if(null != hiGraph)
   {
      hiGraph.showVisual = showVisual;
   }
   
   if(null != subHigraph)
   {
      subHigraph.showVisual = showVisual;
   }
   
   showDepends();
}

function viewDetail()
{
	for(var index in hiGraph.checkedNodes)
	{
		//目前只支持单选
		var node = hiGraph.checkedNodes[index];
		if(null != node)
		{
    		var taskId= node.id;
			var url = "TaskList.jsp?taskId=" + taskId;
		    window.open(url);
			break;
		}
	}
   
    //paintAllHigraph();
}

function startStopTask()
{
	for(var index in hiGraph.checkedNodes)
	{
		//目前只支持单选
		var node = hiGraph.checkedNodes[index];
		if(null != node)
		{
			value = node.state==3?0:1;
    		changeState(node,value);
    		
			break;
		}
	}
}

//改变后台任务状态
function changeState(node,value)
{
	var taskId= node.id;
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
							if(data.length > 2 && ('5' == data[1] || '3' == data[1]))
							{
								if('5' == data[1])
								{
									$.messager.defaults={ok:"确定",cancel:"取消"};
									$.messager.alert('提示','任务<font color="red">'+covTaskId2Name(taskId)+'</font>已经启动,无需再次启动!','info');
								}
							}
							
							//刷新状态
							node.state = value==1?3:2;//1为停止（3表示红色），0为启动（2表示绿色）
							paintAllHigraph();
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

function showDepends(dependType,openSub)
{
   if(null != hiGraph && null != dependType)
   {
      hiGraph.dependType = dependType;
   }
   
   if(null != subHigraph  || true == openSub)
   {
	   for(var index in hiGraph.checkedNodes)
	   {
		   //目前只支持单选
		   var node = hiGraph.checkedNodes[index];
		   if(null != node)
		   {
			  changeHigraphVisited(node, hiGraph.directDepend,hiGraph.dependType);
			  subHigraph = getVisitedHiGraph(hiGraph,0,node.y+node.r+7);
			  break;
		   }
	   }
   }
   
   paintAllHigraph();
}

function initImgs()
{
   //imgs[0] = new Image();
   //imgs[0].src = "themes/icons/clock_no.png";
   //imgs[1] = new Image();
   //imgs[1].src = "themes/icons/clock_ok.png";
   //imgs[2] = new Image();
   //imgs[2].src = "themes/icons/clock_disable.png"; 
   
   imgs[0] = new Image();
   imgs[0].src = "themes/icons/state_init.png";
   imgs[1] = new Image();
   imgs[1].src = "themes/icons/state_start.png";
   imgs[2] = new Image();
   imgs[2].src = "themes/icons/state_success.png"; 
   imgs[3] = new Image();
   imgs[3].src = "themes/icons/state_error.png";
   imgs[4] = new Image();
   imgs[4].src = "themes/icons/state_timeout.png";
   imgs[5] = new Image();
   imgs[5].src = "themes/icons/state_vsucess.png"; 
   imgs[6] = new Image();
   imgs[6].src = "themes/icons/state_noinit.png";
   imgs[7] = new Image();
   imgs[7].src = "themes/icons/state_waitrun.png";
   imgs[8] = new Image();
   imgs[8].src = "themes/icons/state_running.png"; 
   imgs[9] = new Image();
   imgs[9].src = "themes/icons/state_reqdelete.png";
   imgs[10] = new Image();
   imgs[10].src = "themes/icons/state_stop.png";
   imgs[11] = new Image();
   imgs[11].src = "themes/icons/state_nobatch.png"; 
   imgs[12] = new Image();
   imgs[12].src = "themes/icons/state_waitnext.png";
   
   for(var i=0;i<imgs.length;i++)
   {
   	  imgs[i].onload = initMain;
   }
}

function initMain(){
   allComlete = true;
    //只有等图片加载完毕后才能画图像
   for(var i=0;i<imgs.length;i++)
   {
   	  if(!imgs[i].complete)
      {
		 allComlete = false;
	  }
   }
   
   if(allComlete)
   {
        loadDatas();
   }
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

function startMainHigraph(links, nodeArr)
{
    hiGraph = initHigraph(links, nodeArr,showName,showVisual,directDepend,0,0,dirw,dirh,r,clickHiGraph);
    paintMainHigraph(hiGraph);
}

function initHigraph(slinks, nodeArr,showName,showVisual,directDepend,left,top,dirw,dirh,r,clickfun)
{
   var links = [];
   var nodes = {};
   //高度可变
   //计算总高度
   var maxYN = 0;
   for(var index in nodeArr)
   {
      if(nodeArr[index].length > maxYN)
	  {
	     maxYN = nodeArr[index].length;
	  }
   }
   maxYN = maxYN + 1;
   var w = dirw * (nodeArr.length + 1);
   var h = dirh * maxYN;
   
   //初始话层次图结构
   var curGraph = {links:links,nodes:nodes,visitedLinks:{},visitedNodes:{},checkedNodes:{},
                  showName:showName,
                  showVisual:showVisual,
                  directDepend:directDepend,
                  border:{l:left,t:top,w:w,h:h},
                  receiveEvent:function(x,y)
                  {
                     if(x >= subHigraph.border.l && y >= subHigraph.border.t
		      			&& x <= subHigraph.border.l+subHigraph.border.w
		      			&& y <= subHigraph.border.t + subHigraph.border.h)
						{
						   return true;
						}
						else
						{
						   return false;
						}
                  },
                  checkOne:function(node){
                       //仅选择当前值
                       for(var index in this.checkedNodes)
                       {
                          if(null != this.checkedNodes[index])
                          {
                          	this.checkedNodes[index].checked = false;
                          	this.checkedNodes[index] = null;
                          }
                       }
                       
                       node.checked = true;
                       this.checkedNodes[node.name]  = node;
                       
                       if(hiGraph != this)
                       {
                          hiGraph.checkOne(hiGraph.nodes[node.name]);
                       }
                  },
                  checkAdd:function(node){
                       //新增选择项                    
                       node.checked = true;
                       this.checkedNodes[node.name]  = node;
                  },
                  paint:function(ctx)
                  {
                     paintHigraph(this);
                  },
                  close:function()
                  {
                     if(this == subHigraph)
                     {
                        subHigraph = null;
                     }                   
                  },
                  onclick:clickfun,
                    oncontextmenu:function(x,y,e)
					{
				       var visited = false;
					   for(var index in this.checkedNodes)
					   {
					   	  var node = this.checkedNodes[index];
					   	  if(null == node)
	   					  {
	   						 continue;
	   					  }
					   	  var dis = (x-node.x)*(x-node.x)+(y-node.y)*(y-node.y)-node.r*node.r;
					   	  if(dis <=0)
					   	  {
						  	visited = true;
						  	break;
					   	  }
					   }
						
					   if(true == visited)
					   { 
						  showMenu(x+canvas.offsetLeft, y+canvas.offsetTop);
					   }
					   else
					   {
					      visited = this.onclick(x,y,e);
					      if(true == visited)
					      {
					         showMenu(x+canvas.offsetLeft, y+canvas.offsetTop);
					      }
					   }
				   }};
   var xL = nodeArr.length + 1;
   var xIndex = 0;
   var checked = false;
   while(xIndex < xL - 1)
   {
      var ynodes = nodeArr[xIndex];
      ynodes.sort();
      var yL = ynodes.length + 1;
      x = Math.floor(w / xL * (xIndex+1)) + left;
      var yIndex = 0;
      while(yIndex < yL - 1)
      { 
         y = Math.floor(h / yL * (yIndex+1)) + top;
         if(null != ynodes[yIndex].checked)
         {
            checked = ynodes[yIndex].checked;
         }
         else
         {
            checked = false;
         }
         
         var moreFLs = ynodes[yIndex].moreFLs | ynodes[yIndex].moreFromLinks;
         var moreTLs = ynodes[yIndex].moreTLs | ynodes[yIndex].moreToLinks;
         
         nodes[ynodes[yIndex].name] = createNode(ynodes[yIndex].taskId,ynodes[yIndex].name,ynodes[yIndex].type,x,y,r,checked,moreFLs,moreTLs,
         ynodes[yIndex].state,ynodes[yIndex].weight,ynodes[yIndex].ratio,xIndex+1,curGraph);
         if(true == nodes[ynodes[yIndex].name].checked)
         {
            curGraph.checkOne(nodes[ynodes[yIndex].name]);
         }
         yIndex++;
      }
      xIndex++;
   }
   //初始化link
   for(var index in slinks)
   {
      if(null != nodes[slinks[index].source] && null != nodes[slinks[index].target])
      {
      	links[index]=createLink(nodes[slinks[index].source],nodes[slinks[index].target],slinks[index].type,curGraph);
      }
   }

   initEvent();
  
   return curGraph;
}

function clickHiGraph(x,y,e)
{
   	 var checked = false;
   	 for(var index in hiGraph.nodes)
	 {
		var node = hiGraph.nodes[index];
		var dis = (x-node.x)*(x-node.x)+(y-node.y)*(y-node.y)-node.r*node.r;
		if(dis <=0)
	    {
			this.checkOne(node);
			checked = true;
			break;
		}
	 }
	 
	 if(this == hiGraph)
	 {
		if(null != subHigraph)
		{
		   subHigraph.close();
		}
		paintAllHigraph();
	 } 
	  
	 return checked;		 
}

function clickSubHiGraph(x,y,e)
{
     var checked = false;
   	 for(var index in subHigraph.nodes)
	 {
		var node = subHigraph.nodes[index];
		var dis = (x-node.x)*(x-node.x)+(y-node.y)*(y-node.y)-node.r*node.r;
		if(dis <=0)
	    {
		this.checkOne(node);
			checked = true;
			break;
		}
	 }
	 
	 //状态改变
	 if(true == checked)
	 {
	    subHigraph.paint(ctx);
	 }
}

function paintHigraph(hiGraph)
{
   if(null == hiGraph)
   {
     return;
   }
   paintRect(hiGraph.border);
   paintLinks(hiGraph.links);
   paintNodes(hiGraph.nodes); 
   //对选择的节点显示加强，同时做到分层
   paintLinks(hiGraph.visitedLinks);
   paintNodes(hiGraph.visitedNodes); 
   //画出选择的节点
   paintNodes(hiGraph.checkedNodes); 
}

//画主图
function paintMainHigraph()
{
   setCanvas(hiGraph.border.w,hiGraph.border.h);
   hiGraph.paint(ctx);
}

//先画主图，后画子图
function paintAllHigraph()
{ 
   if(null === hiGraph)
   {
      return;
   }

   var w = hiGraph.border.w;
   var h = hiGraph.border.h;
   if(null != subHigraph)
   {
   		if(subHigraph.border.w+subHigraph.border.l > w)
   		{
      		w = subHigraph.border.w+subHigraph.border.l;
   		}
   
   		if(subHigraph.border.h+subHigraph.border.t > h)
   		{
      		h = subHigraph.border.h+subHigraph.border.t;
   		}
   }
   setCanvas(w, h);
   hiGraph.paint(ctx);
   if(null != subHigraph)
   {
      subHigraph.paint(ctx);
   }
}

function createNode(id,name,type,x,y,r,checked,moreFLs,moreTLs,state,weight,ratio,col,hiGraph)
{
   return {name:name,
	id:id,
    type:type,
	x:x,
	y:y,
	col:col,
	r:r,
	visited:false,
	checked:checked,
	moreFLs:moreFLs,
	moreTLs:moreTLs,
	state:state,
	weight:weight,
	ratio:ratio,
	fromLinks:[],
	toLinks:[],
	hiGraph:hiGraph,
	changeVisited:function(state){
		   if(true == state)
		   {
			  hiGraph.visitedNodes[this.name]=this;
			  this.visited = true;
		   }
		   else
		   {
		      hiGraph.visitedNodes[this.name]=null;
			  this.visited = false;
		   }
		},
	paint:function(ctx)
	   {
	      if(this.type == 'visual' && false == this.hiGraph.showVisual)
          {
             return;
          }
           
	      var nodeColor;
	      var finishedColor;
		  var textColor;
	      if(true == this.visited)
		  {
		     nodeColor= "#FF0000";
			 textColor= "#FF0000";
			 finishedColor="#008B00";
			 //color= "#B5B5B5";
		  }
		  else
		  {
		     nodeColor= "#B5B5B5";
			 textColor= "#000000";
			 finishedColor="#008B00";
		  }
		  
		  if(true == this.checked)
		  {
		     nodeColor= "#FF0000";
			 textColor= "#FF0000";
			 finishedColor="#008B00";
			 //color= "#B5B5B5";
		  }
		  
		  this.r =  5+Math.floor(10 * weight); 
		  
		  //选中者先画文本
		  if(true == this.hiGraph.showName && true == this.checked)
		  {
		     drawName(ctx,this.checked,textColor,name,x,y+this.r);
		  }
		 
		  if(this.type=="visual")
		  {
		      drawVisual(this.state,this.checked,x,y,this.r);
		  }
		  else
		  {
		     //如果是运行状态
		    if(this.state == 8)
		    {
		       circle(ctx,this.checked,getStateColor(2),getStateColor(4),this.x,this.y,this.r,this.ratio);
		    }
		    else
		    {
		       var color = getStateColor(this.state);
		       //circle(ctx,this.checked,color,color,this.x,this.y,this.r,0);
		       drawNormal(this.state,this.checked,this.moreFLs,this.moreTLs,x,y,this.r);
		    }
		  }
		  
		  
		  //未选中则先画圈
		  if(true == this.hiGraph.showName && true != this.checked)
		  {
		     drawName(ctx,this.checked,textColor,name,x,y+this.r)
		  }
		}
	};
}

function getStateColor(state)
{
   var stateColor = {};
   //0 INIT 就绪  （淡蓝色）
   stateColor[0] = "#B0E0E6";
   //受限 （黄色）
   stateColor[1] = "#FFFF00";
   //排队 （绿色）
   stateColor[2] = "#008B00";
   //运行 （绿/灰）
   stateColor[3] = "#000000";
   //完成（灰色）
   stateColor[4] = "#C0C0C0";
   //异常（红色）
   stateColor[5] = "#FF0000";
   
   return stateColor[state];
}

function createLink(source,target,type,hiGraph)
{
   var link = {name:source.name+"->"+target.name,
	source:source,
	target:target,
	visited:false,
	type:type,
	hiGraph:hiGraph,
	changeVisited:function(state){
		   if(true == state)
		   {
			  hiGraph.visitedLinks[this.name]=this;
			  this.visited = true;
		   }
		   else
		   {
		      hiGraph.visitedLinks[this.name]=null;
		      this.visited = false;
		   }
		},
	paint:function(ctx)
	   {
	      if(false == this.hiGraph.showVisual && (this.source.type =='visual' || this.target.type == 'visual'))
	      {
	         return;
	      } 
	      
	      var color;
	      if(true == this.visited)
		  {
		     color= "#FF0000";
			 //color= "#008B00";
		  }
		  else
		  {
		     color= "#008B00";
		  }
		  
		  if("weak" == this.type)
		  {
		     dashLine(ctx,color,this.source.x,this.source.y,this.target.x,this.target.y);
		  }
		  else
		  {
		     line(ctx,color,this.source.x,this.source.y,this.target.x,this.target.y);
		  }
		}
	};
	source.fromLinks[source.fromLinks.length] = link;
	target.toLinks[target.toLinks.length] = link;
	return link;
}

//自适应高度和宽度
function setCanvas(w,h)
{
   canvas.width = w<minw?minw:w;
   canvas.height = h;
}

function initEvent()
{
   canvas = document.getElementById("myCanvas");
   canvas.ondblclick=function(e){
		e = e||event;
		var x = e.offsetX;
		var y = e.offsetY;
		
		if(null != subHigraph)
	    {
	        if(subHigraph.receiveEvent(x,y))
	        {
	           subHigraph.ondblclick(x,y,e);
	        }
	        else
	        {
	           hiGraph.ondblclick(x,y,e);
	        }
		}
		else
		{
		    hiGraph.ondblclick(x,y,e);
		}
	};
	canvas.onclick=function(e){
		e = e||event;
		var x = e.offsetX;
		var y = e.offsetY;
		
		if(null != subHigraph)
	    {
	        if(subHigraph.receiveEvent(x,y))
	        {
	           subHigraph.onclick(x,y,e);
	        }
	        else
	        {
	           hiGraph.onclick(x,y,e);
	        }
		}
		else
		{
		    hiGraph.onclick(x,y,e);
		}
	};
	canvas.oncontextmenu=function(e)
	{
		e = e||event;
		var x = e.offsetX;
		var y = e.offsetY;
		
		if(null != subHigraph)
	    {
	        if(subHigraph.receiveEvent(x,y))
	        {
	           subHigraph.oncontextmenu(x,y,e);
	        }
	        else
	        {
	           hiGraph.oncontextmenu(x,y,e);
	        }
		}
		else
		{
		    hiGraph.oncontextmenu(x,y,e);
		}
	}
}

function getVisitedHiGraph(hiGraph,x,y)
{
	//源列在新列中的个数
	var colN = {};
    
	var node;
	for(var index in hiGraph.visitedNodes)
	{
	    node = hiGraph.visitedNodes[index];
		if(null == colN[node.col])
		{
		   colN[node.col] = 1;
		}
		else
		{
			colN[node.col]++;
		}
	}
	
	//源列在新列中的位置
	var colPos = {};
	var i = 0;
	for(var index in colN)
	{
	    if(0 != colN[index])
		{
		   colPos[index]=i++;
		}
		else
		{ 
		   colPos[index]=null;
		}
	}
    
	var nodeArr = [];
	var pos;
	for(var index in hiGraph.visitedNodes)
	{
	    node = hiGraph.visitedNodes[index];
	    pos = colPos[node.col];
	   
	   if(null == nodeArr[pos])
	   {
	      //初始化数组
	      nodeArr[pos]=[];
	   }
	   
	   var moreFromLinks = false;
       //是否有反向依赖不完整
       for(var i in node.fromLinks)
       {
          var link = node.fromLinks[i];
          if(null == hiGraph.visitedLinks[link.name])
          {
             moreFromLinks = true;
             break;
          }
       }
       
       node.moreFromLinks = moreFromLinks;

       var moreToLinks = false;
       //是否有正向依赖不完整
       for(var i in node.toLinks)
       {
          var link = node.toLinks[i];
          if(null == hiGraph.visitedLinks[link.name])
          {
             moreToLinks = true;
             break;
          }
       }
       node.moreToLinks = moreToLinks;
       
	   nodeArr[pos][nodeArr[pos].length]=node;
	   
	   
	}
	
	var slinks=[];
	for(var index in hiGraph.visitedLinks)
	{
	   var link = hiGraph.visitedLinks[index];
	   slinks[slinks.length]={source:link.source.name,target:link.target.name,type:link.type};
	}
	//初始化子图
	return initHigraph(slinks, nodeArr,showName,showVisual,directDepend,0+5,y+10,dirw,subDirH,r,clickSubHiGraph);
}

function changeHigraphVisited(node, dDepend, dependType)
{
   clearHigraphVisited(hiGraph);
   //获取主图中的节点
   node = hiGraph.nodes[node.name];
   //是否是虚拟节点
   if(node.type =='visual' && node.hiGraph.showVisual == false)
   {
      return;
   }
   
   hiGraph.checkOne(node);
   var visited = !node.visited;
   node.changeVisited(visited);
   if(1 == dependType)
   {
       changeLHiState(node, dDepend,visited);
   }
   else if(2 == dependType)
   {
      changeRHiState(node, dDepend,visited);
   }
   else
   {
      changeLHiState(node, dDepend,visited);
      changeRHiState(node, dDepend,visited);
   }
}

//清楚层次图的状态
function clearHigraphVisited(hiGraph)
{
   for(var index in hiGraph.visitedNodes)
   {
      hiGraph.visitedNodes[index].visited = false;
   }
   
   for(var index in hiGraph.visitedLinks)
   {
      hiGraph.visitedLinks[index].visited = false;
   }
   
   hiGraph.visitedNodes={};
   hiGraph.visitedLinks={};
}

function changeRHiState(node,dDepend,visited)
{
   //是否是虚拟节点
   if(node.type =='visual' && node.hiGraph.showVisual == false)
   {
      return;
   }
   
   var rNodes=[];
   var link;
   //修改正向和反向关联线的状态
   for(var index in node.fromLinks)
   {
        link = node.fromLinks[index];
        //如果不显示虚拟节点则直接忽略
   	    if(link.target.type=='visual' && link.hiGraph.showVisual == false)
        {
           continue;
        }
       
        //仅处理没有处理过的状态
        if(visited !=link.visited)
		{
		   node.fromLinks[index].changeVisited(visited);
		   rNodes[rNodes.length]=link.target;
		}
   }
   
   //递归修改左右的树状态
   for(var index in rNodes)
   {  
      //仅处理没有处理过的状态
      if(visited != rNodes[index].visited)
	  {
		//修改节点状态
		rNodes[index].changeVisited(visited);
		//非直接依赖继续更新
		if(false == dDepend)
		{
			changeRHiState(rNodes[index],dDepend,visited);
		}
	  }
   }
}

function changeLHiState(node, dDepend, visited)
{
   //是否是虚拟节点
   if(node.type =='visual' && node.hiGraph.showVisual == false)
   {
      return;
   }
   
   var lNodes=[];
   //修改正向和反向关联线的状态
   var link;
   for(var index in node.toLinks)
   {
       link = node.toLinks[index];
       //如果不显示虚拟节点则直接忽略
   	   if(link.source.type == 'visual' && link.hiGraph.showVisual == false)
       {
          continue;
       }
       
       //仅处理没有处理过的状态
       if(visited != link.visited)
	   {
	     node.toLinks[index].changeVisited(visited);
		 lNodes[lNodes.length]=link.source;
	   }
   }
   
   //递归修改左右的树状态
   for(var index in lNodes)
   {
       //仅处理没有处理过的状态
       if(visited != lNodes[index].visited)
	   {
		  //修改节点状态
		  lNodes[index].changeVisited(visited);
		  //非直接依赖继续更新
		  if(false == dDepend)
		  {
			 changeLHiState(lNodes[index],dDepend,visited);
		  }
	  }
   }
}

function paintNodes(nodes)
{
    for(var index in nodes)
    {
        if(null != nodes[index])
        {
		   	nodes[index].paint(ctx);
		}
    }	
}

function paintLinks(links)
{
    for(var index in links)
    {
		links[index].paint(ctx);
    }
}

function paintRect(border)
{ 
   if(border.l == 0 && border.t == 0)
   {
	   //主图
	   rectW=border.w<minw?minw:border.w;
   }
   else
   {
	   //子图
	   rectW=border.w<minw-border.l-5?minw-border.l-5:border.w;
   }
   
   
   ctx.clearRect(border.l, border.t, rectW, border.h);
   
   //ctx.setLineDash([3]);
   //ctx.lineDashOffset = 3;
   ctx.strokeStyle="#FF0000";
   ctx.strokeRect(border.l,border.t,rectW,border.h);
}

function circle(ctx,checked,color1,color2,x,y,r,ratio)
{
    //保证可识别的视觉
    if(ratio != 0)
    {
       ratio = ratio<0.1?0.1:ratio;
    }
    
    if(ratio != 1)
    {
       ratio = ratio>0.9?0.9:ratio;
    }
    if(true == checked)
    {
    	ctx.fillStyle="#FFFF00";
    	ctx.beginPath();
		ctx.arc(x,y,r+3,0,Math.PI*2,false); 
		ctx.closePath();
		ctx.fill();
	}
    ctx.fillStyle=color1;
    ctx.beginPath();
	ctx.arc(x,y,r,0,Math.PI*2,false); 
	ctx.closePath();
	ctx.fill();
	ctx.beginPath();
	ctx.fillStyle=color2;
	ctx.moveTo(x,y);
	ctx.lineTo(x,y-r);
	ctx.arc(x,y,r,Math.PI*3/2,-Math.PI/2+Math.PI*2*ratio,false);
	ctx.closePath();
	ctx.fill();
	//ctx.beginPath();
	//ctx.strokeStyle="#000000";
	//ctx.arc(x,y,r,0,Math.PI*2,false);
	//ctx.closePath();
	//ctx.stroke();	   	
}


function line(ctx,color,x0,y0,x1,y1)
{
    ctx.strokeStyle=color;
    //ctx.setLineDash([]);
    //ctx.lineDashOffset = 0;
    ctx.beginPath();
	ctx.moveTo(x0,y0);
    ctx.lineTo(x1,y1);
	ctx.stroke(); 	
}

function dashLine(ctx,color,x0,y0,x1,y1)
{
    ctx.strokeStyle=color;
    //ctx.setLineDash([3]);
    //ctx.lineDashOffset = 3;
    ctx.beginPath();
    var x,y;
    var dirL = 3;
    var k = (y1-y0)/(x1-x0);
    var dirX = dirL/Math.sqrt(1+((y1-y0)*(y1-y0))/((x1-x0)*(x1-x0)));
    //是否画线段
    var line = false;
    if(x0 > x1)
    {
       //交换x0,x1,y0,y1
       var tmp = x1;
       x1 = x0;
       x0 = tmp;
       tmp = y1;
       y1 = y0;
       y0 = tmp;
    }
       
       x = x0;
       //从左往右画
       while(x < x1)
       {
          if(true == line)
          {
             ctx.lineTo(x,y);
          }
          else
          {
             ctx.moveTo(x,y);
          }
          line = !line;
          
          x =  x + dirX;
          y =  (x-x0)*k + y0;
          if(x > x1)
          {
             x = x1;
             y = y1;
          }
       }
   
	ctx.stroke(); 
}

function drawName(ctx,checked,color,name,x,y)
{
    var nArr = name.split(',')
    var taskName = nArr[0];
    var cycleId= nArr[1];
    //画任务名
	text(ctx,checked,color,taskName,x,y);
	if(null != cycleId)
	{
	  //画周期Id
      text(ctx,checked,color,cycleId,x,y+12);
    }
}

function text(ctx,checked,color,name,x,y)
{
   ctx.font="6pt sans-serif";
   ctx.fillStyle=color;
   ctx.textAlign="center";
   ctx.textBaseline ="top";
   if(true == checked)
   {
      ctx.font="bold 6pt sans-serif";
      var metrics = ctx.measureText(name);
      ctx.strokeStyle='#FFFF00';
      //ctx.setLineDash([0]);
      ctx.strokeRect(x-metrics.width/2-2,y-2,metrics.width+4,12+4);
      //清除背景区域
      //ctx.clearRect(x-metrics.width/2,y,metrics.width,12);
      ctx.clearRect(x-metrics.width/2-2,y-2,metrics.width+4,12+4);
   }
   ctx.fillText(name,x,y);
}

function drawVisual(state,checked,x,y,r)
{
    var pos = 2;
    if(0 == state)
    {
       pos = 0; 
    }
    else if(2 == state)
    {
       pos = 1; 
    }
    
    if(true == checked)
    {
    	ctx.fillStyle="#FFFF00";
    	ctx.beginPath();
		ctx.arc(x,y,r+3,0,Math.PI*2,false); 
		ctx.closePath();
		ctx.fill();
	}
    
    ctx.fillStyle="#FFFFFF";
    ctx.beginPath();
	ctx.arc(x,y,r,0,Math.PI*2,false); 
	ctx.closePath();
	ctx.fill();
    ctx.drawImage(imgs[pos],x-r,y-r,2*r,2*r);
}

function drawNormal(state,checked,moreFLs,moreTLs,x,y,r)
{
    if(true == checked)
    {
    	ctx.fillStyle="#FFFF00";
    	ctx.beginPath();
		ctx.arc(x,y,r+3,0,Math.PI*2,false); 
		ctx.closePath();
		ctx.fill();
	}
	
	if(true == moreFLs)
    {
    	ctx.strokeStyle="#FF0000";
    	ctx.beginPath();
   		x0=x+r;
    	y0=y-r;
    	diry=4;
    	lines=Math.floor(2*r / diry);
    	w=2;
    	for(var i=1;i<lines;i++)
    	{
       		y0 = y0+diry;
	   		ctx.moveTo(x0,y0);
       		ctx.lineTo(x0+w,y0);
    	}
		ctx.stroke();
    }
    
    if(true == moreTLs)
    {
    	ctx.strokeStyle="#FF0000";
    	ctx.beginPath();
   		x0=x-r;
    	y0=y-r;
    	diry=4;
    	lines=Math.floor(2*r / diry);
    	w=3;
    	for(var i=1;i<lines;i++)
    	{
       		y0 = y0+diry;
	   		ctx.moveTo(x0,y0);
       		ctx.lineTo(x0-w,y0);
    	}
		ctx.stroke();
    }
    
    ctx.fillStyle="#FFFFFF";
    ctx.beginPath();
	ctx.arc(x,y,r,0,Math.PI*2,false); 
	ctx.closePath();
	ctx.fill();
    ctx.drawImage(imgs[state],x-r,y-r,2*r,2*r);
}

</script>
</html>