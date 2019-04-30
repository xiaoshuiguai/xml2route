package model;

public class ArcObj {
	private  String arcid;
	private  String arcname;
	private  String arclength;
	private  String startnode;
	private  String endnode;
	private  String strcoords;
	private  String roadcode;
	private  String geometry;
	private  String trafficdir;
	private  String arctype;
	
	
	public String getArctype() {
		return arctype;
	}


	public void setArctype(String arctype) {
		this.arctype = arctype;
	}


	public ArcObj() {
		super();
	}


	public ArcObj(String arcid) {
		super();
		this.arcid = arcid;
	}
	
	
	public String getArcid() {
		return arcid;
	}
	public void setArcid(String arcid) {
		this.arcid = arcid;
	}
	public String getArcname() {
		return arcname;
	}
	public void setArcname(String arcname) {
		this.arcname = arcname;
	}
	public String getArclength() {
		return arclength;
	}
	public void setArclength(String arclength) {
		this.arclength = arclength;
	}
	public String getStartnode() {
		return startnode;
	}
	public void setStartnode(String startnode) {
		this.startnode = startnode;
	}
	public String getEndnode() {
		return endnode;
	}
	public void setEndnode(String endnode) {
		this.endnode = endnode;
	}
	public String getStrcoords() {
		return strcoords;
	}
	public void setStrcoords(String strcoords) {
		this.strcoords = strcoords;
	}
	public String getRoadcode() {
		return roadcode;
	}
	public void setRoadcode(String roadcode) {
		this.roadcode = roadcode;
	}
	public String getGeometry() {
		return geometry;
	}
	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}
	public String getTrafficdir() {
		return trafficdir;
	}
	public void setTrafficdir(String trafficdir) {
		this.trafficdir = trafficdir;
	}
	
	
}
