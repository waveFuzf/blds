package com.example.blds.service;

import com.example.blds.dao.HzSlideMapper;
import com.example.blds.entity.HzSlide;
import com.example.blds.entity.UploadSlides;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public void save(List<UploadSlides> uploadSlidesList, Integer consultId,Integer type) {
        HzSlide hzSlide=new HzSlide();
        hzSlide.setConsultId(consultId);
        hzSlide.setType(type);
        hzSlide.setIsDelete(0);
        for (UploadSlides u : uploadSlidesList){
            hzSlide.setUuid(u.getUuid());
            hzSlide.setClientSlidePath(u.getPath());
            hzSlideMapper.insert(hzSlide);
        }
    }

}
