package com.ultratendency.challenge;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.coprocessor.AggregationClient;
import org.apache.hadoop.hbase.client.coprocessor.LongColumnInterpreter;
import org.apache.hadoop.hbase.util.Bytes;

import com.ultratendency.challenge.hbase.Database;
import com.ultratendency.challenge.model.Device;


public class Temperature {

	Database db = Database.getInstance();
	
	public Map<String, Integer> getAmoutOfTemperature(String tableName) throws Exception {
		HashMap<String, Integer> amount = new HashMap<>();
		
		db.connect();
		ResultScanner scanner = db.getResultScanner(tableName);
		for (Result result = scanner.next(); (result != null); result = scanner.next()) {
			NavigableMap familyMap = result.getFamilyMap(Bytes.toBytes(Device.TEMPERATURE));
			String key = Bytes.toString(result.getRow());
			int size = familyMap.size();
			System.out.println("ID:" + key + ", size:" + size);
		    amount.put(key, size);
		    
		}
		return amount;
	}

	public Map<String, Integer> getHighestTemperature(String tableName) throws IOException {
		return getHighestTemperature(tableName, null);
	}
	
	public HashMap<String, Integer> getHighestTemperature(String tableName, Day day) throws IOException {
		HashMap<String, Integer> result = new HashMap<>();
		
		db.connect();
		
		AggregationClient aClient = new AggregationClient(db.getConfig());
		
		try {
			
			Object[] keys = db.getRowKeys(tableName).toArray();
			TableName table = TableName.valueOf(tableName);
			
			for(int i = 0, len = keys.length; i < len; i++) {
				Scan scan;
				if(i == len -1) {
					scan = new Scan((byte[])keys[i]);
				}
				else {
					scan = new Scan((byte[])keys[i], (byte[])keys[i+1]);
				}
				
				if(day != null) {
					System.out.println("start:" + day.getStartOfDay().getTime() + ", end:" + day.getEndOfDay().getTime());
					scan.setTimeRange(day.getStartOfDay().getTime(), day.getEndOfDay().getTime());
				}
				
				scan.addFamily(Bytes.toBytes(Device.TEMPERATURE));
				
				Long max = aClient.max(table, new LongColumnInterpreter(), scan);
				
				System.out.println("ID:" + Bytes.toString((byte[])keys[i]) + ", highest temperature:" + max);
				
				//If there is no data in the table, it gets null
				if(max == null) continue;
				
				result.put(Bytes.toString((byte[])keys[i]), Math.toIntExact(max));
			}
			
			
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			aClient.close();
			db.close();
		}
		
		return result;
	}
}
