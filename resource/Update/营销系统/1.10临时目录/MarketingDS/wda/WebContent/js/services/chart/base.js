var BaseService = (function() {
	return {
		getMychart : function(chartId) {
			var myChart = echarts.init(document.getElementById(chartId));
			return myChart;
		}
	};
})();