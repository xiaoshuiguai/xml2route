package com.hisense.adapter.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertyUtil {

	// ���Լ���
	private Properties property = null;
	//�����ļ�������
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
	 * @return ���Լ����Ƿ�ɹ�
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
			log.error("��ȡproperties�ļ�ʧ��",e);
			isSuccessful = false;
		} 

		return isSuccessful;
	}

	/**
	 * 
	 * @param key
	 * @return ����ֵ
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
				log.error("�ر������ļ�ʧ��",e);
				inStream = null;
				isSuccessful = false;
			}
		}
	}
}
