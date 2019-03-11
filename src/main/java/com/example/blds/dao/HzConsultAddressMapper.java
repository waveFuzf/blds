package com.example.blds.dao;

import com.example.blds.entity.HzConsultAddress;
import com.example.blds.tkMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface HzConsultAddressMapper extends tkMapper<HzConsultAddress> {
    @Update({
            "Update hz_consult_address set mail_company=#{mailCompany},mail_number=#{mailCode} where id=#{addressId} "
    })
    Integer updateMailInfo(@Param("addressId") Integer integer, @Param("mailCode") String mailCode,@Param("mailCompany") String mailCompany);
}