define('modules/base/index', ['jquery', 'modules/base/user', 'modules/base/tree', 'splitter', 'jquery/splitter/0.8.0/jquery.splitter.css'],
function(require, exports, module) {
    var $ = require("jquery");

    require("splitter");
    require("jquery/splitter/0.8.0/jquery.splitter.css");
    var user = require("modules/base/user");
    var tree = require("modules/base/tree");
	
    exports.user = user;
    exports.init = function(opts) {
        var h = $(window).height()- $("#dHead").height() - $("#dFoot").height() - $("#navigation").height();
        $('#dataContainer').height(h).split({
        	orientation:'vertical',
        	limit:180,
        	position:274
        });
        
        tree.show(opts);
        
        //截获窗口大小改变事件，调整中间div的高度
        $(window).resize(function(){
            var h = $(window).height()- $("#dHead").height() - $("#dFoot").height() - $("#navigation").height();
        	$('#dataContainer').height(h);
        });
        
        $("#btn_logout").click(function(){
            user.logout();
        });
        
        $("#user_account").click(function(){
			$(".head_function").removeClass("head_function_check");
			$(this).addClass("head_function_check"); 
        	user.showDetail(__webRoot, opts.listContainer);
        });
    }
});