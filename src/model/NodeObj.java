package model;

public class NodeObj {
	private  String nodeid;
	private  String strcoords;
	private  String crossRoads;
	private  String relatedSections;
	private  String geometry;
	private  String joinpoints;
	
	
	public NodeObj() {
		super();
	}

	public NodeObj(String nodeid) {
		super();
		this.nodeid = nodeid;
	}
	
	public String getNodeid() {
		return nodeid;
	}
	public void setNodeid(String nodeid) {
		this.nodeid = nodeid;
	}
	public String getStrcoords() {
		return strcoords;
	}
	public void setStrcoords(String strcoords) {
		this.strcoords = strcoords;
	}
	public String getCrossRoads() {
		return crossRoads;
	}
	public void setCrossRoads(String crossRoads) {
		this.crossRoads = crossRoads;
	}
	public String getRelatedSections() {
		return relatedSections;
	}
	public void setRelatedSections(String relatedSections) {
		this.relatedSections = relatedSections;
	}
	public String getGeometry() {
		return geometry;
	}
	public void setGeometry(String geometry) {
		this.geometry = geometry;
	}
	public String getJoinpoints() {
		return joinpoints;
	}
	public void setJoinpoints(String joinpoints) {
		this.joinpoints = joinpoints;
	}
	
	
}
