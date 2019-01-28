package com.example.blds.service;

import com.example.blds.dao.HzDiagnoseMapper;
import com.example.blds.entity.HzDiagnose;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class HzDiagnoseServiceImpl implements HzDiagnoseService {
    @Autowired
    private HzDiagnoseMapper hzDiagnoseMapper;
    @Override
    public int updateByConsultIdSelective(HzDiagnose dia) {
        Example example=new Example(HzDiagnose.class);
        example.createCriteria().andEqualTo("consultId",dia.getConsultId()).andEqualTo("isDelete",0);
        dia.setId(hzDiagnoseMapper.selectOneByExample(example).getId());
        return hzDiagnoseMapper.updateByPrimaryKey(dia);
    }

    @Override
    public HzDiagnose getDiagnoseByConsultId(Integer consultId) {
        Example example=new Example(HzDiagnose.class);
        example.createCriteria().andEqualTo("consultId",consultId).andEqualTo("isDelete",0);
        return hzDiagnoseMapper.selectOneByExample(example);
    }

    @Override
    public HzDiagnose selectDiagnosByConsultId(Integer consultSelId) {
        HzDiagnose hzDiagnose=new HzDiagnose();
        hzDiagnose.setConsultId(consultSelId);
        hzDiagnose.setIsDelete(0);
        return hzDiagnoseMapper.selectOne(hzDiagnose);
    }
}
