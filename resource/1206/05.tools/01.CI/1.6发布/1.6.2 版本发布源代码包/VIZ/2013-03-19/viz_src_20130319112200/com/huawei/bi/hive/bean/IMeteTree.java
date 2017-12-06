package com.huawei.bi.hive.bean;

import com.huawei.bi.common.domain.TreeNode;

public interface  IMeteTree {
	
	public TreeNode getMetaDataTree(int connId) throws Exception;

}
