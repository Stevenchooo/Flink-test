angular
		.module('app')
		.factory(
				'lineRef',
				function() {
					var LineService = Object.create(BaseService);
					LineService.createChart = function(id, respdata, isRefDet) {
						var myChart = this.getMychart(id);
						
						setTimeout(function() {
							window.onresize = function() {
								myChart.resize();
							}
						}, 200);
						
						var dataValue = [];
						var xDateValue = [];
						var legendData = [];
						var nnData;
						if (isRefDet) {
							if (!(respdata.data && respdata.data.values)) {
								myChart.clear();
								myChart.showLoading({
									text : "暂无数据",
									y : 300,
								});
								return myChart;
							}
							nnData = respdata.data;
							var yData = [];
							for ( var j in nnData.values) {
								yData.push(nnData.values[j].y);
							}
							dataValue.push({
								name : nnData.key,
								type : 'line',
								data : yData
							});
							legendData.push(nnData.key);
							for ( var i in respdata.dataDet) {
								var yData = [];
								for ( var j in respdata.dataDet[i]) {
									yData.push(respdata.dataDet[i][j].y);
								}
								dataValue.push({
									name : i,
									type : 'line',
									data : yData
								});
								legendData.push(i);
							}
						} else {
							if (!respdata || respdata.length <= 0) {
								myChart.clear();
								myChart.showLoading({
									text : "暂无数据",
									y : 300,
								});
								return myChart;
							}
							nnData = respdata[0];
							for (var i = 0; i < respdata.length; i++) {
								var yData = [];
								for ( var j in respdata[i].values) {
									yData.push(respdata[i].values[j].y);
								}
								dataValue.push({
									name : respdata[i].key,
									type : 'line',
									data : yData
								});
								legendData.push(respdata[i].key);
							}
						}
						for ( var i in nnData.values) {
							xDateValue.push(nnData.values[i].x);
						}
						// 指定图表的配置项和数据
						var option = {
							tooltip : {
								trigger : 'axis',
								formatter : function(params) {
									var toltipStr = params[0].name;
									for ( var i in params) {
										toltipStr += "</br>"
												+ params[i].seriesName
												+ "："
												+ (params[i].data ? params[i].data
														.toString()
														.replace(
																/\B(?=(\d{3})+(?!\d))/g,
																",")
														: 0);
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
								name : ' '
							},
							yAxis : {
								type : 'value'
							},
							dataZoom : {
								type : 'slider',
								backgroundColor : '#fff'
							},
							series : dataValue
						};
						// 使用刚指定的配置项和数据显示图表。
						myChart.setOption(option);

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
					return LineService;
				});
