define(function(require, exports, module) {
    require('jquery');

    var MetaBase = {
        init:function($scope) {
        },
        
        directives:function(app) {
        },
        
        querySubPara: function($scope, params) {
            return {
                method: 'POST',
                data: $.extend(params, {segments:this.segments}),
                url: this.webRoot+"/api/model/querySub"
            };
        },
        
        removePara: function($scope, pid, rowId) {
            return {
                method: 'POST',
                url: this.webRoot+"/api/model/remove",
                data: {pid:pid, id:rowId} 
            };
        },
        
        modifyPara: function($scope, id) {
            return {
                method: 'POST', 
                url: this.webRoot+"/api/model/modify",
                data: {
                    id: id,
                    name: $("#id_model_name").val(),
                    val: $("#id_model_val").val()
                }
            };
        },
        
        createPara: function($scope, pid, meta) {
            return {
                method:"POST",
                url: this.webRoot+"/api/model/create",
                data: {
                    pid: pid,
                    name: $("#id_model_name").val(),
                    type: meta,
                    val: $("#id_model_val").val()
                }
            };
        },
        
        modifyDialog : function(id, $scope) {
        	$scope.callApi({
        		url:this.getWebRoot() + "/api/model/detail",
        		method:"POST",
        		data:{id:id}
    		}).success(function(resp, status, headers, config){
                if(resp.resultCode != 0) {
    	          	$scope.failToOperate(resp);
                    return;
                }
                $scope.opr_name = resp.name;
                $scope.opr_desc = resp.val;
		    }).error(function(response, status, headers, config){
		    	$scope.httpError(status);
			});
            
            return 'id_operation_dialog';
        },
        
        createDialog : function($scope) {
            $scope.opr_name = '';
            $scope.opr_desc = '';
            $scope.oprForm.$setPristine();
            return 'id_operation_dialog';
        },
        
        createCheck : function($scope) {
            return $scope.oprForm.$valid;
        },
        
        modifyCheck : function($scope) {
            return $scope.oprForm.$valid;
        },
        
        getName: function() {
            return this.metaName;
        },
        
        getSegments: function() {
            return this.segments;
        },
        
        getWebRoot:function() {
            return this.webRoot;
        },
        
        //扩展angular环境
        extendScope:function($scope, $http) {
        },
        
        showHttpError : function(status, $scope) {
          	dialog({
          		title: getLocalTag("httpFail", "Network error"),
          		ok: true,
	    		okValue: getLocalTag('confirm','Yes'),
        		cancelValue: getLocalTag('cancel','cancel'),
          		content: getLocalTag("httpFail", "Network error"),
          		lock: true,
          		opacity:0.3
          	}).showModal();
    	},
    	
    	/**
    	 * resp:{resultCode:xxx,info:xxx}
    	 */
    	failToOperate : function(resp, $scope) {
	      	dialog({
	      		title: getLocalTag("failToOperate", "Fail to operate"),
	      		ok: true,
	    		okValue: getLocalTag('confirm','Yes'),
        		cancelValue: getLocalTag('cancel','cancel'),
	      		content: getLocalReason(resp.resultCode, resp.info),
	      		lock: true,
	      		opacity:0.3
	      	}).showModal();
    	},
    	
    	/**
    	 * resp:{resultCode:xxx,info:xxx}
    	 */
    	successToOperate : function(resp, $scope) {
	      	dialog({
	      		title: getLocalTag("successToOperate", "Success to operate"),
	      		ok: true,
	    		okValue: getLocalTag('confirm','Yes'),
        		cancelValue: getLocalTag('cancel','cancel'),
	      		content: getLocalReason(resp.resultCode, resp.info),
	      		lock: true,
	      		opacity:0.3
	      	}).showModal();
    	},
    	
    	/**
    	 * 查询后执行，用来控制结果显示的效果
    	 */
    	afterQuery : function($scope) {
    		return true;
    	},
    	
    	/**
    	 * 显示处理中gif，页面不能操作
    	 */
    	showProcessing : function() {
            if ($("#processing").length <= 0) {
                var process =
                        $("<div class='or_popup_wrap2' id='processing'><div class='processing'></div><div class='process_bar'>"
                                + " <img src='" + this.webRoot + "/imgs/process.gif' width='30' height='30'> </div></div>");
                $("body").append(process);
            }
            $("#processing").show();
        },
        
        /**
         * 隐藏处理中gif
         */
        hideProcessing : function() {
            $("#processing").hide();
        }
    };

    exports.instance = function(webRoot, metaName, segments) {
    	var obj = {webRoot:webRoot, metaName:metaName, segments:segments};
    	$.extend(obj, MetaBase);
    	return obj;
    };
//end of define
});