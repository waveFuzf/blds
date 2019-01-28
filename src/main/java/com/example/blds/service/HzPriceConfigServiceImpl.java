package com.example.blds.service;

import com.example.blds.dao.HzPriceConfigMapper;
import com.example.blds.entity.HzPriceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class HzPriceConfigServiceImpl implements HzPriceConfigService {
    @Autowired
    private HzPriceConfigMapper configMapper;
    @Override
    public HzPriceConfig selectPriceByDoctorId(Integer priceTypeId, Integer doctorPositionId) {
        Example example=new Example(HzPriceConfig.class);
        example.createCriteria().andEqualTo("priceTypeId",priceTypeId).andEqualTo("doctorPositionId",doctorPositionId);
        return configMapper.selectOneByExample(example);
    }
}
