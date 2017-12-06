package com.huawei.bi.util;

public class UIUtil {
	public static String display(Object obj) {
		if (obj == null) {
			return "";
		} else {
			return String.valueOf(obj);
		}
	}
	
	public static void main(String[] args) {
		UIUtil.display(3);
	}
}
