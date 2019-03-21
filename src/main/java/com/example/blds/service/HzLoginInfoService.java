package com.example.blds.service;

import com.example.blds.entity.HzHospital;
import com.example.blds.entity.HzLoginInfo;


public interface HzLoginInfoService {
    HzLoginInfo getByUsername(String username);

    boolean save(HzLoginInfo hzLoginInfo);

    Integer createAdmin(HzHospital hzHospital);
}
