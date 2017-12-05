angular
		.module('app')
		.factory(
				'map',
				function() {
					var MapService = Object.create(BaseService);
					MapService.createChart = function(id, mapData) {
						var myChart = this.getMychart(id);

						setTimeout(function() {
							window.onresize = function() {
								myChart.resize();
							}
						}, 200);

						var provinceMap = {
							"内蒙古" : "内蒙古",
							"黑龙江省" : "黑龙江",
						};
						var mapValue = [];
						var minMax = [];
						for ( var i in mapData) {
							minMax.push(mapData[i].event);
							var name = provinceMap[mapData[i].name] ? provinceMap[mapData[i].name]
									: mapData[i].name.substr(0, 2)
							mapValue.push({
								value : mapData[i].event,
								name : name
							});
						}
						var max1 = 0;
						var min1 = 1;
						if (minMax && minMax.length > 0) {
							max1 = Math.max.apply(Math, minMax);
							min1 = Math.min.apply(Math, minMax);
						}
						// 指定图表的配置项和数据
						var option = {
							title : {
								text : '地域分布',
								left : 'center'
							},
							textStyle : {
								fontSize :12
							},
							tooltip : {
								trigger : 'item',
								formatter : function(params) {
									var value = 0;
									if (!isNaN(params.value)) {
										value = params.value
												.toString()
												.replace(
														/\B(?=(\d{3})+(?!\d))/g,
														",");
									}
									return toltipStr = params.name + "："
											+ value;
								}
							},
							dataRange : {
								min : min1,
								max : max1,
								x : 'left',
								y : 'bottom',
								text : [ '最高', '最低' ], // 文本，默认为数值文本
								calculable : true,
								color : [ 'red', '#FFF68F' ],
							},
							series : [ {
								type : 'map',
								mapType : 'china',
								mapLocation : {
									y : 45,
									x : 'left',
									width : '80%'
								},
								label : {
									normal : {
										show : true
									},
									emphasis : {
										show : true
									}
								},
								roam : false,
								data : mapValue
							} ]
						};
						myChart.setOption(option);

						return myChart;
					};
					return MapService;
				});
