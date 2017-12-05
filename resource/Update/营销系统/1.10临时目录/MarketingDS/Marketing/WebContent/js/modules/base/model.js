define("modules/base/model", ['jquery', "modules/base/tree", "dialog", "cookie",'jquery/dialog/6.0.4/css/ui-dialog.css'],
	function(require, exports, module) {
    require("jquery");
    var tree = require("modules/base/tree");
    var mListOpts = {};

	exports.showList = function(opts,doucumentId) {
	    require("dialog");
	    require('jquery/dialog/6.0.4/css/ui-dialog.css');
	    require('cookie');
	    
	    //angular未使用seajs加载，使用seajs加载在ie上不稳定
	    var app = angular.module("ModelList", []);
	    var _path = __webRoot == '' ? '/' : __webRoot;
	    $.cookie(__cookieHeader + "scopeId", opts.id, {path:_path, expires:1, secure:false}); 
	    app.controller("ListCtrl", function($scope, $http) {
	    	$scope.options = opts;

	    	$scope.__needConfirmPwd = true;
	    	
	    	$scope.closePwdDlg = function(){
        		if($scope.pwdDialog){
        			$scope.pwdDialog.close().remove();
        		}
        	};
        	
	    	$scope.checkNeedConfirmPwd = function(id) {
	    		if($scope.__needConfirmPwd){
                    $scope.confirmPwdForm.$setPristine();
                    $scope.password = "";
    	    		$scope.pwdDialog = dialog({
    		    		title:"",
    		    		opacity:0.3,
    		    		modal:true,
    		    		lock:true,
    	        		content:document.getElementById(id)
    		    	}).showModal();
    	    		return true;
                }else{
                	return false;
                }
	    	};
	    	
	    	$scope.confirmPwdCheck = function() {
	    		if($scope.__needConfirmPwd){
	    			$scope.confirmPwdForm.password.$dirty = true;
		            return $scope.confirmPwdForm.password.$valid;
	    		}else{
	    			return true;
	    		}
	        };
	        
	    	$scope.pageClick=function(from) {
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
	    		
	    		var opts = $scope.options;
	    		if(from == -2) { //goto窗口中点击确定
	    			from = parseInt($scope.gotoPageNo);
	    			if(isNaN(from) || from < 0) {
	    				from = 0;
	    			} else if(from >= $scope.pageNum) {
	    				from = $scope.pageNum - 1;
	    			} else {
	    				from--; 
	    			}
	    			from *= opts.perPage;
	    			
	    			$(document).unbind('mousedown');
	    			$("#goto_page_window").fadeOut();
	    		}
	    		
	    		//在最后一页删除，当最后一页删除完毕后，页码需回退
	    		if($scope.resp && from >= $scope.resp.total && from >= opts.perPage) {
	    			opts.from = from - opts.perPage;
	    		} else {
	    			opts.from = from;
	    		}
	    		$scope.loadData({id:opts.id,
	    			             from:opts.from, 
	    			             meta:opts.meta.getName(), 
	    						 segments:opts.segments,
	    						 perPage:opts.perPage
	    						});
	    	};
	    	
	    	$scope.calcPage=function(resp) {
	    		if(resp.total === undefined) {
	    			return;
	    		}
	    		
	    		var opts = $scope.options;
		    	var pageNum = Math.ceil(resp.total/opts.perPage);
		    	//当前后两次查询条件发生变更时，total会变，原有的from可能已失效
		    	if(opts.from >= resp.total) 
		    	{
		    		opts.from = pageNum <= 0 ? 0 : ((pageNum - 1) * opts.perPage);
		    	}
		    	var curPage = opts.from/opts.perPage;
		    	var halfPgNum = opts.halfPgNum ? opts.halfPgNum : 4;
		    	var maxPgNum = halfPgNum * 2 + 1; 
		    	
		    	var pages = [];
		    	if(pageNum > 1) 
		    	{
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
			    	if(pageNum > maxPgNum) 
			    	{
			    		//pages.push({from:-1, cls:'page', str:'...'})
			    	}
			    	if(pageNum > 1 && curPage < pageNum - 1) {
			    		pages.push({from:(curPage+1)*opts.perPage, cls:'uppg-nt', str:'下一页'});
			    	}
		    	}
		    	$scope.pageNum = pageNum;
		    	$scope.pages = pages;
		    	$scope.curPage = curPage + 1;
	    	};
	    	
	    	$scope.failToOperate=function(resp) {
	    		if(resp.resultCode == 8) {
	    			window.location.href=__webRoot+"/page/login";
	    			return;
	    		}
	    		opts.meta.failToOperate(resp, $scope);
	    	};
	    	
	    	$scope.successToOperate=function(resp) {
	    		opts.meta.successToOperate(resp, $scope);
	    	};
	    	
	    	$scope.httpError=function(status) {
	    		opts.meta.showHttpError(status, $scope);
	    	};
	    	
	    	$scope.loadData=function(params) {
	    		var req = opts.meta.querySubPara($scope, params);
	    		$scope.callApi(req).success(function(response, status, headers, config){
	    			if(response.resultCode != 0) {
	    				if(response.resultCode == 50001){
	    					dialog('请输入SID或CID', function() {
	    					}).showModal();
	    				}else{
	    					$scope.failToOperate(response);
	    				}
			          	return;
	    			}
    		        $scope.resp = response;
    		    	$scope.calcPage(response);
	    			opts.meta.afterQuery($scope);
			    }).error(function(response, status, headers, config){
			    	$scope.httpError(status);
    		    });
	    	};
	    	
	    	$scope.refresh=function() {
        		$scope.pageClick($scope.options.from);
	    	};
	    	
	    	$scope.refreshTree=function(metaName) {
      			var meta = tree.getMetas(metaName);
      			if(!meta || !meta.isVisible) {
      				return;
      			}
      			
      			var treeObj = $.fn.zTree.getZTreeObj(tree.getTreeOpts().treeContainer);
      			if(treeObj == null) {
      				console.error("Can't get tree object");
      				return;
      			}
      			var nodes = treeObj.getSelectedNodes();
      			if(nodes == null) {
      				console.error("Can't get selected tree nodes");
      				return;
      			}
      			
      			treeObj.reAsyncChildNodes(nodes[0], "refresh");
	    	};
	    	
	    	$scope.modelDel=function(id) {
		    	var req = opts.meta.removePara($scope, opts.id, id);
	    	    dialog({
	    			title: getLocalTag("prompt", "Prompt"),
	        		content:getLocalTag("sureToDelete", "Are you sure to delete it?"),
	    			cancel:true,
	    			lock:true,
		    		okValue: getLocalTag('confirm','Yes'),
	        		cancelValue: getLocalTag('cancel','cancel'),
	    		    ok: function() {
	    		    	$scope.$apply(function(){
		    	    		$scope.callApi(req).success(function(response, status, headers, config){
		                    	if(response.resultCode == 0) {
		                    		$scope.resp.total--;
		                    		$scope.refresh();
		                        	$scope.successToOperate(response, $scope);
		                    		return;
		                    	}
		                    	$scope.failToOperate(response);
		    			    }).error(function(response, status, headers, config){
		    			    	$scope.httpError(status);
		    			    });
	    		    	});
	    		    }
	        	}).showModal();
	    	};
	    	
	    	$scope.modelCreate=function() {
	    		var opts = $scope.options;
	    		////////////////////////////////////
	    		var my = document.getElementById("_calendar_div");
	    	    if (my != null)
	    	        my.parentNode.removeChild(my);
	    	    ///////////////////////////////////
	    		var dlgId = opts.meta.createDialog($scope);
	    		$scope.dialog = dialog({
	    			title: getLocalTag("modelCreate", "Create") + " " + $scope.getMetaName(),
	        		content:document.getElementById(dlgId),
	    			lock:true,
	    			cancel:true,
		    		okValue: getLocalTag('confirm','Yes'),
	        		cancelValue: getLocalTag('cancel','cancel'),
	    		    ok: function() {
	    		    	if(!opts.meta.createCheck($scope)) {
	    		    		return false;
	    		    	}

	    		    	$scope.$apply(function(){
	    		    		var req = $scope.options.meta.createPara($scope, opts.id, opts.meta.getName());
	    		    		if(req == null) {
	    		    			return;
	    		    		}
	    		    		$scope.callApi(req).success(function(response, status, headers, config){
					          	if(response.resultCode == 0) {
						    		$scope.dialog.close().remove();
				          			$scope.refresh();
					          		return;
					          	}
					          	$scope.failToOperate(response);
						    }).error(function(response, status, headers, config){
						    	$scope.httpError(status);
			    			});
		    			});
	    		    	return false;
	    		    }
	        	}).showModal();
	    	};
	    	
	    	$scope.modelModify=function(id) {
	    		var opts = $scope.options;
               ////////////////////////////////////
	    		var my = document.getElementById("_calendar_div");
	    	    if (my != null)
	    	        my.parentNode.removeChild(my);
	    	    ///////////////////////////////////
	    		var dlgId = opts.meta.modifyDialog(id, $scope);
	    		
	    		$scope.dialog = dialog({
	        		title: getLocalTag("modelModify", "Modify") + " " + $scope.getMetaName(),
	        		content:document.getElementById(dlgId),
	    			lock:true,
	        		cancel:true,
		    		okValue: getLocalTag('confirm','Yes'),
	        		cancelValue: getLocalTag('cancel','cancel'),
	    		    ok: function() {
	    		    	if(!opts.meta.modifyCheck($scope)) {
	    		    		return false;
	    		    	}
	    		    	
	    		    	$scope.$apply(function(){
	    		    		var req = $scope.options.meta.modifyPara($scope, id, opts.id);
	    		    		if(req == null) {
	    		    			return;
	    		    		}
	    		    		$scope.callApi(req).success(function(response, status, headers, config){
					          	if(response.resultCode == 0) {
					          		$scope.dialog.close().remove();
					          		$scope.refresh();
					          		return;
					          	}
					          	$scope.failToOperate(response);
						    }).error(function(response, status, headers, config){
						    	$scope.httpError(status);
						    });
	    		    		return false;
	    		    	});
	    		    }
	        	}).showModal();
	    	};
	    	
	    	$scope.callApi=function(req) { //自动增加临时key，用于防止js跨站攻击
	    		var data = req.params ? req.params : req.data;
	    		if(data) {
	    			data['__userKey'] = __userKey;
	    		} else {
	    			data = {"__userKey":__userKey};
	    		}
	    		
	    		var resp = $http.post(req.url, data, req.config);
	    		if(req.success) {
	    			resp.success(function(response, status, headers, config){
	    				req.success(response, status, headers, config);
	    			});
	    		}
	    		if(req.error) {
	    			resp.error(function(response, status, headers, config){
	    				req.error(response, status, headers, config);
	    			});
	    		}
	    		return resp;
	    	};
	    	
	    	$scope.getMetaName=function(meta) {
	    		var metaName;
	    		if(!meta) {
		    		metaName = opts.meta.getName();
	    		} else {
	    			metaName = meta;
	    		}
	    		
	    		return getLocalTag("meta."+metaName, metaName);
	    	};
	    	
	    	if(tree.getTreeOpts().curMeta) {
	    		$scope.subMetas = tree.getTreeOpts().curMeta.subMetas;
	    	}
	    	opts.meta.init($scope);
	    	//callApi必须放在extendScope,否则extendScope中不能调用
	    	opts.meta.extendScope($scope, $http);
	    	if(opts.from != undefined && opts.from >= 0) {
	    		$scope.pageClick(opts.from); //打开时显示第n页
	    	}
	    });

	    //文件上传操作
        app.directive('fileModel', ['$parse', function ($parse) {
            return {
                restrict: 'A',
                link: function(scope, element, attrs) {
                    var model = $parse(attrs.fileModel);
                    var modelSetter = model.assign;
                    
                    element.bind('change', function(){
                        scope.$apply(function(){
                            modelSetter(scope, element[0].files[0]);
                        });
                    });
                }
            };
        }]);
        
	    opts.meta.directives(app);
	    
	    angular.bootstrap(document, ['ModelList']);

	};
	
	//因为$http不支持同步，当同步时，使用此函数，
	//var model = require("/modules/base/model"); model.callApi(...)
	exports.callApi = function(req) {
		if(req.data) {
			req.data['__userKey'] = __userKey;
		} else {
			req['data'] = {"__userKey":__userKey};
		}
		return $.ajax(req);
	};
//end of define
});