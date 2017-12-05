/*
 * 主要思路：用一个变量（selfInfo）存储全部数据，也可以随意增删改排序，并能体现到图表上；
 *           用一个变量（showInfo）存符合筛选条件的数据（没有筛选则就是selfInfo），表格切页是从这里面找对应页
 * w00296102
 */
define(function (require) {
	var first = true; // 防止事件重复绑定标识
	var Base = function (rows, chooseTabNum, selfInfo, tableId, backEmptp,column) // 一页展示多少，分页器显示几个、数据、table表id、是否流后白、表格列
	{ 
		this.rows = rows; // 如果是0则不分页，一页画到底
		this.chooseTabNum = chooseTabNum; // 如果是偶数，则前小后大
		this.selfInfo = selfInfo; // 全部的数据
		this.fk = tableId;
		this.showInfo = this.selfInfo; // 要展示的数据
		this.$table = $("#" + tableId); // 表格的HTML
		this.backEmptp = backEmptp;
		this.column = column || this.$table.find("th").length; // 不传列数则默认为th格数

		this.turn = true; // 默认顺序排序
	};

	Base.prototype = {

		init : function () 
		{
			if (this.rows == 0) 
			{
				this.$table.find("table").append(
					this.emptyTableHtml(this.showInfo.length));
				for (var i = 0; i < this.showInfo.length; i++) 
				{
					this.putSigleInfo(this.showInfo[i], this.$table.find("table").find(".row_" + i));
				}
			} 
			else 
			{
				this.setTotalPage();
				this.setEmptyTable();
			}
		},

		setTotalPage : function () 
		{ // 设置总页数
			this.totalPage = Math.ceil((this.showInfo.length) / this.rows);
			this.$table.find(".pg-no").html(this.totalPage);
		},

		setEmptyTable : function () 
		{ // 创建空表
			this.$table.find(".up-pager").show();
			$(".uppg")
			.html(
				'<a href="javascript:void(0);" class="first-page" data-page="first">首页</a>'
				 + '<a href="javascript:void(0);" class="uppg-prv" data-page="pre">上一页</a>'
				 + '<a href="javascript:void(0);" class="uppg-nt" data-page="next">下一页</a>'
				 + '<a href="javascript:void(0);" class="last-page" data-page="last">尾页</a>');
			var temNt = this.$table.find(".uppg-nt");
			for (var i = 1; i <= this.totalPage; i++)
			{ // 把所有分页选项都做出来（可优化点，全做出来浪费性能了）
				temNt
				.before("<a href='javascript:void(0);' class='num-page' data-page='"
					 + i + "' style='display:none;'>" + i + "</a>");
			}

			this.$table.find("tbody").html(null);
			this.$table.find("tbody").append(this.emptyTableHtml(this.rows));
			this.turnToPage(1);
			this.setOption(); //
		},

		emptyTableHtml : function (needrow) 
		{
			var html = "";
			
			if(0 == this.selfInfo.length)
			{
				return html;
			}
			
			var td = "";
			for (var i = 0; i < this.column; i++) 
			{
				td += "<td></td>";
			}
			for (var i = 0; i < needrow; i++) 
			{
				html += "<tr class='row_" + i + " info'>" + td + "</tr>";
			}
			return html;
		},

		turnToPage : function (targetPage) 
		{ // 展示对应页内容
			if (targetPage >= 0 && targetPage <= this.totalPage) 
			{
				this.$table.find("td").empty(); // 先清空表
				this.page = targetPage;
				this.setChooseTab();

				if (!targetPage) 
				{
					return;
				}

				var setInfo = function (self, i, n) 
				{
					var $row = self.$table.find(".row_" + n);
					
					var kk = self.fk;
					//console.log(kk);
					if (self.showInfo[i]) {
						$row.show();
						self.putSigleInfo(self.showInfo[i], $row,kk);
					} else if (!self.backEmptp) 
					{
						$row.hide();
					}
				};

				if (this.turn) 
				{
					for (var i = (targetPage - 1) * this.rows, n = 0; i < targetPage
						 * this.rows; i++, n++)
					{
						setInfo(this, i, n);
					}
				} 
				else 
				{
					for (var i = this.showInfo.length - (targetPage - 1)
							 * this.rows - 1, n = 0; i > this.showInfo.length
						 - targetPage * this.rows - 1; i--, n++) 
					{
						setInfo(this, i, n);
					}
				}

			}
		},

		setChooseTab : function () { // 设置分页器
			var $chooseNum = this.$table.find(".uppg");
			$chooseNum.find(".num-page").hide();
			var chooseTabNum = this.chooseTabNum;
			var totalPage = this.totalPage;
			if (this.page === 0) {
				$chooseNum.find("a[data-page='" + 1 + "']").show().addClass(
					"pgcur");
			} else {
				if (this.page > (totalPage - (chooseTabNum + 1) / 2)) {
					for (var i = totalPage - chooseTabNum + 1; i <= totalPage; i++) {
						$chooseNum.find("a[data-page='" + i + "']").show();
					}
				} else if (this.page < (chooseTabNum + 1) / 2) {
					for (var i = 1; i <= chooseTabNum; i++) {
						$chooseNum.find("a[data-page='" + i + "']").show();
					}
				} else {
					for (var i = this.page - Math.floor((chooseTabNum - 1) / 2); i <= this.page
						 + Math.ceil((chooseTabNum - 1) / 2); i++) {
						$chooseNum.find("a[data-page='" + i + "']").show();
					}
				}
				$chooseNum.children().removeClass("pgcur");
				$chooseNum.find("a[data-page='" + this.page + "']").addClass(
					"pgcur");
			}
		},
		eventRegistered : function () 
		{
			var self = this;
			// 动态界面注册事件
			this.$table.find(".uppg").unbind().on("click", "a", function () 
			{
				if ($("#load-me").is(":visible")) {
					return;
				}
				switch ($(this).data("page")) {
				case "pre": // 上一页
					if (self.page === 1) { // 如果当前页是第一页就无视了
						return;
					}
					self.turnToPage(self.page - 1);
					break;
				case "next": // 下一页
					if (self.page == self.totalPage) {
						return;
					}
					self.turnToPage(self.page + 1);
					break;
				case "last": // 尾页
					self.turnToPage(self.totalPage);
					break;
				case "first": // 首页
					self.turnToPage(1);
					break;
				default:
					self.turnToPage($(this).data("page"));
				}
			});
			if (!first) {
				return false;
			}
			first = false;
			$(document)
			.on(
				// 点击桌面任意一个地方，隐藏显示框
				"mousedown",
				function (ev) {
				var tarItem = $(".updt-ddopen");
				if (!tarItem.is(ev.target)
					 && tarItem.has(ev.target).length === 0
					 && $(".deco").parent().hasClass(
						"updt-ddopen")) {
					$(".deco").parent().removeClass(
						"updt-ddopen");
				}

			});

			$(".updt-dropdown").on("click", ".deco", function () {
				if ($(this).parent().hasClass("updt-ddopen")) {
					$(this).parent().removeClass("updt-ddopen");
				} else {
					$(this).parent().addClass("updt-ddopen");
				}
			});

			$(".titsort").on(
				"click",
				".ude-txtopt",
				function () {
				if ($(this).hasClass("ude-disable")) {
					return;
				}
				$(this).addClass("ude-tocur").siblings(".ude-txtopt")
				.removeClass("ude-tocur");
				//$(this).parent().siblings("em").html($(this).html());
				$(this).parents(".titsort").removeClass("updt-ddopen");
			});
			return true;
		},

		setOption : function () {},

	};
	return Base;
});