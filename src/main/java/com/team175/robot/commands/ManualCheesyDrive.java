package com.team175.robot.commands;

import com.team175.robot.Constants;
import com.team175.robot.OI;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.LateralDrive;

/**
 * @author Arvind
 */
public class ManualCheesyDrive extends AldrinCommand {

    private boolean mIsLowGear;

    public ManualCheesyDrive(boolean isLowGear) {
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
            double y = OI.getInstance().getDriverStickY();
            double x = OI.getInstance().getDriverStickX();
            boolean isQuickTurn = (y > Constants.QUICK_TURN_THRESHOLD && y < -Constants.QUICK_TURN_THRESHOLD);
            Drive.getInstance().cheesyDrive(y, x, isQuickTurn);
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
