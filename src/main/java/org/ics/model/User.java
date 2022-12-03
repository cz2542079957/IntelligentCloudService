package org.ics.model;

import lombok.Data;

@Data
public class User
{
    private Integer id;

    private String username;

    private String password;

    private long createTime;

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public User(String username, String password, long createTime)
    {
        this.username = username;
        this.password = password;
        this.createTime = createTime;
    }
}
