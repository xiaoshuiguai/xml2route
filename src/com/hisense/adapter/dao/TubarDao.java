package com.hisense.adapter.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hisense.adapter.util.GISUtil;
import com.hisense.adapter.util.JdbcUtil;
import com.hisense.adapter.util.PropertyUtil;

import model.ArcObj;
import model.NodeObj;
import model.Point;
import model.RoadMV;

public class TubarDao {
	private static final Log log = LogFactory.getLog(TubarDao.class);
	private PropertyUtil propertyUtil = PropertyUtil.getInstance();
	String user = propertyUtil.getProperty("HiatmpUser");
	String password = propertyUtil.getProperty("HiatmpPassword");
	String jdbcUrl = propertyUtil.getProperty("HiatmpDBUrl");

	private static Double BUFFERSIZE = 0d;
	private static Double MERGELENGTH = 0d;
	private static Double CROSSSIZE = 0d;

	Statement stmt = null;
	PreparedStatement pstmt = null;
	Connection conn = null;
	ResultSet rs = null;

	public List<RoadMV> getAllRoadFromTUBAR() {
		List<RoadMV> arrayList = new ArrayList<RoadMV>();
		String sqlStr = propertyUtil.getProperty("GET_TUBAR_SQL");
		log.info(sqlStr);
		try {
			conn = JdbcUtil.getOraConnection(jdbcUrl, user, password);
			// 预编译sql语句
			PreparedStatement pst = conn.prepareStatement(sqlStr, Statement.RETURN_GENERATED_KEYS);

			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlStr);
			// TODO 实例化对象
			if (rs != null) {
				while (rs.next()) {
					RoadMV temp = new RoadMV(rs.getString("gid"));
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

	public void saveNewRoad(RoadMV roadmv, String wgs84) {
		try {
			roadmv.setStrcoords(wgs84.replaceAll(" ", ""));// 生成线坐标
			StringBuffer buff = new StringBuffer("");
			List list;

			// 自动断链,生成弧段列表
			List<ArcObj> arclist = this.genArcsByTUBARLink(roadmv);
			log.info("size:" + arclist.size());

			// 循环弧段列表，生成弧段、节点等数据
			// List<Record> L;
			for (ArcObj arc : arclist) {
				Double alen = GISUtil.getRoadLength(arc.getStrcoords());
				if (alen < MERGELENGTH) {
					// continue;
				}
				buff.delete(0, buff.toString().length());
				buff.append("select r.arcid from route_arc r where to_char(r.strcoords)='").append(arc.getStrcoords())
						.append("'");
				list = Db.find(buff.toString());

				if (list != null && list.size() > 0) {
					continue;
				}
				// 连接点、节点
				// 获取该LINK的开始点，然后更新连接点信息（连接点中存有连接的所有link信息）
				String startpoint = GISUtil.getStartNode(arc.getStrcoords());
				modifyRouteJoinPointTubar(startpoint, arc.getArcid(), arc.getArcname());// 更新连接点
				NodeObj startnode = modifyRouteNode(startpoint);// 更新节点

				String endpoint = GISUtil.getEndNode(arc.getStrcoords());
				modifyRouteJoinPointTubar(endpoint, arc.getArcid(), arc.getArcname());// 更新连接点
				NodeObj endnode = modifyRouteNode(endpoint);// 更新节点

				// 弧段
				arc.setStartnode(startnode.getNodeid());
				arc.setEndnode(endnode.getNodeid());
				if (arc.getStartnode().equalsIgnoreCase(arc.getEndnode())) {
					arc.setArctype("1");
				} else {
					arc.setArctype("0");
				}
				buff.delete(0, buff.toString().length());
				buff.append(
						"insert into route_arc(arcid,arclength,startnode,endnode,strcoords,roadcode,traffic_dir,arcname,geometry) values('")
						.append(arc.getArcid()).append("',").append(arc.getArclength()).append(",'")
						.append(arc.getStartnode()).append("','").append(arc.getEndnode()).append("','")
						.append(arc.getStrcoords()).append("','").append(arc.getRoadcode()).append("','")
						.append(arc.getTrafficdir()).append("','").append(arc.getArcname()).append("',")
						.append("MDSYS.SDO_GEOMETRY(2002,8307,null,MDSYS.SDO_ELEM_INFO_ARRAY(1,2,1),MDSYS.SDO_ORDINATE_ARRAY(")
						.append(arc.getStrcoords()).append(")))");
				Db.update(buff.toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private NodeObj modifyRouteNode(String startpoint) {
		// TODO Auto-generated method stub
		return null;
	}

	private void modifyRouteJoinPointTubar(String startpoint, String arcid, String arcname) {
		// TODO Auto-generated method stub

	}

	private List<ArcObj> genArcsByTUBARLink(RoadMV roadmv) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Point> getStrcoods(String gid) {
		// TODO Auto-generated method stub
		return null;
	}

}
