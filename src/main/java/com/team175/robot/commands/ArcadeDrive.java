package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.LateralDrive;

/**
 * @author Arvind
 */
public class ArcadeDrive extends AldrinCommand {

    private boolean mIsHighGear;

    public ArcadeDrive(boolean isHighGear) {
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
            /*mLogger.debug("LeftPosition: {}", Drive.getInstance().getLeftPosition());
            mLogger.debug("RightPosition: {}", Drive.getInstance().getRightPosition());*/
            Drive.getInstance().arcadeDrive(OI.getInstance().getDriverStickY(), OI.getInstance().getDriverStickX());
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