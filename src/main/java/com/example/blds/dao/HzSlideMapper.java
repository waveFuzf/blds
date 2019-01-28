package com.example.blds.dao;

import com.example.blds.entity.HzSlide;
import com.example.blds.tkMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface HzSlideMapper extends tkMapper<HzSlide> {
    @Update({
            "Update hz_slide Set is_delete=1 , delete_time=now() where consult_id=#{consult_id} and is_delete=0"
    })
    void deleteByConsultId(Integer consult_id);

    @Update({
            "Update hz_slide set is_delete=1 where type=#{type} and consult_id=#{consultId} and is_delete=0"
    })
    int updateByConsultId(@Param("consultId") Integer consultid, @Param("type") Integer type);
    @Select({
            "select * from hz_slide where uuid=#{uuid} and is_delete=0"
    })
    HzSlide selectByUuid(String uuid);
}