'use strict';
angular.module('app').service('localData', [ function() {
	// Local data for product.
	this.getProduct = function() {
		return {
			ProductID : 2,
			ProductName : "Test Equipment 666",
			Category : "Office",
			UnitPrice : 67.89,
			AvailableSince : "04/28/2014",
			ProductStatus : "In Stock"
		}
	};

	this.getGroup = function() {
		return [ {
			id : 10001,
			groupName : '荣耀官网'
		}, {
			id : 10002,
			groupName : '花粉社区'
		}, {
			id : 10003,
			groupName : 'vmall官网'
		} ];
	}
} ]);