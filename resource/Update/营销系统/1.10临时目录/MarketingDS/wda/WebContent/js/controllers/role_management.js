app.factory('roleService', ['$http', '$rootScope', function($http, $rootScope) {
	
	var role = {};
	var roles = [];
    var groups = [];
    var roleright = {};
    var selectedGroups = [];
  return {
	
    getRoles: function() {
      return $http.get(__urlRoot + '/wda/api/role/query',{
    	  params: {
    		  "__userKey" : __userKey,
    	  }
      }).then(function(response) {
    	  roles = response.data;    
        //$rootScope.$broadcast('handleSharedBooks',books);
        return roles;
      })
    },
    
    deleteRole: function(roleName) {
        return $http({
          url: __urlRoot + '/wda/api/role/remove',
          method: "POST",
          params: {role: roleName}
        }).success(function(response) {
        	//websites = response.data;
        });
    },
    
    getRoleByRole : function(roleName) {
      return $http.get(__urlRoot + '/wda/api/role/queryByRole', {
    	  params: {
    		  role: roleName
    	  }
      }).then(function(response) {
    	  role = response.data;    
          //$rootScope.$broadcast('handleSharedBooks',books);
          return role;
        })
    },
    
    getRoleRightByRole : function(roleName) {
        return $http.get(__urlRoot + '/wda/api/role/queryRoleRight', {
      	  params: {
      		  "role" : roleName,
      		  "__userKey" : __userKey
      	  }
        }).then(function(response) {
      	  roleright = response.data;    
//      	$scope.roleRightResults = roleright.results;
            //$rootScope.$broadcast('handleSharedBooks',books);
            return roleright;
          })
      },
    
    saveRole: function($params) {
      return $http({
        headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
        url: __urlRoot + '/wda/api/role/create',
        method: "POST",
        data: $params
      }).success(function(response) {	
          //$rootScope.$broadcast('handleSharedBooks',books);
    	  return response.resultCode;
      });
    },
    
    updateRole: function(roleName) {
    	return $http({
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            url: __urlRoot + '/wda/api/role/create',
            method: "POST",
            data: $params
          })
            .success(function(response) {
            	role = response.data;
              //$rootScope.$broadcast('handleSharedBooks',books);
            });
    },
    
  };
}]);

app.controller('RoleManagementCtrl', 
		['$rootScope', '$scope', '$filter', '$http', '$timeout', 'exDialog', 'roleService', '$state',
		 function($rootScope, $scope, $filter, $http, $timeout, exDialog, roleService, $state) {
    
//	if(__account != 'admin') {
//		$state.go('noright', {});
//	}
	
	$scope.roleManageJson={
			curpage : 1,
			itemcss : "un-item uni-cur",
			lastsearchtxt : '',
	};
	
	$scope.setCurPage = function(page) {
		if (page == $scope.roleManageJson.curpage) {
			return "pgcur";
		} else {
			return "";
		}
	};
	$scope.selectPage = function(page) {
		if (page <= $scope.roleManageJson.totalPage && page >= 1) {
			$scope.roleManageJson.curpage = page;
		}
	};
			
	$scope.options = [{value: 0, name: '否'}, {value: 1, name: '是'}];
	$scope.selected = 0;
	
	roleService.getRoles().then(function(response) {
		 $scope.roleManageJson.data =  response.data;
	    $scope.roles = response.data;
	});
	
	$scope.$on('roleList', function(events, values) {
		 $scope.roleManageJson.data =  values;
		$scope.roles = values;
	});
	
    $scope.editRoleDialog = function (roleName) {
    	roleService.getRoleByRole(roleName).then(function(response) {
    		$scope.role = response.data;
    		
    		exDialog.openPrime({
                scope: $scope,
                template: __tplRoot + 'tpl/role_management_add.html',
                controller: 'RoleManagementCtrl',
                width: '600px',
                draggable: false,
                closeByClickOutside: false
                //animation: false,
                //grayBackground: false
            });
    	});
    };
    
    /*
     * 角色权限
     */
    $scope.updateRoleDialog = function (roleName) {
    	$scope.updateRightRoleName = roleName;
    	roleService.getRoleRightByRole(roleName).then(function(response) {
    		roleright = response.results;
    		$scope.roleRights = roleright;
    		exDialog.openPrime({
                scope: $scope,
                template: __tplRoot + 'tpl/role_right_management_add.html',
                controller: 'RoleManagementCtrl',
                width: '600px',
                draggable: false,
                closeByClickOutside: false
                //animation: false,
                //grayBackground: false
            });
    	});
    };
    
    $scope.addRoleDialog = function () {
    	$scope.role = null;
		exDialog.openPrime({
            scope: $scope,
            template: __tplRoot + 'tpl/role_management_add.html',
            controller: 'RoleManagementCtrl',
            width: '600px',
            draggable: false,
            closeByClickOutside: false
            //animation: false,
            //grayBackground: false
        });
    };
    
    $scope.saveRole = function(role) {
    	if(role) {
    		$scope.updateRole(role);
    	} else {
    		$scope.addRole();
    	}
    };
    
    $scope.getRightName = function(r){
    	return getLocalTag("right."+r, r);
    };
    
    $scope.getMetaName = function(meta){
    	return getLocalTag("meta."+meta, meta);
    };
    
    $scope.updateRole = function (role) {
    	
    	exDialog.openConfirm({
            scope: $scope,
            title: "   ",
            message: "确认保存吗?",
            closeImmediateParent: true
        }).then(function (value) {
        	$params = $.param({
        		"__userKey" : __userKey,
        		"role" : $scope.role.role,
        	    "name": $scope.role.name,
            	"description" : $scope.role.description,
        	});
        	
        	$http({
                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
                url: __urlRoot + '/wda/api/role/create',
                method: "POST",
                data: $params
              }).success(function(response) {	
                  
            	  if(response.resultCode == 0) {
            		  roleService.getRoles().then(function(response) {
            			  $scope.roleManageJson.data =  response.data;
            			  $scope.roles = response.data;
            			  $rootScope.$broadcast('roleList',$scope.roles);
            		  });
            		  
            		  exDialog.closeAll();
            	  }
            	  else {
            		  exDialog.openMessage({
            			  scope: $scope,
            			  title: "   ",
            			  icon: "error",
            	          message: getLocalReason(response.resultCode, response.info),
            	          closeAllDialogs: true
            	      });
            	  }  
              });
        });
    };
    
$scope.saveRoleRight = function () {
    	
    	exDialog.openConfirm({
            scope: $scope,
            title: "   ",
            message: "确认保存吗?",
            closeImmediateParent: true
        }).then(function (value) {
        	$params = $.param({
        		"__userKey" : __userKey,
        		"role" : $scope.updateRightRoleName,
            	"rights" : $scope.roleRights//JSON.stringify()
        	});
        	
        	$http({
                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
                url: __urlRoot + '/wda/api/role/rightSave',
                method: "POST",
                data: $params
              }).success(function(response) {	
            	  if(response.resultCode == 0) {
            		  roleService.getRoles().then(function(response) {
            			  $scope.roleManageJson.data =  response.data;
            			  $scope.roles = response.data;
            			  $rootScope.$broadcast('roleList',$scope.roles);
            		  });
            		  
            		  exDialog.closeAll();
            	  }
            	  else {
            		  exDialog.openMessage({
            			  scope: $scope,
            			  title: "   ",
            			  icon: "error",
            	          message: getLocalReason(response.resultCode, response.info),
            	          closeAllDialogs: true
            	      });
            	  }  
              });
        });
    };
    
    $scope.addRole = function () {
    	
    	exDialog.openConfirm({
            scope: $scope,
            title: "   ",
            message: "确认保存吗?",
            closeImmediateParent: true
        }).then(function (value) {
            
        	$params = $.param({
        		"__userKey" : __userKey,
        		"role" : $scope.role.role,
        	    "name": $scope.role.name,
            	"description" : $scope.role.description
        	});
        	
        	$http({
                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
                url: __urlRoot + '/wda/api/role/create',
                method: "POST",
                data: $params
              }).success(function(response) {	
                  
            	  if(response.resultCode == 0) {
            		  roleService.getRoles().then(function(response) {
            			  $scope.roleManageJson.data =  response.data;
            			  $scope.roles = response.data;
            			  $rootScope.$broadcast('roleList',$scope.roles);
            		  });
            		  
            		  exDialog.closeAll();
            	  }
            	  else {
            		  exDialog.openMessage({
            			  scope: $scope,
            			  title: "   ",
            			  icon: "error",
            	          message: getLocalReason(response.resultCode, response.info),
            	          closeAllDialogs: false
            	      });
            	  }  
              });
        });
    };
    
    $scope.delRoleDialog = function (role) {
		
    	exDialog.openConfirm({
    	    scope: $scope,
    	    title: "   ",
    	    icon: "warning",
    	    closeByClickOutside: false,
    	    message: "确认要删除吗?"
    	}).then(function (value) {
    		
    		$http({
    	          url: __urlRoot + '/wda/api/role/remove',
    	          method: "POST",
    	          params: { 
    	        	  role: role, 
    	        	  __userKey : __userKey
    	          }
    	        }).success(function(response) {
    	        	if(response.resultCode == 0) {
    	        		roleService.getRoles().then(function(response) {
              			 $scope.roleManageJson.data =  response.data;
              			  $scope.roles = response.data;
              			  $rootScope.$broadcast('roleList',$scope.roles);
              		  });
              		  
              		  exDialog.closeAll();
              	  	}
              	  	else {
              		  exDialog.openMessage({
              			  scope: $scope,
              			  title: "错误",
              	          message: getLocalReason(response.resultCode, response.info),
              	          closeAllDialogs: true
              	      });
              	  	}
    	        });
    	});
    };
    
}]);