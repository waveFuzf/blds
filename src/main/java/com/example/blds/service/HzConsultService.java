package com.example.blds.service;

import com.example.blds.entity.HzConsult;
import com.example.blds.entity.HzConsultDoctor;

import java.util.List;

public interface HzConsultService {
    boolean isConsultStatus(Integer consultId, Integer ...consultStatus);

    Integer updateCaseStatus(Integer consultId, int consultStatus);

    Integer cancelToggle(Integer consult_id, Integer isCancel);

    Integer updateConsult(Integer consult_id, int consultStatus,String reason_cancel);

    HzConsult selectById(Integer consid);

    List<HzConsult> getConsultList(HzConsultDoctor consultDoctor);

    Integer updateIsDeleteByConsultId(Integer consId);

    int updateByConsult(HzConsult consult);
}
