define(function(require, exports, module) {
    var MetaBase = require('./meta');
    require('dialog');
    require('jquery');
    var tree = require('modules/base/tree');

    var MetaRole = MetaBase.extend({
        initialize: function(webRoot, metaName, segments) {
        	MetaRole.superclass.initialize.call(this, webRoot, metaName, segments);
        },
        
        querySubPara: function($scope, params) {
            return {
                method: 'POST',
                params:params,
                url: this.webRoot+"/api/role/query"
            };
        },
        
        removePara: function($scope, pid, role) {
            return {
                method: 'POST',
                url: this.webRoot+"/api/role/remove",
                params: {pid:pid, role:role} 
            };
        },
        
        modifyPara: function($scope, role, pid) {
            return {
                method: 'POST', 
                url: this.getWebRoot() + "/api/role/modify",
                params: {
                	pid:pid,
                    role : role,
                    name: $scope.mod_name,
                    desc: $scope.mod_desc
                }
            };
        },
        
        createDialog:function($scope) {
            $scope.role = '';
            $scope.name = '';
            $scope.desc = '';
            $scope.roleCreateForm.$setPristine();
        	return 'id_create_role_dialog';
        },
        
        modifyDialog:function(role, $scope) {
            $scope.mod_role = role;
        	$scope.callApi({
      		    url:this.getWebRoot() + "/api/role/detail",
        		method:"POST",
        		params:{role:role}
    		}).success(function(resp, status, headers, config){
                if(resp.resultCode != 0) {
    	          	$scope.failToOperate(resp);
                    return;
                }
            	$scope.mod_name = resp.name;
            	$scope.mod_desc = resp.desc;
		    }).error(function(response, status, headers, config){
		    	$scope.httpError(status);
			});
            
            return 'id_modify_role_dialog';
        },
        
        createPara: function($scope, pid, meta) {
            return {
                method:"POST",
                url: this.getWebRoot() + "/api/role/create",
                params: {
                	pid:pid,
                	role:$scope.role,
                	name:$scope.name,
                	desc:$scope.desc
                }
            };
        },
        
        createCheck : function($scope) {
            return $scope.roleCreateForm.$valid;
        },
        
        modifyCheck : function($scope) {
        	return $scope.roleModifyForm.$valid;
        },
        
        extendScope : function($scope, $http) {
        	$scope.modelRight = function(role) {
            	var opts = $scope.options;
            	var results = [];
            	$.ajax({
          		  type:"POST",
        		  url:opts.webRoot + "/api/role/rightQuery?t=" + (new Date().getTime().toString(36)),
        		  async:false,
        		  data:{role:role,
            		  __userKey : __userKey,
        			  pid:opts.pid
        	      },
        	       
        		  success:function(resp){
                      if(resp.resultCode != 0) {
                     	 $scope.failToOperate(resp);
                         return;
                      }
                      results = resp.results;
        		  }
            	});
            	
            	$scope.roleRightResults = results;
	    		$scope.dialog = $.dialog({
	    			title: role + getLocalTag("rightSet"),
	        		content:document.getElementById("id_modify_right_dialog"),
	    			cancel:true,
	    			lock:true,
		    		okVal: getLocalTag('confirm','Yes'),
	        		cancelVal: getLocalTag('cancel','cancel'),
	    		    ok: function() {
	    		    	$scope.$apply(function(){
	    		    		var req = {
    		                    method:"POST",
    		                    url: opts.webRoot + "/api/role/rightSave",
    		                    data: {
    		                    	role:role,
    		                    	rights:$scope.roleRightResults
    		                    }
    		                };
	    		    		
			    			$scope.callApi(req).success(function(response, status, headers, config){
					          	if(response.resultCode == 0) {
				          			$scope.refresh();
					          		return;
					          	}
					          	$scope.failToOperate(response);
						    }).error(function(response, status, headers, config){
						    	$scope.httpError(status);
			    			});
		    			});
	    		    }
	        	});
        	};
        	
        	$scope.getRightName = function(r) {
        		return getLocalTag("right."+r, r);
        	};
        }
    });

    module.exports = MetaRole;
});