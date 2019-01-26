package com.team175.robot.commands.teleop;

import com.team175.robot.commands.LoggableCommand;
import com.team175.robot.subsystems.Elevator;

public class MotionMagicElevator extends LoggableCommand {

    public MotionMagicElevator() {
        requires(Elevator.getInstance());
    }

    @Override
    protected void initialize() {
        mLogger.info("ManualArcadeDrive command initialized.");
    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        mLogger.info("ManualArcadeDrive command ended/interrupted.");
    }

    @Override
    protected void interrupted() {
        end();
    }

}
