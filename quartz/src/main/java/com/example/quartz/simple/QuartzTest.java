package com.example.quartz.simple;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.KeyMatcher;

public class QuartzTest {
    public static void main(String[] args) {
        try {
            final Scheduler defaultScheduler = StdSchedulerFactory.getDefaultScheduler();
            defaultScheduler.start();

            JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
                    .withIdentity("user1", "group1")
                    .usingJobData("name", "Hello")
                    .usingJobData("age", 30)
                    .build();

            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("user1", "group1")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(5)
                            .repeatForever())
                    .build();

            defaultScheduler.scheduleJob(jobDetail, trigger);

            defaultScheduler.getListenerManager().addTriggerListener(new TriggerListener(),
                    KeyMatcher.keyEquals(new TriggerKey("user", "group")));
            // GroupMatcher.groupEquals("myGroup");
            // OrMatcher.or(GroupMatcher.groupEquals("a"), GroupMatcher.groupEquals("b"));
            // EverythingMatcher.allJobs();
            // defaultScheduler.getListenerManager().addJobListener(xxx);

            Thread.sleep(10000);

            defaultScheduler.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
