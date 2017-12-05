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
<html lang="zh-CN" ng-app="system_manager_app">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- <meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"> -->
<title>系统管理</title>
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

#configTable th {
	text-align: center;
	vertical-align: middle;
}

#configTable td {
	text-align: center;
	vertical-align: middle;
}
</style>
</head>

<body ng-controller="configController">

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
        <li class="un-item">
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
        <li class="un-item  uni-cur">
            <a href="system_manager" class="uni-link">系统配置</a>
        </li>
    </ul>
    <div class="upm-cont">
    	
        <div class="upmc-container">
        <!-- main body -->
	<div class="panel panel-default"
		style="padding: 10px; margin-left: 0.5%; margin-right: 0.5%; padding-bottom: 0px;">
		<!--
        <div class="row">
            <div class="col-md-2">
                <div class="input-group" style="margin-left:auto;margin-right:auto;padding-bottom:10px;width:95%;">
                    <input type="text" class="form-control" placeholder="config name" aria-describedby="basic-addon2">
                </div>
                <div id="treeview4" style="margin-left:auto;margin-right:auto;height:100%;"></div>
            </div>
            <div class="col-md-10">
        -->
        <div class="row">
        <div class="col-md-12">
		<div class="row">
			<div class="col-md-1">
				<button type="button" class="btn btn-success" data-toggle="modal"
					data-target="#configModal" ng-click="newConfigModal();">
					<i class="glyphicon glyphicon-plus"></i>&nbsp;新建
				</button>
			</div>
			<div class="col-md-1">
				<button type="button" class="btn btn-danger" data-toggle="modal"
					ng-click="deleteConfigsConfirm();" 	>
					<i class="glyphicon glyphicon-trash"></i>&nbsp;删除
				</button>
			</div>
			<div class="col-md-8"></div>
			<div class="col-md-2">
				<div class="btn-group">
					<button type="button" class="btn btn-info"
						ng-click="defaultSort();">默认排序</button>
					<button type="button" class="btn btn-info dropdown-toggle"
						data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
						<span class="caret"></span> <span class="sr-only"></span>
					</button>
					<ul class="dropdown-menu">
						<li><a href="#" ng-click="sortById();">按“ID”排序</a></li>
						<li role="separator" class="divider"></li>
						<li><a href="#" ng-click="sortByName();">按“名称”排序</a></li>
						<li role="separator" class="divider"></li>
						<li><a href="#" ng-click="sortByCategory();">按“类型”排序</a></li>
						<li role="separator" class="divider"></li>
						<li><a href="#" ng-click="sortByLevel();">按“级别”排序</a></li>
					</ul>
				</div>
			</div>
		</div>
		<div class="panel panel-default"
			style="margin-top: 1%; margin-bottom: 0px;">
			<div class="table-responsive">
				<table id="configTable"
					class="table table-hover table-striped table-bordered"
					ng-model="configs">
					<thead class="active">
						<tr>
							<th><input type="checkbox" onclick="tableCheckBox();">
							</th>
							<th>&nbsp;&nbsp;&nbsp;&nbsp;ID&nbsp;&nbsp;&nbsp;&nbsp;</th>
							<th>&nbsp;&nbsp;&nbsp;&nbsp;配置名称&nbsp;&nbsp;&nbsp;&nbsp;</th>
							<th>&nbsp;&nbsp;&nbsp;&nbsp;配置值&nbsp;&nbsp;&nbsp;&nbsp;</th>
							<th>默认值</th>
							<th>类型</th>
							<th>&nbsp;&nbsp;级别&nbsp;&nbsp;</th>
							<th>备注</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="config in configs track by $index">
							<td><input type="checkbox"></td>
							<td><span ng-bind="config.id"></span></td>
							<td><span ng-bind="config.name"></span></td>
							<td><span ng-bind="config.val"></span></td>
							<td><span ng-bind="config.defaultVal"></span></td>
							<td><span ng-bind="config.category"></span></td>
							<td><span ng-bind="config.level"></span></td>
							<td><span ng-bind="config.comment"></span></td>
							<td>
								<button type="button" class="btn btn-link"
									ng-click="deleteConfigConfirm(config.id);" data-toggle="modal"
									data-target="#deleteConfigModalLabel">
									<i class="glyphicon glyphicon-trash"></i>
								</button>
								<button type="button" class="btn btn-link"
									ng-click="updateConfigModal(config);" data-toggle="modal"
									data-target="#configModal">
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
				class="form-control" ng-change="setPageSize();" ng-model="pageSize"
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
		<!--
            </div>
        </div>
        -->
	</div>
        
        </div>

    </div>
</div>
</div>
<div class="up-ft">
    <p>EMUI官方网站 - (粤ICP备09176709号-16）- 华为软件技术有限公司 @ HUAWEI,2011. All Rights Reserved</p>
</div>
	<!-- New/Update Config Modal -->
	<div class="modal fade" id="configModal" role="dialog"
		aria-labelledby="configModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close" ng-click="configModalCancel();">
						<span aria-hidden="true">&times;</span>
					</button>
					<h5 class="modal-title" id="configModalLabel"
						ng-bind="configModalTitle"></h5>
				</div>
				<form name="form" class="form-horizontal" novalidate>
					<div class="modal-body">
						<div class="row" ng-show="false">
							<span ng-model="isNewOrUpdateConfig"></span>
						</div>
						<div class="row" ng-show="false">
							<span ng-model="oldConfig"></span>
						</div>
						<div class="row" ng-show="false">
							<span ng-model="newConfig"></span>
						</div>
						<div class="row">
							<div class="col-md-4"
								style="padding-right: 0px; padding-top: 7px;">
								<span style="float: right; font-size: 13px;">配置名称：</span>
							</div>
							<div class="col-md-8">
								<input type="text" class="form-control" name="name"
									placeholder="Config Name" ng-model="newConfig.name"
									ng-minlength="1" ng-maxlength="255" required> <span
									style="padding-right: 50px;"
									class="glyphicon glyphicon-ok form-control-feedback"
									ng-show="form.name.$valid"></span>
							</div>
						</div>
						<div class="row" style="padding-top: 10px;">
							<div class="col-md-4"
								style="padding-right: 0px; padding-top: 7px;">
								<span style="float: right; font-size: 13px;">配置值：</span>
							</div>
							<div class="col-md-8">
								<input type="text" class="form-control" name="val"
									placeholder="Config Value" ng-model="newConfig.val"
									ng-minlength="1" ng-maxlength="255" required><span
									style="padding-right: 50px;"
									class="glyphicon glyphicon-ok form-control-feedback"
									ng-show="form.val.$valid"></span>
							</div>
						</div>
						<div class="row" style="padding-top: 10px;">
							<div class="col-md-4"
								style="padding-right: 0px; padding-top: 7px;">
								<span style="float: right; font-size: 13px;">默认值：</span>
							</div>
							<div class="col-md-8">
								<input type="text" class="form-control" name="default"
									placeholder="Config Default Value"
									ng-model="newConfig.defaultVal" ng-minlength="1"
									ng-maxlength="255" required><span
									style="padding-right: 50px;"
									class="glyphicon glyphicon-ok form-control-feedback"
									ng-show="form.default.$valid"></span>
							</div>
						</div>
						<div class="row" style="padding-top: 10px;">
							<div class="col-md-4" style="padding-right: 0px;">
								<span style="float: right; font-size: 13px;">类型：</span>
							</div>
							<div class="col-md-8">
								<select ng-model="newConfig.category" style="width: 100px;">
									<option value="用户配置">用户配置</option>
									<option value="系统配置">系统配置</option>
								</select>
							</div>
						</div>
						<div class="row" style="padding-top: 10px;">
							<div class="col-md-4" style="padding-right: 0px;">
								<span style="float: right; font-size: 13px;">级别：</span>
							</div>
							<div class="col-md-8">
								<select ng-model="newConfig.level" style="width: 100px;">
									<option value="8">8</option>
									<option value="7">7</option>
									<option value="6">6</option>
									<option value="5">5</option>
									<option value="4">4</option>
									<option value="3">3</option>
									<option value="2">2</option>
									<option value="1">1</option>
								</select>
							</div>
						</div>
						<div class="row" style="padding-top: 10px;">
							<div class="col-md-4"
								style="padding-right: 0px; padding-top: 7px;">
								<span style="float: right; font-size: 13px;">备注：</span>
							</div>
							<div class="col-md-8">
								<input type="text" class="form-control" name="comment"
									placeholder="Config Comment" ng-model="newConfig.comment"
									ng-minlength="1" ng-maxlength="255" required><span
									style="padding-right: 50px;"
									class="glyphicon glyphicon-ok form-control-feedback"
									ng-show="form.comment.$valid"></span>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal"
							ng-click="configModalCancel();">关闭</button>
						<button type="button" class="btn btn-primary" data-dismiss="modal"
							ng-disabled="form.$invalid" ng-click="configModalSave();">保存</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<div class="modal fade" id="deleteConfigModalLabel" tabindex="-1"
		role="dialog" aria-labelledby="deleteConfigModalLabel">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header" style="text-align: center;">
					<h4 class="modal-title">是否确认删除?</h4>
				</div>
				<div class="modal-footer" style="text-align: center;">
					<div class="row" ng-show="false">
						<span ng-model="deleteConfigId"></span>
					</div>
					<button type="button" class="btn btn-default"
						style="margin-right: 15px;" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal"
						ng-click="deleteConfigModalConfig();">确认</button>
				</div>
			</div>
		</div>
	</div>

	<div class="modal fade" id="deleteConfigsModalLabel" tabindex="-1"
		role="dialog" aria-labelledby="deleteConfigsModalLabel">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header" style="text-align: center;">
					<h4 class="modal-title">是否确认删除?</h4>
				</div>
				<div class="modal-footer" style="text-align: center;">
					<button type="button" class="btn btn-default"
						style="margin-right: 15px;" data-dismiss="modal">取消</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal"
						ng-click="deleteConfigsModalConfig();">确认</button>
				</div>
			</div>
		</div>
	</div>
<!-- 群删提示框 -->
	<div class="modal fade" id="deleteMessage" tabindex="-1"
		role="dialog" aria-labelledby="deleteConfigsModalLabel">
		<div class="modal-dialog modal-sm">
			<div class="modal-content">
				<div class="modal-header" style="text-align: center;">
					<h4 class="modal-title">未选中删除项！</h4>
				</div>
				
			</div>
		</div>
	</div>
	
	<script src="${ctx}/static/js/jquery/jquery-1.11.3.js"></script>
	<script src="${ctx}/static/js/bootstrap-3.3.5/bootstrap.min.js"></script>
	<script src="${ctx}/static/js/angularjs/1.3.9/angular.min.js"></script>

	<script>
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
        $scope.pageInfo = "从 "+rowStart+" 到 "+rowEnd+"，共 "+totalRowNum+" 条记录，每页";
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
								+ "/res/angularjs/system/queryConfigTotalNum",
						params : {},
						data : {},
					}).success(function(data, status, headers, config) {
				totalRowNum = data;
				pageTotalNum = Math.ceil(totalRowNum / pageSize);
				queryConfigs($scope, $http);
				refreshPageInfo($scope);
			}).error(function(data, status, headers, config) {
			});
		}

		// Query All Configs
		function queryAllConfigs($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/system/queryAllConfigs",
						params : {},
						data : {},
					}).success(function(data, status, headers, config) {
				$scope.configs = data;
			}).error(function(data, status, headers, config) {
			});
		}

		// Query Configs By Page
		function queryConfigsByPage($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/system/queryConfigsByPage",
						params : {
							"index" : rowStart - 1,
							"pageSize" : pageSize
						},
						data : {},
					}).success(function(data, status, headers, config) {
				$scope.configs = data;
			}).error(function(data, status, headers, config) {
			});
		}

		// Query Design Configs
		function queryDesignConfigs($scope, $http) {
			paginations.length = 0;
			if (totalRowNum == 0) {
				rowStart = 0;
				rowEnd = 0;
				$scope.configs = [];
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
			queryConfigsByPage($scope, $http);
		}

		// Query Configs
		function queryConfigs($scope, $http) {
			if (pageSize == "ALL") {
				queryAllConfigs($scope, $http);
				$scope.page_nav_show = false;
			} else {
				$scope.page_nav_show = true;
				queryDesignConfigs($scope, $http);
			}
		}

		// New Config Save
		function newConfigSave($scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/angularjs/system/newConfig",
				params : {
					name : $scope.newConfig.name,
					val : $scope.newConfig.val,
					default_val : $scope.newConfig.defaultVal,
					category : $scope.newConfig.category,
					level : $scope.newConfig.level,
					comment : $scope.newConfig.comment
				},
				data : {},
			}).success(function(data, status, headers, config) {
				initPage($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Update Config Save
		function updateConfigSave($scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/angularjs/system/updateConfig",
				params : {
					config : $scope.newConfig
				},
				data : {},
			}).success(function(data, status, headers, config) {
			}).error(function(data, status, headers, config) {
			});
		}

		// Delete Config
		function deleteConfig(configId, $scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/angularjs/system/deleteConfig",
				params : {
					id : configId
				},
				data : {},
			}).success(function(data, status, headers, config) {
				initPage($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Table CheckBox
		function tableCheckBox() {
			var len = $("#configTable > thead :input:checkbox:checked").length;
			var checkedBoxs = $("#configTable > tbody :input:checkbox");
			if (len == 1) {
				for (var i = 0; i < checkedBoxs.length; i++) {
					checkedBoxs[i].checked = true;
				}
			} else if (len == 0) {
				for (var i = 0; i < checkedBoxs.length; i++) {
					checkedBoxs[i].checked = false;
				}
			}
		}

		// Cancel Table CheckBox
		function cancelTableCheckBox() {
			$("#configTable :input:checkbox").each(function() {
				$(this).attr("checked", false);
			});
		}

		// Delete Configs
		function deleteConfigs(configIds, $scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/angularjs/system/deleteConfigs",
				params : {
					ids : configIds
				},
				data : {},
			}).success(function(data, status, headers, config) {
				initPage($scope, $http);
				cancelTableCheckBox();
			}).error(function(data, status, headers, config) {
			});
		}

		// Order By Asc
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
			}
		}

		// Order By Desc
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
			}
		}

		// Regist the AngularJS Module
		var app = angular.module("system_manager_app", []);

		// Fix the Controller
		app
				.controller(
						"configController",
						function($scope, $http) {
							$("#navbar-collapse").find(".active").children("a")
									.css("background-color", "#87CEFA");
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
								queryConfigs($scope, $http);
								refreshPageInfo($scope);
							}

							// Pre Page
							$scope.page_pre = function() {
								cancelTableCheckBox();
								if (totalRowNum == 0 || totalRowNum <= pageSize) {
									return;
								}
								currentPage = currentPage - 1 == 0 ? pageTotalNum
										: currentPage - 1;
								queryConfigs($scope, $http);
								refreshPageInfo($scope);
							}

							// Next Page
							$scope.page_next = function() {
								cancelTableCheckBox();
								if (totalRowNum == 0 || totalRowNum <= pageSize) {
									return;
								}
								currentPage = currentPage + 1 > pageTotalNum ? 1
										: currentPage + 1;
								queryConfigs($scope, $http);
								refreshPageInfo($scope);
							}

							// Page Chosen
							$scope.page_chosen = function(number) {
								cancelTableCheckBox();
								if (totalRowNum == 0 || totalRowNum <= pageSize
										|| number == "..") {
									return;
								}
								currentPage = number;
								queryConfigs($scope, $http);
								refreshPageInfo($scope);
							}

							// New Config
							$scope.newConfigModal = function() {
								$scope.isNewOrUpdateConfig = "new";
								$scope.configModalTitle = "新建配置";
								$scope.newConfig = new Object();
								$scope.newConfig.name = "";
								$scope.newConfig.val = "";
								$scope.newConfig.defaultVal = "";
								$scope.newConfig.category = "用户配置";
								$scope.newConfig.level = 8;
								$scope.newConfig.comment = "";
							}

							// Update Config
							$scope.updateConfigModal = function(config) {
								$scope.isNewOrUpdateConfig = "update";
								$scope.configModalTitle = "更新配置";
								$scope.newConfig = config;
								$scope.oldConfig = new Object();
								$scope.oldConfig.name = config.name;
								$scope.oldConfig.val = config.val;
								$scope.oldConfig.defaultVal = config.defaultVal;
								$scope.oldConfig.category = config.category;
								$scope.oldConfig.level = config.level;
								$scope.oldConfig.comment = config.comment;
							}

							// Config Modal Save
							$scope.configModalSave = function() {
								if ($scope.isNewOrUpdateConfig == "new") {
									newConfigSave($scope, $http);
								} else if ($scope.isNewOrUpdateConfig == "update") {
									updateConfigSave($scope, $http);
								}
							}

							// Config Modal Cancel
							$scope.configModalCancel = function() {
								if ($scope.isNewOrUpdateConfig == "update") {
									$scope.newConfig.name = $scope.oldConfig.name;
									$scope.newConfig.val = $scope.oldConfig.val;
									$scope.newConfig.defaultVal = $scope.oldConfig.defaultVal;
									$scope.newConfig.category = $scope.oldConfig.category;
									$scope.newConfig.level = $scope.oldConfig.level;
									$scope.newConfig.comment = $scope.oldConfig.comment;
								}
							}

							// Delete Config
							$scope.deleteConfig = function(configId) {
								deleteConfig(configId, $scope, $http);
							};

							// Delete Configs
							$scope.deleteConfigs = function() {
								var configIds = new Array();
								var configItems = $(
										"#configTable > tbody :input:checkbox:checked")
										.parent().next().children();
								for (var i = 0; i < configItems.length; i++) {
									configIds.push(configItems[i].textContent);
								}
								if (0 == configIds.length) {
									$("#deleteMessage").modal("show");
								} else {
									$scope.configIds = configIds;
									$("#deleteConfigsModalLabel").modal("show");
								}
									
								
								//deleteConfigs(configIds, $scope, $http);
							};

							// Default Sort
							$scope.defaultSort = function() {
								queryConfigs($scope, $http);
							}

							// Sort By Id Asc
							var sortByIdAsc = true;

							// Sort By Id
							$scope.sortById = function() {
								if (null == $scope.configs
										|| $scope.configs.length == 0) {
									return;
								}
								if (sortByIdAsc) {
									$scope.configs.sort(orderByAsc("id"));
								} else {
									$scope.configs.sort(orderByDesc("id"));
								}
								sortByIdAsc = !sortByIdAsc;
							}

							// Sort By Name Asc
							var sortByNameAsc = true;

							// Sort By Name
							$scope.sortByName = function() {
								if (null == $scope.configs
										|| $scope.configs.length == 0) {
									return;
								}
								if (sortByNameAsc) {
									$scope.configs.sort(orderByAsc("name"));
								} else {
									$scope.configs.sort(orderByDesc("name"));
								}
								sortByNameAsc = !sortByNameAsc;
							}

							// Sort By Category Asc
							var sortByCategoryAsc = true;

							// Sort By Category
							$scope.sortByCategory = function() {
								if (null == $scope.configs
										|| $scope.configs.length == 0) {
									return;
								}
								if (sortByCategoryAsc) {
									$scope.configs.sort(orderByAsc("category"));
								} else {
									$scope.configs
											.sort(orderByDesc("category"));
								}
								sortByCategoryAsc = !sortByCategoryAsc;
							}

							// Sort By Level Asc
							var sortByLevelAsc = true;

							// Sort By Level
							$scope.sortByLevel = function() {
								if (null == $scope.configs
										|| $scope.configs.length == 0) {
									return;
								}
								if (sortByLevelAsc) {
									$scope.configs.sort(orderByAsc("level"));
								} else {
									$scope.configs.sort(orderByDesc("level"));
								}
								sortByLevelAsc = !sortByLevelAsc;
							}

							$scope.deleteConfigConfirm = function(config_id) {
								$scope.deleteConfigId = config_id;
							}

							$scope.deleteConfigModalConfig = function() {
								$scope.deleteConfig($scope.deleteConfigId);
							}

							$scope.deleteConfigsConfirm = function() {
								$scope.deleteConfigs();
							}

							$scope.deleteConfigsModalConfig = function() {
								deleteConfigs($scope.configIds,$scope,$http);
							}

						});
	</script>

</body>
</html>
