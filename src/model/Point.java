package model;

public class Point {
	private String x;
	private String y;
	private String xyString;
	private String x_y;
	public Point() {
		super();
	}

	public String getX_y() {
		return x_y;
	}

	public void setX_y(String x_y) {
		this.x_y = x_y;
	}

	public Point(String x,String y) {
		super();
		this.x = x;
		this.y = y;
		this.xyString = "["+x+","+y+"]";
		this.x_y = x+","+y;
	}

	public String getX() {
		return x;
	}


	public void setX(String x) {
		this.x = x;
	}


	public String getY() {
		return y;
	}


	public void setY(String y) {
		this.y = y;
	}


	public String getXyString() {
		return xyString;
	}


	public void setXyString(String xyString) {
		this.xyString = xyString;
	}


	
}
