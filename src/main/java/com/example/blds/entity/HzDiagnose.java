package com.example.blds.entity;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;
@Data
public class HzDiagnose {
    @Id
    private Integer id;

    private Integer consultId;

    private Long doctorId;

    private String mirrorView;

    private String diagnose;

    private String immuneTag;

    private String immuneRemark;

    private String immuno;

    private String pdf;

    private String remark;

    private Integer slideEstimate;

    private Integer diagnosisEstimate;

    private Date diagnoseTime;

    private Integer materialNum;

    private Integer isCandle;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;

    private Date deleteTime;

}