app.controller('ReferrerTrendCtrl', ['$scope', '$rootScope', '$filter', '$stateParams', '$http', '$localStorage','line', function($scope, $rootScope, $filter, $stateParams, $http, $localStorage,line) {
	
	$scope.sid = $rootScope.sid || $localStorage.sid;
		
	//当前访问来源
	$scope.refType = $stateParams.refType ? $stateParams.refType : '';
	
	$scope.refName = $scope.refType == 1 ? '广告导流' : ($scope.refType == 2 ? '链接导流' : '直接访问');
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
	//时间周期变量，同时用于控制前端时间周期按钮的样式
	//默认为0，表示 今天
	$scope.period = 0;
	
	$scope.isHuanBi = false;
	$scope.separatedDays = 0;
	
	$("#load-me").show();
	$scope.sumHttp = 0;
	$scope.totalHttp = 3;
	
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
	
	//时间维度值，默认为1，表示按小时
	$scope.time_dim_value = 1;
	
	//时间维度状态，默认按时、按天可用
	$scope.time_dim_status = [true, true, false, false];
	
	/* 指标下拉框  */
	/*初始化kpi统计项列表*/
	$scope.allOptions = 
		[ { key : '浏览量(PV)', value: 'pv', checked: true}, 
		  { key : '访问次数', value: 'visits', checked: false }, 
		  { key : '访客数(UV)', value: 'uv', checked: false }, 
		  { key : '新访客数', value: 'nuv', checked: false }, 
		  { key : 'IP数', value: 'ip', checked: false}  
		];
	
	//选中的kpi
	$scope.data = [$scope.allOptions[0]];
	
	$scope.canSelectedKpi = 2;
	
	//查询汇总数据
	fetchReferrerTrendSummary();
	
	function fetchReferrerTrendSummary() {
		
		$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/website/referrer/trend/summary',
			params: {
				__userKey : __userKey,
				site_id : $scope.sid,				
				periodTime  : getPeriodTime(),			
				huanBiDate  : getPeriodTime(true),
				referrer_type: $scope.refType
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp){
				$("#load-me").hide();
			}
		    if(resp.resultCode == 0) {
		        $scope.summaryData = resp.data;
		        $scope.huanBiSummaryData = resp.hb_data;		        
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
	
	fetchReferrerTrendKpiData();
	
	//查询kpi值
	function fetchReferrerTrendKpiData() {
    	$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/website/referrer/trend/kpi',
			params: {
				__userKey : __userKey,
				site_id : $scope.sid,
				periodTime : getPeriodTime(),
				huanBiDate : getPeriodTime(true),
				time_dim : $scope.time_dim_value,
				curKpi1 : $scope.data[0].value,
				curKpi2 : ($scope.data.length == 2 ? $scope.data[1].value : ''),
				referrer_type: $scope.refType	
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp){
				$("#load-me").hide();
			}
		    if(resp.resultCode == 0) {
//		    	$scope.kpi_data = resp.data;
		    	line.createChart('refferTrendLine',resp);
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
    
    fetchReferrerTrendList();
    
    function fetchReferrerTrendList() {
    	
    	$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/website/referrer/trend/list',
			params: {
				__userKey : __userKey,
				site_id : $scope.sid,
				periodTime  : getPeriodTime(),			
				huanBiDate : getPeriodTime(true),
				time_dim : $scope.time_dim_value,
				referrer_type: $scope.refType
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp){
				$("#load-me").hide();
			}
		    if(resp.resultCode == 0) {
		        
		        $scope.referrer_trend_list_data = resp.data;
		        $scope.referrer_trend_list_hbdata = resp.hbdata;
		        $scope.selectDate = resp.selectDate;
		        $scope.selectHbDate = resp.selectHbDate;
		        $scope.hbDateByDay = resp.hbDateByDay;
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
    
	/*
	 * 选择时间周期 按钮事件，调用后台程序查询数据，并刷新前台页面
	 */
	$scope.selectTimePeriod = function(period) {
		if(period==$scope.period){
			return;
		}
		
		$scope.sumHttp = 0;
		$scope.totalHttp =3;
		$("#load-me").show();
		
		//前台页面 控制逻辑
		//// 控制时间周期按钮样式
		$scope.period = period;
		$scope.isHuanBi = false;
		
		if(period == 0) {
			$scope.time_dim_value = 1;
			$scope.time_dim_status = [true, true, false, false];
			
		}else if(period == -1){
			$scope.time_dim_value = 1;
			$scope.time_dim_status = [true, true, false, false];
		} else if(period == -7) {
			$scope.time_dim_value = 2;
			$scope.time_dim_status = [true, true, false, false];
		} else if(period == -30) {
			$scope.time_dim_value = 2;
			$scope.time_dim_status = [true, true, true, false];
		}
		// 重置第一个时间框时间
		resetDatePick(period);
		$scope.datePicker.date1.startDate = thePastDate.clone();
		$scope.datePicker.date1.endDate = thePastDate.clone();
	};
	//时间选择第一个控件
	$scope.timePickerChange = function(){
		
		$scope.sumHttp = 0;
		$scope.totalHttp =3;
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
			//查询汇总数据
			fetchReferrerTrendSummary();
			
			//查询kpi
			fetchReferrerTrendKpiData();
			
			//查询list数据
			fetchReferrerTrendList();
	}
	
	$scope.compareTimePeriodSelect = function(){
		
		$scope.sumHttp = 0;
		$scope.totalHttp =3;
		$("#load-me").show();
		
		if($scope.isHuanBi){
			if($scope.separatedDays == $scope.datePicker.date1.endDate.diff(
					$scope.datePicker.date1.startDate, "days")){
				//查询汇总数据
				fetchReferrerTrendSummary();
				//查询kpi
				fetchReferrerTrendKpiData();
				//查询list数据
				fetchReferrerTrendList();
			}else{
				$scope.datePicker.date1.endDate = $scope.datePicker.date1.startDate
				.clone().add($scope.separatedDays, "days");
			}
			
		}
	}
	
	//选择kpi事件
	$scope.sync = function(bool, item) {
		
		$scope.sumHttp = 0;
		$scope.totalHttp =2;
		$("#load-me").show();
		
		if(bool) {
			// add item
			$scope.data.push(item);
			// if we have gone over maxItems:
			if($scope.data.length > $scope.canSelectedKpi) {
				// remove first item
				$scope.data[0].checked = false;
				$scope.data.splice(0,1);
			}
		} else {
			if($scope.data.length == 1 && item == $scope.data[0]) {
				//至少有一个checkbox被选中
				$scope.data[0].checked = true;
			} else {
				//remove item
				for (var i=0 ; i < $scope.data.length; i++) { 
					if ($scope.data[i] === item) {
						$scope.data.splice(i,1);
					}
				}
			}
		}
		
		//查询汇总数据
		fetchReferrerTrendSummary();
		
		//查询kpi
		fetchReferrerTrendKpiData();	
	};
		
	/*
	 * 环比按钮事件定义
	 */
	$scope.cacheItem = null;
	
	$scope.huanbi = function() {
		
		$scope.sumHttp = 0;
		$scope.totalHttp =3;
		$("#load-me").show();
		
		if($scope.isHuanBi) {
			//可选择的kpi为1
			$scope.canSelectedKpi = 1;
			
			if($scope.data.length == 2) {
				$scope.cacheItem = $scope.data[1];
	        	//remove last item
	  			$scope.data[1].checked = false;
	  			$scope.data.splice(1, 1);
	  		}
			$scope.datePicker.date1.endDate = $scope.datePicker.date.startDate
			.clone().subtract(1, "days");
	        $scope.datePicker.date1.startDate = $scope.datePicker.date1.endDate
			.clone().subtract($scope.separatedDays, "days");
		} else {
			$scope.canSelectedKpi = 2;
			
			if($scope.cacheItem != null) {
				$scope.data.push($scope.cacheItem);
			}
			
			if($scope.data.length == 2) {
				$scope.data[1].checked = true;
			}
			$scope.datePicker.date1.startDate = thePastDate.clone();
			$scope.datePicker.date1.endDate = thePastDate.clone();
			//查询汇总数据
			fetchReferrerTrendSummary();
			
			//查询kpi
			fetchReferrerTrendKpiData();
			
			//查询list数据
			fetchReferrerTrendList();
		}
	};
	
	/*
	 *  事件维度事件定义
	 *  params: value
	 *  1 表示按时
	 *  2 表示按日
	 *  3 表示按周
	 *  4 表示按月
	 */
	$scope.byDim = function(value) {
		
		$scope.sumHttp = 0;
		$scope.totalHttp =3;
		$("#load-me").show();
		
		console.log('bydim ' + value);
		$scope.time_dim_value = value;
		
		//查询汇总数据
		fetchReferrerTrendSummary();
		
		//查询kpi
		fetchReferrerTrendKpiData();	
		
		//查询list数据
		fetchReferrerTrendList();
	};
	
	// 窗口关闭事件
	$(".upp-close").click(function() {
		$(this).parents(".up-mask").fadeOut(500);
	});
}]);
