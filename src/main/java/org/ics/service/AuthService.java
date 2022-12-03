package org.ics.service;

import jakarta.annotation.Resource;
import org.ics.dao.AuthDao;
import org.ics.model.User;
import org.springframework.stereotype.Service;

import javax.naming.Name;

@Service
public class AuthService extends BaseService
{
    /**
     * @Description 添加用户
     * @Params [username, password]
     * @Return
     **/
    public Integer addUser(User user)
    {
        long now = System.currentTimeMillis();
        user.setCreateTime(now);
        return authDao.addUser(user);
    }

    /**
     * @Description
     * @Params [user]
     * @Return
     **/
    public Integer checkUser(User user)
    {
        return authDao.checkUser(user);
    }


    public Integer checkUserExist(String username)
    {
        return authDao.checkUserExist(username);
    }

}
