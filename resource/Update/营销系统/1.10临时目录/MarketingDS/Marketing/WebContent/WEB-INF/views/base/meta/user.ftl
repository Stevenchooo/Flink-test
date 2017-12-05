<div style="width:96%;" ng-app="ModelList">
  <div style="width:100%;" ng-controller="ListCtrl" id="data_grid" ng-cloak>
  
  <#assign cr=(userRight?index_of("|c|") >= 0) />    
  <#assign dr=(userRight?index_of("|d|") >= 0) />    
  <#assign er=(userRight?index_of("|u|") >= 0) />
  <br/>
  <div class="upmob-filter">
    <table style="width:100%"><tr>
      <td style="LINE-HEIGHT: 24px;">${lang.account}
       <input type="text" ng-trim="true" maxlength="32" 
       ng-keypress="enterQuery($event)"
       ng-model="queryAccount" style="width:250px;height:24px;">
      </td>
      <td align="right">
        <input type="button" ng-click="pageClick(0)"
         style="width: 80px;" class="btn-lightblue" value="${lang.query}">
      </td>
    </tr>
    </table>
   
   <br/>
   
   <div class="upmc-controls upmcc-multi">
    <#if cr> 
    	<a href="javascript:void(0)" ng-click="modelCreate()"><b class="link-icon li-batinput"></b> <span class="vm">${lang.modelCreate}</span></a>
    </#if>       
   </div>
   
   
   <div class="up-detable"> 
    <table>
    <tr>
     <th width="10%">${lang.account}</th>
     <th width="10%">${lang.name}</th>
     <th width="10%">${lang.userRole}</th>
     <th width="10%">${lang.department}</th>
     <th width="10%">${lang.phoneNum}</th>
     <th width="20%">${lang.email}</th>
     <th width="7%">${lang.belong}</th>
     <th width="10%">${lang.createTime}</th>
     <th width="7%">${lang.creator}</th>   
<#if cr || dr || er >
     <th width="6%">${lang.modelOperation}
     </th>
</#if>
    </tr>
    <tr ng-repeat="row in resp.result">
     <td>{{row.account}}</td>
     <td>{{row.name}}</td>
     <td>{{row.role}}</td>
     <td>{{row.department}}</td>
     <td>{{row.phoneNum}}</td>
     <td>{{row.email}}</td>
     <td>{{row.deptType}}</td>
     <td>{{row.createTime}}</td>
     <td>{{row.creator}}</td>
<#if er || dr>
     <td>
        <#if er>
          <a href="javascript:void(0)" ng-click="modelModify('{{row.account}}')" class="tb-edit">${lang.modelModify}</a>
        </#if>
        <#if dr>    	             	   
          <a href="javascript:void(0)" ng-click="modelDel('{{row.account}}')"    class="tb-edit">${lang.modelDel}</a>
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
<!-- 创建用户的窗口  -->  
  <div id="id_create_user_dialog" style="display:none;font-size: 12px;">
  <form name="userCreateForm" class="css-form" novalidate autocomplete="off">
   <table border="0" style="width:750px;">
   <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.account}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
      <select ng-model="account"
                ng-trim="true" ng-options="p.userName as p.userName for p in accountList" style="height:24px;width:300px;"  ng-trim="true"    required>
     </select>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userCreateForm.account.$invalid">${lang.account_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.userRole}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
     <select ng-model="crt_role" 
            ng-trim="true" ng-options="p.role as p.name for p in userRoles" style="height:24px;width:300px;"  ng-trim="true"    required>
     </select>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userCreateForm.crt_role.$invalid">${lang.userRole_validate}</span><br>
    </td>
    </td>
   </tr>
   
   <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.name}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
     <input type="text" name="name" ng-model="name" ng-trim="true" ng-minlength="1" ng-maxlength="20" style="width:300px;height:24px;" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userCreateForm.name.$invalid">${lang.name_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.department}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
     <input type="text" name="department" ng-model="department" ng-trim="true" ng-minlength="1" ng-maxlength="50" style="width:300px;height:24px;" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userCreateForm.department.$invalid">${lang.department_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.phoneNum}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
     <input type="text" name="phoneNum" ng-model="phoneNum" ng-trim="true" ng-minlength="6" ng-maxlength="15" ng-pattern='/^(\d{1,4}-?)?\d{7,11}$/' style="width:300px;height:24px;" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userCreateForm.phoneNum.$invalid">${lang.phoneNum_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.email}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
     <input type="email" name="email" ng-model="email" ng-trim="true" ng-maxlength="40" style="width:300px;height:24px;" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userCreateForm.email.$invalid">${lang.email_validate}</span><br>
    </td>
   </tr>
   
   
   <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.belong}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
     <select ng-model="departmentType" 
            ng-trim="true" ng-options="p.id as p.name for p in departments" style="height:24px;width:300px;"  ng-trim="true"    required>
     </select>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userCreateForm.departmentType.$invalid">${lang.departmentType_validate}</span><br>
    </td>
    </td>
   </tr>
   
   
         
         
         
   <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.description}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
     <input type="text" name="description" ng-model="description" ng-trim="true" ng-minlength="1" ng-maxlength="50" style="width:300px;height:24px;"/>
     <span class="error" ng-show="userCreateForm.description.$invalid">${lang.description_validate}</span><br>
    </td>
   </tr>
   
   
   <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.showFlag}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
         <input value="1" ng-model="showFlag" type="radio">是
         <input value="0" ng-model="showFlag" type="radio">否
    </td>
   </tr>
   
   
  </table>
  </form>
  </div>
</#if>
<#if er>
  <div id="id_modify_user_dialog" style="display:none;font-size: 12px;">
  <form name="userModifyForm" class="css-form" novalidate>
   <table border="0" style="width:750px;">
   <tr>
    <td  style="LINE-HEIGHT: 30px;" >${lang.account}:&nbsp;</td>
    <td  style="LINE-HEIGHT: 24px;" >
     <span id="id_mod_account">{{mod_account}}</span>
    </td>
   </tr>
   
   <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.userRole}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
     <select ng-model="mod_role" 
            ng-trim="true" ng-options="p.role as p.name for p in userRoles" style="height:24px;width:300px;"  ng-trim="true"    required>
     </select>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userCreateForm.mod_role.$invalid">${lang.userRole_validate}</span><br>
    </td>
    </td>
   </tr>
   
    <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.name}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
     <input type="text" name="mod_name" ng-model="mod_name" ng-trim="true" ng-minlength="1" ng-maxlength="20" style="width:300px;height:24px;" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userModifyForm.mod_name.$invalid">${lang.name_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.department}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
     <input type="text" name="mod_department" ng-model="mod_department" ng-trim="true" ng-minlength="1" ng-maxlength="50" style="width:300px;height:24px;" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userModifyForm.mod_department.$invalid">${lang.department_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.phoneNum}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
     <input type="text" name="mod_phoneNum" ng-model="mod_phoneNum" ng-trim="true" ng-minlength="6" ng-maxlength="15" ng-pattern="/^(\d{1,4}-?)?\d{7,11}$/" style="width:300px;height:24px;" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userModifyForm.mod_phoneNum.$invalid">${lang.phoneNum_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.email}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
     <input type="email" name="mod_email" ng-model="mod_email" ng-trim="true" ng-maxlength="40" style="width:300px;height:24px;" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userModifyForm.mod_email.$invalid">${lang.email_validate}</span><br>
    </td>
   </tr>
   
    
   <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.belong}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
     <select ng-model="mod_departmentType" 
            ng-trim="true" ng-options="p.id as p.name for p in mod_departments" style="height:24px;width:300px;"  ng-trim="true"    required>
     </select>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userCreateForm.departmentType.$invalid">${lang.departmentType_validate}</span><br>
    </td>
    </td>
   </tr>
   
   <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.description}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
     <input type="text" name="mod_description" ng-model="mod_description" ng-trim="true" ng-minlength="1" ng-maxlength="50" style="width:300px;height:24px;"/>
     <span class="error" ng-show="userModifyForm.mod_description.$invalid">${lang.description_validate}</span><br>
    </td>
   </tr>
   
   
   <tr>
    <td style="LINE-HEIGHT: 30px;" >${lang.showFlag}:&nbsp;</td>
    <td style="LINE-HEIGHT: 24px;" >
         <input value="1" ng-model="mod_showFlag" type="radio">是
         <input value="0" ng-model="mod_showFlag" type="radio">否
    </td>
   </tr>
   
   <tr>
    <input type="text" name="mod_id" ng-model="mod_id" ng-trim="true" ng-minlength="1" ng-maxlength="50" style="display:none;"/>
   </tr>
   
  </table>
  </form>
  </div>
</#if>
</div> <!-- end of up-detable -->
</div> <!-- end of upmob-filter --> 
</div> <!-- end of ListCtrl -->
</div> <!-- end of ModelList -->

<script type="text/javascript">
seajs.use(["modules/base/model", "modules/base/meta/user"], function(model, MetaUser) {
    var meta =  MetaUser.instance("${vars.webRoot}", 'user', ["id","name","createTime","creator","val"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        from:0,
        perPage:15
    });
});
</script>