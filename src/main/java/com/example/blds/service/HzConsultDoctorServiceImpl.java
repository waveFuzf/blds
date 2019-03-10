package com.example.blds.service;

import com.example.blds.dao.HzConsultDoctorMapper;
import com.example.blds.entity.HzConsult;
import com.example.blds.entity.HzConsultDoctor;
import com.example.blds.entity.HzDoctor;
import com.example.blds.entity.HzUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
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

    @Override
    public void save(Integer consultId, HzUser expertDoc, HzUser applyDoc) {
        HzConsultDoctor hcd=new HzConsultDoctor();
        hcd.setConsultId(consultId);
        hcd.setIsDelete(0);
        hcd.setCreateTime(new Date());
        if (expertDoc!=null){
            hcd.setDoctorType(1);
            hcd.autoAssignment(expertDoc);
            doctorMapper.insert(hcd);
        }
        if(applyDoc!=null){
            hcd.setDoctorType(0);
            hcd.autoAssignment(applyDoc);
            doctorMapper.insert(hcd);
        }
    }
}
