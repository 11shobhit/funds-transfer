package exceptions;

import javax.ws.rs.core.Response;

public class InsufficientFundsException extends WebApplicationException {
	private static final long serialVersionUID = 1L;

	public InsufficientFundsException(String message, Response.Status status) {
		super(message, status);
	}

}
