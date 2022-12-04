package org.ics.service;

import org.ics.model.BulletScreen;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BulletScreenService extends BaseService
{
    /**
     * @Description buffered 根据页号和单页数量获取弹幕列表
     * @Params [page, limit]
     * @Return 弹幕列表
     **/
    public ArrayList<BulletScreen> getBulletScreenList(Integer page, Integer limit)
    {
        if (null == page) page = 1;
        if (null == limit) limit = 30;
        Integer offset = (page - 1) * limit;

        ArrayList<BulletScreen> ret = null;
        //查看redis
        ret = bufferedBulletScreenDao.getBulletScreenList(offset, limit);
        if (null != ret)
        {
            return ret;
        }
        //查数据库
        ret = bulletScreenDao.getBulletScreenList(offset, limit);
        if (null != ret)
        {
            //存redis
            bufferedBulletScreenDao.setBulletScreenList(offset, limit, ret);
        }

        return ret;
    }

    /**
     * @Description buffered 获取弹幕总数
     * @Params []
     * @Return 数量
     **/
    public Integer getCount()
    {
        //从缓存获取
        Integer ret = bufferedBulletScreenDao.getCount();
        if (null != ret)
        {
            return ret;
        }
        //从数据库获取
        ret = bulletScreenDao.getCount();
        if (null != ret)
        {
            //存入缓存
            bufferedBulletScreenDao.serCount(ret);
        }
        return ret;
    }
}
