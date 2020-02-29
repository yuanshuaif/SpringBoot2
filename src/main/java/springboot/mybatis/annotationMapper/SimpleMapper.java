package springboot.mybatis.annotationMapper;

import org.apache.ibatis.annotations.*;
import springboot.entity.User;

import java.util.List;

/**
 * Created by Administrator on 2019/3/16.
 * 无xml文件的Mapper
 */
public interface SimpleMapper {
    @Select("SELECT * FROM user")
    @Results({
            @Result(property = "age",  column = "age"),
            @Result(property = "name", column = "name")
    })
    List<User> getAll();

    @Insert("INSERT INTO user(id,name,age) VALUES(#{user.id},#{user.name}, #{user.age})")
    void insert(@Param("user") User user);

    @Update("UPDATE user SET name = #{user.name} WHERE id = #{user.id}")
    void update(@Param("user") User user);
}
