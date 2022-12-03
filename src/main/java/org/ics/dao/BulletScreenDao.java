package org.ics.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ics.model.BulletScreen;

import java.util.ArrayList;

@Mapper
public interface BulletScreenDao
{
    ArrayList<BulletScreen> getBulletScreenList(@Param("offset") Integer offset, @Param("limit") Integer limit);

    Integer getCount();
}
