
app.factory('websiteService', ['$http', '$rootScope', function($http, $rootScope) {
	
	var website = {};
	var websites = [];
	var groups = [];
  
  return {
	
	getWebsites: function() {
		return $http.get(__urlRoot + '/wda/api/wda/admin/website/list').then(function(response) {
			websites = response.data;
	        return websites;
	    })
	},
	    
	getGroups: function() {
		return $http.get(__urlRoot + '/wda/api/wda/admin/group/select').then(function(response) {
			groups = response.data;
	        return groups;
		})
	},
	
    getWebsiteById : function(id) {
      return $http.get(__urlRoot + '/wda/api/wda/admin/website/queryById', {
    	  params: {
    		  "__userKey" : __userKey,
    		  id: id
    	  }
      }).then(function(response) {
    	  website = response.data;
          return website;
        })
    }
  };
}]);


app.controller('WebsiteManagementCtrl', 
		['$rootScope', '$scope', '$http', '$timeout', 'exDialog', 'websiteService', '$state',
		 function($rootScope, $scope, $http, $timeout, exDialog, websiteService, $state) {
	
	if($rootScope.risAdmin == 0) {
		$state.go('noright', {});
	}
	
	$scope.websitesJson={
			curpage : 1,
			itemcss : "un-item uni-cur",
			lastsearchtxt : '',
	};
	
	$scope.setCurPage = function(page) {
		if (page == $scope.websitesJson.curpage) {
			return "pgcur";
		} else {
			return "";
		}
	};
	$scope.selectPage = function(page) {
		if (page <= $scope.websitesJson.totalPage && page >= 1) {
			$scope.websitesJson.curpage = page;
		}
	};
			
	websiteService.getWebsites().then(function(websites) {
		$scope.websitesJson.data = websites.data;
	    $scope.websites = websites.data;
	});
	
	$scope.$on('websiteList', function(events, values) {
		$scope.websitesJson.data = values;
		$scope.websites = values;
	});

	websiteService.getGroups().then(function(response) {
	    $scope.groups = response.data;
	});
	
	$scope.addWebsiteDialog = function () {
		
		$scope.selected = 0;
		$scope.website = null;
        
		exDialog.openPrime({
            scope: $scope,
            template: __tplRoot + 'tpl/website_management_add.html',
            controller: 'WebsiteManagementCtrl',
            width: '600px',
            draggable: false,
            closeByClickOutside: false
            //animation: false,
            //grayBackground: false
        });
    };
    
    $scope.saveWebsite = function(websiteId) {
    	if(websiteId) {
    		$scope.updateWebsite(websiteId);
    	} else {
    		$scope.addWebsite();
    	}
    };
    
    $scope.addWebsite = function () {
    	
    	exDialog.openConfirm({
            scope: $scope,
            title: "   ",
            message: "确认保存吗?",
            closeImmediateParent: false,
            closeByClickOutside : false
        }).then(function (value) {
            
        	$params = $.param({
        		"__userKey" : __userKey,
        		"creator": __account,
        		"site_id": $scope.website.site_id,
        	    "site_name": $scope.website.site_name,
        	    "site_url": $scope.website.site_url,
        	    "group_id": $scope.selected
        	});
        	
        	$http({
                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
                url: __urlRoot + '/wda/api/wda/admin/website/create',
                method: "POST",
                data: $params
              }).success(function(response) {	
            	  if(response.resultCode == 0) {
            		  
            		  websiteService.getWebsites().then(function(response) {
            			  $scope.websitesJson.data = response.data;
            			  $scope.websites = response.data;
            			  $rootScope.$broadcast('websiteList',$scope.websites);
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
    
    $scope.editWebsiteDialog = function (id) {
		
    	websiteService.getWebsiteById(id).then(function(response) {
    		$scope.website = response.data;
    		$scope.selected = $scope.website.group_id;
    		
    		exDialog.openPrime({
                scope: $scope,
                template: __tplRoot + 'tpl/website_management_add.html',
                controller: 'WebsiteManagementCtrl',
                width: '600px',
                draggable: false,
                closeByClickOutside: false
                //animation: false,
                //grayBackground: false
            });
    	});
    };
    
    $scope.updateWebsite = function (id) {
    	
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
        		"id" : id,
        		"site_id": $scope.website.site_id,
        	    "site_name": $scope.website.site_name,
        	    "site_url": $scope.website.site_url,
        	    "group_id": $scope.selected
        	});
        	
        	$http({
                headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
                url: __urlRoot + '/wda/api/wda/admin/website/update',
                method: "POST",
                data: $params
              }).success(function(response) {	
                  
            	  if(response.resultCode == 0) {
            		  websiteService.getWebsites().then(function(response) {
            			  $scope.websitesJson.data = response.data;
            			  $scope.websites = response.data;
            			  $rootScope.$broadcast('websiteList',$scope.websites);
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
    
    $scope.delWebsiteDialog = function (id) {
		
    	exDialog.openConfirm({
    	    scope: $scope,
    	    title: "   ",
    	    icon: "warning",
    	    closeByClickOutside: false,
    	    message: "确认要删除吗?",
    	    closeImmediateParent :false
    	}).then(function (value) {
    		
    		$http({
    	          url: __urlRoot + '/wda/api/wda/admin/website/delete',
    	          method: "POST",
    	          params: { 
    	        	  id: id, 
    	        	  __userKey : __userKey
    	          }
    	        }).success(function(response) {
    	        	if(response.resultCode == 0) {
              		  websiteService.getWebsites().then(function(response) {
              			$scope.websitesJson.data = response.data;
              			  $scope.websites = response.data;
              			  $rootScope.$broadcast('websiteList',$scope.websites);
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