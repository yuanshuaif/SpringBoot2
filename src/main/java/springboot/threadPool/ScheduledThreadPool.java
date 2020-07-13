package springboot.threadPool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 4种类型的线程池:newFixedThreadPool、newSingleThreadExecutor、newCachedThreadPool、newScheduledThreadPool
 * 3种队列：SynchronousQueue、LinkedBlockingQueue、ArrayBlockingQueue
 */
public class ScheduledThreadPool {

    public static void main(String[] args){
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(2);
        for(int i = 0; i < 5; i++){
            // 起2个线程 延迟5秒去执行
            scheduledThreadPool.schedule(new MyRunnable(), 5, TimeUnit.SECONDS);
            // 首次执行延迟1s，每次间隔5s
            scheduledThreadPool.scheduleWithFixedDelay(new MyRunnable(), 1, 5, TimeUnit.SECONDS);
        }
    }

}

 class MyRunnable implements Runnable{

     @Override
     public void run() {
         System.out.println(Thread.currentThread().getName());
     }
 }

