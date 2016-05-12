package com.Cmpe273.ServerSla;


import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.UnknownHostException;
 
import javax.ws.rs.core.UriBuilder;
 
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.api.core.ResourceConfig;
import com.sun.net.httpserver.HttpServer;


@SuppressWarnings("restriction")
public class SlaHTTPServer {

	public static void main(String[] args) throws IOException {
        System.out.println("Starting Embedded Jersey HTTPServer...\n");
        HttpServer slaHTTPServer = createHttpServer();
        slaHTTPServer.start();
        System.out.println(String.format("\nJersey Application Server started with WADL available at " + "%sapplication.wadl\n", getSlaURI()));
        System.out.println("Started  Embedded Jersey HTTPServer Successfully !!!");
    }
 
        private static HttpServer createHttpServer() throws IOException {
        ResourceConfig slaResourceConfig = new PackagesResourceConfig("com.Cmpe273.ServerSla");
        // This tutorial required and then enable below line: http://crunfy.me/1DZIui5
        //crunchifyResourceConfig.getContainerResponseFilters().add(CrunchifyCORSFilter.class);
        return HttpServerFactory.create(getSlaURI(), slaResourceConfig);
    }
 
    private static URI getSlaURI() {
        return UriBuilder.fromUri("http://" + slaGetHostName() + "/").port(8087).build();
    }
 
    private static String slaGetHostName() {
        String hostName = "localhost";
        try {
            hostName = InetAddress.getLocalHost().getCanonicalHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return hostName;
    }
}
	
	

