package com.example.blds.controller.bl;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.aop.UserTokenAop;
import com.example.blds.dao.DiagnoseInfo;
import com.example.blds.dao.HzConsultMapper;
import com.example.blds.dao.HzDiagnoseMapper;
import com.example.blds.entity.HzConsult;
import com.example.blds.entity.HzDiagnose;
import com.example.blds.entity.HzSlide;
import com.example.blds.service.HzConsultService;
import com.example.blds.service.HzDiagnoseService;
import com.example.blds.service.HzFileService;
import com.example.blds.service.HzSlidesService;
import com.example.blds.util.Crypt;
import com.example.blds.util.Enumeration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 病理专家（上级）操作病历控制层
 */
@Api(tags = "【病理】【专家】")
@RequestMapping("/blexpert")
@Controller
public class BlExpertContorller {

    @Autowired
    private HzConsultService consultService;
    @Autowired
    private HzDiagnoseService diagnoseService;
    @Autowired
    private HzSlidesService slideService;
    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    @Autowired
    private HzDiagnoseMapper diagnoseMapper;
    @Autowired
    private HzConsultMapper consultMapper;
    @Autowired
    private HzFileService fileService;


    @ApiOperation(value = "根据slideUuid获取病例ID")
    @PostMapping("/getConsultIdBySlideId.htm")
    @UserTokenAop
    @ResponseBody
    public Result getConsultIdBySlideId(
            @ApiParam(value = "切片ID,3aed124f-c65c-4c52-9dd6-53671c9e4c15") String uuid
    ) throws Exception {
        HzSlide slide = slideService.getSlideByUuid(uuid);
        if (slide == null) {
            return ResultGenerator.genFailResult("此切片不存在");
        }
        String id = Crypt.desEncrypt(slide.getConsultId().toString(), Enumeration.SECRET_KEY.CONSULT_ID_KEY);
        return ResultGenerator.genFailResult(id);
    }


    @ApiOperation(value = "专家补充检查")
    @PostMapping("/expertAddDiagnose.htm")
    @UserTokenAop
    @ResponseBody
    public Result expertAddDiagnose(
            @ApiParam(name = "consultId", value = "病例id") @RequestParam(value = "consultId")
                    String consultIdString,
            @ApiParam(name = "immuneTag", value = "免疫组化标记物") @RequestParam(value = "immuneTag", required = false) String immuneTag,
            @ApiParam(name = "materialNum", value = "白片的数量") @RequestParam(value = "materialNum", required = false) Integer materialNum,
            @ApiParam(name = "isCandel", value = "蜡块的数量")  @RequestParam(value = "isCandle", required = false) boolean isCandel,
            @ApiIgnore HttpSession session
    ){

        Integer result;

        //1、获取并判断病例状态
        Integer consultId=Crypt.desDecryptByInteger(consultIdString, Enumeration.SECRET_KEY.CONSULT_ID_KEY);
        HzConsult consult = new HzConsult();
        consult.setId(consultId);
        consult=consultMapper.selectByPrimaryKey(consult);

        if (consult == null) {
            return ResultGenerator.genFailResult("该病例不存在");
        }
        if (consult.getConsultStatus() == Enumeration.CONSULT_STATUS.DIAGNOSED
                || consult.getConsultStatus() == Enumeration.CONSULT_STATUS.RETURNED) {
            return ResultGenerator.genFailResult("该病例已诊断");
        }
        HzDiagnose updateInfo=new HzDiagnose();
        updateInfo.setImmuneTag(immuneTag);
        updateInfo.setMaterialNum(materialNum);
        updateInfo.setIsCandle(isCandel?1:0);
        updateInfo.setIsDelete(0);
        updateInfo.setConsultId(consultId);

        HzDiagnose hzDiagnose= diagnoseService.getDiagnoseByConsultId(consultId);
        if (hzDiagnose==null){
            updateInfo.setConsultId(consultId);
            updateInfo.setDiagnoseTime(new Date());
            result=diagnoseMapper.insert(updateInfo);
        }else {
            updateInfo.setId(hzDiagnose.getId());
            result=diagnoseMapper.updateByPrimaryKeySelective(updateInfo);
        }

        return ResultGenerator.genSuccessResult(result);

    }

    @ApiOperation(value = "专家诊断")
    @PostMapping("/getDiagnoseDetail.htm")
    @UserTokenAop
    @ResponseBody
    public Result getDiagnoseDetail(@ApiParam(name = "consultId", value = "病例id") @RequestParam(value = "consultId")
                                                String consultIdString){
        HzDiagnose hzDiagnose=diagnoseService.
            getDiagnoseByConsultId(Crypt.desDecryptByInteger(consultIdString, Enumeration.SECRET_KEY.CONSULT_ID_KEY));
        DiagnoseInfo diagnoseInfo=new DiagnoseInfo();
        if(hzDiagnose==null){
            return ResultGenerator.genSuccessResult(diagnoseInfo);
        }
        BeanUtils.copyProperties(hzDiagnose, diagnoseInfo);
        return ResultGenerator.genSuccessResult(diagnoseInfo);
    }



    @ApiOperation(value = "专家诊断")
    @PostMapping("/expertDiagnose.htm")
    @UserTokenAop
    @ResponseBody
    public Result expertDiagnosing(
            @ApiParam(name = "consultId", value = "病例id") @RequestParam(value = "consultId")
                    String consultIdString,
            @ApiParam(name = "slideEstimate", value = "切片质量评价（1：质量不合格，2：质量基本合格，3：质量合格，4：质量优秀）", required = false)
            @RequestParam(value = "slideEstimate", required = false) Integer slideEstimate,
            @ApiParam(name = "diagnosisEstimate", value = "初诊质量（1：诊断不正确，2：诊断基本正确，3：诊断完全正确，4：诊断不明确）")
            @RequestParam(value = "diagnosisEstimate", required = false) Integer diagnosisEstimate,
            @ApiParam(name = "mirrorView", value = "镜下所见")
            @RequestParam(value = "mirrorView", required = false) String mirrorView,
            @ApiParam(name = "diagnose", value = "专家意见")
            @RequestParam(value = "diagnose", required = false) String diagnose
    ) throws Exception {
        Integer result;

        //1、获取并判断病例状态
        Integer consultId=Crypt.desDecryptByInteger(consultIdString, Enumeration.SECRET_KEY.CONSULT_ID_KEY);
        HzConsult consult = new HzConsult();
        consult.setId(consultId);
        consult=consultMapper.selectByPrimaryKey(consult);

        if (consult == null) {
            return ResultGenerator.genFailResult("该病例不存在");
        }
        if (consult.getConsultStatus() == Enumeration.CONSULT_STATUS.DIAGNOSED
                || consult.getConsultStatus() == Enumeration.CONSULT_STATUS.RETURNED) {
            return ResultGenerator.genFailResult("该病例已诊断");
        }
        HzDiagnose updateInfo=new HzDiagnose();
        updateInfo.setIsDelete(0);
        updateInfo.setSlideEstimate(slideEstimate);
        updateInfo.setDiagnosisEstimate(diagnosisEstimate);
        updateInfo.setDiagnose(diagnose);
        updateInfo.setMirrorView(mirrorView);

        HzDiagnose hzDiagnose= diagnoseService.getDiagnoseByConsultId(consultId);
        if (hzDiagnose==null){
            updateInfo.setConsultId(consultId);
            updateInfo.setDiagnoseTime(new Date());
            result=diagnoseMapper.insert(updateInfo);
        }else {
            updateInfo.setId(hzDiagnose.getId());
            result=diagnoseMapper.updateByPrimaryKeySelective(updateInfo);
        }

        return ResultGenerator.genSuccessResult(result);
    }

    @ApiOperation(value = "病例退回")
    @PostMapping("/consultReturnApply.htm")
    @UserTokenAop
    @ResponseBody
    public Result consultReturnApply(
            @ApiParam(value = "加密的consultId") String consultId,
            @ApiParam(value = "reason") String reason
    ) throws Exception {
        Integer consid = Crypt.desDecryptByInteger(consultId, Enumeration.SECRET_KEY.CONSULT_ID_KEY);

        HzConsult consult = new HzConsult();
        consult.setId(consid);
        consult.setReason(reason);
        //状态修改为5 代表已退回 (1草稿 2待支付 3待收货 4待诊断 5已退回 6已诊断 7待安排)
        consult.setConsultStatus(5);

        int n=consultMapper.updateByPrimaryKeySelective(consult);
        return n==1?ResultGenerator.genSuccessResult("成功!"):ResultGenerator.genFailResult("失败");
    }
}
