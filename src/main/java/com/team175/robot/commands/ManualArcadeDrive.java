package com.team175.robot.commands;

import com.team175.robot.OI;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.subsystems.LateralDrive;
import com.team175.robot.subsystems.Lift;
import com.team175.robot.util.AldrinMath;

/**
 * @author Arvind
 */
public class ManualArcadeDrive extends AldrinCommand {

    private boolean mIsHighGear;

    public ManualArcadeDrive(boolean isHighGear) {
        requires(Drive.getInstance());
        requires(LateralDrive.getInstance());

        mIsHighGear = isHighGear;

        super.instantiationLog();
    }

    @Override
    protected void initialize() {
        Drive.getInstance().setHighGear(mIsHighGear);

        super.initLog();
    }

    @Override
    protected void execute() {
        if (!LateralDrive.getInstance().isDeployed()) {
            double y = AldrinMath.addDeadzone(-OI.getInstance().getDriverStickY(), 0.05);
            double x = AldrinMath.addDeadzone(OI.getInstance().getDriverStickTwist(), 0.05);
            Drive.getInstance().arcadeDrive(y, x);
        }
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Drive.getInstance().setPower(0, 0);

        super.endLog();
    }

    @Override
    protected void interrupted() {
        end();
    }

}