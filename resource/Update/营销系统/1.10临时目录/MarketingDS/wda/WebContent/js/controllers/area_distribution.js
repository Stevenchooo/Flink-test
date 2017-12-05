

app.controller('AreaDistributionCtrl', ['$scope', '$rootScope', '$http', '$localStorage', '$filter','map','pie', function($scope, $rootScope, $http, $localStorage, $filter,map,pie) {
	
	$scope.sid = $rootScope.sid || $localStorage.sid;
	var periodDataSet = {
			"0" : [ 0, 0 ],
			"-1" : [ -1, -1 ],
			"-7" : [ -6, 0 ],
			"-30" : [ -29, 0 ]
		}
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
	
	$scope.areaDistributionList={
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
		if (page <= $scope.areaDistributionList.totalPage && page >= 1) {
			$scope.areaDistributionList.curpage = page;
		}
	};
	
	
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
	
	$scope.canSelectedKpi = 1;
	
	//查询汇总数据
	fetchAreaDistribution();
	
	function fetchAreaDistribution() {
		
		$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/visitor/area/distribution',
			params: {
				__userKey : __userKey,
				site_id : $scope.sid,
				periodTime  : getPeriodTime(),			
				huanBiDate  : getPeriodTime(true)
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
	
	function fetchAreaDistributionMap() {
    	$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/visitor/area/distribution/map',
			params: {
				__userKey : __userKey,
				site_id : $scope.sid,
				periodTime : getPeriodTime(),
				curKpi : $scope.data[0].value
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp){
				$("#load-me").hide();
			}
		    if(resp.resultCode == 0) {
		    	$scope.area_distribution_map_data =  resp.data;
		    	$scope.totalKpi =  resp.totalKpi;
		    	
		    	var pieData = resp.area_distribution_pie_data;
		        pie.createChart('areaDisPie', pieData,'');
				map.createChart('areaDisMap', resp.data);
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
    
    fetchAreaDistributionMap();
	//fetchKpiData();
	/*
	 * 查询kpi值
	 */
	function fetchKpiData() {
    	console.log($scope.data[0].key);
    	$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/visitor/area/distribution/kpi',
			params: {
				__userKey : __userKey,
				huanBiDate : getPeriodTime(true),
				site_id : $scope.sid,
				periodTime : getPeriodTime(),				
				curKpi : $scope.data[0].value
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp){
				$("#load-me").hide();
			}
		    if(resp.resultCode == 0) {
		    	$scope.kpi_data = resp.data;
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
    
    fetchAreaDistributionList();
    
    function fetchAreaDistributionList() {
    	
    	$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/visitor/area/distribution/list',
			params: {
				__userKey : __userKey,
				huanBiDate  : getPeriodTime(true),
				site_id : $scope.sid,
				periodTime  : getPeriodTime()
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp){
				$("#load-me").hide();
			}
		    if(resp.resultCode == 0) {
		    	$scope.areaDistributionList.curpage = 1;
		    	var resData = resp.data;
		    	$scope.area_distribution_list_data =[];
		    	for(var i in resData){
		    		if(!isNaN(resData[i].province_name) || resData[i].province_name.length == 0 ){
		    			continue;
		    		}
		    		$scope.area_distribution_list_data.push(resData[i]);
		    	}
		    	$scope.areaDistributionList.data = $scope.area_distribution_list_data;
		        $scope.selectDate = resp.selectDate;
		        var resHbData = resp.hbdata;
		        $scope.area_distribution_list_hbdata = [];
		        for(var i in resHbData){
		    		if(!isNaN(resHbData[i].province_name) || resHbData[i].province_name.length == 0 ){
		    			continue;
		    		}
		    		$scope.area_distribution_list_hbdata.push(resHbData[i]);
		    	}
		        $scope.selectHbDate = resp.selectHbDate;
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
	 * params:
	 *    period: 当前时间周期
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
		$scope.isHuanBi = false;
		// 重置第一个时间框时间
		resetDatePick(period);
		$scope.datePicker.date1.startDate = thePastDate.clone();
		$scope.datePicker.date1.endDate = thePastDate.clone();
		
	};
	//第一个时间控件事件
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
			fetchAreaDistribution();
			fetchAreaDistributionMap();
			fetchAreaDistributionList();
	}
	
	/*
	 * 环比周期的对比
	 */
	$scope.compareTimePeriodSelect = function(){
		
		$("#load-me").show();
		$scope.sumHttp = 0;
		$scope.totalHttp = 3;
		
		if($scope.isHuanBi){
			if($scope.separatedDays == $scope.datePicker.date1.endDate.diff(
					$scope.datePicker.date1.startDate, "days")){
				fetchAreaDistribution();
				fetchAreaDistributionMap();
				fetchAreaDistributionList();
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
		//查询kpi fetchKpiData();
		fetchAreaDistributionMap();
	};
	
	/*
	 * 环比按钮事件定义
	 */
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
			$scope.canSelectedKpi = 1;
			
			if($scope.cacheItem != null) {
				$scope.data.push($scope.cacheItem);
			}
			
			if($scope.data.length == 2) {
				$scope.data[1].checked = true;
			}
			$scope.datePicker.date1.startDate = thePastDate.clone();
			$scope.datePicker.date1.endDate = thePastDate.clone();
			fetchAreaDistribution();
			
			fetchAreaDistributionList();
		}
	};
	
	// 窗口关闭事件
	$(".upp-close").click(function() {
		$(this).parents(".up-mask").fadeOut(500);
	});
}]);
