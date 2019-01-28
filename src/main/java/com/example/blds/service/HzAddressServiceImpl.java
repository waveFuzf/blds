package com.example.blds.service;

import com.example.blds.dao.HzAddressMapper;
import com.example.blds.entity.HzAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class HzAddressServiceImpl implements HzAddressService {
    @Autowired
    private HzAddressMapper hzAddressMapper;
    @Override
    public void editAddress(HzAddress hzAddress, boolean isDefault) {
        if (isDefault){
            hzAddressMapper.deleteByUserId(hzAddress.getUserId());
        }
        hzAddress.setIsDefault(isDefault?1:0);
        if (hzAddress.getId()==null){
            hzAddressMapper.insert(hzAddress);
        }else {
            hzAddressMapper.updateByPrimaryKeySelective(hzAddress);
        }
    }

    @Override
    public int deleteByAddressId(Integer integer) {
        return 0;
    }

    @Override
    public List<HzAddress> selectByUserId(String userId) {
        return hzAddressMapper.selectByUserId(userId);
    }

    @Override
    public List<HzAddress> selectSendAddress() {
        Example example = new Example(HzAddress.class);
        example.createCriteria().andEqualTo("type",0).andEqualTo("userType",3).andEqualTo("isDelete",0);
        example.setOrderByClause("update_time DESC");
        return hzAddressMapper.selectByExample(example);
    }
}
