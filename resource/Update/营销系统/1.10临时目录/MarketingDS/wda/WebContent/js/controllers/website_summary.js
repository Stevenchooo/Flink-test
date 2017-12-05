/**
 * 网站概况
 */
app.controller('WebsiteSummaryCtrl', [
		'$scope',
		'$rootScope',
		'$http',
		'$filter',
		'$stateParams',
		'$localStorage',
		'line','pie','bar','map',
		function($scope, $rootScope, $http, $filter, $stateParams, $localStorage, line, pie, bar,map) {
			
			$scope.userKey = __userKey;
			var periodDataSet = {
					"0" : [ 0, 0 ],
					"-1" : [ -1, -1 ],
					"-7" : [ -6, 0 ],
					"-30" : [ -29, 0 ]
				};
			var timeDim = {
					1 : "小时",
					2 : "天",
					3 : "周",
					4: "月"
			};
			
			$scope.sumHttp = 0;
			$scope.totalHttp = 5;
			$("#load-me").show();
//			var timer = null;
//			$(window).resize(function() {// 窗口自适应
//				if (timer) {
//					return;
//				}
//				timer = setTimeout(function() {
////					if (gain.chartTab[gain.tab] instanceof Object) {
//					var obj = [bar.getMychart("websiteBar"),line.getMychart("websiteSum"),pie.getMychart("websitePie"),pie.getMychart("websitePieRef"),map.getMychart("websiteMapBar")];
//						for ( var i in obj) {
////							var id = obj[i];
////							if (gain.myChart[id] && gain.myChart[id].echartsIns) {
//								try {
//									obj[i].echartsIns.resize();
//								} catch (e) {
//
//								}
////							}
//						}
////					}
//					timer = null;
//				}, 1000);
//			});
			
			var timeFormatter = "YYYY-MM-DD";
			var dateFilter = $filter('date');
			var theCurDate = moment(dateFilter(new Date(), "yyyy-MM-dd"));
			//获取站点sid
			$scope.sid = $rootScope.sid || $localStorage.sid;
			
//			setTimeout(function() {
//				window.onresize = function() {
//					echarts.init(document.getElementById('websiteSum')).echartsIns.resize();
//					echarts.init(document.getElementById('websitePie')).echartsIns.resize();
//				}
//			}, 200);
			
			//缺省显示 浏览量
			$scope.curKPI = "pv";
			
			//控制时间维度按钮是否可用
			$scope.period = 0;
			
			//时间维度
			$scope.timeDim = 0;
			
			//控制环比按钮是否可用
			$scope.isDisabled = false;
			
			//报表曲线下面的复选框的值
			$scope.selectedHuanbiItem = 0;
			
			//初始化checkbox list
			$scope.entities = [{
			      name: '前一天',
			      checked: true
			    }, {
			      name: '上周同期',
			      checked: false
			    }
			];
			//初始化事件控件的值
			$scope.datePicker = {
					date : {
						startDate : theCurDate.clone(),
						endDate : theCurDate.clone()
					}
			};
			
			var getPeriodTime = function() {
				var pick = "date";
				
				var startDate = $scope.datePicker[pick].startDate.format(timeFormatter);
				var endDate = $scope.datePicker[pick].endDate.format(timeFormatter);
				if(startDate == endDate){
					$scope.isDisabled = false;
				}else{
					$scope.isDisabled = true;
				}
				return startDate + "#" + endDate;
			};
			
			var resetDatePick = function(period) {
				var perO = periodDataSet[period];
				if (!perO) {
					return;
				}
				$scope.datePicker.date.startDate = theCurDate.clone();
				$scope.datePicker.date.endDate = theCurDate.clone();
				var perS = perO[0];
				if (perS) {
					$scope.datePicker.date.startDate.subtract(0 - perS, "days");
				}
				var perE = perO[1];
				if (perE) {
					$scope.datePicker.date.endDate.subtract(0 - perE, "days");
				}
			};
			
			var getPeriodByDatePick = function() {
				var dateDiff = [
						$scope.datePicker.date.startDate.diff(theCurDate, "days"),
						$scope.datePicker.date.endDate.diff(theCurDate, "days") ];
				for ( var i in periodDataSet) {
					if (periodDataSet[i].toString() == dateDiff.toString()) {
						return i;
					}
				}
				// 这里的1是没有意义的，完全是为了去掉页面上的样式
				return 1;
			};
			
			
			//请求网站概况汇数据		
			$http({
				method: 'GET',
				url: __urlRoot + '/wda/api/wda/website/summary',
				params: {
					__userKey : __userKey,
					site_id : $scope.sid
				}
			}).success(function(resp, status, headers, config) {
				$scope.sumHttp ++;
				if($scope.sumHttp ==$scope.totalHttp ){
					$("#load-me").hide();
				}
			    if(resp.resultCode == 0) {		    	
			    	//今日此时
			    	$scope.c_data = resp.c_data;
			    	//昨日
			    	$scope.y_data = resp.y_data;
			    	//今日预测
			    	//昨日此时
			    	$scope.y_c_data = resp.y_c_data;
			    	//平均
			    	$scope.a_data = resp.a_data;
			    	//最大值
			    	$scope.m_data = resp.m_data;			    	
			    }
			  })
			  .error(function(resp, status, headers, config) {
				  $scope.sumHttp ++;
					if($scope.sumHttp ==$scope.totalHttp){
						$("#load-me").hide();
					}
					if(!$("#errorHttp").is(":visible")){
						$("#errorHttp").show();
					}
			 });
			
			// 选择时间周期
			$scope.selectTimePeriod = function(period) {
				if(period==$scope.period){
					return;
				}
				$scope.sumHttp = 0;
				$scope.totalHttp =4;
				$("#load-me").show();
				//控制时间周期按钮样式
				$scope.period = period;
				
				//设置报表曲线下面的复选框是否可用
				if(period == 0 || period == -1) {
					$scope.isDisabled = false;
				} else {
					$scope.isDisabled = true;
				}
				// 重置第一个时间框时间
				resetDatePick(period);
			};
			
			$scope.timePickerChange = function(){
				$scope.sumHttp = 0;
				$scope.totalHttp =4;
				$("#load-me").show();
				
				// 样式控制
				$scope.period = getPeriodByDatePick();
				
				//查询kpi曲线数据
				fetchKpiData();
				
				//查询 访问来源top10
			    fetchTopReferrer();
			    
			    //查询受访页面top10
			    fetchTopPages();
			    
			    //查询地域分布
			    fetchTopAreaDistribution();
			};
			
			// 选择kpi统计项
			$scope.selectKpiItem = function(kpiItem) {
				$scope.sumHttp = 0;
				$scope.totalHttp =1;
				$("#load-me").show();
				$scope.curKPI = kpiItem;
				fetchKpiData();
			};
			
			// 选择周期对比
			$scope.selectContrast = function(position, checked, entities) {
				
				$scope.sumHttp = 0;
				$scope.totalHttp =1;
				$("#load-me").show();
				
				//控制checkbox状态
				angular.forEach(entities, function(subscription, index) {
					if (position != index) {
						subscription.checked = false;
				    }
				});
				
				if(checked) {
					$scope.selectedHuanbiItem = position;
				} else {
					$scope.selectedHuanbiItem = -1;
				}
				
				fetchKpiData();
			};
			
		    //查询kpi数据
		    fetchKpiData();
		    
		    //查询 访问来源top10
		    fetchTopReferrer();
		    
		    //查询受访页面top10
		    fetchTopPages();
		    
		    //查询地域分布
		    fetchTopAreaDistribution();
		        
		    function fetchKpiData() {
		    	$http({
					method: 'GET',
					url: __urlRoot + '/wda/api/wda/website/summary/kpi',
					params: {
						__userKey : __userKey,
						site_id : $scope.sid,
						periodTime : getPeriodTime(),
						curKpi : $scope.curKPI,
						huanbi : $scope.selectedHuanbiItem
					}
				}).success(function(resp, status, headers, config) {
					$scope.sumHttp ++;
					if($scope.sumHttp ==$scope.totalHttp ){
						$("#load-me").hide();
					}
				    if(resp.resultCode == 0) {
//				    	$scope.kpi_data = resp.data;
				    	line.createChart('websiteSum', resp);
				    }
				})
				  .error(function(resp, status, headers, config) {
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
		    
		    //查询访问来源top
		    function fetchTopReferrer() {
		    	$http({
					method: 'GET',
					url: __urlRoot + '/wda/api/wda/website/summary/top_referrer',
					params: {
						__userKey : __userKey,
						site_id : $scope.sid,
						periodTime : getPeriodTime()
					}
				}).success(function(resp, status, headers, config) {
					$scope.sumHttp ++;
					if($scope.sumHttp ==$scope.totalHttp ){
						$("#load-me").hide();
					}
				    if(resp.resultCode == 0) {
				        $scope.top_referrer_data = resp.data;
				        $scope.totalPv = resp.totalPv;
				        var pieData = resp.top_referrer_pie_data;
				        pie.createChart('websitePie', pieData,'');					    
				    }
				})
				  .error(function(resp, status, headers, config) {
					  $scope.sumHttp ++;
						if($scope.sumHttp ==$scope.totalHttp){
							$("#load-me").hide();
						}
						if(!$("#errorHttp").is(":visible")){
							$("#errorHttp").show();
						}
				});
		    };
		    
		    //查询受访页面top
		    function fetchTopPages() {
		    	$http({
					method: 'GET',
					url: __urlRoot + '/wda/api/wda/website/summary/top_pages',
					params: {
						__userKey : __userKey,
						site_id : $scope.sid,
						periodTime : getPeriodTime()
					}
				}).success(function(resp, status, headers, config) {
					$scope.sumHttp ++;
					if($scope.sumHttp ==$scope.totalHttp ){
						$("#load-me").hide();
					}
				    if(resp.resultCode == 0) {
				        $scope.top_pages_data = resp.data;
				        $scope.totalPagesPv = resp.totalPv;
				        var pieData = resp.top_pages_pie_data;
				        pie.createChart('websitePieRef', pieData,'', true);
				    }
				})
				  .error(function(resp, status, headers, config) {
					  $scope.sumHttp ++;
						if($scope.sumHttp ==$scope.totalHttp){
							$("#load-me").hide();
						}
						if(!$("#errorHttp").is(":visible")){
							$("#errorHttp").show();
						}
				});
		    };
		    
		    function fetchTopAreaDistribution() {
		    	$http({
					method: 'GET',
					url: __urlRoot + '/wda/api/wda/website/summary/top_area_distribution',
					params: {
						__userKey : __userKey,
						site_id : $scope.sid,
						periodTime : getPeriodTime()
					}
				}).success(function(resp, status, headers, config) {
					$scope.sumHttp ++;
					if($scope.sumHttp ==$scope.totalHttp ){
						$("#load-me").hide();
					}
				    if(resp.resultCode == 0) {
//				    	$scope.top_area_distribution_data =  resp.data; //bar
				        $scope.top_area_v_bar_data = resp.top_area_v_bar_data; //map
						
						var barData = resp.top_area_v_bar_data[0];
						bar.createChart('websiteBar', barData);
						map.createChart('websiteMapBar', resp.data);
						
				    }
				})
				  .error(function(resp, status, headers, config) {
					  $scope.sumHttp ++;
						if($scope.sumHttp ==$scope.totalHttp){
							$("#load-me").hide();
						}
						if(!$("#errorHttp").is(":visible")){
							$("#errorHttp").show();
						}
				});
		    };
		    
		 // 窗口关闭事件
			$(".upp-close").click(function() {
				$(this).parents(".up-mask").fadeOut(500);
			});
		} ]);