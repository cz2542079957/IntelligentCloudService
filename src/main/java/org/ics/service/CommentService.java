package org.ics.service;

import org.ics.bufferedDao.BufferedCommentDao;
import org.ics.model.Comment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CommentService extends BaseService
{
    /**
     * @Description 根据模块号、页号和单页数量获取评论列表
     * @Params [module, page, limit]
     * @Return 评论列表
     **/
    public ArrayList<Comment> getCommentList(Integer module, Integer page, Integer limit)
    {
        if (null == page) page = 1;
        if (null == limit) limit = 20;
        Integer offset = (page - 1) * limit;
        ArrayList<Comment> ret = bufferedCommentDao.getCommentList(module, offset, limit);
        if (null != ret)
        {
            return ret;
        }

        //从数据库拿数据
        ret = commentDao.getCommentList(module, offset, limit);
        if (null != ret)
        {
            //保存到缓存
            bufferedCommentDao.setCommentList(module, offset, limit, ret);
            return ret;
        }
        return null;
    }

    /**
     * @Description buffered 根据模块号获取评论数
     * @Params [module]
     * @Return 评论数
     **/
    public Integer getCommentCount(Integer module)
    {
        Integer ret = bufferedCommentDao.getCommentCount(module);
        if (null != ret)
        {
            return ret;
        }
        ret = commentDao.getCommentCount(module);
        if (null != ret)
        {
            bufferedCommentDao.setCommentCount(module, ret);
            return ret;
        }
        return null;
    }

    /**
     * @Description 添加评论
     * @Params [username, text, module]
     * @Return 0成功  -1失败  -2被限制
     **/
    public Integer addComment(String username, String text, Integer module)
    {
        //查看是否被限制
        if (bufferedCommentDao.checkAstrictUserSendComment(username, module))
        {
            return -2;
        }
        //添加限制
        bufferedCommentDao.astrictUserSendComment(username, module);
        long now = System.currentTimeMillis();
        Integer res = commentDao.addComment(username, text, module, now);
        if (res == 0)
        {
            return -1;
        }
        return 0;
    }
}
