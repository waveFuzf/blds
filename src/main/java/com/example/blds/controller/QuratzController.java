package com.example.blds.controller;

import com.example.blds.Re.Result;
import com.example.blds.config.QuratzScheduler;
import com.example.blds.util.SendMsgUtil;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
public class QuratzController {
    @Autowired
    private QuratzScheduler quratzScheduler;
    @Autowired
    private SendMsgUtil msgUtil;
    @PostMapping("/donosth")
    public Result donosth() throws SchedulerException {
        msgUtil.sendMessageByHttp("你就是只猪！","17826863260");
        return null;
    }
}
