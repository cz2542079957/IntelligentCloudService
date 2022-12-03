package org.ics.model;

import lombok.Data;

@Data
public class BulletScreen
{
    private Integer bsid;

    private String username;

    private String text;

    private long createTime;
}
