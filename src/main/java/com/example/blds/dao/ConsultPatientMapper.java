package com.example.blds.dao;

import com.example.blds.entity.ConsultPatient;

import java.util.List;

import com.example.blds.tkMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface ConsultPatientMapper extends tkMapper<ConsultPatient> {
    @Select({
            "select * from consult_patient where consult_id=#{consid}"
    })
    ConsultPatient selectByConsultId(Integer consid);

}