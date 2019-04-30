package com.hisense.adapter.util;

import java.io.InputStream;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JdbcUtil {
	private static final Log log=LogFactory.getLog(PropertyUtil.class);
	private static String url;
	private static String user;
	private static String jdbcUrl;
	private static String password;
	private static String driverClass;
	private static String sql = "select * from user";
	private static Connection conn = null;
	private static Statement stmt = null;
	private static ResultSet rs = null;
	static {
		try {


			InputStream is = JdbcUtil.class.getClassLoader()//
					.getResourceAsStream("config.properties");
			user = PropertyUtil.getInstance().getProperty("user");
			password = PropertyUtil.getInstance().getProperty("password");
			driverClass = PropertyUtil.getInstance().getProperty("driverClass");
			jdbcUrl = PropertyUtil.getInstance().getProperty("jdbcUrl");

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
	}

	public static Connection getOraConnection(String jdbcUrl, String user,
			String password) throws SQLException {
		try {
			Class.forName(driverClass);
			conn = DriverManager.getConnection(jdbcUrl, user, password);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return conn;
	}

	public static Connection getOraConnection() throws SQLException {
		try {
			Class.forName(driverClass);
			conn = DriverManager.getConnection(jdbcUrl, user, password);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		}
		return conn;
	}

	public static void close(Connection conn) throws SQLException {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
				throw e;
			}
		}
	}

	public static void close(Statement stmt) throws SQLException {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
				throw e;
			}
		}
	}

	public static void close(ResultSet rs) throws SQLException {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
				throw e;
			}
		}
	}

	public static void begin(Connection conn) throws SQLException {
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void begin() throws SQLException {
		try {
			Connection conn = getOraConnection();
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void commit(Connection conn) throws SQLException {
		try {
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void commit() throws SQLException {
		try {
			Connection conn = getOraConnection();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			throw e;
		}
	}


	public static void rollback(Connection conn) throws SQLException {
		try {
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void rollback() throws SQLException {
		try {
			Connection conn = getOraConnection();
			conn.rollback();
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void closeConnection(Connection conn) throws SQLException {
		try {
			close(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			throw e;
		}
	}

	public static void closeConnection() throws SQLException {
		try {
			Connection conn = getOraConnection();
			close(conn);
		} catch (SQLException e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
			throw e;
		}
	}
	
    public static Clob stringToClob(String str) {  
        if (null == str)  
            return null;  
        else {  
            try {  
                java.sql.Clob c = new javax.sql.rowset.serial.SerialClob(str  
                        .toCharArray());  
                return c;  
            } catch (Exception e) {  
                return null;  
            }  
        }  
    }  
}
