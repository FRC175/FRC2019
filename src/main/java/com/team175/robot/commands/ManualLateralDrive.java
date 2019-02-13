package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.LateralDrive;

/**
 * @author Arvind
 */
public class ManualLateralDrive extends AldrinCommand {

    public ManualLateralDrive() {
        requires(LateralDrive.getInstance(), Drive.getInstance());

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().stop();
        LateralDrive.getInstance().deploy(true);

        super.logInit();
    }

    @Override
    protected void execute() {
        LateralDrive.getInstance().setPower(OI.getInstance().getDriverStickX());
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        LateralDrive.getInstance().stop();
        LateralDrive.getInstance().deploy(false);

        super.logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
