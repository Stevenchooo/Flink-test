<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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

img {
	border: 0;
	-ms-interpolation-mode: bicubic;
}

table {
	border-collapse: collapse;
	border-spacing: 0;
}

button, select {
	text-transform: none;
}

button::-moz-focus-inner, input::-moz-focus-inner {
	border: 0;
	padding: 0;
}

textarea {
	overflow: auto;
	vertical-align: top;
	resize: none;
}

ol, ul {
	list-style: none;
}

em {
	font-weight: normal;
	font-style: normal;
}

input, textarea, select {
	font-family: inherit;
	font-size: inherit;
	font-weight: inherit
}

input, textarea, select {
	*font-size: 100%
}

h1, h2, h3, h4, h5, h6, b {
	font-size: 100%;
	font-weight: normal;
}

td {
	word-break: break-all;
	word-wrap: break-word;
}

/* common */
html {
	font-size: 100%;
	-webkit-text-size-adjust: 100%;
	overflow-y: scroll;
}

body {
	font-size: 12px;
	font-family: Microsoft Yahei, STHeiti, Simsun, STSong;
	color: #333;
	background-color: #F7F7F7;
	min-width: 1200px;
}

a {
	text-decoration: none;
	color: #7c7c7c;
}

a:focus, a:active, a:hover, input {
	outline: 0;
}

.mkcl:after, .sm-scale .sm-c:after {
	visibility: hidden;
	display: block;
	font-size: 0;
	content: " ";
	clear: both;
	height: 0;
}

.mkcl, .sm-scale .sm-c {
	*zoom: 1;
}

.posR {
	position: relative;
}

.vm {
	vertical-align: middle;
}

.up-topbar {
	position: relative;
	padding-right: 36px;
	background: #4A96DC url(img/titleban.png) no-repeat 12px center;
	height: 81px;
}

.tb-cont {
	float: right;
	margin-top: 42px;
}

.tb-cont a {
	color: #fff;
}

.tb-cont a:hover {
	text-decoration: underline;
}

.tb-cont li {
	float: left;
	margin-left: 40px;
	line-height: 28px;
}

.up-main {
	border-bottom: 1px solid #DEDEDE;
}

.upm-nav {
	width: 180px;
	float: left;
}

.upm-cont {
	margin-left: 180px;
	background-color: #FFF;
	min-height: 600px;
	padding: 0 18px 20px;
	position: relative;
	overflow-x: hidden;
}

.upmc-container {
	padding-top: 12px;
}

.uni-link {
	border-bottom: 1px solid #D9D9D9;
	height: 48px;
	display: block;
	padding-left: 46px;
	line-height: 48px;
	color: #333;
	font-size: 14px;
	position: relative;
}

.uni-link b {
	width: 11px;
	height: 7px;
	position: absolute;
	right: 32px;
	top: 22px;
	background-position: 0 0;
}

.uni-cur .uni-link b {
	background-position: -50px 0;
}

.uni-cur .uni-link {
	background-color: #76ADDE;
	color: #FFF;
}

.uni-1st .uni-link {
	border-top: 0;
}

.uni-sub {
	display: none;
}

.uni-cur .uni-sub {
	display: block;
	border-bottom: 1px solid #D9D9D9;
}

.uni-sub a {
	display: block;
	padding-left: 60px;
	line-height: 36px;
	height: 36px;
	color: #333;
	font-size: 12px;
}

.uni-cur a.unis-cur, .uni-sub a.unis-cur {
	color: #FFF;
	background-color: #ac6ae4;
}

.uni-sub a.unis-cur:hover {
	color: #fff;
}

.un-item a:hover {
	color: #4A96DC;
}

.uni-cur .uni-link:hover {
	color: #fff;
	cursor: default;
}

.up-ft {
	color: #8E8E8E;
	text-align: center;
	padding: 28px 0;
}

.up-ft p {
	line-height: 2;
}

.upm-sidebar {
	position: absolute;
	top: 0;
	background-color: #f2f2f2;
}

.us-sideactive {
	display: block;
	font-size: 16px;
	word-wrap: break-word;
	width: 30px;
	background-color: #F2F2F2;
	text-align: center;
	color: #666;
	padding: 16px 0;
	position: absolute;
	top: 50%;
	margin-top: -100px;
	left: -30px;
	cursor: pointer;
	cursor: pointer;
}

.us-sidecont {
	padding: 10px;
}

.qr-nogood {
	padding: 150px 0;
}

.qr-nogood table {
	width: 100%;
}

.qr-nogood th {
	text-align: right;
	width: 44.5%;
	padding-right: 1.5%;
}

.qr-nogood td {
	width: 54%;
	color: #24B9BF;
	font-size: 14px;
	line-height: 1.4;
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


		
		<div class="upm-cont upm-index qr-nogood" style="height: 500px;">
          
					<table>
						<tr>						
							<td style="text-align:center;font-size:20px;">对不起，您没有访问此页面的权限<br />如需访问，请联系管理员申请权限
							</td>
						</tr>
					</table>
		</div>
	</div>
	<div class="up-ft">
		<p>EMUI官方网站 - (粤ICP备09176709号-16）- 华为软件技术有限公司 @ HUAWEI,2011. All
			Rights Reserved</p>
	</div>

</body>
</html>
