package com.example.blds.controller.bl;


import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.entity.HzDiagnose;
import com.example.blds.service.HzDiagnoseService;
import com.example.blds.util.Crypt;
import com.example.blds.util.Enumeration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Api(tags = "病理诊断")
@RequestMapping("/bldiagnose")
@Controller
public class BlSpecialistController {

    @Autowired
    private HzDiagnoseService diagnoseService;



    @ApiOperation(value = "根据consultId查询诊断")
    @PostMapping("/selectDiagnosByConsultId.htm")
    @ResponseBody
    public Result selectDiagnosByConsultId(
            @ApiParam(name="consultId", value="密文病例id") @RequestParam(value="consultId") String consultId)
            throws Exception{
        Integer consultSelId = Crypt.desDecryptByInteger(consultId, Enumeration.SECRET_KEY.CONSULT_ID_KEY);

        HzDiagnose diagnose = diagnoseService.selectDiagnosByConsultId(consultSelId);

        return ResultGenerator.genSuccessResult(diagnose);
    }
}
