package com.ultratendency.challenge;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.TopicExistsException;
import org.apache.kafka.common.serialization.StringSerializer;

import com.ultratendency.challenge.model.Device;
import com.ultratendency.challenge.serialization.DeviceSerializer;

public class Topic {
	private final static String CLIENT_ID = "ChallengeTask1";
	
	private String topicName;
    private int numPartitions = -1;
    private int numReplication = -1;
    private String bootstrapServers;
    
    
    public Topic setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
		return this;
	}

	public Topic setTopicName(String topicName) {
		this.topicName = topicName;
		return this;
	}

	public Topic setNumPartitions(int numPartitions) {
		this.numPartitions = numPartitions;
		return this;
	}

	public Topic setNumReplication(int numReplication) {
		this.numReplication = numReplication;
		return this;
	}
	
	public String getTopicName() {
		return topicName;
	}

	public int getNumPartitions() {
		return numPartitions;
	}

	public int getNumReplication() {
		return numReplication;
	}

	private Properties getProperties() {
		Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
        							bootstrapServers);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        							StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        							DeviceSerializer.class.getName());
        
        return props;
	}

	public boolean createTopic() throws Exception{
    	if(topicName == null || numPartitions < 0 || numReplication < 0) return false;
    	
    	//check the topic
    	if(isExsist()) return true;
    	
    	boolean result = true;
    	Properties props = getProperties();
        AdminClient adminClient = AdminClient.create(props);
        
        NewTopic topic = new NewTopic(topicName, numPartitions, (short)numReplication);
        CreateTopicsResult createTopicsResult = adminClient.createTopics(Collections.singleton(topic));

        try {
            createTopicsResult.all().get();
        } catch (ExecutionException e) {
        	result = false;
        	
            if (e.getCause() instanceof TopicExistsException) {
                System.err.println("Topic already exists !!");
            } else if (e.getCause() instanceof TimeoutException) {
                System.err.println("Timeout !!");
            }
            e.printStackTrace();
        } finally {
            adminClient.close();
        }
        
        return result;
    }
	
	private boolean isExsist() {
		boolean result = false;
		AdminClient adminClient = AdminClient.create(getProperties());
		ListTopicsResult topics = adminClient.listTopics();
		try {
			result = topics.namesToListings().get().containsKey(topicName);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} finally {
			adminClient.close();
		}
		
		
		return result;
	}
	
	private Producer<String, Device> createProducer() {
        Properties props = getProperties();
        return  new KafkaProducer<>(props);
    }
	
	void runProducer(String key, Device value) throws Exception  {
		Producer<String, Device> producer = createProducer();
		try {
			ProducerRecord<String, Device> record =
					new ProducerRecord<>(topicName, key,
							value);
			producer.send(record);


		} finally {
			if(producer == null) return;

			producer.flush();
			producer.close();
		}

	}
	
	
}
