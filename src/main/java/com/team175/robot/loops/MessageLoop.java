package com.team175.robot.loops;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * A loop that sends random messages to the SmartDashboard. Primary use is for trolling the drivers.
 *
 * @author Abdullah
 * @author Arvind
 */
public class MessageLoop implements Loop {

    private final String[] mMessages;
    private final String mDashboardKey;

    public MessageLoop(String[] messages, String dashboardKey) {
        mMessages = messages;
        mDashboardKey = dashboardKey;
    }

    @Override
    public void start() {
        SmartDashboard.putString(mDashboardKey, "yo!");
    }

    @Override
    public void loop() {
        SmartDashboard.putString(mDashboardKey, mMessages[(int) (Math.random() * mMessages.length)]);
    }

    @Override
    public void stop() {
        SmartDashboard.putString(mDashboardKey, "Prepare for the worst!");
    }

}
