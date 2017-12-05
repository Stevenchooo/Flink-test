   
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
            </select>
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
        </tr>
        <tr>
        <td style="LINE-HEIGHT: 24px;padding-left: 35px;">${lang.operator}
         <select ng-model="queryInputUser" 
                ng-trim="true" ng-options="p.id as p.name for p in inputUserNames" style="height:24px;width:160px">
         </select>
        </td>
        
        <td style="LINE-HEIGHT: 24px;">${lang.adInfoDeliveryPeriod}
         <input style="width:160px;height:24px;" type="text" id="queryAdInfoDeliveryTimes" name="queryAdInfoDeliveryTimes" ng-model="queryAdInfoDeliveryTimes" />
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
        <a href="javascript:void(0)" ng-click="batchLoad()" ><b class="link-icon li-create"></b> <span class="vm">${lang.mktLandInfoBatchEdit}</span></a>
        <a href="javascript:void(0)" ng-click="batchPublish()"><b class="link-icon li-batinput"></b> <span class="vm">${lang.batchPublishLandInfo}</span></a>
        <a href="javascript:void(0)" ng-click="mktLandEmail()"><b class="link-icon li-email"></b> <span class="vm">${lang.mktInfoEmail}</span></a>
     </#if>
        
    </div> 
    
    <div class="up-detable">
      <table >
        <tr> 
             <th width="5%">${lang.mktLandInfoAId}</th>
             <th width="10%">${lang.mktinfoName}</th>
             <th width="8%">${lang.mktLandInfoWebName}</th>
             <th width="7%">${lang.mktLandInfoChannel}</th>
             <th width="5%">${lang.mktLandInfoAdPosition}</th>
             <th width="5%">${lang.mktLandInfoPort}</th>
             <th width="4%">${lang.mktLandInfoSID}</th>
             <th width="8%">${lang.mktLandInfoCPSName}</th>
             <th width="5%">${lang.mktLandInfoSource}</th>
             <th width="5%">${lang.mktLandInfoLandChannelName}</th>
             <th width="5%">${lang.mktLandInfoLandChannel}</th>
             <th width="4%">${lang.mktLandInfoCID}</th>
             <th width="10%">${lang.mktLandInfoLandUrl}</th>
             <th width="5%">${lang.mktLandInfoStatus}</th>
             <th width="6%">${lang.mktLandInfoOperat}</th>
             
        </tr>
        <tr ng-repeat="row in resp.result">
             <td>{{row.mktLandInfoAid}}</td>
             <td>{{row.mktLandInfoName}}</td>
             <td>{{row.mktLandInfoWebName}}</td>
             <td>{{row.mktLandInfoChannel}}</td>
             <td>{{row.mktLandInfoAdPosition}}</td>
             <td>{{row.mktLandInfoPort}}</td>
             <td>{{row.mktLandInfoSID}}</td>
             <td>{{row.mktLandInfoCPS}}</td>
             <td>{{row.mktLandInfoSource}}</td>
             <td>{{row.mktLandInfoLandChannelName}}</td>
             <td>{{row.mktLandInfoLandChannel}}</td>
             <td>{{row.mktLandInfoCID}}</td>
             <td>{{row.mktLandInfoLandLink}}</td>
             <td>{{row.mktLandInfoState}}</td>
             <td>
                <#if er>
                 <a href="javascript:void(0)" ng-show="canModify('{{row.mktLandInfoStateId}}')" ng-click="modifyAd('{{row.mktLandInfoAid}}')" class="tb-edit">${lang.mktLandInfoEdit}</a>
                </#if>
                <#if er>
                 <a href="javascript:void(0)" ng-show="canPublish('{{row.mktLandInfoStateId}}','{{row.mktLandInfoCID}}')" ng-click="publishAd('{{row.mktLandInfoAid}}', '{{row.mktLandInfoCID}}')" class="tb-edit">${lang.mktLandInfoPublish}</a>
                 </#if>
                 <#if er>
                 <a href="javascript:void(0)" ng-show="canReset('{{row.mktLandInfoStateId}}')" ng-click="resetAd('{{row.mktLandInfoAid}}')" class="tb-reset">${lang.mktLandInfoReset}</a>
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
  
       
<div id="id_modify_mktLandInfo_dialog" style="display:none;font-size: 12px;">
  <form name="mktLandInfoModifyForm" class="css-form" novalidate autocomplete="off">
   <table border="0" class="css-table">



     <tr>
    <td>${lang.mktinfoName}:&nbsp;</td>
    <td>
     {{mktinfoName}}
    </td>
   </tr>
   
    <tr>
    <td>${lang.adInfoWebName}:&nbsp;</td>
    <td>
     {{adInfoWebName}}
    </td>
   </tr>
   
   
    <tr>
    <td>${lang.adInfoChannel}:&nbsp;</td>
    <td>
     {{adInfoChannel}}
    </td>
   </tr>
   
    <tr>
    <td>${lang.mktLandInfoAdPosition}:&nbsp;</td>
    <td>
     {{adInfoPosition}}
    </td>
   </tr>
   
    <tr>
    <td>${lang.adInfoPort}:&nbsp;</td>
    <td>
     {{adInfoPort}}
    </td>
   </tr>



   <tr>
    <td>${lang.mktLandInfoSID}:&nbsp;</td>
    <td>
     <input type="int" name="mktLandInfoSID" ng-model="mktLandInfoSID" ng-trim="true" ng-minlength="0" ng-pattern="/^\d{1,}$/" ng-maxlength="255" />
     <span class="error" ng-show="mktLandInfoModifyForm.mktLandInfoSID.$invalid">${lang.mktLandInfoSID_validate}</span><br>
    </td>
   </tr>

   <tr>
    <td>${lang.mktLandInfoCPSName}:&nbsp;</td>
    <td>
     <input type="text" name="mktLandInfoCPS" ng-model="mktLandInfoCPS" ng-trim="true" ng-minlength="0" ng-maxlength="50" />
     <span class="error" ng-show="mktLandInfoModifyForm.mktLandInfoCPS.$invalid">${lang.mktLandInfoCPS_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.mktLandInfoSource}:&nbsp;</td>
    <td>
     <input type="text" name="mktLandInfoSource" ng-model="mktLandInfoSource" ng-trim="true" ng-minlength="0" ng-maxlength="50" />
     <span class="error" ng-show="mktLandInfoModifyForm.mktLandInfoSource.$invalid">${lang.mktLandInfoSource_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.mktLandInfoLandChannelName}:&nbsp;</td>
    <td>
     <input type="text" name="mktLandInfoLandChannelName" ng-model="mktLandInfoLandChannelName" ng-trim="true" ng-minlength="0" ng-maxlength="50" />
     <span class="error" ng-show="mktLandInfoModifyForm.mktLandInfoLandChannelName.$invalid">${lang.mktLandInfoLandChannelName_validate}</span><br>
    </td>
   </tr>
 
   <tr>
    <td>${lang.mktLandInfoLandChannel}:&nbsp;</td>
    <td>
     <input type="text" name="mktLandInfoLandChannel" ng-model="mktLandInfoLandChannel" ng-trim="true" ng-minlength="0" ng-maxlength="50" />
     <span class="error" ng-show="mktLandInfoModifyForm.mktLandInfoLandChannel.$invalid">${lang.mktLandInfoLandChannel_validate}</span><br>
    </td>
   </tr>

   <tr>
    <td>${lang.mktLandInfoCID}:&nbsp;</td>
    <td>
     <input type="int" name="mktLandInfoCID" ng-model="mktLandInfoCID" ng-trim="true" ng-minlength="1" ng-pattern="/^\d{1,}$/" ng-maxlength="255" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="mktLandInfoModifyForm.mktLandInfoCID.$invalid">${lang.input_type_int_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.mktLandInfoLandUrl}:&nbsp;</td>
    <td>
     <input type="url" name="mktLandInfoLandLink" ng-model="mktLandInfoLandLink" ng-trim="true" ng-minlength="1" ng-maxlength="200" ng-pattern="/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/"  required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="mktLandInfoModifyForm.mktLandInfoLandLink.$invalid">${lang.input_type_url_validate}</span><br>
    </td>
   </tr>
  </table>
  </form>
  </div>    
  

  
 <div id="vmall_landInfo_batchLoad_dialog" style="display:none;">


    <form name="landInfoBatchLoadForm" method="post" enctype="multipart/form-data" action="${vars.webRoot}/api/adInfo/uploadFile" novalidate autocomplete="off">
       <table border="0">
       <tr>
            <td>
                <input type="file" name="uploadFile" id="uploadFile">   
             </td>
            <td>
            
             <input type="button" ng-click="landBatchTemplateDownLoad()"
            style="width: 140px; height: 30px;" class="btn-lightblue" value="${lang.landBatchTemplateDownLoad}">
            </td>
       </tr>
       
       <tr>
            <td colspan=2  style="height: 15px;">
                
            </td>
           </tr>
           
       <tr>
            <td>
            <label><input type="checkbox" ng-model="batchFlag" >${lang.landBatchLoadFlag}</label>
            </td>
            <td></td>
       </tr>
       <tr>
        <td colspan=2>
            <div id="res_div"></div>
        </td>
       </tr>
       </table>
     </form>
     
     <br>
     
     <input type="hidden" id="fileId">
     <input type="hidden" id="dealFlag">
     
     <input type="button" ng-click="landBatchUpLoad()"  
        style="width: 140px; height: 30px;" class="btn-lightblue" value="${lang.landBatchUpLoad}">
        
        
     <input type="button" ng-click="landBatchResDownLoad()" id="batchResDown" 
        style="width: 140px; height: 30px;" class="btn-lightblue" value="${lang.landBatchResDownLoad}">
    
     
</div>
  
<div id="vmall_land_batchPublish_dialog" style="display:none;">


     
     <div>
                                 确定要批量发布着陆链接吗？
     </div>
     
     <br>
     
     <input type="button" ng-click="batchPublishSure()"  
        style="width: 140px; height: 30px;" class="btn-lightblue" value="${lang.batchPublishLandInfo}">
        
     <input type="button" ng-click="batchPublishCancel()"
        style="width: 140px; height: 30px;" class="btn-lightblue" value="${lang.batchPublishCancel}">
     
     
</div>  


<div id="id_mktLandInfoEmail_dialog" style="display:none;font-size: 12px;">
  <form name="mktLandInfoEmailForm" class="css-form" novalidate autocomplete="off">
     <table class="listTab" style="width:600px;">
        <tr>
             <th width="20%"></th>
             <th width="20%">${lang.account}</th>
             <th width="30%">${lang.name}</th>
             <th width="30%">${lang.department}</th>
        </tr>
        <tr ng-repeat="row in mktLandInfoEmailUsers">
        
             <td> 
                <input type="checkbox"  id="emailLandInfoUsers" name="emailLandInfoUsers" value="{{row.account}}" />&nbsp;&nbsp;
             </td>
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
seajs.use(["modules/base/model", "modules/base/meta/mktLandInfo"], function(model, MetaMktLandInfo) {
    var meta =  MetaMktLandInfo.instance("${vars.webRoot}", 'mktLandInfo', ["mktinfoId","name"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        from:0,
        perPage:15
    });
});
</script>