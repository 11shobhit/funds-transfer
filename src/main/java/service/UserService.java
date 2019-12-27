package service;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import beans.User;
import hibernate.dao.UserDaoImpl;
import request.CreateUserRequest;

@Path("/user")
public class UserService {

	public UserService() {
	}

	public UserService(UserDaoImpl daoImpl) {
		this.daoImpl = daoImpl;
	}

	private static UserDaoImpl daoImpl;
	static {
		daoImpl = new UserDaoImpl();
	}

	@POST
	@Consumes(APPLICATION_JSON)
	@Produces(APPLICATION_JSON)
	@Path("/create")
	public Response create(CreateUserRequest req) {
		User user = new User();
		user.setBalance(req.getBalance());
		user.setName(req.getName());
		daoImpl.save(user);
		return Response.ok().build();
	}

	@GET
	@Produces(APPLICATION_JSON)
	@Path("/")
	public List<User> getAll() {
		return daoImpl.getAllUser();
	}

	@DELETE
	@Path("/delete")
	public void deleteAll() {
		daoImpl.delete();
	}

}
