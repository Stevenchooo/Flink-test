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
<html lang="zh-CN" ng-app="scenario_manager_app">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>应用场景对象管理</title>
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

#propertyTable th {
	text-align: center;
	vertical-align: middle;
}

#propertyTable td {
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

<body ng-controller="scenarioController">


	<div class="up-topbar">

		<ul class="tb-cont">

			<li><a href="#">个人信息</a></li>
			<li><a href="javascript:void(0);">退出</a></li>
		</ul>

	</div>


	<div class="up-main mkcl">


		<ul class="upm-nav">
			<!-- 本页选中的话， 就加 uni-cur-->
			<li class="un-item"><a href="index" class="uni-link">首页</a></li>
			<li class="un-item"><a href="domain_manager" class="uni-link">领域对象管理</a>
			</li>
			<li class="un-item uni-cur"><a href="scenario_manager"
				class="uni-link">应用场景管理</a></li>
			<li class="un-item"><a href="business_manager" class="uni-link">业务规则管理</a></li>
			<li class="un-item"><a href="effect_monitor" class="uni-link">效果监控</a></li>
			<li class="un-item"><a href="user_manager" class="uni-link">用户管理</a></li>
			<li class="un-item"><a href="system_manager" class="uni-link">系统配置</a></li>
		</ul>
		<div class="upm-cont">

			<div class="upmc-container">

				<div class="panel panel-default"
					style="padding: 10px; margin-left: 0.5%; margin-right: 0.5%; padding-bottom: 0px;">
					<div class="row">
						<div class="col-md-12">
							<div class="row">
								<div class="col-md-1">
									<button type="button" class="btn btn-success"
										data-toggle="modal" data-target="#modal"
										ng-click="newModal();">
										<i class="glyphicon glyphicon-plus"></i>&nbsp;新建
									</button>
								</div>
								<div class="col-md-1">
									<button type="button" class="btn btn-danger"
										ng-click="deleteScenarios();">
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
									<table id="table"
										class="table table-hover table-striped table-bordered"
										ng-model="scenarios">
										<thead class="active">
											<tr>
												<th><input type="checkbox" onclick="tableCheckBox();">
												</th>
												<th>&nbsp;&nbsp;&nbsp;&nbsp;ID&nbsp;&nbsp;&nbsp;&nbsp;</th>
												<th>&nbsp;&nbsp;&nbsp;&nbsp;应用场景名称
													&nbsp;&nbsp;&nbsp;&nbsp;</th>
												<th>发布</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
											<tr ng-repeat="scenario in scenarios track by $index">
												<td><input type="checkbox"
													ng-disabled="'true'==scenario.published"></td>
												<td><span ng-bind="scenario.id"></span></td>
												<td><span ng-bind="scenario.name"></span></td>
												<td><span ng-bind="{true: '是', false: '否'}[scenario.published]"></span></td>
												<td>
													<button type="button" class="btn btn-link"
														ng-disabled="'true'==scenario.published"
														ng-click="deleteScenario(scenario.id);">
														<i class="glyphicon glyphicon-trash"></i>
													</button>
													<button type="button" class="btn btn-link"
														ng-click="updateModal(scenario);" data-toggle="modal">
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
									style="float: left; margin-top: 23px; font-size: 15px;"></span>
								<select class="form-control" ng-change="setPageSize();"
									ng-model="pageSize"
									style="float: left; margin-top: 20px; width: 75px; margin-left: 10px;">
									<option value="10">10</option>
									<option value="25">25</option>
									<option value="50">50</option>
									<option value="100">100</option>
									<option value="ALL">全部</option>
								</select> <span
									style="float: left; margin-top: 23px; font-size: 15px; margin-left: 10px;">条
								</span>
								<nav id="page_nav" style="float: right;" ng-show="page_nav_show">
									<ul class="pagination">
										<li><a href="" aria-label="Previous"
											ng-click="page_pre();"> <span aria-hidden="true">&laquo;</span>
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

			<div class="upm-sidebar"
				style="right: -360px; width: 360px; height: 804px;z-index: 3;"
				data-expand="closed">
				<a href="javascript:void(0);" class="us-sideactive">展开设置</a>
				<div class="us-sidecont">
				
				<div style="padding-right: 0px;">
				<div class="input-group"
					style="margin-left: auto; margin-right: auto; padding-bottom: 10px; width: 95%;">
					<input type="text" class="form-control " placeholder="package name"
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
							class="package-tree list-group-item  glyphicon glyphicon-plus ng-cloak"
							style="margin-bottom: -1px; border: 1px solid #DDD;"
							ng-bind="' '+tree_package.scenarioPackageName"
							ng-mouseup="tree_package_mouseup($event, tree_package);" ng-cloak>
							</div>
						<div
							ng-repeat="tree_package_relation in tree_package.scenarioPackageRelations track by $index"
							style="display: none;">
							<span
								style="float: left; margin-left: 20px; margin-right: 20px; height: 40px"></span>
							<i class="glyphicon glyphicon-ok-circle"
								style="color: #428BCA; float: left; padding-top: 2px; margin-top: 12px"
								ng-show="tree_package_relation.scenarioPublished=='true'"></i>
							<div
								style="display: block; padding: 10px 15px; margin-bottom: -1px; border: 1px solid #DDD;"
								class="package-tree"
								ng-mouseup="tree_package_relation_mouseup($event, tree_package_relation);">{{tree_package_relation.scenarioName}}</div>
						</div>
					</div>
				</div>
			</div>
				
				</div>
			</div>
		</div>
	</div>
	<div class="up-ft">
		<p>EMUI官方网站 - (粤ICP备09176709号-16）- 华为软件技术有限公司 @ HUAWEI,2011. All
			Rights Reserved</p>
	</div>



	
	<div class="modal fade" id="propertyModal" role="dialog"
		aria-labelledby="propertyModalLabel" data-backdrop="static">
		<div class="modal-dialog modal-lg" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close" ng-click="propertyModalCancel();">
						<span aria-hidden="true">&times;</span>
					</button>
					<h5 class="modal-title" id="propertyModalLabel">应用场景对象属性</h5>
				</div>
				<form name="update_scenario_form" class="form-horizontal" novalidate>
					<div class="modal-body">
						<div class="row" style="padding-bottom: 15px;">
							<div class="col-md-3"
								style="text-align: center; padding-left: 0px; padding-right: 0px;">
								<span style="font-size: 15px;">应用场景对象名称：</span>
							</div>
							<div class="col-md-7">
								<span style="font-size: 15px;" ng-bind="propertyName"></span>
							</div>
							<div class="col-md-1">
								<button type="button" style="padding: 0px;" class="btn btn-link"
									ng-click="addPropertyDomain();">
									<i class="glyphicon glyphicon-plus"></i>&nbsp;领域对象
								</button>
							</div>
						</div>
						<div class="row" ng-show="false">
							<span ng-model="allPublishedDomains"></span>
						</div>
						<table id="propertyTable"
							class="table table-hover table-condensed" ng-model="propertys">
							<tbody ng-repeat="property in propertys track by $index">
								<tr>
									<td
										style="min-width: 120px; padding-left: 10px; text-align: left;"><span>对象:</span></td>
									<td><select class="form-control" style="width: 150px;"
										ng-model="property.domainId"
										ng-options="domain.id as domain.name for domain in allPublishedDomains"
										ng-selected="property.domainId==domain.id"
										ng-init="propertyDomainInit($index);"
										ng-change="propertyDomainChanged($index);">
									</select></td>
									<td><select class="form-control"
										ng-model="property.domainOperator" style="width: 100px;">
											<option value="&&">&&</option>
											<option value="||">||</option>
									</select></td>
									<td>
										<button type="button" class="btn btn-link"
											ng-click="addDomainPropertyRule($index);">
											<i class="glyphicon glyphicon-plus"></i>&nbsp;添加对象属性
										</button>
									</td>
									<td>
										<button type="button" class="btn btn-link"
											ng-click="addCustomRule($index);">
											<i class="glyphicon glyphicon-plus"></i>&nbsp;添加自定义规则
										</button>
									</td>
									<td>
										<button type="button" class="btn btn-link"
											ng-click="deletePropertyDomain($index);">
											<i class="glyphicon glyphicon-remove"></i>&nbsp;移除对象
										</button>
									</td>
								<tr
									ng-repeat="domainPropertie in property.domainProperties track by $index">
									<td style="font-size: 12px; padding-left: 20px;">对象属性：</td>
									<td><select class="form-control" style="width: 150px;"
										ng-model="domainPropertie.domainPropertyId"
										ng-options="domainProperty.id as domainProperty.name for domainProperty in property.domainPropertys"
										ng-selected="domainPropertie.domainPropertyId==domainProperty.id">
									</select></td>
									<td><select class="form-control"
										ng-model="domainPropertie.domainPropertyOperator"
										style="width: 100px;">
											<option value="==">==</option>
											<option value="!=">!=</option>
											<option value="&gt;">&gt;</option>
											<option value="&gt;=">&gt;=</option>
											<option value="&lt;">&lt;</option>
											<option value="&lt;=">&lt;=</option>
									</select></td>
									<td>
										<div class="has-feedback">
											<input type="text" class="form-control"
												ng-model="domainPropertie.domainPropertyOperatorValue"
												name="scenarioName{{$parent.$index}}{{$index}}"
												placeholder="不为空且长度不超过255" ng-minlength="1"
												ng-maxlength="255" required> <span
												class="glyphicon glyphicon-ok form-control-feedback"
												ng-show="update_scenario_form.scenarioName{{$parent.$index}}{{$index}}.$valid"></span>
										</div>
									</td>
									<td><select class="form-control"
										ng-model="domainPropertie.domainPropertyRuleOperator"
										style="width: 100px;">
											<option value="&&">&&</option>
											<option value="||">||</option>
									</select></td>
									<td>
										<button type="button" class="btn btn-link"
											ng-click="deletePropertyDomainForPropertys($parent.$index,$index);">
											<i class="glyphicon glyphicon-remove"></i>&nbsp;
										</button>
									</td>
								</tr>
								<tr
									ng-repeat="customRule in property.customRules track by $index">
									<td style="font-size: 12px;">自定义规则：</td>
									<td><div class="has-feedback">
											<input type="text" class="form-control"
												ng-model="customRule.customRule"
												name="code{{$parent.$index}}{{$index}}"
												placeholder="不为空且长度不超过255" ng-minlength="1"
												ng-maxlength="255" required> <span
												class="glyphicon glyphicon-ok form-control-feedback"
												ng-show="update_scenario_form.code{{$parent.$index}}{{$index}}.$valid"></span>
										</div></td>
									<td><select class="form-control"
										ng-model="customRule.customRuleOperator" style="width: 100px;">
											<option value="&&">&&</option>
											<option value="||">||</option>
									</select></td>
									<td>
										<button type="button" class="btn btn-link"
											ng-click="deletePropertyDomainForCustomRule($parent.$index,$index);">
											<i class="glyphicon glyphicon-remove"></i>&nbsp;
										</button>
									</td>
								</tr>
								</tr>
							</tbody>
						</table>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal"
							ng-click="propertyModalCancel();">关闭</button>
						<button type="button" class="btn btn-primary" data-dismiss="modal"
							ng-click="propertyModalSave();"
							ng-disabled="update_scenario_form.$invalid || judge_Modal_Save_func() || 'true'==oldScenario.published"">保存</button>
					</div>
				</form>
			</div>
		</div>
	</div>

   
   <!-- 群删提示框 -->
   <div class="modal fade" id="deleteMessage" role="dialog" data-backdrop="static">
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
					<h5 class="modal-title" style="text-align:center;">未选中删除项！</h5>
				</div>							
			</div>		
					
		</div>
	</div>
   
  <!--  删除提示框 -->
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
   
   
	<!-- 操作提示信息modal -->
	<div class="modal fade" id="infoModal" role="dialog" data-backdrop="static">
		<div class="modal-dialog modal-lg" role="document"
			style="width: 300px; position: relative; top: 250px;">
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
					<h5 class="modal-title">场景正在使用，请勿编辑！</h5>
				</div>
			</div>
		</div>
	</div>

	<!-- New/Update Scenario Modal -->
	<div class="modal fade" id="modal" role="dialog"
		aria-labelledby="modalLabel" ng-show="isModalShow"
		data-backdrop="static">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal"
						aria-label="Close" ng-click="modalCancel();">
						<span aria-hidden="true">&times;</span>
					</button>
					<h5 class="modal-title" id="modalLabel" ng-bind="modalTitle"></h5>
				</div>
				<form name="new_scenario_form" class="form-horizontal" novalidate>
					<div class="modal-body">
						<div class="row" ng-show="false">
							<span ng-model="isNewOrUpdateScenario"></span>
						</div>
						<div class="row" ng-show="false">
							<span ng-model="oldScenario"></span>
						</div>
						<div class="row" ng-show="false">
							<span ng-model="newScenario"></span>
						</div>
						<div class="row">
							<div class="col-md-4"
								style="padding-right: 0px; padding-top: 7px;">
								<span style="float: right; font-size: 13px;">应用场景对象名称：</span>
							</div>
							<div class="col-md-8">
								<input type="text" name="scenario_name"
									ng-disabled="'true'==oldScenario.published"
									class="form-control" placeholder="不为空且长度不超过255"
									ng-minlength="1" ng-maxlength="255" ng-model="newScenario.name"
									required> <span style="padding-right: 50px;"
									class="glyphicon glyphicon-ok form-control-feedback"
									ng-show="new_scenario_form.scenario_name.$valid"></span>
							</div>
						</div>
						<div class="row" style="padding-top: 10px;">
							<div class="col-md-4" style="padding-right: 0px;">
								<span style="float: right; font-size: 13px;">是否已发布：</span>
							</div>
							<div class="col-md-8">
								<select class="form-control" ng-model="newScenario.published"
									style="width: 80px;" ng-disabled="isNewOrUpdateScenario=='new'">
									<option value="false">否</option>
									<option value="true">是</option>
								</select>
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-info" style="float: left;"
							data-toggle="modal" data-target="#propertyModal"
							ng-show="isNewOrUpdateScenario=='update'"
							ng-click="propertyModal();">
							<i class="glyphicon glyphicon-list-alt"></i>&nbsp;应用场景对象属性
						</button>
						<button type="button" class="btn btn-default" data-dismiss="modal"
							ng-click="modalCancel();">关闭</button>
						<button type="button" class="btn btn-primary" data-dismiss="modal"
							ng-disabled="new_scenario_form.$invalid" ng-click="modalSave();">保存</button>
					</div>
				</form>
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
		<form name="new_package_form" class="form-horizontal" novalidate>
			<ul class="dropdown-menu" id="new_package_menu">
				<li style="padding-top: 20px;">
					<div class="row">
						<div class="col-md-4"
							style="text-align: center; padding-right: 0px; padding-top: 7px;">
							<span style="font-size: 15px;">Package名称:</span>
						</div>
						<div class="col-md-8" style="padding-left: 0px;">
							<input type="text" class="form-control" style="width: 220px;"
								ng-model="new_package.name" name="code"
								placeholder="不为空且长度不超过20" ng-minlength="1" ng-maxlength="20"
								required> <span style="padding-right: 50px;"
								class="glyphicon glyphicon-ok form-control-feedback"
								ng-show="new_package_form.code.$valid"></span>
						</div>
					</div>
				</li>
				<li style="padding-top: 20px;">
					<button type="button" class="btn btn-primary"
						style="float: right; margin-right: 20px;"
						ng-click="new_package_menu_save();"
						ng-disabled="new_package_form.$invalid">保存</button>
					<button type="button" class="btn btn-default"
						style="float: right; margin-right: 20px;"
						ng-click="new_package_menu_cancel();">关闭</button>
				</li>
			</ul>
		</form>
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
						<span
							style="word-break: normal; width: auto; display: block; white-space: pre-wrap; word-wrap: break-word; overflow: hidden;"
							ng-bind="new_package.name"></span>
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
							ng-model="new_package_relation_scenario.id"
							ng-options="package_scenario.id as package_scenario.name for package_scenario in package_scenarios"
							ng-selected="new_package_relation_scenario.id==package_scenario.id">
							
                            <option ng-repeat="package_domain in package_domains track by $index" value="{{package_domain}}" ng-bind="package_domain.name" ng-selected="package_domain.id==new_package_relation_domain.id"></option>
                       
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
								+ "/res/angularjs/scenario/queryScenarioTotalNum",
						params : {},
						data : {},
					}).success(function(data, status, headers, config) {
				totalRowNum = data;
				pageTotalNum = Math.ceil(totalRowNum / pageSize);
				queryScenarios($scope, $http);
				refreshPageInfo($scope);
			}).error(function(data, status, headers, config) {
			});
		}

		// Query All Scenarios
		function queryAllScenarios($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/queryAllScenarios",
						params : {},
						data : {},
					}).success(function(data, status, headers, config) {
				$scope.scenarios = data;
			}).error(function(data, status, headers, config) {
			});
		}

		// Query Scenarios By Page
		function queryScenariosByPage($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/queryScenariosByPage",
						params : {
							"index" : rowStart - 1,
							"pageSize" : pageSize
						},
						data : {},
					}).success(function(data, status, headers, config) {
				$scope.scenarios = data;
			}).error(function(data, status, headers, config) {
			});
		}

		// Query Design Scenarios
		function queryDesignScenarios($scope, $http) {
			paginations.length = 0;
			if (totalRowNum == 0) {
				rowStart = 0;
				rowEnd = 0;
			} else {
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
			}
			queryScenariosByPage($scope, $http);
		}

		// Query Scenarios
		function queryScenarios($scope, $http) {
			if (pageSize == "ALL") {
				queryAllScenarios($scope, $http);
				$scope.page_nav_show = false;
			} else {
				$scope.page_nav_show = true;
				queryDesignScenarios($scope, $http);
			}
		}

		// New Scenario Save
		function newScenarioSave($scope, $http) {
			$http({
				method : "POST",
				url : getPathPrefix() + "/res/angularjs/scenario/newScenario",
				params : {
					"name" : $scope.newScenario.name,
					"published" : $scope.newScenario.published
				},
				data : {},
			}).success(function(data, status, headers, config) {
				initPage($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Update Scenario Save
		function updateScenarioSave($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/updateScenario",
						params : {
							"scenario" : $scope.newScenario
						},
						data : {},
					}).success(function(data, status, headers, config) {
				initPage($scope, $http);
				refreshPackageTree($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Delete Scenario
		function deleteScenario(scenarioId, $scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/deleteScenario",
						params : {
							"id" : scenarioId
						},
						data : {},
					}).success(function(data, status, headers, config) {
				initPage($scope, $http);
				refreshPackageTree($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Table CheckBox
		function tableCheckBox() {
			var len = $("#table > thead :input:checkbox:checked").length;
			var checkedBoxs = $("#table > tbody :input:checkbox");
			if (len == 1) {
				for (var i = 0; i < checkedBoxs.length; i++) {
					if (!checkedBoxs[i].disabled) {
						checkedBoxs[i].checked = true;
					}
				}
			} else if (len == 0) {
				for (var i = 0; i < checkedBoxs.length; i++) {
					if (!checkedBoxs[i].disabled) {
						checkedBoxs[i].checked = false;
					}
				}
			}
		}

		// Cancel Table CheckBox
		function cancelTableCheckBox() {
			$("#table :input:checkbox").each(function() {
				$(this).attr("checked", false);
			});
		}

		// Delete Scenarios
		function deleteScenarios(scenarioIds, $scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/deleteScenarios",
						params : {
							"ids" : scenarioIds
						},
						data : {},
					}).success(function(data, status, headers, config) {
				initPage($scope, $http);
				cancelTableCheckBox();
				refreshPackageTree($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Scenarios Order By Asc
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

		// Scenarios Order By Desc
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

		// New Scenario Property VO Save
		function newScenarioPropertyVOsSave($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/newScenarioPropertyVOs",
						params : {
							"scenarioId" : $scope.newScenario.id,
							"scenarioPropertyVOs" : JSON
									.stringify($scope.propertys)
						},
						data : {},
					}).success(function(data, status, headers, config) {
			}).error(function(data, status, headers, config) {
			});
		}

		// Query Scenario Property VO
		function queryScenarioPropertyVOsByScenarioId($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/queryScenarioPropertyVOsByScenarioId",
						params : {
							"scenarioId" : $scope.newScenario.id
						},
						data : {},
					}).success(function(data, status, headers, config) {
				if (null == data || data.length == 0) {
					$scope.propertys = new Array();
				} else {
					$scope.propertys = data;
				}
			}).error(function(data, status, headers, config) {
			});
		}

		// Query All Published Domains
		function queryAllPublishedDomains($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/queryAllPublishedDomains",
						params : {},
						data : {},
					}).success(function(data, status, headers, config) {
				$scope.allPublishedDomains = data;
			}).error(function(data, status, headers, config) {
			});
		}

		// Query Design Domain Propertys
		function queryDesignDomainPropertys(index, $scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/queryDesignDomainPropertys",
						params : {
							"domainId" : $scope.propertys[index].domainId
						},
						data : {},
					}).success(function(data, status, headers, config) {
				$scope.propertys[index].domainPropertys = data;
			}).error(function(data, status, headers, config) {
			});
		}

		// New scenario Package
		function newScenarioPackage($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/newScenarioPackage",
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

		// Refresh Package Tree
		function refreshPackageTree($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/queryRefreshScenarioPackages",
						params : {},
						data : {},
					}).success(function(data, status, headers, config) {
				$scope.tree_data = data;
			}).error(function(data, status, headers, config) {
			});
		}
		// Query All Package Scenario
		function queryAllScenariosForPackage($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/queryAllScenarios",
						params : {},
						data : {},
					}).success(function(data, status, headers, config) {
				$scope.package_scenarios = data;
				if ($scope.isNewOrUpdatePackageRelation == "new") {
					$scope.new_package_relation_scenario = data[0];
				}
			}).error(function(data, status, headers, config) {
			});
		}

		// New Scenario Package Relation
		function newScenarioPackageRelation($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/newScenarioPackageRelation",
						params : {
							"scenarioPackage" : JSON
									.stringify($scope.new_package),
							"scenarioId" : $scope.new_package_relation_scenario.id
						},
						data : {},
					}).success(function(data, status, headers, config) {
				$("#new_package_relation_menu").hide();
				refreshPackageTree($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Update Scenario Package
		function updateScenarioPackage($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/updateScenarioPackage",
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

		// Delete Scenario Package
		function deleteScenarioPackage($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/deleteScenarioPackage",
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

		// Update Scenario Package Relation
		function updateScenarioPackageRelation($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/updateScenarioPackageRelation",
						params : {
							"scenarioPackageRelation" : $scope.new_tree_package_relation
						},
						data : {},
					}).success(function(data, status, headers, config) {
				$("#new_package_relation_menu").hide();
				refreshPackageTree($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Delete Scenario Package Relation
		function deleteScenarioPackageRelation($scope, $http) {
			$http(
					{
						method : "POST",
						url : getPathPrefix()
								+ "/res/angularjs/scenario/deleteScenarioPackageRelation",
						params : {
							"scenarioPackageRelationId" : $scope.new_tree_package_relation.id
						},
						data : {},
					}).success(function(data, status, headers, config) {
				$("#tree_menu").hide();
				$("#package_relation_menu").hide();
				refreshPackageTree($scope, $http);
			}).error(function(data, status, headers, config) {
			});
		}

		// Regist the AngularJS Module
		var app = angular.module("scenario_manager_app", []);

		// Fix the Controller
		app
				.controller(
						"scenarioController",
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
								queryScenarios($scope, $http);
								refreshPageInfo($scope);
							}

							// Pre Page
							$scope.page_pre = function() {
								if (totalRowNum == 0 || totalRowNum <= pageSize) {
									return;
								}
								cancelTableCheckBox();
								currentPage = currentPage - 1 == 0 ? pageTotalNum
										: currentPage - 1;
								queryScenarios($scope, $http);
								refreshPageInfo($scope);
							}

							// Next Page
							$scope.page_next = function() {
								if (totalRowNum == 0 || totalRowNum <= pageSize) {
									return;
								}
								cancelTableCheckBox();
								currentPage = currentPage + 1 > pageTotalNum ? 1
										: currentPage + 1;
								queryScenarios($scope, $http);
								refreshPageInfo($scope);
							}

							// Page Chosen
							$scope.page_chosen = function(number) {
								if (totalRowNum == 0 || totalRowNum <= pageSize
										|| number == "..") {
									return;
								}
								cancelTableCheckBox();
								currentPage = number;
								queryScenarios($scope, $http);
								refreshPageInfo($scope);
							}

							// New Scenario
							$scope.newModal = function() {
								$scope.oldScenario = new Object();
								$scope.oldScenario.published = "false";
								$scope.isNewOrUpdateScenario = "new";
								$scope.modalTitle = "新建应用场景对象";
								$scope.newScenario = new Object();
								$scope.newScenario.name = "";
								$scope.newScenario.published = "false";
								$scope.isModalShow = true;
							}

							// Update Scenario
							$scope.updateModal = function(scenario) {

								/* $http(
										{
											method : "POST",
											url : getPathPrefix()
													+ "/res/angularjs/scenario/judgeScenarioInUse",
											params : {
												"scenarioId" : scenario.id
											},
											data : {},
										})
										.success(
												function(data, status, headers,
														config) {
													if (data) {
														$("#infoModal").modal(
																"show");
													} else { */
														$scope.isNewOrUpdateScenario = "update";
														$scope.modalTitle = "更新应用场景对象";
														$scope.newScenario = scenario;
														$scope.oldScenario = new Object();
														$scope.oldScenario.name = scenario.name;
														$scope.oldScenario.published = scenario.published;
														$scope.isModalShow = true;
														$("#modal").modal(
																"show");
												/* 	}
												}).error(
												function(data, status, headers,
														config) {
						
												}); */
							}

							// Scenario Modal Save
							$scope.modalSave = function() {
								if ($scope.isNewOrUpdateScenario == "new") {
									newScenarioSave($scope, $http);
								} else if ($scope.isNewOrUpdateScenario == "update") {
									updateScenarioSave($scope, $http);
								}
							}

							// Scenario Modal Cancel
							$scope.modalCancel = function() {
								if ($scope.isNewOrUpdateScenario == "update") {
									$scope.newScenario.name = $scope.oldScenario.name;
									$scope.newScenario.published = $scope.oldScenario.published;
								}
							}

							// Delete Scenario
							$scope.deleteScenario = function(scenarioId) {
								//deleteScenario(scenarioId, $scope, $http);
								$scope.deleteId = scenarioId;
								$scope.deleteFlag = "deleteScenario";
								$("#deleteModal").modal("show");
							};

							$scope.deleteItem = function() {
								var deleteFlag = $scope.deleteFlag;
								if ("deleteScenario" == deleteFlag) {
									deleteScenario($scope.deleteId, $scope,
											$http);
								} else if ("deleteScenarios" == deleteFlag) {
									deleteScenarios($scope.scenarioIds, $scope,
											$http);
								} else if ("deleteScenarioPackage" == deleteFlag) {
									deleteScenarioPackage($scope, $http);
								} else if ("deleteScenarioPackageRelation" == deleteFlag) {
									deleteScenarioPackageRelation($scope, $http);
								}
							}

							$scope.deleteCancel = function() {
								$("#deleteModal").hide();
							}

							// Delete Scenarios
							$scope.deleteScenarios = function() {
								$scope.deleteFlag = "deleteScenarios";
								var scenarioIds = new Array();
								var scenarioItems = $(
										"#table > tbody :input:checkbox:checked")
										.parent().next().children();
								for (var i = 0; i < scenarioItems.length; i++) {
									scenarioIds
											.push(scenarioItems[i].textContent);
								}
								if (0 == scenarioIds.length) {
									$("#deleteMessage").modal("show");
								} else {
									$scope.scenarioIds = scenarioIds;
									$("#deleteModal").modal("show");
								}
							};

							// Default Sort
							$scope.defaultSort = function() {
								queryScenarios($scope, $http);
							}

							// Sort By Id Asc
							var sortByIdAsc = true;

							// Sort By Id
							$scope.sortById = function() {
								if (null == $scope.scenarios
										|| $scope.scenarios.length == 0) {
									return;
								}
								if (sortByIdAsc) {
									$scope.scenarios.sort(orderByAsc("id"));
								} else {
									$scope.scenarios.sort(orderByDesc("id"));
								}
								sortByIdAsc = !sortByIdAsc;
							}

							// Sort By Name Asc
							var sortByNameAsc = true;

							// Sort By Name
							$scope.sortByName = function() {
								if (null == $scope.scenarios
										|| $scope.scenarios.length == 0) {
									return;
								}
								if (sortByNameAsc) {
									$scope.scenarios.sort(orderByAsc("name"));
								} else {
									$scope.scenarios.sort(orderByDesc("name"));
								}
								sortByNameAsc = !sortByNameAsc;
							}

							// Sort By Published Asc
							var sortByPublishedAsc = true;

							// Sort By Published
							$scope.sortByPublished = function() {
								if (null == $scope.scenarios
										|| $scope.scenarios.length == 0) {
									return;
								}
								if (sortByPublishedAsc) {
									$scope.scenarios
											.sort(orderByAsc("published"));
								} else {
									$scope.scenarios
											.sort(orderByDesc("published"));
								}
								sortByPublishedAsc = !sortByPublishedAsc;
							}

							// Scenario Property Modal
							$scope.propertyModal = function() {
								$scope.propertyName = $scope.newScenario.name;
								$scope.isModalShow = false;
								queryAllPublishedDomains($scope, $http);
								queryScenarioPropertyVOsByScenarioId($scope,
										$http);
							}

							// Scenario Property Modal Cancel
							$scope.propertyModalCancel = function() {
								$scope.propertys.length = 0;
								$scope.isModalShow = true;
							}

							// Scenario Propertys Modal Save
							$scope.propertyModalSave = function() {
								newScenarioPropertyVOsSave($scope, $http);
								$scope.propertys.length = 0;
								$scope.isModalShow = true;
							}

							// Add Scenario Property Domain
							$scope.addPropertyDomain = function() {
								var property = new Object();
								property.scenarioId = $scope.newScenario.id;
								property.domainId = $scope.allPublishedDomains[0].id;
								property.domainOperator = "&&";
								property.customRules = new Array();
								property.domainProperties = new Array();
								$scope.propertys.push(property);
							}

							// Property Domain Init
							$scope.propertyDomainInit = function(index) {
								var domain_id = $scope.propertys[index].domainId;
								if (null != domain_id
										&& domain_id.toString().length > 0) {
									queryDesignDomainPropertys(index, $scope,
											$http);
								}
							}

							// Property Domain Changed
							$scope.propertyDomainChanged = function(index) {
								$scope.propertys[index].domainOperator = "&&";
								$scope.propertys[index].domainProperties.length = 0;
								$scope.propertys[index].customRules.length = 0;
								queryDesignDomainPropertys(index, $scope, $http);
							}

							// Delete Scenario Property Domain
							$scope.deletePropertyDomain = function(index) {
								$scope.propertys.splice(index, 1);
							}

							// Add Domain Property Rule
							$scope.addDomainPropertyRule = function(index) {
								var domain_property = new Object();
								domain_property.domainPropertyId = $scope.propertys[index].domainPropertys[0].id;
								domain_property.domainPropertyOperator = "==";
								domain_property.domainPropertyOperatorValue = "";
								domain_property.domainPropertyRuleOperator = "&&";
								$scope.propertys[index].domainProperties
										.push(domain_property);
							}

							// Add Custom Rule
							$scope.addCustomRule = function(index) {
								var custom_rule = new Object();
								custom_rule.customRule = "";
								custom_rule.customRuleOperator = "&&";
								$scope.propertys[index].customRules
										.push(custom_rule);
							}

							// Delete Property Domain For Property
							$scope.deletePropertyDomainForPropertys = function(
									parentIndex, index) {
								$scope.propertys[parentIndex].domainProperties
										.splice(index, 1);
							}

							// Delete Property Domain For Custom Rule
							$scope.deletePropertyDomainForCustomRule = function(
									parentIndex, index) {
								$scope.propertys[parentIndex].customRules
										.splice(index, 1);
							}

							$scope.tree_mouseup = function($event) {
								if ($event.which == 3) {
									// 禁止浏览器弹出右键菜单
									document.oncontextmenu = function() {
										return false;
									}
									var value = window.innerHeight-$event.clientY <= 83 ? ($event.clientY-83) :$event.clientY;
									$("#package_menu").hide();
									$("#tree_menu").hide();
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

							$scope.new_package_menu_save = function() {
								if ($scope.isNewOrUpdatePackage == "new") {
									newScenarioPackage($scope, $http);
								} else {
									updateScenarioPackage($scope, $http);
								}
							}

							$scope.new_package_menu_cancel = function() {
								$("#new_package_menu").hide();
							}

							$scope.tree_refresh_package_func = function() {
								$("#tree_menu").hide();
								refreshPackageTree($scope, $http);
							}

							$scope.tree_package_mouseup = function($event,
									tree_package) {
								if ($event.which == 3) {
									// 禁止浏览器弹出右键菜单
									document.oncontextmenu = function() {
										return false;
									}
									$event.stopPropagation();
									var value = window.innerHeight - $event.clientY <= 177 ? ($event.clientY-177) :$event.clientY;
									$("#tree_menu").hide();
									$("#package_relation_menu").hide();
									$("#package_menu").hide();
									$("#package_menu").attr(
											"style",
											"display: block; position: fixed; top:"
													+ value
													+ "px; left:"
													+ ($event.pageX - 181)
													+ "px;");
									$("#package_menu").show();
									$scope.new_package = new Object();
									$scope.new_package.id = tree_package.scenarioPackageId;
									$scope.new_package.name = tree_package.scenarioPackageName;
								}

								if ($event.which == 1) {
									var $self = $($event.currentTarget);
									getLeftClick($self);
								}
							}

							// New Package Relation
							$scope.package_menu_new_relation_func = function(
									$event) {
								queryAllScenariosForPackage($scope, $http);
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
														+ ($event.pageX - 350)
														+ "px;width:350px;height:200px;");
								$("#new_package_relation_menu").show();
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

							// Delete Package
							$scope.package_menu_delete_package_func = function() {
								//deleteScenarioPackage($scope, $http);
								$scope.deleteFlag = "deleteScenarioPackage";
								$("#deleteModal").modal("show");
							}

							// Close Package Menu
							$scope.package_menu_close_func = function() {
								$("#tree_menu").hide();
								$("#package_menu").hide();
							}

							$scope.new_package_relation_menu_save = function() {
								if ($scope.isNewOrUpdatePackageRelation == "new") {
									newScenarioPackageRelation($scope, $http);
								} else {
									$scope.new_tree_package_relation.scenarioId = $scope.new_package_relation_scenario.id;
									//$scope.new_tree_package_relation.scenario_name = $scope.new_package_relation_scenario.name;
									//$scope.new_tree_package_relation.scenario_published = $scope.new_package_relation_scenario.published;
									updateScenarioPackageRelation($scope, $http);
								}
							}

							$scope.new_package_relation_menu_cancel = function() {
								$("#new_package_relation_menu").hide();
							}

							$scope.tree_package_relation_mouseup = function(
									$event, tree_package_relation) {
								if ($event.which == 3) {
									// 禁止浏览器弹出右键菜单
									document.oncontextmenu = function() {
										return false;
									}
									$event.stopPropagation();
									var value = window.innerHeight - $event.clientY <= 128 ? ($event.clientY-128) :$event.clientY;
									$("#package_menu").hide();
									$("#tree_menu").hide();
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

							$scope.package_relation_menu_update_func = function(
									$event) {
								queryAllScenariosForPackage($scope, $http);
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
														+ ($event.pageX - 350)
														+ "px;width:350px;height:200px;");
								$("#new_package_relation_menu").show();
								$scope.new_package_relation_scenario = new Object();
								$scope.new_package_relation_scenario.id = $scope.new_tree_package_relation.scenarioId;
								$scope.new_package_relation_scenario.name = $scope.new_tree_package_relation.scenarioName;
								$scope.new_package_relation_scenario.published = $scope.new_tree_package_relation.scenarioPublished;
							}

							$scope.package_relation_menu_delete_func = function() {
								//deleteScenarioPackageRelation($scope, $http);
								$scope.deleteFlag = "deleteScenarioPackageRelation";
								$("#deleteModal").modal("show");
							}

							$scope.package_relation_menu_close_func = function() {
								$("#tree_menu").hide();
								$("#package_relation_menu").hide();
							}

							$scope.judge_Modal_Save_func = function() {
								if($scope.propertys) {
								for (var i = 0; i < $scope.propertys.length; i++) {
									if (0 == $scope.propertys[i].domainProperties.length
											&& 0 == $scope.propertys[i].customRules.length) {
										return true;
									}
								}
								}
								return false;
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
					if (tree_data[i].scenarioPackageName
							.indexOf(package_tree_filter_name) >= 0) {
						array.push(tree_data[i]);
					}
				}
				return array;
			}

		});

		$(document).ready(function() {
			$(".us-sideactive").on("click", function() {
				var _this = $(this);
				var _thisP = _this.parent(".upm-sidebar");
				var rightP = parseInt($(".upm-sidebar").css("right"));
				if (_thisP.data("expand") === "closed") {
					_thisP.data("expand", "open").animate({
						"right" : "0px"
					}, function() {
						_this.text("收起设置");
					});
				} else {
					_thisP.data("expand", "closed").animate({
						"right" : "-360px"
					}, function() {
						_this.text("展开设置");
					});
				}

			})
		});
	</script>

</body>
</html>
