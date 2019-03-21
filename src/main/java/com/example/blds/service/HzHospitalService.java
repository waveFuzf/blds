package com.example.blds.service;

import com.example.blds.entity.HzHospital;

import java.util.List;
public interface HzHospitalService {
    List<HzHospital> getHospitalList(String hospitalName);

    HzHospital getByHospitalId(Integer hospitalId);
}
