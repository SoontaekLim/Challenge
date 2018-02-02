package com.ultratendency.challenge.model;

import java.io.Serializable;

public class Device implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String DEVICE_ID = "temperature";
	public static final String TEMPERATURE = "temperature";
	public static final String LOCATION = "location";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String TIME = "time";
	
	
	private String deviceId;
	private int temperature;
	private double latitude;
	private double longitude;
	private long time;
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public int getTemperature() {
		return temperature;
	}
	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double d) {
		this.latitude = d;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	
	
	
}
