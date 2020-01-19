package com.example.quartz.spring;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import javax.xml.ws.RequestWrapper;

@Configuration
public class SpringQuartz {

    @Bean
    public JobDetail jobDetail() {
        return JobBuilder.newJob().ofType(SpringJob.class)
                .storeDurably()
                .withIdentity("job1")
                .withDescription("run job1")
                .build();
    }

    @Bean
    public Trigger trigger() {
        return TriggerBuilder.newTrigger().forJob("job1")
                .withIdentity("trigger1")
                .withDescription("trigger job1")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(0, 1))
                .build();
    }

    @Bean
    @RequestWrapper
    public DataSource quartzDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public Scheduler scheduler(Trigger trigger, JobDetail jobDetail, SchedulerFactoryBean fb)
            throws SchedulerException {
        final Scheduler scheduler = fb.getScheduler();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
        return scheduler;
    }

}
