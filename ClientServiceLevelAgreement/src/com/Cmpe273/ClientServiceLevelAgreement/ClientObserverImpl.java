package com.Cmpe273.ClientServiceLevelAgreement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class ClientObserverImpl implements ClientObserver {

  
    private String name;
    private ServerLatency serverStatus;
    private final String USER_AGENT = "Mozilla/5.0";
    public Map<String,String> customHeaders=new HashMap<String,String> ();
 
     
    public ClientObserverImpl(String nm){
        this.name=nm;
    }
    @Override
    public String update() {
        String msg = (String) serverStatus.getUpdate(this);
        if(msg == null){
            System.out.println(name+":: No new message");
        }
          return msg;
        
    }
 
    @Override
    public void setSubject(ServerLatency serverLatency)
    {
        this.serverStatus=serverLatency;
    }
    
    
    public int sendGet() 
    {
    	 String url = "http://localhost:8080/ServerSla/ControllerServlet";
    
         int responseCode=0;
    
    try
    {
      URL obj = new URL(url);
      HttpURLConnection con = (HttpURLConnection) obj.openConnection();

      System.out.println(con);
      con.setRequestMethod("GET");
      con.setRequestProperty("User-Agent", USER_AGENT);
          

      responseCode = con.getResponseCode();
      
      String activeconnections=con.getHeaderField("ACTIVE_CONNECTIONS");
      String norequests=con.getHeaderField("REQUEST_COUNT");
      
      System.out.println(norequests);
      
      
      
      Map<String, List<String>> map = con.getHeaderFields();
       
          System.out.println("Printing All Response Header for URL: "
                  + obj.toString() + "\n");

          System.out.println("\nSending 'GET' request to URL : " + url);
          System.out.println("Printing input" +con.getInputStream());
      
          BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
          String inputLine;
          StringBuffer response = new StringBuffer();
          
        

      while ((inputLine = in.readLine()) != null) {
        response.append(inputLine);
      }
      in.close();
      
      clientLogic(map);

    
      System.out.println(response.toString());
      
    }catch(Exception e)
    {
      e.printStackTrace();
     }
    
    return responseCode;

  }
    
    
    private void clientLogic(Map<String, List<String>> map)
     {
       System.out.println("\n\n");
       System.out.println("Deciding client Logic now \n\n ");
       
      for (Map.Entry<String, List<String>> entry : map.entrySet()) {
             System.out.println(entry.getKey() + " : " + entry.getValue());
         }
     
     System.out.println("\nGet Response Header By Key ...\n");
         List<String> contentLength = map.get("Content-Length");
         List<String> latency = map.get("LATENCY");
         List<String> concurrent_threads = map.get("CONCURENT_THREADS");
         String revised_latency= latency.get(0);
         
         
         
         if (contentLength == null) 
         {
             System.out
             .println("'Content-Length' doesn't present in Header!");
         } else {
             for (String header : contentLength) {
                 System.out.println("Content-Length: " + header);
            }
         }
       
         
         if(Integer.parseInt(revised_latency)>10)
         {
          customHeaders.put("Keep_Alive", "5");
         }
     }
    
  
  
}
