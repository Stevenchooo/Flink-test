define(function(require, exports, module) {
	require('jquery');
	require("jquery-ui");
	require('dialog');
	require('cookie');
	var notice = require("notice");
	var utils = require('modules/base/utils');

	exports.showReport = function() {

		detail = function(value) {
			$.cookie(__cookieHeader + 'tab', value, {
				expires : 1,
				secure : false
			});
			showListPage('151', '/page/adActivity', 'adActivity');
		}



		// ///////////////////////////////////////////////
		var mktinfoName = $.cookie(__cookieHeader + 'mktinfoName');
		if (typeof (mktinfoName) != "undefined" && mktinfoName != "") {
			$("#mktInfoName").val(mktinfoName);
		}

		// ///////////////////////////////////////////////
		// 获取营销活动名称
		upmobui.common.getSelectList("/api/mktInfo/queryIdNameListWithControl",
				{
					"__userKey" : __userKey
				}, "mktInfoName", "mktinfoId", "mktinfoName", 1, mktinfoName);

		// 获取平台类型
		var reportPlatform = $.cookie(__cookieHeader + 'reportPlatform');
		upmobui.common.getReportPlatfom("/api/mktDic/queryDicCommonList", {
			"__userKey" : __userKey,
			type : "report_platform"
		}, "report_platform", "id", "name", 0, reportPlatform);

		// 获取地域信息
		upmobui.common.getDropdownMenu("/api/mktDic/queryDicCommonList", {
			"__userKey" : __userKey,
			type : "report_area"
		}, "report_area", "id", "name", 0);

		// 获取群体信息
		upmobui.common.getDropdownMenu("/api/mktDic/queryDicCommonList", {
			"__userKey" : __userKey,
			type : "report_person_group"
		}, "report_person_group", "id", "name", 0);

		upmobui.common.buttonSwitch();
		upmobui.common.dropdownMenuSwitch();
		upmobui.common.mediaChartRadioClick();
		upmobui.common.groupTransRadioClick();

		if (typeof (reportPlatform) != "undefined" && reportPlatform != "") {
			var objList = $("#report_platform").children();
			$.each(objList, function(name, value) {

				var obj = $(this);
				if (reportPlatform == obj.attr("value")) {
					obj.trigger("click");
				}
			});
		}

		var reportArea = $.cookie(__cookieHeader + 'reportArea');
		if (typeof (reportArea) != "undefined" && reportArea != "") {
			var objList = $("#report_area").children();
			$.each(objList, function(name, value) {

				var obj = $(this).children();
				if (reportArea == obj.attr("value")) {
					obj.trigger("click");
				}
			});
		}
		var reportPersonGroup = $.cookie(__cookieHeader + 'reportPersonGroup');
		if (typeof (reportPersonGroup) != "undefined"
				&& reportPersonGroup != "") {
			var objList = $("#report_person_group").children();
			$.each(objList, function(name, value) {

				var obj = $(this).children();
				if (reportArea == obj.attr("value")) {
					obj.trigger("click");
				}
			});
		}

		// 20160305
		var d_end = $.cookie(__cookieHeader + 'd_end');
		if (typeof (d_end) != "undefined" && d_end != "") {

			$("#d_start").datepicker({
				dateFormat : "yy-mm-dd"
			}).datepicker("setDate", d_end); // -1代表今天-1天

		} else {
			$("#d_start").datepicker({
				dateFormat : "yy-mm-dd"
			}).datepicker("setDate", -1); // -1代表今天-1天
		}

		
		
		//2016/4/25
		//注意！ 日期用这种！ 第二个框会自动过滤掉第一个框不可选的
		var fromDate = $(".date-from");
		var toDate = $(".date-to");
		
		fromDate.datepicker({
			changeMonth: true,
			changeYear: true, 
			dateFormat: "yy-mm-dd",
			onClose: function(selectedDate) {
				toDate.datepicker("option", "minDate", selectedDate);
			}
		});
		toDate.datepicker({
			changeMonth: true,
			changeYear: true,
			dateFormat: "yy-mm-dd",
			onClose: function(selectedDate) {
				fromDate.datepicker("option", "maxDate", selectedDate);
			}
		});
		
		// tab 切换
		$(".exp-lightab").on("click", ".elt-tab", function(){
			
			if($(this).hasClass("eltt-on")){
				return;
			}

			$(this).addClass("eltt-on").siblings(".elt-tab").removeClass("eltt-on");
			$(this).parent().siblings(".exp-ltcont").children(".exp-figbox").hide().filter('[data-tabvalue="' + $(this).data("tab") + '"]').show();
			if(this.id == "diversion"){
				if(BaseQueryFlag()){
					$("#baseDataTableLoading").show();
					$("#baseDataTable .expfig-detail").hide();
					$("#effectChannelTableLoading").show();
					$("#effectChannelTable .expfig-detail").hide();
					$("#diversionDay .expfig-detail").hide();
					$("#dayLoading").show();
					
					baseDataQuery(mktActivityNameList,adTypeList,mediaNameList,portList,landPlatformList,cycleStart,cycleEnd);
					diversionDayQuery(mktActivityNameList,adTypeList,mediaNameList,portList,landPlatformList,cycleStart,cycleEnd);
					effectChannel(mktActivityNameList,adTypeList,mediaNameList,portList,landPlatformList,cycleStart,cycleEnd);
					
					
					mktActivityNameListBaseTmp = mktActivityNameList;
					adTypeListBaseTmp = adTypeList;
					mediaNameListBaseTmp = mediaNameList;
					portListBaseTmp = portList;
					landPlatformListBaseTmp = landPlatformList;
					cycleStartBaseTmp = cycleStart;
					cycleEndBaseTmp = cycleEnd;
					
				}
				resizeTable(this.id);
			}else if(this.id == "conversion"){
				if(conversionQueryFlag()){
					transPicReprot();
					
					mktActivityNameListConversionTmp = mktActivityNameList;
					adTypeListConversionTmp = adTypeList;
					mediaNameListConversionTmp = mediaNameList;
					portListConversionTmp = portList;
					landPlatformListConversionTmp = landPlatformList;
					cycleStartConversionTmp = cycleStart;
					cycleEndConversionTmp = cycleEnd;
				}
				resizeTable(this.id);
			}else if(this.id == "heat"){
				if(heatQueryFlag()){
					pageUrl();
					
					mktActivityNameListHeatTmp = mktActivityNameList;
					adTypeListHeatTmp = adTypeList;
					mediaNameListHeatTmp = mediaNameList;
					portListHeatTmp = portList;
					landPlatformListHeatTmp = landPlatformList;
					cycleStartHeatTmp = cycleStart;
					cycleEndHeatTmp = cycleEnd;
				}
				resizeTable(this.id);
			}else if(this.id == "crowd"){
				if(crowdQueryFlag()){
					crowd();
					
					mktActivityNameListCrowdTmp = mktActivityNameList;
					adTypeListCrowdTmp = adTypeList;
					mediaNameListCrowdTmp = mediaNameList;
					portListCrowdTmp = portList;
					landPlatformListCrowdTmp = landPlatformList;
					cycleStartCrowdTmp = cycleStart;
					cycleEndCrowdTmp = cycleEnd;
				}
				resizeTable(this.id);
			}
		});
		
		//记录人群查询参数
		var mktActivityNameListCrowdTmp = "";
		var adTypeListCrowdTmp = "";
		var mediaNameListCrowdTmp = "";
		var portListCrowdTmp = "";
		var landPlatformListCrowdTmp = "";
		var cycleStartCrowdTmp = "";
		var cycleEndCrowdTmp = "";
		crowdQueryFlag = function () {
			if(mktActivityNameListCrowdTmp == mktActivityNameList && adTypeListCrowdTmp == adTypeList
					&& mediaNameListCrowdTmp == mediaNameList && portListCrowdTmp == portList &&
					landPlatformListCrowdTmp == landPlatformList && cycleStartCrowdTmp == cycleStart
					&& cycleEndCrowdTmp == cycleEnd){
				return false;
			}else{
				return true;
			}
		};
		
		//记录热度查询参数
		var mktActivityNameListHeatTmp = "";
		var adTypeListHeatTmp = "";
		var mediaNameListHeatTmp = "";
		var portListHeatTmp = "";
		var landPlatformListHeatTmp = "";
		var cycleStartHeatTmp = "";
		var cycleEndHeatTmp = "";
		heatQueryFlag = function () {
			if(mktActivityNameListHeatTmp == mktActivityNameList && adTypeListHeatTmp == adTypeList
					&& mediaNameListHeatTmp == mediaNameList && portListHeatTmp == portList &&
					landPlatformListHeatTmp == landPlatformList && cycleStartHeatTmp == cycleStart
					&& cycleEndHeatTmp == cycleEnd){
				return false;
			}else{
				return true;
			}
		};
		
		//记录导流查询参数
		var mktActivityNameListConversionTmp = "";
		var adTypeListConversionTmp = "";
		var mediaNameListConversionTmp = "";
		var portListConversionTmp = "";
		var landPlatformListConversionTmp = "";
		var cycleStartConversionTmp = "";
		var cycleEndConversionTmp = "";
		conversionQueryFlag = function () {
			if(mktActivityNameListConversionTmp == mktActivityNameList && adTypeListConversionTmp == adTypeList
					&& mediaNameListConversionTmp == mediaNameList && portListConversionTmp == portList &&
					landPlatformListConversionTmp == landPlatformList && cycleStartConversionTmp == cycleStart
					&& cycleEndConversionTmp == cycleEnd){
				return false;
			}else{
				return true;
			}
		};
		
		//记录概况查询参数
		var mktActivityNameListTmp = "";
		var adTypeListTmp = "";
		var mediaNameListTmp = "";
		var portListTmp = "";
		var landPlatformListTmp = "";
		var cycleStartTmp = "";
		var cycleEndTmp = "";
		generalSituatQueryFlag = function () {
			if(mktActivityNameListTmp == mktActivityNameList && adTypeListTmp == adTypeList
					&& mediaNameListTmp == mediaNameList && portListTmp == portList &&
					landPlatformListTmp == landPlatformList && cycleStartTmp == cycleStart
					&& cycleEndTmp == cycleEnd){
				return false;
			}else{
				return true;
			}
		};
		
		//记录导流查询参数
		var mktActivityNameListBaseTmp = "";
		var adTypeListBaseTmp = "";
		var mediaNameListBaseTmp = "";
		var portListBaseTmp = "";
		var landPlatformListBaseTmp = "";
		var cycleStartBaseTmp = "";
		var cycleEndBaseTmp = "";
		BaseQueryFlag = function () {
			if(mktActivityNameListBaseTmp == mktActivityNameList && adTypeListBaseTmp == adTypeList
					&& mediaNameListBaseTmp == mediaNameList && portListBaseTmp == portList &&
					landPlatformListBaseTmp == landPlatformList && cycleStartBaseTmp == cycleStart
					&& cycleEndBaseTmp == cycleEnd){
				return false;
			}else{
				return true;
			}
		};
		
		var mktActivityNameList = "";
		var adTypeList = "";
		var mediaNameList = "";
		var portList = "";
		var landPlatformList = "";
		var cycleStart = "";
		var cycleEnd = "";
		//获取选择的营销活动ID
		getMktID = function () {
			//获取选择的营销活动
			mktActivityNameList = "";
			for(var i = 0;i < acindex;i++){
				if($("#mktName"+i).hasClass("mmsopt-on")){
					//mktActivityNameList += $("#mktName"+i).html().trim() + ",";
					mktActivityNameList += document.getElementById("mktName"+i).attributes.value.nodeValue + ",";
				}
			}
			
			if (mktActivityNameList.length > 0){
				mktActivityNameList = mktActivityNameList.substring(0 , mktActivityNameList.length - 1);
			}
			return mktActivityNameList;
		};
		
		$("#mmsDoSearch").click(function(){
			$("#mmsresFrame").hide();
			mktActivityNameList = getMktID();
			if(mktActivityNameList.length == "") {
				dialog('请选择营销活动！', function() {
				}).showModal();
				return;
			}
			
			//获取广告类型
			adTypeList = "";
			for(var i = 0; i < adTypeId; i++){
				if($("#adTypeId"+i).hasClass("mmsopt-on")){
					adTypeList += document.getElementById("adTypeId"+i).attributes.value.nodeValue + ",";
				}
			}
			
			if (adTypeList.length > 0) {
				adTypeList = adTypeList.substring(0 , adTypeList.length - 1);
			}
			
			//获取选择的媒体
			mediaNameList = "";
			for(var i = 0;i < maindex;i++){
				if($("#web"+i).hasClass("mmsopt-on")){
					mediaNameList += $("#web"+i).html().trim() + ",";
				}
			}
			
			if (mediaNameList.length > 0){
				mediaNameList = mediaNameList.substring(0 , mediaNameList.length - 1);
			}
			
			//获取选择的端口
			portList = "";
			for(var i = 0;i < portId;i++){
				if($("#portId"+i).hasClass("mmsopt-on")){
					portList += $("#portId"+i).html().trim() + ",";
				}
			}
			
			if (portList.length > 0){
				portList = portList.substring(0 , portList.length - 1);
			}
			
			//获取选择的着陆平台
			landPlatformList = "";
			for(var i = 0;i < landPlatformId;i++){
				if($("#landPlatformId"+i).hasClass("mmsopt-on")){
					landPlatformList += $("#landPlatformId"+i).html().trim() + ",";
				}
			}
			
			
			if (landPlatformList.length > 0){
				landPlatformList = landPlatformList.substring(0 , landPlatformList.length - 1);
			}
			
			//判断投放周期的开始时间结束时间是否为空
			cycleStart = $(".date-from").val();
			cycleEnd = $(".date-to").val();
			
			if(cycleStart == "" || cycleEnd == ""){
				dialog('请选择投放周期！', function() {
				}).showModal();
				return;
			}
			
			$("#generalSituation").click();
			if(generalSituatQueryFlag()){
				generalSituatQuery(mktActivityNameList,adTypeList,mediaNameList,portList,landPlatformList,cycleStart,cycleEnd);
			}
			// 收起视乎也不算好看
			//$(".dpf-opblk").hide();
			//$(".dpf-link").data("status", "off").text("展开选择框");
			//显示查询结果
			mktActivityNameListTmp = mktActivityNameList;
			adTypeListTmp = adTypeList;
			mediaNameListTmp = mediaNameList;
			portListTmp = portList;
			landPlatformListTmp = landPlatformList;
			cycleStartTmp = cycleStart;
			cycleEndTmp = cycleEnd;
			$("#mmsresFrame").show();
		});

		generalSituatQueryFlag = function () {
			if(mktActivityNameListTmp == mktActivityNameList && adTypeListTmp == adTypeList
					&& mediaNameListTmp == mediaNameList && portListTmp == portList &&
					landPlatformListTmp == landPlatformList && cycleStartTmp == cycleStart
					&& cycleEndTmp == cycleEnd){
				return false;
			}else{
				return true;
			}
		};

		effectChannel = function (mktActivityNameList,adTypeList,mediaNameList,portList,landPlatformList,cycleStart,cycleEnd) {
			var apiUrl = "/api/adActivity/frQuery";
			var params = {
					"__userKey" : __userKey,
					"pActivtiy_id" : mktActivityNameList,
					"pAd_type":adTypeList,
					"pMaterial_type":mediaNameList,
					"pMedia_type":portList,
					"pLandingPlat":landPlatformList,
					"pBegin_time":cycleStart,
					"pEnd_time":cycleEnd
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
					
					$("#effectChannelTableLoading").hide();
					$("#effectChannelTable .expfig-detail").show();
					
					$('#fr_click_stack').show();
					$('#fr_click_line').show();
					$('#fr_exp_stack').show();
					$('#fr_exp_line').show();
					$('#fr_landing_stack').show();
					$('#fr_landing_line').show();
					var frResults = resp.frResults;
					
					var map = mktChart.common.frChart(frResults);
					chartDiversionFrequencyBG  = map["expChartS"];
					chartDiversionFrequencyDJ  = map["clickChartS"];
					chartDiversionFrequencyLanding  = map["landingChartS"];
					chartDiversionFrequencyBGL  = map["chartExpL"];
					chartDiversionFrequencyDJl  = map["chartClickL"];
					chartDiversionFrequencyLandingl  = map["chartLandingL"];
										
					
//					$(".span_hide").removeClass("span_hide");
//					//var tableList = resp.tableList;
//					var tableListFK = resp.tableListFK;
//					if(typeof(tableListFK) =="undefined")
//					{
//						tableListFK = [];
//					}
//					var instance = new notice(50, 5, tableListFK, "fr_list", false); // 实例化NoticeTable类
//					instance.init(); // 画图
//					instance.eventRegistered(); // 绑定事件
				}
			});	
		};
		
		baseDataQuery = function(mktActivityNameList, adTypeList, mediaNameList, portList, landPlatformList, cycleStart, cycleEnd){
			var apiUrl = "/api/adActivity/baseQuery";
			var params = {
					"__userKey" : __userKey,
					"pActivtiy_id" : mktActivityNameList,
					"pAd_type":adTypeList,
					"pMaterial_type":mediaNameList,
					"pMedia_type":portList,
					"pLandingPlat":landPlatformList,
					"pBegin_time":cycleStart,
					"pEnd_time":cycleEnd
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
					
					$("#baseDataTableLoading").hide();
					$("#baseDataTable .expfig-detail").show();
					
					$('#base_bg_chart').show();
					$('#base_dj_chart').show();
					var pvUvList = resp.pvList;
					var UvList   = resp.uvList;
					//基础数据
					var baseChartMap = mktChart.common.baseChart(pvUvList,UvList);
					chartDiversionBasePV = baseChartMap["pvChart"];				
					chartDiversionBaseUV = baseChartMap["uvChart"];
					
					var chartRadioValue = $("input[name='base_radio']:checked").val();
					if(chartRadioValue == "bg")
					{
						//$('#base_bg_chart').show();
						$('#base_dj_chart').hide();
					}
					else
					{
						$('#base_bg_chart').hide();
						//$('#base_dj_chart').show();
					}
					
//					$(".span_hide").removeClass("span_hide");
//					var tableList = resp.tableList;
//					var instance = new notice(10, 4, tableList, "base_list", false); // 实例化NoticeTable类
//					instance.init(); // 画图
//					instance.eventRegistered(); // 绑定事件
				}
			});
		};
		
		generalSituatQuery = function(mktActivityNameList, adTypeList, mediaNameList, portList, landPlatformList, cycleStart, cycleEnd){
			$("#tableLoading").show();
			$(".exp-ltcont").hide();
			var putCycleStartTime = $("#diversion_start").val();
			var putCycleEndTime = $("#diversion_end").val();
			var reportPersonGroup;
			var reportArea;
			var queryParams = {
					"__userKey" : __userKey,
					"pActivtiy_id" : mktActivityNameList,
					"pAd_type" : adTypeList,
					"pMaterial_type" : mediaNameList,
					"pMedia_type" : portList,
					"pLandingPlat" : landPlatformList,
					"pBegin_time": cycleStart,
					"pEnd_time": cycleEnd
				}
			var apiUrl = "/api/generalSituat/query";
			$.ajax({
				type : "POST",
				url : __webRoot + apiUrl,
				async : true,
				data : queryParams,
				success : function(resp) {
					if (resp.resultCode != 0) {
						return;
					}

					var finalTime=resp.fianlTime;

					$("#tableLoading").hide();
					$(".exp-ltcont").show();
					
					var finalTime = resp.fianlTime[0].finalTime;
					
					$("#finalTime").html(
							"截止日数据（"+finalTime.substring(0,4)+"年"+finalTime.substring(4,6)+"月"+finalTime.substring(6,8)+"日）");
					

					var general = resp.general;
					
					if (typeof (general) != "undefined"
							&& general != "") {
						var all_bg_pv = general[0].all_bg_pv;
						//var bg_pv_rate = general.bg_pv_rate;
						$("#general_all_bg_pv").html(
								"曝光次数: "+all_bg_pv);
						
						var all_bg_uv = general[0].all_bg_uv;
						$("#general_all_bg_uv").html(
								"曝光人次: "+all_bg_uv);
						
						var all_dj_pv = general[0].all_dj_pv;
						//var dj_pv_rate = general.dj_pv_rate;
						$("#general_all_dj_pv").html(
								"点击次数: "+all_dj_pv);
						
						var all_dj_uv = general[0].all_dj_uv;
						$("#general_all_dj_uv").html(
								"点击人次: "+all_dj_uv);

						var ctr = general[0].ctr;
						if (typeof (ctr) == "undefined") {
							ctr = "0";
						}
						$("#general_ctr").html("CTR: "+ctr);
						
						var buy_rate = general[0].buy_rate;
						if (typeof (buy_rate) == "undefined") {
							buy_rate = "0";
						}
						$("#buy_rate").html("购买转化: "+buy_rate);

//						var all_bg_uv = general.all_bg_uv;
//						$("#general_all_bg_uv").html(all_bg_uv);
						
					}

					var mediaList = resp.mediaList;

					var mediaListDJ = resp.mediaDjList;

					if (typeof (mediaList) == "undefined"
							|| mediaList == "") {
						mediaList = [];
					}
					if (typeof (mediaListDJ) == "undefined"
							|| mediaListDJ == "") {
						mediaListDJ = [];
					}
					var meidaLegendArray = [ 'PV', 'UV' ];
					var meidaCategoryArray = [];
					var meidaCategoryArrayDJ = [];
					var bgPvArray = [];
					var bgUvArray = [];
					var djPvArray = [];
					var djUvArray = [];
					$.each(mediaList,
							function(i, value) {
								meidaCategoryArray
										.push(value["mt_media_name"]);
								bgPvArray.push(value["mt_bg_pv_sum"]);
								bgUvArray.push(value["mt_bg_uv_sum"]);
							});
					$.each(mediaListDJ, function(i, value) {
						meidaCategoryArrayDJ
								.push(value["mt_media_name"]);
						djPvArray.push(value["mt_dj_pv_sum"]);
						djUvArray.push(value["mt_dj_uv_sum"]);
					});

					$('#media_chart_bg').show();
					$('#media_chart_dj').show();

					var mediaChartBg = echarts.init(document
							.getElementById('media_chart_bg'),"macarons");

					var mediaChartDj = echarts.init(document
							.getElementById('media_chart_dj'),"macarons");

					var optionBg = upmobui.common.getChartOption(
							meidaLegendArray, meidaCategoryArray, [
									bgPvArray, bgUvArray ], 'bar');

					var optionDj = upmobui.common.getChartOption(
							meidaLegendArray, meidaCategoryArrayDJ, [
									djPvArray, djUvArray ], 'bar');

					mediaChartBg.setOption(optionBg);
					mediaChartDj.setOption(optionDj);
					
					echartsGeneralSituatBG = mediaChartBg;
					echartsGeneralSituatDj = mediaChartDj;
					
					var chartRadioValue = $(
							"input[name='media_chart_radio']:checked")
							.val();

					if (chartRadioValue == "bg") {
						$('#media_chart_bg').show();
						$('#media_chart_dj').hide();
					} else {
						$('#media_chart_bg').hide();
						$('#media_chart_dj').show();
					}

					var hourList = resp.hourList;

					if (typeof (hourList) == "undefined"
							|| hourList == "") {
						hourList = [];
					}

					echartsGeneralSituatHour = mktChart.common.hourChart("hour_chart", hourList);

					var djList = resp.djList;
					if (typeof (djList) == "undefined" || djList == "") {
						djList = [];
					}
					echartsGeneralSituatDJTrend = mktChart.common.djChart("dj_chart", resp.djList);

					var areaList = resp.areaList;

					if (typeof (areaList) == "undefined"
							|| areaList == "") {
						areaList = []

					}
					echartsGeneralSituatMap = mktChart.common.chinaMap("area_chart", areaList);

//					var groupTrans = resp.groupTrans;
//					if (typeof (groupTrans) != "undefined"
//							&& groupTrans != "") {
//						mktChart.common
//								.GroupTransChart(resp.groupTrans);
//					}

				}
			});
		};
		
		$(".mktabber-frame").on("click", ".mktf-mitem", function(){
			
			if($(this).hasClass("mktf-mcur")){
				return;
			}
			
			$(this).addClass("mktf-mcur").siblings(".mktf-mitem").removeClass("mktf-mcur");
			$(".mktabber-frame").find("[data-cont='" + $(this).data("tab") + "']").show().siblings().hide();
			
		});
		
		$(".dpf-opblk").on("click", ".mmsopt-item", function(){
			if($(this).hasClass("mmsopt-on")){
				$(this).removeClass("mmsopt-on");
				if (blackArr.indexOf(this.id) != -1) {
					$(this).css("color","black");
				}else{
					$(this).removeAttr("style");
				}
				if($(this).parent().children(":first").hasClass("mmsopt-on")){
					$(this).parent().children(":first").removeClass("mmsopt-on");
				}
			}
			else{
				$(this).addClass("mmsopt-on");
				$(this).css("color","#087600");
				var otherMediaSpan = $(this).siblings(".mmsopt-item");
				var allSelectFlag = 0;
				for(var i = 0;i<otherMediaSpan.length;i++){
					var a  = "#"+otherMediaSpan[i].id;
					if($("#"+otherMediaSpan[i].id).hasClass("mmsopt-on") && $("#"+otherMediaSpan[i].id).hasClass("mmsopt-item")){
						allSelectFlag++;
					}
				}
				if(allSelectFlag == otherMediaSpan.length){
					$(this).parent().children(":first").addClass("mmsopt-on");
				}
			}
		});
		
		$(".dpf-opblk").on("click", ".mmsopt-lineselect", function(){
			if($(this).hasClass("mmsopt-on")){
				$(this).removeClass("mmsopt-on").siblings(".mmsopt-item").removeClass("mmsopt-on");
				var brotherId = $(this).siblings(".mmsopt-item");
				for (var index = 0;index < brotherId.length; index++) {
					var id = brotherId[index].id;
					if (blackArr.indexOf(id) != -1) {
						$("#"+id).css("color","black");
					}else{
						$("#"+id).removeAttr("style");
					}
				}
			}
			else{
				$(this).addClass("mmsopt-on").siblings(".mmsopt-item").addClass("mmsopt-on");
				$(this).siblings(".mmsopt-item").css("color","#087600");
			}
			
			selectMktMedia(this);
		});
		
		$(".dp-filter").on("click", ".dpf-link", function(){
			if($(this).data("status") === "on"){
				$(this).parents(".dp-filter").eq(0).children("[data-block='" + $(this).data("target") + "']").hide();
				$(this).data("status", "off").text("展开选择框");
			}
			else{
				$(this).parents(".dp-filter").eq(0).children("[data-block='" + $(this).data("target") + "']").show();
				$(this).data("status", "on").text("收起选择框");
			}
		});
		
		//端口赋值
		var portParams = {
				"__userKey" : __userKey,
				type : "port"
		};
		var portId = 0;
		$.ajax({
			type : "POST",
			url : __webRoot + "/api/mktDic/queryDicCommonList",
			async : true,
			data : portParams,
			success : function(resp) {
				if (resp.resultCode != 0) {
					return;
				}
				var portArray = resp.results;
				for(var index in portArray){
					$("#portSelect :last-child").after('<span id="portId'+portId+'" class="mmsopt-item" data-key="活动ID"> '+portArray[index].name+'</span>');
					portId++;
				}
			}
		});
		
		//着陆平台赋值
		var landPlatformId = 0;
		var landPlatformParams = {
				"__userKey" : __userKey,
				type : "land_platform"
		};
		$.ajax({
			type : "POST",
			url : __webRoot + "/api/mktDic/queryDicCommonList",
			async : true,
			data : landPlatformParams,
			success : function(resp) {
				if (resp.resultCode != 0) {
					return;
				}
				var portArray = resp.results;
				for(var index in portArray){
					$("#landPlatformSelect :last-child").after('<span id="landPlatformId'+landPlatformId+'" class="mmsopt-item" data-key="活动ID"> '+portArray[index].name+'</span>');
					landPlatformId++;
				}
			}
		});
		
		//营销活动
		var acindex = 0;
		var mktSelectIndex = 0;
		var activityParams={"__userKey" : __userKey};
		$.ajax({
			type : "POST",
			url : __webRoot +"/api/mktInfo/getMktNameListByDept",
			async : true,
			data : activityParams,
			success : function (resp) {
				if (resp.resultCode != 0) {
					return;
				}
				
				var mkts=utils.mktToJson(resp.result);
				
				var html="";
				if(typeof(mkts) != "undefined" && mkts != ""){
					html = " <a href=\"javascript:;\" class=\"mktf-mitem mktf-mcur\" data-tab=\""+mkts[0].create_year+"\">" 
					+"<span class=\"mktf-mibox\">"+"<em class=\"mktf-mtxt\">"+mkts[0].create_year+"</em></span></a>";
					$(html).appendTo("#tfVert-menu");
					html="";
					html+="<div class=\"mktf-verTab\" data-cont=\""+mkts[0].create_year+"\">";
					for(var i=0;i<mkts.length;i++){
						if(mkts[i].create_year==mkts[0].create_year){
							html+="<div class=\"mms-opt\">"+"<div class=\"mmsopt-h\">"+mkts[i].create_month+"</div>"
							+"<div class=\"mmsopt-b\">"
							+"<span class=\"mmsopt-lineselect\" id=\"mktSelectIndex"+mktSelectIndex+"\">全选</span>";
							mktSelectIndex++;
							for(var j=0;j<mkts[i].mktarr.length;j++){
								html+="<span id=\"mktName"+acindex+"\" value=\""+mkts[i].mktarr[j].mktId+"\" class=\"mmsopt-item\" onClick=\"getMktName(this)\" data-key=\""+mkts[i].mktarr[j].mktName+"\">"+mkts[i].mktarr[j].mktName+"</span>";
								acindex++;
							}
							html+="</div>";
						}else{
							break;
						}
						html+="</div>";
					}
					$(html).appendTo("#dpf-events");
					//年份
					for(var i=1;i<mkts.length;i++){
						html="";
						if(mkts[i].create_year==mkts[i-1].create_year){
							continue;
						}else{
							html = " <a href=\"javascript:;\" class=\"mktf-mitem \" data-tab=\""+mkts[i].create_year+"\">" 
							+"<span class=\"mktf-mibox\">"+"<em class=\"mktf-mtxt\">"+mkts[i].create_year+"</em></span></a>";
							$(html).appendTo("#tfVert-menu");
							html="";
							html+="<div class=\"mktf-verTab\" data-cont=\""+mkts[i].create_year+"\" style=\"display:none;\">";
							//月份
							for(var j=i;j<mkts.length;j++){
								if(mkts[i].create_year==mkts[j].create_year){
									html+="<div class=\"mms-opt\">"+"<div class=\"mmsopt-h\">"+mkts[j].create_month+"</div>"
									+"<div class=\"mmsopt-b\">"
									+"<span class=\"mmsopt-lineselect\" id=\"mktSelectIndex"+mktSelectIndex+"\">全选</span>";
									mktSelectIndex++;
									//活动
									for(var k=0;k<mkts[j].mktarr.length;k++){
										html+="<span id=\"mktName"+acindex+"\" class=\"mmsopt-item\" value=\""+mkts[j].mktarr[k].mktId+"\" onClick=\"getMktName(this)\" data-key=\""+mkts[j].mktarr[k].mktName+"\">"+mkts[j].mktarr[k].mktName+"</span>";
										acindex++;
									}
									html+="</div>";
								}else{
									break;
								}
								html+="</div>";
							}
							$(html).appendTo("#dpf-events");
						}
					}
				}
			}
		});
		//媒体分类
		//广告类型
		var adTypeId = 0;
		var adParams = {
			"__userKey" : __userKey
		}
		$.ajax({
			type : "POST",
			url : __webRoot +"/api/generalSituat/getMaterialType",
			data:adParams,
			async : false,
			success : function (resp) {
				if (resp.resultCode != 0) {
					return;
				}
				var materialTypes =resp.result;
				if(typeof(materialTypes) != "undefined" && materialTypes != ""){
					var html="";
					for(var i=0;i<materialTypes.length;i++){
						html+="<span class=\"mmsopt-item\" id=\"adTypeId"+adTypeId+"\" value=\""+materialTypes[i].materialId+"\" data-key=\""+materialTypes[i].materialId+"\">"+materialTypes[i].materialName+"</span>";
						adTypeId++;
					}
					$(html).appendTo("#materialType");
				}
			},
		});
		//媒体分类及网站
		var maindex = 0;
		$.ajax({
			type : "POST",
			url : __webRoot +"/api/generalSituat/getMediaQuery",
			data:adParams,
			async : false,
			success:function(resp){
				if (resp.resultCode != 0) {
					return;
				}
				var webs=utils.mediaToJson(resp.result);
				if(typeof(webs) != "undefined" && webs != ""){
					for(var i=0;i<webs.length;i++){
						html="";
						html+="<div class=\"mms-opt\">"+
							"<div class=\"mmsopt-h\">"+webs[i].mediaName+"</div>"
							+"<div class=\"mmsopt-b\">"
							+"<span class=\"mmsopt-lineselect\">全选</span>";
						for(var j=0;j<webs[i].mktarr.length;j++){
							html+="<span class=\"mmsopt-item\" id=\"web"+maindex+"\" data-key=\""+webs[i].mktarr[j]+"\"> "+webs[i].mktarr[j]+"</span>";
							maindex++;
						}
						html+="</div></div>";
						$(html).appendTo("#dpf-media");
					}
				}
			}
		});
//		//获取营销活动的id
//		var mktQuery="";
//		var xx=$("#dpf-events .mmsopt-b span");
//		for(var i=0;i<xx.length;i++){
//			mktQuery+=xx.eq(i).val();
//			if(i!=xx.length-1){
//				mktQuery+=",";
//			}
//		}

		var blackArr = [];
		//获取活动对应的媒体
		getMktName = function (th) {
			//var mktName = document.getElementById(th.id).attributes.value.nodeValue;
			var mktName = $("#"+th.id).html();
			adParams = {
					"__userKey" : __userKey,
					"adName":mktName
				};
			var webName="";
			$.ajax({
				type : "POST",
				url : __webRoot +"/api/mktDic/queryDicWebNameListByAd",
				data:adParams,
				async : false,
				success:function(resp){
					if (resp.resultCode != 0) {
						return;
					}
					if(resp.results!=null){
						webName=resp.results;
					}
				}
			});
			if(!$("#"+th.id).hasClass("mmsopt-on")){
				for(var i=0;i<webName.length;i++){
					for(var j=0;j<=maindex;j++){
						if(webName[i].name==$.trim($("#web"+j).html())){
							$("#web"+j).attr("style","color:black;");
							pushInBlackArr("web"+j);
							break;
						}
					}
				}
			}else{
				for(var i=0;i<webName.length;i++){
					for(var j=0;j<=maindex;j++){
						if(webName[i].name==$.trim($("#web"+j).html())){
							$("#web"+j).removeAttr("style");
							spliceInBlackArr("web"+j);
							break;
						}
					}
				}
			}
		};
		
		//根据选中的营销活动获取媒体
		getMedia = function (mktNameList) {
			var webName;
			adParams = {
					"__userKey" : __userKey,
					"adName": mktNameList
				};
			$.ajax({
				type : "POST",
				url : __webRoot +"/api/mktDic/queryDicWebNameListByAdid",
				data: adParams,
				async : false,
				success:function(resp){
					if (resp.resultCode != 0) {
						return;
					}
					if(resp.results!=null){
						webName=resp.results;
					}
				}
			});
			return webName;
		};
		
		selectMktMedia = function (object) {
			var mktNameList = "";
			var brotherDom = $("#"+object.id).siblings();
			for (var index = 0;index < brotherDom.length; index++) {
				mktNameList += brotherDom[index].attributes.value.nodeValue + ",";
			}
			if (mktNameList.length > 0) {
				mktNameList = mktNameList.substring(0 , mktNameList.length-1);
			}
			
			var webName = getMedia(mktNameList);
			
			if($("#"+object.id).hasClass("mmsopt-on")){
				for (var index in webName) {
					for (var i = 0;i < maindex; i++) {
						if($("#web"+i).html().trim() == webName[index].name){
							$("#web"+i).css("color","black");
							pushInBlackArr("web"+i);
							break;
						}
					}
				}
			}else{
				var selectedMediaName = getMedia(getSelectMktName());
				for (var index in webName) {
					for (var i = 0;i < maindex; i++) {
						if($("#web"+i).html().trim() == webName[index].name){
							var flag = true;
							for (var j in selectedMediaName) {
								if(selectedMediaName[j].name == webName[index].name){
									flag = false;
									break;
								}
							}
							if (flag) {
									$("#web"+i).removeAttr("style");
									spliceInBlackArr("web"+i);
									break;
							}
						}
					}
				}
			}
		};
		
		//获取选择的营销活动名称
		getSelectMktName = function () {
			//获取选择的营销活动
			var mktActivityNameList = "";
			for(var i = 0;i < acindex;i++){
				if($("#mktName"+i).hasClass("mmsopt-on")){
					mktActivityNameList += $("#mktName"+i).html().trim() + ",";
				}
			}
			
			if (mktActivityNameList.length > 0){
				mktActivityNameList = mktActivityNameList.substring(0 , mktActivityNameList.length - 1);
			}
			return mktActivityNameList;
		};
		
		changeTable = function () {
			var chartRadioValue = $("input[name='base_radio']:checked").val();
			if(chartRadioValue == "bg")
			{
				$('#base_bg_chart').show();
				$('#base_dj_chart').hide();
				chartDiversionBasePV.resize();
			}
			else
			{
				$('#base_bg_chart').hide();
				$('#base_dj_chart').show();
				chartDiversionBaseUV.resize();
			}
		};
		
		pushInBlackArr = function (mediaName) {
			if(blackArr.indexOf(mediaName) == -1){
				blackArr.push(mediaName);
			}
		};
		
		spliceInBlackArr = function (mediaName) {
			var arrayIndex = blackArr.indexOf(mediaName);
			if(blackArr.indexOf(mediaName) != -1){
				blackArr.splice(arrayIndex , 1);
			}
		};
		
		var dayData = ["dj_num_day_chart","dj_person_day_chart","bg_num_day_chart","bg_person_day_chart"];
		changeDayChart = function (object) {
			var val = object.value;
			for(var index in dayData){
				if(dayData[index] == val){
					$("#"+dayData[index]).show();
					if(val == "dj_num_day_chart"){
						chartDiversionDayDJPV.resize();
					}
					if(val == "dj_person_day_chart"){
						chartDiversionDayDJUV.resize();
					}
					if(val == "bg_num_day_chart"){
						chartDiversionDayBGPV.resize();
					}
					if(val == "bg_person_day_chart"){
						chartDiversionDayBGUV.resize();
					}
				}else{
					$("#"+dayData[index]).hide();
				}
			}
		};
		
		var hourData = ["dj_num_hour_chart","dj_person_hour_chart","bg_num_hour_chart","bg_person_hour_chart"];
		changeHourChart = function (object) {
			var val = object.value;
			for(var index in hourData){
				if(hourData[index] == val){
					$("#"+hourData[index]).show();
					if(val == "dj_num_hour_chart"){
						chartDiversionHourDJPV.resize();
					}
					if(val == "dj_person_hour_chart"){
						chartDiversionHourDJUV.resize();
					}
					if(val == "bg_num_hour_chart"){
						chartDiversionHourBGPV.resize();
					}
					if(val == "bg_person_hour_chart"){
						chartDiversionHourBGUV.resize();
					}
				}else{
					$("#"+hourData[index]).hide();
				}
			}
		};
		
		diversionDayQuery = function (mktActivityNameList,adTypeList,mediaNameList,portList,landPlatformList,cycleStart,cycleEnd) {
			var apiUrl = "/api/generalSituat/diversionDayQuery";
			var params = {
					"__userKey" : __userKey,
					"pActivtiy_id" : mktActivityNameList,
					"pAd_type":adTypeList,
					"pMaterial_type":mediaNameList,
					"pMedia_type":portList,
					"pLandingPlat":landPlatformList,
					"pBegin_time":cycleStart,
					"pEnd_time":cycleEnd
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
					
					$("#diversionDay .expfig-detail").show();
					$("#dayLoading").hide();
					
					//导流按日趋势
					var dailyList = resp.dailyList;
					$("#dj_num_day_chart").show();
					$("#dj_person_day_chart").show();
					$("#bg_num_day_chart").show();
					$("#bg_person_day_chart").show();
					$("#daySelect").val("dj_num_day_chart");
					var dayMap = mktChart.common.diversionDayCharts(dailyList);
					chartDiversionDayDJPV = dayMap["djNumDayChart"];
					chartDiversionDayDJUV = dayMap["djPersonDayChart"];
					chartDiversionDayBGPV = dayMap["bgNumDayChart"];
					chartDiversionDayBGUV = dayMap["bgPersonDayChart"];
					
					//导流当日小时
					var hourList = resp.hourList;
					$("#dj_num_hour_chart").show();
					$("#dj_person_hour_chart").show();
					$("#bg_num_hour_chart").show();
					$("#bg_person_hour_chart").show();
					$("#hourSelect").val("dj_num_hour_chart");
					hourMap = mktChart.common.diversionHourCharts(hourList);
					chartDiversionHourDJPV = hourMap["djNumHourChart"];
					chartDiversionHourDJUV = hourMap["djPersonHourChart"];
					chartDiversionHourBGPV = hourMap["bgNumHourChart"];
					chartDiversionHourBGUV = hourMap["bgPersonHourChart"];
					$("#dj_person_day_chart").hide();
					$("#bg_num_day_chart").hide();
					$("#bg_person_day_chart").hide();
					
					$("#dj_person_hour_chart").hide();
					$("#bg_num_hour_chart").hide();
					$("#bg_person_hour_chart").hide();
				}
			});
		};
		
		transPicReprot = function () {
			$("#conversionDiv").hide();
			$("#conversionLoading").show();
			var apiUrl = "/api/generalSituat/TransPicReprot";
			var params = {
					"__userKey" : __userKey,
					"pActivtiy_id" : mktActivityNameList,
					"pAd_type":adTypeList,
					"pMaterial_type":mediaNameList,
					"pMedia_type":portList,
					"pLandingPlat":landPlatformList,
					"pBegin_time":cycleStart,
					"pEnd_time":cycleEnd
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
					
					$("#conversionDiv").show();
					$("#conversionLoading").hide();
					
					//漏斗图
					var funnel = resp.transTotal;
					chartConversionfunnel = mktChart.common.funnelCharts('funnel_chart',funnel);
					
					//整体转化
					var totalTran = resp.totalTran;
					chartConversionOverall = mktChart.common.overallConversionCharts('overall_conversion_chart',totalTran);
					
					//分类转化
					var classification = resp.dailyresult;
					var map  = mktChart.common.classificationCharts(classification);
					chartConversionbgDj = map["bgDjChart"];
					chartConversiondjDd = map["djDdChart"];
					chartConversionddXd = map["ddXdChart"];
					chartConversionxdGm = map["xdGmChart"];
				}
			});
		};
		
		pageUrl = function () {
			$("#heatDiv").hide();
			$("#heatLoading").show();
			var apiUrl = "/api/generalSituat/GetHotData";
			var params = {
					"__userKey" : __userKey,
					"pActivtiy_id" : mktActivityNameList,
					"pAd_type":adTypeList,
					"pMaterial_type":mediaNameList,
					"pMedia_type":portList,
					"pLandingPlat":landPlatformList,
					"pBegin_time":cycleStart,
					"pEnd_time":cycleEnd
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
					
					$("#heatDiv").show();
					$("#heatLoading").hide();
					
					//页面路径
					var urlResult = resp.url;
					$("#urlTable tr:first").siblings("tr").remove();
					for(var i in urlResult){
						var bnous_rate = parseFloat(urlResult[i].bnous_rate)  * 100;
						if(bnous_rate>100){
							bnous_rate = 100;
						}
						$("#urlTable tr:last-child").after('<tr><td width="60%">'+urlResult[i].url+'</td><td width="10%">'+urlResult[i].landing_pv+'</td><td width="10%">'+urlResult[i].landing_uv+'</td><td width="10%">'+urlResult[i].avg_visit_time+'</td><td width="10%">'+ bnous_rate.toFixed(2) + "%" +'</td></tr>');
					}
					
					//产品页面
					var productResult = resp.product;
					$("#productTable tr:first").siblings("tr").remove();
					for(var i in productResult){
						var bnous_rate = parseFloat(productResult[i].bnous_rate)  * 100;
						if(bnous_rate>100){
							bnous_rate = 100;
						}
						$("#productTable tr:last-child").after('<tr><td width="60%">'+productResult[i].url+'</td><td width="10%">'+productResult[i].landing_pv+'</td><td width="10%">'+productResult[i].landing_uv+'</td><td width="10%">'+productResult[i].avg_visit_time+'</td><td width="10%">'+ bnous_rate.toFixed(2) + "%" +'</td></tr>');
					}
					
				}
			});
		};
		
		crowd = function () {
			$("#crowdDiv").hide();
			$("#crowdLoading").show();
			var apiUrl = "/api/generalSituat/GetPersonType";
			var params = {
					"__userKey" : __userKey,
					"pActivtiy_id" : mktActivityNameList,
					"pAd_type":adTypeList,
					"pMaterial_type":mediaNameList,
					"pMedia_type":portList,
					"pLandingPlat":landPlatformList,
					"pBegin_time":cycleStart,
					"pEnd_time":cycleEnd
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
					
					$("#crowdDiv").show();
					$("#crowdLoading").hide();
					
					//性别比例
					var genderData = resp.gender;
					chartCrowdGender = mktChart.common.genderCharts('gender_chart', genderData);
					
					//年龄分布
					var ageData = resp.age;
					chartCrowdAge = mktChart.common.ageCharts('age_chart', ageData);
					
					//媒体轨迹
					var mediaTrajectoryData = resp.media;
					chartCrowdMedia = mktChart.common.mediaTrajectoryCharts('media_trajectory_chart' , mediaTrajectoryData);
					
					//行业倾向
					var industryOrientationData = resp.industry;
					chartCrowdIndustry = mktChart.common.industryOrientationCharts('industry_orientation_chart' , industryOrientationData);
				}
			});
		};
		
		changeSelect = function (object) {
			var chartRadioValue = $("input[name='media_chart_radio']:checked").val();
		
			if (chartRadioValue == "bg") {
				$('#media_chart_bg').show();
				$('#media_chart_dj').hide();
				echartsGeneralSituatBG.resize();
			} else {
				$('#media_chart_bg').hide();
				$('#media_chart_dj').show();
				echartsGeneralSituatDJ.resize();
			}
		}
		
		var echartsGeneralSituatBG = null;
		var echartsGeneralSituatDj = null;
		var echartsGeneralSituatHour = null;
		var echartsGeneralSituatMap = null;
		var echartsGeneralSituatDJTrend = null;
		var echartsGeneralSituatDJTrend = null;
		var chartDiversionBasePV = null;
		var chartDiversionBaseUV = null;
		var chartDiversionDayDJPV = null;
		var chartDiversionDayDJUV = null;
		var chartDiversionDayBGPV = null;
		var chartDiversionDayBGUV = null;
		var chartDiversionHourDJPV = null;
		var chartDiversionHourDJUV = null;
		var chartDiversionHourBGPV = null;
		var chartDiversionHourBGUV = null;
		var chartDiversionFrequencyBG  = null;
		var chartDiversionFrequencyDJ  = null;
		var chartDiversionFrequencyLanding  = null;
		var chartDiversionFrequencyBGL  = null;
		var chartDiversionFrequencyDJl  = null;
		var chartDiversionFrequencyLandingl  = null;
		var chartConversionfunnel = null;
		var chartConversionOverall = null;
		var chartConversionbgDj = null;
		var chartConversiondjDd = null;
		var chartConversionddXd = null;
		var chartConversionxdGm = null;
		var chartCrowdGender = null;
		var chartCrowdAge = null;
		var chartCrowdMedia = null;
		var chartCrowdIndustry = null;
		//当浏览器大小变化时，eharts图表resize
		$(window).resize(function () { //当浏览器大小变化时
			var tabPage = $("#tabPage").children();
			var id = "";
			for(var i =0; i < tabPage.length; i++){
				var a = tabPage[i];
				if(tabPage[i].className == "elt-tab eltt-on"){
					id = tabPage[i].id;
				}
			}
			resizeTable(id);
		});
		
		resizeTable = function (id) {
			if(id == "generalSituation"){
				if(echartsGeneralSituatBG != null && echartsGeneralSituatDj != null && echartsGeneralSituatHour !=null 
						&& echartsGeneralSituatMap != null && echartsGeneralSituatDJTrend != null){
					echartsGeneralSituatBG.resize();
					echartsGeneralSituatDj.resize();
					echartsGeneralSituatHour.resize();
					echartsGeneralSituatDJTrend.resize();
					echartsGeneralSituatMap.resize();
				}
			}else if(id == "diversion"){
				if(chartDiversionBasePV!=null && chartDiversionBaseUV!=null && chartDiversionDayDJPV!=null && chartDiversionDayDJUV !=null
						&& chartDiversionDayBGPV!=null && chartDiversionDayBGUV!=null && chartDiversionHourDJPV!=null && chartDiversionHourDJUV!=null
						&& chartDiversionHourBGPV!=null && chartDiversionHourBGUV!=null && chartDiversionFrequencyBG!=null
						&& chartDiversionFrequencyDJ !=null && chartDiversionFrequencyLanding !=null 
						&& chartDiversionFrequencyBGL !=null && chartDiversionFrequencyDJl !=null && chartDiversionFrequencyLandingl != null){
					chartDiversionBasePV.resize();
					chartDiversionBaseUV.resize();
					chartDiversionDayDJPV.resize();
					chartDiversionDayDJUV.resize();
					chartDiversionDayBGPV.resize();
					chartDiversionDayBGUV.resize();
					chartDiversionHourDJPV.resize();
					chartDiversionHourDJUV.resize();
					chartDiversionHourBGPV.resize();
					chartDiversionHourBGUV.resize();
					chartDiversionFrequencyBG.resize();
					chartDiversionFrequencyDJ.resize();
					chartDiversionFrequencyLanding.resize();
					chartDiversionFrequencyBGL.resize();
					chartDiversionFrequencyDJl.resize();
					chartDiversionFrequencyLandingl.resize();
				}
			}else if(id == "conversion"){
				if(chartConversionfunnel!=null && chartConversionOverall!=null && chartConversionbgDj !=null
						&& chartConversiondjDd!=null && chartConversionddXd!=null && chartConversionxdGm!=null){
					chartConversionfunnel.resize();
					chartConversionOverall.resize();
					chartConversionbgDj.resize();
					chartConversiondjDd.resize();
					chartConversionddXd.resize();
					chartConversionxdGm.resize();
				}
			}else if(id == "crowd"){
				if(chartCrowdGender!=null && chartCrowdAge!=null && chartCrowdMedia!=null && chartCrowdIndustry!=null){
					chartCrowdGender.resize();
					chartCrowdAge.resize();
					chartCrowdMedia.resize();
					chartCrowdIndustry.resize();
				}
			}
		};
	}
});