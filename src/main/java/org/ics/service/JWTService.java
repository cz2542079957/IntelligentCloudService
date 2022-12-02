package org.ics.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
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
    public String createToken(long id, String username)
    {
        long now = System.currentTimeMillis();
        JWTCreator.Builder builder = JWT.create();
        builder.withAudience(String.valueOf(id));
        builder.withIssuer("ICS");
        builder.withIssuedAt(new Date(now));
        builder.withExpiresAt(new Date(now + expiredTimeInterval));
        builder.withClaim("username", username);

        return builder.sign(Algorithm.HMAC256(id + username));
    }

    /**
     * @Description 验证token是否正确
     * @Params [token, id, username]
     * @Return 返回boolean
     **/
    public boolean verifyToken(String token, long id, String username)
    {
        try
        {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(id + username)).build();
            jwtVerifier.verify(token);
        } catch (JWTVerificationException e)
        {
            return false;
        }
        return true;
    }

    /**
     * @Description 获取token中的id
     * @Params [token]
     * @Return 返回用户id
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

    /**
     * @Description 获取token中的username
     * @Params [token, name]
     * @Return 返回解析的username
     **/
    public String getUsername(String token)
    {
        return JWT.decode(token).getClaim("username").asString();
    }
}
