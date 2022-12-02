package org.ics.service;

import jakarta.annotation.Resource;
import org.ics.dao.AuthDao;
import org.springframework.stereotype.Service;

@Service
public class AuthService extends BaseService
{
    /**
     * @Description
     * @Params
     * @Return
     **/
    public Integer addUser(String username, String password)
    {
        long now = System.currentTimeMillis();
        return authDao.addUser(username, password, now);
    }
}
