package com.Cmpe273.ClientServiceLevelAgreement;

public interface ClientObserver {

	 public String update();
     
	    //attach with subject to observe
	    public void setSubject(ServerLatency serverLatency);
	    
	    public int sendGet() ;
	
	
}
