package com.example.blds.dao;

import com.example.blds.entity.HzAddress;
import com.example.blds.tkMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface HzAddressMapper extends tkMapper<HzAddress> {
    @Update({
            "Update hz_address Set type=0 where user_id=#{userId}"
    })
    void deleteByUserId(Long userId);

    @Select({
            "Select * from hz_address where consult_id=#{consultId} and is_delete=0"
    })
    HzAddress selectByConsultId(Integer consultId);
    @Select({
            "SELECT * FROM hz_address WHERE is_delete = 0 AND user_id = #{userId} ORDER BY is_default DESC ,update_time DESC"
    })
    List<HzAddress> selectByUserId(String userId);
}