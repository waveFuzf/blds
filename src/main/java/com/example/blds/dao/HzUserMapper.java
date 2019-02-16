package com.example.blds.dao;

import com.example.blds.entity.HzUser;
import com.example.blds.tkMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface HzUserMapper extends tkMapper<HzUser> {
    @Select({
            "select hz_user.* from hz_user where user_id = #{uid}"
    })
    HzUser getUserInfoByUid(Integer uid);
    @Update({
            "update hz_user set user_state=#{state} where is_delete='0' and user_id=#{uid}"
    })
    void updateStatusByUid(@Param("state") String state,@Param("uid") Long uid);

    @Select({
            "select hz_user.* from hz_user ORDER BY hz_user.user_score DESC,hz_user.total_consulation DESC"
    })
    List<HzUser> getExpertsInfo();
    @Select({
            "select hz_user.* from hz_user where hz_user.description LIKE #{name} ORDER BY hz_user.user_score DESC,hz_user.total_consulation DESC"
    })
    List<HzUser> getExpertsInfoByTag(String name);

//    column = "{homework_id = homework_id,organization_id = organization_id}"
    @Select({
            "select * from hz_user where user_id = #{doctorId}"
    })
    @Results({
            @Result(column = "user_id",property = "evaluates",many = @Many(select = "com.example.blds.dao.HzEvaluateMapper.getEvalatesByDoctorId"))
    })
    HzUser getExpertsInfoById(Integer doctorId);
}
