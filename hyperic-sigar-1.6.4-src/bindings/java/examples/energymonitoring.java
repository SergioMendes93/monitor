import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;
import java.net.*;

import org.hyperic.sigar.*;

public class energymonitoring {

	static double  lastCPUMeasureSent = 0.0;
	static double  lastMemoryMeasureSent = 0.0;
	static double  threshold = 3.0;
	static String ip = "192.168.1.154";

    public static void main(String[] args) throws Exception {
//		getIP();
		while(true) {
			double firstCPUMeasure = getCPU();	
			System.out.println("First CPU " + firstCPUMeasure);
			double firstMemoryMeasure = getMemory();
			
			Thread.sleep(1000); //we wait and make a second measure then average them to avoid 100,0,100,0 peaks. In this case the average should give 50 idealy

			double finalCPUMeasure = (firstCPUMeasure + getCPU()) / 2;
			double finalMemoryMeasure = (firstMemoryMeasure + getMemory()) / 2;
			System.out.println("Average CPU " + finalCPUMeasure);

			double CPUResult = Math.abs(lastCPUMeasureSent - finalCPUMeasure);
			double MemoryResult = Math.abs(lastMemoryMeasureSent - finalMemoryMeasure);

			System.out.println("CPURESULT = " + CPUResult);
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
			url = "http://192.168.1.154:12345/host/updateboth/"+ip+"&"+String.valueOf(cpu)+"&"+String.valueOf(memory);
		} else if(messageType == 2) {
			url = "http://192.168.1.154:12345/host/updatecpu/"+ip+"&"+String.valueOf(cpu);
		} else {
			url = "http://192.168.1.154:12345/host/updatememory/"+ip+"&"+String.valueOf(memory);
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
		
		return perc.getCombined()*100;	
	}
	public static double getMemory() throws Exception {
		Sigar sigar = new Sigar();
		Mem mem = sigar.getMem();
		return mem.getUsedPercent();
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
