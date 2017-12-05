<div class="upmc-wp" ng-app="ModelList">
  <div style="width:100%;" ng-controller="ListCtrl" id="data_grid" ng-cloak>
    <#assign cr=(userRight?index_of("|c|") >= 0) />    
    <#assign dr=(userRight?index_of("|d|") >= 0) />    
    <#assign er=(userRight?index_of("|u|") >= 0) /> 
    <#assign xr=(userRight?index_of("|x|") >= 0) />  
   
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
        
        <td style="LINE-HEIGHT: 24px;">${lang.adInfoDeliveryPeriod}
         <input style="width:160px;height:24px;" type="text"  id="queryAdInfoDeliveryTimes" name="queryAdInfoDeliveryTimes" ng-model="queryAdInfoDeliveryTimes" />
        </td>
        
        <td style="LINE-HEIGHT: 24px;">${lang.updateDate}
            <input style="width:160px;height:24px;" type="text"  id="queryDate" name="queryDate" ng-model="queryDate" />
        </td>
        
        <td align="left">
            <input type="button" ng-click="adExcelExport()" style="width: 80px;" class="btn-lightblue" value="导出">&nbsp;
            <#if xr>
            <input type="button" ng-click="materialBatchDownload()" style="width: 125px;" class="btn-lightblue" value="素材批量下载">&nbsp;
            </#if>
            <input type="button" id="queryButton" ng-click="pageClick(0)" style="width: 80px;"  class="btn-lightblue" value="${lang.query}">
        </td>
        </tr>
      </table>

    </div>
    

    <div class="upmc-controls upmcc-multi">

     <#if cr>
        <a href="javascript:void(0)" ng-click="modelCreate()" ><b class="link-icon li-create"></b> <span class="vm">创建广告位</span></a>
        <a href="javascript:void(0)" ng-click="batchLoad()"><b class="link-icon li-batinput"></b> <span class="vm">批量录入广告位</span></a>
        <a href="javascript:void(0)" ng-click="batchPublish()"><b class="link-icon li-batad"></b> <span class="vm">批量发布广告位</span></a>
        <a href="javascript:void(0)" ng-click="adInfoEmail()"><b class="link-icon li-email"></b> <span class="vm">邮件通知</span></a>
     </#if>
        
    </div> 

     <div class="up-detable">
          <table>
            <tr>
                 <th width="5%">${lang.adInfoId}</th>
                 <th width="10%">${lang.mktinfoName}</th>
                 <th width="8%">${lang.adInfoWebName}</th>
                 <th width="7%">${lang.adInfoChannel}</th>
                 <th width="5%">${lang.adInfoAdPosition}</th>
                 <th width="5%" ng-show="${vars.materialEnable}">${lang.materialState}</th>
                 <th width="5%">${lang.adInfoPort}</th>
                 <th width="5%">${lang.adInfoPlatform}</th>
                 <th width="5%">${lang.operator}</th>
                 <th width="5%">${lang.updateTime}</th>
                 <th width="5%">${lang.adInfoResource}</th>
                 <th width="10%">${lang.adInfoState}</th>
                 <th width="10%">${lang.adInfoOper}<br>
                   
                 </th>
            </tr>
            <tr ng-repeat="row in resp.result">
                 <td>{{row.adInfoId}}</td>
                 <td>{{row.mktinfoName}}</td>
                 <td>{{row.adInfoWebName}}</td>
                 <td>{{row.adInfoChannel}}</td>
                 <td><a ng-click="adInfoDetail('{{row.adInfoId}}')">{{row.adInfoPosition}}</a></td>
                 <td ng-show="${vars.materialEnable}" >
                   <#if xr>
                     <a href="javascript:void(0)" ng-click="downLoadMaterial('{{row.adInfoId}}','{{row.materialStateId}}','{{row.materialState}}','{{row.materialType}}');">
                     
                     {{row.materialState}}
                     
                     <span  ng-show="isMaterialModify('{{row.materialStateId}}')" >  ({{row.materialOperator}})</span>
                     
                     </a>
                    
                        <span ng-show="isMaterialModify('{{row.materialStateId}}')" ng-click="delMaterial('{{row.adInfoId}}')" class="button delete" title="${lang.materialDelete}"></span>
                     <#else>
                        {{row.materialState}}
                         <span  ng-show="isMaterialModify('{{row.materialStateId}}')" >  ({{row.materialOperator}})</span>
                     </#if>
                 </td>
                 <td>{{row.adInfoPort}}</td>
                 <td>{{row.adInfoPlatform}}</td>
                 <td>{{row.operator}}</td>
                 <td>{{row.updateTime}}</td>
                 <td>{{row.adInfoResource}}</td>
                 <td>{{row.adInfoState}}</td>
                 <td>
                      <#if er>
                      <a href="javascript:void(0)" ng-show="isModify('{{row.adInfoStateId}}')" ng-click="modelModify('{{row.adInfoId}}')" class="tb-edit">${lang.mktInfoModify}</a>
                      </#if>
                      
                      <#if er>
                        <a href="javascript:void(0)" ng-show="isPublish('{{row.adInfoStateId}}' + '|' + '{{row.adInfoPlatform}}' + '|' + '{{row.adInfoDeliveryTimes}}' )" ng-click="adInfoPublish('{{row.adInfoId}}')" class="tb-edit">${lang.adInfoPublish}</a>
                      </#if>
                      
                      <#if er>
                        <a href="javascript:void(0)" class="tb-reset" ng-show="isReset('{{row.adInfoStateId}}')" ng-click="adInfoReset('{{row.adInfoId}}')">${lang.adInfoReset}</a>
                      </#if>
                      
                      <#if dr>
                        <a href="javascript:void(0)" class="tb-reset" ng-show="isDel('{{row.adInfoStateId}}')" ng-click="modelDel('{{row.adInfoId}}')" >${lang.mktInfoDel}</a>
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
     
     
     
     <div id="adInfo_batchLoad_res_dialog" style="display:none;">

  
     </div>
     
     
     <div id="adInfo_batchLoad_dialog" style="display:none;">

        <form name="adInfoBatchLoadForm">
           <table border="0">
           <tr>
                <td>
                    <input type="file" name="uploadFile" id="uploadFile">   
                 </td>
                <td>
                
                 <input type="button" ng-click="adBatchTemplateDownLoad()"  style="width: 140px; height: 30px;" class="btn-lightblue" value="${lang.adBatchTemplateDownLoad}">
                </td>
           </tr>
           <tr>
                <td>
                <label><input type="checkbox" ng-model="batchFlag" >${lang.adBatchLoadFlag}</label>
                </td>
                <td></td>
           </tr>
            <tr>
            <td colspan=2  style="height: 15px;">
                
            </td>
           </tr>
           
           <tr>
            <td colspan=2>
                <div id="res_div"></div>
            </td>
           </tr>
           </table>
         </form>
         
         
         
         <input type="hidden" id="fileId">
         <input type="hidden" id="dealFlag">
         
         <br>
         <input type="button" ng-click="batchFileUpLoad()"  style="width: 140px; height: 30px;" class="btn-lightblue"
             value="${lang.batchFileUpLoad}">
            
         <input type="button" ng-click="adBatchResDownLoad()" id="batchResDown" style="width: 140px; height: 30px;" class="btn-lightblue"
             value="${lang.adBatchResDownLoad}">
         
         
    </div>


     <div id="id_create_adInfo_dialog" style="display:none;font-size: 12px;">
          <form name="adInfoCreateForm" class="css-form" novalidate autocomplete="off">
           <table border="0" class="css-table">
           <tr>
            <td>
            <table border="0" class="css-table">
               <tr>
                <td style="LINE-HEIGHT: 30px;">${lang.mktinfoName}:&nbsp;</td>
                <td style="LINE-HEIGHT: 30px;">
                 <select ng-model="mktinfoId" ng-change="mktinfoIdChange()"
                            ng-trim="true" ng-options="p.mktinfoId as p.mktinfoName for p in mktInfos1" style="height:24px;"  ng-trim="true"    required>
                 </select>
                 <span class="error">*&nbsp;</span>
                 <span class="error" ng-show="isMktinfoIdSelect()">${lang.mktinfoId_validate}</span><br>
                </td>
               </tr>
               
               <tr>
                <td>${lang.adInfoWebName}:&nbsp;</td>
                <td>
                 <select ng-model="adInfoWebName" ng-options="p.id as p.name for p in names" style="height:24px;" ng-trim="true"  required>
                 </select>
                 <span class="error">*&nbsp;</span>
                 <span class="error" ng-show="isAdInfoWebNameSelect()">${lang.adInfoWebName_validate}</span><br>
                </td>
               </tr>
               
               
               <tr>
                <td>${lang.adInfoChannel}:&nbsp;</td>
                <td>
                 <input type="text" name="adInfoChannel" ng-model="adInfoChannel" ng-trim="true"   ng-maxlength="100" required/>
                 <span class="error">*&nbsp;</span>
                 <span class="error" ng-show="adInfoCreateForm.adInfoChannel.$invalid">${lang.adInfoChannel_validate}</span><br>
                </td>
               </tr>
               
               
               <tr>
                <td>${lang.adInfoAdPosition}:&nbsp;</td>
                <td>
                 <input type="text" name="adInfoAdPosition" ng-model="adInfoAdPosition" ng-trim="true"   ng-maxlength="100" required/>
                 <span class="error">*&nbsp;</span>
                 <span class="error" ng-show="adInfoCreateForm.adInfoAdPosition.$invalid">${lang.adInfoAdPosition_validate}</span><br>
                </td>
               </tr>
               
               <tr>
                <td>${lang.adInfoAdMaterialType}:&nbsp;</td>
                <td>
                 <select ng-model="adInfoAdMaterialType" ng-options="p.id as p.name for p in materials" style="height:24px;">
                 </select>
                 <span class="error" ng-show="isNecessary('{{state}}')">*&nbsp;</span>
                </td>
               </tr>
               
               <tr>
                <td>${lang.adInfoAdMaterialDesc}:&nbsp;</td>
                <td>
                    <textarea  ng-model="adInfoAdMaterialDesc" cols=40 rows=4> 
                    </textarea>
                    <span class="error" ng-show="isTrueAdInfoAdMaterialDesc()">${lang.adInfoAdMaterialDescModify_validate}</span><br>
                </td>
               </tr>
               
               
               <tr>
                <td>${lang.adInfoPort}:&nbsp;</td>
                <td>
                 <select ng-model="adInfoPort" ng-options="p.id as p.name for p in ports" style="height:24px;" ng-trim="true"  required>
                 </select>
                 <span class="error">*&nbsp;</span>
                 <span class="error" ng-show="isAdInfoPortSelect()">${lang.adInfoPort_validate}</span><br>
                </td>
               </tr>
            
               <tr>
                <td>${lang.adInfoPlatform}:&nbsp;</td>
                <td>
                  <select ng-model="adInfoPlatform" ng-options="p.id as p.name for p in platforms" style="height:24px;"  ng-trim="true"  required>
                 </select>
                 <span class="error">*&nbsp;</span>
                 <span class="error" ng-show="isAdInfoPlatformSelect()">${lang.adInfoPlatform_validate}</span><br>
                </td>
               </tr>
              
                <tr>
                <td>${lang.adInfoPlatformDesc}:&nbsp;</td>
                <td>
                 <input type="text" name="adInfoPlatformDesc" ng-model="adInfoPlatformDesc" ng-trim="true"  ng-maxlength="255"/>
                 <span class="error" ng-show="isNecessary('{{state}}')">*&nbsp;</span>
                 <span class="error" ng-show="adInfoCreateForm.adInfoPlatformDesc.$invalid">${lang.adInfoPlatformDesc_validate}</span><br>
                </td>
               </tr>
            </table>
            </td>
            <td>
            <table border="0" class="css-table">
               <tr>
                <td>${lang.adInfoDeliveryDays}:&nbsp;</td>
                <td>
                 <input type="number" name="adInfoDeliveryDays" ng-model="adInfoDeliveryDays"  ng-trim="true" min="1" max="2147483647" required/>
                 <span class="error">*&nbsp;</span>
                 <span class="error" ng-show="adInfoCreateForm.adInfoDeliveryDays.$invalid">${lang.adInfoDeliveryDays_validate}</span><br>
                </td>
               </tr>
            
            
              <tr>
                <td>${lang.adInfoDeliveryTimes}:&nbsp;</td>
                <td>
                 <input type="text"  readonly="true" id="adInfoDeliveryTimes" name="adInfoDeliveryTimes" ng-model="adInfoDeliveryTimes" ng-trim="true" ng-trim="true"  required/>
                 <span class="error">*&nbsp;</span>
                 <span class="error" ng-show="adInfoCreateForm.adInfoDeliveryTimes.$invalid">${lang.adInfoDeliveryTimes_validate}</span><br>
                </td>
               </tr>
            
               <tr>
                <td>${lang.adInfoFlowType}:&nbsp;</td>
                <td>
                 <select ng-model="adInfoFlowType" ng-options="p.id as p.name for p in flows" style="height:24px;" ng-trim="true">
                 </select>
                </td>
               </tr>
               
               
               
               <tr>
                <td>${lang.adInfoExpAmount}:&nbsp;</td>
                <td>
                 <input type="number" id="adInfoExpAmount" name="adInfoExpAmount" ng-model="adInfoExpAmount" ng-trim="true" min="0" max="2147483647" />
                 <span class="error" ng-show="adInfoCreateForm.adInfoExpAmount.$invalid">${lang.adInfoExpAmount_validate}</span><br>
                </td>
               </tr>
               
               
               
               <tr>
                <td>${lang.adInfoClickAmount}:&nbsp;</td>
                <td>
                 <input type="number" id="adInfoClickAmount" name="adInfoClickAmount" ng-model="adInfoClickAmount" ng-trim="true"  min="0" max="2147483647" />
                 <span class="error" ng-show="adInfoCreateForm.adInfoClickAmount.$invalid">${lang.adInfoClickAmount_validate}</span><br>
                </td>
               </tr>
               
               
               <tr>
                <td>${lang.adInfoPublishPrice}:&nbsp;</td>
                <td>
                 <input type="number" id="adInfoPublishPrice" name="adInfoPublishPrice" ng-model="adInfoPublishPrice" ng-trim="true" min="0" max="2147483647"  />
                 <span class="error" ng-show="adInfoCreateForm.adInfoPublishPrice.$invalid">${lang.adInfoPublishPrice_validate}</span><br>
                </td>
               </tr>
               
               <tr>
                <td>${lang.adInfoNetPrice}:&nbsp;</td>
                <td>
                 <input type="number" id="adInfoNetPrice" name="adInfoNetPrice" ng-model="adInfoNetPrice" ng-trim="true"  min="0" max="2147483647" />
                 <span class="error" ng-show="adInfoCreateForm.adInfoNetPrice.$invalid">${lang.adInfoNetPrice_validate}</span><br>
                </td>
               </tr>   
               
               
               <tr>
                <td>${lang.adInfoResource}:&nbsp;</td>
                <td>
                 <select ng-model="adInfoResource" ng-options="p.id as p.name for p in resources" style="height:24px;" ng-trim="true"  >
                 </select>
                </td>
               </tr>   
               
               <tr>
                <td>${lang.adInfoIsExposure}:&nbsp;</td>
                <td>
                 <select ng-model="adInfoIsExposure" ng-options="p.id as p.name for p in exposures" style="height:24px;">
                 </select>
                </td>
               </tr>     
               
               
               <tr>
                <td>${lang.adInfoIsClick}:&nbsp;</td>
                <td>
                 <select ng-model="adInfoIsClick" ng-options="p.id as p.name for p in clicks" style="height:24px;">
                 </select>
                </td>
               </tr> 
                    
               <tr>
                <td>${lang.adInfoMonitorPlatform}:&nbsp;</td>
                <td>
                 <select ng-model="adInfoMonitorPlatform" ng-options="p.id as p.name for p in monitors" style="height:24px;"  ng-trim="true"  required>
                 </select>
                 <span class="error">*&nbsp;</span>
                 <span class="error" ng-show="isAdInfoMonitorPlatformSelect()">${lang.adInfoMonitorPlatform_validate}</span><br>
                </td>
               </tr>      
             </table> 
             </td>
            </tr> 
          </table>
       </form>
    </div>    
   
   <div id="id_modify_adInfo_dialog" style="display:none;font-size: 12px;">
  <form name="adInfoModifyForm" class="css-form" novalidate autocomplete="off">
   <table border="0" class="css-table">
   <tr ng-show="showFlagModify">
    <td style="LINE-HEIGHT: 30px;">${lang.mktinfoName}:&nbsp;</td>
    <td style="LINE-HEIGHT: 30px;">
     <select ng-model="mktinfoIdModify"
                ng-trim="true" ng-options="p.mktinfoId as p.mktinfoName for p in mktInfos1" style="height:24px;"  ng-trim="true"    required>
     </select>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="isMktinfoIdSelectModify()">${lang.mktinfoId_validate}</span><br>
    </td>
   </tr>
   
   <tr ng-show="showFlagModify">
    <td>${lang.adInfoWebName}:&nbsp;</td>
    <td>
     <select ng-model="adInfoWebNameModify" ng-options="p.id as p.name for p in names" style="height:24px;" ng-trim="true"  required>
     </select>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="isAdInfoWebNameSelectModify()">${lang.adInfoWebName_validate}</span><br>
    </td>
   </tr>
   
   
   <tr ng-show="showFlagModify">
    <td>${lang.adInfoChannel}:&nbsp;</td>
    <td>
     <input type="text" name="adInfoChannelModify" ng-model="adInfoChannelModify" ng-trim="true"   ng-maxlength="100" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="adInfoCreateForm.adInfoChannelModify.$invalid">${lang.adInfoChannel_validate}</span><br>
    </td>
   </tr>
   
   
   <tr ng-show="showFlagModify">
    <td>${lang.adInfoAdPosition}:&nbsp;</td>
    <td>
     <input type="text" name="adInfoAdPositionModify" ng-model="adInfoAdPositionModify" ng-trim="true"   ng-maxlength="100" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="adInfoCreateForm.adInfoAdPositionModify.$invalid">${lang.adInfoAdPosition_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.adInfoAdMaterialType}:&nbsp;</td>
    <td>
     <select ng-model="adInfoAdMaterialTypeModify" ng-options="p.id as p.name for p in materials" style="height:24px;">
     </select>
     <span class="error" ng-show="isNecessaryModify('{{state}}')">*&nbsp;</span>
    </td>
   </tr>
   
   <tr>
    <td>${lang.adInfoAdMaterialDesc}:&nbsp;</td>
    <td>
        <textarea  ng-model="adInfoAdMaterialDescModify" cols=40 rows=4> 
        </textarea>
         <span class="error" ng-show="isTrueAdInfoAdMaterialDescModify()">${lang.adInfoAdMaterialDescModify_validate}</span><br>
    </td>
   </tr>
   
   
   <tr ng-show="showFlagModify">
    <td>${lang.adInfoPort}:&nbsp;</td>
    <td>
     <select ng-model="adInfoPortModify" ng-options="p.id as p.name for p in ports" style="height:24px;" ng-trim="true"  required>
     </select>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="isAdInfoPortSelectModify()">${lang.adInfoPort_validate}</span><br>
    </td>
   </tr>

   <tr ng-show="showFlagModify">
    <td>${lang.adInfoPlatform}:&nbsp;</td>
    <td>
      <select ng-model="adInfoPlatformModify" ng-options="p.id as p.name for p in platforms" style="height:24px;"  ng-trim="true"  required>
     </select>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="isAdInfoPlatformSelectModify()">${lang.adInfoPlatform_validate}</span><br>
    </td>
   </tr>
  
    <tr ng-show="showFlagModify">
    <td>${lang.adInfoPlatformDesc}:&nbsp;</td>
    <td>
     <input type="text" name="adInfoPlatformDescModify" ng-model="adInfoPlatformDescModify" ng-trim="true"  ng-maxlength="255"/>
     <span class="error" ng-show="isNecessaryModify('{{state}}')">*&nbsp;</span>
     <span class="error" ng-show="adInfoCreateForm.adInfoPlatformDescModify.$invalid">${lang.adInfoPlatformDesc_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.adInfoDeliveryDays}:&nbsp;</td>
    <td>
     <input type="number" name="adInfoDeliveryDaysModify" ng-model="adInfoDeliveryDaysModify" ng-trim="true" min="1" max="2147483647" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="adInfoCreateForm.adInfoDeliveryDaysModify.$invalid">${lang.adInfoDeliveryDays_validate}</span><br>
    </td>
   </tr>


  <tr>
    <td>${lang.adInfoDeliveryTimes}:&nbsp;</td>
    <td>
     <input type="text"  readonly="true" id="adInfoDeliveryTimesModify" name="adInfoDeliveryTimesModify" ng-model="adInfoDeliveryTimesModify" ng-trim="true" ng-trim="true"  required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="adInfoCreateForm.adInfoDeliveryTimesModify.$invalid">${lang.adInfoDeliveryTimes_validate}</span><br>
    </td>
   </tr>

   <tr ng-show="showFlagModify">
    <td>${lang.adInfoFlowType}:&nbsp;</td>
    <td>
     <select ng-model="adInfoFlowTypeModify" ng-options="p.id as p.name for p in flows" style="height:24px;" ng-trim="true">
     </select>
    </td>
   </tr>
   
   
   
   <tr>
    <td>${lang.adInfoExpAmount}:&nbsp;</td>
    <td>
     <input type="number" id="adInfoExpAmountModify" name="adInfoExpAmountModify" ng-model="adInfoExpAmountModify" ng-trim="true" min="0" max="2147483647" />
     <span class="error" ng-show="adInfoCreateForm.adInfoExpAmountModify.$invalid">${lang.adInfoExpAmount_validate}</span><br>
    </td>
   </tr>
   
   
   
   <tr>
    <td>${lang.adInfoClickAmount}:&nbsp;</td>
    <td>
     <input type="number" id="adInfoClickAmountModify" name="adInfoClickAmountModify" ng-model="adInfoClickAmountModify" ng-trim="true"  min="0" max="2147483647" />
     <span class="error" ng-show="adInfoCreateForm.adInfoClickAmountModify.$invalid">${lang.adInfoClickAmount_validate}</span><br>
    </td>
   </tr>
   
   
   <tr>
    <td>${lang.adInfoPublishPrice}:&nbsp;</td>
    <td>
     <input type="number" id="adInfoPublishPriceModify" name="adInfoPublishPriceModify" ng-model="adInfoPublishPriceModify" ng-trim="true" min="0" max="2147483647"  />
     <span class="error" ng-show="adInfoCreateForm.adInfoPublishPriceModify.$invalid">${lang.adInfoPublishPrice_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.adInfoNetPrice}:&nbsp;</td>
    <td>
     <input type="number" id="adInfoNetPriceModify" name="adInfoNetPriceModify" ng-model="adInfoNetPriceModify" ng-trim="true"  min="0" max="2147483647" />
     <span class="error" ng-show="adInfoCreateForm.adInfoNetPriceModify.$invalid">${lang.adInfoNetPrice_validate}</span><br>
    </td>
   </tr>   
   
   
   <tr>
    <td>${lang.adInfoResource}:&nbsp;</td>
    <td>
     <select ng-model="adInfoResourceModify" ng-options="p.id as p.name for p in resources" style="height:24px;" ng-trim="true"  >
     </select>
    </td>
   </tr>   
   
   <tr>
    <td>${lang.adInfoIsExposure}:&nbsp;</td>
    <td>
     <select ng-model="adInfoIsExposureModify" ng-options="p.id as p.name for p in exposures" style="height:24px;">
     </select>
    </td>
   </tr>     
   
   
   <tr>
    <td>${lang.adInfoIsClick}:&nbsp;</td>
    <td>
     <select ng-model="adInfoIsClickModify" ng-options="p.id as p.name for p in clicks" style="height:24px;">
     </select>
    </td>
   </tr> 
        
   <tr ng-show="showFlagModify">
    <td>${lang.adInfoMonitorPlatform}:&nbsp;</td>
    <td>
     <select ng-model="adInfoMonitorPlatformModify" ng-options="p.id as p.name for p in monitors" style="height:24px;"  ng-trim="true"  required>
     </select>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="isAdInfoMonitorPlatformSelectModify()">${lang.adInfoMonitorPlatform_validate}</span><br>
    </td>
   </tr>      
        

  </table>
  </form>
  </div>   
   
   

<div id="ad_material_add_dialog" style="display:none;">


    <form name="adInfoMaterialLoadForm" method="post" enctype="multipart/form-data" action="${vars.webRoot}/api/adInfo/uploadMaterial" novalidate autocomplete="off">
       <table border="0" >
       <tr>
            <td>
                <input  type="radio" name="materialType" ng-model="materialType" value="0" style="width:20px;" required/>${lang.materialFile}:&nbsp;
            </td>
            <td>
                <input type="file" ng-disabled="isUploadMaterialDisable()" name="uploadMaterial" id="uploadMaterial" ng-model="uploadMaterial">   
             </td>
             
             <td>
                 <span class="processLoading"  id="processLoading" ><span style="vertical-align: middle;">${lang.fileLoadProcess}</span></span>
             </td>
       </tr>
       <tr>
            <td colspan=2  style="height: 15px;">
                
            </td>
           </tr>
       <tr>
             <td>
                <input  type="radio" name="materialType" ng-model="materialType" value="1" style="width:20px;" required/>${lang.materialUrl}:&nbsp;
            </td>
            <td>
                <input type="text" ng-disabled="isMaterialUrlDisable()" name="materialUrl" id="materialUrl" ng-model="materialUrl"  ng-trim="true" ng-minlength="1" ng-maxlength="200" ng-pattern="/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/"  required/>
                 <span ng-show="1 == materialType">
                     <span class="error">*&nbsp;</span>
                     <span class="error" ng-show="adInfoMaterialLoadForm.materialUrl.$invalid">${lang.input_type_url_validate}</span><br>
                 </span>
             </td>
             <td></td>
       </tr>
       
       <tr>
            <td colspan=2  style="height: 15px;">
                
            </td>
           </tr>
           
       
       <tr>
             <td>
                 <input type="button" ng-click="materialSave()"
            style="width: 80px; height: 30px;" class="btn-lightblue" value="${lang.materialSave}">
             </td>
              <td>
            
             <input type="button" ng-click="materialCancel()"
           style="width: 80px; height: 30px;" class="btn-lightblue" value="${lang.materialCancel}">
            </td>
            <td></td>
       </tr>  
           
       </table>
       
        <input type="hidden" id="hiddenAid"  ng-model="hiddenAid">
     </form>
        
</div>


    
    

    
   
    

    

    

 










<div id="adInfo_batchPublish_dialog" style="display:none;">


     
     <div>
                                 确定要批量发布广告位吗？
     </div>
     
     <br>
     
     <input type="button" ng-click="batchPublishSure()"  
        style="width: 140px; height: 30px;" class="btn-lightblue" value="${lang.batchPublishInfo}">
        
     <input type="button" ng-click="batchPublishCancel()"
        style="width: 140px; height: 30px;" class="btn-lightblue" value="${lang.batchPublishCancel}">
     
     
</div>


<div id="id_adInfo_details_dialog" style="display:none;font-size: 12px;">
    <form name="adInfoDetailsForm" class="css-form" novalidate autocomplete="off">
        <table border="0" class="listTabDetial" style="width:600px;font-size: 12px;">
            <tr>
                <td style="background: #e8e8e8;width:150px">${lang.detailsAdInfoId}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsAdInfoId}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsAdInfoPlatformDesc}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsAdInfoPlatformDesc}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsIsClick}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsIsClickName}}</td>
            </tr>
            <tr>
                <td style="background: #e8e8e8;width:150px">${lang.detailsMktinfoName}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsMktinfoName}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsAdInfoDeliveryDays}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsAdInfoDeliveryDays}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsMonitorPlatform}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsMonitorPlatform}}</td>
            </tr>
            <tr>
                <td style="background: #e8e8e8;width:150px">${lang.detailsAdInfoWebName}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsAdInfoWebName}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsAdInfoDeliveryTimes}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsAdInfoDeliveryTimes}}</td> 
                <td style="background: #e8e8e8;width:150px">${lang.detailsSid}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsSid}}</td>            
            </tr>
            <tr>
                <td style="background: #e8e8e8;width:150px">${lang.detailsAdInfoChannel}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsAdInfoChannel}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsOperator}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsOperator}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsCpsName}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsCpsName}}</td>
            </tr>
            <tr>
                <td style="background: #e8e8e8;width:150px">${lang.detailsAdInfoPosition}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsAdInfoPosition}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsUpdateTime}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsUpdateTime}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsSource}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsSource}}</td>
            </tr>
            <tr>
                <td style="background: #e8e8e8;width:150px">${lang.detailsMaterialType}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsMaterialType}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsAdInfoFlowType}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsAdInfoFlowType}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsChannelName}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsChannelName}}</td>
            </tr>
            <tr>
                <td style="background: #e8e8e8;width:150px" rowspan=3>${lang.detailsMaterialDesc}:&nbsp;</td>
                <td style="width:250px" rowspan=3>{{rowDetails.detailsMaterialDesc}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsExpAmount}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsExpAmount}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsChannel}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsChannel}}</td>
            </tr>
            <tr>
                <td style="background: #e8e8e8;width:150px">${lang.detailsClickAmount}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsClickAmount}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsCid}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsCid}}</td>
            </tr>
            <tr>
                <td style="background: #e8e8e8;width:150px">${lang.detailsPublishPrice}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsPublishPrice}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsLandUrl}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsLandUrl}}</td>
            </tr>
            <tr>
                <td style="background: #e8e8e8;width:150px">${lang.detailsMaterialState}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsMaterialState}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsNetPrice}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsNetPrice}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsMonitorExposureUrl}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsMonitorExposureUrl}}</td>
            </tr>
            <tr>
                <td style="background: #e8e8e8;width:150px">${lang.detailsAdInfoPort}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsAdInfoPort}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsAdInfoResource}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsAdInfoResource}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsMonitorClickUrl}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsMonitorClickUrl}}</td>
            </tr>
            <tr>
                <td style="background: #e8e8e8;width:150px">${lang.detailsAdInfoPlatform}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsAdInfoPlatform}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsIsExposure}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsIsExposureName}}</td>
                <td style="background: #e8e8e8;width:150px">${lang.detailsAdInfoState}:&nbsp;</td>
                <td style="width:250px">{{rowDetails.detailsAdInfoState}}</td>
            </tr>
        </table>
    </form>
</div>    



<div id="id_adEmail_dialog" style="display:none;font-size: 12px;">
  <form name="adInfoEmailForm" class="css-form" novalidate autocomplete="off">
     <table style="width:600px;" class="listTab" >
        
       <tbody id="ad_tbody">
        <tr>
             <td width="20%"></td>
             <td width="20%">${lang.account}</td>
             <td width="30%">${lang.name}</td>
             <td width="30%">${lang.department}</td>
        </tr>
        </tbody>
        
    </table>
  </form>
</div>

</div> <!-- end of ListCtrl -->
</div> <!-- end of ModelList -->
   
<script type="text/javascript">

seajs.use(["modules/base/model", "modules/base/meta/adInfo"], function(model, MetaAdInfo) {
    var meta =  MetaAdInfo.instance("${vars.webRoot}", 'adInfo', ["mktinfoId","name"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        from:0,
        perPage:15
    });
});
</script>