package springboot.threadPool;

/**
 * Created by 10326 on 2020/4/9.
 */
public class ThreadPoolTest {

    public static void main(String[] args){
        MyselfThreadPool myselfThreadPool = new MyselfThreadPool(MyselfThreadPool.Reject.FIFO);
        for(int i = 0; i < 6; i++) {
            myselfThreadPool.execute(() -> {
             /*   try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                System.out.println("Hello World");
            });
        }
        System.out.println("main 方法执行");
        myselfThreadPool.destroy();

    }



}
