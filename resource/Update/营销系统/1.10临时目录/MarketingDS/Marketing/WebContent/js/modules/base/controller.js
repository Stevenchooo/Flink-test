  define("modules/base/controller", ['jquery', "modules/base/charts", "modules/base/services"],
	function(require, exports, module) {
	require("jquery");
    var charts = require("modules/base/charts");
    var mListOpts = {};
    var navi = require("modules/base/navi");
    
	exports.init = function(opts) {
	    //angular未使用seajs加载，使用seajs加载在ie上不稳定
	    var app = angular.module("maa_app");
	    //services.initServices(app);
	    initControllers(app, opts);
	    
	    if (opts.charts) {
	    	charts.init(opts.charts);
	    }
	};
	
	// 初始化 echarts
	exports.initChart = function(cidList) {
		 charts.init(cidList);
	}
	
	initControllers = function(app, opts) {
	    var _path = __webRoot == '' ? '/' : __webRoot;
		
		app.controller("MainController", function($scope, LoadDataService, FilterQueryService, NotifyService) {
			$scope.options = opts;
			$scope.__needConfirmPwd = true;
			$scope.listResp = {};
			$scope.filterResp = {}
			
			$scope.pageNum = {};
	    	$scope.pages = {};
	    	$scope.curPage = {};
	    	
	    	$scope.subscribe = function(id, callback) {
	    		NotifyService.subscribe(id, callback);
	    	}
	    	
	    	$scope.getcurAppId = function() {
	    		return navi.getcurAppId();
	    	}
	    	
	    	$scope.getcurAccount = function() {
	    		return navi.getcurAccount();
	    	}
	    	
	    	$scope.getcurAppPkg = function() {
	    		return navi.getcurAppPkg();
	    	}
	    	
	    	$scope.refresh = function() {
	    		navi.refreshContainer();
	    	}
			
			// 加载查询条件的项
			$scope.loadFilterItems = function(id, url, params) {
				$.extend(params, {"id" : $scope.options.id});
				$scope.loadFilterItemsWithCallback(id, url, params, null);
			};
			
			// 加载查询条件的项
			$scope.loadFilterItemsWithCallback = function(id, url, params, callback) {
				$.extend(params, {"id" : $scope.options.id});
				LoadDataService.loadData(url, params).then(function(data){
					if(data.resultCode != 0){
						$scope.errInfo(data.resultCode);
					}
					
					if(data.resultCode == 0){
						if (callback) {
							callback(data);
						}
						$scope.filterResp[id] = data;
					}
				}, function(res){
					$scope.httpErrInfo(res);
				});
			};
			
			// 如果列表和图表数据源是一样的，可以只需一次请求
			$scope.showPage = function(url, opts, listId, chartId) {
				$.extend(opts.params, {"id" : $scope.options.id});
				
				charts.showLoading(chartId);
				LoadDataService.loadData(url, opts.params).then(function(data){
					if(data.resultCode == 0){
						$scope.listResp[listId] = data.result;
						charts.showChart(chartId, data.result, opts);
					}
				}, function(status){
					charts.clearChart(chartId);
					charts.hideLoading(chartId);
					$scope.httpErrInfo(status);
				});
			};
			
			// 如果列表和图表数据源是一样的，可以只需一次请求
			$scope.showPageWithCallback = function(url, opts, listId, chartId, callback) {
				$.extend(opts.params, {"id" : $scope.options.id});
				charts.showLoading(chartId);
				LoadDataService.loadData(url, opts.params).then(function(data){
					if(data.resultCode != 0){
						$scope.errInfo(data.resultCode);
						data.result = {};
						charts.showChart(chartId, data.result, opts);
						return;
					}
					
					if(data.resultCode == 0){
						charts.showChart(chartId, data.result, opts);
						if (callback) {
							callback(data.result);
						}
						$scope.listResp[listId] = data.result;
					}
				}, function(status){
					charts.clearChart(chartId);
					charts.hideLoading(chartId);
					$scope.httpErrInfo(status);
				});
			};
			
			// 加载列表数据
			$scope.loadListData = function(listId, url, params) {
				$.extend(params, {"id" : $scope.options.id});
				LoadDataService.loadData(url, params).then(function(data){
					if(data.resultCode == 0){
						$scope.listResp[listId] = data.result;
					}
				}, function(status){
					$scope.httpErrInfo(status);
				});
			};
			
			// 带分页的数据加载
			$scope.loadListDataWithPage = function(listId, url, params, callback) {
				$.extend(params, {"id" : $scope.options.id});
				LoadDataService.loadData(url, params).then(function(data){
					if(data.resultCode == 0){
						$scope.calcPage(listId, data.total, params);
						$scope.listResp[listId] = data.result;
					}
					else {
						$scope.errInfo(data.resultCode);
					}
					
					if (callback) {
						callback(data);
					}
					
				}, function(status){
					$scope.httpErrInfo(status);
				});
			};
			
			// 加载列表数据，可以通过回调数据对数据进行额外处理
			$scope.loadListDataWithCallback = function(listId, url, params, callback) {
				$.extend(params, {"id" : $scope.options.id});
				LoadDataService.loadData(url, params).then(function(data){
					
					if(data.resultCode != 0){
						$scope.errInfo(data.resultCode);
					}
					
					if (callback) {
						callback(data);
					}
				}, function(status){
					$scope.httpErrInfo(status);
				});
			};
			
			// 加载图表数据
			$scope.loadChartData = function(chartId, url, opts) {
				$.extend(opts.params, {"id" : $scope.options.id});
				
				charts.showLoading(chartId);
				LoadDataService.loadData(url, opts.params).then(function(data){
					if(data.resultCode == 0){
						charts.showChart(chartId, data.result, opts);
					}
				}, function(status){
					charts.clearChart(chartId);
					charts.hideLoading(chartId);
					$scope.httpErrInfo(status);
				});
			};
			
			// 调用ajax api
			$scope.callApi = function(url, params, callback) {
				$.extend(params, {"id" : $scope.options.id});
				LoadDataService.loadData(url, params).then(function(data){
					if (callback) {
						callback(data.resultCode);
					}
					
					if (data.resultCode != 0) {
						$scope.errInfo(data.resultCode);
					}
				}, function(status){
					$scope.httpErrInfo(status);
				});
			}
			
			$scope.exportData = function(url, params) {
				var baseUrl = __webRoot + url + "?__userKey=" + __userKey + "&id=" + $scope.options.id;
				
				var paramsJsonStr = JSON.stringify(params);
        		if (paramsJsonStr == null) {
        			return;
        		}
        		var finalUrl = baseUrl + "&exp_value_export=true&export_params=" + paramsJsonStr;
        		
				location.href = finalUrl;
			}
			
			// 创建查询参数
			$scope.createParams = function (begin_date, end_date, eui_ver, app_version, product, device) {
				FilterQueryService.setStartTime(begin_date);
        		FilterQueryService.setEndTime(end_date);
        		FilterQueryService.setEmuiVer(eui_ver);
        		FilterQueryService.setAppVer(app_version);
        		FilterQueryService.setProduct(product);
        		FilterQueryService.setDevice(device);
        		
        		return FilterQueryService.createParams();
			}
			
			// 分页切换
			$scope.pageClick=function(from, listId, url, params, callback) {
	    		if(from == -1) { //点击分页的"..."
	    			var pageTab = $("#id_page_table");
	    			var pageWin = $("#goto_page_window");
	    			var pos = pageTab.position();
	    			pageWin.css("left", pos.left + (pageTab.width() - pageWin.width()));
	    			pageWin.css("top", pos.top + pageWin.height());
	    			$scope.gotoPageNo = '';
	    			pageWin.show();
	    			
	    			$(document).mousedown(function(event) {
	    				if($(event.target).closest("#goto_page_window").length == 0) {
	    					$(document).unbind('mousedown');
	    					$("#goto_page_window").fadeOut();
	    				}
	    			});
	    			
	    			return;
	    		}
	    		
	    		var opts = params;
	    		if(from == -2) { //goto窗口中点击确定
	    			from = parseInt($scope.gotoPageNo);
	    			if(isNaN(from) || from < 0) {
	    				from = 0;
	    			} else if(from >= $scope.pageNum[listId]) {
	    				from = $scope.pageNum[listId] - 1;
	    			} else {
	    				from--; 
	    			}
	    			from *= opts.perPage;
	    			
	    			$(document).unbind('mousedown');
	    			$("#goto_page_window").fadeOut();
	    		}
	    		
	    		//在最后一页删除，当最后一页删除完毕后，页码需回退
	    		if($scope.listResp[listId] && from >= $scope.listResp[listId].total && from >= opts.perPage) {
	    			opts.from = from - opts.perPage;
	    		} else {
	    			opts.from = from;
	    		}
	    		
	    		$scope.loadListDataWithPage(listId, url, params, callback);
	    	};
			
			// 分页计算
			$scope.calcPage=function(listId, total, params) {
	    		if(total === undefined) {
	    			return;
	    		}
	    		
	    		var opts = params;
		    	var pageNum = Math.ceil(total/opts.perPage);
		    	//当前后两次查询条件发生变更时，total会变，原有的from可能已失效
		    	if(opts.from >= total) {
		    		opts.from = pageNum <= 0 ? 0 : ((pageNum - 1) * opts.perPage);
		    	}
		    	var curPage = opts.from/opts.perPage;
		    	var halfPgNum = opts.halfPgNum ? opts.halfPgNum : 4;
		    	var maxPgNum = halfPgNum * 2 + 1; 
		    	
		    	var pages = [];
		    	if(pageNum > 1) {
			    	if(curPage > 0) {
			    		pages.push({from:(curPage-1)*opts.perPage, cls:'uppg-prv', str:'上一页'});
			    	}
			    	
			    	var from = 0;
			    	var i = 0;
			    	var end = pageNum; 
			    	/*当前页尽量放在中间*/
			    	if(pageNum > maxPgNum) {
		    			if(curPage < halfPgNum) {
		    				i = 0;
		    			} else if(curPage >= pageNum - halfPgNum) {
			    			i = pageNum - maxPgNum;
			    		} else {
			    			i = curPage - halfPgNum;
			    		}
		    			end = i + maxPgNum;
		    			from = i * opts.perPage;
		    		}
			    	
			    	for(; i < end; i++) {
			    		if(i != curPage) {
			    			pages.push({from:from, cls:'page', str:(i+1)});
			    		} else {
			    			pages.push({from:from, cls:'pgcur', str:(i+1)});
			    		}
			    		from += opts.perPage;
			    	}
			    	if(pageNum > maxPgNum) {
			    		pages.push({from:-1, cls:'page', str:'...'})
			    	}
			    	if(pageNum > 1 && curPage < pageNum - 1) {
			    		pages.push({from:(curPage+1)*opts.perPage, cls:'uppg-nt', str:'下一页'});
			    	}
		    	}
		    	$scope.pageNum[listId] = pageNum;
		    	$scope.pages[listId] = pages;
		    	$scope.curPage[listId] = curPage + 1;
	    	};
	    	
	    	// 创建对话框
	    	$scope.createInfoDlg = function(title, content, type) {
	    		alert("[" + title + "] " + content);
	    	}
	    	
	    	// http请求失败信息
	    	$scope.httpErrInfo = function(res) {
	    		if (typeof(res) == "undefined") {
	    			$scope.createInfoDlg("错误", "数据编码有误， 请检查！", "error");
	    		}
	    		else {
	    			$scope.createInfoDlg("错误", res, "error");
	    		}
	    	}
	    	
	    	// api调用错误信息
	    	$scope.errInfo = function(retCode) {
	    		$scope.createInfoDlg("错误", getLocalReason(retCode, "error"), "error");
	    	}
	    	
			opts.meta.init($scope);
	    	opts.meta.extendScope($scope);
		});
	}
	
	exports.initAppScope = function(appid) {
		angular.bootstrap(document.getElementById(appid), ['maa_app']);
	}
});