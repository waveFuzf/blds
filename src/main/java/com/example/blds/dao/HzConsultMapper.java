package com.example.blds.dao;

import com.example.blds.entity.HzConsult;
import com.example.blds.entity.HzConsultDoctor;
import com.example.blds.tkMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface HzConsultMapper extends tkMapper<HzConsult> {

    @Update({
            "Update hz_consult SET consult_status=#{consultStatus} where id=#{consultId} and is_delete=0 "
    })
    int updateCaseStatusById(@Param("consultId") Integer consultId, @Param("consultStatus") int consultStatus);
    @Select({
            "Select * from hz_consult where id=#{consid} and is_delete=0"
    })
    @Results({
            @Result(column = "id",property = "hzConsultAddress",one = @One(select = "com.example.blds.dao.HzAddressMapper.selectByConsultId")),
            @Result(column = "id",property = "doctors",many = @Many(select = "com.example.blds.dao.HzConsultDoctorMapper.selectByConsultId"))
    })
    HzConsult selectById(Integer consid);

    @Select({
            "Select c.* from hz_consult c LEFT join hz_consult_doctor cd ON c.id=cd.consult_id where cd.collection=#{consultDoctor.collection}" +
                    " and cd.doctor_id=#{consultDoctor.doctor_id} and cd.doctor_type=#{consultDoctor.doctor_type}"
    })
    List<HzConsult> getConsultList(@Param("consultDoctor") HzConsultDoctor consultDoctor);
}