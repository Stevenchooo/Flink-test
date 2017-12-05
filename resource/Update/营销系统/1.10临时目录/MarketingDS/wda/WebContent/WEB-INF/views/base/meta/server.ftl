<div style="width:96%;" ng-app="ModelList">
  <div style="width:100%;" ng-controller="ListCtrl" id="data_grid" ng-cloak>
    <table class="listTab">
    <tr>
     <th>${lang.modelName}</th>
     <th>${lang.description}</th>
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
     <td>{{row.val}}</td>
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
        &nbsp;<span ng-click="serverReport({{row.id}})" class="button report" title="${lang.report}"></span>
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

<#if cr || er>
  <div id="id_operation_dialog" style="display:none;">
  <form name="oprForm" class="css-form" novalidate>
   <table border="0">
   <tr>
    <td>${lang.modelName}:&nbsp;</td>
    <td>
     <input type="text" name="name" id="id_model_name" ng-model="opr_name" ng-trim="true" ng-maxlength="40" required/>
     <span class="error" ng-show="oprForm.name.$dirty && oprForm.name.$invalid">${lang.validModelName}</span><br>
    </td>
   </tr>
   <tr>
    <td>${lang.description}:&nbsp;</td>
    <td>
     <input type="text" name="desc" id="id_model_val" ng-model="opr_desc" ng-trim="true" ng-maxlength="100"/>
     <span class="error" ng-show="oprForm.desc.$dirty && oprForm.desc.$invalid">${lang.validModelDesc}</span><br>
    </td>
  </tr>
  </table>
  </form>
  </div>
</#if>
</div> <!-- end of ListCtrl -->
<!-- end of report -->
</div> <!-- end of ModelList -->

<!-- report -->
<div id="reportDiv"><div style="heith:100%;margin:10px;">
  <input type="hidden" id="cur_server_id">
  <input type="hidden" id="fail_to_query" value="${lang.failToQuery}">
  <div style="background:#e0e0Ff;">
    <table border="0" width="100%"><tr><td>${lang.report}</td>
    <td align="right"><span class="button close" id="report_close"></span></td></tr></table>
  </div>
  <div style="margin:5px;padding:10px;">
    ${lang.startDate}<input id="startDate" type="text">&nbsp;
    ${lang.endDate}<input id="endDate" type="text">&nbsp;&nbsp;
    <span class="button refresh" id="button_refresh_report"></span>
  </div>
  
  <div id="reportContainer"></div>
  
</div></div>

<script type="text/javascript">
seajs.use(["modules/base/model", "modules/base/meta/server.js"], function(model, MetaServer) {
    var meta = new MetaServer("${vars.webRoot}", "${meta}", ["id","name","createTime","creator","val"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        from:0,
        perPage:20
    });
});
</script>

