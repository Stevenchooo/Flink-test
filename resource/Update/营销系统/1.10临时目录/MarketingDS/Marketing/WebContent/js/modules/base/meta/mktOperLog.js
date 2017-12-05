define(function(require, exports, module) {
    var MetaBase = require('meta');

    require('jquery');
    require('calendar');
    require('jquery/calendar/1.0/tinycal.css');
    require('jquery/ajaxupload/ajaxfileupload.js');
    	
    

    var MetaMktOperLog =  {
        
        setRange : function ($scope, start, end) {
			$scope.query_startTime = start.year + '-' + $.addZero(start.month + 1) + '-' + $.addZero(start.day);
			$scope.query_endTime = end.year + '-' + $.addZero(end.month + 1) + '-' + $.addZero(end.day);
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
            
            var queryName = $scope.queryName; 
            if(typeof(queryName) == "undefined" || queryName == "")
        	{
            	queryName = '';
        	}

			var queryDateBeginDay = queryDate.split('-')[0];
			var queryDateEndDay = queryDate.split('-')[1];
			$.extend(params, {
				queryName : queryName,
				queryDateBeginDay : queryDateBeginDay,
				queryDateEndDay : queryDateEndDay
			});

			return {
				method : 'GET',
				params : params,
				url : this.webRoot + "/api/mktOperLog/query"
			};
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
						if(day0 != day1){
							dialog('选择时间不能超过一天,请重新选择', function(){}).showModal();
							$scope.queryDate = creatInitializeDate();
							return;
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
		
        extendScope: function($scope,$http) {
        	
        	var webRoot = this.getWebRoot();
			
			//初始化下拉菜单
			this.initCaledar($scope);
			
			var queryDate = $scope.queryDate;
			if(typeof(queryDate) == "undefined" || mktId == null)
			{
				$scope.queryDate = creatInitializeDate();
			}
			
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
			
			//导出数据
			$scope.logExport = function () {
                
				 var url = $scope.options.meta.webRoot + "/api/mktOperLog/logExport?__userKey=" + __userKey;
				 
                var queryDate = $scope.queryDate;
                if(typeof(queryDate) == "undefined")
            	{
                	queryDate = '';
            	}
                
				var queryDateBeginDay = queryDate.split('-')[0];				
				var queryDateEndDay = queryDate.split('-')[1];
				var queryName = $scope.queryName;
				 

				 
				 if(typeof(queryName) != "undefined" && queryName != "")
				 {
					 url = url + "&queryName=" + queryName;
				 }
				 
				 if(typeof(queryDateBeginDay) != "undefined" && queryDateBeginDay != "")
				 {
					 url = url + "&queryDateBeginDay=" + queryDateBeginDay;
				 }
				 
				 if(typeof(queryDateEndDay) != "undefined" && queryDateEndDay != "")
				 {
					 url = url + "&queryDateEndDay=" + queryDateEndDay;
				 }
								 
				 location.href = url;

				};
          
        	
        }
        
        
        
        
    };
    
    exports.instance = function(webRoot, metaName, segments) {
    	var obj = MetaBase.instance(webRoot, metaName, segments);
    	$.extend(obj, MetaMktOperLog);
    	return obj;
    };
});
function creatInitializeDate()
{
	var now = new Date();
	now.setDate(now.getDate());
	var mouthDayNow = checkDateFromat(now.getMonth(),now.getDate());
	var day0 = now.getFullYear() + '' + mouthDayNow;
	
	now = new Date();
	now.setDate(now.getDate());
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