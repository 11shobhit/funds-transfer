package manager;

import beans.User;
import exceptions.InsufficientFundsException;
import exceptions.WebApplicationException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import request.FundsTransferRequest;
import util.locks.LocksUtil;

import javax.ws.rs.core.Response;
import java.util.concurrent.atomic.AtomicInteger;

public class FundsTransferManager {

	hibernate.dao.UserDaoImpl UserDaoImpl = new hibernate.dao.UserDaoImpl();
	Transaction transaction = null;
	private static LocksUtil locksUtil = new LocksUtil();

	public void transferFunds(FundsTransferRequest fundsTransferRequest) {
		User unlockedToUser = UserDaoImpl.getUserById(fundsTransferRequest.getToUserId());
		User unlockedFromUser = UserDaoImpl.getUserById(fundsTransferRequest.getFromUserId());

		try (Session session = hibernate.util.HibernateUtil.getSessionFactory().openSession()) {
			locksUtil.doInLock(unlockedToUser.getId(), unlockedFromUser.getId(), () -> {
				User lockedToUser = UserDaoImpl.getUserById(fundsTransferRequest.getToUserId());
				User lockedFromUser = UserDaoImpl.getUserById(fundsTransferRequest.getFromUserId());
				transaction = session.beginTransaction();
				validate(fundsTransferRequest);
				lockedFromUser.setBalance(
						new AtomicInteger(lockedFromUser.getBalance().get() - fundsTransferRequest.getAmount().get()));
				UserDaoImpl.updateUser(lockedFromUser);
				lockedToUser.setBalance(
						new AtomicInteger(lockedToUser.getBalance().get() + fundsTransferRequest.getAmount().get()));
				UserDaoImpl.updateUser(lockedToUser);
				transaction.commit();
			});
		} catch (WebApplicationException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		}
	}

	private void validate(FundsTransferRequest fundsTransferRequest) {
		AtomicInteger fromUserBalance = UserDaoImpl.getBalanceByUserId(fundsTransferRequest.getFromUserId());

		if (null == UserDaoImpl.getUserById(fundsTransferRequest.getFromUserId())
				|| null == UserDaoImpl.getUserById(fundsTransferRequest.getToUserId())) {
			throw new WebApplicationException("User not exists with given Id", Response.Status.BAD_REQUEST);
		}
		if (fromUserBalance.get() < fundsTransferRequest.getAmount().get()) {
			throw new InsufficientFundsException("Insufficient Funds to be transferred :  ",
					Response.Status.BAD_REQUEST);
		}
	}

}
