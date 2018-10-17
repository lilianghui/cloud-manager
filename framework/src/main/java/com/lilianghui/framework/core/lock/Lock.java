package com.lilianghui.framework.core.lock;

public class Lock {

    /* 默认锁的有效时间30s */
    public static final int DEFAULT_LOCK_EXPIRSE_MILL_SECONDS = 30 * 1000;
    /* 默认请求锁等待超时时间10s */
    public static final int DEFAULT_LOCK_WAIT_DEFAULT_TIME_OUT = 10 * 1000;
    /* 默认的轮询获取锁的间隔时间 */
    public static final int DEFAULT_LOOP_WAIT_TIME = 150;
    /* 锁的key前缀 */
    public static final String LOCK_PREFIX = "LOCK_";

    /* 是否获得锁的标志 */
    private boolean lock = false;
    /* 锁的key */
    private String lockKey;
    /* 锁的有效时间(ms) */
    private int lockExpirseTimeout;
    /* 请求锁的阻塞时间(ms) */
    private int lockWaitTimeout;


    public Lock() {
    }

    public Lock(String lockKey, int lockExpirseTimeout, int lockWaitTimeout) {
        this.lockKey = LOCK_PREFIX + lockKey;
        this.lockExpirseTimeout = lockExpirseTimeout;
        this.lockWaitTimeout = lockWaitTimeout;
    }

    public static Lock newInstance(String lockKey) {
        Lock redisLock = new Lock(lockKey, DEFAULT_LOCK_EXPIRSE_MILL_SECONDS, DEFAULT_LOCK_WAIT_DEFAULT_TIME_OUT);
        return redisLock;
    }

    public static Lock newInstance(String lockKey, int lockExpirseTimeout, int lockWaitTimeout) {
        if (lockExpirseTimeout == 0 || lockWaitTimeout == 0) {
            lockExpirseTimeout = DEFAULT_LOCK_EXPIRSE_MILL_SECONDS;
            lockWaitTimeout = DEFAULT_LOCK_WAIT_DEFAULT_TIME_OUT;
        }
        Lock redisLock = new Lock(lockKey, lockExpirseTimeout, lockWaitTimeout);
        return redisLock;
    }


    public boolean isLock() {
        return lock;
    }

    public void setLock(boolean lock) {
        this.lock = lock;
    }

    public String getLockKey() {
        return lockKey;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = LOCK_PREFIX + lockKey;
    }

    public int getLockExpirseTimeout() {
        return lockExpirseTimeout;
    }

    public void setLockExpirseTimeout(int lockExpirseTimeout) {
        this.lockExpirseTimeout = lockExpirseTimeout;
    }

    public int getLockWaitTimeout() {
        return lockWaitTimeout;
    }

    public void setLockWaitTimeout(int lockWaitTimeout) {
        this.lockWaitTimeout = lockWaitTimeout;
    }


}
