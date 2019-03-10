package com.example.blds.entity;

import lombok.Data;

import java.util.List;

@Data
public class SupplementInfo {
    private String consult_id;
    private String isCandel;
    private String materialNum;
    private String censorate;
    private String initialJudgement;
    private String remark;
    private List<UploadSlides> uploadSlidesList;
    private String ultimateJudgement;
    private String supplymentaryOpinion;


}
