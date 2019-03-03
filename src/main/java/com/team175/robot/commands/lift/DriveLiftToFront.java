package com.team175.robot.commands.lift;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Lift;

public class DriveLiftToFront extends AldrinCommand {

    public DriveLiftToFront() {
        requires(Lift.getInstance());
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Lift.getInstance().setDrivePower(0.75);
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return Lift.getInstance().isFrontOnHab();
    }

    @Override
    protected void end() {
        Lift.getInstance().stop();
        super.end();
    }

}
