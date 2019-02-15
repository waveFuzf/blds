package com.example.blds.controller.common;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.entity.HzDoctor;
import com.example.blds.entity.HzEvaluate;
import com.example.blds.service.HzDoctorService;
import com.example.blds.service.HzEvaluateService;
import com.example.blds.util.Enumeration;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Api(tags = "【公共】【专家】")
@RequestMapping("/Doctor")
@Controller
public class DoctorController {

    @Autowired
    private HzEvaluateService evaluateService;
    @Autowired
    private HzDoctorService hzDoctorService;

    @ApiOperation("获取专家信息")
    @PostMapping("/getExpertDoctorList.htm")
    @ResponseBody
    public Result getExpertDoctorList(){
        Page<HzDoctor> pageInfo = PageHelper.startPage(1, 8);
        List<HzDoctor> doctors = hzDoctorService.getExpertDoctorList();
        return ResultGenerator.genSuccessResult(pageInfo);
    }


    @ApiOperation("【获取专家的评价列表】")
    @PostMapping("/getExpertEvaluateList.htm")
    @ResponseBody
    public Result getExpertEvaluateList(
            @ApiParam(value = "专家id", required = true) @RequestParam Long doctorId,
            @ApiParam(value = "页码，默认0") @RequestParam(defaultValue = "1", required = false) Integer pageNo,
            @ApiParam(value = "页长，默认10") @RequestParam(defaultValue = "10", required = false) Integer pageSize
        ) {
        PageInfo<HzEvaluate> page = evaluateService.getExpertEvaluateList(Enumeration.EvaluateType.DOCTOR2EXPERT,
                null, doctorId, pageNo, pageSize);

        //匿名处理
        for (HzEvaluate e: page.getList()) {
            if(StringUtils.isNotBlank(e.getDoctorName())) {
                String name = e.getDoctorName().substring(0, 1);
                name += e.getDoctorName().length() > 2 ? "**" : "*";
                e.setDoctorName(name);
            }
        }
        return ResultGenerator.genFailResult(page);
    }


}
