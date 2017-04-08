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
	static String ip = "192.168.1.4";

	static int BUFFER_POSITIONS = 20;
	static int TIME_BETWEEN_SAMPLES = 3*1000; //3 seconds
	static int TIME_BETWEEM_SENDING_SAMPLES = (BUFFER_POSITIONS * TIME_BETWEEN_SAMPLES) / 2;

    public static void main(String[] args) throws Exception {
//		getIP();
//		SendInfoHostRegistry();
//		Thread t1 = new ThreadMonitorHost();
		//t1.start();
		receiveRequests();
                
	}

	//function used to deal with incoming GET or POST requests from other modules
	public static void receiveRequests() throws Exception{
		HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
    	server.createContext("/newtask", new MyHandler());
    	server.setExecutor(null); // creates a default executor
    	server.start();
	}

	static class MyHandler implements HttpHandler {
		@Override
    	public void handle(HttpExchange t) throws IOException {
			Thread thread = new ThreadMonitorTask(t);
			thread.start();
    	}
	}

	//thread responsible for monitoring task resource usage
	static class ThreadMonitorTask extends Thread {
		private String taskID;
		HttpExchange t;
		String query;
		double lastCPU = 0.0;
		double lastMemory = 0.0;

		public ThreadMonitorTask(HttpExchange t) {
			this.t = t;
			query = t.getRequestURI().getQuery();
			String[] parts = query.split("=");
			taskID = parts[1];
			System.out.println("Task ID received " + taskID);

		}		

		@Override
		public void run() {
			while(true) {
				double sumMemory = 0.0;
				double sumCPU = 0.0;

				for(int i = 0; i < BUFFER_POSITIONS; i++) {
					try {
						sumCPU += getTaskCPU(taskID);
						sumMemory += getTaskMemory(taskID);
						Thread.sleep(TIME_BETWEEN_SAMPLES);
					}catch(Exception e) {System.out.println(e);}
				}
				double avgCPU = sumCPU / BUFFER_POSITIONS;
				double avgMemory = sumMemory / BUFFER_POSITIONS;

				System.out.println("got all samples : " + avgCPU + " " + avgMemory);
				
				//we compare the last sent value with the new one to see if it is worth sending it
				double CPUResult = Math.abs(lastCPU - avgCPU); 
				double MemoryResult = Math.abs(lastMemory - avgMemory);
				

				//if the first condition is true we send both information in one message avoiding sending two messages, one for the cpu update and the other for the memory update
                try {
					if((CPUResult > threshold) && (MemoryResult > threshold)) {
                        sendUpdateTask(avgCPU, avgMemory, 1);
	                    lastCPU = avgCPU;
    	                lastMemory = avgMemory;
        	        } else if(CPUResult > threshold) {
                        sendUpdateTask(avgCPU, avgMemory, 2);
	                    lastCPU = avgCPU;
                	} else if(MemoryResult > threshold) {
                        sendUpdateTask(avgCPU, avgMemory, 3);
	                    lastMemory = avgMemory;
					}
                }catch(Exception e) {System.out.println(e);}
			}
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
					}catch(Exception e) {System.out.println(e);}
				}
				System.out.println("got all samples");
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
				}catch(Exception e) {System.out.println(e);}
			} 

		}
	}

	public static double getTaskCPU(String taskID) throws Exception{
		ProcessBuilder pb = new ProcessBuilder("docker", "stats",taskID, "--format", "{{.CPUPerc}}", "--no-stream");
        Map<String, String> env = pb.environment();
        // set environment variable u
        env.put("DOCKER_TLS_VERIFY", "1");
        env.put("DOCKER_HOST", "tcp://192.168.99.100:2376");
		//TODO: ESTES DOIS ABAIXO TEM QUE SER DINAMICOS, OU SEJA O WORKER VEM COMO ARGUMENTO
        env.put("DOCKER_CERT_PATH", "/home/sergiosmendes/.docker/machine/machines/manager");
        env.put("DOCKER_MACHINE_NAME", "manager");

        Process pr = pb.start();
        BufferedReader stdInput = new BufferedReader(new 
        InputStreamReader(pr.getInputStream()));

		double cpuValue = 0.0;
		String s = null;
		int i = 0;
		while ((s = stdInput.readLine()) != null) {
			String[] parts = s.split("%");
			String[] parts1 = parts[0].split("\"");
			cpuValue = Double.parseDouble(parts1[1]);
			return cpuValue;
		}
		return 0.0;
	}

	public static double getTaskMemory(String taskID) throws Exception{
		ProcessBuilder pb = new ProcessBuilder("docker", "stats",taskID, "--format", "{{.MemPerc}}", "--no-stream");
        Map<String, String> env = pb.environment();
        // set environment variable u
        env.put("DOCKER_TLS_VERIFY", "1");
        env.put("DOCKER_HOST", "tcp://192.168.99.100:2376");
        //TODO: ESTES DOIS ABAIXO TEM QUE SER DINAMICOS, OU SEJA O WORKER VEM COMO ARGUMENTO
        env.put("DOCKER_CERT_PATH", "/home/sergiosmendes/.docker/machine/machines/manager");
        env.put("DOCKER_MACHINE_NAME", "manager");

        Process pr = pb.start();
        BufferedReader stdInput = new BufferedReader(new 
        InputStreamReader(pr.getInputStream()));
		
		double memoryValue = 0.0;
		String s = null;
		int i = 0;
		while ((s = stdInput.readLine()) != null) {
			String[] parts = s.split("%");
			String[] parts1 = parts[0].split("\"");
			memoryValue = Double.parseDouble(parts1[1]);
			System.out.println("Reading");
			return memoryValue;
		}
		return 0.0;
	}

	//this method is responsible for sending information of this host to the host registry such as total cpus, total memory
	public static void SendInfoHostRegistry() throws Exception{
		String numCPUs = String.valueOf(Runtime.getRuntime().availableProcessors());		
		Sigar sigar = new Sigar();
        Mem mem = sigar.getMem();
		String totalMemory = String.valueOf(mem.getTotal());
		
        String url = "http://"+ip+":12345/host/createhost/10&"+totalMemory+"&"+numCPUs;
		try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));       
        }catch(Exception e) {
            System.out.println("Exception " + e);
        }
	}
	public static void sendUpdateTask(double cpu, double memory, int messageType) throws Exception{
        String url = "";
        if(messageType == 1){
            url = "http://"+ip+":1234/task/updateboth/1&"+String.valueOf(cpu)+"&"+String.valueOf(memory);
        } else if(messageType == 2) {
            url = "http://"+ip+":1234/task/updatecpu/1&"+String.valueOf(cpu);
        } else {
            url = "http://"+ip+":1234/task/updatememory/1&"+String.valueOf(memory);
        }
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));       
        }catch(Exception e) {
            System.out.println("Exception " + e);
        }
    }


	public static void sendUpdate(double cpu, double memory, int messageType) {
		String url = "";
		if(messageType == 1){
			url = "http://"+ip+":12345/host/updateboth/"+ip+"&"+String.valueOf(cpu)+"&"+String.valueOf(memory);
		} else if(messageType == 2) {
			url = "http://"+ip+":12345/host/updatecpu/"+ip+"&"+String.valueOf(cpu);
		} else {
			url = "http://"+ip+":12345/host/updatememory/"+ip+"&"+String.valueOf(memory);
		}
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			int responseCode = con.getResponseCode();

			BufferedReader in = new BufferedReader(
		    	    new InputStreamReader(con.getInputStream()));		
		}catch(Exception e) {
			System.out.println("Exception " + e);
		}
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
	
	public static void getIP() {
		try{
	//      String hostname = addr.getHostName(); EM VEZ DE PELO IP POSSO PORVENTURA IDENTIFICAR O HOST POR ESTE NOME
	//      System.out.println("Local host name: "+hostname);
		    URL whatismyip = new URL("http://checkip.amazonaws.com");
			BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

			String ip = in.readLine(); //you get the IP as a String //this is the external IP address
			System.out.println(ip);
		  }
        catch(Exception e){

        }
	}
}
