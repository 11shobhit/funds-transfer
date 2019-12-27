package service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import exceptions.WebApplicationException;
import manager.FundsTransferManager;
import request.FundsTransferRequest;

@Path("/funds")
public class FundsTransferService {
	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Path("/transfer")
	public Response fundsTransfer(FundsTransferRequest fundsTransferRequest) {

		try {
			FundsTransferManager FundsTransferManager = new FundsTransferManager();
			FundsTransferManager.transferFunds(fundsTransferRequest);
		} catch (WebApplicationException e) {
			return Response.status(e.getStatus().getStatusCode()).entity("Exception: " + e.getMessage()).build();
		} catch (Exception e) {
			return Response.status(400).entity("Exception: " + e.getMessage()).build();
		}
		return Response.status(200).entity("Transaction Success ").build();
	}

}
