package com.example.quartz.spring;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SpringJob implements Job {

    @Autowired
    SpringJobService jobService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        jobService.executeJob();
    }
}
