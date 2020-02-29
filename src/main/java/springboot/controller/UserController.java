package springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springboot.entity.User;
import springboot.service.UserService;

/**
 * Created by Administrator on 2019/3/2.
 */
@RestController
public class UserController {

    @Autowired
    private UserService service;
    @RequestMapping("/entity/save")
    String save() {
        User user = new User();
        user.setName("lsj");
        user.setAge("18");
        user.setSex("女");
        service.save(user);
        return "Hello "+"!!!";
    }
    @RequestMapping("/entity/findById")
    String findById() {
        return service.findById("1").toString();
    }
    @RequestMapping("/entity/findByIdCache")
    String findByIdCache() {
        return service.findByIdCache("5").toString();
    }
    @RequestMapping("/entity/findAll")
    String findAll() {
        return service.findAll().toString();
    }
    @RequestMapping("/entity/deleteById")
    String deleteById() {
        service.deleteById("2");
        return "Hello "+"!!!";
    }
    @RequestMapping("/entity/update")
    String update(@RequestParam("id") int id) {
       service.update("lsj", id);
        return "Hello "+"!!!";
    }

    @RequestMapping("/entity/select")
    String select() {
        return service.select(1).toString();
    }

    @RequestMapping("/entity/select1")
    String select1() {
        return service.select1(3).toString();
    }

    @RequestMapping("/entity/findByNameAndAge")
    String findByNameAndAge() {
        return service.findByNameAndAge("ys", "18").toString();
    }

   /* @RequestMapping("/entity/findByName")
    String findByName() {
        Sort sort = new Sort(Sort.Direction.DESC, new String[]{"age"});
        return service.findByName("lsj", sort).toString();
    }*/

    @RequestMapping("/entity/findByName1")
    String findByName1() {
        Sort sort = new Sort(Sort.Direction.DESC, new String[]{"age"});
        Pageable pageable = new PageRequest(1, 1, sort);
        return service.findByName("lsj", pageable).toString();
    }

    @RequestMapping("/entity/transactionTemp")
    String transactionTemp(@RequestParam("id") int id) {
        service.transactionTemp("dk", id);
        return null;
    }

}
