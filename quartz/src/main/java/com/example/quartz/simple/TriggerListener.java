package com.example.quartz.simple;

import org.quartz.Trigger;
import org.quartz.listeners.TriggerListenerSupport;

public class TriggerListener extends TriggerListenerSupport {
    @Override
    public String getName() {
        return "my-trigger-listener";
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        super.triggerMisfired(trigger);
        System.out.println("Misfire");
    }
}
