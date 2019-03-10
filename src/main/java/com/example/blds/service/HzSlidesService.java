package com.example.blds.service;

import com.example.blds.entity.HzSlide;
import com.example.blds.entity.UploadSlides;

import java.util.List;

public interface HzSlidesService {
    void deleteSlidesByConsultId(Integer consult_id);

    int insertBySlide(HzSlide slide);

    HzSlide getSlideByUuid(String uuid);

    void save(List<UploadSlides> uploadSlidesList, Integer consultId,Integer type);
}
