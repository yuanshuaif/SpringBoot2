package springboot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import springboot.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2019/3/2.
 */
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * 自定义sql 更新
     *
     * @param name
     */
    @Modifying
    @Transactional
    @Query(value = "update user u set u.name =:name where u.id =:id")
    public void update(@Param("name") String name, @Param("id") int id);

    /**
     * 1.JpaRepository 默认的查询方法
     * 2.@Query自定义查询 （命名参数）
     * @param id
     * @return
     */
    @Query(value = "select u from user u where u.id =:id")
    public User select(@Param("id") int id);

    /**
     * @Query自定义查询（索引参数）
     * @param id
     * @return
     */
    @Query(value = "select u from user u where u.id =?1")
    public User select1(int id);

    /**
     * 3.常规查询，根据属性名定义查询方法
     * @param name
     * @param age
     * @return
     */
    public List<User> findByNameAndAge(String name, String age);

    /**
     * 排序查询
     * @param name
     * @param sort
     * @return
     */
//    public List<User> findByName(String name, Sort sort);

    /**
     * 分页查询
     * @param name
     * @param pageable
     * @return
     */
    public Page<User> findByName(String name, Pageable pageable);
}
