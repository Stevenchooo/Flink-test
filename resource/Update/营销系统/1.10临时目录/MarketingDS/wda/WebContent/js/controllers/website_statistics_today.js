var websiteStatisticsTodayCtrlFunction = function($scope, $rootScope, $filter,
		$http, $stateParams, $localStorage,line) {
	
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
	var timeFormatter = "YYYY-MM-DD";
	var dateFilter = $filter('date');
	var theCurDate = moment(dateFilter(new Date(), "yyyy-MM-dd"));
	var thePastDate = moment("1970-01-01");
	$scope.sid = $rootScope.sid || $localStorage.sid;

	// 时间周期变量，同时用于控制前端时间周期按钮的样式
	// 默认为0，表示 今天
	$scope.period = 0;

	// 环比按钮变量，默认为false，表示不选中
	$scope.isHuanBi = false;
	$scope.separatedDays = 0;

	// 时间维度值，默认为1，表示按小时
	$scope.time_dim_value = 1;

	// 时间维度状态，默认按时、按天可用
	$scope.time_dim_status = [ true, true, false, false ];

	$scope.websitesTodayJson = {
		curpage : 1,
		itemcss : "un-item uni-cur",
		lastsearchtxt : '',
	};

	$scope.setCurPage = function(page) {
		if (page == $scope.websitesTodayJson.curpage) {
			return "pgcur";
		} else {
			return "";
		}
	};
	$scope.selectPage = function(page) {
		if (page <= $scope.websitesTodayJson.totalPage && page >= 1) {
			$scope.websitesTodayJson.curpage = page;
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
		key : '新访客比率',
		value : 'nuv_rate',
		checked : false
	}, {
		key : 'IP数',
		value : 'ip',
		checked : false
	}, {
		key : '平均访问时长',
		value : 'avg_visit_time',
		checked : false
	}, {
		key : '平均访问页面',
		value : 'avg_visit_pages',
		checked : false
	} ];

	// 选中的kpi
	$scope.data = [ $scope.allOptions[0] ];

	$scope.canSelectedKpi = 2;

	// 加载页面初值
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

	var getPeriodTime = function(isHuanbi) {
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
		var startDiff = $scope.datePicker.date.startDate.diff(theCurDate,
				"days");
		var endDiff = $scope.datePicker.date.endDate.diff(theCurDate, "days");
		var dateDiff = [ startDiff, endDiff ];

		for ( var i in periodDataSet) {
			if (periodDataSet[i].toString() == dateDiff.toString()) {
				return i;
			}
		}
		// 目前只有站点分析里面需要这个period == 2
		if (startDiff == endDiff) {
			// 这里的是控件单天的日期，主要是页面上用到period ==2和period ==1(2代表单天的日期但是又不是今天和昨天的那种)
			return 2;
		}
		// 这里的1是没有意义的，完全是为了去掉页面上的样式
		return 1;
	};
	
	$("#load-me").show();
	$scope.sumHttp = 0;
	$scope.totalHttp = 3;
	// 查询汇总数据
	fetchWebsiteTrendSummary();
	function fetchWebsiteTrendSummary() {

		$http({
			method : 'GET',
			url : __urlRoot + '/wda/api/wda/website/trend/summary',
			params : {
				__userKey : __userKey,
				site_id : $scope.sid,
				periodTime : getPeriodTime(),
				huanBiDate : getPeriodTime(true)
			}
		}).success(function(resp, status, headers, config) {
			if (resp.resultCode == 0) {
				$scope.sumHttp ++;
				if($scope.sumHttp ==$scope.totalHttp){
					$("#load-me").hide();
				}
				$scope.summaryData = resp.data;
				$scope.huanBiSummaryData = resp.hb_data;
			}
		}).error(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp){
				$("#load-me").hide();
			}
			if(!$("#errorHttp").is(":visible")){
				$("#errorHttp").show();
			}
		});
	}

	fetchKpiData();

	/*
	 * 查询kpi值
	 */
	function fetchKpiData() {
		console.log($scope.data[0].key);
		$http({
			method : 'GET',
			url : __urlRoot + '/wda/api/wda/website/trend/kpi',
			params : {
				__userKey : __userKey,
				huanBiDate : getPeriodTime(true),
				site_id : $scope.sid,
				periodTime : getPeriodTime(),
				time_dim : $scope.time_dim_value,
				curKpi1 : $scope.data[0].value,
				curKpi2 : ($scope.data.length == 2 ? $scope.data[1].value : '')
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp ){
				$("#load-me").hide();
			}
			if (resp.resultCode == 0) {
				line.createChart('todayKpi', resp);
			}
		}).error(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp ){
				$("#load-me").hide();
			}
			if(!$("#errorHttp").is(":visible")){
				$("#errorHttp").show();
			}
			return [];
		});
	}

	fetchWebsiteTrendList();
	function fetchWebsiteTrendList() {

		$http({
			method : 'GET',
			url : __urlRoot + '/wda/api/wda/website/trend/list',
			params : {
				__userKey : __userKey,
				huanBiDate : getPeriodTime(true),
				site_id : $scope.sid,
				periodTime : getPeriodTime(),
				time_dim : $scope.time_dim_value
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp ){
				$("#load-me").hide();
			}
			if (resp.resultCode == 0) {
				$scope.websitesTodayJson.curpage = 1;
				$scope.websitesTodayJson.data = resp.data;
			}
		}).error(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp ){
				$("#load-me").hide();
			}
			if(!$("#errorHttp").is(":visible")){
				$("#errorHttp").show();
			}
		});
	};

	/*
	 * 选择时间周期 按钮事件，调用后台程序查询数据，并刷新前台页面 params: period: 当前时间周期
	 */
	$scope.selectTimePeriod = function(period) {
		if(period==$scope.period){
			return;
		}
		
		$scope.sumHttp = 0;
		$scope.totalHttp =3;
		$("#load-me").show();
		// 前台页面 控制逻辑
		// // 控制时间周期按钮样式
		$scope.period = period;
		$scope.isHuanBi = false;
		// //设置时间维度按钮是否可用
		// //选择今天和昨天时， 按时、按日 可用
		// //选择最近7天时， 按时、按日、按周可用
		// //选择最近30天时，按时、按日、按周、按月 可用
		// //! 大时间周期选择大时间维度
		// 状态下，如果再切换到小时间周期，则时间维度自动回退到可用的下一级状态
		if (period == 0) {
			$scope.time_dim_value = 1;
			$scope.time_dim_status = [ true, true, false, false ];
		} else if (period == -1) {
			$scope.time_dim_value = 1;
			$scope.time_dim_status = [ true, true, false, false ];
		} else if (period == -7) {
			$scope.time_dim_value = 2;
			$scope.time_dim_status = [ true, true, false, false ];
		} else if (period == -30) {
			$scope.time_dim_value = 2;
			$scope.time_dim_status = [ true, true, true, false ];
		}
		// 重置第一个时间框时间
		resetDatePick(period);
		$scope.datePicker.date1.startDate = thePastDate.clone();
		$scope.datePicker.date1.endDate = thePastDate.clone();
	};
	// 前一个时间控件改变事件
	$scope.timePickerChange = function() {
		
		$scope.sumHttp = 0;
		$scope.totalHttp = 3;
		$("#load-me").show();
		
		// 样式控制
		$scope.period = getPeriodByDatePick();
		$scope.separatedDays = $scope.datePicker.date.endDate.diff(
				$scope.datePicker.date.startDate, "days");
		if ($scope.separatedDays) {
			// 如果前后时间不同，就按天展示
			$scope.time_dim_value = 2;
		} else {
			$scope.time_dim_value = 1;
		}
		if($scope.separatedDays <7){
			$scope.time_dim_status = [ true, true, false, false ];
		}else if($scope.separatedDays >=7 && $scope.separatedDays < 31){
			$scope.time_dim_status = [ true, true, true, false ];
		}else if($scope.separatedDays >= 31){
			$scope.time_dim_status = [ true, true, true, true ];
		} 
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
		// 查询汇总数据
		fetchWebsiteTrendSummary();
		// 查询kpi
		fetchKpiData();
		// 查询list数据
		fetchWebsiteTrendList();
	};

	// 环比按钮的触发事件
	$scope.cacheItem = null;
	$scope.huanBi = function() {
		
		$scope.sumHttp = 0;
		$scope.totalHttp = 3;
		$("#load-me").show();
		
		if ($scope.isHuanBi) {
			// 可选择的kpi为1
			$scope.canSelectedKpi = 1;

			if ($scope.data.length == 2) {
				$scope.cacheItem = $scope.data[1];
				// remove last item
				$scope.data[1].checked = false;
				$scope.data.splice(1, 1);
			}
			$scope.datePicker.date1.endDate = $scope.datePicker.date.startDate
					.clone().subtract(1, "days");
			$scope.datePicker.date1.startDate = $scope.datePicker.date1.endDate
					.clone().subtract($scope.separatedDays, "days");
		} else {
			$scope.canSelectedKpi = 2;

			if ($scope.cacheItem != null) {
				$scope.data.push($scope.cacheItem);
			}

			if ($scope.data.length == 2) {
				$scope.data[1].checked = true;
			}
			$scope.datePicker.date1.startDate = thePastDate.clone();
			$scope.datePicker.date1.endDate = thePastDate.clone();
			fetchWebsiteTrendSummary();
			// 查询kpi
			fetchKpiData();
			// 查询list数据
			fetchWebsiteTrendList();
		}
	};
	$scope.compareTimePeriodSelect = function() {
		
		$scope.sumHttp = 0;
		$scope.totalHttp = 3;
		$("#load-me").show();
		
		if ($scope.isHuanBi) {
			if ($scope.separatedDays == $scope.datePicker.date1.endDate.diff(
					$scope.datePicker.date1.startDate, "days")) {
				// 查询汇总数据
				fetchWebsiteTrendSummary();
				// 查询kpi
				fetchKpiData();
				// 查询list数据
				fetchWebsiteTrendList();
			} else {
				$scope.datePicker.date1.endDate = $scope.datePicker.date1.startDate
						.clone().add($scope.separatedDays, "days");
			}
		}
	};

	// 选择kpi事件
	$scope.sync = function(bool, item) {
		
		$scope.sumHttp = 0;
		$scope.totalHttp = 1;
		$("#load-me").show();
		
		if (bool) {
			// add item
			$scope.data.push(item);
			// if we have gone over maxItems:
			if ($scope.data.length > $scope.canSelectedKpi) {
				// remove first item
				$scope.data[0].checked = false;
				$scope.data.splice(0, 1);
			}
		} else {
			if ($scope.data.length == 1 && item == $scope.data[0]) {
				// 至少有一个checkbox被选中
				$scope.data[0].checked = true;
			} else {
				// remove item
				for (var i = 0; i < $scope.data.length; i++) {
					if ($scope.data[i] === item) {
						$scope.data.splice(i, 1);
					}
				}
			}
		}
		// 查询kpi
		fetchKpiData();
	};

	/*
	 * 事件维度事件定义 params: value 1 表示按时 2 表示按日 3 表示按周 4 表示按月
	 */
	$scope.byDim = function(value) {
		
		$scope.sumHttp = 0;
		$scope.totalHttp = 2;
		$("#load-me").show();
		
		$scope.time_dim_value = value;
		fetchKpiData();
		// 查询list数据
		fetchWebsiteTrendList();
	};
	
	// 窗口关闭事件
	$(".upp-close").click(function() {
		$(this).parents(".up-mask").fadeOut(500);
	});
};
app.controller('WebsiteStatisticsTodayCtrl', [ '$scope', '$rootScope',
		'$filter', '$http', '$stateParams', '$localStorage','line',
		websiteStatisticsTodayCtrlFunction ]);