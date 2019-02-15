package com.example.blds.service;

import com.example.blds.entity.HzUser;

import java.util.List;

public interface HzUserService {
    HzUser getUserInfoByUid(Integer uid);

    void changeStatusByUid(String state, Long uid);

    List<HzUser> getExpertsInfo(String name);

    List<HzUser> getExpertsInfo(String name, Integer pageSize, Integer pageNo);
}