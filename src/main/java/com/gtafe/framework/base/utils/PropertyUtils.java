package com.gtafe.framework.base.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtils {

	public static String getProperty(String fileName, String key) {
		String destStr = "";
		try {
			InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(fileName);
			Properties p = new Properties();
			p.load(in);
			destStr = p.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return destStr;
	}
}