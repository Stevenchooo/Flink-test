var mktChart = {};

mktChart.common = {
		getMax: function(values){
			var max = 0;
			$.each(values,function(i,value){
				if(parseInt(value.value) > max){
					max = parseInt(value.value);	
				}
			});
			return max;
		},
		
		
		chinaMap : function(id,areaInfoList)
		{
			var impDatas = [];
			var clickDatas = [];
			var uvDatas = [];
			var clickerDatas = [];
			$.each(areaInfoList, function(i, value){
				var province = value["province"];
				var imp = value["area_bg_pv"];
				var click = value["area_dj_pv"];
				var uv = value["area_bg_uv"];
				var clicker = value["area_dj_uvm"];
				if(imp>0)
				{
					impDatas.push({name: province,value:imp});
				}
				
				if(click>0)
				{
					clickDatas.push({name: province,value:click});
				}
				
				if(uv>0)
				{
					uvDatas.push({name: province,value:uv});
				}
				
				if(clicker>0)
				{
					clickerDatas.push({name: province,value:clicker});
				}
				
				
				
				
			});
			
			
			var max = this.getMax(clickDatas);
			//控件的配置
			/*var option = {
			   color : ['#2ec7c9','#b6a2de','#5ab1ef','#ffb980','#d87a80','#8d98b3','#e5cf0d','#97b552','#95706d','#dc69aa','#07a2a4','#9a7fd1','#588dd5','#f5994e','#c05050','#59678c','#c9ab00','#7eb00a','#6f5553','#c14089'],
			   tooltip : {
			        trigger: 'item',
			    },
				legend: {
					orient: 'vertical',
					x:'left',
					selected:{
						'click':false,
						'uv':false,
						'clicker':false
					},
                    selectedMode:"single",
					data:[{
						name:"imp",
					},{
						name:"click",
					},{
						name:"uv",
					},{
						name:"clicker",
					}]
				},
				dataRange: {
					min: 0,
					max: max,
	                text:['高','低'],           // 文本，默认为数值文本
	               
             	},
              toolbox: {
              	show : true,
              	orient : 'vertical',
              	x: 'right',
              	y: 'center',
              	feature : {
              		mark : true,
              		dataView : {readOnly: false},
              		restore : true,
              		saveAsImage : true
              	}
              },
              series : [
	              {
	              	name: 'imp',
	              	type: 'map',
	              	mapType: 'china',
	              	itemStyle:{
	                    normal:{label:{show:true}, color:'#ffd700'},// for legend
	                    emphasis:{label:{show:true}}
	                },
	                data: impDatas
                  },
                  {
                  	name: 'click',
                  	type: 'map',
                  	mapType: 'china',
                  	itemStyle:{
                          normal:{label:{show:true}, color:'#ff8c00'},// for legend
                          emphasis:{label:{show:true}}
                      },
                    data:clickDatas
                  },
                  {
                  	name: 'uv',
                  	type: 'map',
                  	mapType: 'china',
                  	itemStyle:{
                          normal:{label:{show:true}, color:'#da70d6'},// for legend
                          emphasis:{label:{show:true}}
                      },
                     data:uvDatas
                  },
                  {
                  	name: 'clicker',
                  	type: 'map',
                  	mapType: 'china',
                  	itemStyle:{
                          normal:{label:{show:true}, color:'#da10e3'},// for legend
                          emphasis:{label:{show:true}}
                      },
                     data:clickerDatas
                  }
                ]
              };*/
			/////////////////////////////
			option = {
				    tooltip : {
				        trigger: 'item'
				    },
				    legend: {
				        orient: 'vertical',
				        x:'left',
				        selected:{
				        	'imp':false,
							//'click':false,
							'uv':false,
							'clicker':false
						},
	                    selectedMode:"single",
				        data:['imp','click','uv','clicker'],
				    },
				    dataRange: {
				        min: 0,
				        max: max,
				        text:['高','低']

				        },
				    toolbox: {
				        show: true,
				        orient : 'vertical',
				        x: 'right',
				        y: 'center',
				        feature : {
				            saveAsImage : {show: true}
				        }
				    },
				    series : [
				        {
				            name: 'imp',
				            type: 'map',
				            mapType: 'china',
				            roam: false,
				            itemStyle:{
				                normal:{label:{show:true}},
				                emphasis:{label:{show:true}}
				            },
				            data:impDatas
				        },
				        {
				            name: 'click',
				            type: 'map',
				            mapType: 'china',
				            itemStyle:{
				                normal:{label:{show:true}},
				                emphasis:{label:{show:true}}
				            },
				            data: clickDatas
				        },
				        {
				            name: 'uv',
				            type: 'map',
				            mapType: 'china',
				            itemStyle:{
				                normal:{label:{show:true}},
				                emphasis:{label:{show:true}}
				            },
				            data:uvDatas
				        },
				        {
				            name: 'clicker',
				            type: 'map',
				            mapType: 'china',
				            itemStyle:{
				                normal:{label:{show:true}},
				                emphasis:{label:{show:true}}
				                },
				            data:clickerDatas
				        }

				    ],
				    noDataLoadingOption : {
				    	text: '暂无数据',
				    	effect: 'whirling'
				    }
				};			
			
			if(impDatas.length == 0 && clickDatas.length == 0 && uvDatas.length == 0 && clickerDatas.length == 0){
				option.series = [{}];
			}
			/////////////////////////////
			 var chart = echarts.init(document.getElementById(id),"macarons");
			 chart.setOption(option); 
			 return chart;
		},
		
		
		hourChart : function(id, hourList)
		{
			var hourLegendArray = ['Impression','Click'];
			var hourPgPvArray   = [];
			var hourDjPvArray   = [];
			var hourArray       = [];
			var hourArrayRuled  = [];
			if(hourList.length > 0)
			{
				 hourArray = ['00','01','02','03','04','05',
				              '06','07','08','09','10','11',
				              '12','13','14','15','16','17',
				              '18','19','20','21','22','23'];
				 
				 hourArrayRuled = ['00:00','01:00','02:00','03:00','04:00','05:00',
				              '06:00','07:00','08:00','09:00','10:00','11:00',
				              '12:00','13:00','14:00','15:00','16:00','17:00',
				              '18:00','19:00','20:00','21:00','22:00','23:00'];
				 
				 hourPgPvArray = [0,0,0,0,0,0,0,0,0,0,0,0,
				                  0,0,0,0,0,0,0,0,0,0,0,0];
				 hourDjPvArray = [0,0,0,0,0,0,0,0,0,0,0,0,
				                 0,0,0,0,0,0,0,0,0,0,0,0];
				
			}
			
			
			 $.each(hourList, function(i, value){
				 var hour = value["hour"];
				 if(hour == "0" || hour == "1" || hour == "2" || hour == "3" || hour == "4" || hour == "5" || hour == "6" || hour == "7" || hour == "8" || hour == "9"){
					 hour = "0" + hour;
				 }
				 for(var index=0;index<=23;index++)
				 {
					 if(hour == hourArray[index])
					 {
						 hourPgPvArray[index] = parseInt(value["hour_bg_pv_sum"]);
						 hourDjPvArray[index] = parseInt(value["hour_dj_pv_sum"]);
					 }
				 }
			 });
			 
			 var valueArray = [hourPgPvArray,hourDjPvArray];
				var option = {
		                tooltip : {
		                	trigger: 'axis',
		                	formatter:'{b}<br/>{a0}:{c0}<br/>{a1}:{c1}'
		                },
		                legend: {
		                	data: hourLegendArray,
		                	selectedMode: false
		                },
		                toolbox: {
		                	show : true,
		                	feature : {
		                		saveAsImage : true
		                	}
		                },
		                xAxis : [
		                {
		                	type : 'category',
		                	boundaryGap : false,
		                	data : hourArrayRuled
		                }
		                ],
		                yAxis : [
		                {
		                	type : 'value',
		                	name : hourLegendArray[0],
		                	splitArea : {show : true}
		                },
		                {
		                	type : 'value',
		                	name : hourLegendArray[1],
		                	splitArea : {show : true}
		                }
		                ],
		    		    noDataLoadingOption: {
		                    text: '暂无数据',
		                    effect: 'whirling'
		    		    }
		            };
				
				var series = [];
				if(hourList.length>0){
				 $.each(hourLegendArray, function(i, value){
					 series.push({
						    name:hourLegendArray[i],
						    yAxisIndex : i,
			 	            type:'line',
			 	            data:valueArray[i],
			 	            markPoint : {
			 	                data : [
			 	                    {type : 'max', name: '24小时最高'}
			 	                    ]},
					 });
				 });
				}else
				{
					$.each(hourLegendArray, function(i, value){
					 series.push({
						    name:hourLegendArray[i],
						    yAxisIndex : i,
			 	            type:'line',
			 	            data:valueArray[i],
					 });
				 });
				}
			    option.series = series;

				
			 var chart = echarts.init(document.getElementById(id),"macarons");
			 chart.setOption(option); 
			 return chart;
		},


		GroupTransChart : function(groupTrans)
		{
			var allp_bg_pv = groupTrans.allp_bg_pv;
			$("#allp_bg_pv").html(allp_bg_pv);
			
			var allp_bg_uv = groupTrans.allp_bg_uv;
			$("#allp_bg_uv").html(allp_bg_uv);
			
			var wdp_bg_pv = groupTrans.wdp_bg_pv;
			$("#wdp_bg_pv").html(wdp_bg_pv);
			
			var wdp_bg_uv = groupTrans.wdp_bg_uv;  
			$("#wdp_bg_uv").html(wdp_bg_uv);
			
			var uv_TranRate = groupTrans.uv_TranRate;
			$("#uv_TranRate").html(uv_TranRate);
			
			var pv_TranRate = groupTrans.pv_TranRate;
			$("#pv_TranRate").html(pv_TranRate);
			
			
			var radioValue =  $("input[name='group_chart_radio']:checked").val();
			
			if(radioValue == "Imp")
			{
				$("#allp_bg_pv").show();
				$("#allp_bg_uv").hide();
				$("#wdp_bg_pv").show();
				$("#wdp_bg_uv").hide();
				$("#uv_TranRate").hide();
				$("#pv_TranRate").show();
				
			}
			else if(radioValue == "UV")
			{
				$("#allp_bg_pv").hide();
				$("#allp_bg_uv").show();
				$("#wdp_bg_pv").hide();
				$("#wdp_bg_uv").show();
				$("#uv_TranRate").show();
				$("#pv_TranRate").hide();
			}
		},
		
		djChart : function(id,djList)
		{

			if(0 == djList.length)
			{
				var option = {
					    tooltip : {
					        trigger: 'axis'
					    },
					    legend: {
					      
					    },
					    toolbox: {
					        show : true,
					        feature : {
					          
					            saveAsImage : {show: true}
					        }
					    },
					    calculable : false,
					    xAxis : [
					        {
					            type : 'category',
					          
					            data : []
					        }
					    ],
					    yAxis : [
					        {
					            type : 'value'
					        }
					    ],
					    series : [
					      {}
					       
					    ],
					    noDataLoadingOption: {
		                    text: '暂无数据',
		                    effect: 'whirling'
		    		    }
					};
					                    
				
				 var chart = echarts.init(document.getElementById(id),"macarons");
				 chart.setOption(option); 
				 return chart;
			}
			else
				{
				var	 djClicksArray = ['1+','2+','3+','4+','5+','6+','7+','8+','9+','10+'];
				
				var djMediaArray = [];
				var djDataArray = [];
				var obj = {};
				 $.each(djList, function(i, value)
				{
					var media = value.dj_media_name;
					var users = value.click_frequency_users_sum;
					var frequency = value.frequency;
					
					if(obj[media] == null)
					{
						djDataArray[media] = [0,0,0,0,0,0,0,0,0,0];
						djDataArray[media][frequency-1] = users;
						obj[media] = media;
						djMediaArray.push(media);
					}
					else
					{
						djDataArray[media][frequency-1] = users;
					}
					
				 });
				 
				 
					var option = {
			                tooltip : {
			                	trigger: 'axis'
			                },
			                legend: {
			                	data: djMediaArray,
						        itemGap:1
			                },
			                toolbox: {
			                	show : true,
			                	feature : {
			                		saveAsImage : true
			                	}
			                },
			                xAxis : [
			                {
			                	type : 'category',
			                	data : djClicksArray
			                }
			                ],
			                yAxis : [
			                {
				 	            type : 'value'
			                }
			                ],
			                
			            };
					var series = [];
					
					 $.each(djMediaArray, function(i, value){
						 series.push({
							    name:djMediaArray[i],
				 	            type:'line',
				 	            data:djDataArray[djMediaArray[i]],
						 });
					 });
					 
					 option.series = series;

					
				 var chart = echarts.init(document.getElementById(id),"macarons");
				 chart.setOption(option); 
				 
				 return chart;
				}
			
		},

		
		baseChart : function(pvUvList,UvList)
		{
			var legendArray = ['曝光', '点击'];
			var meidaCategoryArray = [];
			var meidaCategoryArrayUV = [];
			var bgPvArray = [];
			var bgUvArray = [];
			var djPvArray = [];
			var djUvArray = [];
			$.each(pvUvList, function (i, value) {
				meidaCategoryArray.push(value["mt_media_name"]);
				bgPvArray.push(value["mt_bg_pv_sum"]);
				djPvArray.push(value["mt_dj_pv_sum"]);
			});

			$.each(UvList, function (i, value) {
				meidaCategoryArrayUV.push(value["mt_media_name"]);
				bgUvArray.push(value["mt_bg_uv_sum"]);
				djUvArray.push(value["mt_dj_uv_sum"]);
			});
			
			var pvChart = echarts.init(document.getElementById('base_bg_chart'),"macarons");

			var uvChart = echarts.init(document.getElementById('base_dj_chart'),"macarons");
			
			
			var optionPv = this.getBaseChartOption(legendArray, meidaCategoryArray, [bgPvArray, djPvArray], 'bar');

			var optionUv = this.getBaseChartOption(legendArray, meidaCategoryArrayUV, [bgUvArray, djUvArray], 'bar');
            
			pvChart.setOption(optionPv);
			uvChart.setOption(optionUv);
			
			pvChart.resize();
			uvChart.resize();
			chartMap = {"pvChart":pvChart,"uvChart":uvChart};
			return chartMap;
		},
		
		
		getBaseChartOption : function(legendArray,categoryArray,valueArray,chartType)
		{
			 var option = {};
			 
			 option.tooltip = {trigger:'axis'};
			 option.legend = {data: legendArray};
			 option.toolbox = {show : true,
	 	        feature : {
	 	            saveAsImage : {show: true}
	 	        }
	 	     };
			 
			option.calculable = false;
			option.xAxis = [
	 	        {
	 	            type : 'category',
	 	            data : categoryArray,
	 	           axisLabel:{
                       interval:0,
                     rotate:45,
                     margin:2,
                     
                     }
	 	        }
	 	    ];
			option.yAxis = [
	 	        {
	 	        	name:legendArray[0],
	 	            type : 'value'
	 	        },
	 	       {
	 	        	name:legendArray[1],
	 	            type : 'value'
	 	        }
	 	    ];
			
			var series = [];
			
			 $.each(legendArray, function(i, value){
				 series.push({
					    yAxisIndex : i,
					    name:legendArray[i],
		 	            type:chartType,
		 	            data:valueArray[i],
				 });
			 });
			 
			 option.noDataLoadingOption = {text: '暂无数据',effect: 'whirling'}
			 option.series = series;
			 return option;
		},
		
		
		getChartOption : function(legendArray,categoryArray,valueArray,chartType)
		{
			 var option = {};
			 
			 option.tooltip = {trigger:'axis'};
			 option.legend = {data: legendArray};
			 option.toolbox = {show : true,
	 	        feature : {
	 	            saveAsImage : {show: true}
	 	        }
	 	     };
			 
			option.calculable = false;
			option.xAxis = [
	 	        {
	 	            type : 'category',
	 	            data : categoryArray
	 	        }
	 	    ];
			option.yAxis = [
	 	        {
	 	            type : 'value'
	 	        }
	 	    ];
			
			var series = [];
			
			 $.each(legendArray, function(i, value){
				 series.push({
					    name:legendArray[i],
		 	            type:chartType,
		 	            data:valueArray[i],
				 });
			 });
			 
			 option.series = series;
			 
			 return option;
		},
		//分时数据折线图
	        hourstatChart : function(hourstatList,queryType)
		{
			var djMediaArray = [];
			var djDataArray = [];
			var djLegendArray = [];
			var bgMediaArray = [];
			var bgDataArray = [];
			var bgLegendArray = [];
			var obj = {};
			if (queryType=='-1'){
				
			
			 $.each(hourstatList, function(i, value)
			{
				var media = value.img_sub_name;
				var media_id = value.img_sub_id;
				var bgpvNum = value.img_bg_pv;
				var djpvNum = value.img_dj_pv;
				var hourTime = parseInt(value.img_hour_time,10);
				
				if(obj[media] == null)
				{
					djDataArray[media] = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
					djDataArray[media][hourTime] = parseInt(djpvNum,10);
					bgDataArray[media] = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
					bgDataArray[media][hourTime] = parseInt(bgpvNum,10);
					obj[media] = media;
					djMediaArray.push(media);
					bgMediaArray.push(media);
				}
				else
				{
					djDataArray[media][hourTime] = parseInt(djpvNum,10);
					bgDataArray[media][hourTime] = parseInt(bgpvNum,10);
				}
				
			 });
			
			var hourArray       = [];
			
			hourArray = ['00:00','01:00','02:00','03:00','04:00','05:00',
			              '06:00','07:00','08:00','09:00','10:00','11:00',
			              '12:00','13:00','14:00','15:00','16:00','17:00',
			              '18:00','19:00','20:00','21:00','22:00','23:00'];	 
			
			
			
			
			
				var djOption = {
		                tooltip : {
		                	trigger: 'axis'
		                },
		                legend: {
		                	data: djMediaArray
		                },
		                toolbox: {
		                	show : true,
		                	feature : {
		                		saveAsImage : true
		                	}
		                },
		                xAxis : [
		                {
		                	type : 'category',
		                	boundaryGap : false,
		                	data : hourArray
		                }
		                ],
		                yAxis : [
		                {
		                	type : 'value'
		                }
		                ],
		                
		            };
				
				
				var bgOption = {
		                tooltip : {
		                	trigger: 'axis'
		                },
		                legend: {
		                	data: bgMediaArray
		                },
		                toolbox: {
		                	show : true,
		                	feature : {
		                		saveAsImage : true
		                	}
		                },
		                xAxis : [
		                {
		                	type : 'category',
		                	boundaryGap : false,
		                	data : hourArray
		                }
		                ],
		                yAxis : [
		                {
		                	type : 'value'
		                }
		                ],
		                
		            };
				
				
				var djSeries = [];
				var bgSeries = [];
				
				 
				
				if(hourstatList.length>0){
				 $.each(djMediaArray, function(i, value){
					 djSeries.push({
						    name:djMediaArray[i],
			 	            type:'line',
			 	            data:djDataArray[djMediaArray[i]],
			 	            markPoint : {
			 	                data : [
			 	                    {type : 'max', name: '24小时最高'}
			 	                    ]},
					 });
				 });
				 $.each(bgMediaArray, function(i, value){
					 bgSeries.push({
						    name:bgMediaArray[i],
			 	            type:'line',
			 	            data:bgDataArray[bgMediaArray[i]],
			 	            markPoint : {
			 	                data : [
			 	                    {type : 'max', name: '24小时最高'}
			 	                    ]},
					 });
				 });
				}else
				{
					$.each(djMediaArray, function(i, value){
					 djSeries.push({
						    name:djMediaArray[i],
			 	            type:'line',
			 	            data:djDataArray[djMediaArray[i]]
					     });
				    });
					
					$.each(bgMediaArray, function(i, value){
						bgSeries.push({
						    name:bgMediaArray[i],
			 	            type:'line',
			 	            data:bgDataArray[bgMediaArray[i]]				 	 
						 });
					 });
					
				}
				
				
				if(0 == hourstatList.length)
				{
					var option = {
						    tooltip : {
						        trigger: 'axis'
						    },
						    legend: {
						      
						    },
						    toolbox: {
						        show : true,
						        feature : {
						          
						            saveAsImage : {show: true}
						        }
						    },
						    calculable : false,
						    xAxis : [
						        {
						            type : 'category',
						          
						            data : []
						        }
						    ],
						    yAxis : [
						        {
						            type : 'value'
						        }
						    ],
						    series : [
						      {}
						       
						    ]
						};
						                    
					
					 var chartbg = echarts.init(document.getElementById('hourstat_bg_chart'),"macarons");
					 chartbg.setOption(option); 
					 var chartdj = echarts.init(document.getElementById('hourstat_dj_chart'),"macarons");
					 chartdj.setOption(option);
				}else{
					djOption.series = djSeries;
				        bgOption.series = bgSeries;

				        var bgChart = echarts.init(document.getElementById('hourstat_bg_chart'),"macarons");
			              bgChart.setOption(bgOption);			 	
				 var djChart = echarts.init(document.getElementById('hourstat_dj_chart'),"macarons");
				 djChart.setOption(djOption); 
				}
			}else{
				
				 $.each(hourstatList, function(i, value)
							{
								var media = value.img_sub_name;
								var media_id = value.img_sub_id;
								var bgpvNum = value.img_bg_pv;
								var djpvNum = value.img_dj_pv;
								var hourTime = parseInt(value.img_hour_time,10);
								
								if(obj[media_id] == null)
								{
									djDataArray[media_id] = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
									djDataArray[media_id][hourTime] = parseInt(djpvNum,10);
									bgDataArray[media_id] = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
									bgDataArray[media_id][hourTime] = parseInt(bgpvNum,10);
									obj[media_id] = media_id;
									djMediaArray.push(media_id);
									bgMediaArray.push(media_id);
								}
								else
								{
									djDataArray[media_id][hourTime] = parseInt(djpvNum,10);
									bgDataArray[media_id][hourTime] = parseInt(bgpvNum,10);
								}
								
							 });
							
							var hourArray       = [];
							
							hourArray = ['00:00','01:00','02:00','03:00','04:00','05:00',
							              '06:00','07:00','08:00','09:00','10:00','11:00',
							              '12:00','13:00','14:00','15:00','16:00','17:00',
							              '18:00','19:00','20:00','21:00','22:00','23:00'];	 
							
							
							
							
							
								var djOption = {
						                tooltip : {
						                	trigger: 'axis'
						                },
						                legend: {
						                	data: djMediaArray
						                },
						                toolbox: {
						                	show : true,
						                	feature : {
						                		saveAsImage : true
						                	}
						                },
						                xAxis : [
						                {
						                	type : 'category',
						                	boundaryGap : false,
						                	data : hourArray
						                }
						                ],
						                yAxis : [
						                {
						                	type : 'value'
						                }
						                ],
						                
						            };
								
								
								var bgOption = {
						                tooltip : {
						                	trigger: 'axis'
						                },
						                legend: {
						                	data: bgMediaArray
						                },
						                toolbox: {
						                	show : true,
						                	feature : {
						                		saveAsImage : true
						                	}
						                },
						                xAxis : [
						                {
						                	type : 'category',
						                	boundaryGap : false,
						                	data : hourArray
						                }
						                ],
						                yAxis : [
						                {
						                	type : 'value'
						                }
						                ],
						                
						            };
								
								
								var djSeries = [];
								var bgSeries = [];
								
								 
								
								if(hourstatList.length>0){
								 $.each(djMediaArray, function(i, value){
									 djSeries.push({
										    name:djMediaArray[i],
							 	            type:'line',
							 	            data:djDataArray[djMediaArray[i]],
							 	            markPoint : {
							 	                data : [
							 	                    {type : 'max', name: '24小时最高'}
							 	                    ]},
									 });
								 });
								 $.each(bgMediaArray, function(i, value){
									 bgSeries.push({
										    name:bgMediaArray[i],
							 	            type:'line',
							 	            data:bgDataArray[bgMediaArray[i]],
							 	            markPoint : {
							 	                data : [
							 	                    {type : 'max', name: '24小时最高'}
							 	                    ]},
									 });
								 });
								}else
								{
									$.each(djMediaArray, function(i, value){
									 djSeries.push({
										    name:djMediaArray[i],
							 	            type:'line',
							 	            data:djDataArray[djMediaArray[i]]
									     });
								    });
									
									$.each(bgMediaArray, function(i, value){
										bgSeries.push({
										    name:bgMediaArray[i],
							 	            type:'line',
							 	            data:bgDataArray[bgMediaArray[i]]				 	 
										 });
									 });
									
								}
								
								
								if(0 == hourstatList.length)
								{
									var option = {
										    tooltip : {
										        trigger: 'axis'
										    },
										    legend: {
										      
										    },
										    toolbox: {
										        show : true,
										        feature : {
										          
										            saveAsImage : {show: true}
										        }
										    },
										    calculable : false,
										    xAxis : [
										        {
										            type : 'category',
										          
										            data : []
										        }
										    ],
										    yAxis : [
										        {
										            type : 'value'
										        }
										    ],
										    series : [
										      {}
										       
										    ]
										};
										                    
									
									 var chartbg = echarts.init(document.getElementById('hourstat_bg_chart'),"macarons");
									 chartbg.setOption(option); 
									 var chartdj = echarts.init(document.getElementById('hourstat_dj_chart'),"macarons");
									 chartdj.setOption(option);
								}else{
									djOption.series = djSeries;
								        bgOption.series = bgSeries;

								        var bgChart = echarts.init(document.getElementById('hourstat_bg_chart'),"macarons");
							              bgChart.setOption(bgOption);			 	
								 var djChart = echarts.init(document.getElementById('hourstat_dj_chart'),"macarons");
								 djChart.setOption(djOption); 
								}
				
			}    
		},
	
		
	       frChart:function(frResults){
		//堆积图
		
		var mediaArrayS = [];
		var expDataArrayS = [];
		var clickDataArrayS = [];
		var landingDataArrayS = [];
		var obj = {};
		var legendS = ['1','2','3+'];
		expDataArrayS[0]=new Array();
		clickDataArrayS[0]=new Array();
		landingDataArrayS[0]=new Array();
		expDataArrayS[1]=new Array();
		clickDataArrayS[1]=new Array();
		landingDataArrayS[1]=new Array();
		expDataArrayS[2]=new Array();
		clickDataArrayS[2]=new Array();
		landingDataArrayS[2]=new Array();
		
		
		
		
		
		$.each(frResults, function(i, value)
				{
					var media = value.media_name;
					var exp = value.exp_frequency_users;
					var click = value.click_frequency_users;
					var landing = value.landing_frequency_users;
					var frequency = value.frequency;
				
					
					if(obj[media] == null)
					{   
						
						if( frequency=='3+'){
							
							expDataArrayS[2][media]= exp;
							
							clickDataArrayS[2][media] = click;
							
							landingDataArrayS[2][media]= landing;
							obj[media] = media;
							mediaArrayS.push(media);
							
						}else if(frequency == "2"){
							expDataArrayS[1][media] = exp;
							clickDataArrayS[1][media] = click;
							landingDataArrayS[1][media]= landing;
							obj[media] = media;
							mediaArrayS.push(media);
						}else if(frequency == "1"){
							expDataArrayS[0][media] = exp;
							clickDataArrayS[0][media] = click;
							landingDataArrayS[0][media] = landing;
							obj[media] = media;
							mediaArrayS.push(media);
						}
						
					}
					else
					{
                                              if( frequency=="3+"){ 
							
							expDataArrayS[2][media] = exp;
							clickDataArrayS[2][media] = click;
							landingDataArrayS[2][media] = landing;
							
						}else if(frequency == "2"){
							expDataArrayS[1][media] = exp;
							clickDataArrayS[1][media] = click;
							landingDataArrayS[1][media]= landing;
						}else if(frequency == "1"){
							expDataArrayS[0][media] = exp;
							clickDataArrayS[0][media] = click;
							landingDataArrayS[0][media] = landing;
						}
						
					}
					
					
					
				 });
		//将二维数组dataArrayS中的数据按照mediaArrayS中的媒体顺序重新从0开始排序
		var expDataArraySRuled = [];
		var clickDataArraySRuled = [];
		var landingDataArraySRuled = [];
		for (var a = 0;a<3;a++){

			clickDataArraySRuled[a] = new Array();
			expDataArraySRuled[a] = new Array();
			landingDataArraySRuled[a] = new Array();
			
		}
		
		
		
		
		$.each(mediaArrayS, function(i, value){
			
			for(var a = 0;a<3;a++){
				
				if(typeof(expDataArrayS[a][mediaArrayS[i]])=='undefined'){expDataArrayS[a][mediaArrayS[i]]=0;}
				if(typeof(clickDataArrayS[a][mediaArrayS[i]])=='undefined'){clickDataArrayS[a][mediaArrayS[i]]=0;}
				if(typeof(landingDataArrayS[a][mediaArrayS[i]])=='undefined'){landingDataArrayS[a][mediaArrayS[i]]=0;}
				
				expDataArraySRuled[a][i]=expDataArrayS[a][mediaArrayS[i]];
				clickDataArraySRuled[a][i]=clickDataArrayS[a][mediaArrayS[i]];
				landingDataArraySRuled[a][i]=landingDataArrayS[a][mediaArrayS[i]];
				
			}
			
			
			
			
		});
		
		
		//转化为百分比
		var expDataArraySRuled2 = [];
		var clickDataArraySRuled2 = [];
		var landingDataArraySRuled2 = [];
		for (var a = 0;a<3;a++){

			clickDataArraySRuled2[a] = new Array();
			expDataArraySRuled2[a] = new Array();
			landingDataArraySRuled2[a] = new Array();
			
		}
		
		var mediaLength = mediaArrayS.length;
		
		for (var a=0;a<3;a++){
			if( a<2){
				for (var b=0;b<mediaLength;b++){
					if((clickDataArraySRuled[0][b]+clickDataArraySRuled[1][b]+clickDataArraySRuled[2][b])==0){
						clickDataArraySRuled2[a][b]= 0;
						expDataArraySRuled2[a][b]= 0;
						landingDataArraySRuled2[a][b]= 0;	
					}
					else
					{
					    clickDataArraySRuled2[a][b]= this.toDecimal(clickDataArraySRuled[a][b],clickDataArraySRuled[0][b],clickDataArraySRuled[1][b],clickDataArraySRuled[2][b]);
					    expDataArraySRuled2[a][b]= this.toDecimal(expDataArraySRuled[a][b],expDataArraySRuled[0][b],expDataArraySRuled[1][b],expDataArraySRuled[2][b]);
					    landingDataArraySRuled2[a][b]= this.toDecimal(landingDataArraySRuled[a][b],landingDataArraySRuled[0][b],landingDataArraySRuled[1][b],landingDataArraySRuled[2][b]);
					}
				}
			}else{
				
				for (var b=0;b<mediaLength;b++)
				{
					if(clickDataArraySRuled[2][b] == 0)
					{
						clickDataArraySRuled2[a][b]= 0;
					}else
					{
						clickDataArraySRuled2[a][b]= 100-(clickDataArraySRuled2[0][b]+clickDataArraySRuled2[1][b]);
					}
					if(expDataArraySRuled[2][b] == 0)
					{
						expDataArraySRuled2[a][b]= 0;
					}else
					{
					    expDataArraySRuled2[a][b]= 100-(expDataArraySRuled2[0][b]+expDataArraySRuled2[1][b]);
					}
					
					if(landingDataArraySRuled[2][b] == 0)
					{
						landingDataArraySRuled2[a][b]= 0;
					}else
					{
						landingDataArraySRuled2[a][b]= 100-(landingDataArraySRuled2[0][b]+landingDataArraySRuled2[1][b]);
					}
				}
			}
		
		}
		
		var expChartS = echarts.init(document.getElementById('fr_exp_stack'),"macarons");

		var optionExpChartS = this.getFrStackOption(mediaArrayS, expDataArraySRuled2, legendS);

		expChartS.setOption(optionExpChartS);
		
		var clickChartS = echarts.init(document.getElementById('fr_click_stack'),"macarons");

		var optionClickChartS = this.getFrStackOption(mediaArrayS, clickDataArraySRuled2, legendS);

		clickChartS.setOption(optionClickChartS);
		
		var landingChartS = echarts.init(document.getElementById('fr_landing_stack'),"macarons");

		var optionLandingChartS = this.getFrStackOption(mediaArrayS, landingDataArraySRuled2, legendS);

		landingChartS.setOption(optionLandingChartS);
        
		//折线图

		
		var chartClickL = echarts.init(document.getElementById('fr_click_line'),"macarons");
		var chartExpL = echarts.init(document.getElementById('fr_exp_line'),"macarons");
		var chartLandingL = echarts.init(document.getElementById('fr_landing_line'),"macarons");
		
		var	 lineXArray = ['1','2','3','4','5','6','7','8','9','10','11','12','13','14','15+'];
		if(0 == frResults.length)
		{
			var option = {
				    tooltip : {
				        trigger: 'axis'
				    },
				    legend: {
				      
				    },
				    toolbox: {
				        show : true,
				        feature : {
				          
				            saveAsImage : {show: true}
				        }
				    },
				    calculable : false,
				    xAxis : [
				        {
				            type : 'category',
				          
				            data : []
				        }
				    ],
				    yAxis : [
				        {
				            type : 'value'
				        }
				    ],
				    series : [
				      {}
				       
				    ],
				    noDataLoadingOption : {
				    	text: '暂无数据',
				    	effect: 'whirling'
				    }
				};
				                    
			
			 chartClickL.setOption(option); 

			 chartExpL.setOption(option); 
			 
			 chartLandingL.setOption(option); 
		}
		else
			{
			
			var djMediaArrayL = [];
			var djDataArrayL = [];
			var bgMediaArrayL = [];
			var bgDataArrayL = [];
			var zlMediaArrayL = [];
			var zlDataArrayL = [];
			var objL = {};
			 $.each(frResults, function(i, value)
			{
				var mediaL = value.media_name;
				var expL = value.exp_frequency_users;
				var clickL = value.click_frequency_users;
				var landingL = value.landing_frequency_users;
				var frequencyL = value.frequency;
				var frequencyIntL = parseInt(frequencyL,10);
				if(objL[mediaL] == null)
				{
					if(frequencyL!='3+'){
					if(frequencyIntL<15||frequencyL=='15+'){
						
						djDataArrayL[mediaL] = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
						djDataArrayL[mediaL][frequencyIntL-1] = clickL;
						bgDataArrayL[mediaL] = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
						bgDataArrayL[mediaL][frequencyIntL-1] = expL;
						zlDataArrayL[mediaL] = [0,0,0,0,0,0,0,0,0,0,0,0,0,0,0];
						zlDataArrayL[mediaL][frequencyIntL-1] = landingL;
						objL[mediaL] = mediaL;
						djMediaArrayL.push(mediaL);
						bgMediaArrayL.push(mediaL);
						zlMediaArrayL.push(mediaL);
					}
					}
				}
				else
				{
					if(frequencyL!='3+'){
						if(frequencyIntL<15||frequencyL=='15+'){
							//console.log(frequencyIntL);
							//console.log(frequencyL);
							//console.log(clickL);
							djDataArrayL[mediaL][frequencyIntL-1] = clickL;
							bgDataArrayL[mediaL][frequencyIntL-1] = expL;
							zlDataArrayL[mediaL][frequencyIntL-1] = landingL;
						}
					}
					
				}
				
			 });
			     var chartClickOptionL = this.getFrLineOption(djMediaArrayL,lineXArray,djDataArrayL);
				 chartClickL.setOption(chartClickOptionL);
				 
				 var chartExpOptionL = this.getFrLineOption(bgMediaArrayL,lineXArray,bgDataArrayL);
				 chartExpL.setOption(chartExpOptionL); 
				 
				 var chartLandingOptionL = this.getFrLineOption(zlMediaArrayL,lineXArray,zlDataArrayL);
				 chartLandingL.setOption(chartLandingOptionL); 
			}
		
			var map = {"expChartS":expChartS,"clickChartS":clickChartS,"landingChartS":landingChartS,"chartClickL":chartClickL,"chartExpL":chartExpL,"chartLandingL":chartLandingL};
		
			return map;
			
		},
		getFrStackOption : function(mediaArray,dataArray,legendArray){
			var option = {};
			 option.tooltip = 
			 {
					 trigger:'axis',
					 formatter:'{b}<br/>{a0}:{c0}%<br/>{a1}&nbsp;&nbsp;&nbsp;:{c1}%<br/>{a2}&nbsp;&nbsp;&nbsp;:{c2}%<br/>'
			 };
			 option.legend = {data: legendArray,selectedMode: false};
			 option.toolbox = {show : true,
		        feature : {
		            saveAsImage : {show: true}
		        }
		     };
			 
			option.calculable = false;
			option.xAxis = [
		        {
		            type : 'category',
		            data : mediaArray,
		            axisLabel:{
                        interval:0,
                      rotate:45,
                      margin:2,
                      
                      }
		        }
		    ];
			option.yAxis = [
		        {
		            type : 'value',
		            axisLabel: {
		                  show: true,
		                  interval: 'auto',
		                  formatter: '{value} %'
		                },
		            max : 100,
		            min : 0
		        }
		    ];
			
			var series = [];
			
			 $.each(dataArray, function(i, value){
				 series.push({
					    name:legendArray[i],
		 	            type:'bar',
		 	            data:dataArray[i],
		 	            stack:"hello"
				 });
			 });
			 
			 option.series = series;
			 
			 option.noDataLoadingOption = {text: '暂无数据',effect: 'whirling'};
			 return option;
			
		    },
		    
	getFrLineOption : function(djMediaArrayL,lineXArray,djDataArrayL){

		var option = {
                tooltip : {
                	trigger: 'axis'
                },
                legend: {
                	data: djMediaArrayL,
                	itemGap:1
                },
                toolbox: {
                	show : true,
                	feature : {
                		saveAsImage : true
                	}
                },
                xAxis : [
                {
                	type : 'category',
                	data : lineXArray,
                	axisLabel:{
                        interval:0,
                      margin:2,
                      
                      }
                }
                ],
                yAxis : [
                {
	 	            type : 'value'
                }
                ],
                animationDuration : 1000
                
            };
		var seriesL = [];
		
		 $.each(djMediaArrayL, function(i, value){
			 seriesL.push({
				    name:djMediaArrayL[i],
	 	            type:'line',
	 	            data:djDataArrayL[djMediaArrayL[i]],
			 });
		 });
		 
		 option.series = seriesL;
		 
		 option.noDataLoadingOption = {text: '暂无数据',effect: 'whirling'};
		 return option;
		
		
		
	}
    ,   
	toDecimal:function(x1,x2,x3,x4) {
		if (typeof(x1) =="undefined" )
		{
			x1 = 0;
		}
		if (typeof(x2) =="undefined" )
		{
			x2 = 0;
		}
		if (typeof(x3) =="undefined" )
		{
			x3 = 0;
		}
		if (typeof(x4) =="undefined" )
		{
			x4 = 0;
		}
        var f1 = parseFloat(parseInt(x1));
        var f2 = parseFloat(parseInt(x2)+parseInt(x3)+parseInt(x4));
        
        if(f2 == 0)
        {
        	return 0;
        }

        if (isNaN(f1)||isNaN(f2)) {  
            return 0;              

       }              
       var f  = (parseFloat(f1/f2))*100;
       return Math.round(f);              
         

   },       
		
    funnelCharts : function (id, funnel){
    	var seriesData = [];
    	if(funnel.length > 0){
    		seriesData.push({"value":100,"name":"点击率:"+funnel[0].ctr+"% 点击UV:"+funnel[0].all_dj_uv+" 曝光UV:"+funnel[0].all_gb_uv});
    		seriesData.push({"value":80,"name":"到达率:"+funnel[0].landingRate+"% 到达UV:"+funnel[0].all_landing_uv+" 点击UV:"+funnel[0].all_dj_uv});
    		seriesData.push({"value":60,"name":"预约率:"+funnel[0].bespeakRate+"% 预约UV:"+funnel[0].all_bespeak_nums+" 到达UV:"+funnel[0].all_landing_uv});
    		seriesData.push({"value":40,"name":"下单率:"+funnel[0].orderRate+"% 下单UV:"+funnel[0].all_order_count+" 到达UV:"+funnel[0].all_landing_uv});
    		seriesData.push({"value":20,"name":"购买率:"+funnel[0].buyRate+"% 购买UV:"+funnel[0].all_pay_count+" 下单UV:"+funnel[0].all_order_count});
    	}else{
    		seriesData = [];
    	}
    	
    	var funnelChart = echarts.init(document.getElementById(id),"macarons");
		option = {
				title : {
    		        subtext: '转化率',
    		    },
			    toolbox: {
			        show : true,
			        feature : {
			            saveAsImage : {show: true}
			        }
			    },
			    series : [
			        {
			            type:'funnel',
			            width: "30%",
			            x:'0%',
			            funnelAlign:'left',
			            data:seriesData
			        }
			    ],
			    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
			};
		
		funnelChart.setOption(option);
		return funnelChart;
    },
    diversionDayCharts : function (dailyList){
    	var dateArray = [];
    	var mediaArray = {};
    	var mediaNameArray = [];
    	var mediaDJArray = {};
    	var mediaDJUVArray = {};
    	var mediaBGArray = {};
    	var mediaBGUVArray = {};
    	
    	//得到日期并去重
    	$.each(dailyList, function(i, value){
    		if("pt_d" in value){
    			var date = value.pt_d;
        		var flag = dateArray.indexOf(date);
        		if(flag == -1){
        			dateArray.push(date);
        		}
    		}
    	});
    	dateArray.sort();
    	
    	//得到媒体
    	$.each(dailyList, function(i, value){
    		if("pt_d" in value && "media_name" in value){
    			var media = value.media_name;
        		if(mediaArray[media] == null){
        			mediaArray[media] = [];
        			mediaNameArray.push(media);
        		}
        		mediaArray[media].push(dailyList[i]);
    		}
    	});
    	
    	mediaNameArray.sort();
    	
    	//点击次数
    	for(var index = 0;index<dateArray.length;index++){
    		for(var i = 0;i<mediaNameArray.length;i++){
    			var flag = false;
        		for(var j = 0;j<mediaArray[mediaNameArray[i]].length;j++){
        			if(dateArray[index] == mediaArray[mediaNameArray[i]][j].pt_d){
        				flag = true;
        				break;
        			}
        		}
        		if(mediaDJArray[mediaNameArray[i]] == null){
        			mediaDJArray[mediaNameArray[i]] = [];
        		}
        		if(mediaDJUVArray[mediaNameArray[i]] == null){
        			mediaDJUVArray[mediaNameArray[i]] = [];
        		}
        		if(mediaBGArray[mediaNameArray[i]] == null){
        			mediaBGArray[mediaNameArray[i]] = [];
        		}
        		if(mediaBGUVArray[mediaNameArray[i]] == null){
        			mediaBGUVArray[mediaNameArray[i]] = [];
        		}
    			if(flag){
        			//点击次数
        			mediaDJArray[mediaNameArray[i]].push(parseInt(mediaArray[mediaNameArray[i]][j].all_dj_pv));
        			//点击人次
        			mediaDJUVArray[mediaNameArray[i]].push(parseInt(mediaArray[mediaNameArray[i]][j].all_dj_uv));
        			//曝光次数
        			mediaBGArray[mediaNameArray[i]].push(parseInt(mediaArray[mediaNameArray[i]][j].all_bg_pv));
        			//曝光人次
        			mediaBGUVArray[mediaNameArray[i]].push(parseInt(mediaArray[mediaNameArray[i]][j].all_bg_uv));
    			}else{
        			//点击次数
        			mediaDJArray[mediaNameArray[i]].push(0);
        			//点击人次
        			mediaDJUVArray[mediaNameArray[i]].push(0);
        			//曝光次数
        			mediaBGArray[mediaNameArray[i]].push(0);
        			//曝光人次
        			mediaBGUVArray[mediaNameArray[i]].push(0);
        		}
        	}
    	}
    	
    	var djNumDayChart = echarts.init(document.getElementById("dj_num_day_chart"),"macarons");
    	djNumOption = {
			    tooltip : {
			        trigger: 'axis'
			    },
			    legend: {
			        data: mediaNameArray,
			        itemGap:1
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            boundaryGap : false,
			            data : dateArray
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value'
			        }
			    ],
			    series : [{}],
			    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
			};
    	
    	var seriesOne = [];
    	for(var i = 0;i<mediaNameArray.length;i++){
    		seriesOne.push(
    			{
				    name: mediaNameArray[i],
	 	            type:'line',
	 	            data: mediaDJArray[mediaNameArray[i]]
    			}
    		);
    	}
    	if(seriesOne.length == 0){
    		seriesOne = [{}];
    	}
    	djNumOption.series = seriesOne;
    	
    	//点击人次
    	var djPersonDayChart = echarts.init(document.getElementById("dj_person_day_chart"),"macarons");
    	djPersonOption = {
			    tooltip : {
			        trigger: 'axis'
			    },
			    legend: {
			        data:mediaNameArray,
			        itemGap:1
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            boundaryGap : false,
			            data : dateArray
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value'
			        }
			    ],
			    series : [{}],
			    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
			};
    	var seriesDJUV = [];
    	for(var i = 0;i<mediaNameArray.length;i++){
    		seriesDJUV.push(
    			{
				    name: mediaNameArray[i],
	 	            type:'line',
	 	            data: mediaDJUVArray[mediaNameArray[i]]
    			}
    		);
    	}
    	if(seriesDJUV.length == 0){
    		seriesDJUV = [{}];
    	}
    	djPersonOption.series = seriesDJUV;
    	
    	
    	//曝光次数
    	var bgNumDayChart = echarts.init(document.getElementById("bg_num_day_chart"),"macarons");
    	bgNumOption = {
			    tooltip : {
			        trigger: 'axis'
			    },
			    legend: {
			        data:mediaNameArray,
			        itemGap:1
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            boundaryGap : false,
			            data : dateArray
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value'
			        }
			    ],
			    series : [{}],
			    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
			};
    	
    	var seriesBG = [];
    	for(var i = 0;i<mediaNameArray.length;i++){
    		seriesBG.push(
    			{
				    name: mediaNameArray[i],
	 	            type:'line',
	 	            data: mediaBGArray[mediaNameArray[i]]
    			}
    		);
    	}
    	if(seriesBG.length == 0){
    		seriesBG = [{}];
    	}
    	bgNumOption.series = seriesBG;
    	
    	//曝光人数
    	var bgPersonDayChart = echarts.init(document.getElementById("bg_person_day_chart"),"macarons");
    	bgPersonOption = {
			    tooltip : {
			        trigger: 'axis'
			    },
			    legend: {
			        data:mediaNameArray,
			        itemGap:1
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            boundaryGap : false,
			            data : dateArray
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value'
			        }
			    ],
			    series : [{}],
			    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
			};
    	
    	var seriesBGUV = [];
    	for(var i = 0;i<mediaNameArray.length;i++){
    		seriesBGUV.push(
    			{
				    name: mediaNameArray[i],
	 	            type:'line',
	 	            data: mediaBGUVArray[mediaNameArray[i]]
    			}
    		);
    	}
    	
    	if(seriesBGUV.length == 0){
    		seriesBGUV = [{}];
    	}
    	
    	bgPersonOption.series = seriesBGUV;
    	
    	djNumDayChart.setOption(djNumOption);
    	djPersonDayChart.setOption(djPersonOption);
    	bgNumDayChart.setOption(bgNumOption);
    	bgPersonDayChart.setOption(bgPersonOption);
    	
    	var chartMap = {"djNumDayChart":djNumDayChart,"djPersonDayChart":djPersonDayChart,"bgNumDayChart":bgNumDayChart,"bgPersonDayChart":bgPersonDayChart};
    	return chartMap;
    },
    diversionHourCharts : function (hourList){
    	var mediaArray = {};
    	var mediaNameArray = [];
    	var mediaDJArray = {};
    	var mediaDJUVArray = {};
    	var mediaBGArray = {};
    	var mediaBGUVArray = {};
    	hourArray = ['00','01','02','03','04','05',
		              '06','07','08','09','10','11',
		              '12','13','14','15','16','17',
		              '18','19','20','21','22','23'];
		 
		hourArrayRuled = ['00:00','01:00','02:00','03:00','04:00','05:00',
		              '06:00','07:00','08:00','09:00','10:00','11:00',
		              '12:00','13:00','14:00','15:00','16:00','17:00',
		              '18:00','19:00','20:00','21:00','22:00','23:00'];
		//将小时为一位的 变为两位
		$.each(hourList, function(i, value){
			if("hour" in value){
				var hour = value.hour;
				if(hour == "0" || hour == "1" || hour == "2" || hour == "3" || hour == "4" || hour == "5" || hour == "6" || hour == "7" || hour == "8" || hour == "9"){
					hourList[i].hour = "0" + hour;
				}
			}
		});
    	
		$.each(hourList, function(i, value){
			if("hour" in value && "media_name" in value){
				var media = value.media_name;
	    		if(mediaArray[media] == null){
	    			mediaArray[media] = [];
	    			mediaNameArray.push(media);
	    		}
	    		mediaArray[media].push(hourList[i]);
			}
    	});
    	
    	mediaNameArray.sort();
    	
    	//点击次数
    	for(var index = 0;index<hourArray.length;index++){
    		for(var i = 0;i<mediaNameArray.length;i++){
    			var existFlag = false;
        		for(var j = 0;j<mediaArray[mediaNameArray[i]].length;j++){
        			if(hourArray[index] == mediaArray[mediaNameArray[i]][j].hour){
        				if(mediaDJArray[mediaNameArray[i]] == null){
                			mediaDJArray[mediaNameArray[i]] = [];
                		}
        				if(mediaDJUVArray[mediaNameArray[i]] == null){
                			mediaDJUVArray[mediaNameArray[i]] = [];
                		}
        				if(mediaBGArray[mediaNameArray[i]] == null){
                			mediaBGArray[mediaNameArray[i]] = [];
                		}
        				if(mediaBGUVArray[mediaNameArray[i]] == null){
                			mediaBGUVArray[mediaNameArray[i]] = [];
                		}
                		mediaDJArray[mediaNameArray[i]].push(parseInt(mediaArray[mediaNameArray[i]][j].all_dj_pv));
                		mediaDJUVArray[mediaNameArray[i]].push(parseInt(mediaArray[mediaNameArray[i]][j].all_dj_uv));
                		mediaBGArray[mediaNameArray[i]].push(parseInt(mediaArray[mediaNameArray[i]][j].all_bg_pv));
                		mediaBGUVArray[mediaNameArray[i]].push(parseInt(mediaArray[mediaNameArray[i]][j].all_bg_uv));
                		existFlag = true;
        				break;
        			}
        		}
        		if(!existFlag){
        			if(mediaDJArray[mediaNameArray[i]] == null){
            			mediaDJArray[mediaNameArray[i]] = [];
            		}
    				if(mediaDJUVArray[mediaNameArray[i]] == null){
            			mediaDJUVArray[mediaNameArray[i]] = [];
            		}
    				if(mediaBGArray[mediaNameArray[i]] == null){
            			mediaBGArray[mediaNameArray[i]] = [];
            		}
    				if(mediaBGUVArray[mediaNameArray[i]] == null){
            			mediaBGUVArray[mediaNameArray[i]] = [];
            		}
        			mediaDJArray[mediaNameArray[i]].push(0);
        			mediaDJUVArray[mediaNameArray[i]].push(0);
            		mediaBGArray[mediaNameArray[i]].push(0);
            		mediaBGUVArray[mediaNameArray[i]].push(0);
        		}
        	}
    	}
		
    	var djNumHourChart = echarts.init(document.getElementById("dj_num_hour_chart"),"macarons");
    	djNumOption = {
			    tooltip : {
			        trigger: 'axis'
			    },
			    legend: {
			        data:mediaNameArray,
			        itemGap:1
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            boundaryGap : false,
			            data : hourArrayRuled
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value'
			        }
			    ],
			    series : [{}],
			    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
			};
    	
    	var seriesDJ = [];
    	for(var i = 0;i<mediaNameArray.length;i++){
    		seriesDJ.push(
    			{
    				name: mediaNameArray[i],
	 	            type:'line',
	 	            data: mediaDJArray[mediaNameArray[i]],
	 	            markPoint : {
		                data : [
		                    {type : 'max', name: '24小时最高'},
		                ]
		            }
    			}
    		);
    	}
    	
    	if(seriesDJ.length == 0){
    		seriesDJ = [{}];
    	}
    	
    	djNumOption.series = seriesDJ;
    	
    	
    	var djPersonHourChart = echarts.init(document.getElementById("dj_person_hour_chart"),"macarons");
    	djPersonOption = {
			    tooltip : {
			        trigger: 'axis'
			    },
			    legend: {
			        data:mediaNameArray,
			        itemGap:1
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            boundaryGap : false,
			            data : hourArrayRuled
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value'
			        }
			    ],
			    series : [{}],
			    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
			};
    	
    	var seriesDJUV = [];
    	for(var i = 0;i<mediaNameArray.length;i++){
    		seriesDJUV.push(
    			{
    				name: mediaNameArray[i],
	 	            type:'line',
	 	            data: mediaDJUVArray[mediaNameArray[i]],
	 	            markPoint : {
		                data : [
		                    {type : 'max', name: '24小时最高'},
		                ]
		            }
    			}
    		);
    	}
    	
    	if(seriesDJUV.length == 0){
    		seriesDJUV = [{}];
    	}
    	
    	djPersonOption.series = seriesDJUV;
    	
    	
    	var bgNumHourChart = echarts.init(document.getElementById("bg_num_hour_chart"),"macarons");
    	bgNumOption = {
			    tooltip : {
			        trigger: 'axis'
			    },
			    legend: {
			        data:mediaNameArray,
			        itemGap:1
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            boundaryGap : false,
			            data : hourArrayRuled
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value'
			        }
			    ],
			    series : [{}],
			    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
			};
    	
    	var seriesBG = [];
    	for(var i = 0;i<mediaNameArray.length;i++){
    		seriesBG.push(
    			{
    				name: mediaNameArray[i],
	 	            type:'line',
	 	            data: mediaBGArray[mediaNameArray[i]],
	 	            markPoint : {
		                data : [
		                    {type : 'max', name: '24小时最高'},
		                ]
		            }
    			}
    		);
    	}
    	
    	if(seriesBG.length == 0){
    		seriesBG = [{}];
    	}
    	
    	bgNumOption.series = seriesBG;
    	
    	var bgPersonHourChart = echarts.init(document.getElementById("bg_person_hour_chart"),"macarons");
    	bgPersonOption = {
			    tooltip : {
			        trigger: 'axis'
			    },
			    legend: {
			        data:mediaNameArray,
			        itemGap:1
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            boundaryGap : false,
			            data : hourArrayRuled
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value'
			        }
			    ],
			    series : [{}],
			    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
			};
    	
    	var seriesBGUV = [];
    	for(var i = 0;i<mediaNameArray.length;i++){
    		seriesBGUV.push(
    			{
    				name: mediaNameArray[i],
	 	            type:'line',
	 	            data: mediaBGUVArray[mediaNameArray[i]],
	 	            markPoint : {
		                data : [
		                    {type : 'max', name: '24小时最高'},
		                ]
		            }
    			}
    		);
    	}
    	
    	if(seriesBGUV.length == 0){
    		seriesBGUV = [{}];
    	}
    	
    	bgPersonOption.series = seriesBGUV;
    	
    	djNumHourChart.setOption(djNumOption);
    	djPersonHourChart.setOption(djPersonOption);
    	bgNumHourChart.setOption(bgNumOption);
    	bgPersonHourChart.setOption(bgPersonOption);
    	
    	var chartMap = {"djNumHourChart":djNumHourChart,"djPersonHourChart":djPersonHourChart,"bgNumHourChart":bgNumHourChart,"bgPersonHourChart":bgPersonHourChart};
    	return chartMap;
    },
    overallConversionCharts : function (id,totalTran){
    	var dateArray = [];
    	var rateArray = [];
    	
    	$.each(totalTran,function(i,value){
    		dateArray.push(value.pt_d);
    		rateArray.push(value.totalTranRate);
    	});
    	
    	var overallConversionChart = echarts.init(document.getElementById(id),"macarons");
    	option = {
    			title : {
    		        subtext: '整体转化',
    		    },
			    tooltip : {
    		        trigger: 'axis',
    		        formatter: "{b} : {c}%"
    		    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            boundaryGap : false,
			            data : dateArray
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value',
			            axisLabel: {
			                  show: true,
			                  interval: 'auto',
			                  formatter: '{value} %'
			                },
			            max : 100,
			            min : 0
			        }
			    ],
			    series : [
			        {
			            name:'整体转化',
			            type:'line',
//			            itemStyle: {
//			                normal: {
//			                	label:{ 
//				                	show: true,
//				                	formatter : '{c} %'
//			                	}
//			                },
//			            },
			            data:rateArray
			        }
			    ],
    		    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
			};
    	overallConversionChart.setOption(option);
    	
    	return overallConversionChart;
    },
    classificationCharts : function (classification) {
    	var dateArray = [];
    	var mediaNameArray = [];
    	var mediaArray = {};
    	var bgDjArray = [];
    	var djDdArray = [];
    	var ddXdArray = [];
    	var xdGmArray = [];
    	
    	//得到日期并去重
    	$.each(classification, function(i, value){
			var date = value.time;
    		var flag = dateArray.indexOf(date);
    		if(flag == -1){
    			dateArray.push(date);
    		}
    	});
    	dateArray.sort();
    	
    	//得到媒体
    	$.each(classification, function(i, value){
    		if("medaiName" in value){
    			var media = value.medaiName;
        		if(mediaArray[media] == null){
        			mediaArray[media] = [];
        			mediaNameArray.push(media);
        		}
        		mediaArray[media].push(classification[i]);
    		}
    	});
    	
    	//点击次数
    	for(var index = 0;index<dateArray.length;index++){
    		for(var i = 0;i<mediaNameArray.length;i++){
    			var existFlag = false;
        		for(var j = 0;j<mediaArray[mediaNameArray[i]].length;j++){
        			if(dateArray[index] == mediaArray[mediaNameArray[i]][j].time){
        				if(bgDjArray[mediaNameArray[i]] == null){
        					bgDjArray[mediaNameArray[i]] = [];
                		}
        				if(djDdArray[mediaNameArray[i]] == null){
        					djDdArray[mediaNameArray[i]] = [];
                		}
        				if(ddXdArray[mediaNameArray[i]] == null){
        					ddXdArray[mediaNameArray[i]] = [];
                		}
        				if(xdGmArray[mediaNameArray[i]] == null){
        					xdGmArray[mediaNameArray[i]] = [];
                		}
        				bgDjArray[mediaNameArray[i]].push(parseFloat(mediaArray[mediaNameArray[i]][j].ctr));
                		djDdArray[mediaNameArray[i]].push(parseFloat(mediaArray[mediaNameArray[i]][j].landingRate));
                		ddXdArray[mediaNameArray[i]].push(parseFloat(mediaArray[mediaNameArray[i]][j].orderRate));
                		xdGmArray[mediaNameArray[i]].push(parseFloat(mediaArray[mediaNameArray[i]][j].buyRate));
                		existFlag = true;
        				break;
        			}
        		}
        		if(!existFlag){
        			if(bgDjArray[mediaNameArray[i]] == null){
        				bgDjArray[mediaNameArray[i]] = [];
            		}
    				if(djDdArray[mediaNameArray[i]] == null){
    					djDdArray[mediaNameArray[i]] = [];
            		}
    				if(ddXdArray[mediaNameArray[i]] == null){
    					ddXdArray[mediaNameArray[i]] = [];
            		}
    				if(xdGmArray[mediaNameArray[i]] == null){
    					xdGmArray[mediaNameArray[i]] = [];
            		}
    				bgDjArray[mediaNameArray[i]].push(0);
    				djDdArray[mediaNameArray[i]].push(0);
    				ddXdArray[mediaNameArray[i]].push(0);
    				xdGmArray[mediaNameArray[i]].push(0);
        		}
        	}
    	}
    	
    	//曝光点击
    	var bgDjChart = echarts.init(document.getElementById("bg_dj_chart"),"macarons");
    	bgDjOption = {
    			title : {
    				subtext: '曝光点击率',
    		    },
    		    tooltip : {
    		        trigger: 'axis',
    		        formatter : function(params) {
    		        	var toltipStr = params[0].name + "</br>";
    		        	for(var i in params){
    		        		toltipStr += params[i].seriesName + ":" + params[i].data + "%</br>";
    		        	}
    		        	return toltipStr;
    		        }
    		    },
			    legend: {
			        data:mediaNameArray,
			        itemGap:1
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            boundaryGap : false,
			            data : dateArray
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value',
			            axisLabel: {
			                  show: true,
			                  interval: 'auto',
			                  formatter: '{value} %'
			                },
			            max : 100,
			            min : 0
			        }
			    ],
			    series : [{}],
    		    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
			};
    	
    	var seriesBgDj = [];
    	for(var i = 0;i<mediaNameArray.length;i++){
    		seriesBgDj.push(
    			{
    				name: mediaNameArray[i],
	 	            type:'line',
	 	            data: bgDjArray[mediaNameArray[i]]
    			}
    		);
    	}
    	
    	if(seriesBgDj.length == 0){
    		seriesBgDj = [{}];
    	}
    	bgDjOption.series = seriesBgDj;
    	
    	bgDjChart.setOption(bgDjOption);
    	
    	//点击到达率
    	var djDdChart = echarts.init(document.getElementById("dj_dd_chart"),"macarons");
    	djDdOption = {
    			title : {
    				subtext: '点击到达率',
    		    },
    		    tooltip : {
    		        trigger: 'axis',
    		        formatter : function(params) {
    		        	var toltipStr = params[0].name + "</br>";
    		        	for(var i in params){
    		        		toltipStr += params[i].seriesName + ":" + params[i].data + "%</br>";
    		        	}
    		        	return toltipStr;
    		        }
    		    },
			    legend: {
			        data:mediaNameArray,
			        itemGap:1
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            boundaryGap : false,
			            data : dateArray
			        }
			    ],
			    yAxis : [
			        {
			        	type : 'value',
			            axisLabel: {
			                  show: true,
			                  interval: 'auto',
			                  formatter: '{value} %'
			                },
			            max : 100,
			            min : 0
			        }
			    ],
			    series : [{}],
    		    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
			};
    	
    	var seriesDjDd = [];
    	for(var i = 0;i<mediaNameArray.length;i++){
    		seriesDjDd.push(
    			{
    				name: mediaNameArray[i],
	 	            type:'line',
	 	            data: djDdArray[mediaNameArray[i]]
    			}
    		);
    	}
    	
    	if(seriesDjDd.length == 0){
    		seriesDjDd = [{}];
    	}
    	
    	djDdOption.series = seriesDjDd;
    	
    	djDdChart.setOption(djDdOption);
    	
    	//到达下单
    	var ddXdChart = echarts.init(document.getElementById("dd_xd_chart"),"macarons");
    	ddXdOption = {
    			title : {
    				subtext: '到达下单率',
    		    },
    		    tooltip : {
    		        trigger: 'axis',
    		        formatter : function(params) {
    		        	var toltipStr = params[0].name + "</br>";
    		        	for(var i in params){
    		        		toltipStr += params[i].seriesName + ":" + params[i].data + "%</br>";
    		        	}
    		        	return toltipStr;
    		        }
    		    },
			    legend: {
			        data:mediaNameArray,
			        itemGap:1
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            boundaryGap : false,
			            data : dateArray
			        }
			    ],
			    yAxis : [
			        {
			        	type : 'value',
			            axisLabel: {
			                  show: true,
			                  interval: 'auto',
			                  formatter: '{value} %'
			                },
			            max : 100,
			            min : 0
			        }
			    ],
			    series : [{}],
    		    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
			};
    	
    	var seriesDdXd = [];
    	for(var i = 0;i<mediaNameArray.length;i++){
    		seriesDdXd.push(
    			{
    				name: mediaNameArray[i],
	 	            type:'line',
	 	            data: ddXdArray[mediaNameArray[i]]
    			}
    		);
    	}
    	
    	if(seriesDdXd.length == 0){
    		seriesDdXd = [{}];
    	}
    	
    	ddXdOption.series = seriesDdXd;
    	
    	ddXdChart.setOption(ddXdOption);
    	
    	//下单购买
    	var xdGmChart = echarts.init(document.getElementById("xd_gm_chart"),"macarons");
    	xdGmOption = {
    			title : {
    				subtext: '下单购买率',
    		    },
    		    tooltip : {
    		        trigger: 'axis',
    		        formatter : function(params) {
    		        	var toltipStr = params[0].name + "</br>";
    		        	for(var i in params){
    		        		toltipStr += params[i].seriesName + ":" + params[i].data + "%</br>";
    		        	}
    		        	return toltipStr;
    		        }
    		    },
			    legend: {
			        data:mediaNameArray,
			        itemGap:1
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            boundaryGap : false,
			            data : dateArray
			        }
			    ],
			    yAxis : [
			        {
			        	type : 'value',
			            axisLabel: {
			                  show: true,
			                  interval: 'auto',
			                  formatter: '{value} %'
			                },
			            max : 100,
			            min : 0
			        }
			    ],
			    series : [{}],
    		    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
			};
    	
    	var seriesXdGm = [];
    	for(var i = 0;i<mediaNameArray.length;i++){
    		seriesXdGm.push(
    			{
    				name: mediaNameArray[i],
	 	            type:'line',
	 	            data: xdGmArray[mediaNameArray[i]]
    			}
    		);
    	}
    	
    	if(seriesXdGm.length == 0){
    		seriesXdGm = [{}];
    	}
    	
    	xdGmOption.series = seriesXdGm;
    	
    	xdGmChart.setOption(xdGmOption);
    	
    	var map = {"bgDjChart":bgDjChart,"djDdChart":djDdChart,"ddXdChart":ddXdChart,"xdGmChart":xdGmChart};
    	return map;
    },
    genderCharts : function (id , genderData){
    	var genderChart = echarts.init(document.getElementById(id),"macarons");
    	
    	genderOption = {
    		    title : {
    		        subtext: '性别比例',
    		    },
    		    tooltip : {
    		        trigger: 'item',
    		        formatter: "{a} <br/>{b} : {c} ({d}%)"
    		    },
    		    legend: {
    		        data:['男','女']
    		    },
    		    toolbox: {
    		        show : true,
    		        feature : {
    		            saveAsImage : {show: true}
    		        }
    		    },
    		    calculable : true,
    		    series : [
    		        {
    		            name:'性别比例',
    		            type:'pie',
    		            center: ['50%', '60%'],
    		            data:genderData
    		        }
    		    ],
			    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
    		};
    	genderChart.setOption(genderOption);
    	
    	return genderChart;
    },
    ageCharts : function (id , ageData){
    	var ageArray = [];
    	var rateArray = [];
    	if(ageData != null){
    		$.each(ageData , function (i , value) {
    			if("name" in value && "value" in value){
    				ageArray.push(value.name);
            		rateArray.push(value.value);
    			}
        	});
    	}
    	
    	var ageChart = echarts.init(document.getElementById(id),"macarons");
    	
    	ageOption = {
    		    title : {
    		        subtext: '年龄分布'
    		    },
    		    tooltip : {
    		        trigger: 'axis',
    		        formatter: "{b} : {c}%"
    		    },
    		    toolbox: {
    		        show : true,
    		        feature : {
    		            saveAsImage : {show: true}
    		        }
    		    },
    		    calculable : true,
    		    xAxis : [
    		        {
    		            type : 'value',
    		            axisLabel: {
    		                show: true,
    		                interval: 'auto',
    		                formatter: '{value} %'
    		            }
    		        }
    		    ],
    		    yAxis : [
    		        {
    		            type : 'category',
    		            data : ageArray
    		        }
    		    ],
    		    series : [
    		        {
    		            name:'年龄分布',
    		            type:'bar',
    		            data:rateArray
    		        }
    		    ],
			    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
    		};
    	ageChart.setOption(ageOption);
    	
    	return ageChart;
    },
    mediaTrajectoryCharts : function (id , mediaTrajectoryData) {
    	var mediaArray = [];
    	var mediaArrayTmp = [];
    	var rateArray = [];
    	if(mediaTrajectoryData != null){
    		$.each(mediaTrajectoryData,function (i , value) {
    			if("name" in value && "value" in value){
    				var name  = value.name;
    				if(name.length>5){
    					name = name.substring(0,5) + "...";
    				}
    				mediaArray.push(name);
    				mediaArrayTmp.push(value.name);
            		rateArray.push(value.value);
    			}
        	});
    	}
    	
    	var mediaTrajectoryChart = echarts.init(document.getElementById(id),"macarons");
    	option = {
    		    tooltip : {
    		        trigger: 'axis',
    		        //formatter: "{b} : {c}%"
    		        formatter : function(params) {
    		        	var name = "";
    		        	for(var i in mediaArrayTmp){
    		        		if(mediaArrayTmp[i] == params[0].name){
    		        			name = mediaArrayTmp[i];
    		        		}else if(mediaArrayTmp[i].substring(0,5) == params[0].name.substring(0,5)){
    		        			name = mediaArrayTmp[i];
    		        		}
    		        	}
    		        	return name + ":" + params[0].data + "%";
    		        }
    		    },
    		    toolbox: {
    		        show : true,
    		        feature : {
    		            saveAsImage : {show: true}
    		        }
    		    },
    		    calculable : true,
    		    xAxis : [
    		        {
    		            type : 'category',
    		            data : mediaArray,
    	 	            axisLabel:{
                        	interval:0,
                        	rotate:30,
                        	margin:2
    	 	            }
    		        }
    		    ],
    		    yAxis : [
    		        {
    		            type : 'value',
    		            axisLabel: {
    		                show: true,
    		                interval: 'auto',
    		                formatter: '{value} %'
    		              }
    		        }
    		    ],
    		    series : [{
    		    	name: "百分比",
		            type:'bar',
		            data: rateArray
    		    }],
    		    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
    		};
    	mediaTrajectoryChart.setOption(option);
    	
    	return mediaTrajectoryChart;
    },
    industryOrientationCharts : function (id , industryOrientationData) {
    	var industryArray = [];
    	var industryArrayTmp = [];
    	var rateArray = [];
    	if(industryOrientationData != null){
    		$.each(industryOrientationData , function (i , value) {
    			if("name" in value && "value" in value){
    				var name = value.name;
    				if(name.length>5){
    					name = name.substring(0,5) + "...";
    				}
    				industryArray.push(name);
    				industryArrayTmp.push(value.name);
    				rateArray.push(value.value);
        		}
        	});
    	}
    	
    	var industryOrientationChart = echarts.init(document.getElementById(id),"macarons");
    	option = {
    		    tooltip : {
    		        trigger: 'axis',
    		        //formatter: "{b} : {c}%"
    		        formatter : function(params) {
    		        	var name = "";
    		        	for(var i in industryArrayTmp){
    		        		if(industryArrayTmp[i] == params[0].name){
    		        			name = industryArrayTmp[i];
    		        		}else if(industryArrayTmp[i].substring(0,5) == params[0].name.substring(0,5)){
    		        			name = industryArrayTmp[i];
    		        		}
    		        	}
    		        	return name + ":" + params[0].data + "%";
    		        }
    		    },
    		    toolbox: {
    		        show : true,
    		        feature : {
    		            saveAsImage : {show: true}
    		        }
    		    },
    		    calculable : true,
    		    xAxis : [
    		        {
    		            type : 'category',
    		            data : industryArray,
    	 	            axisLabel:{
                        	interval:0,
                        	rotate:30,
                        	margin:2
    	 	            }
    		        }
    		    ],
    		    yAxis : [
    		        {
    		            type : 'value',
    		            axisLabel: {
    		                show: true,
    		                interval: 'auto',
    		                formatter: '{value} %'
    		            }
    		        }
    		    ],
    		    series : [
    		        {
    		            name : '百分比',
    		            type : 'bar',
    		            data : rateArray
    		        }
    		    ],
    		    noDataLoadingOption: {
                    text: '暂无数据',
                    effect: 'whirling'
    		    }
    		};
    	industryOrientationChart.setOption(option);
    	
    	return industryOrientationChart;
    }
};