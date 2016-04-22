package com.bjym.mobiledata.redis.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import com.bjym.mobiledata.utils.ToolUtil;

public class RedisConfigUtil {
	private static ResourceBundle logciBundle;
	private static ResourceBundle gatewayBundle;

	private static Map<String, String> logicMap = new HashMap<String, String>();
	private static Map<String, String> gatewayMap = new HashMap<String, String>();

	static {
		InputStream is = null;
		try {
			String classPath = ToolUtil.getAppClassPath();

			is = new FileInputStream(new File(classPath
					+ "/redisconfig.properties"));

			logciBundle = new PropertyResourceBundle(is);

			Enumeration<String> e = logciBundle.getKeys();
			while (e.hasMoreElements()) {
				String key = e.nextElement();
				String value = logciBundle.getString(key);
				logicMap.put(key, value);
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

			is = new FileInputStream(new File(classPath
					+ "/redisconfig.properties"));

			gatewayBundle = new PropertyResourceBundle(is);

			Enumeration<String> e = gatewayBundle.getKeys();
			while (e.hasMoreElements()) {
				String key = e.nextElement();
				String value = gatewayBundle.getString(key);
				gatewayMap.put(key, value);
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

	public static String getLogic(String key, String def) {
		try {
			return logicMap.get(key);
		} catch (Exception ex) {
			return def;
		}
	}

	public static int getLogic(String key, int def) {
		try {
			return Integer.parseInt(logicMap.get(key));
		} catch (Exception ex) {
			return def;
		}
	}

	public static long getLogic(String key, long def) {
		try {
			return Long.parseLong(logicMap.get(key));
		} catch (Exception ex) {
			return def;
		}
	}

	public static float getLogic(String key, float def) {
		try {
			return Float.parseFloat(logicMap.get(key));
		} catch (Exception ex) {
			return def;
		}
	}

	public static boolean getLogic(String key, boolean def) {
		try {
			return "true".equals(logicMap.get(key));
		} catch (Exception ex) {
			return def;
		}
	}

	public static Object getLogic(String key, Object def) {
		try {
			Object obj = logicMap.get(key);
			if (obj == null) {
				return def;
			}
			return obj;
		} catch (Exception ex) {
			return def;
		}
	}

	public static String getGateway(String key, String def) {
		try {
			return gatewayMap.get(key);
		} catch (Exception ex) {
			return def;
		}
	}

	public static int getGateway(String key, int def) {
		try {
			return Integer.parseInt(gatewayMap.get(key));
		} catch (Exception ex) {
			return def;
		}
	}

	public static long getGateway(String key, long def) {
		try {
			return Long.parseLong(gatewayMap.get(key));
		} catch (Exception ex) {
			return def;
		}
	}

	public static float getGateway(String key, float def) {
		try {
			return Float.parseFloat(gatewayMap.get(key));
		} catch (Exception ex) {
			return def;
		}
	}

	public static boolean getGateway(String key, boolean def) {
		try {
			return "true".equals(gatewayMap.get(key));
		} catch (Exception ex) {
			return def;
		}
	}

	public static Object getGateway(String key, Object def) {
		try {
			Object obj = gatewayMap.get(key);
			if (obj == null) {
				return def;
			}
			return obj;
		} catch (Exception ex) {
			return def;
		}
	}

}
