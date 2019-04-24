package com.example.blds.controller;


import com.alibaba.fastjson.JSON;
import com.example.blds.Re.Result;
import com.example.blds.Re.ResultGenerator;
import com.example.blds.config.QuratzScheduler;
import com.example.blds.entity.HzLoginInfo;
import com.example.blds.entity.HzUser;
import com.example.blds.service.HzLoginInfoService;
import com.example.blds.service.HzUserService;
import com.example.blds.util.TokenUtil;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin
@RestController
public class LoginController {
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private HzLoginInfoService hzLoginInfoService;
    @Autowired
    private HzUserService hzUserService;
    @Autowired
    private QuratzScheduler quratzScheduler;
    //我自己测试用用的。
    @PostMapping("/sign")
    public Result Sign(@ApiParam(value = "用户名", required = true) @RequestParam(value = "loginName") String username,
                       @ApiParam(value = "密码", required = true) @RequestParam(value = "password") String password){
        if (hzLoginInfoService.getByUsername(username)==null){
            hzLoginInfoService.save(new HzLoginInfo(username,
                    new SimpleHash("md5", password, ByteSource.Util.bytes(""),
                            2).toHex()));

            return ResultGenerator.genSuccessResult("注册成功.");
        }
        return ResultGenerator.genFailResult("注册失败.账号已存在。");
    }

    @PostMapping("login")
    public Result doLogin(@ApiParam(value = "用户名", required = true) @RequestParam(value = "loginName") String username,
                          @ApiParam(value = "密码", required = true) @RequestParam(value = "password") String password,
                          HttpServletResponse httpServletResponse) throws SchedulerException {
        Subject subject = SecurityUtils.getSubject();
        password = new SimpleHash("md5", password, ByteSource.Util.bytes(""), 2).toHex();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);//通过 MyshiroRealm的doGetAuthenticationInfo()方法来验证是否正确
        } catch (AuthenticationException e) {
            token.clear();
            return ResultGenerator.genFailResult("登录失败，用户名或密码错误！");
        }
        HzLoginInfo user=hzLoginInfoService.getByUsername(username);
        HzUser hzUser=hzUserService.getUserInfoByUid(user.getUid());
        hzUserService.changeStatusByUid("1",hzUser.getId());
        String userSession = JSON.toJSONString(hzUser);
        String redistoken=tokenUtil.createToken(userSession);
        for (int i=0;i<100;i++){
            quratzScheduler.dosth(hzUser);
        }
        return ResultGenerator.genSuccessResult(redistoken,hzUser.getIsSuper());
    }
    @PostMapping("logout")
    public String logOut(@ApiParam(value = "用户token", required = true) @RequestParam(value = "token") String token,
                         @ApiParam(value = "用户名", required = true) @RequestParam(value = "username") String username){
        HzUser hzUser=JSON.parseObject(tokenUtil.checkToken(token),HzUser.class);
        hzUserService.changeStatusByUid("0",hzUser.getUserId());
        tokenUtil.deleteToken(token);
        return "退出成功！";
    }
}