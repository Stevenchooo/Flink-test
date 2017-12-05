define(function(require, exports, module) {
    var MetaBase = require('meta');
    require('dialog');
    require('jquery');
    require('calendar');
    require('jquery/calendar/1.0/tinycal.css');
    

    var MetaMktMonitor = {
        setRange : function ($scope, start, end) {
			$scope.query_startTime = start.year + '-' + $.addZero(start.month + 1) + '-' + $.addZero(start.day);
			$scope.query_endTime = end.year + '-' + $.addZero(end.month + 1) + '-' + $.addZero(end.day);
		},

		initCaledar : function ($scope) {
			
			$.fn.calendar({
				target : "#queryDate",
				mode : 'multi',
				recordCheck:false,
				ok : function (checked, target) {

					var length = checked.length;
					if(length > 2)
					{
						dialog('最多只能够选择两个时间,请重新选择', function(){}).showModal();
						return;
					}
					
					
					var day0 = checked[0].year + '' + $.addZero(checked[0].month + 1) + '' + $.addZero(checked[0].day);
					
					if(length == 2)
					{
						var day1 = checked[1].year + '' + $.addZero(checked[1].month + 1) + '' + $.addZero(checked[1].day);

						if(day0 <= day1)
						{
							$scope.queryDate = day0 + '-' + day1;
						}
						else
						{
							$scope.queryDate = day1 + '-' + day0;
						}
					}
					else
					{
						$scope.queryDate = day0 + '-' + day0;
					}
					
					$scope.$digest();
					return true;
				}

			});
			
			

		},
        querySubPara: function($scope, params) {
      
        	
        	var landPlatform = $scope.landPlatform;
			 if(typeof(landPlatform) == "undefined" || landPlatform =="")
        	 {
				 landPlatform = -1;
        	 }
            var queryDate = $scope.queryDate;
            if(typeof(queryDate) == "undefined" || queryDate == "")
        	{
            	queryDate = '';
        	}
            else
            {
            	if (!$scope.TimeCheck(queryDate))
            	{
					return;
            	}
            }
			var queryDateBeginDay = queryDate.split('-')[0];
			var queryDateEndDay = queryDate.split('-')[1];
			
        	$.extend(params, 
        		{
	        		mktId:$scope.queryMktId,
	        		adState:$scope.queryAdState,
	        		monitorPlatform:0,
	        		adInfoWebName : $scope.queryAdInfoWebName,
					adInfoPort : $scope.queryAdInfoPort,
					inputUser : $scope.queryInputUser,
					landPlatform : landPlatform,
					adqueryDateBeginDay  : queryDateBeginDay,
					adqueryDateEndDay  : queryDateEndDay
        		});
        	
            return {
                method: 'GET',
                params:params,
                url: this.webRoot+"/api/monitorInfo/query"
            };
        },
        
        
        removePara: function($scope,pid, adInfoId) {
            return {
                method: 'GET',
                url: this.webRoot+"/api/adInfo/remove",
                params: {pid:pid,adInfoId:adInfoId} 
            };
        },
        
        
        modifyPara: function($scope, adInfoId, pid) {
            return {
                method: 'POST', 
                url: this.getWebRoot() + "/api/monitor/modify",
                params: {
                	 adInfoId : adInfoId,
                	 monitorBiCode : $scope.monitorBiCode
                }
            };
        },
        
        
        modifyDialog:function(adInfoId, $scope) {
        	
        	$scope.callApi({
        		url:this.getWebRoot() + "/api/monitor/detail",
        		method:"GET",
        		params:{adInfoId:adInfoId}
    		}).success(function(resp, status, headers, config){
                if(resp.resultCode != 0) {
    	          	$scope.failToOperate(resp);
                    return;
                }
                monitorInfo = resp;
                $scope.monitorBiCode =  monitorInfo.monitorBiCode;

		    }).error(function(response, status, headers, config){
		    	$scope.httpError(status);
			});
        	
        	
      
        	 
        	 
            return 'id_create_monitor_dialog';
        },
        
        
        
        
        
        createCheck : function($scope) {
            return $scope.monitorCreateForm.$valid;
        },
        
        
        modifyCheck : function($scope) {
        	return $scope.monitorCreateForm.$valid;
        },
        
        
        extendScope: function($scope,$http) {
        	var webRoot = this.getWebRoot();
        	
        	//初始化下拉菜单
			this.initCaledar($scope);
        	
        	$scope.callApi({
	        	url: this.getWebRoot()+"/api/mktInfo/queryIdNameListWithControl", 
         		method:"GET",
         		params:{}
     		}).success(function(resp, status, headers, config){
                if(resp.resultCode != 0) {
     	         	$scope.failToOperate(resp);
                    return;
                }
                $scope.mktInfos = resp.result;
 		    }).error(function(response, status, headers, config){
 		    	$scope.httpError(status);
 			});
        	
        	
        	$scope.callApi({
	        	url: this.getWebRoot()+"/api/mktDic/queryDicCommonList", 
         		method:"GET",
         		params:{type:"ad_state"}
     		}).success(function(resp, status, headers, config){
                if(resp.resultCode != 0) {
     	         	$scope.failToOperate(resp);
                    return;
                }
                $scope.states = resp.results;
 		    }).error(function(response, status, headers, config){
 		    	$scope.httpError(status);
 			});
        	
        	//录入人赋值
			$scope.callApi({
				url : this.getWebRoot() + "/api/mktDic/queryInputUserNameList",
				method : "GET"
			}).success(function (resp, status, headers, config) {
				if (resp.resultCode != 0) {
					$scope.failToOperate(resp);
					return;
				}
				var arr = {};
				var array = new Array();
				arr.id = '-1';
				arr.name = '全部';
				$scope.inputUserNames = array.concat(arr,resp.results);

			}).error(function (response, status, headers, config) {
				$scope.httpError(status);
			});
			
			//着陆平台赋值
			$scope.callApi({
				url : this.getWebRoot() + "/api/mktDic/queryDicCommonList",
				method : "GET",
				params : {
					type : "land_platform"
				}
			}).success(function (resp, status, headers, config) {
				if (resp.resultCode != 0) {
					$scope.failToOperate(resp);
					return;
				}
				var arr = {};
				var array = new Array();
				arr.id = '-1';
				arr.name = '全部';
				$scope.landPlatforms = array.concat(arr,resp.results);					
				$scope.platforms = resp.results;
				
			}).error(function (response, status, headers, config) {
				$scope.httpError(status);
			});
			
        	//端口所属赋值
			$scope.callApi({
				url : this.getWebRoot() + "/api/mktDic/queryDicCommonList",
				method : "GET",
				params : {
					type : "port"
				}
			}).success(function (resp, status, headers, config) {
				if (resp.resultCode != 0) {
					$scope.failToOperate(resp);
					return;
				}
				
				var arr = {};
				var array = new Array();
				arr.id = '-1';
				arr.name = '全部';
				$scope.portNames = array.concat(arr,resp.results);
			}).error(function (response, status, headers, config) {
				$scope.httpError(status);
			});
			
			
			//web名称赋值
			$scope.callApi({
				url : this.getWebRoot() + "/api/mktDic/queryDicWebNameList",
				method : "GET",
				params : {
					type : "web"
				}
			}).success(function (resp, status, headers, config) {
				if (resp.resultCode != 0) {
					$scope.failToOperate(resp);
					return;
				}
				var arr = {};
				var array = new Array();
				arr.id = '-1';
				arr.name = '全部';
				$scope.webNames = array.concat(arr,resp.results);
			}).error(function (response, status, headers, config) {
				$scope.httpError(status);
			});
			
			
			//批量发布
			$scope.batchPublish = function () {

				dialog({
					content : "确定批量生成监控代码？",
					cancel : true,
					lock : true,
					okValue : getLocalTag('confirm', 'Yes'),
					cancelValue : getLocalTag('cancel', 'cancel'),
					ok : function () {

						var state = $scope.queryAdState;

						if (state == 0 || state == 1 || state == 3) {
							dialog("过滤当前状态不是待生成监控代码，请重新选择", function () {
								return true
							}).showModal();
							return;
						}

						var landPlatform = $scope.landPlatform;
						if (typeof(landPlatform) == "undefined" || landPlatform == "") {
							landPlatform = -1;
						}

						var queryDate = $scope.queryDate;
						if (typeof(queryDate) == "undefined") {
							queryDate = '';
						}
						var queryDateBeginDay = queryDate.split('-')[0];
						var queryDateEndDay = queryDate.split('-')[1];

						var params = {
							__userKey : __userKey,
							mktId : $scope.queryMktId,
							adState : 2,
							monitorPlatform : 0,
							adInfoWebName : $scope.queryAdInfoWebName,
							adInfoPort : $scope.queryAdInfoPort,
							inputUser : $scope.queryInputUser,
							landPlatform : landPlatform,
							adqueryDateBeginDay : queryDateBeginDay,
							adqueryDateEndDay : queryDateEndDay
						};

						$scope.$apply(function () {
							$scope.callApi({
								url : webRoot + "/api/monitor/batchPublish",
								method : "POST",
								params : params
							}).success(function (resp, status, headers, config) {
								if (resp.resultCode != 0) {
									$scope.failToOperate(resp);
									return;
								}

								var success = resp.success;
								var total = resp.total;

								dialog("共" + total + "个广告位满足生成监控代码条件,批量生成监控代码成功 " + success + "条", function () {
									return true
								}).showModal();

								$scope.refresh();
							}).error(function (response, status, headers, config) {
								$scope.httpError(status);
							});
						});
					}
				}).showModal(); ;

			};
        	
			
        	 $scope.isModify = function(bicode,adstate) {
             	return false;
             };
             
             
             $scope.isPublish = function(bicode,adstate) {
              	if(adstate == 2)
          		{
              		return true;
          		}
              	
              	return false;
              };
        	
              
        	
              $scope.isReset = function(adstate) {
                	if(adstate == 3)
            		{
                		return true;
            		}
                	
                	return false;
                };
            
            $scope.modelPublish = function(adInfoId) {
             dialog({
	    			title: getLocalTag("prompt", "Prompt"),
	        		content:getLocalTag("sureToProduce", "Are you sure to produce it?"),
	    			cancel:true,
	    			lock:true,
		    		okValue: getLocalTag('confirm','Yes'),
	        		cancelValue: getLocalTag('cancel','cancel'),
	        		ok: function() {
	        			$scope.$apply(function(){
	        				$scope.callApi({
	            	        	url: webRoot + "/api/monitor/publish", 
	                     		method:"PUT",
	                     		params:{adInfoId:adInfoId}
	                 		}).success(function(resp, status, headers, config){
	                            if(resp.resultCode != 0) {
	                 	         	$scope.failToOperate(resp);
	                                return;
	                            }
	                            
	                            $scope.refresh();
	                            $scope.successToOperate(resp, $scope);
	             		    }).error(function(response, status, headers, config){
	             		    	$scope.httpError(status);
	             			});
	        			});
	        		}
	        	}).showModal();
            	
            };
            
            
           $scope.modelReset = function(adInfoId) {
        	   dialog({
	    			title: getLocalTag("prompt", "Prompt"),
	    			content:getLocalTag("sureToReset", "Are you sure to reset it?"),
	    			cancel:true,
	    			lock:true,
		    		okValue: getLocalTag('confirm','Yes'),
	        		cancelValue: getLocalTag('cancel','cancel'),
	        		ok: function() {
	        			$scope.$apply(function(){
	        				$scope.callApi({
	            	        	url: webRoot + "/api/monitor/reset", 
	                     		method:"PUT",
	                     		params:{adInfoId:adInfoId}
	                 		}).success(function(resp, status, headers, config){
	                            if(resp.resultCode != 0) {
	                 	         	$scope.failToOperate(resp);
	                                return;
	                            }
	                            
	                            $scope.refresh();
	                            $scope.successToOperate(resp, $scope);
	             		    }).error(function(response, status, headers, config){
	             		    	$scope.httpError(status);
	             			});
	        			});
	        		}
	        	}).showModal();
            	
            }; 
            
            $scope.TimeCheck =  function (checkTime) 
			{
			    var BeginDay = checkTime.split('-')[0];
				var EndDay = checkTime.split('-')[1];
			    var timeExample = /^(\d{4}\d{2}\d{2})$/;
			    if (!timeExample.test(BeginDay))
			    {
			        dialog('时间格式非法，请重新输入', function(){}).showModal();
			        return false;
			    }
			    if (!timeExample.test(EndDay))
			    {
			        dialog('时间格式非法，请重新输入', function(){}).showModal();
			        return false;
			    }
			    if(BeginDay > EndDay)
			    {
			        dialog('时间格式非法，请重新输入', function(){}).showModal();
			        return false;
			    }
			    var resultBeginDay = new Array();
			    resultBeginDay[0] = BeginDay.substring(0,4);
			    resultBeginDay[1] = BeginDay.substring(4,6);
			    resultBeginDay[2] = BeginDay.substring(6,8);
			    var resultEndDay  = new Array();
			    resultEndDay[0] = EndDay.substring(0,4);
			    resultEndDay[1] = EndDay.substring(4,6);
			    resultEndDay[2] = EndDay.substring(6,8);
			    if (resultBeginDay == null || resultEndDay == null)
			    {
			        dialog('时间格式非法，请重新输入', function(){}).showModal();
			        return false;
			    }
			    var d1 = new Date(resultBeginDay[0], resultBeginDay[1] - 1, resultBeginDay[2]);
			    var d2 = new Date(resultEndDay[0], resultEndDay[1] - 1, resultEndDay[2]);
			    if((d1.getFullYear() == resultBeginDay[0] && (d1.getMonth() + 1) == resultBeginDay[1] && d1.getDate() == resultBeginDay[2]) && (d2.getFullYear() == resultEndDay[0] && (d2.getMonth() + 1) == resultEndDay[1] && d2.getDate() == resultEndDay[2]))
			    {
			        return true;
			    }
			    dialog('时间格式非法，请重新输入', function(){}).showModal();
		        return false;
			};
			
			$scope.mktMonitorEmail = function()
			{

				 var queryMktId = $scope.queryMktId;
				 var queryAdState = $scope.queryAdState;
				 
				 if(typeof(queryAdState) == "undefined" || queryAdState =="")
	         	 {
					 queryAdState = -1;
	         	 }
				 
				 var adInfoWebName = $scope.queryAdInfoWebName;
				 if(typeof(adInfoWebName) == "undefined" || adInfoWebName =="")
	         	 {
					 adInfoWebName = -1;
	         	 }
				 
				 var adInfoPort = $scope.queryAdInfoPort;
				 if(typeof(adInfoPort) == "undefined" || adInfoPort =="")
	         	 {
					 adInfoPort = -1;
	         	 }
				 
				 var queryInputUser = $scope.queryInputUser;
				 //var queryAdInfoDeliveryTimes = $scope.queryAdInfoDeliveryTimes;
				 var queryDate = $scope.queryDate;
				 
				 var queryAdInfoDeliveryBeginDay = null;
				 var queryAdInfoDeliveryEndDay   = null;
				 /*if(typeof(queryAdInfoDeliveryTimes) != "undefined" && queryAdInfoDeliveryTimes !="")
	         	 {
					 queryAdInfoDeliveryBeginDay = queryAdInfoDeliveryTimes.split('-')[0];
					 queryAdInfoDeliveryEndDay = queryAdInfoDeliveryTimes.split('-')[1];
	         	 }*/
				 
				 var landPlatform = $scope.landPlatform;
				 if(typeof(landPlatform) == "undefined" || landPlatform =="")
	         	 {
					 landPlatform = -1;
	         	 }
				 
				 var queryDateBeginDay  = null;
				 var queryDateEndDay  = null;
				 if(typeof(queryDate) != "undefined" && queryDate !="")
	         	 {
	            	 queryDateBeginDay = queryDate.split('-')[0];
					 queryDateEndDay = queryDate.split('-')[1];
	         	 }
				 
				 
				 if(typeof(queryMktId) == "undefined" || queryMktId == "" || queryMktId == "-1")
				{
					 dialog("请先选择营销活动名称再发送通知邮件！",function(){return true}).showModal();
					 return;
				 }
				
				 $.ajax({
						type : "GET",
						url : webRoot + "/api/monitorInfo/queryMktMonitorEmailUserInfo",
						async : false,
						data : {
							__userKey : __userKey,
							mktId : queryMktId
						},
						success : function (resp) {
							if (resp.resultCode != 0) {
								return;
							}
							$scope.mktMonitorEmailUsers = resp.result;
							var userObj = resp.result;
							
							dialog({
								title : getLocalTag("id_mktMonitorInfoEmail_dialog", "选择要通知的人"),
								ok : function () {
									var users = "";
									for(var i=0;i<userObj.length;i++)
									{
										users += userObj[i].account + ","
									}
				                    
										var paramsobj = {
												__userKey : __userKey,
												mktId : queryMktId,
									   			adState : queryAdState,
									   			adInfoWebName : adInfoWebName,
									   			adInfoPort : adInfoPort,
									   			inputUser : queryInputUser,
									   			adInfoDeliveryBeginDay : queryAdInfoDeliveryBeginDay,
									   			adInfoDeliveryEndDay : queryAdInfoDeliveryEndDay,									   			
									   			adqueryDateBeginDay : queryDateBeginDay,
									   			adqueryDateEndDay : queryDateEndDay,
									   			users : users,
									   			adExportType : 1,
									   			landPlatform : landPlatform
									   			};
									   	var params = $.param(paramsobj,false);
										$.ajax({
									   		type : "POST",
									   		url : webRoot + "/api/monitorInfo/emailMonitorUsers",
									   		async : false,
									   	    data:params,
									   		success : function (resp) {
									   			if (resp.resultCode != 0) {
									   				return false;
									   			}
									   			return true;
									   		}
									   	});
										
										
									 
									return true;
								},
								okValue : "发出邮件",
								content : document.getElementById('id_mktMonitorInfoEmail_dialog'),
								lock : true,
								opacity : 0.3
							}).showModal();
						}
					});
				 
			};
        }
              
    };

    exports.instance = function(webRoot, metaName, segments) {
    	var obj = MetaBase.instance(webRoot, metaName, segments);
    	$.extend(obj, MetaMktMonitor);
    	return obj;
    };
});