package com.ultratendency.challenge;

import java.util.ArrayList;
import java.util.List;

import com.ultratendency.challenge.model.Device;

public class DatabaseConnector {

	private static DatabaseConnector instance = new DatabaseConnector();
	
	private static Device device1;
	private static Device device2;
	private static Device device3;
	
	private DatabaseConnector() {
		
	}
	
	public static DatabaseConnector getInstance() {
		init();
		return instance;
	}
	
	public List<Device> getDevices() {
    	//get devices from database
    	
    	// stub
    	// ***********************************************************
		
		if(device1.getTemperature() < 100 ) {
			device1.setTemperature(device1.getTemperature() + 1);
		}
		else {
			device1.setTemperature(device1.getTemperature() - 1);
		}
    	device1.setTime(System.currentTimeMillis());
    	
    	device2.setTemperature(device1.getTemperature() + 1);
    	device2.setTime(System.currentTimeMillis());
    	
    	device3.setTemperature(device1.getTemperature() - 1);
    	device3.setTime(System.currentTimeMillis());
    	
    	List<Device> devices = new ArrayList<>();
    	devices.add(device1);
    	devices.add(device2);
    	devices.add(device3);
    	// ***********************************************************
    	
    	return devices;
    }
	
	private static void init() {
		// stub
    	// ***********************************************************
    	device1 = new Device();
    	device1.setDeviceId("11c1310e-c0c2-461b-a4eb-f6bf8da2d23c");
    	device1.setTemperature(12);
    	device1.setLatitude(52.14691120000001);
    	device1.setLongitude(11.658838699999933);
    	device1.setTime(System.currentTimeMillis());
    	
    	device2 = new Device();
    	device2.setDeviceId("11c1310e-c0c2-461b-a4eb-f6bf8da2d23d");
    	device2.setTemperature(15);
    	device2.setLatitude(-52.14691120000001);
    	device2.setLongitude(11.658838699999933);
    	device2.setTime(System.currentTimeMillis());
    	
    	device3 = new Device();
    	device3.setDeviceId("11c1310e-c0c2-461b-a4eb-f6bf8da2d23e");
    	device3.setTemperature(16);
    	device3.setLatitude(58.14691120000001);
    	device3.setLongitude(21.658838699999933);
    	device3.setTime(System.currentTimeMillis());
    	
    	// ***********************************************************
	}
}
