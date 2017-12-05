/*
 * provide JavaScript Tools Function
 */

/*
 * 使用JQuery来进行ajax请求
 */
function ajaxJquery(path) {
	$.ajaxSetup({
		async : true
	});
	$.ajaxSetup({
		type : 'POST'
	});
	$.ajaxSetup({
		dataType : 'json'
	});
	$.ajaxSetup({
		contentType : 'application/json'
	});
	function Domain(name, published) {
		this.name = name;
		this.published = published;
	}
	var domain = new Domain("杰克", "true");
	var domain_json = JSON.stringify(domain);
	var ajax_result;
	$.ajax({
		url : path + '/res/ajaxJquery/domain/queryDomains',
		data : domain_json,
		success : function(result) {
			// window.alert(JSON.stringify(result));
			// window.alert("domain id:"+result[0].id);
			showJsonResult(result);
		}
	});
	function showJsonResult(result) {
		var jsonObj = JSON.parse(JSON.stringify(result));
		// window.alert("id:"+jsonObj[0].id);
		// window.alert("name:"+jsonObj[0].name);
		// window.alert("published:"+jsonObj[0].published);
	}
}

function ajaxAngularJS() {
	function Domain(name, published) {
		this.name = name;
		this.published = published;
	}
	var domain = new Domain("杰克", "true");
	var domain_json = JSON.stringify(domain);
	var app = angular.module("domain_manager_app", []);
	app.controller("myNoteCtrl", function($scope, $http) {
		$http({
			method : "POST",
			url : path + "/res/angularjs/domain/queryDomains",
			params : {
				"userName" : "中国",
				"password" : "张三"
			},
			data : domain_json
		}).success(function(data, status, headers, config) {
			var jsonObj = JSON.parse(JSON.stringify(data));
			var message = jsonObj[0].name;
		}).error(function(data, status, headers, config) {
		});
	});
}
