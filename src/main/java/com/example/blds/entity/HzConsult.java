package com.example.blds.entity;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
public class HzConsult {
    @Id
    @GeneratedValue(generator = "JDBC")
    private Integer id;

    private Integer slideType;

    private Integer supplementSlideType;

    private Integer consultStatus;

    private String consultNo;

    private Integer caseTypeId;

    private Integer isCancel;

    private Integer payType;

    private String payUrl;

    private String payOrderNo;

    private Long payOrderCreateTime;

    private Integer drawBackStatus;

    private String caseTypeName;

    private String subspecialityName;

    private String parts;

    private Integer price;

    private Integer supplementPrice;

    private String reasonCancel;

    private String reason;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;

    private Date deleteTime;

    private String phone;

    private String casePresentation;

    private String clinicalDiagnosis;

    private String remake;

    private String oldDiagnosis;

    private String purpose;

    private List<HzSlide> hzSlides;

    private Integer isSettlement;

    @Transient
    private String consult_id;

    @Transient
    private HzConsultAddress hzConsultAddress;

    @Transient
    private List<HzConsultDoctor> doctors;

    @Transient
    private ConsultPatient consultPatient;

    @Transient
    private Integer doctorType;

}