package com.example.blds.service;

import com.example.blds.entity.HzPriceConfig;

import java.util.List;

public interface HzPriceServices {
    List<HzPriceConfig> selectPriceListByType(Integer priceTypeId);

    List<HzPriceConfig> selectByPositionId(String positionName, Integer priceTypeId);

    Integer insertPriceConfig(HzPriceConfig priceConfig);

    Integer updatePriceById(HzPriceConfig priceS);

    Integer deletePriceConfigById(Integer pid);
}
