package com.team175.robot.commands.lateraldrive;

import com.team175.robot.OI;
import com.team175.robot.commands.AldrinCommand;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.LateralDrive;
import com.team175.robot.subsystems.Lift;

/**
 * @author Arvind
 */
public class ControlLateralDrive extends AldrinCommand {

    public ControlLateralDrive() {
        requires(Drive.getInstance(), LateralDrive.getInstance());
        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().stop();
        LateralDrive.getInstance().deploy(true);
        mLogger.debug("Starting lateral position: {}", LateralDrive.getInstance().getPosition());
        super.initialize();
    }

    @Override
    protected void execute() {
        LateralDrive.getInstance().setPower(OI.getInstance().getDriverStickX() * 0.75);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        mLogger.debug("Final lateral position: {}", LateralDrive.getInstance().getPosition());
        LateralDrive.getInstance().stop();
        super.end();
    }

}
