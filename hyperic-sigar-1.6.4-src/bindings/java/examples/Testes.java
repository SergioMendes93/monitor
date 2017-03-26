/*
 * Copyright (c) 2006 Hyperic, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//import org.hyperic.sigar.*;
import java.lang.management.*;
import java.util.HashMap;
import java.util.Collection;
import com.sun.management.*;
 import java.lang.management.RuntimeMXBean;
//import javax.management.*;
import java.io.*;
/*

Example to show the process state for a given pid.

Compile the example:
% javac -classpath sigar-bin/lib/sigar.jar ProcessState.java

State of the java process running the example:
% java -classpath sigar-bin/lib/sigar.jar:. ProcessState
java: Running

State of the bash shell when invoking the example is running:
% java -classpath sigar-bin/lib/sigar.jar:. ProcessState $$
bash: Sleeping

State of emacs editor used to write the example:
% java -classpath sigar-bin/lib/sigar.jar:. ProcessState 2673
emacs: Suspended

See also: examples/Ps.java, examples/Top.java

*/

public class Testes {

    public static void main(String[] args) {
		MBeanServerConnection mbsc = ManagementFactory.getPlatformMBeanServer();

	try {
		OperatingSystemMXBean osMBean = ManagementFactory.newPlatformMXBeanProxy(mbsc, ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
	}catch (IOException e) { System.out.println(e);}
	long nanoBefore = System.nanoTime();
	long cpuBefore = osMBean.getProcessCpuTime();

// Call an expensive task, or sleep if you are monitoring a remote process

	long cpuAfter = osMBean.getProcessCpuTime();
	long nanoAfter = System.nanoTime();

	long percent;
	if (nanoAfter > nanoBefore)
 		percent = ((cpuAfter-cpuBefore)*100L)/(nanoAfter-nanoBefore);
	else percent = 0;

	System.out.println("Cpu usage: "+percent+"%");
}
}
