package com.example.blds.dao;

import com.example.blds.entity.HzEvaluate;
import com.example.blds.tkMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface HzEvaluateMapper extends tkMapper<HzEvaluate> {

    List<HzEvaluate> getExpertEvaluateList(int evaluateType, Long evaluatorId, Long evaluateeId);

    @Select({
            "select * from hz_evaluate where evaluatee_id = #{doctorId}"
    })
    List<HzEvaluate> getEvalatesByDoctorId(Integer doctorId);
}