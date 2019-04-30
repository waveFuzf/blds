package com.example.blds.service;

import com.example.blds.entity.CountResult;
import com.example.blds.entity.HzConsult;
import com.example.blds.entity.HzConsultDoctor;
import com.example.blds.entity.QualityInfo;

import java.util.List;

public interface HzConsultService {
    boolean isConsultStatus(Integer consultId, Integer ...consultStatus);

    Integer updateCaseStatus(Integer consultId, int consultStatus);

    Integer cancelToggle(Integer consult_id, Integer isCancel);

    Integer updateConsult(Integer consult_id, int consultStatus,String reason_cancel);

    HzConsult selectById(Integer consid);

    List<HzConsult> getConsultList(HzConsultDoctor consultDoctor);

    Integer updateIsDeleteByConsultId(Integer consId);

    int updateByConsult(HzConsult consult);

    Integer save(HzConsult hzConsult);

    CountResult getCount(String user_id, Integer doctor_type);

    List<HzConsult> getConsultListByInfo(Integer userId, List<Integer> consultStatusList, Integer isCancel, Integer doctorType);

    List<HzConsult> selectByFormInfo(String hospitalId, List<Integer> consultStatusList, String startTime, String endTime, Integer pageSize, Integer pageNum, Integer radio);

    List<QualityInfo> selectQualityInfo(String beginTime, String endTime, String activeName, String radio);

    void updatePayStatus(String orderNo, String trade_no, boolean bcjc);
}
