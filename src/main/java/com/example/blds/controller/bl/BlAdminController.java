package com.example.blds.controller.bl;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.aop.UserTokenAop;
import com.example.blds.dao.HzSlideMapper;
import com.example.blds.dao.HzSupplementReportMapper;
import com.example.blds.entity.HzDiagnose;
import com.example.blds.entity.HzSlide;
import com.example.blds.entity.HzSupplementReport;
import com.example.blds.entity.UploadSlides;
import com.example.blds.service.HzConsultService;
import com.example.blds.service.HzDiagnoseService;
import com.example.blds.service.HzSlidesService;
import com.example.blds.util.Crypt;
import com.example.blds.util.Enumeration;
import com.example.blds.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 病理申请医生（下级医生）或者运营中心操作病历控制层
 */
@Api(tags = "【管理员】【操作病例】")
@RequestMapping("/bladmin")
@Controller
public class BlAdminController {

    @Autowired
    private HzConsultService consultService;

    @Autowired
    private HzDiagnoseService diagnoseService;

    @Autowired
    private HzSlidesService slidesService;
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private HzSupplementReportMapper supplementReportMapper;

    @Autowired
    private HzSlideMapper slideMapper;


    @UserTokenAop
    @ApiOperation(value = "回退")
    @ResponseBody
    @PostMapping("backToUnDiagnose.htm")
    public Result backToUnDiagnose(
            @ApiParam(name="consult_id", value="加密病例id")@RequestParam(value="consult_id") String consultId
    ){
        int i;
        if(consultService.isConsultStatus(Crypt.desDecryptByInteger(consultId, Enumeration.SECRET_KEY.CONSULT_ID_KEY),Enumeration.CONSULT_STATUS.WAIT_VERIFY,Enumeration.CONSULT_STATUS.RETURNED)){
            i = consultService.updateCaseStatus(Crypt.desDecryptByInteger(consultId, Enumeration.SECRET_KEY.CONSULT_ID_KEY),Enumeration.CONSULT_STATUS.UNDIAGNOSED);
        } else {
            return ResultGenerator.genFailResult("该状态不能改变");
        }
        return ResultGenerator.genSuccessResult(i==1?"该状态已改变":"该状态改变失败");
    }


    @UserTokenAop
    @ApiOperation(value = "修改会诊意见")
    @ResponseBody
    @PostMapping("updateDiagnosisByCaseId.htm")
    public Result updateDiagnosis(
            @ApiParam(name="consult_id", value="加密病例id")@RequestParam(value="consult_id") String consult_id,
            @ApiParam(name="mirrorView", value="镜下所见")@RequestParam(value="mirrorView", required=false) String mirrorView,
            @ApiParam(name="diagnose", value="专家意见")@RequestParam(value="diagnose", required=false) String diagnose,
            @ApiParam(name="remark", value="备注")@RequestParam(value="remark", required=false, defaultValue="") String remark,
            @ApiParam(name="slideEstimate", value="切片质量1.质量不合格2.质量基本合格3.质量合格4.质量优秀5.质量不合格(制片问题)6.质量不合格(设备问题)")@RequestParam(value="slideEstimate", required=false) Integer slideEstimate,
            @ApiParam(name="diagnosisEstimate", value="初诊质量1.没有诊断2.诊断不正确3.诊断基本正确4.诊断完全正确5.诊断不明确")@RequestParam(value="diagnosisEstimate", required=false) Integer diagnosisEstimate
    ){
        HzDiagnose dia = new HzDiagnose();
        dia.setConsultId(Crypt.desDecryptByInteger(consult_id,Enumeration.SECRET_KEY.CONSULT_ID_KEY));
        dia.setMirrorView(mirrorView);
        dia.setDiagnose(diagnose);
        dia.setRemark(remark);
        dia.setSlideEstimate(slideEstimate);
        dia.setDiagnosisEstimate(diagnosisEstimate);
        return ResultGenerator.genSuccessResult(diagnoseService.updateByConsultIdSelective(dia));
    }

    @UserTokenAop
    @ApiOperation(value = "取消、恢复会诊")
    @ResponseBody
    @PostMapping("cancelToggle.htm")
    public Result cancelToggle(
            @ApiParam(name="consult_id", value="加密病例id")@RequestParam(value="consult_id") String consult_id,
            @ApiParam(name="isCancel", value="是否取消会诊，1取消，0恢复")@RequestParam(value="isCancel") Integer isCancel
    ){
        Integer i = consultService.cancelToggle(Crypt.desDecryptByInteger(consult_id,Enumeration.SECRET_KEY.CONSULT_ID_KEY),isCancel);
        return ResultGenerator.genSuccessResult(i);
    }




    @ApiOperation(value = "管理员上传切片")
    @ResponseBody
    @PostMapping("confirmRecevie.htm")
    public Result confirmRecevie(
            @ApiParam(name="consult_id", value="加密病例id")@RequestParam(value="consult_id") String consult_id,
            @ApiParam(name="type", value="类型")@RequestParam(value="type") Integer type,
            @ApiParam(name="uploadList", value="上传切片")@RequestParam(value="uploadList") String uploadList,
            HttpServletRequest request

    ) throws Exception {
        String res=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (res.equals("token无效")){
            return ResultGenerator.genFailResult(res);
        }
        if (!JSONObject.fromObject(res).optString("isSuper").equals("1")){
            return ResultGenerator.genFailResult("你木的权限");
        }
        Integer consid=Integer.valueOf(Crypt.desDecrypt(consult_id,Enumeration.SECRET_KEY.CONSULT_ID_KEY));
        slidesService.save(JSONArray.toList(JSONArray.fromObject(uploadList),UploadSlides.class),consid , type);
        consultService.updateCaseStatus(consid,type==0?4:9);
        HzDiagnose hzDiagnose=diagnoseService.getDiagnoseByConsultId(consid);
        HzSupplementReport hzSupplementReport=new HzSupplementReport();
        hzSupplementReport.setConsultId(consid);
        hzSupplementReport.setCommitTime(new Date());
        hzSupplementReport.setIsCandle(hzDiagnose.getIsCandle());
        hzSupplementReport.setMaterialNum(hzDiagnose.getMaterialNum());
        hzSupplementReport.setIsDelete(0);
        supplementReportMapper.insert(hzSupplementReport);
        return ResultGenerator.genSuccessResult("good job!");
    }

    @ApiOperation(value = "管理员查找切片")
    @ResponseBody
    @PostMapping("getSlidesByConsultId.htm")
    public Result getSlidesByConsultId(
            @ApiParam(name="consult_id", value="加密病例id")@RequestParam(value="consult_id") String consult_id,
            HttpServletRequest request

    ) throws Exception {
        String res=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (res.equals("token无效")){
            return ResultGenerator.genFailResult(res);
        }
        if (!JSONObject.fromObject(res).optString("isSuper").equals("1")){
            return ResultGenerator.genFailResult("你木的权限");
        }
        Example example=new Example(HzSlide.class);
        example.createCriteria().andEqualTo("consultId",
                Crypt.desDecrypt(consult_id,Enumeration.SECRET_KEY.CONSULT_ID_KEY)).andEqualTo("isDelete",0);
        List<HzSlide> slides=slideMapper.selectByExample(example);
        return ResultGenerator.genSuccessResult(slides);
    }

}
