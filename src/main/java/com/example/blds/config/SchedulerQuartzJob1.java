package com.example.blds.config;

import com.example.blds.dao.HzUserMapper;
import com.example.blds.entity.HzUser;
import org.quartz.Job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 实现Job接口
 * @author yvan
 *arg0
 */
public class SchedulerQuartzJob1 implements Job{
    @Autowired
    private HzUserMapper hzUserMapper;

    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        HzUser user= (HzUser) arg0.getJobDetail().getJobDataMap().get("USER");
        hzUserMapper.updateStatusByUid("0",user.getId());
    }

}
