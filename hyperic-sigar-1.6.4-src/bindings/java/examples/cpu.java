import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;


import org.hyperic.sigar.*;

public class cpu {

    public static void main(String[] args) throws Exception{
/*
	Sigar sigar = new Sigar();

	Cpu cpu = sigar.getCpu();
	System.out.println("idle: " + cpu.getIdle());//get overall CPU idle
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
   try {
        final Sigar sigar = new Sigar();
        while (true) {
            ProcCpu cpu = sigar.getProcCpu(4977);
            System.out.println(cpu.getPercent());
	    Thread.sleep(2000); //we wait before trying to send another message 
        }
    } catch (SigarException ex) {
        ex.printStackTrace();
    }
}
}
