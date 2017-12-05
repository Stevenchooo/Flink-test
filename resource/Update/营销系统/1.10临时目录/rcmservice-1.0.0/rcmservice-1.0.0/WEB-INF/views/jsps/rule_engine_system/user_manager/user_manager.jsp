<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
			String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
					+ path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="<%=request.getContextPath()%>" />
<!DOCTYPE html>
<html lang="zh-CN" ng-app="profile_manager_app">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>用户管理</title>
<link href="${ctx}/static/css/bootstrap-3.3.5/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet" type="text/css" href="${ctx}/static/css/bi.css">
<style type="text/css">

#navbar li {
	padding-left: 8%;
}

#profileTable th {
	text-align: center;
	vertical-align: middle;
}

#profileTable td {
	text-align: center;
	vertical-align: middle;
}
</style>
</head>

<body ng-controller="profileController">

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
			<li class="un-item"><a href="business_manager" class="uni-link">业务规则管理</a></li>
			<li class="un-item"><a href="effect_monitor" class="uni-link">效果监控</a></li>
			<li class="un-item uni-cur"><a href="user_manager" class="uni-link">用户管理</a></li>
			<li class="un-item"><a href="system_manager" class="uni-link">系统配置</a></li>
		</ul>


    <div class="upm-cont">
    	
        <div class="upmc-container">
        	
        	<div class="panel panel-default"
			style="margin-top: 1%; margin-bottom: 0px;">
			<div class="table-responsive">
				<table id="profileTable"
					class="table table-hover table-striped table-bordered"
					ng-model="profiles">
					<thead class="active">
						<tr>
							<th>&nbsp;&nbsp;&nbsp;&nbsp;ID&nbsp;&nbsp;&nbsp;&nbsp;</th>
							<th>&nbsp;&nbsp;&nbsp;用户名&nbsp;&nbsp;&nbsp;</th>
							<th>用户类型</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="profile in profiles track by $index">
							<td><span ng-bind="profile.id"></span></td>
							<td><span ng-bind="profile.name"></span></td>
							<td><div>{{getName(profile.role_id)}}</div></td>
							<td>
								<button type="button" class="btn btn-link"
									ng-click="updateProfileModal(profile);" data-toggle="modal"
									data-target="#profileModal">
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
				<option value="ALL">ALL</option>
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
<div class="up-ft">
    <p>EMUI官方网站 - (粤ICP备09176709号-16）- 华为软件技术有限公司 @ HUAWEI,2011. All Rights Reserved</p>
</div>

	<!-- Update Profile Modal -->
	<div class="modal fade" id="profileModal" role="dialog"
		aria-labelledby="profileModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close" ng-click="profileModalCancel();">
						<span aria-hidden="true">&times;</span>
					</button>
					<h5 class="modal-title" id="profileModalLabel"
						ng-bind="profileModalTitle"></h5>
				</div>
				<form name="new_profile_form" class="form-horizontal" novalidate>
					<div class="modal-body">
						<div class="row" ng-show="false">
							<span ng-model="oldProfile"></span>
						</div>
						<div class="row" ng-show="false">
							<span ng-model="newProfile"></span>
						</div>
						<div class="row" style="padding-top: 10px;">
							<div class="col-md-4"
								style="padding-right: 0px; padding-top: 7px;">
								<span style="float: right; font-size: 13px;">用户名称：</span>
							</div>
							<div class="col-md-8">
								<input type="text" class="form-control"
									ng-model="newProfile.name" ng-disabled="true">

							</div>
						</div>
						<div class="row" style="padding-top: 10px;">
							<div class="col-md-4" style="padding-right: 0px;">
								<span style="float: right; font-size: 13px;">用户类型：</span>
							</div>
							<div class="col-md-8">
								<select ng-model="newProfile.role_id"
									ng-options="existRole.id as existRole.name for existRole in roles"
									ng-selected="newProfile.role_id==existRole.id"
									style="width: 120px;">
									<option value="">--请选择--</option>
								</select>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal"
							ng-click="profileModalCancel();">关闭</button>
						<button type="button" class="btn btn-primary" data-dismiss="modal"
							ng-click="profileModalSave();"
							ng-disabled="0 == newProfile.role_id || null == newProfile.role_id">保存</button>
					</div>
				</form>
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
        $scope.pageInfo = "从  " + rowStart + " 到  " + rowEnd + "," + " 共" + totalRowNum + "条记录，每页";
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
			queryAllRoles($scope, $http);
			getTotalRowNum($scope, $http);
		}

		function queryAllRoles($scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/angularjs/user/queryAllRoles",
				params : {},
				data : {},
			}).success(function(data, status, headers, config) {
				$scope.roles = data;
				for (var i = 0; i < data.length; i++) {
					roleMap[data[i].id] = data.name;
				}
			}).error(function() {

			});
		}

		// Get Total Row Num
		function getTotalRowNum($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/user/queryUserTotalNum",
						params : {},
						data : {},
					}).success(function(data, status, headers, config) {
				totalRowNum = data;
				pageTotalNum = Math.ceil(totalRowNum / pageSize);
				queryProfiles($scope, $http);
				refreshPageInfo($scope);
			}).error(function(data, status, headers, config) {
			});
		}

		// Query All Profiles
		function queryAllProfiles($scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/angularjs/user/queryAllUsers",
				params : {},
				data : {},
			}).success(function(data, status, headers, config) {
				$scope.profiles = data;
			}).error(function(data, status, headers, config) {
			});
		}

		// Query Profiles By Page
		function queryProfilesByPage($scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/angularjs/user/queryUsersByPage",
				params : {
					"index" : rowStart - 1,
					"pageSize" : pageSize
				},
				data : {},
			}).success(function(data, status, headers, config) {
				$scope.profiles = data;
			}).error(function(data, status, headers, config) {
			});
		}

		// Query Design Profiles
		function queryDesignProfiles($scope, $http) {
			paginations.length = 0;
			if (totalRowNum == 0) {
				rowStart = 0;
				rowEnd = 0;
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
			queryProfilesByPage($scope, $http);
		}

		// Query Profiles
		function queryProfiles($scope, $http) {
			if (pageSize == "ALL") {
				queryAllProfiles($scope, $http);
				$scope.page_nav_show = false;
			} else {
				$scope.page_nav_show = true;
				queryDesignProfiles($scope, $http);
			}
		}

		// Update Profile Save
		function updateProfileSave($scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/angularjs/user/updateUser",
				params : {
					user : $scope.newProfile
				},
				data : {},
			}).success(function(data, status, headers, config) {
			}).error(function(data, status, headers, config) {
			});
		}

		// Regist the AngularJS Module
		var app = angular.module("profile_manager_app", []);

		// Fix the Controller
		app.controller("profileController", function($scope, $http) {
			$("#navbar-collapse").find(".active").children("a").css(
					"background-color", "#87CEFA");
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
				pageTotalNum = Math.ceil(totalRowNum / pageSize);
				queryProfiles($scope, $http);
				refreshPageInfo($scope);
			}

			// Pre Page
			$scope.page_pre = function() {
				if (totalRowNum == 0 || totalRowNum <= pageSize) {
					return;
				}
				currentPage = currentPage - 1 == 0 ? pageTotalNum
						: currentPage - 1;
				queryProfiles($scope, $http);
				refreshPageInfo($scope);
			}

			// Next Page
			$scope.page_next = function() {
				if (totalRowNum == 0 || totalRowNum <= pageSize) {
					return;
				}
				currentPage = currentPage + 1 > pageTotalNum ? 1
						: currentPage + 1;
				queryProfiles($scope, $http);
				refreshPageInfo($scope);
			}

			// Page Chosen
			$scope.page_chosen = function(number) {
				if (totalRowNum == 0 || totalRowNum <= pageSize
						|| number == "..") {
					return;
				}
				currentPage = number;
				queryProfiles($scope, $http);
				refreshPageInfo($scope);
			}

			// Update Profile
			$scope.updateProfileModal = function(profile) {
				$scope.profileModalTitle = "更新用户";
				$scope.newProfile = profile;
				$scope.oldProfile = new Object();
				$scope.oldProfile.name = profile.name;
				$scope.oldProfile.role_id = profile.role_id;

			}

			// Profile Modal Save
			$scope.profileModalSave = function() {
				updateProfileSave($scope, $http);
			}

			// Profile Modal Cancel
			$scope.profileModalCancel = function() {
				$scope.newProfile.name = $scope.oldProfile.name;
				$scope.newProfile.role_id = $scope.oldProfile.role_id;
			}
			
			$scope.getName = function(id) {
				for(var i = 0;i < $scope.roles.length;i++){
					if($scope.roles[i].id == id){
						return $scope.roles[i].name;
					}
				}
				return;
			}

		});
	</script>

</body>
</html>
