package com.hisense.adapter.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.hisense.adapter.main.getRoadToChange;

public class SendGetHttp {
	private static final Log log = LogFactory.getLog(SendGetHttp.class);
	public static String sendGet(String url, String param) {
		String result = "";

		BufferedReader in = null;

		try {
			String urlNameString = url + "?" + param;
			if (param.equals("")) {
				urlNameString = urlNameString.substring(0,
						urlNameString.length() - 1);
			}
			log.info("×ª»»ÇëÇó£º"+urlNameString);
			URL realUrl = new URL(urlNameString);

			URLConnection connection = realUrl.openConnection();
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0(compatible;MSIE 6.0;Windows NT 5.1;SV1)");
			connection.setRequestProperty("contentType", "UTF-8");
			connection.setRequestProperty("Content-type",
					"application/x-www-form-urlencoded;charset=UTF-8");
			connection.connect();

			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			String line;

			while ((line = in.readLine()) != null) {
				result += line;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			
		}
		return result;
	}
}
