<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<title>反向依赖任务树</title>
<link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="stylesheet" type="text/css" href="css/table.css" />
<link rel="stylesheet" type="text/css" href="themes/icon.css">
<script type="text/javascript" src="js/jquery-1.7.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript">

//格式化任务周期的显示
function simpleFormatTaskCycle(taskIdCycleIdStates)
{
	var taskCycleS = '';
	if(null != taskIdCycleIdStates)
	{
		var taskIdCycleIdStateArr = taskIdCycleIdStates.split(',');
		if(taskIdCycleIdStateArr.length >=4)
		{
			var dateStrStart = taskIdCycleIdStateArr[3];
			var dateStrEnd = taskIdCycleIdStateArr[4];
			var timeDiff =  formateTimeDiff(diffTimeStr(dateStrStart,dateStrEnd));
			var timeED = '';
			//起始时间、运行时间
			if(dateStrStart !='')
			{
				timeED = '&nbsp;&nbsp;<font style="font-weight:bold;" color="green">['+timeDiff
					+','+dateStrEnd+"]</font>";
			}
			
			if(taskIdCycleIdStateArr.length>=6 && 1 == parseInt(taskIdCycleIdStateArr[5]))
			{
				taskCycleS += '<font style="font-weight:bold;" color="green">';
				taskCycleS += covTaskId2Name(taskIdCycleIdStateArr[0])+','+taskIdCycleIdStateArr[1];
				taskCycleS +=timeED;
				//如果是省略节点
				taskCycleS += '&nbsp;&nbsp;......';
				taskCycleS += '</font>';
			}
			else
			{
				taskCycleS += covTaskId2Name(taskIdCycleIdStateArr[0])+','+taskIdCycleIdStateArr[1];
				taskCycleS +=timeED;
			}
		}
		else
		{
			taskCycleS += taskIdCycleIdStates;
		}
	}
	return taskCycleS;
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

//初始化对象
$(function() {
	$('#taskIdList').combobox({
		url : "reqTaskIdJsonObject?containAllCol=false",
		valueField : "key",
		textField : "value",
		editable : true,
		width : 220,
		onChange : function(newValue, oldValue) {
			$('#cycleIdList').combobox('setValue',' ');
			loadCycleIdData();
		},
		onLoadSuccess : function()
		{
			//外部传来的taskId
			initTaskIdNameMap('taskIdList');
			loadCycleIdData(true);
			var taskId = covTaskName2Id($('#taskIdList').combobox('getValue'));
			if (taskId != "" &&  $('#cycleIdList').combobox('getValue') != "")
			{
				queryTaskTree(); 
			}
		}
	});	
});


function loadCycleIdData(first) {
	$('#cycleIdList').combobox(
			{
				url : 'reqCycleIdJsonObject?taskId='
						+ covTaskName2Id($("#taskIdList").combobox('getValue')),
				valueField : 'key',
				textField : 'key',
				editable : true,
				width : 120,
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

//加载任务树
function queryTaskTree()
{
	$("#ajaxLoading").attr("style","display:block");
	maxCount = 200;
	checkSessionValid();
	$.ajax({
		type : "post",
		url : 'getDependReverseTree?taskId='+ covTaskName2Id($("#taskIdList").combobox('getValue')) + 
		'&cycleId=' + $("#cycleIdList").combobox('getValue')+'&maxcount='+maxCount,
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
						if(values.length >= 4)
						{
							var treeJsonObj= $.parseJSON(values[0]);
							//绑定
							queryTaskTreeLocal(treeJsonObj,values[1],maxCount);
						}
					}
					else
					{
						$.messager.defaults={ok:"确定",cancel:"取消"};
						$.messager.alert('提示','查看反向依赖树失败!','error');
					}
				} 
				else
				{
					if(2 == returnValue2PageType)
					{
						
					     var alertMsg = '对不起, 查看反向依赖树失败'+'!<br>权限不足:<br>';
					     if(values.length >= 1)
					     {
							  alertMsg +='<table>';
							  //去掉‘,’号
							  var desValues = values[0].split(',');
							  for(var i=0;i <desValues.length;i++)
						      {
								  if('' != desValues[i])
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
						$.messager.alert('提示','查看反向依赖树失败!','error');
					}
				}
			}
			
			$("#ajaxLoading").attr("style","display:none");
		}
	});
	
}

function queryTaskTreeLocal(treeJsonObj,treeNodeCount,maxCount)
{
	var maxCountL = maxCount;
	var treeNodeCountL = treeNodeCount;
	$('#dependReverseTree').tree({
	    url:null,
	    onClick:function(node){
		     $(this).tree('toggle', node.target);
				},
		onContextMenu: function(e, node){
			 e.preventDefault();
			 $('#dependReverseTree').tree('select', node.target);
			 var data = node.text.split(",");
			 if (data[2] == 6 || data[2] == 11)
			{
				 document.getElementById("redo").disabled = true; 
			     $('#mm').menu('show', {
					left: e.pageX,
					top: e.pageY
				   });
			}
			 else
			{
				 document.getElementById("redo").disabled = false; 
				 $('#mm').menu('show', {
						left: e.pageX,
						top: e.pageY
					});
		    }
			},
	   onLoadSuccess: function(){
		    
		    $('span[class="tabs-title"]').each(function(index,obj){
	    		obj.innerHTML = "反向依赖任务树(最多显示"+maxCountL+"个任务周期)[总节点数:<font color='red'>"+treeNodeCountL+"</font>]";
	    	});
		    
			//替换节点的名字
			$('div[class*="tree-node"]').find('span[class*="tree-title"]').each(function(index,obj)
			{
				obj.innerHTML = simpleFormatTaskCycle(obj.innerHTML);
			});
	   }
		,
		onLoadError:function()
		{
			$.messager.defaults={ok:"确定",cancel:"取消"};
			$.messager.alert('提示','查看反向依赖树失败!','error');
		}
	});  

	$('#dependReverseTree').tree("loadData",treeJsonObj);
}

//跳转到任务周期详情页面
function viewTaskRunningdetail()
{
	var node = $('#dependReverseTree').tree('getSelected');
	if (node)
	{
		var data = node.text.split(",");
		var url = "TaskRunningDetail.jsp?taskId=" + data[0] + "&cycleId=" + data[1];
	    window.open(url);
	}
}
//重新执行
function redoTaskCycle(prefix)
{
	var node = $('#dependReverseTree').tree('getSelected');
	if (node)
	{
		var dataText = node.text.split(",");
		$.messager.defaults={ok:"是",cancel:"否"};
		var tips = '你确定要重做任务周期<font color="red">'+covTaskId2Name(dataText[0])+','+dataText[1]+'</font>?';
			$.messager.confirm('提示', tips, function(r)
			{
				if(r)
				{
					if(true == redoTaskCycleC(dataText[0],dataText[1]))
					{
						//刷新页面
						queryTaskTree();
					}
				}
			});
	}
}
//查看正向依赖树
function viewDependTree()
{	
	var node = $('#dependReverseTree').tree('getSelected');
	if (node)
	{
		var data = node.text.split(",");
		var url = "DependTaskTree.jsp?taskId=" + data[0] + "&cycleId=" + data[1];
	    window.open(url);		
	}
}
//以该节点作为查询条件刷新本页面
function viewReverseDependTree()
{
	var node = $('#dependReverseTree').tree('getSelected');
	if (node)
	{
		var data = node.text.split(",");
		$('#taskIdList').combobox('setValue',data[0]);
		$('#cycleIdList').combobox('setValue',data[1]);
		queryTaskTree();
	}
}
</script>
</head>
<body>
<div style="margin :5px 0px 0px 0px;margin-left: auto; margin-right: auto;background: #fff; border: 1px solid #ccc; width: 1000px;">
            <form id="getDependReverseTree" name="getDependReverseTree" method="post">
			<table width="570" style="margin-left: auto; margin-right: auto;">
				<tr>
					<td><span>任务名称: </span>
					</td>
					<td><input class="easyui-combobox" id="taskIdList"
						name="taskId" panelHeight="200px" style="width:210px" value="<%=taskId%>" /><font
						color="red">*</font>
					</td>
					<td><span>周期ID: </span>
					</td>
					<td><input class="easyui-combobox" id="cycleIdList"
						name="cycleId" panelHeight="200px"  value="<%=cycleId%>"/><font color="red">*</font>
					</td>
					<td><a class="easyui-linkbutton" plain="false"
						iconCls="icon-search" href="javascript:void(0)"
						onclick="queryTaskTree()">查询 </a></td>
				</tr>
			</table>
		</form>
</div>
  <div id="tt" class="easyui-tabs" tools="#tab-tools" style="margin :5px 0px 0px 0px;margin-left: auto;margin-right: auto;width:1000px;">
<div title="反向依赖任务树(最多显示200个任务周期)" tools="#p-tools" style="min-height:460px;padding:20px;">
    <div id="ajaxLoading"  class="ajaxLoading">数据加载中,请等待...</div>
	<div style="margin-left: auto;margin-right: auto;">
           <ul  id="dependReverseTree"></ul> 
    </div>
	</div>
</div>
<div style="margin :5px 0px 0px 0px; margin-left: auto; margin-right: auto; background: #fff; border: 1px solid #ccc; width: 1000px;">
		 <%@include file="footer.jsp"%>
</div>
 <div id="mm" class="easyui-menu" style="display:none;width:140px;">
		<div onclick="viewTaskRunningdetail()">查看任务周期详情</div>
		<div id="redo" onclick="redoTaskCycle()">重新执行</div>
<!-- 		<div onclick="viewLog()">查看日志</div> -->
		<div onclick="viewReverseDependTree()">查看反向依赖任务树</div>
		<div onclick="viewDependTree()">查看正向依赖任务树</div>
	</div>
</body>
</html>