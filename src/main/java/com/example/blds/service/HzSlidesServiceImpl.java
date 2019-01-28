package com.example.blds.service;

import com.example.blds.dao.HzSlideMapper;
import com.example.blds.entity.HzSlide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HzSlidesServiceImpl implements HzSlidesService {
    @Autowired
    private HzSlideMapper hzSlideMapper;
    @Override
    public void deleteSlidesByConsultId(Integer consult_id) {
        hzSlideMapper.deleteByConsultId(consult_id);
    }

    @Override
    public int insertBySlide(HzSlide slide) {
        return hzSlideMapper.insert(slide);
    }

    @Override
    public HzSlide getSlideByUuid(String uuid) {
        return hzSlideMapper.selectByUuid(uuid);
    }

}
