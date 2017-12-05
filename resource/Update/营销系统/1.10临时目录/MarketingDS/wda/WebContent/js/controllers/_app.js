

app.controller('_APPCtrl', [
		'$scope',
		'$rootScope',
		'$http',
		'$state',
		'$stateParams',
		'$localStorage',
		function($scope, $rootScope, $http, $state, $stateParams, $localStorage) {
			
			$scope.userKey = __userKey;
			$scope.account = __account;
			$rootScope.headerShow = true;
			$rootScope.blank = "_blank";
			var str = location.href;
			var index = str.indexOf("hideTitle");
			if(-1 != index){
				$rootScope.index = index;
				$rootScope.headerShow = false;
				$rootScope.blank = "";
			}
			if($rootScope.index){
				$rootScope.headerShow = false;
				$rootScope.blank = "";
			}
			
			//sid参数入口
			//$scope.sid = $stateParams.sid;
			$scope.sid = $rootScope.sid || $localStorage.sid;
			
			$http({
				method: 'GET',
				url: __urlRoot + '/wda/api/wda/home/website/list2',
				params: {
					__userKey : __userKey,
					account: __account
				}
			}).success(function(resp, status, headers, config) {
			    if(resp.resultCode == 0) {
			    	$scope.websites = resp.data;
			    }
			    
			}).error(function(resp, status, headers, config) {
//			      alert('error: ' + resp);
				alert("服务器请求错误，请刷新重试");
			});
			
			//网站下拉框列表被选中的站点
			$scope.selected = $scope.sid;
			
			//切换站点
			$scope.locationChangeFunction = function(id) {
				
				$scope.selected = id;
				$scope.sid = id;
				
				$localStorage.sid = id;
				$rootScope.sid = id;
				
				//待删除
				$rootScope.$broadcast('curSid', $scope.selected);
				
				//$state.go("app.website_summary", {sid: id});
				$state.go($state.current, {}, {reload: true});
				//$state.go("app.website_summary", {}, {reload: true});
			};
			
			$scope.logout = function () {
				$http({
		            headers: {'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'},
		            url: __urlRoot + "/wda/api/user/logout?t=" + (new Date().getTime()) + "&__userKey=" + __userKey,
		            method: "POST"
		          }).success(function(response) {
		        	  location.href = __urlRoot + "/wda/page/home";  
		          });
			};
			
			
		} ]);

