define(function(require, exports, module) {
	exports.dump = function(o) {
		if(o == null) {
			return 'null';
		}
		if(o == undefined) {
			return 'undefined';
		}
        var s = [];
        for(var i in o) {
            s.push(i, "=", o[i], "\n");
        }
        console.info(s.join(''));
    }
	/**
	 * 将数组按照一定字段转为json对象的数组
	 */
	exports.arrToJsonObject = function(o){
		if (o == null||o.length<=0){
			return [];
		}
		var arrJson = [];
		var tmp = {};
		tmp.createDate = o[0].create_time;
		tmp.mktarr = [];
		tmp.mktarr.push(o[0].mktinfoName);
		arrJson.push(tmp);
		for(var i=1;i<o.length;i++){
			var isExist = false;
			for(var j=0;j<arrJson.length;j++){
				
				if(arrJson[j].createDate == o[i].create_time){
					arrJson[j].mktarr.push(o[i].mktinfoName);
					isExist = true;
				}
			}
			if(!isExist){
				var tmpFk = {};
				tmpFk.createDate = o[i].create_time;
				tmpFk.mktarr = [];
				tmpFk.mktarr.push(o[i].mktinfoName);
				arrJson.push(tmpFk);
			}
		}
		return arrJson;
	}
	/*
	 * 年、月、营销活动转换成json
	 */
	exports.mktToJson=function(o){
		if (o == null||o.length<=0){
			return [];
		}
		//月和活动
		var arrJson=[];
		var tmp={};
		tmp.create_year=o[0].create_year;
		tmp.create_month=o[0].create_month;
		tmp.mktarr=[];
		tmp.mktarr.push({"mktName":o[0].mktinfoName,"mktId":o[0].mktinfoId});
		//tmp.mktarr.push(o[0].mktinfoName);
		arrJson.push(tmp);
		for(var i=1;i<o.length;i++){
			var isExist = false;
			for(var j=0;j<arrJson.length;j++){

				if(arrJson[j].create_year==o[i].create_year&&arrJson[j].create_month == o[i].create_month){
					//arrJson[j].mktarr.push(o[i].mktinfoName);
					arrJson[j].mktarr.push({"mktName":o[i].mktinfoName,"mktId":o[i].mktinfoId});
					isExist = true;
				}
			}
			if(!isExist){
				var tmpFk = {};
				tmpFk.create_year=o[i].create_year;
				tmpFk.create_month = o[i].create_month;
				tmpFk.mktarr = [];
				tmpFk.mktarr.push({"mktName":o[i].mktinfoName,"mktId":o[i].mktinfoId});
				arrJson.push(tmpFk);
			}
		}
		return arrJson;
	}
	/*
	 * 媒体类型和网站转换成json
	 */
	exports.mediaToJson=function(o){
		if (o == null||o.length<=0){
			return [];
		}
		var arrJson = [];
		var tmp = {};
		tmp.mediaName = o[0].mediaName;
		tmp.mktarr = [];
		tmp.mktarr.push(o[0].webName);
		arrJson.push(tmp);
		for(var i=1;i<o.length;i++){
			var isExist = false;
			for(var j=0;j<arrJson.length;j++){
				
				if(arrJson[j].mediaName == o[i].mediaName){
					arrJson[j].mktarr.push(o[i].webName);
					isExist = true;
				}
			}
			if(!isExist){
				var tmpFk = {};
				tmpFk.mediaName = o[i].mediaName;
				tmpFk.mktarr = [];
				tmpFk.mktarr.push(o[i].webName);
				arrJson.push(tmpFk);
			}
		}
		return arrJson;
	}
});