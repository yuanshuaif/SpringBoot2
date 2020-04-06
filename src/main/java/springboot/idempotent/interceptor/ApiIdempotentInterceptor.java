package springboot.idempotent.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import springboot.idempotent.annotation.Idempotent;
import springboot.idempotent.service.ITokenService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.lang.reflect.Method;

/**
 * Created by 10326 on 2020/4/6.
 */
@Component
public class ApiIdempotentInterceptor implements HandlerInterceptor{
    @Autowired
    private ITokenService iTokenService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)){
           return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();
        Idempotent idempotent = method.getAnnotation(Idempotent.class);
        if(idempotent != null){
            OutputStream os = null;
            try {
                iTokenService.checkToken(request);
            }catch (RuntimeException re){
                os = response.getOutputStream();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                os.write(re.getMessage().getBytes());
            }finally {
                os.close();
            }
        }
        return true;
    }
}
