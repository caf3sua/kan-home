package com.opisvn.kanhome.service;

public interface SmsService {

	public boolean sendSMS(String toNumber, String smsCode);
}
