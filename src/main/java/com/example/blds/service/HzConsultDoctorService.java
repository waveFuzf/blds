package com.example.blds.service;


import com.example.blds.entity.HzConsultDoctor;

import java.util.List;

public interface HzConsultDoctorService {
    int updateIsCollection(Integer consultId, Integer doctorType, Integer collection);

    List<HzConsultDoctor> getConsultDoctorByConsultId(Integer consultId);
}
