package com.huawei.bi.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	private static Properties confProp = new Properties();

	static {
		InputStream inStream = Config.class.getClassLoader()
				.getResourceAsStream("config");
		try {
			confProp.load(inStream);
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
	}

	public static String get(String key) {
		return confProp.getProperty(key);
	}
	
	public static Properties getProp() {
		return confProp;
	}
}
