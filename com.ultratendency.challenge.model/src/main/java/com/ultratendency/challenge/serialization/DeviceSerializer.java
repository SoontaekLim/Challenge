package com.ultratendency.challenge.serialization;

import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.kafka.common.serialization.Serializer;

import com.ultratendency.challenge.model.Device;

public class DeviceSerializer implements Serializer<Device>{

	@Override
	public void configure(Map<String, ?> configs, boolean isKey) {
		// nothing to do
	}
	@Override
	public byte[] serialize(String topic, Device data) {
		if (data == null)
			return null;
		else
			return SerializationUtils.serialize(data);
	}
	@Override
	public void close() {
		// nothing to do
	}
}
