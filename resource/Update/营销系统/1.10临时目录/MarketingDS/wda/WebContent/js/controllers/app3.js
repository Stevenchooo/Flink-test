
app.controller('BackAdminCtrl', [ '$scope', '$http',  function($scope, $http) {
	$scope.userKey = __userKey;
	$scope.account = __account;
	
	$scope.logout = function () {
		$http({
            headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
            url: __urlRoot + "/wda/api/user/logout?t=" + (new Date().getTime()) + "&__userKey=" + __userKey, //因为使用了httpOnly，客户端无法删除cookie
            method: "POST"
          }).success(function(response) {	
        	  location.href = "/wda/page/home";  
       });
	};
	
} ]);
