package springboot.redis.secondsKill;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 10326 on 2019/4/21.
 */
@Component
public class OptimisticLockTest {

    @Autowired
    private RedisTemplate redisTemplate;// redis连接池

    /*
     * 初始化顾客开始抢商品
     */
    public void initClient() {

        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();// 默认数量250~270

        redisTemplate.setEnableTransactionSupport(true);

        int clientNum = 500;// 模拟客户数目
        for (int i = 0; i < clientNum; i++) {
            cachedThreadPool.execute(new ClientThread(i, redisTemplate));
        }
        cachedThreadPool.shutdown();

        while(true){
            if(cachedThreadPool.isTerminated()){
                System.out.println("所有的线程都结束了！");
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化商品个数
     */
    public void initPrduct() {
        int prdNum = 100;// 商品个数
        String key = "prdNum";

        if (redisTemplate.persist(key)) {
            redisTemplate.delete(key);
        }

        redisTemplate.opsForValue().set(key, String.valueOf(prdNum));// 初始化
    }

}
