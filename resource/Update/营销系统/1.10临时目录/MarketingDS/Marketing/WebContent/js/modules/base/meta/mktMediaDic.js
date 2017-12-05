define(function(require, exports, module) {
    var MetaBase = require('meta');
    require('dialog');
    require('jquery');
    require('calendar');
    require('jquery/calendar/1.0/tinycal.css');
    

    var MetaMktMediaDic = {
        
        querySubPara: function($scope, params) {
      
//        	$.extend(params, {type:'platform'});
        	if ($scope.queryName) {
				$.extend(params, {
					name : $scope.queryName
				});
			}
            return {
                method: 'GET',
                params:params,
                url: this.webRoot+"/api/mktDic/MediaQuery"
            };
        },
        
        removePara: function($scope,pid, info) {
        	var array = info.split('|');
        	var dicPid = array[0];
        	var dicId = array[1];
        	
            return {
                method: 'GET',
                url: this.webRoot+"/api/mktDic/removeMediaWeb",
                params:{pid:pid, webId:dicId,mediaId:dicPid}
            };
        },
        
        createDialog:function($scope) {
        	$scope.mktDicInfoName = '';
        	$scope.dicWebNameCreate ='';
        	$scope.checkWebName = "web";
        	$scope.mktDicInfoModifyForm.$setPristine();
        	
        	//媒体类型下拉框赋值
			$scope.callApi({
				url : this.getWebRoot() + "/api/mktDic/queryDicList",
				method : "GET",
				params : {
					type : "media"
				}
			}).success(function (resp, status, headers, config) {
				if (resp.resultCode != 0) {
					$scope.failToOperate(resp);
					return;
				}
				$scope.medias = resp.results;
				$scope.queryMktDicInfoName = '';
			}).error(function (response, status, headers, config) {
				$scope.httpError(status);
			});
        	
        	return 'id_modify_mktDicInfo_dialog';
        },
        
        createPara: function($scope, pid, meta) {
        	
        	var exists = false;
        	var mktDicInfoMediaName;
        	var mktDicInfoMediaId;
        	var mktDicInfoWebName;
        	var checkedValue = $scope.checkWebName;
        	
			if(typeof(checkedValue) == "undefined" || checkedValue == "")
        	{
        		checkedValue = "web";
        	}
			if(checkedValue != "web")
        	{
				mktDicInfoMediaName = $scope.mktDicInfoName;
				mktDicInfoMediaId = '';
				mktDicInfoWebName = '';
				
				$.ajax({
		    		  type:"POST",
		    		  url:this.getWebRoot() + "/api/mktDic/exists",
		    		  async:false,
		    		  data: {
		    			  __userKey : __userKey,
		    			  mktDicName:mktDicInfoMediaName,
		    			  mktDicId:mktDicInfoMediaId,
		    			  mktDicType:'media'
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
	        			content:getLocalTag('mktDicMediaAlreadyExists', 'media already exists'),
	    	    		okValue: getLocalTag('confirm','Yes'),
	        			ok:true
	        		}).showModal();
	        		return null;
	        	}

	            return {
	                method:"POST",
	                url: this.getWebRoot() + "/api/mktDic/create",
	                params: {
	                	mktDicInfoName:mktDicInfoMediaName,
	                	mktDicInfoType : 'media'
	                }
	            };
        	}
			else
			{
				mktDicInfoMediaId = $scope.queryMktDicInfoName;
				mktDicInfoMediaName = '';
				mktDicInfoWebName = $scope.dicWebNameCreate;
				
				$.ajax({
		    		  type:"POST",
		    		  url:this.getWebRoot() + "/api/mktDic/existsWeb",
		    		  async:false,
		    		  data: {
		    			  __userKey : __userKey,
		    			  mktDicName:mktDicInfoWebName,
		    			  mktDicType:'web'
		    		  },
		    		  success:function(resp){
		                 if(resp.resultCode != 0) {
		                     return;
		                 }
		                 exists = resp.exists;
		    		  }
		      		});
				
				//如果记录存在
	        	if(exists) {
	        		dialog({
	        			title:getLocalTag('error', 'error'),
	        			content:getLocalTag('mktDicWebAlreadyExists', 'web already exists'),
	    	    		okValue: getLocalTag('confirm','Yes'),
	        			ok:true
	        		}).showModal();
	        		return null;
	        	}
	        	
	        	return {
	                method:"POST",
	                url: this.getWebRoot() + "/api/mktDic/create",
	                params: {
	                	mktDicPid:mktDicInfoMediaId,
	    		    	mktDicInfoName:mktDicInfoWebName,
	    		    	mktDicInfoType:'web'
	                }
	            };
    		    	
			}
			
        },
        
        createCheck : function($scope) {
        	var checkedValue = $scope.checkWebName;
			if(typeof(checkedValue) == "undefined" || checkedValue == "")
        	{
        		checkedValue = "web";
        	}
			if(checkedValue == "media")
			{
				return $scope.mktDicInfoModifyForm.mktDicInfoName.$valid;
			}
			else
			{
				if($scope.queryMktDicInfoName !="")
				{
					return $scope.mktDicInfoModifyForm.dicWebNameCreate.$valid;
				}
				else
				{
					return false;
				}
			}
            
        },
        
        modifyCheck : function($scope) {
        	var checkedValue = $scope.checkWebName;
			if(typeof(checkedValue) == "undefined" || checkedValue == "")
        	{
        		checkedValue = "web";
        	}
			if(checkedValue == "media")
			{
				return $scope.mktDicInfoModifyForm.mktDicInfoName.$valid;
			}
			else
			{
				if($scope.queryMktDicInfoName !="")
				{
					return $scope.mktDicInfoModifyForm.dicWebNameCreate.$valid;
				}
				else
				{
					return false;
				}
			}
        },
        
        modifyPara: function($scope, dicWebId, pid) {
        	
        	var array = dicWebId.split('|');
        	var dicWebPid = array[0];
        	var dicWebId = array[1];
        	var dicWebName = array[2];
            var exists = false;
        	var mktDicInfoMediaName;
        	var mktDicInfoMediaId;
        	var mktDicInfoWebId;
        	var mktDicInfoWebName;
        	var checkedValue = $scope.checkWebName;
        	
			if(typeof(checkedValue) == "undefined" || checkedValue == "")
        	{
        		checkedValue = "web";
        	}
			if(checkedValue == "media")
        	{
				mktDicInfoMediaName = $scope.mktDicInfoName;
				mktDicInfoMediaId = '';
				mktDicInfoWebName = '';
				
				$.ajax({
		    		  type:"POST",
		    		  url:this.getWebRoot() + "/api/mktDic/exists",
		    		  async:false,
		    		  data: {
		    			  __userKey : __userKey,
		    			  mktDicName:mktDicInfoMediaName,
		    			  mktDicId:mktDicInfoMediaId,
		    			  mktDicType:'media'
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
	        			content:getLocalTag('mktDicMediaAlreadyExists', 'media already exists'),
	    	    		okValue: getLocalTag('confirm','Yes'),
	        			ok:true
	        		}).showModal();
	        		return null;
	        	}

	            return {
	                method:"POST",
	                url: this.getWebRoot() + "/api/mktDic/mediaModify",
	                params: {
	                	pId:dicWebPid,
	                	pName:mktDicInfoMediaName
	                }
	            };
        	}
			else
			{
				mktDicInfoMediaId = $scope.queryMktDicInfoName;
				mktDicInfoMediaName = '';
				mktDicInfoWebId = dicWebId;
				mktDicInfoWebName = $scope.dicWebNameCreate;
				
//				$.ajax({
//		    		  type:"POST",
//		    		  url:this.getWebRoot() + "/api/mktDic/existsWeb",
//		    		  async:false,
//		    		  data: {
//		    			  __userKey : __userKey,
//		    			  mktDicName:mktDicInfoWebName,
//		    			  mktDicType:'web'
//		    		  },
//		    		  success:function(resp){
//		                 if(resp.resultCode != 0) {
//		                     return;
//		                 }
//		                 exists = resp.exists;
//		    		  }
//		      		});
//				
//				//如果记录存在
//	        	if(exists) {
//	        		dialog({
//	        			title:getLocalTag('error', 'error'),
//	        			content:getLocalTag('mktDicWebAlreadyExists', 'web already exists'),
//	    	    		okValue: getLocalTag('confirm','Yes'),
//	        			ok:true
//	        		});
//	        		return null;
//	        	}
	        	
	        	return {
	                method: 'POST', 
	                url: this.getWebRoot() + "/api/mktDic/modify",
	                params: {
	                	pId : mktDicInfoWebId,
	                	pPid : mktDicInfoMediaId,
	                	pName : mktDicInfoWebName
	                }
	            };
    		    	
			}
            
        },
        
        modifyDialog:function(info,$scope) {
        	
           //媒体类型下拉框赋值
 			$scope.callApi({
 				url : this.getWebRoot() + "/api/mktDic/queryDicList",
 				method : "GET",
 				params : {
 					type : "media"
 				}
 			}).success(function (resp, status, headers, config) {
 				if (resp.resultCode != 0) {
 					$scope.failToOperate(resp);
 					return;
 				}
 				$scope.medias = resp.results;
// 				$scope.queryMktDicInfoName =  resp.results[0].id;
 			}).error(function (response, status, headers, config) {
 				$scope.httpError(status);
 			});
             
 			var array = info.split('|');
        	var dicWebPid = array[0];
        	var dicWebId = array[1];
        	var dicWebName = array[2];
        	$scope.checkWebName = "web";
        	$scope.mktDicInfoName = " ";
        	 $scope.queryMktDicInfoName = dicWebPid;
//             $scope.dicWebId = dicWebId;
             $scope.dicWebNameCreate = dicWebName;
             
 			
            return 'id_modify_mktDicInfo_dialog';
        },
      
        
        extendScope: function($scope,$http) {
        	var webRoot = this.getWebRoot();
        	
        	$scope.mediaSelcetShow = function(){
        		var checkedValue = $scope.checkWebName;
    			if(typeof(checkedValue) == "undefined" || checkedValue == "")
            	{
            		checkedValue = "web";
            	}
    			if(checkedValue != "web")
    			{
    				return $scope.mktDicInfoModifyForm.$invalid;
    			}
    			else
    			{
    				if($scope.queryMktDicInfoName =="")
    				return true;
    			}
                
        	}
        	
//       	    $scope.isModify = function(dicWebId) {
//            	if(typeof(dicWebId) != "undefined" && dicWebId != "")
//        		{
//            		return true;
//        		}
//            	
//            	return false;
//            };
            
          //媒体类型下拉框赋值
			$scope.callApi({
				url : this.getWebRoot() + "/api/mktDic/queryDicList",
				method : "GET",
				params : {
					type : "media"
				}
			}).success(function (resp, status, headers, config) {
				if (resp.resultCode != 0) {
					$scope.failToOperate(resp);
					return;
				}
				$scope.medias = resp.results;
//				$scope.queryMktDicInfoName =  resp.results[0].id;
			}).error(function (response, status, headers, config) {
				$scope.httpError(status);
			});
            
          //
			$scope.isQueryMktDicInfoName = function () {
				var checkedValue = $scope.checkWebName;
				if(typeof(checkedValue) == "undefined" || checkedValue == "")
            	{
            		checkedValue = "web";
            	}
            	if(checkedValue == "web")
            	{
            		return true;
            	}
				
				return false;
				
			};
			
			//
			$scope.isMktDicInfoName = function () {
				var checkedValue = $scope.checkWebName;
				if(typeof(checkedValue) == "undefined" || checkedValue == "")
            	{
            		checkedValue = "web";
            	}
            	if(checkedValue == "web")
            	{
            		return false;
            	}
				
				return true;
				
			};
			
			$scope.isMktDicInfoName = function () {
				var checkedValue = $scope.checkWebName;
				if(typeof(checkedValue) == "undefined" || checkedValue == "")
            	{
            		checkedValue = "web";
            	}
            	if(checkedValue == "web")
            	{
            		return false;
            	}
				
				return true;
				
			};
            
			
			
            $scope.webCreate = function(dicMediaId) {
            	$scope.dicWebName = '';
            	dialog({
	        		title: getLocalTag("createWeb", "create web"),
	        		content:document.getElementById('id_create_web_dialog'),
	        		cancel:true,
		    		okValue: getLocalTag('confirm','Yes'),
	        		cancelVal: getLocalTag('cancel','cancel'),
	    		    ok: function() {
	    		    	if(!$scope.createWebForm.$valid) {
	    			      	dialog({
	    			      		title: "失败",
	    			      		ok: true,
	    			    		okValue: getLocalTag('confirm','Yes'),
	    			      		content: "请检查输入内容是否合法"
	    			      	}).showModal();	
	    		    		
	    		    		return false;
	    		    	}
	    		    	
	    		    	// 检查记录是否已经存在
	    		    	var exists = false;
	    	        	$.ajax({
	    	    		  type:"GET",
	    	    		  url:webRoot + "/api/mktDic/exists",
	    	    		  async:false,
	    	    		  data: {
	    	    			  __userKey : __userKey,
	    	    			  mktDicName:$scope.dicWebName,
	    	    			  mktDicPid:dicMediaId,
	    	    			  mktDicType:'web'
	    	    		  },
	    	    		  success:function(resp){
	    	                 if(resp.resultCode != 0) {
	    	                     return;
	    	                 }
	    	                 exists = resp.exists;
	    	    		  }
	    	      		});
	    	        	
	    	        	//如果记录存在
	    	        	if(exists) {
	    	        		dialog({
	    	        			title:getLocalTag('error', 'error'),
	    	        			content:getLocalTag('mktDicWebAlreadyExists', 'web already exists'),
	    	    	    		okValue: getLocalTag('confirm','Yes'),
	    	        			ok:true
	    	        		}).showModal();
	    	        		return null;
	    	        	}
	    	        	
	    		    		    		    			    		    	
	    		    	$scope.$apply(function() {
	    		    		var req = {
	    		    		    method: 'GET',
		    	    		    url: webRoot + "/api/mktDic/create",
		    	    		    params:{
		    	    		    	mktDicPid:dicMediaId,
		    	    		    	mktDicInfoName:$scope.dicWebName,
		    	    		    	mktDicInfoType:'web'
		    	    		    }
	    		    		};
	    		    		
		    		    	$scope.callApi(req).success(function(response, status, headers, config){
					          	if(response.resultCode == 0) {
					          		$scope.successToOperate(response);
					          		$scope.refresh();
					          		return;
					          	}
					          	$scope.failToOperate(response, $scope);
					          	
						    }).error(function(response, status, headers, config){						    	
						    	$scope.httpError(status);
						    });
		    		    	
	    		    	});
	    		    }
	        	}).showModal();
                
           };
        }
        
        
        
        
    };
    exports.instance = function(webRoot, metaName, segments) {
    	var obj = MetaBase.instance(webRoot, metaName, segments);
    	$.extend(obj, MetaMktMediaDic);
    	return obj;
    };
});