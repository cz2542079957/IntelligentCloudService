package org.ics.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ics.model.Comment;

import java.util.ArrayList;

@Mapper
public interface CommentDao
{
    ArrayList<Comment> getCommentList(@Param("module") Integer module, @Param("offset") Integer offset, @Param("limit") Integer limit);

    Integer getCommentCount(@Param("module") Integer module);
}
