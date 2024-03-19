package com.caychen.boot.redis.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.commands.JedisCommands;
import redis.clients.jedis.params.SetParams;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 基于redis的分布式锁，**2019/7/3开始 试运行**
 *
 * @author liudong
 * @date 2019/7/3
 */
@Slf4j
@Component
public class RedisDistributedLock {
    private static final String UNLOCK_LUA;

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("if redis.call(\"get\",KEYS[1]) == ARGV[1] ");
        sb.append("then ");
        sb.append("    return redis.call(\"del\",KEYS[1]) ");
        sb.append("else ");
        sb.append("    return 0 ");
        sb.append("end ");
        UNLOCK_LUA = sb.toString();
    }

    private StringRedisTemplate redisTemplate;
    private ThreadLocal<String> lockFlag = new ThreadLocal<>();

    public RedisDistributedLock(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean lock(String key, long expire, int retryTimes, long sleepMillis) {
        boolean result = setNxAndPx(key, expire);
        // 如果获取锁失败，按照传入的重试次数进行重试
        while (!result && retryTimes-- > 0) {
            try {
                log.debug("lock failed, retrying..." + retryTimes);
                Thread.sleep(sleepMillis);
            } catch (InterruptedException e) {
                log.info("lock retry interrupted");
                return false;
            }
            result = setNxAndPx(key, expire);
        }
        log.info("lock result " + result);
        return result;
    }

    private boolean setNxAndPx(String key, long expire) {
        try {
            String result = redisTemplate.execute((RedisCallback<String>) connection -> {
                JedisCommands commands = (JedisCommands) connection.getNativeConnection();
                String uuid = UUID.randomUUID().toString();
                lockFlag.set(uuid);
                SetParams setParams = new SetParams();
                setParams.nx().px(expire);
                return commands.set(key, uuid, setParams);
            });
            return !StringUtils.isEmpty(result);
        } catch (Exception e) {
            log.error("set redis exception", e);
        }
        return false;
    }

    public boolean unlock(String key) {
        // 释放锁的时候，有可能因为持锁之后方法执行时间大于锁的有效期，此时有可能已经被另外一个线程持有锁，所以不能直接删除
        try {
            String uuid = lockFlag.get();
            if (uuid == null) {
                log.info("unlock illegal call");
                return false;
            }
            List<String> keys = new ArrayList<>();
            keys.add(key);
            List<String> args = new ArrayList<>();
            args.add(uuid);
            log.debug("keys:{}||args:{}", keys, args);

            // 使用lua脚本删除redis中匹配value的key，可以避免由于方法执行时间过长而redis锁自动过期失效的时候误删其他线程的锁
            // spring自带的执行脚本方法中，集群模式直接抛出不支持执行脚本的异常，所以只能拿到原redis的connection来执行脚本
            Long result = redisTemplate.execute((RedisCallback<Long>) connection -> {
                Object nativeConnection = connection.getNativeConnection();
                // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                // 集群模式
                if (nativeConnection instanceof JedisCluster) {
                    return (Long) ((JedisCluster) nativeConnection).eval(UNLOCK_LUA, keys, args);
                }
                // 单机模式
                else if (nativeConnection instanceof Jedis) {
                    return (Long) ((Jedis) nativeConnection).eval(UNLOCK_LUA, keys, args);
                }
                return 0L;
            });
            log.info("unlock result " + result);
            return result != null && result > 0;
        } catch (Exception e) {
            log.error("release lock exception", e);
        } finally {
            lockFlag.remove();
        }
        log.info("unlock result false");
        return false;
    }
}
