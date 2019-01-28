package com.example.blds.service;

import com.example.blds.entity.HzEvaluate;
import com.github.pagehelper.PageInfo;

public interface HzEvaluateService {
    HzEvaluate selEvaluateByConsultId(Integer consid);

    Integer updateEvaluateById(HzEvaluate evaluate);

    Integer addEvaluate(HzEvaluate evaluate);

    PageInfo<HzEvaluate> getExpertEvaluateList(Integer evaluateType,Long evaluatorId, Long evaluateeId,
                                               Integer pageNo, Integer pageSize);
}
