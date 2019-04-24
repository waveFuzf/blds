package com.example.blds.config;

import java.util.Date;
import java.util.Random;
import java.util.UUID;

import com.example.blds.entity.HzUser;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuratzScheduler {

    @Autowired
    private Scheduler scheduler;

    @Bean
    public Scheduler scheduler() throws SchedulerException{
        SchedulerFactory schedulerFactoryBean = new StdSchedulerFactory();
        return schedulerFactoryBean.getScheduler();
    }

    public void dosth(HzUser user) throws SchedulerException {
        startSchedule(scheduler,user);
        scheduler.start();
    }

    private void startSchedule(Scheduler scheduler,HzUser user) throws SchedulerException {
        Random rod=new Random();
        JobDetail jobDetail = JobBuilder.newJob(SchedulerQuartzJob1.class).withIdentity(UUID.randomUUID().toString(), "group1").build();
        jobDetail.getJobDataMap().put("USER",user);
        Trigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(UUID.randomUUID().toString(), "group1")
                .startAt(DateBuilder.futureDate(1, DateBuilder.IntervalUnit.MINUTE)).build();
        scheduler.scheduleJob(jobDetail, cronTrigger);
    }

    public String getJobInfo(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        return String.format("time:%s,state:%s", cronTrigger.getCronExpression(),
                scheduler.getTriggerState(triggerKey).name());
    }

    public boolean modifyJob(String name, String group, String time) throws SchedulerException {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getCronExpression();
        if (!oldTime.equalsIgnoreCase(time)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                    .withSchedule(cronScheduleBuilder).build();
            date = scheduler.rescheduleJob(triggerKey, trigger);
        }
        return date != null;
    }

    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    public void pauseJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.pauseJob(jobKey);
    }

    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    public void resumeJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.resumeJob(jobKey);
    }

    public void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null)
            return;
        scheduler.deleteJob(jobKey);
    }
}

