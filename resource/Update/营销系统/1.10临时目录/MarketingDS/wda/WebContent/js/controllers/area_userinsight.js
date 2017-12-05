app
		.controller(
				'AreaUserInsightCtrl',
				[
						'$scope',
						'$rootScope',
						'$http',
						'$localStorage',
						'$filter',
						'bar',
						'pie',
						'barY',
						'hbBar',
						function($scope, $rootScope, $http, $localStorage,
								$filter, bar, pie,barY,hbBar) {

							$scope.sid = $rootScope.sid || $localStorage.sid;
							var periodDataSet = {
								"0" : [ 0, 0 ],
								"-1" : [ -1, -1 ],
								"-7" : [ -6, 0 ],
								"-30" : [ -29, 0 ]
							};
							var timeFormatter = "YYYY-MM-DD";
							var dateFilter = $filter('date');
							var theCurDate = moment(dateFilter(new Date(),
									"yyyy-MM-dd"));
							var theYesDate = moment(moment().subtract(1,"days").format(timeFormatter));
							var thePastDate = moment("1970-01-01");
							// 时间周期变量，同时用于控制前端时间周期按钮的样式
							// 默认为-1，表示 昨天
							$scope.period = -1;

							$scope.isHuanBi = false;
							$scope.separatedDays = 0;
							
							$("#load-me").show();
							$scope.sumHttp = 0;
							$scope.totalHttp = 4;
							
							// 加载页面初值
							$scope.datePicker = {
								date : {
									startDate : theYesDate.clone(),
									endDate : theYesDate.clone()
								},
								date1 : {
									startDate : thePastDate.clone(),
									endDate : thePastDate.clone()
								}
							};
							var getPeriodTime = function(isHuanbi) {
								var pick = "date";
								if (isHuanbi) {
									if (!$scope.isHuanBi) {
										return "";
									}
									pick = "date1";
								}
								var startDate = $scope.datePicker[pick].startDate
										.format(timeFormatter);
								var endDate = $scope.datePicker[pick].endDate
										.format(timeFormatter);
								return startDate + "#" + endDate;
							};

							var resetDatePick = function(period) {
								var perO = periodDataSet[period];
								if (!perO) {
									return;
								}
								$scope.datePicker.date.startDate = theCurDate
										.clone();
								$scope.datePicker.date.endDate = theCurDate
										.clone();
								var perS = perO[0];
								if (perS) {
									$scope.datePicker.date.startDate.subtract(
											0 - perS, "days");
								}
								var perE = perO[1];
								if (perE) {
									$scope.datePicker.date.endDate.subtract(
											0 - perE, "days");
								}
							};

							var getPeriodByDatePick = function() {
								var dateDiff = [
										$scope.datePicker.date.startDate.diff(
												theCurDate, "days"),
										$scope.datePicker.date.endDate.diff(
												theCurDate, "days") ];
								for ( var i in periodDataSet) {
									if (periodDataSet[i].toString() == dateDiff
											.toString()) {
										return i;
									}
								}
								// 这里的1是没有意义的，完全是为了去掉页面上的样式
								return 1;
							};

							$scope.areaDistributionList = {
								curpage : 1,
								itemcss : "un-item uni-cur",
								lastsearchtxt : '',
							};

							$scope.setCurPage = function(page) {
								if (page == $scope.areaDistributionList.curpage) {
									return "pgcur";
								} else {
									return "";
								}
							};
							$scope.selectPage = function(page) {
								if (page <= $scope.areaDistributionList.totalPage
										&& page >= 1) {
									$scope.areaDistributionList.curpage = page;
								}
							};

							/* 指标下拉框 */
							/* 初始化kpi统计项列表 */
							$scope.allOptions = [ {
								key : '浏览量(PV)',
								value : 'pv',
								checked : true
							}, {
								key : '访问次数',
								value : 'visits',
								checked : false
							}, {
								key : '访客数(UV)',
								value : 'uv',
								checked : false
							}, {
								key : '新访客数',
								value : 'nuv',
								checked : false
							}, {
								key : 'IP数',
								value : 'ip',
								checked : false
							} ];

							// 选中的kpi
							$scope.data = [ $scope.allOptions[0] ];

							$scope.canSelectedKpi = 1;

							// 查询男女比例
							fetchAreaUserInsightSex();

							function fetchAreaUserInsightSex() {

								$http(
										{
											method : 'GET',
											url : __urlRoot
													+ '/wda/api/wda/visitor/area/userinsight/sex',
											params : {
												__userKey : __userKey,
												site_id : $scope.sid,
												periodTime : getPeriodTime(),
											}
										})
										.success(
												function(resp, status, headers,
														config) {
													$scope.sumHttp ++;
													if($scope.sumHttp ==$scope.totalHttp){
														$("#load-me").hide();
													}
													if (resp.resultCode == 0) {
														var pieData = resp.data;
														pie.createChart('areaUserinsightSex', pieData,'性别比例');
													}
												}).error(
												function(resp, status, headers,
														config) {
													$scope.sumHttp ++;
													if($scope.sumHttp ==$scope.totalHttp){
														$("#load-me").hide();
													}
													if(!$("#errorHttp").is(":visible")){
														$("#errorHttp").show();
													}
												});
							};
							//年龄比例
							fetchAreaUserInsightAge();
							function fetchAreaUserInsightAge() {
								$http(
										{
											method : 'GET',
											url : __urlRoot
													+ '/wda/api/wda/visitor/area/userinsight/age',
											params : {
												__userKey : __userKey,
												site_id : $scope.sid,
												periodTime : getPeriodTime(),
												huanBiDate : getPeriodTime(true)
											}
										})
										.success(
												function(resp, status, headers,
														config) {
													$scope.sumHttp ++;
													if($scope.sumHttp ==$scope.totalHttp){
														$("#load-me").hide();
													}
													if (resp.resultCode == 0) {
														barY.createChart('areaUserinsightAge',resp,'年龄分布');
													}
												}).error(
												function(resp, status, headers,
														config) {
													$scope.sumHttp ++;
													if($scope.sumHttp ==$scope.totalHttp){
														$("#load-me").hide();
													}
													if(!$("#errorHttp").is(":visible")){
														$("#errorHttp").show();
													}
												});

							};
							//媒体比例
							fetchAreaUserInsightMedia();
							function fetchAreaUserInsightMedia() {
								$http(
										{
											method : 'GET',
											url : __urlRoot
													+ '/wda/api/wda/visitor/area/userinsight/media',
											params : {
												__userKey : __userKey,
												huanBiDate : getPeriodTime(true),
												site_id : $scope.sid,
												periodTime : getPeriodTime(),
											}
										})
										.success(
												function(resp, status, headers,
														config) {
													$scope.sumHttp ++;
													if($scope.sumHttp ==$scope.totalHttp){
														$("#load-me").hide();
													}
													if (resp.resultCode == 0) {
														hbBar.createChart('areaUserinsightMedia',resp,'媒体轨迹');
													}
												}).error(
												function(resp, status, headers,
														config) {
													$scope.sumHttp ++;
													if($scope.sumHttp ==$scope.totalHttp){
														$("#load-me").hide();
													}
													if(!$("#errorHttp").is(":visible")){
														$("#errorHttp").show();
													}
													return [];
												});
							};
							//行业倾向
							fetchAreaUserInsightIndustry();
							function fetchAreaUserInsightIndustry() {
								$http(
										{
											method : 'GET',
											url : __urlRoot
													+ '/wda/api/wda/visitor/area/userinsight/industry',
											params : {
												__userKey : __userKey,
												huanBiDate : getPeriodTime(true),
												site_id : $scope.sid,
												periodTime : getPeriodTime()
											}
										})
										.success(
												function(resp, status, headers,
														config) {
													$scope.sumHttp ++;
													if($scope.sumHttp ==$scope.totalHttp){
														$("#load-me").hide();
													}
													if (resp.resultCode == 0) {
														hbBar.createChart('areaUserinsightIndustry',resp,'行业倾向');
													}
												}).error(
												function(resp, status, headers,
														config) {
													$scope.sumHttp ++;
													if($scope.sumHttp ==$scope.totalHttp){
														$("#load-me").hide();
													}
													if(!$("#errorHttp").is(":visible")){
														$("#errorHttp").show();
													}
												});
							}
							;

							/*
							 * 选择时间周期 按钮事件，调用后台程序查询数据，并刷新前台页面 params: period:
							 * 当前时间周期
							 */
							$scope.selectTimePeriod = function(period) {
								if (period == $scope.period) {
									return;
								}
								
								$("#load-me").show();
								$scope.sumHttp = 0;
								$scope.totalHttp = 4;
								
								// 前台页面 控制逻辑
								// // 控制时间周期按钮样式
								$scope.period = period;
								$scope.isHuanBi = false;
								// 重置第一个时间框时间
								resetDatePick(period);
								$scope.datePicker.date1.startDate = thePastDate
										.clone();
								$scope.datePicker.date1.endDate = thePastDate
										.clone();

							};
							// 第一个时间控件事件
							$scope.timePickerChange = function() {
								
								$("#load-me").show();
								$scope.sumHttp = 0;
								$scope.totalHttp = 4;
								
								// 样式控制
								$scope.period = getPeriodByDatePick();
								$scope.separatedDays = $scope.datePicker.date.endDate
										.diff($scope.datePicker.date.startDate,
												"days");
								if ($scope.separatedDays) {
									// 如果前后时间不同，就按天展示
									$scope.time_dim_value = 2;
								} else {
									$scope.time_dim_value = 1;
								}
								if ($scope.isHuanBi) {
									// 第一个时间控件改变，则根据第一个控件的时间差，修改第二个控件的截止时间
									var temEnd = $scope.datePicker.date1.endDate
											.clone();
									$scope.datePicker.date1.endDate = $scope.datePicker.date1.startDate
											.clone().add($scope.separatedDays,
													"days");
									// 如果发生了改变，那么会执行环比时间框的change事件并进行请求数据
									if (temEnd.diff(
											$scope.datePicker.date1.endDate,
											"days")) {
										return;
									}
								}
								fetchAreaUserInsightSex();
								fetchAreaUserInsightAge();
								fetchAreaUserInsightMedia();
								fetchAreaUserInsightIndustry();
							};

							/*
							 * 环比周期的对比
							 */
							$scope.compareTimePeriodSelect = function() {
								
								$("#load-me").show();
								$scope.sumHttp = 0;
								$scope.totalHttp = 4;
								
								if ($scope.isHuanBi) {
									if ($scope.separatedDays == $scope.datePicker.date1.endDate
											.diff(
													$scope.datePicker.date1.startDate,
													"days")) {
										fetchAreaUserInsightSex();
										fetchAreaUserInsightAge();
										fetchAreaUserInsightMedia();
										fetchAreaUserInsightIndustry();
									} else {
										$scope.datePicker.date1.endDate = $scope.datePicker.date1.startDate
												.clone().add(
														$scope.separatedDays,
														"days");
									}
								}
							};

							/*
							 * 环比按钮事件定义
							 */
							$scope.cacheItem = null;

							$scope.huanbi = function() {
								
								$("#load-me").show();
								$scope.sumHttp = 0;
								$scope.totalHttp = 4;
								
								if ($scope.isHuanBi) {

									if ($scope.data.length == 2) {
										$scope.cacheItem = $scope.data[1];
										// remove last item
										$scope.data[1].checked = false;
										$scope.data.splice(1, 1);
									}
									$scope.datePicker.date1.endDate = $scope.datePicker.date.startDate
											.clone().subtract(1, "days");
									$scope.datePicker.date1.startDate = $scope.datePicker.date1.endDate
											.clone().subtract(
													$scope.separatedDays,
													"days");
								} else {

									if ($scope.cacheItem != null) {
										$scope.data.push($scope.cacheItem);
									}

									if ($scope.data.length == 2) {
										$scope.data[1].checked = true;
									}
									$scope.datePicker.date1.startDate = thePastDate
											.clone();
									$scope.datePicker.date1.endDate = thePastDate
											.clone();
									//请求
									fetchAreaUserInsightSex();
									fetchAreaUserInsightAge();
									fetchAreaUserInsightMedia();
									fetchAreaUserInsightIndustry();

								}

							};
							
							// 窗口关闭事件
							$(".upp-close").click(function() {
								$(this).parents(".up-mask").fadeOut(500);
							});
							
						} ]);
