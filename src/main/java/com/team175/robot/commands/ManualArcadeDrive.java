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
        requires(Drive.getInstance());
        requires(LateralDrive.getInstance());

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
            /*double y = AldrinMath.addDeadzone(-OI.getInstance().getDriverStickY(), 0.05);
            double x = AldrinMath.addDeadzone(OI.getInstance().getDriverStickTwist(), 0.05);*/

            double forward = -1 * OI.getInstance().getDriverStickY();
            double turn = OI.getInstance().getDriverStickX();
            forward = Deadband(forward);
            turn = Deadband(turn);

            mLogger.info("Forward: {}", forward);
            mLogger.info("Turn: {}", turn);

            Drive.getInstance().arcadeDrive(forward, turn);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Drive.getInstance().setPower(0, 0);

        super.logEnd();
    }

    @Override
    protected void interrupted() {
        end();
    }

    /** Deadband 5 percent, used on the gamepad */
    double Deadband(double value) {
        /* Upper deadband */
        if (value >= +0.05)
            return value;

        /* Lower deadband */
        if (value <= -0.05)
            return value;

        /* Outside deadband */
        return 0;
    }

}