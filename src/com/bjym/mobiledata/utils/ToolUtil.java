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
		provinceMap.put("����", "0001");
		provinceMap.put("�Ϻ�", "0002");
		provinceMap.put("���", "0003");
		provinceMap.put("����", "0004");
		provinceMap.put("������", "0005");
		provinceMap.put("����", "0006");
		provinceMap.put("����", "0007");
		provinceMap.put("���ɹ�", "0008");
		provinceMap.put("�ӱ�", "0009");
		provinceMap.put("����", "0010");
		provinceMap.put("�㶫", "0011");
		provinceMap.put("����", "0012");
		provinceMap.put("ɽ��", "0013");
		provinceMap.put("�㽭", "0014");
		provinceMap.put("����", "0015");
		provinceMap.put("����", "0016");
		provinceMap.put("����", "0017");
		provinceMap.put("����", "0018");
		provinceMap.put("����", "0019");
		provinceMap.put("�ຣ", "0020");
		provinceMap.put("ɽ��", "0021");
		provinceMap.put("����", "0022");
		provinceMap.put("����", "0023");
		provinceMap.put("����", "0024");
		provinceMap.put("����", "0025");
		provinceMap.put("�Ĵ�", "0026");
		provinceMap.put("����", "0027");
		provinceMap.put("����", "0028");
		provinceMap.put("����", "0029");
		provinceMap.put("����", "0030");
		provinceMap.put("�½�", "0031");
	}

	// ###########################δ֪����

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
