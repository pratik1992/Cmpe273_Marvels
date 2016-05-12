package com.Cmpe273.ServerSla;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.GET;  
import javax.ws.rs.Path;  
import javax.ws.rs.Produces;  
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;  


@Path("/applicationParameters")  
public class ApplicationParameters 
{  
	  double Latency = 0;
	  double ThroughPut = 0;
  // This method is called if HTML and XML is not requested  
  @GET  
  @Produces(MediaType.TEXT_PLAIN)  

  public Response sayPlainTextHello() {
	  
	  Map<String,Integer> appStats=new HashMap<String,Integer>();
	  JSONObject applicationParameters = new JSONObject();
	  try
	  {
		  appStats=htmlParse();
		  setLatency();
			 
		  applicationParameters.put("Request_Count",appStats.get("Request_Count"));
		  applicationParameters.put("Maximum_Threads",appStats.get("Maximum_Threads"));
		  applicationParameters.put("Current_Thread_Busy",appStats.get("Current_Thread_Busy"));
		  applicationParameters.put("Current_Threads",appStats.get("Current_Threads"));
		  applicationParameters.put("Latency",Latency);
		  applicationParameters.put("Throughput",ThroughPut);


	  }
	 
	  catch(Exception e)
	  {
		  e.printStackTrace();
	  }
	  
	 // return "Hello Jersey Plain";  
	  
	  return Response.status(200).entity(applicationParameters.toString()).build();
	  
	 
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
  
  private void setLatency() {
		// Latency and through put
		String output = "";
		int count = 0;
		double totalElapsedTime = 0;
		double totalBytesRead = 0;
		double startTime, endTime;
		String strRead = "";
		try {
			for (int i = 0; i < 10; i++) {
				startTime = System.currentTimeMillis();
				HttpURLConnection conn = null;
				try {
					// URL url = new
					// URL("http://localhost:8080/ServerHealthMonitor/rest/helloWorldREST/JavaCodeGeeks?value="+val);
					URL url = new URL("http://localhost:8080/ServerSla/img/clientserver.jpg");
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Accept", "application/json");

					if (conn.getResponseCode() != 200) 
					{
						throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
					}

					BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
					
					System.out.println("Printing the Response from server" +conn.getResponseCode());

					endTime = System.currentTimeMillis();
					System.out.println(endTime);
					totalElapsedTime += endTime - startTime;
					System.out.println(totalElapsedTime);


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
		System.out.println((int)(Latency));
		System.out.println("Bytes read:"+(int)(totalBytesRead));

		System.out.println("Elapsed Time: "+(int)(totalElapsedTime));

		ThroughPut = totalBytesRead / (totalElapsedTime);
		ThroughPut = Math.round((ThroughPut * 100) * 100.0) / 100.0;
		output = "Latency: " + Latency + " ms, " + "Bytes Read: " + totalBytesRead / count + " bytes, Throughput: "
				+ ThroughPut + " bytes/sec, Count:" + count;
		System.out.println(output);
	}
}
