<#include "../common_header.ftl">
<#compress>
<div style="width:96%;" ng-app="LoginApp">
 <div style="width:100%;" ng-controller="LoginCtrl" id="data_grid" ng-cloak>
   <div style="height:80px;"></div>
   <div class="login_frame">
      <table border="0" class="login_header"><tr>
        <td style="width:50px"><img title="logo" src="${vars.imgRoot}/logo.gif" width="46px" height="42px"/></td>
        <td><div style="position:relative; padding-top:10px;">${lang.system_name}</div></td>
      </table>
      
      <div class="login_banner"></div>
      
      <div class="login_win">
        <form name="loginForm" class="css-form" novalidate autocomplete="off">
          <table border="0">
           <tr>
            <td class="input_name">${lang.account}</td>
            <td class="input_content">
              <input type="text" id="id_account" name="account" class="input_text" ng-model="account" ng-trim="true" ng-minlength="5" ng-maxlength="40" required/>
              <span class="error" ng-show="(loginForm.account.$dirty||loginForm.submitted) && loginForm.account.$invalid">${lang.account_validate1}</span>
            </td>
           </tr>
            
           <tr>
            <td class="input_name">${lang.pwd}</td>
            <td class="input_content">
              <input type="password" id="loginPwd" name="pwd" class="input_text" ng-model="pwd" ng-trim="true" ng-minlength="6" ng-maxlength="30" onpaste="return false" required/>
              <span class="error" ng-show="(loginForm.pwd.$dirty||loginForm.submitted) && loginForm.pwd.$invalid">${lang.password_validate}</span>
            </td>
           </tr>
            
           <tr>
            <td class="input_name">${lang.verifyCode}</td>
            <td class="input_content">
              <div style="display: inline;" id="verifyCodeDiv">
                <input type="text" name="verifyCode" class="input_text" style="width: 50px; vertical-align: top;" ng-minlength="4" ng-maxlength="4" ng-model="verifyCode" ng-trim="true" required/>
                <img id="ImageCheck" src="" style="border-style: None; border-width: 0px;" />
                <img src="${vars.imgRoot}/login/refresh.gif" style="cursor:pointer;margin: 0px; padding: 0px; border: 0px;" title="${lang.changeVerifyCode}" ng-click="refreshVerifyCode()"/>
              </div>
            </td>
           </tr>
            
           <tr>
            <td class="input_name">Language</td>
            <td class="input_content">
                <div style="display:inline;">
		          <select ng-model="lang" ng-change="changeLanguage()">
		          <option value="en">English</option>
		          <option value="zh_CN">中文</option>
		          </select>
                </div>
            </td>
           </tr>
    
           <tr>
            <td class="input_name">&nbsp;</td>
            <td class="input_content">
              <input type="button" class="login_btn" value="${lang.login}" ng-click="login()"/>
              <!-- <a id="mailInfo" ng-click="mail()" style="cursor:pointer;">${lang.mail}</a>  -->
              <span id="loginInfo" class="error"></span>  
            </td>
            
           </tr>
          </table>
        </form>
      </div><!-- end of login_win -->
      
      <div class="login_bottom"></div>
      
           
   </div>
</div> 
</div>


<script src="${vars.webRoot}/vendor/jquery/jquery.min.js"></script>
<script type="text/javascript">

var __cookieHeader="${vars.cookieHead}";
var __webRoot="${vars.webRoot}";
var __casClient=${vars.casEnable};


if(true==__casClient)
{

    jQuery.ajax({
            type:"POST",
            url:__webRoot+"/api/user/login", 
            dataType:"json",
            data: {
            },
            
            success: function(res){
            	
                if(res.resultCode == 0) {
                    showIndex(res.passport, res.__userKey, res.__userHref);
                    
                }
            }
        });
}
else
{
    $("#loginDiv").show();
}

function loginAccountCheck()
{
    var account = $("#loginAccount").val();
    
    var regx = /^.{5,40}$/;
    
    var flag = regx.test(account);
    
    if(flag)
    {
        $("#accountTip").hide();
    }
    else
    {
        $("#accountTip").show();
    }
    
    return flag;
}


function loginPwdCheck()
{
    var pwd = $("#loginPwd").val();
    
    var regx = /^.{6,40}$/;
    
    var flag = regx.test(pwd);
    
    if(flag)
    {
        $("#pwdTip").hide();
    }
    else
    {
        $("#pwdTip").show();
    }
    
    return flag;
}


function showIndex(passport, userKey, userHref) {
    //window.location.href = __webRoot + "/page/index";
    
    if(userHref){
    window.location.href = __webRoot + "/page/rongyao";
    }else{
     window.location.href = __webRoot + "/page";
    }
    
};

function showLogin() {
    window.location.href = __webRoot + "/page/login";
};

function refreshVerifyCode() {
    $("#verifyCode").val('')
    $('#ImageCheck').attr('src', __webRoot + "/page/verifyCode?t=" + (new Date().getTime().toString(36)));
};


function login() {

    if(!loginAccountCheck() || !loginPwdCheck())
    {
        return;
    }
    $.ajax({
        type : "POST",
        url : __webRoot + "/api/user/login",
        dataType : "json",
        data : {
            "account" : $("#loginAccount").val(),
            "password" : $("#loginPwd").val(),
            "verify" : $("#verifyCode").val(),
        },

        success : function (res) 
        {
            if (res.resultCode != 0) 
            {
                $("#loginInfo").html(getLocalReason(res.resultCode, res.info));
               
                refreshVerifyCode();
               
                return;
            } else 
            {
                $("#loginInfo").html('抱歉，您没有权限访问，请联系管理员！');
                showIndex();
            }

        }
    });
};
        
  
</script>
</#compress>  
<#include "../common_footer.ftl">