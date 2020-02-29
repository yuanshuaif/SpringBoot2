package springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import springboot.entity.User;

/**
 * Created by Administrator on 2019/3/2.
 */
//@RepositoryRestResource
@RepositoryRestResource(path = "users")
public interface RestRepository extends JpaRepository<User, Integer> {
    /**
     * 1.spring-data-rest 支持将JpaRepository、MongoRepository的repository转换成REST服务
     * 2.引入依赖的启动器：spring-boot-starter-data-rest
     * 3.在application.yml中通过配置spring.data.rest的相关属性来配置 RepositoryRestConfiguration
     * 4.@RepositoryRestResource(path = "users"),@RestResource 暴露自定义REST服务
     * 5.Jpa的repository转化成的REST服务
     *      http://localhost:8080/lsj/users        查询全部数据            GET
     *      http://localhost:8080/lsj/users/5      根据Id查询某一条数据    GET
     *      http://localhost:8080/lsj/users        保存数据                POST   {"age":"17","name":"ys"}
     *      http://localhost:8080/lsj/users/3      更新某一条数据          PUT    {"age":"27","name":"ys"} 根据主键更新全部字段
     *      http://localhost:8080/lsj/users/4      删除某一条数据          DELETE
     *      http://localhost:8080/lsj/users?page=2&size=2          分页           page 0是第一页,size 每页显示多少个（GET请求）
     *      http://localhost:8080/lsj/users?page=2&size=2&sort=age,asc   排序     联合使用时，先排序后分页(GET请求)
     * 6.在配置文件中指定的根路径
     *       http://localhost:8080/{profile}/users                       profile(lsj):配置文件中指定的根路径
     *      "profile": {
     *          "href": "http://localhost:8080/profile/users"
     *       }
     * 7.调用自定义的REST服务
     *      http://localhost:8080/lsj/users/search/findByNameStartsWith?name=ki（GET请求）
     *      "search": {
     *          "href": "http://localhost:8082/ys/users/search"
     *      }
     * 8.默认的节点路径 实体类+s  =>> users                          Restful的入口
     *   自定义的节点路径 使用@RepositoryRestResource(path = "users")
     * 9.RestFul接口支持HAL和JSON格式
     *   SpringDataRest支持HAL格式（分页的信息；请求地址的信息(分页、排序的url；自定义Rest服务的url；根路径的url等等）；具体数据信息）
     * @param name
     * @return
     */

    @RestResource(path = "findByNameStartsWith")
    User findByNameStartsWith(@Param("name") String name);
}
