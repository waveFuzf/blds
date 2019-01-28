package com.example.blds.service;

import com.example.blds.dao.HzFileMapper;
import com.example.blds.entity.HzFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class HzFileServiceImpl implements HzFileService {
    @Autowired
    private HzFileMapper fileMapper;
    @Override
    public List<HzFile> getExpressDeliveryByConsultId(Integer consult, Integer type) {
        Example example=new Example(HzFile.class);
        example.createCriteria().andEqualTo("consultId",consult).andEqualTo("type",type).andEqualTo("isDelete",0);
        return fileMapper.selectByExample(example);
    }
}
