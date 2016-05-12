package com.Cmpe273.ClientServiceLevelAgreement;


import java.util.ArrayList;
import java.util.List;

public class ServerLatencyImpl implements ServerLatency {

	private List<ClientObserver> clientObservers;
    private String message;
    private boolean changed;
    private final Object MUTEX= new Object();
     
    public ServerLatencyImpl(){
        this.clientObservers=new ArrayList<>();
    }
    @Override
    public void register(ClientObserver clientObserverObj) {
        if(clientObserverObj == null) throw new NullPointerException("Null Observer");
        synchronized (MUTEX) {
        if(!clientObservers.contains(clientObserverObj)) 
        	clientObservers.add(clientObserverObj);
        }
    }
 
    @Override
    public void unregister(ClientObserver clientObserverObj) {
        synchronized (MUTEX) {
        	clientObservers.remove(clientObserverObj);
        }
    }
    
    
    public void notifyObservers() {
        List<ClientObserver> observersLocal = null;
        //synchronization is used to make sure any observer registered after message is received is not notified
        synchronized (MUTEX) {
            if (!changed)
                return;
            observersLocal = new ArrayList<>(this.clientObservers);
            this.changed=false;
        }
        for (ClientObserver clientObserverObj : observersLocal) {
        	clientObserverObj.update();
        }
 
    }
 
    @Override
    public Object getUpdate(ClientObserver clientObserverObj) {
        return this.message;
    }
     
    //method to post message to the topic
    public void postMessage(String msg){
        System.out.println("Message Posted to Topic:"+msg);
        this.message=msg;
        this.changed=true;
        notifyObservers();
    }
	
	
}
