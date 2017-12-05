app.controller('HomeCtrl', [ '$rootScope', '$scope','$http', '$state', '$localStorage', function($rootScope, $scope, $http, $state, $localStorage) {
	$scope.app.settings.navbarHeaderColor='bg-orange'; 
	$scope.app.settings.navbarCollapseColor='bg-orange'; 
	$scope.app.settings.asideColor='bg-white';
	
	$scope.userKey = __userKey;
	$scope.account = __account;
	$scope.cookieHeader = __cookieHeader;
	$scope.isAdmin = $rootScope.risAdmin;
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
	
	$http({
		method: 'GET',
		url: __urlRoot + '/wda/api/wda/home/website/list',
		params: {
			__userKey : __userKey
		}
	}).success(function(resp, status, headers, config) {
	    if(resp.resultCode == 0) {
	    	$scope.websitesJson.data = resp.data;
	    }
	  })
	  .error(function(resp, status, headers, config) {
		  alert("服务器请求错误，请刷新重试");
	  });
	
	$scope.logout = function () {
		
		$http({
            headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
            url: __urlRoot + "/wda/api/user/logout?t=" + (new Date().getTime()) + "&__userKey=" + __userKey,
            method: "POST"
          }).success(function(response) {
        	  location.href = __urlRoot + "/wda/page/home";  
          });
	};
	
	$scope.rongyaoLogin = function(){
		window.open( __urlRoot + "/wda/page/rongyao");  
	};
	
	$scope.nav = function(id) {
		$localStorage.sid = id;
		$rootScope.sid = id;
		
		$state.go("app.website_summary", {}, {reload: true});
	};
	
} ]);