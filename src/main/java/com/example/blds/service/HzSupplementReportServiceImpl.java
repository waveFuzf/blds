package com.example.blds.service;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.dao.HzConsultMapper;
import com.example.blds.dao.HzDiagnoseMapper;
import com.example.blds.dao.HzSlideMapper;
import com.example.blds.dao.HzSupplementReportMapper;
import com.example.blds.entity.*;
import com.example.blds.util.Enumeration;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class HzSupplementReportServiceImpl implements HzSupplementReportService{
    @Autowired
    private HzSupplementReportMapper SupplementReportMapper;
    @Autowired
    private HzPriceConfigService priceServices;
    @Autowired
    private HzDiagnoseMapper diagnoseMapper;
    @Autowired
    private HzConsultMapper hzConsultMapper;
    @Autowired
    private HzSlideMapper slideMapper;

    @Override
    public Result saveSupplementReport(String specialistId,Integer signtype, HzSupplementReport supplementReport, String slideList, Integer supplementSlideType, Integer priceTypeId, Integer doctorPositionId) {
        int status;
        Example example=new Example(HzSupplementReport.class);
        example.createCriteria().andEqualTo("consultId",supplementReport.getConsultId()).andEqualTo("isDelete","0");
        HzSupplementReport hsp = SupplementReportMapper.selectOneByExample(example);
        Integer bcjcprice = null;
//        Integer count = null;
//        //提交操作 需要计算补充检查价格
//        if (sign == 1 && signtype == 0) {
//            if (doctorPositionId != null){
//                HzPriceConfig priceConfig=priceServices.selectPriceByDoctorId(priceTypeId,doctorPositionId);
//                if(priceConfig == null){
//                    return ResultGenerator.genFailResult("未设置补充检查价格请联系管理员");
//                }else {
//                    bcjcprice= priceConfig.getPrice();
//                }
//                HzDiagnose diagnose = new HzDiagnose();
//                diagnose.setConsultId(supplementReport.getConsultId());
//                diagnose.setIsDelete(0);
//                diagnose = diagnoseMapper.selectOne(diagnose);
//                if (!StringUtils.equals("", diagnose.getImmuneTag())) {
//                    count = diagnose.getImmuneTag().split(",").length;
//                }else {
//                    return ResultGenerator.genFailResult("未设置ImmuneTag");
//                }
//            }
//        }
//        Date date=new Date();
//
//        if (hsp == null) {
//
//            if (sign == 1) {
//                // 修改此表补充检查提交时间
//                supplementReport.setCommitTime(date);
//                //修改病例表状态
//                if (signtype == 1) {
//                    status = Enumeration.CONSULT_STATUS.DIAGNOSED;
//                    supplementReport.setReportTime(date);
//                } else {
//                    status = Enumeration.CONSULT_STATUS.UNPAID;
//                }
//                updateConsultSatus(bcjcprice, supplementReport.getConsultId(), status, Enumeration.BcjcSlideEnum.SZQP);
//
//            }
//            //创建一条数据
//            if (SupplementReportMapper.insertSelective(supplementReport) == 0) {
//                return ResultGenerator.genFailResult("创建补充据失败");
//            }
//        } else {
//            //流程走到专家后 supplementReport不可能为空
//            if (sign == 1) {//提交
//                if (signtype == 0) {
//                    status = Enumeration.CONSULT_STATUS.UNPAID;
//                    supplementReport.setCommitTime(date);
//                } else {
//                    status = Enumeration.CONSULT_STATUS.DIAGNOSED;
//                    supplementReport.setReportTime(date);
//                }
//                //修改病例表状态  价格
//                updateConsultSatus(bcjcprice, supplementReport.getConsultId(), status, Enumeration.BcjcSlideEnum.SZQP);
//
//            }
//            //修改 数据
//            Example e=new Example(HzSupplementReport.class);
//            e.createCriteria().andEqualTo("consultId",supplementReport.getConsultId()).andEqualTo("isDelete",0);
//            SupplementReportMapper.updateByExample(supplementReport,e);

//        }

        //使用旧的service
//        if (slideList != null) {
//            if (!setlideUrl(signtype, slideList, supplementReport)) {
//                return ResultGenerator.genFailResult("切片插入失败");
//            }
//        }
        return ResultGenerator.genFailResult("操作成功");
    }

    @Override
    public HzSupplementReport selectSuppleReport(Integer consult_id) {
        Example example=new Example(HzSupplementReport.class);
        example.createCriteria().andEqualTo("consultId",consult_id).andEqualTo("isDelete",0);
        HzSupplementReport hzSupplementReport=SupplementReportMapper.selectOneByExample(example);
        List<HzSlide> slides=slideMapper.selectByConsultId(consult_id,1);
        if (slides.size()!=0){
            hzSupplementReport.setHzSlideList(slides);
        }

        return hzSupplementReport;
    }

//    private boolean setlideUrl(Integer signtype, String slideList, HzSupplementReport supplementReport) {
//        JSONArray array = JSONArray.fromObject(slideList);
//        if (signtype == 0) {
//            slideMapper.updateByConsultId(supplementReport.getConsultId(), Enumeration.CONSULT_TYPE.BCJC);
//        }
//
//        for (int i = 0; i < array.size(); i++) {
//            JSONObject slideJSON = array.optJSONObject(i);
//            HzSlide slide = new HzSlide();
//            slide.setConsultId(supplementReport.getConsultId());
//            int processStatus = slideJSON.optInt("processStatus", -1);
//            slide.setProcessStatus(processStatus == -1 ? 0 : processStatus);
//            slide.setType(0);
//            slide.setSlideName(slideJSON.optString("slideName"));
//            slide.setUuid(UUID.randomUUID().toString());
//            slide.setCreateTime(new Date());
//            slide.setClientSlidePath(slideJSON.optString("clientSlidePath"));
//            slide.setCosSlidePath(slideJSON.optString("cosSlidePath"));
//            slide.setSlideSize(slideJSON.optString("slideSize"));
//            slide.setFactoryUuid(slideJSON.optString("factoryUuid"));
//
//            int n = slideMapper.insertSelective(slide);
//            if (n == 0) {
//                return false;
//            }
//        }
//        return true;
//    }

    private boolean updateConsultSatus(Integer bcjcprice, Integer consultId, int status, int szqp) {
        HzConsult consult = new HzConsult();
        consult.setId(consultId);
        consult.setConsultStatus(status);
        consult.setSupplementPrice(bcjcprice);
        try{
            hzConsultMapper.updateByPrimaryKeySelective(consult);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
