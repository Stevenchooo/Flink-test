define(function(require, exports, module) {
    var MetaUser = require('./user');
    require('dialog');
    require('jquery');
    var tree = require('modules/base/tree');

    var UserDetail = MetaUser.extend({
        initialize: function(webRoot, metaName, segments) {
        	UserDetail.superclass.initialize.call(this, webRoot, metaName, segments);
        },
        
        querySubPara: function($scope, params) {
            return {
                method: 'POST',
                params:params,
                url: this.webRoot+"/api/user/detail"
            };
        },
        
        modifyPara: function($scope, account, pid) {
            return {
                method: 'POST', 
                url: this.getWebRoot() + "/api/user/self_modify",
                params: {
                    email: $scope.mod_email,
                    phoneNum: $scope.mod_phoneNum
                }
            };
        },
        
        extendScope: function($scope, $http) {
        	var webRoot = this.getWebRoot();
        	
        	$scope.modifyPwd = function(account) {
        		$scope.account = account;
                $scope.modifyPwdForm.$setPristine();
                
    	        $scope.refreshVerifyCode2 = function() {
    	            $scope.verifyCode2='';
    	            $('#ImageCheck2').attr('src', __webRoot + "/page/verifyCode?t=" + (new Date().getTime().toString(36)));
    	        };
                          
    	        $scope.refreshVerifyCode2();
    	        
	    		$scope.dialog = $.dialog({
	        		title: getLocalTag("modifyPassword", "Modify password"),
	        		content:document.getElementById('id_modify_pwd_dialog'),
	        		cancel:true,
		    		okVal: getLocalTag('confirm','Yes'),
	        		cancelVal: getLocalTag('cancel','cancel'),
	    		    ok: function() {
	    		    	if(!$scope.modifyPwdForm.$valid) {
	    			      	$.dialog({
	    			      		title: "失败",
	    			      		ok: true,
	    			    		okVal: getLocalTag('confirm','Yes'),
	    			      		content: "请检查用户名，密码，验证码是否符合要求"
	    			      	});	
	    		    		
	    		    		return false;
	    		    	}
	    		    		    		    			    		    	
	    		    	$scope.$apply(function() {
	    		    		var req = {
	    		    		    method: 'POST',
		    	    		    url: webRoot + "/api/user/changePassword",
		    	    		    params:{
		    		    		    account:account,
		    		    		    oldPassword:$scope.oldPassword,
		    		    		    password:$scope.newPassword,
		    		    		    confirmPassword:$scope.confirmPassword,
		    		    		    verify:$scope.verifyCode2
		    	    		    }
	    		    		};
		    		    	$scope.callApi(req).success(function(response, status, headers, config){
					          	if(response.resultCode == 0) {
					          		$scope.successToOperate(response);
					          		return;
					          	}
					          	$scope.failToOperate(response, $scope);
					          	
						    }).error(function(response, status, headers, config){						    	
						    	$scope.httpError(status);
						    });
		    		    	
		                    $scope.oldPassword = ''; //用完之后就清除，不能保留
		                    $scope.newPassword = '';
		                    $scope.confirmPassword = '';
		                    $scope.verifyCode2 = '';
	    		    	});
	    		    },
	    		    cancel:function(){
	                    $scope.oldPassword = '';
	                    $scope.newPassword = '';
	                    $scope.confirmPassword = '';
	                    $scope.verifyCode2 = '';
	    		    }
	        	});
            };
        }
    });

    module.exports = UserDetail;
});