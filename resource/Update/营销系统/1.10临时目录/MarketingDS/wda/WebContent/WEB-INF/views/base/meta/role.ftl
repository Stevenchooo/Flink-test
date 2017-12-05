<div style="width:96%;" ng-app="ModelList">
  <div style="width:100%;" ng-controller="ListCtrl" id="data_grid" ng-cloak>
    <table class="listTab">
    
    <tr>
     <th>${lang.userRole}</th>
     <th>${lang.roleName}</th>
     <th>${lang.roleDesc}</th>
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
    <tr ng-repeat="row in resp.roles">
     <th>{{row.role}}</th>
     <td>{{row.name}}</td>
     <td>{{row.desc}}</td>
<#if er || dr>
     <th>
        <#if er>
          <span ng-click="modelModify('{{row.role}}')" class="button edit" title="${lang.modelModify}"></span>
          &nbsp;<span ng-click="modelRight('{{row.role}}')" class="button roleRight" title="${lang.rightSet}"></span>
        </#if>
        <#if dr>
          &nbsp;<span ng-click="modelDel('{{row.role}}')" class="button delete" title="${lang.modelDel}"></span>
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
<!-- 创建角色的窗口  -->  
  <div id="id_create_role_dialog" style="display:none;">
  <form name="roleCreateForm" class="css-form" novalidate>
   <table border="0">
   <tr>
    <td>${lang.userRole}:&nbsp;</td>
    <td>
     <input type="text" name="role" id="id_role" ng-model="role" ng-trim="true" ng-minlength="1" ng-maxlength="20" required/>
     <span class="error" ng-show="roleCreateForm.role.$dirty && roleCreateForm.role.$invalid">${lang.roleValidate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.roleName}:&nbsp;</td>
    <td>
     <input type="text" name="name" id="id_name" ng-model="name" ng-trim="true" ng-minlength="1" ng-maxlength="50" required/>
     <span class="error" ng-show="roleCreateForm.name.$dirty && roleCreateForm.name.$invalid">${lang.roleNameValidate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.roleDesc}:&nbsp;</td>
    <td>
     <input type="text" name="desc" id="id_desc" ng-model="desc" ng-trim="true" ng-maxlength="250"/>
     <span class="error" ng-show="roleCreateForm.desc.$dirty && roleCreateForm.desc.$invalid">${lang.roleDescValidate}</span><br>
    </td>
   </tr>
  </table>
  </form>
  </div>
</#if>
<#if er>
<!-- 修改角色窗口  -->  
  <div id="id_modify_role_dialog" style="display:none;">
  <form name="roleModifyForm" class="css-form" novalidate>
   <table border="0">
   <tr>
    <td>${lang.userRole}:&nbsp;</td>
    <td>
     <span id="id_mod_role">{{mod_role}}</span>
    </td>
   </tr>
   
   <tr>
    <td>${lang.roleName}:&nbsp;</td>
    <td>
     <input type="text" name="mod_name" id="id_mod_name" ng-model="mod_name" ng-trim="true" ng-minlength="1" ng-maxlength="20" required/>
     <span class="error" ng-show="roleModifyForm.mod_name.$dirty && roleModifyForm.mod_name.$invalid">${lang.roleValidate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.roleDesc}:&nbsp;</td>
    <td>
     <input type="text" name="mod_desc" id="id_mod_desc" ng-model="mod_desc" ng-trim="true" ng-maxlength="255"/>
     <span class="error" ng-show="roleModifyForm.mod_desc.$dirty && roleModifyForm.mod_desc.$invalid">${lang.roleDescValidate}</span><br>
    </td>
   </tr>
  </table>
  </form>
  </div>
  
<!-- 修改角色权限窗口  -->  
  <div id="id_modify_right_dialog" style="display:none;height:300px;overflow:scroll;overflow-x:hidden;">
   <table class="listTab" style="width:500px;">
   <tr>
    <th align="center">${lang.dataType}</th>
    <th align="center">${lang.rightSet}</th>
   </tr>
   
   <tr ng-repeat="result in roleRightResults">
    <th align="center">{{getMetaName(result.dataType)}}</th>
    <td>
      <span ng-repeat="rgt in result.operations">
        <input type="checkbox" ng-checked="{{rgt.val}}" ng-model="rgt.val" />{{getRightName(rgt.name)}}&nbsp;&nbsp;
      </span>
    </td>
   </tr>
  </table>
  </div>
</#if>
</div> <!-- end of ListCtrl -->
</div> <!-- end of ModelList -->

<script type="text/javascript">
seajs.use(["modules/base/model", "modules/base/meta/role"], function(model, MetaRole) {
    var meta = new MetaRole("${vars.webRoot}", 'role', ["role","name","desc"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        from:0,
        perPage:20
    });
});
</script>