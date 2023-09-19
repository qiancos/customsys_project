package com.xxgc.interceptor;


import com.alibaba.fastjson2.JSON;
import com.xxgc.common.Result;
import com.xxgc.common.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    //JWT验证拦截器
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token =request.getHeader("X-Token");
        System.out.println(request.getRequestURI()+"待验证:"+token);
        if (token!=null){
            try{
                jwtUtil.parseToken(token);
                log.debug(request.getRequestURI()+"放行....");
                return true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        log.debug(request.getRequestURI()+"禁止访问...");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(Result.fail(20003,"JWT无效，请重新登录")));
        return false;
    }
}
