package com.lilianghui.framework.core.lock;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;


//@Component
public class RedisLock {

    private static final Logger log = LoggerFactory.getLogger(RedisLock.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * 1、获得当前系统时间，计算锁的到期时间
     * 2、setNx操作，加锁
     * 3、如果，加锁成功，设置锁的到期时间，返回true；取锁失败，取出当前锁的value(到期时间)
     * 4、如果value不为空而且小于当前系统时间，进行getAndSet操作，重新设置value，并取出旧value；否则，等待间隔时间后，重复步骤2；
     * 5、如果步骤3和4取出的value一样，加锁成功，设置锁的到期时间，返回true；否则，别人加锁成功，恢复锁的value，等待间隔时间后，重复步骤2。
     */
    public boolean lock(Lock lk) {
        log.info("{}-----尝试获取锁...", Thread.currentThread().getName());
        int lockWaitMillSeconds = lk.getLockWaitTimeout();
        String lockKey = lk.getLockKey();
        int lockExpirseTimeout = lk.getLockExpirseTimeout();
        // key 的值，表示key的到期时间
        String redisValue = String.valueOf(System.currentTimeMillis() + lockExpirseTimeout);
        while (lockWaitMillSeconds > 0) {
            lk.setLock(setIfAbsent(lk.getLockKey(), redisValue));
            if (lk.isLock()) {
                // 拿到锁,设置锁的有效期,这里可能因为故障没有被执行，锁会一直存在，这时就需要value的有效期去判断锁是否失效
                this.redisTemplate.expire(lockKey, lockExpirseTimeout, TimeUnit.MILLISECONDS);
                log.info("{}-----获得锁", Thread.currentThread().getName());
                return lk.isLock();
            } else {
                // 锁存在，判断锁有没有过期
                String oldValue = (String) this.redisTemplate.opsForValue().get(lockKey);
                if (StringUtils.isNotEmpty(oldValue) && Long.parseLong(oldValue) < System.currentTimeMillis()) {
                    // 锁的到期时间小于当前时间，说明锁已失效, 修改value，获得锁
                    String currentRedisValue = getAndSet(lockKey, String.valueOf(lockExpirseTimeout + System.currentTimeMillis()));
                    // 如果两个值不相等，说明有另外一个线程拿到了锁，阻塞
                    if (currentRedisValue.equals(oldValue)) {
                        // 如果修改的锁的有效期之前没被其他线程修改，则获得锁, 设置锁的超时时间
                        redisTemplate.expire(lockKey, lockExpirseTimeout, TimeUnit.MILLISECONDS);
                        log.info("{}-----获得锁", Thread.currentThread().getName());
                        lk.setLock(true);
                        return lk.isLock();
                    } else {
                        // 有另外一个线程获得了这个超时的锁,不修改锁的value
                        redisTemplate.opsForValue().set(lockKey, currentRedisValue);
                    }
                }
            }
            // 减掉固定轮询获取锁的间隔时间
            lockWaitMillSeconds -= Lock.DEFAULT_LOOP_WAIT_TIME;
            try {
                log.info("{}-----等待{}ms后，再尝试获取锁...", Thread.currentThread().getName(), Lock.DEFAULT_LOOP_WAIT_TIME);
                // 取锁失败时，应该在随机延时后进行重试，避免不同客户端同时重试导致谁都无法拿到锁的情况出现,也可以采用等待队列的方式
                Thread.sleep(Lock.DEFAULT_LOOP_WAIT_TIME);
            } catch (InterruptedException e) {
                log.error("redis 同步锁出现未知异常", e);
            }
        }
        log.info("{}-----请求锁超时，获得锁失败", Thread.currentThread().getName());
        lk.setLock(false);
        return lk.isLock();
    }

    public void unlock(Lock lk) {
        if (lk.isLock()) {
            this.redisTemplate.delete(lk.getLockKey());
            lk.setLock(false);
        }
    }

    private boolean setIfAbsent(String lockKey, String expirseTimeStr) {
        // setIfAbsent通过jedis的setNx实现
        return this.redisTemplate.opsForValue().setIfAbsent(lockKey, expirseTimeStr);
    }

    private String getAndSet(String lockKey, String expirseTimeStr) {
        // 获取原来的值，并设置新的值，原子操作
        return (String) this.redisTemplate.opsForValue().getAndSet(lockKey, expirseTimeStr);
    }
}
