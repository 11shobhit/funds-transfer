package service;

import beans.User;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import request.CreateUserRequest;
import request.FundsTransferRequest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class FundsTransferServiceTest extends JerseyTest {

	@Test
	public void testFundsTransferService() throws Exception {

		CreateUserRequest req = getCreateUserRequest();
		MultivaluedMap<String, Object> headers = getHeaders();

		// create two users
		Response r = target("user/create").request(MediaType.APPLICATION_JSON).headers(headers)
				.post(Entity.entity(req, MediaType.APPLICATION_JSON));
		target("user/create").request(MediaType.APPLICATION_JSON).headers(headers)
				.post(Entity.entity(req, MediaType.APPLICATION_JSON));

		// assert user created response
		Response response = target("user").request().get();
		List<User> items = target("user").request().get(new GenericType<List<User>>() {
		});
		assertThat(response.hasEntity(), is(true));
		assertThat(response.getStatus(), is(200));

		// transfer funds to invalid user id
		FundsTransferRequest invalidUserIdRequest = getFundsTransferRequest();
		invalidUserIdRequest.setToUserId(10000);
		Response invalidUserIdResponse = target("funds/transfer").request().post(Entity.json(invalidUserIdRequest));

		assertEquals("Http Response should be 400: ", Response.Status.BAD_REQUEST.getStatusCode(),
				invalidUserIdResponse.getStatus());

		// transfer funds with valid user id
		Response validUserIdResponse = target("funds/transfer").request().post(Entity.json(getFundsTransferRequest()));

		assertEquals("Http Response should be 200: ", Response.Status.OK.getStatusCode(),
				validUserIdResponse.getStatus());

		// assert balance after transferring funds
		items = target("user").request().get(new GenericType<List<User>>() {
		});
		items.stream().forEach(user -> {
			if (user.getId() == 1)
				assertTrue(user.getBalance().get() == 200);
			if (user.getId() == 2)
				assertTrue(user.getBalance().get() == 0);
		});

		// transfer funds with insufficient funds
		Response insufficientFundsResponse = target("funds/transfer").request()
				.post(Entity.json(getFundsTransferRequest()));
		assertEquals("Http Response should be 400: ", Response.Status.BAD_REQUEST.getStatusCode(),
				insufficientFundsResponse.getStatus());

		// assert balance after transferring funds
		items = target("user").request().get(new GenericType<List<User>>() {
		});
		items.stream().forEach(user -> {
			if (user.getId() == 1)
				assertTrue(user.getBalance().get() == 200);
			if (user.getId() == 2)
				assertTrue(user.getBalance().get() == 0);
		});

		target("user/delete").request().delete();
	}

	private FundsTransferRequest getFundsTransferRequest() {
		FundsTransferRequest ftr = new FundsTransferRequest();
		ftr.setAmount(new AtomicInteger(100));
		ftr.setToUserId(1);
		ftr.setFromUserId(2);
		return ftr;
	}

	private MultivaluedMap<String, Object> getHeaders() {
		MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
		headers.add("Access-Control-Allow-Origin", "*");
		headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS");
		headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, X-Auth-Token");
		headers.add("Content-Type", MediaType.APPLICATION_JSON);
		return headers;
	}

	private CreateUserRequest getCreateUserRequest() {
		CreateUserRequest req = new CreateUserRequest();
		req.setBalance(new AtomicInteger(100));
		req.setName("nameJunit");
		return req;
	}

	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new ResourceConfig(UserService.class, FundsTransferService.class);
	}
}
