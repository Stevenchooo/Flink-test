define(function(require, exports, module) {
    var MetaBase = require('meta');
    require('dialog');
    require('jquery');

    var MetaUser = MetaBase.extend({
        initialize: function(webRoot, metaName, segments) {
        	MetaUser.superclass.initialize.call(this, webRoot, metaName, segments);
        },
        
        directives: function(app) {
        	app.directive('validPassword', ['$http', function($http) {
    	  		return {
    	  			restrict: 'EA',
    	  		    require: '?ngModel',
    	  		    link: function(scope, ele, attrs, ctrl) {
    	  		    	ctrl.$parsers.unshift(function(viewValue) {
    	  		    	var acc = scope[attrs.validPassword];
    	  		        if(!acc || acc.length < 5) {
    	  		        	scope.invalid_password=getLocalTag('accountFirst', "Input valid account first");
    	  		        	ctrl.$setValidity('validPassword', false);
    	  		        	scope[attrs.validPassword]='';
    	  		        	return;
    	  		        }
	  	  		        $http({
	  	  		            method: 'POST',
	  	  		            url: scope.options.webRoot + "/api/user/checkPassword",
	  	  		            params: {
	  	  		        	    account:acc,
	  	  		        	    password:viewValue
	  	  		            }
	  	  		        }).success(function(data, status, headers, cfg) {
	  	  		        	if(data.resultCode != 0) {
	  	  		        		scope.invalid_password=getLocalReason(data.resultCode, "Invalid password");
	  	  		        		ctrl.$setValidity('validPassword', false);
	  	  		        	} else {
	  	  	  		        	scope[attrs.ngModel]=viewValue;
	  	  		        		ctrl.$setValidity('validPassword', true);
	  	  		        	}
	  	  		        }).error(function(data, status, headers, cfg) {
    		        		scope.invalid_password=getLocalReason(data.resultCode, "Invalid password");
    		        		ctrl.$setValidity('validPassword', false);
	  	  		        });
    	  		      });
    	  		    }
    	  		  }
    	  	}]);
        },
        
        querySubPara: function($scope, params) {
        	if($scope.queryAccount) {
        		$.extend(params, {account:$scope.queryAccount});
        	}
            return {
                method: 'POST',
                params:params,
                url: this.webRoot+"/api/user/query"
            };
        },
        
        removePara: function($scope, pid, account) {
            return {
                method: 'POST',
                url: this.webRoot+"/api/user/remove",
                params: {pid:pid, account:account} 
            };
        },
        
        modifyPara: function($scope, account, pid) {
            return {
                method: 'POST', 
                url: this.getWebRoot() + "/api/user/modify",
                params: {
                	pid:pid,
                    account : account,
                    email: $scope.mod_email,
                    phoneNum: $scope.mod_phoneNum
                }
            };
        },
        
        createDialog:function($scope) {
            $scope.account = '';
            $scope.pwd = '';
            $scope.confirmPassword = '';
            $scope.phoneNum = '';
            $scope.email = '';
            $scope.userCreateForm.$setPristine();
        	return 'id_create_user_dialog';
        },
        
        modifyDialog:function(account, $scope) {
        	$scope.callApi({
        		url:this.getWebRoot() + "/api/user/digest",
        		method:"POST",
        		params:{account:account}
    		}).success(function(resp, status, headers, config){
                if(resp.resultCode != 0) {
    	          	$scope.failToOperate(resp);
                    return;
                }
            	$scope.mod_phoneNum = resp.phoneNum;
            	$scope.mod_account = resp.account;
            	$scope.mod_email = resp.email;
		    }).error(function(response, status, headers, config){
		    	$scope.httpError(status);
			});
            return 'id_modify_user_dialog';
        },
        
        createPara: function($scope, pid, meta) {
        	var account = $scope.account;
        	var exists = false;
        	$.ajax({
    		  type:"POST",
    		  url:this.getWebRoot() + "/api/user/exists",
    		  async:false,
    		  data: {
    			  modelId:pid,
    			  account:account
    		  },
    		  success:function(resp){
                 if(resp.resultCode != 0) {
                     return;
                 }
                 exists = resp.exists;
    		  }
      		});
        	
        	if(exists) {
        		$.dialog({
        			title:getLocalTag('error', 'error'),
        			content:getLocalTag('userAlreadyExists', 'User already exists'),
    	    		okVal: getLocalTag('confirm','Yes'),
        			ok:true
        		});
        		return null;
        	}

            return {
                method:"POST",
                url: this.getWebRoot() + "/api/user/create",
                params: {
                	pid:pid,
                	account:account,
                	password:$scope.pwd,
                	confirmPassword:$scope.confirmPassword,
                	phoneNum:$scope.phoneNum,
                	email:$scope.email
                }
            };
        },
        
        createCheck : function($scope) {
            return $scope.userCreateForm.$valid;
        },
        
        modifyCheck : function($scope) {
        	return $scope.userModifyForm.$valid;
        },
        
        extendScope: function($scope, $http) {
        	var webRoot = this.getWebRoot();
        	
	        $scope.enterQuery = function(ev) {
	        	if (ev.keyCode !== 13) {
	        		return; // your code 
	        	}
	        	$scope.pageClick(0);
        	};
	        
	        $scope.adminResetPassword = function(account) {
                $.dialog({
       	      	   title: getLocalTag("tips", "Prompt"),
       	      	   content:getLocalTag("resetOrunLock", "Do you want to reset password or unlock?").format(account),
       	      	   lock: true,
       	      	   opacity:0.3,
       	      	   cancel:true,
       	      	   button: [
       	              {
       	                  name: getLocalTag('resetPwd','resetPwd'),
       	                  callback: function () {
          	       	           $.ajax({
          	      	      		  type:"POST",
          	      	      		  url:webRoot + "/api/user/adminResetPassword",
          	      	      		  data: {
          	      	      			  account:account,
          	      	      			  __userKey:__userKey
          	      	      		  },
          	      	      		  
          	      	      		  success:function(resp){
          	                          if(resp.resultCode != 0) {
          	                      	     $scope.failToOperate(resp);
          	                             return;
          	                          }
          	                          
          	                          $.dialog({
          	      	       	      	     title: getLocalTag("successToRstPwd", "Success to reset password"),
          	      	       	      	     ok: true,
          	      	       	    	     okVal: getLocalTag('confirm','Yes'),
          	      	       	      	     content: getLocalTag("newPasswordIs", "New password is ") + ' ' + resp.password,
          	      	       	      	     lock: true,
          	      	       	      	     opacity:0.3
          	      	       	          });	                   
          	      	      		  }
          	       	           });
       	                  },
       	                  focus: true
       	              },
       	              {
       	                  name: getLocalTag('unLock','unLock'),
       	                  callback: function () {
       	                	$.ajax({
        	      	      		  type:"POST",
        	      	      		  url:webRoot + "/api/user/adminUnlock",
        	      	      		  data: {
        	      	      			  account:account,
        	      	      			  __userKey:__userKey
        	      	      		  },
        	      	      		  
        	      	      		  success:function(resp){
        	                          if(resp.resultCode != 0) {
        	                      	     $scope.failToOperate(resp);
        	                             return;
        	                          }
        	                          
        	                          $.dialog({
        	      	       	      	     title: getLocalTag("successToUnlock", "Success to unlock"),
        	      	       	      	     ok: true,
        	      	       	    	     okVal: getLocalTag('confirm','Yes'),
        	      	       	      	     content: getLocalTag("successToUnlock", "Success to unlock"),
        	      	       	      	     lock: true,
        	      	       	      	     opacity:0.3
        	      	       	          });	                   
        	      	      		  }
                     		   });

            	          }
       	              }
       	          ]
	       	    });
        	};
        }
    });

    module.exports = MetaUser;
});