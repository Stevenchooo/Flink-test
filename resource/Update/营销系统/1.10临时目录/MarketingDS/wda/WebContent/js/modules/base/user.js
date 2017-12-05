define("modules/base/user", ['jquery', 'cookie', 'dialog', 'jquery/dialog/4.1.7/skins/default.css'], 
function(require, exports, module) {
    var $ = require("jquery");
    var cookie = require("cookie");
    
    exports.logout = function() {
    	$.ajax({
    		type:"POST",
    		url:__webRoot+"/api/user/logout?t=" + (new Date().getTime()) + "&__userKey=" + __userKey, //因为使用了httpOnly，客户端无法删除cookie
        	success: function(res) {
                location.href = __webRoot + "/page/login";
        	}
    	});
    };
    
    exports.showDetail = function(root, listId) {
	    $.get(root + "/page/userDetail?__userKey=" + __userKey, {}, function(data){
	        $("#" + listId).html(data);
	    });
    };
    
    exports.loginInit = function(opts) {
	    var app = angular.module("LoginApp", []);
	    
	    
	    app.controller("LoginCtrl", function($scope, $http) {
            require("dialog");
            require('jquery/dialog/4.1.7/skins/default.css');
            
            $scope.options = opts;
            var lang = cookie.get(__cookieHeader + 'lang');
            if(lang == null || lang == "") {
            	var l = navigator.language ? navigator.language : navigator.browserLanguage;
            	if(l.indexOf('zh') >= 0) {
            		lang = 'zh_CN';
            	} else {
            		lang = 'en';
            	}
            }
            $scope.lang = lang;
            
	        $scope.refreshVerifyCode = function() {
	            $scope.verifyCode='';
	            var imgId = 'ImageCheck';
	            if(arguments.length > 0) {
	            	imgId = arguments[0];
	            }
	            $('#' + imgId).attr('src', __webRoot + "/page/verifyCode?t=" + (new Date().getTime().toString(36)));
	        };
	        
	        // 找回密码
	        $scope.mail = function() {
	        	$scope.showMailForm(
          			getLocalTag("findPassword", "找回密码")
          		);
	        };
        
	        // 进入登录界面，刷新
	        $scope.refreshVerifyCode();
	        
	        $scope.login = function() {
		    	if(!$scope.loginForm.$valid) {
			      	$.dialog({
			      		title: "失败",
			      		ok: true,
			    		okVal: getLocalTag('confirm','Yes'),
			      		content: "请检查用户名，密码，验证码是否符合要求"
			      	});		    		
		    		
		    		return;
		    	}
    	
		    	$.ajax({
	            	type:"POST",
	            	url:__webRoot+"/api/user/login", 
	                dataType:"json",
	                data: {
	                    "account":$scope.account,
	                    "password":$scope.pwd,
	                    "verify":$scope.verifyCode,
	                    "lang":$scope.lang
	                },
	                
	                success: function(res){
	                    if(res.resultCode != 0) {
                    		$("#loginInfo").html(getLocalReason(res.resultCode, res.info));
    	    		    	$scope.$apply(function() {
    	    		    		// 失败的情况下刷新
    	    		    		$scope.refreshVerifyCode();
    	    		    	});
	                        return;
	                    } else {
                    		$("#loginInfo").html('抱歉，您无权限访问页面');
	                    }
	                   
	                    document.getElementById("loginPwd").value = "";
	                    if(res.expiredDays < -3) { //密码超期的天数
	                    	$scope.showIndex(res.passport, res.__userKey);
	                    	return;
	                    }
	                    
	                    if(res.expiredDays <= 0) { //密码即将超期提醒，最后3天只提醒
		                    $.dialog({
		                        title: getLocalTag("prompt", "Prompt"),
		                        content: getLocalTag("passwordExpiring", "Password will be expired in {0} days").format(1-res.expiredDays),
		        	    		okVal: getLocalTag('confirm','Yes'),
		                		cancelVal: getLocalTag('cancel','cancel'),
		                        ok: function(){
			                    	$scope.showIndex(res.passport, res.__userKey);
		                        }
		                    });
	                    	return;
	                    }
	                    
	                    $scope.userKey = res.__userKey;
	                    
	                    var title = res.expiredDays >= 10000 ?
	                    	getLocalTag("loginFirstTime", "Login first time")
	                    	:getLocalTag("passwordExpired", "Password expired {0} day(s)").format(res.expiredDays);
	                    
	                    // 在弹出强制修改窗口之前，刷新验证码
                       	$scope.refreshVerifyCode('ModifyImageCheck');
		    		    
	                    //超期了，则强制修改密码
	                    $.dialog({	  	   
	                        title: title,
        	        		content: document.getElementById('id_modify_pwd_dialog'),
        	        		lock:true,
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
	    	    		    	
	    	    		    	var is_ok = false;
	    	    		    	$scope.$apply(function() {
	    	    		    		is_ok = $scope.changePwd();
	    	    		    		if(is_ok) {
	    	    		    			$scope.pwd = '';
										// 刷新登录界面验证码
										$scope.refreshVerifyCode();	    	    		    		    	    		    			    	    		    		
	    	    		    		}
									else
									{
										// 刷新本界面验证码
										$scope.refreshVerifyCode('ModifyImageCheck');	 										
									}
    	    		    			
	    	    		    	});
	                        	
	                            return is_ok;
	                        }
	                    });
	                }
	            });
	        };
        
	        $scope.changePwd = function() {
		    	if(!$scope.modifyPwdForm.$valid) {
		    		return false;
		    	}

	        	var is_ok = false;
	    		var req = {
	    		    type: 'POST',
	    		    async: false, //同步，需要等待执行结果
	    		    url: __webRoot + "/api/user/changePassword",
	    		    data:{
	    		    	__userKey:$scope.userKey,
		    		    account:$scope.account,
		    		    oldPassword:$scope.oldPassword,
		    		    password:$scope.newPassword,
		    		    confirmPassword:$scope.confirmPassword,
		    		    verify:$scope.verifyCode1
	    		    },
	    		    success: function(response){
			          	if(response.resultCode == 0) {
			          		$scope.showMessage(
			          			getLocalTag("successToOperate", "Success to operate"),
			          			getLocalTag("passwordChanged", "Password changed, please relogin!")
			          		);
			          		is_ok = true;
			          		return;
			          	}
			          	else
			          	{
   	    		    		$scope.refreshVerifyCode();
   	    		    		$scope.verifyCode1 = '';
			          	}
			          	
		          		$scope.showMessage(
		          			getLocalTag("failToOperate", "Fail to operate"),
		          			getLocalReason(response.resultCode, response.info)
		          		);
				    }
	    		};
	    		
	    		$.ajax(req);
	    		
	    		return is_ok;
	        };
	        
	    	$scope.showMessage=function(title, msg) {
		      	$.dialog({
		      		title: title,
		      		ok: true,
		    		okVal: getLocalTag('confirm','Yes'),
	        		cancelVal: getLocalTag('cancel','cancel'),
		      		content: msg
		      	});
	    	};
	    	
	    	// 显示获得密码的界面
	    	$scope.showMailForm=function(title) {
		      	$.dialog({
		      		title: title,
		      		content: document.getElementById('id_get_pwd_by_mail'),
		      		ok: true,
		    		okVal: getLocalTag('confirm','Yes'),
		    		
		    		ok: function() {
   			
		    			var is_ok = false;
			    		var req = {
			    		    type: 'POST',
			    		    async: false, //同步，需要等待执行结果
			    		    url: __webRoot + "/api/user/getPwdByMail",
			    		    data:{
				    		    account:$scope.youraccount
			    		    },
			    		    success: function(response){			    		    	
					          	if(response.resultCode == 0) {
					          		$scope.showMessage(
					          			"找回密码", "重置密码链接已发送到注册邮箱，请查看"
					          		);
					          		is_ok = true;
					          		return;
					          	}
					          	
				          		$scope.showMessage(
				          			getLocalTag("failToOperate", "Fail to operate"),
				          			getLocalReason(response.resultCode, response.info)
				          		);
						    }
			    		};
			    		
			    		$.ajax(req);
			    		
			    		return is_ok;		    			
		    		}
		      	});
	    	};
	    	
	    	
	        $scope.showIndex = function(passport, userKey) {
	        	window.location.href = __webRoot + "/page";
	        	//window.location.href = __webRoot + "/index.html";
	        };
	        
	        $scope.changeLanguage = function() {
	        	var _path = __webRoot == '' ? '/' : __webRoot;
	        	cookie.set(__cookieHeader + "lang", $scope.lang, {path:_path, expires:100, secure:false});
	        	window.location.href = __webRoot + "/page/login";
	        };
	    });
	    
    	
	    angular.element(document).ready(function () {
	    	//判断密码是否符合安全规范要求
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
		  		        	return;
		  		        }
	  	  		        $http({
	  	  		            method: 'POST',
	  	  		            url: __webRoot + "/api/user/checkPassword",
	  	  		            params: {
	  	  		        	    account:acc,
	  	  		        	    password:viewValue
	  	  		            }
	  	  		        }).success(function(data, status, headers, cfg) {
	  	  		        	if(data.resultCode != 0) {
	  	  		        		scope.invalid_password=getLocalReason(data.resultCode, "Invalid password");
	  	  		        		ctrl.$setValidity('validPassword', false);
	  	  		        	} else {
	  	  		        		ctrl.$setValidity('validPassword', true);
		  	  		        	scope[attrs.ngModel]=viewValue;
	  	  		        	}
	  	  		        }).error(function(data, status, headers, cfg) {
			        		scope.invalid_password=getLocalReason(data.resultCode, "Invalid password");
			        		ctrl.$setValidity('validPassword', false);
	  	  		        });
		  		      });
		  		    }
		  		  }
		  	}]);
		    angular.bootstrap(document, ['LoginApp']);
		    $("#id_account").focus();
	    });
    };
});