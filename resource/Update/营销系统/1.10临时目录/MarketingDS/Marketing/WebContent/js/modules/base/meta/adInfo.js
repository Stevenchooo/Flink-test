define(function (require, exports, module) {
	var MetaBase = require('meta');
	require('dialog');
    require('jquery');
	require('calendar');
	require('jquery/ajaxupload/ajaxfileupload.js');
	require('jquery/calendar/1.0/tinycal.css');

	var MetaAdInfo = {

			setRange : function ($scope, start, end) {
				$scope.query_startTime = start.year + '-' + $.addZero(start.month + 1) + '-' + $.addZero(start.day);
				$scope.query_endTime = end.year + '-' + $.addZero(end.month + 1) + '-' + $.addZero(end.day);
			},

			initCaledar : function ($scope) {
				$.fn.calendar({
					target : "#adInfoDeliveryTimes",
					mode : 'multi',
					recordCheck:false,
					ok : function (checked, target) {

						var length = checked.length;
						if(length > 2)
						{
							dialog('最多只能够选择两个时间,请重新选择', function(){}).showModal();;
							return;
						}
						
						
						var day0 = checked[0].year + '' + $.addZero(checked[0].month + 1) + '' + $.addZero(checked[0].day);
						
						if(length == 2)
						{
							var day1 = checked[1].year + '' + $.addZero(checked[1].month + 1) + '' + $.addZero(checked[1].day);
							
							if(day0 <= day1)
							{
								$scope.adInfoDeliveryTimes = day0 + '-' + day1;
							}
							else
							{
								$scope.adInfoDeliveryTimes = day1 + '-' + day0;
							}
	
							
						}
						else
						{
							$scope.adInfoDeliveryTimes = day0 + '-' + day0;
						}
						
						
						$scope.$digest();
						return true;
					}

				});
				
				$.fn.calendar({
					target : "#queryDate",
					mode : 'multi',
					recordCheck:false,
					ok : function (checked, target) {

						var length = checked.length;
						if(length > 2)
						{
							dialog('最多只能够选择两个时间,请重新选择', function(){}).showModal();;
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
				


				$.fn.calendar({
					target : "#adInfoDeliveryTimesModify",
					mode : 'multi',
					recordCheck:false,
					ok : function (checked, target) {

						var length = checked.length;
						if(length > 2)
						{
							dialog('最多只能够选择两个时间,请重新选择', function(){}).showModal();;
							return;
						}
						
						
						var day0 = checked[0].year + '' + $.addZero(checked[0].month + 1) + '' + $.addZero(checked[0].day);
						
						if(length == 2)
						{
							var day1 = checked[1].year + '' + $.addZero(checked[1].month + 1) + '' + $.addZero(checked[1].day);
	
							if(day0 <= day1)
							{
								$scope.adInfoDeliveryTimesModify = day0 + '-' + day1;
							}
							else
							{
								$scope.adInfoDeliveryTimesModify = day1 + '-' + day0;
							}
						}
						else
						{
							$scope.adInfoDeliveryTimesModify = day0 + '-' + day0;
						}
						
						$scope.$digest();
						return true;
					}

				});
				
			},
			querySubPara : function ($scope, params) {
                
                
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
                /*         
				var queryAdInfoDeliveryBeginDay = queryAdInfoDeliveryTimes.split('-')[0];
				var queryAdInfoDeliveryEndDay = queryAdInfoDeliveryTimes.split('-')[1];
				*/
				var queryDateBeginDay = queryDate.split('-')[0];
				var queryDateEndDay = queryDate.split('-')[1];
				
				$.extend(params, {
					mktId : $scope.queryMktId,
					adState : $scope.queryAdState,
					adInfoWebName : $scope.queryAdInfoWebName,
					adInfoPort : $scope.queryAdInfoPort,
					inputUser : $scope.queryInputUser,
					landPlatform : $scope.landPlatform,
					adqueryDateBeginDay  : queryDateBeginDay,
					adqueryDateEndDay  : queryDateEndDay
					
				});

				return {
					method : 'GET',
					params : params,
					url : this.webRoot + "/api/adInfo/query"
				};
			},

			removePara : function ($scope, pid, adInfoId) {
				return {
					method : 'GET',
					url : this.webRoot + "/api/adInfo/remove",
					params : {
						pid : pid,
						adInfoId : adInfoId
					}
				};
			},

			modifyPara : function ($scope, adInfoId, pid) {
				var adWebNanmeInfo = '';
				var adInfoWebName = '';
				var adInfoMediaType = '';
				
				var modifyCheckFlag = $scope.showFlagModify;
				if(typeof(modifyCheckFlag) == "undefined")
				{
					modifyCheckFlag = true;
				}
				if(modifyCheckFlag == true)
				{
					adWebNanmeInfo = $scope.adInfoWebName;
					adInfoWebName = adWebNanmeInfo.split('_')[0];
					adInfoMediaType =adWebNanmeInfo.split('_')[1];
					return {
						method : 'POST',
						url : this.getWebRoot() + "/api/adInfo/modify",
						params : {
							adInfoId : adInfoId,
							mktinfoId : $scope.mktinfoId,
							adInfoWebName : adInfoWebName,
							adInfoChannel : $scope.adInfoChannel,
							adInfoAdPosition : $scope.adInfoAdPosition,
							adInfoAdMaterialType : $scope.adInfoAdMaterialType,
							adInfoAdMaterialDesc : $scope.adInfoAdMaterialDesc,
							adInfoPort : $scope.adInfoPort,
							adInfoPlatform : $scope.adInfoPlatform,
							adInfoPlatformDesc : $scope.adInfoPlatformDesc,
							adInfoDeliveryDays : $scope.adInfoDeliveryDays,
							adInfoDeliveryTimes : $scope.adInfoDeliveryTimes,
							adInfoFlowType : $scope.adInfoFlowType,
							adInfoExpAmount : $scope.adInfoExpAmount,
							adInfoClickAmount : $scope.adInfoClickAmount,
							adInfoPublishPrice : $scope.adInfoPublishPrice,
							adInfoNetPrice : $scope.adInfoNetPrice,
							adInfoResource : $scope.adInfoResource,
							adInfoIsExposure : $scope.adInfoIsExposure,
							adInfoIsClick : $scope.adInfoIsClick,
							adInfoMonitorPlatform : $scope.adInfoMonitorPlatform,
							adInfoMediaType : adInfoMediaType
						}
					};
				}
				else
				{
					adWebNanmeInfo = $scope.adInfoWebNameModify;
					adInfoWebName = adWebNanmeInfo.split('_')[0];
					adInfoMediaType =adWebNanmeInfo.split('_')[1];
					return {
						method : 'POST',
						url : this.getWebRoot() + "/api/adInfo/modify",
						params : {
							adInfoId : adInfoId,
							mktinfoId : $scope.mktinfoIdModify,
							adInfoWebName : adInfoWebName,
							adInfoChannel : $scope.adInfoChannelModify,
							adInfoAdPosition : $scope.adInfoAdPositionModify,
							adInfoAdMaterialType : $scope.adInfoAdMaterialTypeModify,
							adInfoAdMaterialDesc : $scope.adInfoAdMaterialDescModify,
							adInfoPort : $scope.adInfoPortModify,
							adInfoPlatform : $scope.adInfoPlatformModify,
							adInfoPlatformDesc : $scope.adInfoPlatformDescModify,
							adInfoDeliveryDays : $scope.adInfoDeliveryDaysModify,
							adInfoDeliveryTimes : $scope.adInfoDeliveryTimesModify,
							adInfoFlowType : $scope.adInfoFlowTypeModify,
							adInfoExpAmount : $scope.adInfoExpAmountModify,
							adInfoClickAmount : $scope.adInfoClickAmountModify,
							adInfoPublishPrice : $scope.adInfoPublishPriceModify,
							adInfoNetPrice : $scope.adInfoNetPriceModify,
							adInfoResource : $scope.adInfoResourceModify,
							adInfoIsExposure : $scope.adInfoIsExposureModify,
							adInfoIsClick : $scope.adInfoIsClickModify,
							adInfoMonitorPlatform : $scope.adInfoMonitorPlatformModify,
							adInfoMediaType : adInfoMediaType
						}
					};
				}
				
				
			},

			modifyDialog : function (adInfoId, $scope) {

				var webRoot = this.getWebRoot();

				var adInfo = new Object();
				
				var returnType = 'id_create_adInfo_dialog';
				$.ajax({
					type : "GET",
					url : this.getWebRoot() + "/api/adInfo/detail",
					async : false,
					data : {
						__userKey : __userKey,
						adInfoId : adInfoId
					},
					success : function (resp) {
						if (resp.resultCode != 0) 
						{
							return;
						}

						adInfo = resp;
						if (adInfo.detailsAdInfoStateId == 0) 
						{
							$scope.showFlagModify = true;
							
							$scope.mktinfoId = adInfo.detailsmktinfoId;
							//网站名称下拉框赋值
							$scope.callApi({
								url : webRoot + "/api/mktDic/queryWebNameList",
								method : "POST",
								params : {
									mktinfoId:adInfo.detailsmktinfoId
								}
							}).success(function (resp, status, headers, config) {
								if (resp.resultCode != 0) {
									$scope.failToOperate(resp);
									return;
								}
								$scope.names = resp.results;
								$scope.adInfoWebName = adInfo.detailsMktinfoNameId + '_' + adInfo.detailsAdInfoMediaType;
							}).error(function (response, status, headers, config) {
								$scope.httpError(status);
							});
							
							$scope.adInfoChannel = adInfo.detailsAdInfoChannel;
							$scope.adInfoAdPosition = adInfo.detailsAdInfoPosition;
							$scope.adInfoAdMaterialType = adInfo.detailsMaterialTypeId;
							$scope.adInfoAdMaterialDesc = adInfo.detailsMaterialDesc;
							$scope.adInfoPort = adInfo.detailsAdInfoPortId;
							$scope.adInfoPlatform = adInfo.detailsAdInfoPlatformId;
							$scope.adInfoPlatformDesc = adInfo.detailsAdInfoPlatformDesc;
							$scope.adInfoDeliveryDays = parseInt(adInfo.detailsAdInfoDeliveryDays);
							$scope.adInfoDeliveryTimes = adInfo.detailsAdInfoDeliveryTimes;
							$scope.adInfoFlowType = adInfo.detailsAdInfoFlowTypeId;
							$scope.adInfoExpAmount = parseInt(adInfo.detailsExpAmount);
							$scope.adInfoClickAmount = parseInt(adInfo.detailsClickAmount);
							$scope.adInfoPublishPrice = parseInt(adInfo.detailsPublishPrice);
							$scope.adInfoNetPrice = parseInt(adInfo.detailsNetPrice);
							$scope.adInfoResource = adInfo.detailsAdInfoResourceId;
							$scope.adInfoIsExposure = adInfo.detailsIsExposure;
							$scope.adInfoIsClick = adInfo.detailsIsClick;
							$scope.adInfoMonitorPlatform = adInfo.detailsMonitorPlatformId;
							$scope.adInfoWebName = adInfo.detailsMktinfoNameId + '_' + adInfo.detailsAdInfoMediaType;
							
							returnType = 'id_create_adInfo_dialog';
						} 
						else{
							$scope.showFlagModify = false;
							
							$scope.mktinfoIdModify = adInfo.detailsmktinfoId;
							$scope.adInfoChannelModify = adInfo.detailsAdInfoChannel;
							$scope.adInfoAdPositionModify = adInfo.detailsAdInfoPosition;
							$scope.adInfoAdMaterialTypeModify = adInfo.detailsMaterialTypeId;
							$scope.adInfoAdMaterialDescModify = adInfo.detailsMaterialDesc;
							$scope.adInfoPortModify = adInfo.detailsAdInfoPortId;
							$scope.adInfoPlatformModify = adInfo.detailsAdInfoPlatformId;
							$scope.adInfoPlatformDescModify = adInfo.detailsAdInfoPlatformDesc;
							$scope.adInfoDeliveryDaysModify = parseInt(adInfo.detailsAdInfoDeliveryDays);
							$scope.adInfoDeliveryTimesModify = adInfo.detailsAdInfoDeliveryTimes;
							$scope.adInfoFlowTypeModify = adInfo.detailsAdInfoFlowTypeId;
							$scope.adInfoExpAmountModify = parseInt(adInfo.detailsExpAmount);
							$scope.adInfoClickAmountModify = parseInt(adInfo.detailsClickAmount);
							$scope.adInfoPublishPriceModify = parseInt(adInfo.detailsPublishPrice);
							$scope.adInfoNetPriceModify = parseInt(adInfo.detailsNetPrice);
							$scope.adInfoResourceModify = adInfo.detailsAdInfoResourceId;
							$scope.adInfoIsExposureModify = adInfo.detailsIsExposure;
							$scope.adInfoIsClickModify = adInfo.detailsIsClick;
							$scope.adInfoMonitorPlatformModify = adInfo.detailsMonitorPlatformId;
							$scope.adInfoWebNameModify = adInfo.detailsMktinfoNameId + '_' + adInfo.detailsAdInfoMediaType;
							
							returnType = 'id_modify_adInfo_dialog';
						}

					}
				});
				return returnType;
			},

			createDialog : function ($scope) {
				$scope.mktinfoId = '';
				$scope.adInfoWebName = '';
				$scope.adInfoChannel = '';
				$scope.adInfoAdPosition = '全部';
				//广告素材类型默认图片
				$scope.adInfoAdMaterialType = '0';
				$scope.adInfoAdMaterialDesc = '';
				
				//端口所属默认PC
				$scope.adInfoPort = '0';
				
				//着陆平台默认vmall
				$scope.adInfoPlatform = '0';
				$scope.adInfoPlatformDesc = '';
				$scope.adInfoDeliveryDays = 30;
				$scope.adInfoDeliveryTimes = creatDefaultDate();
				
				//引流类型默认预约引流
				$scope.adInfoFlowType = '0';
				$scope.adInfoExpAmount = '';
				$scope.adInfoClickAmount = '';
				$scope.adInfoPublishPrice = '';
				$scope.adInfoNetPrice = '';
				
				//资源类型默认BD资源
				$scope.adInfoResource = '1';
				
				//默认监控
				$scope.adInfoIsExposure = '1';
				$scope.adInfoIsClick = '1';
				$scope.adInfoMonitorPlatform = '0';
				$scope.adInfoMediaType = '';
				$scope.state = '0';
				$scope.showFlag = true;
				$scope.adInfoCreateForm.$setPristine();
				return 'id_create_adInfo_dialog';
			},

			createPara : function ($scope, pid, meta) {

				var mktinfoId = $scope.mktinfoId;
				var adWebNanmeInfo = $scope.adInfoWebName;
				var adInfoWebName = adWebNanmeInfo.split('_')[0];
				var adInfoMediaType =adWebNanmeInfo.split('_')[1];
				var adInfoChannel = $scope.adInfoChannel;
				
				var adInfoAdPosition = $scope.adInfoAdPosition;
				
				//没有广告位信息默认全部
				if(adInfoAdPosition == '')
				{
					adInfoAdPosition = '全部';
				}
				var adInfoAdMaterialType = $scope.adInfoAdMaterialType;
				var adInfoAdMaterialDesc = $scope.adInfoAdMaterialDesc;
				var adInfoPort = $scope.adInfoPort;
				var adInfoPlatform = $scope.adInfoPlatform == ''? 0 : $scope.adInfoPlatform;
				var adInfoPlatformDesc = $scope.adInfoPlatformDesc;
				var adInfoDeliveryDays = $scope.adInfoDeliveryDays;
				
				//用户不填写投放天数默认30
				if(adInfoDeliveryDays == '')
				{
					adInfoDeliveryDays = 30;
				}
				var adInfoDeliveryTimes = $scope.adInfoDeliveryTimes;
				var adInfoDeliveryBeginTime = adInfoDeliveryTimes.split('-')[0];
				var adInfoDeliveryEndTime = adInfoDeliveryTimes.split('-')[1];
				var adInfoFlowType = $scope.adInfoFlowType;
				var adInfoExpAmount = $scope.adInfoExpAmount;
				var adInfoClickAmount = $scope.adInfoClickAmount;
				var adInfoPublishPrice = $scope.adInfoPublishPrice;
				var adInfoNetPrice = $scope.adInfoNetPrice;
				var adInfoResource = $scope.adInfoResource;
				var adInfoIsExposure = ($scope.adInfoIsExposure == '')? 1 : $scope.adInfoIsExposure; 
				var adInfoIsClick = ($scope.adInfoIsClick == '') ? 1 : $scope.adInfoIsClick;
				var adInfoMonitorPlatform = $scope.adInfoMonitorPlatform;
				

				var exists = false;
				$.ajax({
					type : "GET",
					url : this.getWebRoot() + "/api/adInfo/exists",
					async : false,
					data : {
						__userKey : __userKey,
						mktinfoId : mktinfoId,
						adInfoWebName : adInfoWebName,
						adInfoChannel : adInfoChannel,
						adInfoAdPosition : adInfoAdPosition,
						adInfoPort : adInfoPort
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
						content : getLocalTag('adInfoAlreadyExists', 'adInfo already exists'),
						okValueue : getLocalTag('confirm', 'Yes'),
						ok : true
					}).showModal();
					return null;
				}

				return {
					method : "POST",
					url : this.getWebRoot() + "/api/adInfo/create",
					params : {
						mktinfoId : mktinfoId,
						adInfoWebName : adInfoWebName,
						adInfoChannel : adInfoChannel,
						adInfoAdPosition : adInfoAdPosition,
						adInfoAdMaterialType : adInfoAdMaterialType,
						adInfoPort : adInfoPort,
						adInfoPlatform : adInfoPlatform,
						adInfoPlatformDesc : adInfoPlatformDesc,
						adInfoDeliveryDays : adInfoDeliveryDays,
						adInfoDeliveryTimes : adInfoDeliveryTimes,
						adInfoFlowType : adInfoFlowType,
						adInfoExpAmount : adInfoExpAmount,
						adInfoClickAmount : adInfoClickAmount,
						adInfoPublishPrice : adInfoPublishPrice,
						adInfoNetPrice : adInfoNetPrice,
						adInfoResource : adInfoResource,
						adInfoIsExposure : adInfoIsExposure,
						adInfoIsClick : adInfoIsClick,
						adInfoMonitorPlatform : adInfoMonitorPlatform,
						adInfoMediaType : adInfoMediaType,
						adInfoAdMaterialDesc : adInfoAdMaterialDesc,
						adInfoDeliveryBeginTime : adInfoDeliveryBeginTime,
						adInfoDeliveryEndTime : adInfoDeliveryEndTime
					}
				};
			},

			createCheck : function ($scope) {
				if($scope.isTrueAdInfoAdMaterialDesc())
				{
					return false;
				}
				return $scope.adInfoCreateForm.$valid;
			},

			modifyCheck : function ($scope) {
				
				var modifyCheckFlag = $scope.showFlagModify;
				if(typeof(modifyCheckFlag) == "undefined")
				{
					modifyCheckFlag = true;
				}
				if(modifyCheckFlag == true)
				{
					if($scope.isTrueAdInfoAdMaterialDesc())
					{
						return false;
					}
					return $scope.adInfoCreateForm.$valid;
				}
				if($scope.isTrueAdInfoAdMaterialDescModify())
				{
					return false;
				}
				return $scope.adInfoModifyForm.$valid;
			},

			extendScope : function ($scope, $http) {
				var webRoot = this.getWebRoot();
				
				//初始化下拉菜单
				this.initCaledar($scope);
				
				//获取营销活动ID与名称列表
				$scope.callApi({
					url : this.getWebRoot() + "/api/mktInfo/queryIdNameListWithControl",
					method : "GET",
					params : {}
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					//搜索框赋值
					$scope.mktInfos = resp.result;
					
					//创建活动下拉框赋值
					$scope.mktInfos1 = resp.result.slice(1);
				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});

				//广告位状态下拉框赋值
				$scope.callApi({
					url : this.getWebRoot() + "/api/mktDic/queryDicCommonList",
					method : "GET",
					params : {
						type : "ad_state"
					}
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					$scope.states = resp.results;
				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});

				
				

				
				//素材类型赋值
				$scope.callApi({
					url : this.getWebRoot() + "/api/mktDic/queryDicCommonList",
					method : "GET",
					params : {
						type : "material"
					}
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					$scope.materials = resp.results;
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
					$scope.ports = resp.results;
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
				
				
				
				
				//引流类型赋值
				$scope.callApi({
					url : this.getWebRoot() + "/api/mktDic/queryDicCommonList",
					method : "GET",
					params : {
						type : "flow"
					}
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					$scope.flows = resp.results;
				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});

				//监控平台赋值
				$scope.callApi({
					url : this.getWebRoot() + "/api/mktDic/queryDicCommonList",
					method : "GET",
					params : {
						type : "monitor_platform"
					}
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					$scope.monitors = resp.results;
				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});
				

				//是否监控、曝光赋值
				$scope.callApi({
					url : this.getWebRoot() + "/api/mktDic/queryDicCommonList",
					method : "GET",
					params : {
						type : "boolean"
					}
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					$scope.clicks = resp.results;
					$scope.exposures = resp.results;
				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});

				//资源类型赋值
				$scope.callApi({
					url : this.getWebRoot() + "/api/mktDic/queryDicCommonList",
					method : "GET",
					params : {
						type : "resource"
					}
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					$scope.resources = resp.results;
				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});

			

				//广告位详情
				$scope.adInfoDetail = function (adInfoId) {
					$scope.callApi({
						url : webRoot + "/api/adInfo/detail",
						method : "GET",
						params : {
							adInfoId : adInfoId
						}
					}).success(function (resp, status, headers, config) {
						if (resp.resultCode != 0) {
							$scope.failToOperate(resp);
							return;
						}

						$scope.rowDetails = resp;

					}).error(function (response, status, headers, config) {
						$scope.httpError(status);
					});

					dialog({
						title : getLocalTag("id_adInfo_details_dialog", "详情"),
						ok : true,
						okValue : getLocalTag('confirm', 'Yes'),
						content : document.getElementById('id_adInfo_details_dialog'),
						lock : true,
						opacity : 0.3
					}).showModal();;
				};

				//批量导入信息导出
				$scope.adBatchResDownLoad = function(){
					var fileId = $("#fileId").val();
					var url = $scope.options.meta.webRoot + "/api/adInfo/adBatchResDownLoad?__userKey=" + __userKey + "&fileId=" + fileId;
					location.href = url;
				};
				
				
				$scope.batchFileUpLoad = function(){
					var flag = $("#dealFlag").val();
					var fileName = $("#uploadFile").val();
					if(null == fileName || "" == fileName)
					{
						dialog("请先选择上传excel模板文件",function(){return true}).showModal();;
						return;
					}
					var reg = /.*.xls$/;
					if(!reg.test(fileName))
					{
						dialog("上传excel模板文件类型必须为 Excel 97-2003 工作表，请先修改文件！ ",function(){return true}).showModal();;
						return;
					}
					
					if(0 == flag)
					{
						$("#dealFlag").val(1);
						$("#batchResDown").hide();
						
						var batchFlag = ($scope.batchFlag == true) ? true : false;
						
						$("#res_div").html("<div class='processLoading'><span style='vertical-align: middle;'>文件正在导入，请稍后。</span></div>");
						var url = webRoot + "/api/adInfo/uploadFile?__userKey=" + __userKey + "&batchFlag=" + batchFlag;
					    $.ajaxFileUpload
					    (
					        {
					            url:url,//用于文件上传的服务器端请求地址
					            secureuri:false,//一般设置为false
					            fileElementId:'uploadFile',//文件上传空间的id属性  <input type="file" id="file" name="file" />
					            dataType : 'text',
					            success: function (data, status)  
					            {
					            	
					            	 $("#queryButton").trigger('click');
					            	 var beginIndex = data.indexOf("{");
					            	 var endIndex = data.indexOf("}");
					            	 var objStr = data.substring(beginIndex,endIndex + 1);
					            	 var obj = eval("("+objStr+")");
					            	 var total = obj.total;
					            	 var createSuccess= obj.createSuccess;
					            	 var updateSuccess= obj.updateSuccess;
					            	 var fail = total - createSuccess-updateSuccess;
					            	 var fileTimeMillis= obj.fileTimeMillis;
					            	 
					            	 var tip = "";
					            	 if (typeof(total) == "undefined" || typeof(createSuccess) == "undefined" || typeof(updateSuccess) == "undefined") 
					            	 {
					            		  tip = "数据上传处理失败！";
					            	 }
					            	 else
					            	{
					            	       tip = "总共处理了" + total + "条记录,其中成功新增" + createSuccess + "条记录,成功更新" + updateSuccess +"条记录，操作失败" + fail + "条记录";
					            	       $("#batchResDown").show();
					            	}
					            	 
					            	 
					            	 $("#res_div").html(tip);
					            	 $("#fileId").val(fileTimeMillis);
					            	 $("#dealFlag").val(0);
					            	 
					            }
					        }
					    );
					    
					}
					
				};
				
				
				//批量导入
				$scope.batchLoad = function(){
					
					$("#res_div").html(null);
					$scope.batchFlag = false;
					
					$("#batchResDown").hide();
					$("#dealFlag").val(0);
					$("#uploadFile").val(null);
					dialog({
						title : getLocalTag("adInfo_batchLoad_dialog", "批量录入广告位"),
						cancel:true,
						cancelValue : '关闭',
						content : document.getElementById('adInfo_batchLoad_dialog'),
						lock : true,
						opacity : 0.3
					}).showModal();;
					
				};
			
				//批量发布
				$scope.batchPublish = function () {

					dialog({
						title : getLocalTag("prompt", "Prompt"),
						content : "确定要批量发布广告位吗？",
						cancel : true,
						lock : true,
						okValue : getLocalTag('confirm', 'Yes'),
						cancelValue : getLocalTag('cancel', 'cancel'),
						ok : function () {

							var state = $scope.queryAdState;

							if (state == 1 || state == 2 || state == 3) {
								dialog("过滤当前状态不是初始化状态，请重新选择", function () {
									return true
								}).showModal(); ;
								return;
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
								adState : 0,
								monitorPlatform : 0,
								adInfoWebName : $scope.queryAdInfoWebName,
								adInfoPort : $scope.queryAdInfoPort,
								inputUser : $scope.queryInputUser,
								landPlatform : $scope.landPlatform,
								adqueryDateBeginDay : queryDateBeginDay,
								adqueryDateEndDay : queryDateEndDay
							};

							$scope.$apply(function () {
								$scope.callApi({
									url : webRoot + "/api/adInfo/batchPublish",
									method : "POST",
									params : params
								}).success(function (resp, status, headers, config) {
									if (resp.resultCode != 0) {
										$scope.failToOperate(resp);
										return;
									}
									var success = resp.success;
									var total = resp.total;

									dialog("共" + total + "个广告位满足发布条件,批量发布成功 " + success + "条", function () {
										return true
									}).showModal(); ;

									$scope.refresh();
								}).error(function (response, status, headers, config) {
									$scope.httpError(status);
								});
							});
						}
					}).showModal(); ;

				};
				
				//广告位模板下载
				$scope.adBatchTemplateDownLoad = function(){
					var url = $scope.options.meta.webRoot + "/api/adInfo/downLoadTemplate?__userKey=" + __userKey;
					location.href = url;
				};
				
				
		

				//是否超出长度
				$scope.isTrueAdInfoAdMaterialDescModify = function () {

					var len = 0;
					var adInfoAdMaterialDescModify = $scope.adInfoAdMaterialDescModify;
					if(typeof(adInfoAdMaterialDescModify) =='undefined')
					{
						return false;
					}
					
		            for (var i = 0; i < adInfoAdMaterialDescModify.length; i++) {
		               var length = adInfoAdMaterialDescModify.charCodeAt(i);
		               if(length>=0&&length<=128)
		                {
		                    len += 1;
		                }
		                else
		                {
		                    len += 2;
		                }
		            }
		            if(len>2000)
			        {
			        	return true;
			        }
					
			        return false;
					
				};
				
				//是否超出长度
				$scope.isTrueAdInfoAdMaterialDesc = function () {

					var len = 0;
					var adInfoAdMaterialDesc = $scope.adInfoAdMaterialDesc;
					if(typeof(adInfoAdMaterialDesc) == "undefined")
					{
						return false;
					}
		            for (var i = 0; i < adInfoAdMaterialDesc.length; i++) {
		               var length = adInfoAdMaterialDesc.charCodeAt(i);
		               if(length>=0&&length<=128)
		                {
		                    len += 1;
		                }
		                else
		                {
		                    len += 2;
		                }
		            }
			        if(len>2000)
			        {
			        	return true;
			        }
					
			        return false;
					
				};
				
				//是否可以发布
				$scope.isPublish = function (adInfo) {

					var array = adInfo.split('|');
					var state = array[0];
					var platfrom = array[1];
					var days = array[2];

					if (state == 0) {
						if (platfrom != '' && days != '') {
							return true;
						}
					}

					return false;
				};

				//是否可以重置
				$scope.isReset = function (adstate) {
					if (adstate == 0) {
						return false;
					}

					return true;
				};
				
				
				//营销活动是否选中
				$scope.isMktinfoIdSelect = function () {
					var value = $scope.mktinfoId;
					if (value != '') {
						return false;
					}

					return true;
				};
				
				//媒体类型是否选中
				$scope.isAdInfoMediaTypeSelect = function () {
					var value = $scope.adInfoMediaType;
					if (value != '') {
						return false;
					}

					return true;
				};
				
				//网站名称是否选中
				$scope.isAdInfoWebNameSelect = function () {
					var value = $scope.adInfoWebName;
					if (value != '') {
						return false;
					}

					return true;
				};
				
				
				//端口所属是否选中
				$scope.isAdInfoPortSelect = function () {
					var value = $scope.adInfoPort;
					if (value != '') {
						return false;
					}

					return true;
				};
				
				//着陆平台是否选中
				$scope.isAdInfoPlatformSelect =  function () {
					var value = $scope.adInfoPlatform;
					if (value != '') {
						return false;
					}

					return true;
				};
				
				//监控平台是否选中
				$scope.isAdInfoMonitorPlatformSelect =  function () {
					var value = $scope.adInfoMonitorPlatform;
					if (value != '') {
						return false;
					}

					return true;
				};
				

				//是否可以修改
				$scope.isModify = function (adstate) {
					if (adstate == 0) {
						return true;
					}

					return true;
				};

				
				//是否是必选项
				$scope.isNecessary = function (adstate) {
					if (adstate != 0) {
						return true;
					}

					return false;
				};

				//是否可以删除
				$scope.isDel = function (adstate) {
					if (adstate == 0) {
						return true;
					}

					return false;
				};

				//是否可以添加素材
				$scope.isMaterialAdd = function (adstate) {
					if (adstate == 0) {
						return true;
					}

					return false;
				};
				
				//是否可以修改素材
				$scope.isMaterialModify = function (adstate) {
					if (adstate == 0) {
						return false;
						
					}

					return true;
				};
				
				
				
				//营销活动是否选中
				$scope.isMktinfoIdSelectModify = function () {
					var value = $scope.mktinfoIdModify;
					if (value != '') {
						return false;
					}

					return true;
				};
				
				//媒体类型是否选中
				$scope.isAdInfoMediaTypeSelectModify = function () {
					var value = $scope.adInfoMediaTypeModify;
					if (value != '') {
						return false;
					}

					return true;
				};
				
				//网站名称是否选中
				$scope.isAdInfoWebNameSelectModify = function () {
					var value = $scope.adInfoWebNameModify;
					if (value != '') {
						return false;
					}

					return true;
				};
				
				
				//端口所属是否选中
				$scope.isAdInfoPortSelectModify = function () {
					var value = $scope.adInfoPortModify;
					if (value != '') {
						return false;
					}

					return true;
				};
				
				//着陆平台是否选中
				$scope.isAdInfoPlatformSelectModify =  function () {
					var value = $scope.adInfoPlatformModify;
					if (value != '') {
						return false;
					}

					return true;
				};
				
				//监控平台是否选中
				$scope.isAdInfoMonitorPlatformSelectModify =  function () {
					var value = $scope.adInfoMonitorPlatformModify;
					if (value != '') {
						return false;
					}

					return true;
				};
				
				//是否是必选项
				$scope.isNecessaryModify = function (adstate) {
					if (adstate != 0) {
						return true;
					}

					return false;
				};

				
				//广告位发布
				$scope.adInfoPublish = function (adInfoId) {
					dialog({
		    			title: getLocalTag("prompt", "Prompt"),
		        		content:getLocalTag("sureToPublish", "Are you sure to publish it?"),
		    			cancel:true,
		    			lock:true,
			    		okValue: getLocalTag('confirm','Yes'),
		        		cancelValue: getLocalTag('cancel','cancel'),
		        		ok: function() {
		        			$scope.$apply(function(){
		        				$scope.callApi({
		    						url : webRoot + "/api/adInfo/adInfoPublish",
		    						method : "GET",
		    						params : {
		    							adInfoId : adInfoId
		    						}
		    					}).success(function (resp, status, headers, config) {
		    						if (resp.resultCode != 0) {
		    							$scope.failToOperate(resp);
		    							return;
		    						}

		    						$scope.refresh();
		    						$scope.successToOperate(resp, $scope);
		    					}).error(function (response, status, headers, config) {
		    						$scope.httpError(status);
		    					});
		        			});
		        		}
		        	}).showModal();;
					
				};

				
				//广告位重置
				$scope.adInfoReset = function (adInfoId) {
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
		    						url : webRoot + "/api/adInfo/modifyState",
		    						method : "GET",
		    						params : {
		    							adInfoId : adInfoId,
		    							adInfoState : 0
		    						}
		    					}).success(function (resp, status, headers, config) {
		    						if (resp.resultCode != 0) {
		    							$scope.failToOperate(resp);
		    							return;
		    						}
		    						$scope.refresh();
		    						$scope.successToOperate(resp, $scope);

		    					}).error(function (response, status, headers, config) {
		    						$scope.httpError(status);
		    					});
		        			});
		        		}
		        	}).showModal();;
					
				};
				
				
				//删除素材
				$scope.delMaterial = function (adInfoId) {
					dialog({
		    			title: getLocalTag("delMaterial", "Del Material"),
		        		content:getLocalTag("sureToDelMaterial", "Are you sure to del it?"),
		    			cancel:true,
		    			lock:true,
			    		okValue: getLocalTag('confirm','Yes'),
		        		cancelValue: getLocalTag('cancel','cancel'),
		        		ok: function() {
		        			$scope.$apply(function(){
		        				$scope.callApi({
		    						url : webRoot + "/api/adInfo/delMaterial",
		    						method : "POST",
		    						params : {
		    							adInfoId : adInfoId
		    						}
		    					}).success(function (resp, status, headers, config) {
		    						if (resp.resultCode != 0) {
		    							$scope.failToOperate(resp);
		    							return;
		    						}
		    						$scope.refresh();
		    						$scope.successToOperate(resp, $scope);

		    					}).error(function (response, status, headers, config) {
		    						$scope.httpError(status);
		    					});
		        			});
		        		}
		        	}).showModal();;
					
				};
				
				//export
				$scope.adExcelExport = function () {
		 			
					 var queryMktId = $scope.queryMktId;
					 var queryAdState = $scope.queryAdState;
					 var adInfoWebName = $scope.queryAdInfoWebName;
					 var adInfoPort = $scope.queryAdInfoPort;
					 var queryInputUser = $scope.queryInputUser;
					 var landPlatform = $scope.landPlatform;
					 var adExportCheck = $scope.adExportCheck;
					 
					 var url = $scope.options.meta.webRoot + "/api/adInfo/excelExport?__userKey=" + __userKey;
					 
					 if(typeof(queryMktId) != "undefined" && queryMktId != "")
					 {
						 url = url + "&mktId=" + queryMktId;
					 }
					 
					 if(typeof(queryAdState) != "undefined" && queryAdState != "")
					 {
						 url = url + "&adState=" + queryAdState;
					 }
					 
					 if(typeof(adInfoWebName) != "undefined" && adInfoWebName != "")
					 {
						 url = url + "&adInfoWebName=" + adInfoWebName;
					 }
					 
					 
					 if(typeof(adInfoPort) != "undefined" && adInfoPort != "")
					 {
						 url = url + "&adInfoPort=" + adInfoPort;
					 }
					 
					 if(typeof(queryInputUser) != "undefined" && queryInputUser != "")
					 {
						 url = url + "&inputUser=" + queryInputUser;
					 }
					
					 /*
					 if(typeof(queryAdInfoDeliveryTimes) != "undefined" && queryAdInfoDeliveryTimes !="")
		         	 {
						 var queryAdInfoDeliveryBeginDay = queryAdInfoDeliveryTimes.split('-')[0];
						 var queryAdInfoDeliveryEndDay = queryAdInfoDeliveryTimes.split('-')[1];
						 url = url + "&adInfoDeliveryBeginDay=" + queryAdInfoDeliveryBeginDay + "&adInfoDeliveryEndDay=" + queryAdInfoDeliveryEndDay;
		         	 }
					 else
					 {
						 url = url + "&adInfoDeliveryBeginDay=" + "" + "&adInfoDeliveryEndDay=" + "";
			         	 
					 }*/
		             
					 var queryDate = $scope.queryDate;
					 if(typeof(queryDate) != "undefined" && queryDate !="")
					 {
						 var queryDateBeginDay = queryDate.split('-')[0];
						 var queryDateEndDay = queryDate.split('-')[1];
						 url = url  + "&adqueryDateBeginDay=" + queryDateBeginDay + "&adqueryDateEndDay=" + queryDateEndDay;
					 }
					 else
					 {
						 url = url  + "&adqueryDateBeginDay=" + "" + "&adqueryDateEndDay=" + "";
					 }
					 
					 if(typeof(adExportCheck) != "undefined" && adExportCheck != "")
					 {
						 adExportCheck = '1';
						 url = url + "&adExportType=" + adExportCheck;
					 }
					 else
					 {
						 adExportCheck = '1';
						 url = url + "&adExportType=" + adExportCheck;
					 }
					 if(typeof(landPlatform) != "undefined" && landPlatform != "")
					 {
						 url = url + "&landPlatform=" + landPlatform;
					 }
					 location.href = url;
			
					};
					
					
					//素材批量下载
					$scope.materialBatchDownload = function () {
			 			
						 var queryMktId = $scope.queryMktId;
						 var queryAdState = $scope.queryAdState;
						 var adInfoWebName = $scope.queryAdInfoWebName;
						 var adInfoPort = $scope.queryAdInfoPort;
						 var queryInputUser = $scope.queryInputUser;
						 var landPlatform = $scope.landPlatform;
						 
						 var url = $scope.options.meta.webRoot + "/api/adInfo/materialBatchDownload?__userKey=" + __userKey;
						 
						 if(typeof(queryMktId) != "undefined" && queryMktId != ""&& queryMktId != "-1")
						 {
							 url = url + "&mktId=" + queryMktId;
						 }
						 else
						 {
							 dialog("请先选择营销活动名称再下载素材信息！",function(){return true}).showModal();;
							 return;
						 }
						 
						 
						 if(typeof(queryAdState) != "undefined" && queryAdState != "")
						 {
							 url = url + "&adState=" + queryAdState;
						 }
						 
						 if(typeof(adInfoWebName) != "undefined" && adInfoWebName != "")
						 {
							 url = url + "&adInfoWebName=" + adInfoWebName;
						 }
						 
						 
						 if(typeof(adInfoPort) != "undefined" && adInfoPort != "")
						 {
							 url = url + "&adInfoPort=" + adInfoPort;
						 }
						 
						 if(typeof(queryInputUser) != "undefined" && queryInputUser != "")
						 {
							 url = url + "&inputUser=" + adInfoPort;
						 }
						 /*
						 if(typeof(queryAdInfoDeliveryTimes) != "undefined" && queryAdInfoDeliveryTimes !="")
			         	 {
							 var queryAdInfoDeliveryBeginDay = queryAdInfoDeliveryTimes.split('-')[0];
							 var queryAdInfoDeliveryEndDay = queryAdInfoDeliveryTimes.split('-')[1];
							 url = url + "&adInfoDeliveryBeginDay=" + queryAdInfoDeliveryBeginDay + "&adInfoDeliveryEndDay=" + queryAdInfoDeliveryEndDay;
			         	 }
						 else
						 {
							 url = url + "&adInfoDeliveryBeginDay=" + "" + "&adInfoDeliveryEndDay=" + "";
				         	 
						 }*/
			             
						 var queryDate = $scope.queryDate;
						 if(typeof(queryDate) != "undefined" && queryDate !="")
						 {
							 var queryDateBeginDay = queryDate.split('-')[0];
							 var queryDateEndDay = queryDate.split('-')[1];
							 url = url  + "&adqueryDateBeginDay=" + queryDateBeginDay + "&adqueryDateEndDay=" + queryDateEndDay;
			         	 }
			             else
			             {
			            	 url = url  + "&adqueryDateBeginDay=" + "" + "&adqueryDateEndDay=" + "";
			             }
						 if(typeof(landPlatform) != "undefined" && landPlatform != "")
						 {
							 url = url + "&landPlatform=" + landPlatform;
						 }

						 
						 location.href = url;
				
						};
				
				
			    //上传素材信息
				$scope.addMaterial = function (adInfoId) {
					//默认选择文件上传
					$scope.materialType = "0";
					$("#uploadMaterial").val("");
					$("#uploadMaterial").val(null);
					$scope.materialUrl = "";
					$("#processLoading").hide();
					$scope.hiddenAid = adInfoId;
					//弹出窗口
					dialog({
						id: 'ad_material_add_dialog',
						title : getLocalTag("ad_material_add_dialog", "素材上传"),
						content : document.getElementById('ad_material_add_dialog'),
						lock : true,
						opacity : 0.3
					}).showModal();;
				};
				
				
			   //文件是否disable
				$scope.isUploadMaterialDisable = function()
				{
					var type = $scope.materialType;
					
					//若不是文件类型，就会disable
					if(type != "0")
					{
						return true;
					}
					
					return false;
				};
				
				
				//输入链接是否disable
				$scope.isMaterialUrlDisable = function()
				{
					var type = $scope.materialType;
					
					//若不是链接类型，就会disable
					if(type != "1")
					{
						return true;
					}
					
					return false;
				};
				
				//上传关闭
				$scope.materialCancel = function()
				{
					dialog({id: 'ad_material_add_dialog'}).close();
				};
				
				 
				//上传数据
				$scope.materialSave = function () 
				{
					var materialType = $scope.materialType;
					var uploadMaterial = $("#uploadMaterial").val();
					var materialUrl = $scope.materialUrl;
					var adInfoId = $scope.hiddenAid;
					
					//文件上传
					if(materialType == "0")
					{
						if('' == uploadMaterial || null == uploadMaterial)
						{
							dialog("请先选择上传素材文件",function(){return true}).showModal();;
							return;
						}
						
						//文件长度判断
						 var size = document.getElementById('uploadMaterial').files[0].size;
						 if(size > 100000000)
						 {
							 dialog('素材上传失败！系统最大支持素材文件大小为100M', function(){}).showModal();;
							 return;
						 }
						 
						
						 $("#processLoading").show();
						var url = webRoot + "/api/adInfo/materialFileSave?__userKey=" + __userKey + "&adInfoId=" + adInfoId;
					    $.ajaxFileUpload
					    (
					        {
					            url:url,//用于文件上传的服务器端请求地址
					            secureuri:false,//一般设置为false
					            fileElementId:'uploadMaterial',//文件上传空间的id属性  <input type="file" id="file" name="file" />
					            dataType : 'text',
					            success: function (data, status)  
					            {
					            	$("#processLoading").hide();
					            	 var beginIndex = data.indexOf("{");
					            	 var endIndex = data.indexOf("}");
					            	 var objStr = data.substring(beginIndex,endIndex + 1);
					            	 var obj = eval("("+objStr+")");
					                 var resultCode = obj.resultCode;
					                 if(resultCode == 0)
				                	 {
					                	 dialog({id: 'ad_material_add_dialog'}).close();
					              
					                	 $("#queryButton").trigger('click');
					                	
				                	 }
					                 else if(resultCode == 1801)
				                	 {
					                	 dialog('素材上传失败！系统最大支持素材文件大小为100M', function(){}).showModal();;
				                	 }
					                 else if(resultCode == 1802)
				                	 {
					                	 dialog('素材上传失败！素材名称过长,素材名称最长100个字符,请修改后重传', function(){}).showModal();;
				                	 }
					                 else
				                	 {
					                	 dialog('素材上传失败！', function(){}).showModal();;
				                	 }
					            	 
					            }
					            
					        }
					    );
					}
					
					//录入地址
					if(materialType == "1")
					{
						if(typeof(materialUrl) == "undefined")
						{
							return;
						}
						$scope.callApi({
    						url : webRoot + "/api/adInfo/materialUrlSave",
    						method : "POST",
    						params : {
    							adInfoId : adInfoId,
    							materialUrl : materialUrl
    						}
    					}).success(function (resp, status, headers, config) {
    						if (resp.resultCode != 0) {
    							$scope.failToOperate(resp);
    							return;
    						}
    						
    						dialog({id: 'ad_material_add_dialog'}).close();
    						$scope.refresh();
    						$scope.successToOperate(resp, $scope);

    					}).error(function (response, status, headers, config) {
    						$scope.httpError(status);
    					});
					}
					
					
			
				    
				};
				
				$scope.downLoadMaterial = function (adInfoId,materialState,materialShowName,materialType) 
				{
					if(materialState == 1)
					{
						if(materialType == 1)
						{
							// location.href = materialShowName;
							
							window.open(materialShowName);
						}
						
						
						if(materialType == 0)
						{
							 
							 var url = $scope.options.meta.webRoot + "/api/adInfo/downLoadMaterial?__userKey=" + __userKey;
							 
							 url = url + "&adInfoId=" + adInfoId;
							 
							 location.href = url;
						}
					}
					else
					{
						$scope.addMaterial(adInfoId);
					}
				};
				
				$scope.TimeCheck =  function (checkTime) 
				{
				    var BeginDay = checkTime.split('-')[0];
					var EndDay = checkTime.split('-')[1];
				    var timeExample = /^(\d{4}\d{2}\d{2})$/;
				    if (!timeExample.test(BeginDay))
				    {
				        dialog('时间格式非法，请重新输入', function(){}).showModal();;
				        return false;
				    }
				    if (!timeExample.test(EndDay))
				    {
				        dialog('时间格式非法，请重新输入', function(){}).showModal();;
				        return false;
				    }
				    if(BeginDay > EndDay)
				    {
				        dialog('时间格式非法，请重新输入', function(){}).showModal();;
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
				        dialog('时间格式非法，请重新输入', function(){}).showModal();;
				        return false;
				    }
				    var d1 = new Date(resultBeginDay[0], resultBeginDay[1] - 1, resultBeginDay[2]);
				    var d2 = new Date(resultEndDay[0], resultEndDay[1] - 1, resultEndDay[2]);
				    if((d1.getFullYear() == resultBeginDay[0] && (d1.getMonth() + 1) == resultBeginDay[1] && d1.getDate() == resultBeginDay[2]) && (d2.getFullYear() == resultEndDay[0] && (d2.getMonth() + 1) == resultEndDay[1] && d2.getDate() == resultEndDay[2]))
				    {
				        return true;
				    }
				    dialog('时间格式非法，请重新输入', function(){}).showModal();;
			        return false;
				};
				
				
				$scope.adInfoEmail = function()
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
					 var landPlatform = $scope.landPlatform;
					 if(typeof(landPlatform) == "undefined" || landPlatform =="")
					{
						 landPlatform = -1;
				    }
					 var queryDate = $scope.queryDate;
					 
					 var queryAdInfoDeliveryBeginDay = null;
					 var queryAdInfoDeliveryEndDay   = null;
					 /*if(typeof(queryAdInfoDeliveryTimes) != "undefined" && queryAdInfoDeliveryTimes !="")
		         	 {
						 queryAdInfoDeliveryBeginDay = queryAdInfoDeliveryTimes.split('-')[0];
						 queryAdInfoDeliveryEndDay = queryAdInfoDeliveryTimes.split('-')[1];
		         	 }
		       */
					 
					 var queryDateBeginDay  = null;
					 var queryDateEndDay  = null;
					 if(typeof(queryDate) != "undefined" && queryDate !="")
					 {
						 queryDateBeginDay = queryDate.split('-')[0];
						 queryDateEndDay = queryDate.split('-')[1];
					 }
					 
					 
					 
					 if(typeof(queryMktId) == "undefined" || queryMktId == "" || queryMktId == "-1")
					{
						 dialog("请先选择营销活动名称再发送通知邮件！",function(){return true}).showModal();;
						 return;
					 }
					 
					 $("#ad_tbody").html(null);
					 
					 $.ajax({
							type : "GET",
							url : webRoot + "/api/adInfo/queryAdInfoEmailUserInfo",
							async : false,
							data : {
								__userKey : __userKey,
								mktId : queryMktId
							},
							success : function (resp) {
								if (resp.resultCode != 0) {
									return;
								}
								var users = resp.result;
								for(var i = 0; i < users.length; i++)
								{
									var account = users[i].account;
									var name = users[i].name;
									var selectFlag = users[i].selectFlag;
									var department = users[i].department;
									var html = "<tr><td><input type='checkbox' name='emailUsers'  value=" + account + " />&nbsp;&nbsp;</td><td>" + account + "</td><td>" + name + "</td><td>" + department + "</td></tr>";
									
									if(selectFlag == 1)
									{
										 html = "<tr><td><input type='checkbox' checked='checked' name='emailUsers'  value=" + account + " />&nbsp;&nbsp;</td><td>" + account + "</td><td>" + name + "</td><td>" + department + "</td></tr>";
									}
									
									//$("#ad_tbody").append(html);
									$(html).appendTo("#ad_tbody");
								}
								
								
								dialog({
									title : getLocalTag("id_adEmail_dialog", "选择要通知的人"),
									ok : function () {
										
										var users = "";
										 $("[name='emailUsers']:checked").each(function(index, element) {
											 users += $(this).val() + ",";
					                     });
					                    
										 if(users == "")
										 {
											 dialog('请选择通知的人', function(){}).showModal();;
											 return;
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
										   		url : webRoot + "/api/adInfo/emailAdInfoUserList",
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
									content : document.getElementById('id_adEmail_dialog'),
									lock : true,
									opacity : 0.3
								}).showModal();
								
								
								
							}
						});
					 
				};
				
				$scope.mktinfoIdChange = function(){
					// alert();
					//根据活动id,获取网站名称列表
					//网站名称下拉框赋值
					$scope.callApi({
						url : webRoot + "/api/mktDic/queryWebNameList",
						method : "POST",
						params : {
							mktinfoId:$scope.mktinfoId
						}
					}).success(function (resp, status, headers, config) {
						if (resp.resultCode != 0) {
							$scope.failToOperate(resp);
							return;
						}
						$scope.names = resp.results;
					}).error(function (response, status, headers, config) {
						$scope.httpError(status);
					});
					//根据id，设置默认的着陆平台
					
					$scope.callApi({
						url : webRoot + "/api/adInfo/getDefaultLandPlatform",
						method : "POST",
						params : {
							mktId:$scope.mktinfoId
						}
					}).success(function (resp, status, headers, config) {
						if (resp.resultCode != 0) {
							$scope.failToOperate(resp);
							return;
						}
						
						if(0 == resp.deptType)
						{
							$scope.adInfoPlatform = "0";
						}
						else
						{
							$scope.adInfoPlatform = "2";
						}
					}).error(function (response, status, headers, config) {
						$scope.httpError(status);
					});
				}
				
			}

		};
	

	 exports.instance = function(webRoot, metaName, segments) {
	    	var obj = MetaBase.instance(webRoot, metaName, segments);
	    	$.extend(obj, MetaAdInfo);
	    	return obj;
	    };
});


function creatDefaultDate()
{
	var now = new Date();
	now.setDate(now.getDate());
	var mouthDayNow = checkDateFromat(now.getMonth(),now.getDate());
	var day0 = now.getFullYear() + '' + mouthDayNow;
	
	ow = new Date();
	now.setDate(now.getDate()+30);
	var mouthDayBefore = checkDateFromat(now.getMonth(),now.getDate());
	var day1 = now.getFullYear() + '' + mouthDayBefore;
	var InitializeDate = day0 + '-' + day1;
	return InitializeDate;
}
function checkDateFromat(mouth,day)
{
	var monthFormat;
	var dayFormat;
	if(mouth+1<10)
	{
		monthFormat = '0' + (mouth + 1);
	}
	else
	{
		monthFormat = mouth + 1;
	}
		
	if(day<10)
	{
		dayFormat = '0' + day;
	}
	else
	{
		dayFormat = day;
	}
	var InitializeDate = monthFormat + '' + dayFormat;
	return InitializeDate;
}


