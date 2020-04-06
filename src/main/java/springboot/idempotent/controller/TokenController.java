package springboot.idempotent.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import springboot.idempotent.annotation.Idempotent;
import springboot.idempotent.config.IdempotentConstant;
import springboot.idempotent.service.ITokenService;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 10326 on 2020/4/6.
 */
@Controller
@RequestMapping("/token")
public class TokenController {
    @Autowired
    private ITokenService iTokenService;

    @RequestMapping("/getToken")
    public @ResponseBody String getToken(HttpServletRequest request){
        return iTokenService.createToken();
    }

    @Idempotent
    @RequestMapping("/checkToken")
    public @ResponseBody String checkToken(HttpServletRequest request){
        return "OK";
    }
}
