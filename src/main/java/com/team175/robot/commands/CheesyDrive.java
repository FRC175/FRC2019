package com.team175.robot.commands;

import com.team175.robot.Constants;
import com.team175.robot.OI;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.LateralDrive;

/**
 * @author Arvind
 */
public class CheesyDrive extends AldrinCommand {

    private boolean mIsHighGear;

    public CheesyDrive(boolean isHighGear) {
        requires(Drive.getInstance(), LateralDrive.getInstance());

        mIsHighGear = isHighGear;

        super.logInstantiation();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setHighGear(mIsHighGear);

        super.logInit();
    }

    @Override
    protected void execute() {
        if (!LateralDrive.getInstance().isDeployed()) {
            double y = OI.getInstance().getDriverStickY();
            double x = OI.getInstance().getDriverStickX();
            boolean isQuickTurn = (y > Constants.QUICK_TURN_THRESHOLD && y < -Constants.QUICK_TURN_THRESHOLD);

            mLogger.debug("LeftPosition: {}", Drive.getInstance().getLeftPosition());
            mLogger.debug("RightPosition: {}", Drive.getInstance().getRightPosition());
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
