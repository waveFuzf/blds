package com.example.blds.service;

import com.example.blds.entity.HzDiagnose;

public interface HzDiagnoseService {
    int updateByConsultIdSelective(HzDiagnose dia);

    HzDiagnose getDiagnoseByConsultId(Integer consultId);

    HzDiagnose selectDiagnosByConsultId(Integer consultSelId);
}
