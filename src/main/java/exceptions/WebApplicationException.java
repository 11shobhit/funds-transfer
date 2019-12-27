package exceptions;

import javax.ws.rs.core.Response;

public class WebApplicationException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Response.Status status;

	public WebApplicationException(String message, Response.Status status) {
		super(message);
		this.status = status;
	}

	public Response.Status getStatus() {
		return status;
	}

}