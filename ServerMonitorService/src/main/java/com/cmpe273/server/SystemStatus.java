package com.cmpe273.server;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
//import org.hyperic.sigar.FileSystemUsage;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.ProcStat;
import org.hyperic.sigar.Sigar;
import org.hyperic.sigar.SigarException;
import org.json.JSONObject;


@Path("/SystemStatus")
public class SystemStatus {
	@GET
	//@Path("/{parameter}")
	public Response responseMsg(@PathParam("parameter") String parameter,
			@DefaultValue("Nothing to say") @QueryParam("value") String value) {
		// TODO Auto-generated method stub

		try {
			int logicalProcessors;
			long processes, threads;
			String vendor, model;
			double clock, cpuutilization, totalMemory, usedMemory, memUtilization;

			Sigar sigar = new Sigar();
			CpuInfo[] cpuInfoList = null;
			cpuInfoList = sigar.getCpuInfoList();
			CpuPerc cpuPerc = sigar.getCpuPerc();
			ProcStat procStat = sigar.getProcStat();
			Mem mem = null;
			mem = sigar.getMem();

			logicalProcessors = cpuInfoList.length;
			processes = procStat.getRunning();
			threads = procStat.getThreads();
			clock = cpuInfoList[0].getMhz();// In MHz
			vendor = cpuInfoList[0].getVendor();
			model = cpuInfoList[0].getModel();
			cpuutilization = Math.round((cpuPerc.getCombined() * 100) * 100.0) / 100.0;// Percentage

			totalMemory = mem.getTotal() / 1024 / 1024;// in MB
			usedMemory = mem.getUsed() / 1024 / 1024;// in MB
			memUtilization = Math.round(mem.getUsedPercent() * 100.0) / 100.0; // percentage

			// FileSystemUsage filesystemusage = sigar.getFileSystemUsage("C:");
			// System.out.print("\nFileSystem usage: " +
			// filesystemusage.getUsePercent());
			System.out.println("I am here!!Logical processors: "+logicalProcessors);

			JSONObject cpuHealthStatus = new JSONObject();
			JSONObject cpuObj = new JSONObject();
			cpuObj.put("LogicalProcessors", logicalProcessors);
			cpuObj.put("Vendor", vendor);
			cpuObj.put("Model", model);
			cpuObj.put("Clock", clock);
			cpuObj.put("Utilization", cpuutilization);
			cpuObj.put("Processes", processes);
			cpuObj.put("Threads", threads);
			cpuHealthStatus.put("CPU", cpuObj);

			JSONObject memObj = new JSONObject();
			memObj.put("Total", totalMemory);
			memObj.put("Used", usedMemory);
			memObj.put("Utilization", memUtilization);
			cpuHealthStatus.put("Memory", memObj);

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			cpuHealthStatus.put("LastUpdateTime", dateFormat.format(date));

			System.out.print(cpuHealthStatus);
			return Response.status(200).entity(cpuHealthStatus.toString()).build();
		} 
		catch(SigarException ex)
		{
			System.out.println("Sigar Exception occurred:"+ex.getMessage());
			return Response.status(200).entity(ex.getMessage()).build();
		}
		catch (Exception ex) {
			System.out.println("Exception occurred:"+ex.getMessage());
			return Response.status(200).entity(ex.getMessage()).build();
		}
	}
}
