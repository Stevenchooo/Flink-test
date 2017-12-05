<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="<%=request.getContextPath()%>" />
<!DOCTYPE html>
<html lang="zh-CN" ng-app="domain_manager_app">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- <meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"> -->
<title>领域对象管理</title>
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/bi.css">
<link href="${ctx}/static/css/bootstrap-3.3.5/bootstrap.min.css"
	rel="stylesheet">
<!--[if lt IE 9]>
	      <script src="${ctx}/static/js/bootstrap-3.3.5/html5shiv.min.js"></script>
	      <script src="${ctx}/static/js/bootstrap-3.3.5/respond.min.js"></script>
	    <![endif]-->
<style type="text/css">

#navbar li {
	padding-left: 8%;
}

#domainTable th {
	text-align: center;
	vertical-align: middle;
}

#domainTable td {
	text-align: center;
	vertical-align: middle;
}

#domainPropertyTable th {
	text-align: center;
	vertical-align: middle;
}

#domainPropertyTable td {
	text-align: center;
	vertical-align: middle;
}

.package-tree {
	color: #428BCA;
	font-size: 14px;
	line-height: 1.42857;
	font-weight: 100;
}
</style>
</head>

<body ng-controller="domainController">
<div class="up-topbar">

    <ul class="tb-cont">

        <li>
            <a href="#">个人信息</a>
        </li>
        <li>
            <a href="javascript:void(0);">退出</a>
        </li>
    </ul>

</div>

<div class="up-main mkcl">


    <ul class="upm-nav">
    	<!-- 本页选中的话， 就加 uni-cur-->
        <li class="un-item ">
            <a href="index" class="uni-link">首页</a>
        </li>
        <li class="un-item uni-cur">
            <a href="domain_manager" class="uni-link">领域对象管理</a>
        </li>
        <li class="un-item">
            <a href="scenario_manager" class="uni-link">应用场景管理</a>
        </li>
        <li class="un-item">
            <a href="business_manager" class="uni-link">业务规则管理</a>
        </li>
        <li class="un-item">
            <a href="effect_monitor" class="uni-link">效果监控</a>
        </li>
        <li class="un-item">
            <a href="user_manager" class="uni-link">用户管理</a>
        </li>
        <li class="un-item">
            <a href="system_manager" class="uni-link">系统配置</a>
        </li>
    </ul>
    <div class="upm-cont">
    	
        <div class="upmc-container">
            <!-- main body -->
	<div class="panel panel-default"
		style="padding: 10px; margin-left: 0.5%; margin-right: 0.5%; padding-bottom: 0px;">
		<div class="row">
			
			<div class="col-md-12">
				<div class="row">
					<div class="col-md-1">
						<button type="button" class="btn btn-success" data-toggle="modal"
							data-target="#domainModal" ng-click="newDomainModal();">
							<i class="glyphicon glyphicon-plus"></i>&nbsp;新建
						</button>
					</div>
					<div class="col-md-1">
						<button type="button" class="btn btn-danger"
							ng-click="deleteDomains();">
							<i class="glyphicon glyphicon-trash"></i>&nbsp;删除
						</button>
					</div>
					<div class="col-md-7"></div>
					<div class="col-md-3">
						<div class="btn-group">
							<button type="button" class="btn btn-info"
								ng-click="defaultSort();">默认排序</button>
							<button type="button" class="btn btn-info dropdown-toggle"
								data-toggle="dropdown" aria-haspopup="true"
								aria-expanded="false">
								<span class="caret"></span> <span class="sr-only"></span>
							</button>
							<ul class="dropdown-menu">
								<li><a href="#" ng-click="sortById();">按“ID”排序</a></li>
								<li role="separator" class="divider"></li>
								<li><a href="#" ng-click="sortByName();">按“名称”排序</a></li>
								<li role="separator" class="divider"></li>
								<li><a href="#" ng-click="sortByPublished();">按“发布情况”排序</a></li>
							</ul>
						</div>
					</div>
				</div>
				<div class="panel panel-default"
					style="margin-top: 1%; margin-bottom: 0px;">
					<div class="table-responsive">
						<table id="domainTable"
							class="table table-hover table-striped table-bordered"
							ng-model="domains">
							<thead class="active">
								<tr>
									<th><input type="checkbox" onclick="tableCheckBox();">
									</th>
									<th>&nbsp;&nbsp;&nbsp;&nbsp;ID&nbsp;&nbsp;&nbsp;&nbsp;</th>
									<th>&nbsp;&nbsp;&nbsp;&nbsp;领域对象名称&nbsp;&nbsp;&nbsp;&nbsp;</th>
									<th>发布</th>
									<th>操作</th>
								</tr>
							</thead>
							<tbody>
								<tr ng-repeat="domain in domains track by $index">
									<td><input type="checkbox"
										ng-disabled="'true'==domain.published"></td>
									<td><span ng-bind="domain.id"></span></td>
									<td><span ng-bind="domain.name"></span></td>
									<td><span ng-bind="{true: '是', false: '否'}[domain.published]"></span></td>
									<td>
										<button type="button" class="btn btn-link"
											ng-disabled="'true'==domain.published"
											ng-click="deleteDomain(domain.id);">
											<i class="glyphicon glyphicon-trash"></i>
										</button>
										<button type="button" class="btn btn-link"
											ng-click="updateDomainModal(domain);" data-toggle="modal">
											<i class="glyphicon glyphicon-pencil"></i>
										</button>
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div style="margin-left: auto; margin-right: auto; width: 95%">
					<span ng-bind="pageInfo"
						style="float: left; margin-top: 23px; font-size: 15px;"></span> <select
						class="form-control" ng-change="setPageSize();"
						ng-model="pageSize"
						style="float: left; margin-top: 20px; width: 75px; margin-left: 10px;">
						<option value="10">10</option>
						<option value="25">25</option>
						<option value="50">50</option>
						<option value="100">100</option>
						<option value="ALL">全部</option>
					</select> <span
						style="float: left; margin-top: 23px; font-size: 15px; margin-left: 10px;">条</span>
					<nav id="page_nav" style="float: right;" ng-show="page_nav_show">
						<ul class="pagination">
							<li><a href="" aria-label="Previous" ng-click="page_pre();">
									<span aria-hidden="true">&laquo;</span>
							</a></li>
							<li ng-repeat="pagination in paginations track by $index"
								class="ng-class:pagination.pagination_class;"><a href=""
								ng-bind="pagination.number"
								ng-click="page_chosen(pagination.number);"></a></li>
							<li><a href="" aria-label="Next" ng-click="page_next();">
									<span aria-hidden="true">&raquo;</span>
							</a></li>
						</ul>
					</nav>
				</div>
			</div>
		</div>
	</div>
            
        </div>

        <div class="upm-sidebar" style="right:-360px;width:360px;height:803px;z-index: 3;" data-expand="closed">
            <a href="javascript:void(0);" class="us-sideactive">展开设置</a>
            <div class="us-sidecont">
            <div  style="padding-right: 0px;">
				<div class="input-group"
					style="margin-left: auto; margin-right: auto; padding-bottom: 10px; width: 95%;">
					<input type="text" class="form-control" placeholder="package name"
						ng-model="package_tree_filter_name">
				</div>
				<div ng-show="false">
					<span ng-model="new_tree_package_relation"></span>
				</div>
				<div class="panel panel-default"
					style="height: 720px; overflow-y: auto;"
					ng-mouseup="tree_mouseup($event);">
					<div ng-show="false" ng-model="tree_data"></div>
					<div
						ng-repeat="tree_package in tree_data | package_tree_filter:package_tree_filter_name">
						<div
							class="package-tree list-group-item  glyphicon glyphicon-plus ng-cloak "
							style="margin-bottom: -1px; border: 1px solid #DDD;"
							ng-bind="' '+tree_package.domainPackageName"
							ng-mouseup="tree_package_mouseup($event, tree_package);" ng-cloak>
							</div>
						<div
							ng-repeat="tree_package_relation in tree_package.domainPackageRelations track by $index"
							style="display: none;">
							<span
								style="float: left; margin-left: 20px; margin-right: 20px; height: 40px"></span>
							<i class="glyphicon glyphicon-ok-circle"
								style="color: #428BCA; float: left; padding-top: 2px; margin-top: 12px"
								ng-show="tree_package_relation.domainPublished=='true'"></i>
							<div
								style="display: block; padding: 10px 15px; margin-bottom: -1px; border: 1px solid #DDD;"
								class="package-tree"
								ng-mouseup="tree_package_relation_mouseup($event, tree_package_relation);">{{tree_package_relation.domainName}}</div>
						</div>
					</div>
				</div>
			</div>
            </div>
        </div>
    </div>
</div>
<div class="up-ft">
    <p>EMUI官方网站 - (粤ICP备09176709号-16）- 华为软件技术有限公司 @ HUAWEI,2011. All Rights Reserved</p>
</div>
	<!-- 操作提示信息modal -->
	<div class="modal fade" id="infoModal" role="dialog">
		<div class="modal-dialog modal-lg" role="document"
			style="width: 300px; position: relative; top: 250px">
			<input ng-bind="result" ng-show="false" />
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close" ng-click="domainPropertyModalCancel();">
						<span aria-hidden="true">&times;</span>
					</button>
					<h5 class="modal-title" id="domainPropertyModalLabel">提示：</h5>
				</div>
				<div class="modal-body">
					<h5 class="modal-title">对象正在使用，请勿编辑！</h5>
				</div>
			</div>
		</div>
	</div>
	<!-- 删除提示框 -->
	<div class="modal fade" id="deleteModal" role="dialog"
		data-backdrop="static">
		<div class="modal-dialog modal-lg" role="document"
			style="width: 300px; position: relative; top: 250px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close" ng-click="deleteCancel();">
						<span aria-hidden="true">&times;</span>
					</button>
					<h5 class="modal-title">提示：</h5>
				</div>
				<div class="modal-body">
					<h5 class="modal-title">删除后将无法恢复！确定删除吗？</h5>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal"
						ng-click="deleteCancel();">取消</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal"
						ng-click="deleteItem();">确定</button>
				</div>
			</div>

		</div>
	</div>
	<!-- 群删提示框 -->
	<div class="modal fade" id="deleteMessage" role="dialog"
		data-backdrop="static">
		<div class="modal-dialog modal-lg" role="document"
			style="width: 300px; position: relative; top: 250px;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close" ng-click="resultCancel();">
						<span aria-hidden="true">&times;</span>
					</button>
					<h5 class="modal-title">提示：</h5>
				</div>
				<div class="modal-body">
					<h5 class="modal-title" style="text-align: center;">未选中删除项！</h5>
				</div>
			</div>

		</div>
	</div>
	<!-- New/Update Domain Property Modal -->
	<div class="modal fade" id="domainPropertyModal" role="dialog"
		aria-labelledby="domainPropertyModalLabel" data-backdrop="static">

		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close" ng-click="domainPropertyModalCancel();">
						<span aria-hidden="true">&times;</span>
					</button>
					<h5 class="modal-title" id="domainPropertyModalLabel">领域对象属性</h5>
				</div>
				<form name="formProperty" class="form-horizontal" novalidate>
					<div class="modal-body">
						<div class="row" style="padding-bottom: 15px;">
							<div class="col-md-2" style="text-align: right;">
								<button type="button" style="padding: 0px;" class="btn btn-link"
									ng-click="addDomainPropertyModal();">
									<i class="glyphicon glyphicon-plus"></i>&nbsp;
								</button>
							</div>
							<div class="col-md-3"
								style="text-align: center; padding-left: 0px; padding-right: 0px;">
								<span style="font-size: 15px;">对象名称：</span>
							</div>
							<div class="col-md-7" style="padding-left: 0px;">
								<span style="font-size: 15px;" ng-bind="domainPropertyName"></span>
							</div>
						</div>
						<table id="domainPropertyTable"
							class="table table-hover table-striped"
							ng-model="domainPropertys">
							<tbody>
								<tr
									ng-repeat="domainProperty in domainPropertys track by $index">
									<td style="padding-right: 0px;">属性名称:</td>
									<td style="padding-left: 0px;"><div class="has-feedback">
											<input type="text" name="name{{$index}}" class="form-control"
												placeholder="不为空且长度不超过255个字符" ng-model="domainProperty.name"
												ng-minlength="1" ng-maxlength="255" required> <span
												class="glyphicon glyphicon-ok form-control-feedback"
												ng-show="formProperty.name{{$index}}.$valid"></span>
										</div></td>
									<td style="padding-right: 0px;">属性值类型:</td>
									<td style="padding-left: 0px;"><select
										class="form-control" ng-model="domainProperty.category"
										style="width: 100px;">
											<option value="boolean">boolean</option>
											<option value="int">int</option>
											<option value="float">float</option>
											<option value="double">double</option>
											<option value="varchar">varchar</option>
											<option value="date">date</option>
									</select></td>
									<td style="padding-right: 0px;">默认值:</td>
									<td style="padding-left: 0px;"><div class="has-feedback">
											<input data-res-parse
												ng-if="'varchar'!=domainProperty.category"
												type="number" name="default{{$index}}" class="form-control"
												placeholder="长度不超过255"
												ng-model="domainProperty.defaultVal" 
												ng-maxlength="255" > <input
												ng-if="'varchar'==domainProperty.category" type="text"
												name="default{{$index}}" class="form-control"
												placeholder="长度不超过255"
												ng-model="domainProperty.defaultVal" 
												ng-maxlength="255" ><span
												class="glyphicon glyphicon-ok form-control-feedback"
												ng-show="formProperty.default{{$index}}.$valid"></span>
										</div></td>
									<td>
										<button type="button" class="btn btn-link"
											ng-click="deleteDomainPropertyModal($index);">
											<i class="glyphicon glyphicon-trash"></i>
										</button>
									</td>
								</tr>
							</tbody>
						</table>
					</div>

					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal"
							ng-click="domainPropertyModalCancel();">关闭</button>
						<button type="button" class="btn btn-primary" data-dismiss="modal"
							ng-click="domainPropertyModalSave();"
							ng-disabled="'true' == oldDomain.published||formProperty.$invalid">保存</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- New/Update Domain Modal -->
	<div class="modal fade" id="domainModal" role="dialog"
		aria-labelledby="domainModalLabel" ng-show="isDomainModalShow"
		data-backdrop="static">

		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close" ng-click="domainModalCancel();">
						<span aria-hidden="true">&times;</span>
					</button>
					<h5 class="modal-title" id="domainModalLabel"
						ng-bind="domainModalTitle"></h5>
				</div>
				<form id="form_save" name="form" class="form-horizontal" novalidate>
					<div class="modal-body">
						<div class="row" ng-show="false">
							<span ng-model="isNewOrUpdateDomain"></span>
						</div>
						<div class="row" ng-show="false">
							<span ng-model="oldDomain"></span>
						</div>
						<div class="row" ng-show="false">
							<span ng-model="newDomain"></span>
						</div>
						<div class="row" class="form-group  has-feedback">
							<div class="col-md-4"
								style="padding-right: 0px; padding-top: 7px;">
								<span style="float: right; font-size: 13px;">对象名称：</span>
							</div>
							<div class="col-md-8">
								<input type="text" name="name" class="form-control"
									ng-disabled="'true' == oldDomain.published"
									placeholder="不为空且长度不超过255个字符" ng-model="newDomain.name"
									ng-minlength="1" ng-maxlength="255" required> <span
									style="padding-right: 50px;"
									class="glyphicon glyphicon-ok form-control-feedback"
									ng-show="form.name.$valid"></span>
							</div>
						</div>
						<div class="row" style="padding-top: 10px;">
							<div class="col-md-4" style="padding-right: 0px;">
								<span style="float: right; font-size: 13px;">是否已发布：</span>
							</div>
							<div class="col-md-8">
								<select class="form-control" ng-model="newDomain.published"
									style="width: 80px;" ng-disabled="isNewOrUpdateDomain=='new'">
									<option value="false">否</option>
									<option value="true">是</option>
								</select>
							</div>
						</div>
					</div>
				</form>
				<div class="modal-footer">
					<button type="button" 
						class="btn btn-info" style="float: left;"
						ng-show="isNewOrUpdateDomain=='update'"
						ng-click="setDomainPropertyName();">
						<i class="glyphicon glyphicon-list-alt"></i>&nbsp;领域对象属性
					</button>
					<button type="button" class="btn btn-default" data-dismiss="modal"
						ng-click="domainModalCancel();">关闭</button>
					<input ng-bind="save_temp" type=text ng-show="false" />
					<button type="button" class="btn btn-primary" data-dismiss="modal"
						ng-disabled="'ok'==save_temp||form.$invalid"
						ng-click="domainModalSave();">保存</button>
				</div>
			</div>
		</div>
	</div>

	<!-- tree右键菜单 -->
	<div>
		<ul class="dropdown-menu" id="tree_menu">
			<li style="text-align: center;"><a href="#"
				ng-click="tree_new_package_func($event);">新建Package</a></li>
			<li class="divider"></li>
			<li style="text-align: center;"><a href="#"
				ng-click="tree_refresh_package_func();">刷新</a></li>
		</ul>
	</div>

	<!-- New/Update Package  -->
	<div>
		<div ng-show="false">
			<span ng-model="new_package"></span>
		</div>
		<div ng-show="false">
			<span ng-model="isNewOrUpdatePackage"></span>
		</div>

		<ul class="dropdown-menu" id="new_package_menu">
			<form name="pkgForm" class="form-horizontal" novalidate>
				<li style="padding-top: 20px;">
					<div class="row" class="form-group  has-feedback">
						<div class="col-md-4"
							style="text-align: center; padding-right: 0px; padding-top: 7px;">
							<span style="font-size: 15px;">Package名称:</span>
						</div>
						<div class="col-md-8" style="padding-left: 0px;">
							<input type="text" class="form-control" style="width: 220px;"
								name="name" placeholder="不为空且长度不超过20"
								ng-model="new_package.name" ng-minlength="1" ng-maxlength="20"
								required> <span style="padding-right: 50px;"
								class="glyphicon glyphicon-ok form-control-feedback"
								ng-show="pkgForm.name.$valid"></span>
						</div>
					</div>
				</li>
			</form>
			<li style="padding-top: 20px;">
				<button type="button" class="btn btn-primary"
					style="float: right; margin-right: 20px;"
					ng-disabled="pkgForm.$invalid" ng-click="new_package_menu_save();">保存</button>
				<button type="button" class="btn btn-default"
					style="float: right; margin-right: 20px;"
					ng-click="new_package_menu_cancel();">关闭</button>
			</li>
		</ul>

	</div>

	<!-- Package 右键菜单 -->
	<div>
		<ul class="dropdown-menu" id="package_menu">
			<li style="text-align: center;"><a href="#"
				ng-click="package_menu_new_relation_func($event);">新建PackageRelation</a></li>
			<li class="divider"></li>
			<li style="text-align: center;"><a href="#"
				ng-click="package_menu_update_package_func($event);">修改</a></li>
			<li class="divider"></li>
			<li style="text-align: center;"><a href="#"
				ng-click="package_menu_delete_package_func();">删除</a></li>
			<li class="divider"></li>
			<li style="text-align: center;"><a href="#"
				ng-click="package_menu_close_func();">关闭</a></li>
		</ul>
	</div>

	<!-- New/Update Package Relation  -->
	<div>
		<div ng-show="false">
			<span ng-model="isNewOrUpdatePackageRelation"></span>
		</div>
		<ul class="dropdown-menu" id="new_package_relation_menu">
			<li style="padding-top: 20px;">
				<div class="row">
					<div class="col-md-5"
						style="text-align: right; padding-right: 10px; padding-top: 7px;">
						<span style="font-size: 15px;">Package名称:</span>
					</div>
					<div class="col-md-7" style="padding-left: 0px; padding-top: 7px;">
						<span ng-bind="new_package.name"
							style="word-break: normal; width: auto; display: block; white-space: pre-wrap; word-wrap: break-word; overflow: hidden;"></span>
					</div>
				</div>
			</li>
			<li style="padding-top: 20px;">
				<div class="row">
					<div class="col-md-5"
						style="text-align: right; padding-right: 10px; padding-top: 7px;">
						<span style="font-size: 15px;">领域对象:</span>
					</div>
					<div class="col-md-7" style="padding-left: 0px;">
						<select class="form-control" style="width: 150px;"
							ng-model="new_package_relation_domain.id"
							ng-options="package_domain.id as package_domain.name for package_domain in package_domains"
							ng-selected="new_package_relation_domain.id==package_domain.id">
							<!-- 
                            <option ng-repeat="package_domain in package_domains track by $index" value="{{package_domain}}" ng-bind="package_domain.name" ng-selected="package_domain.id==new_package_relation_domain.id"></option>
                        -->
						</select>
					</div>
				</div>
			</li>
			<li style="padding-top: 20px;">
				<button type="button" class="btn btn-primary"
					style="float: right; margin-right: 20px;"
					ng-click="new_package_relation_menu_save();">保存</button>
				<button type="button" class="btn btn-default"
					style="float: right; margin-right: 20px;"
					ng-click="new_package_relation_menu_cancel();">关闭</button>
			</li>
		</ul>
	</div>

	<!-- Package Relation 右键菜单 -->
	<div>
		<ul class="dropdown-menu" id="package_relation_menu">
			<li style="text-align: center;"><a href="#"
				ng-click="package_relation_menu_update_func($event);">修改</a></li>
			<li class="divider"></li>
			<li style="text-align: center;"><a href="#"
				ng-click="package_relation_menu_delete_func();">删除</a></li>
			<li class="divider"></li>
			<li style="text-align: center;"><a href="#"
				ng-click="package_relation_menu_close_func();">关闭</a></li>
		</ul>
	</div>

	

	<script src="${ctx}/static/js/jquery/jquery-1.11.3.js"></script>
	<script src="${ctx}/static/js/bootstrap-3.3.5/bootstrap.min.js"></script>
	<script src="${ctx}/static/js/angularjs/1.3.9/angular.min.js"></script>

	<script>
	$(document).ready(function () {
        $(".us-sideactive").on("click", function () {
            var _this = $(this);
			var _thisP = _this.parent(".upm-sidebar");
            var rightP = parseInt($(".upm-sidebar").css("right"));
            if(_thisP.data("expand") === "closed"){
                _thisP.data("expand", "open").animate({"right":"0px"}, function () {
                    _this.text("收起设置");
                });
            }else{
                _thisP.data("expand", "closed").animate({"right":"-360px"}, function () {
                    _this.text("展开设置");
                });
            }

        });
    });
	
	//package树
	function getLeftClick($self){
		if($self.hasClass("glyphicon-minus")){
			$self.removeClass("glyphicon-minus");
			$self.addClass("glyphicon-plus");
			$self.siblings("div").hide();
			
		}else if($self.hasClass("glyphicon-plus")){
			$self.removeClass("glyphicon-plus");
			$self.addClass("glyphicon-minus");
			$self.siblings("div").show();
		} 
		
	};
	
    // table page
    var pageSize = 10;
    var paginations = new Array();
    // init value zero
    var totalRowNum = 0;
    var pageTotalNum;
    var currentPage;
    var rowStart;
    var rowEnd;


	$("#navbar-collapse a").mouseover(function(){
		if(!$(this).parent().hasClass("active")){
		$(this).css("background-color","#DCDCDC");
		}
		});
	$("#navbar-collapse a").mouseout(function(){
		if(!$(this).parent().hasClass("active")){
		$(this).css("background-color","#F8F8F8");
		}
		});
    // Refresh Page Info
    function refreshPageInfo($scope) {
        $scope.pageInfo = "从 "+rowStart+" 到 "+rowEnd+" ，共"+totalRowNum+"条记录，每页";
    }
    // Get Path
    function getPathPrefix() {
        return "<%=path%>";
		}

		// Init Page
		function initPage($scope, $http) {
			pageSize = 10;
			currentPage = 1;
			$scope.pageSize = pageSize;
			getTotalRowNum($scope, $http);
		}

		// Get Total Row Num
		function getTotalRowNum($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/domain/queryDomainTotalNum",
						params : {},
						data : {},
					}).success(function(data, status, headers, config) {
				totalRowNum = data;
				pageTotalNum = Math.ceil(totalRowNum / pageSize);
				queryDomains($scope, $http);
				refreshPageInfo($scope);
			}).error(function(data, status, headers, config) {
			});
		}

		// Query All Domains
		function queryAllDomains($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/domain/queryAllDomains",
						params : {},
						data : {},
					}).success(function(data, status, headers, config) {
				$scope.domains = data;
			}).error(function(data, status, headers, config) {
			});
		}

		// Query Domains By Page
		function queryDomainsByPage($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/domain/queryDomainsByPage",
						params : {
							"index" : rowStart - 1,
							"pageSize" : pageSize
						},
						data : {},
					}).success(function(data, status, headers, config) {
				$scope.domains = data;
			}).error(function(data, status, headers, config) {
			});
		}

		// Query Design Domains
		function queryDesignDomains($scope, $http) {
			paginations.length = 0;
			if (totalRowNum == 0) {
				rowStart = 0;
				rowEnd = 0;
				$scope.domains = [];
				return;
			}
			if (pageTotalNum <= 7) {
				for (var i = 0; i < pageTotalNum; i++) {
					var pagination = new Object();
					pagination.number = i + 1;
					if (currentPage == i + 1) {
						pagination.pagination_class = "active";
					}
					paginations.push(pagination);
				}
			} else {
				for (var i = 0; i < 7; i++) {
					var pagination = new Object();
					if (i == 0) {
						pagination.number = 1;
						if (currentPage == 1) {
							pagination.pagination_class = "active";
						}
					} else if (i == 6) {
						pagination.number = pageTotalNum;
						if (currentPage == pageTotalNum) {
							pagination.pagination_class = "active";
						}
					} else {
						if (currentPage <= 4) {
							if (i == 5) {
								pagination.number = "..";
								pagination.pagination_class = "disabled";
							} else {
								pagination.number = i + 1;
							}
							if (currentPage == i + 1) {
								pagination.pagination_class = "active";
							}
						} else if (currentPage >= pageTotalNum - 3) {
							if (i == 1) {
								pagination.number = "..";
								pagination.pagination_class = "disabled";
							} else {
								pagination.number = pageTotalNum + i - 6;
							}
							if (currentPage == pageTotalNum + i - 6) {
								pagination.pagination_class = "active";
							}
						} else {
							if (i == 1 || i == 5) {
								pagination.number = "..";
								pagination.pagination_class = "disabled";
							} else if (i == 2) {
								pagination.number = currentPage - 1;
							} else if (i == 3) {
								pagination.number = currentPage;
								pagination.pagination_class = "active";
							} else if (i == 4) {
								pagination.number = currentPage + 1;
							}
						}
					}
					paginations.push(pagination);
				}
			}
			rowStart = (currentPage - 1) * pageSize + 1;
			if (currentPage == pageTotalNum) {
				rowEnd = totalRowNum;
			} else {
				rowEnd = currentPage * pageSize;
			}
			queryDomainsByPage($scope, $http);
		}

		// Query Domains
		function queryDomains($scope, $http) {
			if (pageSize == "ALL") {
				queryAllDomains($scope, $http);
				$scope.page_nav_show = false;
			} else {
				$scope.page_nav_show = true;
				queryDesignDomains($scope, $http);
			}
		}

		// New Domain Save
		function newDomainSave($scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/angularjs/domain/newDomain",
				params : {
					"name" : $scope.newDomain.name,
					"published" : $scope.newDomain.published
				},
				data : {},
			}).success(function(data, status, headers, config) {
				initPage($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Update Domain Save
		function updateDomainSave($scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/angularjs/domain/updateDomain",
				params : {
					"domain" : $scope.newDomain
				},
				data : {},
			}).success(function(data, status, headers, config) {
				// Refresh Package Tree
				refreshPackageTree($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Delete Domain
		function deleteDomain(domainId, $scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/angularjs/domain/deleteDomain",
				params : {
					"id" : domainId
				},
				data : {},
			}).success(function(data, status, headers, config) {
				initPage($scope, $http);
				// Refresh Package Tree
				refreshPackageTree($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Table CheckBox
		function tableCheckBox() {
			var len = $("#domainTable > thead :input:checkbox:checked").length;
			var checkedBoxs = $("#domainTable > tbody :input:checkbox");
			if (len == 1) {
				for (var i = 0; i < checkedBoxs.length; i++) {
					if (!checkedBoxs[i].disabled) {
						checkedBoxs[i].checked = true;
					} else {
						checkedBoxs[i].checked = false;
					}

				}
			} else if (len == 0) {
				for (var i = 0; i < checkedBoxs.length; i++) {
					checkedBoxs[i].checked = false;
				}
			}
		}

		// Cancel Table CheckBox
		function cancelTableCheckBox() {
			$("#domainTable :input:checkbox").each(function() {
				$(this).attr("checked", false);
			});
		}

		// Delete Domains
		function deleteDomains(domainIds, $scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/angularjs/domain/deleteDomains",
				params : {
					"ids" : domainIds
				},
				data : {},
			}).success(function(data, status, headers, config) {
				$scope.result = data;
				if (data.length) {
					$('#infoModal').modal('show');
				}
				initPage($scope, $http);
				cancelTableCheckBox();
			}).error(function(data, status, headers, config) {
			});
		}

		// Domains Order By Asc
		var orderByAsc = function(name) {
			return function(thisVal, thatVal) {
				if (typeof thisVal === "object" && typeof thatVal === "object"
						&& thisVal && thatVal) {
					var a = thisVal[name];
					var b = thatVal[name];
					if (a === b) {
						return 0;
					}
					if (typeof a === typeof b) {
						return a < b ? -1 : 1;
					}
					return typeof a < typeof b ? -1 : 1;
				} else {
					throw ("asc sort error");
				}
			};
		};

		// Domains Order By Desc
		var orderByDesc = function(name) {
			return function(thisVal, thatVal) {
				if (typeof thisVal === "object" && typeof thatVal === "object"
						&& thisVal && thatVal) {
					var a = thisVal[name];
					var b = thatVal[name];
					if (a === b) {
						return 0;
					}
					if (typeof a === typeof b) {
						return a < b ? 1 : -1;
					}
					return typeof a < typeof b ? 1 : -1;
				} else {
					throw ("asc sort error");
				}
			};
		};

		// New Domain Property Save
		function newDomainPropertysSave($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/domain/newDomainPropertys",
						params : {
							"domainId" : $scope.newDomain.id,
							"domainPropertys" : JSON
									.stringify($scope.domainPropertys)
						},
						data : {},
					}).success(function(data, status, headers, config) {
			}).error(function(data, status, headers, config) {
			});
		}

		function queryDomainPropertysByDomainId($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/domain/queryDomainPropertysByDomainId",
						params : {
							"domainId" : $scope.newDomain.id
						},
						data : {},
					}).success(function(data, status, headers, config) {
				if (null == data || data.length == 0) {
					$scope.domainPropertys = new Array();
				} else {
					$scope.domainPropertys = data;
				}
			}).error(function(data, status, headers, config) {
			});
		}

		// Query All Package Domains
		function queryAllDomainsForPackage($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/domain/queryAllDomains",
						params : {},
						data : {},
					})
					.success(
							function(data, status, headers, config) {
								$scope.package_domains = data;
								if ($scope.isNewOrUpdatePackageRelation == "new") {
									$scope.new_package_relation_domain = $scope.package_domains[0];
								} else {
									/* var tmp =	$.grep(
											$scope.package_domains,
											function(
													n,
													i) {
												return n.id == $scope.new_package_relation_domain.id;
											});
									$scope.new_package_relation_domain=tmp[0]; */
								}
							}).error(function(data, status, headers, config) {
					});
		}

		// New Domain Package
		function newDomainPackage($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/domain/newDomainPackage",
						params : {
							"package" : JSON.stringify($scope.new_package)
						},
						data : {},
					}).success(function(data, status, headers, config) {
				$("#new_package_menu").hide();
				refreshPackageTree($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Update Domain Package
		function updateDomainPackage($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/domain/updateDomainPackage",
						params : {
							"package" : JSON.stringify($scope.new_package)
						},
						data : {},
					}).success(function(data, status, headers, config) {
				$("#new_package_menu").hide();
				refreshPackageTree($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Delete Domain Package
		function deleteDomainPackage($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/domain/deleteDomainPackage",
						params : {
							"packageId" : $scope.new_package.id
						},
						data : {},
					}).success(function(data, status, headers, config) {
				$("#tree_menu").hide();
				$("#package_menu").hide();
				refreshPackageTree($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// New Domain Package Relation
		function newDomainPackageRelation($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/domain/newDomainPackageRelation",
						params : {
							"domainPackage" : JSON
									.stringify($scope.new_package),
							"domain" : $scope.new_package_relation_domain.id
						},
						data : {},
					}).success(function(data, status, headers, config) {
				$("#new_package_relation_menu").hide();
				refreshPackageTree($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Update Domain Package Relation
		function updateDomainPackageRelation($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/domain/updateDomainPackageRelation",
						params : {
							"domainPackageRelation" : $scope.new_tree_package_relation
						},
						data : {},
					}).success(function(data, status, headers, config) {
				$("#new_package_relation_menu").hide();
				refreshPackageTree($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Delete Domain Package Relation
		function deleteDomainPackageRelation($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/domain/deleteDomainPackageRelation",
						params : {
							"domainPackageRelationId" : $scope.new_tree_package_relation.id
						},
						data : {},
					}).success(function(data, status, headers, config) {
				$("#tree_menu").hide();
				$("#package_relation_menu").hide();
				refreshPackageTree($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Refresh Package Tree
		function refreshPackageTree($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/domain/queryRefreshDomainPackages",
						params : {},
						data : {},
					}).success(function(data, status, headers, config) {
				$scope.tree_data = data;
			}).error(function(data, status, headers, config) {
			});
		}

		// Regist the AngularJS Module
		var app = angular.module("domain_manager_app", []);

		// Fix the Controller
		app
				.controller(
						"domainController",
						function($scope, $http) {
							$("#navbar-collapse").find(".active").children("a")
									.css("background-color", "#87CEFA");
							// 刷新 Package Tree
							refreshPackageTree($scope, $http);

							// Set Pagesize
							$scope.pageSize = pageSize;

							// Set Paginations
							$scope.paginations = paginations;

							// Init Page
							initPage($scope, $http);

							// Reset Page Size
							$scope.setPageSize = function() {
								pageSize = $scope.pageSize;
								currentPage = 1;
								pageTotalNum = Math
										.ceil(totalRowNum / pageSize);
								queryDomains($scope, $http);
								refreshPageInfo($scope);
							};

							// Pre Page
							$scope.page_pre = function() {
								cancelTableCheckBox();
								if (totalRowNum == 0 || totalRowNum <= pageSize) {
									return;
								}
								currentPage = currentPage - 1 == 0 ? pageTotalNum
										: currentPage - 1;
								queryDomains($scope, $http);
								refreshPageInfo($scope);
							};

							// Next Page
							$scope.page_next = function() {
								cancelTableCheckBox();
								if (totalRowNum == 0 || totalRowNum <= pageSize) {
									return;
								}
								currentPage = currentPage + 1 > pageTotalNum ? 1
										: currentPage + 1;
								queryDomains($scope, $http);
								refreshPageInfo($scope);
							};

							// Page Chosen
							$scope.page_chosen = function(number) {
								cancelTableCheckBox();
								if (totalRowNum == 0 || totalRowNum <= pageSize
										|| number == "..") {
									return;
								}
								currentPage = number;
								queryDomains($scope, $http);
								refreshPageInfo($scope);
							};

							// New Domain
							$scope.newDomainModal = function() {
								$scope.isNewOrUpdateDomain = "new";
								$scope.domainModalTitle = "新建领域对象";
								$scope.newDomain = new Object();
								$scope.newDomain.name = "";
								$scope.newDomain.published = "false";
								$scope.isDomainModalShow = true;
								$scope.save_temp = "no";
							};

							// Update Domain
							$scope.updateDomainModal = function(domain) {
								//checkBeforeUpdate(domain, $scope, $http);
								$http(
										{
											method : "POST",
											url : getPathPrefix()
													+ "/res/angularjs/domain/domainUsed",
											params : {
												"id" : domain.id
											},
											data : {},
										})
										.success(
												function(data, status, headers,
														config) {
													if (data) {
														/* $('#infoModal').modal(
																'show');
														return; */
														$scope.isNewOrUpdateDomain = "update";
														$scope.domainModalTitle = "更新领域对象";
														$scope.newDomain = domain;
														$scope.oldDomain = new Object();
														$scope.oldDomain.name = domain.name;
														$scope.oldDomain.published = domain.published;
														$scope.isDomainModalShow = true;
														$('#domainModal')
																.modal('show');
														$scope.save_temp = 'ok';
													} else {
														$scope.isNewOrUpdateDomain = "update";
														$scope.domainModalTitle = "更新领域对象";
														$scope.newDomain = domain;
														$scope.oldDomain = new Object();
														$scope.oldDomain.name = domain.name;
														$scope.oldDomain.published = domain.published;
														$scope.isDomainModalShow = true;
														$('#domainModal')
																.modal('show');
														$scope.save_temp = 'no';
													}
												}).error(
												function(data, status, headers,
														config) {
												});
							};

							// Domain Modal Save
							$scope.domainModalSave = function() {
								if ($scope.isNewOrUpdateDomain == "new") {
									newDomainSave($scope, $http);
								} else if ($scope.isNewOrUpdateDomain == "update") {
									updateDomainSave($scope, $http);
								}
								$scope.oldDomain.published = "false";
							};

							// Domain Modal Cancel
							$scope.domainModalCancel = function() {
								if ($scope.isNewOrUpdateDomain == "update") {
									$scope.newDomain.name = $scope.oldDomain.name;
									$scope.newDomain.published = $scope.oldDomain.published;
									$scope.oldDomain.published = "false";
								}
							};

							// Delete Domain
							$scope.deleteDomain = function(domainId) {
								$("#deleteModal").modal('show');
								$scope.delFlag = "deleteDomain";
								$scope.domain_Id = domainId;
								//checkBeforeDel(domainId, $scope, $http);

								//deleteDomain(domainId, $scope, $http);
							};

							// Delete Domains
							$scope.deleteDomains = function() {
								$scope.delFlag = "deleteDomains";
								var domainIds = new Array();
								var domainItems = $(
										"#domainTable > tbody :input:checkbox:checked")
										.parent().next().children();
								for (var i = 0; i < domainItems.length; i++) {
									domainIds.push(domainItems[i].textContent);
								}
								//deleteDomains(domainIds, $scope, $http);
								if (0 == domainIds.length) {
									$("#deleteMessage").modal("show");
								} else {
									$scope.domainIds = domainIds;
									$("#deleteModal").modal("show");
								}

							};

							// Default Sort
							$scope.defaultSort = function() {
								queryDomains($scope, $http);
							};

							// Sort By Id Asc
							var sortByIdAsc = true;

							// Sort By Id
							$scope.sortById = function() {
								if (null == $scope.domains
										|| $scope.domains.length == 0) {
									return;
								}
								if (sortByIdAsc) {
									$scope.domains.sort(orderByAsc("id"));
								} else {
									$scope.domains.sort(orderByDesc("id"));
								}
								sortByIdAsc = !sortByIdAsc;
							};

							// Sort By Name Asc
							var sortByNameAsc = true;

							// Sort By Name
							$scope.sortByName = function() {
								if (null == $scope.domains
										|| $scope.domains.length == 0) {
									return;
								}
								if (sortByNameAsc) {
									$scope.domains.sort(orderByAsc("name"));
								} else {
									$scope.domains.sort(orderByDesc("name"));
								}
								sortByNameAsc = !sortByNameAsc;
							};

							// Sort By Published Asc
							var sortByPublishedAsc = true;

							// Sort By Published
							$scope.sortByPublished = function() {
								if (null == $scope.domains
										|| $scope.domains.length == 0) {
									return;
								}
								if (sortByPublishedAsc) {
									$scope.domains
											.sort(orderByAsc("published"));
								} else {
									$scope.domains
											.sort(orderByDesc("published"));
								}
								sortByPublishedAsc = !sortByPublishedAsc;
							};

							// Set Domain Property Name
							$scope.setDomainPropertyName = function() {
								$scope.domainPropertyName = $scope.newDomain.name;
								$scope.isDomainModalShow = false;
								queryDomainPropertysByDomainId($scope, $http);
								$("#domainPropertyModal").modal('show');
							};

							// Domain Property Modal Cancel
							$scope.domainPropertyModalCancel = function() {
								$scope.domainPropertys.length = 0;
								$scope.isDomainModalShow = true;
							};

							// Add Domain Property Modal
							$scope.addDomainPropertyModal = function() {
								var domainProperty = new Object();
								domainProperty.name = "";
								domainProperty.category = "int";
								domainProperty.defaultVal = "";
								$scope.domainPropertys.push(domainProperty);
							};

							// Delete Domain Property Modal
							$scope.deleteDomainPropertyModal = function(index) {
								$scope.domainPropertys.splice(index, 1);
							};

							// Domain Propertys Modal Save
							$scope.domainPropertyModalSave = function() {
								newDomainPropertysSave($scope, $http);
								$scope.domainPropertys.length = 0;
								$scope.isDomainModalShow = true;
							};

							$scope.tree_mouseup = function($event) {
								if ($event.which == 3) {
									// 禁止浏览器弹出右键菜单
									document.oncontextmenu = function() {
										return false;
									};
									var value = window.innerHeight-$event.clientY <= 83 ? ($event.clientY-83) :$event.clientY;
									$("#tree_menu").hide();
									$("#package_relation_menu").hide();
									$("#package_menu").hide();
									$("#tree_menu").attr(
											"style",
											"display: block; position: fixed; top:"
													+ value
													+ "px; left:"
													+ ($event.pageX - 160)
													+ "px;");
									$("#tree_menu").show();
									
									//$("#package_menu").hide();
									//$("#package_relation_menu").hide();
								}
							};

							$scope.tree_new_package_func = function($event) {
								$scope.new_package = new Object();
								$scope.isNewOrUpdatePackage = "new";
								var value = window.innerHeight-$event.clientY <= 200 ? ($event.clientY-200) : $event.clientY;
								// 关闭之前菜单
								$("#tree_menu").hide();
								// 新菜单出现
								$("#new_package_menu").hide();
								$("#new_package_menu")
										.attr(
												"style",
												"display: block; position: fixed; top:"
														+ value
														+ "px; left:"
														+ ($event.pageX-350)
														+ "px;width:350px;height:200px;");
								$("#new_package_menu").show();
							};

							$scope.new_package_menu_save = function() {
								if ($scope.isNewOrUpdatePackage == "new") {
									newDomainPackage($scope, $http);
								} else {
									updateDomainPackage($scope, $http);
								}
							};

							$scope.new_package_menu_cancel = function() {
								$("#new_package_menu").hide();
							};

							$scope.tree_refresh_package_func = function() {
								$("#tree_menu").hide();
								refreshPackageTree($scope, $http);
							};

							$scope.tree_package_mouseup = function($event,
									tree_package) {
								if ($event.which == 3) {
									// 禁止浏览器弹出右键菜单
									document.oncontextmenu = function() {
										return false;
									};
									$event.stopPropagation();
									var value = window.innerHeight - $event.clientY <= 177 ? ($event.clientY-177) :$event.clientY;
									$("#package_menu").hide();
									$("#package_menu").attr(
											"style",
											"display: block; position: fixed; top:"
													+ value
													+ "px; left:"
													+ ($event.pageX - 181)
													+ "px;");
									$("#package_relation_menu").hide();
									$("#tree_menu").hide();
									$("#package_menu").show();
									$scope.new_package = new Object();
									$scope.new_package.id = tree_package.domainPackageId;
									$scope.new_package.name = tree_package.domainPackageName;
								}

								if ($event.which == 1) {
									var $self = $($event.currentTarget);
									getLeftClick($self);
								}
							};

							// New Package Relation
							$scope.package_menu_new_relation_func = function(
									$event) {
								queryAllDomainsForPackage($scope, $http);
								$scope.isNewOrUpdatePackageRelation = "new";
								var value = window.innerHeight - $event.clientY <= 200 ? ($event.clientY-200) :$event.clientY;
								$("#tree_menu").hide();
								$("#package_menu").hide();
								// 新菜单出现
								$("#new_package_relation_menu").hide();
								$("#new_package_relation_menu")
										.attr(
												"style",
												"display: block; position: fixed; top:"
														+ value
														+ "px; left:"
														+ ($event.pageX-350)
														+ "px;width:350px;height:200px;");
								$("#new_package_relation_menu").show();
							};

							// Update Package
							$scope.package_menu_update_package_func = function(
									$event) {
								$("#tree_menu").hide();
								$("#package_menu").hide();
								$scope.isNewOrUpdatePackage = "update";
								var value = window.innerHeight - $event.clientY <= 200 ? ($event.clientY-200) :$event.clientY;
								// 新菜单出现
								$("#new_package_menu").hide();
								$("#new_package_menu")
										.attr(
												"style",
												"display: block; position: fixed; top:"
														+ value
														+ "px; left:"
														+ ($event.pageX-350)
														+ "px;width:350px;height:200px;");
								$("#new_package_menu").show();
							};

							// Delete Package
							$scope.package_menu_delete_package_func = function() {
								$("#deleteModal").modal('show');
								$scope.delFlag = "deleteDomainPackage";
								//deleteDomainPackage($scope, $http);
							};

							// Close Package Menu
							$scope.package_menu_close_func = function() {
								$("#tree_menu").hide();
								$("#package_menu").hide();
							};

							$scope.new_package_relation_menu_save = function() {
								if ($scope.isNewOrUpdatePackageRelation == "new") {
									newDomainPackageRelation($scope, $http);
								} else {
									$scope.new_tree_package_relation.domainId = $scope.new_package_relation_domain.id;
									//name和published的值是package里的旧值，无意义，java端根据id重新赋值。
									$scope.new_tree_package_relation.domainName = $scope.new_package_relation_domain.name;
									$scope.new_tree_package_relation.domainPublished = $scope.new_package_relation_domain.published;
									updateDomainPackageRelation($scope, $http);
								}
							};

							$scope.new_package_relation_menu_cancel = function() {
								$("#new_package_relation_menu").hide();
							};

							$scope.tree_package_relation_mouseup = function(
									$event, tree_package_relation) {
								
								if ($event.which == 3) {
									$event.stopPropagation();
									// 禁止浏览器弹出右键菜单
									document.oncontextmenu = function() {
										return false;
									};
									var value = window.innerHeight - $event.clientY <= 128 ? ($event.clientY-128) :$event.clientY;
									$("#package_relation_menu").hide();
									$("#package_relation_menu").attr(
											"style",
											"display: block; position: fixed; top:"
													+ value
													+ "px; left:"
													+ ($event.pageX - 160)
													+ "px;");
									$("#package_menu").hide();
									$("#tree_menu").hide();
									$("#package_relation_menu").show();
									$scope.new_tree_package_relation = tree_package_relation;
								}
							};

							$scope.package_relation_menu_update_func = function(
									$event) {
								queryAllDomainsForPackage($scope, $http);
								$scope.isNewOrUpdatePackageRelation = "update";
								var value = window.innerHeight - $event.clientY <= 200 ? ($event.clientY-200) :$event.clientY;
								$("#tree_menu").hide();
								$("#package_relation_menu").hide();
								// 新菜单出现
								$("#new_package_relation_menu").hide();
								$("#new_package_relation_menu")
										.attr(
												"style",
												"display: block; position: fixed; top:"
														+ value
														+ "px; left:"
														+ ($event.pageX-350)
														+ "px;width:350px;height:200px;");
								$("#new_package_relation_menu").show();
								$scope.new_package_relation_domain = new Object();
								$scope.new_package_relation_domain.id = $scope.new_tree_package_relation.domainId;
								$scope.new_package_relation_domain.name = $scope.new_tree_package_relation.domainName;
								$scope.new_package_relation_domain.published = $scope.new_tree_package_relation.domainPublished;
							};

							$scope.package_relation_menu_delete_func = function() {
								$("#deleteModal").modal('show');
								$scope.delFlag = "deleteDomainPackageRelation";
								//deleteDomainPackageRelation($scope, $http);
							};

							$scope.package_relation_menu_close_func = function() {
								$("#tree_menu").hide();
								$("#package_relation_menu").hide();
							};
							$scope.deleteCancel = function() {
								$("#deleteModal").hide();
							};
							$scope.deleteItem = function() {
								if ('deleteDomain' == $scope.delFlag) {
									deleteDomain($scope.domain_Id, $scope,
											$http);
								} else if ('deleteDomains' == $scope.delFlag) {
									deleteDomains($scope.domainIds, $scope,
											$http);
								} else if('deleteDomainPackage'==$scope.delFlag){
									deleteDomainPackage($scope, $http);
								} else if('deleteDomainPackageRelation'==$scope.delFlag){
									deleteDomainPackageRelation($scope, $http);
								}
							};
						});
		app.directive('resParse', function() {
			return {
				restrict : 'A',
				require : 'ngModel',
				link : function(scope, element, attrs, ngModel) {
					if (element.get(0).type === 'number') {
						ngModel.$parsers.push(function(value) {
							return value.toString();
						});

						ngModel.$formatters.push(function(value) {
							return parseFloat(value, 10);
						});
					}
				}
			};
		});

		app.filter("package_tree_filter", function() {
			return function(tree_data, package_tree_filter_name) {
				if ("" == package_tree_filter_name
						|| null == package_tree_filter_name) {
					return tree_data;
				}
				var array = [];
				for (var i = 0; i < tree_data.length; i++) {
					if (tree_data[i].domainPackageName
							.indexOf(package_tree_filter_name) >= 0) {
						array.push(tree_data[i]);
					}
				}
				return array;
			};

		});

		$(document).ready(function() {
		});
	</script>

</body>
</html>
