package org.ics.bufferedDao;

import org.apache.ibatis.annotations.Param;
import org.ics.model.Comment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class BufferedCommentDao extends BaseBufferedDao
{
    public ArrayList<Comment> getCommentList(Integer module, Integer offset, Integer limit)
    {
        ArrayList<Comment> ret = null;
        StringBuilder field = new StringBuilder();
        field.append(offset).append("_").append(limit);
        String data = jedisUtil.getHashValueCheckExpiration(CommentBufferName + module, field.toString());
        if (null != data)
            ret = gson.fromJson(data, ArrayList.class);
        return ret;
    }

    public Integer setCommentList(Integer module, Integer offset, Integer limit, ArrayList<Comment> list)
    {
        StringBuilder field = new StringBuilder();
        field.append(offset).append("_").append(limit);
        jedisUtil.setHashValueWithExpiration(CommentBufferName + module, field.toString(), list, 30);
        return 0;
    }

    public Integer getCommentCount(Integer module)
    {
        try
        {
            Integer ret = Integer.valueOf(jedisUtil.getStringValueCheckExpiration(CommentBufferCountName + module));
            return ret;
        } catch (NumberFormatException e)
        {
            return null;
        }
    }

    public Integer setCommentCount(Integer module, Integer count)
    {
        jedisUtil.setStringValueWithExpiration(CommentBufferCountName + module, 30, String.valueOf(count));
        return 0;
    }
}
