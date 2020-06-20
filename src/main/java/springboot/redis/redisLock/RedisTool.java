package springboot.redis.redisLock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 10326 on 2019/5/11.
 * ###Redis分布式锁的实现###
 * https://www.cnblogs.com/linjiqin/p/8003838.html
 * 为了确保分布式锁可用，我们至少要确保锁的实现同时满足以下四个条件：
 *      1.互斥性。在任意时刻，只有一个客户端能持有锁。
 *      2.不会发生死锁。即使有一个客户端在持有锁的期间崩溃而没有主动解锁，也能保证后续其他客户端能加锁。(超时时间)
 *      3.具有容错性。只要大部分的Redis节点正常运行，客户端就可以加锁和解锁。(集群、主从同步)
 *      4.解铃还须系铃人。加锁和解锁必须是同一个客户端，客户端自己不能把别人加的锁给解了。
 */
@Component
public class RedisTool {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    @Autowired
    private Jedis jedis;

    // 可重入锁，每个线程重入计数器
    private static ThreadLocal<Map<String, Integer>> local = ThreadLocal.withInitial(HashMap::new);

//    private static ThreadLocal<Long> localExpire = new ThreadLocal<Long>();
    /**
     * 尝试获取分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, long expireTime) {
        /*
          第一个为key，我们使用key来当锁，因为key是唯一的。
          第二个为value，我们传的是requestId，很多童鞋可能不明白，有key作为锁不就够了吗，为什么还要用到value？
            原因就是我们在上面讲到可靠性时，分布式锁要满足第四个条件解铃还须系铃人，通过给value赋值为requestId，
            我们就知道这把锁是哪个请求加的了，在解锁的时候就可以有依据。
            requestId可以使用UUID.randomUUID().toString()方法生成。
          第三个为nxxx，这个参数我们填的是NX，意思是SET IF NOT EXIST，即当key不存在时，我们进行set操作；若key已经存在，则不做任何操作；
          第四个为expx，这个参数我们传的是PX，意思是我们要给这个key加一个过期的设置，具体时间由第五个参数决定。
          第五个为time，与第四个参数相呼应，代表key的过期时间。

          执行上面的set()方法就只会导致两种结果：
          1. 当前没有锁（key不存在），那么就进行加锁操作，并对锁设置个有效期，同时value表示加锁的客户端。
          2. 已有锁存在，不做任何操作。

          心细的童鞋就会发现了，我们的加锁代码满足我们可靠性里描述的三个条件。
          首先，set()加入了NX参数，可以保证如果已有key存在，则函数不会调用成功，也就是只有一个客户端能持有锁，满足互斥性。
          其次，由于我们对锁设置了过期时间，即使锁的持有者后续发生崩溃而没有解锁，锁也会因为到了过期时间而自动解锁（即key被删除），不会发生死锁。
          最后，因为我们将value赋值为requestId，代表加锁的客户端请求标识，那么在客户端在解锁的时候就可以进行校验是否是同一个客户端。
          由于我们只考虑Redis单机部署的场景，所以容错性我们暂不考虑。(集群部署)
        */
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);

        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }

        return false;
    }

    /**
     较常见的错误示例就是使用jedis.setnx()和jedis.expire()组合实现加锁，代码如下：
     setnx()方法作用就是SET IF NOT EXIST，expire()方法就是给锁加一个过期时间。
     乍一看好像和前面的set()方法结果一样，然而由于这是两条Redis命令，不具有原子性，如果程序在执行完setnx()之后突然崩溃，导致锁没有设置过期时间。那么将可能会发生死锁。
     * @param jedis
     * @param lockKey
     * @param requestId
     * @param expireTime
     */
    public static void wrongGetLock1(Jedis jedis, String lockKey, String requestId, int expireTime) {
        Long result = jedis.setnx(lockKey, requestId);
        if (result == 1) {
            // 若在这里程序突然崩溃，则无法设置过期时间，将可能发生死锁
            jedis.expire(lockKey, expireTime);
        }
    }

    /**
         实现思路：使用jedis.setnx()命令实现加锁，其中key是锁，value是锁的过期时间。
         执行过程：
         1. 通过setnx()方法尝试加锁，如果当前锁不存在，返回加锁成功。
         2. 如果锁已经存在则获取锁的过期时间，和当前时间比较，如果锁已经过期，则设置新的过期时间，返回加锁成功。代码如下：
         那么这段代码问题在哪里？
         1. 由于是客户端自己生成过期时间，所以需要强制要求分布式下每个客户端的时间必须同步。
         2. 当锁过期的时候，如果多个客户端同时执行jedis.getSet()方法，那么虽然最终只有一个客户端可以加锁，但是这个客户端的锁的过期时间可能被其他客户端覆盖。（时间极短，可以忽略）
         3. 锁不具备拥有者标识，即任何客户端都可以解锁。
     * @param jedis
     * @param lockKey
     * @param expireTime
     * @return
     */
    public static boolean wrongGetLock2(Jedis jedis, String lockKey, int expireTime) {

        long expires = System.currentTimeMillis() + expireTime;
        String expiresStr = String.valueOf(expires);

        // 如果当前锁不存在，返回加锁成功
        if (jedis.setnx(lockKey, expiresStr) == 1) {
//            localExpire.set(Long.valueOf(expiresStr));
            return true;
        }

        // 如果锁存在，获取锁的过期时间
        String currentValueStr = jedis.get(lockKey);
        if (currentValueStr != null && Long.parseLong(currentValueStr) < System.currentTimeMillis()) {
            // 锁已过期，获取上一个锁的过期时间，并设置现在锁的过期时间
            String oldValueStr = jedis.getSet(lockKey, expiresStr);
            if (oldValueStr != null && oldValueStr.equals(currentValueStr)) {
//                localExpire.set(Long.valueOf(expiresStr));
                // 考虑多线程并发的情况，只有一个线程的设置值和当前值相同，它才有权利加锁
                return true;
            }
        }

        // 其他情况，一律返回加锁失败
        return false;

    }

    /**
     可以看到，我们解锁只需要两行代码就搞定了！第一行代码，我们写了一个简单的Lua脚本代码。
     第二行代码，我们将Lua代码传到jedis.eval()方法里，并使参数KEYS[1]赋值为lockKey，ARGV[1]赋值为requestId。eval()方法是将Lua代码交给Redis服务端执行。
     那么这段Lua代码的功能是什么呢？其实很简单，首先获取锁对应的value值，检查是否与requestId相等，如果相等则删除锁（解锁）。那么为什么要使用Lua语言来实现呢？因为要确保上述操作是原子性的。关于非原子性会带来什么问题，可以阅读【解锁代码-错误示例2】 。
     那么为什么执行eval()方法可以确保原子性，源于Redis的特性，下面是官网对eval命令的部分解释：
     简单来说，就是在eval命令执行Lua代码的时候，Lua代码将被当成一个命令去执行，并且直到eval命令执行完成，Redis才会执行其他命令。
     * 释放分布式锁
     * @param jedis Redis客户端
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }

    /**
     * 最常见的解锁代码就是直接使用jedis.del()方法删除锁，这种不先判断锁的拥有者而直接解锁的方式，会导致任何客户端都可以随时进行解锁，即使这把锁不是它的。
     * @param jedis
     * @param lockKey
     */
    public static void wrongReleaseLock1(Jedis jedis, String lockKey) {
        jedis.del(lockKey);
    }

    /**
     如代码注释，问题在于如果调用jedis.del()方法的时候，这把锁已经不属于当前客户端的时候会解除他人加的锁。
     那么是否真的有这种场景？答案是肯定的，比如客户端A加锁，一段时间之后客户端A解锁，在执行jedis.del()之前，锁突然过期了，
     此时客户端B尝试加锁成功，然后客户端A再执行del()方法，则将客户端B的锁给解除了。
     * @param jedis
     * @param lockKey
     * @param requestId
     */
    public static void wrongReleaseLock2(Jedis jedis, String lockKey, String requestId) {

        // 判断加锁与解锁是不是同一个客户端
        if (requestId.equals(jedis.get(lockKey))) {
            // 若在此时，这把锁突然不是这个客户端的，则会误解锁
            jedis.del(lockKey);
        }

//        Long expire = localExpire.get();
//        if (expire != null && expire > System.currentTimeMillis()) {
//            jedis.del(lockKey);
//        }

    }
    public static void main(String args[]){

    }

    // 可重入的分布式锁
    // https://mp.weixin.qq.com/s/bl4OWKUKPFD2VlcdArHBhQ
    //基于 ThreadLocal 实现方案;基于 Redis Hash 实现方案
    //可重入锁最大特性就是计数，计算加锁的次数。所以当可重入锁需要在分布式环境实现时，我们也就需要统计加锁次数。
    // 加锁方法首先判断当前线程是否已经已经拥有该锁，若已经拥有，直接对锁的重入次数加 1。
    //若还没拥有该锁，则尝试去 Redis 加锁，加锁成功之后，再对重入次数加 1 。
    public Boolean tryLock(String lockName, String requestId, long leaseTime) {
        Map<String, Integer> map = local.get();
        if(map.containsKey(lockName)){
            map.put(lockName, map.get(lockName) + 1);
            return true;
        }else{
            if(tryGetDistributedLock(jedis, lockName, requestId, leaseTime)){
                map.put(lockName, 1);
                return true;
            }
        }
        return false;
    }

//    释放锁的时首先判断重入次数，若大于 1，则代表该锁是被该线程拥有，所以直接将锁重入次数减 1 即可。
//    若当前可重入次数小于等于 1，首先移除 Map中锁对应的 key，然后再到 Redis 释放锁。
    public void unlock(String lockName, String requestId) {
        Map<String, Integer> map = local.get();
        if(map != null && map.containsKey(lockName)){
            if(map.get(lockName) <= 1){
                map.remove(lockName);
                Boolean result = releaseDistributedLock(jedis, lockName, requestId);
                if(!result){
                    throw new RuntimeException("释放锁失败");
                }
            }else{
                map.put(lockName, map.get(lockName) - 1);
            }
        }

    }

//    过期时间问题（该方案致命的问题）
//    上述加锁的代码可以看到，重入加锁时，仅仅对本地计数加 1 而已。这样可能就会导致一种情况，由于业务执行过长，Redis 已经过期释放锁。
//    而再次重入加锁时，由于本地还存在数据，认为锁还在被持有，这就不符合实际情况。
//    如果要在本地增加过期时间，还需要考虑本地与 Redis 过期时间一致性的，代码就会变得很复杂。

//    不同线程/进程可重入问题
//    狭义上可重入性应该只是对于同一线程的可重入，但是实际业务可能需要不同的应用线程之间可以重入同把锁。
//    而 ThreadLocal的方案仅仅只能满足同一线程重入，无法解决不同线程/进程之间重入问题。
//    不同线程/进程重入问题就需要使用下述方案 Redis Hash 方案解决。


//    基于 Redis Hash 可重入锁
//    加锁的 lua 脚本如下：
//    ---- 1 代表 true
//    ---- 0 代表 false
//        if (redis.call('exists', KEYS[1]) == 0) then
//        redis.call('hincrby', KEYS[1], ARGV[2], 1);
//        redis.call('pexpire', KEYS[1], ARGV[1]);
//        return 1;
//        end ;
//    if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then
//        redis.call('hincrby', KEYS[1], ARGV[2], 1);
//        redis.call('pexpire', KEYS[1], ARGV[1]);
//        return 1;
//        end ;
//    return 0;

//    如果 KEYS:[lock],ARGV[1000,uuid]
//    不熟悉 lua 语言同学也不要怕，上述逻辑还是比较简单的。
//    加锁代码首先使用 Redis exists 命令判断当前 lock 这个锁是否存在。
//    如果锁不存在的话，直接使用 hincrby创建一个键为 lock hash 表，并且为 Hash 表中键为 uuid 初始化为 0，然后再次加 1，最后再设置过期时间。
//    如果当前锁存在，则使用 hexists判断当前 lock 对应的 hash 表中是否存在  uuid 这个键，如果存在,再次使用 hincrby 加 1，最后再次设置过期时间。
//    最后如果上述两个逻辑都不符合，直接返回。

//    加锁代码如下:

//    // 初始化代码
//
//    String lockLuaScript = IOUtils.toString(ResourceUtils.getURL("classpath:lock.lua").openStream(), Charsets.UTF_8);
//    lockScript = new DefaultRedisScript<>(lockLuaScript, Boolean.class);
//
//    /**
//     * 可重入锁
//     *
//     * @param lockName  锁名字,代表需要争临界资源
//     * @param request   唯一标识，可以使用 uuid，根据该值判断是否可以重入
//     * @param leaseTime 锁释放时间
//     * @param unit      锁释放时间单位
//     * @return
//     */
//    public Boolean tryLock(String lockName, String request, long leaseTime, TimeUnit unit) {
//        long internalLockLeaseTime = unit.toMillis(leaseTime);
//        return stringRedisTemplate.execute(lockScript, Lists.newArrayList(lockName), String.valueOf(internalLockLeaseTime), request);
//    }


//    解锁的 Lua 脚本如下：
//    -- 判断 hash set 可重入 key 的值是否等于 0
//        -- 如果为 0 代表 该可重入 key 不存在
//    if (redis.call('hexists', KEYS[1], ARGV[1]) == 0) then
//        return nil;
//        end ;
//    -- 计算当前可重入次数
//        local counter = redis.call('hincrby', KEYS[1], ARGV[1], -1);
//    -- 小于等于 0 代表可以解锁
//    if (counter > 0) then
//        return 0;
//    else
//            redis.call('del', KEYS[1]);
//        return 1;
//        end ;
//    return nil;

//    首先使用 hexists 判断 Redis Hash 表是否存给定的域。
//    如果 lock 对应 Hash 表不存在，或者 Hash 表不存在 uuid 这个 key，直接返回 nil。
//    若存在的情况下，代表当前锁被其持有，首先使用 hincrby使可重入次数减 1 ，然后判断计算之后可重入次数，若小于等于 0，则使用 del 删除这把锁。

    // 初始化代码：


//    String unlockLuaScript = IOUtils.toString(ResourceUtils.getURL("classpath:unlock.lua").openStream(), Charsets.UTF_8);
//    unlockScript = new DefaultRedisScript<>(unlockLuaScript, Long.class);
//
//    /**
//     * 解锁
//     * 若可重入 key 次数大于 1，将可重入 key 次数减 1 <br>
//     * 解锁 lua 脚本返回含义：<br>
//     * 1:代表解锁成功 <br>
//     * 0:代表锁未释放，可重入次数减 1 <br>
//     * nil：代表其他线程尝试解锁 <br>
//     * <p>
//     * 如果使用 DefaultRedisScript<Boolean>，由于 Spring-data-redis eval 类型转化，<br>
//     * 当 Redis 返回  Nil bulk, 默认将会转化为 false，将会影响解锁语义，所以下述使用：<br>
//     * DefaultRedisScript<Long>
//     * <p>
//     * 具体转化代码请查看：<br>
//     * JedisScriptReturnConverter<br>
//     *
//     * @param lockName 锁名称
//     * @param request  唯一标识，可以使用 uuid
//     * @throws IllegalMonitorStateException 解锁之前，请先加锁。若为加锁，解锁将会抛出该错误
//     */
//    public void unlock(String lockName, String request) {
//        Long result = stringRedisTemplate.execute(unlockScript, Lists.newArrayList(lockName), request);
//        // 如果未返回值，代表其他线程尝试解锁
//        if (result == null) {
//            throw new IllegalMonitorStateException("attempt to unlock lock, not locked by lockName:+" + lockName + " with request: "
//                    + request);
//        }
//    }

 /*   解锁代码执行方式与加锁类似，只不过解锁的执行结果返回类型使用 Long。这里之所以没有跟加锁一样使用 Boolean ,这是因为解锁 lua 脚本中，三个返回值含义如下：
            1 代表解锁成功，锁被释放
0 代表可重入次数被减 1
            null 代表其他线程尝试解锁，解锁失败
    如果返回值使用 Boolean，Spring-data-redis 进行类型转换时将会把 null 转为 false，这就会影响我们逻辑判断，所以返回类型只好使用 Long。

*/
    /*spring-data-redis 低版本问题
    如果 Spring-Boot 使用 Jedis 作为连接客户端,并且使用Redis  Cluster 集群模式，需要使用  2.1.9 以上版本的spring-boot-starter-data-redis,不然执行过程中将会抛出：
    org.springframework.dao.InvalidDataAccessApiUsageException: EvalSha is not supported in cluster environment.*/


//    可重入分布式锁关键在于对于锁重入的计数，这篇文章主要给出两种解决方案，一种基于 ThreadLocal 实现方案，这种方案实现简单，运行也比较高效。但是若要处理锁过期的问题，代码实现就比较复杂。
//    另外一种采用 Redis Hash 数据结构实现方案，解决了 ThreadLocal 的缺陷，但是代码实现难度稍大，需要熟悉 Lua 脚本，以及Redis 一些命令。另外使用 spring-data-redis 等操作 Redis 时不经意间就会遇到各种问题。


//    下面主要我们来讲下 Lua 数据转化 Redis 的规则中几条比较容易踩坑：
//    1、Lua number 与 Redis 数据类型转换
//    Lua 中 number 类型是一个双精度的浮点数，但是 Redis 只支持整数类型，所以这个转化过程将会丢弃小数位。

//    2、Lua boolean 与 Redis 类型转换
//    这个转化比较容易踩坑，Redis 中是不存在 boolean 类型，所以当Lua 中 true 将会转为 Redis 整数 1。而 Lua 中 false 并不是转化整数，而是转化 null 返回给客户端。

//    3、Lua nil 与 Redis 类型转换
//    Lua nil 可以当做是一个空值，可以等同于 Java 中的 null。在 Lua 中如果 nil 出现在条件表达式，将会当做 false 处理。
//    所以 Lua nil 也将会 null 返回给客户端。
}
