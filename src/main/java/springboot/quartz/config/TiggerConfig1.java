package springboot.quartz.config;

import org.quartz.CronTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import springboot.quartz.job.ScheduledTasks1;

/**
 * Created by yuanshuai on 2019/4/5.
 * 2/3 得到JobDetail和Tigger
 */
//@Configuration
public class TiggerConfig1 {

    @Autowired
    private ConfigBeanFactory configBeanFactory;
    @Autowired
    private ScheduledTasks1 scheduledTasks1;

    private final static String METHOD = "println";
    private final static String CRON = "0/5 * * * * ? ";
    private final static String[] ARGS = new String[]{"println"};

    @Bean(name = "detailFactoryBean1")
    public MethodInvokingJobDetailFactoryBean detailFactoryBean(){
        return configBeanFactory.getJobDetail(scheduledTasks1, METHOD, ARGS);
    }

    @Bean(name = "cronTrigger1")
    public CronTrigger cronTrigger(@Qualifier("detailFactoryBean1") MethodInvokingJobDetailFactoryBean detailFactoryBean){
        return configBeanFactory.getCronTrigger(detailFactoryBean, CRON);
    }

}
