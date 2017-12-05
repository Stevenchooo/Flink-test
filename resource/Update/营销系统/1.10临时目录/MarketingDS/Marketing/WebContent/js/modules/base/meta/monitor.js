define(function(require, exports, module) {
    var MetaBase = require('./meta');
    require('dialog');
    require('jquery');

    var MetaMonitor = {
        
        modifyPara: function($scope, id) {
            return {
                method: 'POST', 
                url: this.getWebRoot() + "/api/model/modify",
                params: {
                    id: id,
                    name: $("#id_mod_resourceId").val(),
                    val: $("#id_mod_topLimit").val() + ':' + $("#id_mod_reportPeriod").val()
                }
            };
        },
        
        modifyDialog:function(id, $scope) {
        	$scope.callApi({
                url:this.getWebRoot() + "/api/model/detail",
        		method:"GET",
        		params:{id:id}
    		}).success(function(resp, status, headers, config){
                if(resp.resultCode != 0) {
    	          	$scope.failToOperate(resp);
                    return;
                }
                $scope.mod_resourceId = resp.name;
                var pos = resp.val.indexOf(':');
                if(pos > 0) {
               	    $scope.mod_topLimit = parseInt(resp.val.substr(0, pos));
               	    $scope.mod_reportPeriod = parseInt(resp.val.substr(pos + 1));
                } else {
               	    $scope.mod_topLimit = parseInt(resp.val);
               	    $scope.mod_reportPeriod = 30;
                }
		    }).error(function(response, status, headers, config){
		    	$scope.httpError(status);
			});
            
            return 'id_modify_dialog';
        },
        
        createDialog:function($scope) {
        	$scope.crt_resourceId = '';
        	$scope.crt_topLimit = '';
        	$scope.crt_reportPeriod = '';
            $scope.createForm.$setPristine();
        	return 'id_create_dialog';
        },
        
        createPara: function($scope, pid, meta) {
            return {
                method:"POST",
                url: this.getWebRoot()+"/api/model/create",
                params: {
                    pid: pid,
                    name: $("#id_crt_resourceId").val(),
                    type: meta,
                    val: $("#id_crt_topLimit").val() + ':' + $("#id_crt_reportPeriod").val()
                }
            };
        },
        
        createCheck : function($scope) {
            return $scope.createForm.$valid;
        },
        
        modifyCheck : function($scope) {
            return $scope.modifyForm.$valid;
        },
        
        extendScope: function($scope,$http) {
        	$scope.split = function(s, sp, sn){
        		var ss = s.split(sp);
        		if(ss.length > sn) {
        			return ss[sn];
        		}
        		return '';
        	};
        }
    };

    exports.instance = function(webRoot, metaName, segments) {
    	var obj = MetaBase.instance(webRoot, metaName, segments);
    	$.extend(obj, MetaMonitor);
    	return obj;
    };
});