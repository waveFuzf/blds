package com.example.blds.entity;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;
@Data
public class HzSlide {
    @Id
    private Integer id;

    private String uuid;

    private Integer consultId;

    private Integer type;

    private String clientSlidePath;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;

    private Date deleteTime;

}