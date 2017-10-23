package com.opisvn.kanhome.schedule.task;

import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.StringUtils;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.opisvn.kanhome.mqtt.TLSSocketFactory;


@Component
public class FacebookScheduledTasks {

//	@Autowired
//	private MqttAsyncClient mqttAsyncClient;
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 20000)
    // @Scheduled(cron = "*/5 * * * * *")
    // see: http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/scheduling/support/CronSequenceGenerator.html
    public void reportCurrentTime() throws MqttException {
//    	String topic        = "notification/1";
//        String content      = "new notification" + System.currentTimeMillis();
//        int qos             = 0;
//        
//        mqttAsyncClient.setCallback(this);
//        mqttAsyncClient.subscribe(topic, qos);
    }

//	public MqttAsyncClient getMqttAsyncClient() {
//		return mqttAsyncClient;
//	}
//
//	public void setMqttAsyncClient(MqttAsyncClient mqttAsyncClient) {
//		this.mqttAsyncClient = mqttAsyncClient;
//	}
}
