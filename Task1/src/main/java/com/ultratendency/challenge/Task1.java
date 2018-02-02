package com.ultratendency.challenge;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Task1 {

	private static final String OPT_TOPIC = "topic";
	private static final String OPT_SERVER = "server";
	private static final String OPT_PARTITION = "partition";
	private static final String OPT_REPLICATION = "replication";
	
	public static void main(String[] args) throws Exception {
		if (args.length < 8) {
			System.out.println("This program needs 4 arguments");
			System.out.println("-server <BOOTSTRAP_SERVERS> -topic <TOPIC> -partition <PARTITIONS_NUMBER> -replication <REPLICATION>");
			return;
	    }
		Options options = new Options();
		options.addOption(OPT_TOPIC, true, "Kafka topic id");
		options.addOption(OPT_SERVER, true, "host server");
		options.addOption(OPT_PARTITION, true, "size of partitions");
		options.addOption(OPT_REPLICATION, true, "number of replication");
		
		CommandLineParser parser = new GnuParser();
		CommandLine cmd;
		try {
			cmd = parser.parse( options, args);
			if (cmd.getArgs().length != 0) {
				throw new ParseException("Command takes no arguments");
			}
		} catch (ParseException pe) {
			pe.printStackTrace();
			return;
		}
		
		String servers =  cmd.getOptionValue(OPT_SERVER);
		String topicName = cmd.getOptionValue(OPT_TOPIC);
		int partitions = Integer.valueOf(cmd.getOptionValue(OPT_PARTITION));
		int replication = Integer.valueOf(cmd.getOptionValue(OPT_REPLICATION));
		
		
		Topic topic = new Topic();
    	topic.setBootstrapServers(servers)
    		.setTopicName(topicName)
    		.setNumPartitions(partitions)
    		.setNumReplication(replication);
    	topic.createTopic();
    	
    	Simulator simulator = new Simulator();
    	simulator.setTopic(topic);
    	simulator.start();
	}

}
