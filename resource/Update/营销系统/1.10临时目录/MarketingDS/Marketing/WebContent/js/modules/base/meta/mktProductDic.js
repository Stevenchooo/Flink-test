define(function(require, exports, module) {
    var MetaBase = require('meta');
    require('dialog');
    require('jquery');
    require('calendar');
    require('jquery/calendar/1.0/tinycal.css');
    

    var MetaMktProductDic = {
        
        querySubPara: function($scope, params) {
      
        	$.extend(params, {type:'product',id:null});
        	
            return {
                method: 'GET',
                params:params,
                url: this.webRoot+"/api/mktDic/commonQuery"
            };
        },
        
        removePara: function($scope,pid,dicInfoId) {
            return {
                method: 'GET',
                url: this.webRoot+"/api/mktDic/removeProduct",
                params: {pid:pid,mktDicId:dicInfoId} 
            };
        },
        
        createDialog:function($scope) {
        	$scope.mktDicInfoName = '';
        	return 'id_modify_mktproduct_dialog';
        },
        
        createPara: function($scope, pid, meta) {
        	
        	var mktDicInfoName = $scope.mktDicInfoName;
        	
        	
        	var exists = false;
        	$.ajax({
    		  type:"GET",
    		  url:this.getWebRoot() + "/api/mktDic/exists",
    		  async:false,
    		  data: {
    			  __userKey : __userKey,
    			  mktDicName:mktDicInfoName,
    			  mktDicType:'product'
    		  },
    		  success:function(resp){
                 if(resp.resultCode != 0) {
                     return;
                 }
                 exists = resp.exists;
    		  }
      		});
        	
        	
        	if(exists) {
        		dialog({
        			title:getLocalTag('error', 'error'),
        			content:getLocalTag('mktDicPlatformAlreadyExists', 'platform already exists'),
    	    		okValue: getLocalTag('confirm','Yes'),
        			ok:true
        		}).showModal();
        		return null;
        	}

            return {
                method:"POST",
                url: this.getWebRoot() + "/api/mktDic/create",
                params: {
                	mktDicInfoName:mktDicInfoName,
                	mktDicInfoType : 'product'
                }
            };
        },
        
        createCheck : function($scope) {
            return $scope.mktDicInfoModifyForm.$valid;
        },
        
      
        modifyPara : function($scope, mktDicId) {
			return {
				method : 'POST',
				url : this.getWebRoot() + "/api/mktDic/modify",
				params : {
					pId : mktDicId,
					pName : $scope.mktDicInfoName
				}
			};
		},

		
		modifyCheck : function($scope) {
			
			return $scope.mktDicInfoModifyForm.$valid;
		},

		modifyDialog : function(mktDicId, $scope) {
			$scope.callApi(
							{
								url : this.getWebRoot()
										+ "/api/mktDic/commonQuery",
								method : "GET",
								params : {
									id : mktDicId,
									type:'product'
								}
							})
					.success(
							function(resp, status, headers, config) {
								if (resp.resultCode != 0) {
									$scope.failToOperate(resp);
									return;
								}
								var mktDicInfo = resp.results[0];
								$scope.mktDicInfoName = mktDicInfo.name;
							})
					.error(function(response, status, headers, config) {
						$scope.httpError(status);
					});
			return 'id_modify_mktproduct_dialog';
		},
		
		
        extendScope: function($scope,$http) {
          
        	
        }
        
    };

    exports.instance = function(webRoot, metaName, segments) {
    	var obj = MetaBase.instance(webRoot, metaName, segments);
    	$.extend(obj, MetaMktProductDic);
    	return obj;
    };
});