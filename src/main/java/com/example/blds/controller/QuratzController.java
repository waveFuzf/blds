package com.example.blds.controller;

import com.example.blds.Re.Result;
import com.example.blds.config.QuratzScheduler;
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

    @PostMapping("/dosth")
    public Result Sign() throws SchedulerException {
        quratzScheduler.startJob();
        return null;
    }
}
