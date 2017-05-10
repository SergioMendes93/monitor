import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import java.net.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import org.json.simple.JSONObject;
import org.hyperic.sigar.*;

public class energymonitoring {

	static double  lastCPUMeasureSent = 0.0;
	static double  lastMemoryMeasureSent = 0.0;
	static double  threshold = 0.1;
	static String ipHostRegistry = "146.193.41.142";
	static String ip = "";

	static int BUFFER_POSITIONS = 20;
	static int TIME_BETWEEN_SAMPLES = 3*1000; //3 seconds
	static int TIME_BETWEEM_SENDING_SAMPLES = (BUFFER_POSITIONS * TIME_BETWEEN_SAMPLES) / 2;

	static HashMap<String,Task> currentTasks = new HashMap<String,Task>();

    	public static void main(String[] args) throws Exception {
		ip = getIP();
		//ip = getIP();
		SendInfoHostRegistry();
		Thread t1 = new ThreadMonitorHost();
		t1.start();
		
		Thread t2 = new ThreadMonitorTask();
		t2.start();
	}

	//thread responsible for monitoring task resource usage
	static class ThreadMonitorTask extends Thread {

		@Override
		public void run() {
			while(true) {
				for(int i = 0; i < BUFFER_POSITIONS; i++) {
					try {	
						getTaskInfo(i);
						Thread.sleep(TIME_BETWEEN_SAMPLES);
					}catch(Exception e) {}
				}

				ArrayList<String> tasksToRemove = new ArrayList<String>();

				for(Map.Entry<String, Task> entry: currentTasks.entrySet()) {
					Task task = entry.getValue();
					String taskID = entry.getKey();

					//if the task no longer exists, remove it
					if(!task.alive){
						tasksToRemove.add(taskID);
						continue;
					}
					currentTasks.get(taskID).alive = false;

					double avgCPU = task.currentCPU / BUFFER_POSITIONS;
					double avgMemory = task.currentMemory / BUFFER_POSITIONS;

					//we compare the last sent value with the new one to see if it is worth sending it
					double CPUResult = Math.abs(task.lastCPU - avgCPU); 
					double MemoryResult = Math.abs(task.lastMemory - avgMemory);
					
					//if the first condition is true we send both information in one message avoiding sending two messages, one for the cpu update and the other for the memory update
                	try {
						if((CPUResult > threshold) && (MemoryResult > threshold)) {
                        	sendUpdateTask(avgCPU, avgMemory, 1, taskID);
	                    	currentTasks.get(taskID).lastCPU = avgCPU;
    	                	currentTasks.get(taskID).lastMemory = avgMemory;
        	        	} else if(CPUResult > threshold) {
                        	sendUpdateTask(avgCPU, avgMemory, 2, taskID);
	                    	currentTasks.get(taskID).lastCPU = avgCPU;
                		} else if(MemoryResult > threshold) {
                        	sendUpdateTask(avgCPU, avgMemory, 3, taskID);
	                    	currentTasks.get(taskID).lastMemory = avgMemory;
						}
                	}catch(Exception e) {}
				}
				
				//removing tasks that have ended so they are no longer monitored
				for (String taskID: tasksToRemove)
					currentTasks.remove(taskID);
				
			}
		}
	}

		public static void getTaskInfo(int i) throws Exception{
    	    Runtime rt = Runtime.getRuntime();
        	Process pr = rt.exec(new String[]{"docker","stats", "--format", "\"{{.ID}} {{.CPUPerc}} {{.MemPerc}}\"", "--no-stream"});      

        	BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

    	    String s = null;
        	while ((s = input.readLine()) != null) {
            	String[] parts = s.split(" "); //we split by spaces and we will get [0] = taskID [1]=cpuPerc [2]=memPerc

				String[] partsTask = parts[0].split("\"");
				String taskID = partsTask[1];
        		
				String[] partsCPU = parts[1].split("%");
            	double cpuValue = Double.parseDouble(partsCPU[0]);

				String[] partsMemory = parts[2].split("%");
            	double memoryValue = Double.parseDouble(partsMemory[0]);

				try {
					if( i == 0) {
						currentTasks.get(taskID).currentCPU = cpuValue / 100;
						currentTasks.get(taskID).currentMemory = memoryValue / 100;
					} else{
						currentTasks.get(taskID).currentCPU += cpuValue / 100;
						currentTasks.get(taskID).currentMemory += memoryValue / 100;
					}
				}
				catch(Exception e) {
					currentTasks.put(taskID,new Task());
					currentTasks.get(taskID).currentCPU = cpuValue / 100;
					currentTasks.get(taskID).currentMemory = memoryValue / 100;
				}
				currentTasks.get(taskID).alive = true;
        	}
		}
	
	//thread responsible for monitoring host resource usage
	static class ThreadMonitorHost extends Thread {
		@Override
		public void run(){
			while(true) {
				double sumMemory = 0.0;
				double sumCPU = 0.0;										

				for(int i = 0; i < BUFFER_POSITIONS; i++) {
					try{
						sumCPU += getCPU();
						sumMemory += getMemory();

						Thread.sleep(TIME_BETWEEN_SAMPLES);
					}catch(Exception e) {}
				}
				double avgCPU = sumCPU / BUFFER_POSITIONS;
				double avgMemory = sumMemory / BUFFER_POSITIONS;

				//we compare the last sent value with the new one to see if it is worth sending it
				double CPUResult = Math.abs(lastCPUMeasureSent - avgCPU); 
				double MemoryResult = Math.abs(lastMemoryMeasureSent - avgMemory);

				//if the first condition is true we send both information in one message avoiding sending two messages, one for the cpu update and the other for the memory update
				try {
					if((CPUResult > threshold) && (MemoryResult > threshold)) {
						sendUpdate(avgCPU, avgMemory, 1);
						lastCPUMeasureSent = avgCPU;
						lastMemoryMeasureSent = avgMemory;				
					} else if(CPUResult > threshold) {
						sendUpdate(avgCPU, avgMemory, 2);				
						lastCPUMeasureSent = avgCPU;
					} else if(MemoryResult > threshold) {
						sendUpdate(avgCPU, avgMemory, 3);				
						lastMemoryMeasureSent = avgMemory;				
					}
				}catch(Exception e) {}
			} 

		}
	}

	//this method is responsible for sending information of this host to the host registry such as total cpus, total memory
	public static void SendInfoHostRegistry() throws Exception{
		String numCPUs = String.valueOf(Runtime.getRuntime().availableProcessors());		
		Sigar sigar = new Sigar();
        	Mem mem = sigar.getMem();
		String totalMemory = String.valueOf(mem.getTotal());
		
        	String url = "http://"+ipHostRegistry+":12345/host/createhost/"+ip+"&"+totalMemory+"&"+numCPUs;
		try {
            		URL obj = new URL(url);
            		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            		int responseCode = con.getResponseCode();

            		BufferedReader in = new BufferedReader(
                    	new InputStreamReader(con.getInputStream()));       
        	}catch(Exception e) {}
	}

	public static void sendUpdateTask(double cpu, double memory, int messageType, String taskID) throws Exception{
	        String url = "";
        	if(messageType == 1){
            		url = "http://"+ip+":1234/task/updateboth/"+taskID+"&"+String.valueOf(cpu)+"&"+String.valueOf(memory);
        	} else if(messageType == 2) {
            		url = "http://"+ip+":1234/task/updatecpu/"+taskID+"&"+String.valueOf(cpu);
       		} else {
            		url = "http://"+ip+":1234/task/updatememory/"+taskID+"&"+String.valueOf(memory);
        	}
        	try {
            		URL obj = new URL(url);
            		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            		int responseCode = con.getResponseCode();

            		BufferedReader in = new BufferedReader(
                    	new InputStreamReader(con.getInputStream()));       
        	}catch(Exception e) { }
    	}

	//to host registry
	public static void sendUpdate(double cpu, double memory, int messageType) {
		String url = "";
		if(messageType == 1){
			url = "http://"+ipHostRegistry+":12345/host/updateboth/"+ip+"&"+String.valueOf(cpu)+"&"+String.valueOf(memory);
		} else if(messageType == 2) {
			url = "http://"+ipHostRegistry+":12345/host/updatecpu/"+ip+"&"+String.valueOf(cpu);
		} else {
			url = "http://"+ipHostRegistry+":12345/host/updatememory/"+ip+"&"+String.valueOf(memory);
		}
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(
		    	    new InputStreamReader(con.getInputStream()));		
		}catch(Exception e) {}
	}
	
	public static double getCPU() throws Exception {
		Sigar sigar = new Sigar();
		Cpu cpu = sigar.getCpu();
		CpuPerc perc = sigar.getCpuPerc();
		
		return perc.getCombined();	
	}
	public static double getMemory() throws Exception {
		Sigar sigar = new Sigar();
		Mem mem = sigar.getMem();
		return mem.getUsedPercent()/100;
	}
	
	public static String getIP() {
		try{
	//      String hostname = addr.getHostName(); EM VEZ DE PELO IP POSSO PORVENTURA IDENTIFICAR O HOST POR ESTE NOME
	//      System.out.println("Local host name: "+hostname);
			URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
                	whatismyip.openStream()));

			String ip = in.readLine(); //you get the IP as a String //this is the external IP address
			return ip;
		}
        	catch(Exception e){}
		return "";
	}
}

class Task {
	public double lastCPU = 0.0;
	public double lastMemory = 0.0;
	public double currentCPU = 0.0;	
	public double currentMemory = 0.0;
	public boolean alive = false;

}
