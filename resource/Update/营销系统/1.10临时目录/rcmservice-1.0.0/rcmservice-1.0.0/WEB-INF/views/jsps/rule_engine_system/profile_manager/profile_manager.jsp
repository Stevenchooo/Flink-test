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
<!--[if lt IE 9]>
          <script src="${ctx}/static/js/bootstrap-3.3.5/html5shiv.min.js"></script>
          <script src="${ctx}/static/js/bootstrap-3.3.5/respond.min.js"></script>
        <![endif]-->
<style type="text/css">
body {
	margin-top: 60px;
}

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

	<nav id="navbar" class="navbar navbar-default navbar-fixed-top">
		<div class=container-fluid>
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar-collapse"
					aria-expanded="false">
					<span class="sr-only">切换导航</span> <span class="icon-bar"></span> <span
						class="icon-bar"></span> <span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="index">首页</a>
			</div>
			<div class="collapse navbar-collapse" id="navbar-collapse">
				<ul class="nav navbar-nav" style="width: 87%;">
					<li><a href="domain_manager">领域对象管理</a></li>
					<li><a href="scenario_manager">应用场景管理</a></li>
					<li><a href="business_manager">业务规则管理</a></li>
					<li class="active"><a href="profile_manager">用户管理</a></li>
					<li><a href="system_manager">系统配置</a></li>
				</ul>
				<ul class="nav navbar-nav navbar-right" style="width: 8%;">
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown" role="button" aria-haspopup="true"
						aria-expanded="false"> 系统 <span class="caret"></span>
					</a>
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

	<!-- New/Update Profile Modal -->
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
							<span ng-model="isNewOrUpdateProfile"></span>
						</div>
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
									ng-model="newProfile.name" name="userName"
									placeholder="不为空且长度不超过255" ng-minlength="1" ng-maxlength="255"
									required> <span style="padding-right:50px;"
									class="glyphicon glyphicon-ok form-control-feedback"
									ng-show="new_profile_form.userName.$valid"></span>

							</div>
						</div>
						<div class="row" style="padding-top: 10px;">
							<div class="col-md-4"
								style="padding-right: 0px; padding-top: 7px;">
								<span style="float: right; font-size: 13px;">用户密码：</span>
							</div>
							<div class="col-md-8">
								<input type="text" class="form-control"
									ng-model="newProfile.passwd" name="passWord"
									placeholder="不为空且长度不超过255" ng-minlength="1" ng-maxlength="255"
									required> <span style="padding-right:50px;"
									class="glyphicon glyphicon-ok form-control-feedback"
									ng-show="new_profile_form.passWord.$valid"></span>
							</div>
						</div>
						<div class="row" style="padding-top: 10px;">
							<div class="col-md-4" style="padding-right: 0px;">
								<span style="float: right; font-size: 13px;">用户类型：</span>
							</div>
							<div class="col-md-8">
								<select ng-model="newProfile.category" style="width: 120px;">
									<option value="2">一般用户</option>
									<option value="1">业务用户</option>
									<option value="0">系统管理员</option>
								</select>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal"
							ng-click="profileModalCancel();">关闭</button>
						<button type="button" class="btn btn-primary" data-dismiss="modal"
							ng-click="profileModalSave();"
							ng-disabled="new_profile_form.$invalid">保存</button>
					</div>
				</form>
			</div>
		</div>
	</div>

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
			<div class="col-md-1">
				<button type="button" class="btn btn-success" data-toggle="modal"
					data-target="#profileModal" ng-click="newProfileModal();">
					<i class="glyphicon glyphicon-plus"></i>&nbsp;新建
				</button>
			</div>
			<div class="col-md-1">
				<button type="button" class="btn btn-danger"
					ng-click="deleteProfiles();">
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
						<li><a href="#" ng-click="sortByUuid();">按“UUID”排序</a></li>
						<li role="separator" class="divider"></li>
						<li><a href="#" ng-click="sortByName();">按“名称”排序</a></li>
						<li role="separator" class="divider"></li>
						<li><a href="#" ng-click="sortByCategory();">按“用户类型”排序</a></li>
					</ul>
				</div>
			</div>
		</div>
		<div class="panel panel-default"
			style="margin-top: 1%; margin-bottom: 0px;">
			<div class="table-responsive">
				<table id="profileTable"
					class="table table-hover table-striped table-bordered"
					ng-model="profiles">
					<thead class="active">
						<tr>
							<th><input type="checkbox" onclick="tableCheckBox();">
							</th>
							<th>&nbsp;&nbsp;&nbsp;&nbsp;ID&nbsp;&nbsp;&nbsp;&nbsp;</th>
							<th>&nbsp;&nbsp;&nbsp;UUID&nbsp;&nbsp;&nbsp;</th>
							<th>&nbsp;&nbsp;&nbsp;用户名&nbsp;&nbsp;&nbsp;</th>
							<th>密码</th>
							<th>用户类型</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="profile in profiles track by $index">
							<td><input type="checkbox"></td>
							<td><span ng-bind="profile.id"></span></td>
							<td><span ng-bind="profile.uuid"></span></td>
							<td><span ng-bind="profile.name"></span></td>
							<td><span ng-bind="profile.passwd"></span></td>
							<td><span ng-bind="profile.category"></span></td>
							<td>
								<button type="button" class="btn btn-link"
									ng-click="deleteProfile(profile.id);">
									<i class="glyphicon glyphicon-trash"></i>
								</button>
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
		<!--
            </div>
        </div>
        -->
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
			getTotalRowNum($scope, $http);
		}

		// Get Total Row Num
		function getTotalRowNum($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/profile/queryProfileTotalNum",
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
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/profile/queryAllProfiles",
						params : {},
						data : {},
					}).success(function(data, status, headers, config) {
				$scope.profiles = data;
			}).error(function(data, status, headers, config) {
			});
		}

		// Query Profiles By Page
		function queryProfilesByPage($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/profile/queryProfilesByPage",
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

		// New Profile Save
		function newProfileSave($scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/angularjs/profile/newProfile",
				params : {
					name : $scope.newProfile.name,
					passwd : $scope.newProfile.passwd,
					category : $scope.newProfile.category
				},
				data : {},
			}).success(function(data, status, headers, config) {
				initPage($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Update Profile Save
		function updateProfileSave($scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/angularjs/profile/updateProfile",
				params : {
					profile : $scope.newProfile
				},
				data : {},
			}).success(function(data, status, headers, config) {
			}).error(function(data, status, headers, config) {
			});
		}

		// Delete Profile
		function deleteProfile(profileId, $scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/angularjs/profile/deleteProfile",
				params : {
					id : profileId
				},
				data : {},
			}).success(function(data, status, headers, config) {
				initPage($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Table CheckBox
		function tableCheckBox() {
			var len = $("#profileTable > thead :input:checkbox:checked").length;
			var checkedBoxs = $("#profileTable > tbody :input:checkbox");
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
			$("#profileTable :input:checkbox").each(function() {
				$(this).attr("checked", false);
			});
		}

		// Delete Profiles
		function deleteProfiles(profileIds, $scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/profile/deleteProfiles",
						params : {
							ids : profileIds
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
		var app = angular.module("profile_manager_app", []);

		// Fix the Controller
		app.controller("profileController", function($scope, $http) {

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

			// New Profile
			$scope.newProfileModal = function() {
				$scope.isNewOrUpdateProfile = "new";
				$scope.profileModalTitle = "新建用户";
				$scope.newProfile = new Object();
				$scope.newProfile.name = "";
				$scope.newProfile.passwd = "";
				$scope.newProfile.category = "2";
			}

			// Update Profile
			$scope.updateProfileModal = function(profile) {
				$scope.isNewOrUpdateProfile = "update";
				$scope.profileModalTitle = "更新用户";
				$scope.newProfile = profile;
				$scope.oldProfile = new Object();
				$scope.oldProfile.name = profile.name;
				$scope.oldProfile.passwd = profile.passwd;
				$scope.oldProfile.category = profile.category;
			}

			// Profile Modal Save
			$scope.profileModalSave = function() {
				if ($scope.isNewOrUpdateProfile == "new") {
					newProfileSave($scope, $http);
				} else if ($scope.isNewOrUpdateProfile == "update") {
					updateProfileSave($scope, $http);
				}
			}

			// Profile Modal Cancel
			$scope.profileModalCancel = function() {
				if ($scope.isNewOrUpdateProfile == "update") {
					$scope.newProfile.name = $scope.oldProfile.name;
					$scope.newProfile.passwd = $scope.oldProfile.passwd;
					$scope.newProfile.category = $scope.oldProfile.category;
				}
			}

			// Delete Profile
			$scope.deleteProfile = function(profileId) {
				deleteProfile(profileId, $scope, $http);
			};

			// Delete Profiles
			$scope.deleteProfiles = function() {
				var profileIds = new Array();
				var profileItems = $(
						"#profileTable > tbody :input:checkbox:checked")
						.parent().next().children();
				for (var i = 0; i < profileItems.length; i++) {
					profileIds.push(profileItems[i].textContent);
				}
				deleteProfiles(profileIds, $scope, $http);
			};

			// Default Sort
			$scope.defaultSort = function() {
				queryProfiles($scope, $http);
			}

			// Sort By Id Asc
			var sortByIdAsc = true;

			// Sort By Id
			$scope.sortById = function() {
				if (null == $scope.profiles || $scope.profiles.length == 0) {
					return;
				}
				if (sortByIdAsc) {
					$scope.profiles.sort(orderByAsc("id"));
				} else {
					$scope.profiles.sort(orderByDesc("id"));
				}
				sortByIdAsc = !sortByIdAsc;
			}

			// Sort By Uuid Asc
			var sortByUuidAsc = true;

			// Sort By Uuid
			$scope.sortByUuid = function() {
				if (null == $scope.profiles || $scope.profiles.length == 0) {
					return;
				}
				if (sortByUuidAsc) {
					$scope.profiles.sort(orderByAsc("uuid"));
				} else {
					$scope.profiles.sort(orderByDesc("uuid"));
				}
				sortByUuidAsc = !sortByUuidAsc;
			}

			// Sort By Name Asc
			var sortByNameAsc = true;

			// Sort By Name
			$scope.sortByName = function() {
				if (null == $scope.profiles || $scope.profiles.length == 0) {
					return;
				}
				if (sortByNameAsc) {
					$scope.profiles.sort(orderByAsc("name"));
				} else {
					$scope.profiles.sort(orderByDesc("name"));
				}
				sortByNameAsc = !sortByNameAsc;
			}

			// Sort By Category Asc
			var sortByCategoryAsc = true;

			// Sort By Category
			$scope.sortByCategory = function() {
				if (null == $scope.profiles || $scope.profiles.length == 0) {
					return;
				}
				if (sortByCategoryAsc) {
					$scope.profiles.sort(orderByAsc("category"));
				} else {
					$scope.profiles.sort(orderByDesc("category"));
				}
				sortByCategoryAsc = !sortByCategoryAsc;
			}

		});
	</script>

</body>
</html>
