package com.smy.WenMa.Tool;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Map;

@Component
public class Jwt {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expire-time}")
    private long expireTime;

    /**
     * 生成Token
     *
     * @param claims 要存储在Token中的信息
     * @return 生成的Token字符串
     */
    public String generateToken(Map<String, String> claims) {
        // 设置过期时间
        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MILLISECOND, (int) expireTime);

        // 创建JWT Builder
        JWTCreator.Builder builder = JWT.create();

        // 添加载荷
        claims.forEach(builder::withClaim);

        // 设置签名和过期时间，生成Token
        return builder.withExpiresAt(instance.getTime())
                .sign(Algorithm.HMAC256(secret));
    }

    /**
     * 验证Token
     *
     * @param token 要验证的Token
     * @return 验证后的DecodedJWT对象
     * @throws JWTVerificationException 验证失败时抛出异常
     */
    public DecodedJWT verifyToken(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
    }

    /**
     * 解析Token
     *
     * @param token 要解析的Token
     * @return 解析后的DecodedJWT对象
     */
    public DecodedJWT parseToken(String token) {
        return JWT.decode(token);
    }

    /**
     * 从Token中获取指定的Claim
     *
     * @param token     Token字符串
     * @param claimName Claim名称
     * @return Claim值
     */
    public String getClaim(String token, String claimName) {
        DecodedJWT decodedJWT = parseToken(token);
        return decodedJWT.getClaim(claimName).asString();
    }
}
