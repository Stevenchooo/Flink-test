define(function(require, exports, module) {
    var MetaBase = require('./meta');
    require('dialog');
    require('jquery');
    var tree = require('modules/base/tree');
    var model = require('modules/base/model');

    var MetaAdmin = {
        
        querySubPara: function($scope, params) {
            return {
                method: 'GET',
                params: params,
                url: this.webRoot+"/api/model/queryAdmin"
            };
        },
        
        modifyPara: function($scope, id) {
            return {
                method: 'POST', 
                url: this.getWebRoot() + "/api/admin/modify",
                params: {
                    id: id,
                    role: $scope.mod_role
                }
            };
        },
        
        removePara: function($scope, pid, rowId) {
            return {
                method: 'POST',
                url: this.webRoot+"/api/admin/remove",
                data: {pid:pid, id:rowId} 
            };
        },
        
        createDialog:function($scope) {
        	
        	$scope.callApi({
	        	url: this.getWebRoot()+"/api/admin/queryAccount", 
         		method:"GET",
         		params:{pid:$scope.options.id}
     		}).success(function(resp, status, headers, config){
                if(resp.resultCode != 0) {
     	         	$scope.failToOperate(resp);
                    return;
                }
                $scope.accounts = resp.result;
             	
 		    }).error(function(response, status, headers, config){
 		    	$scope.httpError(status);
 			});
        	
        	
            $scope.crt_account = '';
            $scope.createForm.$setPristine();
        	return 'id_create_dialog';
        },
        
        modifyDialog:function(id, $scope) {
        	$scope.callApi({
      		    url:this.getWebRoot() + "/api/admin/detail",
        		method:"GET",
        		params:{id:id}
    		}).success(function(resp, status, headers, config){
                if(resp.resultCode != 0) {
    	          	$scope.failToOperate(resp);
                    return;
                }
                $scope.account = resp.account;
                $scope.mod_role = resp.role;
		    }).error(function(response, status, headers, config){
		    	$scope.httpError(status);
			});
            
            return 'id_modify_dialog';
        },
        
        createPara: function($scope, pid, meta) {
        	var account = $scope.crt_account;
        	var exists = false;
        	model.callApi({
    		  type:"GET",
    		  url:this.getWebRoot() + "/api/user/exists",
    		  async:false,
    		  data:{modelId:pid, account:account},
    		  success:function(resp){
                 if(resp.resultCode != 0) {
                     return;
                 }
                 exists = resp.exists;
    		  }
      		});
        	
        	if(!exists) {
        		dialog({
        			title:getLocalTag("failToOperate"),
        			content:getLocalTag("userNotExists", "User dosen't exist"),
    	    		okValue: getLocalTag('confirm','Yes'),
        			ok:true
        		}).showModal();
        		return null;
        	}

            return {
                method:"POST",
                url: this.getWebRoot() + "/api/admin/create",
                params: {
                    pid: pid,
                    account: account,
                    role: $scope.crt_role,
                }
            };
        },
        
        createCheck : function($scope) {
            return $scope.createForm.$valid;
        },
        
        modifyCheck : function($scope) {
            return true;
        },
        
        extendScope: function($scope,$http) {
        	$scope.callApi({
	        	url: this.getWebRoot()+"/api/role/query", 
         		method:"GET",
         		params:{}
     		}).success(function(resp, status, headers, config){
                if(resp.resultCode != 0) {
     	         	$scope.failToOperate(resp);
                    return;
                }
                $scope.roles = resp.roles;
             	$scope.crt_role = resp.roles[0].role;
            	$scope.mod_role = resp.roles[0].role;
 		    }).error(function(response, status, headers, config){
 		    	$scope.httpError(status);
 			});
        }
    };

    exports.instance = function(webRoot, metaName, segments) {
    	var obj = MetaBase.instance(webRoot, metaName, segments);
    	$.extend(obj, MetaAdmin);
    	return obj;
    };
});