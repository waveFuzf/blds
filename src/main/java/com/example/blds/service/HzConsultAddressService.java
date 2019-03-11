package com.example.blds.service;

import com.example.blds.entity.HzConsultAddress;

public interface HzConsultAddressService {
    HzConsultAddress setAndGetAddress(Integer sign, HzConsultAddress consultAddress);

    HzConsultAddress selectByConsultId(Integer consid, Integer integer);

    Integer editMailInfo(Integer integer, String mailCode, String mailCompany);
}
