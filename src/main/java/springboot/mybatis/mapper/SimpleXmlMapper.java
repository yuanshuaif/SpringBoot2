package springboot.mybatis.mapper;

import springboot.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2019/3/16.
 */
public interface SimpleXmlMapper {
    List<User> getAll();

    void insert(User user);

    void update(User user);
}
