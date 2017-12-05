define(function (require, exports, module) {
	require('jquery');
	require("jquery-ui");
	require('cookie');
	var notice = require("notice");

	exports.showReport = function () {
		
		
		

		baseQuery = function()
		{
			var apiUrl = "/api/adActivity/baseQuery";		
			//获取查询条件
			var mktinfoName = $("#mktInfoName").val();
			var reportPlatform = $("#report_platform").children(".fba-on").attr("value");
			
			$.cookie(__cookieHeader + 'mktinfoName', mktinfoName, {
				expires : 1,
				secure : false
			});
			$.cookie(__cookieHeader + 'reportPlatform', reportPlatform, {
				expires : 1,
				secure : false
			});
			
			var queryDateStart = $("#d_start").val();
			var queryDateEnd = $("#d_end").val();
			if(queryDateStart > queryDateEnd)
			{
				dialog('开始时间必须小于结束时间，请重新选择', function(){}).showModal();
				return;
			}
			
			if(queryDateStart ==""){
				queryDateStart = $.cookie(__cookieHeader + 'd_start');
				queryDateEnd   = $.cookie(__cookieHeader + 'd_end');
			}
			
			$.cookie(__cookieHeader + 'd_start', queryDateStart, {
				expires : 1,
				secure : false
			});
			$.cookie(__cookieHeader + 'd_end', queryDateEnd, {
				expires : 1,
				secure : false
			});
			
			var reportArea = $("#report_area").parent().children(".dropdown-toggle").attr("value");
			var reportPersonGroup = $("#report_person_group").parent().children(".dropdown-toggle").attr("value");
			
			
			var params = {
					"__userKey" : __userKey,
					"pActivtiy_id" : mktinfoName,
					"pMedia_type" : reportPlatform,
					"pUser_type" : reportPersonGroup,
					"pPt_dStart" : queryDateStart,
					"pPt_dEnd" :   queryDateEnd,
					"pArea_type" : reportArea
				};

			$.ajax({
				type : "POST",
				url : __webRoot + apiUrl,
				async : false,
				data : params,
				success : function (resp) {
					if (resp.resultCode != 0) {
						return;
					}
					
					$('#base_bg_chart').show();
					$('#base_dj_chart').show();
					var pvUvList = resp.pvUvList;
					var UvList   = resp.UvList;
					mktChart.common.baseChart(pvUvList,UvList);
									
					
					var chartRadioValue = $("input[name='base_radio']:checked").val();
					if(chartRadioValue == "bg")
					{
						$('#base_bg_chart').show();
						$('#base_dj_chart').hide();
					}
					else
					{
						$('#base_bg_chart').hide();
						$('#base_dj_chart').show();
					}
					
					$(".span_hide").removeClass("span_hide");
					var tableList = resp.tableList;
					var instance = new notice(10, 4, tableList, "base_list", false); // 实例化NoticeTable类
					instance.init(); // 画图
					instance.eventRegistered(); // 绑定事件
				}
			});
			
			
				
				$("#d_hourstat").datepicker({
					dateFormat : "yy-mm-dd"
				}).datepicker("setDate", queryDateEnd); 
				
				$("#d_fr").datepicker({
					dateFormat : "yy-mm-dd"
				}).datepicker("setDate", queryDateEnd); 
							
		}
		//导出基础数据
		baseExport = function()
		{
			var url = __webRoot + "/api/adActivity/baseExport?__userKey=" + __userKey;
			
			var mktinfoName = $("#mktInfoName").val();
			var reportPlatform = $("#report_platform").children(".fba-on").attr("value");									
			var reportArea = $("#report_area").parent().children(".dropdown-toggle").attr("value");
			var reportPersonGroup = $("#report_person_group").parent().children(".dropdown-toggle").attr("value");	
			var queryDateStart = $("#d_start").val();
			var queryDateEnd = $("#d_end").val();
			url = url + "&mktinfoName=" + mktinfoName;
			url = url + "&reportPlatform=" + reportPlatform;
			url = url + "&reportArea=" + reportArea;
			url = url + "&reportPersonGroup=" + reportPersonGroup;
			url = url + "&queryDateStart=" + queryDateStart;
			url = url + "&queryDateEnd=" + queryDateEnd;
			location.href = url;
		}
		hourExport = function()
		{
            var url = __webRoot + "/api/adActivity/hourExport?__userKey=" + __userKey;
			
            var mktinfoName = $("#mktInfoName").val();
			var reportPlatform = $("#report_platform").children(".fba-on").attr("value");
			var reportMedia = $("#report_media").parent().children(".dropdown-toggle").attr("value");
			
			if (typeof(reportMedia) =="undefined" || reportMedia == "")
			{
				reportMedia = -1;
			}
			var queryDate = $("#d_hourstat").val();
			
			url = url + "&mktinfoName=" + mktinfoName;
			url = url + "&reportPlatform=" + reportPlatform;
			url = url + "&reportMedia=" + reportMedia;
			url = url + "&queryDate=" + queryDate;
			location.href = url;
		}
		//分时数据查询
		hourQuery = function()
		{			
		 
		   var apiUrl = "/api/adActivity/hourQuery";			
			//获取查询条件
		    var queryDate = $("#d_hourstat").val();
		    
		    if(queryDate ==""){
				queryDate   = $.cookie(__cookieHeader + 'd_end');
			}
		    
			var mktinfoName = $("#mktInfoName").val();
			var reportPlatform = $("#report_platform").children(".fba-on").attr("value");
			
			$.cookie(__cookieHeader + 'mktinfoName', mktinfoName, {
				expires : 1,
				secure : false
			});
			$.cookie(__cookieHeader + 'reportPlatform', reportPlatform, {
				expires : 1,
				secure : false
			});
			$.cookie(__cookieHeader + 'd_start', queryDate, {
				expires : 1,
				secure : false
			});
			$.cookie(__cookieHeader + 'd_end', queryDate, {
				expires : 1,
				secure : false
			});
			
			
			var reportMedia = $("#report_media").parent().children(".dropdown-toggle").attr("value");
			if (typeof(reportMedia)!="undefined")
			{
			}
			else
		    {
				reportMedia = "-1";
		    }
			
			
			var params = {
					"__userKey" : __userKey,
					"pActivtiy_id" : mktinfoName,
					"pFirst_id": reportMedia,
					"pMedia_type" : reportPlatform,
					"pPt_d" : queryDate
				};
			$.ajax({
				type : "POST",
				url : __webRoot + apiUrl,
				async : true,
				data : params,
				success : function (resp) {
					if (resp.resultCode != 0) {
						return;
					}
					
					$('#hourstat_bg_chart').show();
					$('#hourstat_dj_chart').show();
					var imgList = resp.imgList;
					
					mktChart.common.hourstatChart(imgList,reportMedia);
					
					
				
					
					var chartRadioValue = $("input[name='hourstat_radio']:checked").val();
					
					if(chartRadioValue == "bg")
					{
						$('#hourstat_bg_chart').show();
						$('#hourstat_dj_chart').hide();
					}
					else
					{
						$('#hourstat_bg_chart').hide();
						$('#hourstat_dj_chart').show();
					}
					
					$(".span_hide").removeClass("span_hide");
					var tableList = resp.tableList;
					var instance = new notice(50, 5, tableList, "hour_tblList", false); // 实例化NoticeTable类
					instance.init(); // 画图
					instance.eventRegistered(); // 绑定事件
				}
			});	
			
//			$("#d_start").datepicker({
//				dateFormat : "yy-mm-dd",
//				 onClose: function( selectedDate ) {
//				        $( "#d_start" ).datepicker( "option", "minDate", selectedDate );
//				      }
//			}).datepicker("setDate", queryDate); 
//			$("#d_end").datepicker({
//				dateFormat : "yy-mm-dd",
//				
//				onClose: function( selectedDate ) {
//					 $( "#d_end" ).datepicker( "option", "maxDate", selectedDate );
//			      }
//			}).datepicker("setDate", queryDate);
			
			$("#d_fr").datepicker({
				dateFormat : "yy-mm-dd"
			}).datepicker("setDate", queryDate); 
			
			$("#d_start").datepicker({
				dateFormat : "yy-mm-dd"
			}).datepicker("setDate", queryDate); 
			
			$("#d_end").datepicker({
				dateFormat : "yy-mm-dd"
			}).datepicker("setDate", queryDate); 
			
			
		}
		
		
		frQuery = function(){
            
			var apiUrl = "/api/adActivity/frQuery";
			
			//获取查询条件
			var mktinfoName = $("#mktInfoName").val();
			var reportPlatform = $("#report_platform").children(".fba-on").attr("value");
			var reportProvince = $("#report_province_fr").parent().children(".dropdown-toggle").attr("value");
			
			if (typeof(reportProvince)!="undefined")
			{
			}
			else
		    {
				reportProvince = "0";
		    }
			
			
			var reportPersonGroup = $("#report_person_group_fr").parent().children(".dropdown-toggle").attr("value");
			var reportSelectType = $("#fr_select_type").val();
			var reportSelectMax = $("#fr_select_max").val();
			
			if (typeof(reportSelectType) =="undefined" || reportSelectType == "")
			{
				reportSelectType = 1;//默认点击
			}
			
			if (typeof(reportSelectMax) =="undefined" || reportSelectMax == "")
			{
				reportSelectMax = 10;
			}
			
	       if (typeof(reportPersonGroup)=="undefined" || reportPersonGroup == "")
		    {
            	reportPersonGroup = 0;
		    }
			//动态画出表头
			$("#fr_list").find("thead").append(null);
			$("#fr_list").find("thead").html("");
			var html = "<tr><th>媒体</th>";
			for(var index=1;index<=reportSelectMax;index++)
			{
				html = html+"<th id=\"fr_list_td"+index+"\">"+index+"+"+
				"<span style=\"font-size:10px;top:-5px;left:5px\" class=\"glyphicon glyphicon-chevron-up\" aria-hidden=\"true\"></span>"
				+"<span style=\"font-size:10px;top:5px;left:-5px\" class=\"glyphicon glyphicon-chevron-down\" aria-hidden=\"true\"></span>"
				+"</th>"
		    }
			html = html + "</tr>";
			$("#fr_list").find("thead").append(html);
			var queryDate = $("#d_fr").val();
		    
		    if(queryDate ==""){
				queryDate   = $.cookie(__cookieHeader + 'd_end');
			}
		    
			$.cookie(__cookieHeader + 'mktinfoName', mktinfoName, {
				expires : 1,
				secure : false
			});
			$.cookie(__cookieHeader + 'reportPlatform', reportPlatform, {
				expires : 1,
				secure : false
			});
			$.cookie(__cookieHeader + 'd_start', queryDate, {
				expires : 1,
				secure : false
			});
			$.cookie(__cookieHeader + 'd_end', queryDate, {
				expires : 1,
				secure : false
			});
			
			
			
			
			var params = {
					"__userKey" : __userKey,
					"pActivtiy_id" : mktinfoName,
					"pMedia_type" : reportPlatform,
					"pUser_type" : reportPersonGroup,
					"pPt_d" :   queryDate,
					"pArea_type" : 0,
					"pProvince_id" : reportProvince,
					"ptype":reportSelectType,
					"psalectMax":reportSelectMax
				};
			$.ajax({
				type : "POST",
				url : __webRoot + apiUrl,
				async : false,
				data : params,
				success : function (resp) {
					if (resp.resultCode != 0) {
						return;
					}
					
					$('#fr_click_stack').show();
					$('#fr_click_line').show();
					$('#fr_exp_stack').show();
					$('#fr_exp_line').show();
					$('#fr_landing_stack').show();
					$('#fr_landing_line').show();
					var frResults = resp.frResults;
					
					mktChart.common.frChart(frResults);
										
					
					$(".span_hide").removeClass("span_hide");
					//var tableList = resp.tableList;
					var tableListFK = resp.tableListFK;
					if(typeof(tableListFK) =="undefined")
					{
						tableListFK = [];
					}
					var instance = new notice(50, 5, tableListFK, "fr_list", false); // 实例化NoticeTable类
					instance.init(); // 画图
					instance.eventRegistered(); // 绑定事件
				}
			});	
//			$("#d_start").datepicker({
//				dateFormat : "yy-mm-dd",
//				 onClose: function( selectedDate ) {
//				        $( "#d_start" ).datepicker( "option", "minDate", selectedDate );
//				      }
//			}).datepicker("setDate", queryDate); 
//			$("#d_end").datepicker({
//				dateFormat : "yy-mm-dd",
//				
//				onClose: function( selectedDate ) {
//					 $( "#d_end" ).datepicker( "option", "maxDate", selectedDate );
//			      }
//			}).datepicker("setDate", queryDate);
			
			$("#d_hourstat").datepicker({
				dateFormat : "yy-mm-dd"
			}).datepicker("setDate", queryDate); 
			
			$("#d_start").datepicker({
				dateFormat : "yy-mm-dd"
			}).datepicker("setDate", queryDate); 
			
			$("#d_end").datepicker({
				dateFormat : "yy-mm-dd"
			}).datepicker("setDate", queryDate);
			
		}
		
		//导出基础数据
		frExport = function()
		{
            var url = __webRoot + "/api/adActivity/frExport?__userKey=" + __userKey;
			
          //获取查询条件
			var mktinfoName = $("#mktInfoName").val();
			var reportPlatform = $("#report_platform").children(".fba-on").attr("value");
			var reportProvince = $("#report_province_fr").parent().children(".dropdown-toggle").attr("value");
			
			if (typeof(reportProvince)=="undefined" || reportProvince == "")
		    {
				reportProvince = 0;
		    }			
			var reportPersonGroup = $("#report_person_group_fr").parent().children(".dropdown-toggle").attr("value");
			var reportSelectType = $("#fr_select_type").val();
			var reportSelectMax = $("#fr_select_max").val();
            var queryDate = $("#d_fr").val();
		    
            if (typeof(reportPersonGroup)=="undefined" || reportPersonGroup == "")
		    {
            	reportPersonGroup = 0;
		    }
            
            
		    if(queryDate ==""){
				queryDate   = $.cookie(__cookieHeader + 'd_end');
			}
			
			if (typeof(reportSelectType) =="undefined" || reportSelectType == "")
			{
				reportSelectType = 1;
			}
			
			if (typeof(reportSelectMax) =="undefined" || reportSelectMax == "")
			{
				reportSelectMax = 10;
			}
			
			url = url + "&mktinfoName=" + mktinfoName;
			url = url + "&reportPlatform=" + reportPlatform;			
			url = url + "&reportPersonGroup=" + reportPersonGroup;			
			url = url + "&pPt_d=" + queryDate;
			url = url + "&pArea_type=" + 0;			
			url = url + "&reportProvince=" + reportProvince;
			url = url + "&reportSelectType=" + reportSelectType;
			url = url + "&reportSelectMax=" + reportSelectMax;
			location.href = url;
		}
		
		
		$("#fr_list").find("thead").append(null);
		$("#fr_list").find("thead").html("");
		var html = "<tr><th>媒体</th>";
		for(var index=1;index<=10;index++)
		{
			html = html+"<th id=\"fr_list_td"+index+"\">"+index+"+"+
			"<span style=\"font-size:10px;top:-5px;left:5px\" class=\"glyphicon glyphicon-chevron-up\" aria-hidden=\"true\"></span>"
			+"<span style=\"font-size:10px;top:5px;left:-5px\" class=\"glyphicon glyphicon-chevron-down\" aria-hidden=\"true\"></span>"
			+"</th>"
	    }
		html = html + "</tr>";
		$("#fr_list").find("thead").append(html);
		
		
		//从cookie获取值
		var mktinfoName = $.cookie(__cookieHeader + 'mktinfoName');
		if (typeof(mktinfoName) != "undefined" && mktinfoName != "") {
			$("#mktInfoName").val(mktinfoName);
		}
		var reportPlatform = $.cookie(__cookieHeader + 'reportPlatform');

		
		//获取营销活动名称
		upmobui.common.getSelectList("/api/mktInfo/queryIdNameListWithControl", {
			"__userKey" : __userKey
		}, "mktInfoName", "mktinfoId", "mktinfoName", 1,mktinfoName);
			   
		mktNameChange = function(){
			$('#report_media').html('');
			$("#report_media").parent().children(".dropdown-toggle").removeAttr("value");
			upmobui.common.getDropdownMenu("/api/mktDic/queryActToWebList", {
				"__userKey" : __userKey,
				activity_id : $("#mktInfoName").val()
			}, "report_media", "id", "name", 0);
			upmobui.common.buttonSwitch();
			upmobui.common.dropdownMenuSwitch();
			upmobui.common.tabSwitch();
		}
		
		//获取平台类型
		upmobui.common.getReportPlatfom("/api/mktDic/queryDicCommonList", {
			"__userKey" : __userKey,
			type : "report_platform"
		}, "report_platform", "id", "name", 0,reportPlatform);
		//获取地域信息
		upmobui.common.getDropdownMenu("/api/mktDic/queryDicCommonList", {
			"__userKey" : __userKey,
			type : "province"
		}, "report_area", "id", "name", 0);

		//获取群体信息
		upmobui.common.getDropdownMenu("/api/mktDic/queryDicCommonList", {
			"__userKey" : __userKey,
			type : "report_person_group"
		}, "report_person_group", "id", "name", 0);
		
		//获取媒体信息
		upmobui.common.getDropdownMenu("/api/mktDic/queryActToWebList", {
			"__userKey" : __userKey,
			activity_id : $("#mktInfoName").val()
		}, "report_media", "id", "name", 0);
		
		upmobui.common.getDropdownMenu("/api/mktDic/queryDicCommonList", {
			"__userKey" : __userKey,
			type : "province"
		}, "report_province_fr", "id", "name", 0);

		//获取群体信息
		upmobui.common.getDropdownMenu("/api/mktDic/queryDicCommonList", {
			"__userKey" : __userKey,
			type : "report_person_group"
		}, "report_person_group_fr", "id", "name", 0);
		
		
		
		
		upmobui.common.buttonSwitch();
		upmobui.common.dropdownMenuSwitch();
		upmobui.common.tabSwitch();
		upmobui.common.baseRadioClick();
		upmobui.common.hourRadioClick();
		
		var tab = $.cookie(__cookieHeader + 'tab');
		if (typeof(tab) == "undefined" || tab == "" || tab == "undefined") 
		{
            tab = 0;
		}
		
		$("#page_" + tab).trigger("click");
		
		
		if (typeof(reportPlatform) != "undefined" && reportPlatform != "") {
			var objList = $("#report_platform").children();
			$.each(objList, function (name, value) {

				var obj = $(this);
				if (reportPlatform == obj.attr("value")) {
					obj.trigger("click");
				}
			});
		}
				
		
		var d_start = $.cookie(__cookieHeader + 'd_start');
		var d_end   = $.cookie(__cookieHeader + 'd_end');
		
		if(typeof(d_start) != "undefined" && d_start != ""){
			$("#d_start").datepicker({
				dateFormat : "yy-mm-dd"
//					,
//				 onClose: function( selectedDate ) {
//				        $( "#d_start" ).datepicker( "option", "minDate", selectedDate );
//				      }
			}).datepicker("setDate", d_start); 
		}else{
			$("#d_start").datepicker({
				dateFormat : "yy-mm-dd"
//					,
//				 onClose: function( selectedDate ) {
//				        $( "#d_start" ).datepicker( "option", "minDate", selectedDate );
//				      }
			}).datepicker("setDate", -30); 
		}
		
		if(typeof(d_end) != "undefined" && d_end != ""){
			
			$("#d_hourstat").datepicker({
				dateFormat : "yy-mm-dd"
			}).datepicker("setDate", d_end); 
			
			$("#d_fr").datepicker({
				dateFormat : "yy-mm-dd"
			}).datepicker("setDate", d_end); 
			
			$("#d_end").datepicker({
				dateFormat : "yy-mm-dd"
//					,
//				
//				onClose: function( selectedDate ) {
//					 $( "#d_end" ).datepicker( "option", "maxDate", selectedDate );
//			      }
			}).datepicker("setDate", d_end);
			
		}else{
			$("#d_hourstat").datepicker({
				dateFormat : "yy-mm-dd"
			}).datepicker("setDate", -1);
			
			$("#d_fr").datepicker({
				dateFormat : "yy-mm-dd"
			}).datepicker("setDate", -1);
			
			$("#d_end").datepicker({
				dateFormat : "yy-mm-dd"
//					,
//				
//				onClose: function( selectedDate ) {
//					 $( "#d_end" ).datepicker( "option", "maxDate", selectedDate );
//			      }
			}).datepicker("setDate", -1);
		}
        

		if(tab == 1)
		{			
			baseQuery();
		}else if (tab == 0){			
			hourQuery();
		}else if (tab == 2){
			frQuery();
		}
	}
});