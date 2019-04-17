package com.example.blds.dao;

import com.example.blds.entity.HzUser;
import com.example.blds.tkMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface HzUserMapper extends tkMapper<HzUser> {
    @Select({
            "select u.* from hz_user u where u.is_super=1 and u.hospital_id =#{hid} "
    })
    HzUser getAdminByhid(@Param("hid")Integer hid);
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
            @Result(column = "user_id",property = "evaluates",many = @Many(select = "com.example.blds.dao.HzEvaluateMapper.getEvalatesByDoctorId")),
            @Result(column = "position",property = "prices",many = @Many(select ="com.example.blds.dao.HzPriceConfigMapper.selectByPosition"))
    })
    HzUser getExpertsInfoById(Integer doctorId);
    @Select({
            "select * from hz_user where name LIKE #{name}"
    })
    List<HzUser> getExpertsInfoByName(String name);

    @Select({
            "select hz_user.*,hz_price_config.price as price from hz_user LEFT join hz_price_config ON hz_user.position=hz_price_config.position_name " +
                    "where hz_price_config.price_type_id = #{caseTypeId} ORDER BY hz_user.user_score DESC,hz_user.total_consulation DESC"
    })
    List<HzUser> getExpertsInfoAndPrice(Integer caseTypeId);

    @Select({
            "select hz_user.*,hz_price_config.price as price from hz_user LEFT join hz_price_config ON hz_user.position=hz_price_config.position_name " +
                    "where hz_user.name LIKE #{name} and hz_price_config.price_type_id = #{caseTypeId} ORDER BY hz_user.user_score DESC,hz_user.total_consulation DESC"
    })
    List<HzUser> getExpertsInfoAndPriceByTag(@Param("name") String name, @Param("caseTypeId") Integer caseTypeId);
}
