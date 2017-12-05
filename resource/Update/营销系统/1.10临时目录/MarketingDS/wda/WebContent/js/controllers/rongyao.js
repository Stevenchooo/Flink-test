'use strict';

/* Controllers */
var myAppModule = angular.module('appRongyao', []);
angular
		.module('appRongyao')
		.controller(
				'AppRongYaoCtrl',
				[
						'$scope',
						'$http',
						'barLine',
						'line',
						'barCrowd',
						'wordCloud',
						function($scope, $http, barLine, line, barCrowd,
								wordCloud1) {

							$scope.mainShow = true;
							$scope.sideShow = false;
							$scope.wdaShow = false;
							$scope.requestLocation = "";
							$scope.account = __account;
							$scope.reportId = 0;

							$scope.hotWordArray = [];
							$scope.reportTypeChoose = 0;

							$scope.reportData = '';

							var dateStr = "";
							var myDate = new Date();
							$scope.yearOfThis = myDate.getFullYear();
							dateStr = myDate.getFullYear() + "年"
									+ (myDate.getMonth() + 1) + "月"
									+ myDate.getDate() + "日";
							myDate.setMonth(myDate.getMonth() - 3);
							dateStr = myDate.getFullYear() + "年"
									+ (myDate.getMonth() + 1) + "月"
									+ myDate.getDate() + "日" + "至" + dateStr;
							$scope.thrMonDate = dateStr;
							// 报告走马灯
							function RunReport(k, num) {
								var count = k % num;
								if (count != 0) {
									$(".cdr-item").hide();
									$("#report_" + (k * 2 - 1)).show();
									$("#report_" + (k * 2 - 2)).show();
								} else {
									$(".cdr-item").hide();
									$("#report_" + (num * 2 - 1)).show();
									$("#report_" + (num * 2 - 2)).show();
								}
								k++;
								if (k == (num + 1)) {
									k = 1;
								}
								window.setTimeout(function() {
									RunReport(k, num);
								}, 5000);
							}

							$scope.reportJson = {
								curpage : 1,
								itemcss : "un-item uni-cur",
								lastsearchtxt : '',
							};

							$scope.setCurPage = function(page) {
								if (page == $scope.reportJson.curpage) {
									return "pgcur";
								} else {
									return "";
								}
							};
							$scope.selectPage = function(page) {
								if (page <= $scope.reportJson.totalPage
										&& page >= 1) {
									$scope.reportJson.curpage = page;
								}
							};

							$scope.setShowDown = function(row) {
								if (row.reportCreator == __account
										|| row.reportManagerUser
												.indexOf(__account) > -1
										|| row.reportDownUser
												.indexOf(__account) > -1
										|| row.reportDownUser == "全部") {
									return "rlid-show";
								} else {
									return "rlid-disable";
								}
							};

							$scope.isReportCenter = false;

							$scope.isHonor = 0;
							// 请求权限页面是否展示
							$http({
								method : 'GET',
								url : __urlRoot + '/wda/api/wda/page/isornot',
								params : {
									__userKey : __userKey,
									account : __account
								}
							}).success(function(resp, status, headers, config) {
								if (resp.resultCode == 0) {
									$scope.isHonor = resp.data[0].isHonor;
									$("#pageHonor").show();
								}
							}).error(function(resp, status, headers, config) {
								if (!$("#errorHttp").is(":visible")) {
									$("#errorHttp").show();
								}
							});

							obtainCenterIp();
							function obtainCenterIp() {
								$http(
										{
											method : 'GET',
											url : __urlRoot
													+ '/wda/api/wda/page/rongyao/obtainCenterIp'
										})
										.success(
												function(resp, status, headers,
														config) {
													if (resp.resultCode == 0) {
														$scope.marketIp = resp.marketIp;
														$scope.questionnaireIp = resp.questionnaireIp;
														$scope.wdaIp = resp.wdaIp;
														$scope.socialPublicOpinionIp = resp.socialPublicOpinionIp;
														$scope.userInsightIp = resp.userInsightIp;
														preToMarket();
														if (screen.width < 1400) {
															document.body.style.zoom = 0.9;
															document.body.style.cssText += '; -moz-transform: scale('
																	+ 0.9
																	+ ');-moz-transform-origin: center 0;';
														}
													}
												}).error(
												function(resp, status, headers,
														config) {
													if (!$("#errorHttp").is(
															":visible")) {
														$("#errorHttp").show();
													}
												});
							}
							;

							// 荣耀界面报告下载数据请求
							$scope.download = function(row, isHome) {
								if (isHome) {
									if (!(row.reportCreator == __account
											|| row.reportManagerUser
													.indexOf(__account) > -1
											|| row.reportDownUser
													.indexOf(__account) > -1 || row.reportDownUser == "全部")) {
										return;
									}
								}

								var reportId = row.id;
								var url = __urlRoot
										+ '/wda/api/wda/page/rongyao/reportDownload?__userKey='
										+ __userKey;
								if (typeof (reportId) != "undefined"
										&& reportId != "" && reportId != "-1") {
									url = url + "&reportId=" + reportId;
								}
								location.href = url;
							};

							// 荣耀中心，营销大数据概况的请求
							honorSummary();
							function honorSummary() {
								$http(
										{
											method : 'GET',
											url : __urlRoot
													+ '/wda/api/wda/page/rongyao/honorSummary',
											params : {
												__userKey : __userKey
											}
										})
										.success(
												function(resp, status, headers,
														config) {
													if (resp.resultCode == 0) {
														$scope.honorSummary = resp.summary[0];
													}
												}).error(
												function(resp, status, headers,
														config) {
													if (!$("#errorHttp").is(
															":visible")) {
														$("#errorHttp").show();
													}
												});
							}
							;

							// 荣耀中心，人群属性的请求
							personAnalysis();
							function personAnalysis() {
								$http(
										{
											method : 'GET',
											url : __urlRoot
													+ '/wda/api/wda/page/rongyao/personAnalysis',
											params : {
												__userKey : __userKey,
											}
										})
										.success(
												function(resp, status, headers,
														config) {
													if (resp.resultCode == 0) {
														var honor = resp.personAnalysisData;
														var honorPlay = resp.personAnalysisData1;
														barCrowd.createChart(
																"crowd1",
																"crowd2",
																"crowd3", resp);
													}
												}).error(
												function(resp, status, headers,
														config) {
													if (!$("#errorHttp").is(
															":visible")) {
														$("#errorHttp").show();
													}
												});
							}
							;
							$scope.wordCloudData = {
								datas : [ {
									brandName : '荣耀',
									words : [ {
										name : '屏幕好',
										value : 123
									}, {
										name : '全金属',
										value : 45
									}, {
										name : '弧面玻璃',
										value : 100
									}, {
										name : '后置指纹识别',
										value : 125
									}, {
										name : 'EMUI4.0',
										value : 128
									} ]
								}, {
									brandName : '小米',
									words : [ {
										name : '差评1',
										value : 123
									}, {
										name : '差评2',
										value : 45
									}, {
										name : '差评3',
										value : 1
									}, {
										name : '差评4',
										value : 125
									}, {
										name : '差评5',
										value : 128
									} ]
								} ],
								result_code : 200,
								resultInfo : 'success'
							};
							// 荣耀中心，社会舆情的请求
							// wordCloud();
							// wordCloud1.createChart("wordCloud1",$scope.wordCloudData);
							function wordCloud() {
								$http(
										{
											method : 'GET',
											url : __urlRoot
													+ '/wda/api/wda/page/rongyao/wordCloud',
											params : {
												__userKey : __userKey
											}
										})
										.success(
												function(resp, status, headers,
														config) {
													if (resp.resultCode == 0) {
													}
												}).error(
												function(resp, status, headers,
														config) {
													if (!$("#errorHttp").is(
															":visible")) {
														$("#errorHttp").show();
													}
												});
							}
							;
							// 荣耀中心，社会舆情的请求
							// brandVoice();
							function brandVoice() {
								$http(
										{
											method : 'GET',
											url : __urlRoot
													+ '/wda/api/wda/page/rongyao/brandVoice',
											params : {
												__userKey : __userKey
											}
										})
										.success(
												function(resp, status, headers,
														config) {
													if (resp.resultCode == 0) {
													}
												}).error(
												function(resp, status, headers,
														config) {
													if (!$("#errorHttp").is(
															":visible")) {
														$("#errorHttp").show();
													}
												});
							}
							;

							// 荣耀中心，流量经营的请求
							flowManagement();
							function flowManagement() {
								$http(
										{
											method : 'GET',
											url : __urlRoot
													+ '/wda/api/wda/page/rongyao/flowManagement',
											params : {
												__userKey : __userKey,
											}
										})
										.success(
												function(resp, status, headers,
														config) {
													if (resp.resultCode == 0) {
														var dataLineTrend = resp.lineTrend;
														var dataLineCTR = resp.lineCTR;

														line
																.createChart(
																		"flowLineTrend",
																		"flowLineCTR",
																		dataLineTrend,
																		dataLineCTR,
																		resp.lineTitle[0].am
																				+ "至"
																				+ resp.lineTitle[0].bm);
													}
												}).error(
												function(resp, status, headers,
														config) {
													if (!$("#errorHttp").is(
															":visible")) {
														$("#errorHttp").show();
													}
												});
							}
							;

							// 荣耀中心，阵地经营的请求
							positionManagement();
							function positionManagement(id1, id2, isVamll) {

								$http(
										{
											method : 'GET',
											url : __urlRoot
													+ '/wda/api/wda/page/rongyao/positionManagement',
											params : {
												__userKey : __userKey,
											}
										})
										.success(
												function(resp, status, headers,
														config) {
													if (resp.resultCode == 0) {
														var dataBarPC = resp.dataBarPC;
														var dataLinePC = resp.dataLinePC;
														var wapBarList = resp.wapBarList;
														var wapLineList = resp.wapLineList;

														var clubBarPC = resp.clubBarList;
														var clubLinePC = resp.clubLineList;

														barLine.createChart(
																'barVamll',
																'lineVmall',
																dataBarPC,
																wapBarList,
																dataLinePC,
																wapLineList,
																true);
														barLine.createChart(
																'barCNClub',
																'lineCNClub',
																clubBarPC, [],
																clubLinePC, [],
																false);
													}
												}).error(
												function(resp, status, headers,
														config) {
													if (!$("#errorHttp").is(
															":visible")) {
														$("#errorHttp").show();
													}
												});
							}
							;

							// 报告信息
							fetchReportInformation();
							function fetchReportInformation() {
								$http(
										{
											method : 'GET',
											url : __urlRoot
													+ '/wda/api/wda/page/rongyao/reportInformation',
											params : {
												__userKey : __userKey
											}
										})
										.success(
												function(resp, status, headers,
														config) {
													if (resp.resultCode == 0) {
														$scope.reportData = resp.data;

														var num = $scope.reportData.length % 2;
														if (num != 0) {
															num = $scope.reportData.length / 2 + 1;
														} else {
															num = $scope.reportData.length / 2;
														}

														setTimeout(function() {
															RunReport(1, num);
														}, 200);
													}
												}).error(
												function(resp, status, headers,
														config) {
													if (!$("#errorHttp").is(
															":visible")) {
														$("#errorHttp").show();
													}
												});

							}
							;
							// 调研报告整个页面所有的信息
							fetchReportCenterAll();
							function fetchReportCenterAll() {
								$http(
										{
											method : 'GET',
											url : __urlRoot
													+ '/wda/api/wda/page/rongyao/reportCenterAll',
											params : {
												__userKey : __userKey
											}
										})
										.success(
												function(resp, status, headers,
														config) {
													if (resp.resultCode == 0) {
														// $scope.reportData =
														// resp.data;
														$scope.reportJson.data = resp.reportJsonData;
														$scope.reportType = resp.reportType;
														$scope.hotWord = resp.hotWord;
														$scope.reportJsonData = $scope.reportJson.data;
													}
												}).error(
												function(resp, status, headers,
														config) {
													if (!$("#errorHttp").is(
															":visible")) {
														$("#errorHttp").show();
													}
												});
							}
							;

							$scope.mainPage = function() {
								if ($("#mainPage").hasClass("cdmn-on")) {
									return;
								}
								$("#guideArea").find("a")
										.removeClass("cdmn-on");
								$("#mainPage").addClass("cdmn-on");
								$scope.sideShow = false;
								$scope.wdaShow = false;
								$("#mainFrame").attr("src", "");
								$scope.mainShow = true;

							};

							$scope.flowManagement = function() {
								doJump("flowManagement", $scope.marketIp);
							};

							$scope.areaManagement = function() {
								doJump("areaManagement", $scope.wdaIp);
							};

							$scope.socialPublicOpinion = function() {
								// $
								// .ajax({
								// type : "post",
								// url : "接口",
								// data : {
								// userName : "",
								// password : ""
								// },
								// async : true,
								// success : function(data,textStatus) {
								// //相应操作
								// }
								// });
								doJump("socialPublicOpinion",
										$scope.socialPublicOpinionIp);
							};

							$scope.researchInsight = function() {
								doJump("researchInsight",
										$scope.questionnaireIp);
							};

							$scope.researchReport = function() {
								$("#guideArea").find("a")
										.removeClass("cdmn-on");
								$("#researchReport").addClass("cdmn-on");

								$scope.isReportCenter = true;
								$scope.sideShow = true;
								$scope.mainShow = false;
								$scope.wdaShow = false;

							};

							var doJump = function(id, src) {
								if ($("#" + id + "").hasClass("cdmn-on")) {
									return;
								}
								$("#mainFrame").attr("src", "");
								$("#guideArea").find("a")
										.removeClass("cdmn-on");
								$("#" + id + "").addClass("cdmn-on");
								$scope.mainShow = false;
								$scope.wdaShow = false;
								$("#mainFrame").attr("src", src);
								$scope.sideShow = true;
								$scope.isReportCenter = false;
							};

							var preToMarket = function() {
								$("#mainFrame").attr("src", $scope.marketIp);
							}

							// 报告类型点击事件
							$("#repTypes")
									.on(
											"click",
											".ann-stop",
											function() {

												var useData = $scope.reportJsonData;

												var flag = false;
												if ($(this)
														.hasClass("selected")) {
													flag = true;
												}
												$(this)
														.siblings()
														.each(
																function() {
																	if ($(this)
																			.html() != "可下载") {
																		$(this)
																				.removeClass(
																						"selected");
																	}
																});
												// 如果报告类型未选中
												if (flag) {
													$(this).removeClass(
															"selected");
													$scope.reportTypeChoose = 0;
													var data = [];
													for ( var i in useData) {
														var downCon = (useData[i].reportCreator == __account
																|| useData[i].reportManagerUser
																		.indexOf(__account) > -1
																|| useData[i].reportDownUser
																		.indexOf(__account) > -1 || useData[i].reportDownUser == "全部");

														if ($scope.hotWordArray.length > 0) {
															if ($("#repTypes")
																	.find(
																			".download")
																	.hasClass(
																			"selected")) {
																if (myContain(
																		$scope.hotWordArray,
																		useData[i].reportKeyword)
																		&& downCon) {
																	data
																			.push(useData[i]);
																}
															} else {
																if (myContain(
																		$scope.hotWordArray,
																		useData[i].reportKeyword)) {
																	data
																			.push(useData[i]);
																}
															}
														} else {
															if ($("#repTypes")
																	.find(
																			".download")
																	.hasClass(
																			"selected")) {
																if (downCon) {
																	data
																			.push(useData[i]);
																}
															} else {
																data
																		.push(useData[i]);
															}
														}
													}
													$scope.reportJson.data = data;
												} else {
													$(this)
															.addClass(
																	"selected");
													var nameType = $(this)
															.html();
													var id;
													for ( var i in $scope.reportType) {
														if ($scope.reportType[i].reportTypeName == nameType) {
															id = $scope.reportType[i].id;
															break;
														}
													}
													$scope.reportTypeChoose = id;

													var data = [];
													for ( var i in useData) {
														if (useData[i].reportTypeId == id) {
															var downCon = (useData[i].reportCreator == __account
																	|| useData[i].reportManagerUser
																			.indexOf(__account) > -1
																	|| useData[i].reportDownUser
																			.indexOf(__account) > -1 || useData[i].reportDownUser == "全部");

															if ($scope.hotWordArray.length > 0) {
																if ($(
																		"#repTypes")
																		.find(
																				".download")
																		.hasClass(
																				"selected")) {
																	if (myContain(
																			$scope.hotWordArray,
																			useData[i].reportKeyword)
																			&& downCon) {
																		data
																				.push(useData[i]);
																	}
																} else {
																	if (myContain(
																			$scope.hotWordArray,
																			useData[i].reportKeyword)) {
																		data
																				.push(useData[i]);
																	}
																}
															} else {
																if ($(
																		"#repTypes")
																		.find(
																				".download")
																		.hasClass(
																				"selected")) {
																	if (downCon) {
																		data
																				.push(useData[i]);
																	}
																} else {
																	data
																			.push(useData[i]);
																}
															}
														}
													}
													$scope.reportJson.data = data;
												}
												$scope.tempData = $scope.reportJson.data;
												$("#firstPage").click();
											});

							// 可下载点击事件
							$("#repTypes")
									.on(
											"click",
											".download",
											function() {

												var useData = $scope.reportJsonData;

												if ($(this)
														.hasClass("selected")) {
													$(this).removeClass(
															"selected");
													var data = [];
													for ( var i in useData) {
														if ($scope.hotWordArray.length > 0) {
															if ($scope.reportTypeChoose != 0) {
																if (myContain(
																		$scope.hotWordArray,
																		useData[i].reportKeyword)
																		&& useData[i].reportTypeId == $scope.reportTypeChoose) {
																	data
																			.push(useData[i]);
																}
															} else {
																if (myContain(
																		$scope.hotWordArray,
																		useData[i].reportKeyword)) {
																	data
																			.push(useData[i]);
																}
															}
														} else {
															if ($scope.reportTypeChoose != 0) {
																if (useData[i].reportTypeId == $scope.reportTypeChoose) {
																	data
																			.push(useData[i]);
																}
															} else {
																data
																		.push(useData[i]);
															}
														}
													}
													$scope.reportJson.data = data;
												} else {
													$(this)
															.addClass(
																	"selected");
													var data = [];
													for ( var i in useData) {
														if (useData[i].reportCreator == __account
																|| useData[i].reportManagerUser
																		.indexOf(__account) > -1
																|| useData[i].reportDownUser
																		.indexOf(__account) > -1
																|| useData[i].reportDownUser == "全部") {
															if ($scope.hotWordArray.length > 0) {
																if ($scope.reportTypeChoose != 0) {
																	if (myContain(
																			$scope.hotWordArray,
																			useData[i].reportKeyword)
																			&& useData[i].reportTypeId == $scope.reportTypeChoose) {
																		data
																				.push(useData[i]);
																	}
																} else {
																	if (myContain(
																			$scope.hotWordArray,
																			useData[i].reportKeyword)) {
																		data
																				.push(useData[i]);
																	}
																}
															} else {
																if ($scope.reportTypeChoose != 0) {
																	if (useData[i].reportTypeId == $scope.reportTypeChoose) {
																		data
																				.push(useData[i]);
																	}
																} else {
																	data
																			.push(useData[i]);
																}
															}
														}
													}

													$scope.reportJson.data = data;
												}
												$scope.tempData = $scope.reportJson.data;
												$("#firstPage").click();
											});

							var myContain = function(array, a) {
								if (array) {
									for ( var i in array) {
										if (a == array[i]) {
											return true;
										}
									}
								}
								return false;
							};

							// 热门关键词点击事件
							$("#repKeywords")
									.on(
											"click",
											"a",
											function() {
												var useData = $scope.reportJsonData;

												if ($(this)
														.hasClass("selected")) {
													$(this).removeClass(
															"selected");
												} else {
													$(this)
															.addClass(
																	"selected");
												}

												var keyArray = [];
												$(this).parent().find(
														".selected").each(
														function() {
															keyArray.push($(
																	this)
																	.html());
														});

												$scope.hotWordArray = keyArray;

												if (keyArray.length > 0) {
													var data = [];
													for ( var i in useData) {
														if (myContain(
																$scope.hotWordArray,
																useData[i].reportKeyword)) {

															var downCon = (useData[i].reportCreator == __account
																	|| useData[i].reportManagerUser
																			.indexOf(__account) > -1
																	|| useData[i].reportDownUser
																			.indexOf(__account) > -1 || useData[i].reportDownUser == "全部");

															if ($scope.reportTypeChoose != 0) {
																if ($(
																		"#repTypes")
																		.find(
																				".download")
																		.hasClass(
																				"selected")) {
																	if (useData[i].reportTypeId == $scope.reportTypeChoose
																			&& downCon) {
																		data
																				.push(useData[i]);
																	}
																} else {
																	if (useData[i].reportTypeId == $scope.reportTypeChoose) {
																		data
																				.push(useData[i]);
																	}
																}
															} else {
																if ($(
																		"#repTypes")
																		.find(
																				".download")
																		.hasClass(
																				"selected")) {
																	if (downCon) {
																		data
																				.push(useData[i]);
																	}
																} else {
																	data
																			.push(useData[i]);
																}
															}
														}
													}
													$scope.reportJson.data = data;
												} else {
													var data = [];
													for ( var i in useData) {
														var downCon = (useData[i].reportCreator == __account
																|| useData[i].reportManagerUser
																		.indexOf(__account) > -1
																|| useData[i].reportDownUser
																		.indexOf(__account) > -1 || useData[i].reportDownUser == "全部");

														if ($scope.reportTypeChoose != 0) {
															if ($("#repTypes")
																	.find(
																			".download")
																	.hasClass(
																			"selected")) {
																if (useData[i].reportTypeId == $scope.reportTypeChoose
																		&& downCon) {
																	data
																			.push(useData[i]);
																}
															} else {
																if (useData[i].reportTypeId == $scope.reportTypeChoose) {
																	data
																			.push(useData[i]);
																}
															}
														} else {
															if ($("#repTypes")
																	.find(
																			".download")
																	.hasClass(
																			"selected")) {
																if (downCon) {
																	data
																			.push(useData[i]);
																}
															} else {
																data
																		.push(useData[i]);
															}
														}
													}
													$scope.reportJson.data = data;
												}
												$("#firstPage").click();
											});

							// 有下载权限的点击事件
							$(".rl-item").on("click", ".rli-show", function() {
								var reportId = $(this).data("id");

								var form = $("#downloadReportForm");
								var input1 = $("#downloadReportId");
								input1.attr("value", reportId);
								form.append(input1);
								form.submit();// 表单提交
							});

							// 没有下载权限的弹出框点击事件
							$("#rep-list").on("click", ".rlid-disable",
									function() {
										$("#noRepRights").show();
									});

							// 没有下载权限的窗口关闭事件
							$(".upp-close").click(function() {
								$(this).parents(".up-mask").fadeOut(500);
							});

						} ]);

angular
		.module('appRongyao')
		.factory('HackerNewsService', function(CacheService) {
			return {
				getNews : function(key) {
					var news = CacheService.get(key);

					if (news) {
						return news;
					}

					return null;
				},
				setNews : function(key, value) {
					CacheService.put(key, value);
				},
				clearNews : function(key) {
					CacheService.put(key, '');
				}
			};
		})

		.factory(
				'barLine',
				function() {
					var BarService = Object.create(BaseService);
					BarService.createChart = function(idBar, idLine, dataBarPC,
							wapBarList, dataLinePC, wapLineList, PCWAP) {
						var myChartBar = this.getMychart(idBar);
						var myChartLine = this.getMychart(idLine);

						if (dataBarPC.length <= 0) {
							myChartBar.clear();
							myChartBar.showLoading({
								text : "暂无数据",
								textStyle : {
									fontSize : 30
								},
								y : 300,
								effect : 'bar'
							});
							myChartLine.clear();
							myChartLine.showLoading({
								text : "暂无数据",
								y : 300,
							});
						} else {
							// 阵地经营-柱状图
							var xBar = [];
							var yWapBar = [];
							var yPCBar = [];
							for ( var i in dataBarPC) {
								xBar
										.push(dataBarPC[i].x == 1 ? "广告导流"
												: dataBarPC[i].x == 2 ? "链接导流"
														: "直接访问");
								yPCBar.push(dataBarPC[i].y);
							}

							if (wapBarList.length > 0) {
								for ( var i in wapBarList) {
									yWapBar.push(wapBarList[i].y);
								}
							}

							var legendData = PCWAP ? [ "PC", "WAP" ] : [];
							var titleName = PCWAP ? "荣耀官网" : "花粉论坛";

							var name1 = PCWAP ? "PC" : "";
							var name2 = PCWAP ? "WAP" : "";

							var seriesDataBar = [ {
								name : "PC",
								type : 'bar',
								data : yPCBar
							}, {
								name : "WAP",
								type : 'bar',
								data : yWapBar
							} ];

							if (!PCWAP) {
								seriesDataBar = [ {
									name : "花粉论坛",
									type : 'bar',
									data : yPCBar
								} ];
							}

							var optionBar = {
								title : {
									text : titleName,
									left : "center"
								},
								tooltip : {
									trigger : 'axis'
								},
								xAxis : {
									type : 'value'
								},
								yAxis : {
									data : xBar
								},
								legend : {
									data : legendData,
									left : 'left'
								},
								series : seriesDataBar
							};
							myChartBar.setOption(optionBar);

							// 折线图
							var xLine = [];
							var yLinePC = [];
							var yLineWAP = [];

							for ( var i in dataLinePC) {
								xLine.push(dataLinePC[i].x);
								yLinePC.push(dataLinePC[i].y);
							}

							if (wapLineList.length > 0) {
								for ( var i in wapLineList) {
									yLineWAP.push(wapLineList[i].y);
								}
							}

							// 获取2个数组的最大值
							var maxyLinePC = Math.max.apply(Math, yLinePC);
							var maxyLineWAP = Math.max.apply(Math, yLineWAP);

							var numM = [ maxyLinePC, maxyLineWAP ];
							var maxY = Math.max.apply(Math, numM);

							var name1 = PCWAP ? "PC" : "";
							var name2 = PCWAP ? "WAP" : "";

							var seriesDataLine = [ {
								name : name1,
								type : 'line',
								data : yLinePC
							}, {
								name : name2,
								type : 'line',
								data : yLineWAP
							} ];

							if (!PCWAP) {
								seriesDataLine = [ {
									name : "花粉论坛",
									type : 'line',
									data : yLinePC
								} ];
							}
							// 阵地经营-折线图
							var optionLine = {
								tooltip : {
									trigger : 'axis'
								},
								xAxis : {
									data : xLine,
								},
								yAxis : {
									type : 'value'
								},
								grid : {
									left : 100
								},
								legend : {
									data : legendData,
									left : 'left'
								},
								dataZoom : {
									type : 'slider',
								},
								calculable : true,
								series : seriesDataLine
							};

							// 判断折线图的grid居左距离
							if (maxY.toString().length > 12) {
								optionLine.grid.left = 120;
							} else if (maxY.toString().length > 9) {
								optionLine.grid.left = 100;
							} else if (maxY.toString().length > 6) {
								optionLine.grid.left = 80;
							}

							myChartLine.setOption(optionLine);

						}
					};
					return BarService;
				})
		.factory(
				'wordCloud',
				function() {
					var BarService = Object.create(BaseService);
					BarService.createChart = function(wordcloudId, data) {
						var myChartWordCloud = this.getMychart(wordcloudId);

						function createRandomItemStyle() {
							return {
								normal : {
									color : 'rgb('
											+ [
													Math
															.round(Math
																	.random() * 160),
													Math
															.round(Math
																	.random() * 160),
													Math
															.round(Math
																	.random() * 160) ]
													.join(',') + ')'
								}
							};
						}
						var wordcloudData = data.datas[0].words;
						var tempDataList = [];

						for ( var i in wordcloudData) {
							var tempObject = {
								name : '',
								value : '',
								itemStyle : ''
							};
							tempObject.name = wordcloudData[i].name;
							tempObject.value = wordcloudData[i].value;
							tempObject.itemStyle = createRandomItemStyle();
							tempDataList.push(tempObject);
						}

						var option = {
							title : {
								text : data.datas[0].brandName
							},
							tooltip : {
								show : false,
								trigger : 'item'
							},
							series : [ {
								name : data.datas[0].brandName,
								type : 'wordCloud',
								size : [ '80%', '80%' ],
								textRotation : [ 0, 45, 90, -45 ],
								textPadding : 0,
								autoSize : {
									enable : true,
									minSize : 5
								},
								data : tempDataList
							} ]
						};
						myChartWordCloud.setOption(option);

					};
					return BarService;
				})
		.factory(
				'line',
				function() {
					var BarService = Object.create(BaseService);
					BarService.createChart = function(idLineTrend, idLineCTR,
							dataLineTrend, dataLineCTR, lineTitle) {
						var myChartLineTrend = this.getMychart(idLineTrend);
						var myChartLineCTR = this.getMychart(idLineCTR);

						var exposure_uv = []; // 曝光uv
						var click_uv = []; // 点击uv
						var ctr = []; //

						var xData = []
						var seriesData = [];

						if (dataLineTrend && dataLineTrend.length > 0) {
							for ( var i in dataLineTrend) {
								xData.push(dataLineTrend[i].pt_d);
								exposure_uv.push(dataLineTrend[i].exposure_uv);
								click_uv.push(dataLineTrend[i].click_uv);
								ctr.push(dataLineTrend[i].ctr);
							}

							seriesData = [ {
								name : "曝光uv",
								type : 'line',
								data : exposure_uv
							}, {
								name : "点击uv",
								type : 'line',
								data : click_uv
							}, {
								name : "CTR",
								type : 'line',
								data : ctr
							} ];

							var d = new Date();
							var nowyear = d.getFullYear();

							// 获取三个数组的最大值
							var maxExposure_uv = Math.max.apply(Math,
									exposure_uv);
							var maxClick_uv = Math.max.apply(Math, click_uv);
							var maxCtr = Math.max.apply(Math, ctr);

							var numM = [ maxExposure_uv, maxClick_uv, maxCtr ];
							var maxY = Math.max.apply(Math, numM);

							// 流量经营-折线图
							var optionLineTrend = {
								title : {
									text : nowyear + "年累计至今",
									left : "center"
								},
								tooltip : {
									trigger : 'axis'
								},
								grid : {
									left : 100
								},
								xAxis : {
									data : xData,
								},
								yAxis : {
									type : 'value'
								},
								dataZoom : {
									type : 'slider',
								},
								calculable : true,
								series : seriesData
							};

							// 判断折线图的grid居左距离
							if (maxY.toString().length > 12) {
								optionLineTrend.grid.left = 120;
							} else if (maxY.toString().length > 9) {
								optionLineTrend.grid.left = 100;
							} else if (maxY.toString().length > 6) {
								optionLineTrend.grid.left = 80;
							}

							myChartLineTrend.setOption(optionLineTrend);
						} else {
							myChartLineTrend.clear();
							myChartLineTrend.showLoading({
								text : "暂无数据",
								y : 300,
							});
						}

						var roi = [];
						var xDataROI = []

						if (dataLineCTR && dataLineCTR.length > 0) {
							for ( var i in dataLineCTR) {
								xDataROI.push(dataLineCTR[i].pt_d);
								roi.push(dataLineCTR[i].roi);
							}

							// 流量经营-折线图
							var optionLineROI = {
								title : {
									text : lineTitle,
									left : "center"
								},
								tooltip : {
									trigger : 'axis'
								},
								xAxis : {
									data : xData,
								},
								yAxis : {
									type : 'value'
								},
								dataZoom : {
									type : 'slider',
								},
								calculable : true,
								series : [ {
									name : "ROI",
									type : 'line',
									data : roi
								} ]
							};
							myChartLineCTR.setOption(optionLineROI);
						} else {
							myChartLineCTR.clear();
							myChartLineCTR.showLoading({
								text : "暂无数据",
								y : 300,
							});
						}
						// return myChart;
					};
					return BarService;
				})
		.factory(
				'barCrowd',
				function() {
					var BarService = Object.create(BaseService);
					BarService.createChart = function(idBar1, idBar2, idBar3,
							resp) {
						var myChartBar1 = this.getMychart(idBar1);
						var myChartBar2 = this.getMychart(idBar2);
						var myChartBar3 = this.getMychart(idBar3);

						var honorData = resp.personAnalysisData;
						var honorPlayData = resp.personAnalysisData1;
						if (!honorData) {
							myChartBar1.clear();
							myChartBar1.showLoading({
								text : "暂无数据",
								y : 300,
							});
							myChartBar2.clear();
							myChartBar2.showLoading({
								text : "暂无数据",
								y : 300,
							});
							myChartBar3.clear();
							myChartBar3.showLoading({
								text : "暂无数据",
								y : 300,
							});
							return;
						}

						// 荣耀的数据
						var honorDataSex;
						var honorDataAge;
						var honorDataWork;
						for ( var i in honorData.QuestionFilter) {
							if (honorData.QuestionFilter[i].title.indexOf("性别") >= 0) {
								honorDataSex = honorData.QuestionFilter[i].answerfilters;
							} else if (honorData.QuestionFilter[i].title
									.indexOf("年龄") >= 0) {
								honorDataAge = honorData.QuestionFilter[i].answerfilters;
							} else if (honorData.QuestionFilter[i].title
									.indexOf("职业") >= 0) {
								honorDataWork = honorData.QuestionFilter[i].answerfilters;
							}
						}

						// 荣耀畅玩的数据
						var honorPlaySex;
						var honorPlayAge;
						var honorPlayWork;
						for ( var i in honorPlayData.QuestionFilter) {
							if (honorPlayData.QuestionFilter[i].title
									.indexOf("性别") >= 0) {
								honorPlaySex = honorPlayData.QuestionFilter[i].answerfilters;
							} else if (honorPlayData.QuestionFilter[i].title
									.indexOf("年龄") >= 0) {
								honorPlayAge = honorPlayData.QuestionFilter[i].answerfilters;
							} else if (honorPlayData.QuestionFilter[i].title
									.indexOf("职业") >= 0) {
								honorPlayWork = honorPlayData.QuestionFilter[i].answerfilters;
							}
						}

						// work
						var legendDataWork = [];
						var workhonorData = [];
						var sumHonorWork = 0;
						for ( var i in honorDataWork) {
							legendDataWork.push(honorDataWork[i].answerText);
							workhonorData.push(honorDataWork[i].answerNum);
							sumHonorWork = sumHonorWork
									+ honorDataWork[i].answerNum;
						}

						var honorPlayDataW = [];
						var sumPlayWork = 0;
						for ( var i in honorPlayWork) {
							honorPlayDataW.push(honorPlayWork[i].answerNum);
							sumPlayWork = sumPlayWork
									+ honorPlayWork[i].answerNum;
						}

						var perHonorW = [];
						for ( var i in workhonorData) {
							perHonorW
									.push((workhonorData[i] / sumHonorWork * 100)
											.toFixed(2));
						}

						var perPlayW = [];
						for ( var i in honorPlayDataW) {
							perPlayW
									.push((honorPlayDataW[i] / sumPlayWork * 100)
											.toFixed(2));
						}

						var legendSub = [];
						var seriesWork = [];
						for ( var i in legendDataWork) {
							var dor = "";
							if (legendDataWork[i].length > 3) {
								dor = "..."
							}
							var name = legendDataWork[i].substring(0, 3) + dor;
							seriesWork.push({
								name : name,
								type : 'bar',
								barCategoryGap : '45%',
								data : [ perHonorW[i], perPlayW[i] ],
								stack : "荣耀"
							});
							legendSub.push(name);
						}

						var optionBar1 = {
							title : {
								text : "职业",
								left : "center"
							},
							legend : {
								data : legendSub,
								orient : 'vertical',
								top : 'center',
								align : 'left',
								left : 'right',
								itemGap : 15,
								itemHeight : 10
							},
							tooltip : {
								trigger : 'axis',
								formatter : function(params) {
									var toltipStr = '';
									for ( var i in params) {
										// var name = params[i].seriesName
										// .substring(0, 3);
										// for ( var i in legendDataWork) {
										// if (legendDataWork[i].indexOf(name)
										// >= 0) {
										var value = params[i].data;
										if (isNaN(value)) {
											value = 0;
										}
										var name = legendDataWork[i];
										toltipStr = "</br>" + name + "："
												+ value + "%" + toltipStr;
										// }

										// }
									}
									return params[0].name + toltipStr;
								}
							},
							color : [ '#4573A7', '#A94543', '#89A54E',
									'#6E5C8E', '#4098B0', '#D58442' ],
							grid : {
								show : false,
								borderWidth : 0,
								left : '15%',
								right : '20%'
							},
							xAxis : [ {
								type : 'category',
								data : [ '荣耀', '荣耀畅玩' ],
								splitLine : {
									show : false
								},
								axisTick : {
									show : false
								}
							} ],
							yAxis : {
								type : 'value',
								axisTick : {
									show : false
								},
								max : 120,
								axisLabel : {
									show : true,
									interval : 'auto',
									formatter : function(value) {
										if (value == 0) {
											return value;
										}
										if (value > 100) {
											return "  ";
										}
										return value + "%";
									}
								}
							},
							series : seriesWork
						};
						myChartBar1.setOption(optionBar1);

						var honorSum = honorDataSex[0].answerNum
								+ honorDataSex[1].answerNum;
						var playSum = honorPlaySex[0].answerNum
								+ honorPlaySex[1].answerNum;

						var honorM = (honorDataSex[0].answerNum / honorSum * 100)
								.toFixed(2);
						var honorF = (100 - honorM).toFixed(2);
						var honorPlayM = (honorPlaySex[0].answerNum / playSum * 100)
								.toFixed(2);
						var honorPlayF = (100 - honorPlayM).toFixed(2);
						// 男女比例
						var optionBar2 = {
							legend : {
								data : [ '男性', '女性' ]
							},
							tooltip : {
								trigger : 'axis',
								formatter : function(params) {
									var toltipStr = "";
									for ( var i in params) {
										var value = params[i].value;
										if (isNaN(value)) {
											value = 0;
										}
										toltipStr += "</br>"
												+ params[i].seriesName + "："
												+ value + "%";
									}
									return params[0].name + toltipStr;
								}
							},
							color : [ '#8DB7E7', '#E7B9B9' ],
							grid : {
								show : false,
								borderWidth : 0,
								left : '3%',
								right : '3%'
							// bottom: '5%',
							// containLabel: true
							},
							xAxis : [ {
								type : 'category',
								data : [ '荣耀', '荣耀畅玩' ],
								splitLine : {
									show : false
								},
								axisTick : {
									show : false
								}
							} ],
							yAxis : {
								type : 'value',
								axisTick : {
									show : false
								},
								axisLine : {
									show : false
								},
								splitLine : {
									show : false
								},
								axisLabel : {
									show : false
								}
							},
							series : [ {
								name : '男性',
								type : 'bar',
								barCategoryGap : '30%',
								data : [ {
									value : honorM,
									label : {
										normal : {
											show : true,
											position : 'top',
											textStyle : {
												color : '#000'
											},
											formatter : function(params) {
												return params.value + "%";
											}
										}
									}
								}, {
									value : honorPlayM,
									label : {
										normal : {
											show : true,
											position : 'top',
											textStyle : {
												color : '#000'
											},
											formatter : function(params) {
												return params.value + "%";
											}
										}
									}
								} ]
							}, {
								name : '女性',
								type : 'bar',
								barCategoryGap : '30%',
								data : [ {
									value : honorF,
									label : {
										normal : {
											show : true,
											position : 'top',
											textStyle : {
												color : '#000'
											},
											formatter : function(params) {
												return params.value + "%";
											}
										}
									}
								}, {
									value : honorPlayF,
									label : {
										normal : {
											show : true,
											position : 'top',
											textStyle : {
												color : '#000'
											},
											formatter : function(params) {
												return params.value + "%";
											}
										}
									}
								} ]
							} ]
						};
						myChartBar2.setOption(optionBar2);

						// 年龄
						var legendDataAge = [];
						var agehonorData = [];
						var sumHonor = 0;
						for ( var i in honorDataAge) {
							legendDataAge.push(honorDataAge[i].answerText);
							agehonorData.push(honorDataAge[i].answerNum);
							sumHonor = sumHonor + honorDataAge[i].answerNum;
						}

						var honorPlayData = [];
						var sumPlay = 0;
						for ( var i in honorPlayAge) {
							honorPlayData.push(honorPlayAge[i].answerNum);
							sumPlay = sumPlay + honorPlayAge[i].answerNum;
						}

						var perHonor = [];
						for ( var i in agehonorData) {
							perHonor.push((agehonorData[i] / sumHonor * 100)
									.toFixed(2));
						}

						var perPlay = [];
						for ( var i in honorPlayData) {
							perPlay.push((honorPlayData[i] / sumPlay * 100)
									.toFixed(2));
						}

						var seriesAge = [];
						for ( var i in legendDataAge) {
							seriesAge.push({
								name : legendDataAge[i],
								type : 'bar',
								barCategoryGap : '45%',
								data : [ perHonor[i], perPlay[i] ],
								stack : "荣耀"
							});
						}

						var optionBar3 = {
							title : {
								text : "年龄",
								left : "center"
							},
							legend : {
								data : legendDataAge,
								orient : 'vertical',
								top : 'center',
								align : 'left',
								left : 'right',
								itemGap : 15,
								itemHeight : 10
							},
							tooltip : {
								trigger : 'axis',
								formatter : function(params) {
									var toltipStr = "";
									for ( var i in params) {
										var value = params[i].data;
										if (isNaN(value)) {
											value = 0;
										}
										toltipStr = "</br>"
												+ params[i].seriesName + "："
												+ value + "%" + toltipStr;
									}
									return params[0].name + toltipStr;
								}
							},
							color : [ '#C1232B', '#B5C334', '#FCCE10',
									'#E87C25', '#27727B', '#FE8463', '#9BCA63',
									'#FAD860', '#F3A43B', '#60C0DD', '#D7504B',
									'#C6E579', '#F4E001', '#F0805A', '#26C0C0' ],
							grid : {
								show : false,
								borderWidth : 0,
								left : '15%',
								right : '20%'
							},
							xAxis : [ {
								type : 'category',
								data : [ '荣耀', '荣耀畅玩' ],
								splitLine : {
									show : false
								},
								axisTick : {
									show : false
								}
							} ],
							yAxis : {
								type : 'value',
								axisTick : {
									show : false
								},
								max : 120,
								axisLabel : {
									show : true,
									interval : 'auto',
									formatter : function(value) {
										if (value == 0) {
											return value;
										}
										if (value > 100) {
											return "  ";
										}
										return value + "%";
									}
								}
							},
							series : seriesAge
						};
						myChartBar3.setOption(optionBar3);
					};
					return BarService;
				});

angular.module('appRongyao').factory('websitesListData', function() {
	return {
		name : {}
	};
});

angular.module('appRongyao').filter('formatnumber', function() {
	return function(input) {
		if (input == 0 || input == null || isNaN(input))
			return '--';

		var ii = parseFloat(input);
		var s = "";
		if (ii.toString().indexOf(".") >= 0) {
			s = parseFloat(input).toFixed(2) + ""; // 数字转换为字符串 .toFixed(2)
			// 因为有的地方input从后台传到前台是字符串，有*.*0格式，所以转换一下
		} else {
			s = parseFloat(input) + ""; // 数字转换为字符串 .toFixed(2)
			// 因为有的地方input从后台传到前台是字符串，有*.*0格式，所以转换一下
		}

		return s.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	};
}).filter('formatper', function() {
	return function(input) {
		if (input == 0 || input == null || isNaN(input))
			return '--';

		var ii = parseFloat(input);
		if (ii < 0.000001) {
			return ii.toFixed(8);
		} else if (ii < 0.0001) {
			return ii.toFixed(6);
		} else if (ii < 0.01) {
			return ii.toFixed(4);
		} else {
			return ii.toFixed(2);
		}
	};
}).filter(
		"reportFilter",
		function() {
			return function(input, curpage, pageSize, searchtxt) {
				if (!input.data) {
					return [];
				}

				var array = [];
				if (searchtxt != input.lastsearchtxt) {
					input.curpage = 1;
				}

				input.lastsearchtxt = searchtxt;
				if (searchtxt) {
					for (var i = 0; i < input.data.length; i++) {
						var obj = input.data[i];
						var sss = obj.reportTitle + "";
						var creator = obj.reportCreator + "";
						var owner = obj.reportOwner + "";
						if (sss.indexOf(searchtxt) > -1
								|| creator.indexOf(searchtxt) > -1
								|| owner.indexOf(searchtxt) > -1) {
							array.push(obj);
						}
					}
				} else {
					array = input.data;
				}

				var array2 = util(input, curpage, pageSize, array);
				return array2;
			};
		});

var util = function(input, curpage, pageSize, array) {
	var array2 = [];
	var startindex = (curpage - 1) * pageSize;
	var endindex = curpage * pageSize - 1;

	for (var j = startindex; j <= endindex; j++) {
		if (j < array.length) {
			var obj = array[j];
			array2.push(obj);
		}
	}

	var lastrecodenum = array.length % pageSize;
	var page = parseInt(array.length / pageSize);
	var pages = 0;
	if (lastrecodenum == 0) {
		pages = page;
	} else {
		pages = page + 1;
	}
	input.totalPage = 0;
	input.showPage = [];

	if (curpage <= 2) {
		if (pages >= 5) {
			for (var p = 1; p <= 5; p++) {
				input.showPage.push(p);
			}
		} else {
			for (var p = 1; p <= pages; p++) {
				input.showPage.push(p);
			}
		}
	}
	if (curpage >= 3) {
		var endpage = curpage + 2;
		if (endpage <= pages) {
			for (var p = curpage - 2; p <= curpage; p++) {
				input.showPage.push(p);
			}
			for (var p = curpage + 1; p <= endpage; p++) {
				input.showPage.push(p);
			}
		} else {
			for (var p = pages - 4; p <= pages; p++) {
				if (p > 0) {
					input.showPage.push(p);
				}
			}
		}
	}

	input.totalPage = pages;

	return array2;
};
