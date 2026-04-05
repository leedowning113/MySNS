package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.NamingException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import util.ConnectionPool;

public class FeedDAO {
	public boolean insert(String jsonstr) throws NamingException, SQLException, ParseException {
		Connection conn = ConnectionPool.get();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			synchronized (this) {
				String sql = "SELECT no FROM feed ORDER BY no DESC LIMIT 1";
				stmt = conn.prepareStatement(sql);
				rs = stmt.executeQuery();

				int max = (!rs.next()) ? 0 : rs.getInt("no");
				stmt.close();
				rs.close();

				JSONParser parser = new JSONParser();
				JSONObject jsonobj = (JSONObject) parser.parse(jsonstr);
				jsonobj.put("no", max + 1);

				String uid = jsonobj.get("id").toString();
				sql = "SELECT jsonstr FROM user WHERE id = ?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1, uid);
				rs = stmt.executeQuery();
				if (rs.next()) {
					String usrstr = rs.getString("jsonstr");
					JSONObject usrobj = (JSONObject) parser.parse(usrstr);
					usrobj.remove("password");
					usrobj.remove("ts");
					jsonobj.put("user", usrobj);
				}
				stmt.close();
				rs.close();

				sql = "INSERT INTO feed(no, id, jsonstr) VALUES(?, ?, ?)";
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, max + 1);
				stmt.setString(2, uid);
				stmt.setString(3, jsonobj.toJSONString());

				int count = stmt.executeUpdate();
				return count == 1;
			}
		} finally {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		}
	}

	public String getList() throws NamingException, SQLException {
		Connection conn = ConnectionPool.get();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String sql = "SELECT * FROM feed ORDER BY no DESC";
			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			String str = "[";
			int cnt = 0;
			while (rs.next()) {
				if (cnt++ > 0) str += ", ";
				str += rs.getString("jsonstr");
			}
			return str + "]";
		} finally {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		}
	}

	public String getGroup(String frids, String maxNo) throws NamingException, SQLException {
		Connection conn = ConnectionPool.get();
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			String safeFrids = sanitizeIdList(frids);
			Integer safeMaxNo = sanitizeMaxNo(maxNo);

			String sql = "SELECT jsonstr FROM feed WHERE id IN (" + safeFrids + ")";
			if (safeMaxNo != null) {
				sql += " AND no < " + safeMaxNo;
			}
			sql += " ORDER BY no DESC LIMIT 3";

			stmt = conn.prepareStatement(sql);
			rs = stmt.executeQuery();

			String str = "[";
			int cnt = 0;
			while (rs.next()) {
				if (cnt++ > 0) str += ", ";
				str += rs.getString("jsonstr");
			}
			return str + "]";
		} finally {
			if (rs != null) rs.close();
			if (stmt != null) stmt.close();
			if (conn != null) conn.close();
		}
	}

	private String sanitizeIdList(String frids) {
		if (frids == null || !frids.matches("\"[^\"]+\"(,\"[^\"]+\")*")) {
			return "\"\"";
		}
		return frids;
	}

	private Integer sanitizeMaxNo(String maxNo) {
		if (maxNo == null || maxNo.isBlank()) {
			return null;
		}
		return Integer.valueOf(maxNo);
	}
}
