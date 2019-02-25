/*
package com.team175.robot.commands.old;

import com.team175.robot.OI;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.Lift;

public class ManualFrontLift extends AldrinCommand {

    public ManualFrontLift() {
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
        Lift.getInstance().setFrontPower(OI.getInstance().getDriverStickY());
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
*/
