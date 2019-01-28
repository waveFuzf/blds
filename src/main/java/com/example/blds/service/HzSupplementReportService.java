package com.example.blds.service;

import com.example.blds.Re.Result;
import com.example.blds.entity.HzSupplementReport;

public interface HzSupplementReportService {
    Result saveSupplementReport(String specialistId, Integer sign, Integer signtype, HzSupplementReport supplementReport, String slideList, Integer supplementSlideType, Integer consultType, Integer priceTypeId);

    HzSupplementReport selectSuppleReport(String consult_id);
}
