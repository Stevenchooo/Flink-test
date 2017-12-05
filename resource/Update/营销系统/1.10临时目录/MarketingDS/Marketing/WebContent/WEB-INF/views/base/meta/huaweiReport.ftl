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
              <input type="button" ng-click="reportExport()"
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
             <th width="10%">
                 <div class="xytt-holder">
                     ${lang.mktinfoName}
                     <span class="xy-tooltip xyt-below" style="margin-top:15px;border-right: 1px solid #D2D2D2;">
                         营销活动名称
                         <b class="xytdeco"></b>
                     </span>
                 </div>
             </th>
             <th width="8%">
                 <div class="xytt-holder">             	
                     ${lang.mktLandInfoWebName}
                     <span class="xy-tooltip xyt-below" style="margin-top:15px;border-right: 1px solid #D2D2D2;">
                         投放媒体名称
                         <b class="xytdeco"></b>
                     </span>
                 </div>                         
             </th>
             <th width="7%">
                 <div class="xytt-holder">
                     ${lang.mktLandInfoChannel}
                     <span class="xy-tooltip xyt-below" style="margin-top:15px;border-right: 1px solid #D2D2D2;">
                         投放媒体频道
                         <b class="xytdeco"></b>
                     </span>
                 </div>  
             </th>
             <th width="8%">
                 <div class="xytt-holder">
                     ${lang.mktLandInfoAdPosition}
                     <span class="xy-tooltip xyt-below" style="margin-top:15px;border-right: 1px solid #D2D2D2;">
                         投放媒体广告位置
                         <b class="xytdeco"></b>
                     </span>
                 </div>
             </th> 
             <th width="5%">
                 <div class="xytt-holder">
                     ${lang.mktLandInfoPort}
                     <span class="xy-tooltip xyt-below" style="margin-top:15px;border-right: 1px solid #D2D2D2;">
                         端口所属
                         <b class="xytdeco"></b>
                     </span>
                 </div>
             </th>
             <th width="8%">
                 <div class="xytt-holder">
                     ${lang.mktPlatform}
                     <span class="xy-tooltip xyt-below" style="margin-top:15px;border-right: 1px solid #D2D2D2;">
                         广告着陆的网站平台
                         <b class="xytdeco"></b>
                     </span>
                 </div>
             </th>
             <th width="5%">
                 <div class="xytt-holder">
                     ${lang.reportDate}
                     <span class="xy-tooltip xyt-below" style="margin-top:15px;border-right: 1px solid #D2D2D2;">
                         统计周期
                         <b class="xytdeco"></b>
                     </span>
                 </div>
             </th>
             <th width="8%">
                 <div class="xytt-holder">
                     ${lang.mktLandInfoSID}
                     <span class="xy-tooltip xyt-below" style="margin-top:15px;border-right: 1px solid #D2D2D2;">
                         广告位唯一标识
                         <b class="xytdeco"></b>
                     </span>
                 </div>
             </th>
             <th width="8%">
                 <div class="xytt-holder">
                     ${lang.mktLandInfoCID}
                     <span class="xy-tooltip xyt-below" style="margin-top:15px;border-right: 1px solid #D2D2D2;">
                         Vmall渠道号或广告位标识
                         <b class="xytdeco"></b>
                     </span>
                 </div>
             </th>
             <th width="6%">${lang.bgPv}</th>
             <th width="6%">${lang.bgUv}</th>
             <th width="6%">${lang.djPv}</th>
             <th width="6%">${lang.djUv}</th>
             <th width="5%">${lang.dsp}</th>
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
             <td>{{row.bgPv}}</td>
             <td>{{row.bgUv}}</td>
             <td>{{row.djPv}}</td>
             <td>{{row.djUv}}</td>
             <td>
             	   <a ng-click="dspDetailInfo('{{row.mktLandInfoSID}}','{{row.adInfoId}}','{{row.adInfoWebName}}','{{row.adInfoChannel}}','{{row.adInfoPosition}}','{{row.reportDate}}')" >{{row.dsp}}</a>
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
    
      <div id="id_dsp_details_dialog1" style="display:none;font-size: 12px;">
      <form>
        <table id="tab_dsp_details"  border="0" class="listTab" style="width:600px;">
        	<tr >
             <th colspan="6" ng-model="dsphead">{{dsphead}}</th>
          </tr>
          
          <tr>
             <th width="30%">${lang.webDns}</th>
             <th width="30%">${lang.dspChannel}</th>
             <th width="10%">${lang.bgPv}</th>
             <th width="10%">${lang.bgUv}</th>
             <th width="10%">${lang.djPv}</th>
             <th width="10%">${lang.djUv}</th>
        </tr>

        <tbody id="dsp_tbody">
        
        </tbody>

        </table>
      </form>
      </div>       
    </div> <!-- end of up-detable-->    
    </div> <!-- end of upmob-filter --> 
   
    <div id="id_selectMkt_dialog" style="font-size: 12px; font-family: Microsoft; max-height: 500px; overflow-y: auto;">
      <form name="selectMktForm"  novalidate autocomplete="off">
         <table style="width:850px;"  >
            
           <tbody id="ad_tbody">
            
            </tbody>
            
        </table>
        
      </form>
    </div>    
  </div> <!-- end of ListCtrl -->
</div> <!-- end of ModelList -->



<script type="text/javascript">
 
seajs.use(["modules/base/model", "modules/base/meta/huaweiReport"], function(model, MetaHuaweiReport) {
    var meta =  MetaHuaweiReport.instance("${vars.webRoot}", 'huaweiReport', ["mktinfoId","name"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        from:0,
        perPage:500
    });
});
</script>