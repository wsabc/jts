package com.example.quartz.simple;

import org.quartz.DateBuilder;
import org.quartz.JobKey;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class HelloTrigger {
    Trigger get() {
        return TriggerBuilder.newTrigger()
                .withIdentity(TriggerKey.triggerKey("user", "group"))
                .startNow()
                .endAt(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
                .forJob(new JobKey("user", "group"))
                .build();
    }

    Trigger getSimpleTriggerRunNow() {
        return TriggerBuilder.newTrigger()
                .withIdentity("user", "group")
                // .startAt(Date.from(Instant.now())) // default now, trigger now
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                // this cause all 10 are running simultaneously or with all scheduler threads
                                .withIntervalInSeconds(0)
                                .withRepeatCount(10))
                .forJob("user", "group")
                .build();
    }

    Trigger getSimpleTriggerInFuture() {
        return TriggerBuilder.newTrigger()
                .withIdentity("user", "group")
                .startAt(DateBuilder.futureDate(5, DateBuilder.IntervalUnit.MINUTE))
                // .startAt(DateBuilder.evenHourDate(null)) // next even-hour
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(10)
                                .withRepeatCount(10))
                .endAt(DateBuilder.dateOf(23, 30, 0, 31, 12))
                .forJob("user", "group")
                .build();
    }

    Trigger getSimpleTriggerWithMisfireStrategy() {
        return TriggerBuilder.newTrigger()
                // .withIdentity("user", "group") // with default generated trigger key
                .withSchedule(
                        SimpleScheduleBuilder.simpleSchedule()
                                .withIntervalInSeconds(10)
                                .withRepeatCount(10)
                                .withMisfireHandlingInstructionFireNow())
                .forJob("user", "group")
                .build();
    }
}
