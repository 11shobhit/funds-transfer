package local.server;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;

import hibernate.dao.UserDaoImpl;
import service.FundsTransferService;
import service.UserService;

public class WebApp {
	static final String BASE_URI = "http://localhost:9998/";

	public static void main(String[] args) {
		startServer();
	}

	public static HttpServer startServer() {
		FundsTransferService ts = new FundsTransferService();
		UserDaoImpl UserDaoImpl = new UserDaoImpl();
		ResourceConfig config = new ResourceConfig().register(new UserService(UserDaoImpl))
				.register(new FundsTransferService()).register(JacksonJsonProvider.class);
		return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), config);
	}
}
