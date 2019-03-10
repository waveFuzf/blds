package com.example.blds.dao;

import com.example.blds.entity.HzUser;
import com.example.blds.entity.UploadSlides;
import lombok.Data;

import java.util.List;

@Data
public class ConsultInfo {
    private Integer age;
    private String casePresentation;
    private Integer caseTypeId;
    private String caseTypeName;
    private String clinicalDiagnosis;
    private Integer consultStatus;
    private HzUser expertDoc;
    private HzUser applyDoc;
    private String name;
    private String oldDiagnosis;
    private String otherpurpose;
    private Integer paramId;
    private String parts;
    private String patientRemark;
    private String phone;
    private List<String> purpose;
    private String sex;
    private List<UploadSlides> uploadSlidesList;
    private Integer slideType;
    private String subspecialityId;
    private String subspecialityName;
}
