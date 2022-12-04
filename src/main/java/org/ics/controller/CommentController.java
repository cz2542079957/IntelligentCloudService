package org.ics.controller;


import jakarta.websocket.server.PathParam;
import org.apache.ibatis.annotations.Param;
import org.ics.model.Comment;
import org.ics.utils.ReturnStates;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/ics/comment")
public class CommentController extends BaseController
{
    @GetMapping("/list")
    public Map<String, Object> getCommentList(@RequestHeader Map<String, String> headers, @PathParam("module") Integer module, @PathParam("page") Integer page, @PathParam("limit") Integer limit)
    {
        Map<String, Object> ret = new HashMap<>();
        //获取评论总数
        Integer count = commentService.getCommentCount(module);
        //获取评论
        ArrayList<Comment> list = commentService.getCommentList(module, page, limit);
        return setReturnState(ret, ReturnStates.success, new ReturnKV("count", count), new ReturnKV("data", list));
    }


}
