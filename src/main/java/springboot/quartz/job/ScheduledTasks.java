package springboot.quartz.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

/**
 * Created by yuanshuai on 2019/4/5.
 * 1.编写job任务类
 */
@Component
public class ScheduledTasks implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("This is a simple quartz");
    }
    public void execute(){
        System.out.println("This is a void quartz");
    }
}
