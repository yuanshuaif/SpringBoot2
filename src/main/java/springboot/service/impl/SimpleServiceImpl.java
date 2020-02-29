package springboot.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import springboot.entity.User;
import springboot.mybatis.annotationMapper.SimpleMapper;
import springboot.repository.UserRepository;
import springboot.service.SimpleService;
import springboot.service.UserService;

import java.util.List;
import java.util.Optional;

/**
 * Created by Administrator on 2019/3/2.
 * 1.内层异常吃掉，事务不会回滚
 * 2.内层异常抛出，有事务注解,即使外层catch，事务回滚(Transaction silently rolled back because it has been marked as rollback-only)
 * 3.内层异常抛出，无事务注解, 外层catch, 事务不会回滚
 */
@Component
public class SimpleServiceImpl implements SimpleService {

    @Autowired
    private SimpleMapper simpleMapper;

   /**
     *  Transaction silently rolled back because it has been marked as rollback-only
     *  此处(@Transactional)切面会捕获到异常，会标记事务为只读，会导致事务回滚
     * @param user
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void update(User user){
        try {
            simpleMapper.update(user); // 回滚
            String s = null;
            s.toString();
        }catch (Exception e){
//            throw new RuntimeException(); // 注释掉不回滚
        }
    }

}
