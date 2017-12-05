<#include "../common_header.ftl">
<#compress>
<div id="loginDiv" style="width:96%;display:none;">
 <div style="width:100%;" id="data_grid">
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
              <input type="text" id="loginAccount" name="account"  style="height:22px" class="input_text"  onkeyup="loginAccountCheck()"/>
              <span class="error" id="accountTip" style="display:none;" >${lang.account_validate1}</span>
            </td>
           </tr>
            
           <tr>
            <td class="input_name">${lang.pwd}</td>
            <td class="input_content">
              <input type="password" id="loginPwd" name="pwd"  style="height:22px" class="input_text"  onkeyup="loginPwdCheck()"/>
              <span class="error" id="pwdTip" style="display:none;">${lang.password_validate}</span>
            </td>
           </tr>
            
           <tr>
            <td class="input_name">${lang.verifyCode}</td>
            <td class="input_content">
              <div style="display: inline;" id="verifyCodeDiv">
                <input type="text" name="verifyCode" id="verifyCode" class="input_text" style="width: 50px; vertical-align: top;height:22px" />
                <img id="ImageCheck" src="${vars.webRoot}/page/verifyCode" style="border-style: None; border-width: 0px;" />
                <img src="${vars.imgRoot}/login/refresh.gif" style="cursor:pointer;margin: 0px; padding: 0px; border: 0px;" title="${lang.changeVerifyCode}"  onClick="refreshVerifyCode()"/>
              </div>
            </td>
           </tr>
            
    
           <tr>
            <td class="input_name">&nbsp;</td>
            <td class="input_content">
              <input type="button" class="login_btn" value="${lang.login}" onClick="login()"/>
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


<script src="${vars.webRoot}/js/jquery/jquery/2.1.3/jquery.js"></script>
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
                    showIndex(res.passport, res.__userKey);
                    
                } 
                else if(res.resultCode == 7001)
                {
                    window.location.href = "https://emui.huawei.com/d/BIPortal/noprivilege.jsp";
                }
                else
                {
                    window.location.href = "https://biportal/BIPortal/login";
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


function showIndex(passport, userKey) {
    window.location.href = __webRoot + "/page/index";
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
                $("#loginInfo").html('');
                showIndex();
            }

        }
    });
};
        
  
</script>
</#compress>  
<#include "../common_footer.ftl">