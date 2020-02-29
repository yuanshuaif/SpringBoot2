package springboot.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * Created by yuanshuai on 2019/4/5.
 * 1.编写任务类
 */
@Component
public class ScheduledTasks1 implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("This is a simple quartz1");
    }
    public void println(String msg){
//        System.out.println(msg);
        try {
            this.execute(null);
        } catch (JobExecutionException e) {
            e.printStackTrace();
        }
    }
}
