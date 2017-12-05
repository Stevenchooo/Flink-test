define(function (require) {
	var tool = require('tools');
	var Base = require('base');
	
	var fk = "";
	var NoticeTable = function (rows, chooseTabNum, selfInfo, tableId,backEmptp, column)
	{
		fk = tableId;
		Base.call(this, rows, chooseTabNum, selfInfo, tableId, backEmptp,column);
		this.QueryCondition = {
				Creater : "全选"
			};
	};

	tool.extend(NoticeTable, Base);
	NoticeTable.prototype.putSigleInfo = function (info, $dom,kk) { // 子类需实现该方法，一条信息放入一行空表
		
		//console.log("notice"+kk);
		if(kk=="base_list"){
			$dom.find("td").eq(0).html(info.all_media_name);

		    $dom.find("td").eq(1).html(info.all_bg_pv_sum);
		
		    $dom.find("td").eq(2).html(info.all_bg_uv_sum);

		    $dom.find("td").eq(3).html(info.all_dj_pv_sum);

			$dom.find("td").eq(4).html(info.all_dj_uv_sum);
		}else if(kk=="hour_tblList"){
			
			var ctr = 0;
			if(info.tbl_bg_pv != 0){
				ctr = (info.tbl_dj_pv/info.tbl_bg_pv).toFixed(2);
			}
			
			$dom.find("td").eq(0).html(info.tbl_sid);
			$dom.find("td").eq(1).html(info.tbl_media_name);
			$dom.find("td").eq(2).html(info.tbl_channel);
			$dom.find("td").eq(3).html(info.tbl_ad_position);
			$dom.find("td").eq(4).html(info.tbl_hour);
			$dom.find("td").eq(5).html(info.tbl_bg_pv);
			$dom.find("td").eq(6).html(info.tbl_dj_pv);
			$dom.find("td").eq(7).html(ctr);
			
			
		}else if(kk=="fr_list"){
			
			var thNum = parseInt($('#fr_select_max').val())+1;

			for (var i = 0; i < thNum; i++) 
			{
				if(i==0)
				{
					$dom.find("td").eq(i).html(info.mediaName);
				}else
				{
					$dom.find("td").eq(i).html(info.userList[i-1]);
				}
				
			}
		}
		
		

	};
	
	NoticeTable.prototype.showCreaterResult = function ()
	{ // 展示匹配的条数
		this.showInfo = [];
		for (var i in this.selfInfo) 
		{
			
			this.showInfo.push(this.selfInfo[i]);
			
		}

		this.setTotalPage();
		if (this.showInfo.length) { // 如果有结果
			this.turnToPage(1);
		} else { // 没查到结果
			this.turnToPage(0);
		}
	};


	NoticeTable.prototype.eventRegistered = function () 
	{
		var self = this;
		
		Base.prototype.eventRegistered.call(this); 
			
			$("#base_table_Imp span").unbind().on("click", function () { // Imp
			
				var s = self;
				var test = self.selfInfo;
				
				var test1 = test.sort(function(a, b) {
					return b.all_bg_pv_sum - a.all_bg_pv_sum;
				});
			    self.selfInfo = test1;
			    
				var obj = $(this);
				if(obj.hasClass("glyphicon-chevron-up"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = false;
				}
				else if(obj.hasClass("glyphicon-chevron-down"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = true;
				}
				
				self.showCreaterResult();
			});
			
			

			$("#base_table_uv span").unbind().on("click", function () { // uv
			
				var s = self;
				var test = self.selfInfo;
				
				var test1 = test.sort(function(a, b) {
					return b.all_bg_uv_sum - a.all_bg_uv_sum;
				});
			    self.selfInfo = test1;
			    
				var obj = $(this);
				if(obj.hasClass("glyphicon-chevron-up"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = false;
				}
				else if(obj.hasClass("glyphicon-chevron-down"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = true;
				}
				
				self.showCreaterResult();
			});
		
			
		
			$("#base_table_click span").unbind().on("click", function () { // uv
				
				var s = self;
				var test = self.selfInfo;
				
				var test1 = test.sort(function(a, b) {
					return b.all_dj_pv_sum - a.all_dj_pv_sum;
				});
			    self.selfInfo = test1;
			    
				var obj = $(this);
				if(obj.hasClass("glyphicon-chevron-up"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = false;
				}
				else if(obj.hasClass("glyphicon-chevron-down"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = true;
				}
				
				self.showCreaterResult();
			});
			
			
			
			
			$("#base_table_clicker span").unbind().on("click", function () { // uv
				
				var s = self;
				var test = self.selfInfo;
				
				var test1 = test.sort(function(a, b) {
					return b.all_dj_uv_sum - a.all_dj_uv_sum;
				});
			    self.selfInfo = test1;
			    
				var obj = $(this);
				if(obj.hasClass("glyphicon-chevron-up"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = false;
				}
				else if(obj.hasClass("glyphicon-chevron-down"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = true;
				}
				
				self.showCreaterResult();
			});
			
            $("#fr_list_td1 span").unbind().on("click", function () { 
				
				var s = self;
				var test = self.selfInfo;
				
				var test1 = test.sort(function(a, b) {
					return b.userList[0] - a.userList[0];
				});
			    self.selfInfo = test1;
			    
				var obj = $(this);
				if(obj.hasClass("glyphicon-chevron-up"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = false;
				}
				else if(obj.hasClass("glyphicon-chevron-down"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = true;
				}
				
				self.showCreaterResult();
			});

            $("#fr_list_td2 span").unbind().on("click", function () { 
				
				var s = self;
				var test = self.selfInfo;
				
				var test1 = test.sort(function(a, b) {
					return b.userList[1] - a.userList[1];
				});
			    self.selfInfo = test1;
			    
				var obj = $(this);
				if(obj.hasClass("glyphicon-chevron-up"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = false;
				}
				else if(obj.hasClass("glyphicon-chevron-down"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = true;
				}
				
				self.showCreaterResult();
			});
            
            $("#fr_list_td3 span").unbind().on("click", function () { 
				
				var s = self;
				var test = self.selfInfo;
				
				var test1 = test.sort(function(a, b) {
					return b.userList[2] - a.userList[2];
				});
			    self.selfInfo = test1;
			    
				var obj = $(this);
				if(obj.hasClass("glyphicon-chevron-up"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = false;
				}
				else if(obj.hasClass("glyphicon-chevron-down"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = true;
				}
				
				self.showCreaterResult();
			});
            
            $("#fr_list_td4 span").unbind().on("click", function () { 
				
				var s = self;
				var test = self.selfInfo;
				
				var test1 = test.sort(function(a, b) {
					return b.userList[3] - a.userList[3];
				});
			    self.selfInfo = test1;
			    
				var obj = $(this);
				if(obj.hasClass("glyphicon-chevron-up"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = false;
				}
				else if(obj.hasClass("glyphicon-chevron-down"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = true;
				}
				
				self.showCreaterResult();
			});
            
            $("#fr_list_td5 span").unbind().on("click", function () { 
				
				var s = self;
				var test = self.selfInfo;
				
				var test1 = test.sort(function(a, b) {
					return b.userList[4] - a.userList[4];
				});
			    self.selfInfo = test1;
			    
				var obj = $(this);
				if(obj.hasClass("glyphicon-chevron-up"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = false;
				}
				else if(obj.hasClass("glyphicon-chevron-down"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = true;
				}
				
				self.showCreaterResult();
			});
            
            $("#fr_list_td6 span").unbind().on("click", function () { 
				
				var s = self;
				var test = self.selfInfo;
				
				var test1 = test.sort(function(a, b) {
					return b.userList[5] - a.userList[5];
				});
			    self.selfInfo = test1;
			    
				var obj = $(this);
				if(obj.hasClass("glyphicon-chevron-up"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = false;
				}
				else if(obj.hasClass("glyphicon-chevron-down"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = true;
				}
				
				self.showCreaterResult();
			});
            
            
            $("#fr_list_td7 span").unbind().on("click", function () { 
				
				var s = self;
				var test = self.selfInfo;
				
				var test1 = test.sort(function(a, b) {
					return b.userList[6] - a.userList[6];
				});
			    self.selfInfo = test1;
			    
				var obj = $(this);
				if(obj.hasClass("glyphicon-chevron-up"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = false;
				}
				else if(obj.hasClass("glyphicon-chevron-down"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = true;
				}
				
				self.showCreaterResult();
			});
            
            
            $("#fr_list_td8 span").unbind().on("click", function () { 
				
				var s = self;
				var test = self.selfInfo;
				
				var test1 = test.sort(function(a, b) {
					return b.userList[7] - a.userList[7];
				});
			    self.selfInfo = test1;
			    
				var obj = $(this);
				if(obj.hasClass("glyphicon-chevron-up"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = false;
				}
				else if(obj.hasClass("glyphicon-chevron-down"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = true;
				}
				
				self.showCreaterResult();
			});
            
            
            $("#fr_list_td9 span").unbind().on("click", function () { 
				
				var s = self;
				var test = self.selfInfo;
				
				var test1 = test.sort(function(a, b) {
					return b.userList[8] - a.userList[8];
				});
			    self.selfInfo = test1;
			    
				var obj = $(this);
				if(obj.hasClass("glyphicon-chevron-up"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = false;
				}
				else if(obj.hasClass("glyphicon-chevron-down"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = true;
				}
				
				self.showCreaterResult();
			});
            
            
            $("#fr_list_td10 span").unbind().on("click", function () { 
				
				var s = self;
				var test = self.selfInfo;
				
				var test1 = test.sort(function(a, b) {
					return b.userList[9] - a.userList[9];
				});
			    self.selfInfo = test1;
			    
				var obj = $(this);
				if(obj.hasClass("glyphicon-chevron-up"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = false;
				}
				else if(obj.hasClass("glyphicon-chevron-down"))
				{
					$(".span_hide").removeClass("span_hide");
					obj.addClass("span_hide");
					self.turn = true;
				}
				
				self.showCreaterResult();
			});

            
            
	};

	return NoticeTable;
});