package com.example.blds.service;

import com.example.blds.dao.HzPriceConfigMapper;
import com.example.blds.entity.HzPriceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class HzPriceServiceImpl implements HzPriceServices {
    @Autowired
    private HzPriceConfigMapper priceConfigMapper;
    @Override
    public List<HzPriceConfig> selectPriceListByType(Integer priceTypeId) {
        Example example=new Example(HzPriceConfig.class);
        example.createCriteria().andEqualTo("priceTypeId",priceTypeId).andEqualTo("isDelete",0);
        return priceConfigMapper.selectByExample(example);
    }

    @Override
    public List<HzPriceConfig> selectByPositionId(String positionId, Integer priceTypeId) {
        Example example=new Example(HzPriceConfig.class);
        example.createCriteria().andEqualTo("priceTypeId",priceTypeId).andEqualTo("positionName",positionId).andEqualTo("isDelete",0);
        return priceConfigMapper.selectByExample(example);
    }

    @Override
    public Integer insertPriceConfig(HzPriceConfig priceConfig) {
        return priceConfigMapper.insert(priceConfig);
    }

    @Override
    public Integer updatePriceById(HzPriceConfig priceS) {
        return priceConfigMapper.updateByPrimaryKeySelective(priceS);
    }

    @Override
    public Integer deletePriceConfigById(Integer pid) {
        HzPriceConfig hzPriceConfig = new HzPriceConfig();
        hzPriceConfig.setId(pid);
        hzPriceConfig.setIsDelete(1);
        return priceConfigMapper.updateByPrimaryKeySelective(hzPriceConfig);
    }
}
