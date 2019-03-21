package com.example.blds.service;

import com.example.blds.dao.HzHospitalMapper;
import com.example.blds.entity.HzHospital;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Service
public class HzHospitalServiceImpl implements HzHospitalService {
    @Autowired
    private HzHospitalMapper hzHospitalMapper;

    @Override
    public List<HzHospital> getHospitalList(String hospitalName) {
        List<HzHospital> hzHospitals=hzHospitalMapper.getHospitalList("%"+hospitalName.trim()+"%");
        return hzHospitals;
    }

    @Override
    public HzHospital getByHospitalId(Integer hospitalId) {
        Example example=new Example(HzHospital.class);
        example.createCriteria().andLike("isDelete","0").andEqualTo("hospitalId",hospitalId);
        return hzHospitalMapper.selectOneByExample(example);
    }

}
