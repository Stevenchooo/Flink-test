String.prototype.format = function() {
    var args = arguments;
    return this.replace(/\{(\d+)\}/g,               
        function(m,i){
            return args[i];
        }
    );
}

function getLocalReason(code, def) {
	return local_reason[code] ? local_reason[code] : def; 
}

function getLocalTag(name, def) {
	return local_tags[name] ? local_tags[name] : def;
} 
