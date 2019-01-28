package com.example.blds.service;

import com.example.blds.entity.HzAddress;

import java.util.List;

public interface HzAddressService {
    void editAddress(HzAddress hzAddress, boolean isDefault);

    int deleteByAddressId(Integer integer);

    List<HzAddress> selectByUserId(String userId);

    List<HzAddress> selectSendAddress();
}
