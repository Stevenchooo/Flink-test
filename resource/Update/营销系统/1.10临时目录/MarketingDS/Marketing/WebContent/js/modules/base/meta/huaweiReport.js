define(function(require, exports, module) {
	var MetaBase = require('meta');
	require('dialog');
	require('jquery');
	require('calendar');
	require('jquery/calendar/1.0/tinycal.css');
	require('jquery/ajaxupload/ajaxfileupload.js');
	var utils = require('modules/base/utils');
	var navi = require("modules/base/navi");

	var MetaHuaweiReport = {
		setRange : function($scope, start, end) {
			$scope.query_startTime = start.year + '-'
					+ $.addZero(start.month + 1) + '-' + $.addZero(start.day);
			$scope.query_endTime = end.year + '-' + $.addZero(end.month + 1)
					+ '-' + $.addZero(end.day);
		},

		initCaledar : function($scope) {
                    upmadmin.common.multiSel();

            	   if(navi.getcurActNameList()!="")
            	   {
            		 $scope.queryMktId=navi.getcurActNameList();
              	     $scope.aidList=navi.getcurActIdList();
              	     $scope.webNames=navi.getcurwebNames();
              	     $scope.queryAdInfoWebName=navi.getcurWebNameList();
              	     $scope.queryAdInfoWebNameId=navi.getcurWebIdList();
              	     $scope.queryAdInfoPort=navi.getcurQueryAdInfoPort();
              	     $scope.queryAdInfoPlatform=navi.getcurQueryAdInfoPlatform();
              	     $scope.queryInputUser=navi.getcurQueryInputUser();
              	     $scope.inputCid=navi.getcurInputCid();
              	     $scope.queryDate=navi.getcurQueryDate();
              	     $scope.inputSid=navi.getcurInputSid(); 
            	   }  
            	     
            	     
			$.fn.calendar({
				target : "#queryDate",
				mode : 'multi',
				recordCheck : false,
				ok : function(checked, target) {

					var length = checked.length;
					if (length > 2) {
						dialog('最多只能够选择两个时间,请重新选择', function() {
						}).showModal();
						return;
					}

					var day0 = checked[0].year + ''
							+ $.addZero(checked[0].month + 1) + ''
							+ $.addZero(checked[0].day);

					if (length == 2) {
						var day1 = checked[1].year + ''
								+ $.addZero(checked[1].month + 1) + ''
								+ $.addZero(checked[1].day);

						if (day0 <= day1) {
							$scope.queryDate = day0 + '-' + day1;
						} else {
							$scope.queryDate = day1 + '-' + day0;
						}
					} else {
						$scope.queryDate = day0 + '-' + day0;
					}
					$scope.webSelectAll = false;
					$scope.$digest();
					return true;
				}

			});

			$.fn.calendar({
						target : "#queryAdInfoDeliveryTimes",
						mode : 'multi',
						recordCheck : false,
						ok : function(checked, target) {

							var length = checked.length;
							if (length > 2) {
								dialog('最多只能够选择两个时间,请重新选择', function() {
								}).showModal();
								return;
							}

							var day0 = checked[0].year + ''
									+ $.addZero(checked[0].month + 1) + ''
									+ $.addZero(checked[0].day);

							if (length == 2) {
								var day1 = checked[1].year + ''
										+ $.addZero(checked[1].month + 1) + ''
										+ $.addZero(checked[1].day);

								if (day0 <= day1) {
									$scope.queryAdInfoDeliveryTimes = day0
											+ '-' + day1;
								} else {
									$scope.queryAdInfoDeliveryTimes = day1
											+ '-' + day0;
								}
							} else {
								$scope.queryAdInfoDeliveryTimes = day0 + '-'
										+ day0;
							}
							$scope.webSelectAll = false;
							$scope.$digest();
							return true;
						}

					});

		},

		querySubPara : function($scope, params) {

			var queryAdInfoDeliveryTimes = $scope.queryAdInfoDeliveryTimes;
			if (typeof (queryAdInfoDeliveryTimes) == "undefined"
					|| queryAdInfoDeliveryTimes == "") {
				queryAdInfoDeliveryTimes = '';
			} else {
				if (!$scope.TimeCheck(queryAdInfoDeliveryTimes)) {
					return;
				}
			}
			var sid = $scope.inputSid;
			var cid = $scope.inputCid;
			var mktId = $scope.queryMktId;			
			
//			if ((mktId=="全部")
//					&& (typeof (sid) == "undefined" || sid == "")
//					&& (typeof (cid) == "undefined" || cid == "")) {
//					dialog('请输入SID或CID', function() {
//					}).showModal();
//					return;
//		    }

			if (typeof (sid) == "undefined" || sid == "") {
				sid = '';
			}
			if (typeof (cid) == "undefined" || cid == "") {
				cid = '';
			}
			var queryDate = $scope.queryDate;
			if (typeof (queryDate) == "undefined") {
				queryDate = '';
			}
			var queryDateBeginDay = queryDate.split('-')[0];
			var queryDateEndDay = queryDate.split('-')[1];
			
			if(typeof($scope.UserDept) == 'undefined')
			{
				$scope.UserDept = -1;
			}
			navi.setcurActNameList($scope.queryMktId);
			navi.setcurActIdList($scope.aidList);
			navi.setcurwebNames($scope.webNames);
			navi.setcurWebNameList($scope.queryAdInfoWebName);
			navi.setcurWebIdList($scope.queryAdInfoWebNameId);
			navi.setcurQueryAdInfoPort($scope.queryAdInfoPort);
			navi.setcurQueryAdInfoPlatform($scope.queryAdInfoPlatform);
			navi.setcurQueryInputUser($scope.queryInputUser);
			navi.setcurInputCid($scope.inputCid);
			navi.setcurQueryDate($scope.queryDate);
			navi.setcurInputSid($scope.inputSid);
			var mktName = $scope.aidList;
			if(mktId == "全部")
			{
				mktName = "-1";
			}
			
			
			$.extend(params, {
				mktId : mktName,
				adInfoWebName : $scope.queryAdInfoWebNameId,
				adInfoPort : $scope.queryAdInfoPort,
				adInfoPlatform : $scope.queryAdInfoPlatform,
				inputUser : $scope.queryInputUser,
				cid : cid,
				adqueryDateBeginDay : queryDateBeginDay,
				adqueryDateEndDay : queryDateEndDay,
				adquerySid : sid,
				adType : $scope.UserDept
			});

			return {
				method : 'GET',
				params : params,
				url : this.webRoot + "/api/huaweiReport/ReportQuery"
			};
		},

		extendScope : function($scope, $http) {
			var webRoot = this.getWebRoot();

			// 初始化下拉菜单
			this.initCaledar($scope);

			var queryDate = $scope.queryDate;
			if (typeof (queryDate) == "undefined" ) {
				$scope.queryDate = creatInitializeDate();
			}
			// 当前用户部门 0 或 1中国区或荣耀区 20160226
			$scope.callApi({
				url : this.getWebRoot() + "/api/mktDic/queryUserDept",
				method : "GET",
				params : {}
			}).success(function(resp, status, headers, config) {
				if (resp.resultCode != 0) {
					$scope.failToOperate(resp);
					return;
				}
				$scope.UserDept = resp.results[0].deptType;
			}).error(function(response, status, headers, config) {
				$scope.httpError(status);
			});
			
			//获取营销活动ID与名称列表
			$scope.callApi({
				url : this.getWebRoot() + "/api/mktInfo/getMktNameListByDept",
				method : "GET"
			}).success(function (resp, status, headers, config) {
				if (resp.resultCode != 0) {
					$scope.failToOperate(resp);
					return;
				}
				//搜索框赋值
				$scope.mktInfos = resp.result;					
			}).error(function (response, status, headers, config) {
				$scope.httpError(status);
			});

			// 广告位状态下拉框赋值
			$scope.callApi({
				url : this.getWebRoot() + "/api/mktDic/queryDicCommonList",
				method : "GET",
				params : {
					type : "ad_state"
				}
			}).success(function(resp, status, headers, config) {
				if (resp.resultCode != 0) {
					$scope.failToOperate(resp);
					return;
				}
				$scope.states = resp.results;
			}).error(function(response, status, headers, config) {
				$scope.httpError(status);
			});

			

			// 端口所属赋值
			$scope.callApi({
				url : this.getWebRoot() + "/api/mktDic/queryDicCommonList",
				method : "GET",
				params : {
					type : "port"
				}
			}).success(function(resp, status, headers, config) {
				if (resp.resultCode != 0) {
					$scope.failToOperate(resp);
					return;
				}

				var arr = {};
				var array = new Array();
				arr.id = '-1';
				arr.name = '全部';
				$scope.portNames = array.concat(arr, resp.results);
				$scope.ports = resp.results;
				//$scope.queryAdInfoPort = '-1';
				//$scope.queryAdInfoPort=navi.getcurQueryAdInfoPort();
				if(typeof(navi.getcurQueryAdInfoPort())=="undefined"||navi.getcurQueryAdInfoPort()==null
					||navi.getcurQueryAdInfoPort()==''||navi.getcurQueryAdInfoPort()=="")
				{
								$scope.queryAdInfoPort = '-1';
				}else{
								$scope.queryAdInfoPort = navi.getcurQueryAdInfoPort();
				}
			}).error(function(response, status, headers, config) {
				$scope.httpError(status);
			});

			// web名称赋值
//			$scope.callApi({
//				url : this.getWebRoot() + "/api/mktDic/queryDicWebNameList",
//				method : "GET",
//				params : {
//					type : "web"
//				}
//			}).success(function(resp, status, headers, config) {
//				if (resp.resultCode != 0) {
//					$scope.failToOperate(resp);
//					return;
//				}
//				var arr = {};
//				var array = new Array();
//				arr.id = '-1';
//				arr.name = '全部';
//				$scope.webNames = array.concat(arr, resp.results);
//				$scope.queryAdInfoWebName = '-1';
//			}).error(function(response, status, headers, config) {
//				$scope.httpError(status);
//			});

			// 着陆平台名称赋值
			$scope.callApi({
				url : this.getWebRoot() + "/api/mktDic/queryDicCommonList",
				method : "GET",
				params : {
					type : "land_platform"
				}
			}).success(function(resp, status, headers, config) {
				if (resp.resultCode != 0) {
					$scope.failToOperate(resp);
					return;
				}
				var arr = {};
				var array = new Array();
				arr.id = '-1';
				arr.name = '全部';
				$scope.platNames = array.concat(arr, resp.results);
				//$scope.queryAdInfoPlatform = '-1';
				//$scope.queryAdInfoPlatform = navi.getcurQueryAdInfoPlatform();
				if(typeof(navi.getcurQueryAdInfoPlatform())=="undefined"||navi.getcurQueryAdInfoPlatform()==null
					||navi.getcurQueryAdInfoPlatform()==''||navi.getcurQueryAdInfoPlatform()=="")
				{
							$scope.queryAdInfoPlatform = '-1';
				}else{
							$scope.queryAdInfoPlatform = navi.getcurQueryAdInfoPlatform();
				}
			}).error(function(response, status, headers, config) {
				$scope.httpError(status);
			});

			// 录入人赋值
			$scope.callApi(
					{
						url : this.getWebRoot()
								+ "/api/mktDic/queryReportInputUserNameList",
						method : "GET",
						params : {
							pType : $scope.UserDept
						}
					}).success(function(resp, status, headers, config) {
				if (resp.resultCode != 0) {
					$scope.failToOperate(resp);
					return;
				}
				var arr = {};
				var array = new Array();
				arr.id = '-1';
				arr.name = '全部';
				$scope.inputUserNames = array.concat(arr, resp.results);
				//$scope.queryInputUser = '-1';
				//$scope.queryInputUser = navi.getcurQueryInputUser();
				if(typeof(navi.getcurQueryInputUser())=="undefined"||navi.getcurQueryInputUser()==null
					||navi.getcurQueryInputUser()==''||navi.getcurQueryInputUser()=="")
				{
					$scope.queryInputUser = '-1';
				}else{
					$scope.queryInputUser = navi.getcurQueryInputUser();
				}
			}).error(function(response, status, headers, config) {
				$scope.httpError(status);
			});
			//弹出对话框，按营销活动投放时间（年、月份）分类展示营销活动名称标签
			$scope.getMktName = function()
			{
				$("#ad_tbody label").removeClass("wxselected");
				$("#ad_tbody label").removeClass("wxmore");
				$("#ad_tbody").off("click");
				$("#ad_tbody").html("");
				
				$("<tr><td width=\"10%\"></td><td colspan=\"4\"><a href=\"javascript:void(0)\" id=\"all\" data-checked=\"false\" >全选</a></td></tr>").appendTo("#ad_tbody");
				var mkts = utils.arrToJsonObject($scope.mktInfos);
				for(var i =0 ;i<mkts.length;i++)
				{
					var accounts = mkts[i].mktarr;
					var tmpDate = mkts[i].createDate;
					var html = "";	
					if(i < 2){
						html = "<tr height=\"30px\"><td width=\"10%\"  >" + mkts[i].createDate + ":</td>" ;
					}
					else{
						html = "<tr class=\"wxdnone\" height=\"30px\"><td width=\"10%\" >" + mkts[i].createDate + ":</td>" ;
					}
					var fk = 1;
					for(var j= 0;j<accounts.length;j++){
						
						if(fk<=4){

							if(i < 2){
								html +="<td width=\"20%\" ><label data-wxvalue=\""+accounts[j]+"\">"+accounts[j] + "</label></td>" ; 
							}
							else{
								html +="<td width=\"20%\"   class=\"wxdnone\"><label data-wxvalue=\""+accounts[j]+"\">"+accounts[j] + "</label></td>" ; 
							}
							fk++;
						}else{
							
							if(i < 2){
								html +="<tr height=\"30px\"><td width=\"10%\" ></td>" +"<td width=\"20%\" ><label data-wxvalue=\""+accounts[j]+"\">"+accounts[j] + "</label></td>" ;
							}
							else{
								html +="<tr class=\"wxdnone\" height=\"30px\"><td width=\"10%\"  class=\"wxdnone\"></td>" +"<td width=\"20%\" ><label data-wxvalue=\""+accounts[j]+"\">"+accounts[j] + "</label></td>" ;
							}
							
							fk = 2;
						}
						
					}
					html +="</tr>";
				
					$(html).appendTo("#ad_tbody");
				}
				html = "<tr><td colspan=\"5\" style=\"text-align:right; padding:14px 0;font-size:12px;\"><a href=\"javascript:void(0)\" id=\"more\" data-wxvalue=\"more\" >更多营销活动</a></td></tr>"
				
					
				$(html).appendTo("#ad_tbody");
				$("#ad_tbody").on("click", "#all", function(){
					if($(this).hasClass("wxmore")){
						$(this).removeClass("wxmore");
						$scope.isall = false;
						$("#ad_tbody label").removeClass("wxselected");
					}
					else{
						$scope.isall = true;
						$("#ad_tbody label").addClass("wxselected");
						$(this).addClass("wxmore");
					}
				});
				
				$("#ad_tbody").on("click", "#more", function(){
					if($(this).hasClass("wxmore")){
						$(this).removeClass("wxmore");
						$("#ad_tbody .wxdnone").hide();
						$("#more").html("更多营销活动");
					}else{
						$(this).addClass("wxmore");
						$("#ad_tbody .wxdnone").show();
						$("#more").html("隐藏");
					}
				});
				
				$("#ad_tbody").on("click", "label", function(){				
						if($(this).hasClass("wxselected")){
							$(this).removeClass("wxselected");
							$("#all").removeClass("wxmore");
						}
						else{

						    $(this).addClass("wxselected");
							//判断是否所以的被选中
						}						
					
				});
				dialog({
					title : getLocalTag("id_selectMkt_dialog", "选择营销活动"),
					ok : function () {
						$scope.queryMktId="";
						if($("#ad_tbody #all").hasClass("wxmore"))
						{//全选
							$scope.queryMktId="全部";
						}else{
							var xxx = $("#ad_tbody .wxselected");
							for(var i = 0; i < xxx.length; i ++){
								$scope.queryMktId+=xxx.eq(i).data("wxvalue");
								if(i!=xxx.length-1){
									$scope.queryMktId+=",";
								}
								
							}
						}
						$scope.queryAdInfoWebName="";
						$scope.$digest();
						var params = {
								"adName" : $scope.queryMktId
							};

							$scope.$apply(function () {
								$scope.callApi({
									url : webRoot + "/api/mktDic/queryDicWebNameListByAd",
									method : "GET",
									params : params
								}).success(function (resp, status, headers, config) {
									if (resp.resultCode != 0) {
										$scope.failToOperate(resp);
										return;
									}
									if(resp.results.length>0){
										var arr={};
										var array=new Array();
										arr.id=-1;
										arr.name="全部";
										$scope.webNames=array.concat(arr,resp.results);
									}else{
										$scope.webNames=[];
									}
									$scope.aidList  = resp.aidList.join(",");
									
									navi.setcurwebNames($scope.webNames);
									navi.setcurActNameList($scope.queryMktId);
									navi.setcurActIdList($scope.aidList);
									
								}).error(function (response, status, headers, config) {
									$scope.httpError(status);
								});

							});								
													
						$scope.$digest();
						return true;
							
						 
					},
					okValue : "确定",
					content : document.getElementById('id_selectMkt_dialog'),
					lock : true,
					opacity : 0.3
				}).showModal();


			};
			$scope.reportExport = function() {
				
				var sid = $scope.inputSid;
				var cid = $scope.inputCid;
				
				if (($scope.queryMktId=="全部")
						&& (typeof (sid) == "undefined" || sid == "")
						&& (typeof (cid) == "undefined" || cid == "")) {
						dialog('请输入SID或CID', function() {
						}).showModal();
						return;
			    }
												
				var url = $scope.options.meta.webRoot
						+ "/api/huaweiReport/reportExport?__userKey="
						+ __userKey;
				
				var mktId = $scope.aidList;
				if($scope.queryMktId == "全部")
				{
					mktId = "-1";
				}
				var adInfoWebName = $scope.queryAdInfoWebNameId;
				var adInfoPort = $scope.queryAdInfoPort;
				
				if (typeof (queryAdInfoDeliveryTimes) == "undefined") {
					queryAdInfoDeliveryTimes = '';
				}

				var queryDate = $scope.queryDate;
				if (typeof (queryDate) == "undefined") {
					queryDate = '';
				}
				var queryDateBeginDay = queryDate.split('-')[0];

				var queryDateEndDay = queryDate.split('-')[1];
				var inputUser = $scope.queryInputUser;

				if (typeof (mktId) != "undefined" && mktId != "") {
					url = url + "&mktId=" + mktId;
				}

				if (typeof (adInfoWebName) != "undefined"
						&& adInfoWebName != "") {
					url = url + "&adInfoWebName=" + adInfoWebName;
				}

				if (typeof (adInfoPort) != "undefined" && adInfoPort != "") {
					url = url + "&adInfoPort=" + adInfoPort;
				}

				var adInfoPlatform = $scope.queryAdInfoPlatform;
				if (typeof (adInfoPlatform) != "undefined"
						&& adInfoPlatform != "") {
					url = url + "&adInfoPlatform=" + adInfoPlatform;
				}

				if (typeof (inputUser) != "undefined" && inputUser != "") {
					url = url + "&inputUser=" + inputUser;
				}

				if (typeof (cid) != "undefined"&& cid != "") {
					url = url + "&cid="+ cid;
				}

				// add by sxy 2015113 for BUG
				if (typeof (queryDateBeginDay) != "undefined"
						&& queryDateBeginDay != "") {
					url = url + "&adqueryDateBeginDay=" + queryDateBeginDay;
				}

				if (typeof (queryDateEndDay) != "undefined"
						&& queryDateEndDay != "") {
					url = url + "&adqueryDateEndDay=" + queryDateEndDay;
				}

				if (typeof (sid) != "undefined" && sid != "") {
					url = url + "&adquerySid=" + sid;
				}
				url = url + "&adType=" + $scope.UserDept;
				location.href = url;

			};

			 //全选或不选
            $scope.onAllCheckChange = function(p) 
            {
                var isChecked = p;
                for (var i = 1; i < $scope.webNames.length; i++) {
                	
                	$scope.webNames[i].checked = isChecked;
                	if(isChecked)
	                {
                		$scope.queryAdInfoWebName = "全部";
                		$scope.queryAdInfoWebNameId="-1";
                	}else{
                		$scope.queryAdInfoWebName = "";
                		$scope.queryAdInfoWebNameId = "";
                	}
                    
                	
                }
            }
          //单选
         $scope.onCheckChange = function() 
         {
               		                
        	 $scope.queryAdInfoWebName = "";
        	 $scope.queryAdInfoWebNameId="";
             var count = 0;
             
             for (var i = 0; i < $scope.webNames.length; i++) 
             {
            	 var isChecked = $scope.webNames[i].checked ;
            	 var webName   = $scope.webNames[i].name;
            	 var webId     = $scope.webNames[i].id;
            	 if(webId == -1)
                 {
            		 if($scope.selectAll != isChecked)
            		 {
            			 this.onAllCheckChange(isChecked);
            			 $scope.selectAll = isChecked; 
	            		 return; 
            		 }
            		 
                 }
            	 else
            	 {
            		 if(isChecked)
		             {
	                	if($scope.queryAdInfoWebName =="")
	                	{
	                		$scope.queryAdInfoWebName=$scope.webNames[i].name;
	                		$scope.queryAdInfoWebNameId=$scope.webNames[i].id;
	                	}
	                	else
	                	{
	                		$scope.queryAdInfoWebName+=","+$scope.webNames[i].name;
	                		$scope.queryAdInfoWebNameId+=","+$scope.webNames[i].id;
	                	}
	                	count++;
	                } 
            	 }	                	
		     }
             
             if(count == $scope.webNames.length - 1)
             {		                	
            	 $scope.webNames[0].checked = true;
            	 this.onAllCheckChange(true);
            	 $scope.selectAll = true;
             }else
             {
            	 $scope.webNames[0].checked = false;
            	 $scope.selectAll = false;
             }
        }
	    
     
			$scope.TimeCheck = function(checkTime) {
				var BeginDay = checkTime.split('-')[0];
				var EndDay = checkTime.split('-')[1];
				var timeExample = /^(\d{4}\d{2}\d{2})$/;
				if (!timeExample.test(BeginDay)) {
					dialog('时间格式非法，请重新输入', function() {
					}).showModal();
					return false;
				}
				if (!timeExample.test(EndDay)) {
					dialog('时间格式非法，请重新输入', function() {
					}).showModal();
					return false;
				}
				if (BeginDay > EndDay) {
					dialog('时间格式非法，请重新输入', function() {
					}).showModal();
					return false;
				}
				var resultBeginDay = new Array();
				resultBeginDay[0] = BeginDay.substring(0, 4);
				resultBeginDay[1] = BeginDay.substring(4, 6);
				resultBeginDay[2] = BeginDay.substring(6, 8);
				var resultEndDay = new Array();
				resultEndDay[0] = EndDay.substring(0, 4);
				resultEndDay[1] = EndDay.substring(4, 6);
				resultEndDay[2] = EndDay.substring(6, 8);
				if (resultBeginDay == null || resultEndDay == null) {
					dialog('时间格式非法，请重新输入', function() {
					}).showModal();
					return false;
				}
				var d1 = new Date(resultBeginDay[0], resultBeginDay[1] - 1,
						resultBeginDay[2]);
				var d2 = new Date(resultEndDay[0], resultEndDay[1] - 1,
						resultEndDay[2]);
				if ((d1.getFullYear() == resultBeginDay[0]
						&& (d1.getMonth() + 1) == resultBeginDay[1] && d1
						.getDate() == resultBeginDay[2])
						&& (d2.getFullYear() == resultEndDay[0]
								&& (d2.getMonth() + 1) == resultEndDay[1] && d2
								.getDate() == resultEndDay[2])) {
					return true;
				}
				dialog('时间格式非法，请重新输入', function() {
				}).showModal();
				return false;
			};

			// DSP位详情 为什么之前的刚刚函数没有提出来形成独立的方法集
			// add by sxy 20151110
			$scope.dspDetailInfo = function(r_Ssid, r_IadInfoId, r_SWebName,
					r_SChannel, r_SPosition, r_date) {

				var queryDate = $scope.queryDate;
				var queryDateBeginDay = queryDate.split('-')[0];
				var queryDateEndDay = queryDate.split('-')[1];
				$("#dsp_tbody").html("");

				$scope
						.callApi({
							url : webRoot + "/api/huaweiReport/dspDetailInfo",
							method : "GET",
							params : {
								adInfoId : r_IadInfoId,
								sid : r_Ssid,
								beginDate : queryDateBeginDay,
								endDate : queryDateEndDay,
								date : r_date

							}
						})
						.success(
								function(resp, status, headers, config) {
									if (resp.resultCode != 0) {
										$scope.failToOperate(resp);
										return;
									}

									// $scope.dsprowDetails = resp;

									var showDate = "";
									if (queryDateBeginDay != queryDateEndDay) {
										showDate = $scope.queryDate;
									} else {
										showDate = queryDateBeginDay;
									}
									$scope.dsphead = "网站名称" + r_SWebName
											+ " 频道" + r_SChannel + " 广告位"
											+ r_SPosition + " 在" + showDate
											+ "的投放明细";
									// ///////////////////////////////////////////////////////
									var users = resp.result;
									var webDns = "";
									var html = "";
									$("#dsp_tbody").html("");
									var start = 0;// 开始位置
									var rows = 1;// 合并的列数
									for (var i = 0; i < users.length; i++) {
										if (webDns == users[i].webDns) {
											rows++;
										} else {

											if (rows > 1) {// 存在需要合并的
												var num = rows;
												rows = rows + start;

												for (var j = start; j < rows; j++) {
													if (j == start) {
														html += "<tr><td style='background: #ffffff;'rowspan="
																+ num
																+ ">"
																+ users[j].webDns
																+ "</td>"
																+ "<td style='background: #ffffff;'>"
																+ users[j].Channel
																+ "</td>"
																+ "<td style='background: #ffffff;'>"
																+ users[j].bgPv
																+ "</td>"
																+ "<td style='background: #ffffff;'>"
																+ users[j].bgUv
																+ "</td>"
																+ "<td style='background: #ffffff;'>"
																+ users[j].djPv
																+ "</td>"
																+ "<td style='background: #ffffff;'>"
																+ users[j].djUv
																+ "</td></tr>    ";
													} else {
														html += "<tr><td style='display: none;background: #ffffff;'>"
																+ users[j].webDns
																+ "</td>"
																+ "<td style='background: #ffffff;'>"
																+ users[j].Channel
																+ "</td>"
																+ "<td style='background: #ffffff;'>"
																+ users[j].bgPv
																+ "</td>"
																+ "<td style='background: #ffffff;'>"
																+ users[j].bgUv
																+ "</td>"
																+ "<td style='background: #ffffff;'>"
																+ users[j].djPv
																+ "</td>"
																+ "<td style='background: #ffffff;'>"
																+ users[j].djUv
																+ "</td></tr>      ";
													}
												}
												// 当前是最后一条
												if (i == users.length - 1) {
													html += "<tr><td style='background: #ffffff;'>"
															+ users[i].webDns
															+ "</td>"
															+ "<td style='background: #ffffff;'>"
															+ users[i].Channel
															+ "</td>"
															+ "<td style='background: #ffffff;'>"
															+ users[i].bgPv
															+ "</td>"
															+ "<td style='background: #ffffff;'>"
															+ users[i].bgUv
															+ "</td>"
															+ "<td style='background: #ffffff;'>"
															+ users[i].djPv
															+ "</td>"
															+ "<td style='background: #ffffff;'>"
															+ users[i].djUv
															+ "</td></tr>    ";

												}
												rows = 1;
											} else {
												if (i != 0) {
													html += "<tr><td style='background: #ffffff;'>"
															+ users[i - 1].webDns
															+ "</td>"
															+ "<td style='background: #ffffff;'>"
															+ users[i - 1].Channel
															+ "</td>"
															+ "<td style='background: #ffffff;'>"
															+ users[i - 1].bgPv
															+ "</td>"
															+ "<td style='background: #ffffff;'>"
															+ users[i - 1].bgUv
															+ "</td>"
															+ "<td style='background: #ffffff;'>"
															+ users[i - 1].djPv
															+ "</td>"
															+ "<td style='background: #ffffff;'>"
															+ users[i - 1].djUv
															+ "</td></tr>    ";
												}
												if (i == users.length - 1) {
													html += "<tr><td style='background: #ffffff;'>"
															+ users[i].webDns
															+ "</td>"
															+ "<td style='background: #ffffff;'>"
															+ users[i].Channel
															+ "</td>"
															+ "<td style='background: #ffffff;'>"
															+ users[i].bgPv
															+ "</td>"
															+ "<td style='background: #ffffff;'>"
															+ users[i].bgUv
															+ "</td>"
															+ "<td style='background: #ffffff;'>"
															+ users[i].djPv
															+ "</td>"
															+ "<td style='background: #ffffff;'>"
															+ users[i].djUv
															+ "</td></tr>    ";

												}

											}
											webDns = users[i].webDns;
											start = i;
										}

									}
									if (rows > 1) {
										var num = rows;
										rows = rows + start;
										for (var j = start; j < rows; j++) {
											if (j == start) {
												html += "<tr><td style='background: #ffffff;' rowspan="
														+ num
														+ ">"
														+ users[j].webDns
														+ "</td>"
														+ "<td style='background: #ffffff;'>"
														+ users[j].Channel
														+ "</td>"
														+ "<td style='background: #ffffff;'>"
														+ users[j].bgPv
														+ "</td>"
														+ "<td style='background: #ffffff;'>"
														+ users[j].bgUv
														+ "</td>"
														+ "<td style='background: #ffffff;'>"
														+ users[j].djPv
														+ "</td>"
														+ "<td style='background: #ffffff;'>"
														+ users[j].djUv
														+ "</td></tr>    ";
											} else {
												html += "<tr><td style='display: none;background: #ffffff;'>"
														+ users[j].webDns
														+ "</td>"
														+ "<td style='background: #ffffff;'>"
														+ users[j].Channel
														+ "</td>"
														+ "<td style='background: #ffffff;'>"
														+ users[j].bgPv
														+ "</td>"
														+ "<td style='background: #ffffff;'>"
														+ users[j].bgUv
														+ "</td>"
														+ "<td style='background: #ffffff;'>"
														+ users[j].djPv
														+ "</td>"
														+ "<td style='background: #ffffff;'>"
														+ users[j].djUv
														+ "</td></tr>      ";
											}
										}
									}
									$("#dsp_tbody").html(html);
									// $("#dsp_tbody").append(html);
								}).error(
								function(response, status, headers, config) {
									$scope.httpError(status);
								});
				$("#dsp_tbody").html("");
				dialog(
						{
							title : getLocalTag("id_dsp_details_dialog1", "详情"),
							ok : true,
							okValue : getLocalTag('confirm', 'Yes'),
							content : document
									.getElementById('id_dsp_details_dialog1'),
							lock : true,
							opacity : 0.3
						}).showModal();

			};

		}

	};

	exports.instance = function(webRoot, metaName, segments) {
		var obj = MetaBase.instance(webRoot, metaName, segments);
		$.extend(obj, MetaHuaweiReport);
		return obj;
	};
});

function creatInitializeDate() {
	var now = new Date();
	now.setDate(now.getDate() - 1);
	var mouthDayNow = checkDateFromat(now.getMonth(), now.getDate());
	var day0 = now.getFullYear() + '' + mouthDayNow;

	now = new Date();
	now.setDate(now.getDate() - 7);
	var mouthDayBefore = checkDateFromat(now.getMonth(), now.getDate());
	var day1 = now.getFullYear() + '' + mouthDayBefore;
	var InitializeDate = day1 + '-' + day0;
	return InitializeDate;
}
function checkDateFromat(mouth, day) {
	var monthFormat;
	var dayFormat;
	if (mouth + 1 < 10) {
		monthFormat = '0' + (mouth + 1);
	} else {
		monthFormat = mouth + 1;
	}

	if (day < 10) {
		dayFormat = '0' + day;
	} else {
		dayFormat = day;
	}
	var InitializeDate = monthFormat + '' + dayFormat;
	return InitializeDate;
}