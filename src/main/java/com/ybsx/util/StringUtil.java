package com.ybsx.util;

import java.text.DecimalFormat;

import com.sun.tools.doclets.internal.toolkit.resources.doclets;

/**
 * 字符串工具类
 * @author zhouKai
 * 2018年5月7日 下午2:23:10
 */
public class StringUtil {

	/**
	 * 是否为空
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str) {
		if (str == null || str.isEmpty() || str.replaceAll(" ", "").isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isBlank(String... strs) {
		for (int i = 0; i < strs.length; i++) {
			if (isBlank(strs[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 首字母大写
	 * @param in
	 * @return
	 */
	public static String upperHeadChar(String in) {
		String head = in.substring(0, 1);
		String out = head.toUpperCase() + in.substring(1, in.length());
		return out;
	}
	
	/*
	 * 将秒转为分 秒
	 */
	public static String  transSecondToMS(double seconds) {
			int value=(int)seconds;
	        int minutes = value / 60;
	        int remainingSeconds = value % 60;
	        return minutes+"分"+remainingSeconds+"秒";
	}

	public static String tranToPercent(Double valueOf) {
          DecimalFormat df = new DecimalFormat("0.00%");
          String r = df.format(valueOf);
          return r;
	}
	

}
