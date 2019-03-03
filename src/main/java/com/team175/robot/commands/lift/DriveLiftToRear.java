package com.team175.robot.commands.lift;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;

public class DriveLiftToRear extends AldrinCommand {

    public DriveLiftToRear() {
        requires(Lift.getInstance());
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        // TODO: Sync speeds
        Lift.getInstance().setDrivePower(0.75);
        Drive.getInstance().setPower(0.25);
        super.initialize();
    }

    @Override
    protected boolean isFinished() {
        return Lift.getInstance().isRearOnHab();
    }

    @Override
    protected void end() {
        Lift.getInstance().stop();
        super.end();
    }

}
