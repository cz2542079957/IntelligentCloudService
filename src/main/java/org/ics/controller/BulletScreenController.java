package org.ics.controller;

import jakarta.websocket.server.PathParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/bulletScreen")
public class BulletScreenController extends BaseController
{

    @GetMapping("/list")
    public Map<String, Object> getBulletScreenList(@PathParam("page") Integer page, @PathParam("limit") Integer limit, @RequestHeader Map<String, String> headers)
    {
        Map<String, Object> ret = new HashMap<>();
        System.out.println(page);
        System.out.println(limit);


        return ret;
    }
}
