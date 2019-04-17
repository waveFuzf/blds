package com.example.blds.entity;

import lombok.Data;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
public class HzEvaluate {
    @Id
    private Integer id;

    private Integer consultId;

    private Integer evaluateType;

    private Long evaluatorId;

    private Long evaluateeId;

    private Integer evaluateStatus;

    private Integer evaluateWhole;

    private Integer evaluateProfession;

    private Integer evaluateIntime;

    private String evaluateText;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;

    private Date deleteTime;

    @Transient
    private String evaluate_id;

    @Transient
    private String doctorName;
    @Transient
    private List<HzEvaluate> additionalComments;
}