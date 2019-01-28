package com.example.blds.dao;

import com.example.blds.entity.HzEvaluate;
import com.example.blds.tkMapper;

import java.util.List;

public interface HzEvaluateMapper extends tkMapper<HzEvaluate> {

    List<HzEvaluate> getExpertEvaluateList(int evaluateType, Long evaluatorId, Long evaluateeId);
}