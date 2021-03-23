package com.test32.common.util;

import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.management.*;


@Component
public class CommonDebugUtil
{
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	long logStartTime;
	long logCheckPointTime;

	public long getLogStartTime() {
		return logStartTime;
	}

	public void setLogStartTime()
	{
		logStartTime = System.currentTimeMillis();
		logCheckPointTime = System.currentTimeMillis();
	}

	public String getTotalElapsedTime()
	{
		Float elapsed = ( System.currentTimeMillis() - logStartTime)/1000.0f;
		return elapsed.toString();
	}

	public String getElapsedTime()
	{
		long interval = System.currentTimeMillis() - logCheckPointTime;

		logCheckPointTime = System.currentTimeMillis();

		Float elapsed = interval / 1000.0f;
		return elapsed.toString();
	}

	public void showCPU( )
	{
		OperatingSystemMXBean osbean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean( );
		RuntimeMXBean runbean = ManagementFactory.getRuntimeMXBean();
		long bfprocesstime = osbean.getProcessCpuTime( );
		long bfuptime = runbean.getUptime( );
		long ncpus = osbean.getAvailableProcessors( );
		for ( int i = 0 ; i < 1000000 ; ++i )
		{
			ncpus = osbean.getAvailableProcessors( );
		}
		long afprocesstime = osbean.getProcessCpuTime( );
		long afuptime = runbean.getUptime( );
		float cal = ( afprocesstime - bfprocesstime ) / ( ( afuptime - bfuptime ) * 10000f );
		float usage = Math.min( 99f , cal );
		logger.info("Calculation: " + cal);
		logger.info("CPU Usage: " + usage);
	}

	public void showMemory( )
	{
		MemoryMXBean membean = ManagementFactory.getMemoryMXBean();
		MemoryUsage heap = membean.getHeapMemoryUsage();
		logger.info("Heap Memory: " + heap.toString());
		MemoryUsage nonheap = membean.getNonHeapMemoryUsage( );
		logger.info("NonHeap Memory: " + nonheap.toString());

	}
	public void showClassLoading( )
	{
		ClassLoadingMXBean classbean = ManagementFactory.getClassLoadingMXBean( );
		logger.info("TotalLoadedClassCount: " + classbean.getTotalLoadedClassCount());
		logger.info("LoadedClassCount: " + classbean.getLoadedClassCount());
		logger.info("UnloadedClassCount: " + classbean.getUnloadedClassCount());
	}

	public void showThreadBean( )
	{
		ThreadMXBean tbean = ManagementFactory.getThreadMXBean( );
		long[] ids = tbean.getAllThreadIds( );
		logger.info("Thread Count: " + tbean.getThreadCount());
		for ( long id : ids )
		{
			logger.info("Thread CPU Time(" + id + ")" + tbean.getThreadCpuTime(id));
			logger.info("Thread User Time(" + id + ")" + tbean.getThreadCpuTime(id));
		}
	}

	public void showOSBean( )
	{
		OperatingSystemMXBean osbean = ( OperatingSystemMXBean ) ManagementFactory.getOperatingSystemMXBean( );
		logger.info("OS Name: " + osbean.getName());
		logger.info("OS Arch: " + osbean.getArch());
		logger.info("Available Processors: " + osbean.getAvailableProcessors());
		logger.info("TotalPhysicalMemorySize: " + osbean.getTotalPhysicalMemorySize());
		logger.info("FreePhysicalMemorySize: " + osbean.getFreePhysicalMemorySize());
		logger.info("TotalSwapSpaceSize: " + osbean.getTotalSwapSpaceSize());
		logger.info("FreeSwapSpaceSize: " + osbean.getFreeSwapSpaceSize());
		logger.info("CommittedVirtualMemorySize: " + osbean.getCommittedVirtualMemorySize());
		logger.info("SystemLoadAverage: " + osbean.getSystemLoadAverage());
	}
}
