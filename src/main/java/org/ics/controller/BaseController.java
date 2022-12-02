package org.ics.controller;

import jakarta.annotation.Resource;
import lombok.Data;
import org.ics.service.AuthService;
import org.ics.utils.ConfigGetter;
import org.ics.utils.ReturnStates;
import org.ics.utils.python.CheckPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class BaseController
{
    @Resource
    CheckPassword checkPassword;
    @Resource
    ConfigGetter configGetter;
    @Resource
    AuthService authService;

    /**
     * @Description 无数据返回值构造器
     * @Params [ret, stat   ``e]
     **/
    public Map<String, Object> setReturnState(Map<String, Object> ret, ReturnStates state)
    {
        return setReturnState(ret, state, (ReturnKV) null);
    }

    /**
     * @Description 单数据返回值构造器
     * @Params [ret, state, data]
     **/
    public Map<String, Object> setReturnState(Map<String, Object> ret, ReturnStates state, Object data)
    {
        return setReturnState(ret, state, new ReturnKV("data", data), null);
    }

    /**
     * @Description 多数据返回值构造器
     * @Params [ret, state, kvs]
     **/
    public Map<String, Object> setReturnState(Map<String, Object> ret, ReturnStates state, ReturnKV... kvs)
    {
        ret.put("code", state.getCode());
        ret.put("msg", state.getMsg() + state.getType());
        for (ReturnKV temp : kvs)
        {
            if (null == temp)
                continue;
            ret.put(temp.getKey(), temp.getVal());
        }
        return ret;
    }
}

@Data
class ReturnKV
{
    public ReturnKV(String key, Object val)
    {
        this.key = key;
        this.val = val;
    }

    private String key;
    private Object val;
}
 