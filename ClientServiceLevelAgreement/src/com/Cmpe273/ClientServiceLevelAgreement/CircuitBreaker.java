package com.Cmpe273.ClientServiceLevelAgreement;

import redis.clients.jedis.Jedis;

public class CircuitBreaker {

	 public static void main(String[] args) {
	        //create subject
	        ServerLatencyImpl serverStatus = new ServerLatencyImpl();
	         
	        //create observers
	        ClientObserver clientObserver1 = new ClientObserverImpl("Client1");
	        ClientObserver clientObserver2 = new ClientObserverImpl("Client2");
	        ClientObserver clientObserver3 = new ClientObserverImpl("Client3");	         
	        //register observers to the subject
	        serverStatus.register(clientObserver1);
	        serverStatus.register(clientObserver1);
	        serverStatus.register(clientObserver1);
	         
	        //attach observer to subject
	        clientObserver1.setSubject(serverStatus);
	        clientObserver2.setSubject(serverStatus);
	        clientObserver3.setSubject(serverStatus);
	        
	        //first get call
	        
	        clientObserver1.sendGet();
	        Jedis jedis = new Jedis("localhost");
	        String serverState=jedis.get("SERVER_STATUS");
	        try{
	        	 if (Integer.parseInt(serverState)>10)
	 	        {
	 	          serverStatus.postMessage("CLOSE");
	 	        
	 	        }
	 	       if(Integer.parseInt(serverState)>5 && Integer.parseInt(serverState)<10)
	 	        {
	 	        	serverStatus.postMessage("HALF_OPEN");
	 	        }
	 	       else serverStatus.postMessage("CLOSED");
	        }catch(Exception e)
	        {
	        	e.printStackTrace();
	        }
	       
	       
	        
	        String checkServerStatus=clientObserver1.update();
	        
	        if (checkServerStatus=="CLOSED"|| checkServerStatus=="HALF_OPEN")
	        {
	        	/*try {
	        		clientObserver1.sendGet();
		        	clientObserver2.sendGet();
		        	clientObserver3.sendGet();
	        	}
	        	catch(Exception e)
	        	{
	        		e.printStackTrace();
	        	}*/
	        	
	        	
	        }
	         
	  
	        
	    }
	
	
}
