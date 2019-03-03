package com.team175.robot.commands.lift;

import com.team175.robot.OI;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;

public class ControlLift extends AldrinCommand {

    public ControlLift() {
        requires(Lift.getInstance(), Drive.getInstance());
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().stop();
        super.initialize();
    }

    @Override
    protected void execute() {
        Lift.getInstance().setFrontPower(OI.getInstance().getDriverStickY());
        Lift.getInstance().setRearPower(OI.getInstance().getDriverStickY());
        // Lift.getInstance().setDrivePower(OI.getInstance().getDriverStickX());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Lift.getInstance().stop();
        super.end();
    }

}
