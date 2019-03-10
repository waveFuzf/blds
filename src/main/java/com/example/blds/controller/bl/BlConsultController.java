package com.example.blds.controller.bl;

import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.aop.UserTokenAop;
import com.example.blds.service.HzConsultService;
import com.example.blds.util.Crypt;
import com.example.blds.util.Enumeration;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/blConsult")
@RestController
public class BlConsultController {

    @Autowired
    private HzConsultService consultService;

    @UserTokenAop
    @ApiOperation(value = "改变病理status")
    @ResponseBody
    @PostMapping("changeStatusByNo")
    public Result changeStatusByNo(@RequestParam("token")String token,@RequestParam("consultNo")String consultNo,
                                   @RequestParam("consultStatus")Integer consultStatus) throws Exception {
        Integer result=consultService.updateCaseStatus(Integer.valueOf(Crypt.desDecrypt(consultNo,Enumeration.SECRET_KEY.CONSULT_ID_KEY)),consultStatus);
        return ResultGenerator.genSuccessResult(result);
    }

}
