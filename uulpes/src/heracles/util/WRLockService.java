package heracles.util;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WRLockService {
    private ReadWriteLock LOCK = new ReentrantReadWriteLock();

    public ReadWriteLock getLOCK() {
        return LOCK;
    }

    public void writeLock() {
        LOCK.writeLock().lock();
    }

    public void writeUnLock() {
        LOCK.writeLock().unlock();
    }

    public void readLock() {
        LOCK.readLock().lock();
    }

    public void readUnLock() {
        LOCK.readLock().unlock();
    }
}
