<div style="width:96%;" ng-app="ModelList">
  <div style="width:100%;" ng-controller="ListCtrl" id="data_grid" ng-cloak>
  
    <br>
    <table class="listTab">
    
    <tr>
     <th>${lang.account}</th>
     <th>${lang.userRole}</th>
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
     <th>{{row.account}} ({{row.name}})</th>
     <td>{{row.role}}</td>
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
    <div id="goto_page_window" style="width:${lang.pageWindowWidth}px;">
    <table border="0" style="margin:1px;"><tr>
      <td><input type="text" ng-model="gotoPageNo" class="page_input"></td>
      <td ng-click="pageClick(-2)" style="cursor:pointer;color:blue;">${lang.goToPage}</td></tr>
    </table>
    </div>
    
<#if cr>
  <div id="id_create_dialog" style="display:none;">
  <form name="createForm" class="css-form" novalidate>
   <table border="0">
   <tr>
    <td>${lang.account}:&nbsp;</td>
    <td>
      <select ng-model="crt_account" name="account" id="id_crt_account"
                ng-trim="true" ng-options="p.id as p.name for p in accounts" style="height:24px;width:160px"  ng-trim="true" required>
      </select>
         
     <span class="error" ng-show="createForm.account.$dirty && createForm.account.$invalid">${lang.account_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.userRole}:&nbsp;</td>
    <td>
     <select ng-model="crt_role" ng-options="r.role as r.name for r in roles" style="height:24px;width:160px">
     </select>
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
    <td>${lang.account}:&nbsp;</td>
    <td><span>{{account}}</span><br></td>
   </tr>
   
   <tr>
    <td>${lang.userRole}:&nbsp;</td>
    <td>
     <select ng-model="mod_role" name="mod_role" ng-options="r.role as r.name for r in roles">
     </select>
    </td>
   </tr>
  </table>
  </form>
  </div>
</#if>
</div> <!-- end of ListCtrl -->
</div> <!-- end of ModelList -->

<script type="text/javascript">
seajs.use(["modules/base/model", "modules/base/meta/admin"], function(model, MetaAdmin) {
    var meta = MetaAdmin.instance("${vars.webRoot}", 'admin', ["id","name","createTime","creator","val"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        from:0,
        perPage:15
    });
});
</script>