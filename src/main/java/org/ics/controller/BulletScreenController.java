package org.ics.controller;

import jakarta.websocket.server.PathParam;
import org.ics.model.BulletScreen;
import org.ics.utils.ReturnStates;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    //todo 发送弹幕

}
