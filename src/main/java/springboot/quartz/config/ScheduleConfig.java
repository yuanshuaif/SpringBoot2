package springboot.quartz.config;

import org.quartz.CronTrigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Created by yuanshuai on 2019/4/5.
 * 4.将tigger注册到scheduled里
 */
//@Configuration
public class ScheduleConfig {

    @Autowired
    @Qualifier("cronTrigger")
    private CronTrigger cronTrigger;
    @Autowired
    @Qualifier("cronTrigger1")
    private CronTrigger cronTrigger1;
    @Bean
    public SchedulerFactoryBean schedulerFactory(){
        SchedulerFactoryBean bean = new SchedulerFactoryBean ();// quartz通过这个工厂来进行对各触发器的管理
        CronTrigger[] triggers = new CronTrigger[]{cronTrigger, cronTrigger1};
        bean.setTriggers (triggers);
        return bean;
    }
}
