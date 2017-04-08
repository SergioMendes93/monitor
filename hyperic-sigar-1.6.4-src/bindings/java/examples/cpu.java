import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;


//import org.hyperic.sigar.*;

public class cpu {

    public static void main(String[] args) throws Exception{
        	Runtime rt = Runtime.getRuntime();
           	Process pr = rt.exec("docker stats ce848be9d4ed --format \"{{.MemPerc}}\" --no-stream ");
BufferedReader stdInput = new BufferedReader(new 
     InputStreamReader(pr.getInputStream()));


// read the output from the command
System.out.println("Here is the standard output of the command:\n");
String s = null;
while ((s = stdInput.readLine()) != null) {
	System.out.println("aqui");
    System.out.println(s);
}


/*
	System.out.println("irq: " + cpu.getIrq());
	System.out.println("nice: " + cpu.getNice());
	System.out.println("soft irq: " + cpu.getSoftIrq());
	System.out.println("stolen: " + cpu.getStolen());
	System.out.println("sys: " + cpu.getSys());
	System.out.println("total: " + cpu.getTotal());
	System.out.println("user: " + cpu.getUser());
	System.out.println();


	CpuPerc perc = sigar.getCpuPerc();
	System.out.println("overall CPU usage");
	System.out.println("system idle: " + perc.getIdle());//get current CPU idle rate
	System.out.println("conbined: "+ perc.getCombined());//get current CPU usage


	CpuPerc[] cpuPercs = sigar.getCpuPercList();
	System.out.println("each CPU usage");
	for (CpuPerc cpuPerc : cpuPercs) {
		System.out.println("system idle: " + cpuPerc.getIdle());//get current CPU idle rate
		System.out.println("conbined: "+ cpuPerc.getCombined());//get current usage
		System.out.println();*/
/*   try {
        Sigar sigar = new Sigar();

	Cpu cpu = sigar.getCpu();
	System.out.println("idle: " + cpu.getIdle());//get overall CPU idle

	double[] buga = sigar.getLoadAverage();
	System.out.println(buga[0]);
	System.out.println(buga[1]);
	System.out.println(buga[2]);
    } catch (SigarException ex) {
        ex.printStackTrace();
    }*/
	}
}
