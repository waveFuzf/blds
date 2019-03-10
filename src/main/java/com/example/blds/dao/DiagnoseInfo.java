package com.example.blds.dao;

import com.example.blds.entity.HzDiagnose;
import lombok.Data;

@Data
public class DiagnoseInfo {

    private String mirrorView;

    private String diagnose;

    private String immuneTag;

    private String immuneRemark;

    private String immuno;

    private String remark;

    private Integer slideEstimate;

    private Integer diagnosisEstimate;

    private Integer materialNum;

    private Integer isCandle;

    private String pdf;

}
