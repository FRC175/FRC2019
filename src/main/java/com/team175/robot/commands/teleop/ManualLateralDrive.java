package com.team175.robot.commands.teleop;

import com.team175.robot.OI;
import com.team175.robot.commands.LoggableCommand;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.LateralDrive;

public class ManualLateralDrive extends LoggableCommand {

    public ManualLateralDrive() {
        requires(LateralDrive.getInstance());
        requires(Drive.getInstance());
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setPower(0, 0);
        LateralDrive.getInstance().deploy(true);

        mLogger.info("ManualLateralDrive command initialized.");
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

        mLogger.info("ManualArcadeDrive command ended/initialized.");
    }

    @Override
    protected void interrupted() {
        end();
    }

}
