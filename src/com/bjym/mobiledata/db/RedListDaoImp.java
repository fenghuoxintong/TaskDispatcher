package com.bjym.mobiledata.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RedListDaoImp {
	public boolean find(String phone) {
		String query_sql = "select * from redlist where mobileno = ?";

		Connection conn = DBUtils.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(query_sql);
			pst.setString(1, phone);
			rs = pst.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtils.close(rs, pst, conn);
		}
		return false;
	}

}
