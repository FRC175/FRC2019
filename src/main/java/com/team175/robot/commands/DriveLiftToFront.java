package com.team175.robot.commands;

import com.team175.robot.subsystems.Lift;

public class DriveLiftToFront extends AldrinCommand {

    public DriveLiftToFront() {
        requires(Lift.getInstance());

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        super.logInit();
    }

    @Override
    protected void execute() {
        Lift.getInstance().setDrivePower(0.75);
    }

    @Override
    protected boolean isFinished() {
        return Lift.getInstance().isFrontOnHab();
    }

    @Override
    protected void end() {
        super.logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
