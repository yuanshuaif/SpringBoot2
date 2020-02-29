package springboot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//@MapperScan("springboot.mybatis.annotationMapper") (无xml配置)
//@EnableScheduling
@MapperScan({"springboot.mybatis.annotationMapper", "springboot.mybatis.mapper"})
@EnableTransactionManagement
public class RedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);
	}

}
