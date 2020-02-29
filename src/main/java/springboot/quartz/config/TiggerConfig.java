package springboot.quartz.config;

import org.quartz.CronTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import springboot.quartz.job.ScheduledTasks;

/**
 * Created by yuanshuai on 2019/4/5.
 * 2/3 得到JobDetail和Tigger
 */
@Configuration
public class TiggerConfig {

    @Autowired
    private ConfigBeanFactory configBeanFactory;
    @Autowired
    private ScheduledTasks scheduledTasks;

    private final static String METHOD = "execute";
    private final static String CRON = "0/5 * * * * ? ";
    private final static String[] ARGS = new String[]{};

    @Bean(name = "detailFactoryBean")
    public MethodInvokingJobDetailFactoryBean detailFactoryBean(){
        return configBeanFactory.getJobDetail(scheduledTasks, METHOD, null);
    }

    @Bean(name = "cronTrigger")
    public CronTrigger cronTrigger(@Qualifier("detailFactoryBean") MethodInvokingJobDetailFactoryBean detailFactoryBean){
        return configBeanFactory.getCronTrigger(detailFactoryBean, CRON);
    }

}
