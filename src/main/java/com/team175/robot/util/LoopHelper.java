package com.team175.robot.util;

import com.team175.robot.Robot;
import edu.wpi.first.wpilibj.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A slightly modified version of Team254's Looper object that is simplified by taking advantage of Java's new
 * (~5 years old) functional programming stuff.
 *
 * This code runs all of the robot's loops. Loop objects are stored in a List object. They are started when the robot
 * powers up and stopped after the match.
 *
 * @author Team254
 */
public class LoopHelper {

    private final List<Loop> mLoops;
    private final Notifier mNotifier;
    private final Logger mLogger;

    private boolean mIsRunning;

    private static double PERIOD = Robot.getDefaultPeriod() / 2;

    public LoopHelper(Loop... loops) {
        mLoops = List.of(loops);
        mNotifier = new Notifier(() -> mLoops.forEach(Loop::loop));
        mLogger = LoggerFactory.getLogger(getClass().getSimpleName());
        mIsRunning = false;
    }

    public void start() {
        if (!mIsRunning) {
            mLogger.info("Starting loops.");
            mLoops.forEach(Loop::start);
            mLogger.info("Starting loops' period method");
            mNotifier.startPeriodic(PERIOD);
            mIsRunning = true;
        }
    }

    public void stop() {
        if (mIsRunning) {
            mLogger.info("Stopping loops.");
            mNotifier.stop();
            mLoops.forEach(Loop::stop);
            mIsRunning = false;
        }
    }

}
