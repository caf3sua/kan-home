package com.opisvn.kanhome.service.util;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;

public class KanhomeUtil {

	public static final int CODE_LENGHT = 5;
	
	public static String generateSmsCode() {
		String code = StringUtils.EMPTY;
		Random rn = new Random();

		for (int i = 0; i < CODE_LENGHT; i++) {
			int answer = rn.nextInt(9) + 1;
			code = code + answer;
		}

		return code;
	}
	
	public static boolean isMyanmarPhoneNumber(String phonenumber) {
		if (StringUtils.isEmpty(phonenumber)) {
			return false;
		}
		
		// trim space
		if (StringUtils.trim(phonenumber).contains("+95") || phonenumber.indexOf("95") == 0) {
			return true;
		}
		
		return false;
	}
	
	public static boolean isVietnamPhoneNumber(String phonenumber) {
		if (StringUtils.isEmpty(phonenumber)) {
			return false;
		}
		
		// trim space
		if (StringUtils.trim(phonenumber).contains("+84") || phonenumber.indexOf("0") == 0 || phonenumber.indexOf("84") == 0) {
			return true;
		}
		
		return false;
	}
	
	public static String formattVietnamPhoneNumber(String phonenumber) {
		if (StringUtils.isEmpty(phonenumber)) {
			return StringUtils.EMPTY;
		}
		
		// trim space
		if (StringUtils.trim(phonenumber).contains("+84")) {
			return StringUtils.trim(phonenumber).replace("+84", "0");
		}
		
		return phonenumber;
	}
}
