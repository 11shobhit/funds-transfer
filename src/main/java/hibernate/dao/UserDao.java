package hibernate.dao;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import beans.User;

public interface UserDao {
	void save(User user);

	List<User> getAllUser();

	AtomicInteger getBalanceByUserId(long userId);

}
