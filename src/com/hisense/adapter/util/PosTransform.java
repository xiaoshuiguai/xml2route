package com.hisense.adapter.util;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
/**
 * 
    * @ClassName: PosTransform
    * @Description: 鍧愭爣绯昏浆鎹㈢畻娉�
    * @author Administrator
    * @date 2018-1-26
    *
 */
public class PosTransform {
	private static DecimalFormat dFormat=new DecimalFormat("#.000000");  
	private static double PI = 3.14159265358979324;
	private static double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
	
	private static Map<String, Double> changemap = new HashMap<String, Double>();
	/**
	 * 杞崲缁忓害
	 * @param x
	 * @param y
	 * @return
	 */
	public double transformLat(double x,double y) {
		double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y + 0.2 * Math.sqrt(Math.abs(x));
		ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * PI) + 40.0 * Math.sin(y / 3.0 * PI)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * PI) + 320 * Math.sin(y * PI / 30.0)) * 2.0 / 3.0;
        return ret;
	}
	/**
	 * 杞崲绾害
	 * @param x
	 * @param y
	 * @return
	 */
	public double transformLon(double x,double y) {
		double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * PI) + 20.0 * Math.sin(2.0 * x * PI)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * PI) + 40.0 * Math.sin(x / 3.0 * PI)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * PI) + 300.0 * Math.sin(x / 30.0 * PI)) * 2.0 / 3.0;
        return ret;
	}
	/**
	 * 鎵�鍦ㄧ粡绾害鏄笉鏄秴鍑轰腑鍥借寖鍥�
	 * @param lat
	 * @param lon
	 * @return
	 */
	public boolean outOfChina(double lat,double lon) {
		if(lon < 72.004 || lon > 1378347) {
			return true;
		}
		if(lat < 0.8293 || lat > 55.8271) {
			return true;
		}
		return false;
	}
	/**
	 * 
	 * @param lat
	 * @param lon
	 * @return
	 */
	public Map<String,Double> delta(double lat,double lon) {
		double a = 6378245.0;
		double ee = 0.00669342162296594323;
		double dLat = transformLat(lon - 105.0, lat - 35.0);
		double dLon = transformLon(lon - 105.0, lat - 35.0);
		double radLat = lat / 180.0 * PI;
		double magic = Math.sin(radLat);
		magic = 1 - ee * magic * magic;
		double sqrtMagic = Math.sqrt(magic);
		dLat = (dLat * 180.0) / ((a*(1 - ee)) / (magic * sqrtMagic) * PI);
		dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * PI);
		Map<String,Double> map = new HashMap<String,Double>();
		map.put("lat", dLat);
		map.put("lon", dLon);
		return map;
	}
	/**
	 * success
	 * WGS-84 To GCJ-02
	 * @param wgsLat
	 * @param wgsLon
	 * @return
	 */
	public Map<String,Double> wgsToGcj(double wgsLat,double wgsLon){
		Map<String,Double> map = new HashMap<String,Double>();
		if(outOfChina(wgsLat,wgsLon)) {
			map.put("lat", wgsLat);
			map.put("lon", wgsLon);
			return map;
		}
		Map<String,Double> deltaMap = delta(wgsLat,wgsLon);
		double lat = deltaMap.get("lat");
		double lon = deltaMap.get("lon");
		lat = wgsLat + lat;
		lon = wgsLon + lon;
		map.put("lat",lat);
		map.put("lon", lon);
		return map;
	}
	/**
	 * success
	 * GCJ-02 To WGS-84
	 * @param gcjLat
	 * @param gcjLon
	 * @return
	 */
	public Map<String,Double> gcjToWgs(double gcjLat,double gcjLon){
		Map<String,Double> map = new HashMap<String,Double>();
		if(outOfChina(gcjLat,gcjLon)) {
			map.put("lat",gcjLat);
			map.put("lon",gcjLon);
			return map;
		}
		Map<String,Double> deltaMap = delta(gcjLat,gcjLon);
		double lat = deltaMap.get("lat");
		double lon = deltaMap.get("lon");
		lat = gcjLat - lat;
		lon = gcjLon - lon;
		
        String latStr=dFormat.format(lat);  
        lat= Double.valueOf(latStr);  
        
        String lonStr=dFormat.format(lon);  
        lon= Double.valueOf(lonStr);  
        
		map.put("lat", lat);
		map.put("lon", lon);
		return map;
	}
	/**
	 * success
	 * 绮剧‘杞崲锛堝疄闄呬竴鑸級
	 * @param gcjLat
	 * @param gcjLon
	 * @return
	 */
	public Map<String,Double> gcjToWgsExactly(double gcjLat,double gcjLon){
		Map<String,Double> map = new HashMap<String,Double>();
		double initDelta = 0.01;
		double threshold = 0.000000001;
		double dLat = initDelta;
		double dLon = initDelta;
		double mLat = gcjLat - dLat;
		double mLon = gcjLon - dLon;
		double pLat = gcjLat + dLat;
		double pLon = gcjLon + dLon;
		double wgsLat, wgsLon = 0;
		int i =0;
        while (true) {
                wgsLat = (mLat + pLat) / 2;
                wgsLon = (mLon + pLon) / 2;
                Map<String,Double> tmp = gcjToWgs(wgsLat, wgsLon);
                dLat = tmp.get("lat") - gcjLat;
                dLon = tmp.get("lon") - gcjLon;
                if ((Math.abs(dLat) < threshold) && (Math.abs(dLon) < threshold)) {
                	break;
                }
                if (dLat > 0) {
                	pLat = wgsLat;
                }else {
                	mLat = wgsLat;
                }
                if (dLon > 0) {
                	pLon = wgsLon;
                }else {
                	mLon = wgsLon;
                }
                if (++i > 10000) {
                	break;
                }
            }
            map.put("lat", wgsLat);
            map.put("lon", wgsLon);
		return map;
	}
	/**
	 * GCJ-02 To BD-09
	 * @param gcjLat
	 * @param gcjLon
	 * @return
	 */
	public Map<String,Double> gcjToBd(double gcjLat,double gcjLon){
		double x = gcjLon;
		double y = gcjLat;
		double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
		double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
		double bdLon = z * Math.cos(theta) + 0.0065;
		double bdLat = z * Math.sin(theta) + 0.006;
		Map<String,Double> map = new HashMap<String,Double>();
		map.put("lat",bdLat);
		map.put("lon", bdLon);
		return map;
	}
	/**
	 * BD-09 To GCJ-02
	 * @param bdLat
	 * @param bdLon
	 * @return
	 */
	public Map<String,Double> bdToGcj(double bdLat,double bdLon){
		double x = bdLon - 0.0065;
		double y = bdLat - 0.006;
		double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
		double theta =  Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
		double gcjLon = z * Math.cos(theta);
		double gcjLat = z * Math.sin(theta);
		Map<String,Double> map = new HashMap<String,Double>();
		map.put("lat", gcjLat);
		map.put("lon", gcjLon);
		return map;
	}
	/**
	 * Wgs-84 To Bd-09
	 * @param wgsLat
	 * @param wgsLon
	 * @return
	 */
	public Map<String,Double> wgsToBd(double wgsLat,double wgsLon){
		Map<String,Double> map = wgsToGcj(wgsLat,wgsLon);
		double gcjLat = map.get("lat");
		double gcjLon = map.get("lon");
		Map<String,Double>	bdMap = gcjToBd(gcjLat,gcjLon);
		return bdMap;
	}
	/**
	 * Bd-09 To Wgs-84
	 * @param bdLat
	 * @param bdLon
	 * @return
	 */
	public Map<String,Double> bdToWgs(double bdLat,double bdLon){
		Map<String,Double> map = bdToGcj(bdLat,bdLon);
		double gcjLat = map.get("lat");
		double gcjLon = map.get("lon");
		Map<String,Double> wgsMap = gcjToWgs(gcjLat,gcjLon);
		return wgsMap;
	}
	
	
	public final String lineStrGCJ2WGS(String strpoints) {
		String posStr84 = "";
		
		if (strpoints != null && strpoints.length() > 0) {
			String[] tubarpointsarr = strpoints.split(",");
			int length = tubarpointsarr.length;

			for(int i=0;i<length/2;i++){
				changemap = this.gcjToWgs(Double.valueOf(tubarpointsarr[i*2+1]),	Double.valueOf(tubarpointsarr[i*2]));
				posStr84 += changemap.get("lon") + "," + changemap.get("lat")+ ",";
			}
		}
		posStr84 = posStr84.substring(0, posStr84.length() - 1);
		return posStr84;
	}
	
	
	/**
	 * @Title: main
	 * @Description: TODO(杩欓噷鐢ㄤ竴鍙ヨ瘽鎻忚堪杩欎釜鏂规硶鐨勪綔鐢�)
	 * @param @param args    鍙傛暟
	 * @return void    杩斿洖绫诲瀷
	 * @throws
	 */

	public static void main(String[] args) {
		PosTransform ct = new PosTransform();
		Map<String,Double> map = ct.wgsToBd(36.08617529296875,120.35930693359375);
		double lat = map.get("lat");
		double lon = map.get("lon");
		System.out.println(lat+","+lon);
	}

}
