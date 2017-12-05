<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<div class="tb-note" style="display: none;">
	<p>
		您的浏览器较陈旧，在本站中不能获得最佳体验，建议使用 <a
			href="http://rj.baidu.com/soft/detail/14744.html">谷歌(42+)</a>，<a
			href="http://www.firefox.com.cn/download/">火狐(36+)</a>，<a
			href="http://windows.microsoft.com/zh-cn/internet-explorer/download-ie">IE(11+)</a>
		等较新浏览器
	</p>
	<a class="tbn-close" href="javascript:void(0)"></a>
</div>

<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="<%=request.getContextPath()%>" />
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>EMUI推荐平台</title>
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/bi.css">
<link href="${ctx}/static/css/bootstrap-3.3.5/bootstrap.min.css"
	rel="stylesheet">


<style type="text/css">
#navbar li {
	padding-left: 8%;
}

#carousel-generic {
	width: 50%;
	margin-left: 19%;
}

.tb-note {
	background-color: #fdd97e;
	height: 34px;
	width: 100%;
	border-bottom: 1px solid #bbb;
}

.tb-note p {
	line-height: 34px;
	font-size: 14px;
	color: #666;
	padding-left: 18px;
}

.tb-note p a {
	text-decoration: underline;
	color: #2793BF;
}
</style>
</head>
<body>

	<div class="up-topbar">

		<ul class="tb-cont">

			<li><a href="#">个人信息</a></li>
			<li><a href="javascript:void(0);">退出</a></li>
		</ul>

	</div>


	<div class="up-main mkcl">


		<ul class="upm-nav">
			<!-- 本页选中的话， 就加 uni-cur-->
			<li class="un-item uni-cur"><a href="index"
				class="uni-link">首页</a></li>
			<li class="un-item"><a href="domain_manager" class="uni-link">领域对象管理</a>
			</li>
			<li class="un-item"><a href="scenario_manager" class="uni-link">应用场景管理</a></li>
			<li class="un-item"><a href="business_manager" class="uni-link">业务规则管理</a></li>
			<li class="un-item"><a href="effect_monitor" class="uni-link">效果监控</a></li>
			<li class="un-item"><a href="user_manager" class="uni-link">用户管理</a></li>
			<li class="un-item"><a href="system_manager" class="uni-link">系统配置</a></li>
		</ul>
		<div class="upm-cont upm-index"
			style="height: 668px; background: url(${ctx}/static/images/indexImg.jpg) repeat-x;">

			<div class="upmc-container">
				<p style="color: #fff; line-height: 2;">
					
				</p>
			</div>

		</div>
	</div>
	<div class="up-ft">
		<p>EMUI官方网站 - (粤ICP备09176709号-16）- 华为软件技术有限公司 @ HUAWEI,2011. All
			Rights Reserved</p>
	</div>

	<%-- <nav id="navbar" class="navbar navbar-default navbar-fixed-top">
		<div class=container-fluid>
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar-collapse"
					aria-expanded="false">
					<span class="sr-only">切换导航</span> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
				</button>

			</div>
			<div class="collapse navbar-collapse" id="navbar-collapse">
				<ul class="nav navbar-nav" style="width: 87%;">
					<li class="active"><a href="index">首页</a></li>
					<li><a href="domain_manager">领域对象管理</a></li>
					<li><a href="scenario_manager">应用场景管理</a></li>
					<li><a href="business_manager">业务规则管理</a></li>
					<li><a href="effect_monitor">效果监控</a></li>
					<li><a href="user_manager">用户管理</a></li>
					<li><a href="system_manager">系统配置</a></li>
				</ul>
				<ul class="nav navbar-nav navbar-right" style="width: 8%;">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false">系统 <span class="caret"></span></a>
						<ul class="dropdown-menu">
							<li style="text-align: center; padding-left: 0px;"><a
								href="#">个人信息</a></li>
							<li role="separator" class="divider"></li>
							<li style="text-align: center; padding-left: 0px;"><a
								href="#">退出</a></li>
						</ul></li>
				</ul>
			</div>
		</div>
	</nav>

	<div id="carousel-generic" class="carousel slide" data-ride="carousel">
		<ol class="carousel-indicators">
			<li data-target="#carousel-generic" data-slide-to="0"></li>
			<li data-target="#carousel-generic" data-slide-to="1"></li>
			<li data-target="#carousel-generic" data-slide-to="2"></li>
			<li data-target="#carousel-generic" data-slide-to="3"></li>
			<li data-target="#carousel-generic" data-slide-to="4"></li>
			<li data-target="#carousel-generic" data-slide-to="5"></li>
		</ol>
		<div class="carousel-inner" role="listbox">
			<div class="item active">
				<img src="${ctx}/static/images/res/index/carousel_1.jpg" alt="首页">
				<div class="carousel-caption">...</div>
			</div>
			<div class="item">
				<img src="${ctx}/static/images/res/index/carousel_2.jpg"
					alt="领域对象管理">
				<div class="carousel-caption">...</div>
			</div>
			<div class="item">
				<img src="${ctx}/static/images/res/index/carousel_3.jpg"
					alt="应用场景管理">
				<div class="carousel-caption">...</div>
			</div>
			<div class="item">
				<img src="${ctx}/static/images/res/index/carousel_4.jpg"
					alt="业务规则管理">
				<div class="carousel-caption">...</div>
			</div>
			<div class="item">
				<img src="${ctx}/static/images/res/index/carousel_5.jpg" alt="用户管理">
				<div class="carousel-caption">...</div>
			</div>
			<div class="item">
				<img src="${ctx}/static/images/res/index/carousel_6.jpg" alt="系统配置">
				<div class="carousel-caption">...</div>
			</div>
		</div>
		<a class="left carousel-control" href="#carousel-generic"
			role="button" data-slide="prev"> <span
			class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span> <span
			class="sr-only">Previous</span>
		</a> <a class="right carousel-control" href="#carousel-generic"
			role="button" data-slide="next"> <span
			class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
			<span class="sr-only">Next</span>
		</a>
	</div> --%>
	<script src="${ctx}/static/js/jquery/jquery-1.11.3.js"></script>
	<script src="${ctx}/static/js/bootstrap-3.3.5/bootstrap.min.js"></script>
	<script>
		var checkBrowser = function() {

			var supportBrowser = false;
			var ua = navigator.userAgent.toLowerCase();
			// 火狐
			var firefox = ua.match(/firefox\/([\d.]+)/);
			// 谷歌
			var chrome = ua.match(/chrome\/([\d.]+)/);
			// IE(6-9)不可以，IE10,11可以
			var msie = ua.match(/msie\s([\d.]+)/);

			if (firefox || chrome || (null == msie)
					|| ("msie 10.0,10.0" == msie)) {
				supportBrowser = true;
			} else {
				supportBrowser = false;
			}
			return supportBrowser;
		};

		var noIE8 = function() {
			if (!checkBrowser()) {
				$(".tb-note").show();
			}

			$(".tb-note").on("click", ".tbn-close", function() {
				$(".tb-note").slideUp(400);
			});
			setTimeout(function() { // 15秒后自动收起
				$(".tb-note").slideUp(400);
			}, 15000);
		};
		noIE8();

		/* 	$("#navbar-collapse a").mouseover(function() {
				if (!$(this).parent().hasClass("active")) {
					$(this).css("background-color", "#DCDCDC");
				}
			});
			$("#navbar-collapse a").mouseout(function() {
				if (!$(this).parent().hasClass("active")) {
					$(this).css("background-color", "#F8F8F8");
				}
			});
			$(document).ready(
					function() {
						$("#navbar-collapse").find(".active").children("a").css(
								"background-color", "#87CEFA");
					}); */
	</script>


</body>
</html>
