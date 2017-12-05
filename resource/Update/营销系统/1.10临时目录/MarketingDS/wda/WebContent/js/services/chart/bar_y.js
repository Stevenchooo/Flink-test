angular.module('app').factory('barY', function() {
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
			totalBar = totalBar+parseInt(barData.data[i].x);
		}
		for ( var i in barData.data) {
			xBar.push((parseInt(barData.data[i].x) / totalBar).toFixed(4)*100);
			yBar.push(barData.data[i].y);
		}
		var hbTotalBar = 0;
		if(barData.hbData && barData.hbData.length > 0){
			for(var i in barData.hbData){
				hbTotalBar = hbTotalBar +parseInt(barData.hbData[i].x);
			}
		}
		var hbxBar =[];
		if(barData.hbData && barData.hbData.length > 0){
			var map = {};
			for(var i in barData.hbData){
				map[barData.hbData[i].y] = barData.hbData[i].x;
			}
			for(var i in yBar){
				var x = parseInt(map[yBar[i]] ? map[yBar[i]] :0);
				hbxBar.push((x / hbTotalBar).toFixed(4)*100);
			}
			
//			for(var i in barData.hbData){
//				hbxBar.push((parseInt(barData.hbData[i].x) / hbTotalBar).toFixed(4)*100);
//			}
		}
		var option = {
				title : {
			        text: titleData,
			        x:'left'
			    },
			tooltip : {
				trigger: 'axis',
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
		        data: hbxBar.length > 0 ? ['人群比例', '环比人群比例'] : '',
				right : 'left'
		    },
			xAxis : {
				type: 'value',
				axisLabel: {
	                  show: true,
	                  interval: 'auto',
	                  formatter: '{value}%'
	                },
		        name :'百分比'
				
			},
			yAxis : {
				type: 'category',
				data : yBar,
				name : '年龄'
			},
			series : [ 
			  {
				name : '人群比例',
				type : 'bar',
				data : xBar,
				itemStyle : hbxBar.length > 0 ? {}:{normal: {
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
			  },{
				    name : hbxBar.length > 0 ?'环比人群比例':'-',
					type : 'bar',
					data : hbxBar
			} 
			]
		};
		myChart.setOption(option);

		return myChart;
	};
	return BarService;
});
