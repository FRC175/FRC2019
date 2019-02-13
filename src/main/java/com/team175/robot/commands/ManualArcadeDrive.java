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
            /*double y = AldrinMath.addDeadZone(OI.getInstance().getDriverStickY(), 0.05);
            double x = AldrinMath.addDeadZone(OI.getInstance().getDriverStickX(), 0.05);

            mLogger.debug("Y: {}", y);
            mLogger.debug("X: {}", x);

            double forward = OI.getInstance().getDriverStickY();
            double turn = OI.getInstance().getDriverStickX();
            forward = Deadband(forward);
            turn = Deadband(turn);*/

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

    /*double Deadband(double value) {
        if (value >= +0.05)
            return value;

        if (value <= -0.05)
            return value;

        return 0;
    }*/

}