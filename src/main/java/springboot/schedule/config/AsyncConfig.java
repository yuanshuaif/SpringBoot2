package springboot.schedule.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by 10326 on 2019/4/7.
 * SpringTask
 * 使用线程池
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    private static final int COREPOOLSIZE = 10;
    private static final int MAXPOOLSIZE = 100;
    private static final int QUEUECAPACITY = 10;

    @Bean
    public Executor taskExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(COREPOOLSIZE);
        executor.setMaxPoolSize(MAXPOOLSIZE);
        executor.setQueueCapacity(QUEUECAPACITY);
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            /*设置拒绝策略*/
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                // TODO Auto-generated method stub
            }
        });
        return executor;
    }
}
