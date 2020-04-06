package springboot.idempotent.service;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by 10326 on 2020/4/6.
 */
public interface ITokenService {
    String createToken();
    boolean checkToken(HttpServletRequest request);
}
