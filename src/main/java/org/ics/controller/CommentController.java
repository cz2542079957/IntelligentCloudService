package org.ics.controller;


import jakarta.websocket.server.PathParam;
import org.apache.ibatis.annotations.Param;
import org.ics.model.Comment;
import org.ics.utils.ReturnStates;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/send")
    public Map<String, Object> sendComment(@RequestHeader Map<String, String> headers, @RequestBody Map<String, Object> req)
    {
        Map<String, Object> ret = new HashMap<>();
        if (!req.containsKey("username") || !req.containsKey("text") || !req.containsKey("module"))
        {
            return setReturnState(ret, ReturnStates.commentParamsLost);
        }
        //参数提取
        String username = (String) req.get("username");
        String text = (String) req.get("text");
        Integer module = (Integer) req.get("module");
        Integer res = commentService.addComment(username, text, module);
        if (res == -1)
        {
            // 添加数据失败
            return setReturnState(ret, ReturnStates.commentAddCommentError);
        }
        if (res == -2)
        {
            //被限制
            return setReturnState(ret, ReturnStates.commentAddCommentAstrict);
        }
        // 数据正常添加
        return setReturnState(ret, ReturnStates.success);

    }

}
