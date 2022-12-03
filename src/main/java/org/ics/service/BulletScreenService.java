package org.ics.service;

import org.ics.model.BulletScreen;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BulletScreenService extends BaseService
{
    public ArrayList<BulletScreen> getBulletScreenList(Integer page, Integer limit)
    {
        if (null == page) page = 1;
        if (null == limit) limit = 30;
        Integer offset = (page - 1) * limit;
        return bulletScreenDao.getBulletScreenList(offset, limit);
    }

    public Integer getCount()
    {
        return bulletScreenDao.getCount();
    }
}
