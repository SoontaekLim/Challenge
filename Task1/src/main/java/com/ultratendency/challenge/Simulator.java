package com.ultratendency.challenge;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.ultratendency.challenge.model.Device;


public class Simulator {

	private Topic topic;
    private boolean isStop = false;
    
    public void setTopic(Topic topic) {
		this.topic = topic;
	}

	
    
    public void simulate(Device devices) {
    	//run simulator
    	//stub
    }
    
    //start service
    public void start() throws Exception {
    	if(topic == null) throw new Exception("topic is null");
    	ThreadFactory threadFactory = Executors.defaultThreadFactory();
    	Thread thread = threadFactory.newThread(new Runnable() {
			
			@Override
			public void run() {
				DatabaseConnector db = DatabaseConnector.getInstance();
				while(!isStop()) {
					try {
						
						List<Device> devices = db.getDevices();
						for(Device device : devices) {
							simulate(device);
							
							//send signal
							topic.runProducer(device.getDeviceId(), device);
						}
						
						
						Thread.sleep(1000);
						
						
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
    	
    	
    	thread.start();
    	thread.join();
    	
    }
  
    
    public boolean isStop() {
    	return isStop;
    }
    
    public void setStop(boolean value) {
    	this.isStop = value;
    }
    
    
   
}
