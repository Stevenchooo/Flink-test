package com.huawei.bi.common.bean;

import com.huawei.bi.common.da.ResourceDA;
import com.huawei.bi.common.domain.TreeNode;

public class ResourceBean {

	ResourceDA resourceDA = new ResourceDA();

	/**
	 * get the resource root
	 * 
	 * @return
	 * @throws Exception
	 */
	public TreeNode getResourceRoot(String nodeType, String user) throws Exception {

		return resourceDA.getResourceRoot(nodeType, user);
	}
}
