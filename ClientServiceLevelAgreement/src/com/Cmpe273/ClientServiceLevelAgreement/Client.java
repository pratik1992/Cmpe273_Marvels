package com.Cmpe273.ClientServiceLevelAgreement;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.swing.JEditorPane;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import redis.clients.jedis.Jedis;

public class Client {

	private final String USER_AGENT = "Mozilla/5.0";
	
	public static Map<String,String> customHeaders=new HashMap<String,String> ();


	public static void main(String[] args) throws Exception {

		Client http = new Client();
		

		System.out.println("Testing 1 - Send Http GET request");
		Jedis jedis = new Jedis("localhost");
	    System.out.println("Connection to server sucessfully");
		
		http.sendGet();
		
		
		
		
	

	}
	
	
		private void sendGet() throws Exception {

			String url = "http://localhost:8080/ServerSla/ControllerServlet";
			
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		
			con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", USER_AGENT);

			int responseCode = con.getResponseCode();
			String activeconnections=con.getHeaderField("ACTIVE_CONNECTIONS");
			String norequests=con.getHeaderField("REQUEST_COUNT");
			
			
			Map<String, List<String>> map = con.getHeaderFields();
			 
            System.out.println("Printing All Response Header for URL: "
                    + obj.toString() + "\n");
 
            
            
			
			//System.out.println("Number of Active Connections  "+activeconnections);
			//System.out.println("Number of Requests   "+norequests);
			System.out.println("\nSending 'GET' request to URL : " + url);
			//System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			clientLogic(map);

		
			System.out.println(response.toString());
			

		}
		
		   private void sendPost() throws Exception {

			String url = "https://selfsolve.apple.com/wcResults.do";
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

			//add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			String urlParameters = "sn=C02G8416DRJM&cn=&locale=&caller=&num=12345";
			
			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'POST' request to URL : " + url);
			System.out.println("Post parameters : " + urlParameters);
			System.out.println("Response Code : " + responseCode);

			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			

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
	            String revised_latency= latency.toString();
	            
	            
	            
	            if (contentLength == null) {
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
