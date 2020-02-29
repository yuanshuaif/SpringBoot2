package springboot.schedule;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2019/3/9.
 */
@Component
public class ScheduleTask1 {
    private int count=0;

    /**
     * @Async 注解使用在public、默认、protected
     */
    //CronTriggerBean
    @Async
//    @Scheduled(cron="*/6 * * * * ?")
    protected void process(){
        System.out.println(Thread.currentThread().getName()+"this is scheduler task runing  "+(count++));
    }
}
