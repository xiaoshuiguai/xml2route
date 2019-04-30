package com.hisense.adapter.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


public class Test {
	
	 public static void main(String[] args) throws Exception{
		 String s = SendGetHttp.sendGet("http://125.70.9.194:6080/arcgis/rest/services/Utilities/Geometry/GeometryServer/project", "inSR=4326&outSR=4490&geometries={\"geometryType\":\"esriGeometryPolyline\",\"geometries\":[{\"paths\":[[[-117,34],[-116,34],[-117,33]]]}]}&transformation=&transformForward=false&f=pjson");
			JSONObject jsonObject = JSONObject.parseObject(s);
			JSONArray geometries = jsonObject.getJSONArray("geometries");
			JSONObject jsonObjectb = JSONObject.parseObject(geometries.get(0).toString());
			JSONArray b = jsonObjectb.getJSONArray("paths");
			JSONArray c = (JSONArray) b.get(0);
			for(int i = 0,sum = c.size() ; i<sum;i++){
				JSONArray d = (JSONArray) c.get(i);
				System.out.println(d.get(0)+","+d.get(1));
			}
			
			JSONArray d = (JSONArray) c.get(0);
					
			System.out.println(d.get(0)+","+d.get(1));
	 }

}
