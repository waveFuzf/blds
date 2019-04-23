package com.example.blds.config;

import org.quartz.Job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
/**
 * 实现Job接口
 * @author yvan
 *
 */
public class SchedulerQuartzJob1 implements Job{

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        System.out.println("开始："+System.currentTimeMillis());
        // TODO 业务
        System.out.println("结束："+System.currentTimeMillis());
    }

}
