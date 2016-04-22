package com.bjym.mobiledata.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ToolUtil {
	private static Map<String, String> provinceMap = new HashMap<String, String>();
	static {
		provinceMap.put("北京", "0001");
		provinceMap.put("上海", "0002");
		provinceMap.put("天津", "0003");
		provinceMap.put("重庆", "0004");
		provinceMap.put("黑龙江", "0005");
		provinceMap.put("吉林", "0006");
		provinceMap.put("辽宁", "0007");
		provinceMap.put("内蒙古", "0008");
		provinceMap.put("河北", "0009");
		provinceMap.put("河南", "0010");
		provinceMap.put("广东", "0011");
		provinceMap.put("湖北", "0012");
		provinceMap.put("山东", "0013");
		provinceMap.put("浙江", "0014");
		provinceMap.put("安徽", "0015");
		provinceMap.put("江苏", "0016");
		provinceMap.put("江西", "0017");
		provinceMap.put("云南", "0018");
		provinceMap.put("宁夏", "0019");
		provinceMap.put("青海", "0020");
		provinceMap.put("山西", "0021");
		provinceMap.put("陕西", "0022");
		provinceMap.put("湖南", "0023");
		provinceMap.put("福建", "0024");
		provinceMap.put("甘肃", "0025");
		provinceMap.put("四川", "0026");
		provinceMap.put("广西", "0027");
		provinceMap.put("贵州", "0028");
		provinceMap.put("海南", "0029");
		provinceMap.put("西藏", "0030");
		provinceMap.put("新疆", "0031");
	}

	// ###########################未知地区

	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		return str.substring(0, 8) + str.substring(9, 13)
				+ str.substring(14, 18);
	}

	public static String getSessionID() {
		char[] cArr = { 'A', 'B', 'C', 'D', 'E', 'F' };
		Random random = new Random();
		char suffix = cArr[random.nextInt(6)];

		long timeStamp = System.currentTimeMillis();

		int randomNum = random.nextInt(99);
		String str = String.valueOf(suffix) + String.valueOf(timeStamp)
				+ randomNum;

		return str;
		// return String.valueOf(timeStamp + randomNum);

	}

	public static String getProductID() {
		long timeStamp = System.currentTimeMillis();

		Random random = new Random();
		int randomNum = random.nextInt(9999);
		return String.valueOf(timeStamp + randomNum);
	}

	public static boolean isMobileNO(String mobile) {
		if(mobile==null||"".equals(mobile)){
			return false;
		}
		
		if(mobile.length()!=11){
			return false;
		}
		
		return true;
	}

	public static boolean isExist(String key) {
		boolean result = true;
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		if (map.get(key) == null) {
			map.put(key, 1);
			result = false;
		}
		return result;
	}

	public static String getClassPath() {
		String classPath = ToolUtil.class.getResource("/").getPath();
		// LoggerUtil.info("ToolUtil.class.getResource(\"/\").getPath():"
		// + classPath);

		String rootPath = "";
		// windows
		if ("\\".equals(File.separator)) {
			rootPath = classPath.substring(1, classPath
					.indexOf("/WEB-INF/classes"));
			rootPath = rootPath.replace("/", "\\");
		}
		// linux
		if ("/".equals(File.separator)) {
			rootPath = classPath.substring(0, classPath
					.indexOf("/WEB-INF/classes"));
			rootPath = rootPath.replace("\\", "/");
		}
		return classPath;
	}

	public static String getAppClassPath() {
		return System.getProperty("user.dir");
	}

	public static String provinceToPcode(String provinceName) {
		return provinceMap.get(provinceName);
	}

	public static void main(String[] args) {
		System.out.println(ToolUtil.isMobileNO("1527493560"));
	}
}
