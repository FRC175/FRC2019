package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;

public class ManualRearLift extends AldrinCommand {

    public ManualRearLift() {
        requires(Lift.getInstance(), Drive.getInstance());

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().stop();

        super.logInit();
    }

    @Override
    protected void execute() {
        Lift.getInstance().setRearPower(OI.getInstance().getDriverStickY());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Lift.getInstance().stop();

        super.logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
