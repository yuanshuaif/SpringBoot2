package springboot.idempotent.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springboot.idempotent.interceptor.ApiIdempotentInterceptor;

/**
 * Created by 10326 on 2020/4/6.
 */
@Configuration
public class IdempotentConfiguration extends WebMvcConfigurationSupport{
    @Autowired
   private ApiIdempotentInterceptor apiIdempotentInterceptor;

    @Bean
    public InterceptorRegistry registry(){
        return new InterceptorRegistry();
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiIdempotentInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/token/getToken");
        super.addInterceptors(registry);
    }
}
