package com.example.blds.dao;

import com.example.blds.entity.HzSupplementReport;
import com.example.blds.tkMapper;
import org.apache.ibatis.annotations.Select;

public interface HzSupplementReportMapper extends tkMapper<HzSupplementReport> {

    @Select({
            "select * from hz_supplement_report where consult_id=#{supplementReport.consultId}"
    })
    HzSupplementReport selectByConsultid(HzSupplementReport supplementReport);
}