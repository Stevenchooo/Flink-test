angular.module('app').factory(
		'line',
		function() {
			var LineService = Object.create(BaseService);
			LineService.createChart = function(id, lineData) {
				var myChart = this.getMychart(id);
				
				setTimeout(function() {
					window.onresize = function() {
						myChart.resize();
					}
				}, 200);
				
				var xDateValue = [];
				var yDateValue = [];
				var legendData = [];
				var nnData = lineData.data;
				for ( var i in nnData.values) {
					xDateValue.push(nnData.values[i].x);
					yDateValue.push(nnData.values[i].y);
				}
				var bbDateValue = [];
				var bbkey = '';
				if (lineData.hb_data && lineData.hb_data.values) {
					var bbData = lineData.hb_data.values;
					bbkey = lineData.hb_data.key;
					for ( var i in bbData) {
						bbDateValue.push(bbData[i].y);
					}
				}
				legendData.push(nnData.key);
				if (bbkey.length > 0) {
					legendData.push(bbkey + " ");
				}

				// var xName = timeDim[$scope.time_dim_value];
				// 指定图表的配置项和数据
				var option = {
					tooltip : {
						trigger : 'axis',
						formatter : function(params) {
							var data0 = params[0].data.toFixed(2);
							var toltipStr = params[0].name
									+ "</br>"
									+ params[0].seriesName
									+ "："
									+ data0.toString().replace(
											/\B(?=(\d{3})+(?!\d))/g, ",");
							if (params[1].dataIndex != -1) {
								var data1 = params[1].data.toFixed(2);
								toltipStr += "</br>"
										+ params[1].seriesName
										+ "："
										+ data1.toString().replace(
												/\B(?=(\d{3})+(?!\d))/g, ",");
							}
							return toltipStr;
						}
					},
					legend : {
						data : legendData,
						right : 'right'
					},
					xAxis : {
						data : xDateValue,
						name : ''// xName
					},
					yAxis : {
						type : 'value'
					},
					dataZoom : {
						type : 'slider',
						backgroundColor : '#fff'
					},
					series : [ {
						name : nnData.key,
						type : 'line',
						data : yDateValue
					}, {
						name : bbkey + " ",
						type : 'line',
						data : bbDateValue
					} ]
				};
				// 使用刚指定的配置项和数据显示图表。
				myChart.setOption(option);

				if (!yDateValue || yDateValue.length <= 0) {
					myChart.clear();
					myChart.showLoading({
						text : "暂无数据",
						y : 300,
					});
				}
				return myChart;
			};

			LineService.nullChart = function(id) {
				var myChart = this.getMychart(id);
				myChart.clear();
				myChart.showLoading({
					text : "暂无数据",
					y : 300,
				});
				return myChart;
			};
			LineService.resizeChart = function(id) {
				var myChart = this.getMychart(id);
				myChart.resize();
				return myChart;
			};
			return LineService;
		});
