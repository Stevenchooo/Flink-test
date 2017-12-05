define(function (require, exports, module) {
	var MetaBase = require('meta');
	require('dialog');
	require('jquery');
	require('calendar');
	require('jquery/calendar/1.0/tinycal.css');

	var MetaMktInfo = {

			setRange : function ($scope, start, end) {
				$scope.query_startTime = start.year + '-' + $.addZero(start.month + 1) + '-' + $.addZero(start.day);
				$scope.query_endTime = end.year + '-' + $.addZero(end.month + 1) + '-' + $.addZero(end.day);
			},
			
			
			updateMktUserInfo : function($scope,webRoot,mktinfoId) {
				var users = $scope.mktUsers;
				
				$scope.callApi({
					url : webRoot + "/api/mktInfo/updateMktUsers",
					method : "GET",
					params : {
						mktinfoId : mktinfoId,
						users : users
					}
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					

				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});
				
			},			

			initCaledar : function ($scope) {
				$.fn.calendar({
					target : "#mktinfoReserveStartTime",
					mode : 'single',
					ok : function (checked, target) {
						$scope.mktinfoReserveStartTime = checked[0].year + '-' + $.addZero(checked[0].month + 1) + '-' + $.addZero(checked[0].day);
						$scope.$digest();
						return true;
					}

				});

				$.fn.calendar({
					target : "#mktinfoReserveEndTime",
					mode : 'single',
					ok : function (checked, target) {
						$scope.mktinfoReserveEndTime = checked[0].year + '-' + $.addZero(checked[0].month + 1) + '-' + $.addZero(checked[0].day);
						$scope.$digest();
						return true;
					}

				});

				$.fn.calendar({
					target : "#mktinfoPurchaseStartTime",
					mode : 'single',
					ok : function (checked, target) {
						$scope.mktinfoPurchaseStartTime = checked[0].year + '-' + $.addZero(checked[0].month + 1) + '-' + $.addZero(checked[0].day);
						$scope.$digest();
						return true;
					}

				});

				$.fn.calendar({
					target : "#mktinfoDeliveryStartTime",
					mode : 'single',
					ok : function (checked, target) {
						$scope.mktinfoDeliveryStartTime = checked[0].year + '-' + $.addZero(checked[0].month + 1) + '-' + $.addZero(checked[0].day);
						$scope.$digest();
						return true;
					}

				});

				$.fn.calendar({
					target : "#mktinfoDeliveryEndTime",
					mode : 'single',
					ok : function (checked, target) {
						$scope.mktinfoDeliveryEndTime = checked[0].year + '-' + $.addZero(checked[0].month + 1) + '-' + $.addZero(checked[0].day);
						$scope.$digest();
						return true;
					}

				});
			},

			querySubPara : function ($scope, params) {
				if ($scope.queryName) {
					$.extend(params, {
						name : $scope.queryName
					});
				}
				return {
					method : 'GET',
					params : params,
					url : this.webRoot + "/api/mktInfo/query"
				};
			},

			removePara : function ($scope, pid, mktinfoId) {
				return {
					method : 'GET',
					url : this.webRoot + "/api/mktInfo/remove",
					params : {
						pid : pid,
						mktinfoId : mktinfoId
					}
				};
			},

			modifyPara : function ($scope, mktinfoId, pid) {
				this.initCaledar($scope);
				return {
					method : 'POST',
					url : this.getWebRoot() + "/api/mktInfo/modify",
					params : {
						pId : mktinfoId,
						pName : $scope.mktinfoName = '' ? null : $scope.mktinfoName,
						pProduct : $scope.mktinfoProduct = '' ? null : $scope.mktinfoProduct,
						pSalePoint : $scope.mktinfoSalePoint = '' ? null : $scope.mktinfoSalePoint,
						pslogan : $scope.mktinfoSlogan = '' ? null : $scope.mktinfoSlogan,
						pStrategicPosition : $scope.mktinfoStrategicPosition = '' ? null : $scope.mktinfoStrategicPosition,
						pTargetPopulation : $scope.mktinfoTargetPopulation = '' ? null : $scope.mktinfoTargetPopulation,
						pExpectedPrice : $scope.mktinfoExpectedPrice = '' ? null : $scope.mktinfoExpectedPrice,
						pMarketPace : $scope.mktinfoMarketPace = '' ? null : $scope.mktinfoMarketPace,
						pPlatform : $scope.mktinfoPlatform = '' ? null : $scope.mktinfoPlatform,
						pBudget : $scope.mktinfoBudget = '' ? null : $scope.mktinfoBudget,
						pPurchaseMethod : $scope.mktinfoPurchaseMethod = '' ? null : $scope.mktinfoPurchaseMethod,
						pReserveStartTime : $scope.mktinfoReserveStartTime = '' ? null : $scope.mktinfoReserveStartTime,
						pReserveEndTime : $scope.mktinfoReserveEndTime = '' ? null : $scope.mktinfoReserveEndTime,
						pPurchaseStartTime : $scope.mktinfoPurchaseStartTime = '' ? null : $scope.mktinfoPurchaseStartTime,
						pDeliveryStartTime : $scope.mktinfoDeliveryStartTime = '' ? null : $scope.mktinfoDeliveryStartTime,
						pDeliveryEndTime : $scope.mktinfoDeliveryEndTime = '' ? null : $scope.mktinfoDeliveryEndTime
					}
				};
			},

			modifyDialog : function (mktinfoId, $scope) {
				this.initCaledar($scope);
				$scope.callApi({
					url : this.getWebRoot() + "/api/mktInfo/detail",
					method : "GET",
					params : {
						mktinfoId : mktinfoId
					}
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					var mktInfo = resp;
					$scope.mktinfoName = mktInfo.mktinfoName;
					$scope.mktinfoProduct = mktInfo.mktinfoProduct;
					$scope.mktinfoSalePoint = mktInfo.mktinfoSalePoint;
					$scope.mktinfoSlogan = mktInfo.mktinfoSlogan;
					$scope.mktinfoStrategicPosition = mktInfo.mktinfoStrategicPosition;
					$scope.mktinfoTargetPopulation = mktInfo.mktinfoTargetPopulation;
					$scope.mktinfoExpectedPrice = mktInfo.mktinfoExpectedPrice;
					$scope.mktinfoMarketPace = mktInfo.mktinfoMarketPace;
					$scope.mktinfoPlatform = mktInfo.mktinfoPlatform;
					$scope.mktinfoBudget = mktInfo.mktinfoBudget;
					$scope.mktinfoPurchaseMethod = mktInfo.mktinfoPurchaseMethod;

					if (typeof(mktInfo.mktinfoReserveStartTime) != "undefined" && mktInfo.mktinfoReserveStartTime != "0000-00-00 00:00:00") {
						$scope.mktinfoReserveStartTime = mktInfo.mktinfoReserveStartTime;
					} else {
						$scope.mktinfoReserveStartTime = null;
					}

					if (typeof(mktInfo.mktinfoReserveEndTime) != "undefined" && mktInfo.mktinfoReserveEndTime != "0000-00-00 00:00:00") {
						$scope.mktinfoReserveEndTime = mktInfo.mktinfoReserveEndTime;
					} else {
						$scope.mktinfoReserveEndTime = null;
					}

					if (typeof(mktInfo.mktinfoReserveEndTime) != "undefined" && mktInfo.mktinfoReserveEndTime != "0000-00-00 00:00:00") {
						$scope.mktinfoPurchaseStartTime = mktInfo.mktinfoPurchaseStartTime;
					} else {
						$scope.mktinfoReserveEndTime = null;
					}

					$scope.mktinfoDeliveryStartTime = mktInfo.mktinfoDeliveryStartTime;
					$scope.mktinfoDeliveryEndTime = mktInfo.mktinfoDeliveryEndTime;
				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});
				return 'id_create_mktInfo_dialog';
			},

			createDialog : function ($scope) {
				$scope.mktinfoName = '';
				$scope.mktinfoProduct = '';
				$scope.mktinfoName = '';
				$scope.mktinfoProduct = '';
				$scope.mktinfoSalePoint = '';
				$scope.mktinfoSlogan = '';
				$scope.mktinfoStrategicPosition = '';
				$scope.mktinfoTargetPopulation = '';
				$scope.mktinfoExpectedPrice = '';
				$scope.mktinfoMarketPace = '';
				$scope.mktinfoPlatform = '';
				$scope.mktinfoBudget = '';
				$scope.mktinfoPurchaseMethod = '';
				$scope.mktinfoReserveStartTime = '';
				$scope.mktinfoReserveEndTime = '';
				$scope.mktinfoPurchaseStartTime = '';
				$scope.mktinfoDeliveryStartTime = '';
				$scope.mktinfoDeliveryEndTime = '';
				$scope.mktInfoCreateForm.$setPristine();
				return 'id_create_mktInfo_dialog';
			},

			createPara : function ($scope, pid, meta) {
				this.initCaledar($scope);
				var mktInfoName = $scope.mktinfoName;
				var exists = false;
				$.ajax({
					type : "POST",
					url : this.getWebRoot() + "/api/mktInfo/exists",
					async : false,
					data : {
						__userKey : __userKey,
						modelId : pid,
						mktInfoName : mktInfoName
					},
					success : function (resp) {
						if (resp.resultCode != 0) {
							return;
						}
						exists = resp.exists;
					}
				});

				if (exists) {
					dialog({
						title : getLocalTag('error', 'error'),
						content : getLocalTag('mktInfoAlreadyExists', 'mktInfo already exists'),
						okValue : getLocalTag('confirm', 'Yes'),
						ok : true
					}).showModal();;
					return null;
				}

				return {
					method : "POST",
					url : this.getWebRoot() + "/api/mktInfo/create",
					params : {
						pid : pid,
						pName : $scope.mktinfoName = '' ? null : $scope.mktinfoName,
						pProduct : $scope.mktinfoProduct = '' ? null : $scope.mktinfoProduct,
						pSalePoint : $scope.mktinfoSalePoint = '' ? null : $scope.mktinfoSalePoint,
						pslogan : $scope.mktinfoSlogan = '' ? null : $scope.mktinfoSlogan,
						pStrategicPosition : $scope.mktinfoStrategicPosition = '' ? null : $scope.mktinfoStrategicPosition,
						pTargetPopulation : $scope.mktinfoTargetPopulation = '' ? null : $scope.mktinfoTargetPopulation,
						pExpectedPrice : $scope.mktinfoExpectedPrice = '' ? null : $scope.mktinfoExpectedPrice,
						pMarketPace : $scope.mktinfoMarketPace = '' ? null : $scope.mktinfoMarketPace,
						pPlatform : $scope.mktinfoPlatform = '' ? null : $scope.mktinfoPlatform,
						pBudget : $scope.mktinfoBudget = '' ? null : $scope.mktinfoBudget,
						pPurchaseMethod : $scope.mktinfoPurchaseMethod = '' ? null : $scope.mktinfoPurchaseMethod,
						pReserveStartTime : $scope.mktinfoReserveStartTime = '' ? null : $scope.mktinfoReserveStartTime,
						pReserveEndTime : $scope.mktinfoReserveEndTime = '' ? null : $scope.mktinfoReserveEndTime,
						pPurchaseStartTime : $scope.mktinfoPurchaseStartTime = '' ? null : $scope.mktinfoPurchaseStartTime,
						pDeliveryStartTime : $scope.mktinfoDeliveryStartTime = '' ? null : $scope.mktinfoDeliveryStartTime,
						pDeliveryEndTime : $scope.mktinfoDeliveryEndTime = '' ? null : $scope.mktinfoDeliveryEndTime

					}
				};
			},

			createCheck : function ($scope) {
				this.initCaledar($scope);
				var flag = $scope.mktInfoCreateForm.$valid;

				if (!flag) {
					return false;
				}

				var pReserveStartTime = $scope.mktinfoReserveStartTime;
				var pReserveEndTime = $scope.mktinfoReserveEndTime;
				var pDeliveryStartTime = $scope.mktinfoDeliveryStartTime;
				var pDeliveryEndTime = $scope.mktinfoDeliveryEndTime;

				if (pDeliveryStartTime > pDeliveryEndTime) {
					dialog({
						title : getLocalTag('error', 'error'),
						content : getLocalTag('mktInfoDeliverTimeError', 'mktInfoDeliverTimeError'),
						okValue : getLocalTag('confirm', 'Yes'),
						ok : true
					}).showModal();
					return false;
				}

				if (typeof(pReserveStartTime) != "undefined" && pReserveStartTime != ""
					 && typeof(pReserveEndTime) != "undefined" && pReserveEndTime != ""
					 && pReserveStartTime > pReserveEndTime) {
					dialog({
						title : getLocalTag('error', 'error'),
						content : getLocalTag('mktInfoReserveTimeError', 'mktInfoReserveTimeError'),
						okValue : getLocalTag('confirm', 'Yes'),
						ok : true
					}).showModal();
					return false;
				}

				return true;

			},

			modifyCheck : function ($scope) {
				var flag = $scope.mktInfoCreateForm.$valid;
				this.initCaledar($scope);
				if (!flag) {
					return false;
				}

				var pReserveStartTime = $scope.mktinfoReserveStartTime;
				var pReserveEndTime = $scope.mktinfoReserveEndTime;
				var pDeliveryStartTime = $scope.mktinfoDeliveryStartTime;
				var pDeliveryEndTime = $scope.mktinfoDeliveryEndTime;

				if (pDeliveryStartTime > pDeliveryEndTime) {
					dialog({
						title : getLocalTag('error', 'error'),
						content : getLocalTag('mktInfoDeliverTimeError', 'mktInfoDeliverTimeError'),
						okValue : getLocalTag('confirm', 'Yes'),
						ok : true
					});
					return false;
				}

				if (typeof(pReserveStartTime) != "undefined" && pReserveStartTime != ""
					 && typeof(pReserveEndTime) != "undefined" && pReserveEndTime != ""
					 && pReserveStartTime > pReserveEndTime) {
					dialog({
						title : getLocalTag('error', 'error'),
						content : getLocalTag('mktInfoReserveTimeError', 'mktInfoReserveTimeError'),
						okValue : getLocalTag('confirm', 'Yes'),
						ok : true
					});
					return false;
				}

				return true;
			},

			extendScope : function ($scope, $http) {
				var webRoot = this.getWebRoot();

				this.initCaledar($scope);

				$scope.callApi({
					url : this.getWebRoot() + "/api/mktDic/queryDicList",
					method : "GET",
					params : {
						type : "product"
					}
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					$scope.products = resp.results;
				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});

				$scope.callApi({
					url : this.getWebRoot() + "/api/mktDic/queryDicCommonList",
					method : "GET",
					params : {
						type : "platform"
					}
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					$scope.platforms = resp.results;

				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});

				$scope.callApi({
					url : this.getWebRoot() + "/api/mktDic/queryDicCommonList",
					method : "GET",
					params : {
						type : "purchase_method"
					}
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					$scope.methods = resp.results;
				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});

				$scope.mktInfoDetail = function (mktinfoId) {
					
                    ////////////////////////////////////
		    		var my = document.getElementById("_calendar_div");
		    	    if (my != null)
		    	        my.parentNode.removeChild(my);
		    	    ///////////////////////////////////
					$scope.callApi({
						url : webRoot + "/api/mktInfo/detail",
						method : "GET",
						params : {
							mktinfoId : mktinfoId
						}
					}).success(function (resp, status, headers, config) {
						if (resp.resultCode != 0) {
							$scope.failToOperate(resp);
							return;
						}
						var mktInfo = resp;
						$scope.mktinfoName = mktInfo.mktinfoName;
						$scope.mktinfoProduct = mktInfo.mktinfoProduct;
						$scope.mktinfoSalePoint = mktInfo.mktinfoSalePoint;
						$scope.mktinfoSlogan = mktInfo.mktinfoSlogan;
						$scope.mktinfoStrategicPosition = mktInfo.mktinfoStrategicPosition;
						$scope.mktinfoTargetPopulation = mktInfo.mktinfoTargetPopulation;
						$scope.mktinfoExpectedPrice = mktInfo.mktinfoExpectedPrice;
						$scope.mktinfoMarketPace = mktInfo.mktinfoMarketPace;
						$scope.mktinfoPlatform = mktInfo.mktinfoPlatform;
						$scope.mktinfoBudget = mktInfo.mktinfoBudget;
						$scope.mktinfoPurchaseMethod = mktInfo.mktinfoPurchaseMethod;
						if (typeof(mktInfo.mktinfoReserveStartTime) != "undefined" && mktInfo.mktinfoReserveStartTime != "0000-00-00 00:00:00") {
							$scope.mktinfoReserveStartTime = mktInfo.mktinfoReserveStartTime;
						} else {
							$scope.mktinfoReserveStartTime = null;
						}

						if (typeof(mktInfo.mktinfoReserveEndTime) != "undefined" && mktInfo.mktinfoReserveEndTime != "0000-00-00 00:00:00") {
							$scope.mktinfoReserveEndTime = mktInfo.mktinfoReserveEndTime;
						} else {
							$scope.mktinfoReserveEndTime = null;
						}

						if (typeof(mktInfo.mktinfoReserveEndTime) != "undefined" && mktInfo.mktinfoReserveEndTime != "0000-00-00 00:00:00") {
							$scope.mktinfoPurchaseStartTime = mktInfo.mktinfoPurchaseStartTime;
						} else {
							$scope.mktinfoReserveEndTime = null;
						}
						$scope.mktinfoDeliveryStartTime = mktInfo.mktinfoDeliveryStartTime;
						$scope.mktinfoDeliveryEndTime = mktInfo.mktinfoDeliveryEndTime;
					}).error(function (response, status, headers, config) {
						$scope.httpError(status);
					});

					dialog({
						title : getLocalTag("id_create_mktInfo_dialog", "详情"),
						ok : true,
						okValue : getLocalTag('confirm', 'Yes'),
						content : document.getElementById('id_create_mktInfo_dialog'),
						lock : true,
						opacity : 0.3
					}).showModal();
				};

				$scope.mktEmail = function (mktinfoId) {
					$.ajax({
						type : "GET",
						url : webRoot + "/api/mktInfo/queryMktEmailUserInfo",
						async : false,
						data : {
							__userKey : __userKey,
							pageInfo : "adInfo",
							mktinfoId : mktinfoId,
							adInfoId : null
						},
						success : function (resp) {
							if (resp.resultCode != 0) {
								return;
							}
							$scope.mktEmailUsers = resp.result;
							
							
							dialog({
								title : getLocalTag("id_mktEmail_dialog", "选择要通知的人"),
								ok : function () {
									var users = "";
									 $("[name='emailUsers']:checked").each(function(index, element) {
										 users += $(this).val() + ",";
				                     });
				                    
									 if(users == "")
									 {
										 dialog('请选择通知的人', function(){}).showModal();
										 return;
									 }
										var paramsobj = {
												__userKey : __userKey,
									   			mktinfoId : mktinfoId,
									   			users : users};
									   	var params = $.param(paramsobj,false);
										$.ajax({
									   		type : "POST",
									   		url : webRoot + "/api/mktInfo/emailMktUsers",
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
								content : document.getElementById('id_mktEmail_dialog'),
								lock : true,
								opacity : 0.3
							}).showModal();
						}
					});

				}
				
				
				$scope.mktEnableUser = function (mktinfoId) {

					$.ajax({
						type : "GET",
						url : webRoot + "/api/mktInfo/queryMktUserInfo",
						async : false,
						data : {
							__userKey : __userKey,
							mktinfoId : mktinfoId
						},
						success : function (resp) {
							if (resp.resultCode != 0) {
								return;
							}
							$scope.mktUsers = resp.result;
							
							dialog({
								title : getLocalTag("id_mktUser_dialog", "设置账号权限"),
								ok : function () {
									var usertemp = $scope.mktUsers;
									var users = '';
									for(var i=0; i<usertemp.length; i++)
									{
									   var account = usertemp[i].account;
									   var flag = usertemp[i].flag;
									   
									   if(flag != 2)
									   {
										   if(users == '')
										   {
											   users =  account + ',' + flag;
										   }
										   else
										   {
											   users =  account + ',' + flag + ';' + users;
										   }
										   
									   }
									}
									var paramsobj = {
											__userKey : __userKey,
								   			mktinfoId : mktinfoId,
								   			users : users};
								   	var params = $.param(paramsobj,false);
									$.ajax({
								   		type : "POST",
								   		url : webRoot + "/api/mktInfo/updateMktUsers",
								   		async : false,
								   		data : {
								   			
								   		},
								   	    data:params,
								   		success : function (resp) {
								   			if (resp.resultCode != 0) {
								   				return false;
								   			}
								   			return true;
								   		}
								   	});
									
										
								},
								okValue : getLocalTag('confirm', 'Yes'),
								content : document.getElementById('id_mktUser_dialog'),
								lock : true,
								opacity : 0.3
							}).showModal();

						}
					});

				}
			}

		};

	  exports.instance = function(webRoot, metaName, segments) {
	    	var obj = MetaBase.instance(webRoot, metaName, segments);
	    	$.extend(obj, MetaMktInfo);
	    	return obj;
	    };
});