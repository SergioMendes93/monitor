/*****************************************************
 * WARNING: this file was generated by -e
 * on Sat Mar 25 13:58:42 2017.
 * Any changes made here will be LOST.
 *****************************************************/
package org.hyperic.sigar;

import java.util.HashMap;
import java.util.Map;

/**
 * FileSystemUsage sigar class.
 */
public class FileSystemUsage implements java.io.Serializable {

    private static final long serialVersionUID = 18905L;

    public FileSystemUsage() { }

    public native void gather(Sigar sigar, String name) throws SigarException;

    /**
     * This method is not intended to be called directly.
     * use Sigar.getFileSystemUsage() instead.
     * @exception SigarException on failure.
     * @see org.hyperic.sigar.Sigar#getFileSystemUsage
     */
    static FileSystemUsage fetch(Sigar sigar, String name) throws SigarException {
        FileSystemUsage fileSystemUsage = new FileSystemUsage();
        fileSystemUsage.gather(sigar, name);
        return fileSystemUsage;
    }

    long total = 0;

    /**
     * Get the Total Kbytes of filesystem.<p>
     * Supported Platforms: All.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>df</code><br>
     * <li> Darwin: <code>df</code><br>
     * <li> FreeBSD: <code>df</code><br>
     * <li> HPUX: <code>df</code><br>
     * <li> Linux: <code>df</code><br>
     * <li> Solaris: <code>df</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return Total Kbytes of filesystem
     */
    public long getTotal() { return total; }
    long free = 0;

    /**
     * Get the Total free Kbytes on filesystem.<p>
     * Supported Platforms: All.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>df</code><br>
     * <li> Darwin: <code>df</code><br>
     * <li> FreeBSD: <code>df</code><br>
     * <li> HPUX: <code>df</code><br>
     * <li> Linux: <code>df</code><br>
     * <li> Solaris: <code>df</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return Total free Kbytes on filesystem
     */
    public long getFree() { return free; }
    long used = 0;

    /**
     * Get the Total used Kbytes on filesystem.<p>
     * Supported Platforms: All.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>df</code><br>
     * <li> Darwin: <code>df</code><br>
     * <li> FreeBSD: <code>df</code><br>
     * <li> HPUX: <code>df</code><br>
     * <li> Linux: <code>df</code><br>
     * <li> Solaris: <code>df</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return Total used Kbytes on filesystem
     */
    public long getUsed() { return used; }
    long avail = 0;

    /**
     * Get the Total free Kbytes on filesystem available to caller.<p>
     * Supported Platforms: All.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>df</code><br>
     * <li> Darwin: <code>df</code><br>
     * <li> FreeBSD: <code>df</code><br>
     * <li> HPUX: <code>df</code><br>
     * <li> Linux: <code>df</code><br>
     * <li> Solaris: <code>df</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return Total free Kbytes on filesystem available to caller
     */
    public long getAvail() { return avail; }
    long files = 0;

    /**
     * Get the Total number of file nodes on the filesystem.<p>
     * Supported Platforms: AIX, Darwin, FreeBSD, HPUX, Linux, Solaris.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>df</code><br>
     * <li> Darwin: <code>df</code><br>
     * <li> FreeBSD: <code>df</code><br>
     * <li> HPUX: <code>df</code><br>
     * <li> Linux: <code>df</code><br>
     * <li> Solaris: <code>df</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return Total number of file nodes on the filesystem
     */
    public long getFiles() { return files; }
    long freeFiles = 0;

    /**
     * Get the Number of free file nodes on the filesystem.<p>
     * Supported Platforms: AIX, Darwin, FreeBSD, HPUX, Linux, Solaris.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>df</code><br>
     * <li> Darwin: <code>df</code><br>
     * <li> FreeBSD: <code>df</code><br>
     * <li> HPUX: <code>df</code><br>
     * <li> Linux: <code>df</code><br>
     * <li> Solaris: <code>df</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return Number of free file nodes on the filesystem
     */
    public long getFreeFiles() { return freeFiles; }
    long diskReads = 0;

    /**
     * Get the Number of physical disk reads.<p>
     * Supported Platforms: AIX, FreeBSD, HPUX, Linux, Solaris, Win32.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>df</code><br>
     * <li> Darwin: <code>df</code><br>
     * <li> FreeBSD: <code>df</code><br>
     * <li> HPUX: <code>df</code><br>
     * <li> Linux: <code>df</code><br>
     * <li> Solaris: <code>df</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return Number of physical disk reads
     */
    public long getDiskReads() { return diskReads; }
    long diskWrites = 0;

    /**
     * Get the Number of physical disk writes.<p>
     * Supported Platforms: AIX, FreeBSD, HPUX, Linux, Solaris, Win32.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>df</code><br>
     * <li> Darwin: <code>df</code><br>
     * <li> FreeBSD: <code>df</code><br>
     * <li> HPUX: <code>df</code><br>
     * <li> Linux: <code>df</code><br>
     * <li> Solaris: <code>df</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return Number of physical disk writes
     */
    public long getDiskWrites() { return diskWrites; }
    long diskReadBytes = 0;

    /**
     * Get the Number of physical disk bytes read.<p>
     * Supported Platforms: Undocumented.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>df</code><br>
     * <li> Darwin: <code>df</code><br>
     * <li> FreeBSD: <code>df</code><br>
     * <li> HPUX: <code>df</code><br>
     * <li> Linux: <code>df</code><br>
     * <li> Solaris: <code>df</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return Number of physical disk bytes read
     */
    public long getDiskReadBytes() { return diskReadBytes; }
    long diskWriteBytes = 0;

    /**
     * Get the Number of physical disk bytes written.<p>
     * Supported Platforms: Undocumented.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>df</code><br>
     * <li> Darwin: <code>df</code><br>
     * <li> FreeBSD: <code>df</code><br>
     * <li> HPUX: <code>df</code><br>
     * <li> Linux: <code>df</code><br>
     * <li> Solaris: <code>df</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return Number of physical disk bytes written
     */
    public long getDiskWriteBytes() { return diskWriteBytes; }
    double diskQueue = 0;

    /**
     * Get the disk_queue.<p>
     * Supported Platforms: Undocumented.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>df</code><br>
     * <li> Darwin: <code>df</code><br>
     * <li> FreeBSD: <code>df</code><br>
     * <li> HPUX: <code>df</code><br>
     * <li> Linux: <code>df</code><br>
     * <li> Solaris: <code>df</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return disk_queue
     */
    public double getDiskQueue() { return diskQueue; }
    double diskServiceTime = 0;

    /**
     * Get the disk_service_time.<p>
     * Supported Platforms: Undocumented.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>df</code><br>
     * <li> Darwin: <code>df</code><br>
     * <li> FreeBSD: <code>df</code><br>
     * <li> HPUX: <code>df</code><br>
     * <li> Linux: <code>df</code><br>
     * <li> Solaris: <code>df</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return disk_service_time
     */
    public double getDiskServiceTime() { return diskServiceTime; }
    double usePercent = 0;

    /**
     * Get the Percent of disk used.<p>
     * Supported Platforms: All.
     * <p>
     * System equivalent commands:<ul>
     * <li> AIX: <code>df</code><br>
     * <li> Darwin: <code>df</code><br>
     * <li> FreeBSD: <code>df</code><br>
     * <li> HPUX: <code>df</code><br>
     * <li> Linux: <code>df</code><br>
     * <li> Solaris: <code>df</code><br>
     * <li> Win32: <code></code><br>
     * </ul>
     * @return Percent of disk used
     */
    public double getUsePercent() { return usePercent; }

    void copyTo(FileSystemUsage copy) {
        copy.total = this.total;
        copy.free = this.free;
        copy.used = this.used;
        copy.avail = this.avail;
        copy.files = this.files;
        copy.freeFiles = this.freeFiles;
        copy.diskReads = this.diskReads;
        copy.diskWrites = this.diskWrites;
        copy.diskReadBytes = this.diskReadBytes;
        copy.diskWriteBytes = this.diskWriteBytes;
        copy.diskQueue = this.diskQueue;
        copy.diskServiceTime = this.diskServiceTime;
        copy.usePercent = this.usePercent;
    }

    public Map toMap() {
        Map map = new HashMap();
        String strtotal = 
            String.valueOf(this.total);
        if (!"-1".equals(strtotal))
            map.put("Total", strtotal);
        String strfree = 
            String.valueOf(this.free);
        if (!"-1".equals(strfree))
            map.put("Free", strfree);
        String strused = 
            String.valueOf(this.used);
        if (!"-1".equals(strused))
            map.put("Used", strused);
        String stravail = 
            String.valueOf(this.avail);
        if (!"-1".equals(stravail))
            map.put("Avail", stravail);
        String strfiles = 
            String.valueOf(this.files);
        if (!"-1".equals(strfiles))
            map.put("Files", strfiles);
        String strfreeFiles = 
            String.valueOf(this.freeFiles);
        if (!"-1".equals(strfreeFiles))
            map.put("FreeFiles", strfreeFiles);
        String strdiskReads = 
            String.valueOf(this.diskReads);
        if (!"-1".equals(strdiskReads))
            map.put("DiskReads", strdiskReads);
        String strdiskWrites = 
            String.valueOf(this.diskWrites);
        if (!"-1".equals(strdiskWrites))
            map.put("DiskWrites", strdiskWrites);
        String strdiskReadBytes = 
            String.valueOf(this.diskReadBytes);
        if (!"-1".equals(strdiskReadBytes))
            map.put("DiskReadBytes", strdiskReadBytes);
        String strdiskWriteBytes = 
            String.valueOf(this.diskWriteBytes);
        if (!"-1".equals(strdiskWriteBytes))
            map.put("DiskWriteBytes", strdiskWriteBytes);
        String strdiskQueue = 
            String.valueOf(this.diskQueue);
        if (!"-1".equals(strdiskQueue))
            map.put("DiskQueue", strdiskQueue);
        String strdiskServiceTime = 
            String.valueOf(this.diskServiceTime);
        if (!"-1".equals(strdiskServiceTime))
            map.put("DiskServiceTime", strdiskServiceTime);
        String strusePercent = 
            String.valueOf(this.usePercent);
        if (!"-1".equals(strusePercent))
            map.put("UsePercent", strusePercent);
        return map;
    }

    public String toString() {
        return toMap().toString();
    }

}
