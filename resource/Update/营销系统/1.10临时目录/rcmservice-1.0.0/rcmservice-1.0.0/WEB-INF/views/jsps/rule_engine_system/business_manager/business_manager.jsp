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
<html lang="zh-CN" ng-app="business_manager_app">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>业务规则管理</title>
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

#table th {
	text-align: center;
	vertical-align: middle;
}

#table td {
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

<body ng-controller="businessController">

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
			<li class="un-item"><a href="index" class="uni-link">首页</a></li>
			<li class="un-item"><a href="domain_manager" class="uni-link">领域对象管理</a>
			</li>
			<li class="un-item"><a href="scenario_manager"
				class="uni-link">应用场景管理</a></li>
			<li class="un-item uni-cur"><a href="business_manager" class="uni-link">业务规则管理</a></li>
			<li class="un-item"><a href="effect_monitor" class="uni-link">效果监控</a></li>
			<li class="un-item"><a href="user_manager" class="uni-link">用户管理</a></li>
			<li class="un-item"><a href="system_manager" class="uni-link">系统配置</a></li>
		</ul>
     <div class="upm-cont">
    	
        <div class="upmc-container">
        	
        	<!-- BPM DIV -->
			<div class="col-md-12">
				<div class="panel panel-default" style="height: 973px;">
					<iframe id="activitiModelEditor" src="" height="100%" width="100%"
						frameborder="0" border=0 scrolling="no"></iframe>
				</div>
			</div>
        	
        </div>

        <div class="upm-sidebar" style="right:-360px;width:360px;height:1030px;z-index: 3;" data-expand="closed">
            <a href="javascript:void(0);" class="us-sideactive">展开设置</a>
            <div class="us-sidecont">
          
                <div  style="padding-right: 0px;">
				<div class="input-group"
					style="margin-left: auto; margin-right: auto; padding-bottom: 10px; width: 95%;">
					<input type="text" class="form-control " placeholder="package name"
						ng-model="package_tree_filter_name">
				</div>
				<div ng-show="false">
					<span ng-model="new_tree_package_relation"></span>
				</div>
				<div class="panel panel-default" style="height: 945px;"
					ng-mouseup="tree_mouseup($event);">
					<div ng-show="false" ng-model="tree_data"></div>
					<div
						ng-repeat="tree_package in tree_data | package_tree_filter:package_tree_filter_name">
						<div
							class="package-tree list-group-item  glyphicon glyphicon-plus ng-cloak"
							style="margin-bottom: -1px; border: 1px solid #DDD;"
							ng-bind="' '+tree_package.businessPackageName"
							ng-mouseup="tree_package_mouseup($event, tree_package);" ng-cloak>
						</div>
						<div style="display: none;"
							ng-repeat="tree_package_relation in tree_package.businessPackageRelations track by $index">
							<span
								style="float: left; margin-left: 20px; margin-right: 20px; height: 40px"></span>
							<i class="glyphicon glyphicon-ok-circle"
								style="color: #428BCA;float: left; padding-top: 2px; margin-top: 12px"
								ng-show="tree_package_relation.businessPublished=='true'"></i>
							<div class="package-tree"
								style="display: block; padding: 10px 15px; margin-bottom: -1px; border: 1px solid #DDD;"
								ng-mouseup="tree_package_relation_mouseup($event, tree_package_relation);">{{tree_package_relation.businessName}}</div>
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



    <!-- 删除提示框 -->
   <div class="modal fade" id="deleteModal" role="dialog" data-backdrop="static">
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
							ng-click="deleteItem();"
							>确定</button>
					</div>
			</div>		
					
		</div>
	</div>
	
	<!-- 发布结果提示框 -->
   <div class="modal fade" id="publishResult" role="dialog" data-backdrop="static">
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
					<h5 class="modal-title" style="text-align:center;" ng-bind="publishResult"></h5>
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
				<div class="row">
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
					ng-click="new_package_menu_save();"
					ng-disabled="pkgForm.$invalid">保存</button>
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
				ng-click="package_menu_new_relation_func($event);">新建业务规则</a></li>
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
		<div ng-show="false">
			<span ng-model="new_package_relation_business"></span>
		</div>
		<ul class="dropdown-menu" id="new_package_relation_menu">
			<li style="padding-top: 20px;">
				<div class="row">
					<div class="col-md-3"
						style="text-align: right; padding-right: 10px; padding-top: 7px;">
						<span style="font-size: 15px;">Package名称:</span>
					</div>
					<div class="col-md-9" style="padding-left: 0px; padding-top: 7px;">
						<span style="font-size: 15px;" ng-bind="new_package.name"></span>
					</div>
				</div>
			</li>
			<li style="padding-top: 20px;">
				<div class="row">
					<div class="col-md-3"
						style="text-align: right; padding-right: 10px; padding-top: 7px;">
						<span style="font-size: 15px;">业务规则名称:</span>
					</div>
					<div class="col-md-9" style="padding-left: 0px;">
						<input type="text" class="form-control" style="width: 220px;"
							placeholder="名称不能以数字开头"
							ng-model="new_package_relation_business.name">
					</div>
				</div>
			</li>
			<li style="padding-top: 20px;"
				ng-show="isNewOrUpdatePackageRelation=='update'">
				<div class="row">
					<div class="col-md-3"
						style="text-align: right; padding-right: 10px; padding-top: 7px;">
						<span style="font-size: 15px;">是否发布:</span>
					</div>
					<div class="col-md-9" style="padding-left: 0px;">
						<select class="form-control" style="width: 150px;"
							ng-model="new_package_relation_business.published"
							style="width:80px;">
							<option value="false">false</option>
							<option value="true">true</option>
						</select>
					</div>
				</div>
			</li>
			<li style="padding-top: 20px;">
				<button type="button" class="btn btn-primary"
					style="float: right; margin-right: 20px;"
					ng-click="new_package_relation_menu_save();" ng-disabled ="new_package_relation_business.name | business_name_filter">保存</button>
				<button type="button" class="btn btn-default"
					style="float: right; margin-right: 20px;"
					ng-click="new_package_relation_menu_cancel();">关闭</button>
			</li>
		</ul>
	</div>

	<!-- Package Relation 右键菜单 -->
	<div>
		<ul class="dropdown-menu" id="package_relation_menu">
			 <li style="text-align: center;" ng-show="new_tree_package_relation.businessPublished=='false'"><a href="#"
				ng-click="rule_edit_func($event);">规则编排</a></li>
			<li class="divider" ng-show="new_tree_package_relation.businessPublished=='false'"></li>
			<li style="text-align: center;" ng-show="new_tree_package_relation.businessPublished=='false'"><a href="#"
				ng-click="rule_exec_func($event);">规则发布</a></li>
			<li class="divider" ng-show="new_tree_package_relation.businessPublished=='false'"></li>
			<li style="text-align: center;" ng-show="new_tree_package_relation.businessPublished=='true'"><a href="#"
				ng-click="rule_cancel_func($event);" >取消发布</a></li>
			<!-- <li class="divider" ng-show="new_tree_package_relation.businessPublished=='true'"></li>	
			<li style="text-align: center;" ng-show="new_tree_package_relation.businessPublished=='false'"><a href="#"
				ng-click="package_relation_menu_update_func($event);" >修改</a></li> -->
			<!-- <li class="divider" ng-show="new_tree_package_relation.businessPublished=='false'"></li> -->
			<li style="text-align: center;" ng-show="new_tree_package_relation.businessPublished=='false'"><a href="#"
				ng-click="package_relation_menu_delete_func();" >删除</a></li>
			<li class="divider" ng-show="new_tree_package_relation.businessPublished=='false'"></li> 
			<li style="text-align: center;"><a href="#"
				ng-click="package_relation_menu_close_func();">关闭</a></li>
		</ul>
	</div>


	<script src="${ctx}/static/js/jquery/jquery-1.11.3.js"></script>
	<script src="${ctx}/static/js/bootstrap-3.3.5/bootstrap.min.js"></script>
	<script src="${ctx}/static/js/angularjs/1.3.9/angular.min.js"></script>

	<script>
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

    // Get Path
    function getPathPrefix() {
        return "<%=path%>";
		}

		// New Business Package
		function newBusinessPackage($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/business/newBusinessPackage",
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

		// Update Business Package
		function updateBusinessPackage($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/business/updateBusinessPackage",
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

		// Delete Business Package
		function deleteBusinessPackage($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/business/deleteBusinessPackage",
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

		// New Business Package Relation
		function newBusinessPackageRelation($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/business/newBusinessPackageRelation",
						params : {
							"businessPackage" : JSON
									.stringify($scope.new_package),
							"business" : $scope.new_package_relation_business
						},
						data : {},
					}).success(function(data, status, headers, config) {
				$("#new_package_relation_menu").hide();
				$scope.new_package_relation_business.name = "";
				$scope.new_package_relation_business.published = "";
				refreshPackageTree($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Update Business Package Relation
		function updateBusinessPackageRelation($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/business/updateBusinessPackageRelation",
						params : {
							"businessPackageRelation" : $scope.new_tree_package_relation
						},
						data : {},
					}).success(function(data, status, headers, config) {
				$("#new_package_relation_menu").hide();
				refreshPackageTree($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Delete Business Package Relation
		function deleteBusinessPackageRelation($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/business/deleteBusinessPackageRelation",
						params : {
							"businessPackageRelationId" : $scope.new_tree_package_relation.id
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
								+ "/res/angularjs/business/queryRefreshBusinessPackages",
						params : {},
						data : {},
					}).success(function(data, status, headers, config) {
				$scope.tree_data = data;
			}).error(function(data, status, headers, config) {
			});
		}

		function callActivitiModel($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix() + "/activiti/callActivitiModel",
						params : {
							"business_id" : $scope.new_tree_package_relation.businessId
						},
						data : {},
					}).success(function(data, status, headers, config) {
				refreshModelEditor(data);
			}).error(function(data, status, headers, config) {
			});
		}

		function executeActivitiModel($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/activiti/executeActivitiModel",
						params : {
							"business_id" : $scope.new_tree_package_relation.businessId
						},
						data : {},
					}).success(function(data, status, headers, config) {
				$("#activitiModelEditor").attr("src", "");
				refreshPackageTree($scope, $http);		
				$scope.publishResult = "发布成功！";
				$("#publishResult").modal(
				"show");
			}).error(function(data, status, headers, config) {
				$scope.publishResult = "发布失败！";
				$("#publishResult").modal(
				"show");
			});
		}
		
		function cancelPublish($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/business/cancelPublish",
						params : {
							"businessId" : $scope.new_tree_package_relation.businessId
						},
						data : {},
					}).success(function(data, status, headers, config) {
						refreshPackageTree($scope, $http);
						$scope.publishResult = "取消发布成功！";
						$("#publishResult").modal(
						"show");
			}).error(function(data, status, headers, config) {
				$scope.publishResult = "取消发布失败！";
				$("#publishResult").modal(
				"show");
			});
		}

		function refreshModelEditor(modelId) {
			$("#activitiModelEditor").attr(
					"src",
					getPathPrefix() + "/activiti_modeler_editor?modelId="
							+ modelId);
		}

		// Regist the AngularJS Module
		var app = angular.module("business_manager_app", []);

		// Fix the Controller
		app
				.controller(
						"businessController",
						function($scope, $http) {
							$("#navbar-collapse").find(".active").children("a")
									.css("background-color", "#87CEFA");
							// 刷新 Package Tree
							refreshPackageTree($scope, $http);

							// 菜单(新建Package/刷新)
							$scope.tree_mouseup = function($event) {
								if ($event.which == 3) {
									// 禁止浏览器弹出右键菜单
									document.oncontextmenu = function() {
										return false;
									}
									var value = window.innerHeight - $event.clientY <= 83 ? ($event.clientY-83) :$event.clientY;
									$("#tree_menu").hide();
									$("#package_menu").hide();
									$("#package_relation_menu").hide();
									$("#tree_menu").attr(
											"style",
											"display: block; position: fixed; top:"
													+ value
													+ "px; left:"
													+ ($event.pageX -160)
													+ "px;");
									$("#tree_menu").show();
								}
							}

							// 刷新
							$scope.tree_refresh_package_func = function() {
								$("#tree_menu").hide();
								refreshPackageTree($scope, $http);
							}

							// 菜单(新建业务规则/修改/删除/关闭)
							$scope.tree_package_mouseup = function($event,
									tree_package) {
								if ($event.which == 3) {
									// 禁止浏览器弹出右键菜单
									document.oncontextmenu = function() {
										return false;
									}
									$event.stopPropagation();
									var value = window.innerHeight - $event.clientY <= 173 ? ($event.clientY-173) :$event.clientY;
									$("#tree_menu").hide();
									$("#package_menu").hide();
									$("#package_relation_menu").hide();
									$("#package_menu").attr(
											"style",
											"display: block; position: fixed; top:"
													+ value
													+ "px; left:"
													+ ($event.pageX - 181)
													+ "px;");
									$("#package_menu").show();
									$scope.new_package = new Object();
									$scope.new_package.id = tree_package.businessPackageId;
									$scope.new_package.name = tree_package.businessPackageName;
								}

								if ($event.which == 1) {
									var $self = $($event.currentTarget);
									getLeftClick($self);
								}
							}

							// 新建Package
							$scope.tree_new_package_func = function($event) {
								$scope.new_package = new Object();
								$scope.isNewOrUpdatePackage = "new";
								var value = window.innerHeight - $event.clientY <= 200 ? ($event.clientY-200) :$event.clientY;
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
														+ ($event.pageX - 350)
														+ "px;width:350px;height:200px;");
								$("#new_package_menu").show();
							}

							// package save
							$scope.new_package_menu_save = function() {
								if ($scope.isNewOrUpdatePackage == "new") {
									newBusinessPackage($scope, $http);
								} else {
									updateBusinessPackage($scope, $http);
								}
							}

							// package save cancel
							$scope.new_package_menu_cancel = function() {
								$("#new_package_menu").hide();
							}

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
														+ ($event.pageX - 350)
														+ "px;width:350px;height:200px;");
								$("#new_package_menu").show();
							}
							
							$scope.deleteItem = function() {
								var deleteFlag = $scope.deleteFlag;
								 if("deleteBusinessPackage" == deleteFlag){
									deleteBusinessPackage($scope, $http);
								}else if("deleteBusinessPackageRelation" == deleteFlag){
									deleteBusinessPackageRelation($scope, $http);
								}
							} 
							
							$scope.deleteCancel = function() {
								$("#deleteModal").hide();
							}

							// Delete Package
							$scope.package_menu_delete_package_func = function() {
								//deleteBusinessPackage($scope, $http);
								$scope.deleteFlag = "deleteBusinessPackage";
								$("#deleteModal").modal(
								"show");
							}

							// Close Package Menu
							$scope.package_menu_close_func = function() {
								$("#tree_menu").hide();
								$("#package_menu").hide();
							}

							// New Business Relation
							$scope.package_menu_new_relation_func = function(
									$event) {
								$scope.isNewOrUpdatePackageRelation = "new";
								var value = window.innerHeight - $event.clientY <= 250 ? ($event.clientY-250) :$event.clientY;
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
														+ ($event.pageX - 500)
														+ "px;width:500px;height:250px;");
								$("#new_package_relation_menu").show();
							}

							// New Business Relation Save
							$scope.new_package_relation_menu_save = function() {
								if ($scope.isNewOrUpdatePackageRelation == "new") {
									newBusinessPackageRelation($scope, $http);
								} else {
									$scope.new_tree_package_relation.businessId = $scope.new_package_relation_business.id;
									$scope.new_tree_package_relation.businessName = $scope.new_package_relation_business.name;
									$scope.new_tree_package_relation.businessPublished = $scope.new_package_relation_business.published;
									updateBusinessPackageRelation($scope, $http);
								}
							}

							// New Business Relation Save cancel
							$scope.new_package_relation_menu_cancel = function() {
								$("#new_package_relation_menu").hide();
								$scope.new_package_relation_business.name = "";
								$scope.new_package_relation_business.published = "";
							}

							// 菜单(修改/删除/关闭)
							$scope.tree_package_relation_mouseup = function(
									$event, tree_package_relation) {
								if ($event.which == 3) {
									// 禁止浏览器弹出右键菜单
									document.oncontextmenu = function() {
										return false;
									}
									$event.stopPropagation();
									var value = window.innerHeight - $event.clientY <= 173 ? ($event.clientY-173) :$event.clientY;
									$("#tree_menu").hide();
									$("#package_menu").hide();
									$("#package_relation_menu").hide();
									$("#package_relation_menu").attr(
											"style",
											"display: block; position: fixed; top:"
													+ value
													+ "px; left:"
													+ ($event.pageX -160) 
													+ "px;");
									$("#package_relation_menu").show();
									$scope.new_tree_package_relation = tree_package_relation;
								}
							}

							// Update Package Business Relation
							$scope.package_relation_menu_update_func = function(
									$event) {
								$scope.isNewOrUpdatePackageRelation = "update";
								var value = window.innerHeight - $event.clientY <= 250 ? ($event.clientY-250) :$event.clientY;
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
														+ ($event.pageX - 500)
														+ "px;width:500px;height:250px;");
								$("#new_package_relation_menu").show();
								$scope.new_package = new Object();
								$scope.new_package.id = $scope.new_tree_package_relation.packageId;
								$scope.new_package.name = $scope.new_tree_package_relation.packageName;
								$scope.new_package_relation_business = new Object();
								$scope.new_package_relation_business.id = $scope.new_tree_package_relation.businessId;
								$scope.new_package_relation_business.name = $scope.new_tree_package_relation.businessName;
								$scope.new_package_relation_business.published = $scope.new_tree_package_relation.businessPublished;
							}

							// Delete Package Business Relation
							$scope.package_relation_menu_delete_func = function() {
								//deleteBusinessPackageRelation($scope, $http);
								$scope.deleteFlag = "deleteBusinessPackageRelation";
								$("#deleteModal").modal(
								"show");
							}

							// Close Package Business Relation
							$scope.package_relation_menu_close_func = function() {
								$("#tree_menu").hide();
								$("#package_relation_menu").hide();
							}

							// Call Activiti Model
							$scope.rule_edit_func = function($event) {
								$("#tree_menu").hide();
								$("#package_relation_menu").hide();
								callActivitiModel($scope, $http);
							}

							// Execute Activiti Model
							$scope.rule_exec_func = function($event) {
								$("#tree_menu").hide();
								$("#package_relation_menu").hide();
								executeActivitiModel($scope, $http);
							}
							
							$scope.rule_cancel_func = function($event) {
								$("#tree_menu").hide();
								$("#package_relation_menu").hide();
								cancelPublish($scope, $http);
							}

						});
		app.filter("package_tree_filter", function() {
			return function(tree_data, package_tree_filter_name) {
				if ("" == package_tree_filter_name
						|| null == package_tree_filter_name) {
					return tree_data;
				}
				var array = [];
				for (var i = 0; i < tree_data.length; i++) {
					if (tree_data[i].businessPackageName
							.indexOf(package_tree_filter_name) >= 0) {
						array.push(tree_data[i]);
					}
				}
				return array;
			}

		}).filter("business_name_filter",function(){
			return function (business_name){
				//如果业务规则名称为空或者不是数字，或者其中含有空格，不给其做保存操作
				if("" == business_name || null == business_name || !isNaN(business_name.substring(0,1)) || business_name.indexOf(" ") != -1){
					return true;
				}
				return false;
			}
		});

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

		        })
		    })
	</script>

</body>
</html>
