package com.bjym.mobiledata.db;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.bjym.mobiledata.utils.LoggerUtil;
import com.bjym.mobiledata.utils.PropertiesUtil;
import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DBUtils {
	private static ComboPooledDataSource ds;

	public static synchronized Connection getConnection() {
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException e) {
			LoggerUtil.info("�������ݿ������쳣:" + e.getMessage());
			e.printStackTrace();
		}
		return con;
	}

	public static void close(ResultSet rs) {
		close(rs, null, null);
	}

	public static void close(PreparedStatement pst) {
		close(null, pst, null);
	}

	public static void close(PreparedStatement pst, Connection con) {
		close(null, pst, con);
	}

	public static void close(ResultSet rs, Statement st, Connection con) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (st != null) {
				st.close();
				st = null;
			}
			if (con != null) {
				con.close();
				con = null;
			}
		} catch (Exception e) {
			LoggerUtil.info("�ͷ����ݿ���Դ����:" + e.getMessage());
			e.printStackTrace();
		}
	}

	public static void close(ResultSet rs, PreparedStatement pst, Connection con) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (pst != null) {
				pst.close();
				pst = null;
			}
			if (con != null) {
				con.close();
				con = null;
			}
		} catch (Exception e) {
			LoggerUtil.info("�ͷ����ݿ���Դ����:" + e.getMessage());
			e.printStackTrace();
		}
	}

	static {
		ds = new ComboPooledDataSource();
//		LoggerUtil.info(PropertiesUtil.getParameterC3P0("c3p0.driverClass"));
//		LoggerUtil.info(PropertiesUtil.getParameterC3P0("c3p0.jdbcUrl"));
//		LoggerUtil.info(PropertiesUtil.getParameterC3P0("c3p0.user"));
//		LoggerUtil.info(PropertiesUtil.getParameterC3P0("c3p0.password"));
//		LoggerUtil.info(PropertiesUtil
//				.getParameterC3P0("c3p0.acquireIncrement"));
//		LoggerUtil.info(PropertiesUtil
//				.getParameterC3P0("c3p0.acquireRetryAttempts"));
//		LoggerUtil.info(PropertiesUtil
//				.getParameterC3P0("c3p0.acquireRetryDelay"));
//		LoggerUtil.info(PropertiesUtil
//				.getParameterC3P0("c3p0.autoCommitOnClose"));
//		LoggerUtil.info(PropertiesUtil
//				.getParameterC3P0("c3p0.breakAfterAcquireFailure"));
//		LoggerUtil
//				.info(PropertiesUtil.getParameterC3P0("c3p0.checkoutTimeout"));
//		LoggerUtil.info(PropertiesUtil
//				.getParameterC3P0("c3p0.idleConnectionTestPeriod"));
//		LoggerUtil
//				.info(PropertiesUtil.getParameterC3P0("c3p0.initialPoolSize"));
//		LoggerUtil.info(PropertiesUtil.getParameterC3P0("c3p0.maxIdleTime"));
//		LoggerUtil.info(PropertiesUtil.getParameterC3P0("c3p0.minPoolSize"));
//		LoggerUtil.info(PropertiesUtil.getParameterC3P0("c3p0.maxStatements"));
//		LoggerUtil.info(PropertiesUtil
//				.getParameterC3P0("c3p0.maxStatementsPerConnection"));
//		LoggerUtil.info(PropertiesUtil
//				.getParameterC3P0("c3p0.numHelperThreads"));
//		LoggerUtil.info(PropertiesUtil
//				.getParameterC3P0("c3p0.preferredTestQuery"));
//		LoggerUtil.info(PropertiesUtil.getParameterC3P0("c3p0.propertyCycle"));
//		LoggerUtil.info(PropertiesUtil
//				.getParameterC3P0("c3p0.testConnectionOnCheckout"));
//		LoggerUtil.info(PropertiesUtil
//				.getParameterC3P0("c3p0.testConnectionOnCheckin"));
		try {
			ds.setDriverClass(PropertiesUtil
					.getParameterC3P0("c3p0.driverClass"));
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		ds.setJdbcUrl(PropertiesUtil.getParameterC3P0("c3p0.jdbcUrl"));
		ds.setUser(PropertiesUtil.getParameterC3P0("c3p0.user"));
		ds.setPassword(PropertiesUtil.getParameterC3P0("c3p0.password"));
		ds.setAcquireIncrement(Integer.parseInt(PropertiesUtil
				.getParameterC3P0("c3p0.acquireIncrement")));
		ds.setAcquireRetryAttempts(Integer.parseInt(PropertiesUtil
				.getParameterC3P0("c3p0.acquireRetryAttempts")));
		ds.setAcquireRetryDelay(Integer.parseInt(PropertiesUtil
				.getParameterC3P0("c3p0.acquireRetryDelay")));
		ds.setAutoCommitOnClose(Boolean.parseBoolean(PropertiesUtil
				.getParameterC3P0("c3p0.autoCommitOnClose")));
		ds.setBreakAfterAcquireFailure(Boolean.parseBoolean(PropertiesUtil
				.getParameterC3P0("c3p0.breakAfterAcquireFailure")));
		ds.setCheckoutTimeout(Integer.parseInt(PropertiesUtil
				.getParameterC3P0("c3p0.checkoutTimeout")));
		ds.setIdleConnectionTestPeriod(Integer.parseInt(PropertiesUtil
				.getParameterC3P0("c3p0.idleConnectionTestPeriod")));
		ds.setInitialPoolSize(Integer.parseInt(PropertiesUtil
				.getParameterC3P0("c3p0.initialPoolSize")));
		ds.setMaxIdleTime(Integer.parseInt(PropertiesUtil
				.getParameterC3P0("c3p0.maxIdleTime")));
		ds.setMaxPoolSize(Integer.parseInt(PropertiesUtil
				.getParameterC3P0("c3p0.maxPoolSize")));
		ds.setMinPoolSize(Integer.parseInt(PropertiesUtil
				.getParameterC3P0("c3p0.minPoolSize")));
		ds.setMaxStatements(Integer.parseInt(PropertiesUtil
				.getParameterC3P0("c3p0.maxStatements")));
		ds.setMaxStatementsPerConnection(Integer.parseInt(PropertiesUtil
				.getParameterC3P0("c3p0.maxStatementsPerConnection")));
		ds.setNumHelperThreads(Integer.parseInt(PropertiesUtil
				.getParameterC3P0("c3p0.numHelperThreads")));
		ds.setPreferredTestQuery(PropertiesUtil
				.getParameterC3P0("c3p0.preferredTestQuery"));
		ds.setPropertyCycle(Integer.parseInt(PropertiesUtil
				.getParameterC3P0("c3p0.propertyCycle")));
		ds.setTestConnectionOnCheckout(Boolean.parseBoolean(PropertiesUtil
				.getParameterC3P0("c3p0.testConnectionOnCheckout")));
		ds.setTestConnectionOnCheckin(Boolean.parseBoolean(PropertiesUtil
				.getParameterC3P0("c3p0.testConnectionOnCheckin")));
	}
}
