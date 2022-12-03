package org.ics.utils;

import com.alibaba.druid.sql.ast.expr.SQLInSubQueryExpr;
import jakarta.annotation.Resource;
import org.ics.service.JWTService;
import org.springframework.stereotype.Component;

import javax.lang.model.element.NestingKind;
import java.util.Map;

@Component
public class HeadersChecker
{
    @Resource
    public JWTService jwtService;

    public boolean needNothing()
    {
        return true;
    }

    /**
     * @Description 检查headers中是否有token，不校验正确性
     * @Params [headers]
     * @Return boolean
     **/
    public boolean needToken(Map<String, String> headers)
    {
        if (headers.containsKey("token"))
        {
            return true;
        }
        return false;
    }

    /**
     * @Description 需要合法的token
     * @Params [headers, username]
     * @Return boolean
     **/
    public boolean needValidToken(Map<String, String> headers, String username)
    {
        //不存在token
        if (!needToken(headers))
        {
            return false;
        }
        String token = headers.get("token");
        // 验证token
        Integer res = jwtService.verifyToken(token, username);
        if (res != 0)
        {
            return false;
        }
        return true;
    }

    /**
     * @Description 从headers中获取token
     * @Params [headers]
     * @Return token, 不存在则返回null
     **/
    public String getToken(Map<String, String> headers)
    {
        if (!needToken(headers))
        {
            //不存在 返回null
            return null;
        }
        return headers.get("token");
    }

}
