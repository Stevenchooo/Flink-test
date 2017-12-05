define("modules/base/user", ['jquery', 'dialog', 'jquery/dialog/6.0.4/css/ui-dialog.css'],
	function (require, exports, module) {
	require("jquery");

	exports.logout = function () {
		$.ajax({
			type : "GET",
			url : __webRoot + "/api/user/logout?t=" + (new Date().getTime().toString(36)), //因为使用了httpOnly，
			success : function (res) {
				//location.href = __webRoot + "/page/login";
            	location.href = "https://biportal:2398/BIPortal/logout";
			}
		});
	};

	exports.showDetail = function (root, listId) {
		$.get(root + "/page/userDetail?__userKey=" + __userKey, {}, function (data) {
			$("#" + listId).html(data);
		});
	};

	exports.loginInit = function (opts) {
		var app = angular.module("LoginApp", []);

		app.controller("LoginCtrl", function ($scope, $http) {
			require("dialog");
			require('jquery/dialog/6.0.4/css/ui-dialog.css');

			$scope.options = opts;

			$scope.refreshVerifyCode = function () {
				$scope.verifyCode = '';
				$('#ImageCheck').attr('src', __webRoot + "/page/verifyCode?t=" + (new Date().getTime().toString(36)));
			};

			// 找回密码
			$scope.mail = function () {

				$scope.showMailForm(
					getLocalTag("findPassword", "找回密码"));

			};

			$scope.refreshVerifyCode();

			$scope.login = function () {
				if (!$scope.loginForm.$valid) {
					$.dialog({
						title : "失败",
						ok : true,
						okVal : getLocalTag('confirm', 'Yes'),
						content : "请检查用户名，密码，验证码是否符合要求"
					});

					return;
				}

				$.ajax({
					type : "POST",
					url : __webRoot + "/api/user/login",
					dataType : "json",
					data : {
						"account" : $scope.account,
						"password" : $scope.pwd,
						"verify" : $scope.verifyCode,
						"lang" : $scope.lang
					},

					success : function (res) 
					{
						if (res.resultCode != 0) 
						{
							$("#loginInfo").html(getLocalReason(res.resultCode, res.info));
							$scope.$apply(function ()
						    {
								$scope.refreshVerifyCode();
							});
							return;
						} else 
						{
							$("#loginInfo").html('');
							$scope.showIndex();
						}

					}
				});
			};



			$scope.showMessage = function (title, msg) {
				$.dialog({
					title : title,
					ok : true,
					okVal : getLocalTag('confirm', 'Yes'),
					cancelVal : getLocalTag('cancel', 'cancel'),
					content : msg
				});
			};


			$scope.showIndex = function (passport, userKey) {
				window.location.href = __webRoot + "/page/index";
			};

			$scope.changeLanguage = function () {
				var _path = __webRoot == '' ? '/' : __webRoot;
				window.location.href = __webRoot + "/page/login";
			};
		});

		angular.element(document).ready(function () {
			//判断密码是否符合安全规范要求
			app.directive('validPassword', ['$http', function ($http) {
						return {
							restrict : 'EA',
							require : '?ngModel',
							link : function (scope, ele, attrs, ctrl) {
								ctrl.$parsers.unshift(function (viewValue) {
									var acc = scope[attrs.validPassword];
									if (!acc || acc.length < 5) {
										scope.invalid_password = getLocalTag('accountFirst', "Input valid account first");
										ctrl.$setValidity('validPassword', false);
										return;
									}
									$http({
										method : 'GET',
										url : __webRoot + "/api/user/checkPassword",
										params : {
											account : acc,
											password : viewValue
										}
									}).success(function (data, status, headers, cfg) {
										if (data.resultCode != 0) {
											scope.invalid_password = getLocalReason(data.resultCode, "Invalid password");
											ctrl.$setValidity('validPassword', false);
										} else {
											ctrl.$setValidity('validPassword', true);
											scope[attrs.ngModel] = viewValue;
										}
									}).error(function (data, status, headers, cfg) {
										scope.invalid_password = getLocalReason(data.resultCode, "Invalid password");
										ctrl.$setValidity('validPassword', false);
									});
								});
							}
						}
					}
				]);
			angular.bootstrap(document, ['LoginApp']);
			$("#id_account").focus();
		});
	};
});