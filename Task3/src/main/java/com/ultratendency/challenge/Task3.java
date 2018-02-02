package com.ultratendency.challenge;

import java.time.LocalDate;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Task3 {
	private static final String OPT_TABLE = "table";
	private static final String OPT_MODE = "mode";
	private static final String OPT_DATE = "date";

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("This program needs at least 2 arguments");
			System.out.println("-table <TABLE_NAME> -mode [highest|amount]");
	        return;
	    }
		Options options = new Options();
		options.addOption(OPT_TABLE, true, "Table name");
		options.addOption(OPT_MODE, true, "run mode [highest|amount]");
		options.addOption(OPT_DATE, false, "Date [YYYY-MM-DD]");
		
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
		
		String tableName = cmd.getOptionValue(OPT_TABLE);
		String mode = cmd.getOptionValue(OPT_MODE);
		String[] modes = mode.split("\\|");
		
		for(String element : modes) {
			Temperature temperature = new Temperature();
			if(element.equals("highest")) {
				if(cmd.hasOption(OPT_DATE)) {
					LocalDate date = LocalDate.parse(cmd.getOptionValue(OPT_DATE));
					Day day = new Day(date);
					temperature.getHighestTemperature(tableName, day);
				}
				else {
					temperature.getHighestTemperature(tableName);
				}
				
			}
			if(element.equals("amount")) {
				temperature.getAmoutOfTemperature(tableName);
			}
		}
		
	}
}
