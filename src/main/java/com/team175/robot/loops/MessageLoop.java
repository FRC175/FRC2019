package com.team175.robot.loops;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A loop that sends random messages to the SmartDashboard. Primary use is for trolling the drivers.
 *
 * @author Abdullah, Arvind
 */
public class MessageLoop implements Loop {

    private String[] mMessages;

    public MessageLoop(String[] messages, String dashboardKey) {
        mMessages = messages;
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {

    }

    @Override
    public void stop() {

    }

}
