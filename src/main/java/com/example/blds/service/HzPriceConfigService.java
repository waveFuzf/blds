package com.example.blds.service;

import com.example.blds.entity.HzPriceConfig;

public interface HzPriceConfigService {
    HzPriceConfig selectPriceByDoctorId(Integer priceTypeId, Integer doctorPositionId);
}
