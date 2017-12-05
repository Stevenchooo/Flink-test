   
<div class="upmc-wp"  ng-app="ModelList">
  <div style="width:100%;" ng-controller="ListCtrl" id="data_grid" ng-cloak>
  
    <#assign cr=(userRight?index_of("|c|") >= 0) />    
    <#assign dr=(userRight?index_of("|d|") >= 0) />    
    <#assign er=(userRight?index_of("|u|") >= 0) />
  
    <div class="upmob-filter">
       <table style="width:100%">
        <tr>
        <td style="LINE-HEIGHT: 30px;">${lang.mktinfoName}
        <select ng-model="queryMktId" 
                ng-trim="true" ng-options="p.mktinfoId as p.mktinfoName for p in mktInfos" style="width:160px;height:24px;">
        </td>
        
        <td style="LINE-HEIGHT: 30px;">${lang.adInfoWebName}
         <select ng-model="queryAdInfoWebName" 
                ng-trim="true" ng-options="p.id as p.name for p in webNames" style="height:24px;width:160px">
         </select>
        </td>
        
        <td style="LINE-HEIGHT: 30px;">${lang.adInfoPort}
         <select ng-model="queryAdInfoPort" 
                ng-trim="true" ng-options="p.id as p.name for p in portNames" style="height:24px;width:160px">
         </select>
        </td>
        
        
       <td style="LINE-HEIGHT: 30px;">${lang.mktLandInfoCurrentStatus}
         <select ng-model="queryAdState" 
                ng-trim="true" ng-options="p.id as p.name for p in states" style="width:150px;height:24px;">
         </select>
        </td>
        
        <tr>
        <td style="LINE-HEIGHT: 24px;padding-left: 35px;">${lang.operator}
         <select ng-model="queryInputUser" 
                ng-trim="true" ng-options="p.id as p.name for p in inputUserNames" style="height:24px;width:160px">
         </select>
        </td>
        

        <td style="LINE-HEIGHT: 24px;">${lang.mktPlatform}
        
         <select ng-model="landPlatform" 
                ng-trim="true" ng-options="p.id as p.name for p in landPlatforms" style="height:24px;width:160px">
         </select>
        </td>
        
        <td style="LINE-HEIGHT: 24px;">${lang.updateDate}
            <input style="width:160px;height:24px;" type="text"  id="queryDate" name="queryDate" ng-model="queryDate" />
        </td>
        
        <td align="left" style="padding-left: 120px">
            <input type="button" id="queryButton"  ng-click="pageClick(0)"
            style="width: 80px;" class="btn-lightblue" value="${lang.query}">
        </td>
        </tr>
      </table>
    </div>    
    
    <div class="upmc-controls upmcc-multi">

     <#if er>
        <a href="javascript:void(0)" ng-click="batchPublish()"><b class="link-icon li-batinput"></b> <span class="vm">${lang.batchPublishMonitorInfo}</span></a>
        <a href="javascript:void(0)" ng-click="mktMonitorEmail()"><b class="link-icon li-email"></b> <span class="vm">${lang.mktInfoEmail}</span></a>
     </#if>
        
    </div> 
    
    <div class="up-detable">
      <table >
        <tr> 
             <th width="5%">${lang.mktLandInfoAId}</th>
             <th width="10%">${lang.mktinfoName}</th>
             <th width="5%">${lang.adInfoWebName}</th>
             <th width="10%">${lang.adInfoChannel}</th>
             <th width="10%">${lang.adInfoAdPosition}</th>
             <th width="5%">${lang.adInfoPort}</th>
             <th width="5%">${lang.mktLandInfoCID}</th>
             <th width="10%">${lang.mktLandInfoLandUrl}</th>
             <th width="10%">${lang.monitorBiCode}</th>
             <th width="10%">${lang.monitorExposureUrl}</th>
             <th width="10%">${lang.monitorClickUrl}</th>
             <th width="5%">${lang.adInfoState}</th>
             <th width="5%">${lang.adInfoOper}</th>
        </tr>
        <tr ng-repeat="row in resp.result">
             <td>{{row.adInfoId}}</td>
             <td>{{row.mktLandInfoName}}</td>
             <td>{{row.adInfoWebName}}</td>
             <td>{{row.adInfoChannel}}</td>
             <td>{{row.adInfoPosition}}</td>
             <td>{{row.adInfoPort}}</td>
             <td>{{row.cid}}</td>
             <td>{{row.landLink}}</td>
             <td>{{row.monitorBiCode}}</td>
             <td>{{row.monitorExposureUrl}} <a ng-show="isReset('{{row.adInfoStateId}}')" href="{{row.monitorExposureUrl}}" target="_blank">${lang.monitorTest}</a></td>
             <td>{{row.monitorClickUrl}} <a ng-show="isReset('{{row.adInfoStateId}}')" href="{{row.monitorClickUrl}}" target="_blank">${lang.monitorTest}</a></td>
             <td>{{row.adInfoState}}</td>
             <td>
                  <#if er>
                      <a href="javascript:void(0)" class="tb-reset" ng-show="isPublish('{{row.monitorBiCode}}','{{row.adInfoStateId}}')" ng-click="modelPublish('{{row.adInfoId}}')">${lang.monitorPublish}</a>
                  </#if>
                  <#if er>
                     <a href="javascript:void(0)" class="tb-reset" ng-show="isReset('{{row.adInfoStateId}}')" ng-click="modelReset('{{row.adInfoId}}')">${lang.monitorReset}</a>
                  </#if>
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
  
       
  
 
   
   
<div id="monitorInfo_batchPublish_dialog" style="display:none;font-size: 12px;">


     
     <div id="">
                                 确定要批量生成BI监控代码吗？
     </div>
     
    <br>
     
     <input type="button" ng-click="batchPublishSure()"  
        style="width: 140px; height: 30px;" class="btn-lightblue" value="${lang.batchPublishMonitorInfo}">
        
     <input type="button" ng-click="batchPublishCancel()" id="batchResDown" 
        style="width: 140px; height: 30px;" class="btn-lightblue" value="${lang.batchPublishCancel}">
     
     
</div>
<div id="id_mktMonitorInfoEmail_dialog" style="display:none;font-size: 12px;">
  <form name="mktMonitorInfoEmailForm" class="css-form" novalidate autocomplete="off">
     <table class="listTab" style="width:600px;">
        <tr>
             <th width="20%">${lang.account}</th>
             <th width="30%">${lang.name}</th>
             <th width="30%">${lang.department}</th>
        </tr>
        <tr ng-repeat="row in mktMonitorEmailUsers">
             <td>{{row.account}}</td>
             <td>{{row.name}}</td>
             <td>{{row.department}}</td>
        </tr>
    </table>
  </form>
</div>     
    
</div> <!-- end of ListCtrl -->
</div> <!-- end of ModelList -->



<script type="text/javascript">
seajs.use(["modules/base/model", "modules/base/meta/mktMonitor"], function(model, MetaMktMonitor) {
    var meta = MetaMktMonitor.instance("${vars.webRoot}", 'monitor', ["monitor","name"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        monitorPlatform:0,
        from:0,
        perPage:15
    });
});
</script>