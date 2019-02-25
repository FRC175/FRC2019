/*package com.team175.robot.commands.old;

import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;

public class DriveLiftToRear extends AldrinCommand {

    public DriveLiftToRear() {
        requires(Lift.getInstance(), Drive.getInstance());

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        super.logInit();
    }

    @Override
    protected void execute() {
        Lift.getInstance().setDrivePower(0.75);
        Drive.getInstance().setPower(0.25); // Make speed the same
    }

    @Override
    protected boolean isFinished() {
        return Lift.getInstance().isRearOnHab();
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

}*/
