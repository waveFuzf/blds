package com.example.blds.service;

import com.example.blds.dao.HzConsultMapper;
import com.example.blds.entity.CountResult;
import com.example.blds.entity.HzConsult;
import com.example.blds.entity.HzConsultDoctor;
import com.example.blds.util.Crypt;
import com.example.blds.util.Enumeration;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;

@Service
public class HzConsultServiceImpl implements HzConsultService {
    @Autowired
    private HzConsultMapper hzConsultMapper;
    @Override
    public boolean isConsultStatus(Integer consultId, Integer ...consultStatus) {
        Example example=new Example(HzConsult.class);
        example.createCriteria().andEqualTo("id",consultId).andEqualTo("isDelete",0);
        HzConsult hzConsult=hzConsultMapper.selectOneByExample(example);
        return !Arrays.stream(consultStatus).filter(x -> x.equals(hzConsult.getConsultStatus())).equals(0);
    }

    @Override
    public Integer updateCaseStatus(Integer consultId, int consultStatus) {
        return hzConsultMapper.updateCaseStatusById(consultId,consultStatus);
    }

    @Override
    public Integer cancelToggle(Integer consultId, Integer isCancel) {
        Example example=new Example(HzConsult.class);
        example.createCriteria().andEqualTo("id",consultId).andEqualTo("isDelete",0);
        HzConsult consult=new HzConsult();
        consult.setIsCancel(isCancel);
        return hzConsultMapper.updateByExampleSelective(consult,example);
    }

    @Override
    public Integer updateConsult(Integer consult_id, int consultStatus, String reason_cancel) {
        HzConsult hzConsult=new HzConsult();
        hzConsult.setId(consult_id);
        hzConsult.setConsultStatus(consultStatus);
        hzConsult.setReasonCancel(reason_cancel);
        return hzConsultMapper.updateByPrimaryKeySelective(hzConsult);
    }

    @Override
    public HzConsult selectById(Integer consid) {
        return hzConsultMapper.selectById(consid);
    }

    @Override
    public List<HzConsult> getConsultList(HzConsultDoctor consultDoctor) {
        List<HzConsult> consultList = hzConsultMapper.getConsultList(consultDoctor);
        Crypt.desEncryptConsultList(consultList, Enumeration.SECRET_KEY.CONSULT_ID_KEY);
        return consultList;
    }

    @Override
    public Integer updateIsDeleteByConsultId(Integer consId) {
        HzConsult hzConsult=new HzConsult();
        hzConsult.setId(consId);
        hzConsult.setIsDelete(0);
        return hzConsultMapper.updateByPrimaryKeySelective(hzConsult);
    }

    @Override
    public int updateByConsult(HzConsult consult) {
        return hzConsultMapper.updateByPrimaryKeySelective(consult);
    }

    @Override
    public Integer save(HzConsult hzConsult) {
        hzConsultMapper.insert(hzConsult);
        return hzConsult.getId();
    }

    @Override
    public CountResult getCount(String user_id, Integer doctor_type) {
        return hzConsultMapper.getCount(user_id,doctor_type);
    }

    @Override
    public List<HzConsult> getConsultListByInfo(Integer userId, List<Integer> consultStatusList, Integer isCancel, Integer doctorType) {
        return hzConsultMapper.getConsultListByInfo(userId,consultStatusList,isCancel,doctorType);
    }
}
