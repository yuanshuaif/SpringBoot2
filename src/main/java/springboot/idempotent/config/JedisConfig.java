package springboot.idempotent.config;

/**
 * Created by 10326 on 2020/4/6.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;

@Configuration
public class JedisConfig {
    @Autowired
    private Environment env;
    private JedisPool pool;
    private String maxActive;
    private String maxIdle;
    private String maxWait;
    private String host;
    private String password;
    private String timeout;
    private String database;
    private String port;

    @PostConstruct
    public void  init(){
        database = env.getProperty("spring.redis.database");
        host = env.getProperty("spring.redis.host");
        port = env.getProperty("spring.redis.port");
        password = env.getProperty("spring.redis.password");
        maxActive = env.getProperty("spring.redis.jedis.pool.max-active");
        maxIdle  = env.getProperty("spring.redis.jedis.pool.max-idle");
        maxWait = env.getProperty("spring.redis.jedis.pool.max-wait");
        timeout = env.getProperty("spring.redis.timeout");
    }

    @Bean
    public JedisPoolConfig constructJedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        // 控制一个pool可分配多少个jedis实例，通过pool.getResource()来获取；
        // 如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        config.setMaxTotal(Integer.parseInt(maxActive));
        // 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
        config.setMaxIdle(Integer.parseInt(maxIdle));
        // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
        config.setMaxWaitMillis(Integer.parseInt(maxWait));
        config.setTestOnBorrow(true);
        return config;
    }

    @Bean
    public JedisPool constructJedisPool() {
        String ip = this.host;
        int port = Integer.parseInt(this.port);
        String password = this.password;
        int timeout = Integer.parseInt(this.timeout);
        int database = Integer.parseInt(this.database);
        if (null == pool) {
            if (StringUtils.isEmpty(password)) {
                pool = new JedisPool(constructJedisPoolConfig(), ip, port, timeout);
            } else {
                pool = new JedisPool(constructJedisPoolConfig(), ip, port, timeout, password, database);
            }
        }
        return pool;
    }
}
