/*****************************************************
 * WARNING: this file was generated by -e
 * on Sat Mar 25 13:58:42 2017.
 * Any changes made here will be LOST.
 *****************************************************/
package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;

/**
 * CpuInfo sigar class.
 */
public class CpuInfo implements java.io.Serializable {

    private static final long serialVersionUID = 9710L;

    public CpuInfo() { }

    public native void gather(Sigar sigar) throws SigarException;

    /**
     * This method is not intended to be called directly.
     * use Sigar.getCpuInfo() instead.
     * @exception SigarException on failure.
     * @see org.hyperic.sigar.Sigar#getCpuInfo
     */
    static CpuInfo fetch(Sigar sigar) throws SigarException {
        CpuInfo cpuInfo = new CpuInfo();
        cpuInfo.gather(sigar);
        return cpuInfo;
    }

    String vendor = null;

    /**
     * Get the CPU vendor id.<p>
     * Supported Platforms: AIX, FreeBSD, Linux, HPUX, Solaris, Win32.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>lsattr -El proc0</code><br>
     * <li> Darwin: <code></code><br>
     * <li> FreeBSD: <code></code><br>
     * <li> HPUX: <code></code><br>
     * <li> Linux: <code>cat /proc/cpuinfo</code><br>
     * <li> Solaris: <code>psrinfo -v</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return CPU vendor id
     */
    public String getVendor() { return vendor; }
    String model = null;

    /**
     * Get the CPU model.<p>
     * Supported Platforms: AIX, FreeBSD, Linux, HPUX, Solaris, Win32.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>lsattr -El proc0</code><br>
     * <li> Darwin: <code></code><br>
     * <li> FreeBSD: <code></code><br>
     * <li> HPUX: <code></code><br>
     * <li> Linux: <code>cat /proc/cpuinfo</code><br>
     * <li> Solaris: <code>psrinfo -v</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return CPU model
     */
    public String getModel() { return model; }
    int mhz = 0;

    /**
     * Get the CPU speed.<p>
     * Supported Platforms: AIX, FreeBSD, HPUX, Linux, Solaris, Win32.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>lsattr -El proc0</code><br>
     * <li> Darwin: <code></code><br>
     * <li> FreeBSD: <code></code><br>
     * <li> HPUX: <code></code><br>
     * <li> Linux: <code>cat /proc/cpuinfo</code><br>
     * <li> Solaris: <code>psrinfo -v</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return CPU speed
     */
    public int getMhz() { return mhz; }
    long cacheSize = 0;

    /**
     * Get the CPU cache size.<p>
     * Supported Platforms: AIX, Linux.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>lsattr -El proc0</code><br>
     * <li> Darwin: <code></code><br>
     * <li> FreeBSD: <code></code><br>
     * <li> HPUX: <code></code><br>
     * <li> Linux: <code>cat /proc/cpuinfo</code><br>
     * <li> Solaris: <code>psrinfo -v</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return CPU cache size
     */
    public long getCacheSize() { return cacheSize; }
    int totalCores = 0;

    /**
     * Get the Total CPU cores (logical).<p>
     * Supported Platforms: Undocumented.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>lsattr -El proc0</code><br>
     * <li> Darwin: <code></code><br>
     * <li> FreeBSD: <code></code><br>
     * <li> HPUX: <code></code><br>
     * <li> Linux: <code>cat /proc/cpuinfo</code><br>
     * <li> Solaris: <code>psrinfo -v</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return Total CPU cores (logical)
     */
    public int getTotalCores() { return totalCores; }
    int totalSockets = 0;

    /**
     * Get the Total CPU sockets (physical).<p>
     * Supported Platforms: Undocumented.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>lsattr -El proc0</code><br>
     * <li> Darwin: <code></code><br>
     * <li> FreeBSD: <code></code><br>
     * <li> HPUX: <code></code><br>
     * <li> Linux: <code>cat /proc/cpuinfo</code><br>
     * <li> Solaris: <code>psrinfo -v</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return Total CPU sockets (physical)
     */
    public int getTotalSockets() { return totalSockets; }
    int coresPerSocket = 0;

    /**
     * Get the Number of CPU cores per CPU socket.<p>
     * Supported Platforms: Undocumented.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>lsattr -El proc0</code><br>
     * <li> Darwin: <code></code><br>
     * <li> FreeBSD: <code></code><br>
     * <li> HPUX: <code></code><br>
     * <li> Linux: <code>cat /proc/cpuinfo</code><br>
     * <li> Solaris: <code>psrinfo -v</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return Number of CPU cores per CPU socket
     */
    public int getCoresPerSocket() { return coresPerSocket; }

    void copyTo(CpuInfo copy) {
        copy.vendor = this.vendor;
        copy.model = this.model;
        copy.mhz = this.mhz;
        copy.cacheSize = this.cacheSize;
        copy.totalCores = this.totalCores;
        copy.totalSockets = this.totalSockets;
        copy.coresPerSocket = this.coresPerSocket;
    }

    public Map toMap() {
        Map map = new HashMap();
        String strvendor = 
            String.valueOf(this.vendor);
        if (!"-1".equals(strvendor))
            map.put("Vendor", strvendor);
        String strmodel = 
            String.valueOf(this.model);
        if (!"-1".equals(strmodel))
            map.put("Model", strmodel);
        String strmhz = 
            String.valueOf(this.mhz);
        if (!"-1".equals(strmhz))
            map.put("Mhz", strmhz);
        String strcacheSize = 
            String.valueOf(this.cacheSize);
        if (!"-1".equals(strcacheSize))
            map.put("CacheSize", strcacheSize);
        String strtotalCores = 
            String.valueOf(this.totalCores);
        if (!"-1".equals(strtotalCores))
            map.put("TotalCores", strtotalCores);
        String strtotalSockets = 
            String.valueOf(this.totalSockets);
        if (!"-1".equals(strtotalSockets))
            map.put("TotalSockets", strtotalSockets);
        String strcoresPerSocket = 
            String.valueOf(this.coresPerSocket);
        if (!"-1".equals(strcoresPerSocket))
            map.put("CoresPerSocket", strcoresPerSocket);
        return map;
    }

    public String toString() {
        return toMap().toString();
    }

}
