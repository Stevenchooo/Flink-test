var upmobui = {};

upmobui.common = {
	
	d : {
		p_mt : -1200,	// pop margin top
		p_t : 500,		// pop timer
		isIe678 : null	// check if the browser is ie678
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
//			if($(this).parent().hasClass("uni-cur")){
//				$(this).parent().removeClass("uni-cur");
//			}
//			else{
//				$(this).parent().addClass("uni-cur");
//			}
			
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
		
		// 应用总控， 展开后点击其他地方可收取
		var selOpt = $(".mks-options");
		$(".mk-select").on("click", ".mks-box", function(){
			if(selOpt.is(":hidden")){
				selOpt.slideDown("fast");
			}
			else{
				selOpt.slideUp("fast");
			}
			
			//$(this).siblings(".mks-options").slideDown("fast");
		});
		
		$(document).on("click", function(evt){
			if(!$(evt.target).parents(".mk-select").length){
				if(!selOpt.is(":hidden"))
				{
					selOpt.slideUp("fast");
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
	
	// 图表内的TAB切换， true 如果点击是有效的
	figTab : function(el){
		"use strict";
		
		if(el.hasClass("fba-on")){
			return false;	// 点本身已经激活的TAB， 不干事
		}
		
		el.addClass("fba-on").siblings().removeClass("fba-on");
		return true;
	},
	
	// 切换时间的共用方法
	// 传入的 boxFunc, linkFunc 是在本页面的方法里进行实现的
	// 公共方法会将 tab号 和 时间区间 传给 boxFunc, linkFunc
	expPeriods : function(boxFunc, linkFunc){
		"use strict";
		
		$(".ps-box .date-txt").datepicker({ dateFormat : "yy-mm-dd" });
		$(".ps-box").on("change", ".date-txt", function(){
			var t1, t2, tts = $(".ps-box .date-txt");
			if(!!tts.eq(0).val().length && !!tts.eq(1).val().length){
				var reg = new RegExp("-","g");
				t1 = new Date(tts.eq(0).val().replace(reg,"/")).getTime();
				t2 = new Date(tts.eq(1).val().replace(reg,"/")).getTime();
				
				if(t2 - t1 <= 0){
					return;
				}
			}
			else{
				return;
			}
			
			// 上面是两种不成立的情况，就return掉了
			$(this).parent().addClass("ps-on").siblings().removeClass("ps-on");
			
			boxFunc(tts.eq(0).val(), tts.eq(1).val());
		});
		
		$(".period-select").on("click", ".ps-link", function(){
			
			if($(this).hasClass("ps-on")){
				return;
			}
			
			$(this).addClass("ps-on").siblings().removeClass("ps-on");
			
			$(".ps-box .date-txt").val("");
			
			linkFunc($(this).data("tab"));
		});	
	},
	
	// 体验页面添加和删除对比
	expAddCond : function(){
		"use strict";
		
		$(".cond-add").on("click", function(){
			var hideConds = $(".exp-addcondition").children(".dpf-line:hidden");
			
			if(hideConds.length > 0){
				hideConds.eq(0).show();
				$(this).children("em").html(hideConds.length - 1);
			}
		});
		
		$(".exp-addcondition").on("click", ".cond-del", function(){
			$(this).parent().hide();
			$(this).siblings(".dpf-item").find(".mks-value").val("请选择...");
			$(this).siblings(".dpf-item").find(".option-val").val(0).siblings(".orisel-txt").val("请选择...").siblings(".mo-multi").find(".mom-ckbox").prop("checked", false);
			$(this).siblings(".dpf-item").children("select").prop("selectedIndex",0);
			$(this).parents(".dp-filter").eq(0).find(".cond-add em").html($(".exp-addcondition").children(".dpf-line:hidden").length);
		});
	},
	
	stop : function(e){
		if(e.keyCode == 8) 
		    e.keyCode = 0;
	},

};

upmobui.appman_mine = function(){
	"use strict";
	
	//upmobui.common.pageFunc();
	$("#app_add").on("click", function(){
		var pwin = $("#app_add_win");
		pwin.find(".f2-txt").val("");
		pwin.data("editing", false).data("row", "");
		upmobui.common.showPop(pwin);
	});
	
	$(".up-detable").on("click", ".edit-app", function(){
		
		var pwin = $("#app_add_win");
		pwin.data("editing", true); // pwin 上做个记号，代表是编辑
		pwin.data("row", $(this).data("rowid"));
		var rowData = $(this).parent().siblings("td");
		var winTxt = pwin.find(".f2-txt");
		for(var i = 0; i < 3; i++){
			winTxt.eq(i).val(rowData.eq(i).html());
		}
		upmobui.common.showPop(pwin);
		
	});
	
	$("#app_ok").on("click", function(){
		var pwin = $("#app_add_win");
		var winTxt = pwin.find(".f2-txt");
		
		
		if(pwin.data("editing")){
			
			var rowData = $(".up-detable").find("[data-rowid='" + pwin.data("row") + "']").parent().siblings("td");
			for(var i = 0; i < 3; i++){
				rowData.eq(i).html(winTxt.eq(i).val());
			}
			
			// ajax修改数据， 略
			// 修改完传回2个数据， 时间， 上传者ID, 时间取前台时间，用户ID放在隐藏域之中也不是不行，安全性略差
			rowData.eq(3).html("服务器时间");
			rowData.eq(4).html("用户ID");
			
		}
		else{
			var rowItem = "<tr>";
			for(var j = 0; j < 3; j++){
				rowItem += "<td>" + winTxt.eq(j).val() + "</td>";
			}
			
			// ajax添加数据， 略
			// 添加完，后台需要传回3个数据， 时间， 上传者ID， 以及记录生成后的ID
			rowItem += "<td>" + "TIME" + "</td>";
			rowItem += "<td>" + "USERID" + "</td>";
			rowItem += '<td><a href="javascript:void(0)" class="edit-app" data-rowid="记录生成后的ID">修改</a></td></tr>';
			$(".up-detable table").append($(rowItem));
		}
		
		upmobui.common.hidePop(pwin);
	
	});
};

upmobui.appman_distribution = function(){
	"use strict";
	
	//upmobui.common.pageFunc();
	
	$("#dist_add").on("click", function(){
		var pwin = $("#dist_add_win");
		pwin.find(".f2-txt").val("");
		pwin.data("editing", false).data("row", "");
		upmobui.common.showPop(pwin);
	});
	
	$(".up-detable").on("click", ".edit-dist", function(){
		
		var pwin = $("#dist_add_win");
		pwin.data("editing", true); // pwin 上做个记号，代表是编辑
		pwin.data("row", $(this).data("rowid"));
		var rowData = $(this).parent().siblings("td");
		var winTxt = pwin.find(".f2-txt");
		winTxt.eq(0).val(rowData.eq(1).html());
		upmobui.common.showPop(pwin);
		
	});
	
	$("#dist_ok").on("click", function(){
		var pwin = $("#dist_add_win");
		var winTxt = pwin.find(".f2-txt");
		
		
		if(pwin.data("editing")){
			
			var rowData = $(".up-detable").find("[data-rowid='" + pwin.data("row") + "']").parent().siblings("td");
			winTxt.eq(0).val(rowData.eq(1).html());
			
			// ajax修改数据， 略
			// 修改完传回3个数据， 渠道ID, 时间， 上传者ID, 时间取前台时间，用户ID放在隐藏域之中也不是不行，安全性略差
			rowData.eq(0).html("渠道ID");
			rowData.eq(2).html("服务器时间");
			rowData.eq(3).html("用户ID");
			
		}
		else{
			
			// ajax添加数据， 略
			// 添加完，后台需要传回3个数据， 渠道ID, 时间， 上传者ID， 以及记录生成后的ID
			
			var rowItem = "<tr>";
			rowItem += "<td>" + "渠道ID" + "</td>";
			rowItem += "<td>" + winTxt.eq(0).val() + "</td>";
			rowItem += "<td>" + "TIME" + "</td>";
			rowItem += "<td>" + "USERID" + "</td>";
			rowItem += '<td><a href="javascript:void(0)" class="edit-dist" data-rowid="记录生成后的ID">修改</a></td></tr>';
			$(".up-detable table").append($(rowItem));
		}
		
		upmobui.common.hidePop(pwin);
	
	});
	
	
	$(".up-detable").on("click", ".del-dist", function(){
		
		// ajax 将  data-rowid, 传回后台进行删除操作， 略
		$(this).parents("tr").remove();
	});	
};

upmobui.ua_involve = function(){
	
	"use strict";
	$(".fh-tip").tooltip({
		// 略， 同 ua_overall
		items: "[data-tip]",
		tooltipClass: "mktip",
		position:{my:"left+12 top", at:"right top-8"},
	    content: function(){
			return "<p><em>欲解释的词语：</em>解释解释解释解释解释解释解释解释解释解释解释解释解释</p>" +
				"<p><em>欲解释的词语：</em>解释解释解释解释解释解释解释解释解释解释解释解释解释</p>";
		} 
	});
		
};

upmobui.func_pathvisit = function(){
	"use strict";
	// 页面访问路径， 展开TABLE
	$(".expandable-table").on("click", ".mainline", function(){
		
		if($(this).data("exp") === false){

			$(this).siblings(".subline").show();
			$(this).children(".et-exp").html("-");
			$(this).data("exp", true);
		}
		else{
			$(this).siblings(".subline").hide();
			$(this).children(".et-exp").html("+");
			$(this).data("exp", false);
		}
	});
	
	$(".fh-tip").tooltip({
		// 略， 同 ua_overall
		items: "[data-tip]",
		tooltipClass: "mktip",
		position:{my:"left+12 top", at:"right top-8"},
	    content: function(){
			return "<p><em>欲解释的词语：</em>解释解释解释解释解释解释解释解释解释解释解释解释解释</p>" +
				"<p><em>欲解释的词语：</em>解释解释解释解释解释解释解释解释解释解释解释解释解释</p>";
		} 
	});
		
};

upmobui.func_cust_path = function(){
	
	"use strict";
	
	// 本页面的弹窗使用大幅度跳动的弹窗
	$("body").off("click", ".up-mask .upp-close, .up-mask .upp-cancel");
	
	var nodeData = [];
	var curStrong = null;
	$("#pathTree").on("click", "span", function(){
		upmobui.common.showPopNoAnimate($("#treeNodes"));
		curStrong = $(this);
		$(this).addClass("node-picking");
	});
	
	$("#treeNodes").on("click", ".upp-close", function(){
		upmobui.common.hidePopNoAnimate($("#treeNodes"));
	});
	
	$("#createPath").click(function(){
		upmobui.common.showPopNoAnimate($("#treeNodes"));
		curStrong = "root";
	});
	
	$("#clearPath").click(function(){
		$(".upmc-topnote").show();
		$(".tree-actions").hide();
		nodeData = [];
		$("#pathTree").empty();
	});
	
	$("#submitPath").click(function(){
		$(".upmc-topnote").show();
		$(".tree-actions").hide();
		
		alert("提交nodeData 到后台");
		/* 格式大致如下：
		var nodeData = [
			{"pid":"mkRoot", "id":"a001", "name" : "启动"}, 
			{"pid":"a001", "id":"b001", "name" : "分享"}, 
			{"pid":"b001", "id":"c001", "name" : "分享至QQ"}, 
			{"pid":"b001", "id":"c002", "name" : "分享至WX"}
			
		];
		
		*/
		
		
		// 另外清除掉当前路径
		nodeData = [];
		$("#pathTree").empty();
	});
	

	$("#nodePick").click(function(){
		
		upmobui.common.hidePopNoAnimate($("#treeNodes"));
		
		var addedNode = $(this).parent().siblings(".nl-box").find("input[type='radio']:checked");
		if(addedNode.length > 0){
			
			var nodeDom = null,
				nodeJson = null;
			if(curStrong === "root"){
				
				nodeDom = "<span><strong data-id=" + addedNode.data("id") + ">+</strong> " + addedNode.data("rn") + "</span>";
				$("#pathTree").append(nodeDom);
				nodeJson = {"pid" : "mkRoot", "id" : addedNode.data("id"), "name" : addedNode.data("rn")};
				nodeData.push(nodeJson);
				
				$(".upmc-topnote").hide();
				$(".tree-actions").show();
			}
			else{
				
				curStrong.removeClass("node-picking");
				var ulnode = curStrong.siblings("ul");
				if(ulnode.length > 0){
					nodeDom = "<li><span><strong data-id=" + addedNode.data("id") + ">+</strong> " + addedNode.data("rn") + "</span></li>";
					ulnode.append(nodeDom);
				}
				else{
					nodeDom = "<ul><li><span><strong data-id=" + addedNode.data("id") + ">+</strong> " + addedNode.data("rn") + "</span></li></ul>";
					curStrong.parent().append(nodeDom);
				}
				
				nodeJson = {"pid" : curStrong.data("id"), "id" : addedNode.data("id"), "name" : addedNode.data("rn")};
				nodeData.push(nodeJson);

			}
			
		}
	});
	
	$(".nl-box").on("click", "tbody tr", function(){
		$(this).find("input[type='radio']").prop("checked", true);
	});
		
};

upmobui.func_pathconversion = function(){
	
	"use strict";
	$("#d_start").datepicker({ dateFormat : "yy-mm-dd" }).datepicker("setDate", "today");
	
	// cond 是节点之间的统计信息
	var nodeData = [
		{"pid":"mkRoot", "id":"a001", "name" : "启动"}, 
		{"pid":"a001", "id":"b001", "name" : "分享", "cond" : "60次， 30%"}, 
		{"pid":"b001", "id":"c001", "name" : "分享至QQ", "cond" : "100次， 50%"}, 
		{"pid":"b001", "id":"c002", "name" : "分享至WX", "cond" : "100次， 50%"}
		
	];
	
	// 查找 dom 中， data-id 为id 的标签，若找不到报错
	function getMatchedNode(dom, id){

		var domChildren = dom.getElementsByTagName("*");
		for (var i = 0; i < domChildren.length; i++) {
        	if(domChildren[i].getAttribute("data-id") === id){
				return domChildren[i];
			}
    	}
		
		alert("现有DOM中不存在 data-id=" + id + "元素");	// 后期去除掉
		return null;
		
	}
	
	// 查找 dom的直接子元素，是否包含UL标签
	function getULTag(dom){
		
		var domChildren = dom.childNodes;
		for (var i = 0; i < domChildren.length; i++) {
			if(domChildren[i].nodeName === "UL"){
				return domChildren[i];
			}
    	}
		
		return null;
		
	}
	
	// treebox 为画树的容器
	function createGraph(json, treebox){
		
		var root = null;
			
		if(json[0].pid === "mkRoot"){
			root = document.createElement("div");
			root.setAttribute("data-id", json[0].id);
			var rootname = document.createElement("span");
			rootname.innerHTML = json[0].name;
			root.appendChild(rootname);
			treebox.appendChild(root);
			
			for(var i = 1; i < json.length; i++){	// skip 0

				var elMatch = getMatchedNode(treebox, json[i].pid),
					childUL = getULTag(elMatch),
					subnodeP = null,
					subnode = document.createElement("li");
					
				subnode = document.createElement("li");
				subnode.setAttribute("data-id", json[i].id);
				subnode.innerHTML = "|-- <span>" + json[i].name + "</span> (" + json[i].cond + ")";
					
				if(!!childUL){
					childUL.appendChild(subnode);
				}
				else{
					subnodeP = document.createElement("ul");
					subnodeP.appendChild(subnode);
					elMatch.appendChild(subnodeP);
				}

			}
			
		}
		else{
			alert("json首元素非root元素");
		}
	}
	
	//createGraph(upData, document.getElementById("treeGenZone"));
	$("#pathSearch").click(function(){
		createGraph(nodeData, document.getElementById("treeGenZone"));
	});

};

upmobui.exp_app = function(){
	
	"use strict";
	
	$(".exp-res").on("click", ".exp-detailfig", function(){
		
		if($("#detailFig").data("id") === $(this).data("id")){
			upmobui.common.showPop($("#detailFig"));
			return;	// 如果这个报表已经加载了， 那么就直接显示出来
		}
		
		$("#detailFig").data("id", $(this).data("id"));
		//alert($(this).data("id"));	// 通过ID， AJAX 加载详情报表的数据， 在回调函数中做以下事情：
		//$("#detailFig .upp-cont").empty();	// 清空目前已有的报表 或者 使用报表的销毁/清空方法
		// 将AJAX得到的数据重新生成报表， 并置入 $("#detailFig .upp-cont") 之中
		
		// 最后将弹出显示出来
		upmobui.common.showPop($("#detailFig"));
		
	});
};

upmobui.exp_func = function(){
	
	"use strict";
	//upmobui.common.expAddCond();
	
//	$(".fh-tip").tooltip({
//		// 略， 同 ua_overall
//		items: "[data-tip]",
//		tooltipClass: "mktip",
//		position:{my:"left+12 top", at:"right top-8"},
//	    content: function(){
//			return "<p><em>欲解释的词语：</em>解释解释解释解释解释解释解释解释解释解释解释解释解释</p>" +
//				"<p><em>欲解释的词语：</em>解释解释解释解释解释解释解释解释解释解释解释解释解释</p>";
//		} 
//	});
	
//	upmobui.common.expPeriods(
//		function(t1, t2){
//			alert(t1 + " " + t2);	// 前台传给后台时间戳，根据时间戳，变化报表
//		}, 
//		function(tabNo){
//			if(tabNo === 1){
//				// 点击 选项1 干的事情在这里, 即切换选项1的报表
//				alert(1);
//			}
//			else if(tabNo === 2){
//				// 2的事情
//				alert(tabNo);
//			}
//			else if(tabNo === 3){
//				// 3 的事情
//				alert(tabNo);
//			}
//			//...
//		}
//	);
	
	$(".exp-res").on("click", ".exp-export", function(){
		//alert($(this).data("id"));
	});	
	
	
//	$("#exp_quote").on("click", function(){
//		
//		// 做加载的事情
//		
//		$("#exp_res").show();
//	});
		
};

upmobui.exp_usability = function(){
	
	"use strict";
//	$(".fh-tip").tooltip({
//		// 略， 同 ua_overall
//		items: "[data-tip]",
//		tooltipClass: "mktip",
//		position:{my:"left+12 top", at:"right top-8"},
//	    content: function(){
//			return "<p><em>欲解释的词语：</em>解释解释解释解释解释解释解释解释解释解释解释解释解释</p>" +
//				"<p><em>欲解释的词语：</em>解释解释解释解释解释解释解释解释解释解释解释解释解释</p>";
//		} 
//	});
	
	/*upmobui.common.expPeriods(
		function(t1, t2){
			alert(t1 + " " + t2);	// 前台传给后台时间戳，根据时间戳，变化报表
		}, 
		function(tabNo){
			if(tabNo === 1){
				// 点击 选项1 干的事情在这里, 即切换选项1的报表
				alert(1);
			}
			else if(tabNo === 2){
				// 2的事情
				alert(tabNo);
			}
			else if(tabNo === 3){
				// 3 的事情
				alert(tabNo);
			}
			//...
		}
	);*/
	
	/*$(".exp-res").on("click", ".exp-export", function(){
		alert($(this).data("id"));
	});	*/
	
	
//	$("#exp_quote").on("click", function(){
//		
//		// 做加载的事情
//		
//		$("#exp_res").show();
//	});
		
};

upmobui.exp_sellpoints = function(){
	
	"use strict";
	upmobui.common.expAddCond();
	
	$(".fh-tip").tooltip({
		// 略， 同 ua_overall
		items: "[data-tip]",
		tooltipClass: "mktip",
		position:{my:"left+12 top", at:"right top-8"},
	    content: function(){
			return "<p><em>欲解释的词语：</em>解释解释解释解释解释解释解释解释解释解释解释解释解释</p>" +
				"<p><em>欲解释的词语：</em>解释解释解释解释解释解释解释解释解释解释解释解释解释</p>";
		} 
	});
	
	upmobui.common.expPeriods(
		function(t1, t2){
			alert(t1 + " " + t2);	// 前台传给后台时间戳，根据时间戳，变化报表
		}, 
		function(tabNo){
			if(tabNo === 1){
				// 点击 选项1 干的事情在这里, 即切换选项1的报表
				alert(1);
			}
			else if(tabNo === 2){
				// 2的事情
				alert(tabNo);
			}
			else if(tabNo === 3){
				// 3 的事情
				alert(tabNo);
			}
			//...
		}
	);
	
	$(".exp-res").on("click", ".exp-export", function(){
		alert($(this).data("id"));
	});	
	
	
	$("#exp_quote").on("click", function(){
		
		// 做加载的事情
		
		$("#exp_res").show();
	});
		
};