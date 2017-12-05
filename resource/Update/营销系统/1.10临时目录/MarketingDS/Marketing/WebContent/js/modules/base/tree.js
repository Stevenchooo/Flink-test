define("modules/base/tree", ['jquery', 'tree', 'jquery/tree/3.5/zTreeStyle.css'], 
function(require, exports, module) {
    require("jquery");

    var mTreeOpts = {};
    var mMetas = null; // 元模型
	
    require("tree");
	require('jquery/tree/3.5/zTreeStyle.css');
	var mTree = null;
	
	exports.getTreeOpts = function() {
		return mTreeOpts;
	};
	
	exports.getMetas = function() {
		return mMetas;
	};
	
	exports.getMeta = function(name) {
		return mMetas[name];
	};
	
	exports.show = function(opts) {
		mTreeOpts = opts;

        if(mMetas == null) {
        	mMetas = {};
	        // 获得元模型
	        $.ajax({type:"GET", url: __webRoot+"/api/meta/query", async:false, 
	            success: function(res) {
	                if(res.resultCode != 0) {
	                    return;
	                }
	        		var m, len = res.metas.length;
	        		
	        		for(var i = 0; i < len; i++) {
	        			m = res.metas[i];
	        			mMetas[m.name] = m;
	        		}
	        		
	        		var subs;
	        		for(var i = 0 in mMetas) {
	        			m = mMetas[i];
	        			if(m.subMetas && m.subMetas.length > 0) { 
	        				subs = m.subMetas.substr(1, m.subMetas.length - 2).split('|');//第一个、最后一个是'|'
	        				m.subMetas = [];
	        				
		        			for(var j = 0, num=subs.length; j < num; j++) {
		        				if(mMetas[subs[j]]) {
			        				m.subMetas.push(mMetas[subs[j]]);
		        				}
		        			}
	        			}
	        		}
	            }
	        });
        }
		
        var url = __webRoot+"/api/admin/models?__userKey="+__userKey;
        if(opts.async) {
        	url += "async=1";
        }
        
		//获得当前管理的项
        $.ajax({type:"GET", url: url, 
            success: function(res) {
                if(res.resultCode != 0) {
                    return;
                }
        		var zNodes = [];
        		var icon = null;
        		var m, len = res.models.length;
        		
        		for(var i = 0; i < len; i++) {
        			m = res.models[i];
        			if(mMetas[m.meta] && mMetas[m.meta].icon) {
        				icon = __webRoot + mMetas[m.meta].icon; 
        			} else {
        				icon = null;
        			}
        			zNodes.push({
        				id:m.id,
        				pid:m.pid,
        				name:getLocalTag("menu." + m.name, m.name),
        				isParent:m.isParent,
        				meta:m.meta,
        				icon:icon
        			});
        		}
                
        		var settings = {
    				view: {
    					dblClickExpand: false,
    					showLine: false
    				},
    				
    				data: {
    					simpleData: {
    						enable: true,
			                idKey: "id",  
			                pIdKey: "pid"
    					}
    				},
    				callback: {
    					onClick:onZtreeClick
    				}
    			};
        		
        		if(opts.async) {
        			$.extend(settings, {
        				async: { //延迟加载，点击"+"后才会下载下层的数据
        					enable: true,
        					type: "get",
        					url: __webRoot + "/api/model/querySub?__userKey="+__userKey,
        					autoParam: ["id"],
        					otherParam: {"visible":"true"},
        					dataFilter: ztreeDataFilter
        				}
        			});
        		}

        		mTree = $.fn.zTree.init($("#" + mTreeOpts.treeContainer), settings, zNodes);
        		var nodes = mTree.getNodes();
        		mTree.expandNode(nodes[0], true); //默认展开第一个节点
                var childNodes = mTree.transformToArray(nodes[0]);
                if(childNodes != null && childNodes.length > 0) {
                	node = childNodes[1];
	                if(node) {
		                mTree.expandNode(node, true); 
		                mTree.selectNode(node);
		                var meta = mMetas[node.meta];
		                if(meta.url) { //如果无url，则显示欢迎页
		                	showListPage(node.id, meta.name, meta.url);
		                }
	                }
                }
            }
        });
	};

	//树上节点被点击
    function onZtreeClick(event, treeId, treeNode, clickFlag) {
    	var node = treeNode;
    	var str = '';
    	while(node != null) {
        	if(node.name == '/') {
        		str = node.name +  str;
        	} else {
        		str = node.name + '/' +  str;
        	}
    		node = node.getParentNode();
    	}
    	
    	//生成顶部的路径
    	$(mTreeOpts.naviBar).text(str);
    	
    	if(treeNode.isParent) {
    		mTree.expandNode(treeNode, true); 
    	}
    	
    	var parentMeta = mMetas[treeNode.meta];
    	//添加管理员时会用到此meta，选择可以管理的meta
    	mTreeOpts.curMeta = parentMeta;
    	if(!parentMeta) {
    		return true;
    	}
    	
    	//生成顶部的菜单栏
		if(!parentMeta.subMetas || parentMeta.subMetas.length <= 0) {
			$(mTreeOpts.featureBar).html(''); //右上角菜单消失
			if(parentMeta.url) {
				showListPage(treeNode.id, parentMeta.name, parentMeta.url); //没有下层数据，直接显示本层的数据
			}
			return true;
		}
		
		var subs = parentMeta.subMetas;
		var validSubMetas = [];
		var meta;
		
		str = [];
		for(var i = 0; i < subs.length; i++) {
			meta = subs[i];
			//无权限或不可见，不显示在顶部
			if(!meta.isVisible || checkRight(meta.name, treeNode.id) == '') {
				continue;
			}
			
			validSubMetas.push(meta);
			str.push('&nbsp;&nbsp;|&nbsp;&nbsp;<span class="head_function" id="id_meta_',
					 meta.name, '">', 
					 getLocalTag("meta." + meta.name, meta.desc),
					 '</span>');
		}

		if(validSubMetas.length > 0) {
			str.push('&nbsp;|');
	    	$(mTreeOpts.featureBar).html(str.join(''));
	    	
	    	//绑定事件，最好用on，放在页面已生成后
	    	for(var i = 0; i < validSubMetas.length; i++) {
	    		meta = validSubMetas[i];
				$("#id_meta_" + meta.name).on('click', {meta:meta.name, nodeId:treeNode.id}, function(e) {
					$(".head_function").removeClass("head_function_check");
					$(this).addClass("head_function_check"); 
					showListPage(e.data.nodeId, e.data.meta, mMetas[e.data.meta].url);
				});
			}
    	
	    	meta = validSubMetas[0]; //如果右上角有菜单，则显示右上角菜单的内容
    	} else {
	    	$(mTreeOpts.featureBar).html('');
	    	
        	var childNodes;
    		//如果右上角没有，但是有子菜单，则显示第一个子菜单的内容，并选中
	    	if(treeNode.isParent && (childNodes = mTree.transformToArray(treeNode)) != null) {
				node = childNodes[1];
				if(node) {
					mTree.selectNode(childNodes[1]); 
					meta = mMetas[node.meta];
				}
	    	} else {
	    		meta = mMetas[treeNode.meta];
	    	}
    	}
		
		$(".head_function").removeClass("head_function_check");
    	$("#id_meta_" + meta.name).addClass("head_function_check");
    	showListPage(treeNode.id, meta.name, meta.url);
    	
    	return true;
    }
    
    function checkRight(meta, pid) {
    	var oprRight = '';
    	$.ajax({
  		  type:"GET",
  		  url: __webRoot + "/api/admin/checkRight",
  		  async:false,
  		  data:{
  			  pid:pid,
  			  meta:meta,
  			  __userKey:__userKey
  	      },
  		  success:function(resp){
              if(resp.resultCode != 0) {
                  return;
              }
              oprRight = resp.userRight;
  		  }    		
    	});
    	return oprRight;
    }
    
	function ztreeDataFilter(treeId, parentNode, res) {
        if(res.resultCode != 0) {
            return;
        }
		var zNodes = [];
		var m, len = res.models.length;
		var icon, name;
		var meta;
		
		for(var i = 0; i < len; i++) {
			m = res.models[i];
			meta = mMetas[m.meta];
			icon = meta.icon;
			if(icon != '') {
				m['icon'] = __webRoot + icon; 
			}
			name = m['name']; 
			m['name'] = getLocalTag(name, name); //将标签换为本地语言
			zNodes.push(m);
		}
        return zNodes;
    }
	
    function showListPage(id, meta, url) {
    	console.info(id);
    	$("#" + mTreeOpts.listContainer).html('');
	    $.get(__webRoot + url, {id:id, meta:meta, __userKey:__userKey}, function(data){
	        $("#" + mTreeOpts.listContainer).html(data);
	    });
	}
//end of define
});