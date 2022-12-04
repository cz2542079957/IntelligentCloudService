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


}
