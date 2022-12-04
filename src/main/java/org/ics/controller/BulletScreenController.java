package org.ics.controller;

import jakarta.websocket.server.PathParam;
import lombok.val;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ics.model.BulletScreen;
import org.ics.utils.ReturnStates;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ics/bulletScreen")
public class BulletScreenController extends BaseController
{

    @GetMapping("/list")
    public Map<String, Object> getBulletScreenList(@PathParam("page") Integer page, @PathParam("limit") Integer limit, @RequestHeader Map<String, String> headers)
    {
        Map<String, Object> ret = new HashMap<>();
        ArrayList<BulletScreen> data = bulletScreenService.getBulletScreenList(page, limit);
        Integer count = bulletScreenService.getCount();
        return setReturnState(ret, ReturnStates.success, new ReturnKV("data", data), new ReturnKV("count", count));
    }


    @PostMapping("/send")
    @Transactional(rollbackFor = {Exception.class})
    public Map<String, Object> sendBulletScreen(@RequestHeader Map<String, String> headers, @RequestBody Map<String, String> req)
    {
        Map<String, Object> ret = new HashMap<>();
        //验证headers
        if (!req.containsKey("username") || !req.containsKey("text"))
        {
            //参数缺失
            return setReturnState(ret, ReturnStates.bsParamsLost);
        }
        //验证token
        String username = req.get("username");
        String text = req.get("text");
        if (!headersChecker.needValidToken(headers, username))
        {
            //token 验证不通过
            return setReturnState(ret, ReturnStates.tokenError);
        }

        Integer res = bulletScreenService.addBulletScreen(username, text);
        if (res != 0)
        {
            if (res == -1)
                return setReturnState(ret, ReturnStates.bsAddBulletScreenError);
            else if (res == -2)
                return setReturnState(ret, ReturnStates.bsAddBulletScreenAstrict);
        }
        return setReturnState(ret, ReturnStates.success);
    }
}
