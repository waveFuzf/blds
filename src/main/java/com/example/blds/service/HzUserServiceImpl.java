package com.example.blds.service;

import com.example.blds.dao.HzUserMapper;
import com.example.blds.entity.HzUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class HzUserServiceImpl implements HzUserService {
    @Autowired
    private HzUserMapper hzUserMapper;
    @Override
    public HzUser getUserInfoByUid(Integer uid) {
        return hzUserMapper.getUserInfoByUid(uid);
    }

    @Override
    public void changeStatusByUid(String state, Long uid) {
        hzUserMapper.updateStatusByUid(state,uid);
    }

    @Override
    public List<HzUser> getExpertsInfo(String name, Integer pageSize, Integer pageNo) {
        if(Objects.equals(pageSize,null)){
            pageSize=4;
        }
        if(Objects.equals(pageNo,null)){
            pageNo=1;
        }
        Page<HzUser> pageInfo = PageHelper.startPage(pageNo, pageSize);
        List<HzUser> user=Objects.equals(name,null)?hzUserMapper.getExpertsInfo():hzUserMapper.getExpertsInfoByTag("%"+name+"%");
        return pageInfo;
    }

    @Override
    public List<HzUser> getExpertsInfoByName(String name, Integer pageSize, Integer pageNo) {
        if(Objects.equals(pageSize,null)){
            pageSize=4;
        }
        if(Objects.equals(pageNo,null)){
            pageNo=1;
        }
        Page<HzUser> pageInfo = PageHelper.startPage(pageNo, pageSize);
        List<HzUser> user=hzUserMapper.getExpertsInfoByName("%"+name+"%");
        return pageInfo;
    }

    @Override
    public List<HzUser> getExpertsInfoByName(String name, Integer pageSize, Integer pageNo, Integer caseTypeId) {
        if(Objects.equals(pageSize,null)){
            pageSize=4;
        }
        if(Objects.equals(pageNo,null)){
            pageNo=1;
        }
        Page<HzUser> pageInfo = PageHelper.startPage(pageNo, pageSize);
        List<HzUser> user=Objects.equals(name,null)?hzUserMapper.getExpertsInfoAndPrice(caseTypeId):hzUserMapper.getExpertsInfoAndPriceByTag("%"+name+"%",caseTypeId);
        return pageInfo;
    }
}
