package com.team175.robot.commands;

import com.team175.robot.util.CSVLogger;
import com.team175.robot.util.ClosedLoopTunable;

import edu.wpi.first.wpilibj.Notifier;

/**
 * TODO: Implement PID updating from SmartDashboard.
 *
 * @author Arvind
 */
public class ClosedLoopTuner extends AldrinCommand {

    private Notifier mNotifier;
    private ClosedLoopTunable mSubsystem;

    public ClosedLoopTuner(ClosedLoopTunable subsystem) {
        // requires();
        mNotifier = new Notifier(CSVLogger.getInstance());
        mSubsystem = subsystem;

        super.instantiationLog();
    }

    @Override
    public void initialize() {
        mSubsystem.updatePID();

        CSVLogger.getInstance().setTarget(mSubsystem);
        mNotifier.startPeriodic(0.01); // 10 ms?

        super.initLog();
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    public void end() {
        mNotifier.stop();
        CSVLogger.getInstance().stop();

        super.endLog();
    }

    @Override
    protected void interrupted() {
        end();
    }

}