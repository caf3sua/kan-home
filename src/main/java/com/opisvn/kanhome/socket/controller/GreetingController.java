package com.opisvn.kanhome.socket.controller;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.opisvn.kanhome.socket.message.Greeting;
import com.opisvn.kanhome.socket.message.HelloMessage;
import com.opisvn.kanhome.socket.message.KanhomeMessage;

@Controller
public class GreetingController {
	private final Logger log = LoggerFactory.getLogger(GreetingController.class);
	
	private MqttAsyncClient mqttAsyncClient;
	
	public GreetingController(MqttAsyncClient mqttAsyncClient) {
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
	    	log.debug("MQTT request subscribe iWater, message {}", message);
	    	IMqttToken result = mqttAsyncClient.subscribe("iwater/" + message.getDeviceId(), 0);
	    	log.debug("MQTT request subscribe iWater result {}", result);
    }
    
    @MessageMapping("/iheater")
    public void iHeater(KanhomeMessage message) throws Exception {
	    	log.debug("MQTT request subscribe iWater, message {}", message);
	    	IMqttToken result = mqttAsyncClient.subscribe("iheater/" + message.getDeviceId(), 0);
	    	log.debug("MQTT request subscribe iWater result {}", result);
    }
}
