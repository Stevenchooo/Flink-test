package com.huawei.bi.hive.bean;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.huawei.bi.common.domain.TreeNode;
import com.huawei.bi.util.Constant;
import com.huawei.bi.util.DBUtil;
import com.huawei.termcloud.uniaccount.crypt.CryptUtil;

public class JDBCClient implements IHiveClient {
	private String driver;
	private String url;
	private String user;
	private String password;

	public JDBCClient(String driver, String url, String user, String password) {
		super();
		this.driver = driver;
		this.url = url;
		this.user = user;
		//解密密码
		this.password = CryptUtil.decryptForAESStr(password, Constant.KET_WORD);
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
    }

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public TreeNode getServerNode() throws Exception {
		Connection conn = null;

		// server's node
		TreeNode serverNode = new TreeNode();
		// TODO substring the server name and port
		serverNode.setName("Root");
		serverNode.setType(TreeNode.TYPE_SERVER);
		serverNode.setOpen(true);

		try {
			Class.forName(driver);

			conn = DriverManager.getConnection(url, user, password);

			DatabaseMetaData dbMeta = conn.getMetaData();
			ResultSet catalogRs = dbMeta.getCatalogs();

			List<String> dbNameList = new ArrayList<String>();
			while (catalogRs.next()) {
				String dbName = catalogRs.getString(1);
				dbNameList.add(dbName);
			}

			List<TreeNode> dbNodeList = new ArrayList<TreeNode>();
			// loop the db
			for (String dbName : dbNameList) {

				// database's node
				TreeNode dbNode = new TreeNode();
				dbNode.setName(dbName);
				dbNode.setPname("Root");
				dbNode.setType(TreeNode.TYPE_DB);

				List<TreeNode> tableNodeList = new ArrayList<TreeNode>();
				ResultSet tableRs = dbMeta.getTables(dbName, null, "%", null);
				while (tableRs.next()) {
					String tableName = tableRs.getString(3);
					String tableType = tableRs.getString(4);
					String tableRmk = tableRs.getString(5);

					TreeNode node = new TreeNode();
					node.setName(tableName);
					node.setPname(dbName);
					node.setType(TreeNode.TYPE_TABLE);
					tableNodeList.add(node);
				}

				dbNode.setChildren(tableNodeList);
				dbNodeList.add(dbNode);
			}

			serverNode.setChildren(dbNodeList);

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			DBUtil.close(null, null, conn);
		}

		return serverNode;
	}

	@Override
	public String getDbDesc(String name) throws Exception {
		// TODO return sth?
		return "";
	}

	@Override
	public String getTableDesc(String dbName, String tableName)
			throws Exception {
		Connection conn = null;
		StringBuffer sb = new StringBuffer();
		sb.append("<table style='border:0;padding:1px'>");
		sb.append("<tr>");
		sb.append("<td>name</td><td>data type</td><td>size</td><td>nullable</td><td>remarks</td><td>auto incre</td>");
		sb.append("</tr>");

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);

			DatabaseMetaData dbMeta = conn.getMetaData();

			ResultSet colRs = dbMeta.getColumns(dbName, null, tableName, "%");
			while (colRs.next()) {
				sb.append("<tr>");

				sb.append("<td>");
				sb.append(colRs.getString("COLUMN_NAME"));
				sb.append("</td>");

				sb.append("<td>");
				sb.append(colRs.getString("TYPE_NAME"));
				sb.append("</td>");

				sb.append("<td>");
				sb.append(colRs.getString("COLUMN_SIZE"));
				sb.append("</td>");

				sb.append("<td>");
				sb.append(colRs.getString("IS_NULLABLE"));
				sb.append("</td>");

				sb.append("<td>");
				sb.append(colRs.getString("REMARKS"));
				sb.append("</td>");

				sb.append("<td>");
				sb.append(colRs.getString("IS_AUTOINCREMENT"));
				sb.append("</td>");

				sb.append("</tr>");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			sb.append("</table>");
			DBUtil.close(null, null, conn);
		}

		return sb.toString();
	}

	@Override
	public String getServerDesc() throws Exception {
		// TODO return something?
		return "";
	}

	@Override
	public String excuteScriptSilently(String code) throws Exception {
		Connection conn = null;
		StringBuffer sb = new StringBuffer();
		sb.append("<table style='padding: 0;'>");

		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(url, user, password);

			Statement stat = conn.createStatement();
			ResultSet rs = stat.executeQuery(code);

			int colSize = rs.getMetaData().getColumnCount();

			// TODO the column head, not work ;(
			int row = 0;
			while (rs.next()) {

				// if (row == 0) {
				// sb.append("<tr>");
				// for (int i = 1; i <= colSize; i++) {
				// sb.append("<td>");
				// sb.append(rs.getString(rs.getMetaData().getco));
				// sb.append("</td>");
				// }
				// sb.append("<tr>");
				//
				// row++;
				// }

				sb.append("<tr>");
				for (int i = 1; i <= colSize; i++) {
					sb.append("<td style='border: 1px solid black'>");
					sb.append(rs.getString(i));
					sb.append("</td>");
				}
				sb.append("<tr>");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			sb.append("</table>");
			DBUtil.close(null, null, conn);
		}

		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		// String r = new JDBCClient("com.mysql.jdbc.Driver",
		// "jdbc:mysql://localhost/viz", "root", "root").getTableDesc(
		// "viz", "test1");
		// System.out.println(r);

		String tab = new JDBCClient("com.mysql.jdbc.Driver",
				"jdbc:mysql://localhost/viz", "root", "root")
				.excuteScriptSilently("select * from task");
		System.out.println(tab);
	}

	@Override
	public String getColomnByTableName(String dbName, String tableName)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
