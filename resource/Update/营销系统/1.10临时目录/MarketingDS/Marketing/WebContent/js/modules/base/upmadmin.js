var upmadmin = {};

upmadmin.common = {
	
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
	
	
//	pageFunc : function(){
//		"use strict";
//
//		var _this = this;
//
//		// 收缩菜单
//		$(".upm-nav").on("click", ".uni-expand", function(){
//			if($(this).parent().hasClass("uni-cur")){
//				$(this).parent().removeClass("uni-cur");
//			}
//			else{
//				$(this).parent().addClass("uni-cur");
//			}
//		});
//		
//		// 关闭窗口
//		$("body").on("click", ".up-mask .upp-close, .up-mask .upp-cancel", function(){
//			_this.hidePop($(this).parents(".up-mask"));
//		});
//		
//	
//	},
	
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
	
	// 多选
	multiSel : function(){
		"use strict";
		
		$(".mk-select").on("click", ".mks-box", function(){
			if ($(this).siblings(".mks-options").children("label").length > 0) {
				$(this).siblings(".mks-options").slideDown("fast");
			}
		});
		
		$(".multi-select").on("click", ".mks-box", function(){

			var tmo = $(this).siblings(".mks-options");
			if(tmo.children("label").length > 0){
				tmo.slideDown("fast");
			}
			$(".mks-options").not(tmo).slideUp("fast");
		});

		
//		$(".mo-single").on("click", "li", function(){
//			var mos = $(this).parent();
//			mos.siblings(".option-val").val($(this).val());
//			mos.siblings(".mks-box").find(".mks-value").html($(this).text()).attr("title", $(this).text());
//			mos.slideUp("fast");
//		});
		
		$(".mo-multi").on("click", ".mom-ckbox", function(){
			var optUL = $(this).parents(".mo-multi");
			var selOriTxt = optUL.siblings(".orisel-txt").val();
			var selTxt = "", // 显示
				selValue = ""; // 实际值
			optUL.find(".mom-ckbox").each(function() {
				if($(this).prop("checked")){
					if (selTxt == "") {
						selTxt += $(this).parent().attr("title");
					}
					else {
						selTxt += "," + $(this).parent().attr("title");
					}
					
					if (selValue == "") {
						selValue += $(this).val();
					}
					else {
						selValue += "," + $(this).val();
					}
				}
			});
			if(!selTxt.length){
				selTxt = selOriTxt;
			}
			optUL.siblings(".mks-box").find(".mks-value").val(selTxt).attr("title", selTxt);
			optUL.siblings(".option-val").val(selValue);
		});
		
		var bodyClick = function(evt){
			var selOpt = $(".mo-multi");
			if(!$(evt.target).parents(".mk-select").length){	
				selOpt.slideUp("fast");
			}
		};
		$(document).on("click", bodyClick);
	},
	
	// 搜索+多选
	selSearch : function(){
		"use strict";
		
		$(".mk-selsearch").on("click", ".mks-left, .mks-right", function(){
			$(this).parent().siblings(".mkss-exp").slideDown("fast");
		});
		
		$(".mk-selsearch").on("click", ".mkss-cancel", function(){
			$(this).parents(".mkss-exp").slideUp("fast");
		});
		
		$(".mk-selsearch").on("click", ".mkss-add", function(){
			var mkssExp = $(this).parents(".mkss-exp");
			var inpChecked = mkssExp.find("input:checked");
			var selTxt = "", // 显示
				selValue = ""; // 实际值
				
			// 目前已有选项为空
			if(mkssExp.siblings(".option-val").val() === "notselected"){
				
				inpChecked.each(function() {
					selTxt += $(this).parent().attr("title") + " ";
					selValue += $(this).val() + ",";
				});	
				
				mkssExp.siblings(".option-val").val(selValue);
				mkssExp.siblings(".orisel-txt").val(selTxt);
				mkssExp.siblings(".mks-box").find(".mks-value").val(selTxt);
			}
			else{
				
				// 若已有选项不为空， 需要排除重复选项, 只有value不存在的会被添加
				inpChecked.each(function(idx, el) {
					
					mkssExp.siblings(".option-val").val();
					if(mkssExp.siblings(".option-val").val().indexOf(el.value) < 0){
						selTxt += $(this).parent().attr("title") + " ";
						selValue += $(this).val() + ",";
					}
				});	
				
				var curSV = selValue + mkssExp.siblings(".option-val").val();
				var curST = selTxt + mkssExp.siblings(".orisel-txt").val();
				mkssExp.siblings(".option-val").val(curSV);
				mkssExp.siblings(".orisel-txt").val(curST);
				mkssExp.siblings(".mks-box").find(".mks-value").val(curST);
			}
			
		});
		
		$(".mk-selsearch").on("click", ".mks-del", function(){
			var mksBox = $(this).parents(".mks-box");
			mksBox.find(".mks-value").val("请选择...");
			mksBox.siblings(".option-val").val("notselected");
			mksBox.siblings(".orisel-txt").val("请选择...");
			mksBox.siblings(".mkss-exp").find("input[type='checkbox']").prop("checked", false);
			
		});
		
		
	}	
	

};

upmadmin.rights_superadmin = function(){
	"use strict";
	
	//upmadmin.common.pageFunc();
	
	
	

};



upmadmin.rights_sa_roleact = function(){
	"use strict";
	
	//upmadmin.common.pageFunc();
	upmadmin.common.multiSel();
	upmadmin.common.selSearch();
	
	$(".mkss-search").on("click", ".mkss-btn", function(){
		alert('ajax获取数据，然后清空ul mkss-items 下的所有li, 然后组装新的li');
		
		// ajax略
		// li 的格式： <li><label title="选项内容1"><input type="checkbox" class="mom-ckbox" value="0">选项内容1</label></li>, 如下：
		var json = [{"c":"11", "v":"56"}, {"c":"12", "v":"57"}];
		var html = "";
		for(var i = 0; i < json.length; i++){
			html += '<li><label title="' + json[i].c + '"><input type="checkbox" class="mom-ckbox" value="' + json[i].v +  '">' + json[i].c +  '</label></li>';
		}
		
		$(this).parent().siblings(".mkss-result").children(".mkss-items").empty().append(html);
		
	});
};

upmadmin.rights_ca_people = function(){
	"use strict";
	
	$(".row-add").on("click", function(){
		
		var newrow = '<tr><td><input type="text" class="rtxt" /></td><td><input type="text" class="rtxt" /></td><td><input type="text" class="rtxt" /></td></tr>';
		$(this).siblings(".fb-table").children("tbody").append(newrow);
		
	});
};

upmadmin.exp_man_emui = function(){
	"use strict";
	
	//upmadmin.common.pageFunc();
	upmadmin.common.multiSel();
	
	var qst = null;
	$(".up-detable").on("click", ".el-del", function(){
		qst = confirm("确定要删除本条记录吗？");
		if (qst === true){
			
			// 通过 data-rowid ajax删除后端的那条记录， 
			//alert($(this).data("rowid"));
			
			// 界面上删除记录
			$(this).parents("tr").eq(0).remove();
		}
	});
};



upmadmin.exp_man_app = function(){
	"use strict";
	
	//upmadmin.common.pageFunc();
	upmadmin.common.multiSel();
	
	var qst = null;
	$(".up-detable").on("click", ".el-del", function(){
		qst = confirm("确定要删除本条记录吗？");
		if (qst === true){
			
			// 通过 data-rowid ajax删除后端的那条记录， 
			//alert($(this).data("rowid"));
			
			// 界面上删除记录
			$(this).parents("tr").eq(0).remove();
		}
	});
	

};



upmadmin.exp_man_func = function(){
	"use strict";
	
	//upmadmin.common.pageFunc();
	upmadmin.common.multiSel();
	
	var qst = null;
	$(".up-detable").on("click", ".el-del", function(){
		qst = confirm("确定要删除本条记录吗？");
		if (qst === true){
			
			// 通过 data-rowid ajax删除后端的那条记录， 
			//alert($(this).data("rowid"));
			
			// 界面上删除记录
			$(this).parents("tr").eq(0).remove();
		}
	});
	

};



upmadmin.exp_man_sellpoints = function(){
	"use strict";
	
	//upmadmin.common.pageFunc();
	upmadmin.common.multiSel();
	
	var qst = null;
	$(".up-detable").on("click", ".el-del", function(){
		qst = confirm("确定要删除本条记录吗？");
		if (qst === true){
			
			// 通过 data-rowid ajax删除后端的那条记录， 
			//alert($(this).data("rowid"));
			
			// 界面上删除记录
			$(this).parents("tr").eq(0).remove();
		}
	});
	

};

upmadmin.exp_man_basedata = function(){
	"use strict";
	var qst = null;
	$(".up-detable").on("click", ".el-del", function(){
		qst = confirm("确定要删除本条记录吗？");
		if (qst === true){
			
			// 通过 data-rowid ajax删除后端的那条记录， 
			//alert($(this).data("rowid"));
			
			// 界面上删除记录
			$(this).parents("tr").eq(0).remove();
		}
	});
	
	if($(".date-txt").length){
		$(".date-txt").datepicker({ dateFormat : "yy-mm-dd" });
	}
};