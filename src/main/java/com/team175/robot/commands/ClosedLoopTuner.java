package com.team175.robot.commands;

import com.team175.robot.Robot;
import com.team175.robot.util.tuning.CSVLogger;
import com.team175.robot.util.tuning.ClosedLoopTunable;

import edu.wpi.first.wpilibj.Notifier;

/**
 * @author Arvind
 */
public class ClosedLoopTuner extends AldrinCommand {

    private ClosedLoopTunable mSubsystem;
    private CSVLogger mWriter;
    private Notifier mNotifier;

    public ClosedLoopTuner(ClosedLoopTunable subsystem) {
        mSubsystem = subsystem;
        mWriter = new CSVLogger(mSubsystem);
        mNotifier = new Notifier(mWriter);

        super.logInstantiation();
    }

    @Override
    public void initialize() {
        mSubsystem.reset();

        double refreshRate;
        try {
            // Take half of kDefaultPeriod in the Robot's superclass (in case either FastTimedRobot or TimeRobot is used
            refreshRate = Double.parseDouble(Robot.class.getSuperclass().getField("kDefaultPeriod").get(null).toString())
                    / 2.0;
        } catch (IllegalAccessException | NoSuchFieldException | NumberFormatException e) {
            mLogger.warn("Failed to parse refresh rate! Using default of 10 ms instead...", e);
            refreshRate = 0.01; // 10 ms
        }
        mLogger.debug("Refresh Rate: {}", refreshRate);

        try {
            mNotifier.startPeriodic(refreshRate);
            mSubsystem.updateGains();
        } catch (Exception e) {
            mLogger.error("Failed to start CSVLogger thread!", e);
        }

        super.logInit();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    public void end() {
        mNotifier.stop();
        mWriter.stop();

        super.logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

}