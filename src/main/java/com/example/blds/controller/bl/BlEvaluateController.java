package com.example.blds.controller.bl;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.entity.HzConsult;
import com.example.blds.entity.HzConsultDoctor;
import com.example.blds.entity.HzEvaluate;
import com.example.blds.service.HzConsultService;
import com.example.blds.service.HzEvaluateService;
import com.example.blds.util.Crypt;
import com.example.blds.util.Enumeration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 评价管理 申请医生评价
 * @author Liujingguang
 */
@Api(tags = "评价管理  (评价列表、评价查询 评价审核 申请医生评价)")
@RequestMapping("/evaluate")
@Controller
public class BlEvaluateController {

    @Autowired
    private HzEvaluateService evaluateService;

    @Autowired
    private HzConsultService consultService;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @ApiOperation(value = "申请医生评价")
    @PostMapping("/applyDoctorEvaluate.htm")
    @ResponseBody
    public Result applyDoctorEvaluate(HttpServletRequest request,
              @ApiParam(name="consultId", value="加密的评价病例id") @RequestParam(value="consultId") String consultId,
              @ApiParam(name="evaluateType", value="评价类别 (0.下级医生对上级评价 1.上级医生对下级评价)") @RequestParam(value="evaluateType") Integer evaluateType,
              @ApiParam(name="evaluateWhole", value="整体评价 1~5") @RequestParam(value="evaluateWhole") Integer evaluateWhole,
              @ApiParam(name="evaluateProfession", value="专业评价 1~5") @RequestParam(value="evaluateProfession") Integer evaluateProfession,
              @ApiParam(name="evaluateIntime", value="及时性评价 1~5") @RequestParam(value="evaluateIntime") Integer evaluateIntime,
              @ApiParam(name="evaluateText", value="文字评价") @RequestParam(value="evaluateText") String evaluateText
    ) throws Exception {

        Integer consid = Crypt.desDecryptByInteger(consultId,Enumeration.SECRET_KEY.CONSULT_ID_KEY);

        HzConsult consult = consultService.selectById(consid);

        if (consult==null){
            return ResultGenerator.genFailResult("病例不存在");
        }else if(consult.getDoctors().size()<2) {
            return ResultGenerator.genFailResult("病例数据异常");
        }

        HzEvaluate evaluatec = evaluateService.selEvaluateByConsultId(consid);

        //设置前台传来的值
        HzEvaluate evaluate = new HzEvaluate();
        evaluate.setConsultId(consid);
        evaluate.setEvaluateType(evaluateType);
        evaluate.setEvaluateWhole(evaluateWhole);
        evaluate.setEvaluateProfession(evaluateProfession);
        evaluate.setEvaluateIntime(evaluateIntime);
        evaluate.setEvaluateText(evaluateText);
        evaluate.setCreateTime(new Date());
        evaluate.setEvaluateStatus(0);

        //评价被评价者id设置
        List<HzConsultDoctor> doctors=consult.getDoctors();
        evaluate.setEvaluatorId(doctors.get(0).getDoctorId());
        evaluate.setEvaluateeId(doctors.get(1).getDoctorId());
        //存入数据库
        Integer insertLine = 0;
        if(evaluatec!=null){
            evaluate.setId(evaluatec.getId());
            insertLine = evaluateService.updateEvaluateById(evaluate);
        }else if(evaluatec==null){
            insertLine = evaluateService.addEvaluate(evaluate);
        }

        if(insertLine == 0){
           return ResultGenerator.genFailResult("数据库插入失败");
        }

        return ResultGenerator.genSuccessResult("成功");
    }



    @ApiOperation(value = "根据consultId查询评价")
    @PostMapping("/selectEvaluateByConsultId.htm")
    @ResponseBody
    public Result selectDiagnosByConsultId(
            @ApiParam(name="consultId", value="密文病例id") @RequestParam(value="consultId") String consultId)
            throws Exception{
        Integer consultSelId = Crypt.desDecryptByInteger(consultId, Enumeration.SECRET_KEY.CONSULT_ID_KEY);

        HzEvaluate evaluate = evaluateService.selEvaluateByConsultId(consultSelId);
        if(evaluate != null) {
            evaluate.setEvaluate_id(Crypt.desEncrypt(evaluate.getId().toString(),Enumeration.SECRET_KEY.EVALUATE_ID_KEY));
            evaluate.setId(null);
        }

        return ResultGenerator.genSuccessResult(evaluate);
    }

}
