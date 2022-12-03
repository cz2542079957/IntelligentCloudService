package org.ics.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.context.event.EventPublicationInterceptor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService
{
    public long expiredTimeInterval = 3600000 * 24 * 3; //过期间隔3天

    /**
     * @Description 创建token
     * @Params [id, username]
     * @Return 返回token串
     **/
    public String createToken(String username)
    {
        long now = System.currentTimeMillis();
        JWTCreator.Builder builder = JWT.create();
        builder.withAudience(String.valueOf(username));
        builder.withIssuer("ICS");
        builder.withIssuedAt(new Date(now));
        builder.withExpiresAt(new Date(now + expiredTimeInterval));
        return builder.sign(Algorithm.HMAC256(username));
    }

    /**
     * @Description 验证token是否正确
     * @Params [token, username]
     * @Return -2过期  -1不正确  0正确
     **/
    public Integer verifyToken(String token, String username)
    {
        try
        {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(username)).build();
            jwtVerifier.verify(token);
            Date expiredTime = JWT.decode(token).getExpiresAt();
            if (expiredTime.compareTo(new Date()) <= 0)
            {
                //过期
                return -2;
            }
        } catch (JWTVerificationException e)
        {
            return -2;
        }
        return 0;
    }

    /**
     * @Description 获取token中的username
     * @Params [token]
     * @Return 返回用户username
     **/
    public String getAudience(String token)
    {
        String audience = null;
        try
        {
            audience = JWT.decode(token).getAudience().get(0);
        } catch (JWTDecodeException e)
        {
            e.printStackTrace();
        }
        return audience;
    }
}
