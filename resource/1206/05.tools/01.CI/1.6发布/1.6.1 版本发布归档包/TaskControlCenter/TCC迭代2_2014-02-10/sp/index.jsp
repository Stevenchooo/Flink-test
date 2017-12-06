<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<head>
<title>TCC导航页面</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="x-ua-compatible" content="ie=7" />
<script type="text/javascript" src="js/jquery-1.7.js"></script>
<script type="text/javascript" src="js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="js/common.js"></script>
<link rel="stylesheet" type="text/css" href="css/common.css" />
<link rel="stylesheet" type="text/css" href="css/table.css" />
<link rel="stylesheet" type="text/css" href="themes/icon.css" />
<link rel="stylesheet" type="text/css" href="themes/default/easyui.css" />
<style type="text/css">
html,body {
	margin: 0px auto;
	padding: 0px;
	height: 100%;
	font-size: 12px;
	overflow: hidden;
	font-family: "微软雅黑", "黑体";
}

.left {
	float: left;
	border: 1px solid #99BBE8;
	margin-right: 5px;
	height: 100%;
	width: 152px;
}

iframe,div {
	margin: 0px auto;
	padding: 0px;
}

#Container {
	margin: 0 auto;
	width: 100%;
	height: 100%;
}

#Header {
	height: 50px;
	border-bottom: 1px solid #CCC;
	background-color: #F4FBFF;
}

#Header .tit {
	padding: 12px 0 0 20px;
	background-color: #3b82cc
}

#Header h3 b {
	color: #FFF;
	font-size: 20px;
}

#Header h3 b {
	color: #FFF;
	font-size: 20px;
}

#userOpt {
	position: absolute;
	right: 4px;
	width: 300px;
	top: 4px;
	height: 18px;
	line-height: 18px;
}

#MainContent {
	height: 100%;
	width: 100%;
	background-color: #FFF
}

#Content {
	margin: 0px auto;
	margin-left: 125px !important;
	margin-left: 125px;
	height: 100%;
	background: #ffa;
	overflow: hidden;
	padding: 0px;
}

#treeMenu {
	width: 150px;
	clear: both;
}

#treeMenu .linkItem {
	padding: 1px;
}

#treeMenu .indexmenu {
	padding-top: 5px;
	padding-bottom: 5px;
	border-left: 1px solid #006699;
	border-bottom: 1px solid #006699;
	margin-right: 0px !important;
	margin-right: -1px;
	padding-left: 9px;
}

#treeMenu .indexmenu {
}

#treeMenu .indexmenu a {
	color: #006699;
	text-decoration: none;
	font-weight: bold;
}

#treeMenu .menu {
    position:relative;
	background-repeat: no-repeat;
	line-height: 22px !important;
	line-height: 28px;
	font-weight: bold;
	padding-left: 10px;
	background-image: url(images/left_line.gif);
	background-position: bottom;
	background-repeat: no-repeat;
	padding-top: 2px !important;
	padding-bottom: 2px !important;
	padding-top: 6px;
	padding-bottom: 6px;
}

#treeMenu .activedmenu {
	position:relative;
	background-repeat: no-repeat;
	line-height: 28px;
	font-weight: bold;
	padding-left: 10px;
	border-bottom: 1px solid #A5B7DC;
	padding-top: 0px !important;
	padding-bottom: 0px !important;
	padding-top: 6px;
	padding-bottom: 6px;
	background-image: url(images/left_line.gif);
	background-position: bottom;
}

#treeMenu .menu a:link,#treeMenu .menu a:visited,#treeMenu .activedmenu a:link,#treeMenu .activedmenu a:visited
	{
	color: #3B82CC;
	text-decoration: none
}

#treeMenu .item2 {
	text-align: left;
	margin-top: 7px;
	margin-bottom: 7px;
}

#treeMenu .selectItem2 {
	padding-top: 5px;
	padding-bottom: 5px;
	border-left: 1px solid #3B82CC;
	border-top: 1px solid #3B82CC;
	border-bottom: 1px solid #3B82CC;
	margin-right: 0px !important;
	margin-right: -1px;
	margin-top: 5px;
}

#treeMenu .selectItem2 a:link {
	color: #3B82CC;
	text-decoration: none
}

#treeMenu .itembox {
	margin-left: 15px;
}

#treeMenu div {
	clear: both;
}

#treeMenu {
	font-size: 14px;
	color: #006699;
	font-weight: bold;
}

#treeMenu .itembox a {
	text-decoration: none;
	color: #006699;
	font-size: 13px;
	font-weight: normal;
}

#treeMenu .itembox a:hover {
	
}
</style>
<script type="text/javascript">
	$(document).ready(function() {
		resizeU();
		$(window).resize(resizeU);
		function resizeU() {
			var divkuangH = $(window).height();
			$("#MainContent").height(divkuangH - 50);
		}

	});

	function modifyPwd() {
		checkSessionValid();
		$.ajax({
			type : "post",
			url : "modifyPwd",
			data : $.param({
				"userName" : $("#user").val(),
				"oldPwd" : $("#oldPwd").val(),
				"newPwd" : $("#newPwd").val(),
				"confirmPwd" : $("#confirmPwd").val()
			}),
			async : false,
			success : function(data, textStatus) {
				if (textStatus == "success") {
					var jsonObj = $.parseJSON(data);
					var success = jsonObj["success"];
					if (success == true) {
						$.messager.alert('提示', '恭喜,修改成功!', 'info');
						$("#popOptUser").window("close");
					} else {
						var error = jsonObj["error"];
						if (error == "wrongPwd") {
							$.messager.alert('提示', '对不起,修改失败,密码错误!', 'info');
						}
						else if(error == "inputInvalid")
						{
							$.messager.alert('提示', '对不起,修改失败，输入中包含不合法的字符,如:"<",">",""","\'","\\t",其它不可见字符!!', 'info');
						}
						else if (error == "emptyPwd") {
							$.messager.alert('提示', '对不起,密码不能为空!', 'info');
						} else if (error == "differentPwd") {
							$.messager.alert('提示', '对不起,修改失败,密码不一致!', 'info');
						} else if (error == "lowComplexity") {
								$.messager.alert('提示',	'对不起,保存失败,密码复制度太低[可使用字母大小写、数字、特殊字符、增加长度组合增加复杂度]!', 'info');
						} else {
							$.messager.alert('提示', '对不起,修改失败!', 'info');
						}
					}
				}
			}
		});
	}

	function logout() {
		if (checkSessionValid()) {
			$.messager.defaults = {
				ok : "确定",
				cancel : "取消"
			};
			$.messager.confirm('提示', "您确定要登出吗?", function(r) {
				if (r) {
					$.ajax({
						type : "post",
						url : "logout",
						async : false
					});
					window.location.href = "login.jsp";
				}
			});
		}
	}

	function showUser() {
		$("#hideOptUser").attr("style", "display:block");
		loadUserInfo();
		clearPasswordPage();
		$("#userTabs").tabs('select', "用户信息");
		$("#popOptUser").window({
			modal : true,
			shadow : false,
			closed : false,
			title : "我的信息"
		});
	}

	function clearPasswordPage() {
		$("#oldPwd").val(null);
		$("#newPwd").val(null);
		$("#confirmPwd").val(null);
	}

	function loadUserInfo() {
		var userName = $("#user").val();
		var roleId = "";
		var desc = "";
		var createTime = "";
		checkSessionValid();
		$.ajax({
			type : "post",
			url : "getUserInfoByName",
			data : $.param({
				"userName" : $("#user").val()
			}),
			async : false,
			success : function(data, textStatus) {
				if (textStatus == "success") {
					var jsonObj = $.parseJSON(data);
					var success = jsonObj["success"];
					if (success == true) {
						roleId = jsonObj["roleId"];
						desc = jsonObj["desc"];
						createTime = jsonObj["createTime"];
					}
				}
			}
		});
		$("#userName").html(userName);
		$("#roleId").html(roleId);
		$("#desc").val(desc);
		$("#createTime").html(formatTime(createTime));
	}

	$(function() {
		var userName = "";
		checkSessionValid();
		$.ajax({
			type : "post",
			url : "getUserNameFromSession",
			async : false,
			success : function(data, textStatus) {
				if (textStatus == "success") {
					var jsonObj = $.parseJSON(data);
					var success = jsonObj["success"];
					if (success == true) {
						userName = jsonObj["userName"];
					}
				}
			}
		});
		$("#user").val(userName);
		$("#name").html("Hi, " + userName + "!");
		$.messager.defaults = {
			ok : "确定",
			cancel : "取消"
		};
	});
</script>
<script language="javascript">
var activeMenu=null;
var activeItem=null;
function AlicnMenu(){
var value = null;
var children = [];
this.bindData = function(data){
value = data;
};
this.draw = function(parentObj){
for(var i=0;i<value.length;i++){
var nodeElement =  window.document.createElement("div");
nodeElement.id = "menu_"+ value[i].id;
nodeElement.className = "menu";
var statelink = window.document.createElement("a");
statelink.id = "statelink_"+value[i].id;
setStatueImg(value[i],statelink,true);
var namelink = window.document.createElement("a");
namelink.id = "menu" + value[i].id;
namelink.className = "linkItem";
if(value[i].children.length!=0){
statelink.href=namelink.href='javascript:menuClick(' + value[i].id + ',1)';
}else{
if(value[i].url&&value[i].url!="#"&&value[i].url!="")namelink.href =value[i].url;
/*在右边显示或者新弹出页面*/
if(value[i].target&&value[i].target=="mainFrame"&&value[i].target!="")namelink.target ="mainFrame";
if(value[i].target&&value[i].target=="_blank"&&value[i].target!="")namelink.target ="_blank";
}
namelink.innerHTML = value[i].name;
nodeElement.appendChild(statelink);
nodeElement.appendChild(namelink);
if(value[i].statusIcon){
var floatImg= window.document.createElement("img");
floatImg.src=value[i].statusIcon;
floatImg.alt='新';
floatImg.align="absmiddle";
nodeElement.appendChild(floatImg);
}
parentObj.appendChild(nodeElement);
if(value[i].children.length!=0){
drawChildren(value[i],parentObj);
}
}
};
this.setActivedItem=function(nodeId){
//document.write(document.all.tree1.innerHTML)
if(document.getElementById("statelink_"+nodeId)!=null){
var namelink = document.getElementById("statelink_"+nodeId);
if(namelink.parentNode.className=="menu"){//当前为menu
namelink.parentNode.className="indexmenu";
return;
}
namelink.parentNode.className="selectItem2";
if(namelink.parentNode.parentNode.className=="itembox"){
var nodeId=namelink.parentNode.parentNode.id.replace("children_","");
setStateImg(nodeId,"collapse");
namelink.parentNode.parentNode.style.display="block";
activeMenu = namelink.parentNode.parentNode;
}
if(namelink.parentNode.parentNode.parentNode.className=="itembox"){
namelink.parentNode.parentNode.parentNode.style.display="block";
var nodeId = namelink.parentNode.parentNode.parentNode.id.replace("children_","");
setStateImg(nodeId,"collapse");
activeMenu = namelink.parentNode.parentNode.parentNode;
getObj("menu_"+nodeId).className="activedmenu";//arrow
}
}
};
}
function getObj(str){
return document.getElementById(str);
}
function drawChildren(value,nodeElement){
var childrenElement = null;
childrenElement =  window.document.createElement("div");
childrenElement.id="children_"+value.id;
childrenElement.className="itembox";
for(var i=0;i < value.children.length;i++){
var childElement =  window.document.createElement("div");
var statelink = window.document.createElement("a");
statelink.id = "statelink_"+value.children[i].id;
childElement.className="item2";
var namelink = window.document.createElement("a");
namelink.id = "namelink_" + value.children[i].id;
if(value.children[i].children.length>0){
statelink.href = namelink.href='javascript:menuClick(' + value.children[i].id + ',2)';
}else{
if(value.children[i].url&&value.children[i].url!="#"&&value.children[i].url!="")namelink.href=value.children[i].url;
/*在右边显示或者新弹出页面*/
if(value.children[i].target&&value.children[i].target=="mainFrame"&&value.children[i].target!="")namelink.target ="mainFrame";
if(value.children[i].target&&value.children[i].target=="_blank"&&value.children[i].target!="")namelink.target ="_blank";
}
setStatueImg(value.children[i],statelink,false);
namelink.innerHTML =  value.children[i].name;
childElement.appendChild(statelink);
childElement.appendChild(namelink);
childrenElement.appendChild(childElement);
if(value.children[i].children.length>0){
drawChildren(value.children[i],childrenElement);
}
/*tip仅供非一级菜单*/
if(document.all&&value.children[i].tips){
tipsDiv= window.document.createElement("div");
tipsDiv.innerHTML="<div><div class='left'>"+value.children[i].tips+"<\/div><div class='right'><img src='images/close.gif' alt='' style='cursor:pointer;' onclick='this.parentNode.parentNode.parentNode.className=\"hidebox\"'/><\/div>";
tipsDiv.className="myaliMenuTips";
childElement.appendChild(tipsDiv);
}
if(document.all&&value.children[i].statusIcon){
var floatImg= window.document.createElement("img");
floatImg.src=value.children[i].statusIcon;
floatImg.alt='新';
floatImg.align="absmiddle";
childElement.appendChild(floatImg);
}
}
childrenElement.style.display = "none";
nodeElement.appendChild(childrenElement);
}
function setStatueImg(value,statelink,flag){//flag代表叶子节点是否出现leaf.gif
if(value.children.length>0){
statelink.innerHTML="<img src='images/expand.gif' border=0 align='absmiddle' style='cursor:hand'>";
return;
}else{
statelink.innerHTML="<img src='images/leaf.gif' border=0 align='absmiddle' style='cursor:hand'>";
return;
}
}
function menuClick(nodeId,flag){
if(flag==1){//click menu
if(getObj("children_"+nodeId).style.display=="none"){
getObj("children_"+nodeId).style.display="block";
setStateImg(nodeId,"collapse");
if(activeMenu!=null)
{
activeMenu.style.display="none";
setStateImg((activeMenu.id).replace("children_",""),"expand");
getObj("menu_"+(activeMenu.id).replace("children_","")).className="menu";//arrow
}
getObj("menu_"+nodeId).className="activedmenu";//arrow
activeMenu = getObj("children_"+nodeId);
return;
}else{
getObj("children_"+nodeId).style.display="none";
setStateImg(nodeId,"expand");
getObj("menu_"+nodeId).className="menu";//arrow
activeMenu =null;
return;
}
}
if(flag==2){
if(getObj("children_"+nodeId).style.display=="none"){
getObj("children_"+nodeId).style.display="block";
getObj("statelink_"+nodeId).innerHTML="<img src='images/collapse.gif' border=0 align='absmiddle' style='cursor:hand'>";
if(activeItem!=null){
activeItem.style.display="none";
setStateImg((activeItem.id).replace("children_",""),"expand");	
}
activeItem = getObj("children_"+nodeId);
return;
activeItem.className="item";
//activeItem=document.getElementById("namelink_"+nodeId);
//document.getElementById("namelink_"+nodeId).className="selectItem";
}
else{
document.getElementById("children_"+nodeId).style.display="none";
document.getElementById("statelink_"+nodeId).innerHTML="<img src='images/expand.gif' border=0 align='absmiddle' style='cursor:hand'>";
activeItem =null;
return;
//activeItem.className="item2";
//activeItem=document.getElementById("namelink_"+nodeId);
//document.getElementById("namelink_"+nodeId).className="selectItem";
}
}
}
function setStateImg(nodeId,state){
//alert(nodeId)
if(document.getElementById("statelink_"+nodeId)){}else return;
var statelink = document.getElementById("statelink_"+nodeId);
if(state=="collapse"){
if(statelink.parentNode.className=="menu")statelink.parentNode.className="activedmenu";
statelink.innerHTML="<img src='images/collapse.gif' border=0 align='absmiddle' style='cursor:hand'>";
return;
}
if(state=="expand"){
statelink.innerHTML="<img src='images/expand.gif' border=0 align='absmiddle' style='cursor:hand'>";
return;
}
return;
}
</script>
</head>
<body>
	<script type="text/javascript">
		var roots = {
			menu : [
				{id:"0",name:" TCC管理平台", target:"mainFrame", children:[]},
			     {id : "2",	name : "普通管理",children : [
			          {	id : "2.0",	name : "任务管理",url : "TaskList.jsp",target : "mainFrame",	children : []},
			          {	id : "2.8",	name : "任务依赖图",url : "TaskDG.jsp",target : "mainFrame",	children : []},
			          {	id : "2.1",	name : "周期管理",url : "TaskRunningStateQuery.jsp",	target : "mainFrame",children : []},
			          { id : "2.2",	name : "批量重做",url : "BatchDependTaskRedo.jsp",target : "mainFrame",children : []	},
			          {	id : "2.3",	name : "告警管理",url : "AlarmManage.jsp",target : "mainFrame",children : []	},
			          { id : "2.4",	name : "周期队列",url : "TaskCycleQueue.jsp",target : "mainFrame",children : []},
			          {	id : "2.5",	name : "长执行时间查询",	url : "LongShellQuery.jsp",	target : "mainFrame",children : []},
			          {	id : "2.6",	name : "TCC帮助",url : "Help.jsp",	target : "_blank",children : []},
			          {	id : "2.7",	name : "Hadoop帮助",	url : "HadoopFAQ.html",	target : "_blank",children : []}
			     ]}
			   ]};
	</script>
	<script type="text/javascript">
	function treeInit(){
		  var menu = null;
		  var parent1 = document.getElementById("treeMenu");
		  menu=new AlicnMenu();
		  menu.bindData(roots.menu);
		  menu.draw(parent1);
		  menuClick(2,1);
		  menu.setActivedItem("0");
		  if(document.getElementById("leftmenu")&&document.getElementById("content")&&document.getElementById("content").offsetHeight){
			if(document.getElementById("content").offsetHeight > document.getElementById("leftmenu").offsetHeight+150){
				if(document.all){
					document.getElementById("leftmenu").style.height=document.getElementById("content").offsetHeight;
				}else{
					document.getElementById("leftmenu").setAttribute("style","height:"+document.getElementById("content").offsetHeight+"px");
				}
			}
		}
		}
	</script>

	<div id="Container">
		<div id="Header">
			<div class="tit">
				<h3>
					<b>任务控制中心（TCC）</b>
				</h3>
			</div>
			<div id="userOpt">
				<span id="name"></span> <a id="userInfo" href="javascript:void(0)"
					onclick='showUser()'>我的信息</a> <a id="logout"
					href="javascript:void(0)" onclick='logout()'>登出</a>
			</div>
		</div>

		<div id="MainContent">
			<div class="left">
				<div id="leftmenu">
					<div id="treeMenu"></div>
					<script type="text/javascript">
						treeInit();
					</script>
				</div>
			</div>
			<div id="Content">
				<iframe id="mainFrame" name="mainFrame" frameborder="0"
					scrolling="yes" width="100%" height="100%" src="TaskList.jsp"></iframe>
			</div>

		</div>
	</div>
	<div id="popOptUser" class="easyui-window" inline="false" closed="true"
		style="width: 610px; height: 350px; padding: 10px">
		<div id="hideOptUser" class="easyui-layout" fit="true"
			style="display: none">
			<div region="center" border="false"
				style="padding: 10px; background: #fff; border: 0px solid #ccc;">
				<form id="userInfoForm" name="userInfoForm" method="post">
					<div>
						<input id="user" type="hidden" />
					</div>
					<div id="userTabs" class="easyui-tabs"
						style="margin: 0px 0px 5px 0px; margin-left: auto; margin-right: auto; background: #fff; border: 0px solid #ccc; width: 550px;"
						fit="false" plain="true">
						<div title="用户信息" style="padding: 10px; min-height: 220px;">
							<div
								style="margin: 5px 0px 5px 0px; margin-left: auto; margin-right: auto; background: #fff; border: 0px solid #ccc; width: 450px;">
								<table width="450px" cellpadding="10px" cellspacing="15px" boarder="0px"
									style="margin-left: auto; margin-right: auto;">
									<tr>
										<td style="width: 70px"><span>用户名: </span></td>
										<td><span id="userName"
											style="color: black"></span>
										</td>
									</tr>
									<tr>
										<td style="width: 70px"><span>角色:</span></td>
										<td><span id="roleId"
											style="color: black"></span>
										</td>
									</tr>
									<tr>
										<td style="width: 70px"><span>创建时间:</span></td>
										<td><span id="createTime"
											style="color: black"></span>
										</td>
									</tr>
									<tr>
										<td style="width: 70px"><span>描述:</span></td>
										<td><textarea id="desc" maxlength="2048" rows="5" readonly="true"
									style="width: 350px;color: black"></textarea>
										</td>
									</tr>
								</table>
							</div>
						</div>
						<div title="修改密码" style="padding: 10px; min-height: 220px;">
							<div
								style="margin: 5px 0px 5px 0px; margin-left: auto; margin-right: auto; background: #fff; border: 1px solid #ccc; width: 450px;">
								<table width="450px" cellspacing="25px" boarder="0px"
									style="margin-left: auto; margin-right: auto;">
									<tr>
										<td><span>旧密码: </span></td>
										<td><input id="oldPwd" type="password"
											style="width: 300px" panelHeight="200px" />
										</td>
									</tr>
									<tr>
										<td><span>新密码:</span></td>
										<td><input id="newPwd" type="password"
											style="width: 300px" panelHeight="200px" />
										</td>
									</tr>
									<tr>
										<td><span>密码确认:</span></td>
										<td><input id="confirmPwd" type="password"
											style="width: 300px" panelHeight="200px" /></td>
									</tr>
								</table>
							</div>
							<div region="south" border="false"
								style="text-align: right; padding: 5px 30px; height: 20px;">
								<a class="easyui-linkbutton" plain="true" iconCls="icon-ok"
									href="javascript:void(0)" onclick="modifyPwd()">保存修改</a>
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>