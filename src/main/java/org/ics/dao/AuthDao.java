package org.ics.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface AuthDao
{
    Integer addUser(@Param("username") String username, @Param("password") String password, @Param("createTime") long createTime);
}
