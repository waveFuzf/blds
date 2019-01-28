package com.example.blds.service;

import com.example.blds.entity.HzFile;

import java.util.List;

public interface HzFileService {
    List<HzFile> getExpressDeliveryByConsultId(Integer integer, Integer type);
}
