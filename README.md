##SpringDataJPA+Redis+Rest+Mybatis+SpringTask+Quartz+Mail+Actuator结合

#JPA的使用:
    1）导jar包
    2）yml文件配置
    3）实体类使用@Entity注解
    4）自定义repository继承JPARepository
    
#Redis缓存的使用:
    1）导jar包
    2）properties文件配置
    3）配置类配置RedisCacheManager
    4）实体类实现Serializable接口
    5) @EnableCaching
    6）使用redis+cache注解
    （也可以使用redisTemplate）
#分布式session的实现:
    1）导包web、session、redis
    2）redis的基础配置
    3）添加注解@EnableRedisHttpSession
    4）使用httpsession，其他的 Session 同步到 Redis 等操作，框架已经自动帮你完成。
#Redis秒杀场景的实现:
#分布式锁一般有三种实现方式：1. 数据库乐观锁；2. 基于Redis的分布式锁；3. 基于ZooKeeper的分布式锁。
#Redis的分布式锁：
    需要满足4个条件：
    1）互斥性。任意时间，只有一个客户端可以持有锁。（）
    2）容错性。只要大部分的Redis节点正常，客户端就能够加锁和解锁。（Redis集群）
    3）不会发生死锁。即使一个客户端在持有锁的期间崩溃而没有主动解锁，其他客户端也能够获取锁。（设置超时时间）
    4）解铃还须系铃人。加锁和解锁必须是同一个客户端，客户端不能解其他客户端的锁。
    具体实现：
    1）导包
    <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
        <version>2.9.0</version>
    </dependency>
    
#Rest
    1.引入依赖的启动器：spring-boot-starter-data-rest
    2.在application.yml中通过配置spring.data.rest的相关属性来配置 RepositoryRestConfiguration
    3.@RepositoryRestResource(path = "users"),@RestResource 暴露自定义REST服务

#Mybatis
    Mybatis的2中方式：
    1.无配置文件注解版
        1）pom.xml文件的配置
        2）properties文件的配置
            数据库连接池的相关配置
            (Spring Boot 会自动加载 spring.datasource.* 相关配置，<br/>
            数据源就会自动注入到 sqlSessionFactory 中，sqlSessionFactory 会自动注入到 Mapper 中)
        3) 在配置类上添加@MapperScan注解
            扫描哪些Mapper文件
        4）Mapper文件中定义接口；使用@Select、@Delete、@Update、@Insert等注解编写增删改查语句
            @Results：指定返回哪些字段
            @Result 修饰返回的结果集，关联实体类属性和数据库字段一一对应，
            如果实体类属性和数据库属性名保持一致，就不需要这个属性来修饰。
         *）主配置文件的配置在properties中进行配置
    2.简易版xml
        1）、2）、3）
        4）在properties文件中配置主配置文件及映射文件的位置
        5）主配置文件(可不用)、Mapper映射文件、Mapper接口
            
#SpringTask
     1)@EnableScheduling
     2)@Scheduled(cron="*/6 * * * * ?")
     3)@EnableAsync 使用线程池(使用线程池的原因，防止一个任务阻塞导致其他任务阻塞)
     
#Quartz
    特点调度与任务分离
    1)添加jar包依赖
    2)实现Job接口并且在execute方法中实现自己的业务逻辑
    3)创建JobDetail实例并定义Trigger注册到scheduler，启动scheduler开启调度
    
#Mail
    1）jar包
    2）properties文件配置
    3）Mail POJO
    4）service服务类
    5）test测试类
 
#Actuator
    1.SpringBoot是微服务，微服务的特点决定了功能模块的部署是分布式的。系统间通过服务调用进行交互。
    health:health 主要用来检查应用的运行状态，这是我们使用最高频的一个监控点。
    info:info 就是我们自己配置在配置文件中以 info 开头的配置信息
    beans:根据示例就可以看出，展示了 bean 的别名、类型、是否单例、类的地址、依赖等信息。
    conditions:使用 conditions 可以在应用运行时查看代码了某个配置在什么条件下生效，或者某个自动配置为什么没有生效。
    heapdump:返回一个 GZip 压缩的 JVM 堆 dump。内存快照
    shutdown:开启接口优雅关闭 Spring Boot 应用，要使用这个功能首先需要在配置文件中开启：management.endpoint.shutdown.enabled=true
    mappings:描述全部的 URI 路径，以及它们和控制器的映射关系
    threaddump:threaddump 接口会生成当前线程活动的快照。
    
#spring事务（传播行为）
    required：核心点事务切面有没有感受到异常
    required_new：独立事务
    nested：外层没有事务新建事务；外部方法有事务，则内部事务为外部事务的子事务
    （外层异常，子事务回滚；子事务异常，外事务感知到，外事务回滚；子事务异常，外事务感知不到，外事务不回滚）
    + 其他4种不常用的事务传播行为