package com.example.blds.service;

import com.example.blds.entity.HzEvaluate;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface HzEvaluateService {
    HzEvaluate selEvaluateByConsultId(Integer consid, Integer userId, Integer type);

    Integer updateEvaluateById(HzEvaluate evaluate);

    Integer addEvaluate(HzEvaluate evaluate);

    PageInfo<HzEvaluate> getExpertEvaluateList(Integer evaluateType,Long evaluatorId, Long evaluateeId,
                                               Integer pageNo, Integer pageSize);

    List<HzEvaluate> selEvaluateById(Integer doctorId, Integer pageNo);

    HzEvaluate selEvaluateByConsultId(Integer consid,Integer userId);
}
