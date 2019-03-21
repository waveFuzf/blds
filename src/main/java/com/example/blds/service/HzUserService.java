package com.example.blds.service;

import com.example.blds.entity.HzHospital;
import com.example.blds.entity.HzUser;

import java.util.List;

public interface HzUserService {
    HzUser getUserInfoByUid(Integer uid);

    void changeStatusByUid(String state, Long uid);

    List<HzUser> getExpertsInfo(String name, Integer pageSize, Integer pageNo);

    List<HzUser> getExpertsInfoByName(String name, Integer pageSize, Integer pageNo);

    List<HzUser> getExpertsInfoByName(String name, Integer pageSize, Integer pageNo, Integer caseTypeId);

    Integer createAdmin(HzHospital hzHospital, Integer id);
}
