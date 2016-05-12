package com.Cmpe273.ServerSla;

public class ApplicationStats {

	private int _requestCount;
	private int _latency;
	private int _maxThreads;
	private int _activeconnections;
	private int _currentThreadCount;
	private int _currentThreadBusy;
	
	
	public int getRequestCount()
	{
		return _requestCount;
	}
	
	public void setRequestCount(int value)
	{
		_requestCount= value;
	}
	
	public int getLatency()
	{
		return _latency;
	}
	
	public void setLatency(int value)
	{
		_latency=value;
	}
	
	public int getMaxThreads()
	{
		return _maxThreads;
	}
	
	public void setMaxThreads(int value)
	{
		_maxThreads=value;
	}
	
	public int getThreadCount()
	{
		return _currentThreadCount;
	}
	
	public void setThreadCount(int value)
	{
		_currentThreadCount=value;
	}
	public int getThreadusy()
	{
		return _currentThreadBusy;
	}
	
	public void setThreadBusy(int value)
	{
		_currentThreadBusy=value;
	}
	
	public int getActiveConnections()
	{
		return _activeconnections;
	}
	
	public void setActiveConnections(int activeconnections)
	{
		_activeconnections=activeconnections;
	}
}
