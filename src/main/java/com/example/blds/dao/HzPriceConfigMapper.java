package com.example.blds.dao;

import com.example.blds.entity.HzPriceConfig;
import com.example.blds.tkMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HzPriceConfigMapper extends tkMapper<HzPriceConfig> {
    @Select({
            "select * from hz_price_config where position_name=#{position}"
    })
    List<HzPriceConfig> selectByPosition(String position);

}