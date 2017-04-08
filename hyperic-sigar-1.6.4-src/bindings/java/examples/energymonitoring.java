import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import java.net.*;

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
		Thread t1 = new ThreadMonitorHost();
		Thread t2 = new ThreadMonitorTask();
		//t1.start();
		t2.start();
	}

	//thread responsible for monitoring task resource usage
	static class ThreadMonitorTask extends Thread {
		@Override
		public void run() {
			try {
				double cpu = getTaskCPU();
				double memory = getTaskMemory();
				System.out.println(cpu);
				System.out.println(memory);
				sendUpdateTask(cpu,memory,1);
			}catch(Exception e) {System.out.println(e);}
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
				if((CPUResult > threshold) && (MemoryResult > threshold)) {
					try {
						sendUpdate(avgCPU, avgMemory, 1);
					}catch(Exception e) {
						System.out.println(e);
					}
					lastCPUMeasureSent = avgCPU;
					lastMemoryMeasureSent = avgMemory;				
				} else if(CPUResult > threshold) {
					try {
						sendUpdate(avgCPU, avgMemory, 2);				
					} catch(Exception e) {System.out.println(e);}
					lastCPUMeasureSent = avgCPU;
				} else if(MemoryResult > threshold) {
					try {
						sendUpdate(avgCPU, avgMemory, 3);				
					} catch(Exception e) {System.out.println(e);}
					lastMemoryMeasureSent = avgMemory;				
				}
			} 

		}
	}

	public static double getTaskCPU() throws Exception{
		Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("docker stats ad9f54d3deb7 --format \"{{.CPUPerc}}\" --no-stream ");
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

	public static double getTaskMemory() throws Exception{
		Runtime rt = Runtime.getRuntime();
        Process pr = rt.exec("docker stats ad9f54d3deb7 --format \"{{.MemPerc}}\" --no-stream ");
		BufferedReader stdInput = new BufferedReader(new 
     	InputStreamReader(pr.getInputStream()));

		double memoryValue = 0.0;
		String s = null;
		int i = 0;
		while ((s = stdInput.readLine()) != null) {
			String[] parts = s.split("%");
			String[] parts1 = parts[0].split("\"");
			memoryValue = Double.parseDouble(parts1[1]);
			return memoryValue;
		}
		System.out.println("AQUI");

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
