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
        super.initialize();
    }

    @Override
    protected void execute() {
        // mLogger.debug("LateralPosition: {}", LateralDrive.getInstance().getPosition());
        // LateralDrive.getInstance().setPower(OI.getInstance().getDriverStickX());
        LateralDrive.getInstance().setPower(OI.getInstance().getDriverStickX() * 0.5);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        LateralDrive.getInstance().stop();
        super.end();
    }

}
