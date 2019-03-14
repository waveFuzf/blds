package com.example.blds.dao;

import com.example.blds.entity.CountResult;
import com.example.blds.entity.HzConsult;
import com.example.blds.entity.HzConsultDoctor;
import com.example.blds.tkMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface HzConsultMapper extends tkMapper<HzConsult> {

    @Update({
            "Update hz_consult SET consult_status=#{consultStatus} where id=#{consultId} and is_delete=0 "
    })
    int updateCaseStatusById(@Param("consultId") Integer consultId, @Param("consultStatus") int consultStatus);
    @Select({
            "Select *,0 tag from hz_consult where id=#{consid} and is_delete=0"
    })
    @Results({
//            @Result(column = "id",property = "hzConsultAddress",one = @One(select = "com.example.blds.dao.HzAddressMapper.selectByConsultId")),
            @Result(column = "id",property = "doctors",many = @Many(select = "com.example.blds.dao.HzConsultDoctorMapper.selectByConsultId")),
            @Result(column = "id",property = "consultPatient",one = @One(select = "com.example.blds.dao.ConsultPatientMapper.selectByConsultId")),
            @Result(column = "{type=tag,consultId=id}",property = "hzSlides",many = @Many(select = "com.example.blds.dao.HzSlideMapper.selectByConsultId"))
    })
    HzConsult selectById(Integer consid);

    @Select({
            "Select c.*,c.id node,cd.doctor_type from hz_consult c LEFT join hz_consult_doctor cd ON c.id=cd.consult_id where cd.collection=#{consultDoctor.collection}" +
                    " and cd.doctor_id=#{consultDoctor.doctorId}"
    })
    @Results({
            @Result(column = "node",property = "id",jdbcType = JdbcType.INTEGER),
            @Result(column = "id",property = "consultPatient",one=@One(select = "com.example.blds.dao.ConsultPatientMapper.selectByConsultId")),
            @Result(column = "id",property = "doctors" ,many = @Many(select = "com.example.blds.dao.HzConsultDoctorMapper.selectByConsultId"))
    })

    List<HzConsult> getConsultList(@Param("consultDoctor") HzConsultDoctor consultDoctor);
    @Select({
            "select IFNULL(SUM(case WHEN c.consult_status=6 THEN 1 ELSE 0 END),0) AS done, " +
                    "IFNULL(SUM(case WHEN c.consult_status!=6 THEN 1 ELSE 0 END),0) AS undone from hz_consult c " +
                    "LEFT JOIN hz_consult_doctor cd ON c.id=cd.consult_id WHERE cd.doctor_type=#{type} " +
                    "AND cd.doctor_id=#{userId} "
    })
    CountResult getCount(@Param("userId") String user_id, @Param("type") Integer doctor_type);

    @Select({"<script>",
            "select c.*,c.id as node from hz_consult c LEFT JOIN hz_consult_doctor cd ON c.id=cd.consult_id  ",
            "<where>",
            "<if test='userId != null'>",
            "cd.doctor_id = #{userId}",
            "</if>",
            "<if test='consultStatus != null'>",
            "and c.consult_status in ",
            "<foreach collection='consultStatus' item='id' index='index' open='(' close=')' separator=','>",
                "#{id}",
                "</foreach>",
            "</if>",
            "<if test='isCancel != null'>",
            "and c.isCancel=#{isCancel}",
            "</if>",
            "<if test='doctorType != null'>",
            "and cd.doctor_type=#{doctorType}",
            "</if>",
            "</where>",
            "</script>"
    })
    @Results({
            @Result(column = "node",property = "id",jdbcType = JdbcType.INTEGER),
            @Result(column = "id",property = "consultPatient",one=@One(select = "com.example.blds.dao.ConsultPatientMapper.selectByConsultId")),
            @Result(column = "id",property = "doctors" ,many = @Many(select = "com.example.blds.dao.HzConsultDoctorMapper.selectByConsultId"))
    })
    List<HzConsult> getConsultListByInfo(@Param("userId") Integer userId, @Param("consultStatus") List<Integer> consultStatusList, @Param("isCancel") Integer isCancel, @Param("doctorType") Integer doctorType);



    @Select({"<script>",
            "select c.*,c.id node from hz_consult c LEFT JOIN hz_consult_doctor cd ON c.id=cd.consult_id ",
            "<where>",
            "<if test='consultStatusList != null'>",
            "and c.consult_status in ",
            "<foreach collection='consultStatusList' item='id' index='index' open='(' close=')' separator=','>",
            "#{id}",
            "</foreach>",
            "</if>",
            "<if test='hospitalId != null and hospitalId!=\"\"' >",
            "and cd.hospital_id=#{hospitalId}",
            "</if>",
            "<if test='startTime != null and endTime !=null and startTime!=\"\" and endTime!=\"\"'>",
            "and c.create_time between #{startTime} and #{endTime}",
            "</if>",
            "and cd.doctor_type=0",
            "</where>",
            "</script>"
    })
    @Results({
            @Result(column = "id",property = "doctors" ,many = @Many(select = "com.example.blds.dao.HzConsultDoctorMapper.selectByConsultId")),
            @Result(column = "node",property = "id",jdbcType = JdbcType.INTEGER),
            @Result(column = "id",property = "consultPatient",one=@One(select = "com.example.blds.dao.ConsultPatientMapper.selectByConsultId")),
    })
    List<HzConsult> selectByFormInfo(@Param("hospitalId") String hospitalId, @Param("consultStatusList") List<Integer> consultStatusList, @Param("startTime") String startTime, @Param("endTime") String endTime);
}