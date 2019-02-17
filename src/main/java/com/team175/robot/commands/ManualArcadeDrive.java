package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.LateralDrive;

/**
 * @author Arvind
 */
public class ManualArcadeDrive extends AldrinCommand {

    private boolean mIsLowGear;

    public ManualArcadeDrive(boolean isLowGear) {
        requires(Drive.getInstance(), LateralDrive.getInstance());

        mIsLowGear = isLowGear;

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setLowGear(mIsLowGear);

        super.logInit();
    }

    @Override
    protected void execute() {
        if (!LateralDrive.getInstance().isDeployed()) {
            Drive.getInstance().arcadeDrive(OI.getInstance().getDriverStickY(), OI.getInstance().getDriverStickX());
            // Drive.getInstance().altArcadeDrive(OI.getInstance().getDriverStickY(), OI.getInstance().getDriverStickX());
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Drive.getInstance().stop();

        super.logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

}