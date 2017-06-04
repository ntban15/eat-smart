package com.annguyen.android.eatsmart.libs;

import com.annguyen.android.eatsmart.libs.base.EventBus;

/**
 * Created by annguyen on 6/1/2017.
 */

public class GreenRobotEventBus implements EventBus {
    private org.greenrobot.eventbus.EventBus greenRobot;

    public GreenRobotEventBus(org.greenrobot.eventbus.EventBus greenRobot) {
        this.greenRobot = greenRobot;
    }

    @Override
    public void register(Object subscriber) {
        greenRobot.register(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        greenRobot.unregister(subscriber);
    }

    @Override
    public void post(Object event) {
        greenRobot.post(event);
    }
}
