package com.Cmpe273.ServerSla;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

@Path("/ServerResponseStatus")
public class ServerResponseStatus {
	@Context
	private HttpServletRequest request;

	@GET
	//@Path("/{parameter}")
	public Response responseMsg(@PathParam("parameter") String parameter,
			@DefaultValue("Nothing to say") @QueryParam("value") String value) {
		//HttpSession session = request.getSession(true);
		//String stat = (String)session.getAttribute("ServerFail");
		ServletContext context = request.getSession().getServletContext();
		
		//context.setAttribute("ServerFail", parameter);
		String stat = (String)context.getAttribute("ServerFail");
		//Object attribute = context.getAttribute("someValue");
    	if (stat!=null) {
    		System.out.println("From ServerResonseStatus API: "+stat.toString());
    	}
    	else
    	{
    		System.out.println("From ServerResonseStatus API: ServerFail is null");
    	}
    	
		return Response.status(200).entity(stat).build();
		
	}
}