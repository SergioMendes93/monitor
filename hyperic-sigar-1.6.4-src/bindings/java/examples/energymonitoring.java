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
	static String ip = "192.168.1.172";

    public static void main(String[] args) throws Exception {
//		getIP();
		SendInfoHostRegistry();
//		MonitorResourceUsage();

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
	public static void MonitorResourceUsage() throws Exception{
		while(true) {
			double firstCPUMeasure = getCPU();	
			double firstMemoryMeasure = getMemory();
			
			Thread.sleep(1000); //we wait and make a second measure then average them to avoid 100,0,100,0 peaks. In this case the average should give 50 idealy

			double finalCPUMeasure = (firstCPUMeasure + getCPU()) / 2;
			double finalMemoryMeasure = (firstMemoryMeasure + getMemory()) / 2;

			//divide by 100 to get values from 0-1
			double CPUResult = Math.abs(lastCPUMeasureSent - finalCPUMeasure); 
			double MemoryResult = Math.abs(lastMemoryMeasureSent - finalMemoryMeasure);

			System.out.println("CPURESULT = " + CPUResult);
			System.out.println("MEmoryResult = " + MemoryResult);
			//if the first condition is true we send both information in one message avoiding sending two messages, one for the cpu update and the other for the memory update
			if((CPUResult > threshold) && (MemoryResult > threshold)) {
				sendUpdate(finalCPUMeasure, finalMemoryMeasure, 1);
				lastCPUMeasureSent = finalCPUMeasure;
				lastMemoryMeasureSent = finalMemoryMeasure;				
			} else if(CPUResult > threshold) {
				sendUpdate(finalCPUMeasure, finalMemoryMeasure, 2);				
				lastCPUMeasureSent = finalCPUMeasure;
			} else if(MemoryResult > threshold) {
				sendUpdate(finalCPUMeasure, finalMemoryMeasure, 3);				
				lastMemoryMeasureSent = finalMemoryMeasure;				
			}
			Thread.sleep(1000); //we wait before trying to send another message 
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
