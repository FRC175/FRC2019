package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;

public class ManualLift extends AldrinCommand {

    public ManualLift() {
        requires(Lift.getInstance());
        requires(Drive.getInstance());

        super.instantiationLog();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setPower(0, 0);

        super.initLog();
    }

    @Override
    protected void execute() {
        Lift.getInstance().setLiftPower(OI.getInstance().getDriverStickY());
        Lift.getInstance().setDrivePower(OI.getInstance().getDriverStickX());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Lift.getInstance().setLiftPower(0);
        Lift.getInstance().setDrivePower(0);

        super.endLog();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
