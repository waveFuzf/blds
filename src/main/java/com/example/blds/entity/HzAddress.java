package com.example.blds.entity;
import lombok.Data;

import javax.persistence.Id;
import java.util.Date;
@Data
public class HzAddress {
    @Id
    private Integer id;

    private Integer type;

    private Long userId;

    private Integer isDefault;

    private String province;

    private String city;

    private String address;

    private String name;

    private String phone;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;

    private Date deleteTime;

}