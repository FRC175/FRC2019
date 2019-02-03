package com.team175.robot.commands;

import com.team175.robot.Robot;
import com.team175.robot.util.logging.CSVLogger;
import com.team175.robot.util.PIDTunable;

import edu.wpi.first.wpilibj.Notifier;

/**
 * @author Arvind
 */
public class PIDTuner extends AldrinCommand {

    private PIDTunable mSubsystem;
    private CSVLogger mWriter;
    private Notifier mNotifier;

    public PIDTuner(PIDTunable subsystem) {
        mSubsystem = subsystem;
        mWriter = new CSVLogger(mSubsystem);
        mNotifier = new Notifier(mWriter);

        super.logInstantiation();
    }

    @Override
    public void initialize() {
        mSubsystem.updatePID();
        double refreshRate = 0.01; // 10 ms?
        try {
            refreshRate = Double.parseDouble(Robot.class.getSuperclass().getField("kDefaultPeriod").get(null).toString())
                    / 2.0;
        } catch (IllegalAccessException | NoSuchFieldException | NumberFormatException e) {
            mLogger.error("Failed to parse refresh rate!", e);
        }
        mNotifier.startPeriodic(refreshRate);

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