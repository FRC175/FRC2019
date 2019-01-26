package com.team175.robot.commands.teleop;

import com.team175.robot.OI;
import com.team175.robot.commands.LoggableCommand;
import com.team175.robot.subsystems.Elevator;

public class ManualElevator extends LoggableCommand {

    public ManualElevator() {
        requires(Elevator.getInstance());
    }

    @Override
    protected void initialize() {
        mLogger.info("ManualElevator command initialized.");
    }

    @Override
    protected void execute() {
        Elevator.getInstance().setPower(OI.getInstance().getOperatorStickY());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Elevator.getInstance().setPower(0);
        mLogger.info("ManualElevator command ended/interrupted.");
    }

    @Override
    protected void interrupted() {
        end();
    }

}
