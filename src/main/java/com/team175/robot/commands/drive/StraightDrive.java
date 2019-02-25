package com.team175.robot.commands.drive;

import com.team175.robot.OI;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.LateralDrive;

/**
 * @author Arvind
 */
public class StraightDrive extends AldrinCommand {

    public StraightDrive() {
        requires(Drive.getInstance(), LateralDrive.getInstance());
        super.logInstantiation();
    }

    @Override
    protected void execute() {
        if (!LateralDrive.getInstance().isDeployed()) {
            Drive.getInstance().straightDrive(OI.getInstance().getDriverStickY());
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Drive.getInstance().stop();
        super.end();
    }

}
