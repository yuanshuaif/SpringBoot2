package springboot.idempotent.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by 10326 on 2020/4/6.
 */
@Component
@Slf4j
public class JedisUtil {

    @Autowired
    private JedisPool jedisPool;

    private Jedis getJedis() {
        return jedisPool.getResource();
    }

    /**
     * 设值
     * @param key
     * @param value
     * @return
     */
    public String set (String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.set(key, value);
        }catch(Exception e) {
            log.error(  "set key:{} value:{} error", key, value, e);
            return null;
        }finally{
            close(jedis);
        }
    }

    /**
     * 设值
     * @param key
     * @param value
     * @param expireTime 过期时间, 单位: s
     * @return
     */
    public String set (String key, String value, int expireTime) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.setex(key, expireTime, value);
        }catch(Exception e) {
            log.error(  "set key:{} value:{} expireTime:{} error", key, value, expireTime, e);
            return null;
        }finally{
            close(jedis);
        }
    }

    /**
     * 取值
     * @param key
     * @return
     */
    public String get (String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.get(key);
        }catch(Exception e) {
            log.error(  "set key:{} error", key, e);
            return null;
        }finally{
            close(jedis);
        }
    }

    /**
     * 删除
     * @param key
     * @return
     */
    public Long del (String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.del(key);
        }catch(Exception e) {
            log.error(  "set key:{} error", key, e);
            return null;
        }finally{
            close(jedis);
        }
    }

    /**
     * 判断key是否存在
     * @param key
     * @return
     */
    public Boolean exists (String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.exists(key);
        }catch(Exception e) {
            log.error(  "set key:{} error", key, e);
            return null;
        }finally{
            close(jedis);
        }
    }

    /**
     * 设置过期时间
     * @param key
     * @param expireTime 过期时间, 单位: s
     * @return
     */
    public Long set (String key, int expireTime) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.expire(key, expireTime);
        }catch(Exception e) {
            log.error(  "set key:{} expireTime:{} error", key, expireTime, e);
            return null;
        }finally{
            close(jedis);
        }
    }

    /**
     * 获取剩余时间
     * @param key
     * @return
     */
    public Long ttl (String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            return jedis.ttl(key);
        }catch(Exception e) {
            log.error(  "set key:{} error", key, e);
            return null;
        }finally{
            close(jedis);
        }
    }

    private void close(Jedis jedis) {
        if(null != jedis) {
            jedis.close();
        }
    }

}
