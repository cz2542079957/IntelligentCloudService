package org.ics.service;

import jakarta.annotation.Resource;
import org.ics.bufferedDao.BufferedBulletScreenDao;
import org.ics.bufferedDao.BufferedCommentDao;
import org.ics.dao.AuthDao;
import org.ics.dao.BulletScreenDao;
import org.ics.dao.CommentDao;
import org.springframework.stereotype.Service;

@Service
public class BaseService
{
    @Resource
    public AuthDao authDao;
    @Resource
    public BulletScreenDao bulletScreenDao;
    @Resource
    public CommentDao commentDao;

    @Resource
    public BufferedBulletScreenDao bufferedBulletScreenDao;
    @Resource
    public BufferedCommentDao bufferedCommentDao;

}
