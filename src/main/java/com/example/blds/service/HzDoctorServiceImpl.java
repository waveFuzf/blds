package com.example.blds.service;

import com.example.blds.dao.HzDoctorMapper;
import com.example.blds.entity.HzDoctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HzDoctorServiceImpl implements HzDoctorService {
    @Autowired
    private HzDoctorMapper hzDoctorMapper;
    @Override
    public List<HzDoctor> getExpertDoctorList() {
        return hzDoctorMapper.selectAll();
    }
}
