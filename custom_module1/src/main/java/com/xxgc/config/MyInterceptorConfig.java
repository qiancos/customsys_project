package com.xxgc.config;


import com.xxgc.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//注册拦截器
@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private JwtInterceptor jwtInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration registration=
                registry.addInterceptor(jwtInterceptor);
        registration.addPathPatterns("/**")
                .excludePathPatterns("/user/login","/user/info","/user/logout","/error","/swagger-ui/**","/swagger-resources/**","/v3/**");

    }
}
