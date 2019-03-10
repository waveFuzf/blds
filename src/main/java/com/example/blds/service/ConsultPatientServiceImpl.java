package com.example.blds.service;


import com.example.blds.dao.ConsultPatientMapper;
import com.example.blds.entity.ConsultPatient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultPatientServiceImpl implements ConsultPatientService {
    @Autowired
    private ConsultPatientMapper consultPatientMapper;
    @Override
    public Integer save(ConsultPatient consultPatient) {
        return consultPatientMapper.insert(consultPatient);
    }
}
