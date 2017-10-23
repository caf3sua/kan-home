package com.opisvn.kanhome.socket.message;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
public class KanhomeMessage {
	private String deviceId;
	
	private String topic;
	
	private String message;
	
	private MqttMessage mqttMessage;
	
	private Object data;

    public KanhomeMessage() {
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MqttMessage getMqttMessage() {
		return mqttMessage;
	}

	public void setMqttMessage(MqttMessage mqttMessage) {
		this.mqttMessage = mqttMessage;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
