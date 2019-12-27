package util.locks;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class LocksUtil {
	private static final Map<Long, ReentrantLock> locks = new ConcurrentHashMap<>();

	public void createLock(Long accountId) {
		locks.putIfAbsent(accountId, new ReentrantLock());
	}

	public void removeLock(Long accountId) {
		locks.remove(accountId);
	}

	public void doInLock(Long accountId1, Long accountId2, Runnable action) {
		createLock(accountId1);
		createLock(accountId2);
		ReentrantLock lock1 = locks.get(accountId1);
		ReentrantLock lock2 = locks.get(accountId2);
		boolean gotTwoLocks = false;
		do {
			if (lock1.tryLock()) {
				if (lock2.tryLock()) {
					gotTwoLocks = true;
				} else {
					lock1.unlock();
				}
			}
		} while (!gotTwoLocks);
		try {
			action.run();
		} finally {

			lock1.unlock();
			lock2.unlock();
		}
	}
}
