<div class="upmc-wp" ng-app="ModelList">
  <div style="width:100%;" ng-controller="ListCtrl" id="data_grid" ng-cloak>
  <div class="upmob-filter">	
  	<br/>
<#assign cr=(userRight?index_of("|c|") >= 0) />    
<#assign dr=(userRight?index_of("|d|") >= 0) />    
<#assign er=(userRight?index_of("|u|") >= 0) /> 
	
	
	  <div class="upmc-controls upmcc-multi">
	  	<#if cr> 
    	<a href="javascript:void(0)" ng-click="modelCreate()"><b class="link-icon li-batinput"></b> <span class="vm">${lang.modelCreate}</span></a>
    	</#if>       
    </div>
    
  	<div class="up-detable">
    <table>    
    <tr>
     <th>${lang.userRole}</th>
     <th>${lang.roleName}</th>
     <th>${lang.roleDesc}</th>   
<#if cr || dr || er >
     <th>${lang.modelOperation}
</#if>
    </tr>
    <tr ng-repeat="row in resp.roles">
     <td>{{row.role}}</td>
     <td>{{row.name}}</td>
     <td>{{row.desc}}</td>
<#if er || dr>
     <td>
        <#if er>
          
          <a href="javascript:void(0)" ng-click="modelModify('{{row.role}}')" class="tb-edit">${lang.modelModify}</a>&nbsp;
          <a href="javascript:void(0)" ng-click="modelRight('{{row.role}}')" class="tb-edit">${lang.rightSet}</a>
        </#if>
        <#if dr>
        	&nbsp;<a href="javascript:void(0)" ng-click="modelDel('{{row.role}}')" class="tb-edit">${lang.modelDel}</a>
        </#if>
     </td>
</#if>
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

<#if cr>
<!-- 创建角色的窗口  -->  
  <div id="id_create_role_dialog" style="display:none;font-size: 12px;">
  <form name="roleCreateForm" class="css-form" novalidate>
   <table border="0">
   <tr>
    <td style="LINE-HEIGHT: 30px;">${lang.userRole}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;">
     <input type="text" name="role" id="id_role" ng-model="role" ng-trim="true" ng-minlength="1" ng-maxlength="20" style="width:126px;height:24px;" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="roleCreateForm.role.$dirty && roleCreateForm.role.$invalid">${lang.roleValidate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td style="LINE-HEIGHT: 30px;">${lang.roleName}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;">
     <input type="text" name="name" id="id_name" ng-model="name" ng-trim="true" ng-minlength="1" ng-maxlength="50" style="width:126px;height:24px;" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="roleCreateForm.name.$dirty && roleCreateForm.name.$invalid">${lang.roleNameValidate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td style="LINE-HEIGHT: 30px;">${lang.roleDesc}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;">
     <input type="text" name="desc" id="id_desc" ng-model="desc" ng-trim="true" ng-maxlength="255" style="width:126px;height:24px;" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="roleCreateForm.desc.$dirty && roleCreateForm.desc.$invalid">${lang.roleDescValidate}</span><br>
    </td>
   </tr>
  </table>
  </form>
  </div>
</#if>
<#if er>
<!-- 修改角色窗口  -->  
  <div id="id_modify_role_dialog" style="display:none;font-size: 12px;">
  <form name="roleModifyForm" class="css-form" novalidate>
   <table border="0">
   <tr>
    <td style="">${lang.userRole}:&nbsp;</td>
    <td style="">
     <span id="id_mod_role">{{mod_role}}</span>
    </td>
   </tr>
   
   <tr>
    <td style="LINE-HEIGHT: 30px;">${lang.roleName}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;">
     <input type="text" name="mod_name" id="id_mod_name" ng-model="mod_name" ng-trim="true" ng-minlength="1" ng-maxlength="50" style="width:126px;height:24px;" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="roleModifyForm.mod_name.$dirty && roleModifyForm.mod_name.$invalid">${lang.roleNameValidate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td style="LINE-HEIGHT: 30px;">${lang.roleDesc}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;">
     <input type="text" name="mod_desc" id="id_mod_desc" ng-model="mod_desc" ng-trim="true" ng-maxlength="255" style="width:126px;height:24px;" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="roleModifyForm.mod_desc.$dirty && roleModifyForm.mod_desc.$invalid">${lang.roleDescValidate}</span><br>
    </td>
   </tr>
  </table>
  </form>
  </div>
  
<!-- 修改角色权限窗口  -->  
  <div id="id_modify_right_dialog" style="display:none;height:300px;overflow:scroll;overflow-x:hidden;font-size: 12px;">
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
</div> <!-- end of up-detable -->
</div> <!-- end of upmob-filter -->
</div> <!-- end of ListCtrl -->
</div> <!-- end of ModelList -->

<script type="text/javascript">
seajs.use(["modules/base/model", "modules/base/meta/role"], function(model, MetaRole) {
    var meta =  MetaRole.instance("${vars.webRoot}", 'role', ["role","name","desc"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        from:0,
        perPage:20
    });
});
</script>