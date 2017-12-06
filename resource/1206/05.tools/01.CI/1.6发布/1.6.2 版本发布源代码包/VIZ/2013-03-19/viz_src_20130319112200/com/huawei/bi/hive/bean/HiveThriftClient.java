package com.huawei.bi.hive.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.hive.metastore.api.Database;
import org.apache.hadoop.hive.metastore.api.FieldSchema;
import org.apache.hadoop.hive.metastore.api.Table;
import org.apache.hadoop.hive.service.HiveClient;
import org.apache.log4j.Logger;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import com.huawei.bi.common.domain.TreeNode;
import com.huawei.bi.util.Util;

public class HiveThriftClient extends AbstractHiveClient {

	public static final Logger logger = Logger
			.getLogger(HiveThriftClient.class); // logger

	String conn;

	public String getConn() {
		return conn;
	}

	public void setConn(String conn) {
		this.conn = conn;
	}

	public HiveThriftClient(String conn) {
		super();
		this.conn = conn;
	}

	@Override
	public TreeNode getServerNode() throws Exception {

		// get host & port from conn
		ConnParam connParam = getConnMap(conn);

		TTransport transport = new TSocket(connParam.host, connParam.port);
		TProtocol protocol = new TBinaryProtocol(transport);
		HiveClient client = new HiveClient(protocol);

		transport.open();

		List<String> dbList = client.get_all_databases();
		
		// server's node
		TreeNode serverNode = new TreeNode();
		serverNode.setName(connParam.host + ":" + connParam.port);
		serverNode.setType(TreeNode.TYPE_SERVER);
		serverNode.setOpen(true);

		List<TreeNode> dbNodeList = new ArrayList<TreeNode>();
		for (String db : dbList) {

			// database's node
			TreeNode dbNode = new TreeNode();
			dbNode.setName(db);
			dbNode.setPname(connParam.host + ":" + connParam.port);
			dbNode.setType(TreeNode.TYPE_DB);

			List<String> tableList = client.get_all_tables(db);
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

		// if json is wanted, following the below
		// String json = new JSONArray(new TreeNode[] { serverNode
		// }).toString();
		transport.close();
		return serverNode;
	}

	@Override
	public String getDbDesc(String name) throws Exception {
		ConnParam connParam = getConnMap(conn);

		TTransport transport = new TSocket(connParam.host, connParam.port);
		TProtocol protocol = new TBinaryProtocol(transport);
		HiveClient client = new HiveClient(protocol);

		transport.open();

		Database db = client.get_database(name);

		StringBuilder sb = new StringBuilder();
		sb.append(db.getDescription());
		sb.append("<br/>");

		sb.append(db.getLocationUri());
		sb.append("<br/>");

		Map<String, String> map = db.getParameters();
		sb.append("parameters:");
		sb.append("<br/>");
		sb.append(Util.printMap(map));
		transport.close();
		return sb.toString();
	}

	@Override
	public String getTableDesc(String dbName, String tableName)
			throws Exception {
		ConnParam connParam = getConnMap(conn);

		TTransport transport = new TSocket(connParam.host, connParam.port);
		TProtocol protocol = new TBinaryProtocol(transport);
		HiveClient client = new HiveClient(protocol);

		transport.open();
		StringBuilder sb = new StringBuilder();

		Table table = client.get_table(dbName, tableName);

		// essambly the description
		sb.append(dbName + "." + tableName);
		sb.append("<br/>");
		sb.append("owner: " + table.getOwner());
		sb.append("<br/>");
		sb.append("create time: " + table.getCreateTime());
		sb.append("<br/>");
		sb.append("table type: " + table.getTableType());
		sb.append("<br/>");

		// fields
		client.execute("desc " + tableName);
		List<String> list = client.fetchAll();
		sb.append("<br/>");
		sb.append("fields:");
		sb.append("<br/>");
		sb.append(Util.printList(list));
		transport.close();
		return sb.toString();
	}

	@Override
	public String getServerDesc() throws Exception {
		ConnParam connParam = getConnMap(conn);

		StringBuilder sb = new StringBuilder();
		sb.append("host: " + connParam.host);
		sb.append("<br/>");
		sb.append("port: " + connParam.port);
		sb.append("<br/>");

		return sb.toString();
	}

	@Override
	public String excuteScriptSilently(String code) throws Exception {
		ConnParam connParam = getConnMap(conn);

		TTransport transport = new TSocket(connParam.host, connParam.port);
		TProtocol protocol = new TBinaryProtocol(transport);
		HiveClient client = new HiveClient(protocol);

		transport.open();

		client.execute(code);

		List<String> list = client.fetchAll();
		transport.close();
		return Util.printList(list);
	}
	
	public String getColomnByTableName(String dbName, String tableName) throws Exception{
		ConnParam connParam = getConnMap(conn);
		
		TTransport transport = new TSocket(connParam.host, connParam.port);
		TProtocol protocol = new TBinaryProtocol(transport);
		HiveClient client = new HiveClient(protocol);
		
		transport.open();
		
		List list = client.get_fields(dbName, tableName);
		
		transport.close();
		StringBuffer sb = new StringBuffer();
		for (Object object : list) {
			FieldSchema field = (FieldSchema)object;
			sb.append(field.getName());
			sb.append("|");
			sb.append(field.getType());
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public static void main(String[] args) throws Exception {
		TTransport transport = new TSocket("10.36.65.179", 10001);
		TProtocol protocol = new TBinaryProtocol(transport);
		HiveClient client = new HiveClient(protocol);
		short s = 10;
		transport.open();
		List list = client.get_fields("default", "test_fengqu");
		transport.close();
		
		StringBuffer sb = new StringBuffer();
		for (Object object : list) {
			FieldSchema field = (FieldSchema)object;
			sb.append(field.getName());
			sb.append("|");
			sb.append(field.getType());
			sb.append("\n");
		}
		System.out.println(sb.toString());
	}

}
