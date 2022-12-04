package org.ics.bufferedDao;

import org.ics.model.BulletScreen;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.ArrayList;

@Component
public class BufferedBulletScreenDao extends BaseBufferedDao
{
    public ArrayList<BulletScreen> getBulletScreenList(Integer offset, Integer limit)
    {
        ArrayList<BulletScreen> ret = null;
        StringBuilder key = new StringBuilder();
        key.append(offset);
        key.append("_");
        key.append(limit);
        String data = jedisUtil.getHashValueCheckExpiration(BulletScreenBufferName, key.toString());
        if (null != data)
            ret = gson.fromJson(data, ArrayList.class);
        return ret;
    }

    public Integer setBulletScreenList(Integer offset, Integer limit, ArrayList<BulletScreen> list)
    {
        StringBuilder key = new StringBuilder();
        key.append(offset);
        key.append("_");
        key.append(limit);
        jedisUtil.setHashValueWithExpiration(BulletScreenBufferName, key.toString(), list);
        return 0;
    }

    public Integer getCount()
    {
        try
        {
            Integer ret = Integer.valueOf(jedisUtil.getStringValueCheckExpiration(BulletScreenCountBufferName));
            return ret;
        } catch (NumberFormatException e)
        {
            return null;
        }
    }

    public Integer serCount(Integer count)
    {
        jedisUtil.setStringValueWithExpiration(BulletScreenCountBufferName, 30, String.valueOf(count));
        return 0;
    }
}
