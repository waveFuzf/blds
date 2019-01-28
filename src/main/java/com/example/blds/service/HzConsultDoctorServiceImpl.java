package com.example.blds.service;

import com.example.blds.dao.HzConsultDoctorMapper;
import com.example.blds.entity.HzConsultDoctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class HzConsultDoctorServiceImpl implements HzConsultDoctorService {
    @Autowired
    private HzConsultDoctorMapper doctorMapper;
    @Override
    public int updateIsCollection(Integer consultId, Integer doctorType, Integer collection) {
        return doctorMapper.updateIsCollection(consultId,doctorType,collection);
    }

    @Override
    public List<HzConsultDoctor> getConsultDoctorByConsultId(Integer consultId) {
        Example example = new Example(HzConsultDoctor.class);
        example.createCriteria().andEqualTo("consultId",consultId).andEqualTo("isDelete",0);
        return doctorMapper.selectByExample(example);
    }
}
