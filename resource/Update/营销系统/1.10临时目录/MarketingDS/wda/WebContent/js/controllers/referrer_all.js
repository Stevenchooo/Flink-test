
app.controller('AllReferrerCtrl', [ '$scope', '$rootScope', '$http', '$stateParams', '$localStorage','lineRef', function($scope, $rootScope, $http, $stateParams, $localStorage,lineRef) {
	
	//设置当前选择的tab页
	$scope.modifyParentScope(1);
	$scope.sid = $rootScope.sid || $localStorage.sid;
	
	$scope.tree_data = [false,false,false];
	
	$("#load-me").show();
	$scope.sumHttp = 0;
	$scope.totalHttp = 4;
	
	$scope.treeJson = {
			0 : [], //'广告导流'
			1 :[], //'链接导流'
			2 : [] //'直接访问'
	};
	
	$scope.treeParJson = {
			0 : [], //'广告导流'
			1 :[], //'链接导流'
			2 : [] //'直接访问'
	};
	
	fetchTreeData();
	$scope.sum = [0,0];
	$scope.clickaaa=function(con){
		if(con==0){
			if($scope.sum[0] == 0){
				$scope.tree_data[0] = true;
				$scope.sum[0] = 1;
				return;
			}
			if($scope.sum[0] == 1){
				$scope.tree_data[0] = false;
				$scope.sum[0] = 0;
				return;
			}
		}
		if(con == 1){
			if($scope.sum[1] == 0){
				$scope.tree_data[1] = true;
				$scope.sum[1] = 1;
				return;
			}
			if($scope.sum[1] == 1){
				$scope.tree_data[1] = false;
				$scope.sum[1] = 0;
				return;
			}
		}
	};
	
    function fetchTreeData() {
		console.log( $scope.data );
    	$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/website/referrer/tree',
			params: {
				__userKey : __userKey,
				site_id : $scope.sid,
				periodTime : $scope.getPeriodTime(),
				huanBiDate  : $scope.getPeriodTime(true)					
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp){
				$("#load-me").hide();
			}
		    if(resp.resultCode == 0) {
		    	var data = JSON.parse(resp.data);
		    	if(!data || data.length<=0){
		    		return ;
		    	}
		    	var refername = [];
		    	var referMap = [];
		    	for(var i in data){
		    		refername.push(data[i].referrer_name);
		    		$scope.treeJson[data[i].referrer_name == '广告导流' ? 0 : data[i].referrer_name == '链接导流' ? 1 : 2 ] = data[i].children;
		    		$scope.treeParJson[data[i].referrer_name == '广告导流' ? 0 : data[i].referrer_name == '链接导流' ? 1 : 2 ] = data[i];
		    	}
		    	if(myContain(refername, '广告导流')){
	    			$scope.tree_data[2]= true;
	    		}else{
	    			$scope.tree_data[2]= false;
	    		}
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
    
	//查询访问来源汇总数据
	fetchReferrerSummary();
	
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
	
	function fetchReferrerSummary() {
		$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/website/referrer/summary',
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
	
	$scope.col_defs = [{
		field : "referrer_name",
		displayName : "来源类型"
	},{
		field:"referrer_name",
		displayName : " ",
		cellTemplate : "<span ng-if=\" row.branch[col.field] == '广告导流' \"><a target='_blank' ui-sref='app.referrer_trend({refType: 1})'><i class='fa fa-line-chart'></i></a></span>" +
				"<span ng-if=\" row.branch[col.field] == '链接导流' \"><a target='_blank' ui-sref='app.referrer_trend({refType: 2})'><i class='fa fa-line-chart'></i></a></span>" + 
				"<span ng-if=\" row.branch[col.field] == '直接访问' \"><a target='_blank' ui-sref='app.referrer_trend({refType: 3})'><i class='fa fa-line-chart'></i></a></span>" + 
				"<span ng-if=\" row.branch[col.field] != '广告导流' && row.branch[col.field] != '链接导流' && row.branch[col.field] != '直接访问' \"><a target='_blank' ui-sref='app.referrer_trend2({refName: row.branch[col.field]})'><i class='fa fa-line-chart'></i></a></span>"
		
	}, {
		field : "pv",
		displayName : "浏览量(PV)"
	}, {
		field : "visits",
		displayName : "访问次数"
	}, {
		field : "avg_visit_time",
		displayName : "平均访问时长(秒)"
	}, {
		field : "avg_visit_pages",
		displayName : "平均访问页面"
	} ];
	
	
	$scope.data = [$scope.allOptions[0]];
	
	fetchKpiPieData();
	
	/*
	 * 查询kpi值
	 */
	function fetchKpiPieData() {
		console.log( $scope.data );
    	$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/website/referrer/kpi/pie',
			params: {
				__userKey : __userKey,
				site_id : $scope.sid,
				periodTime : $scope.getPeriodTime(),
				curKpi : $scope.data[0].value								
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp){
				$("#load-me").hide();
			}
		    if(resp.resultCode == 0) {
//		    	$scope.kpi_pie_data = resp.data;
		    	
		    	var pieData = resp.data;
		        var myChart = echarts.init(document.getElementById('refferAllPie'));
		        var legendPieData = [];
		        var valuePie = [];
		        for(var i in pieData){
		        	var key = pieData[i].key==1 ? '广告导流' : pieData[i].key==2 ? '链接导流' : '直接访问' ;
		        	legendPieData.push(key);
		        	valuePie.push({
		        		name: key,
		        		value :pieData[i].y
		        	});
		        }
		        
			    // 指定图表的配置项和数据
			    var option = {
			            tooltip : {
			                trigger: 'item',
			                formatter: "{a} <br/>{b} : {c} ({d}%)"
			            },
			        legend: {
			            data:legendPieData,
			            align : 'right'
			        },
			        series: [{
			        	name : "",
			        	center: ['50%', '55%'],
			        	radius : '45%',
			            type: 'pie',
			            data: valuePie
			        }]
			    };
			    myChart.setOption(option);
			    if(!legendPieData || legendPieData.length <=0){
			    	myChart.clear();
				    myChart.showLoading({
						text : "暂无数据",
						y : 250,
					});
			    }
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
	
    //全部来源界面折线图
    fetchKpiLineData();
    function fetchKpiLineData() {
		console.log( $scope.data );
    	$http({
			method: 'GET',
			url: __urlRoot + '/wda/api/wda/website/referrer/kpi/line',
			params: {
				__userKey : __userKey,
				site_id : $scope.sid,
				periodTime : $scope.getPeriodTime(),
				curKpi : $scope.data[0].value								
			}
		}).success(function(resp, status, headers, config) {
			$scope.sumHttp ++;
			if($scope.sumHttp ==$scope.totalHttp){
				$("#load-me").hide();
			}
		    if(resp.resultCode == 0) {
		    	var data = JSON.parse(resp.data);
		    	lineRef.createChart('refferAllLine',data,false);
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
    
	$scope.sync = function(bool, item) {
		
		$("#load-me").show();
		$scope.sumHttp = 0;
		$scope.totalHttp = 2;
		
		if(bool) {
			// add item
			$scope.data.push(item);
			// if we have gone over maxItems:
			if($scope.data.length > 1) {
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