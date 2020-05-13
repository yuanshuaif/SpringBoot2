package springboot.schedule.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
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
public class AsyncConfig extends AsyncConfigurerSupport {

    private static final int COREPOOLSIZE = 2;
    private static final int MAXPOOLSIZE = 5;
    private static final int QUEUECAPACITY = 10;

    // 异步线程池
    @Override
    public Executor getAsyncExecutor(){
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
        executor.initialize();
        return executor;
    }
}
