define(function (require, exports, module) {
	var MetaBase = require('meta');
	require('dialog');
	require('jquery');
	require('calendar');
	require('jquery/calendar/1.0/tinycal.css');
	require('jquery/ajaxupload/ajaxfileupload.js');

	var MetamktHTransRep = {
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
				
				$.fn.calendar({
					target : "#queryAdInfoDeliveryTimes",
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
								$scope.queryAdInfoDeliveryTimes = day0 + '-' + day1;
							}
							else
							{
								$scope.queryAdInfoDeliveryTimes = day1 + '-' + day0;
							}
						}
						else
						{
							$scope.queryAdInfoDeliveryTimes = day0 + '-' + day0;
						}
						
						$scope.$digest();
						return true;
					}

				});
				

			},
           querySubPara : function ($scope, params) {
				

				var queryAdInfoDeliveryTimes = $scope.queryAdInfoDeliveryTimes;
                if(typeof(queryAdInfoDeliveryTimes) == "undefined" || queryAdInfoDeliveryTimes == "")
            	{
                	queryAdInfoDeliveryTimes = '';
            	}
                else
                {
                	if (!$scope.TimeCheck(queryAdInfoDeliveryTimes))
                	{
						return;
                	}
                }
                
              var sid = $scope.inputSid;
              var mktId = $scope.queryMktId;
	          if((mktId=='-1' || mktId == -1)&&
	             (typeof(sid) == "undefined" || sid == ""))
	          {
	             dialog('请输入SID', function(){}).showModal();
	  			 return ;
	          }
	          if(typeof(sid) == "undefined" || sid == ""){
	              	sid = '';
	          }
                
                var queryDate = $scope.queryDate;
                if(typeof(queryDate) == "undefined")
            	{
                	queryDate = '';
            	}
				var queryAdInfoDeliveryBeginDay = queryAdInfoDeliveryTimes.split('-')[0];
				var queryAdInfoDeliveryEndDay = queryAdInfoDeliveryTimes.split('-')[1];
				var queryDateBeginDay = queryDate.split('-')[0];
				var queryDateEndDay = queryDate.split('-')[1];
				$.extend(params, {
					mktId : mktId,
					adInfoWebName : $scope.queryAdInfoWebName,
					adInfoPort : $scope.queryAdInfoPort,
					adInfoPlatform : $scope.queryAdInfoPlatform,
					inputUser : $scope.queryInputUser,
					adInfoDeliveryBeginDay : queryAdInfoDeliveryBeginDay,
					adInfoDeliveryEndDay :queryAdInfoDeliveryEndDay,
					adqueryDateBeginDay  : queryDateBeginDay,
					adqueryDateEndDay  : queryDateEndDay,
					adquerySid:sid,
					adType:0
				});

				return {
					method : 'GET',
					params : params,
					url : this.webRoot + "/api/mktHTransRep/ReportQuery"
				};
			},
			
	        extendScope : function ($scope, $http) {
				var webRoot = this.getWebRoot();
				
				//初始化下拉菜单
				this.initCaledar($scope);
				
				var queryDate = $scope.queryDate;
				if(typeof(queryDate) == "undefined" || mktId == null)
				{
					$scope.queryDate = creatInitializeDate();
				}
				
				//获取营销活动ID与名称列表
				$scope.callApi({
					url : this.getWebRoot() + "/api/mktReport/queryIdNameListWithReport",
					method : "GET",
					params : {"pType" : 0}
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					//搜索框赋值
					var arr = {};
					var array = new Array();
					arr.mktinfoId = '-1';
					arr.mktinfoName = '全部';
					$scope.mktInfos = array.concat(arr,resp.result);					
					$scope.queryMktId = $scope.mktInfos[0].mktinfoId;
				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});
				//录入人赋值
				$scope.callApi({
					url : this.getWebRoot() + "/api/mktDic/queryReportInputUserNameList",
					method : "GET",
					params : {
						pType : 0
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
					$scope.inputUserNames = array.concat(arr,resp.results);
					$scope.queryInputUser = '-1';
				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});
				
				//着陆平台名称赋值
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
					$scope.platNames = array.concat(arr,resp.results);
					$scope.queryAdInfoPlatform = '-1';
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
					$scope.queryAdInfoWebName = '-1';
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
					$scope.queryAdInfoPort = '-1';
				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});
				
				//导出数据
				$scope.transReportExport = function () {
	                
					 var url = $scope.options.meta.webRoot + "/api/mktHTransRep/reportExport?__userKey=" + __userKey;
					 var mktId  = $scope.queryMktId;
					 var adInfoWebName = $scope.queryAdInfoWebName;
					 var adInfoPort = $scope.queryAdInfoPort;
					 var queryAdInfoDeliveryTimes = $scope.queryAdInfoDeliveryTimes;
					 var sid = $scope.inputSid;
	                if(typeof(queryAdInfoDeliveryTimes) == "undefined")
	            	{
	                	queryAdInfoDeliveryTimes = '';
	            	}
	                
	                var queryDate = $scope.queryDate;
	                if(typeof(queryDate) == "undefined")
	            	{
	                	queryDate = '';
	            	}
					var queryAdInfoDeliveryBeginDay = queryAdInfoDeliveryTimes.split('-')[0];
					var queryAdInfoDeliveryEndDay = queryAdInfoDeliveryTimes.split('-')[1];
					var queryDateBeginDay = queryDate.split('-')[0];
					
					var queryDateEndDay = queryDate.split('-')[1];
					var inputUser = $scope.queryInputUser;
					 
					 if(typeof(mktId) != "undefined" && mktId != "")
					 {
						 url = url + "&mktId=" + mktId;
					 }
					 
					 if(typeof(adInfoWebName) != "undefined" && adInfoWebName != "")
					 {
						 url = url + "&adInfoWebName=" + adInfoWebName;
					 }
					 
					 
					 if(typeof(adInfoPort) != "undefined" && adInfoPort != "")
					 {
						 url = url + "&adInfoPort=" + adInfoPort;
					 }
					 
					 var adInfoPlatform = $scope.queryAdInfoPlatform;
					 if(typeof(adInfoPlatform) != "undefined" && adInfoPlatform != "")
					 {
						 url = url + "&adInfoPlatform=" + adInfoPlatform;
					 }
					 
					 if(typeof(inputUser) != "undefined" && inputUser != "")
					 {
						 url = url + "&inputUser=" + inputUser;
					 }
					 
					 if(typeof(adInfoDeliveryBeginDay) != "undefined" && adInfoDeliveryBeginDay != "")
					 {
						 url = url + "&adInfoDeliveryBeginDay=" + adInfoDeliveryBeginDay;
					 }
					 
					 
					 if(typeof(adInfoDeliveryEndDay) != "undefined" && adInfoDeliveryEndDay != "")
					 {
						 url = url + "&adInfoDeliveryEndDay=" + adInfoDeliveryEndDay;
					 }
					 

					 if(typeof(queryDateBeginDay) != "undefined" && queryDateBeginDay != "")
					 {
						 url = url + "&adqueryDateBeginDay=" + queryDateBeginDay;
					 }
					 
					 if(typeof(queryDateEndDay) != "undefined" && queryDateEndDay != "")
					 {
						 url = url + "&adqueryDateEndDay=" + queryDateEndDay;
					 }
					
					 if(typeof(sid) != "undefined" && sid != "")
					 {
						 url = url + "&adquerySid=" + sid;
					 }
					 url = url + "&adType=" + 0;
					 location.href = url;

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
				
				
	        }
	        
	
			};

	 exports.instance = function(webRoot, metaName, segments) {
	    	var obj = MetaBase.instance(webRoot, metaName, segments);
	    	$.extend(obj, MetamktHTransRep);
	    	return obj;
	    };
});
function creatInitializeDate()
{
	var now = new Date();
	now.setDate(now.getDate()-1);
	var mouthDayNow = checkDateFromat(now.getMonth(),now.getDate());
	var day0 = now.getFullYear() + '' + mouthDayNow;
	
	now = new Date();
	now.setDate(now.getDate()-7);
	var mouthDayBefore = checkDateFromat(now.getMonth(),now.getDate());
	var day1 = now.getFullYear() + '' + mouthDayBefore;
	var InitializeDate = day1 + '-' + day0;
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