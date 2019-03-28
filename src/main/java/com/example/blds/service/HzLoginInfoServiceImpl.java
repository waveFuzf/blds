package com.example.blds.service;

import com.example.blds.dao.HzLoginInfoMapper;
import com.example.blds.entity.HzHospital;
import com.example.blds.entity.HzLoginInfo;
import com.example.blds.util.ChineseCharacterUtil;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
@Service
public class HzLoginInfoServiceImpl implements HzLoginInfoService {
    @Autowired
    private HzLoginInfoMapper hzLoginInfoMapper;
    @Override
    public HzLoginInfo getByUsername(String username) {
        Example e=new Example(HzLoginInfo.class);
        e.createCriteria().andEqualTo("loginName",username).andEqualTo("isDelete","0");
        List<HzLoginInfo> checkUser=hzLoginInfoMapper.selectByExample(e);
        if (checkUser.size()==0){
            return null;
        }
        return checkUser.get(0);
    }

    @Override
    public Integer save(HzLoginInfo user) {
        user.setCreateTime(new Date());
        user.setIsDelete(0);
        hzLoginInfoMapper.insert(user);
        return user.getUid();
    }

    @Override
    public Integer createAdmin(HzHospital hzHospital) {
        HzLoginInfo hzLoginInfo=new HzLoginInfo();
        hzLoginInfo.setCreateTime(new Date());
        hzLoginInfo.setIsDelete(0);
        String hos=ChineseCharacterUtil.convertHanzi2Pinyin(hzHospital.getName(),false);
        hzLoginInfo.setLoginName(hos+"_admin");
        hzLoginInfo.setPassword(new SimpleHash("md5", "blds"+hos, ByteSource.Util.bytes(""),
                2).toHex());
        hzLoginInfoMapper.insert(hzLoginInfo);
        return hzLoginInfo.getUid();
    }

}
