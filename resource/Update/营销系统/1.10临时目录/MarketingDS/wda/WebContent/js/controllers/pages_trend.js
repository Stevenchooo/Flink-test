app.controller('PagesTrendCtrl', 
		['$scope', '$rootScope', '$filter', '$stateParams', '$http', '$localStorage', 'line',
        function($scope, $rootScope, $filter, $stateParams, $http, $localStorage,line) {
	
	$scope.sid = $rootScope.sid || $localStorage.sid;
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

    //当前pageUrl
	$scope.pageUrl = $stateParams.pageUrl ? $stateParams.pageUrl : '';
	
	//时间周期变量，同时用于控制前端时间周期按钮的样式，默认为0，表示 今天
	$scope.period = 0;
	$scope.isHuanBi = false;
	//相隔几天，为了做联动
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
	//样式控制
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
	
	$scope.allOptions = 
		[ { key : '浏览量(PV)', value: 'pv', checked: true}, 
		  { key : '访客数(UV)', value: 'uv', checked: false },
		  { key : '访问次数', value: 'visits', checked: false },
		  { key : 'IP数', value: 'ip', checked: false}, 
		  { key : '入口页次数', value: 'landing_pages', checked: false},
		  { key : '贡献下游浏览量', value: 'up_down_pages', checked: false},
		  { key : '退出页次数', value: 'exit_pages', checked: false}
		];
	
	
	//选中的kpi
	$scope.data = [$scope.allOptions[0]];
	
	$scope.canSelectedKpi = 2;
	
	$scope.pageTrendJson={
			curpage : 1,
			itemcss : "un-item uni-cur",
			lastsearchtxt : '',
	};
	
	$scope.setCurPage = function(page) {
		if (page == $scope.pageTrendJson.curpage) {
			return "pgcur";
		} else {
			return "";
		}
	};
	
	$scope.selectPage = function(page) {
		if (page <= $scope.pageTrendJson.totalPage && page >= 1) {
			$scope.pageTrendJson.curpage = page;
		}
	};
	
	
	//查询该页面汇总数据
	fetchPageTrendSummary();
	
	function fetchPageTrendSummary() {
		
		$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/pages/trend/summary',
			params: {
				__userKey : __userKey,
				site_id : $scope.sid,
				page_url : encodeURI($scope.pageUrl),
				periodTime  : getPeriodTime(),	
				huanBiDate  : getPeriodTime(true)
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp){
				$("#load-me").hide();
			}
		    if(resp.resultCode == 0) {
		        $scope.pageSummaryData = resp.data;
		        $scope.huanBiPageSummaryData = resp.hb_data;		        
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
	
	fetchKpiData();
	
	/*
	 * 查询kpi值
	 */
	function fetchKpiData() {
		console.log( $scope.pageUrl );
    	$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/pages/trend/kpi',
			params: {
				__userKey : __userKey,
				site_id : $scope.sid,
				page_url : encodeURI($scope.pageUrl),
				periodTime : getPeriodTime(),
				huanBiDate : getPeriodTime(true),
				time_dim : $scope.time_dim_value,
				curKpi1 : $scope.data[0].value,
				curKpi2 : ($scope.data.length == 2 ? $scope.data[1].value : '')								
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp){
				$("#load-me").hide();
			}
		    if(resp.resultCode == 0) {
				line.createChart('pageTrendLine', resp);
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
    
    fetchPagesTrendList();
    
    function fetchPagesTrendList() {
    	$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/pages/trend/list',
			params: {
				__userKey : __userKey,
				site_id : $scope.sid,
				page_url : encodeURI($scope.pageUrl),
				periodTime  : getPeriodTime(),
				huanBiDate  : getPeriodTime(true),
				time_dim : $scope.time_dim_value
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp){
				$("#load-me").hide();
			}
		    if(resp.resultCode == 0) {
		    	$scope.pageTrendJson.curpage = 1;
		    	$scope.page_trend_list_data = resp.data;
		        $scope.page_trend_list_hbdata = resp.hbdata;
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
		
		$("#load-me").show();
		$scope.sumHttp = 0;
		$scope.totalHttp = 3;
		
		//前台页面 控制逻辑
		//// 控制时间周期按钮样式
		$scope.period = period;
		//控制页面上环比按钮的
		$scope.isHuanBi = false;
		if(period == 0) {
			$scope.time_dim_value = 1;
			$scope.time_dim_status = [true, true, false, false];
		} else if(period == -1){
			$scope.time_dim_value = 1;
			$scope.time_dim_status = [true, true, false, false];
		}else if(period == -7) {
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
	//时间控件
	$scope.timePickerChange = function(){
		
		$("#load-me").show();
		$scope.sumHttp = 0;
		$scope.totalHttp = 3;
		
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
		
		//查询该页面汇总数据
		fetchPageTrendSummary();
			
		fetchPagesTrendList();
			
		//查询该页面kpi
		fetchKpiData();
		
	}
	
	$scope.compareTimePeriodSelect = function(){
		
		$("#load-me").show();
		$scope.sumHttp = 0;
		$scope.totalHttp = 3;
		
		if($scope.isHuanBi){
			if($scope.separatedDays == $scope.datePicker.date1.endDate.diff(
					$scope.datePicker.date1.startDate, "days")){
				//查询该页面汇总数据
				fetchPageTrendSummary();
				
				fetchPagesTrendList();
				
				//查询该页面kpi
				fetchKpiData();
			}else{
				$scope.datePicker.date1.endDate = $scope.datePicker.date1.startDate
				.clone().add($scope.separatedDays, "days");
			}
		
		}
	}
	
	/*
	 * 选择kpi事件
	 */
	$scope.sync = function(bool, item) {
		
		$("#load-me").show();
		$scope.sumHttp = 0;
		$scope.totalHttp = 1;
		
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
		
		//查询kpi
		fetchKpiData();
	};
	
	//chart下方的checkbox按钮事件
	$scope.huanbiIndex = 0;
	$scope.huanbiChecked = true;
	$scope.selectedHuanbiItem = -1;
	
	//1. 如果选中了2个kpi，去掉最后一个；同时，选择界面限制只能选择一个
	//2. 设置data2的数据
	$scope.cacheItem = null;
	
	$scope.huanbi = function() {
		
		$("#load-me").show();
		$scope.sumHttp = 0;
		$scope.totalHttp = 3;
		
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
			//查询该页面汇总数据
			fetchPageTrendSummary();
			
			//查询该页面kpi
			fetchKpiData();
			
			fetchPagesTrendList();
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
		
		$("#load-me").show();
		$scope.sumHttp = 0;
		$scope.totalHttp = 2;
		
		$scope.time_dim_value = value;
		
		fetchKpiData();
		
		//查询list数据
		fetchPagesTrendList();
	};
	
	// 窗口关闭事件
	$(".upp-close").click(function() {
		$(this).parents(".up-mask").fadeOut(500);
	});
	
}]);


