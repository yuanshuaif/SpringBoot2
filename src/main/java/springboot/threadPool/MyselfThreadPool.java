package springboot.threadPool;


import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by 10326 on 2020/4/9.
 * 固定线程池
 */
public class MyselfThreadPool {

    // 线程的默认数量
    private static final int TASK_NUM = 2;
    // 工作队列的默认数量
    private static final int WORK_NUM = 5;
    private int taskNum;
    private int workNum;
    // 拒绝策略
    private Reject reject;
    private final Set<WorkThread> workThreads;
    private final BlockingQueue<Runnable> taskQueue;
    enum Reject{
        FIFO, REJECT, REJECT_THROW_EX;
    }

    public MyselfThreadPool(){
        this(TASK_NUM, WORK_NUM, Reject.REJECT);
    }

    public MyselfThreadPool(Reject reject){
        this(TASK_NUM, WORK_NUM, reject);
    }

    public MyselfThreadPool(int taskNum, int workNum, Reject reject){
        if(taskNum <= 0) taskNum = TASK_NUM;
        if(workNum <= 0) workNum = WORK_NUM;
        this.taskNum = taskNum;
        this.workNum = workNum;
        this.reject = reject;
        taskQueue = new ArrayBlockingQueue<>(workNum);
        workThreads = new HashSet<>(taskNum);
        for(int i = 0; i <  taskNum; i++ ){
            WorkThread workThread = new WorkThread("Thread:" + i + UUID.randomUUID());
            workThread.start();
            workThreads.add(workThread);
        }
    }

    public void execute(Runnable runnable){
        try {
            if (taskQueue.offer(runnable)) {// 添加成功
            } else {// 队列已满，走降级策略
                switch (reject) {
                    case FIFO:
                        rejectFIFO(runnable);
                        break;
                    case REJECT:
                        reject(runnable);
                        break;
                    case REJECT_THROW_EX:
                        rejectThrowEx(runnable);
                        break;
                    default:
                        rejectThrowEx(runnable);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void rejectFIFO(Runnable runnable){// 抛弃最早的任务
        taskQueue.poll();
        execute(runnable);
    }

    private void reject(Runnable runnable){// 抛弃任务
    }

    private void rejectThrowEx(Runnable runnable) throws RuntimeException{// 抛弃任务
        throw new RuntimeException("线程已满，该任务已被拒绝");
    }

    public void destroy(){
        if (CollectionUtils.isEmpty(workThreads)) return;
        for (WorkThread workThread : workThreads) {
            workThread.interrupt();
            workThread = null;
        }
        workThreads.clear();
    }

    private final class WorkThread extends Thread{

         String name;
         private ReentrantLock reentrantLock = new ReentrantLock();
         public WorkThread(String name){
             this.name = name;
         }

        public ReentrantLock getReentrantLock() {
            return reentrantLock;
        }

        @Override
        public void run() {
            while(!interrupted()){
                try {
                    if(taskQueue.isEmpty())
                        continue;
                    Runnable task = taskQueue.take();
                    if(task != null) {
                        task.run();
                    }
                    task = null;
                } catch (InterruptedException e) {
                    interrupt();
                    e.printStackTrace();
                }
            }
            System.out.println(name + "———终止了!");
        }
    }
}
