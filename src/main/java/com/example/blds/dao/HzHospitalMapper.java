package com.example.blds.dao;

import com.example.blds.entity.HzHospital;
import com.example.blds.tkMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface HzHospitalMapper extends tkMapper<HzHospital> {
    @Select({
            "<script>",
            "select h.*,h.hospital_id as hid from hz_hospital h ",
            "<where>",
            "<if test='name!=\"\"'>",
            "and h.name LIKE #{name}",
            "</if>",
            "</where>",
            "</script>"
    })
    @Results({
            @Result(column = "hid",property = "hospitalId"),
            @Result(column = "hospital_id",property = "admin",one = @One(select = "com.example.blds.dao.HzUserMapper.getAdminByhid"))
    })
    List<HzHospital> getHospitalList(@Param("name") String hospitalName);
}