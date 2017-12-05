'use strict';

/* Controllers */

angular
		.module('app')
		.controller(
				'AppCtrl',
				[
						'$scope',
						'$translate',
						'$localStorage',
						'$window','$http','$rootScope',
						function($scope, $translate, $localStorage, $window,$http,$rootScope) {
							// add 'ie' classes to html
							var isIE = !!navigator.userAgent.match(/MSIE/i);
							isIE
									&& angular.element($window.document.body)
											.addClass('ie');
							isSmartDevice($window)
									&& angular.element($window.document.body)
											.addClass('smart');

							// config
							$scope.app = {
								name : 'Web Analytics',
								version : '2.0.0',
								// for chart colors
								color : {
									primary : '#7266ba',
									info : '#23b7e5',
									success : '#27c24c',
									warning : '#fad733',
									danger : '#f05050',
									light : '#e8eff0',
									dark : '#3a3f51',
									black : '#1c2b36'
								},
								settings : {
									themeID : 1,
									navbarHeaderColor : 'bg-black',
									navbarCollapseColor : 'bg-white-only',
									asideColor : 'bg-black',
									headerFixed : true,
									asideFixed : false,
									asideFolded : false,
									asideDock : false,
									container : false
								}
							}

							// save settings to local storage
							if (angular.isDefined($localStorage.settings)) {
								$scope.app.settings = $localStorage.settings;
							} else {
								$localStorage.settings = $scope.app.settings;
							}
							$scope.$watch('app.settings', function() {
								if ($scope.app.settings.asideDock
										&& $scope.app.settings.asideFixed) {
									// aside dock and fixed must set the header
									// fixed.
									$scope.app.settings.headerFixed = true;
								}
								// save to local storage
								$localStorage.settings = $scope.app.settings;
							}, true);

							// angular translate
							$scope.lang = {
								isopen : false
							};
							$scope.langs = {
								cn : 'Chinese',
								en : 'English',
								de_DE : 'German',
								it_IT : 'Italian'
							};
							$scope.selectLang = $scope.langs[$translate
									.proposedLanguage()]
									|| "Chinese";
							$scope.setLang = function(langKey, $event) {
								// set the current lang
								$scope.selectLang = $scope.langs[langKey];
								// You can change the language during runtime
								$translate.use(langKey);
								$scope.lang.isopen = !$scope.lang.isopen;
							};

							function isSmartDevice($window) {
								// Adapted from
								// http://www.detectmobilebrowsers.com
								var ua = $window['navigator']['userAgent']
										|| $window['navigator']['vendor']
										|| $window['opera'];
								// Checks for iOs, Android, Blackberry, Opera
								// Mini, and Windows mobile devices
								return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/)
										.test(ua);
							}
							
							$http({
								method: 'GET',
								url: __urlRoot + '/wda/api/wda/home/website/ismanager',
								params: {
									__userKey : __userKey,
									account : __account
								}
							}).success(function(resp, status, headers, config) {
							    if(resp.resultCode == 0) {
							    	$scope.isAdmin = resp.data[0].isAdmin;
							    	$rootScope.risAdmin = $scope.isAdmin;
							    }
							  })
							  .error(function(resp, status, headers, config) {
								  alert("服务器请求错误，请刷新重试");
							  });
							

						} ]);

angular.module('CacheService', [ 'ng' ]).factory('CacheService',
		function($cacheFactory) {
			return $cacheFactory('CacheService');
		});

angular.module('app').factory('HackerNewsService', function(CacheService) {
	return {
		getNews : function(key) {
			var news = CacheService.get(key);

			if (news) {
				return news;
			}

			return null;
		},
		setNews : function(key, value) {
			CacheService.put(key, value);
		},
		clearNews : function(key) {
			CacheService.put(key, '');
		}
	};
});

angular.module('app').filter('formatnumber', function() {
	return function(input) {
		if (input == 0 || input == null)
			return '--';

		var ii = parseFloat(input);
		var s = "";
		if(ii.toString().indexOf(".") >= 0){
			s = parseFloat(input).toFixed(2) + ""; // 数字转换为字符串 .toFixed(2) 因为有的地方input从后台传到前台是字符串，有*.*0格式，所以转换一下
		}else{
			s = parseFloat(input) + ""; // 数字转换为字符串 .toFixed(2) 因为有的地方input从后台传到前台是字符串，有*.*0格式，所以转换一下
		}
		
		return s.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	};
})

.filter(
		'formatSecond',
		function() {
			return function(input) {
				if (input == null || input == 0)
					return '--';
				if(input> 0 && input < 1)
					return input;

				var theTime = parseInt(input);// 秒
				var theTime1 = 0;// 分
				var theTime2 = 0;// 小时
				if (theTime >= 60) {
					theTime1 = parseInt(theTime / 60);
					theTime = parseInt(theTime % 60);
					if (theTime1 >= 60) {
						theTime2 = parseInt(theTime1 / 60);
						theTime1 = parseInt(theTime1 % 60);
					}
				}

				theTime2 = parseInt(theTime1 / 60) < 10 ? '0' + theTime2
						: parseInt(theTime1 / 60);
				theTime1 = parseInt(theTime1 % 60) < 10 ? '0' + theTime1
						: parseInt(theTime1 % 60);
				theTime = parseInt(theTime % 60) < 10 ? '0'+theTime 
						: parseInt(theTime % 60);

				var result = "" + theTime;

				if (theTime1 > 0) {
					result = "" + theTime1 + ":" + result;
				} else {
					result = "00" + ":" + result;
				}

				if (theTime2 > 0) {
					result = "" + theTime2 + ":" + result;
				} else {
					result = "00" + ":" + result;
				}

				return result;
			};
		})

.filter('formatdate', function() {
	return function(d) {
		if (d == null || d == 0)
			return '--';

		var s = d + "";
		return s.replace(/(\d{4})(\d{2})(\d{2})/g, '$1/$2/$3');
	};
})

.filter('formathour', function() {
	return function(h) {
		var strh;

		switch (h) {
		case 0:
			strh = "00:00 - 00:59";
			break;
		case 1:
			strh = "01:00 - 01:59";
			break;
		case 2:
			strh = "02:00 - 02:59";
			break;
		case 3:
			strh = "03:00 - 03:59";
			break;
		case 4:
			strh = "04:00 - 04:59";
			break;
		case 5:
			strh = "05:00 - 05:59";
			break;
		case 6:
			strh = "06:00 - 06:59";
			break;
		case 7:
			strh = "07:00 - 07:59";
			break;
		case 8:
			strh = "08:00 - 08:59";
			break;
		case 9:
			strh = "09:00 - 09:59";
			break;
		case 10:
			strh = "10:00 - 10:59";
			break;
		case 11:
			strh = "11:00 - 11:59";
			break;
		case 12:
			strh = "12:00 - 12:59";
			break;
		case 13:
			strh = "13:00 - 13:59";
			break;
		case 14:
			strh = "14:00 - 14:59";
			break;
		case 15:
			strh = "15:00 - 15:59";
			break;
		case 16:
			strh = "16:00 - 16:59";
			break;
		case 17:
			strh = "17:00 - 17:59";
			break;
		case 18:
			strh = "18:00 - 18:59";
			break;
		case 19:
			strh = "19:00 - 19:59";
			break;
		case 20:
			strh = "20:00 - 20:59";
			break;
		case 21:
			strh = "21:00 - 21:59";
			break;
		case 22:
			strh = "22:00 - 22:59";
			break;
		case 23:
			strh = "23:00 - 23:59";
			break;
		default:
			strh = "--";
		}

		return strh;
	};
})

.filter('getTimePeriodName', function() {
	return function(p) {
		if (p == null)
			return '';

		if (p == 0) {
			return '今天';
		} else if (p == -1) {
			return '昨天';
		} else if (p == -7) {
			return '最近7天';
		} else if (p == -30) {
			return '最近30天';
		}

		return '';
	};
})

.filter('formatDateHour', function() {
	return function(d, h) {
		if (d == null || d == 0)
			return '--';

		var s = d + "";
		var date = s.replace(/(\d{4})(\d{2})(\d{2})/g, '$1-$2-$3');
		if (h == null) {
			return (date + " ");
		} else {
			if (h < 10) {
				return (date + " " + "0" + h + ":00:00");
			}else{
				return (date + " " + h + ":00:00");
			}
		}
		return '--';
	};
})

.filter('truncate', function() {
	return function(value, wordwise, max, tail) {
		if (!value)
			return '';

		max = parseInt(max, 10);
		if (!max)
			return value;
		if (value.length <= max)
			return value;

		value = value.substr(0, max);
		if (wordwise) {
			var lastspace = value.lastIndexOf(' ');
			if (lastspace != -1) {
				value = value.substr(0, lastspace);
			}
		}

		return value + (tail || ' …');
	};
})

.filter('percentage', [ '$filter', function($filter) {
	return function(input, decimals) {
		if (input == 0 || input == null)
			return '--';
		return $filter('number')(input * 100, decimals) + '%';
	};
} ])

angular.module('app').factory('websitesListData', function() {
	return {
		name : {}
	};
});
