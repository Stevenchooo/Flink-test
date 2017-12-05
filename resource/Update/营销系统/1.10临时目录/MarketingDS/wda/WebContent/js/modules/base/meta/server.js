define(function(require, exports, module) {
    var MetaBase = require('./meta');
    require('dialog');
    require('jquery');
    require('jquery/dialog/4.1.7/skins/default.css');
    var model = require("modules/base/model");
    
    function initReportDiv(webRoot) {
        require("report");
        require("flot.time");
        if($.browser && $.browser.msie) {
            require("jquery/report/1.1/excanvas");
        }
        
        require('jquery/calendar/1.0/tinycal.js');
        require('jquery/calendar/1.0/tinycal.css');
        $("#button_refresh_report").click(function() {
        	$.fn.calendar({
        		target:"#button_refresh_report",
        		mode:'range',
        		num:2,
	    		okVal: getLocalTag('confirm','Yes'),
        		cancelVal: getLocalTag('cancel','cancel'),
        		ok:function(checked, target) {
        			var start = checked[0].year + '-' + $.addZero(checked[0].month+1) + '-' + $.addZero(checked[0].day); 
        			var end = checked[1].year + '-' + $.addZero(checked[1].month+1) + '-' + $.addZero(checked[1].day); 
                	$("#startDate").val(start);
                	$("#endDate").val(end);
                	
                	refreshReport(
                		webRoot, 
                    	$('#cur_server_id').val(),
                    	start + ' 00:00:00+0800',
                    	end + ' 00:00:00+0800'
                    );
        			return true;
        		}
        	});        	
        });

    }
    
    function refreshReport(webRoot, serverId, startTime, endTime) {
    	model.callApi({type:"POST", url: webRoot+"/api/report/items",
            data:{id:serverId},
            dataType:"json", 
            success: function(resp) {
                if(resp.resultCode != 0) {
                    $.dialog({
                        title:$("#fail_to_query").val(),
                        content:"resultCode=" + resp.resultCode + ",info=" + resp.info
                    });
                    return;
                }
                
                var item;
                var divs = [];
                var num = resp.items.length
                var h = $("#treeContainer").height() - 10;
                if(h > (num * 150 + 150)) {
                	h = num * 150 + 150;
                }
                var listContainer = $("#listContainer"); 
                var w = listContainer.width();
                var offset = listContainer.offset();
                var today = new Date();
                
                for(var i = 0; i < num; i++) {
                    item = resp.items[i];
                    divs.push('<div style="width:100%;font-weight:bold;font-size:14px;" id="name_',item.id,'"></div>'
                              ,'<div id="report_', item.id, '" style="height:130px;width:', (w -20) ,'px;"/>');
                    model.callApi({type:"POST", url: webRoot+"/api/report/data",
                        data:{
                            serverId:serverId,
                            itemId:item.id,
                            startTime:startTime,
                            endTime:endTime
                        },
                        dataType:"json",
                        success:function(resp) {
                            if(resp.resultCode != 0) {
                                $.dialog({
                                    title:$("#fail_to_query").val(),
                                    content:"resultCode=" + resp.resultCode + ",info=" + resp.info
                                });
                                return;
                            }
                            $("#name_" + resp.id).html(resp.name);
                            $.plot("#report_" + resp.id, [resp.data], {
                                xaxis: { mode: "time" }
                            });
                        }
                    }); 
                }
                $("#reportContainer").html(divs.join(''));
                
                
                //追加一个层，使背景变灰
                var reportDiv = $("#reportDiv");
                reportDiv.css("height", h + "px")
                		 .css("width", w+"px")
                         .css("top","0px")
                         .css("left",(offset.left-10)+"px");
                
                reportDiv.fadeIn();
                
                $("#report_close").on("click", function() {
                	$("#reportDiv").fadeOut();
                });
            }
        });
    	
    }
    
    var MetaServer = MetaBase.extend({
        initialize: function(webRoot, metaName, segments) {
        	MetaServer.superclass.initialize.call(this, webRoot, metaName, segments);
        	initReportDiv(webRoot);
        },
        
        extendScope: function($scope) {
        	var opts = $scope.options;
        	var webRoot = opts.webRoot;
	    	$scope.serverReport = function(serverId) {
	    		var today = new Date();
	    		$('#cur_server_id').val(serverId);
	    		var dt = today.getFullYear() + '-' + (today.getMonth()+1) + '-' + today.getDate();
	    		$("#startDate").val(dt);
	    		$("#endDate").val(dt);
	    		refreshReport(
	    			webRoot,
	    			serverId,
	    			dt + ' 00:00:00+0800',
	    			dt + ' 23:59:59+0800'
	    		);
	    	};
        }
    });

    module.exports = MetaServer;
});