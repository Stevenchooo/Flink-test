var upmobui = {};

upmobui.common = {
	
	d : {
		p_mt : -300,	// pop margin top
		p_t : 500,		// pop timer
		isIe678 : null,	// check if the browser is ie678
		del_msg : "是否删除本条记录？",
		date_range_err : "截止日期不可早于开始日期！"
	},
	
	ie678 : function(){
		"use strict";
		
		if(this.d.isIe678 === null){
			var re  = new RegExp("MSIE ([0-8]{1,})");
			if (re.exec(navigator.userAgent) !== null){
				this.d.isIe678 = true;
			}
			else{
				this.d.isIe678 = false;
			}
		}
		
		return this.d.isIe678;
	},
	
	
	pageFunc : function(){
		"use strict";

		var _this = this;

		// 收缩菜单
		$(".upm-nav").on("click", ".uni-expand", function(){
			if($(this).parent().hasClass("uni-cur")){
				$(this).parent().removeClass("uni-cur");
			}
			else{
				$(".un-item").removeClass("uni-cur");
				$(this).parent().addClass("uni-cur");
			}
		});
		
		
		// 关闭窗口
		$("body").on("click", ".up-mask .upp-close, .up-mask .upp-cancel", function(){
			_this.hidePop($(this).parents(".up-mask"));
		});
		
		
		
		// GO TOP 按钮
		var st, 
			ch = document.documentElement.clientHeight || document.body.clientHeight,
			gobtn = $('<a class="up-gotop" href="javascript:void(0);" onclick="window.scrollTo(0,0)" title="回到顶部" style="display:none;"></a>');
			
		$("body").append(gobtn);

		$(window).on("scroll", function(){
			st = document.documentElement.scrollTop || document.body.scrollTop;
			
			if(st > ch && gobtn.is(":hidden")){
				if(!_this.ie678()){
					gobtn.fadeIn(300);
				}
				else{
					gobtn.show();
				}
			}
			else if(st <= ch && gobtn.is(":visible")){
				if(!_this.ie678()){
					gobtn.fadeOut(300);
				}
				else{
					gobtn.hide();
				}
			}
			
		});
	
	},
	
	showPop : function(maskObj){
		"use strict";
		
		if(!this.ie678()){
			maskObj.fadeIn(this.d.p_t);
		}
		else{
			maskObj.show();
		}
		maskObj.children(".up-popup").animate({"margin-top" : -maskObj.children(".up-popup").height() / 2}, this.d.p_t);
	},
	
	
	hidePop : function(maskObj){
		"use strict";
		
		if(!this.ie678()){
			maskObj.fadeOut(this.d.p_t);
		}
		else{
			maskObj.hide();
		}
		maskObj.children(".up-popup").animate({"margin-top" : this.d.p_mt}, this.d.p_t);
	},
	
	showPopNoAnimate : function(maskObj){
		"use strict";
		
		if(!this.ie678()){
			maskObj.fadeIn(this.d.p_t);
		}
		else{
			maskObj.show();
		}
	},
	
	
	hidePopNoAnimate : function(maskObj){
		"use strict";
		
		if(!this.ie678()){
			maskObj.fadeOut(this.d.p_t);
		}
		else{
			maskObj.hide();
		}
	},
	
	tabSwitch : function(){
		"use strict";
		
		// 页面tab点击
		$(".page-tabar").on("click", ".pt-link a", function(){
			
			if($(this).parent().hasClass("ptl-selected")){
				return;
			}
			
			$(this).parent().addClass("ptl-selected").siblings(".pt-link").removeClass("ptl-selected");
			$(this).parents(".page-tabar").siblings(".pt-tabcont").hide().filter('[data-tabvalue="' + $(this).data("tabindex") + '"]').show();
			
		});
	},
	buttonSwitch : function(){
		"use strict";
		
		// 页面tab点击
		$(".uni-cur").on("click", function(){
			
			$(this).parent().children().removeClass("fba-on");
			$(this).addClass("fba-on");
			
		});
	},
	
	//图形报表下拉初始化
	dropdownMenuSwitch : function(){
		"use strict";
		
		$('.dropdown-menu a').on('click', function(){  
			var html = $(this).html() + '<span class="caret"></span>';
		    var obj = $(this).parent().parent().parent().children(".dropdown-toggle");
		    obj.attr("value",$(this).attr("value"));
		    obj.html(html);
		});
	},
	
	mediaChartRadioClick : function(){
		$('input:radio').on('click',function(){
			var val = $(this).attr("value"); 
			if(val == "bg")
			{
				$("#media_chart_bg").show();
				$("#media_chart_dj").hide();
			}
			else if(val == "dj")
			{
				$("#media_chart_bg").hide();
				$("#media_chart_dj").show();
			}
		});
	},
	
	groupTransRadioClick : function(){
		$('input:radio').on('click',function(){
			var val = $(this).attr("value"); 
			if(val == "Imp")
			{
				$("#allp_bg_pv").show();
				$("#allp_bg_uv").hide();
				$("#wdp_bg_pv").show();
				$("#wdp_bg_uv").hide();
				$("#uv_TranRate").hide();
				$("#pv_TranRate").show();
				
			}
			else if(val == "UV")
			{
				$("#allp_bg_pv").hide();
				$("#allp_bg_uv").show();
				$("#wdp_bg_pv").hide();
				$("#wdp_bg_uv").show();
				$("#uv_TranRate").show();
				$("#pv_TranRate").hide();
			}
		});
	},
	
	
	baseRadioClick : function(){
		$('input:radio').on('click',function(){
			var chartRadioValue = $(this).attr("value"); 
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
		});
	},
	
	hourRadioClick : function(){
		$('input:radio').on('click',function(){
			var chartRadioValue = $(this).attr("value"); 
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
		});
	},
	
	
	getChartOption : function(legendArray,categoryArray,valueArray,chartType)
	{
		 var option = {};
		 
		 option.tooltip = {trigger:'axis'};
		 option.legend = {data: legendArray};
		 option.toolbox = {show : true,
 	        feature : {
 	            saveAsImage : {show: true}
 	        }
 	     };
		 
		option.calculable = true;
		option.xAxis = [
 	        {
 	            type : 'category',
 	            data : categoryArray
 	        }
 	    ];
		option.yAxis = [
 	        {
 	            type : 'value'
 	        }
 	    ];
		
		var series = [];
		
		 $.each(legendArray, function(i, value){
			 series.push({
				    name:legendArray[i],
	 	            type:chartType,
	 	            data:valueArray[i],
			 });
		 });
		 
		 option.series = series;
		 
		 option.noDataLoadingOption = {text: '暂无数据',effect: 'whirling'};
		 return option;
	},
	getSelectList : function(apiUrl,params,jqueryId,keyid,keyValue,index,mktinfoName){
		"use strict";
		
		$.ajax({
			type : "POST",
			url : __webRoot + apiUrl,
			async : false,
			data:params,
			success : function (resp) {
				if (resp.resultCode != 0) {
					return;
				}
				
				$("#" + jqueryId).empty();
				var nameList = resp.result.slice(index);
				if(typeof(mktinfoName) != "undefined" || mktinfoName != "")
				{
					for(var i = 0; i < nameList.length; i++)
					{
						var obj = nameList[i];
						if(mktinfoName == obj[keyid])
						{
							$("#" + jqueryId).append("<option value='" + obj[keyid] +"'>" +obj[keyValue] + "</option>");
						}
					}
				}
				
				for(var i = 0; i < nameList.length; i++)
				{
					var obj = nameList[i];
					if(mktinfoName == obj[keyid] && (typeof(mktinfoName) != "undefined" || mktinfoName != ""))
					{
						continue;
					}
					$("#" + jqueryId).append("<option value='" + obj[keyid] +"'>" +obj[keyValue] + "</option>");
				}
				
			}
		});
	},
	
	
	getReportPlatfom : function(apiUrl,params,jqueryId,keyid,keyValue,index,reportPlatform){
		"use strict";
		//获取平台类型
		$.ajax({
			type : "POST",
			url : __webRoot + apiUrl,
			async : false,
			data:params,
			success : function (resp) {
				if (resp.resultCode != 0) {
					return;
				}
				
				$("#" + jqueryId).empty();
				var nameList = resp.results.slice(index);
				for(var i = 0; i < nameList.length; i++)
				{
					var obj = nameList[i];
					if(i == 0 && (typeof(reportPlatform) == "undefined" || reportPlatform == ""))
					{
						$("#" + jqueryId).append("<a class='uni-cur fba-on' value='" + obj[keyid] + "' href='javascript:void(0)'>" + obj[keyValue]+ "</a>");

					}
					else
					{
						$("#" + jqueryId).append("<a class='uni-cur' value='" + obj[keyid] + "' href='javascript:void(0)'>" + obj[keyValue]+ "</a>");
					}
				}
				
			}
		});
	},
	
	getDropdownMenu : function(apiUrl,params,jqueryId,keyid,keyValue,index){
		"use strict";
		$.ajax({
			type : "POST",
			url : __webRoot + apiUrl,
			async : false,
			data:params,
			success : function (resp) {
				if (resp.resultCode != 0) {
					return;
				}
				
				$("#" + jqueryId).empty();
				var nameList = resp.results.slice(index);
				for(var i = 0; i < nameList.length; i++)
				{
					
					var obj = nameList[i];
					$("#" + jqueryId).append("<li><a href='#' value='" + obj[keyid] + "'>" + obj[keyValue] + "</a></li>");
					if(i == 0)
					{
						var html = obj[keyValue] + '<span class="caret"></span>';
						var obj = $("#" + jqueryId).parent().children(".dropdown-toggle");
						obj.attr("value",obj[keyid]);
						obj.html(html);
					}
				}
				
			}
		});
	},
	

	popConfirm : function(msg){
		"use strict";
		
		return window.confirm(msg);
	},
	
	// 多选
	multiSel : function(){
		"use strict";
		
		$(".multi-select").on("click", ".mks-box", function(){
			$(this).siblings(".mks-options").slideDown("fast");
		});
		
		$(".mo-single").on("click", "li", function(){
			var mos = $(this).parent();
			mos.siblings(".option-val").val($(this).val());
			mos.siblings(".mks-box").find(".mks-value").html($(this).text()).attr("title", $(this).text());
			mos.slideUp("fast");
		});
		
		$(".mo-multi").on("click", ".mom-ckbox", function(){
			var optUL = $(this).parents(".mo-multi");
			var selOriTxt = optUL.siblings(".orisel-txt").val();
			var selTxt = "", // 显示
				selValue = ""; // 实际值
			optUL.find(".mom-ckbox").each(function() {
				if($(this).prop("checked")){
					selTxt += $(this).parent().attr("title") + " ";
					selValue += $(this).val() + ",";
				}
			});
			if(!selTxt.length){
				selTxt = selOriTxt;
			}
			optUL.siblings(".mks-box").find(".mks-value").val(selTxt).attr("title", selTxt);
			optUL.siblings(".option-val").val(selValue);
		});
		
		var bodyClick = function(evt){
			var selOpt = $(".mks-options");
			if(!$(evt.target).parents(".mk-select").length){	
				selOpt.slideUp("fast");
			}
		};
		$(document).on("click", bodyClick);
	}
	


};

upmobui.table_s1 = function(){
	"use strict";
	
	upmobui.common.pageFunc();	// 页面共用方法
	
	var edit_win = $("#useredit_win");
	
	// 点击添加
	$("#useredit_add").on("click", function(){
		
		edit_win.find(".f2-txt").val("");
		upmobui.common.showPop(edit_win);
	});
	
	// 点击修改按钮
	$(".up-detable").on("click", ".tb-edit", function(){
		
		edit_win.find(".f2-txt").val("");
		// 1. 通过 $(this).data("rowid")， 进行ajax数据加载, 然后将数据填入弹窗之中， 看是否可向现有系统靠拢
		
		// 2. 显示窗口出来
		upmobui.common.showPop(edit_win);
		
	});
	
	// 点击弹窗中的确定按钮，进行添加记录
	$(".up-popup").on("click", ".add-ok", function(){

		// ajax 将数据传往后台，看是否可向现有系统靠拢
		
		
		upmobui.common.hidePop(edit_win);
	
	});
	
	
	// 点击记录上的删除按钮
	$(".up-detable").on("click", ".tb-del", function(){
		
		if(upmobui.common.popConfirm(upmobui.common.d.del_msg) === true){
			// ajax 将  data-rowid, 传回后台进行删除操作，看是否可向现有系统靠拢
			
			$(this).parents("tr").remove();
		}
	});	
};

upmobui.table_s2 = function(){
	"use strict";
	
	upmobui.common.pageFunc();	// 页面共用方法
	
	// 时间区间设置和验证
	$(".up-popup .pair-date").datepicker({ dateFormat : "yy-mm-dd" });
	$(".up-popup").on("change", ".pair-date", function(){
		var t1, t2, tts = $(this).parent().children(".pair-date");
		$(this).parent().children(".f2-linerr").remove();
		if(!!tts.eq(0).val().length && !!tts.eq(1).val().length){
			var reg = new RegExp("-","g");
			t1 = new Date(tts.eq(0).val().replace(reg,"/")).getTime();
			t2 = new Date(tts.eq(1).val().replace(reg,"/")).getTime();
			
			if(t2 - t1 <= 0){
				$(this).parent().append('<p class="f2-linerr">' + upmobui.common.d.date_range_err + '</p>');
			}
		}

	});
	
	// 单时间设置
	$(".up-popup .date-txt").datepicker({ dateFormat : "yy-mm-dd" });
	
	
	/*
		以下与 table_s1 一致
	*/
	var edit_win = $("#evtedit_win");
	
	// 点击添加
	$("#evtedit_add").on("click", function(){
		
		edit_win.find(".f2-txt").val("");
		
		upmobui.common.showPop(edit_win);
	});
	
	// 点击修改按钮
	$(".up-detable").on("click", ".tb-edit", function(){
		
		edit_win.find(".f2-txt").val("");
		// 1. 通过 $(this).data("rowid")， 进行ajax数据加载, 然后将数据填入弹窗之中， 看是否可向现有系统靠拢
		
		// 2. 显示窗口出来
		upmobui.common.showPop(edit_win);
		
	});
	
	// 点击弹窗中的确定按钮，进行添加记录
	$(".up-popup").on("click", ".add-ok", function(){

		// ajax 将数据传往后台，看是否可向现有系统靠拢
		
		
		upmobui.common.hidePop(edit_win);
	
	});
	
	
	// 点击记录上的删除按钮
	$(".up-detable").on("click", ".tb-del", function(){
		
		if(upmobui.common.popConfirm(upmobui.common.d.del_msg) === true){
			// ajax 将  data-rowid, 传回后台进行删除操作，看是否可向现有系统靠拢
			
			$(this).parents("tr").remove();
		}
	});	
};



upmobui.table_s3 = function(){
	"use strict";
	
	upmobui.common.pageFunc();	// 页面共用方法
	
	// 错误浮窗， 可用于不太方便塞错误信息的地方。
	$(".dpf-item .pair-date").tooltip({
		items: "[data-tip]",
		tooltipClass: "mktip-err",
		position:{my:"left+8 top", at:"right top-4"},
		disabled: true,
		close: function() { 
			$(this).tooltip("disable");
		},
	    content: function(){
			return "<p>" + upmobui.common.d.date_range_err + "</p>";
		}
		
	});
	
	// 时间区间设置和验证
	$(".dpf-item .pair-date").datepicker({ dateFormat : "yy-mm-dd" });
	$(".dpf-item").on("change", ".pair-date", function(){
		var t1, t2, tts = $(this).parent().children(".pair-date");
		tts.eq(1).tooltip("disable");
		if(!!tts.eq(0).val().length && !!tts.eq(1).val().length){
			var reg = new RegExp("-","g");
			t1 = new Date(tts.eq(0).val().replace(reg,"/")).getTime();
			t2 = new Date(tts.eq(1).val().replace(reg,"/")).getTime();
			
			if(t2 - t1 < 0){
				tts.eq(1).tooltip("enable").tooltip("open");
			}
		}

	});
	
	
	
	
	
	/*
		以下与 table_s1 一致
	*/
	var edit_win = $("#evtedit_win");
	
	// 点击添加
	$("#evtedit_add").on("click", function(){
		
		edit_win.find(".f2-txt").val("");
		
		upmobui.common.showPop(edit_win);
	});
	
	// 点击修改按钮
	$(".up-detable").on("click", ".tb-edit", function(){
		
		edit_win.find(".f2-txt").val("");
		// 1. 通过 $(this).data("rowid")， 进行ajax数据加载, 然后将数据填入弹窗之中， 看是否可向现有系统靠拢
		
		// 2. 显示窗口出来
		upmobui.common.showPop(edit_win);
		
	});
	
	// 点击弹窗中的确定按钮，进行添加记录
	$(".up-popup").on("click", ".add-ok", function(){

		// ajax 将数据传往后台，看是否可向现有系统靠拢
		
		
		upmobui.common.hidePop(edit_win);
	
	});
	
	
	// 点击记录上的删除按钮
	$(".up-detable").on("click", ".tb-del", function(){
		
		if(upmobui.common.popConfirm(upmobui.common.d.del_msg) === true){
			// ajax 将  data-rowid, 传回后台进行删除操作，看是否可向现有系统靠拢
			
			$(this).parents("tr").remove();
		}
	});	
};

