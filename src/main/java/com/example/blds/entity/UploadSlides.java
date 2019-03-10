package com.example.blds.entity;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;
@Data
public class UploadSlides {
    @Id
    private Integer id;

    private String uuid;

    private String path;

    private Integer uploaderId;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;

    private Date deleteTime;

}