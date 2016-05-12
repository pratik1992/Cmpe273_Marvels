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

@Path("/ServerResponseFail")
public class ServerResponseFail {
	@Context
	private HttpServletRequest request;

	@GET
	@Path("/{parameter}")
	public Response responseMsg(@PathParam("parameter") String parameter,
			@DefaultValue("Nothing to say") @QueryParam("value") String value) {
		try {
			//HttpSession session = request.getSession(true);
			ServletContext session = request.getSession().getServletContext();
			session.setAttribute("ServerFail", parameter);

			System.out.println(parameter);
		} catch (Exception ex) {
		}
		return Response.status(200).entity("OK").build();
	}
}