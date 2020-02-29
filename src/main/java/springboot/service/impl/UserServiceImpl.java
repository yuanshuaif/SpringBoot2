package springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import springboot.entity.User;
import springboot.repository.UserRepository;
import springboot.service.SimpleService;
import springboot.service.UserService;

import java.util.List;
import java.util.Optional;

/**
 * Created by Administrator on 2019/3/2.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SimpleService simpleService;
    @Override
    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * @CachePut/@CacheEvict
     * @param id
     * @return
     */
    @Cacheable(cacheNames = "user", key = "#id+'ys'")
    @Override
    public User findById(String id) {
        Optional<User> optional = userRepository.findById(Integer.valueOf(id));
        return optional.get();
    }

    @Cacheable(value = "user", key = "#root.args[0]")
    @Override
    public User findByIdCache(String id) {
        Optional<User> optional = userRepository.findById(Integer.valueOf(id));
        return optional.orElseGet(User::new);
    }

    /**
     * 不指定key使用默认的生成策略
     * @return
     */
    @Cacheable(cacheNames = "user", keyGenerator = "keyGenerator")
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(Integer.valueOf(id));
    }

    /**
     * 自定义sql——更新
     */
    @Override
    public void update(String name, int id) {
        userRepository.update(name, id);
    }
    /**
     * 自定义sql——查询（命名参数）
     */
    @Override
    public User select(int id) {
        return userRepository.select(id);
    }

    /**
     * 自定义sql——查询（索引参数）
     */
    @Override
    public User select1(int id) {
        return userRepository.select1(id);
    }

    /**
     * 常规查询，根据属性定义方法
     */
    @Override
    public List<User> findByNameAndAge(String name,String age){
        return userRepository.findByNameAndAge(name, age);
    }

    /**
     * 排序查询
     * @param name
     * @param sort
     * @return
     */
  /*  @Override
    public List<User> findByName(String name,Sort sort){
        return userRepository.findByName(name, sort);
    }*/
    /**
     * 分页查询
     * @param name
     * @param pageable
     * @return
     */
    @Override
    public List<User> findByName(String name,Pageable pageable){
        Page<User> list = userRepository.findByName(name, pageable);
        return list.getContent();
    }

    /**
     * 0.外层异常全部回滚
     * @param name
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    @Override
    public void transactionTemp(String name, int id) {
        User user = userRepository.select(id);
        userRepository.update(name, id);
        update2();
        user.setName("lsj");
        try {
            simpleService.update(user);
        }catch (Exception e){

        }

    }

    // 4.有事务调无事务，与调用方共用一个事务
    // 5.无事务调用有事务，事务不生效
    private void update2(){
        userRepository.update("lsj", 2);
    }
}
