package com.huawei.bi.hive.bean;

import com.huawei.bi.common.domain.TreeNode;

/**
 * 
 * TODO rename
 * 
 * @author jim
 *
 */
public interface IHiveClient {

	/**
	 * @param conn
	 * @return
	 * @throws Exception
	 */
	public TreeNode getServerNode() throws Exception;

	/**
	 * 
	 * @param conn
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public String getDbDesc(String name) throws Exception;

	/**
	 * 
	 * @param conn
	 * @param dbName
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public String getTableDesc( String dbName, String tableName)
			throws Exception;

	/**
	 * 
	 * @param type
	 * @param conn
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public String getServerDesc() throws Exception;

	/**
	 * 
	 * @param conn
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public String excuteScriptSilently( String code)
			throws Exception;
	
	public String getColomnByTableName(String dbName,String tableName) throws Exception;

}
