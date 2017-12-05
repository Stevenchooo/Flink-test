define(function() {
	Date.prototype.format = function(format) {// 设置时间格式
		var o = {
			"M+" : this.getMonth() + 1, // month
			"d+" : this.getDate(), // day
			"h+" : this.getHours(), // hour
			"m+" : this.getMinutes(), // minute
			"s+" : this.getSeconds(), // second
			"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
			"S" : this.getMilliseconds()
		// millisecond
		};
		if (/(y+)/.test(format))
			format = format.replace(RegExp.$1, (this.getFullYear() + "")
					.substr(4 - RegExp.$1.length));
		for ( var k in o)
			if (new RegExp("(" + k + ")").test(format))
				format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
						: ("00" + o[k]).substr(("" + o[k]).length));
		return format;
	};

	var tool = {};

	tool.extend = function(subClass, superClass) {
		var F = function() {
		};
		F.prototype = superClass.prototype;
		subClass.prototype = new F();
		subClass.prototype.constructor = subClass;
		subClass.superclass = superClass.prototype; // 加多了个属性指向父类本身以便调用父类函数
		if (superClass.prototype.constructor == Object.prototype.constructor)
		{
			superClass.prototype.constructor = superClass;
		}
	};

	var reg = new RegExp("-", "g");
	tool.setTimeParam = function(info) {
		if (info.createTime) {
			info.cT = new Date(info.createTime.replace(reg, "/")).getTime();
		}
		if (info.channel == 2) {
			if (info.messageSentRecord.startDate) {
				info.pT = new Date(info.messageSentRecord.startDate.replace(
						reg, "/")).getTime();
			}

		} else {
			if (info.promoteTime) {
				info.pT = new Date(info.promoteTime.replace(reg, "/"))
						.getTime();
			}
		}
	};

	tool.unique = function(array) { // 去重
		if (array.length < 2)
			return array[0] ? [ array[0] ] : [];
		var n = {}, r = []; // n为hash表，r为临时数组
		for (var i = 0; i < array.length; i++) // 遍历当前数组
		{
			if (!n[array[i]]) // 如果hash表中没有当前项
			{
				n[array[i]] = true; // 存入hash表
				r.push(array[i]); // 把当前数组的当前项push到临时数组里面
			}
		}
		return r;
	};
	return tool;
});