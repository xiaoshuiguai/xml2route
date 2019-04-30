package com.hisense.adapter.util;

import java.text.DecimalFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgeo.proj4j.BasicCoordinateTransform;
import org.osgeo.proj4j.CRSFactory;
import org.osgeo.proj4j.CoordinateReferenceSystem;
import org.osgeo.proj4j.CoordinateTransform;
import org.osgeo.proj4j.ProjCoordinate;

public class PosTransformPlus {
	static double netOffsetX;
	static double netOffsetY;
	static CoordinateTransform coordinateTrans;
	private static final Log log = LogFactory.getLog(PosTransformPlus.class);
	static PropertyUtil instance = PropertyUtil.getInstance();
	DecimalFormat df = new DecimalFormat("#.000000");

	ProjCoordinate src;
	ProjCoordinate tgt;
	
	public static PosTransformPlus getInstance() {
		CRSFactory CRSUTM = new CRSFactory();
		CRSFactory CRS84 = new CRSFactory();
		CoordinateReferenceSystem srcRS = CRSUTM.createFromParameters("xmlpos",instance.getProperty("srcRS"));
		
		CoordinateReferenceSystem tgtRS = CRS84.createFromName(instance.getProperty("tgtRS"));
		
		netOffsetX = Double.valueOf(instance.getProperty("netOffsetX"));
		netOffsetY = Double.valueOf(instance.getProperty("netOffsetY"));
		log.debug(netOffsetX+","+netOffsetY);
		
		coordinateTrans = new BasicCoordinateTransform(srcRS, tgtRS);
		PosTransformPlus du = new PosTransformPlus();
		return du;
	}

	public String transform(String pointStr) {
		String[] point = pointStr.split(",");
		double deadX = Double.valueOf(point[0]) - netOffsetX;
		double deadY = Double.valueOf(point[1]) - netOffsetY;
		src = new ProjCoordinate(deadX, deadY);
		tgt = new ProjCoordinate();
		ProjCoordinate returnVal = coordinateTrans.transform(src, tgt);
		return df.format(returnVal.x) + "," + df.format(returnVal.y);
	}
	
	public ProjCoordinate transform2P(String pointStr) {
		String[] point = pointStr.split(",");
		double deadX = Double.valueOf(point[0]) - netOffsetX;
		double deadY = Double.valueOf(point[1]) - netOffsetY;
		src = new ProjCoordinate(deadX, deadY);
		tgt = new ProjCoordinate();
		return coordinateTrans.transform(src, tgt);
	}

}
