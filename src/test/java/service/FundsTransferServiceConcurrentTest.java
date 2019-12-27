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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class FundsTransferServiceConcurrentTest extends JerseyTest {
	@Override
	protected Application configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);
		return new ResourceConfig(UserService.class, FundsTransferService.class);
	}

	@Test
	public void testConcurrentRequest() throws ExecutionException, InterruptedException {
		CreateUserRequest req = getCreateUserRequest();
		//create two users
		MultivaluedMap<String, Object> headers = getHeaders();
		target("user/create").request(MediaType.APPLICATION_JSON).headers(headers)
				.post(Entity.entity(req, MediaType.APPLICATION_JSON));
		target("user/create").request(MediaType.APPLICATION_JSON).headers(headers)
				.post(Entity.entity(req, MediaType.APPLICATION_JSON));

		// assert user created response
		Response response = target("user").request().get();
		List<User> items = target("user").request().get(new GenericType<List<User>>() {
		});
		assertThat(response.hasEntity(), is(true));
		assertThat(response.getStatus(), is(200));
		assertTrue(items.size() == 2);

		// send two conccurrent request
		CompletableFuture<Response> cf1 = CompletableFuture.supplyAsync(() -> {
			FundsTransferRequest ftr = new FundsTransferRequest();
			ftr.setAmount(new AtomicInteger(100));
			ftr.setToUserId(2);
			ftr.setFromUserId(1);
			return target("funds/transfer").request().post(Entity.json(ftr));
		});
		CompletableFuture<Response> cf2 = CompletableFuture.supplyAsync(() -> {
			FundsTransferRequest ftr = new FundsTransferRequest();
			ftr.setAmount(new AtomicInteger(100));
			ftr.setToUserId(1);
			ftr.setFromUserId(2);
			return target("funds/transfer").request().post(Entity.json(ftr));
		});
		CompletableFuture.allOf(cf1, cf2).join();
		Response responseCf1 = cf1.get();
		Response responseCf2 = cf2.get();
		items = target("user").request().get(new GenericType<List<User>>() {
		});

		// both the usesrs should have balance 100
		items.stream().forEach(user -> {
			if (user.getId() == 1)
				assertTrue(user.getBalance().get() == 100);
			if (user.getId() == 2)
				assertTrue(user.getBalance().get() == 100);
		});

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
}
