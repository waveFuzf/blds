package com.example.blds.util;

import com.alibaba.fastjson.JSONObject;
import com.example.blds.Re.Result;
import com.example.blds.controller.LoginController;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
@Component
public class SendMsgUtil {
    @Autowired
    private RestTemplate restTemplate;

    public  JSONObject sendMessageByHttp(String msg, String phone) {
        MultiValueMap<String, Object> urlParm = new LinkedMultiValueMap<String, Object>();
        urlParm.add("loginName","1777");
        urlParm.add("password","123456");
        ResponseEntity<Result> responseEntity = restTemplate.postForEntity("http://localhost:8080/login", urlParm,Result.class);
        return null;
    }

    public static void main(String[] args) {
        new SendMsgUtil().sendMessageByHttp("你就是蜘蛛","17826863260");
    }
}
