package com.caychen.boot.redis.component;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Caychen
 * @Date: 2021/8/28 9:45
 * @Description:
 */
@Component
@Slf4j
public class RedisComponent {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public Boolean isExists(String key) {
        return redisTemplate.hasKey(key);
    }

    public Long ttl(String key) {
        return stringRedisTemplate.getExpire(key);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Boolean delKey(String key) {
        return stringRedisTemplate.delete(key);
    }

    public Long delKeys(Collection keys) {
        return stringRedisTemplate.delete(keys);
    }

    public <T> T get(String key, Class<T> clazz) {
        Object v = this.get(key);

        if (v != null) {
            if (v instanceof Number) {
                return (T) v.toString();
            } else if (v instanceof CharSequence) {
                return (T) v;
            } else {
                return JSON.parseObject(JSON.toJSONString(v), clazz);
            }
        }
        return null;
    }

    public void setWithExpire(String key, Object value, Long expire, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expire, timeUnit);
    }

    public void hsetByMap(String key, Map<String, Object> m) {
        redisTemplate.opsForHash().putAll(key, m);
    }

    public void hsetByMapWithExpire(String key, Map<String, Object> m, Long expire, TimeUnit timeUnit) {
        redisTemplate.opsForHash().putAll(key, m);
        redisTemplate.expire(key, expire, timeUnit);
    }

    public void hset(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    public Object hget(String key, Object hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 加锁，自旋重试三次
     *
     * @return
     */
    public Boolean tryLock(String lockKey, String requestId, Long expire, TimeUnit timeUnit, Integer retryCount) {
        Boolean locked = false;
        if (retryCount == null || retryCount < 0) {
            retryCount = 1;
        }

        int tryCount = retryCount;
        while (!locked && tryCount > 0) {
            locked = redisTemplate.opsForValue().setIfAbsent(lockKey, requestId, expire, timeUnit);
            tryCount--;
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                log.error("线程被中断" + Thread.currentThread().getId(), e);
            }
        }
        return locked;
    }

    /**
     * 非原子解锁，可能解别人锁，不安全
     *
     * @return
     */
    public Boolean unlock(String lockKey, String requestId) {
        if (lockKey == null || requestId == null) {
            return false;
        }
        Boolean releaseLock = false;
        if (StringUtils.equals((String) redisTemplate.opsForValue().get(lockKey), requestId)) {
            releaseLock = redisTemplate.delete(lockKey);
        }
        return releaseLock;
    }

    /**
     * 使用lua脚本解锁，不会解除别人锁
     *
     * @return
     */
    public Boolean unlockByLua(String lockKey, String requestId) {
        if (lockKey == null || requestId == null) {
            return false;
        }
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript();
        //用于解锁的lua脚本位置
        redisScript.setLocation(new ClassPathResource("unlock.lua"));
        redisScript.setResultType(Long.class);
        //没有指定序列化方式，默认使用上面配置的
        Object result = redisTemplate.execute(redisScript, Arrays.asList(lockKey), requestId);
        return Objects.equals(result, Long.valueOf(1));
    }
}
