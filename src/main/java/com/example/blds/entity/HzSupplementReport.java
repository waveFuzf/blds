package com.example.blds.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.example.blds.CustomJsonDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Data
public class HzSupplementReport {
    @Id
    private Integer id;

    private Integer consultId;

    private String censorate;

    private Integer materialNum;

    private Integer isCandle;

    private String initialJudgement;

    private String remarkDoctor;

    private String ultimateJudgement;

    private String supplementaryOpinion;

    private Date commitTime;

    private Date reportTime;

    private String reportPath;

    private String sendReportPath;

    private Integer isDelete;

    private List<HzSlide> hzSlideList;

}