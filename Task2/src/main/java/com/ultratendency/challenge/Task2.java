package com.ultratendency.challenge;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Task2 {
	private static final String OPT_TABLE = "table";
	private static final String OPT_TOPIC = "topic";
	private static final String OPT_SERVER = "server";
	
	public static void main(String[] args) throws Exception {
    	
		if (args.length < 6) {
			System.out.println("This program needs 3 arguments");
			System.out.println("-server <BOOTSTRAP_SERVERS> -table <TABLE_NAME> -topic <TOPIC>");
			return;
	    }
		Options options = new Options();
		options.addOption(OPT_TABLE, true, "Table name");
		options.addOption(OPT_TOPIC, true, "Kafka topic id");
		options.addOption(OPT_SERVER, true, "host server");
		
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
		
		final String tableName = cmd.getOptionValue(OPT_TABLE);
		final String topic = cmd.getOptionValue(OPT_TOPIC);
		final String server = cmd.getOptionValue(OPT_SERVER);
		
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
		Thread thread = threadFactory.newThread(new Runnable() {
			
			@Override
			public void run() {
				SparkStreaming kafka = new SparkStreaming();
				kafka.setServers(server).setTableName(tableName).setTopic(topic);
				try {
					kafka.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		thread.start();
		thread.join();
		
	}

}
