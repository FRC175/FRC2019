package com.team175.robot.commands.lift;

import com.team175.robot.OI;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;

public class ControlLiftDrive extends AldrinCommand {

    public ControlLiftDrive() {
        requires(Lift.getInstance());
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        super.initialize();
    }

    @Override
    protected void execute() {
        Lift.getInstance().setDrivePower(OI.getInstance().getDriverStickX());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        // Lift.getInstance().stop();
        Lift.getInstance().setDrivePower(0);
        super.end();
    }

}
