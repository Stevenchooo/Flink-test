<div style="width:96%;" ng-app="ModelList">
  <div style="width:100%;" ng-controller="ListCtrl" id="data_grid" ng-cloak>

    <table style="width:100%"><tr>
      <td style="LINE-HEIGHT: 30px;">${lang.account}
       <input type="text" ng-trim="true" maxlength="32" 
       ng-keypress="enterQuery($event)"
       ng-model="queryAccount" style="width:250px;height:24px;">
      </td>
      <td align="right">
        <input type="button" ng-click="pageClick(0)"
         style="width: 80px;" class="baseButton" value="${lang.query}">
      </td>
    </tr></table>
    <hr/><br/>
    
    <table class="listTab">
    <tr>
     <th>${lang.account}</th>
     <th>${lang.phoneNum}</th>
     <th>${lang.email}</th>
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
    <tr ng-repeat="row in resp.result">
     <th>{{row.account}}</th>
     <td>{{row.phoneNum}}</td>
     <td>{{row.email}}</td>
     <td>{{row.createTime}}</td>
     <td>{{row.creator}}</td>
<#if er || dr>
     <th>
        <#if er>
          <span ng-click="modelModify('{{row.account}}')" class="button edit" title="${lang.modelModify}"></span>
          <span ng-click="adminResetPassword('{{row.account}}')" class="button update" title="${lang.resetPassword}"></span>
        </#if>
        <#if dr>
          &nbsp;<span ng-click="modelDel('{{row.account}}')" class="button delete" title="${lang.modelDel}"></span>
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
<!-- 创建用户的窗口  -->  
  <div id="id_create_user_dialog" style="display:none;">
  <form name="userCreateForm" class="css-form" novalidate autocomplete="off">
   <table border="0">
   <tr>
    <td>${lang.account}:&nbsp;</td>
    <td>
     <input type="text" name="account" ng-model="account" ng-trim="true" ng-minlength="6" ng-maxlength="40" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userCreateForm.account.$dirty && userCreateForm.account.$invalid">${lang.account_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.pwd}:&nbsp;</td>
    <td>
     <input type="password" name="pwd" ng-model="pwd" ng-trim="true" onpaste="return false" required valid-password="account"/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userCreateForm.pwd.$dirty && userCreateForm.pwd.$invalid">{{invalid_password}}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.confirmPassword}:&nbsp;</td>
    <td>
     <input type="password" name="confirmPassword" ng-model="confirmPassword" ng-trim="true" onpaste="return false" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userCreateForm.confirmPassword.$dirty && confirmPassword != pwd">${lang.confirmPasswordEquals}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.phoneNum}:&nbsp;</td>
    <td>
     <input type="text" name="phoneNum" ng-model="phoneNum" ng-trim="true" ng-minlength="6" ng-maxlength="15" ng-pattern='/^(\d{1,4}-?)?\d{7,11}$/' required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userCreateForm.phoneNum.$dirty && userCreateForm.phoneNum.$invalid">${lang.phoneNum_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.email}:&nbsp;</td>
    <td>
     <input type="email" name="email" ng-model="email" ng-trim="true" ng-maxlength="40" required/>
     <span class="error">*&nbsp;</span>
     <span class="error" ng-show="userCreateForm.email.$dirty && userCreateForm.email.$invalid">${lang.email_validate}</span><br>
    </td>
   </tr>
  </table>
  </form>
  </div>
</#if>
<#if er>
  <div id="id_modify_user_dialog" style="display:none;">
  <form name="userModifyForm" class="css-form" novalidate>
   <table border="0">
   <tr>
    <td>${lang.account}:&nbsp;</td>
    <td>
     <span id="id_mod_account">{{mod_account}}</span>
    </td>
   </tr>
   
   <tr>
    <td>${lang.phoneNum}:&nbsp;</td>
    <td>
     <input type="text" name="mod_phoneNum" id="id_mod_user_phoneNum" ng-model="mod_phoneNum" ng-trim="true" ng-minlength="6" ng-maxlength="15" ng-pattern="/^(\d{1,4}-?)?\d{7,11}$/" required/>
     <span class="error" ng-show="userModifyForm.mod_phoneNum.$dirty && userModifyForm.mod_phoneNum.$invalid">${lang.phoneNum_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.email}:&nbsp;</td>
    <td>
     <input type="email" name="mod_email" id="id_mod_user_email" ng-model="mod_email" ng-trim="true" ng-maxlength="40" required/>
     <span class="error" ng-show="userModifyForm.mod_email.$dirty && userModifyForm.mod_email.$invalid">${lang.email_validate}</span><br>
    </td>
   </tr>
  </table>
  </form>
  </div>
</#if>
</div> <!-- end of ListCtrl -->
</div> <!-- end of ModelList -->

<script type="text/javascript">
seajs.use(["modules/base/model", "modules/base/meta/user"], function(model, MetaUser) {
    var meta = new MetaUser("${vars.webRoot}", 'user', ["id","name","createTime","creator","val"]);
    model.showList({
        id:"${id}",
        meta:meta,
        webRoot:"${vars.webRoot}",
        from:0,
        perPage:15
    });
});
</script>