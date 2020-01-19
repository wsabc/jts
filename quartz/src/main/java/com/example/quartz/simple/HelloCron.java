package com.example.quartz.simple;

import org.quartz.CronScheduleBuilder;
import org.quartz.DateBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;

import java.util.TimeZone;

public class HelloCron {
    public Trigger getByExpression() {
        return TriggerBuilder.newTrigger().forJob("user", "group")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 */1 8-17 * * ?"))
                .build();
    }

    public Trigger getByApi() {
        return TriggerBuilder.newTrigger().forJob("user", "group")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(10, 42))
                .build();
    }

    public Trigger getInTZ() {
        return TriggerBuilder.newTrigger().forJob("user", "group")
                .withSchedule(CronScheduleBuilder
                        .weeklyOnDayAndHourAndMinute(DateBuilder.WEDNESDAY, 10, 42)
                        .inTimeZone(TimeZone.getTimeZone("America/Los_Angeles")))
                .build();
    }

    public Trigger getWithMisfire() {
        return TriggerBuilder.newTrigger().forJob("user", "group")
                .withSchedule(CronScheduleBuilder
                        .weeklyOnDayAndHourAndMinute(DateBuilder.WEDNESDAY, 10, 42)
                        .withMisfireHandlingInstructionFireAndProceed()
                        .inTimeZone(TimeZone.getTimeZone("America/Los_Angeles")))
                .build();
    }
}
