define(function(require, exports, module) {
	exports.dump = function(o) {
		if(o == null) {
			return 'null';
		}
		if(o == undefined) {
			return 'undefined';
		}
        var s = [];
        for(var i in o) {
            s.push(i, "=", o[i], "\n");
        }
        console.info(s.join(''));
    }
});