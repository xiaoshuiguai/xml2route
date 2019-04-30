package com.hisense.adapter.main;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.SAXException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hisense.adapter.dao.RoadsDao;
import com.hisense.adapter.util.PropertyUtil;
import com.hisense.adapter.util.SendGetHttp;

import model.Point;
import model.RoadMV;

public class getRoadToChange {
	private static final Log log = LogFactory.getLog(getRoadToChange.class);
	static RoadsDao roadsdao = new RoadsDao();
	
	private PropertyUtil propertyUtil = PropertyUtil.getInstance();
	String changeServerviceURL = propertyUtil.getProperty("CHANGESERVERVICEURL");
	String inSR = propertyUtil.getProperty("INSR");
	String outSR = propertyUtil.getProperty("OUTSR");

	
	/**
	 * 获取全部数据
	    * @Title: getJoinPointList
	    * @Description: 获取全部待转换数据    参数
	    * void    返回类型
	    * @throws
		* ---------------------------
		* @author LiNan3
	    * @date 2018年11月17日
	 */
	public void getGidList() {
		List<RoadMV> RoadMVList = roadsdao.getRoadMV();
		int sum = RoadMVList.size();
		int sumPlus = sum -1;
		for (int i = 0; i < sum; i++) {
			log.info("++++++++++++++++++++++++++++++++++++++++++++");
			log.info("|||||  总数：" + sumPlus + ",开始处理【" + i + "】||||||");
			log.info("++++++++++++++++++++++++++++++++++++++++++++");
			
			dealRoadMVOneByOne(RoadMVList.get(i).getGid());
			
			log.info("++++++++++++++++++++++++++++++++++++++++++++");
			log.info("|||||  总数：" + sumPlus + ",处理结束【" + i + "】||||||");
			log.info("++++++++++++++++++++++++++++++++++++++++++++");
		}

	}

	
	/**
	 * 逐条处理，1.通过gid取得线的字符串；2.调用接口转换坐标；3.向新表中保存数据
	    * @Title: dealRoadMVOneByOne
	    * @Description: 通过gid逐条处理
	    * @param gid    参数
	    * void    返回类型
	    * @throws
		* ---------------------------
		* @author LiNan3
	    * @date 2018年11月17日
	 */
	private void dealRoadMVOneByOne(String gid) {
		List<Point> RoadMVList = roadsdao.getStrcoods(gid);
		log.error("gid");
		String toChangeStr = "";
		for(int j = 0 ,sum = RoadMVList.size();j<sum;j++){
			toChangeStr+=RoadMVList.get(j).getXyString()+",";
		}
		String pre84Pos = toChangeStr.substring(0, toChangeStr.length()-1);
		log.info("转换前坐标："+pre84Pos);
		String pos2000 = sendHttpToGet2000Pos(pre84Pos);
		if(pos2000!=null && !"-1".equals(pos2000)){
			log.info("转换后坐标："+pos2000);
			roadsdao.saveNewRoad(gid,pos2000);
		}else{
			log.error("[gid]:"+gid+",转换失败，不进行保存");
		}
	}
	
	
	/**
	 * 
	    * @Title: sendHttpToGet2000Pos
	    * @Description: 调用http请求转换为2000坐标系
	    * @param Pos84    参数
	    * void    返回类型
	    * @throws
		* ---------------------------
		* @author LiNan3
	    * @date 2018年11月17日
	 */
	public String sendHttpToGet2000Pos(String Pos84){
		String s = SendGetHttp.sendGet(changeServerviceURL,
				"inSR="+inSR+"&outSR="+outSR+
				"&geometries={\"geometryType\":\"esriGeometryPolyline\","
						+ "\"geometries\":[{\"paths\":[["+Pos84+"]]}]"
						+ "}"
				+ "&transformation=&transformForward=false&f=pjson");
		if(s==null||"".equals(s.trim())){
			return "-1";
		}
		
		JSONObject jsonObject = JSONObject.parseObject(s);
		JSONArray geometries = jsonObject.getJSONArray("geometries");
		JSONObject jsonObjectb = JSONObject.parseObject(geometries.get(0).toString());
		JSONArray b = jsonObjectb.getJSONArray("paths");
		JSONArray c = (JSONArray) b.get(0);
		String pos2000 = "";
		for(int i = 0,sum = c.size() ; i<sum;i++){
			JSONArray d = (JSONArray) c.get(i);
			pos2000+=d.get(0)+","+d.get(1)+",";
		}
		return pos2000.substring(0, pos2000.length() -1 );
	}

	
	
	public static void main(String[] args) throws Exception{

		 getRoadToChange test = new getRoadToChange();
		 try {
			 test.getGidList();
		 } catch (Exception e) {
			 // TODO Auto-generated catch block
			 e.printStackTrace();
		 }

	}
}
