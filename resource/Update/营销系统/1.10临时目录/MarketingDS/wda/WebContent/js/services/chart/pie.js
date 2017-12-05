angular.module('app').factory(
		'pie',
		function() {
			var PieService = Object.create(BaseService);
			PieService.createChart = function(id, pieData,titleData, flag) {
				var myChart = this.getMychart(id);

				setTimeout(function() {
					window.onresize = function() {
						myChart.resize();
					}
				}, 150);

				var legendPieData = [];
				var valuePie = [];
				for ( var i in pieData) {
					legendPieData.push(pieData[i].key);
					valuePie.push({
						name : pieData[i].key,
						value : pieData[i].y.toFixed(2)
					});
				}

				if (!legendPieData || legendPieData.length <= 0) {
					myChart.clear();
					myChart.showLoading({
						text : "暂无数据",
						y : 250,
					});
				}
				
				if(flag){
					legendPieData = [];
				}
				
				var option = {
						title : {
					        text: titleData,
					        x:'left'
					    },
					tooltip : {
						trigger : 'item',
						formatter : function(params) {
							var value = 0;
							if (!isNaN(params.value)) {
								value = params.value.toString().replace(
										/\B(?=(\d{3})+(?!\d))/g, ",");
							}
							return toltipStr = params.name + "：" + value + "("
									+ params.percent + "%)";
						}
					},
					legend : {
						data : legendPieData,
						right : 'left'
					},
					series : [ {
						name : "",
						center : [ '50%', '60%' ],
						radius : '45%',
						type : 'pie',
						data : valuePie
					} ]
				};
				myChart.setOption(option);
				
				return myChart;
			};
			return PieService;
		});
