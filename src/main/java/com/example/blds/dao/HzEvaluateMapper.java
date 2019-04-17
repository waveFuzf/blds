package com.example.blds.dao;

import com.example.blds.entity.HzEvaluate;
import com.example.blds.tkMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface HzEvaluateMapper extends tkMapper<HzEvaluate> {

    List<HzEvaluate> getExpertEvaluateList(int evaluateType, Long evaluatorId, Long evaluateeId);

    @Select({
            "select * from hz_evaluate where evaluatee_id = #{doctorId} and evaluate_status=0 and evaluate_type=0 and is_delete=0"
    })
    @Results({
            @Result(column = "{evaluatorId=evaluator_id,consid=consult_id}",property = "additionalComments",many = @Many(select = "com.example.blds.dao.HzEvaluateMapper.getAdditionalComments"))
    })
    List<HzEvaluate> getEvalatesByDoctorId(Integer doctorId);

    @Select({
            "select * from hz_evaluate where consult_id = #{consid}  and evaluate_type=1 and evaluate_status=0 and evaluator_id=#{evaluatorId} and is_delete=0 and evaluate_status=0"
    })
    List<HzEvaluate> getAdditionalComments(@Param("consid") Integer consid,@Param("evaluatorId")Integer evaluatorId);

    @Select({
            "<script>",
            "select * from hz_evaluate ",
            "<where>",
            "<if test='type == 0'>",
            "evaluator_id = #{userId}",
            "</if>",
            "<if test='type == 1'>",
            "evaluatee_id = #{userId}",
            "</if>",
            "</where>" ,
            "and consult_id = #{consid} and evaluate_type=0 and is_delete=0 ",
            "</script>"
    })
    @Results({
            @Result(column = "{evaluatorId=evaluator_id,consid=consult_id}",property = "additionalComments",many = @Many(select = "com.example.blds.dao.HzEvaluateMapper.getAdditionalComments"))})
    HzEvaluate selEvaluateByConsultId(@Param("consid") Integer consid, @Param("userId") Integer userId,@Param("type") Integer type);
}