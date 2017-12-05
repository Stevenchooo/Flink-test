define("modules/base/navi", ['jquery', 'modules/base/user'],
	function(require, exports, module) {
	require("jquery");
	require("dialog");
	var user = require("modules/base/user");
    
    
    
    var mMetas = null;
    
    var curActNameList = "";
    var curActIdList   = "";
    var curWebNameList = "";
    var curWebIdList   = "";
    var curwebNames = null;
    var curQueryAdInfoPort = null;
    var curQueryAdInfoPlatform = null;
    var curQueryInputUser = null;
    var curInputCid = null;
    var curQueryDate = null;
    var curInputSid = null;
    
    
    
    exports.setcurActNameList = function(param) {
    	curActNameList = param;
    }
    exports.getcurActNameList = function() {
        return curActNameList;
    }
    exports.setcurActIdList = function(param) {
    	curActIdList = param;
    }
    exports.getcurActIdList = function() {
        return curActIdList;
    }
    
    exports.setcurWebNameList = function(param) {
    	curWebNameList = param;
    }
    exports.getcurWebNameList = function() {
    	return curWebNameList;
    }
       
    exports.setcurWebIdList = function(param) {
    	curWebIdList = param;
    }
    exports.getcurWebIdList = function() {
        return curWebIdList;
    }
    
    exports.setcurwebNames = function(param) {
    	curwebNames = param;
    }
    exports.getcurwebNames = function() {
        return curwebNames;
    }
    
    exports.setcurQueryAdInfoPort = function(param) {
    	curQueryAdInfoPort = param;
    }
    exports.getcurQueryAdInfoPort = function() {
        return curQueryAdInfoPort;
    }
       
    exports.setcurQueryAdInfoPlatform = function(param) {
    	curQueryAdInfoPlatform = param;
    }
    exports.getcurQueryAdInfoPlatform = function() {
        return curQueryAdInfoPlatform;
    }
    
    exports.setcurQueryInputUser = function(param) {
    	curQueryInputUser = param;
    }
    exports.getcurQueryInputUser = function() {
        return curQueryInputUser;
    }
        
    exports.setcurInputCid = function(param) {
    	curInputCid = param;
    }
    exports.getcurInputCid = function() {
        return curInputCid;
    }
    exports.setcurQueryDate = function(param) {
    	curQueryDate = param;
    }
    exports.getcurQueryDate = function() {
        return curQueryDate;
    }
    
    exports.setcurInputSid = function(param) {
    	curInputSid = param;
    }
    exports.getcurInputSid = function() {
        return curInputSid;
    }
    
    
    
	exports.showList = function(account) 
	{
		//判断是否是荣耀中心发来的请求
		var honorFlag = false;
	    var apiUrl = "/api/generalSituat/GetHonorSession";
		var params = {
				"__userKey" : __userKey
			};
		$.ajax({
			type : "POST",
			url : __webRoot + apiUrl,
			async : false,
			data : params,
			success : function (resp) {
				if (resp.resultCode != 0) {
					return;
				}
				var falg = resp.flag;
				//隐藏标题
				if(falg){
					honorFlag = true;
				}
			}
		});
		
		curAccount = account;
		if (mMetas == null) 
		{
			mMetas = {};
			var url = "/api/meta/query";
			var params = {};
		     $.ajax({type:"GET", url: __webRoot+"/api/meta/query", async:false, 
		            success: function(res) {
		                if(res.resultCode != 0) {
		                    return;
		                }
		        		var m, len = res.metas.length;
		        		
		        		for(var i = 0; i < len; i++) {
		        			m = res.metas[i];
		        			mMetas[m.name] = m;
		        		}
		        		
		        		var subs;
		        		for(var i = 0 in mMetas) {
		        			m = mMetas[i];
		        			if(m.subMetas && m.subMetas.length > 0) { 
		        				subs = m.subMetas.substr(1, m.subMetas.length - 2).split('|');//第一个、最后一个是'|'
		        				m.subMetas = [];
		        				
			        			for(var j = 0, num=subs.length; j < num; j++) {
			        				if(mMetas[subs[j]]) {
				        				m.subMetas.push(mMetas[subs[j]]);
			        				}
			        			}
		        			}
		        		}
		            }
		        });
		}
		
		
	       var url = __webRoot+"/api/admin/models";
	       
	       var modelsParams = {
					"__userKey" : __userKey
		   };
			//获得当前管理的项
	        $.ajax({type:"POST", url: url, 
	        	data : modelsParams,
	            success: function(res) {
	                if(res.resultCode != 0) 
	                {
	                    return;
	                }
	                
            		var m, len = res.models.length;
            		
            		var treeNodes = new Array();
            		
            		for(var i = 0; i < len; i++) 
            		{
            			m = res.models[i];
            			
            			if (m.isParent && m.pid != 0) 
            			{
            				var node = 
            				{
            					id: m.id,
            					pid: m.pid,
	            				name:getLocalTag("menu." + m.name, m.name),
	            				meta:m.meta,
	            				childs: []
	            			};
            				
            				for (var j = 0; j < len; j++) 
            				{
            					n = res.models[j];
            					if (n.pid == m.id) 
            					{
            						node.childs.push({
            							meta:n.meta,
	            						name:getLocalTag("menu." + n.name, n.name),
    	            					url: mMetas[n.meta].url,
    	            					id: n.id,
            							pid: n.pid
	            					});
            					}
            					
            				}
            				
            				treeNodes.push(node);
            			}
            		}
            		
            		
            		//刷新页面
            		//默认展示的页面
            		var curNode = null;
            		treeNodesLength = treeNodes.length;
            		var permissionsFlag = true;
            		if(treeNodesLength == 0){
            			window.location.href = "https://emui.huawei.com/d/BIPortal/noprivilege.jsp";
            		}else{
            			for(var i = 0 ; i < treeNodesLength; i++)
            			{
                			var nodes = treeNodes[i];
                			var id = nodes.id;
                			var childs = nodes.childs;
                			var name = nodes.name;
                			if(name == "图表分析"){
                				permissionsFlag = false;
                			}
                			var html = "<li id='p_" +  id +"' class='un-item ng-scope'><a href='javascript:void(0);' class='uni-link uni-expand ng-binding'>" + name +"<b></b></a>";
                			var childsLength = childs.length;
                			for(var j = 0; j < childsLength; j++)
            				{
                				
                				var childsNode = childs[j];
                				var c_id = childsNode.id;
                				var c_name = childsNode.name;
                				var c_url = childsNode.url;
                				var c_meta = childsNode.meta;
                				html = html + " <div class='uni-sub ng-scope'><a id='c_" + c_id+ "' class='unis-link ng-binding' href='#' onClick=showListPage('"+c_id+"','" + c_url+"','" + c_meta+ "')>"+ c_name +"</a></div>";
                				if(curNode == null){
                					curNode = {id: c_id, meta: c_meta, url: c_url,pid:id};
                				}else if(c_id == 13){
                					curNode = {id: c_id, meta: c_meta, url: c_url,pid:id};
                				}
            				}
                			
                			html = html + "</li>";
                			$("#nav-bar").append(html);
            			}
                		firstPage(curNode.pid,curNode.id);
                		
                		//判断是否是荣耀中心发来的请求
                		if(honorFlag){
                		//if(true){
                			$("#nav-bar").hide();
                			if(permissionsFlag){
                				window.location.href = "https://emui.huawei.com/d/BIPortal/noprivilege.jsp";
                			}else{
                				showListPage('150','/page/generalSituat','generalSituat');
                			}
                		}else{
                			$(".up-topbar").show();
                			$(".upm-cont").css("margin-left","180px");
                			showListPage(curNode.id,curNode.url,curNode.meta);
                		}
            		}
	            }

	        });
	        
	        upmobui.common.pageFunc();
	};
	
	$("#btn_logout").click(function(){
        user.logout();
    });
	
	firstPage = function(pid, id) {
		$("#p_" + pid).addClass("uni-cur");
		$(".unis-link").removeClass("unis-cur");
		$("#p_" + pid).children(".uni-sub").children("#c_" + id).addClass("unis-cur");
	}
	
	showListPage = function(id, url,meta) {
		$(".unis-link").removeClass("unis-cur");
		$("#c_"+id).addClass("unis-cur");
		
		
		var childs = $("#naviContainer").parent().children();
		
		for(var i = 2; i< childs.length;i++)
		{
			childs[i].remove();
		}
		
		$("#listContainer2").empty();
		
		$.ajax({
    		type:"POST",
    		url:__webRoot + url,
    		dataType:"html",
    		data: {"id" : id,"meta" : meta,"__userKey" : __userKey},
        	success: function(res) {
        		$("#listContainer2").html(res);
        	},
        	error : function(res) {
        		window.location.reload();
        	}
    	});
	}
	
});
