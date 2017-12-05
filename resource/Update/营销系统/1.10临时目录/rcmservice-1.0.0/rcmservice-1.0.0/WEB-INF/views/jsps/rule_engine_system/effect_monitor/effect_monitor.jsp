<%@ page language="java" import="java.util.*,javax.servlet.*"
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
<html lang="zh-CN" ng-app="effect_monitor_app">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<!-- <meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"> -->
<title>效果监控</title>
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

.package-tree {
	color: #428BCA;
	font-size: 14px;
	line-height: 1.42857;
	font-weight: 100;
}
.na{
list-style:none;
}
#myTab li{width:auto;float:left;margin:4px 2px; padding:0px; display:inline;}
</style>
</head>
<body ng-controller="monitorController" ng-cloak>
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
        <li class="un-item  uni-cur">
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
			<div ng-show="false" ng-model="effects"></div>
			<div ng-show="false" ng-model="category"></div>
			
			<div class="col-md-12">
				<div class="row"></div>
				<div class="panel panel-default"
					style="margin-top: 1%; margin-bottom: 5px; height: 680px;">
					
					<ul id="myTab" class="na" >
						<li name="shouye" ><a href="" ng-click=showhome()>首页</a></li>
						<li ng-repeat="effect in effects track by $index"
							id="tabs{{effect.id}}" name="tabs{{effect.id}}" >
						<div>
							<a 
							 ng-click="showTab(effect.id);" 
							ng-show="isShow[effect.id]" ng-bind="' '+effect.name+' '"></a><button type="button" class="close" aria-hidden="true"
								ng-show="isShow[effect.id]" ng-click="hideTab(effect.id);"
								style="padding-left: 0px;">&times;</button></div>
							</li>
					</ul>
					<div id="myTabContent" class="tab-content">
						<!-- <div class="tab-pane fade" id="home"> -->
							<iframe frameborder=0 id="address" 
								src=""
								style="height: 600px; width: 100%;"></iframe>
					</div>
				</div>
			</div>
		</div>
	</div>
        </div>

        <div class="upm-sidebar" style="right:-360px;width:360px;height:800px;z-index: 3;" data-expand="closed">
            <a href="javascript:void(0);" class="us-sideactive">展开设置</a>
            <div class="us-sidecont">
           <div style="padding-right: 0px;">
				<div class="panel panel-default"
					style="height: 760px; overflow-y: auto;">

					<div class="panel panel-default">
						<div class="panel-heading" ng-mouseup="tree_mouseup($event,0);">
							<h4 class="panel-title">
								<a data-toggle="collapse" href="#collapseOne"> 实时效果 </a>
							</h4>
						</div>
						<div id="collapseOne">
							<div ng-repeat="package in packages0 track by $index">
								<div
									class="package-tree list-group-item  glyphicon glyphicon-plus ng-cloak"
									ng-mouseup="effectMonitorNewMouseUp($event,package)" ng-bind="' '+package.packageName">
									</div>

								<div class="package-tree" style="display: none;"
									ng-repeat="effect_relation in package.effectPackageRelations track by $index"
									ng-mouseup="effectMonitorUpdateMouseUp($event,package.packageId,effect_relation)">
									<a href="#" class="package-tree"
										style="display: block; padding: 10px 65px; margin-bottom: -1px; border: 1px solid #DDD;"
										ng-click="showTab(effect_relation.urlId);" ng-bind="' '+effect_relation.urlName"></a>
								</div>
							</div>
						</div>
					</div>
					<div class="panel panel-default">
						<div class="panel-heading" ng-mouseup="tree_mouseup($event,1);">
							<h4 class="panel-title">
								<a data-toggle="collapse" href="#collapseTwo"> 离线效果 </a>
							</h4>
						</div>
						<div id="collapseTwo">
							<div ng-repeat="package in packages1 track by $index">
								<div
									class="package-tree list-group-item  glyphicon glyphicon-plus "
									ng-mouseup="effectMonitorNewMouseUp($event,package)" ng-bind="' '+package.packageName">
									</div>

								<div class="package-tree" style="display: none;"
									ng-repeat="effect_relation in package.effectPackageRelations track by $index"
									ng-mouseup="effectMonitorUpdateMouseUp($event,package.packageId,effect_relation)">
									<a href="#" class="package-tree"
										style="display: block; padding: 10px 65px; margin-bottom: -1px; border: 1px solid #DDD;"
										ng-click="showTab(effect_relation.urlId);" ng-bind="' '+effect_relation.urlName"></a>
								</div>
							</div>
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
	<div>
		<ul class="dropdown-menu" id="package_menu">
			<li style="text-align: center;"><a href="#"
				ng-click="package_menu_new_effect_func($event);">新建业务菜单</a></li>
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
	<div>
		<div ng-show="false">
			<span ng-model="new_package"></span>
		</div>
		<div ng-show="false">
			<span ng-model="isNewOrUpdatePackage"></span>
		</div>
		<ul class="dropdown-menu" id="new_package_menu">
			<form name="pkg1Form" class="form-horizontal" novalidate>
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
								ng-show="pkg1Form.name.$valid"></span>
						</div>
					</div>
				</li>
			</form>
			<li style="padding-top: 20px;">
				<button type="button" class="btn btn-primary"
					style="float: right; margin-right: 20px;"
					ng-disabled="pkg1Form.$invalid" ng-click="new_package_menu_save();">保存</button>
				<button type="button" class="btn btn-default"
					style="float: right; margin-right: 20px;"
					ng-click="new_package_menu_cancel();">关闭</button>
			</li>
		</ul>
	</div>
	<div>
		<ul class="dropdown-menu" id="package_new_panel">
			<div ng-show="false">
				<span ng-model="isNewOrUpdateEffect"></span>
			</div>
			<form name="pkgForm" class="form-horizontal" novalidate>

				<li style="padding-top: 20px;">
					<div class="row">
						<div class="col-md-4"
							style="text-align: right; padding-right: 10px; padding-top: 7px;">
							<span style="font-size: 15px;">业务名称:</span>
						</div>
						<div class="col-md-8" style="padding-left: 0px; padding-top: 7px;">
							<input type="text" class="form-control" style="width: 200px;"
								name="name" placeholder="name" ng-model="new_effect.name"
								ng-minlength="1" ng-maxlength="50" required> <span
								style="padding-right: 70px;"
								class="glyphicon glyphicon-ok form-control-feedback"
								ng-show="pkgForm.name.$valid"></span>
						</div>
					</div>
				</li>
				<li style="padding-top: 20px;">
					<div class="row">
						<div class="col-md-4"
							style="text-align: right; padding-right: 10px; padding-top: 7px;">
							<span style="font-size: 15px;">业务url:</span>
						</div>
						<div class="col-md-8" style="padding-left: 0px;">
							<input type="text" class="form-control" style="width: 200px;"
								name="url" placeholder="url" ng-model="new_effect.url"
								ng-minlength="1" ng-maxlength="255" required> <span
								style="padding-right: 70px;"
								class="glyphicon glyphicon-ok form-control-feedback"
								ng-show="pkgForm.url.$valid&&!(new_effect.url.indexOf('http'))"></span>
						</div>
					</div>
				</li>
			</form>
			<li style="padding-top: 20px;">
				<button type="button" class="btn btn-primary"
					style="float: right; margin-right: 20px;"
					ng-click="package_menu_new_effect_save();"
					ng-disabled="pkgForm.$invalid">保存</button>
				<button type="button" class="btn btn-default"
					style="float: right; margin-right: 20px;"
					ng-click="package_menu_new_effect_cancel();">关闭</button>
			</li>
		</ul>
	</div>
	<!-- Package Relation 右键菜单 -->
	<div>
		<ul class="dropdown-menu" id="package_info_menu">
			<li style="text-align: center;"><a href="#"
				ng-click="package_menu_update_effect_func($event);">修改</a></li>
			<li class="divider"></li>
			<li style="text-align: center;"><a href="#"
				ng-click="package_menu_delete_effect_func();">删除</a></li>
			<li class="divider"></li>
			<li style="text-align: center;"><a href="#"
				ng-click="package_info_menu_close_func();">关闭</a></li>
		</ul>
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

	        })
	    })
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
	/* 
		$('#myTab a').click(function(e) {
			e.preventDefault();
			$(this).tab('show');
		}); */
		
 		function getPathPrefix() {
        return "<%=path%>";
		}

		var initTree = function($scope, $http) {

			$http({
				method : "POST",
				url : getPathPrefix() + "/res/monitor/queryPackageByType",
				params : {},
				data : {},
			}).success(
					function(data, status, headers, config) {
						$scope.packages0 = {};
						$scope.packages1 = {};
						for ( var i in data) {
							if ('0' == data[i].category) {
								$scope.packages0[i] = data[i];
							} else {
								$scope.packages1[i] = data[i];
							}
						}
						//$scope.packages = data;
						$scope.isShow = {};
						$("#collapseOne").addClass("in");
						$("#collapseOne").find(".glyphicon-minus").siblings(
								"div").hide();
						$("#collapseOne").find(".glyphicon-minus").removeClass(
								"glyphicon-minus").addClass("glyphicon-plus");
						$("#collapseTwo").addClass("in");
						$("#collapseTwo").find(".glyphicon-minus").siblings(
								"div").hide();
						$("#collapseTwo").find(".glyphicon-minus").removeClass(
								"glyphicon-minus").addClass("glyphicon-plus");
						$("#myTab li:eq(0) a").tab('show');

					}).error(function(data, status, headers, config) {
			});

		};
		var initPage = function($scope, $sce, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/monitor/queryAllEffect",
				params : {},
				data : {},
			})
					.success(
							function(data, status, headers, config) {
								$scope.scurl = {};
								$scope.effects = new Array();
								$scope.isOk = {};
								//$scope.isDone=false;
								$("#myTab li[name='shouye']").find("a").css("text-decoration","underline");
								//$("#myTab li[name='shouye']").addClass("active");
								
								if (null == data || data.length == 0) {
								} else {
                                      var isOkTemp = {};
                                      var effectsTemp = {};
                                      var scurlTemp = {};
									for (var j = 0; j < data.length; j++) {
										isOkTemp[data[j]["url"].id] = data[j]["avilable"];
										effectsTemp[j] = data[j]["url"];
										scurlTemp[effectsTemp[j].id] = $sce
												.trustAsResourceUrl(effectsTemp[j].url);
									}
									$scope.isOk = isOkTemp;
									$scope.scurl = scurlTemp;
									$scope.effects = effectsTemp;
									$("#address").attr("src", "");
									//$scope.isDone=true;
								}
							}).error(function(data, status, headers, config) {
					});
		};

		// New Package
		function newPackage($scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/monitor/newPackage",
				params : {
					"package" : JSON.stringify($scope.new_package),
					"category" : $scope.category
				},
				data : {},
			}).success(function(data, status, headers, config) {
				$("#new_package_menu").hide();
				initTree($scope, $http);
				initPage($scope, $sce, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Update Package
		function updatePackage($scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/monitor/updatePackage",
				params : {
					"package" : JSON.stringify($scope.new_package.name),
					"pkg_id" : $scope.new_id
				},
				data : {},
			}).success(function(data, status, headers, config) {
				$("#new_package_menu").hide();
				initTree($scope, $http);
				initPage($scope, $sce, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Delete Package
		function deletePackage($scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/monitor/deletePackage",
				params : {
					"packageId" : $scope.new_id
				},
				data : {},
			}).success(function(data, status, headers, config) {
				$("#tree_menu").hide();
				$("#package_menu").hide();
				initTree($scope, $http);
				initPage($scope, $sce, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		function newEffect($scope, $sce, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/monitor/newEffect",
				params : {
					"new_effect" : JSON.stringify($scope.new_effect),
					"pkg_id" : $scope.new_id
				},
				data : {},
			}).success(function(data, status, headers, config) {
				$("#package_new_panel").hide();
				initTree($scope, $http);
				initPage($scope, $sce, $http);
			}).error(function(data, status, headers, config) {
			});
		}
		function updateEffect($scope, $sce, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/monitor/updateEffect",
				params : {
					"update_effect" : JSON.stringify($scope.new_effect)
				},
				data : {},
			}).success(function(data, status, headers, config) {
				$("#package_new_panel").hide();
				initTree($scope, $http);
				initPage($scope, $sce, $http);
			}).error(function(data, status, headers, config) {
			});
		}
		function deleteEffect($scope, $sce, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/monitor/deleteEffect",
				params : {
					"effectId" : $scope.new_effect.id
				},
				data : {},
			}).success(function(data, status, headers, config) {
				$("#package_info_menu").hide();
				initTree($scope, $http);
				initPage($scope, $sce, $http);
			}).error(function(data, status, headers, config) {
			});
		}
		// Regist the AngularJS Module
		var app = angular.module("effect_monitor_app", []);

		// Fix the Controller
		app.controller("monitorController",
				function($scope, $sce, $http) {
					$("#navbar-collapse").find(".active").children("a").css(
							"background-color", "#87CEFA");
					initTree($scope, $http);
					initPage($scope, $sce, $http);
					$scope.showhome = function() {
						$("#myTab li.active").removeClass("active");
						//$("#myTab li[name='shouye']").addClass("active");
						$("#address").attr("src", "");
						$("#myTab li").find("a").css("text-decoration","none ");
						$("#myTab li[name='shouye']").find("a").css("text-decoration","underline");
					};
					$scope.showTab = function(idx) {
						
						if($scope.isOk[idx]){
							$("#address").attr("src", $scope.scurl[idx]);
						}else{
							$("#address").attr("src","${ctx}/static/error.html");
						}
						$("#myTab li.active").removeClass("active");
						$("#myTab li[name='tabs"+idx+"']").addClass("active");
						$("#myTab li").find("a").css("text-decoration","none ");
						$("#myTab li.active").find("a").css("text-decoration","underline");
						$scope.isShow[idx] = true;
						//$("#myTab li:eq(" + idx + ") a").tab('show'); 
					};
					$scope.hideTab = function(idx) {
						$scope.isShow[idx] = false;
						/* if ($("#myTab li.active").attr('id') == "tabs"
								+ idx) { */
						//$("#myTab li[name='shouye']").addClass("active");
						$("#myTab li.active").find("a").css("text-decoration","");
						$("#myTab li[name='shouye']").find("a").css("text-decoration","underline");
						$("#myTab li[name='tabs"+idx+"']").removeClass("active");
						$("#address").attr("src", "");
						//}
					};

					$scope.tree_mouseup = function($event, category) {
						if ($event.which == 3) {
							// 禁止浏览器弹出右键菜单
							document.oncontextmenu = function() {
								return false;
							};
							var value = window.innerHeight - $event.clientY <= 83 ? ($event.clientY-83) :$event.clientY;
							$("#tree_menu").hide();
							$("#package_info_menu").hide();
							$("#package_menu").hide();
							$("#tree_menu").attr(
									"style",
									"display: block; position: fixed; top:"
											+ value + "px; left:"
											+ ($event.pageX - 160)
											+ "px;");
							$("#tree_menu").show();
							$scope.category = category;
						}
					};
					$scope.effectMonitorNewMouseUp = function($event, pkg) {
						if ($event.which == 3) {
							$event.stopPropagation();
							// 禁止浏览器弹出右键菜单
							document.oncontextmenu = function() {
								return false;
							};
							var value = window.innerHeight - $event.clientY <= 173 ? ($event.clientY-173) :$event.clientY;
							$("#package_menu").hide();
							$("#tree_menu").hide();
							$("#package_info_menu").hide();
							$("#package_menu").attr(
									"style",
									"display: block; position: fixed; top:"
											+ value + "px; left:"
											+ ($event.pageX - 160)
											+ "px;");
							$("#package_menu").show();
							$scope.new_effect = new Object();
							$scope.new_id = pkg.packageId;
							$scope.new_package = new Object();
							$scope.new_package.name = pkg.packageName;
						}
						if ($event.which == 1) {
							var $self = $($event.currentTarget);
							getLeftClick($self);
						}
					};
					$scope.effectMonitorUpdateMouseUp = function($event,
							pkg_id, effect) {
						if ($event.which == 3) {
							$event.stopPropagation();
							// 禁止浏览器弹出右键菜单
							document.oncontextmenu = function() {
								return false;
							};
							var value = window.innerHeight - $event.clientY <= 173 ? ($event.clientY-173) :$event.clientY;
							$("#package_info_menu").hide();
							$("#package_info_menu").attr(
									"style",
									"display: block; position: fixed; top:"
											+ value + "px; left:"
											+ ($event.pageX - 160)
											+ "px;");
							$("#package_info_menu").show();
							$("#package_menu").hide();
							$("#tree_menu").hide();
							$scope.new_effect = new Object();
							$scope.new_effect.id = effect.urlId;
							$scope.new_effect.name = effect.urlName;
							$scope.new_effect.url = effect.url;
							$scope.new_id = pkg_id;
						}
					};
					$scope.package_menu_close_func = function() {
						$("#package_menu").hide();
					};
					$scope.package_info_menu_close_func = function() {
						$("#package_info_menu").hide();
					};
					$scope.package_menu_new_effect_func = function($event) {
						$scope.isNewOrUpdateEffect = "new";
						var value = window.innerHeight - $event.clientY <= 200 ? ($event.clientY-200) :$event.clientY;
						$("#package_menu").hide();
						$("#package_new_panel").hide();
						$("#package_new_panel").attr(
								"style",
								"display: block; position: fixed; top:"
										+ value + "px; left:"
										+ ($event.pageX - 350)
										+ "px;width:350px;height:200px;");
						$("#package_new_panel").show();
					};

					$scope.package_menu_update_effect_func = function($event) {
						$scope.isNewOrUpdateEffect = "update";
						var value = window.innerHeight - $event.clientY <= 200 ? ($event.clientY-200) :$event.clientY;
						$("#package_info_menu").hide();
						$("#package_new_panel").hide();
						$("#package_new_panel").attr(
								"style",
								"display: block; position: fixed; top:"
										+ value + "px; left:"
										+ ($event.pageX - 350)
										+ "px;width:350px;height:200px;");
						$("#package_new_panel").show();
					};
					$scope.package_menu_new_effect_cancel = function() {
						$("#package_new_panel").hide();
					};
					$scope.package_menu_new_effect_save = function() {
						if ($scope.isNewOrUpdateEffect == "new") {
							newEffect($scope, $sce, $http);
						} else {
							updateEffect($scope, $sce, $http);
						}
					};
					$scope.package_menu_delete_effect_func = function() {
						$("#deleteModal").modal('show');
						$scope.delFlag = "deleteEffect";
						//deleteEffect($scope, $sce, $http);
					};
					$scope.tree_new_package_func = function($event) {
						$scope.new_package = new Object();
						$scope.isNewOrUpdatePackage = "new";
						var value = window.innerHeight - $event.clientY <= 200 ? ($event.clientY-200) :$event.clientY;
						// 关闭之前菜单
						$("#tree_menu").hide();
						// 新菜单出现
						$("#new_package_menu").hide();
						$("#new_package_menu").attr(
								"style",
								"display: block; position: fixed; top:"
										+ value + "px; left:"
										+ ($event.pageX - 350)
										+ "px;width:350px;height:200px;");
						$("#new_package_menu").show();
					};
					// Update Package
					$scope.package_menu_update_package_func = function($event) {
						$("#tree_menu").hide();
						$("#package_menu").hide();
						$scope.isNewOrUpdatePackage = "update";
						var value = window.innerHeight - $event.clientY <= 200 ? ($event.clientY-200) :$event.clientY;
						// 新菜单出现
						$("#new_package_menu").hide();
						$("#new_package_menu").attr(
								"style",
								"display: block; position: fixed; top:"
										+ value + "px; left:"
										+ ($event.pageX - 350)
										+ "px;width:350px;height:200px;");
						$("#new_package_menu").show();
					};

					$scope.new_package_menu_save = function() {
						if ($scope.isNewOrUpdatePackage == "new") {
							newPackage($scope, $http);
						} else {
							updatePackage($scope, $http);
						}
					};
					// Delete Package
					$scope.package_menu_delete_package_func = function() {
						$("#deleteModal").modal('show');
						$scope.delFlag = "deletePackage";
						//deletePackage($scope, $http);
					};

					// Close Package Menu
					$scope.new_package_menu_cancel = function() {
						$("#new_package_menu").hide();
					};
					$scope.tree_refresh_package_func = function() {
						$("#tree_menu").hide();
						initTree($scope, $http);
					};
					$scope.deleteItem = function() {
						if ('deleteEffect' == $scope.delFlag) {
							deleteEffect($scope, $sce, $http);
						} else if ('deletePackage' == $scope.delFlag) {
							deletePackage($scope, $http);
						} 
						};
				});
	</script>
</body>
</html>