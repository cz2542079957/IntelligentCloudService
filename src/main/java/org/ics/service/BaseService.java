package org.ics.service;

import jakarta.annotation.Resource;
import org.ics.dao.AuthDao;
import org.ics.dao.BulletScreenDao;
import org.springframework.stereotype.Service;

@Service
public class BaseService
{
    @Resource
    public AuthDao authDao;

    @Resource
    public BulletScreenDao bulletScreenDao;

}
