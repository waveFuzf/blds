package com.example.blds.dao;

import com.example.blds.entity.CountResult;
import com.example.blds.entity.HzConsult;
import com.example.blds.entity.HzConsultDoctor;
import com.example.blds.entity.QualityInfo;
import com.example.blds.tkMapper;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.Date;
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
                    "IFNULL(SUM(case WHEN c.consult_status in (4,9) THEN 1 ELSE 0 END),0) AS undone from hz_consult c " +
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

    @Select({
            "<script>",
            "select cd.hospital_name as name,",
            "<if test='activeName==\"1\"'>",
            "SUM(case when d.slide_estimate=1 then 1 else 0 end) as one, ",
            "SUM(case when d.slide_estimate=2 then 1 else 0 end) as two, ",
            "SUM(case when d.slide_estimate=3 then 1 else 0 end) as three, ",
            "SUM(case when d.slide_estimate=4 then 1 else 0 end) as four,",
            "COUNT(*) as count ",
            "</if>",
            "<if test='activeName==\"2\"'>",
            "SUM(case when d.diagnosis_estimate=1 then 1 else 0 end) as one, ",
            "SUM(case when d.diagnosis_estimate=2 then 1 else 0 end) as two, ",
            "SUM(case when d.diagnosis_estimate=3 then 1 else 0 end) as three, ",
            "SUM(case when d.diagnosis_estimate=4 then 1 else 0 end) as four, ",
            "COUNT(*) as count ",
            "</if>",
            "from hz_consult c LEFT JOIN hz_diagnose d ON d.consult_id=c.id ",
            " LEFT JOIN hz_consult_doctor cd ON cd.consult_id=c.id ",
            "<where>  c.consult_status=6 and cd.doctor_type=0 ",
            "<if test='startTime != null and endTime !=null and startTime!=\"\" and endTime!=\"\"'>",
            "and c.create_time between #{startTime} and #{endTime} GROUP BY cd.hospital_name ",
            "</if>",
            "</where>",
            "</script>"
    })
    List<QualityInfo> selectQualityInfoByHospital(@Param("startTime") String beginTime, @Param("endTime") String endTime, @Param("activeName") String activeName);

    @Select({
            "<script>",
            "select c.subspeciality_name as name,",
            "<if test='activeName==\"1\"'>",
            "SUM(case when d.slide_estimate=1 then 1 else 0 end) as one, ",
            "SUM(case when d.slide_estimate=2 then 1 else 0 end) as two, ",
            "SUM(case when d.slide_estimate=3 then 1 else 0 end) as three, ",
            "SUM(case when d.slide_estimate=4 then 1 else 0 end) as four， ",
            "COUNT(*) as count ",
            "</if>",
            "<if test='activeName==\"2\"'>",
            "SUM(case when d.diagnosis_estimate=1 then 1 else 0 end) as one, ",
            "SUM(case when d.diagnosis_estimate=2 then 1 else 0 end) as two, ",
            "SUM(case when d.diagnosis_estimate=3 then 1 else 0 end) as three, ",
            "SUM(case when d.diagnosis_estimate=4 then 1 else 0 end) as four, ",
            "COUNT(*) as count ",
            "</if>",
            "from hz_consult c LEFT JOIN hz_diagnose d ON d.consult_id=c.id ",
            "<where> and c.consult_status=6 ",
            "<if test='startTime != null and endTime !=null and startTime!=\"\" and endTime!=\"\"'>",
            "and c.create_time between #{startTime} and #{endTime} GROUP BY c.subspeciality_name",
            "</if>",
            "</where>",
            "</script>"
    })
    @Results({
           @Result(column = "name",property = "name",jdbcType = JdbcType.VARCHAR),
            @Result(column = "one",property = "one",jdbcType = JdbcType.INTEGER),
            @Result(column = "two",property = "two",jdbcType = JdbcType.INTEGER),
            @Result(column = "three",property = "three",jdbcType = JdbcType.INTEGER),
            @Result(column = "four",property = "four",jdbcType = JdbcType.INTEGER),
    })
    List<QualityInfo> selectQualityInfoByParts(@Param("startTime") String beginTime, @Param("endTime") String endTime, @Param("activeName") String activeName);
}