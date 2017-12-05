<#include "../common_header.ftl">
<div id="naviContainer">
	<div class="up-topbar" style="display:none;">
	    <ul class="tb-cont">
            <li>
                <a href="#">${__account}</a>
            </li>
            <li>
                <a id="btn_logout" href="javascript:void(0);">${lang.logout}</a>
            </li>
        </ul>
	</div>
	
	<div class="up-main mkcl">
	
	<ul id="nav-bar" class="upm-nav">
		
	</ul>
	        
    <div class="upm-cont">
    	<div id="listContainer2">
    		<center>
          	</center>
        </div>
    </div>
	 
	
</div>

<script>
	seajs.use(["navi", "modules/base/user"], function(navi, user) {
	    navi.showList("${__account}");
	});
	
</script>
<#include "../common_footer.ftl">