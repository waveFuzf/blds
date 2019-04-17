package com.example.blds.controller.bl;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.entity.HzConsult;
import com.example.blds.entity.HzEvaluate;
import com.example.blds.service.HzConsultService;
import com.example.blds.service.HzEvaluateService;
import com.example.blds.util.Crypt;
import com.example.blds.util.Enumeration;
import com.example.blds.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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

    @Autowired
    private TokenUtil tokenUtil;

    @ApiOperation(value = "申请医生评价")
    @PostMapping("/applyDoctorEvaluate.htm")
    @ResponseBody
    public Result applyDoctorEvaluate(HttpServletRequest request,
              @ApiParam(name="consultId", value="加密的评价病例id") @RequestParam(value="consultId") String consultId,
              @ApiParam(name="evaluateWhole", value="整体评价 1~5") @RequestParam(value="evaluateWhole",required = false) Integer evaluateWhole,
              @ApiParam(name="evaluateProfession", value="专业评价 1~5") @RequestParam(value="evaluateProfession",required = false) Integer evaluateProfession,
              @ApiParam(name="evaluateIntime", value="及时性评价 1~5") @RequestParam(value="evaluateIntime",required = false) Integer evaluateIntime,
              @ApiParam(name="evaluateText", value="文字评价") @RequestParam(value="evaluateText") String evaluateText,
              @ApiParam(name="type",value = "类型")@RequestParam (value = "type")Integer type
    ) throws Exception {
        String str=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (str.equals("token无效")){
            return ResultGenerator.genFailResult("用户token无效");
        }
        JSONObject obj=JSONObject.fromObject(str);
        Integer consid = Crypt.desDecryptByInteger(consultId,Enumeration.SECRET_KEY.CONSULT_ID_KEY);

        HzConsult consult = consultService.selectById(consid);

        if (consult==null||!(consult.getConsultStatus()==6)){
            return ResultGenerator.genFailResult("病例不存在或病例数据异常！");
        }
        HzEvaluate eva=evaluateService.selEvaluateByConsultId(consid,obj.optInt("userId"));
        if(eva!=null&&type==0){
            return ResultGenerator.genFailResult("请勿二次评价！");
        }

        HzEvaluate evaluate = new HzEvaluate();
        evaluate.setConsultId(consid);
        evaluate.setEvaluateWhole(evaluateWhole);
        evaluate.setEvaluateProfession(evaluateProfession);
        evaluate.setEvaluateIntime(evaluateIntime);
        evaluate.setEvaluateText(evaluateText);
        evaluate.setEvaluateType(type);
        evaluate.setCreateTime(new Date());
        evaluate.setEvaluateStatus(0);
        evaluate.setIsDelete(0);
        evaluate.setDoctorName(consult.getDoctors().get(0).getDoctorName());

        evaluate.setEvaluatorId(consult.getDoctors().get(0).getDoctorId());
        evaluate.setEvaluateeId(consult.getDoctors().get(1).getDoctorId());

        Integer insertLine = 0;
        insertLine=evaluateService.addEvaluate(evaluate);
        if(insertLine == 0){
           return ResultGenerator.genFailResult("数据库插入失败");
        }
        return ResultGenerator.genSuccessResult("成功");
    }


    @ApiOperation(value = "公开隐藏")
    @PostMapping("/evaluateShowToggle.htm")
    @ResponseBody
    public Result editEvaluateShow(
            @RequestParam boolean status,
            @RequestParam String evaluateId,
            HttpServletRequest request
    ) throws Exception {
        String res=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (res.equals("token无效")){
            return ResultGenerator.genFailResult(res);
        }
        if (!JSONObject.fromObject(res).optString("isSuper").equals("1")){
            return ResultGenerator.genFailResult("你木的权限");
        }
        HzEvaluate evaluate=new HzEvaluate();
        evaluate.setId(Integer.valueOf(Crypt.desDecrypt(evaluateId,Enumeration.SECRET_KEY.EVALUATE_ID_KEY)));
        evaluate.setEvaluateStatus(status?0:1);

        return ResultGenerator.genSuccessResult(evaluateService.updateEvaluateById(evaluate));
    }


    @ApiOperation(value = "根据consultId查询评价")
    @PostMapping("/selectEvaluateByConsultId.htm")
    @ResponseBody
    public Result selectDiagnosByConsultId(
            @ApiParam(name="consultId", value="密文病例id") @RequestParam(value="consultId") String consultId,
            @ApiParam(name="userId", value="用户id") @RequestParam(value="userId",required = false) Integer userId,
            @ApiParam(name="type",value = "用户类型")@RequestParam(value = "type")Integer type)
            throws Exception{
        Integer consultSelId = Crypt.desDecryptByInteger(consultId, Enumeration.SECRET_KEY.CONSULT_ID_KEY);

        HzEvaluate evaluate = evaluateService.selEvaluateByConsultId(consultSelId,userId,type);
        if(evaluate != null) {
            evaluate.setEvaluate_id(Crypt.desEncrypt(evaluate.getId().toString(),Enumeration.SECRET_KEY.EVALUATE_ID_KEY));
            evaluate.setId(null);
            evaluate.setConsultId(null);
        }

        return ResultGenerator.genSuccessResult(evaluate);
    }

    @ApiOperation(value = "根据医生ID查询评价")
    @PostMapping("/selectEvaluateById.htm")
    @ResponseBody
    public Result selectEvaluateById(
            @ApiParam(name="doctorId", value="医生ID") @RequestParam(value="doctorId") Integer doctorId,
            @RequestParam(value = "pageNo",required = false,defaultValue = "1")Integer pageNo)
            throws Exception{
        List<HzEvaluate> evaluates=new ArrayList<>();
        evaluates=evaluateService.selEvaluateById(doctorId,pageNo);
        return ResultGenerator.genSuccessResult(evaluates);


    }


}
