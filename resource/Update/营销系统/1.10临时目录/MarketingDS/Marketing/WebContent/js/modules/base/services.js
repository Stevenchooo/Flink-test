define("modules/base/services",function(require, exports, module) {
	
	var callbacks = {};
	
	exports.initServices = function(app) {

		app.config(['$httpProvider', function($httpProvider) {
			$httpProvider.defaults.headers.post['X-Requested-With'] = 'XMLHttpRequest'; 
		}]);
		
	    app.service('LoadDataService', function($http, $q) {
			this.loadData = function(reqUrl, params) {
				//自动增加临时key，用于防止js跨站攻击
				if(params) {
					params['__userKey'] = __userKey;
				} 
				else {
					params = {"__userKey":__userKey};
				}
				
				var deferred = $q.defer();
				$http({
					method: 'POST',
					dataType:"json",
					url: __webRoot + reqUrl,
					data: params,
				}).success(function(data){
					deferred.resolve(data);
				}).error(function(data, status, headers, config){
					if (status == 403) {
						window.location.reload();
						return;
					}
					
					deferred.reject(status);
				});
				
				return deferred.promise;
			}
		});
	    
	    app.service('NotifyService', function() {
	    	
	    	this.publish = function(id, data) {
	    		if (callbacks[id]) {
	    			var c = callbacks[id];
	    			c(data);
	    		}
	    	}
	    	
	    	this.subscribe = function(id, callback) {
	    		callbacks[id] = callback;
	    	}
	    });
	    
		app.service('FilterQueryService', function($http, $q){
			var _startDate = '';
			var _endDate = '';
			var _emuiVer = '';
			var _appVer = '';
			var _product = '';
			var _device = '';
	
			var makeUrl = function(url){
				return __webRoot + url;
			}
			
			this.setStartTime = function(startDate) {
				_startDate = startDate;
			}
	
			this.setEndTime = function(endDate){
				_endDate = endDate;
			}
			
			this.setEmuiVer = function(emuiVer) {
				_emuiVer = emuiVer;
			}
			
			this.setAppVer = function(appVer) {
				_appVer = appVer;
			}
			
			this.setProduct = function(product) {
				_product = product;
			}
			
			this.setDevice = function(device) {
				_device = device;
			}
			
			this.createParams = function() {
				var params = {};
				
				if (checkParams(_startDate)) {
					params['begin_date'] = _startDate;
				}
				
				if (checkParams(_endDate)) {
					params['end_date'] = _endDate;
				}
				
				if (checkParams(_emuiVer)) {
					params['eui_ver'] = _emuiVer;
				}
				
				if (checkParams(_appVer)) {
					params['app_ver'] = _appVer;
				}
				
				if (checkParams(_product)) {
					params['product'] = _product;
				}
				
				if (checkParams(_device)) {
					_device = _device.replace(/(^\s*)|(\s*$)/g, "")
					params['device'] = _device.split(',');
				}
				
				return params;
			}
			
			var checkParams = function(value) {
				if (typeof(value) == 'undefined' || value == null || value == ''){
					return false;
				}
				
				return true;
			}
		});

	};
//end of define
});