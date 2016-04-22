package com.bjym.mobiledata.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class PropertiesUtil {
	private static ResourceBundle appBundle;
	private static ResourceBundle c3p0Bundle;

	private static Map<String, String> configMap = new HashMap<String, String>();
	private static Map<String, String> c3p0Map = new HashMap<String, String>();

	static {
		InputStream is = null;
		try {
			String classPath = ToolUtil.getAppClassPath();

			is = new FileInputStream(new File(classPath + "/config.properties"));

			appBundle = new PropertyResourceBundle(is);

			Enumeration<String> e = appBundle.getKeys();
			while (e.hasMoreElements()) {
				String key = e.nextElement();
				String value = appBundle.getString(key);
				configMap.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			String classPath = ToolUtil.getAppClassPath();
			is = new FileInputStream(new File(classPath + "/c3p0.properties"));
			c3p0Bundle = new PropertyResourceBundle(is);

			Enumeration<String> e = c3p0Bundle.getKeys();
			while (e.hasMoreElements()) {
				String key = e.nextElement();
				String value = c3p0Bundle.getString(key);
				c3p0Map.put(key, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String get(String key, String def) {
		try {
			return configMap.get(key);
		} catch (Exception ex) {
			return def;
		}
	}

	public static int get(String key, int def) {
		try {
			return Integer.parseInt(configMap.get(key));
		} catch (Exception ex) {
			return def;
		}
	}

	public static long get(String key, long def) {
		try {
			return Long.parseLong(configMap.get(key));
		} catch (Exception ex) {
			return def;
		}
	}

	public static float get(String key, float def) {
		try {
			return Float.parseFloat(configMap.get(key));
		} catch (Exception ex) {
			return def;
		}
	}

	public static boolean get(String key, boolean def) {
		try {
			return "true".equals(configMap.get(key));
		} catch (Exception ex) {
			return def;
		}
	}

	public static Object get(String key, Object def) {
		try {
			Object obj = configMap.get(key);
			if (obj == null) {
				return def;
			}
			return obj;
		} catch (Exception ex) {
			return def;
		}
	}

	public static String getParameterC3P0(String name) {
		return c3p0Map.get(name);
	}

}
