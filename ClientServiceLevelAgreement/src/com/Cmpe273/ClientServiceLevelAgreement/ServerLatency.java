package com.Cmpe273.ClientServiceLevelAgreement;

public interface ServerLatency {

	public void register(ClientObserver clientObserverObj);
    public void unregister(ClientObserver clientObserverObj);
     
    //method to notify observers of change
    public void notifyObservers();
     
    //method to get updates from subject
    public Object getUpdate(ClientObserver clientObserverObj);
	
	
}
