<style>
.userDetail {
    margin-left:auto;  
    margin-right:auto;
    border-collapse:collapse;
    width:80%;
}

table.userDetail td  {
    border: 1px solid #BBBBBB;
    padding: 8px 0;
}

table.userDetail th  {
    border: 1px solid #BBBBBB;
    padding: 8px 0;
    background: #e8e8e8;
}

table.userRight {
    border-collapse:collapse;
    width:100%;
    text-align:center;
}

table.userRight td  {
    border: 0;
    padding: 2px 0;
}

table.userRight tr:first-child td  {
    padding: 2px 0;
    border-top: 0;
    border-right: 0;
    border-bottom: 1px solid #BBBBBB;
    border-left: 0;
    font-weight:bold;
}
</style>
<div style="width:96%;" ng-app="ModelList">
  <div style="width:100%;" ng-controller="ListCtrl" ng-cloak>
    <div class="userDetail" style="text-align:right;">
      <span ng-click="modelModify('${__account}')" class="button edit" title="${lang.modelUserDetail}"></span>
      &nbsp;&nbsp;
      <span ng-click="modifyPwd('${__account}')" class="button roleRight" title="${lang.modifyPassword}"></span>
      <br/><br/>
    </div>
    <table class="userDetail">
      <tr><th>${lang.account}:</th><td>{{resp.account}}</td></tr>
      <tr><th>${lang.phoneNum}:</th><td>{{resp.phoneNum}}</td></tr>
      <tr><th>${lang.email}:</th><td>{{resp.email}}</td></tr>
      <tr><th>${lang.createTime}:</th><td>{{resp.createTime}}</td></tr>
      <tr><th v-align="middle">${lang.userRight}:</th><td>
        <table class="userRight">
          <tr>
            <td>${lang.userRightPath}</td>
            <td>${lang.userRole}</td>
            </tr>
            <tr ng-repeat="row in resp.roles">
             <td>{{row.path}}</td>
             <td>{{row.role}}</td>
            </tr>
        </table>
      </td></tr>
    </table>
<!-- modify dialog -->    
  <div id="id_modify_user_dialog" style="display:none;">
  <form name="userModifyForm" class="css-form" novalidate>
   <table border="0">
   <tr>
    <td>${lang.account}:&nbsp;</td>
    <td><span id="id_mod_account">{{resp.account}}</span></td>
   </tr>
   
   <tr>
    <td>${lang.phoneNum}:&nbsp;</td>
    <td>
     <input type="text" name="mod_phoneNum" ng-model="mod_phoneNum" ng-trim="true" ng-minlength="6" ng-maxlength="15" ng-pattern="/^(\d{1,4}-?)?\d{7,11}$/" value="{{resp.phoneNum}}" required/>
     <span class="error" ng-show="userModifyForm.mod_phoneNum.$dirty && userModifyForm.mod_phoneNum.$invalid">${lang.phoneNum_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.email}:&nbsp;</td>
    <td>
     <input type="email" name="mod_email" ng-model="mod_email" ng-trim="true" ng-maxlength="40" value="{{resp.email}}" required/>
     <span class="error" ng-show="userModifyForm.mod_email.$dirty && userModifyForm.mod_email.$invalid">${lang.email_validate}</span><br>
    </td>
   </tr>
  </table>
  </form>
  </div>
<!-- end of modify dialog -->

<!-- modify password dialog -->    
  <div id="id_modify_pwd_dialog" style="display:none;">
  <form name="modifyPwdForm" class="css-form" novalidate autocomplete="off">
   <table border="0">
   <tr>
    <td>${lang.oldPassword}:&nbsp;</td>
    <td>
     <input type="password" name="oldPassword" ng-model="oldPassword" ng-trim="true" ng-minlength="6" ng-maxlength="40" onpaste="return false" required/>
     <span class="error" ng-show="modifyPwdForm.oldPassword.$dirty && modifyPwdForm.oldPassword.$invalid">${lang.password_validate}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.newPassword}:&nbsp;</td>
    <td>
     <input type="password" name="newPassword" ng-model="newPassword" ng-trim="true" onpaste="return false" required valid-password="account"/>
     <span class="error" ng-show="modifyPwdForm.newPassword.$dirty && modifyPwdForm.newPassword.$invalid">{{invalid_password}}</span><br>
    </td>
   </tr>
   
   <tr>
    <td>${lang.confirmPassword}:&nbsp;</td>
    <td>
     <input type="password" name="confirmPassword" ng-model="confirmPassword" onpaste="return false" ng-trim="true" required/>
     <span class="error" ng-show="modifyPwdForm.confirmPassword.$dirty && confirmPassword != newPassword">${lang.confirmPasswordEquals}</span><br>
    </td>
   </tr>
   
	<tr>
	 <td>${lang.verifyCode}:&nbsp;</td>
	 <td>
	  <div style="display: inline;" id="verifyCodeDiv">
	   <input type="text" name="verifyCode2" class="input_text" style="width: 50px; vertical-align: top;" ng-minlength="4" ng-maxlength="4" ng-model="verifyCode2" ng-trim="true" required/>
	   <img id="ImageCheck2" src="${vars.webRoot}/page/verifyCode?t=UserChange" style="border-style: None; border-width: 0px;" />
	   <img src="${vars.imgRoot}/login/refresh.gif" style="cursor:pointer;margin: 0px; padding: 0px; border: 0px;" title="${lang.changeVerifyCode}" ng-click="refreshVerifyCode2()"/>
	  </div>
	 </td>
	</tr> 

  </table>
  </form>
  </div>
<!-- end of modify password dialog -->

  </div> <!-- end of ListCtrl -->
</div> <!-- end of ModelList -->
<script type="text/javascript">
seajs.use(["modules/base/model", "modules/base/meta/userDetail"], function(model, UserDetail) {
    var meta = new UserDetail("${vars.webRoot}", 'user', ["account","createTime","phoneNum","email"]);
    model.showList({
        id:0,
        from:0,
        perPage:10,
        meta:meta,
        webRoot:"${vars.webRoot}"
    });
});
</script>
