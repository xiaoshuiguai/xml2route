package com.hisense.adapter.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertyUtil {

	// 属性集合
	private Properties property = null;
	//属性文件输入流
	InputStream inStream = null;
	boolean isSuccessful = false;
	private static final Log log=LogFactory.getLog(PropertyUtil.class);
	private static PropertyUtil util=new PropertyUtil();
	private PropertyUtil(){
		
	}
	public static PropertyUtil getInstance(){
		return util;
	}
	/**
	 * 
	 * @return 属性加载是否成功
	 */
	public Boolean init() {

		if (isSuccessful) {
			return isSuccessful;
		}
		try {
			property = new Properties();
			inStream = this.getClass().getResourceAsStream("/config.properties");
			property.load(new InputStreamReader(inStream,"UTF-8"));
			isSuccessful = true;
		} catch (Exception e) {
			log.error("读取properties文件失败",e);
			isSuccessful = false;
		} 

		return isSuccessful;
	}

	/**
	 * 
	 * @param key
	 * @return 属性值
	 */
	public String getProperty(String key) {
		String value = "";

		if (init() == true) {
			value = property.getProperty(key);
		}

		return value == null ? "" : value;
	}

	public void close() {

		if (inStream != null) {
			try {
				inStream.close();
				inStream = null;
				isSuccessful = false;
			} catch (IOException e) {
				log.error("关闭属性文件失败",e);
				inStream = null;
				isSuccessful = false;
			}
		}
	}
}
