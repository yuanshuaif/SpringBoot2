package springboot.schedule;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2019/3/9.
 */
@Component
public class ScheduleTask2 {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

  /*  @Scheduled(fixedRate = 6000) ：上一次开始执行时间点之后6秒再执行
    @Scheduled(fixedDelay = 6000) ：上一次执行完毕时间点之后6秒再执行*/
  // SimpleTriggerBean
    @Async
//    @Scheduled(fixedRate = 6000)
    public void reportCurrentTime() {
        // 使用线程池的意义
      /*  try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        System.out.println(Thread.currentThread().getName()+"现在时间：" + dateFormat.format(new Date()));
    }
}
