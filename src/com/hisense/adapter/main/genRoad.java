package com.hisense.adapter.main;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hisense.adapter.dao.SecDao;
import com.hisense.adapter.dao.TubarDao;
import com.hisense.adapter.util.PosTransform;
import com.hisense.adapter.util.PropertyUtil;

import model.Point;
import model.RoadMV;

public class genRoad {

	private static final Log log = LogFactory.getLog(getRoadToChange.class);
	static TubarDao roadsdao = new TubarDao();

	private PropertyUtil propertyUtil = PropertyUtil.getInstance();
	String changeServerviceURL = propertyUtil.getProperty("CHANGESERVERVICEURL");
	String inSR = propertyUtil.getProperty("INSR");
	String outSR = propertyUtil.getProperty("OUTSR");
	PosTransform postransform = new PosTransform();

	public void initRoadLinkFromTUBAR() {
		List<RoadMV> RoadMVList = roadsdao.getAllRoadFromTUBAR();
		int sum = RoadMVList.size();
		int sumPlus = sum - 1;
		for (int i = 0; i < sum; i++) {
			log.info("++++++++++++++++++++++++++++++++++++++++++++");
			log.info("|||||  总数：" + sumPlus + ",开始处理【" + i + "】||||||");
			log.info("++++++++++++++++++++++++++++++++++++++++++++");

			dealRoadMVOneByOne(RoadMVList.get(i));

			log.info("++++++++++++++++++++++++++++++++++++++++++++");
			log.info("|||||  总数：" + sumPlus + ",处理结束【" + i + "】||||||");
			log.info("++++++++++++++++++++++++++++++++++++++++++++");
		}
	}

	private void dealRoadMVOneByOne(RoadMV roadmv) {
		List<Point> RoadMVList = roadsdao.getStrcoods(roadmv.getGid());
		String toChangeStr = "";
		for(int j = 0 ,sum = RoadMVList.size();j<sum;j++){
			Map<String,Double> map = postransform.gcjToWgs(
					Double.valueOf(RoadMVList.get(j).getY()), 
					Double.valueOf(RoadMVList.get(j).getX())
				);
			toChangeStr+=map.get("lon")+","+map.get("lat")+",";
		}
		
		String wgs84 = toChangeStr.substring(0, toChangeStr.length()-1);
		if(wgs84!=null && !"-1".equals(wgs84)){
			log.info("转换后坐标："+wgs84);
			roadsdao.saveNewRoad(roadmv,wgs84);
		}else{
			log.error("[gid]:"+""+",转换失败，不进行保存");
		}
		
	}
}
