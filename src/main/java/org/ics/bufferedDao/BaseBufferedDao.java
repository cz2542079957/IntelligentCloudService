package org.ics.bufferedDao;

import com.google.gson.Gson;
import jakarta.annotation.Resource;
import jakarta.annotation.Resources;
import org.ics.utils.JedisUtil;

public class BaseBufferedDao
{
    @Resource
    JedisUtil jedisUtil;
    @Resource
    Gson gson;

    public static String BulletScreenBufferName = "BulletScreen";
    public static String BulletScreenCountBufferName = "BulletScreenCount";
    public static String AstrictUserSendBulletScreenBufferName = "AstrictUserSendBulletScreen";

    public static String CommentBufferName = "Comment"; //前缀 还需要module
    public static String CommentBufferCountName = "CommentCount";  //前缀 还需要module


}
