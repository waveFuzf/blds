package com.example.blds.dao;

import com.example.blds.entity.HzConsultDoctor;

import com.example.blds.tkMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface HzConsultDoctorMapper extends tkMapper<HzConsultDoctor> {

    @Update({
            "Update hz_consult_doctor set collection=#{collection} where consult_id=#{consult_id} and doctor_type=#{doctor_type}" +
                    " and is_delete=0"
    })
    int updateIsCollection(@Param("consult_id") Integer consultId, @Param("doctor_type") Integer doctorType,
                           @Param("collection") Integer collection);
    @Select({
            "select * from hz_consult_doctor where consult_id=#{consult_id} and is_delete=0"
    })
    List<HzConsultDoctor> selectByConsultId(Integer consultId);
}