# Cmpe273_Marvels

Folder Structure:
-----------------
1. ServerSla:
	- APIs to fetch system status(CPU utilization, memory utlization etc) and application status(Latency, Throughput, Threads count etc)
	- Website to fecth the system health status and application status dynamically from the APIs and display it.(SIGAR API)
	
	
2. Client:(ClientServiceLevelAgreement)

  	Circuit breaker: It checks the response headers and keeps it open if a certain number of error responses 
  	are received from the server responses. 
  	It waits for a specified amount of time and then goes to half open state and 
  	then to open state on successful responses from the server.
	
	  Client program to send concurrent requests to the server and receive responses. 
	  Take decisions to communicate with the server based on the response headers.(eg Retry-After,Latency etc)
	  
	  
3. DashBoard for rendering the system as well server performance parameters using bootstrap and jquery and Ajax
    
    System Health- Realtime Dyanmic Display of  CPU utilization and Memory Utilization parameters using googgle charts and  
    twitter bootsrap 
    
    Server Config =Realtime Dyanmic information of concurrent threads, active sessions ,Request Count per second ,Throughput of the system etc

	
