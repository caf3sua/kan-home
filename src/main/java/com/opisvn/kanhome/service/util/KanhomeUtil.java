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
}
