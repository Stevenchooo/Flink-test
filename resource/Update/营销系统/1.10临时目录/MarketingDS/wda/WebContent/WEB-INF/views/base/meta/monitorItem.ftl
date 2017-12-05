<div style="width:96%;" ng-app="ModelList">
  <div style="width:100%;" ng-controller="ListCtrl" id="data_grid" ng-cloak>
    <table class="listTab">
    
    <tr>
     <th>${lang.resourceId}</th>
     <th>${lang.monitorType}</th>
     <th>${lang.topLimit}</th>
     <th>${lang.reportPeriod}</th>
     <th>${lang.createTime}</th>
     <th>${lang.creator}</th>
<#assign cr=(userRight?index_of("|c|") >= 0) />    
<#assign dr=(userRight?index_of("|d|") >= 0) />    
<#assign er=(userRight?index_of("|u|") >= 0) />    
<#if cr || dr || er >
     <th>${lang.modelOperation}
    <#if cr>
        <span class="button add" ng-click="modelCreate()" title="${lang.modelCreate}" />
    </#if>
     </th>
</#if>
    </tr>
    <tr ng-repeat="row in resp.models">
     <th>{{row.name}}</th>
     <td>{{row.meta}}</td>
     <td>{{split(row.val, ':', 0)}}</td>
     <td>{{split(row.val, ':', 1)}}</td>
     <td>{{row.createTime}}</td>
     <td>{{row.creator}}</td>
<#if er || dr>
     <th>
        <#if er>
          <span ng-click="modelModify({{row.id}})" class="button edit" title="${lang.modelModify}"></span>
        </#if>
        <#if dr>
          &nbsp;<span ng-click="modelDel({{row.id}})" class="button delete" title="${lang.modelDel}"></span>
        </#if>
     </th>
</#if>
    </tr>
    </table>

    <table border="0" width="100%" id="id_page_table"><tr>
     <td>
      ${lang.totalNum}:&nbsp;{{resp.total}}&nbsp;&nbsp;
      ${lang.totalPageNum}:&nbsp;{{pageNum}}&nbsp;&nbsp;
      ${lang.curPageNo}:&nbsp;{{curPage}}
     </td>
     <td align="right">
      <span ng-repeat="pg in pages" class="{{pg.cls}}" ng-click="pageClick({{pg.from}})">{{pg.str}}</span>
     </td>
    </tr>
    </table>
    <div id="goto_page_window" style="width:${lang.pageWindowWidth}px;"><table border="0" style="margin:1px;"><tr>
      <td><input type="text" ng-model="gotoPageNo" class="page_input"></td>
      <td ng-click="pageClick(-2)" style="cursor:pointer;color:blue;">${lang.goToPage}</td></tr>
    </table></div>

<#if cr>
  <div id="id_create_dialog" style="display:none;">
  <form name="createForm" class="css-form" novalidate>
   <input type="hidden" id="id_crt_meta" value="${meta}"/>
   <table border="0">
   <tr>
    <td>${lang.resourceId}:&nbsp;</td>
    <td>
     <input type="text" name="resourceId" id="id_crt_resourceId" ng-model="crt_resourceId" ng-trim="true" ng-maxlength="255" required/>
     <span class="error" ng-show="createForm.resourceId.$dirty && createForm.resourceId.$invalid">${lang.resourceId_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.topLimit}:&nbsp;</td>
    <td>
     <input type="number" name="topLimit" id="id_crt_topLimit" ng-model="crt_topLimit" ng-trim="true" min="0" ng-maxlength="40" required/>
     <span class="error" ng-show="createForm.topLimit.$dirty && createForm.topLimit.$invalid">${lang.topLimit_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.reportPeriod}:&nbsp;</td>
    <td>
     <input type="number" name="reportPeriod" id="id_crt_reportPeriod" ng-model="crt_reportPeriod" ng-trim="true" min="30" ng-maxlength="40" required/>${lang.second}
     <span class="error" ng-show="createForm.reportPeriod.$dirty && createForm.reportPeriod.$invalid">${lang.topLimit_validate}</span><br>
    </td>
   </tr>
   
  </table>
  </form>
  </div>
</#if>
<#if er>
  <div id="id_modify_dialog" style="display:none;">
  <form name="modifyForm" class="css-form" novalidate>
   <table border="0">
   <tr>
    <td>${lang.resourceId}:&nbsp;</td>
    <td>
     <input type="text" name="resourceId" id="id_mod_resourceId" ng-model="mod_resourceId" ng-trim="true" ng-maxlength="255" required/>
     <span class="error" ng-show="modifyForm.resourceId.$dirty && modifyForm.resourceId.$invalid">${lang.resourceId_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.topLimit}:&nbsp;</td>
    <td>
     <input type="number" name="topLimit" id="id_mod_topLimit" ng-model="mod_topLimit" ng-trim="true" min="0" ng-maxlength="40" required/>
     <span class="error" ng-show="modifyForm.topLimit.$dirty && modifyForm.topLimit.$invalid">${lang.topLimit_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.reportPeriod}:&nbsp;</td>
    <td>
     <input type="number" name="reportPeriod" id="id_mod_reportPeriod" ng-model="mod_reportPeriod" ng-trim="true" min="30" ng-maxlength="40" required/>${lang.second}
     <span class="error" ng-show="modifyForm.reportPeriod.$dirty && modifyForm.reportPeriod.$invalid">${lang.topLimit_validate}</span><br>
    </td>
   </tr>
   
  </table>
  </form>
  </div>
</#if>
</div> <!-- end of ListCtrl -->
</div> <!-- end of ModelList -->


<script type="text/javascript">
seajs.use(["modules/base/model", "modules/base/meta/monitor"], function(model, MetaMonitor) {
    var meta = new MetaMonitor("${vars.webRoot}", '${meta}', ["id","name","createTime","creator","val", "meta"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        from:0,
        perPage:20
    });
});
</script>