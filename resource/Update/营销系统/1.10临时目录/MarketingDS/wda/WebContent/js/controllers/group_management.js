app.factory('groupService', ['$http', '$rootScope', function($http, $rootScope) {
	
	var group = {};
	var groups = [];
	var users = [];
  
  return {
	
    getGroups: function() {
      return $http.get(__urlRoot + '/wda/api/wda/admin/group/list').then(function(response) {
    	  groups = response.data;    
        //$rootScope.$broadcast('handleSharedBooks',books);
        return groups;
      })
    },
    
    getUsers: function() {
        return $http.get(__urlRoot + '/wda/api/wda/admin/user/select').then(function(response) {
      	  users = response.data;
          return users;
        })
    },
      
    
    deleteGroup: function(id) {
        return $http({
          url: __urlRoot + '/wda/api/wda/admin/group/delete',
          method: "POST",
          params: {id: id}
        }).success(function(response) {
        	//websites = response.data;
        });
    },
    
    getGroupById : function(id) {
      return $http.get(__urlRoot + '/wda/api/wda/admin/group/queryById', {
    	  params: {
    		  id: id
    	  }
      }).then(function(response) {
    	  group = response.data;    
          //$rootScope.$broadcast('handleSharedBooks',books);
          return group;
        })
    },
    
    addGroup: function($params) {
      return $http({
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        url: __urlRoot + '/wda/api/wda/admin/group/create',
        method: "POST",
        data: $params
      })
        .success(function(response) {
          //groups = response.data;
          //$rootScope.$broadcast('handleSharedBooks',books);
        });
    },
    
    updateGroup: function(id) {
    	return $http({
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            url: __urlRoot + '/wda/api/wda/admin/group/update',
            method: "POST",
            data: $params
          })
            .success(function(response) {
              group = response.data;
              //$rootScope.$broadcast('handleSharedBooks',books);
            });
    }
  };
}]);

app.controller('GroupManagementCtrl', 
		['$rootScope', '$scope', '$filter', '$http', '$timeout', 'exDialog', 'groupService', '$state',
		 function($rootScope, $scope, $filter, $http, $timeout, exDialog, groupService, $state) {
    
	if($rootScope.risAdmin == 0) {
		$state.go('noright', {});
	}
			
	$scope.groupManageJson={
			curpage : 1,
			itemcss : "un-item uni-cur",
			lastsearchtxt : '',
	};
	
	$scope.setCurPage = function(page) {
		if (page == $scope.groupManageJson.curpage) {
			return "pgcur";
		} else {
			return "";
		}
	};
	$scope.selectPage = function(page) {
		if (page <= $scope.groupManageJson.totalPage && page >= 1) {
			$scope.groupManageJson.curpage = page;
		}
	};
	
	//group list
	groupService.getGroups().then(function(response) {
		$scope.groupManageJson.data = response.data;
	    $scope.groups = response.data;
	});
	
	groupService.getUsers().then(function(response) {
	    $scope.users = response.data;
	});
	
	$scope.$on('groupList', function(events, values) {
		$scope.groupManageJson.data =  values;
		$scope.groups = values;
	});
	
	$scope.selectMemberDialog = function() {
		
		exDialog.openPrime({
            scope: $scope,
            template: __tplRoot + 'tpl/member_list.html',
            controller: 'GroupManagementCtrl',
            width: '600px',
            draggable: true,
            closeByClickOutside: false
        });
	};
	
	$scope.addGroupDialog = function () {
		
		$scope.group = null;
		
		$scope.selectedUsers = [];
		
		exDialog.openPrime({
            scope: $scope,
            template: __tplRoot + 'tpl/group_management_add.html',
            controller: 'GroupManagementCtrl',
            width: '600px',
            draggable: false,
            closeByClickOutside: false
        });
    };
    
    $scope.saveGroup = function(groupId) {
    	
    	if(groupId) {
    		$scope.updateGroup(groupId);
    	} else {
    		$scope.addGroup();
    	}
    };
    
    $scope.addGroup = function () {
    	
    	exDialog.openConfirm({
            scope: $scope,
            title: "   ",
            message: "确认保存吗?",
            closeImmediateParent: false,
            closeByClickOutside : false
        }).then(function (value) {
                    	
        	var selectUserId = [];
        	var selectUserName = [];
        	
        	if($scope.selectedUsers != null && $scope.selectedUsers.length > 0) {
        		for(var i=0; i<$scope.selectedUsers.length;i++) {
        			selectUserId.push($scope.selectedUsers[i].id);
        			selectUserName.push($scope.selectedUsers[i].name);
            	}	
        	}
        	
        	$params = $.param({
        		"__userKey" : __userKey,
        		"creator": __account,
        		"name" : $scope.group.name,
        		"userId": selectUserId.join(","),
        		"userName": selectUserName.join(",")
        	});
        	
        	$http({
                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
                url: __urlRoot + '/wda/api/wda/admin/group/create',
                method: "POST",
                data: $params
              }).success(function(response) {	
                  
            	  if(response.resultCode == 0) {
            		  //groupId
            		  groupService.getGroups().then(function(response) {
            			  $scope.groups = response.data;
            			  $scope.groupManageJson.data = response.data;
            			  $rootScope.$broadcast('groupList',$scope.groups);
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
    
    $scope.editGroupDialog = function (id) {
    	groupService.getGroupById(id).then(function(response) {
    		$scope.group = response.data;
    		$scope.selectedUsers = response.userData;
    		
    		exDialog.openPrime({
                scope: $scope,
                template: __tplRoot + 'tpl/group_management_add.html',
                controller: 'GroupManagementCtrl',
                width: '600px',
                draggable: false,
                closeByClickOutside: false
                //animation: false,
                //grayBackground: false
            });
    	});
    };
    
    $scope.updateGroup = function (groupId) {
    	
    	exDialog.openConfirm({
            scope: $scope,
            title: "   ",
            message: "确认保存吗?",
            closeImmediateParent: false,
            closeByClickOutside : false
        }).then(function (value) {
            
        	var selectUserId = [];
        	var selectUserName = [];
        	
        	if($scope.selectedUsers != null && $scope.selectedUsers.length > 0) {
        		for(var i=0; i<$scope.selectedUsers.length;i++) {
        			selectUserId.push($scope.selectedUsers[i].id);
        			selectUserName.push($scope.selectedUsers[i].name);
            	}	
        	}
        	
        	$params = $.param({
        		"__userKey" : __userKey,
        		"creator": __account,
        		"name" : $scope.group.name,
        	    "id": groupId,
        	    "userId": selectUserId.join(","),
        		"userName": selectUserName.join(",")
        	});
        	
        	$http({
                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
                url: __urlRoot + '/wda/api/wda/admin/group/update',
                method: "POST",
                data: $params
              }).success(function(response) {	
                  
            	  if(response.resultCode == 0) {
            		  groupService.getGroups().then(function(response) {
            			  $scope.groups = response.data;
            			  $scope.groupManageJson.data = response.data;
            			  $rootScope.$broadcast('groupList',$scope.groups);
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
    
    $scope.delGroupDialog = function (id) {
		
    	exDialog.openConfirm({
    	    scope: $scope,
    	    title: "   ",
    	    icon: "warning",
    	    closeByClickOutside: false,
    	    message: "确认要删除吗?",
    	    closeImmediateParent : false
    	}).then(function (value) {
    		
    		$http({
    	          url: __urlRoot + '/wda/api/wda/admin/group/delete',
    	          method: "POST",
    	          params: { 
    	        	  id: id, 
    	        	  __userKey : __userKey
    	          }
    	        }).success(function(response) {
    	        	if(response.resultCode == 0) {
              		  groupService.getGroups().then(function(response) {
              			  $scope.groups = response.data;
              			  $scope.groupManageJson.data = response.data;
              			  $rootScope.$broadcast('groupsList',$scope.groups);
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