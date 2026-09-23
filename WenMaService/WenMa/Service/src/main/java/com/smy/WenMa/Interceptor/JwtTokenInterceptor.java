package com.smy.WenMa.Interceptor;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.smy.WenMa.Tool.Jwt;
import jakarta.servlet.ServletRegistration;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {
    @Autowired
    Jwt jwt;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getRequestURI();
        String token = request.getHeader("token");
        if (url.contains("/student/space")) {
            DecodedJWT res = jwt.parseToken(token);
//            System.out.println("getToken"+res.getToken());
//            System.out.println("getHeader"+res.getHeader());
//            System.out.println("getPayload"+res.getPayload());
//            System.out.println("getSignature"+res.getSignature());
            if (res == null) {
                return false;
            }
            return true;
        } else if (url.contains("/register")) {
            return true;
        }
        return false;
    }

}
