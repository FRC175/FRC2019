package com.team175.robot.commands.lift;

import com.team175.robot.OI;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.positions.LiftPosition;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;

public class ControlRearLift extends AldrinCommand {

    public ControlRearLift() {
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
        Lift.getInstance().setRearPower(OI.getInstance().getDriverStickY());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Lift.getInstance().setRearPower(0);
        Lift.getInstance().setRearBrake(true);
        super.end();
    }

}
