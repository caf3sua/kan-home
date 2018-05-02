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
import com.opisvn.kanhome.service.util.KanhomeUtil;
import com.opisvn.kanhome.service.util.SpeedSMSAPI;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class SmsServiceImpl implements SmsService {
	
	private final Logger log = LoggerFactory.getLogger(SmsServiceImpl.class);
	
	private final Environment env;

    public SmsServiceImpl(Environment env) {
		this.env = env;
    }

	@Override
	public boolean sendSMS(String toNumber, String smsCode) {
		try {
			// No send if Myanmar country
			if (KanhomeUtil.isMyanmarPhoneNumber(toNumber)) {
				log.debug("No sendSMS, toNumber {} smsCode {}, Myanmar country phone number", toNumber, smsCode);
				return false;
			}
			
			// Get properties value
			log.debug("sendSMS, toNumber {}, smsCode {}", toNumber, smsCode);
			// SMS Vietnam
			if (KanhomeUtil.isVietnamPhoneNumber(toNumber)) {
				return sendVietnamSMS(toNumber, smsCode);
			} else {
				// International SMS
				return sendInternationalSMS(toNumber, smsCode);
			}
		} catch (Exception e) {
			log.error("ERROR Send sms, " + e.getMessage(), e);
			return false;
		}
	}
	
	private boolean sendVietnamSMS(String toNumber, String smsCode) {
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
	
	private boolean sendInternationalSMS(String toNumber, String smsCode) {
		String ACCOUNT_SID = env.getProperty("spring.sms.twilio-provider.account-sid");
		String AUTH_TOKEN = env.getProperty("spring.sms.twilio-provider.auth-token");

		String content = env.getProperty("spring.sms.provider.message");
		String sendMessage = String.format(content, smsCode);
		
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

	    Message message = Message
	        .creator(new PhoneNumber(toNumber), "", sendMessage)
	        .create();

	    if (message.getErrorCode() == null) {
	    	log.error("Send international SMS fail, {}", message);
	    	return true;
	    } else {
	    	return false;
	    }
	}
   

}
