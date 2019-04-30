package com.hisense.adapter.dao;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hisense.adapter.util.JdbcUtil;
import com.hisense.adapter.util.PropertyUtil;

import model.Point;
import model.RoadMV;

public class SecDao {
	private static final Log log = LogFactory.getLog(SecDao.class);
	private PropertyUtil propertyUtil = PropertyUtil.getInstance();
	String user = propertyUtil.getProperty("HiatmpUser");
	String password = propertyUtil.getProperty("HiatmpPassword");
	String jdbcUrl = propertyUtil.getProperty("HiatmpDBUrl");

	String NewTable = propertyUtil.getProperty("NEWTABLENAME");
	Statement stmt = null;
	PreparedStatement pstmt = null;
	Connection conn = null;
	ResultSet rs = null;
	
	public List<RoadMV> getSecMV() {
		List<RoadMV> arrayList = new ArrayList<RoadMV>();
		String sqlStr = "select t.objectid_12 from chengdu_sec t";
		log.info(sqlStr);
		try {
			conn = JdbcUtil.getOraConnection(jdbcUrl, user, password);
			// ‘§±‡“Îsql”Ôæ‰
			PreparedStatement pst = conn.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);

			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlStr);
			if (rs != null) {
				while (rs.next()) {
					RoadMV temp = new RoadMV(rs.getString("objectid_12"));
					arrayList.add(temp);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			try {
				JdbcUtil.close(rs);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
			try {
				JdbcUtil.close(stmt);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
			try {
				JdbcUtil.close(conn);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}

		return arrayList;
	}

	public List<Point> getStrcoods(String gid) {
		List<Point> arrayList = new ArrayList<Point>();
		String sqlStr = "SELECT C.objectid_12, t.id,T.X, T.Y FROM chengdu_sec C, "
				+ "TABLE(SDO_UTIL.GETVERTICES(C.shape)) T  WHERE c.objectid_12 =? ORDER BY t.id";
		log.info(sqlStr);
		try {
			conn = JdbcUtil.getOraConnection(jdbcUrl, user, password);
			// ‘§±‡“Îsql”Ôæ‰
			PreparedStatement pst = conn.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);

			pst = conn.prepareStatement(sqlStr);
			pst.setString(1, gid);
			rs = pst.executeQuery();
			if (rs != null) {
				while (rs.next()) {
					Point temp = new Point(rs.getString("x"), rs.getString("y"));
					arrayList.add(temp);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			try {
				JdbcUtil.close(rs);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
			try {
				JdbcUtil.close(stmt);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
			try {
				JdbcUtil.close(conn);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}
		return arrayList;
	}

	public void saveNewRoad(String gid, String wgs84) {
		List<RoadMV> arrayList = new ArrayList<RoadMV>();
		String sqlStr = "insert into chengdu_sec84 (objectid_12,POSITIONS,GEOMETRY) VALUES('" + gid
				+ "',?,MDSYS.SDO_GEOMETRY(2002," + "                           8307,"
				+ "                           NULL," + "                           MDSYS.SDO_ELEM_INFO_ARRAY(1, 2, 1),"
				+ "                           MDSYS.SDO_ORDINATE_ARRAY(" + wgs84 + ")))";
		log.info("gid:[" + gid + "],±£¥Êsql£∫" + sqlStr);

		try {
			conn = JdbcUtil.getOraConnection(jdbcUrl, user, password);
			conn.setAutoCommit(false);
			// 1.’‚÷÷∑Ω∑®–¥»ÎCLOB◊÷∂Œø…“‘°£
			PreparedStatement stat = conn.prepareStatement(sqlStr);
			StringReader reader = new StringReader(wgs84);
			stat.setCharacterStream(1, reader, wgs84.length());
			stat.executeUpdate();
			conn.setAutoCommit(true);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage(), e);
		} finally {
			try {
				JdbcUtil.close(rs);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
			try {
				JdbcUtil.close(stmt);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
			try {
				JdbcUtil.close(conn);
			} catch (SQLException e) {
				e.printStackTrace();
				log.error(e.getMessage(), e);
			}
		}
	}

}
