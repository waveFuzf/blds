package com.example.blds.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
@Data
public class HzPriceConfig {
    @Id
    private Integer id;

    private Integer unionId;

    private Integer priority;

    private Integer priceTypeId;

    private String priceTypeName;

    private Integer positionId;

    private String positionName;

    private Integer price;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;

    private Date deleteTime;

    @Transient
    private String price_id;
}