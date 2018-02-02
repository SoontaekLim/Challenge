package com.ultratendency.challenge.serialization;

import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Deserializer;

import com.ultratendency.challenge.model.Device;


public class DeviceDeserializer implements Deserializer<Device> {

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// nothing to do
		
	}

	@Override
	public Device deserialize(String topic, byte[] data) {
		if (data == null)
			return null;
		else
			return SerializationUtils.deserialize(data);
	}

	@Override
	public void close() {
		// nothing to do
		
	}

}
