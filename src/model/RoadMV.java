package model;

public class RoadMV {
	private String gid;
	private String pathname;
	private String direction;
	private String strcoords;

	
	public RoadMV() {
		super();
	}


	public RoadMV(String gid) {
		super();
		this.gid = gid;
	}
	

	public String getGid() {
		return gid;
	}

	public void setGid(String gid) {
		this.gid = gid;
	}

	public String getPathname() {
		return pathname;
	}


	public void setPathname(String pathname) {
		this.pathname = pathname;
	}


	public String getDirection() {
		return direction;
	}


	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getStrcoords() {
		return strcoords;
	}


	public void setStrcoords(String strcoords) {
		this.strcoords = strcoords;
	}



	
	
}
