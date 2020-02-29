package springboot.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import springboot.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2019/3/2.
 */

public interface UserService {
    public void save(User user);
    public User findById(String id);
    public User findByIdCache(String id);
    public List<User> findAll();
    public void deleteById(String id);
    public void update(String name, int id);
    public User select(int id);
    public User select1(int id);
    public List<User> findByNameAndAge(String name, String age);
//    public List<User> findByName(String name, Sort sort);
    public List<User> findByName(String name, Pageable pageable);

    public void transactionTemp(String name, int id);
}
