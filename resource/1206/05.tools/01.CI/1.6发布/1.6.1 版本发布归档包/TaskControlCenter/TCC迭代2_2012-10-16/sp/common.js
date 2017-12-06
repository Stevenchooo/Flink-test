/*
 * common.js
 */

//任务ID名字键值对
var taskIdNameMap = {};
var taskNameIdMap = {};

//初始化任务Id名字键值对
function initTaskIdNameMap(taskidListName)
{
	var datas = $('#'+taskidListName).combobox("getData");
	for(var index in datas)
	{
		taskIdNameMap[datas[index]['key']]=datas[index]['value'];
		taskNameIdMap[datas[index]['value']]=datas[index]['key'];
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

//校验依赖周期ID列表
$.extend($.fn.validatebox.defaults.rules, {
	dependTaskIdList: {
	validator: function(value, param){
		return vaildTaskIdList(value);
	},
	message: '多个之间采用分号分隔.格式：任务名,是否全周期依赖(0或1),是否忽略错误(0或1);...例如：task1,0,0;task2,0,1'
	}
	});
	
//校验依赖周期ID列表
$.extend($.fn.validatebox.defaults.rules, {
	validEmailsType: {
	validator: function(value, param){
		return validEmails(value);
	},
	message: '请输入正确的华为邮箱列表,多个使用";"号分隔!'
	}
	});


function vaildTaskIdList(value)
{
	//格式：4043009,0,0;4093025,0,0
	var patrn= /^([\u4E00-\u9FA5_|a-z|A-Z]([\u4E00-\u9FA5_|a-z|A-Z|0-9]){0,127},(0|1),(0|1)(,(0|1))?;){0,99}([\u4E00-\u9FA5_|a-z|A-Z]([\u4E00-\u9FA5_|a-z|A-Z|0-9]){0,127},(0|1),(0|1)(,(0|1))?;?)?$/;
	if(value.match(patrn))
	{
		return true;
	}
	else
	{
		return false;
	}
}

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

//将yyyy-MM-DDThh:mm:ss格式转换为MM/DD/yyyy hh:mm:ss格式
function covDate2OtherFormat(dateTimeStr)
{
	if(null != dateTimeStr && "" != dateTimeStr)
	{
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

//对日期函数的处理，如"2000-01-01T01:00:00"转化成"2000-01-01 01:00:00"
//"2000-01-01"转化成"2000-01-01 00:00:00"
function formatTime(dateValue)
{
	if (null == dateValue) {
		return null;
	}
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

//初始化任务Id名字键值对
function initTaskIdNameMapByStr(taskidNames)
{
	if(null != taskidNames)
	var datas = taskidNames.split(';');
	for(var index in datas)
	{
		if(null != datas[index] && '' != datas[index])
		{
			var idnames = datas[index].split(',');
			if(idnames.length >=2)
			{
				taskIdNameMap[idnames[0]]=idnames[1];
				taskNameIdMap[idnames[1]]=idnames[0];
			}
		}
	}
}

//改变任务名字
function changeTaskName(srcName, dstName)
{
	var taskId = covTaskName2Id(srcName);
	if(taskId != srcName)
	{
		taskIdNameMap[taskId] = dstName;
		taskNameIdMap[srcName] = undefined;
		taskNameIdMap[dstName] = taskId;
	}
}

//校验名字
$.extend($.fn.validatebox.defaults.rules, {
	nameValidType: {
	validator: function(value, param){
		return vaildTaskName(value);
	},
	message: '以汉字、字母或下划线开头并由字母、汉字、数字、下划线、减号组合而成的字符串序列，长度限制为1-128.'
	}
	});

//校验邮箱列表
$.extend($.fn.validatebox.defaults.rules, {
	emailsValidType: {
	validator: function(value, param){
		return validEmails(value);
	},
	message: '请输入正确的华为邮箱列表, 多个邮箱以分号";"分割.例如：aaa.bbb@huawei.com;aaa1.bbb1@huawei.com'
	}
	});

//校验电话列表
$.extend($.fn.validatebox.defaults.rules, {
	phonesValidType: {
	validator: function(value, param){
		return validPhones(value);
	},
	message: '请输入正确的电话号码列表, 多个号码以分号";"分割.例如：13912345678;13812345678'
	}
	});

function vaildTaskName(value)
{
	//格式：以非数字开头的由字母数字下划线组合成的名字，长度为1-128直接
	var patrn= /^[\u4E00-\u9FA5_|a-z|A-Z]([\u4E00-\u9FA5_|\-|a-z|A-Z|0-9]){0,127}$/;
	if(value.match(patrn))
	{
		return true;
	}
	else
	{
		return false;
	}
}

function vaildName(value)
{
	return vaildTaskName(value);
}

function validEmails(emails)
{
	var val = emails;
	//var patrn = /^\+?[a-z0-9](([-+.]|[_]+)?[a-z0-9]+)*@huawei\.com$/; 这个表达式效率很低，直接导致浏览器无响应!
	var patrn = /^[a-z0-9]([a-z|\.|_|0-9])*@huawei\.com$/;
	var datas = val.split(';');
	var successedd = false;
	for(var index in datas)
	{
		if('' !=  $.trim(datas[index]))
		{
			if(!datas[index].match(patrn))
    		{
    			return false;
    		}
			else
			{
				successedd = true;
			}
		}
	}
	
	//至少成功一次
	if(successedd)
	{
		return true;
	}
}

function validPhones(phones)
{
	var val = phones;
	var patrn = /^[0-9]{11,11}$/;
	var datas = val.split(';');
	var successedd = false;
	for(var index in datas)
	{
		if('' !=  $.trim(datas[index]))
		{
			if(!datas[index].match(patrn))
    		{
    			return false;
    		}
			else
			{
				successedd = true;
			}
		}
	}
	
	//至少成功一次
	if(successedd)
	{
		return true;
	}
}


function vaildServiceName(value)
{
	return vaildTaskName(value);
}

function covTaskName2Id(name)
{
	name += '';
	var taskId = '';
	if(null != name)
	{
		var nameArr = name.split(";");
		var value;
		for(var index in nameArr)
		{
			if('' != nameArr[index])
			{
				value = taskNameIdMap[nameArr[index]];
				if(undefined != value)
				{
					taskId += taskNameIdMap[nameArr[index]]+";";
				}
			}
		}
		
		//去掉";"号
		if('' != taskId && null != taskId)
		{
			taskId = taskId.substring(0, taskId.length-1);
		}
	}
	
	if('' == taskId || null == taskId)
	{
		taskId = name;
	}
	
	return taskId;
}

function covTaskId2Name(taskId)
{
	var name = '';
	if(null != taskId)
	{
		name = taskIdNameMap[taskId];
	}
	
	if('' == name || null == name)
	{
		name = taskId;
	}
	
	return name;
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

//根据不同的状态显示不同的图标
function showState(state)
{
	var statePng="";
	switch (parseInt(state)) {
	case 0:
		statePng = '<img src="themes/icons/state_init.png"/>';
		break;
	case 1:
		statePng = '<img src="themes/icons/state_start.png"/>';
		break;
	case 2:
		statePng = '<img src="themes/icons/state_success.png"/>';
		break;
	case 3:
		statePng = "<img src='themes/icons/state_error.png'/>";
		break;
	case 4:
		statePng = "<img src='themes/icons/state_timeout.png'/>";
		break;
	case 5:
		statePng = "<img src='themes/icons/state_vsucess.png'/>";
		break;
	case 6:
		statePng = "<img src='themes/icons/state_noinit.png'/>";
		break;
	case 7:
		statePng = "<img src='themes/icons/state_waitrun.png'/>";
		break;
	case 8:
		statePng = "<img src='themes/icons/state_running.png'/>";
		break;
	case 9:
		statePng = "<img src='themes/icons/state_reqdelete.png'/>";
		break;
	case 10:
		statePng = "<img src='themes/icons/state_stop.png'/>";
		break;
	case 11:
		statePng = "<img src='themes/icons/state_nobatch.png'/>";
		break;
	default:
		statePng="<img src='themes/icons/state_unknown.png'/>";
		break;
	}
	return statePng;
}

//校验value是否为空串
function isEmpty(value)
{
	if(null == value || "" == value)
	{
		return true;
	}
	else
	{
		var patern=/^(\s|\n)*$/;
		value +='';
		return patern.exec(value);
	}
}

//格式化任务周期的显示
function formatTaskCycleC(taskId,cycleId)
{
	var taskCycleS = '';
	if(null != taskId && null != cycleId)
	{
		taskCycleS += '<font style="font-weight:bold;" color="red">';
		taskCycleS += covTaskId2Name(taskId)+','+cycleId;
		taskCycleS += '</font>';
	}
	return taskCycleS;
}

function redoTaskCycleC(taskId,cycleId)
{
	var isSucessed = false;
	checkSessionValid();
	$.ajax({
		type:"post",
		url:"redoTaskCycle?taskId="+taskId+"&cycleId="+cycleId,
		async : false,
		success : function(data, textStatus) {
			if (textStatus == "success") {
				data = data.split(",");
				if (data[0] == "true") {
					isSucessed = true;
					
					//提示是否继续选择
					$.messager.defaults={ok:"确定",cancel:"取消"};
					$.messager.alert('提示','请求重做任务周期'+formatTaskCycleC(taskId,cycleId)+'成功!','info');
					
				} else {
					
					isSucessed = false;
					if(alertPrivilegeNotEnouth(data, '请求重做任务周期'+formatTaskCycleC(taskId,cycleId)+'失败'))
					{
						  return isSucessed;
					}
					else
					{
						$.messager.defaults={ok:"确定",cancel:"取消"};
						$.messager.alert('提示','请求重做任务周期'+formatTaskCycle(taskId,cycleId)+'失败!','error');
					}
				}
			}
		}
	});
	return isSucessed;
}

function alertPrivilegeNotEnouth(data, reason)
{
	//是否是权限不足
	var notEnough = false;
	if(data.length >=2)
	  {
		  if('2' == data[1])
		  {
			  var alertMsg = '对不起, '+reason+'!<br>权限不足:<br>';
			  alertMsg +='<table>';
			  for(var i=2;i <data.length;i++)
		      {
				  if('' != data[i])
			      {
					  alertMsg +='<tr>';
					  alertMsg +='<td>';
					  alertMsg += data[i];
					  alertMsg +='</td>';
					  alertMsg +='</tr>';
			      }
		      }
			  alertMsg +='</table>';
			  alertMsg = alertMsg.replace(/\[/g, '[<font color="red">');
			  alertMsg = alertMsg.replace(/\]/g, ']</font>');
			  $.messager.alert('提示',alertMsg,'info');
			  notEnough = true;
		  }
	  }
	return notEnough;
}

function alertPrivilegeNotEnouthJson(value, reason)
{
	//是否是权限不足
	var notEnough = false;
	var alertMsg = '对不起, '+reason+'!<br>权限不足:<br>';
	  alertMsg +='<table>';
	  //去掉‘,’号
	  var desValues = value.split(',');
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
	  notEnough = true;
	return notEnough;
}

function checkSessionValid()
{
	var isSessionValid = false;
	$.ajax({
		type : "post",
		url : "isSessionValid",
		async : false,
		success : function(data, textStatus)
		{
			if (textStatus == "success")
			{
				if("true" == data)
				{
					isSessionValid = true;
				}
			}
		}
	});
	
	if(!isSessionValid)
	{
		//跳转到log
		window.location.href = "login.jsp";
	}
	return isSessionValid;
}
function alertForeignKeyConstrainJson(value, objToDel, objType)
{
	var alertMsg = '';
	alertMsg += '请先删除所有绑定此' + objType + '<font color="red">[' + objToDel + ']</font>' + '的' + value + "。";
	$.messager.alert('提示',alertMsg,'info');
}

function isSystemAdmin()
{
	var systemAdmin = false;;
	checkSessionValid();
	$.ajax({
		type : "post",
		url : "isSystemAdmin",
		async : false,
		success : function(data, textStatus)
		{
			if (textStatus == "success")
			{
				if(data == "true")
				{
					systemAdmin = true;
				}
			}
		}
	});
	return systemAdmin;
}

//将yyyy-MM-DDThh:mm:ss格式转换为MM/DD/yyyy hh:mm:ss格式
function covDate2OtherFormat(dateTimeStr)
{
	if(null != dateTimeStr && "" != dateTimeStr)
	{
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