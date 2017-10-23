package com.opisvn.kanhome.service.impl;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.opisvn.kanhome.service.SmsService;
import com.opisvn.kanhome.service.util.SpeedSMSAPI;

@Service
public class SmsServiceImpl implements SmsService {
	
	private final Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);
	
	private final Environment env;

    public SmsServiceImpl(Environment env) {
		this.env = env;
    }

	@Override
	public boolean sendSMS(String toNumber, String smsCode) {
		// Get properties value
		log.debug("sendSMS, toNumber {}, smsCode {}", toNumber, smsCode);
	    String accessToken = env.getProperty("spring.sms.provider.accessToken");
	    String message = env.getProperty("spring.sms.provider.message");
		String sendMessage = String.format(message, smsCode);
		
	    SpeedSMSAPI api = new SpeedSMSAPI(accessToken);
		String json = "{\"to\": [\"" + toNumber + "\"], \"content\": \"" + sendMessage + "\", \"sms_type\": 1, \"brandname\":\"KanHome\"}";
		String result = null;
		try {
			result = api.sendSMS(json);
			System.out.println(result);
			
			JSONObject jsonObj = null;
	        try {
	            jsonObj = new JSONObject(result);
	            String status = (String) jsonObj.get("status");
	            if (StringUtils.equalsIgnoreCase(status, "success")) {
	            	return true;
	            }
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		return false;
	}
   

}
