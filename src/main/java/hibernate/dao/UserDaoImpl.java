package hibernate.dao;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import beans.User;

public class UserDaoImpl implements UserDao {

	Transaction transaction = null;

	@Override
	public void save(User user) {

		try (Session session = hibernate.util.HibernateUtil.getSessionFactory().openSession()) {
			session.save(user);
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}

	}

	@Override
	public List<User> getAllUser() {
		List<User> users = null;
		try (Session session = hibernate.util.HibernateUtil.getSessionFactory().openSession()) {
			users = session.createQuery("from User", User.class).list();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return users;
	}

	@Override
	public AtomicInteger getBalanceByUserId(long userId) {
		AtomicInteger balance = null;
		try (Session session = hibernate.util.HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM User u WHERE u.id = :user_id";
			Query<User> query = session.createQuery(hql);
			query.setParameter("user_id", userId);
			User u = query.getSingleResult();
			balance = u.getBalance();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return balance;
	}

	public User getUserById(long userId) {
		List<User> users = null;
		try (Session session = hibernate.util.HibernateUtil.getSessionFactory().openSession()) {
			String hql = "FROM User u WHERE u.id = :user_id";
			Query<User> query = session.createQuery(hql);
			query.setParameter("user_id", userId);
			users = query.getResultList();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
		return users.size() > 0 ? users.get(0) : null;
	}

	public void updateUser(User user) {
		try (Session session = hibernate.util.HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.update(user);
			session.persist(user);
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}

	}

	public void delete() {
		try (Session session = hibernate.util.HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.createQuery(" delete from User").executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			e.printStackTrace();
		}
	}
}
