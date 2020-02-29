package springboot.redis.redisSession;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * Created by Administrator on 2019/3/9.
 */
@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {
}
