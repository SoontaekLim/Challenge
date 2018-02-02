package com.ultratendency.challenge;


import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import com.ultratendency.challenge.hbase.Database;
import com.ultratendency.challenge.model.Device;
import com.ultratendency.challenge.serialization.DeviceDeserializer;

public class SparkStreaming implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tableName;
	private String groupId = "Challenge"; //default value
	private String servers;
	private String topic;
	
	private static final String APP_NAME = "ChallengeTask2";
	private static final String MASTER_VALUE = "local[3]";
	

	public SparkStreaming setTableName(String tableName) {
		this.tableName = tableName;
		return this;
	}

	public SparkStreaming setGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	public SparkStreaming setServers(String servers) {
		this.servers = servers;
		return this;
	}

	public SparkStreaming setTopic(String topic) {
		this.topic = topic;
		return this;
	}
	
	
	private Map<String, Object> getParams() throws Exception {
		if(servers == null) throw new Exception("server value is null");
		
		Map<String, Object> kafkaParams = new HashMap<>();
		kafkaParams.put("bootstrap.servers", servers);
		kafkaParams.put("key.deserializer",  StringDeserializer.class);
		kafkaParams.put("value.deserializer", DeviceDeserializer.class);
		kafkaParams.put("group.id", groupId);
		kafkaParams.put("auto.offset.reset", "latest");
		kafkaParams.put("enable.auto.commit", false);
		
		return kafkaParams;
	}
	
	public void run() throws Exception {
	    
		Store store = new Store();
		store.setTableName(tableName);
			
			Collection<String> topics = Arrays.asList(topic);
			
			SparkConf sparkConf = new SparkConf().setMaster(MASTER_VALUE).setAppName(APP_NAME);
			JavaStreamingContext streamingContext = new JavaStreamingContext(sparkConf, Durations.seconds(1));
			
			JavaInputDStream<ConsumerRecord<String, Device>> stream =
			  KafkaUtils.createDirectStream(
			    streamingContext,
			    LocationStrategies.PreferConsistent(),
			    ConsumerStrategies.<String, Device>Subscribe(topics, getParams())
			  );

			JavaDStream<String> lines = stream.map(new Function<ConsumerRecord<String, Device>, String>() {

				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				@Override
				public String call(ConsumerRecord<String, Device> v1) throws Exception {
					System.out.printf("offset = %d, key = %s, value = %s, partition=%d", v1.offset(), v1.key(), v1.value(),v1.partition());
					System.out.println("Key : " + v1.key() + " Value : " + v1.value().getTemperature());
					
					//store to HBase
					if(v1.value() != null) store.put(v1.value()); //storeToDB(v1.value());
					
					return  v1.key();
				}
			});
			
			lines.print();
			
			streamingContext.start();
			streamingContext.awaitTermination();
			
	}
	
	private void storeToDB(Device device) throws Exception {
		
	}
	
}
