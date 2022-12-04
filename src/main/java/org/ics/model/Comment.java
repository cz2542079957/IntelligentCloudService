package org.ics.model;

import lombok.Data;

@Data
public class Comment
{
    private Integer cmtid;

    private String username;

    private String text;

    private Integer module;
 
    private long createTime;

}
