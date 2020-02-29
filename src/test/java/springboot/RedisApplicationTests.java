package springboot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import springboot.redis.redisLock.RedisTool;
import springboot.redis.secondsKill.OptimisticLockTest;

@RunWith(SpringRunner.class)
@SpringBootTest
/**
 * 秒杀场景使用的是 redis乐观锁的重试机制
 */
public class RedisApplicationTests {

	@Autowired
	private OptimisticLockTest optimisticLockTest;

	/*Redis中的事务(transaction)是一组命令的集合。事务同命令一样都是Redis最小的执行单位，一个事务中的命令要么都执行，要么都不执行。
	Redis事务的实现需要用到 MULTI 和 EXEC 两个命令，事务开始的时候先向Redis服务器发送 MULTI 命令，然后依次发送需要在本次事务中处理的命令，最后再发送 EXEC 命令表示事务命令结束。
	Redis的事务是下面4个命令来实现
 			1.multi，开启Redis的事务，置客户端为事务态。
			2.exec，提交事务，执行从multi到此命令前的命令队列，置客户端为非事务态。
			3.discard，取消事务，置客户端为非事务态。
			4.watch,监视键值对，作用时如果事务提交exec时发现监视的监视对发生变化，事务将被取消。*/

	/**
	 * 商品秒杀场景的demo：
	 * 1.将销售的商品缓存到redis数据库中
	 * 2.顾客开始抢商品 watch+multi-exec
	 * 3.watch+multi-exec命令组合放在SessionCallback中，保证是同一个connection
	 *
	 * Redis.clents.jedis.exceptions.JedisDataException:ERR EXEC without MULTI问题。
	 * multi-exec命令组合放在SessionCallback中
	 *
	 *
	 * 高并发的精髓：0.增加redis连接池；
	 */
	@Test
	public void contextLoads() {
		optimisticLockTest.initPrduct();
		optimisticLockTest.initClient();
	}

	/*@Test
	public void tryGetDistributedLock() {
		redisTool.tryGetDistributedLock(jedis, "ys", UUID.randomUUID().toString(), 1);
	}*/

}
