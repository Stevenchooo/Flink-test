define(function(require, exports, module) {
    var MetaBase = require('meta');
    require('dialog');
    require('jquery');

    var MetaUser = {
        
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
	  	  		            method: 'GET',
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
                method: 'GET',
                params:params,
                url: this.webRoot+"/api/user/query"
            };
        },
        
        removePara: function($scope, pid, account) {
            return {
                method: 'GET',
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
                    role:$scope.mod_role,
                    email: $scope.mod_email,
                    phoneNum: $scope.mod_phoneNum,
                    name : $scope.mod_name,
                    department : $scope.mod_department,
                    description : $scope.mod_description,
                    showFlag : $scope.mod_showFlag,
                    departmentType : $scope.mod_departmentType,
                    id : $scope.mod_id,
                }
            };
        },
        
        createDialog:function($scope) {
        	
        	//获取用户列表
        	$scope.callApi({
        		url:this.getWebRoot() + "/api/user/getUserList",
        		method:"GET",
    		}).success(function(resp, status, headers, config){
                if(resp.resultCode != 0) {
    	          	$scope.failToOperate(resp);
                    return;
                }
            	$scope.accountList = resp.result;
		    }).error(function(response, status, headers, config){
		    	$scope.httpError(status);
			});
        	
        	
            $scope.account         = '';
            $scope.crt_role        = '';
            $scope.phoneNum        = '';
            $scope.email           = '';
            $scope.name            = '';
            $scope.department      = '';
        	$scope.description     = '';
        	$scope.showFlag        = '';
			$scope.departmentType = '';
            $scope.userCreateForm.$setPristine();
        	return 'id_create_user_dialog';
        },
        
        modifyDialog:function(account, $scope) {
        	
        	$scope.mod_phoneNum       = '';
        	$scope.mod_account        = '';
        	$scope.mod_role           = '';
        	$scope.mod_email          = '';
        	$scope.mod_name           = '';
        	$scope.mod_department     = '';
        	$scope.mod_description    = '';
        	$scope.mod_showFlag       = '';
        	$scope.mod_departmentType = '';
        	$scope.mod_id             = '';
      
			
        	$scope.callApi({
        		url:this.getWebRoot() + "/api/user/digest",
        		method:"GET",
        		params:{account:account}
    		}).success(function(resp, status, headers, config){
                if(resp.resultCode != 0) {
    	          	$scope.failToOperate(resp);
                    return;
                }
            	$scope.mod_phoneNum       = resp.phoneNum;
            	$scope.mod_account        = resp.account;
            	$scope.mod_role           = resp.role;
            	$scope.mod_email          = resp.email;
            	$scope.mod_name           = resp.name;
            	$scope.mod_department     = resp.department;
            	$scope.mod_description    = resp.description;
            	$scope.mod_showFlag       = resp.showFlag;
            	$scope.mod_departmentType = "" + resp.departmentType;
            	$scope.mod_id             = "" + resp.id;
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
        		dialog({
        			title:getLocalTag('error', 'error'),
        			content:getLocalTag('userAlreadyExists', 'User already exists'),
    	    		okValue: getLocalTag('confirm','Yes'),
        			ok:true
        		}).showModal();
        		return null;
        	}

            return {
                method:"POST",
                url: this.getWebRoot() + "/api/user/create",
                params: {
                	pid:pid,
                	account:account,
                	role:$scope.crt_role,
                	phoneNum:$scope.phoneNum,
                	email:$scope.email,
                	name:$scope.name,
                	department:$scope.department,
                	description:$scope.description,
                	showFlag:$scope.showFlag,
                	departmentType:$scope.departmentType,
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
        	
          	//归属赋值
			$scope.callApi({
				url : this.getWebRoot() + "/api/mktDic/queryDicCommonList",
				method : "GET",
				params : {
					type : "department"
				}
			}).success(function (resp, status, headers, config) {
				if (resp.resultCode != 0) {
					$scope.failToOperate(resp);
					return;
				}
				$scope.mod_departments = resp.results;
				$scope.departments = resp.results;
			}).error(function (response, status, headers, config) {
				$scope.httpError(status);
			});
			
			//获取角色列表 add by sxy 20151209
        	$scope.callApi({
	        	url: this.getWebRoot()+"/api/role/query", 
         		method:"GET",
         		params:{}
     		}).success(function(resp, status, headers, config){
                if(resp.resultCode != 0) {
     	         	$scope.failToOperate(resp);
                    return;
                }
                $scope.userRoles = resp.roles;
                $scope.crt_role  = resp.roles[0].role;
 		    }).error(function(response, status, headers, config){
 		    	$scope.httpError(status);
 			});
			
			
	        $scope.enterQuery = function(ev) {
	        	if (ev.keyCode !== 13) {
	        		return; // your code 
	        	}
	        	$scope.pageClick(0);
        	};
	        
	        $scope.adminResetPassword = function(account) {
                dialog({
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
          	      	      		  type:"GET",
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
          	                          
          	                          dialog({
          	      	       	      	     title: getLocalTag("successToRstPwd", "Success to reset password"),
          	      	       	      	     ok: true,
          	      	       	    	     okValue: getLocalTag('confirm','Yes'),
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
        	      	      		  type:"GET",
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
        	                          
        	                          dialog({
        	      	       	      	     title: getLocalTag("successToUnlock", "Success to unlock"),
        	      	       	      	     ok: true,
        	      	       	    	     okValue: getLocalTag('confirm','Yes'),
        	      	       	      	     content: getLocalTag("successToUnlock", "Success to unlock"),
        	      	       	      	     lock: true,
        	      	       	      	     opacity:0.3
        	      	       	          });	                   
        	      	      		  }
                     		   });

            	          }
       	              }
       	          ]
	       	    }).showModal();
        	};
        }
    };
    
    exports.instance = function(webRoot, metaName, segments) {
    	var obj = MetaBase.instance(webRoot, metaName, segments);
    	$.extend(obj, MetaUser);
    	return obj;
    };

});