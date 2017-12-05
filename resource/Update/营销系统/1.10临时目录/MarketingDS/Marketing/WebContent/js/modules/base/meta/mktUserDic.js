define(function(require, exports, module) {
    var MetaBase = require('meta');
    require('dialog');
    require('jquery');
    require('calendar');
    require('jquery/calendar/1.0/tinycal.css');
    

    var MetaMktUserDic = {
        
        querySubPara: function($scope, params) {
      
        	
            return {
                method: 'GET',
                params:params,
                url: this.webRoot+"/api/mktUesrDic/UserInfoQuery"
            };
        },
    };

    exports.instance = function(webRoot, metaName, segments) {
    	var obj = MetaBase.instance(webRoot, metaName, segments);
    	$.extend(obj, MetaMktUserDic);
    	return obj;
    };
    
});