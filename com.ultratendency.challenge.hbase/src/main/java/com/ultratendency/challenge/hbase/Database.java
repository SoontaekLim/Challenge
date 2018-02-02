package com.ultratendency.challenge.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;

/**
 * @author Soontaek Lim
 *
 */
public class Database {
	Connection connection = null;
	Configuration config = null;
	
	private Database() {
	}
	
	public static Database getInstance() {
		return new Database();
	}
	
	public void connect() throws IOException {
		connection = ConnectionFactory.createConnection(getConfig());
	}
	
	public Configuration getConfig() {
		if(config == null) config = HBaseConfiguration.create();
		
		return config;
	}
	
	public void close() throws IOException {
		if(connection != null) connection.close();
	}
	
	public ResultScanner getResultScanner(String tableName) throws Exception {
		Scan scan = new Scan();
		return getResultScanner(tableName, scan);
	}
	
	
	public ResultScanner getResultScanner(String tableName, Scan scan) throws Exception {
		if(tableName == null) throw new Exception("Table name is null");
		if(connection == null) throw new Exception("Database connection is null");
		if(connection.isClosed()) throw new Exception("Database connection is close");
		
		Table table = connection.getTable(TableName.valueOf(tableName));
		
		return table.getScanner(scan);
	}
	
	/**
	 * Get data from the table
	 * @param tableName
	 * @param get
	 * @return
	 * @throws Exception
	 */
	public Result get(String tableName, Get get) throws Exception {
		if(tableName == null) throw new Exception("Table name is null");
		if(connection == null) throw new Exception("Database connection is null");
		if(connection.isClosed()) throw new Exception("Database connection is close");
		
		Table table = connection.getTable(TableName.valueOf(tableName));
		Result result = table.get(get);
		
		return result;
	}
	
	/**
	 * Input the data to the table
	 * @param tableName
	 * @param put
	 * @throws Exception
	 */
	public void put(String tableName, Put put) throws Exception {
		if(tableName == null) throw new Exception("Table name is null");
		if(connection == null) throw new Exception("Database connection is null");
		if(connection.isClosed()) throw new Exception("Database connection is close");
		
		Table table = connection.getTable(TableName.valueOf(tableName));
		table.put(put);
	}
	

	/**
	 * @param tableName
	 * @return List<byte[]> It's a list of keys in the table
	 * @throws Exception
	 */
	public List<byte[]> getRowKeys(String tableName) throws Exception {
		List<byte[]> keys = new ArrayList<>();
		ResultScanner scanner = getResultScanner(tableName);
		for(Result result : scanner) {
			keys.add(result.getRow());
		}
		
		return keys;
	}
	
}
