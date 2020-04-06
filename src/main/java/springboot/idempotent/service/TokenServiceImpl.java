package springboot.idempotent.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import springboot.idempotent.config.IdempotentConstant;
import springboot.idempotent.util.JedisUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Created by 10326 on 2020/4/6.
 */
@Service
public class TokenServiceImpl implements ITokenService{

    @Autowired
    private JedisUtil jedisUtil;

    @Override
    public String createToken() {
        StringBuffer token = new StringBuffer();
        token.append(IdempotentConstant.PREFIX).append(UUID.randomUUID().toString());
        jedisUtil.set(token.toString(), token.toString(), IdempotentConstant.EXPIRE_TIME);
        return token.toString();
    }

    @Override
    public boolean checkToken(HttpServletRequest request) {
        String token = request.getHeader(IdempotentConstant.TOKEN_NAME);
        if(StringUtils.isEmpty(token)){
            token = request.getParameter(IdempotentConstant.TOKEN_NAME);
            if(StringUtils.isEmpty(token)){
                throw new RuntimeException("token为空");
            }
        }
        if(!jedisUtil.exists(token)){
            throw new RuntimeException("token不存在");
        }
        Long del = jedisUtil.del(token);
        if(del < 1){
            throw new RuntimeException("token已过期");
        }
        return true;
    }
}
