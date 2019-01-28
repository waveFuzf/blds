package com.example.blds.service;

import com.example.blds.dao.HzConsultAddressMapper;
import com.example.blds.entity.HzConsultAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class HzConsultAddressServiceImpl implements HzConsultAddressService {
    @Autowired
    private HzConsultAddressMapper consultAddressMapper;
    @Override
    public HzConsultAddress setAndGetAddress(Integer sign, HzConsultAddress consultAddress) {
        HzConsultAddress coAddress = consultAddressMapper.selectOne(consultAddress);
        if (sign == 1) {
            return coAddress;

        } else {
            consultAddressMapper.updateByPrimaryKeySelective(consultAddress);
            return null;
        }
    }

    @Override
    public HzConsultAddress selectByConsultId(Integer integer) {
        Example example=new Example(HzConsultAddress.class);
        example.createCriteria().andEqualTo("consultId",integer).andEqualTo("isDelete",0);
        return consultAddressMapper.selectOneByExample(example);
    }
}
