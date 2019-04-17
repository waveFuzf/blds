package com.example.blds.service;

import com.example.blds.dao.HzEvaluateMapper;
import com.example.blds.entity.HzEvaluate;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class HzEvaluateServiceImpl implements HzEvaluateService {
    @Autowired
    private HzEvaluateMapper evaluateMapper;
    @Override
    public HzEvaluate selEvaluateByConsultId(Integer consid, Integer userId,Integer type) {
        return evaluateMapper.selEvaluateByConsultId(consid,userId,type);
    }
    @Override
    public HzEvaluate selEvaluateByConsultId(Integer consid,Integer userId) {
        Example example=new Example(HzEvaluate.class);
        example.createCriteria().andEqualTo("consultId",consid).andEqualTo("isDelete",0).andEqualTo("evaluateType",0).andEqualTo("evaluatorId",userId);
        return evaluateMapper.selectOneByExample(example);
    }

    @Override
    public Integer updateEvaluateById(HzEvaluate evaluate) {
        return evaluateMapper.updateByPrimaryKeySelective(evaluate);
    }

    @Override
    public Integer addEvaluate(HzEvaluate evaluate) {
        return evaluateMapper.insert(evaluate);
    }

    @Override
    public PageInfo<HzEvaluate> getExpertEvaluateList(Integer evaluateType, Long evaluatorId, Long evaluateeId, Integer pageNo, Integer pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        List<HzEvaluate> list = evaluateMapper.getExpertEvaluateList(evaluateType, evaluatorId, evaluateeId);
        return new PageInfo<>(list);
    }

    @Override
    public List<HzEvaluate> selEvaluateById(Integer doctorId, Integer pageNo) {
        Page<HzEvaluate> pageInfo = PageHelper.startPage(pageNo, 5);
        List<HzEvaluate> list = evaluateMapper.getEvalatesByDoctorId(doctorId);
        return pageInfo;
    }


}
