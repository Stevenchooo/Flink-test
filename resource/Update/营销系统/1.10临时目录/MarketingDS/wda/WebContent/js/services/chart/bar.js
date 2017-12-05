angular.module('app').factory('bar', function() {
	var BarService = Object.create(BaseService);
	BarService.createChart = function(id, barData) {
		var myChart = this.getMychart(id);

		setTimeout(function() {
			window.onresize = function() {
				myChart.resize();
			}
		}, 200);

		var xBar = [];
		var yBar = [];
		if (barData.values.length <= 0) {
			myChart.clear();
			myChart.showLoading({
				text : "暂无数据",
				y : 300,
			});
			return;
		}
		for ( var i in barData.values) {
			xBar.push(barData.values[i].label);
			var barValue = barData.values[i].value * 100;
			if (barValue < 0.0001) {
				barValue = barValue.toFixed(6);
			} else if (barValue < 0.01) {
				barValue = barValue.toFixed(4);
			} else {
				barValue = barValue.toFixed(2)
			}
			yBar.push(barValue);
		}
		var option = {
			tooltip : {
				trigger : 'axis',
				formatter : function(params) {
					return params[0].seriesName + '</br>' + params[0].name + "：" + params[0].value + '%';
				}
			},
			xAxis : {
				data : xBar,
				name : '省份'
			},
			yAxis : {
				type : 'value',
				axisLabel : {
					show : true,
					interval : 'auto',
					formatter : '{value}%'
				},
				name : '占比'
			},
			series : [ {
				name : barData.key,
				type : 'bar',
				data : yBar
			} ]
		};
		myChart.setOption(option);

		return myChart;
	};
	return BarService;
});
