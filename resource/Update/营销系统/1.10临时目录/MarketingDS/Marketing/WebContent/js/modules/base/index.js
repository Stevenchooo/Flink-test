define('modules/base/index', ['jquery', 'modules/base/tree', 'splitter', 'jquery/splitter/0.8.0/jquery.splitter.css'],
function(require, exports, module) {
    require("jquery");

    require("splitter");
    require("jquery/splitter/0.8.0/jquery.splitter.css");
    var tree = require("modules/base/tree");
	
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
        
    }
});