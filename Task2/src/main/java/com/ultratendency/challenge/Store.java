package com.ultratendency.challenge;

import java.io.Serializable;
import java.util.Date;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;

import com.ultratendency.challenge.hbase.Database;
import com.ultratendency.challenge.model.Device;

public class Store implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tableName = null;
	
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	
	
	public void put(Device device) throws Exception {
		if(tableName == null) throw new Exception("Table name is null");
		Database db = Database.getInstance();
		try {
			db.connect();
			db.put(tableName, getPutData(device));
		} finally {
			db.close();
		}
		
	}
	
	public Put getPutData(Device device) {
		Put put = new Put(Bytes.toBytes(device.getDeviceId()));
		put.addColumn(Bytes.toBytes(Device.TEMPERATURE), Bytes.toBytes(System.currentTimeMillis()), Bytes.toBytes((long)device.getTemperature()));
		put.addColumn(Bytes.toBytes(Device.LOCATION), Bytes.toBytes(Device.LATITUDE), Bytes.toBytes(device.getLatitude()));
		put.addColumn(Bytes.toBytes(Device.LOCATION), Bytes.toBytes(Device.LONGITUDE), Bytes.toBytes(device.getLongitude()));
		
		//convert the timestamp to readable value
		Date date = new Date(device.getTime());
		put.addColumn(Bytes.toBytes(Device.TIME), Bytes.toBytes(""), Bytes.toBytes(date.toString()));
		
		return put;
	}
	
}
