package com.huawei.bi.hive.da;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.huawei.bi.hive.domain.Script;
import com.huawei.bi.util.DBUtil;

public class ScriptDA {

	public void update(Script script) throws ClassNotFoundException,
			SQLException {

		String sqlUpdate = "update script set script=? where resourceId=?";

		Connection conn = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;

		try {
			conn = DBUtil.getConnection();

			// insert
			pstat = conn.prepareStatement(sqlUpdate);
			pstat.setString(1, script.getText());
			pstat.setInt(2, script.getResourceId());

			pstat.executeUpdate();
		} finally {
			DBUtil.close(rs, pstat, conn);
		}
	}

	public Script get(int resourceId) throws ClassNotFoundException,
			SQLException {
		Script result = null;

		String sqlUpdate = "select * from script where resourceId=?";

		Connection conn = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;

		try {
			conn = DBUtil.getConnection();

			// query
			pstat = conn.prepareStatement(sqlUpdate);
			pstat.setInt(1, resourceId);

			rs = pstat.executeQuery();
			if (rs.next()) {
				result = new Script();
				result.setResourceId(rs.getInt("resourceId"));
				result.setText(rs.getString("script"));
			}
		} finally {
			DBUtil.close(rs, pstat, conn);
		}

		return result;
	}

	public int add(Script script) throws ClassNotFoundException, SQLException {

		int result = 0;
		String sqlInsert = "INSERT INTO script (resourceId,script) VALUES (?,?)";

		Connection conn = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;

		try {
			conn = DBUtil.getConnection();

			// insert
			pstat = conn.prepareStatement(sqlInsert,
					Statement.RETURN_GENERATED_KEYS);
			pstat.setInt(1, script.getResourceId());
			pstat.setString(2, script.getText());

			pstat.executeUpdate();
		} finally {
			DBUtil.close(rs, pstat, conn);
		}

		return result;
	}

	public void delete(int scriptId) throws ClassNotFoundException, SQLException {
		String sql = "delete FROM script WHERE resourceId = ?";

		Connection conn = null;
		PreparedStatement pstat = null;
		ResultSet rs = null;

		try {
			conn = DBUtil.getConnection();
			pstat = conn.prepareStatement(sql);

			pstat.setInt(1, scriptId);
			
			pstat.executeUpdate();
		} finally {
			DBUtil.close(rs, pstat, conn);
		}

	}

}
