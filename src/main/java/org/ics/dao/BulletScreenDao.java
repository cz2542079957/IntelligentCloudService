package org.ics.dao;

import org.apache.commons.compress.harmony.pack200.NewAttributeBands;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ics.model.BulletScreen;

import java.util.ArrayList;

@Mapper
public interface BulletScreenDao
{
    ArrayList<BulletScreen> getBulletScreenList(@Param("offset") Integer offset, @Param("limit") Integer limit);

    Integer getCount();

    Integer addBulletScreen(@Param("username") String username, @Param("text") String text, @Param("createTime") long createTime);
}
