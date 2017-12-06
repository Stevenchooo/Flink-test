package com.huawei.bi.hive.bean;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.huawei.bi.common.domain.TreeNode;
import com.huawei.bi.util.Util;

public class HiveCommClient extends AbstractHiveClient {

	String conn;

	public String getConn() {
		return conn;
	}

	public void setConn(String conn) {
		this.conn = conn;
	}

	public HiveCommClient(String conn) {
		super();
		this.conn = conn;
	}

	@Override
	public TreeNode getServerNode() throws Exception {

		List<String> dbList = excuteScriptSilentlyAsList("show databases");

		// server's node
		TreeNode serverNode = new TreeNode();
		serverNode.setName(conn);
		serverNode.setType(TreeNode.TYPE_SERVER);
		serverNode.setOpen(true);

		List<TreeNode> dbNodeList = new ArrayList<TreeNode>();
		for (String db : dbList) {

			// database's node
			TreeNode dbNode = new TreeNode();
			dbNode.setName(db);
			dbNode.setPname(conn);
			dbNode.setType(TreeNode.TYPE_DB);

			List<String> tableList = excuteScriptSilentlyAsList("use " + db
					+ ";show tables");
			List<TreeNode> tableNodeList = new ArrayList<TreeNode>();
			for (String table : tableList) {
				TreeNode node = new TreeNode();
				node.setName(table);
				node.setPname(db);
				node.setType(TreeNode.TYPE_TABLE);
				tableNodeList.add(node);
			}

			// add table's node into database's
			dbNode.setChildren(tableNodeList);

			dbNodeList.add(dbNode);
		}

		// add database's node into server's
		serverNode.setChildren(dbNodeList);

		return serverNode;
	}

	@Override
	public String getDbDesc(String name) throws Exception {
		String code = "describe database " + name;
		return excuteScriptSilently(code);
	}

	@Override
	public String getTableDesc(String dbName, String tableName)
			throws Exception {
		String code = "use " + dbName + ";describe " + tableName;
		return excuteScriptSilently(code);
	}

	@Override
	public String getServerDesc() throws Exception {
		// TODO any goods?
		return conn;
	}

	/**
	 * 
	 * @param conn
	 * @param code
	 * @return
	 * @throws Exception
	 */
	protected List<String> excuteScriptSilentlyAsList(String code)
			throws Exception {
		// TODO get host from conn and exe
		// now, only localhost is supported

		String[] command = new String[] { "hive", "-e", code };
		Process process = Runtime.getRuntime().exec(command);

		InputStream inputStream = process.getInputStream();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inputStream));

		List<String> list = new ArrayList<String>();
		String line = null;
		while ((line = reader.readLine()) != null)
		{
			list.add(line);
		}

		return list;
	}

	@Override
	public String excuteScriptSilently(String code) throws Exception {
		// TODO get host from conn and exe
		// now, only localhost is supported
		List<String> list = excuteScriptSilentlyAsList(code);
		return Util.printList(list);
	}

	@Override
	public String getColomnByTableName(String dbName, String tableName)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
