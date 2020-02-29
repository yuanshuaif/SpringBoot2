package springboot.redis.secondsKill;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;

import java.util.List;

/**
 * Created by 10326 on 2019/4/21.
 * 顾客线程
 */
public class ClientThread implements Runnable {
    // j>=i（部分数据回滚）
    private static int i = 0;//统计抢到的总数
    private static int j = 0;// 统计没有抢到的总数
    private String key = "prdNum";// 商品主键
    private String clientName;
    private RedisTemplate redisTemplate;
    public ClientThread(int num, RedisTemplate redisTemplate) {
        this.clientName = "------编号=" + num + "------";
        this.redisTemplate = redisTemplate;
    }

    public void run() {
        try {
            Thread.sleep((int)(Math.random() * 2500));// 核心 核心 核心 使请求落到不同的点上
        } catch (InterruptedException e1) {
        }

        while (true){ // 0.模拟没有抢到，可以继续抢
            try {
                int prdNum = Integer.parseInt((String) redisTemplate.opsForValue().get(key));// 当前商品个数
                if (prdNum > 0) {
                    List<Object> result = (List<Object>) redisTemplate.execute(new SessionCallback<Object>(){
                        @Override
                        public Object execute(RedisOperations operations) throws DataAccessException{
                            redisTemplate.watch(key);
                            operations.multi();
                            operations.opsForValue().set(key, String.valueOf(prdNum - 1));
                            Object val=operations.exec();
                            return val;
                        }
                    });
                    /*数据回滚后，result.size() == 0*/
                    if (result.size() > 0 && (Boolean) result.get(0) == true) {
                            i++;    // 此处可能出现线程安全的问题，但改处只是为了统计，所以无大碍
                        System.out.println("好高兴，顾客:" + clientName + "抢到商品" + i);
                        break;
                    } else {
                            j++;    // 此处可能出现线程安全的问题
                        System.out.println("悲剧了，顾客:" + clientName + "没有抢到商品" + j);// 可能是watch-key被外部修改，或者是数据操作被驳回
                    }
                } else {
                    System.out.println("悲剧了，库存为0，顾客:" + clientName + "没有抢到商品");
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
