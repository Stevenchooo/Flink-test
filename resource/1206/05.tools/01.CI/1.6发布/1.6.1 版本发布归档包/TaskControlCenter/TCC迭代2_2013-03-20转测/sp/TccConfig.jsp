<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="js/jquery-1.7.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<script type="text/javascript" src="js/formValidator-4.0.1.js"></script>
<script type="text/javascript" src="js/formValidatorRegex.js"></script>
<script language="javascript" type="text/javascript"
	src="DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="stylesheet" type="text/css" href="css/table.css" />
<link rel="stylesheet" type="text/css" href="themes/icon.css" />
<link rel="stylesheet" type="text/css" href="themes/default/easyui.css">
<style type="text/css">
	input
	{
		float:left;
	}
	td > div
	{
		float:left;
	}
</style>
<title>TCC配置</title>
	<%
		boolean isFullName = false;
		boolean isSendEmail = false;
		boolean isLogScheduleDuration = false;
		boolean taskIdCenter = false;
	    Object isFullNameO = request.getAttribute("isFullName");
		if(null != isFullNameO && (Boolean)isFullNameO == true)
		{
		    isFullName = true;
		}
		
		Object isSendEmailO = request.getAttribute("isSendEmail");
		if(null != isSendEmailO && (Boolean)isSendEmailO == true)
		{
		    isSendEmail = true;
		}

		Object isLogScheduleDurationO = request.getAttribute("isLogScheduleDuration");
		if(null != isLogScheduleDurationO && (Boolean)isLogScheduleDurationO == true)
		{
		    isLogScheduleDuration = true;
		}
		
		Object taskIdCenterO = request.getAttribute("taskIdCenter");
		if(null != taskIdCenterO && (Boolean)taskIdCenterO == true)
		{
		    taskIdCenter = true;
		}
	%>
<script type="text/javascript">

$(function() {
	$.messager.defaults={ok:"确定",cancel:"取消"};
	validataConfig();
	});

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

function rebootTcc()
{
	$.messager.defaults={ok:"是",cancel:"否"};
	$.messager.confirm('警告!', "你确定要<font color='red'>重新启动</font>吗?", function(r){
		if(r)
		{
			checkSessionValid();
	$.ajax({
				type:"post",
				url:"rebootTcc",
				async:false,
				success:function(data, textStatus)
				{
					if (textStatus == "success")
					{
						var datas = data.split("|");
						if (datas[0] == "true")
						{
							wait();
							bSuccessed = true;
						} 
						else
						{
							$.messager.defaults={ok:"确定",cancel:"取消"};
							$.messager.alert('提示','对不起，重启失败!','error');
							bSuccessed = false;
						}
					}
				}
				
			});
		}
	});
	$.messager.defaults={ok:"确定",cancel:"取消"};
	return bSuccessed;
}
	
function saveTccConfig()
	{
		checkSessionValid();
		$.ajax({
		       type: "post",
		       url: "saveTccConfig",
		       data : $.param({
					"benchDate":$("#benchDate").val(),
					"maxRunningCycleTaskNum":$.trim($("#maxRunningCycleTaskNum").val()),
					"conRetryTimes":$.trim($("#conRetryTimes").val()),
					"maxSSHConnectionNum":$.trim($("#maxSSHConnectionNum").val()),
					"reserveNLCCount":$.trim($("#reserveNLCCount").val()),
					"jobDetailUrl":$.trim($("#jobDetailUrl").val()),
					"memcachedAddr":$.trim($("#memcachedAddr").val()),
					"backupDir":$.trim($("#backupDir").val()),
					"killJobCmdTemplate":$.trim($("#killJobCmdTemplate").val()),
					"executeCmdTemplate":$.trim($("#executeCmdTemplate").val()),
					"lSCmdTemplate":$.trim($("#lSCmdTemplate").val()),
					"isFullName":$("#isFullName").attr("checked")=="checked" ? true : false,
					"isSendEmail":$("#isSendEmail").attr("checked")=="checked" ? true : false,
					"isLogScheduleDuration":$("#isLogScheduleDuration").attr("checked")=="checked" ? true : false,
					"taskIdCenter":$("#taskIdCenter").attr("checked")=="checked" ? true : false,
					"emailsTo":$.trim($("#emailsTo").val()),
					"portalUrl":$.trim($("#portalUrl").val()),
					"emailFrom":$.trim($("#emailFrom").val()),
					"url":$.trim($("#url").val()),
					"username":$.trim($("#username").val()),
					"password":$.trim($("#password").val()),
					"rsLogThreshold":$("#rsLogThreshold").combobox('getValue'),
					"rslog2dbThreshold":$("#rslog2dbThreshold").combobox('getValue'),
					"tccLogThreshold":$("#tccLogThreshold").combobox('getValue'),
					"tcclog2dbThreshold":$("#tcclog2dbThreshold").combobox('getValue'),
					"consoleThreshold":$("#consoleThreshold").combobox('getValue')
				}),
		       async: false,
		       success: function(data, textStatus){
		    	   if (textStatus == "success") {
						  data = data.split("|");
						  if (data[0] == "true")
						  {
							  $.messager.alert('提示','保存成功!','info');
						  }
						  else
						  {
							  if(data.length > 1)
							  {
								  $.messager.alert('提示','对不起,'+data[1],'error');
							  }
							  else
							  {
								  $.messager.alert('提示','对不起,保存失败!','error');
							  }
						  }
					 }
				  }
			  });
	}
	
function validataConfig()
{
	 $.formValidator.initConfig({submitButtonID:"saveTccConfig",debug:true,onSuccess:function(){saveTccConfig();},onError:function(){$.messager.alert('提示','对不起，配置项中有不符合要求的修改，请先更正!');}});
	//校验渠道类型名
	$("#benchDate").formValidator({empty:false,onShow:"OK",onFocus:"请选择1号的日期",
        onCorrect:"OK"}).functionValidator({
        fun:function(val,elem){
        		 if(isEmpty($("#benchDate").val()))
        		 {
        			 return false;
        		 }
        		 else
        		 {
        			 return true;
        		 }
        }
        ,onError:"<font color='red'>请选择1号日期,不能为空</font>"
        ,onCorrect:"OK"
        
    });
	 $("#maxSSHConnectionNum").formValidator({empty:false,onShow:"每个节点的最大连接数",onFocus:"一个任务周期执行时占用一个连接",onCorrect:"OK"});
	 $("#executeCmdTemplate").formValidator({empty:false,onShow:'OK',onFocus:'必需包含4个"%s"',onCorrect:"OK"}).functionValidator({
        fun:function(val,elem){
        	 var data = $("#executeCmdTemplate").val();
        	 var datas = data.split('%s');
        	 if(datas.length == 5)
             {
        		return true;
             }
        	 else
        	 {
        		 return false;
        	 }
        }
        ,onError:'<font color="red">必需包含4个"%s"</font>'
        ,onCorrect:"OK"
        
    });
	 $("#lSCmdTemplate").formValidator({empty:false,onShow:'OK',onFocus:'必需包含1个"%s"',onCorrect:"OK"}).functionValidator({
	        fun:function(val,elem){
	        	 var data = $("#lSCmdTemplate").val();
	        	 var datas = data.split('%s');
	        	 if(datas.length == 2)
	             {
	        		return true;
	             }
	        	 else
	        	 {
	        		 return false;
	        	 }
	        }
	        ,onError:'<font color="red">必需包含1个"%s"</font>'
	        ,onCorrect:"OK"
	        
	    });
	 $("#killJobCmdTemplate").formValidator({empty:false,onShow:'OK',onFocus:'必需包含1个"%s"',onCorrect:"OK"}).functionValidator({
	        fun:function(val,elem){
	        	 var data = $("#killJobCmdTemplate").val();
	        	 var datas = data.split('%s');
	        	 if(datas.length == 2)
	             {
	        		return true;
	             }
	        	 else
	        	 {
	        		 return false;
	        	 }
	        }
	        ,onError:'<font color="red">必需包含1个"%s"</font>'
	        ,onCorrect:"OK"
	        
	    });
	 $("#jobDetailUrl").formValidator({empty:false,onShow:'OK',onFocus:'必需包含1个"%s"',onCorrect:"OK"}).functionValidator({
	        fun:function(val,elem){
	        	 var data = $("#jobDetailUrl").val();
	        	 var datas = data.split('%s');
	        	 if(datas.length == 2)
	             {
	        		return true;
	             }
	        	 else
	        	 {
	        		 return false;
	        	 }
	        }
	        ,onError:'<font color="red">必需包含1个"%s"</font>'
	        ,onCorrect:"OK"
	        
	    });
	 $("#backupDir").formValidator({empty:false,onShow:'OK',onFocus:'必需包含1个"%s"',onCorrect:"OK"}).functionValidator({
	        fun:function(val,elem){
	        	 var data = $("#backupDir").val();
	        	 var datas = data.split('%s');
	        	 if(datas.length == 2)
	             {
	        		return true;
	             }
	        	 else
	        	 {
	        		 return false;
	        	 }
	        }
	        ,onError:'<font color="red">必需包含1个"%s"</font>'
	        ,onCorrect:"OK"
	        
	    });
	 
	 $("#emailsTo").formValidator({empty:false,onShow:'OK',onFocus:'多个邮箱请使用";"号分隔',onCorrect:"OK"}).functionValidator({
	        fun:function(val,elem){
	        	var patrn = /^\+?[a-z0-9](([-+.]|[_]+)?[a-z0-9]+)*@([a-z0-9]+(\.|\-))+[a-z]{2,6}$/;
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
	        ,onError:'<font color="red">请输入合法的邮箱地址，多个邮箱请使用";"号分隔</font>'
	        ,onCorrect:"OK"
	    });
}

	
</script>

</head>

<body>
	<div id="tccConfig" style="margin-left:auto;margin-right:auto;width: 1000px;padding: 10px">
			<div region="center" border="false"	style="padding: 10px; background: #fff; border: 1px solid #ccc;">
				<form id="tccConfigAction" name="tccConfigAction" action="tccConfigAction" method="post">
					<table width="980" cellpadding="10px" cellspacing="5px" style="margin-left:auto;margin-right:auto;">
						<tr>
							<td width="250"><span>基准时间(<font color='red'>重启后生效</font>):</span></td>
							<td>
							<input id="benchDateHide" type="hidden" value="<%=request.getAttribute("benchDate")%>"/>
							<input id="benchDate" readOnly="true" value="<%=request.getAttribute("benchDate")%>" onclick="WdatePicker({dateFmt:'yyyy-MM-01'})" class="Wdate" style="width:150px"/>
							<div id="benchDateTip"/>
							</td>
						</tr>
						<tr>
							<td><span>最大并发数: </span></td>
							<td>
							    <input id="maxRunningCycleTaskNum" value="<%=request.getAttribute("maxRunningCycleTaskNum")%>" class="easyui-numberspinner" increment="10" min="0" max="500" precision="0" style="width:150px"/>
							</td>
						</tr>

						<tr>
							<td><span>节点的最大连接数: </span></td>
							<td>
							<div style="float:left;">
							<input id="maxSSHConnectionNum" value="<%=request.getAttribute("maxSSHConnectionNum")%>" class="easyui-numberspinner" increment="10" min="0" max="500" precision="0" style="width:150px"/>
							</div>
							<div id="maxSSHConnectionNumTip" />
							</td>
						</tr>
						<tr>
							<td><span>Shell命令模板: </span></td>
							<td><input id="executeCmdTemplate" value="<%=request.getAttribute("executeCmdTemplate")%>" style="width:350px"/>
								<div id="executeCmdTemplateTip" />
							</td>
						</tr>

						<tr>
							<td><span>文件查询命令模板: </span></td>
							<td><input id="lSCmdTemplate" value="<%=request.getAttribute("lSCmdTemplate")%>" style="width:350px"/>
								<div id="lSCmdTemplateTip" />
							</td>
						</tr>
						<tr>
						<td><span>文件列表是否全路径: </span></td>
							<td><input id="isFullName" type="checkbox"/>
								<div id="isFullNameTip" />
							</td>
						</tr>
						<tr>
							<td><span>job的kill命令模板: </span></td>
							<td><input id="killJobCmdTemplate" value="<%=request.getAttribute("killJobCmdTemplate")%>" style="width:550px"/>
								<div id="killJobCmdTemplateTip" />
							</td>
						</tr>
						<tr>
							<td><span>job的详细信息展示页面地址: </span></td>
							<td><input id="jobDetailUrl" value="<%=request.getAttribute("jobDetailUrl")%>" style="width:550px"/>
								<div id="jobDetailUrlTip" />
							</td>
						</tr>
						<tr>
							<td><span>保留最近N个成功运行的任务周期所产生的日志（远程壳输出日志）: </span></td>
							<td>
							<div style="float:left;">
							<input id="reserveNLCCount" value="<%=request.getAttribute("reserveNLCCount")%>" class="easyui-numberspinner" increment="1" min="1" max="500" precision="0" style="width:150px"/>
							</div>
							<div id="reserveNLCCountTip" />
							</td>
						</tr>
						
						<tr style="display:none;">
							<td><span>批文件（ods任务处理的文件）备份目录: </span></td>
							<td><input id="backupDir" value="<%=request.getAttribute("backupDir")%>" style="width:550px"/>
								<div id="backupDirTip" />
							</td>
						</tr>
						<td><span>是否发送提示邮件: </span></td>
							<td><input id="isSendEmail" type="checkbox"/>
								<div id="isSendEmailTip" />
							</td>
						</tr>
						<tr>
							<td><span>TCC管理平台地址: </span></td>
							<td><input id="portalUrl" value="<%=request.getAttribute("portalUrl")%>" style="width:550px"/>
								<div id="portalUrlTip" />
							</td>
						</tr>
						<tr>
							<td><span>发送人显示名称: </span></td>
							<td><input id="emailFrom" value="<%=request.getAttribute("emailFrom")%>" style="width:550px"/>
								<div id="emailFromTip" />
							</td>
						</tr>
						<tr>
							<td><span>是否记录调度时长: </span></td>
							<td><input id="isLogScheduleDuration" type="checkbox"/>
								<div id="isLogScheduleDurationTip" />
							</td>
						</tr>
						<tr>
							<td><span>任务ID中心: </span></td>
							<td><input id="taskIdCenter" type="checkbox"/>
								<div id="taskIdCenterTip" />
							</td>
						</tr>
						<tr>
							<td><span>ssh连接重试次数: </span></td>
							<td>
								<input id="conRetryTimes" value="<%=request.getAttribute("conRetryTimes")%>" class="easyui-numberspinner" increment="2" min="1" max="100" precision="0" style="width:150px"/>
							</td>
						</tr>
						<tr>
							<td><span>数据库连接地址(<font color='red'>重启后生效</font>): </span></td>
							<td><input id="url" value="<%=request.getAttribute("url")%>" style="width:550px"/>
								<div id="urlTip" />
							</td>
						</tr>
						<tr>
							<td><span>用户名(<font color='red'>重启后生效</font>): </span></td>
							<td><input id="username" value="<%=request.getAttribute("username")%>" style="width:550px"/>
								<div id="usernameTip" />
							</td>
						</tr>

						<tr>
							<td><span>密码(<font color='red'>重启后生效</font>): </span></td>
							<td><input id="password" type="password" value="<%=request.getAttribute("password")%>" style="width:550px"/>
								<div id="passwordTip" />
							</td>
						</tr>
						
						<tr>
							<td><span>tcc日志级别(<font color='red'>重启后生效</font>): </span></td>
							<td>
								<select id="tccLogThreshold" value="<%=request.getAttribute("tccLogThreshold")%>" editable="false"  class="easyui-combobox"
									panelHeight="auto" style="width: 220px" required="true">
									<option value="ERROR">错误</option>
									<option value="INFO">信息</option>
									<option value="DEBUG">调试</option>
									<option value="FATAL">致命</option>
									<option value="WARN">警告</option>
								</select>
							</td>
						</tr>
						<tr>
							<td><span>记录到db的tcc日志级别(<font color='red'>重启后生效</font>): </span></td>
							<td>
								<select id="tcclog2dbThreshold"  value="<%=request.getAttribute("tcclog2dbThreshold")%>" editable="false"  class="easyui-combobox"
									panelHeight="auto" style="width: 220px" required="true">
									<option value="ERROR">错误</option>
									<option value="INFO">信息</option>
									<option value="DEBUG">调试</option>
									<option value="FATAL">致命</option>
									<option value="WARN">警告</option>
								</select>
							</td>
						</tr>
						<tr>
							<td><span>remoteshell日志级别(<font color='red'>重启后生效</font>): </span></td>
							<td>
								<select id="rsLogThreshold" value="<%=request.getAttribute("rsLogThreshold")%>" editable="false"  class="easyui-combobox"
									panelHeight="auto" style="width: 220px" required="true">
									<option value="ERROR">错误</option>
									<option value="INFO">信息</option>
									<option value="DEBUG">调试</option>
									<option value="FATAL">致命</option>
									<option value="WARN">警告</option>
								</select>
							</td>
						</tr>
						<tr>
							<td><span>记录到db的remoteshell日志级别(<font color='red'>重启后生效</font>): </span></td>
							<td>
								<select id="rslog2dbThreshold" value="<%=request.getAttribute("rslog2dbThreshold")%>" editable="false"  class="easyui-combobox"
									panelHeight="auto" style="width: 220px" required="true">
									<option value="ERROR">错误</option>
									<option value="INFO">信息</option>
									<option value="DEBUG">调试</option>
									<option value="FATAL">致命</option>
									<option value="WARN">警告</option>
								</select>
							</td>
						</tr>
						<tr>
							<td><span>控制台输出级别(<font color='red'>重启后生效</font>): </span></td>
							<td>
								<select id="consoleThreshold" value="<%=request.getAttribute("consoleThreshold")%>" editable="false"  class="easyui-combobox"
									panelHeight="auto" style="width: 220px" required="true">
									<option value="ERROR">错误</option>
									<option value="INFO">信息</option>
									<option value="DEBUG">调试</option>
									<option value="FATAL">致命</option>
									<option value="WARN">警告</option>
								</select>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div region="south" border="true" style="float:left; padding: 5px 0; height: 35px;">
				<a id='rebootTcc' class="easyui-linkbutton" plain="true" iconCls="icon-cancel"
					href="javascript:void(0)" onclick="rebootTcc()"><font style="font-weight:900;"  size="4" color='#FF0000'>重启TCC</font></a>
			</div>
			<div region="south" border="true"
				style="text-align: right; padding: 5px 0; height: 35px;">
				<a id='saveTccConfig' class="easyui-linkbutton" plain="true" iconCls="icon-ok"
					href="javascript:void(0)">保存(未标<font color='red'>重启后生效</font>的配置项立即生效) </a>
			</div>
	</div>

	<script type="text/javascript">
		//$('#isFullName').each(function(index,obj){obj.checked=<%=isFullName%>;});
		//$('#isSendEmail').each(function(index,obj){obj.checked=<%=isSendEmail%>;});
		$('#isFullName').attr('checked', <%=isFullName%>);
		$('#isSendEmail').attr('checked', <%=isSendEmail%>);
		$('#isLogScheduleDuration').attr('checked',<%=isLogScheduleDuration%>);
		$('#taskIdCenter').attr('checked',<%=taskIdCenter%>);
		$(function() {
			loadCombox();
			});
			
		function loadCombox()
		{
			$("#rsLogThreshold").combobox('setValue',"<%=request.getAttribute("rsLogThreshold")%>");
			$("#rslog2dbThreshold").combobox('setValue',"<%=request.getAttribute("rslog2dbThreshold")%>");
			$("#tccLogThreshold").combobox('setValue',"<%=request.getAttribute("tccLogThreshold")%>");
			$("#tcclog2dbThreshold").combobox('setValue',"<%=request.getAttribute("tcclog2dbThreshold")%>");
			$("#consoleThreshold").combobox('setValue',"<%=request.getAttribute("consoleThreshold")%>");
		}
	</script>
</body>
</html>