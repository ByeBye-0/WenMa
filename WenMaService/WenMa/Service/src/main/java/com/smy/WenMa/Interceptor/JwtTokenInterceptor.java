package com.smy.WenMa.Interceptor;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.smy.WenMa.Tool.Jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.HandlerInterceptor;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class JwtTokenInterceptor implements HandlerInterceptor {
    @Autowired
    Jwt jwt;
    //    白名单
    public static final List<String> whitelist = List.of(
            "/login/", "/courses/all", "/courses/detail/", "/courses/{coursesId}"
    );

    private boolean unauthorized(HttpServletResponse response ,String msg)
    {
//        SC_UNAUTHORIZED = 401;
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=utf-8");
        try {
            response.getWriter().write("{\"code\":401,\"msg\":\"" + msg + "\",\"data\":null}");
        } catch (IOException ignored) {

        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//       预检cors
        if (CorsUtils.isCorsRequest(request)) {
            return true;
        }
        String url = request.getRequestURI();
        if (whitelist.stream().allMatch(url::startsWith)) {
            return true;
        }
        String token = request.getHeader("token");
        if(token==null||token.isEmpty()){
            return  unauthorized(response,"<缺少登录凭证>");
        }

        try {
            DecodedJWT decoded = jwt.verifyToken(token);   // 验签 + 校验过期，失败抛异常
            request.setAttribute("userId", decoded.getClaim("userId").asString());
            request.setAttribute("userType", decoded.getClaim("userType").asString());
            return true;
        } catch (JWTVerificationException e) {
            return unauthorized(response, "登录已失效，请重新登录");
        }
    }


    /*
    *
    *
public class JwtTokenInterceptor implements HandlerInterceptor {


    // 无需登录的接口（前缀匹配），其余一律鉴权
    private static final List<String> WHITELIST = List.of(
            "/login/", "/courses/all", "/courses/detail/", "/courses/{coursesId}"
    );

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (CorsUtils.isPreFlightRequest(request)) return true;   // 预检交给 CORS 过滤器

        String url = request.getRequestURI();
        if (WHITELIST.stream().anyMatch(url::startsWith)) return true;

        String token = request.getHeader("token");
        if (token == null || token.isBlank()) {
            return unauthorized(response, "缺少登录凭证");
        }

        try {
            DecodedJWT decoded = jwt.verifyToken(token);   // 验签 + 校验过期，失败抛异常
            request.setAttribute("userId", decoded.getClaim("userId").asString());
            request.setAttribute("userType", decoded.getClaim("userType").asString());
            return true;
        } catch (JWTVerificationException e) {
            return unauthorized(response, "登录已失效，请重新登录");
        }
    }

    private boolean unauthorized(HttpServletResponse response, String msg) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        try {
            response.getWriter().write("{\"code\":401,\"msg\":\"" + msg + "\",\"data\":null}");
        } catch (IOException ignored) {}
        return false;
    }
}
    * */
}
