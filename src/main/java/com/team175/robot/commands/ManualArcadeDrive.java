package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.LateralDrive;
import com.team175.robot.util.Mathematiques;

/**
 * @author Arvind
 */
public class ManualArcadeDrive extends LoggableCommand {

    private boolean mIsHighGear;

    public ManualArcadeDrive(boolean isHighGear) {
        requires(Drive.getInstance());
        requires(LateralDrive.getInstance());

        mIsHighGear = isHighGear;
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setHighGear(mIsHighGear);
        mLogger.info("ManualArcadeDrive command initialized.");
    }

    @Override
    protected void execute() {
        if (!LateralDrive.getInstance().isDeployed()) {
            double x = Mathematiques.addDeadzone(OI.getInstance().getDriverStickTwist(), 0.05);
            double y = Mathematiques.addDeadzone(OI.getInstance().getDriverStickY(), 0.05);
            Drive.getInstance().arcadeDrive(x, y);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Drive.getInstance().setPower(0, 0);

        mLogger.info("ManualArcadeDrive command ended/interrupted.");
    }

    @Override
    protected void interrupted() {
        end();
    }

}