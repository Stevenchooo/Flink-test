'use strict';

var PagesAnalyticsCtrl = ['$rootScope', '$http', '$location', '$state', '$filter', '$scope', '$stateParams',
		function($rootScope, $http, $location, $state, $filter, $scope, tabs) {
	        var periodDataSet = {
			"0" : [ 0, 0 ],
			"-1" : [ -1, -1 ],
			"-7" : [ -6, 0 ],
			"-30" : [ -29, 0 ]
		    };
	        var timeFormatter = "YYYY-MM-DD";
		    var dateFilter = $filter('date');
		    var theCurDate = moment(dateFilter(new Date(), "yyyy-MM-dd"));
		    var thePastDate = moment("1970-01-01");
			$scope.initialise = function() {
				$scope.tabData = [ {
					heading : '指标概况',
					route : 'app.pages.summary'
				}];
				
				/* 迭代2实现，后台数据已计算
				$scope.tabData = [ {
					heading : '指标概况',
					route : 'app.pages.summary'
				}, {
					heading : '页面价值分析',
					route : 'app.pages.value'
				}, {
					heading : '入口页分析',
					route : 'app.pages.landing'
				}, {
					heading : '退出页分析',
					route : 'app.pages.exit'
				} ];
				*/
			};

			$scope.initialise();
						
			//控制时间维度按钮是否为active
			$scope.period = 0;
			//控制页面上下游样式
			$scope.isActive =[ true, false, false ];
			
			//是否选择环比
			$scope.isHuanBi = false;
			
			//查看页面上下游弹窗是否展示
			$scope.isPageSum = false;
			
			$scope.separatedDays = 0;
			
			//加载页面初值
			$scope.datePicker = {
					date : {
						startDate : theCurDate.clone(),
						endDate : theCurDate.clone()
					},
					date1 : {
						startDate : thePastDate.clone(),
						endDate : thePastDate.clone()
					}
			};
			
			 $scope.getPeriodTime = function(isHuanbi) {
				var pick = "date";
				if (isHuanbi) {
					if (!$scope.isHuanBi) {
						return "";
					}
					pick = "date1";
				}
				var startDate = $scope.datePicker[pick].startDate.format(timeFormatter);
				var endDate = $scope.datePicker[pick].endDate.format(timeFormatter);
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
			//被子类调用，设置当前被选择的tab
			$scope.curSelectedTab = 1;
			$scope.modifyParentScope = function(index) {
			    $scope.curSelectedTab = index;
			};
			
			/*
			 * 选择 环比按钮
			 */
			$scope.huanbi = function() {
				
				if(!$scope.isHuanBi){
					$scope.datePicker.date1.startDate = thePastDate.clone();
					$scope.datePicker.date1.endDate = thePastDate.clone();
					//刷新 当前选中的tab页面，并传递时间周期、是否环比参数
					if($scope.curSelectedTab == 1) {
						//指标概况
						$state.go('app.pages.summary', {periodTime : $scope.getPeriodTime(), huanBiDate : $scope.getPeriodTime(true)});
					} else if($scope.curSelectedTab == 2) {
						//页面价值分析
						$state.go('app.pages.value', {periodTime : $scope.getPeriodTime(), huanBiDate : $scope.getPeriodTime(true)});
					} else if($scope.curSelectedTab == 3) {
						//入口页分析
						$state.go('app.pages.landing', {periodTime : $scope.getPeriodTime(), huanBiDate : $scope.getPeriodTime(true)});
					} else if($scope.curSelectedTab == 4) {
						//退出页分析
						$state.go('app.pages.exit', {periodTime : $scope.getPeriodTime(), huanBiDate : $scope.getPeriodTime(true)});
					} else {
						$state.go('app.pages.summary', {periodTime : $scope.getPeriodTime(), huanBiDate : $scope.getPeriodTime(true)});
					}
				}else{
					$scope.datePicker.date1.endDate = $scope.datePicker.date.startDate
					.clone().subtract(1, "days");
			        $scope.datePicker.date1.startDate = $scope.datePicker.date1.endDate
					.clone().subtract($scope.separatedDays, "days");
				}
				
			};
			
			
			/*
			 * 选择时间周期
			 */
			$scope.selectTimePeriod = function(period) {
				if(period==$scope.period){
					return;
				}
				//用于控制时间周期按钮样式，被选中的显示为蓝色背景
				$scope.period = period;
				$scope.isHuanBi = false;
				// 重置第一个时间框时间
				resetDatePick(period);
				$scope.datePicker.date1.startDate = thePastDate.clone();
				$scope.datePicker.date1.endDate = thePastDate.clone();
				
				
//				//刷新 当前选中的tab页面，并传递时间周期、是否环比参数
//				if($scope.curSelectedTab == 1) {
//					//指标概况
//					$state.go('app.pages.summary', {periodTime : $scope.periodTime, huanBiDate : $scope.huanBiDate});
//				} else if($scope.curSelectedTab == 2) {
//					//页面价值分析
//					$state.go('app.pages.value', {periodTime : $scope.periodTime, huanBiDate : $scope.huanBiDate});
//				} else if($scope.curSelectedTab == 3) {
//					//入口页分析
//					$state.go('app.pages.landing', {periodTime : $scope.periodTime, huanBiDate : $scope.huanBiDate});
//				} else if($scope.curSelectedTab == 4) {
//					//退出页分析
//					$state.go('app.pages.exit', {periodTime : $scope.periodTime, huanBiDate : $scope.huanBiDate});
//				} else {
//					$state.go('app.pages.summary', {periodTime : $scope.periodTime, huanBiDate : $scope.huanBiDate});
//				}
			};
			
			$scope.timePickerChange = function(){
				// 样式控制
				$scope.period = getPeriodByDatePick();
				$scope.separatedDays = $scope.datePicker.date.endDate.diff(
						$scope.datePicker.date.startDate, "days");
				
				if ($scope.isHuanBi) {
					// 第一个时间控件改变，则根据第一个控件的时间差，修改第二个控件的截止时间
					var temEnd = $scope.datePicker.date1.endDate.clone();
					$scope.datePicker.date1.endDate = $scope.datePicker.date1.startDate
							.clone().add($scope.separatedDays, "days");
					// 如果发生了改变，那么会执行环比时间框的change事件并进行请求数据
					if (temEnd.diff($scope.datePicker.date1.endDate, "days")) {
						return;
					}
				}
				
				//刷新 当前选中的tab页面，并传递时间周期、是否环比参数
				if($scope.curSelectedTab == 1) {
					//指标概况
					$state.go('app.pages.summary', {periodTime : $scope.getPeriodTime(), huanBiDate : $scope.getPeriodTime(true)});
				} else if($scope.curSelectedTab == 2) {
					//页面价值分析
					$state.go('app.pages.value', {periodTime : $scope.getPeriodTime(), huanBiDate : $scope.getPeriodTime(true)});
				} else if($scope.curSelectedTab == 3) {
					//入口页分析
					$state.go('app.pages.landing', {periodTime : $scope.getPeriodTime(), huanBiDate : $scope.getPeriodTime(true)});
				} else if($scope.curSelectedTab == 4) {
					//退出页分析
					$state.go('app.pages.exit', {periodTime : $scope.getPeriodTime(), huanBiDate : $scope.getPeriodTime(true)});
				} else {
					$state.go('app.pages.summary', {periodTime : $scope.getPeriodTime(), huanBiDate : $scope.getPeriodTime(true)});
				}
			};
			
			$scope.comparePeriodTimeSelect = function(){
				
				if($scope.isHuanBi){
					if($scope.separatedDays == $scope.datePicker.date1.endDate.diff(
							$scope.datePicker.date1.startDate, "days")){
						//刷新 当前选中的tab页面，并传递时间周期、是否环比参数
						if($scope.curSelectedTab == 1) {
							//指标概况
							$state.go('app.pages.summary', {periodTime : $scope.getPeriodTime(), huanBiDate : $scope.getPeriodTime(true)});
						} else if($scope.curSelectedTab == 2) {
							//页面价值分析
							$state.go('app.pages.value', {periodTime : $scope.getPeriodTime(), huanBiDate : $scope.getPeriodTime(true)});
						} else if($scope.curSelectedTab == 3) {
							//入口页分析
							$state.go('app.pages.landing', {periodTime : $scope.getPeriodTime(), huanBiDate : $scope.getPeriodTime(true)});
						} else if($scope.curSelectedTab == 4) {
							//退出页分析
							$state.go('app.pages.exit', {periodTime : $scope.getPeriodTime(), huanBiDate : $scope.getPeriodTime(true)});
						} else {
							$state.go('app.pages.summary', {periodTime : $scope.getPeriodTime(), huanBiDate : $scope.getPeriodTime(true)});
						}
					}else{
						$scope.datePicker.date1.endDate = $scope.datePicker.date1.startDate
						.clone().add($scope.separatedDays, "days");
					}
				
				}
			};
			
			/*
			 * 选择周期对比
			 */
			$scope.selectContrast = function(checked) {
				
				console.log('start selectContrast ' + checked);
				$scope.isHuanbi = checked;
				
				//刷新 当前选中的tab页面，并传递时间周期、是否环比参数
				if($scope.curSelectedTab == 1) {
					//指标概况
					$state.go('app.pages.summary', {period : $scope.period, isHuanbi : checked});
				} else if($scope.curSelectedTab == 2) {
					//页面价值分析
					$state.go('app.pages.value', {period : $scope.period, isHuanbi : checked});
				} else if($scope.curSelectedTab == 3) {
					//入口页分析
					$state.go('app.pages.landing', {period : $scope.period, isHuanbi : checked});
				} else if($scope.curSelectedTab == 4) {
					//退出页分析
					$state.go('app.pages.exit', {period : $scope.period, isHuanbi : checked});
				} else {
					$state.go('app.pages.summary', {period : $scope.period, isHuanbi : checked});
				}
				
			};
		} ];

angular.module('app').controller('PagesAnalyticsCtrl', PagesAnalyticsCtrl);
