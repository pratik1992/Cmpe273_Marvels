package com.Cmpe273.ServerSla;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Path("/ApplicationStatus")
public class ApplicationStatus {
	long Latency = 0;
	long ThroughPut = 0;

	@GET
	// @Path("/{parameter}")
	public Response responseMsg(@PathParam("parameter") String parameter,
			@DefaultValue("Nothing to say") @QueryParam("value") String value) {
		setLatency();
		JSONObject applicationStatus = new JSONObject();
		try {
			//System.out.println("I am here");
			applicationStatus.put("Latency", Latency);
			applicationStatus.put("Throughput", ThroughPut);
			
			//applicationStatus.put("RequestCount", value)			
			
			return Response.status(200).entity(applicationStatus.toString()).build();
		} catch (Exception ex) {
			return Response.status(200).entity(ex.getMessage()).build();
		}
	}

	/**
	 * @return
	 */
	private void setLatency() {
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
					URL url = new URL("http://api.petfinder.com/auth.getToken?key=45678");
					conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Accept", "application/json");

					if (conn.getResponseCode() != 200) {
						throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
					}

					BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

					endTime = System.currentTimeMillis();
					totalElapsedTime += endTime - startTime;

					while ((strRead = br.readLine()) != null) {
						totalBytesRead += (strRead.getBytes().length);
					}
					count++;
				} catch (Exception ex) {
				} finally {
					conn.disconnect();
				}
				// return bytesRead;
			}
		} catch (Exception ex) {
			output += ex.toString();
		}
		Latency = totalElapsedTime / (count * 2);
		ThroughPut = totalBytesRead / (totalElapsedTime / 1000);
		output = "Latency: " + Latency + " ms, " + "Bytes Read: " + totalBytesRead / count + " bytes, Throughput: "
				+ ThroughPut + " bytes/sec, Count:" + count;
		System.out.println(output);
	}
}
