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
	 * ��ȡȫ������
	    * @Title: getJoinPointList
	    * @Description: ��ȡȫ����ת������    ����
	    * void    ��������
	    * @throws
		* ---------------------------
		* @author LiNan3
	    * @date 2018��11��17��
	 */
	public void getGidList() {
		List<RoadMV> RoadMVList = roadsdao.getRoadMV();
		int sum = RoadMVList.size();
		int sumPlus = sum -1;
		for (int i = 0; i < sum; i++) {
			log.info("++++++++++++++++++++++++++++++++++++++++++++");
			log.info("|||||  ������" + sumPlus + ",��ʼ����" + i + "��||||||");
			log.info("++++++++++++++++++++++++++++++++++++++++++++");
			
			dealRoadMVOneByOne(RoadMVList.get(i).getGid());
			
			log.info("++++++++++++++++++++++++++++++++++++++++++++");
			log.info("|||||  ������" + sumPlus + ",���������" + i + "��||||||");
			log.info("++++++++++++++++++++++++++++++++++++++++++++");
		}

	}

	
	/**
	 * ��������1.ͨ��gidȡ���ߵ��ַ�����2.���ýӿ�ת�����ꣻ3.���±��б�������
	    * @Title: dealRoadMVOneByOne
	    * @Description: ͨ��gid��������
	    * @param gid    ����
	    * void    ��������
	    * @throws
		* ---------------------------
		* @author LiNan3
	    * @date 2018��11��17��
	 */
	private void dealRoadMVOneByOne(String gid) {
		List<Point> RoadMVList = roadsdao.getStrcoods(gid);
		log.error("gid");
		String toChangeStr = "";
		for(int j = 0 ,sum = RoadMVList.size();j<sum;j++){
			toChangeStr+=RoadMVList.get(j).getXyString()+",";
		}
		String pre84Pos = toChangeStr.substring(0, toChangeStr.length()-1);
		log.info("ת��ǰ���꣺"+pre84Pos);
		String pos2000 = sendHttpToGet2000Pos(pre84Pos);
		if(pos2000!=null && !"-1".equals(pos2000)){
			log.info("ת�������꣺"+pos2000);
			roadsdao.saveNewRoad(gid,pos2000);
		}else{
			log.error("[gid]:"+gid+",ת��ʧ�ܣ������б���");
		}
	}
	
	
	/**
	 * 
	    * @Title: sendHttpToGet2000Pos
	    * @Description: ����http����ת��Ϊ2000����ϵ
	    * @param Pos84    ����
	    * void    ��������
	    * @throws
		* ---------------------------
		* @author LiNan3
	    * @date 2018��11��17��
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
