package com.example.blds.service;

import com.example.blds.Re.Result;
import com.example.blds.entity.HzSupplementReport;
import io.swagger.models.auth.In;

public interface HzSupplementReportService {
    Result saveSupplementReport(String specialistId, Integer signtype, HzSupplementReport supplementReport, String slideList, Integer supplementSlideType, Integer consultType, Integer priceTypeId);

    HzSupplementReport selectSuppleReport(Integer consult_id);
}
