define(function (require, exports, module) {
	var MetaBase = require('meta');
	require('dialog');
	require('jquery');
	require('calendar');
	require('jquery/calendar/1.0/tinycal.css');
	require('jquery/ajaxupload/ajaxfileupload.js');

	var MetaMktLandForOther = {
			setRange : function ($scope, start, end) {
				$scope.query_startTime = start.year + '-' + $.addZero(start.month + 1) + '-' + $.addZero(start.day);
				$scope.query_endTime = end.year + '-' + $.addZero(end.month + 1) + '-' + $.addZero(end.day);
			},

			initCaledar : function ($scope) {
				
				$.fn.calendar({
					target : "#queryDate",
					mode : 'multi',
					recordCheck:false,
					ok : function (checked, target) {

						var length = checked.length;
						if(length > 2)
						{
							dialog('最多只能够选择两个时间,请重新选择', function(){}).showModal();;
							return;
						}
						
						
						var day0 = checked[0].year + '' + $.addZero(checked[0].month + 1) + '' + $.addZero(checked[0].day);
						
						if(length == 2)
						{
							var day1 = checked[1].year + '' + $.addZero(checked[1].month + 1) + '' + $.addZero(checked[1].day);
	
							if(day0 <= day1)
							{
								$scope.queryDate = day0 + '-' + day1;
							}
							else
							{
								$scope.queryDate = day1 + '-' + day0;
							}
						}
						else
						{
							$scope.queryDate = day0 + '-' + day0;
						}
						
						$scope.$digest();
						return true;
					}

				});
				
				$.fn.calendar({
					target : "#queryAdInfoDeliveryTimes",
					mode : 'multi',
					recordCheck:false,
					ok : function (checked, target) {

						var length = checked.length;
						if(length > 2)
						{
							dialog('最多只能够选择两个时间,请重新选择', function(){}).showModal();;
							return;
						}
						
						
						var day0 = checked[0].year + '' + $.addZero(checked[0].month + 1) + '' + $.addZero(checked[0].day);
						
						if(length == 2)
						{
							var day1 = checked[1].year + '' + $.addZero(checked[1].month + 1) + '' + $.addZero(checked[1].day);
	
							if(day0 <= day1)
							{
								$scope.queryAdInfoDeliveryTimes = day0 + '-' + day1;
							}
							else
							{
								$scope.queryAdInfoDeliveryTimes = day1 + '-' + day0;
							}
						}
						else
						{
							$scope.queryAdInfoDeliveryTimes = day0 + '-' + day0;
						}
						
						$scope.$digest();
						return true;
					}

				});
				

			},
			querySubPara : function ($scope, params) {

				var queryAdInfoDeliveryTimes = $scope.queryAdInfoDeliveryTimes;
                if(typeof(queryAdInfoDeliveryTimes) == "undefined" || queryAdInfoDeliveryTimes == "")
            	{
                	queryAdInfoDeliveryTimes = '';
            	}
                else
                {
                	if (!$scope.TimeCheck(queryAdInfoDeliveryTimes))
                	{
						return;
                	}
                }
                
                var queryDate = $scope.queryDate;
                if(typeof(queryDate) == "undefined" || queryDate == "")
            	{
                	queryDate = '';
            	}
                else
                {
                	if (!$scope.TimeCheck(queryDate))
                	{
						return;
                	}
                }
                
				var queryAdInfoDeliveryBeginDay = queryAdInfoDeliveryTimes.split('-')[0];
				var queryAdInfoDeliveryEndDay = queryAdInfoDeliveryTimes.split('-')[1];
				var queryDateBeginDay = queryDate.split('-')[0];
				var queryDateEndDay = queryDate.split('-')[1];
				
				$.extend(params, {
					mktId : $scope.queryMktId,
					adState : $scope.queryAdState,
					isVMallPlatform : 2,
					adInfoWebName : $scope.queryAdInfoWebName,
					adInfoPort : $scope.queryAdInfoPort,
					inputUser : $scope.queryInputUser,
					adInfoDeliveryBeginDay : queryAdInfoDeliveryBeginDay,
					adInfoDeliveryEndDay :queryAdInfoDeliveryEndDay,
					adqueryDateBeginDay  : queryDateBeginDay,
					adqueryDateEndDay  : queryDateEndDay
				});

				return {
					method : 'GET',
					params : params,
					url : this.webRoot + "/api/mktLandForOther/query"
				};
			},

			modifyPara : function ($scope, mktLandInfoAid, pid) {
				return {
					method : 'POST',
					url : this.getWebRoot() + "/api/mktLandForOther/modifyHonor",
					params : {
						pId : mktLandInfoAid,
						pLandInfoLandLink : $scope.mktLandInfoLandLink
					}
				};
			},

			modifyCheck : function ($scope) {

				return $scope.mktLandInfoModifyForm.$valid;
			},

			modifyDialog : function (mktLandInfoAid, $scope) {
				$scope.callApi({
					url : this.getWebRoot()
					 + "/api/mktLandForOther/querymodifycontents",
					method : "GET",
					params : {
						mktLandInfoAid : mktLandInfoAid
					}
				})
				.success(
					function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					var mktLandInfo = resp;
				
					$scope.mktLandInfoLandLink = mktLandInfo.mktLandInfoLandLink;
					$scope.mktinfoName = mktLandInfo.mktinfoName;
					$scope.adInfoWebName = mktLandInfo.adInfoWebName;
					$scope.adInfoChannel = mktLandInfo.adInfoChannel;
					$scope.adInfoPosition = mktLandInfo.adInfoPosition;
					$scope.adInfoPort = mktLandInfo.adInfoPort;
					
				})
				.error(function (response, status, headers, config) {
					$scope.httpError(status);
				});
				return 'id_modify_mktLandInfo_other_dialog';
			},

			extendScope : function ($scope, $http) {
				var webRoot = this.getWebRoot();

				//初始化下拉菜单
				this.initCaledar($scope);
				
				$scope.callApi({
					url : this.getWebRoot()
					 + "/api/mktInfo/queryIdNameListWithControl",
					method : "GET",
					params : {}
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					$scope.mktInfos = resp.result;
				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});

				$scope.callApi({
					url : this.getWebRoot()
					 + "/api/mktDic/queryDicCommonList",
					method : "GET",
					params : {
						type : "ad_state"
					}
				})
				.success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					$scope.states = resp.results;
				})
				.error(function (response, status, headers, config) {
					$scope.httpError(status);
				});
				
				//端口所属赋值
				$scope.callApi({
					url : this.getWebRoot() + "/api/mktDic/queryDicCommonList",
					method : "GET",
					params : {
						type : "port"
					}
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					
					var arr = {};
					var array = new Array();
					arr.id = '-1';
					arr.name = '全部';
					$scope.portNames = array.concat(arr,resp.results);
				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});
				
				
				//web名称赋值
				$scope.callApi({
					url : this.getWebRoot() + "/api/mktDic/queryDicWebNameList",
					method : "GET",
					params : {
						type : "web"
					}
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					var arr = {};
					var array = new Array();
					arr.id = '-1';
					arr.name = '全部';
					$scope.webNames = array.concat(arr,resp.results);
				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});

				//录入人赋值
				$scope.callApi({
					url : this.getWebRoot() + "/api/mktDic/queryInputUserNameList",
					method : "GET"
				}).success(function (resp, status, headers, config) {
					if (resp.resultCode != 0) {
						$scope.failToOperate(resp);
						return;
					}
					var arr = {};
					var array = new Array();
					arr.id = '-1';
					arr.name = '全部';
					$scope.inputUserNames = array.concat(arr,resp.results);

				}).error(function (response, status, headers, config) {
					$scope.httpError(status);
				});
				
				
				// 重置广告位状态
				$scope.resetAd = function (adInfoId) {
					 dialog({
		    			title: getLocalTag("prompt", "Prompt"),
		    			content:getLocalTag("sureToReset", "Are you sure to reset it?"),
		    			cancel:true,
		    			lock:true,
			    		okValue: getLocalTag('confirm','Yes'),
		        		cancelValue: getLocalTag('cancel','cancel'),
		        		ok: function() {
		        			$scope.$apply(function(){
		        				$scope.callApi({
		    						method : 'POST',
		    						url : webRoot + "/api/adInfo/modifyState",
		    						params : {
		    							adInfoId : adInfoId,
		    							adInfoState : 1
		    						}
		    					}).success(function (resp, status, headers, config) {
		    						if (resp.resultCode != 0) {
		    							$scope.failToOperate(resp);
		    							return;
		    						}
		    						$scope.refresh();
		    						$scope.successToOperate(resp, $scope);
		    					}).error(function (response, status, headers, config) {
		    						$scope.httpError(status);
		    					});
		        			});
		        		}
		        	}).showModal();;
					
				};

				$scope.publishAd = function (adInfoId, cid) {

					if (typeof(cid) == "undefined" || cid == "") {
						dialog({
							title : getLocalTag('error', 'error'),
							content : getLocalTag('mktLandInfoNoPublishForNull', 'can not publis cause info is null'),
							okValue : getLocalTag('confirm', 'Yes'),
							ok : true
						}).showModal();;

						return null;
					}
					dialog({
		    			title: getLocalTag("prompt", "Prompt"),
		        		content:getLocalTag("sureToPublish", "Are you sure to publish it?"),
		    			cancel:true,
		    			lock:true,
			    		okValue: getLocalTag('confirm','Yes'),
		        		cancelValue: getLocalTag('cancel','cancel'),
		        		ok: function() {
		        			$scope.$apply(function(){
		        				$scope.callApi({
		    						method : 'POST',
		    						url : webRoot + "/api/adInfo/modifyState",
		    						params : {
		    							adInfoId : adInfoId,
		    							adInfoState : 2
		    						}
		    					}).success(function (resp, status, headers, config) {
		    						if (resp.resultCode != 0) {
		    							$scope.failToOperate(resp);
		    							return;
		    						}
		    						$scope.refresh();
		    						$scope.successToOperate(resp, $scope);
		    					}).error(function (response, status, headers, config) {
		    						$scope.httpError(status);
		    					});
		        			});
		        		}
		        	}).showModal();;
					
				};

				$scope.modifyAd = function (adInfoId) {

					$scope.callApi({
						url : webRoot + "/api/mktLandForOther/detail",
						method : "GET",
						params : {
							adInfoId : adInfoId
						}
					}).success(function (resp, status, headers, config) {
						if (resp.resultCode != 0) {
							$scope.failToOperate(resp);
							return;
						}
						var adInfo = resp;
						if (adInfo.detailsAdInfoStateId == 1) {
							// 可编辑
							$scope.modelModify(adInfoId);
						} else {
							dialog({
								title : getLocalTag("id_modify_tip_dialog", "提示"),
								ok : true,
								okValue : getLocalTag('confirm', 'Yes'),
								content : getLocalTag('resetToModify', 'Please reset first'),
								lock : true,
								opacity : 0.3
							}).showModal();;
						}

					}).error(function (response, status, headers, config) {
						$scope.httpError(status);
					});
				};

				$scope.canModify = function (adstate) {
					if (adstate == 1) {
						return true;
					}

					return false;
				};

				$scope.canPublish = function (adstate, cid) {

					if (adstate == 1 && typeof(cid) != "undefined" && cid != "") {
						return true;
					}

					return false;
				};

				$scope.canReset = function (adstate) {
					if (adstate == 2) {
						return true;
					}

					return false;
				};
				
				$scope.landBatchResDownLoad = function()
				{
					var fileId = $("#fileId").val();
					var url = $scope.options.meta.webRoot + "/api/mktLandForOther/landBatchResDownLoad?__userKey=" + __userKey + "&fileId=" + fileId;
					location.href = url;
				}
				
				
				$scope.landBatchTemplateDownLoad = function ()
				{
					var url = $scope.options.meta.webRoot + "/api/mktLandForOther/templateDownLoad?__userKey=" + __userKey + "&vmallFlag=false";
					location.href = url;
				};
				
				
				$scope.landBatchUpLoad = function(){
					var flag = $("#dealFlag").val();
					var fileName = $("#uploadFile").val();
					if(null == fileName || "" == fileName )
					{
						dialog("请先选择上传excel模板文件",function(){return true}).showModal();;
						return;
					}
					
					var reg = /.*.xls$/;
					if(!reg.test(fileName))
					{
						dialog("上传excel模板文件类型必须为 Excel 97-2003 工作表，请先修改文件！ ",function(){return true}).showModal();;
						return;
					}
					
					if(0 == flag)
					{
						$("#dealFlag").val(1);
						$("#batchResDown").hide();
						
						var batchFlag = ($scope.batchFlag == true) ? true : false;
						
						$("#res_div").html("<div class='processLoading'><span style='vertical-align: middle;'>文件正在导入，请稍后。</span></div>");
						var url = webRoot + "/api/mktLandForOther/uploadFile?__userKey=" + __userKey +"&vmallFlag=2"+  "&batchFlag=" + batchFlag;
					    $.ajaxFileUpload
					    (
					        {
					            url:url,//用于文件上传的服务器端请求地址
					            secureuri:false,//一般设置为false
					            fileElementId:'uploadFile',//文件上传空间的id属性  <input type="file" id="file" name="file" />
					            dataType : 'text',
					            success: function (data, status)  
					            {
					            	$("#queryButton").trigger('click');
					            	
					            	 var beginIndex = data.indexOf("{");
					            	 var endIndex = data.indexOf("}");
					            	 var objStr = data.substring(beginIndex,endIndex + 1);
					            	 var obj = eval("("+objStr+")");
					            	 var total = obj.total;
					            	 var updateSuccess= obj.updateSuccess;
					            	 var createSuccess = obj.createSuccess;
					            	 /*
					            	  *  int vmallIgnore = 0;
                                         int honorIgnore = 0;
                                         int otherIgnore = 0;
					            	  */
					            	 var vmallIgnore = obj.vmallIgnore;
					            	 var honorIgnore = obj.honorIgnore;
					            	 var fail = total -updateSuccess - createSuccess - honorIgnore - vmallIgnore;
					            	 var fileTimeMillis= obj.fileTimeMillis;
					            	 
					            	 var tip = "";
					            	 if (typeof(total) == "undefined" || typeof(createSuccess) == "undefined" 
					            		 || typeof(updateSuccess) == "undefined" || typeof(vmallIgnore) == "undefined"
					            		 || typeof(honorIgnore) == "undefined" ) 
					            	 {
					            		  tip = "数据上传处理失败！";
					            	 }
					            	 else
					            	{
					            	       tip = "总共处理了" + total + "条记录,成功新增" + createSuccess +"条记录， 成功更新" + 
					            	              updateSuccess +"条记录，忽略vmall平台"+ vmallIgnore +"条记录，"+
					            	              "忽略荣耀平台"+honorIgnore+"条记录，"+"操作失败" + fail + "条记录";
					            	       $("#batchResDown").show();
					            	}
					            	 
					            	 
					            	 $("#res_div").html(tip);
					            	 $("#fileId").val(fileTimeMillis);
					            	 $("#dealFlag").val(0);
					            	 
					            	 
					            }
					        }
					    );
					}
					
					
				};
				
				
				//批量发布
				$scope.batchPublish = function () {

					dialog({
						title : getLocalTag("prompt", "Prompt"),
						content : "确定要批量发布第三方着陆链接吗？",
						cancel : true,
						lock : true,
						okValue : getLocalTag('confirm', 'Yes'),
						cancelValue : getLocalTag('cancel', 'cancel'),
						ok : function () {

							var state = $scope.queryAdState;

							if (state == 0 || state == 2 || state == 3) {
								dialog("过滤当前状态不是待录入着陆链接状态，请重新选择", function () {
									return true
								}).showModal(); ;
								return;
							}

							var queryAdInfoDeliveryTimes = $scope.queryAdInfoDeliveryTimes;
							if (typeof(queryAdInfoDeliveryTimes) == "undefined") {
								queryAdInfoDeliveryTimes = '';
							}

							var queryDate = $scope.queryDate;
							if (typeof(queryDate) == "undefined") {
								queryDate = '';
							}
							var queryAdInfoDeliveryBeginDay = queryAdInfoDeliveryTimes.split('-')[0];
							var queryAdInfoDeliveryEndDay = queryAdInfoDeliveryTimes.split('-')[1];
							var queryDateBeginDay = queryDate.split('-')[0];
							var queryDateEndDay = queryDate.split('-')[1];

							var params = {
									__userKey : __userKey,
									mktId:$scope.queryMktId,
									adState:1,
									monitorPlatform:0,
									adInfoWebName : $scope.queryAdInfoWebName,
									adInfoPort : $scope.queryAdInfoPort,
									inputUser : $scope.queryInputUser,
									adInfoDeliveryBeginDay : queryAdInfoDeliveryBeginDay,
									adInfoDeliveryEndDay :queryAdInfoDeliveryEndDay,
									adqueryDateBeginDay  : queryDateBeginDay,
									adqueryDateEndDay  : queryDateEndDay,
									isVMallPlatform : 2
						  };

							$scope.$apply(function () {
								$scope.callApi({
									url : webRoot + "/api/mktLandForOther/batchPublish",
									method : "POST",
									params : params
								}).success(function (resp, status, headers, config) {
									if (resp.resultCode != 0) {
										$scope.failToOperate(resp);
										return;
									}

									var success = resp.success;
									var total = resp.total;

									dialog("共" + total + "个广告位满足发布条件,批量发布成功 " + success + "条", function () {

										return true
									}).showModal(); ;

									$scope.refresh();
								}).error(function (response, status, headers, config) {
									$scope.httpError(status);
								});
							});
						}
					}).showModal(); ;

				};
				
				//批量导入
				$scope.batchLoad = function(){
					
						$("#res_div").html(null);
						$scope.batchFlag = false;
						$("#uploadFile").val(null);
						$("#batchResDown").hide();
						$("#dealFlag").val(0);
						dialog({
							title : getLocalTag("other_landInfo_batchLoad_dialog", "批量录入第三方着陆链接"),
							cancel:true,
							cancelValue : '关闭',
							content : document.getElementById('other_landInfo_batchLoad_dialog'),
							lock : true,
							opacity : 0.3
						}).showModal();;
						
					};
					$scope.TimeCheck =  function (checkTime) 
					{
					    var BeginDay = checkTime.split('-')[0];
						var EndDay = checkTime.split('-')[1];
					    var timeExample = /^(\d{4}\d{2}\d{2})$/;
					    if (!timeExample.test(BeginDay))
					    {
					        dialog('时间格式非法，请重新输入', function(){}).showModal();;
					        return false;
					    }
					    if (!timeExample.test(EndDay))
					    {
					        dialog('时间格式非法，请重新输入', function(){}).showModal();;
					        return false;
					    }
					    if(BeginDay > EndDay)
					    {
					        dialog('时间格式非法，请重新输入', function(){}).showModal();;
					        return false;
					    }
					    var resultBeginDay = new Array();
					    resultBeginDay[0] = BeginDay.substring(0,4);
					    resultBeginDay[1] = BeginDay.substring(4,6);
					    resultBeginDay[2] = BeginDay.substring(6,8);
					    var resultEndDay  = new Array();
					    resultEndDay[0] = EndDay.substring(0,4);
					    resultEndDay[1] = EndDay.substring(4,6);
					    resultEndDay[2] = EndDay.substring(6,8);
					    if (resultBeginDay == null || resultEndDay == null)
					    {
					        dialog('时间格式非法，请重新输入', function(){}).showModal();;
					        return false;
					    }
					    var d1 = new Date(resultBeginDay[0], resultBeginDay[1] - 1, resultBeginDay[2]);
					    var d2 = new Date(resultEndDay[0], resultEndDay[1] - 1, resultEndDay[2]);
					    if((d1.getFullYear() == resultBeginDay[0] && (d1.getMonth() + 1) == resultBeginDay[1] && d1.getDate() == resultBeginDay[2]) && (d2.getFullYear() == resultEndDay[0] && (d2.getMonth() + 1) == resultEndDay[1] && d2.getDate() == resultEndDay[2]))
					    {
					        return true;
					    }
					    dialog('时间格式非法，请重新输入', function(){}).showModal();;
				        return false;
					};
					
					
					$scope.mktLandEmail = function()
					{

						 var queryMktId = $scope.queryMktId;
						 var queryAdState = $scope.queryAdState;
						 var adInfoWebName = $scope.queryAdInfoWebName;
						 var adInfoPort = $scope.queryAdInfoPort;
						 var queryInputUser = $scope.queryInputUser;
						 var queryAdInfoDeliveryTimes = $scope.queryAdInfoDeliveryTimes;
						 var queryDate = $scope.queryDate;
						 
						 if(typeof(queryMktId) == "undefined" || queryMktId == "" || queryMktId == "-1")
						{
							 dialog("请先选择营销活动名称再发送通知邮件！",function(){return true}).showModal();;
							 return;
						 }
						
						 $.ajax({
								type : "GET",
								url : webRoot + "/api/mktLandForOther/queryMktLandEmailUserInfo",
								async : false,
								data : {
									__userKey : __userKey,
									mktinfoId : queryMktId,
									pageInfo : "monitorInfo"
								},
								success : function (resp) {
									if (resp.resultCode != 0) {
										return;
									}
									$scope.mktLandHonnrEmailUsers = resp.result;
									
									
									dialog({
										title : getLocalTag("id_mktLandOtherEmail_dialog", "选择要通知的人"),
										ok : function () {
											var users = "";
											 $("[name='emailLandHonnrUsers']:checked").each(function(index, element) {
												 users += $(this).val() + ",";
						                     });
						                    
											 if(users == "")
											 {
												 dialog('请选择通知的人', function(){}).showModal();;
												 return;
											 }
												var paramsobj = {
														__userKey : __userKey,
											   			mktinfoId : queryMktId,
											   			users : users};
											   	var params = $.param(paramsobj,false);
												$.ajax({
											   		type : "POST",
											   		url : webRoot + "/api/mktLandForOther/emailLandUsers",
											   		async : false,
											   	    data:params,
											   		success : function (resp) {
											   			if (resp.resultCode != 0) {
											   				return false;
											   			}
											   			return true;
											   		}
											   	});
												
												
											 
											return true;
										},
										okValue : "发出邮件",
										content : document.getElementById('id_mktLandOtherEmail_dialog'),
										lock : true,
										opacity : 0.3
									}).showModal();;
								}
							});
						 
					};
					
				}
	
			};
	
	 exports.instance = function(webRoot, metaName, segments) {
	    	var obj = MetaBase.instance(webRoot, metaName, segments);
	    	$.extend(obj, MetaMktLandForOther);
	    	return obj;
	    };

});