package com.opisvn.kanhome.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.opisvn.kanhome.socket.message.KanhomeMessage;

@Component
public class KanHomeMqttCallback implements MqttCallback {
	private final Logger log = LoggerFactory.getLogger(KanHomeMqttCallback.class);
	
	private SimpMessagingTemplate template;
	
    public KanHomeMqttCallback(SimpMessagingTemplate template) {
        this.template = template;
    }
    
	@Override
	public void connectionLost(Throwable cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		log.debug("Topic {}, message {}", topic, message);
		final byte[] payload = message.getPayload();
		String messageMqtt = new String(payload);
		//messageMqtt = messageMqtt.replaceAll("\"", "");
		JSONObject jsonObj = new JSONObject(messageMqtt);
		
		KanhomeMessage kanhomeMessage = new KanhomeMessage();
		kanhomeMessage.setTopic(topic);
		kanhomeMessage.setMessage(messageMqtt);
		kanhomeMessage.setData(jsonObj);
		
		this.template.convertAndSend("/topic/iWater", kanhomeMessage);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// TODO Auto-generated method stub
		
	}

	public SimpMessagingTemplate getTemplate() {
		return template;
	}

	public void setTemplate(SimpMessagingTemplate template) {
		this.template = template;
	}
}
