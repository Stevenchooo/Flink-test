angular.module('app').factory('hbBar', function() {
	var BarService = Object.create(BaseService);
	BarService.createChart = function(id, barData,titleData) {
		var myChart = this.getMychart(id);

		setTimeout(function() {
			window.onresize = function() {
				myChart.resize();
			}
		}, 200);

		var xBar = [];
		var yBar = [];
		if (barData.data.length <= 0) {
			myChart.clear();
			myChart.showLoading({
				text : "暂无数据",
				y : 300,
			});
			return;
		}
		var totalBar = 0;
		for(var i in barData.data){
			totalBar = totalBar+parseInt(barData.data[i].y);
		}
		for ( var i in barData.data) {
			xBar.push(barData.data[i].x);
			yBar.push((parseInt(barData.data[i].y) / totalBar).toFixed(4)*100);
		}
		var hbTotalBar = 0;
		if(barData.hbData && barData.hbData.length > 0){
			for(var i in barData.hbData){
				hbTotalBar = hbTotalBar +parseInt(barData.hbData[i].y);
			}
		}
		var hbyBar = [];
		if(barData.hbData && barData.hbData.length > 0){
			var map = {};
			for(var i in barData.hbData){
				map[barData.hbData[i].x] = barData.hbData[i].y;
			}
			for(var i in xBar){
				var y = parseInt(map[xBar[i]] ? map[xBar[i]] :0);
				hbyBar.push((y / hbTotalBar).toFixed(4)*100);
			}
		}
		
		var option = {
				title : {
			        text: titleData,
			        x:'left'
			    },
			tooltip : {
				trigger : 'axis',
				axisPointer: {
		            type: 'shadow'
		        },
				formatter : function(params) {
		        	var data0  = params[0].data.toFixed(2);
					var toltipStr = params[0].name
							+ "</br>"
							+ params[0].seriesName
							+ "："
							+ data0.toString().replace(
									/\B(?=(\d{3})+(?!\d))/g, ",") + "%";
					if (params[1].dataIndex != -1) {
						var data1  = params[1].data.toFixed(2);
						toltipStr += "</br>"
								+ params[1].seriesName
								+ "："
								+ data1.toString().replace(
										/\B(?=(\d{3})+(?!\d))/g, ",") +"%";
					}
					return toltipStr;
				}
			},
			color : [ '#4169E1', '#EEC900' ],
			legend: {
		        data: hbyBar.length > 0 ? [titleData+'比例', '环比'+titleData+'比例'] : '',
				right : 'left'
		    },
			xAxis : {
				data : xBar,
				name : '类别'
			},
			yAxis : {
				type : 'value',
				name : '百分比',
				axisLabel: {
	                  show: true,
	                  interval: 'auto',
	                  formatter: '{value}%'
	                },
			},
			dataZoom : {
				type : 'slider'
			},
			series : [ {
				name : titleData+'比例',
				type : 'bar',
				data : yBar,
				itemStyle : hbyBar.length > 0 ? {}:{normal: {
					color: function(params) {
						var colorList = [
						'#C1232B','#B5C334','#FCCE10','#E87C25','#27727B',
						'#FE8463','#9BCA63','#FAD860','#F3A43B','#60C0DD',
						'#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'
						];
							params.dataIndex = params.dataIndex % 15;
						return colorList[params.dataIndex];
						}
					}
				}
			}, {
				name : hbyBar.length > 0 ?'环比'+titleData+'比例':'-',
				type : 'bar',
				data : hbyBar
			}
			]
		};
		myChart.setOption(option);

		return myChart;
	};
	return BarService;
});
