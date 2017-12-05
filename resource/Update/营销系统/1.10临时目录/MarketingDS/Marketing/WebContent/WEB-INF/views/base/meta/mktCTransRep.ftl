<div class="upmc-wp" ng-app="ModelList">
  <div style="width:100%;" ng-controller="ListCtrl" id="data_grid" ng-cloak>
  
    <div class="upmob-filter"> 
       <table style="width:100%">
          <tr>
          <td style="LINE-HEIGHT: 24px;">${lang.mktinfoName}
              <input type="text" ng-click="getMktName()" ng-trim="true"  ng-model="queryMktId" style="width:160px;height:24px;">
          </td>
          
         <td style="LINE-HEIGHT: 30px;">${lang.adInfoWebName}
    
              
              <div class="dpf-item">
                  <div class="mk-select multi-select">
                      <div class="mks-box mkcl">
                          <div class="mks-left mks-trigger" >
                            <input ng-readonly="webNames.length == 0|| webNames==null" 
                               ng-model="queryAdInfoWebName" type="text" style="height:100%;color:black" class="mks-value"  readonly/>
                          </div>
                          <a href="javascript:void(0);" class="mks-right" hidefocus><span class="deco"></span></a>
                      </div>
                      <ul class="mks-options mo-multi">
                          <label  ng-repeat="fk1 in webNames" title="{{fk1.name}}">
                              <input type="checkbox" ng-model="fk1.checked" ng-change="onCheckChange()"/>{{fk1.name}}
                          </label>
                      </ul>
                  </div>
              </div>
           
         </td>
         
          
          <td style="LINE-HEIGHT: 30px;">${lang.adInfoPort}
           <select ng-model="queryAdInfoPort" 
                  ng-trim="true" ng-options="p.id as p.name for p in portNames" style="height:24px;width:160px">
           </select>
          </td>
          
          <td style="LINE-HEIGHT: 30px;">${lang.adInfoPlatform}
           <select ng-model="queryAdInfoPlatform" 
                  ng-trim="true" ng-options="p.id as p.name for p in platNames" style="height:24px;width:160px">
           </select>
          </td>
          <td >
              <input type="button" ng-click="transReportExport()"
              style="width: 80px;" class="btn-lightblue" value="${lang.reportExport}">
          </td>
          </tr>
          <tr>
          <td style="LINE-HEIGHT: 24px;padding-left: 35px;">${lang.operator}
           <select ng-model="queryInputUser" 
                  ng-trim="true" ng-options="p.id as p.name for p in inputUserNames" style="height:24px;width:160px">
           </select>
          </td>
          
          <td style="LINE-HEIGHT: 24px;padding-left:30px;">Cid
              <input type="text" ng-trim="true"  ng-model="inputCid" style="width:160px;height:24px;">
          </td>
          
          <td style="LINE-HEIGHT: 24px;">${lang.reportDate}
              <input style="width:160px;height:24px;" type="text" readonly="true" id="queryDate" name="queryDate" ng-model="queryDate" required/>
          </td>
          
          <td style="LINE-HEIGHT: 24px;padding-left:30px;">${lang.inputSid}
              <input type="text" ng-trim="true"  ng-model="inputSid" style="width:160px;height:24px;">
          </td>
          
            
          <td >
              <input type="button" ng-click="pageClick(0)"
              style="width: 80px;" class="btn-lightblue" value="${lang.query}">
          </td>
          
          </tr> 
       </table>
        
      <br/>
      
      <div class="upmc-controls upmcc-multi">        
      </div> 
      <div class="up-detable">
        <table>
          <tr>
             <th width="8%" >
	             <div class="xytt-holder">
	             ${lang.mktinfoName}
	             <span class="xy-tooltip xyt-below" style="margin-top:15px;border-right: 1px solid #D2D2D2;">
		                       	   营销活动名称
		 		  	 <b class="xytdeco"></b>
					 </span>
	             </div>
             </th>
              <th width="8%" >
	              <div class="xytt-holder">
	              <span class="xy-tooltip xyt-below" style="margin-top:15px;border-right: 1px solid #D2D2D2;">
		                       	 投放媒体名称
		 		  	 <b class="xytdeco"></b>
					 </span>
	             ${lang.mktMediaName}
	             </div>
             </th>
             <th width="6%" >
	             <div class="xytt-holder">
	             <span class="xy-tooltip xyt-below" style="margin-top:15px;border-right: 1px solid #D2D2D2;">
		                       	 投放媒体频道
		 		  	 <b class="xytdeco"></b>
					 </span>
	             ${lang.adInfoChannel}
	             </div>
             </th>
 			 <th width="8%" >
	 			 <div class="xytt-holder">
	             <span class="xy-tooltip xyt-below" style="margin-top:15px;border-right: 1px solid #D2D2D2;">
		                       	投放媒体广告位置
		 		  	 <b class="xytdeco"></b>
					 </span>
	             ${lang.adInfoAdPosition}
	             </div>
             </th>             
             <th width="6%" >
	             <div class="xytt-holder">
	             <span class="xy-tooltip xyt-below" style="margin-top:15px;">
		                       	端口所属
		 		  	 <b class="xytdeco"></b>
					 </span>
	             ${lang.adInfoPort}
	             </div>
             </th> 
             <th width="6%" >
	             <div class="xytt-holder">
	             <span class="xy-tooltip xyt-below" style="margin-top:15px;">
		                       	广告着陆的网站平台
		 		  	 <b class="xytdeco"></b>
					 </span>
	             ${lang.adInfoPlatform}
	             </div>
             </th>
			 <th width="6%" >
				 <div class="xytt-holder">
	             <span class="xy-tooltip xyt-below" style="margin-top:15px;">
		                       	统计周期
		 		  	 <b class="xytdeco"></b>
					 </span>
	             ${lang.reportDate}
	             </div>
             </th>
			 <th width="6%">
				 <div class="xytt-holder"> 
	             <span class="xy-tooltip xyt-below" style="margin-top:15px;">
		                       	广告位唯一标识
		 		  	 <b class="xytdeco"></b>
					 </span>
	             ${lang.mktLandInfoSID}
	             </div>
             </th>
			 <th width="6%">
				 <div class="xytt-holder"> 
	             <span class="xy-tooltip xyt-below" style="margin-top:15px;">
		                       	Vmall渠道号或广告位标识
		 		  	 <b class="xytdeco"></b>
					 </span>
	             ${lang.mktLandInfoCID}
	             </div>
             </th>
			 <th width="6%">
				 <div class="xytt-holder"> 
	             <span class="xy-tooltip xyt-below" style="margin-top:15px;">
		                       	着陆人数，通过cookie+ip按天去重
		 		  	 <b class="xytdeco"></b>
					 </span>
	             ${lang.landUv}
	             </div>
             </th>             
			 <th width="6%">
				 <div class="xytt-holder"> 
	             <span class="xy-tooltip xyt-below" style="margin-top:15px;">
		                       	着陆率=着陆UV/点击UV
		 		  	 <b class="xytdeco"></b>
					 </span>
	             ${lang.landRate}
	             </div>
             </th>
			 <th width="6%">
				 <div class="xytt-holder"> 
	             <span class="xy-tooltip xyt-below" style="margin-top:15px;">
		                       	通过该广告位完成支付的订单
		 		  	 <b class="xytdeco"></b>
					 </span>
	             ${lang.orderPayCount}
	             </div>
             </th>
			 <th width="6%">
				 <div class="xytt-holder"> 
	             <span class="xy-tooltip xyt-below" style="margin-top:15px;">
		                       	通过该广告位完成支付，且没有取消支付的订单
		 		  	 <b class="xytdeco"></b>
					 </span>
	             ${lang.realOrderCount}
	             </div>
             </th>
			 <th width="6%" >
				 <div class="xytt-holder"> 
	             <span class="xy-tooltip xyt-below" style="margin-top:15px;">
		                       	通过该广告位完成支付的订单金额总计
		 		  	 <b class="xytdeco"></b>
					 </span>
	             ${lang.orderPayPriceCount}
	             </div>
             </th>
			 <th width="6%">
				 <div class="xytt-holder"> 
	             <span class="xy-tooltip xyt-below" style="margin-top:15px;">
		                  	    通过该广告位完成支付，且没有取消支付的订单金额总计
		 		  	 <b class="xytdeco"></b>
					 </span>
	             ${lang.realOrderAmount}
	             </div>
             </th>
             <th width="6%">${lang.mktInfoDetail}</th>
        </tr>     
        
        <tr ng-repeat="row in resp.result">             	
             <td>{{row.mktInfoName}}</td>
             <td>{{row.adInfoWebName}}</td>
             <td>{{row.adInfoChannel}}</td>
             <td>{{row.adInfoPosition}}</td>
             <td>{{row.adInfoPort}}</td>
             <td>{{row.platform}}</td>
             <td>{{row.reportDate}}</td>
             <td>{{row.mktLandInfoSID}}</td>
             <td>{{row.mktLandInfoCID}}</td>
             <td>{{row.landingUv}}</td>
             <td>{{row.landingRate}}</td>
             <td>{{row.orderPayCount}}</td>
             <td>{{row.realOrderCount}}</td>
             <td>{{row.orderPayPriceCount}}</td>
             <td>{{row.realOrderAmount}}</td>
              <td>
               	<a href="javascript:void(0)" ng_click="mktDetailInfo('{{row.mktInfoName}}','{{row.adInfoWebName}}','{{row.mktLandInfoSID}}','{{row.mktLandInfoCID}}','{{row.reportDate}}')">详情</a>
               </td>
          </tr>
        </table>
        <div class="up-pager">
          <div class="uppg">
              <a href="javascript:void(0);" ng-repeat="pg in pages" ng-click="pageClick({{pg.from}})" class="{{pg.cls}}">{{pg.str}}</a>
          </div>
          <div class="pg-count">
                       共<span class="pg-no">{{pageNum}}</span>页
          </div>
          
        </div>
      </div>
      
    </div> <!-- end of upmob-filter -->   
    <div id="id_mkt_info_detail" class="up-detable" style="display:none;font-size: 12px;">
       <table class="css-table" style="table-layout:fixed; ">
        <tr > 
            <th width="6%">
            	<div class="xytt-holder"> 
	               <span class="xy-tooltip xyt-below" style="margin-top:20px;">
	            	                营销活动名称
	            	<b class="xytdeco"></b>
	            </span>
            ${lang.mktinfoName} 
            </div>
            </th>
            <th width="6%">
            	<div class="xytt-holder"> 
	               <span class="xy-tooltip xyt-below" style="margin-top:20px;">
	            	                投放媒体名称
	            	<b class="xytdeco"></b>
	            </span>
            ${lang.mktMediaName}
            </div>
            </th>
            <th width="3%">
	            <div class="xytt-holder"> 
	               <span class="xy-tooltip xyt-below" style="margin-top:20px;">
	            	            投放媒体频道
	            	<b class="xytdeco"></b>
	            </span>
	            ${lang.adInfoChannel}
	            </div>
            </th>
            <th width="3%">
            	<div class="xytt-holder"> 
		            <span class="xy-tooltip xyt-below" style="margin-top:20px;">
		            	            投放媒体广告位置
		            	<b class="xytdeco"></b>
		            </span>
            ${lang.adInfoAdPosition}
            </div>
            </th>
            
            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:20px;">
            	            端口所属
            	<b class="xytdeco"></b>
            </span>
            ${lang.adInfoPort}
            </div>
            </th>
            <th width="5%">
          	  ${lang.mktinfoProduct}
            </th>
            <th width="4%">
          	  ${lang.mktLandInfoUseDate}
            </th>
            <th width="4%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:20px;">
            	          广告着陆的网站平台
            	<b class="xytdeco"></b>
            </span>
            ${lang.adInfoPlatform}
            </div>
            </th>
            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:20px;">
            	          统计周期
            	<b class="xytdeco"></b>
            </span>
            ${lang.reportDate}
            </div>
            </th>
            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:20px;">
            	          广告位唯一标识
            	<b class="xytdeco"></b>
            </span>
            ${lang.mktLandInfoSID}
            </div>
            </th>
            <th width="3%" >
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:20px;">
            	   Vmall渠道号或广告位标识
            	<b class="xytdeco"></b>
            </span>
            ${lang.mktLandInfoCID}
            </div>
            </th>
            <th width="3%">
               ${lang.bgPv}
            </th>
            <th width="3%" >
               ${lang.bgUv}
            </th>
            <th width="3%">
               ${lang.djPv}
            </th>
            <th width="3%" >
               ${lang.djUv}
            </th>
            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:16px;">
            	着陆人数，通过cookie+ip按天去重
            	<b class="xytdeco"></b>
            </span>
            ${lang.landUv}
            </div>
            </th>
            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:16px;">
            	着陆率=着陆UV/点击UV
            	<b class="xytdeco"></b>
            </span>
            ${lang.landRate}
            </div>
            </th>
            <th width="3%" >
               ${lang.orderCount}
            </th>
            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:16px;">
            	通过该广告位完成支付的订单
            	<b class="xytdeco"></b>
            </span>
            ${lang.orderPayCount}
            </div>
            </th>
            
            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:16px;">
            	通过该广告位完成支付，且没有取消支付的订单
            	<b class="xytdeco"></b>
            </span>
            ${lang.realOrderCount}
            </div>
            </th>
            
            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:16px;">
            	通过该广告位完成支付的订单金额总计
            	<b class="xytdeco"></b>
            </span>
            ${lang.orderPayPriceCount}
            </div>
            </th>
            
            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:16px;">
            	通过该广告位完成支付，且没有取消支付的订单金额总计
            	<b class="xytdeco"></b>
            </span>
            ${lang.realOrderAmount}
            </div>
            </th>
            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:16px;">
            	通过该广告位完成预约的用户数
            	<b class="xytdeco"></b>
            </span>
            ${lang.bespeakNums}
            </div>
            </th>
            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:16px;">
            	广告位来源
            	<b class="xytdeco"></b>
            </span>
            ${lang.resource}
            </div>
            </th>
            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:16px;">
            	投放人
            	<b class="xytdeco"></b>
            </span>
            ${lang.detailsOperator}
            </div>
            </th>

            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:16px;">
            	通过关联cookie的算法近似计算该广告位下单数
            	<b class="xytdeco"></b>
            </span>
            ${lang.ocBI}
            </div>
            </th>
            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:16px;">
            	通过关联cookie的算法近似计算该广告位支付订单数
            	<b class="xytdeco"></b>
            </span>
            ${lang.opcBI}
            </div>
            </th>
            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:16px;">
            	通过关联cookie的算法近似计算该广告位实际支付订单数
            	<b class="xytdeco"></b>
            </span>
            ${lang.ropcBI}
            </div>
            </th>
            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:16px;">
            	通过关联cookie的算法近似计算该广告位支付金额
            	<b class="xytdeco"></b>
            </span>
            ${lang.oppcBI}
            </div>
            </th>
            <th width="3%">
            <div class="xytt-holder"> 
               <span class="xy-tooltip xyt-below" style="margin-top:16px;">
            	通过关联cookie的算法近似计算该广告位实际支付金额
            	<b class="xytdeco"></b>
            </span>
            ${lang.roppcBI}
            </div>
            </th>
            
        </tr>
        <tr>
          <td>{{resp.result[index].mktInfoName}}</td>
          <td>{{resp.result[index].adInfoWebName}}</td>
          <td>{{resp.result[index].adInfoChannel}}</td>
          <td>{{resp.result[index].adInfoPosition}}</td>
          <td>{{resp.result[index].adInfoPort}}</td>
          <td>{{resp.result[index].productName}}</td>
          <td>{{resp.result[index].deliveryTimes}}</td>
          <td>{{resp.result[index].platform}}</td>
          <td>{{resp.result[index].reportDate}}</td>
          <td>{{resp.result[index].mktLandInfoSID}}</td>
          <td>{{resp.result[index].mktLandInfoCID}}</td> 
          <td>{{resp.result[index].bgPv}}</td>
          <td>{{resp.result[index].bgUv}}</td>
          <td>{{resp.result[index].djPv}}</td>
          <td>{{resp.result[index].djUv}}</td>
          <td>{{resp.result[index].landingUv}}</td> 
          <td>{{resp.result[index].landingRate}}</td>
          <td>{{resp.result[index].orderCount}}</td>
          <td>{{resp.result[index].orderPayCount}}</td>
          <td>{{resp.result[index].realOrderCount}}</td>
          <td>{{resp.result[index].orderPayPriceCount}}</td>
          <td>{{resp.result[index].realOrderAmount}}</td>
          <td>{{resp.result[index].reserverUv}}</td>
          <td>{{resp.result[index].resource}}</td> 
          <td>{{resp.result[index].operator}}</td>
          <td>{{resp.result[index].orderCountBi}}</td>
          <td>{{resp.result[index].orderPayCountBi}}</td>
          <td>{{resp.result[index].realOrderCountBi}}</td>
          <td>{{resp.result[index].orderPayPriceCountBi}}</td> 
          <td>{{resp.result[index].realOrderAmountBi}}</td>
        </tr>
      </table>
      
    </div><!--end of id_mkt_info_detail-->
    <div id="id_selectMkt_dialog" style="font-size: 12px; font-family: Microsoft; max-height: 500px; overflow-y: auto;">
      <form name="selectMktForm"  novalidate autocomplete="off">
         <table style="width:850px;">            
           <tbody id="ad_tbody">            
           </tbody>            
         </table>        
      </form>
    </div>  
  </div> <!-- end of ListCtrl -->
</div> <!-- end of ModelList -->



<script type="text/javascript">
 
seajs.use(["modules/base/model", "modules/base/meta/mktCTransRep"], function(model, MetamktCTransRep) {
    var meta =  MetamktCTransRep.instance("${vars.webRoot}", 'mktCTransRep', ["mktinfoId","name"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        from:0,
        perPage:500
    });
});
</script>