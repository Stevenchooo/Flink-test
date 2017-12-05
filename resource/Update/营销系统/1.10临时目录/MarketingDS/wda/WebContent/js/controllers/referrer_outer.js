app.controller('ReferrerOuterCtrl', [ '$scope', '$rootScope', '$http', '$filter','$stateParams', '$localStorage','pie','lineRef', function($scope, $rootScope, $http, $filter, $stateParams, $localStorage,pie,lineRef) {
	//设置当前选择的tab页
	$scope.modifyParentScope(3);
	
	$scope.sumHttp = 0;
	$scope.totalHttp =4;
	$("#load-me").show();
	
	//当前网站id
	$scope.sid = $rootScope.sid || $localStorage.sid;
	
	$scope.referOuterJson={
			curpage : 1,
			itemcss : "un-item uni-cur",
			lastsearchtxt : '',
	};
	
	$scope.setCurPage = function(page) {
		if (page == $scope.referOuterJson.curpage) {
			return "pgcur";
		} else {
			return "";
		}
	};
	$scope.selectPage = function(page) {
		if (page <= $scope.referOuterJson.totalPage && page >= 1) {
			$scope.referOuterJson.curpage = page;
		}
	};
		
	//查询访问来源汇总数据
	fetchPromotionReferrerSummary();
	
	function fetchPromotionReferrerSummary() {
		
		$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/website/referrer/promotion/summary',
			params: {
				__userKey : __userKey,
				site_id : $scope.sid,
				periodTime  : $scope.getPeriodTime(),
				huanBiDate  : $scope.getPeriodTime(true),
				referrer_type: 2
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

	$scope.allOptions = [ {
		key : '浏览量(PV)',
		value: 'pv',
		checked: true
	}, {
		key : '访问次数',
		value: 'visits',
		checked: false
	}, {
		key : '访客数(UV)',
		value: 'uv',
		checked: false
	}, {
		key : '新访客数',
		value: 'nuv',
		checked: false
	}, {
		key : 'IP数',
		value: 'ip',
		checked: false
	}];
	
	$scope.data = [$scope.allOptions[0]];
	
	$scope.canSelectedKpi = 1;
	
	$scope.refTop1 = '';
	var refTop2 = '';
	var refTop3 = '';
	
	/*
	 * 查询kpi值
	 */
	fetchKpiPieData();
	
	/*
	 * 查询kpi值
	 */
	function fetchKpiPieData() {
		
    	$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/website/referrer/promotion/kpi/pie',
			params: {
				__userKey : __userKey,
				site_id : $scope.sid,
				periodTime : $scope.getPeriodTime(),
				curKpi : $scope.data[0].value,
				referrer_type: 2
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp){
				$("#load-me").hide();
			}
		    if(resp.resultCode == 0) {
		    	var pieData = resp.data;
		        pie.createChart('refferOuterPie', pieData,'');
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
	
    fetchKpiLineData();
    function fetchKpiLineData() {
		
    	$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/website/referrer/promotion/kpi/line',
			params: {
				__userKey : __userKey,
				site_id : $scope.sid,
				periodTime : $scope.getPeriodTime(),
				curKpi : $scope.data[0].value,
				referrer_type: 2
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp){
				$("#load-me").hide();
			}
		    if(resp.resultCode == 0) {
				lineRef.createChart('refferOuterLine', resp, true);
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
    
    
    fetchPromotionReferrerList();
    function fetchPromotionReferrerList() {
		console.log( $scope.data );
    	$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/website/referrer/promotion/list',
			params: {
				__userKey : __userKey,
				site_id : $scope.sid,
				periodTime : $scope.getPeriodTime(),
				huanBiDate  : $scope.getPeriodTime(true),
				referrer_type: 2								
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp){
				$("#load-me").hide();
			}
		    if(resp.resultCode == 0) {
		    	$scope.referOuterJson.curpage = 1;
		    	$scope.referOuterJson.data =  resp.data;
		        $scope.referrer_hbdata = resp.hbdata;
		        $scope.selectDate = resp.selectDate;
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
		    return [];
		});
    };
    
    $scope.cacheItem = null;
	
	$scope.huanbi = function(checked) {
		
		$scope.sumHttp = 0;
		$scope.totalHttp =1;
		$("#load-me").show();
		
		$scope.isHuanBi = checked;
		if(checked) {
			//可选择的kpi为1
			$scope.canSelectedKpi = 1;
			
			if($scope.data.length == 2) {
				$scope.cacheItem = $scope.data[1];
	        	//remove last item
	  			$scope.data[1].checked = false;
	  			$scope.data.splice(1, 1);
	  		}
		} else {
			$scope.canSelectedKpi = 2;
			
			if($scope.cacheItem != null) {
				$scope.data.push($scope.cacheItem);
			}
			
			if($scope.data.length == 2) {
				$scope.data[1].checked = true;
			}
		}
		
		fetchPromotionReferrerList();
	};
	
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
		
		fetchKpiPieData();
		fetchKpiLineData();		
	};
	
	// 窗口关闭事件
	$(".upp-close").click(function() {
		$(this).parents(".up-mask").fadeOut(500);
	});
} ]);
