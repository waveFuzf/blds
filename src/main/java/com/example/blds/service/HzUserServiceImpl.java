package com.example.blds.service;

import com.example.blds.dao.HzUserMapper;
import com.example.blds.entity.HzHospital;
import com.example.blds.entity.HzUser;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public Integer createAdmin(HzHospital hzHospital, Integer id) {
        HzUser hzUser=new HzUser();
        hzUser.setName(hzHospital.getName()+"管理员");
        hzUser.setUserId(Long.valueOf(id));
        hzUser.setUserState(0);
        hzUser.setIsSuper(1);
        hzUser.setHospitalId(Long.valueOf(hzHospital.getHospitalId()));
        hzUser.setHospitalName(hzHospital.getName());
        hzUser.setIsDelete(0);
        hzUserMapper.insert(hzUser);
        return null;
    }

    @Override
    public List<HzUser> getUsersByHospitalId(String hospitalId, Integer pageNo, String name) {
        List<HzUser> user=new ArrayList<>();
        if (Objects.equals(name,"")){
            user=hzUserMapper.getUsersByHospitalId(hospitalId,null);
        }else {
            user=hzUserMapper.getUsersByHospitalId(hospitalId,"%"+name+"%");
        }

        return user;
    }

    @Override
    public boolean deleteByUserId(Integer userId, String hospitalId) {

        return hzUserMapper.deleteByUserId(userId,hospitalId)==1;
    }
}
