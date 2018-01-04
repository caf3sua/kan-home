package com.opisvn.kanhome.socket.controller;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.opisvn.kanhome.socket.message.Greeting;
import com.opisvn.kanhome.socket.message.HelloMessage;
import com.opisvn.kanhome.socket.message.KanhomeMessage;

@Controller
public class KanhomeMqttController {
	private final Logger log = LoggerFactory.getLogger(KanhomeMqttController.class);
	
	private MqttAsyncClient mqttAsyncClient;
	
	public KanhomeMqttController(MqttAsyncClient mqttAsyncClient) {
		this.mqttAsyncClient = mqttAsyncClient;
	}

	@MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + message.getName() + "!");
    }
	
//    @SendTo("/topic/notification")
//    public String notification(String message) throws Exception {
//        return "hello";
//    }
    
    
    @MessageMapping("/iWater")
    public void iWater(KanhomeMessage message) throws Exception {
    		if (!mqttAsyncClient.isConnected()) {
    			log.debug("MQTT request to reconnect server");
    			mqttAsyncClient.reconnect();
    			Thread.sleep(1000);
    		}
    	
	    	log.debug("MQTT request subscribe iWater, message {}", message);
	    	IMqttToken result = mqttAsyncClient.subscribe("iwater/" + message.getDeviceId(), 0);
	    	log.debug("MQTT request subscribe iWater result {}", result);
	    		    	
	    	
	    	// Publish iheater/jHXFos20/sub
//	    	if (result.isComplete()) {
	    		mqttAsyncClient.publish("iwater/" + message.getDeviceId() + "/sub" , getPublishMessage());
//	    	}
    }
    
    @MessageMapping("/iheater")
    public void iHeater(KanhomeMessage message) throws Exception {
    		if (!mqttAsyncClient.isConnected()) {
    			log.debug("MQTT request to reconnect server");
			mqttAsyncClient.reconnect();
			Thread.sleep(1000);
		}
    	
	    	log.debug("MQTT request subscribe iheater, message {}", message);
	    	IMqttToken result = mqttAsyncClient.subscribe("iheater/" + message.getDeviceId(), 0);
	    	log.debug("MQTT request subscribe iheater result {}", result);
	    	
	    	// Publish iheater/jHXFos20/sub
//	    	if (result.isComplete()) {
	    		log.debug("MQTT request publish iheater, deviceid:" + message.getDeviceId());
	    		mqttAsyncClient.publish("iheater/" + message.getDeviceId() + "/sub" , getPublishMessage());
//	    	}
    }
    
    private MqttMessage getPublishMessage() {
    	MqttMessage message = new MqttMessage();
        message.setPayload("{\"type\":\"RDAT\",\"value\":1}".getBytes());
        
        return message;
    }
}
