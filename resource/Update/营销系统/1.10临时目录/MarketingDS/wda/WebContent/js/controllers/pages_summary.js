app.controller('PagesSummaryCtrl', [
		'$scope',
		'$rootScope',
		'$http',
		'$filter',
		'$stateParams',
		'$localStorage',
		function($scope, $rootScope, $http, $filter, $stateParams, $localStorage) {
			
			$scope.sid = $rootScope.sid || $localStorage.sid;
			//查看页面上下游弹窗是否展示
			$scope.isPageSum = $rootScope.isPageSum || $localStorage.isPageSum;
			
			//设置当前选择的tab页
			$scope.modifyParentScope(1);
			
			$("#load-me").show();
			$scope.sumHttp = 0;
			$scope.totalHttp = 2;
			
			$scope.pageSummaryJson={
					curpage : 1,
					itemcss : "un-item uni-cur",
					lastsearchtxt : '',
			};
			
			$scope.setCurPage = function(page) {
				if (page == $scope.pageSummaryJson.curpage) {
					return "pgcur";
				} else {
					return "";
				}
			};
			
			$scope.selectPage = function(page) {
				if (page <= $scope.pageSummaryJson.totalPage && page >= 1) {
					$scope.pageSummaryJson.curpage = page;
				}
			};
			
			//点击查看页面上下游界面：show出窗口，查询数据
			$scope.checkPageDetails = function(condition) {
				if(isNaN(condition)){
					$scope.isPageSum = true;
					$scope.pageUrl = condition;
					$scope.tabStyle = 1;
				}else if(condition == 1){
					$scope.isActive =[ true, false, false ];
					$scope.tabStyle = 1;
				}else if(condition == 2){
					$scope.isActive =[ false, true, false ];
					$scope.tabStyle = 2;
				}else if(condition == 3){
					$scope.isActive =[ false, false, true ];
					$scope.tabStyle = 3;
				}
				$scope.updown = [];
				
				fetchPageUpDown();
			};
			
			//点击关闭窗口
			$scope.closeMask = function(pageUrl) {
				$scope.isPageSum = false;
				$scope.isActive =[ true, false, false ];
			};
			//查询汇总数据
			fetchPageSummary();
			function fetchPageSummary() {
				
				$http({
					method: 'GET',
					url: __urlRoot + '/wda/api/wda/pages/summary',
					params: {
						__userKey : __userKey,
						site_id : $scope.sid,
						periodTime  : $scope.getPeriodTime(),
						huanBiDate : $scope.getPeriodTime(true)
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
			
			//查询受访页面上下游
			function fetchPageUpDown() {
				
				$http({
					method: 'GET',
					url: __urlRoot + '/wda/api/wda/pages/updown',
					params: {
						__userKey : __userKey,
						site_id : $scope.sid,
						periodTime  : $scope.getPeriodTime(),
						pageUrl : $scope.pageUrl,
						tab : $scope.tabStyle
					}
				}).success(function(resp, status, headers, config) {
					$scope.sumHttp ++;
					if($scope.sumHttp ==$scope.totalHttp){
						$("#load-me").hide();
					}
				    if(resp.resultCode == 0) {
				    	$scope.updown = resp.data;	        
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
			
			//查询受访页面列表
			fetchPageList();
		    function fetchPageList() {
		    	
		    	$http({
					method: 'GET',
					url: __urlRoot + '/wda/api/wda/pages/list',
					params: {
						__userKey : __userKey,
						site_id : $scope.sid,
						periodTime  : $scope.getPeriodTime(),
						huanBiDate  : $scope.getPeriodTime(true)
					}
				}).success(function(resp, status, headers, config) {
					$scope.sumHttp ++;
					if($scope.sumHttp ==$scope.totalHttp){
						$("#load-me").hide();
					}
				    if(resp.resultCode == 0) {
				    	$scope.pageSummaryJson.curpage = 1;
				    	$scope.pageSummaryJson.data = resp.data;
				        $scope.page_list_hbdata = resp.hbdata;
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
				});
		    };
		    
		 // 窗口关闭事件
			$(".upp-close").click(function() {
				$(this).parents(".up-mask").fadeOut(500);
			});
		} ]);