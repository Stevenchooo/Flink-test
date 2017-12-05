angular.module('app').filter("pageFilter", function() {
	return function(input, curpage, pageSize, searchtxt) {
		if (!input.data) {
			return [];
		}

		var array = [];
		if (searchtxt != input.lastsearchtxt) {
			input.curpage = 1;
		}

		input.lastsearchtxt = searchtxt;
		if (searchtxt) {
			for (var i = 0; i < input.data.length; i++) {
				var obj = input.data[i];
				if (obj.site_url.indexOf(searchtxt) > -1) {
					array.push(obj);
				}
			}
		} else {
			array = input.data;
		}

		var array2 = util(input, curpage, pageSize, array);
		return array2;
	};
})

.filter("pageSummarypageFilter", function() {
	return function(input, curpage, pageSize, searchtxt) {
		if (!input.data) {
			return [];
		}

		var array = [];
		if (searchtxt != input.lastsearchtxt) {
			input.curpage = 1;
		}

		input.lastsearchtxt = searchtxt;
		if (searchtxt) {
			for (var i = 0; i < input.data.length; i++) {
				var obj = input.data[i];
				if (obj.page_url.indexOf(searchtxt) > -1) {
					array.push(obj);
				}
			}
		} else {
			array = input.data;
		}

		var array2 = util(input, curpage, pageSize, array);
		return array2;
	};
})

.filter("areaDistributionListFilter", function() {
	return function(input, curpage, pageSize, searchtxt) {
		if (!input.data) {
			return [];
		}

		var array = [];
		if (searchtxt != input.lastsearchtxt) {
			input.curpage = 1;
		}

		input.lastsearchtxt = searchtxt;
		if (searchtxt) {
			for (var i = 0; i < input.data.length; i++) {
				var obj = input.data[i];
				if (obj.province_name.indexOf(searchtxt) > -1) {
					array.push(obj);
				}
			}
		} else {
			array = input.data;
		}

		var array2 = util(input, curpage, pageSize, array);
		return array2;
	};

})

.filter("groupManageFilter", function() {
	return function(input, curpage, pageSize, searchtxt) {
		if (!input.data) {
			return [];
		}

		var array = [];
		if (searchtxt != input.lastsearchtxt) {
			input.curpage = 1;
		}

		var isSer = false;
		if (searchtxt && searchtxt.length > 0) {
			isSer = true;
		}
		input.lastsearchtxt = searchtxt;
		if (searchtxt) {
			for (var i = 0; i < input.data.length; i++) {
				var obj = input.data[i];
				if (obj.name.indexOf(searchtxt) > -1) {
					array.push(obj);
				}
			}
		} else {
			array = input.data;
		}

		var array2 = util(input, curpage, pageSize, array, true, isSer);
		return array2;
	};

})

.filter("userManageFilter", function() {
	return function(input, curpage, pageSize, searchtxt) {
		if (!input.data) {
			return [];
		}

		var array = [];
		if (searchtxt != input.lastsearchtxt) {
			input.curpage = 1;
		}

		var isSer = false;
		if (searchtxt && searchtxt.length > 0) {
			isSer = true;
		}
		input.lastsearchtxt = searchtxt;
		if (searchtxt) {
			for (var i = 0; i < input.data.length; i++) {
				var obj = input.data[i];
				if (obj.account.indexOf(searchtxt) > -1) {
					array.push(obj);
				}
			}
		} else {
			array = input.data;
		}

		var array2 = util(input, curpage, pageSize, array, true, isSer);
		return array2;
	};
})

.filter("roleManageFilter", function() {
	return function(input, curpage, pageSize, searchtxt) {
		if (!input.data) {
			return [];
		}

		var array = [];
		if (searchtxt != input.lastsearchtxt) {
			input.curpage = 1;
		}

		input.lastsearchtxt = searchtxt;
		if (searchtxt) {
			for (var i = 0; i < input.data.length; i++) {
				var obj = input.data[i];
				if (obj.name.indexOf(searchtxt) > -1) {
					array.push(obj);
				}
			}
		} else {
			array = input.data;
		}

		var array2 = util(input, curpage, pageSize, array);
		return array2;
	};

})

.filter("todayPageFilter", function() {
	return function(input, curpage, pageSize, searchtxt) {
		if (!input.data) {
			return [];
		}

		var array = input.data;

		var array2 = util(input, curpage, pageSize, array);
		return array2;
	};
})

.filter("websiteManageFilter", function() {
	return function(input, curpage, pageSize, searchtxt) {
		if (!input.data) {
			return [];
		}

		var array = [];
		if (searchtxt != input.lastsearchtxt) {
			input.curpage = 1;
		}

		var isSer = false;
		if (searchtxt && searchtxt.length > 0) {
			isSer = true;
		}
		input.lastsearchtxt = searchtxt;
		if (searchtxt) {
			for (var i = 0; i < input.data.length; i++) {
				var obj = input.data[i];
				if (obj.site_id.indexOf(searchtxt) > -1) {
					array.push(obj);
				}
			}
		} else {
			array = input.data;
		}

		var array2 = util(input, curpage, pageSize, array, true, isSer);
		return array2;
	};
})

.filter("yesterdayPageFilter", function() {
	return function(input, curpage, pageSize, searchtxt) {
		if (!input.data) {
			return [];
		}

		var array = [];
		if (searchtxt != input.lastsearchtxt) {
			input.curpage = 1;
		}

		input.lastsearchtxt = searchtxt;
		if (searchtxt) {
			for (var i = 0; i < input.data.length; i++) {
				var obj = input.data[i];
				if (obj.site_url.indexOf(searchtxt) > -1) {
					array.push(obj);
				}
			}
		} else {
			array = input.data;
		}

		var array2 = util(input, curpage, pageSize, array);
		return array2;
	};
});

var util = function(input, curpage, pageSize, array, isBack, isSer) {
	var array2 = [];
	var startindex = (curpage - 1) * pageSize;
	var endindex = curpage * pageSize - 1;

	for (var j = startindex; j <= endindex; j++) {
		if (j < array.length) {
			var obj = array[j];
			array2.push(obj);
		}
	}

	if (isBack && !isSer && (array2 == null || array2.length <= 0)) {
		curpage = curpage - 1;
		for (var j = startindex - pageSize; j < startindex; j++) {
			if (j < array.length) {
				var obj = array[j];
				array2.push(obj);
			}
		}
		input.curpage = curpage;
	}

	var lastrecodenum = array.length % pageSize;
	var page = parseInt(array.length / pageSize);
	var pages = 0;
	if (lastrecodenum == 0) {
		pages = page;
	} else {
		pages = page + 1;
	}
	input.totalPage = 0;
	input.showPage = [];

	if (curpage <= 2) {
		if (pages >= 5) {
			for (var p = 1; p <= 5; p++) {
				input.showPage.push(p);
			}
		} else {
			for (var p = 1; p <= pages; p++) {
				input.showPage.push(p);
			}
		}
	}
	if (curpage >= 3) {
		var endpage = curpage + 2;
		if (endpage <= pages) {
			for (var p = curpage - 2; p <= curpage; p++) {
				input.showPage.push(p);
			}
			for (var p = curpage + 1; p <= endpage; p++) {
				input.showPage.push(p);
			}
		} else {
			for (var p = pages - 4; p <= pages; p++) {
				if (p > 0) {
					input.showPage.push(p);
				}
			}
		}
	}

	input.totalPage = pages;

	return array2;
};