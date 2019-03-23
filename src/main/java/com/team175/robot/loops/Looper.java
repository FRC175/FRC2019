package com.team175.robot.loops;

import edu.wpi.first.wpilibj.Notifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * A slightly modified version of Team254's Looper object that is mainly simplified by taking advantage of Java's new
 * (~5 years old) functional programming stuff.
 *
 * This code runs all of the robot's loops. Loop objects are stored in a List object. They are started when the robot
 * powers up and stopped after the match.
 *
 * @author Team254
 */
public final class Looper {

    private final List<Loop> mLoops;
    private final Notifier mNotifier;
    private final Logger mLogger;
    private final double mPeriod;

    private boolean mIsRunning;

    public Looper(List<Loop> loops, double period) {
        mLoops = loops;
        mNotifier = new Notifier(() -> mLoops.forEach(Loop::loop));
        mLogger = LoggerFactory.getLogger(getClass().getSimpleName());
        mPeriod = period;
        mIsRunning = false;
    }

    public void start() {
        if (!mIsRunning) {
            mLogger.info("Starting loops.");
            mLoops.forEach(Loop::start);
            mLogger.info("Starting loops' periodic method.");
            mNotifier.startPeriodic(mPeriod);
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
