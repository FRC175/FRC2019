package com.team175.robot.commands.lift;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Lift;

public class ControlLiftDrive extends AldrinCommand {

    public ControlLiftDrive() {
        requires(Lift.getInstance());
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        super.initialize();
        Lift.getInstance().setDrivePower(1);
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
