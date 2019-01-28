package com.example.blds.service;

import com.example.blds.entity.HzSlide;

public interface HzSlidesService {
    void deleteSlidesByConsultId(Integer consult_id);

    int insertBySlide(HzSlide slide);

    HzSlide getSlideByUuid(String uuid);
}
