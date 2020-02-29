package springboot.quartz.config;

import org.quartz.CronTrigger;
import org.quartz.JobExecutionContext;
import org.quartz.impl.JobExecutionContextImpl;
import org.springframework.expression.ParseException;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.stereotype.Component;

/**
 * Created by yuanshuai on 2019/4/5.
 *  配置中设定了
  ① 组装MethodInvokingJobDetailFactoryBean，MethodInvokingJobDetailFactoryBean：此工厂主要用来制作一个jobDetail，即制作一个任务。
  ② concurrent：对于相同的JobDetail，当指定多个Trigger时, 很可能第一个job完成之前，
  第二个job就开始了。指定concurrent设为false，多个job不会并发运行，第二个job将不会在第一个job完成之前开始。
  ③ cronExpression：0/10 * * * * ?表示每10秒执行一次，具体可参考附表。
  ④ triggers：通过再添加其他的ref元素可在list中放置多个触发器。 ScheduleConfig中的schedulerFactory()方法
 */
@Component
public class ConfigBeanFactory {

    public MethodInvokingJobDetailFactoryBean getJobDetail(Object obj, String method, String... args){
        // 2.封装JobDetail
        MethodInvokingJobDetailFactoryBean bean = new MethodInvokingJobDetailFactoryBean ();
        bean.setTargetObject (obj); //任务
        bean.setTargetMethod (method); //任务的方法
        bean.setArguments(args); //参数
        bean.setConcurrent (false); // 不会并发
        return bean;
    }

    public CronTrigger getCronTrigger(MethodInvokingJobDetailFactoryBean bean, String cron){

        // 3.封装Tigger
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean ();
        tigger.setJobDetail(bean.getObject ());
        try {
            tigger.setCronExpression (cron);//每5秒执行一次
            tigger.afterPropertiesSet();
        } catch (ParseException e) {
            e.printStackTrace ();
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return tigger.getObject();
    }
}
