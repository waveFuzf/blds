package com.example.blds.controller.bl;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.entity.QualityInfo;
import com.example.blds.service.HzConsultService;
import com.example.blds.util.TokenUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "质控")
@RequestMapping("/blQuality")
@Controller
public class blQualityController {

    @Autowired
    private TokenUtil tokenUtil;

    @Autowired
    private HzConsultService consultService;

    @ApiOperation(value = "分类查询")
    @PostMapping("/selectQualityInfo.htm")
    @ResponseBody
    public Result selectQualityInfo(
            @RequestParam(required = false) String beginTime,
            @RequestParam(required = false) String endTime,
            @RequestParam String activeName,
            @RequestParam String radio,
            HttpServletRequest request
            ) throws Exception {
        String res=tokenUtil.checkToken(request.getCookies()[1].getValue());
        if (res.equals("token无效")){
            return ResultGenerator.genFailResult(res);
        }
        if (!JSONObject.fromObject(res).optString("isSuper").equals("1")){
            return ResultGenerator.genFailResult("你木的权限");
        }
        List<QualityInfo> qualityInfo=consultService.selectQualityInfo(beginTime,endTime,activeName,radio);
        int total=qualityInfo.stream().mapToInt(QualityInfo::getCount).sum();
        return ResultGenerator.genSuccessResult(qualityInfo,total);
    }




}
