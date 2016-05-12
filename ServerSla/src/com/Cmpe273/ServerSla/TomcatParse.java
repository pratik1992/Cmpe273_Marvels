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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.apache.commons.codec.binary.Base64;
import redis.clients.jedis.Jedis;
import javax.servlet.http.HttpServletRequest;





public class TomcatParse
{
	public static HttpServletRequest request;
	public static boolean fail=false;
    public static void main(String[] args) 
    {
    	Document htmldoc;
    	
    	try
    	{
    	URL url = new URL("http://localhost:8080/ServerSla/rest/ServerResponseStatus");
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
		
		conn.disconnect();
		

    	}catch(Exception e)
    	{
    		
    	}
}
 
	
    	
        /* try
         {
        	URL url = new URL("http://localhost:8080/manager/web.xml");
            URLConnection connection = url.openConnection();
            connection.setRequestProperty ("Authorization", basicAuth);

    		// need http protocol
    		//htmldoc = Jsoup.connect("http://localhost:8080/manager/status").header("Authorization", "Basic " + basicAuth).get();
        	
        	String str=htmlParse();
        	System.out.println(str);
    			
    	}

    	 catch (Exception e) {
    		e.printStackTrace();
    	}*/
    
    	
    
    public static String htmlParse() throws Exception
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
		
			
    	
    	Document document = Jsoup.connect(strURL)
				.header("Authorization", "Basic " + encodedString)
				.get();
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
		
		for (Element element : elements) {
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
	
		//Elements links = document.select("p");
		System.out.println(document);
		
		Map<String,Integer> applicationValues=new HashMap<String,Integer>();
		
		applicationValues.put("Current_Threads", Integer.parseInt(currentThreadCount));
		applicationValues.put("Maximum_Threads", Integer.parseInt(maxThreads));
		applicationValues.put("Current_Thread_Busy", Integer.parseInt(currentThreadBusy));
		applicationValues.put("Request_Count", Integer.parseInt(currentThreadBusy));

		
		//System.out.println(currentThreadCount+maxThreads+currentThreadBusy);
		return data;
    }

   
}
