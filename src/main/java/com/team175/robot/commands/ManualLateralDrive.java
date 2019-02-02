package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.LateralDrive;

/**
 * @author Arvind
 */
public class ManualLateralDrive extends AldrinCommand {

    public ManualLateralDrive() {
        requires(LateralDrive.getInstance());
        requires(Drive.getInstance());

        super.instantiationLog();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setPower(0, 0);
        LateralDrive.getInstance().deploy(true);

        super.initLog();
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
        LateralDrive.getInstance().setPower(0);
        LateralDrive.getInstance().deploy(false);

        super.endLog();
    }

    @Override
    protected void interrupted() {
        end();
    }

}
