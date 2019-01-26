package com.team175.robot.commands.teleop;

import com.team175.robot.OI;
import com.team175.robot.commands.LoggableCommand;
import com.team175.robot.subsystems.Drive;
import com.team175.robot.util.Mathematics;

/**
 * @author Arvind
 */
public class ManualArcadeDrive extends LoggableCommand {

    public ManualArcadeDrive() {
        requires(Drive.getInstance());
    }

    @Override
    protected void initialize() {
        mLogger.info("ManualArcadeDrive command initialized.");
    }

    @Override
    protected void execute() {
        double x = Mathematics.addDeadzone(OI.getInstance().getDriverStickTwist());
        double y = Mathematics.addDeadzone(OI.getInstance().getDriverStickY());
        Drive.getInstance().arcadeDrive(x, y);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        mLogger.info("ManualArcadeDrive command ended/interrupted.");
    }

    @Override
    protected void interrupted() {
        end();
    }

}