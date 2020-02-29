package springboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * Created by Administrator on 2019/3/9.
 */
@RestController
public class RedisSessionController {
    @RequestMapping("/uid")
    String uid(HttpSession session) {
        UUID uid = (UUID) session.getAttribute("uid");//  spring的过滤器会拦截该session，并从redis缓存中读取
        if (uid == null) {
            uid = UUID.randomUUID();
        }
        session.setAttribute("uid", uid);// spring的过滤器会拦截该session，并将其同步到redis缓存中
        return session.getId();
    }
}
