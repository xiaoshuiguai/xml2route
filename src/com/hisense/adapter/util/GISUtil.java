package com.hisense.adapter.util;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.PrecisionModel;

public class GISUtil {
	 private static double _C_P = 0.0174532925199432957692222222222;
	    private static GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(),8307);

	    /**
	     * 计算两个坐标点之间的距离
	     *
	     * @param fromx
	     * @param fromy
	     * @param otherX
	     * @param otherY
	     * @return
	     */
	    public static double dist(double fromx, double fromy, double otherX, double otherY) {

	        double dlon = (otherX - fromx) * _C_P;
	        double dlat = (otherY - fromy) * _C_P;
	        double a = Math.sin(0.5 * dlat) * Math.sin(0.5 * dlat) + Math.cos(fromy * _C_P) * Math.cos(otherY * _C_P) * (Math.sin(0.5 * dlon) * Math.sin(0.5 * dlon));
	        a = Math.abs(a);
	        if (a > 1) {
	            //alert("不合法数据:" + "a:" + a + ",P1:" + p1.toString() + ",P2:" + p2.toString());
	        }
	        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	        double d = c * 6371008.77141506;
	        //System.out.println("distance:"+this.getDistance()+"---"+d);
	        return d;
//	        return 0;
	        //return Math.sqrt(Math.pow(this.x - otherX, 2) + Math.pow(this.y - otherY, 2));
	    }

	    public static double dist(String from,String to) {
	        double fromx = Double.parseDouble(from.split(",")[0]);
	        double fromy = Double.parseDouble(from.split(",")[1]);

	        double otherX = Double.parseDouble(to.split(",")[0]);
	        double otherY = Double.parseDouble(to.split(",")[1]);
	        return GISUtil.dist(fromx, fromy, otherX, otherY);

	    }
	    
	    /**
	     * 地理距离转平面距离
	     * @param strcoords 基准坐标点
	     * @param distance 地理距离，单位米
	     * @return 平面距离
	     */
	    public static double getRectDistance(String strcoords,Double distance) {
	    	//@todo 
	    	Double lon = Double.parseDouble(strcoords.split(",")[0]);
	    	Double lat = Double.parseDouble(strcoords.split(",")[1]);
	    	Double dMeter1 = dist(lon,lat,lon+1,lat);
	    	return distance/dMeter1;
	    }
	    
	    /**
	     * 生成oracle 空间字段
	     * @param strcoords 坐标串
	     * @param geomtype 几何类型 "point" "line" "polygon"
	     * @return
	     */
		public static String genGeomStr(String strcoords,String geomtype){
			String result = "";
			if(geomtype.equalsIgnoreCase("line")){
				result = "MDSYS.SDO_GEOMETRY(2002, 8307,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,2,1),MDSYS.SDO_ORDINATE_ARRAY("+strcoords+"))";
			}else if(geomtype.equalsIgnoreCase("point")){
				result = "mdsys.sdo_geometry(2001,8307,MDSYS.SDO_POINT_TYPE("+strcoords+",0),null,null)";
			}else if(geomtype.equalsIgnoreCase("polygon")){
				String[] strcoordarr = strcoords.split(",");
				if(strcoordarr.length<=4){
					result =  "";
				}else if(!(strcoordarr[0]+","+strcoordarr[1]).equalsIgnoreCase(strcoordarr[strcoordarr.length-2]+","+strcoordarr[strcoordarr.length-1])){
					strcoords = strcoords+","+strcoordarr[0]+","+strcoordarr[1];
				}
				result = "MDSYS.SDO_GEOMETRY(2003, 8307,NULL,MDSYS.SDO_ELEM_INFO_ARRAY(1,2,1),MDSYS.SDO_ORDINATE_ARRAY("+strcoords+"))";
			}
			return result;
		}
	    
	    /**
	     * 计算线的起点坐标
	     * @param strcoords 线坐标
	     * @return 起点坐标
	     */
	    public static String getStartNode(String strcoords) {
	    	String[] strArr = strcoords.split(",");
	    	return strArr[0].replaceAll(" ", "")+","+strArr[1].replaceAll(" ", "");
	    }
	    
	    /**
	     * 计算线的终点坐标
	     * @param strcoords 线坐标
	     * @return 终点坐标
	     */
	    public static String getEndNode(String strcoords) {
	    	String[] strArr = strcoords.split(",");
	    	return strArr[strArr.length-2]+","+strArr[strArr.length-1];
	    }
	    
	    public static LineString genLineString(String coordinates){
	    	String[] strArr = coordinates.split(",");
	        int pointnum = strArr.length / 2;
	        Coordinate[] crdArr = new Coordinate[pointnum];
	        for (int i = 0; i < pointnum; i++) {
	        	crdArr[i] = new Coordinate(Double.parseDouble(strArr[i*2]),Double.parseDouble(strArr[i*2+1]));
	        }
	        LineString g = geometryFactory.createLineString(crdArr);
	    	return g;
	    }
	    
	    public static String getLineStrcoords(Geometry lineString){
	    	Coordinate[] crdArr = lineString.getCoordinates();
	    	String strcoord = "";
	    	for(Coordinate crd:crdArr){
	    		strcoord+=crd.x+","+crd.y+",";
	    	}
	    	if(strcoord.length()>1){
	    		strcoord =  strcoord.substring(0,strcoord.length()-1);
	    	}
	    	return strcoord;
	    	
	    }
	    
	    /**
	     * 计算多个点的质心
	     * @param points
	     * @return
	     */
	    public static String genCentroid(String[] points){
	    	Point[] geomArr = new Point[points.length];
	    	for(int i=0;i<points.length;i++){
	    		String pointstr = points[i];
	    		geomArr[i] = geometryFactory.createPoint(new Coordinate(Double.parseDouble(pointstr.split(",")[0]),Double.parseDouble(pointstr.split(",")[1])));
	    	}
	    	Point centroid = geometryFactory.createMultiPoint(geomArr).getCentroid();
	    	return Double.toString(centroid.getX())+","+Double.toString(centroid.getY());
	    }
	    
	    public static String genCentroid(String strpoints){
	    	String[] points = strpoints.split(",");
	    	String[] result = new String[points.length/2];
	    	for(int i=0;i<points.length/2;i++){
	    		result[i] = points[i*2]+","+points[i*2+1];
	    	}
	    	return genCentroid(result);
	    }

	    /**
	     * 计算两条路段的交叉点
	     *
	     * @param segm1 路段1坐标
	     * @param segm2 路段2坐标
	     * @return 交叉点坐标
	     */
	    public static String getIntersection(String segm1, String segm2) {
	        String intersection = "";
	        Geometry g1 = GISUtil.genLineString(segm1);
	        Geometry g2 = GISUtil.genLineString(segm2);
	        Point intsPoint = g1.intersection(g2).getCentroid();
	        intersection = GISUtil.formatPos(Double.toString(intsPoint.getX()),4) + "," + GISUtil.formatPos(Double.toString(intsPoint.getY()),5);
	        


	        return intersection;
	    }

	    /**
	     * 坐标格式化方法 保留小数点后五位，四舍五入
	     *
	     * @param pos
	     * @return
	     */
	    public static String formatPos(String pos,int num) {
	        String format = ".";
	        for(int i=0;i<num;i++){
	            format+="0";
	        }
	        DecimalFormat decimalFormat = new DecimalFormat(format);

	        return decimalFormat.format(Double.parseDouble(pos));

	    }
	    
	    /**
	     * 坐标格式化方法 保留小数点后五位，四舍五入
	     *
	     * @param pos
	     * @return
	     */
	    public static String formatPoss(String pos,int num) {
	    	String[] poss = pos.split(",");
	    	String result = "";
	    	for(String point:poss){
	    		result+= formatPos(point,num)+",";
	    	}
	    	return result.substring(0,result.length()-1);
	    }

	    /**
	     * 计算路段长度
	     *
	     * @param coordinates 路段的坐标点集合
	     * @return
	     */
	    public static final Double getRoadLength(String coordinates) {
	        Double result = 0d;
	        String[] points = coordinates.split(",");
	        if (points.length < 4) {
	            return 0d;
	        }
	        Double fromx = Double.parseDouble(points[0]);
	        Double fromy = Double.parseDouble(points[1]);
	        for (int i = 1; i < points.length / 2; i++) {
	            Double tox = Double.parseDouble(points[i * 2]);
	            Double toy = Double.parseDouble(points[i * 2 + 1]);
	            result += GISUtil.dist(fromx, fromy, tox, toy);
	            fromx = tox;
	            fromy = toy;
	        }
	        return Double.parseDouble(GISUtil.formatPos(Double.toString(result),4));
	    }
	    
	    /**
	     * 计算点在线上的位置
	     * @param coordinates 线坐标
	     * @param point 点坐标
	     * @return 位置
	     */
	    public static final int getPointInLinePos(String coordinates,String pointstr){
	    	String[] points = coordinates.split(",");
	    	String tempstr = "";
	    	int i=0;
	    	for(i=0;i<points.length/2-1;i++){
	    		tempstr+=points[i*2]+","+points[i*2+1]+",";
	    		String prevline = tempstr+points[i*2+2]+","+points[i*2+3];
	    		String newline = tempstr+pointstr;
	    		if(getRoadLength(prevline)>getRoadLength(newline)){
	    			break;
	    		}
	    	}
	    	return i;
	    }
	    
	    public static final List<String> splitLineByPoints(String coordinates,List<String> points){
	    	List<String> result = new ArrayList<String>();
	    	Map<Integer,String> poss = new HashMap<Integer,String>();
	    	for(String pointstr:points){
	    		poss.put(getPointInLinePos(coordinates, pointstr), pointstr);
	    	}
	    	String[] oripoints = coordinates.split(",");
	    	String tempstr = "";
	    	Boolean issplit = false;
	    	for(int i=0;i<oripoints.length/2;i++){
	    		issplit = false;
	    		Iterator iter = poss.keySet().iterator();
	    		while(iter.hasNext()){
	    			int pos = (Integer) iter.next();
	    			if(i==pos){
	    				Double prex = Double.parseDouble(oripoints[i*2]);
	    				Double prey = Double.parseDouble(oripoints[i*2+1]);
	    				Double possx = Double.parseDouble(poss.get(pos).split(",")[0]);
	    				Double possy = Double.parseDouble(poss.get(pos).split(",")[1]);
	    				if(Math.abs(prex-possx)<=0.0001 && Math.abs(prey-possy)<=0.001){
	    					tempstr+=oripoints[i*2]+","+oripoints[i*2+1];
	    				}else{
	    					tempstr+=oripoints[i*2]+","+oripoints[i*2+1]+","+poss.get(pos);
	    				}
	    				result.add(tempstr);
	    				tempstr = poss.get(pos)+",";
	    				issplit = true;
	    				break;
	    			}
	    		}
	    		if(!issplit){
	    			tempstr+=oripoints[i*2]+","+oripoints[i*2+1]+",";
	    		}
	    	}
	    	if(!issplit){
	    		result.add(tempstr.substring(0,tempstr.length()-1));
	    	}
	    	return result;
	    }
	    

	    /**
	     * 计算指定路段的进口道方向
	     *
	     * @param coordinates
	     * @return
	     */
	    public static final int getDirection(String coordinates) {
	        int direction = 0;
	        String[] points = coordinates.split(",");
	        if (points.length < 4) {
	            return direction;
	        }
	        Double x1 = Double.parseDouble(points[0]);
	        Double y1 = Double.parseDouble(points[1]);
	        Double x2 = Double.parseDouble(points[2]);
	        Double y2 = Double.parseDouble(points[3]);
	        if (x1 - x2 == 0d) {
	            if (y2 > y1) {
	                direction = 3;
	            } else {
	                direction = 4;
	            }
	        } else if (y1 - y2 == 0d) {
	            if (x2 > x1) {
	                direction = 2;
	            } else {
	                direction = 1;
	            }
	        } else {
	            Double a = (y2 - y1) / (x2 - x1);
	            if(a>=-0.577 && a<0.577){
	                if(x1<x2){
	                    direction = 2;
	                }else{
	                    direction = 1;
	                }
	            }else if(a>=0.577 && a<1.732){
	                if (x1<x2){
	                    direction = 6;
	                }else{
	                    direction = 5;
	                }
	            }else if(a>=1.732 || a<-1.732){
	                if(y1<y2){
	                    direction = 3;
	                }else{
	                    direction = 4;
	                }
	            }else if(a>=-1.732 && a<-0.577){
	                if(x1<x2){
	                    direction = 8;
	                }else{
	                    direction = 7;
	                }
	            }
	        }
	        return direction;
	    }

	    /**
	     * 反转坐标串
	     * @param strcoords
	     * @return
	     */
	    public static final String resetDirection(String strcoords) {
	        String[] strArr = strcoords.split(",");
	        int pointnum = strArr.length / 2;
	        String newstrcrd = "";
	        for (int i = pointnum - 1; i >= 0; i--) {
	            newstrcrd += strArr[i * 2] + "," + strArr[i * 2 + 1] + ",";
	        }
	        newstrcrd = newstrcrd.substring(0, newstrcrd.length() - 1);
	        return newstrcrd;
	    }
	    
	    /**
	     * 将坐标字符串转换为坐标集合
	     * @param strpoints
	     * @param points
	     */
	    public static final  void genPoints(String strpoints,List<String> points){
			if(strpoints!=null && strpoints.length()>0){
				String[] pointsarr = strpoints.split(",");
				if(pointsarr.length>=2){
					if(points == null){
						points = new ArrayList<String>();
					}
					for(int i=0;i<pointsarr.length/2;i++){
						String point = pointsarr[i*2]+","+pointsarr[i*2+1];
						points.add(point);
					}
				}
			}
		}
	    
	    public static final Double getAngle (String startpos,String endpos) {
	    	Double startx = Double.parseDouble(startpos.split(",")[0]);
	    	Double starty = Double.parseDouble(startpos.split(",")[1]);
	    	
	    	Double endx = Double.parseDouble(endpos.split(",")[0]);
	    	Double endy = Double.parseDouble(endpos.split(",")[1]);
			Double diff_x = endx - startx,
				diff_y = endy - starty;
			if(diff_x == 0){
				if(diff_y == 0){
					return 0d;
				}else if(diff_y>0){
					return 90d;
				}else{
					return 270d;
				}
			}
			//返回角度,不是弧度
			Double angle =  360*Math.atan(diff_y/diff_x)/(2*Math.PI);
			if(diff_x<0){
				return 180+angle;
			}else{
				return angle;
			}
		};

	    
	    /**
		 * 生成圆坐标点集合
		 * @param pos 圆心
		 * @param pointsNum 点的数量
		 * @param radius 半径
		 * @return
		 */
		public static String generatePoints(String pos, int pointsNum, String radius) {
			Double x0, y0, ra;
			StringBuffer points = new StringBuffer();
			try {
				x0 = Double.parseDouble(pos.split(",")[0]);
				y0 = Double.parseDouble(pos.split(",")[1]);
				ra = Double.parseDouble(radius);
			} catch (Exception e) {
				return null;
			}
			Double angle = 0.0;
			int r = 360 / pointsNum;
			Double x, y;
			for (int i = 0; i < pointsNum; i++) {
				angle = r * i + 0.0;
				x = x0 + Math.cos(angle*Math.PI/180) * ra;
				y = y0 + Math.sin(angle*Math.PI/180) * ra;
				points.append(Double.toString(x) + "," + Double.toString(y)+ ",");
			}
			points.append(Double.toString(x0+Math.cos(0)*ra)+","+Double.toString(y0+Math.sin(0)*ra));
			String result=points.toString();
			return result;
		}
}
