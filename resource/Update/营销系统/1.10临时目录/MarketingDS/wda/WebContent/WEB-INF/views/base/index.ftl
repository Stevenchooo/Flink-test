<#include "../common_header.ftl">
<style>
html,body {
    min-width: 975px;
}
</style>
<div id="dHead">
<div id="head_div">
  <div id="head_logo">${lang.system_name}</div>
  <div style="float:right;">
    <div class="head_text">
        <span class="head_welcome">Hello <strong id="user_account" class="head_function">${__account}</strong>  ${__userKey}    </span>&nbsp;|&nbsp;
        <span id="btn_logout" class="head_function">${lang.logout}</span>&nbsp;|&nbsp;
    </div>
    <div id="head_menu"></div>
  </div>
</div>
</div>
<div id="dBody">
<div id="main_div">
    <div id="navigation">/</div>
    <div id="dataContainer">
        <div id="treeContainer">
            <ul id="modelTree" class="ztree" style="width:260px; overflow:auto;"></ul>
        </div>
        <div id="rightContainer">
            <div id="listContainer">
              <center>
              <br/>
              <img src="${vars.imgRoot}/index/global.png" />
              <br/>
              <br/>
              <div>${lang.system_name}</div>
              </center>
            </div>
        </div>
    </div>
</div><!-- end of main_div -->
</div><!-- end of dBody -->

<div id="dFoot">${lang.copyRight}</div>

<script type="text/javascript">
seajs.use(["jquery", "angular", "modules/base/index"], function($, angular, index) {
    index.init({
        treeContainer:"modelTree",
        naviBar:"#navigation", 
        featureBar:"#head_menu", 
        listContainer:"listContainer"
    });
});
</script>
<#include "../common_footer.ftl">