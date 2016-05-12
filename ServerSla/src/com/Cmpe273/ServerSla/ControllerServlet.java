package com.Cmpe273.ServerSla;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import redis.clients.jedis.Jedis;
import java.util.*;

//@WebServlet("/ControllerServlet")
public class ControllerServlet extends HttpServlet implements Test {
    private static final long serialVersionUID = 1L;
    HashMap<String, Integer> products = new HashMap<String, Integer>() {{
        put("PID1",5);
        put("PID2",4);
        put("PID3",5);
        put("PID4",5);
        put("PID5",6);
        put("PID6",7);
        put("PID7",11);
        put("PID8",12);
        put("PID9",39);
        put("PID10",10);
    }};
    
    long Latency = 0;
    long ThroughPut = 0;
    boolean fail=true;
    
    

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControllerServlet() {
        super();

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
    
        Map<String, Integer> applicationValues = new HashMap<String, Integer>();
        
        /*URL url = new URL("http://localhost:8080/ServerSla/rest/ServerResponseStatus");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(
            (conn.getInputStream())));

        String output;
        System.out.println("Output from Server .... \n");
        while ((output = br.readLine()) != null) {
            System.out.println(output);
        }

        if (output=="true")
        {
            fail=true;
        }
        
        conn.disconnect();*/
        
        //ServletContext session = request.getSession().getServletContext();
		//String stat = (String) session.getAttribute("ServerFail");
		
		//System.out.println("STAT:"+stat);
        
        

     if(fail==false)
        {
        
        System.out.println("Printing request from client "+request.getHeader("TestHeader"));
        
            Enumeration names = request.getHeaderNames();
            
            while (names.hasMoreElements()) 
            {
              String name = (String) names.nextElement();
              Enumeration values = request.getHeaders(name);  // support multiple values
              if (values != null) {
                while (values.hasMoreElements()) {
                  String value = (String) values.nextElement();
                  System.out.println(name + ": " + value);
                }
              }
            }
        
            /*try
            {
            ServletContext session = request.getSession().getServletContext();
			String stat = (String) session.getAttribute("ServerFail");
			JSONObject status = new JSONObject();
			status.put("status", stat);

			System.out.println("******************API called:"+status.get("status"));

			if (status.get("status") == "true") {

			}
            }
            catch(Exception ex)
            {
            	
            }*/
        
        System.out.println("Controller Servlet hit");
        //response.getWriter().append("Served at: ").append(request.getContextPath());
        
        applicationValues=htmlParse();
       // long Latency=setLatency();      // Call to get latency sandeep's logic 
        
       // int latency=(int)Latency;
        //String timeout=decideTimeout(latency);// Decide keepAlive header
        
        //int retryAfter=decideRetryAfter(latency);
        
        
        
        ApplicationStats appStats=new ApplicationStats();
        
        String pid=request.getParameter("id");
        //int productQty=products.get(pid);
        
        response.setStatus(200);
        response.setContentType("application/json");
        response.addHeader("Connection", "Keep-Alive");
        response.addHeader("Keep-Alive", "timeout=60000");
        response.addIntHeader("Active_Connections", 5);
        response.addIntHeader("Request_Count", applicationValues.get("Request_Count"));
        response.addIntHeader("Latency",40000);
        response.addIntHeader("Concurrent_Threads", 20);
        response.addIntHeader("Retry-After", 10000);
        
        
        
        //Response body return products 
        
        //response.getWriter().println("Your Product Qty:  "+productQty);
        
    
        
        }
        
        else
        {
            response.addHeader("Service_Unavailable","503");
        }
        
    


    }

    

     public  Map<String,Integer> htmlParse()  
        {
            
            String strURL = "http://localhost:8080/manager/status/all#1.0";
            String strUserId = "tomcat";
            String strPasword = "s3cret";
            String authString = strUserId + ":" + strPasword;
            String encodedString = 
                    new String( Base64.encodeBase64(authString.getBytes()) );
            String data=null;
            String threadData=null;
            String maxThreads=null;
            String currentThreadCount=null;
            String currentThreadBusy=null;
            
                Document document=null;
            
            try {
                 document = Jsoup.connect(strURL).header("Authorization", "Basic " + encodedString).get();
            }
            catch(IOException io)
            {
                io.printStackTrace();
            }
            
            String title = document.title();
            System.out.println("title : " + title);
        
            Elements elements = document.body().select("*");
            boolean hTagFound = false;
            boolean h1TagFound=false;
            for (Element element:elements)
            {
                if(h1TagFound==true)
                {
                     threadData=(element.ownText());    
                     System.out.println(threadData);
                     int currentThreadIndex=threadData.indexOf("Current thread count:");
                     int currentThreadBusyIndex=threadData.indexOf("Current thread busy");
                     int maxthreadsIndex=threadData.indexOf("Max threads");
                     currentThreadBusy=threadData.substring(currentThreadBusyIndex,threadData.indexOf("Max processing time")-1);
                     maxThreads=threadData.substring(0, currentThreadIndex);
                     currentThreadCount=threadData.substring(currentThreadIndex,currentThreadBusyIndex);
                     maxthreadsIndex=maxThreads.indexOf(":");
                     maxThreads=maxThreads.substring(maxthreadsIndex+2,currentThreadIndex-1);
                     System.out.println(maxThreads);
                     currentThreadIndex=currentThreadCount.indexOf(":");
                     currentThreadCount=currentThreadCount.substring(currentThreadIndex+2,currentThreadCount.length()-1);
                     currentThreadBusyIndex=currentThreadBusy.indexOf(":");
                     currentThreadBusy=currentThreadBusy.substring(currentThreadBusyIndex+2,currentThreadBusy.length());
                     System.out.println(currentThreadCount+currentThreadBusy+maxThreads);
                    
                     break;
                }
                
                
                if(element.ownText().contains("http-bio-8080"))
                {
                    h1TagFound = true;
                }
            }
            
            for (Element element : elements) 
            {
                if(hTagFound == true)
                {
                    data=(element.ownText());   
                    System.out.println(data);
                    
                    int requestIndex=data.indexOf("Request");
                    int errorIndex=data.indexOf("Error");
                    
                    data=data.substring(requestIndex, errorIndex-1);
                    requestIndex=data.indexOf(":");
                    data=data.substring(requestIndex+2,data.length());
                    System.out.println(data);
                  
                    
                   break;
                }
                if(element.ownText().contains("Controller Servlet"))
                {
                    hTagFound = true;
                }
                
            }
        
            Elements links = document.select("p");
            
            Map<String,Integer> applicationValues=new HashMap<String,Integer>();
            
            applicationValues.put("Current_Threads", Integer.parseInt(currentThreadCount));
            applicationValues.put("Maximum_Threads", Integer.parseInt(maxThreads));
            applicationValues.put("Current_Thread_Busy",Integer.parseInt(currentThreadBusy));
            applicationValues.put("Request_Count", Integer.parseInt(currentThreadBusy));

            
            
            return applicationValues;
        }
     
     
     
      private long setLatency() 
      {
            // Latency and through put
            String output = "";
            int count = 0;
            long totalElapsedTime = 0;
            long totalBytesRead = 0;
            long startTime, endTime;
            String strRead = "";
            try {
                for (int i = 0; i < 10; i++) {
                    startTime = System.currentTimeMillis();
                    HttpURLConnection conn = null;
                    try {
                        // URL url = new
                        // URL("http://localhost:8080/ServerHealthMonitor/rest/helloWorldREST/JavaCodeGeeks?value="+val);
                        URL url = new URL("http://localhost:8080/ServerSla/ControllerServlet");
                        conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("Accept", "application/json");

                        if (conn.getResponseCode() != 200) 
                        {
                            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                        }

                        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                        
                    

                        endTime = System.currentTimeMillis();
                    
                        totalElapsedTime += endTime - startTime;
                        


                        while ((strRead = br.readLine()) != null) 
                        {
                            totalBytesRead += (strRead.getBytes().length);
                        }
                        count++;
                        System.out.println("Printing "+count);
                    } 
                    catch (Exception ex) 
                    {
                    } 
                    finally 
                    {
                        conn.disconnect();
                    }
                    // return bytesRead;
                }
            } 
            catch (Exception ex) 
            {
                output += ex.toString();
            }
            System.out.println("Priniting elapsed time"+totalElapsedTime);
            Latency = totalElapsedTime / (count * 2);
            ThroughPut = totalBytesRead / (totalElapsedTime);
            output = "Latency: " + Latency + " ms, " + "Bytes Read: " + totalBytesRead / count + " bytes, Throughput: "
                    + ThroughPut + " bytes/sec, Count:" + count;
            
            return Latency;
            
        }
      
      
      private String decideTimeout(int latency)
      {
          String timeout=null;
          
          if (latency>10)
          {
              
              timeout="timeout=120";
              
          }
          
          else
          {
              timeout="timeout=240";
          }
          return timeout;
         
      }
      
      private int decideRetryAfter(int latency)
      {
          int retryAfter=0;
          
          if (latency>10)
          {
              
              retryAfter=10;
              
          }
          
          else
          {
              retryAfter=5;
          }
          return retryAfter;
         
      }
         
      
}


