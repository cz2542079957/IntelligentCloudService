package org.ics.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ics.model.User;

@Mapper
public interface AuthDao
{
    Integer addUser(@Param("user") User user);

    Integer checkUser(@Param("user") User user);

    Integer checkUserExist(@Param("username") String username);

}
