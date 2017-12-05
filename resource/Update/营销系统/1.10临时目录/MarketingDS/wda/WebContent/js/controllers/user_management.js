app
		.factory(
				'userService',
				[
						'$http',
						'$rootScope',
						function($http, $rootScope) {

							var user = {};
							var users = [];
							var groups = [];
							var selectedGroups = [];
							// var roles = [];
							return {

								getUsers : function() {
									return $http
											.get(
													__urlRoot
															+ '/wda/api/wda/admin/user/list')
											.then(function(response) {
												users = response.data;
												// $rootScope.$broadcast('handleSharedBooks',books);
												return users;
											})
								},

								deleteUser : function(id) {
									return $http(
											{
												url : __urlRoot
														+ '/wda/api/wda/admin/user/delete',
												method : "POST",
												params : {
													id : id
												}
											}).success(function(response) {
										// websites = response.data;
										// $rootScope.$broadcast('handleSharedBooks',books);
									});
								},

								getUserById : function(id) {
									return $http
											.get(
													__urlRoot
															+ '/wda/api/wda/admin/user/queryById',
													{
														params : {
															id : id
														}
													}).then(function(response) {
												user = response.data;
												// $rootScope.$broadcast('handleSharedBooks',books);
												return user;
											})
								},

								getUserGroups : function(id) {
									return $http
											.get(
													__urlRoot
															+ '/wda/api/wda/admin/user/queryUserGroups',
													{
														params : {
															userId : id
														}
													}).then(function(response) {
												selectedGroups = response.data;
												// $rootScope.$broadcast('handleSharedBooks',books);
												return selectedGroups;
											})
								},

								saveUser : function($params) {
									return $http(
											{
												headers : {
													'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
												},
												url : __urlRoot
														+ '/wda/api/wda/admin/user/create',
												method : "POST",
												data : $params
											}).success(function(response) {
										// $rootScope.$broadcast('handleSharedBooks',books);

										return response.resultCode;
									});
								},

								updateUser : function(id) {

									return $http(
											{
												headers : {
													'Content-Type' : 'application/x-www-form-urlencoded'
												},
												url : __urlRoot
														+ '/wda/api/wda/admin/user/update',
												method : "POST",
												data : $params
											}).success(function(response) {
										user = response.data;
									});
								},

								getGroups : function() {
									return $http
											.get(
													__urlRoot
															+ '/wda/api/wda/admin/group/select')
											.then(function(response) {
												groups = response.data;
												return groups;
											})
								},

							// getRoles: function() {
							// return $http.get('/wda/api/role/query',{
							// params: {
							// "__userKey" : __userKey,
							// }
							// }).then(function(response) {
							// roles = response.data;
							// return roles;
							// })
							// },

							};
						} ]);

app
		.controller(
				'UserManagementCtrl',
				[
						'$rootScope',
						'$scope',
						'$filter',
						'$http',
						'$timeout',
						'exDialog',
						'userService',
						'$state',
						function($rootScope, $scope, $filter, $http, $timeout,
								exDialog, userService, $state) {

							if($rootScope.risAdmin == 0) {
								$state.go('noright', {});
							}

							$scope.userManageJson = {
								curpage : 1,
								itemcss : "un-item uni-cur",
								lastsearchtxt : '',
							};

							$scope.setCurPage = function(page) {
								if (page == $scope.userManageJson.curpage) {
									return "pgcur";
								} else {
									return "";
								}
							};
							$scope.selectPage = function(page) {
								if (page <= $scope.userManageJson.totalPage
										&& page >= 1) {
									$scope.userManageJson.curpage = page;
								}
							};
							
							//点击切换是否是管理员
							$scope.isManager = function(value) {
								$scope.isManagerValue = value;
							};
							
							//点击切换是否是荣耀中心
							$scope.isHonor = function(value) {
								$scope.isHonorValue = value;
							};

							$scope.options = [ {
								value : 0,
								name : '否'
							}, {
								value : 1,
								name : '是'
							} ];
							$scope.selected = 0;

							userService.getUsers().then(function(response) {
								$scope.userManageJson.data = response.data;
								$scope.users = response.data;
							});

							$scope.$on('userList', function(events, values) {
								$scope.userManageJson.data = values;
								$scope.users = values;
							});

							userService.getGroups().then(function(response) {
								$scope.groups = response.data;
							});

							// userService.getRoles().then(function(response) {
							// $scope.roles = response.data;
							// });

							$scope.editUserDialog = function(id) {

								userService
										.getUserById(id)
										.then(
												function(response) {
													$scope.user = response.data;
													$scope.isManagerValue = $scope.user.isAdmin;
													$scope.isHonorValue = $scope.user.isHonor;
													// $scope.role_selected =
													// $scope.user.role;
													exDialog
															.openPrime({
																scope : $scope,
																template : __tplRoot
																		+ 'tpl/user_management_add.html',
																controller : 'UserManagementCtrl',
																width : '600px',
																draggable : false,
																closeByClickOutside : false
															// animation: false,
															// grayBackground:
															// false
															});

												});
							};

							$scope.updateWebsiteDialog = function(id) {
								$params = $.param({
									"id" : id,
									"site_id" : $scope.website.site_id,
									"site_name" : $scope.website.site_name,
									"site_url" : $scope.website.site_url,
									"group_id" : $scope.selected,
								// "role_name" : $scope.role_selected
								});

								userService.updateWebsites($params);

								exDialog.openMessage({
									scope : $scope,
									message : "已更新.",
									closeAllDialogs : true
								});
							};

							$scope.addUserDialog = function() {
								$scope.user = null;
								$scope.isManagerValue = 0;
								$scope.isHonorValue = 0;

								exDialog.openPrime({
									scope : $scope,
									template : __tplRoot
											+ 'tpl/user_management_add.html',
									controller : 'UserManagementCtrl',
									width : '600px',
									draggable : false,
									closeByClickOutside : false
								// animation: false,
								// grayBackground: false
								});
							};

							$scope.saveUser = function(userId) {
								if (userId) {
									$scope.updateUser(userId);
								} else {
									$scope.addUser();
								}
							};

							$scope.updateUser = function(userId) {
								exDialog
										.openConfirm({
											scope : $scope,
											title : "   ",
											message : "确认保存吗?",
											closeImmediateParent : false,
											closeByClickOutside : false
										})
										.then(
												function(value) {
													$params = $
															.param({
																"__userKey" : __userKey,
																"creator" : __account,
																"account" : $scope.user.account,
																"fullName" : $scope.user.fullName,
																// "role" :
																// $scope.role_selected,
																"department" : $scope.user.department,
																"phoneNum" : $scope.user.phoneNum,
																"email" : $scope.user.email,
																"description" : $scope.user.description,
																"id" : userId,
																"isAdmin" : $scope.isManagerValue,
																"isHonor" : $scope.isHonorValue,
															});

													$http(
															{
																headers : {
																	'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
																},
																url : __urlRoot
																		+ '/wda/api/wda/admin/user/update',
																method : "POST",
																data : $params
															})
															.success(
																	function(
																			response) {
																		if (response.resultCode == 0) {
																			userService
																					.getUsers()
																					.then(
																							function(
																									response) {
																								$scope.userManageJson.data = response.data;
																								$scope.users = response.data;
																								$rootScope
																										.$broadcast(
																												'userList',
																												$scope.users);
																							});

																			exDialog
																					.closeAll();
																		} else {
																			exDialog
																					.openMessage({
																						scope : $scope,
																						title : "   ",
																						icon : "error",
																						message : getLocalReason(
																								response.resultCode,
																								response.info),
																						closeAllDialogs : false,
																						closeByClickOutside : false,
																						closeImmediateParent : false
																					});
																		}
																	});
												});
							};

							$scope.addUser = function() {

								exDialog
										.openConfirm({
											scope : $scope,
											title : "   ",
											message : "确认保存吗?",
											closeImmediateParent : false,
											closeByClickOutside : false
										})
										.then(
												function(value) {

													$params = $
															.param({
																"__userKey" : __userKey,
																"creator" : __account,
																"account" : $scope.user.account,
																// "password" :
																// $scope.user.password,
																// "confirmPassword"
																// :
																// $scope.user.confirmPassword,
																"fullName" : $scope.user.fullName,
																// "role" :
																// $scope.role_selected,
																"department" : $scope.user.department,
																"phoneNum" : $scope.user.phoneNum,
																"email" : $scope.user.email,
																"description" : $scope.user.description,
																"isAdmin" : $scope.isManagerValue,
																"isHonor" : $scope.isHonorValue,
															});

													$http(
															{
																headers : {
																	'Content-Type' : 'application/x-www-form-urlencoded;charset=utf-8'
																},
																url : __urlRoot
																		+ '/wda/api/wda/admin/user/create',
																method : "POST",
																data : $params
															})
															.success(
																	function(
																			response) {

																		if (response.resultCode == 0) {
																			userService
																					.getUsers()
																					.then(
																							function(
																									response) {
																								$scope.userManageJson.data = response.data;
																								$scope.users = response.data;
																								$rootScope
																										.$broadcast(
																												'userList',
																												$scope.users);
																							});

																			exDialog
																					.closeAll();
																		} else {
																			exDialog
																					.openMessage({
																						scope : $scope,
																						title : "   ",
																						icon : "error",
																						message : getLocalReason(
																								response.resultCode,
																								response.info),
																						closeAllDialogs : false
																					});
																		}
																	});
												});
							};

							$scope.delUserDialog = function(id) {

								exDialog
										.openConfirm({
											scope : $scope,
											title : "   ",
											icon : "warning",
											closeByClickOutside : false,
											message : "确认要删除吗?",
											closeImmediateParent : false
										})
										.then(
												function(value) {

													$http(
															{
																url : __urlRoot
																		+ '/wda/api/wda/admin/user/delete',
																method : "POST",
																params : {
																	id : id,
																	__userKey : __userKey
																}
															})
															.success(
																	function(
																			response) {
																		if (response.resultCode == 0) {
																			userService
																					.getUsers()
																					.then(
																							function(
																									response) {
																								$scope.userManageJson.data = response.data;
																								$scope.users = response.data;
																								$rootScope
																										.$broadcast(
																												'userList',
																												$scope.users);
																							});

																			exDialog
																					.closeAll();
																		} else {
																			exDialog
																					.openMessage({
																						scope : $scope,
																						title : "错误",
																						message : getLocalReason(
																								response.resultCode,
																								response.info),
																						closeAllDialogs : false,
																						closeByClickOutside : false,
																						closeImmediateParent : false
																					});
																		}
																	});
												});
							};

							$scope.groupDialog = function(id) {

								userService
										.getUserGroups(id)
										.then(
												function(response) {
													$scope.selectedGroups = response.data;

													exDialog
															.openPrime({
																scope : $scope,
																template : __tplRoot
																		+ 'tpl/user_group_management.html',
																controller : 'UserManagementCtrl',
																width : '600px',
																draggable : true,
																closeByClickOutside : false,
																closeImmediateParent : false
															// animation: false,
															// grayBackground:
															// false
															});
												});
							};
						} ]);